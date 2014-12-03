/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.travel;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 *
 * @author Caleb
 */
public class SurrenderState extends EncounterState {

    public SurrenderState(Encounter encounter) {
        super(encounter);
    }

    @Override
    public void setButtons(EncounterScreenController sceneControl, HBox box) {
        box.getChildren().clear();
        
        //Add attack button
        Button attackButton = addButton("Attack", box);
        attackButton.setOnAction(event -> sceneControl.attackPressed());
        
        //Add plunder button
        Button plunderButton = addButton("Plunder", box);
        plunderButton.setOnAction(event -> sceneControl.plunderPressed());
        plunderButton.setDefaultButton(true);
    }
    
    @Override
    public String getNextActionText() {
        return "Your opponent hails that he surrenders to you.";
    }
    
    @Override
    public void playerAttacks(EncounterScreenController encounterControl, boolean enemyIntimidated) {
        final int originalPlayerHull = encounter.getPlayer().getShip().getHullStrength();
        final int originalOpponentHull = encounter.getOpponent().getHullStrength();

        encounterControl.playerAttacks();
        encounterControl.checkIfShipsDestroyed();
        encounterControl.checkIfEnemyEscapes();

        encounterControl.checkIfChangeState(originalPlayerHull, originalOpponentHull);
    }
}
