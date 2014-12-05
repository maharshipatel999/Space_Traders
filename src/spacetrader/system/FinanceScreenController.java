/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.system;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import spacetrader.Player;
import spacetrader.PoliceRecord;
import spacetrader.SkillList;
import spacetrader.SkillList.Skill;
import spacetrader.Tools;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class FinanceScreenController extends SceneController
    implements Initializable {

    @FXML
    private Label currentBalance, debt, debtText;
    @FXML
    private TextField loanAmt;
    @FXML
    private Button getLoan;
    @FXML
    private Label shipVal, dailyCost, shipValueLabel, loanLabel;
    @FXML
    private Button buyInsurance, payLoan, backButton;
    @FXML
    private TextField payBackAmt;
    @FXML
    private HBox shipValueBox, insuranceCostBox;
 
    private Player player;
    private int shipValue;

    /**
     * Sets up the finance screen for this planet. Shows options for loans and
     * insurance.
     *
     * @param player the user, who uses this bank
     */
    @FXML
    public void setUpFinanceScreen(Player player) {
        this.player = player;
        this.shipValue = player.getShip().currentShipPriceWithoutCargo(player.getEffectiveSkill(SkillList.Skill.TRADER));
        
        ChangeListener<String> textFieldListener= (observable, oldValue, newValue) -> {
            if (validateQuantityText(newValue)) {
                ((StringProperty) observable).setValue(newValue);
            } else {
                ((StringProperty) observable).setValue(oldValue);
            }
        };
        
        loanAmt.textProperty().addListener(textFieldListener);
        payBackAmt.textProperty().addListener(textFieldListener);
        
        
        
        updateScreen();
    }
    
    private boolean validateQuantityText(String text) {
        boolean valid = true;
        try {
            int value = Integer.parseInt(text);
            if (value < 0) {
                valid = false;
            }
        } catch (NumberFormatException e) {
            valid = false;
        }
        return valid || text.isEmpty();
    }
    
    private int calculateCostOfInsurance() {
        int cost = (shipValue * 5) / 2000;
        cost = Math.max(1, cost);
        
        return cost;
    }
    
    private int getMaxLoan() {
        int maxLoan;
        if (player.getPoliceRecord().compareTo(PoliceRecord.CLEAN) >= 0)  {
            maxLoan = ((player.getCurrentWorth() / 10) / 500) * 500;
            maxLoan = Tools.applyBounds(maxLoan, 1000, 25000);
        } else {
            maxLoan = 500;
        }
        maxLoan *= (1 + (player.getEffectiveSkill(Skill.INVESTOR) / 100.0));
        
        return maxLoan;
    }
    
    private void updateScreen() {
        
        int cost = calculateCostOfInsurance();
        
        currentBalance.setText("Current Balance: ₪" + player.getCredits());
        debt.setText("₪" + player.getDebt());
        shipVal.setText("₪" + shipValue);
        dailyCost.setText("₪" + cost);
        
        if (player.getDebt() == 0) {
            payLoan.setDisable(true);
            payBackAmt.setDisable(true);
            debtText.setText("You have no debt to pay off. "); //space is important
        } else {
            debtText.setText("How much debt would you like to pay off?");
            payLoan.setDisable(false);
            payBackAmt.setDisable(false);
         }
        loanAmt.clear();
        payBackAmt.clear();
        
        loanLabel.setText(String.format("You can borrrow up to ₪%d. How much would you like?", getMaxLoan()));
        
        if (!player.getShip().hasEscapePod()) {
            buyInsurance.setDisable(true);
            shipValueLabel.setText("Insurance pays out when you must escape "
                    + "from your ship with an escape pod. Since you don't have "
                    + "a pod, you can't get insurance.");
            shipVal.setFont(new Font(14));
            shipVal.setText("");
            insuranceCostBox.setVisible(false);
        } else {
            shipValueLabel.setText("Current Value of Your Ship: ");
            insuranceCostBox.setVisible(true);
            shipVal.setFont(new Font(19));
            if (player.getInsuranceCost() > 0) {
                buyInsurance.setText("Buy Inurance");
                InformationPresenter.getInstance().showTextOnHover(buyInsurance,
                        "When your ship is destroyed, if you have insurance on your"
                        + " ship, the bank will fully refund your ship's costs.");
            } else {
                buyInsurance.setText("Stop Insurance");
                InformationPresenter.getInstance().showTextOnHover(buyInsurance,
                        "If you stop your insurance, your no-claim will return to"
                        + " 0%, even if you buy new insurance immediately.");
            }
        }
    }

    /**
     * Allows player to buy loan of amount up to maxLoan. Updates wallet and debt.
     */
    @FXML
    private void buyLoan() {
        String loanStr = loanAmt.getText();
        try {
            int loan = Integer.parseInt(loanStr);
            if (loan + player.getDebt() > getMaxLoan()) {
                loanAmt.clear();
                mainControl.displayWarningMessage(null, "Max Loan Reached!",
                        "You may not be more than ₪%d in debt!", getMaxLoan());
                return;
            }
            player.addDebt(loan);
            player.addCredits(loan);
            updateScreen();
        } catch (NumberFormatException e) {
            loanAmt.clear();
            mainControl.displayErrorMessage(null, "Incorrect entry!",
                    "Please enter a number between 1 - %d only.", getMaxLoan());
        }
        
    }

    /**
     * Allows player to pay loan. Updates wallet and sets debt to 0.
     */
    @FXML
    private void payLoan() {
        String amtStr = payBackAmt.getText();
        try {
            int amt = Integer.parseInt(amtStr);
            if (amt > player.getCredits()) {
                mainControl.displayWarningMessage(null, "Insufficient Funds!",
                    "You do not have enough money to pay that much.");
                return;
            }
            player.removeCredits(amt);
            player.removeDebt(amt);
        } catch (NumberFormatException e) {
            payBackAmt.clear();
            mainControl.displayErrorMessage(null, "Incorrect entry!",
                    "Please enter a valid numerical value.");
        }
        updateScreen();
    }

    /**
     * Allows player to buy (or sell) insurance. Sets insurance daily cost.
     */
    @FXML
    private void buyInsurance() {
        if (player.getInsuranceCost() <= 0) { //player has no insurance
            int cost = calculateCostOfInsurance();
            player.setInsuranceCost(cost);
        } else if (mainControl.displayYesNoConfirmation(null, "Stop Insurance?",
                "Do you really wish to stop your insurance and lose your no-claim?")) {
            player.setInsuranceCost(0);
            player.resetNoClaimDays();
            mainControl.displayInfoMessage(null, "Insurance Stopped", "Your insurance has been stopped.");
        }
        
        updateScreen();
    }

    /**
     * Changes screen back to home screen
     */
    @FXML
    protected void goBackToHomeScreen() {
        mainControl.goToHomeScreen(player.getLocation());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
