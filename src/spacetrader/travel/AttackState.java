/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import spacetrader.PoliceRecord;

/**
 *
 * @author Caleb
 */
public class AttackState extends EncounterState {

    private boolean firstDisplay; //whether or not text has been displayed
    
    public AttackState(Encounter encounter) {
        super(encounter);
        this.firstDisplay = true;
    }

    @Override
    public void setButtons(EncounterScreenController sceneControl, HBox box) {
        box.getChildren().clear();
        
        //Add attack button
        Button attackButton = addButton("Attack", box);
        attackButton.setOnAction(event -> {
            this.firstDisplay = false;
            sceneControl.attackPressed();
        });

        //Add flee button
        Button fleeButton = addButton("Flee", box);
        fleeButton.setOnAction(event -> {
            this.firstDisplay = false;
            sceneControl.fleePressed();
        });

        if (encounter.playerCanSurrender()) {
            //Add surrender button
            Button surrenderButton = addButton("Surrender", box);
            surrenderButton.setOnAction(event -> {
                this.firstDisplay = false;
                sceneControl.surrenderPressed();
        });
        }
    }
    
    @Override
    public String getNextActionText() {
        if (firstDisplay && (encounter instanceof PoliceEncounter) && encounter.getPlayer().getPoliceRecord().compareTo(PoliceRecord.CRIMINAL) > 0) {
            return "The police hail they want you to surrender.";
        } else {
            return "Your opponent attacks.";
        }
    }
    
    @Override
    public void playerAttacks(EncounterScreenController encounterControl, boolean enemyIntimidated) {
        if (enemyIntimidated) {
            encounter.setState(new FleeState(encounter));
            encounter.getState().playerAttacks(encounterControl, enemyIntimidated);
        }
        
        final int originalPlayerHull = encounter.getPlayer().getShip().getHullStrength();
        final int originalOpponentHull = encounter.getOpponent().getHullStrength();
        
        encounterControl.opponentAttacks(false); //opponent is not fleeing
        encounterControl.playerAttacks();
        encounterControl.checkIfShipsDestroyed();
        
        encounterControl.checkIfChangeState(originalPlayerHull, originalOpponentHull);
    }
    
    @Override
    public void playerFlees(EncounterScreenController encounterControl) {
        final int originalPlayerHull = encounter.getPlayer().getShip().getHullStrength();
        final int originalOpponentHull = encounter.getOpponent().getHullStrength();
        
        encounterControl.opponentAttacks(true); //opponent is fleeing
        encounterControl.checkIfShipsDestroyed();
        encounterControl.checkIfPlayerEscapes();
        
        encounterControl.checkIfChangeState(originalPlayerHull, originalOpponentHull);
    }
}
