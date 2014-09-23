/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class FirstScreenController implements Initializable {

    private MainController mainControl;
    
    @FXML private TextArea spaceInformation;
    
    /**
     * Gives this controller a reference to the MainController.
     * @param mainControl the Main Controller of SpaceTrader
     */
    public void setMainControl(MainController mainControl) {
        this.mainControl = mainControl;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }
    
    /**
     * Displays every Planet in the TextArea
     * @param universe the universe who's planets should be displayed
     * @param playerName the name of the game's player
     */
    public void displayUniverse(Universe universe, String playerName) {
        for (Planet system : universe.getPlanets()) {
            spaceInformation.appendText(system + "\n");
        }
        spaceInformation.appendText("Player: " + playerName + "\n");
    }
}
