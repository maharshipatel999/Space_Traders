/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class InspectionState extends EncounterState {

    public InspectionState(PoliceEncounter encounter) {
        super(encounter);
    }

    @Override
    public void setButtons(EncounterScreenController sceneControl, HBox box) {
        PoliceEncounterController policeController = (PoliceEncounterController) sceneControl;
        box.getChildren().clear();
        
        //Add submit button
        Button submitButton = addButton("Submit", box);
        submitButton.setOnAction(event -> policeController.submitPressed());
        submitButton.setDefaultButton(true);

        //Add bribe button
        Button bribeButton = addButton("Bribe", box);
        bribeButton.setOnAction(event -> policeController.bribePressed());
        
        //Add attack button
        Button attackButton = addButton("Attack", box);
        attackButton.setOnAction(event -> sceneControl.attackPressed());

        //Add flee button
        Button fleeButton = addButton("Flee", box);
        fleeButton.setOnAction(event -> sceneControl.fleePressed());
    }
    
    @Override
    public String getNextActionText() {
        return "The police summon you to submit to an inspection.";
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
    
    @Override
    public void playerFlees(EncounterScreenController encounterControl) {
        ((PoliceEncounter) encounter).updateRecordFleeFromInspection();
        
        encounterControl.setState(new AttackState(encounter));
        encounter.getState().playerAttacks(encounterControl, false);
    }
}
