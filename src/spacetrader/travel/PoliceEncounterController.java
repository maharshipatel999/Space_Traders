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
import javafx.geometry.Point2D;
import javafx.scene.text.Text;
import spacetrader.PoliceRecord;
import spacetrader.exceptions.InsufficientFundsException;
import spacetrader.planets.Planet;

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

    @Override
    protected void attackPressed(ActionEvent e) {
        boolean attackConfirmed;
        if (!encounter.getPlayer().getShip().isCarryingIllegalGoods()) {
            attackConfirmed = getPlayerConfirmation();
        } else {
            attackConfirmed = mainControl.displayYesNoConfirmation(
                    "Engaging In Criminal Activity!",
                    "Are you sure you want to do this?",
                    PoliceEncounter.ATTACK_CONFIM_MSG);
        }
        if (!attackConfirmed) {
            return;
        }

        //the player's police record should be no greater than the crook score.
        int updatedRecord = Math.min(encounter.getPlayer().getPoliceRecordScore(), PoliceRecord.CROOK.minScore());
        encounter.getPlayer().setPoliceRecordScore(updatedRecord + PoliceEncounter.ATTACK_POLICE_SCORE);

        super.attackPressed(e);
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
            String fineMsg = String.format("The police discovers illegal goods "
                    + "in your cargo holds. These goods are impounded and you are "
                    + "fined ₪%d", ((PoliceEncounter) encounter).determineFine());
            mainControl.displayInfoMessage(null, "Inspection Failed!", fineMsg);
        } else {
            mainControl.displayInfoMessage(null, "Inspection Passed!", "The Police find nothing illegal in your cargo holds and appologize for the inconvience.");
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

        int updatedRecord;
        if (encounter.getPlayer().getPoliceRecord().compareTo(PoliceRecord.DUBIOUS) >= 0) {
            updatedRecord = PoliceRecord.DUBIOUS.minScore() - 1;
        } else {
            int currentRecord = encounter.getPlayer().getPoliceRecordScore();
            updatedRecord = currentRecord + PoliceEncounter.FLEE_FROM_INSPECTION;
        }
        encounter.getPlayer().setPoliceRecordScore(updatedRecord);

        //go the the attack screen.
        infoText.setText("You try to flee!");
        mainControl.goBackToWarpScreen();
    }

    /**
     * Initiates Bribe Sequence for Player when ActionEvent Occurs
     *
     * @param event specific ActionEvent that occurred
     */
    @FXML
    protected void bribePressed(ActionEvent event) {
        if (!encounter.getPlayer().getShip().isCarryingIllegalGoods()) {
            if (!getPlayerConfirmation()) {
                return;
            }
        }
        if (encounter.getPlayer().getLocation().getPoliticalSystem().bribeLevel() <= 0) {
            mainControl.displayInfoMessage(null, "Bribery Failed!", "These officers cannot be bribed.");
        } else {
            Planet destination = new Planet("Earth", new Point2D(5, 10)); //FIX THIS
            int bribeAmount = ((PoliceEncounter) encounter).calculcateBribe(destination);
            String masthead = String.format("These police officers are willing to forego inspection for the amount of ₪%d.", bribeAmount);
            String message = "Do you accept their offer?";
            boolean response = mainControl.displayYesNoConfirmation("Bribery Offer", masthead, message);
            if (response) {
                try {
                    encounter.getPlayer().removeCredits(bribeAmount);
                } catch (InsufficientFundsException e) {
                    mainControl.displayInfoMessage(null, "Cannot Afford Bribe", "You don't have enough cash for a bribe.");
                    return;
                }
            mainControl.displayInfoMessage(null, "Bribe Successful", "The officers accept your bribe and send you off on your way.");
            } else {
                return;
            }
        }

        mainControl.goBackToWarpScreen();
    }

    /**
     * Asks the player to confirm if they want to be stupid with the police.
     *
     * @return true if they confirmed, false otherwise
     */
    private boolean getPlayerConfirmation() {
        boolean response = mainControl.displayYesNoConfirmation("Not Carrying Illegal Goods",
                "Are you sure you want to do this?",
                "You are not carrying illegal goods so you have nothing to fear!");

        return (response);
    }
}
