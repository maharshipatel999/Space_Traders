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

    private Universe universe;
    private Player player;
    
    @FXML private TextArea spaceInformation;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void setUniverse(Universe universe, Player player) {
        this.universe = universe;
        this.player = player;
        displayUniverse();
    }
    
    private void displayUniverse() {
        for (SolarSystem system : universe.getSolarSystems()) {
            spaceInformation.appendText(system + "\n");
        }
        spaceInformation.appendText("Player: " + player.getName() + "\n");
    }
}
