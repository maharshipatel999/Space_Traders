/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.system;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import spacetrader.planets.Planet;
import spacetrader.Universe;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class FirstScreenController extends SceneController implements Initializable {

    private Universe universe;
    
    @FXML private TextArea spaceInformation;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }
    
    @FXML protected void goToMarket(ActionEvent event) {
        //Pick a random planet to start off game
        ArrayList<Planet> planets = universe.getPlanets();
        Random rand = new Random();
        mainControl.goToMarketScreen(planets.get(rand.nextInt(planets.size())));
    }
    
    /**
     * Displays every Planet in the TextArea
     * @param universe the universe who's planets should be displayed
     * @param playerName the name of the game's player
     */
    public void displayUniverse(Universe universe, String playerName) {
        this.universe = universe;
        for (Planet system : universe.getPlanets()) {
            spaceInformation.appendText(system + "\n");
        }
        spaceInformation.appendText("Player: " + playerName + "\n");
    }
}
