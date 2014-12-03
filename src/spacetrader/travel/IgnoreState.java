/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.travel;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import static spacetrader.travel.EncounterState.addButton;

/**
 *
 * @author Caleb
 */
public class IgnoreState extends EncounterState {
    
    public IgnoreState(Encounter encounter) {
        super(encounter);
    }

    @Override
    public void setButtons(EncounterScreenController sceneControl, HBox box) {
        box.getChildren().clear();
        
        //Add attack button
        Button attackButton = addButton("Attack", box);
        attackButton.setOnAction(event -> sceneControl.attackPressed());

        //Add ignore button
        Button ignoreButton = addButton("Ignore", box);
        ignoreButton.setOnAction(event -> sceneControl.ignorePressed());
        ignoreButton.setDefaultButton(true);
    }
    
    @Override
    public String getNextActionText() {
        if (encounter.playerIsCloaked()) {
            return "It doesn't notice you.";
        } else {
            return "It ignores you.";
        }
    }
    
    @Override
    public void playerAttacks(EncounterScreenController encounterControl, boolean enemyIntimidated) {
        //Change encounter state to FleeState or AttackState and delegate to that new state.
        if (encounter.opponentIntimidated()) {
            encounterControl.setState(new FleeState(encounter));
        } else {
            encounterControl.setState(new AttackState(encounter));
        }
        encounter.getState().playerAttacks(encounterControl, enemyIntimidated);
    }
}
