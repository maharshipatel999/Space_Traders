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

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class PirateEncounterController extends EncounterScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    protected void attackPressed(ActionEvent e) {
        super.attackPressed(e);
    }

    /**
     * Follows Player surrender sequence in response to attack button. pressed
     */
    @FXML
    protected void surrenderPressed() {

    }

    /**
     * Follows Player flee sequence in response to attack button pressed.
     */
    @FXML
    protected void fleePressed() {

    }
}
