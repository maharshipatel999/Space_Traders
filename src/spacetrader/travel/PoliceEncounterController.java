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
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class PoliceEncounterController extends EncounterScreenController implements Initializable {

    @FXML
    private Text infoText;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Initiates Player attack sequence (Attack Pressed)
     *
     * @param e Event that is being processed
     */
    @FXML
    protected void attackPressed(ActionEvent e) {
        if (!encounter.getPlayer().getShip().isCarryingIllegalGoods()) {
            if (!getPlayerConfirmation()) {
                return;
            }

        }
        infoText.setText("You attack!");
    }

    /**
     * Handles the event where the submit button is pressed.
     *
     * @param e the event which is being processed
     */
    @FXML
    protected void submitPressed(ActionEvent e) {
        boolean inspectionFailed = ((PoliceEncounter) encounter).inspectPlayer();

        if (inspectionFailed) {
            mainControl.displayAlertMessage("Inspection Failed!", "The Customs Police took all the illegal goods from your ship, and sent you on your way.");
        } else {
            mainControl.displayAlertMessage("Inspection Passed!", "The Police find nothing illegal in your cargo holds and appologize for the inconvience.");
        }
        mainControl.goBackToWarpScreen();
    }

    /**
     * Handles event where Player flees when Flee Button is pressed.
     *
     * @param e Event that is being processed
     */
    @FXML
    protected void fleePressed(ActionEvent e) {
        if (!encounter.getPlayer().getShip().isCarryingIllegalGoods()) {
            if (!getPlayerConfirmation()) {
                return;
            }
        }
        infoText.setText("You try to flee!");
    }

    /**
     * Initiates Bribe Sequence for Player when ActionEvent Occurs
     *
     * @param e specific ActionEvent that occurred
     */
    @FXML
    protected void bribePressed(ActionEvent e) {
        if (!encounter.getPlayer().getShip().isCarryingIllegalGoods()) {
            if (!getPlayerConfirmation()) {
                return;
            }
        }
        if (encounter.getPlayer().getLocation().getPoliticalSystem().bribeLevel() <= 0) {
            mainControl.displayAlertMessage("Bribery Failed!", "These officers cannot be bribed.");
        } else {
            mainControl.displayAlertMessage("Bribery Offer", "I will offer you a bribery!");
        }
    }

    /**
     * Asks the player to confirm if they want to be stupid with the police.
     *
     * @return true if they confirmed, false otherwise
     */
    private boolean getPlayerConfirmation() {
        Action response = mainControl.displayYesNoComfirmation("Not Carrying Illegal Goods",
                "Are you sure you want to do this?",
                "You are not carrying illegal goods so you have nothing to fear!");

        return (response == Dialog.Actions.YES);

    }
}
