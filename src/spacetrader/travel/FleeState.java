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
public class FleeState extends EncounterState {

    private boolean uninvoked;

    public FleeState(Encounter encounter) {
        super(encounter);
        this.uninvoked = true;
    }

    @Override
    public void setButtons(EncounterScreenController sceneControl, HBox box) {
        box.getChildren().clear();

        //Add attack button
        Button attackButton = addButton("Attack", box);
        attackButton.setOnAction(event -> sceneControl.attackPressed());

        if (uninvoked) {
            //Add ignore button
            Button ignoreButton = addButton("Ignore", box);
            ignoreButton.setOnAction(event -> {
                sceneControl.ignorePressed();
                this.uninvoked = false;
            });
        } else {
            //Add flee button
            Button fleeButton = addButton("Flee", box);
            fleeButton.setOnAction(event -> sceneControl.fleePressed());

            if (encounter.playerCanSurrender()) {
                //Add surrender button
                Button surrenderButton = addButton("Surrender", box);
                surrenderButton.setOnAction(event -> sceneControl.surrenderPressed());
            }
        }
    }

    @Override
    public String getNextActionText() {
        return "Your opponent is fleeing.";
    }

    @Override
    public void playerAttacks(EncounterScreenController encounterControl,
            boolean enemyIntimidated) {
        final int originalPlayerHull = encounter.getPlayer().getShip().getHullStrength();
        final int originalOpponentHull = encounter.getOpponent().getHullStrength();

        encounterControl.playerAttacks();
        encounterControl.checkIfShipsDestroyed();
        encounterControl.checkIfEnemyEscapes();

        encounterControl.checkIfChangeState(originalPlayerHull, originalOpponentHull);
    }

    @Override
    public void playerFlees(EncounterScreenController encounterControl) {
        encounterControl.checkIfPlayerEscapes();
    }
}
