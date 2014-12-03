/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import static spacetrader.travel.EncounterState.addButton;

public class TradeState extends EncounterState {

    private boolean offerAccepted;
    private final boolean traderBuying;

    public TradeState(TraderEncounter encounter, boolean traderBuying) {
        super(encounter);
        this.offerAccepted = false;
        this.traderBuying = traderBuying;
    }

    @Override
    public void setButtons(EncounterScreenController sceneControl, HBox box) {
        TraderEncounterController traderController = (TraderEncounterController) sceneControl;
        box.getChildren().clear();

        if (!offerAccepted) {
            //Add attack button
            Button attackButton = addButton("Attack", box);
            attackButton.setOnAction(event -> sceneControl.attackPressed());

            //Add ignore button
            Button ignoreButton = addButton("Ignore", box);
            ignoreButton.setOnAction(event -> sceneControl.ignorePressed());

            //Add trade button
            Button acceptOfferButton = addButton("Trade", box);
            acceptOfferButton.setOnAction(event -> {
                traderController.acceptOfferPressed();
                offerAccepted = true;
                setButtons(sceneControl, box);
            });
            
        } else {
            //Add reject button
            Button rejectButton = addButton("Reject", box);
            rejectButton.setOnAction(event -> sceneControl.ignorePressed());

            //Add trade button
            Button tradeButton = addButton("Trade", box);
            tradeButton.setOnAction(event -> traderController.tradePressed());
        }
    }
    
    @Override
    public String getNextActionText() {
        return "You are hailed with an offer to trade goods.";
    }
    
    /**
     * Gets whether the trader is buying or selling.
     * 
     * @return true if the trader is buying
     */
    public boolean isTraderBuying() {
        return traderBuying;
    }
    
    @Override
    public void playerAttacks(EncounterScreenController encounterControl, boolean enemyIntimidated) {
        if (offerAccepted) {
            super.playerAttacks(encounterControl, enemyIntimidated);
        } else {
            //Change encounter state to FleeState or AttackState and delegate to that new state.
            if (encounter.opponentIntimidated()) {
                encounterControl.setState(new FleeState(encounter));
            } else {
                encounterControl.setState(new AttackState(encounter));
            }
            encounter.getState().playerAttacks(encounterControl, enemyIntimidated);
        }
    }
}
