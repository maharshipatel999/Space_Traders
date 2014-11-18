/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import java.awt.Point;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
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
        mainControl.displayAlertMessage("Attack!", "You agrresively start attacking the police!");
        mainControl.goBackToWarpScreen();
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
            mainControl.displayAlertMessage("Bribery Failed!", "These officers cannot be bribed.");
        } else {
            Planet destination = new Planet("Earth", new Point(5, 10)); //FIX THIS
            int bribeAmount = ((PoliceEncounter) encounter).calculcateBribe(destination);
            String masthead = String.format("The police will let you go off ₪%d.", bribeAmount);
            String message = "Do you accept their offer?";
            boolean response = mainControl.displayYesNoConfirmation("Bribery Offer", masthead, message);
            if (response) {
                try {
                    encounter.getPlayer().getWallet().remove(bribeAmount);
                } catch (InsufficientFundsException e) {
                    mainControl.displayAlertMessage("Cannot Afford Bribe", "You do not have enough money to bribe the police.");
                }
                mainControl.displayAlertMessage("Bribe Successful", "The officers accept your bribe and send you off on your way.");
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
