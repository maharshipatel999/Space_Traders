/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This class keeps references to the game's Universe and Player,
 * as well as the Main Controller.
 * 
 * @author nkaru_000, Caleb Stokols
 */
public class SpaceTrader extends Application {
    
    private Universe universe;
    private Player player;
    private MainController mainControl;
    
    @Override
    public void start(Stage primaryStage) {
        mainControl = new MainController(this, primaryStage);
        mainControl.goToWelcomeScreen();
    }

    /**
     * Gets the universe for this game.
     * 
     * @return this game's universe
     */
    public Universe getUniverse() {
        return universe;
    }

    /**
     * Sets the universe for this game.
     * 
     * @param universe this game's universe, cannot be null
     */
    public void setUniverse(Universe universe) {
        if (universe == null) {
            throw new IllegalArgumentException("Universe cannot be null.");
        }
        this.universe = universe;
    }

    /**
     * Gets the main player of this game.
     * 
     * @return this game's player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the main player of this game.
     * 
     * @param player this game's player, cannot be null
     */
    public void setPlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        this.player = player;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
