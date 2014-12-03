/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.travel;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import spacetrader.PoliceRecord;
import spacetrader.commerce.Cargo;
import spacetrader.commerce.TradeGood;
import spacetrader.exceptions.CargoIsFullException;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class TraderEncounterController extends EncounterScreenController implements Initializable {

    private TextField quantityField;
    private Label traderOffer, currAmountText;
    private VBox tradePane;
    
    private int maxQuantity;
    private TradeGood tradingGood;
    private int tradingPrice;
    
    private boolean acceptedOfferToTrade; //specifies if the player has accepted the trader's offer to trader
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final int VBOX_LABEL_SPACING = 20;
        final int HBOX_LABEL_SPACING = 20;
        final int SIDE_PADDING = 25;
        
        //initialize traderOffer label
        traderOffer = new Label("Offer");
        traderOffer.setFont(new Font(23));
        traderOffer.setWrapText(true);
        traderOffer.setAlignment(Pos.CENTER);
        traderOffer.setContentDisplay(ContentDisplay.CENTER);

        //intialize currAmountText label
        currAmountText = new Label("Current amount");
        currAmountText.setFont(new Font(23));
        currAmountText.setWrapText(true);
        currAmountText.setAlignment(Pos.CENTER);
        currAmountText.setContentDisplay(ContentDisplay.CENTER);
        
        //initialize amountLabel label
        Label amountLabel = new Label("Amount:");
        amountLabel.setFont(new Font(17));
        
        //initialize quantityfield textfield
        quantityField = new TextField("0");
        quantityField.setPrefSize(70, 31);
        quantityField.setPromptText("Enter An Amount");
        
        //initialize hbox
        HBox hbox = new HBox(HBOX_LABEL_SPACING, amountLabel, quantityField);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPrefHeight(31);
        
        //initialize trade pane
        tradePane = new VBox(VBOX_LABEL_SPACING, traderOffer, currAmountText, hbox);
        tradePane.prefHeight(139);
        tradePane.setAlignment(Pos.CENTER);
        tradePane.setPadding(new Insets(0, SIDE_PADDING, 0, SIDE_PADDING));
    }
    
    @FXML @Override
    protected void attackPressed() {
        if (encounter.state instanceof TradeState || encounter.state instanceof IgnoreState) {
            int currentRecord = encounter.getPlayer().getPoliceRecordScore();
            int updatedRecord;
            if (currentRecord > PoliceRecord.CLEAN.minScore()) {
                updatedRecord = PoliceRecord.DUBIOUS.minScore();
            } else {
                updatedRecord = currentRecord + TraderEncounter.ATTACK_TRADER_SCORE;
            }

            encounter.getPlayer().setPoliceRecordScore(updatedRecord);
        }
        super.attackPressed();
    }
    
    @FXML @Override
    protected void surrenderPressed() {
        throw new UnsupportedOperationException("You cannot surrender to a Trader");
    }
    
    @Override
    public void setEncounter(Encounter encounter) {
        super.setEncounter(encounter);
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
     * Handles the event where the trade button is pressed. Changes to show the
     * offer the trader will make.
     */
    protected void acceptOfferPressed() {
        borderPane.setCenter(tradePane);
        TraderEncounter traderEncounter = (TraderEncounter) encounter;
        boolean traderBuying = ((TradeState) encounter.getState()).isTraderBuying();
        
        
        traderOffer.setText("You are hailed with an offer to trade goods.");
        
        this.tradingGood = traderEncounter.getRandomTradeableItem();
        this.tradingPrice = traderEncounter.getRandomPrice(this.tradingGood);

        String traderMessage, currentQuantityDesc;
        if (traderBuying) {
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

    /**
     * Handles the event where the trade button is pressed after the trader
     * has made an offer
     */
    @FXML
    protected void tradePressed() {
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
                mainControl.displayInfoMessage(null, "Offer Rejected!", "You rudely reject the trader's offer and zoom off towards your destination.");
            } else if (quantity > maxQuantity) {
                mainControl.displayInfoMessage(null, "Illegal Amount", "The amount you have chosen is too large. Please choose an amount between 0 and " + maxQuantity + ".");
                return;
            } else if (((TradeState) encounter.getState()).isTraderBuying()) {
                encounter.getPlayer().getCargo().removeItem(tradingGood, quantity);
                encounter.getOpponent().getCargo().addItem(tradingGood, quantity, tradingPrice);
                encounter.getPlayer().addCredits(quantity * tradingPrice);
                mainControl.displayInfoMessage(null, "Offer Accepted!", 
                    String.format("You succesfully sold %d unit(s) of %s for ₪%d! "
                            + "The trader thanks you for doing business with him.",
                            quantity, tradingGood.toString(), tradingPrice * quantity));
            } else {
                encounter.getPlayer().getCargo().addItem(tradingGood, quantity, tradingPrice);
                encounter.getOpponent().getCargo().removeItem(tradingGood, quantity);
                encounter.getPlayer().removeCredits(quantity * tradingPrice);
                mainControl.displayInfoMessage(null, "Offer Accepted!", 
                    String.format("You successfully purchased %d unit(s) of %s for ₪%d! "
                            + "The trader thanks you for doing business with him.", 
                            quantity, tradingGood.toString(), tradingPrice * quantity));
            }
            finishEncounter();
        } catch (NumberFormatException e) {
            mainControl.displayErrorMessage(null, "Incorrect entry!", "Please enter an integer!");
        } catch (CargoIsFullException e) {
            int remainingSlots =  encounter.getPlayer().getCargo().getRemainingCapacity();
            mainControl.displayWarningMessage(null, "Full Cargo!", "You only have room to buy " + remainingSlots + ".");
        }
    }
}