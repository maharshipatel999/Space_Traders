/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.system;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;
import spacetrader.Planet;
import spacetrader.Player;
import spacetrader.Universe;
import spacetrader.commerce.PriceIncreaseEvent;
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
    
    SpaceTrader game;
    Stage stage;
    RandomEventGenerator eventGenerator;
    
    /**
     * Creates the MainController.
     * MainController has a reference to the main class and stage. 
     * @param game the main SpaceTrader class
     * @param stage the game's main window frame
     */
    public MainController(SpaceTrader game, Stage stage) {
        this.game = game;
        this.stage = stage;
        
        stage.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
            if (game.getUniverse() != null) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    goToStartScreen();
                }
            }
        });
    }
    
    /**
     * Sets the game's universe to a new universe and the game's player
     * to a specified Player.
     * @param player the player of the game
     */
    public void setUpGame(Player player) {
        game.setUniverse(new Universe());
        game.setPlayer(player);
        player.setLocation(game.getUniverse().getPlanet("Pallet"));
        goToHomeScreen(player.getLocation());
    }
    
    /**
     * Moves a player to a selected planet. The destination planet's prices should
     * be recalculated. Every planet in the universe who's priceIncreaseEvent counter
     * has ran out should have it removed. If the destination planet doesn't have a
     * priceIncreaseEvent currently, one should be randomly added based on a small probability.
     * All the planets who's tradeGood stock has been decremented should have their stock increased.
     * (Not every planet because then planets that are visited late in the game would
     * have a huge stock of tradeGoods.
     * @param destination which planet the player is traveling to
     * @param distance which planet the player is leaving
     */
    public void takeTurn(Planet destination, int distance) { 
        //processes time aspect of price increase events
        for (Planet planet : game.getUniverse().getPlanets()) {
            if (planet.getPriceIncDuration() > 0) {
                planet.setPriceIncDuration(planet.getPriceIncDuration() - 1);
            } else {
                planet.setRandomPriceIncEvent();
            }
        }

        destination.getMarket().setAllPrices();
        game.getPlayer().setLocation(destination);
        game.getPlayer().getShip().getTank().removeFuel(distance);
        goToHomeScreen(destination);
        if (destination.getPriceIncEvent() != PriceIncreaseEvent.NONE) {
            displayAlertMessage("Notice!", destination.getPriceIncEvent().desc());
        }
        if (eventGenerator == null) {
            eventGenerator = new RandomEventGenerator(game.getPlayer(), game.getUniverse(), this);

        }
        
        if (eventGenerator.eventOccurs()) {
            RandomEvent event = eventGenerator.getRandomEvent();
            event.doEvent();
            displayAlertMessage("Special Event!", event.getMessage());
       }
    }
    
    /**
     * Transitions the game screen to the Welcome Screen.
     */
    public void goToWelcomeScreen() {
        stage.setTitle("Space Traders!");
        WelcomeScreenController control;
        control = (WelcomeScreenController) changeScene("/spacetrader/WelcomeScreen.fxml", stage);
        control.setMainControl(this);
    }
    
    /**
     * Transitions the game screen to the Character Dialog.
     */
    public void goToPlayerConfigScreen() {
        CharacterDialogController control;
        control = (CharacterDialogController) changeScene("/spacetrader/CharacterDialog.fxml", stage);
        control.setMainControl(this);
    }
    /**
     * Transitions the game screen to the First Screen.
     * @param planet the planet who's home screen we should view
     */
    public void goToHomeScreen(Planet planet) {
        HomeScreenController control;
        control = (HomeScreenController) changeScene("/spacetrader/HomeScreen.fxml", stage);
        control.setMainControl(this);
        planet.setVisited();
        control.setUpHomeScreen(planet);
    }
    /**
     * Transitions the game screen to the First Screen.
     * @param planet the planet who's market we're visiting
     */
   public void goToMarketScreen(Planet planet) {
        stage.setTitle("Welcome to the Market!");        
        MarketScreenController control;
        control = (MarketScreenController) changeScene("/spacetrader/MarketScreen.fxml", stage);
        control.setMainControl(this);
        control.setUpMarketScreen(planet, game.getPlayer());
    }

   /**
     * Transitions the game screen to the Warp Screen
     * @param source the planet we are on
     * @param dest the planet we are going to
     */
   public void goToWarpScreen(Planet source, Planet dest) {
       stage.setTitle("Traveling to " + dest.getName());
       WarpScreenController control;
       control = (WarpScreenController) changeScene("/spacetrader/travel/WarpScreen.fxml", stage);
       control.setMainControl(this);
       control.travel(source, dest);
   }
   
   /**
     * Transitions the game screen to the Space Map Screen.
     * @param planet the planet who's market we're on
     */
   public void goToSpaceMapScreen(Planet planet) {
        stage.setTitle("Space Map!");        
        SpaceMapScreenController control;
        control = (SpaceMapScreenController) changeScene("/spacetrader/SpaceMapScreen.fxml", stage);
        control.setMainControl(this);
        control.setUpMap(game.getPlayer(), planet, game.getUniverse().getPlanets());
    }
   
   /**
    * Transitions to the encounter screen corresponding with the specified encounter
    * @param encounter the encounter which is currently getting handled
    */
   public void goToEncounterScreen(Encounter encounter) {
        stage.setTitle("Space Encounter!");
        EncounterScreenController control;
        control = (EncounterScreenController) changeScene(encounter.getFXMLScene(), stage);
        control.setMainControl(this);
        control.setEncounter(encounter);
   }
   
   /**
     * Transitions the game screen to the Space Map Screen.
     */
   public final void goToStartScreen() {
        Stage startStage = new Stage();
        startStage.initOwner(stage);
        //startStage.setAlwaysOnTop(true);
        startStage.initModality(Modality.WINDOW_MODAL);
        startStage.setTitle("Start Screen!");
        
        StartScreenController control;
        control = (StartScreenController) changeScene("/spacetrader/StartScreen.fxml", startStage);
        control.setMainControl(this);
        control.setUpPlayerStats(game.getPlayer(), startStage);
        
        
       /* FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlScene));
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            Logger.getLogger(SpaceTrader.class.getName()).log(Level.SEVERE, null, e);
        }
        myStage.show();
        return loader.getController();*/
    }
   
    public void goToOverwriteScreen() {
        stage.setTitle("Save Game!");        
        OverwriteScreenController control;
        control = (OverwriteScreenController) changeScene("/spacetrader/persistence/OverwriteScreen.fxml", stage);
        control.setMainControl(this);
    }
    
    public void goToReloadScreen() {
        stage.setTitle("Reload Game!");        
        ReloadGameScreenController control;
        control = (ReloadGameScreenController) changeScene("/spacetrader/persistence/ReloadGameScreen.fxml", stage);
        control.setMainControl(this);
    }
    
    public void displayAlertMessage(String alertTitle, String alert) {
        Dialogs.create()
        .owner(stage)
        .title(alertTitle)
        //.masthead(null)
        .message(alert)
        .showInformation();
        
        /*final int MIN_WIDTH = 400;
        final int MIN_HEIGHT = 120;
        
        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle(alertTitle);
        Text message = new Text(alert);
        message.setWrappingWidth(MIN_WIDTH);
        message.setTextAlignment(TextAlignment.CENTER);
        message.setFont(new Font(18.0));
        
        Button confirmButton  = new Button("OK");
        confirmButton.addEventHandler(ActionEvent.ACTION, (ActionEvent) -> {
            dialog.close();
        });
        AnchorPane pane = new AnchorPane();
        pane.setPadding(new Insets(10, 10, 0, 10));
        AnchorPane.setRightAnchor(confirmButton, 10.0);
        AnchorPane.setBottomAnchor(confirmButton, 10.0);
        //AnchorPane.setTopAnchor(message, 10.0);
        pane.getChildren().addAll(message, confirmButton);
        pane.setMinSize(MIN_WIDTH, MIN_HEIGHT);
        dialog.setScene(new Scene(pane));
        dialog.showAndWait();*/
    }
    
    /** 
     * Changes the current scene to the scene specified by fxmlScene.
     * @param fxmlScene the name of the fxml file that holds the new scene
     * @param myStage the value of myStage
     * @return the corresponding Controller of the fxml file
     */
    private Initializable changeScene(String fxmlScene, Stage myStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlScene));
        try {
            myStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            Logger.getLogger(SpaceTrader.class.getName()).log(Level.SEVERE, null, e);
        }
        myStage.show();
        return loader.getController();
    }
}
