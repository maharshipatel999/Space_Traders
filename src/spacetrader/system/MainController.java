/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.system;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import spacetrader.HighScoreList;
import spacetrader.HighScoreSlot;
import spacetrader.Player;
import spacetrader.Universe;
import spacetrader.persistence.OverwriteScreenController;
import spacetrader.persistence.ReloadGameScreenController;
import spacetrader.planets.Planet;
import spacetrader.system.SpaceTrader.Difficulty;
import spacetrader.travel.BattleController;
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

    private InformationPresenter popUpControl;
    private HighScoreList list;


    private WarpScreenController warpScreenControl;

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
    }
    
    /**
     * Gets the game's current difficulty.
     * 
     * @return the game's difficulty
     */
    public Difficulty getDifficulty() {
        return game.getDifficulty();
    }

    /**
     * Display the popup for selecting admin cheats.
     */
    public void displayAdminCheats() {
        AdminCheats cheats = new AdminCheats();
        Stage cheatStage = new Stage();
        InformationPresenter.getInstance().displayAdminCheats(cheats, cheatStage, (e) -> {
            cheatStage.close();
            setUpGameWithCheats(cheats);
        });
    }

    /**
     * Starts the game using user-defined values
     *
     * @param cheats the holder of the user-defined values
     */
    private void setUpGameWithCheats(AdminCheats cheats) {
        this.game.setUpGameWithCheats(cheats);
        this.eventGenerator = new RandomEventGenerator(game.getPlayer(),
                    game.getUniverse(), this);
    }

    /**
     * Sets the game's universe to a new universe and the game's player to a
     * specified Player.
     *
     * @param player the player of the game
     */
    public void setUpGame(Player player) {
        this.game.setUpGame(new Universe(), player);
        this.eventGenerator = new RandomEventGenerator(game.getPlayer(),
                    game.getUniverse(), this);
    }

    /**
     * Takes care of everything that happens when a player arrives at a planet.
     * Moves a player to a selected planet. Adjust Price Increase Events on all
     * planets. All the planets who's tradeGood stock has been decremented
     * should have their stock increased. The player fixes his startingShip's
     * hull strength. Random events can occur at this point.
     *
     * @param destination the planet the player is arriving at
     */
    public void arriveAtPlanet(Planet destination) {
        game.increaseDays();
        game.getUniverse().updatePriceIncreaseEvents(); //update planet statuses
        
        game.getPlayer().getShip().autoRepairHull();//auto repair ship
        destination.movePlayerLocation(game.getPlayer(), this); //update player's location

        //display and execute random event if there is one
        if (eventGenerator.eventOccurs()) {
            RandomEvent event = eventGenerator.getRandomEvent();
            event.doEvent();
            displayInfoMessage(null, "Special Event!", event.getMessage());
            if (game.getPlayer().getShip().getHullStrength() <= 0) {
                game.getPlayer().getShip().setHullStrength(1);
            }
        }
    }

    /**
     * This method is called if the player is arriving at a planet in an unusual
     * way. All the necessities of arriving at the planet will occur.
     * Specifically, price increase events will be updated and possibly
     * displayed, and the player's location will be swapped.
     *
     * @param destination the planet the player is arriving at
     */
    public void specialArrivalAtPlanet(Planet destination) {
        game.getUniverse().updatePriceIncreaseEvents(); //update planet statuses
        destination.movePlayerLocation(game.getPlayer(), this); //update player's location
    }

    public void endGame(boolean playerKilled) {
        int daysLived = game.getDays();
        int playerWorth = game.getPlayer().getCurrentWorth();
        int difficulty = 2; //normal

        int score = getFinalScore(playerKilled, daysLived, playerWorth, difficulty);

        displayInfoMessage("Game Over", "Your ship was destroyed!", "You have died a most bloody death.\n\n"
                + "Your final score is %d! You played for %d turns. The overall "
                + "worth of your trader was ₪%d. Congrats!", score, daysLived, playerWorth);
        
        this.list = new HighScoreList();
        if (list.updateSlots(game, score)) {
            System.out.println("updateSlots returned true");
            goToHighScoreScreen();
        } else {
            goToWelcomeScreen();
        }
    }

    /**
     * Increases the amount of days.
     */
    public void increaseDays() {
        game.increaseDays();
    }
    
    public void goToMeteorScreen(Planet source, Planet destination) {        
        MeteorScreenController control;
        control = (MeteorScreenController) extractControllerFromFXML("/spacetrader/travel/MeteorScreen.fxml", stage);
        control.setMeteorScreen(control.getScene());
        stage.setTitle("Watch out for Meteors in the SolarSpace!");
        control.setMainControl(this);
        control.setPlayer(game.getPlayer(), source, destination);
        control.runGame();

                stage.setScene(control.getScene());


    }
    
    /**
     * Gets the number of turns that the player has completed.
     * 
     * @return the number of days that have passed
     */
    public int getDays() {
        return game.getDays();
    }

    /**
     * An encounter calls this when pirates raided the player's ship.
     */
    public void setPlayerWasRaided() {
        warpScreenControl.setRaided();
    }

    public int getFinalScore(boolean playerKilled, int days, int worth,
            int difficulty) {
        worth = (worth < 1000000 ? worth : 1000000 + ((worth - 1000000) / 10));

        if (playerKilled) {
            return (difficulty + 1) * (int) ((worth * 90) / 50000);
        } else if (!playerKilled) {
            return (difficulty + 1) * (int) ((worth * 95) / 50000);
        } else {
            int d = ((difficulty + 1) * 100) - days;
            if (d < 0) {
                d = 0;
            }
            return (difficulty + 1) * (int) ((worth + (d * 1000)) / 500);
        }
    }

    /**
     * Creates a Scene from an fxml file, and then returns the Scene's
     * SceneController.
     *
     * @param fxmlScene the name of the fxml file that holds the new scene
     * @param myStage the value of myStage
     * @return the corresponding Controller of the fxml file
     */
    private SceneController extractControllerFromFXML(String fxmlScene,
            Stage myStage) {
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
    
    public void goToHighScoreScreen() {
        HighScoreScreenController control;
        control = (HighScoreScreenController) extractControllerFromFXML(
                "/spacetrader/HighScoreScreen.fxml", stage);
        stage.setScene(control.getScene());
        control.setUpHighScoreScreen(list);
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
    public void goToHomeScreen(Planet planet) {
        HomeScreenController control;
        control = (HomeScreenController) extractControllerFromFXML(
                "/spacetrader/planets/HomeScreen.fxml", stage);
        control.setUpHomeScreen(game.getPlayer(), planet);
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
        Stage encounterStage = new Stage();
        encounterStage.initOwner(stage);
        encounterStage.initModality(Modality.WINDOW_MODAL);

        control = (EncounterScreenController) extractControllerFromFXML(encounter.getFXMLScene(), encounterStage);
        control.setEncounter(encounter);
        //stage.setTitle("Space Encounter!");
        encounterStage.setScene(control.getScene());
        encounterStage.show();
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
     * Transitions the game screen to the Finance Screen.
     *
     * @param encounter the encounter that initiated the battle
     */
    public void goToBattleScreen(Encounter encounter) {
        BattleController control;
        control = (BattleController) extractControllerFromFXML("/spacetrader/travel/BattleScreen.fxml", stage);
        control.setUpBattle(encounter);
        stage.setTitle(encounter.getName() + " Encounter!");
        stage.setScene(control.getScene());
    }

    /**
     * Display alert message based on passed in string.
     *
     * @param msgTitle the value of msgTitle
     * @param header title of state
     * @param message alert message
     * @param args Arguments referenced by the format specifiers in the message
     * string. If there are more arguments than format specifiers, the extra
     * arguments are ignored. The number of arguments is variable and may be
     * zero.
     */
    public void displayInfoMessage(String msgTitle, String header,
            String message, Object... args) {
        InformationPresenter.getInstance().displayInfoMessage(msgTitle, header, message, args);
    }

    /**
     * Display alert message based on passed in string.
     *
     * @param msgTitle the value of msgTitle
     * @param header title of state
     * @param message alert message
     * @param args Arguments referenced by the format specifiers in the message
     * string. If there are more arguments than format specifiers, the extra
     * arguments are ignored. The number of arguments is variable and may be
     * zero.
     */
    public void displayWarningMessage(String msgTitle, String header,
            String message, Object... args) {
        InformationPresenter.getInstance().displayWarningMessage(msgTitle, header, message, args);
    }

    /**
     * Display alert message based on passed in string.
     *
     * @param msgTitle the value of msgTitle
     * @param header title of state
     * @param message alert message
     * @param args Arguments referenced by the format specifiers in the message
     * string. If there are more arguments than format specifiers, the extra
     * arguments are ignored. The number of arguments is variable and may be
     * zero.
     */
    public void displayErrorMessage(String msgTitle, String header,
            String message, Object... args) {
        InformationPresenter.getInstance().displayErrorMessage(msgTitle, header, message, args);
    }

    /**
     * Display yes-no confirmation.
     *
     * @param optionsTitle title of state
     * @param mastHead the header
     * @param message message to display
     * @param args Arguments referenced by the format specifiers in the message
     * string. If there are more arguments than format specifiers, the extra
     * arguments are ignored. The number of arguments is variable and may be
     * zero.
     * @return true if the player confirmed yes, otherwise false
     */
    public boolean displayYesNoConfirmation(String optionsTitle, String mastHead,
            String message, Object... args) {
        return InformationPresenter.getInstance()
                .displayYesNoConfirmation(optionsTitle, mastHead, message, args);
    }

    /**
     * Display custom confirmation.
     *
     * @param optionsTitle title of state
     * @param mastHead the header
     * @param message message to display
     * @param buttonNames the names of the buttons the player can choose from
     * @return true if the player confirmed yes, otherwise false
     */
    public String displayCustomConfirmation(String optionsTitle, String mastHead,
            String message, String... buttonNames) {
        return InformationPresenter.getInstance()
                .displayCustomConfirmation(optionsTitle, mastHead, message, buttonNames);
    }

    /**
     * Display progress bar.
     *
     * @param progressTitle title of state
     * @param service
     */
    public void displayProgess(String progressTitle, Service service) {
        InformationPresenter.getInstance().displayProgess(progressTitle, service);
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

        InformationPresenter.getInstance().displaySaveProgress(progressTitle, updateMessage, finishMessage,
                (WorkerStateEvent e) -> {
                    goToHomeScreen(game.getPlayer().getLocation());
                });
    }
}
