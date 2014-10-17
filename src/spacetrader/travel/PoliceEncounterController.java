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
import spacetrader.system.SceneController;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class PoliceEncounterController extends SceneController implements Initializable {

    @FXML private Text infoText;
    
    private Encounter encounter;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void setUpEncounter(Encounter encounter) {
        this.encounter = encounter;
    }
    
    @FXML protected void attackPressed(ActionEvent e) {
        infoText.setText("You attack!");
    }
    
    @FXML protected void submitPressed(ActionEvent e) {
        int fine = Math.min(80, -1 * encounter.getPlayer().getPoliceRecordScore());
        fine *= encounter.getPlayer().getCurrentWorth();
        fine /= 100;
        fine = (1 + (fine / 500)) * 500;
    }
    
    @FXML protected void fleePressed(ActionEvent e) {
        infoText.setText("You try to flee!");
    }
    
    @FXML protected void bribePressed(ActionEvent e) {
        //if (encounter.getPlayer().getLocation().getBribeLevel() <= 0) {
        //    mainControl.displayPopUpMessage(
        //}
    }
}
