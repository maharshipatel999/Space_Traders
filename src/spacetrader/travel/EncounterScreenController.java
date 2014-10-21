/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.travel;

import spacetrader.system.SceneController;

/**
 *
 * @author Caleb
 */
public class EncounterScreenController extends SceneController {
    
    protected Encounter encounter;
    
    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }
}
