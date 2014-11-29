/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.system;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import spacetrader.Mercenary;
import spacetrader.Player;
import spacetrader.PoliceRecord;
import spacetrader.SkillList.Skill;
import static spacetrader.Tools.rand;
import spacetrader.Universe;
import static spacetrader.Universe.HEIGHT;
import static spacetrader.Universe.WIDTH;
import spacetrader.commerce.PriceIncreaseEvent;
import spacetrader.commerce.TradeGood;
import spacetrader.commerce.Wallet;
import spacetrader.exceptions.InsufficientFundsException;
import spacetrader.persistence.OverwriteScreenController;
import spacetrader.persistence.ReloadGameScreenController;
import spacetrader.planets.Planet;
import spacetrader.ships.PlayerShip;
import spacetrader.ships.ShipType;
import spacetrader.ships.Weapon;
import spacetrader.ships.WeaponType;
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
        popUpControl = new InformationPresenter(stage);
        
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
        Stage cheatStage = new Stage();
        popUpControl.displayAdminCheats(cheats, cheatStage, (e) -> {
            cheatStage.close();
            setUpGameWithCheats(cheats);
        });
    }

    /**
     * Starts the game using user-defined values
     * @param cheats the holder of the user-defined values
     */
    private void setUpGameWithCheats(AdminCheats cheats) {
        debugStatus = cheats.getDebugMode();
        Planet homePlanet = new Planet("Pallet", new Point2D(WIDTH / 2, HEIGHT / 2),
                cheats.getTechLevel(), cheats.getResource(), cheats.getPoliticalSystem());
        game.setUniverse(new Universe(homePlanet));

        Player cheatPlayer = new Player("LubMaster", 3, 3, 3, 3, 3);
        cheatPlayer.setCredits(cheats.getInitialCredits());
        cheatPlayer.setShip(new PlayerShip(cheats.getStartingShip()));
        cheatPlayer.getShip().getWeapons().addItem(new Weapon(WeaponType.PULSE));
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
        int engineerRepairs = rand.nextInt(
                Math.max(1, game.getPlayer().getEffectiveSkill(Skill.ENGINEER)));
        int currentHull = game.getPlayer().getShip().getHullStrength();
        game.getPlayer().getShip().setHullStrength(currentHull
                + engineerRepairs);

        changePlayerLocation(destination);
        if (destination.getPriceIncEvent() != PriceIncreaseEvent.NONE) {
            displayInfoMessage(null, "Notice!", destination.getName()
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
            displayInfoMessage(null, "Special Event!", event.getMessage());
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
                            + "\n\nUntil you stop your insurance at the bank, or aquire more money,"
                            + " you will not be allowed to depart.";
                    break;
                case "mercenaries":
                    msgTitle = "Unable to Pay Crew Salaries";
                    message = "";
                    break;
                case "debt":
                    msgTitle = "Too Much Debt";
                    message = Wallet.MAX_DEBT_MSG;
                    break;
                default: //should never happen
                    msgTitle = "Unknown Fee Error";
                    message = "You have a mysterious fee which can not be paid. Sorry.";
                    break;
            }
            //Go back to the Planet Home Screen
            displayInfoMessage(null, msgTitle, message);
            goToHomeScreen(player, player.getLocation());
        }
        if (game.getPlayer().getDebt() > Wallet.DEBT_WARNING) {
            displayWarningMessage(null, "Debt Warning!", "You get this warning because "
                    + "your debt has exceeded 75000 credits. If you don't pay "
                    + "back quickly, you may find yourself stuck in a system with"
                    + " no way to leave. You have been warned.");
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
     * Happens when the player gets arrested by the police. Fines and imprisons the player.
     */
    public void playerGetsArrested() {
        Player player = game.getPlayer();
	int negPoliceScore = -player.getPoliceRecordScore();
        
	int fine = ((1 + (((player.getCurrentWorth() * Math.min(80, negPoliceScore)) / 100) / 500)) * 500);
	int daysImprisoned = Math.max( 30, negPoliceScore );

	displayInfoMessage(null, "You've been arrested!", "At least you survived." );
        displayInfoMessage(null, "Conviction", "Your fine and number of days in prison are based on"
                + " the kind of criminal you were found to be.");

	String conviction = String.format("You are convicted to %d days in prison and a fine of ₪%d.", daysImprisoned, fine);
        displayInfoMessage(null, "Conviction Determined", conviction);

        //Remove Illegal Goods
	if (player.getShip().isCarryingIllegalGoods()) {
            displayInfoMessage(null, "Illegal Goods Impounded", "What would you expect?" );
            player.getCargo().clearItem(TradeGood.NARCOTICS);
            player.getCargo().clearItem(TradeGood.FIREARMS);
	}
        //Remove Insurance
	if (player.getInsuranceCost() <= 0) {
            displayInfoMessage(null, "", "InsuranceLostAlert" );
            player.setInsuranceCost(0);
            //NoClaim = 0; reset no-claim to zero
	}
        //Remove Crew
        Mercenary[] crew = player.getShip().getCrew();
	if (crew.length > 0) {
            displayInfoMessage(null, "Mercenaries Leave", "You can't pay your mercenaries "
                    + "while you are imprisoned, and so they have sought new employment." );
            for (int i = 0; i < crew.length; i++) {
                player.getShip().fireMercenary(crew[i]);
            }
	}

	//Arrival();
        //TODO Handle arriving at planet. set prices, change quantities, shuffle status
        
        //Increment Days
        for (int i = 0; i < daysImprisoned; i++) {
            game.increaseDays();
        }

        //Pay the fine
	if (player.getCredits() >= fine)
		player.removeCredits(fine);
        else {
            //Sell the player's ship
            player.addCredits(player.getShip().currentShipPrice());

            if (player.getCredits() >= fine) {
                player.removeCredits(fine);
            } else {
                player.setCredits(0);
            }
            displayInfoMessage(null, "Ship Sold", "The Space Corps needs cash to make"
                    + " you pay for the damages you did. Your ship is the only "
                    + "valuable possession you have.");
            displayInfoMessage(null, "Flea Received", "It's standard practice for the"
                    + " police to leave a condemned criminal with at least the "
                    + "means to leave the solar system." );

            //CreateFlea();
            player.setShip(new PlayerShip(ShipType.FLEA));
	}
	
        //Update their Police Record
	player.setPoliceRecordScore(PoliceRecord.DUBIOUS.minScore());

        //Pay off as much as the player's debt as possible.
        int debt = player.getDebt();
	if (player.getDebt() > 0) {
            if (player.getCredits() >= debt) {
                player.removeCredits(debt);
                player.removeDebt(debt);
            } else {
                player.removeDebt(player.getCredits());
                player.setCredits(0);
            }
	}
	
        //Pay Interest on all the days imprisoned.
        try {
            for (int i = 0; i < daysImprisoned; ++i) {
                player.payInterest();
            }
        } catch (InsufficientFundsException e) {
            displayInfoMessage(null, "Too Much Debt", Wallet.MAX_DEBT_MSG);
        }

        //go to the new planet
        changePlayerLocation(warpScreenControl.getDestination());
    }
    
    /**
     * An encounter calls this when pirates raided the player's ship.
     */
    public void setPlayerWasRaided() {
        warpScreenControl.setRaided();
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
     * arguments are ignored. The number of arguments is variable and may be zero.
     */
    public void displayInfoMessage(String msgTitle, String header, String message, Object ... args) {
        popUpControl.displayInfoMessage(msgTitle, header, message, args);
    }
    
     /**
     * Display alert message based on passed in string.
     *
     * @param msgTitle the value of msgTitle
     * @param header title of state
     * @param message alert message
     * @param args Arguments referenced by the format specifiers in the message
     * string. If there are more arguments than format specifiers, the extra 
     * arguments are ignored. The number of arguments is variable and may be zero.
     */
    public void displayWarningMessage(String msgTitle, String header, String message, Object ... args) {
        popUpControl.displayWarningMessage(msgTitle, header, message, args);
    }
     /**
     * Display alert message based on passed in string.
     *
     * @param msgTitle the value of msgTitle
     * @param header title of state
     * @param message alert message
     * @param args Arguments referenced by the format specifiers in the message
     * string. If there are more arguments than format specifiers, the extra 
     * arguments are ignored. The number of arguments is variable and may be zero.
     */
    public void displayErrorMessage(String msgTitle, String header, String message, Object ... args) {
        popUpControl.displayErrorMessage(msgTitle, header, message, args);
    }

    /**
     * Display yes-no confirmation.
     *
     * @param optionsTitle title of state
     * @param mastHead the header
     * @param message message to display
     * @param args Arguments referenced by the format specifiers in the message
     * string. If there are more arguments than format specifiers, the extra 
     * arguments are ignored. The number of arguments is variable and may be zero.
     * @return true if the player confirmed yes, otherwise false
     */
    public boolean displayYesNoConfirmation(String optionsTitle, String mastHead, String message, Object ... args) {
        message = String.format(message, args);
        Optional<ButtonType> result = popUpControl.displayOptionsDialog(
                optionsTitle, mastHead, message, ButtonType.NO, ButtonType.YES);
        return result.isPresent() && result.get() == ButtonType.YES;
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
    public String displayCustomConfirmation(String optionsTitle, String mastHead, String message, String ... buttonNames) {
        Optional<ButtonType> result = popUpControl.displayOptionsDialog(
                optionsTitle, mastHead, message, buttonNames);
        if (!result.isPresent()) {
            return "";
        }
        for (String name : buttonNames) {
            if (name.equals(result.get().getText())) {
                return name;
            }
        }
        return "";
        
    }

    /**
     * Display progress bar.
     *
     * @param progressTitle title of state
     * @param service
     */
    public void displayProgess(String progressTitle, Service service) {
        popUpControl.displayProgess(progressTitle, service);
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
        
        popUpControl.displaySaveProgress(progressTitle, updateMessage, finishMessage,
            (WorkerStateEvent e) -> {
                goToHomeScreen(game.getPlayer(),
                game.getPlayer().getLocation());
            });
    }
}
