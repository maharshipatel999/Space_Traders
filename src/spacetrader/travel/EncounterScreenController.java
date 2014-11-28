/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import spacetrader.system.SceneController;

/**
 * Represents a controller for an encounter screen.
 *
 * @author Caleb
 */
public class EncounterScreenController extends SceneController {

    private static final String NO_WEAPONS_HRLP_MSG = 
        "You either are flying a ship without any weapon slots, so your only "
        + "option is to flee from fights, or you haven't bought any weapons yet."
        + " Sorry, no weapons, no attacking.";
    
    
    protected Encounter encounter;

    /**
     * Sets the encounter for this encounter controller.
     *
     * @param encounter
     */
    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }
    
    /**
     * Initiates Player attack sequence (Attack Pressed).
     *
     * @param e event that is being processed
     */
    @FXML
    protected void attackPressed(ActionEvent e) {
        if (encounter.getPlayer().getShip().getTotalWeaponStrength() <= 0) {
            mainControl.displayErrorMessage(null, "No Weapons", "You can't attack without weapons!");
            mainControl.goBackToWarpScreen();
        } else {
            mainControl.goToBattleScreen(encounter);
        }
    }
}
