/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.travel;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class PoliceEncounterController extends EncounterScreenController implements Initializable {

    @FXML private Text infoText;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML protected void attackPressed(ActionEvent e) {
        infoText.setText("You attack!");
    }
    
    /**
     * Handles the event where the submit button is pressed.
     * @param e the event which is being processed
     */
    @FXML protected void submitPressed(ActionEvent e) {
        ((PoliceEncounter) encounter).inspectPlayer();
    }
    
    @FXML protected void fleePressed(ActionEvent e) {
        infoText.setText("You try to flee!");
    }
    
    @FXML protected void bribePressed(ActionEvent e) {
        //if (encounter.getPlayer().getLocation().getPoliticalSystem().bribeLevel() <= 0) {
            mainControl.displayAlertMessage("Bribery Failed!", "These officers cannot be bribed.");
        //}
    }
}
