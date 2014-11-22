/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.system;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.dialog.Dialogs;
import org.controlsfx.property.BeanProperty;
import spacetrader.Player;
import spacetrader.PoliceRecord;
import spacetrader.Reputation;
import spacetrader.SkillList.Skill;
import static spacetrader.Tools.rand;
import spacetrader.Universe;
import static spacetrader.Universe.HEIGHT;
import static spacetrader.Universe.WIDTH;
import spacetrader.commerce.PriceIncreaseEvent;
import spacetrader.exceptions.InsufficientFundsException;
import spacetrader.persistence.OverwriteScreenController;
import spacetrader.persistence.ReloadGameScreenController;
import spacetrader.planets.Planet;
import spacetrader.planets.PoliticalSystem;
import spacetrader.planets.Resource;
import spacetrader.planets.TechLevel;
import spacetrader.ships.PlayerShip;
import spacetrader.ships.ShipType;
import spacetrader.travel.Encounter;
import spacetrader.travel.EncounterScreenController;
import spacetrader.travel.RandomEvent;
import spacetrader.travel.RandomEventGenerator;
import spacetrader.travel.WarpScreenController;

/**
 *
 * @author Caleb Stokols
 */
public class MainController {

    private final SpaceTrader game;
    private final Stage stage;
    private RandomEventGenerator eventGenerator;

    private WarpScreenController warpScreenControl;

    public enum Debug {
        OFF, TRADER_ENCOUNTER, POLICE_ENCOUNTER, PIRATE_ENCOUNTER, METEORS
    }

    public static Debug debugStatus = Debug.OFF;

    /**
     * Creates the MainController. MainController has a reference to the main
     * class and stage.
     *
     * @param game the main SpaceTrader class
     * @param stage the game's main window frame
     */
    public MainController(SpaceTrader game, Stage stage) {
        this.game = game;
        this.stage = stage;

        //stage.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
        //    if (game.getUniverse() != null) {
        //        if (event.getCode() == KeyCode.ESCAPE) {
        //            goToStartScreen();
        //        }
        //    }
        //});
    }

    public void displayAdminCheats() {
        AdminCheats cheats = new AdminCheats();

        ObservableList<PropertySheet.Item> list = FXCollections.observableArrayList();
        try {
            for (String var : new String[]{"initialCredits", "techLevel", "politicalSystem", "resource", "startingShip", "policeRecord", "reputation", "debugMode"}) {
                list.add(new BeanProperty(cheats, new PropertyDescriptor(var, cheats.getClass())));
            }
        } catch (IntrospectionException ex) {
            Logger.getLogger(WelcomeScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Create Stage
        Stage cheatStage = new Stage();
        cheatStage.initOwner(stage);
        cheatStage.initModality(Modality.WINDOW_MODAL);
        
        //Create Pane
        VBox pane = new VBox();
        pane.setAlignment(Pos.BOTTOM_RIGHT);
        
        //Create Property Sheet
        PropertySheet propertySheet = new PropertySheet(list);
        propertySheet.setModeSwitcherVisible(false);
        propertySheet.setSearchBoxVisible(false);
        
        //Create Done Button
        Button doneButton = new Button("Done");
        doneButton.setOnAction((e) -> {
            cheatStage.close();
            setUpGameWithCheats(cheats);
        });
        doneButton.setDefaultButton(true);
        
        //add nodes to pane, and set the pane to the stage's scene.
        pane.getChildren().addAll(propertySheet, doneButton);
        Scene scene = new Scene(pane);
        cheatStage.setScene(scene);
        cheatStage.show();
    }

    /**
     * Starts the game using user-defined values
     * @param cheats the holder of the user-defined values
     */
    private void setUpGameWithCheats(AdminCheats cheats) {
        debugStatus = cheats.debugMode;
        Planet homePlanet = new Planet("Pallet", new Point2D(WIDTH / 2, HEIGHT / 2),
                cheats.getTechLevel(), cheats.getResource(), cheats.getPoliticalSystem());
        game.setUniverse(new Universe(homePlanet));

        Player cheatPlayer = new Player("LubMaster", 3, 3, 3, 3, 3);
        cheatPlayer.setCredits(cheats.getInitialCredits());
        cheatPlayer.setShip(new PlayerShip(cheats.getStartingShip()));
        cheatPlayer.setReputationScore(cheats.getReputation().minRep());
        cheatPlayer.setPoliceRecordScore(cheats.getPoliceRecord().minScore());
        game.setPlayer(cheatPlayer);

        changePlayerLocation(homePlanet);
    }

    /**
     * Sets the game's universe to a new universe and the game's player to a
     * specified Player.
     *
     * @param player the player of the game
     */
    public void setUpGame(Player player) {
        game.setUniverse(new Universe());
        game.setPlayer(player);
        changePlayerLocation(game.getUniverse().getPlanet("Pallet"));
    }

    /**
     * Takes care of everything that happens when a player arrives at a planet.
     * Moves a player to a selected planet. Adjust Price Increase Events on all
 planets. All the planets who's tradeGood stock has been decremented
 should have their stock increased. The player fixes his startingShip's hull
 strength. Random events can occur at this point.
     *
     * @param source the planet the player left
     * @param destination the planet the player is arriving at
     */
    public void arriveAtPlanet(Planet source, Planet destination) {
        game.increaseDays();

        //processes time aspect of price increase events
        for (Planet planet : game.getUniverse().getPlanets()) {
            if (planet.getPriceIncDuration() > 0) {
                planet.setPriceIncDuration(planet.getPriceIncDuration() - 1);
            } else {
                planet.setRandomPriceIncEvent();
            }
        }

        //The player autofixes their startingShip depending on their engineer skill
        int engineerRepairs = rand.nextInt(Math.max(1,
                game.getPlayer().getEffectiveSkill(Skill.ENGINEER)));
        int currentHull = game.getPlayer().getShip()
                .getHullStrength();
        game.getPlayer().getShip().setHullStrength(currentHull
                + engineerRepairs);

        changePlayerLocation(destination);
        if (destination.getPriceIncEvent() != PriceIncreaseEvent.NONE) {
            displayAlertMessage("Notice!", destination.getName()
                    + " is currently "
                    + destination.getPriceIncEvent().desc().toLowerCase());
        }
        if (eventGenerator == null) {
            eventGenerator = new RandomEventGenerator(game.getPlayer(),
                    game.getUniverse(), this);
        }

        if (eventGenerator.eventOccurs()) {
            RandomEvent event = eventGenerator.getRandomEvent();
            event.doEvent();
            displayAlertMessage("Special Event!", event.getMessage());
        }
    }

    /**
     * The destination planet's prices should be recalculated. Determine if the
 player can pay their mercenary, interest, and insurance costs. If not,
 kick them back to the home screen. Adjust the player's police record and
 reputation. Deduct fuel from the player's startingShip. Finally, go to the warp
 screen.
     *
     * @param destination which planet the player is traveling to
     * @param source which planet the player is leaving
     */
    public void departFromPlanet(Planet source, Planet destination) {
        Player player = game.getPlayer();

        try {
            player.payDailyFees();
        } catch (InsufficientFundsException e) {
            String msgTitle, message;
            switch (e.getMessage()) {
                case "insurance":
                    msgTitle = "Unable to Pay Insurance";
                    message = "You do not have enough money to pay for your insurance!"
                            + "\n\nUntil you sell your insurance, or aquire more money,"
                            + " you will not be allowed to depart.";
                    break;
                case "mercenaries":
                    msgTitle = "Unable to Pay Crew Salaries";
                    message = "";
                    break;
                case "debt":
                    msgTitle = "Too Much Debt";
                    message = "";
                    break;
                default: //should never happen
                    msgTitle = "Unknown Fee Error";
                    message = "You have a mysterious fee which can not be paid. Sorry.";
                    break;
            }

            displayAlertMessage(msgTitle, message);
            goToHomeScreen(player, player.getLocation());
        }

        destination.getMarket().setAllPrices(game.getPlayer());

        //Decrease police record score if very high
        if (game.getDays() % 3 == 0 && player.getPoliceRecord()
                .compareTo(PoliceRecord.CLEAN) > 0) {
            int newRecord = player.getPoliceRecordScore() - 1;
            player.setPoliceRecordScore(newRecord);
        }

        //Increase police record score if very low
        if (game.getPlayer().getPoliceRecord()
                .compareTo(PoliceRecord.DUBIOUS) < 0) {
            int newRecord = player.getPoliceRecordScore() + 1;
            player.setPoliceRecordScore(newRecord);
        }

        //Deduct fuel
        int distance = (int) Universe
                .distanceBetweenPlanets(source, destination);
        player.getShip().removeFuel(distance);

        //Start doing encounters!
        goToWarpScreen(source, destination);
    }

    /**
     * Takes care of the actual act of going to a planet. Things that happens
     * every time you go to a planet from a different planet should be put here.
     *
     * @param destination
     */
    public void changePlayerLocation(Planet destination) {
        game.getPlayer().setLocation(destination);
        destination.setVisited();
        destination.getMarket().setAllPrices(game.getPlayer());
        goToHomeScreen(game.getPlayer(), destination);
    }

    /**
     * Creates a Scene from an fxml file, and then returns the Scene's
     * SceneController.
     *
     * @param fxmlScene the name of the fxml file that holds the new scene
     * @param myStage the value of myStage
     * @return the corresponding Controller of the fxml file
     */
    private SceneController extractControllerFromFXML(String fxmlScene, Stage myStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlScene));
        try {
            Scene theScene = new Scene(loader.load());
            SceneController controller = loader.getController();
            controller.setScene(theScene);
            controller.setMainControl(this);
            return controller;
        } catch (IOException e) {
            Logger.getLogger(SpaceTrader.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return null;
    }

    /**
     * Gives the mainController access to this MainController. Sets the Stage's
     * scene to this Controller's scene. Shows the scene.
     *
     * @param control a SceneController
     */
    public void setUpControllerAndStage(SceneController control) {

    }

    /**
     * Transitions the game screen to the Welcome Screen.
     */
    public void goToWelcomeScreen() {
        WelcomeScreenController control;
        control = (WelcomeScreenController) extractControllerFromFXML(
                "/spacetrader/WelcomeScreen.fxml", stage);
        stage.setScene(control.getScene());
        stage.setTitle("Space Traders!");
        stage.show();
    }

    /**
     * Transitions the game screen to the Character Dialog.
     */
    public void goToPlayerConfigScreen() {
        CharacterDialogController control;
        control = (CharacterDialogController) extractControllerFromFXML(
                "/spacetrader/CharacterDialog.fxml", stage);
        stage.setScene(control.getScene());
    }

    /**
     * Transitions the game screen to the First Screen. Does not do anything
     * extra.
     *
     * @param planet the planet who's home screen we should view
     */
    public void goToHomeScreen(Player player, Planet planet) {
        HomeScreenController control;
        control = (HomeScreenController) extractControllerFromFXML(
                "/spacetrader/planets/HomeScreen.fxml", stage);
        control.setUpHomeScreen(player, planet);
        stage.setTitle("Planet Home");
        stage.setScene(control.getScene());
    }

    /**
     * Transitions the game screen to the First Screen.
     *
     * @param planet the planet who's market we're visiting
     */
    public void goToMarketScreen(Planet planet) {
        MarketScreenController control;
        control = (MarketScreenController) extractControllerFromFXML(
                "/spacetrader/commerce/MarketScreen.fxml", stage);
        control.setUpMarketScreen(planet, game.getPlayer());
        stage.setTitle("Welcome to the Market");
        stage.setScene(control.getScene());
    }

    /**
     * Transitions the game screen to the Ship Yard Screen.
     */
    public void goToShipYardScreen() {
        ShipYardScreenController control;
        control = (ShipYardScreenController) extractControllerFromFXML(
                "/spacetrader/ships/ShipYardScreen.fxml", stage);
        control.setUpShipYardScreen(game.getPlayer());
        stage.setTitle("Welcome to the Ship Yard");
        stage.setScene(control.getScene());
    }

    /**
     * Transitions the game screen to the Ship Market.
     */
    public void goToShipMarket() {
        ShipMarketController control;
        control = (ShipMarketController) extractControllerFromFXML(
                "/spacetrader/ships/ShipMarket.fxml", stage);
        control.setUpShipMarketScreen(game.getPlayer());
        stage.setTitle("Welcome to the Ship Market");
        stage.setScene(control.getScene());
    }

    /**
     * Transitions to equipment screen
     */
    public void goToEquipmentMarket() {
        GadgetScreenController control;
        control = (GadgetScreenController) extractControllerFromFXML(
                "/spacetrader/ships/GadgetScreen.fxml", stage);
        control.setUpEquipmentMarketScreen(game.getPlayer());
        stage.setTitle("Welcome to the Equipment Market");
        stage.setScene(control.getScene());

    }

    /**
     * Transitions the game screen to the Space Map Screen.
     *
     * @param planet the planet who's market we're on
     */
    public void goToSpaceMapScreen(Planet planet) {
        SpaceMapScreenController control;
        control = (SpaceMapScreenController) extractControllerFromFXML(
                "/spacetrader/SpaceMapScreen.fxml", stage);
        control.setUpMap(game.getPlayer(), planet,
                game.getUniverse().getPlanets());
        stage.setTitle("Galactic Planet Map");
        stage.setScene(control.getScene());
    }

    /**
     * Transitions the game screen to the Warp Screen.
     *
     * @param source the planet we are on
     * @param dest the planet we are going to
     */
    public void goToWarpScreen(Planet source, Planet dest) {

        this.warpScreenControl = (WarpScreenController) extractControllerFromFXML("/spacetrader/travel/WarpScreen.fxml", stage);
        stage.setTitle("Traveling to " + dest.getName());
        stage.setScene(this.warpScreenControl.getScene());
        this.warpScreenControl.setUpWarping(source, dest, game.getPlayer());
    }

    /**
     * Goes back to a warp screen that was already in progress
     */
    public void goBackToWarpScreen() {
        if (this.warpScreenControl == null) { //this is only for testing purposes
            goToWarpScreen(game.getUniverse().getPlanet("Pallet"),
                    game.getUniverse().getPlanets().get(10));
        }
        stage.setScene(this.warpScreenControl.getScene());
        this.warpScreenControl.continueTraveling();
    }

    /**
     * Transitions to the encounter screen corresponding with the specified
     * encounter
     *
     * @param encounter the encounter which is currently getting handled
     */
    public void goToEncounterScreen(Encounter encounter) {
        EncounterScreenController control;
        control = (EncounterScreenController) extractControllerFromFXML(encounter.getFXMLScene(), stage);
        control.setEncounter(encounter);
        //stage.setTitle("Space Encounter!");
        stage.setScene(control.getScene());
    }

    /**
     * Transitions the game screen to the Space Map Screen.
     */
    public final void goToStartScreen() {
        Stage startStage = new Stage();
        startStage.setTitle("Start Screen!");
        startStage.initOwner(stage);
        startStage.initModality(Modality.WINDOW_MODAL);

        StartScreenController control;
        control = (StartScreenController) extractControllerFromFXML(
                "/spacetrader/StartScreen.fxml", startStage);
        control.setMainControl(this);
        control.setUpPlayerStats(game.getPlayer(), startStage);
        startStage.setScene(control.getScene());
        startStage.show();
    }

    /**
     * Transitions the game screen to the Overwrite Screen.
     */
    public void goToOverwriteScreen() {
        OverwriteScreenController control;
        control = (OverwriteScreenController) extractControllerFromFXML(
                "/spacetrader/persistence/OverwriteScreen.fxml", stage);
        control.setUpSaveScreen(game);
        stage.setTitle("Save Game!");
        stage.setScene(control.getScene());
    }

    /**
     * Transitions the game screen to the Reload Screen.
     */
    public void goToReloadScreen() {
        ReloadGameScreenController control;
        control = (ReloadGameScreenController) extractControllerFromFXML(
                "/spacetrader/persistence/ReloadGameScreen.fxml", stage);
        control.setUpReloadScreen(game);
        stage.setTitle("Reload Game!");
        stage.setScene(control.getScene());
    }

    /**
     * Transitions the game screen to the Finance Screen.
     *
     * @param planet planet to set up finance screen for
     */
    public void goToFinanceScreen(Planet planet) {
        FinanceScreenController control;
        control = (FinanceScreenController) extractControllerFromFXML("/spacetrader/commerce/FinanceScreen.fxml", stage);
        control.setUpFinanceScreen(game.getPlayer());
        stage.setTitle("Welcome to Bank of " + planet.getName() + "!");
        stage.setScene(control.getScene());
    }

    /**
     * Display alert message based on passed in string.
     *
     * @param header title of state
     * @param message alert message
     */
    public void displayAlertMessage(String header, String message) {
        /*Alert dialog = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
        dialog.setTitle("Information");
        dialog.setResizable(true);
        dialog.setHeaderText(header);
        dialog.initStyle(StageStyle.UTILITY);*/

        Dialogs.create()
                .owner(stage)
                .title(header)
                //.masthead(null)
                .message(message)
                .showInformation();
    }

    /**
     * Display yes-no confirmation.
     *
     * @param optionsTitle title of state
     * @param mastHead
     * @param message message to display
     * @return true if the player confirmed yes, otherwise false
     */
    public boolean displayYesNoConfirmation(String optionsTitle,
            String mastHead, String message) {
        
        Alert dialog = new Alert(AlertType.CONFIRMATION, message, ButtonType.NO, ButtonType.YES);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle(optionsTitle);
        if (mastHead != null) {
            dialog.setHeaderText(mastHead);
        }
        Optional<ButtonType> result = dialog.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;

        /*Action response = Dialogs.create()
                .owner(stage)
                .title(optionsTitle)
                .masthead(mastHead)
                .message(message)
                .actions(Dialog.Actions.YES, Dialog.Actions.NO)
                .showConfirm();
        return response;*/
    }

    /**
     * Display progress bar.
     *
     * @param progressTitle title of state
     * @param service
     */
    public void displayProgess(String progressTitle,
            Service service) {
        Dialogs.create()
                .owner(stage)
                .title(progressTitle)
                .showWorkerProgress(service);

        service.start();
    }

    /**
     * This is for displaying save progress to the player
     *
     * @param progressTitle the popup window title
     * @param updateMessage the message that is displayed over the progress bar
     * @param finishMessage the message displayed when finished
     */
    public void displaySaveProgress(String progressTitle,
            String updateMessage, String finishMessage) {
        Service<Void> saveService = new Service<Void>() {
            @Override
            public Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    public Void call() throws InterruptedException {
                        updateMessage(updateMessage);
                        updateProgress(0, 100);
                        for (int i = 0; i < 100; i++) {
                            Thread.sleep(10);
                            updateProgress(i + 1, 100);
                        }
                        updateMessage(finishMessage);
                        return null;
                    }
                };
            }
        };

        saveService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent e) {
                goToHomeScreen(game.getPlayer(),
                        game.getPlayer().getLocation());
            }
        });
        displayProgess(progressTitle, saveService);
    }

    /**
     * Holds the values for all the testing options.
     * Every instance variable must have a getter and setter.
     */
    public class AdminCheats {

        private int initialCredits;
        private TechLevel techLevel;
        private PoliticalSystem politicalSystem;
        private Resource resource;
        private ShipType startingShip;
        private PoliceRecord policeRecord;
        private Reputation reputation;
        private Debug debugMode;

        private AdminCheats() {
            techLevel = TechLevel.INDUSTRIAL;
            politicalSystem = PoliticalSystem.DEMOCRACY;
            resource = Resource.NONE;
            initialCredits = 5000;
            startingShip = ShipType.GNAT;
            reputation = Reputation.HARMLESS;
            policeRecord = PoliceRecord.CLEAN;
            debugMode = Debug.OFF;
            
        }
        
        public TechLevel getTechLevel() {
            return techLevel;
        }

        public void setTechLevel(TechLevel techLevel) {
            this.techLevel = techLevel;
        }

        public PoliticalSystem getPoliticalSystem() {
            return politicalSystem;
        }

        public void setPoliticalSystem(PoliticalSystem politicalSystem) {
            this.politicalSystem = politicalSystem;
        }

        public int getInitialCredits() {
            return initialCredits;
        }

        public void setInitialCredits(int initialCredits) {
            this.initialCredits = initialCredits;
        }

        public ShipType getStartingShip() {
            return startingShip;
        }

        public void setStartingShip(ShipType startingShip) {
            this.startingShip = startingShip;
        }

        public Resource getResource() {
            return resource;
        }

        public void setResource(Resource resource) {
            this.resource = resource;
        }

        public PoliceRecord getPoliceRecord() {
            return policeRecord;
        }

        public void setPoliceRecord(PoliceRecord policeRecord) {
            this.policeRecord = policeRecord;
        }

        public Reputation getReputation() {
            return reputation;
        }

        public void setReputation(Reputation reputation) {
            this.reputation = reputation;
        }

        public Debug getDebugMode() {
            return debugMode;
        }

        public void setDebugMode(Debug debugMode) {
            this.debugMode = debugMode;
        }
        
    }
}
