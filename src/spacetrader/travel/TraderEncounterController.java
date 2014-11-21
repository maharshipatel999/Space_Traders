/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.travel;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spacetrader.PoliceRecord;
import spacetrader.commerce.Cargo;
import spacetrader.commerce.TradeGood;
import spacetrader.exceptions.CargoIsFullException;
import spacetrader.travel.Encounter.State;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class TraderEncounterController extends EncounterScreenController implements Initializable {

    @FXML
    private Label traderOffer, currAmountText;
    @FXML
    private TextField quantityField;
    
    private int maxQuantity;
    private TradeGood tradingGood;
    private int tradingPrice;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @Override
    protected void attackPressed(ActionEvent e) {
        int currentRecord = encounter.getPlayer().getPoliceRecordScore();
        int updatedRecord;
        if (currentRecord > PoliceRecord.CLEAN.minScore()) {
            updatedRecord = PoliceRecord.DUBIOUS.minScore();
        } else {
            updatedRecord = currentRecord + PoliceEncounter.ATTACK_POLICE_SCORE;
        }
        
        encounter.getPlayer().setPoliceRecordScore(updatedRecord);
        
        boolean opponentHasNoWeapons = (encounter.getOpponent().getTotalWeaponStrength() <= 0);
        if (opponentHasNoWeapons || ((TraderEncounter) encounter).playerReputationTooHigh()) {
            //trader flees
        } else {
            super.attackPressed(e);
        }
    }
    
    @Override
    public void setEncounter(Encounter encounter) {
        super.setEncounter(encounter);
        TraderEncounter traderEncounter = (TraderEncounter) encounter;
        
        traderOffer.setText("You are hailed with an offer to trade goods.");
        
        if (encounter.getState() != State.BUY && encounter.getState() != State.SELL) {
            //do something
            mainControl.goBackToWarpScreen();
        }
        
        this.tradingGood = traderEncounter.getRandomTradeableItem();
        this.tradingPrice = traderEncounter.getRandomPrice(this.tradingGood);

        String traderMessage, currentQuantityDesc;
        if (encounter.getState() == State.BUY) {
            traderMessage = String.format("The trader wants to buy %s, and offers ₪%d each.", tradingGood.toString(), tradingPrice);
            
            Cargo playerCargo = encounter.getPlayer().getCargo();
            this.maxQuantity = playerCargo.getQuantity(tradingGood);
            currentQuantityDesc = String.format(
            "You have %d unit(s) available. You paid about ₪%d per unit.\n\nHow many do you wish to sell?",
                this.maxQuantity, (playerCargo.getCostOfGood(tradingGood) / playerCargo.getQuantity(tradingGood)));
        } else {
            traderMessage = String.format("The trader wants to sell you %s for the price of ₪%d each.", tradingGood.toString(), tradingPrice);
            
            Cargo opponentCargo = encounter.getOpponent().getCargo();
            int actualAmount = opponentCargo.getQuantity(tradingGood);
            int affordableAmount = (encounter.getPlayer().getCredits() / tradingPrice);
            this.maxQuantity = Math.min(actualAmount, affordableAmount);
            currentQuantityDesc = String.format(
            "The trader has %d unit(s) for sale. You can afford %d units.\n\nHow many do you wish to buy?",
                actualAmount, affordableAmount);
            
        }
        traderOffer.setText(traderMessage);
        currAmountText.setText(currentQuantityDesc);
        
        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (validateQuantityText(newValue)) {
                ((StringProperty) observable).setValue(newValue);
            } else {
                ((StringProperty) observable).setValue(oldValue);
            }
        });
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

    /**
     * Handles the event where the trade button is pressed
     * 
     * @param event the action event
     */
    @FXML
    protected void acceptOffer(ActionEvent event) {
        String quantityText = quantityField.getText();
        if (quantityText.isEmpty()) {
            quantityField.requestFocus();
            return;
        }
        try {
            int quantity = Integer.parseInt(quantityText);
            
            if (quantity <= 0) {
                boolean response = mainControl.displayYesNoConfirmation("Rejection Confirmation", "Really?", "Are you sure you want to reject their offer?");
                if (!response) {
                    return;
                }
                mainControl.displayAlertMessage("Offer Rejected!", "You rudely reject the trader's offer and zoom off towards your destination.");
            } else if (quantity > maxQuantity) {
                mainControl.displayAlertMessage("Illegal Amount", "The amount you have chosen is too large. Please choose an amount between 0 and " + maxQuantity + ".");
                return;
            } else if (encounter.getState() == State.BUY) {
                encounter.getPlayer().getCargo().removeItem(tradingGood, quantity);
                encounter.getOpponent().getCargo().addItem(tradingGood, quantity, tradingPrice);
                encounter.getPlayer().addCredits(quantity * tradingPrice);
                mainControl.displayAlertMessage("Offer Accepted!", 
                    String.format("You succesfully sold %d unit(s) of %s for ₪%d! "
                            + "The trader thanks you for doing business with him.",
                            quantity, tradingGood.toString(), tradingPrice * quantity));
            } else {
                encounter.getPlayer().getCargo().addItem(tradingGood, quantity, tradingPrice);
                encounter.getOpponent().getCargo().removeItem(tradingGood, quantity);
                encounter.getPlayer().removeCredits(quantity * tradingPrice);
                mainControl.displayAlertMessage("Offer Accepted!", 
                    String.format("You successfully purchased %d unit(s) of %s for ₪%d! "
                            + "The trader thanks you for doing business with him.", 
                            quantity, tradingGood.toString(), tradingPrice * quantity));
            }
            mainControl.goBackToWarpScreen();
        } catch (NumberFormatException e) {
            mainControl.displayAlertMessage("Incorrect entry!", "Please enter an integer!");
        } catch (CargoIsFullException e) {
            int remainingSlots =  encounter.getPlayer().getCargo().getRemainingCapacity();
            mainControl.displayAlertMessage("Full Cargo!", "You only have room to buy " + remainingSlots + ".");
        }
    }

    @FXML
    protected void rejectOffer(ActionEvent event) {
        mainControl.goBackToWarpScreen();
    }
}