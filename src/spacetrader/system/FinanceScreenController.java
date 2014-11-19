/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.system;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spacetrader.Player;
import spacetrader.planets.Planet;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class FinanceScreenController extends SceneController
    implements Initializable {

    @FXML
    private Label currentBalance;
    @FXML
    private Label debt;
    @FXML
    private TextField loanAmt;
    @FXML
    private Button getLoan;
    @FXML
    private Label shipVal;
    @FXML
    private Label dailyCost;
    @FXML
    private Button buyInsurance;
    @FXML
    private Button payLoan;
    @FXML
    private Button backButton;
    @FXML
    private TextField payBackAmt;

    private Player player;
    private Planet planet;

    /**
     * Sets up the finance screen for this planet. Shows options for loans and
     * insurance
     *
     * @param player the user, who uses this bank
     */
    @FXML
    public void setUpFinanceScreen(Player player) {
        this.player = player;
        currentBalance.setText("Current balance: ₪"
                + player.getCredits());
        debt.setText("₪" + player.getDebt());
        shipVal.setText("₪" + player.getShip()
                .currentShipPriceWithoutCargo());
        int val = player.getShip().currentShipPriceWithoutCargo();
        int cost = val / 1000;
        dailyCost.setText("₪" + cost);
        if (!player.getShip().hasEscapePod()) {
            buyInsurance.setDisable(true);
            buyInsurance.setText("Escape pod required");
        }
        int ownMoney = player.getCredits() - player.getDebt();
        if (!(ownMoney > player.getDebt())) {
            payLoan.setDisable(true);
            payLoan.setText("Insufficient funds to pay loans");
        }
    }

    /**
     * Allows player to buy loan of amount 1 - 1000. Updates wallet and debt
     *
     */
    @FXML
    private void buyLoan() {
        String loanStr = loanAmt.getText();
        try {
            int loan = Integer.parseInt(loanStr);
            if (loan > 1000) {
                loanAmt.clear();
                mainControl.displayAlertMessage("Incorrect entry!",
                        "Please enter a number 1 - 1000 only!");
                return;
            }
            player.addDebt(loan);
            player.addCredits(loan);
            currentBalance.setText("Current balance: ₪" 
                    + player.getCredits());
            debt.setText("₪" + player.getDebt());
            loanAmt.clear();
        } catch (NumberFormatException e) {
            loanAmt.clear();
            mainControl.displayAlertMessage("Incorrect entry!",
                    "Please enter a number 1 - 1000 only!");
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
                loanAmt.clear();
                mainControl.displayAlertMessage("Incorrect entry!",
                    "You do not have enough money to pay that much!");
                return;
            }
            player.removeCredits(amt);
            player.removeDebt(amt);
            currentBalance.setText("Current balance: ₪" 
                    + player.getCredits());
            debt.setText("₪" + player.getDebt());
            payBackAmt.clear();
        } catch (NumberFormatException e) {
            payBackAmt.clear();
            mainControl.displayAlertMessage("Incorrect entry!",
                    "Please enter a valid numerical value!");
        }  
    }

    /**
     * Allows player to buy insurance. Sets insurance daily cost
     */
    @FXML
    private void buyInsurance() {
        int val = player.getShip().currentShipPriceWithoutCargo();
        int cost = val / 1000;
        player.setInsuranceCost(cost);
    }

    /**
     * Changes screen back to home screen
     */
    @FXML
    protected void goBackToHomeScreen() {
        mainControl.goToHomeScreen(player, player.getLocation());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
