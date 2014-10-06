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
import javafx.stage.Stage;
import spacetrader.Planet;
import spacetrader.Player;
import spacetrader.Universe;

/**
 *
 * @author Caleb Stokols
 */
public class MainController {
    
    SpaceTrader game;
    Stage stage;
    
    /**
     * Creates the MainController.
     * MainController has a reference to the main class and stage. 
     */
    public MainController(SpaceTrader game, Stage stage) {
        this.game = game;
        this.stage = stage;
    }
    
    /**
     * Sets the game's universe to a new universe and the game's player
     * to a specified Player.
     * @param player 
     */
    public void setUpGame(Player player) {
        game.setUniverse(new Universe());
        game.setPlayer(player);
        goToSpaceMapScreen();
        //goToFirstScreen();
    }
    
    /**
     * Transitions the game screen to the Welcome Screen.
     */
    public void goToWelcomeScreen() {
        stage.setTitle("Space Traders!");
        WelcomeScreenController control;
        control = (WelcomeScreenController) changeScene("/spacetrader/WelcomeScreen.fxml");
        control.setMainControl(this);
    }
    
    /**
     * Transitions the game screen to the Character Dialog.
     */
    public void goToPlayerConfigScreen() {
        CharacterDialogController control;
        control = (CharacterDialogController) changeScene("/spacetrader/CharacterDialog.fxml");
        control.setMainControl(this);
    }
    /**
     * Transitions the game screen to the First Screen.
     */
    public void goToFirstScreen() {
        FirstScreenController control;
        control = (FirstScreenController) changeScene("/spacetrader/FirstScreen.fxml");
        control.setMainControl(this);
        control.displayUniverse(game.getUniverse(), game.getPlayer().getName());
    }
    /**
     * Transitions the game screen to the First Screen.
     * @param planet the planet who's market we're visiting
     */
   public void goToMarketScreen(Planet planet) {
        stage.setTitle("Welcome to the Market!");        
        MarketScreenController control;
        control = (MarketScreenController) changeScene("/spacetrader/MarketScreen.fxml");
        control.setMainControl(this);
        control.setUpMarketScreen(planet, game.getPlayer());
    }
   
   /**
     * Transitions the game screen to the Space Map Screen.
     * @param planet the planet who's market we're visiting
     */
   public void goToSpaceMapScreen() {
        stage.setTitle("Space Map!");        
        SpaceMapScreenController control;
        control = (SpaceMapScreenController) changeScene("/spacetrader/SpaceMapScreen.fxml");
        control.setMainControl(this);
        control.setUpMap(game.getUniverse().getPlanets());
    }
    
    /**
     * Changes the current scene to the scene specified by fxmlScene.
     * @param fxmlScene the name of the fxml file that holds the new scene
     * @return the corresponding Controller of the fxml file
     */
    private Initializable changeScene(String fxmlScene) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlScene));
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            Logger.getLogger(SpaceTrader.class.getName()).log(Level.SEVERE, null, e);
        }
        stage.show();
        return loader.getController();
    }
}
