/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import spacetrader.PoliceRecord;
import spacetrader.exceptions.InsufficientFundsException;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class PoliceEncounterController extends EncounterScreenController implements Initializable {

    @FXML
    private Text infoText;
    
    private boolean playerAlreadyAttacked = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML @Override
    protected void attackPressed() {
        //if player is being inspected, ask them to confirm if they're carrying illegal goods.
        if (!encounter.getPlayer().getShip().isCarryingIllegalGoods() || playerConfirmsToAvoidInspection()) {
            if (playerAlreadyAttacked || playerConfirmsToAttack()) {
                playerAlreadyAttacked = true;

                //This happens every time the player attacks the police.
                ((PoliceEncounter) encounter).updateRecordFromAttacking();
                super.attackPressed();
            }
        }
        
    }
    
    @Override
    protected void fleePressed() {
        if (!encounter.getPlayer().getShip().isCarryingIllegalGoods() || playerConfirmsToAvoidInspection()) {
            super.fleePressed();
        }
    }
    
    @Override
    protected void surrenderPressed() {
        if (encounter.getPlayer().getPoliceRecord().compareTo(PoliceRecord.PSYCHO) <= 0) {
            displayNoSurrenderMessage();
        } else if (playerConfirmsToSurrender()) {
            playerGetsArrested();
        }
        //mainControl.goBackToWarpScreen();
    }

    /**
     * Handles the event where the submit button is pressed.
     */
    @FXML
    protected void submitPressed() {
        boolean inspectionFailed = ((PoliceEncounter) encounter).inspectPlayer();

        if (inspectionFailed) {
            mainControl.displayInfoMessage(null, "Inspection Failed!",
                    "The police discovers illegal goods in your cargo holds."
                    + " These goods are impounded and you are fined ₪%d",
                    ((PoliceEncounter) encounter).determineFine());
        } else {
            mainControl.displayInfoMessage(null, "Inspection Passed!", 
                    "The Police find nothing illegal in your cargo holds and"
                    + " appologize for the inconvience.");
        }
        finishEncounter();
    }

    /**
     * Initiates Bribe sequence for player when they press the bribe button.
     */
    protected void bribePressed() {
        if (!encounter.getPlayer().getShip().isCarryingIllegalGoods() && !playerConfirmsToAvoidInspection()) {
            return;
        }
        if (encounter.getPlayer().getLocation().getPoliticalSystem().bribeLevel() <= 0) {
            mainControl.displayInfoMessage(null, "Bribery Failed!", "These officers cannot be bribed.");
        } else {
            int bribeAmount = ((PoliceEncounter) encounter).calculcateBribe(encounter.getDestination());
            String masthead = String.format("These police officers are willing to forego inspection for the amount of ₪%d.", bribeAmount);
            String message = "Do you accept their offer?";
            boolean acceptsBribe = mainControl.displayYesNoConfirmation("Bribery Offer", masthead, message);
            if (acceptsBribe) {
                try {
                    encounter.getPlayer().removeCredits(bribeAmount);
                    mainControl.displayInfoMessage(null, "Bribe Successful", 
                            "The officers accept your bribe and send you off on your way.");
                    finishEncounter();
                } catch (InsufficientFundsException e) {
                    mainControl.displayInfoMessage(null, "Cannot Afford Bribe", "You don't have enough cash for a bribe.");
                }
            }
        }

        
    }

    /**
     * Asks the player to confirm if they want to resist submitting to a police
     * inspection even though they are not carrying illegal goods.
     *
     * @return true if they confirmed, false otherwise
     */
    private boolean playerConfirmsToAvoidInspection() {
        //Only ask them to confirm if they're being inspected.
        if (encounter.getState() instanceof InspectionState) {
            return mainControl.displayYesNoConfirmation("Not Carrying Illegal Goods",
                    "Are you sure you want to do this?",
                    "You are not carrying illegal goods so you have nothing to fear!");
        } else {
            return true;
        }
    }
    
    /**
     * Confirms if the player wants to attack the police, a crime which will
     * turn them into a criminal.
     * 
     * @return true if the player confirmed, false otherwise
     */
    private boolean playerConfirmsToAttack() {
        return mainControl.displayYesNoConfirmation(
                "Engaging In Criminal Activity!",
                "Are you sure you wish to attack the police?",
                "This will turn you into a criminal!");
    }
    
    /**
     * Asks the player to confirm if they want to surrender.
     * 
     * @return true if they confirmed, false otherwise
     */
    private boolean playerConfirmsToSurrender() {
        return mainControl.displayYesNoConfirmation("Surrender?",
                "Do you really want to surrender?",
                "Your fine and time in prison will depend on how big a criminal"
                + " you are. Your fine will be taken from your cash. If you"
                + " don't have enough cash, the police will sell your ship to"
                + " get it. If you have debts, the police will pay them from"
                + " your credits (if you have enough) before you go to prison,"
                + " because otherwise the interest would be staggering.");
    }
    
    /**
     * Informs the player that they cannot surrender.
     */
    private void displayNoSurrenderMessage() {
        mainControl.displayInfoMessage(null, "You May Not Surrender!",
                "You are too big of a criminal, so surrendering is NOT an "
                + "option anymore.");
    }
}
