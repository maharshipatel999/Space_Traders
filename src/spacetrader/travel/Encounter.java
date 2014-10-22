/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.travel;

import spacetrader.Player;
import spacetrader.SpaceShip;

/**
 * Represents an abstract Encounter.
 * @author Caleb
 */
public abstract class Encounter {
    
    private Player player;
    private SpaceShip opponent;
    private String encounterScene;
    
    /**
     * Creates a new Encounter. Encounters have a player, and an fxmlscene.
     * @param player the player of the game
     * @param fxmlScene the scene which should be displayed with this encounter.
     */
    public Encounter(Player player, String fxmlScene) {
        this.player = player;
        this.encounterScene = fxmlScene;
    }

    /**
     * Gets the player having this encounter.
     * @return the encounter's player
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Gets the fxml scene associated with this encounter. For instance, if this
     * is a PoliceEncounter, it will return the "PoliceEncounterScreen.fxml".
     * @return fxml scene of this encounter's associated view
     */
    public String getFXMLScene() {
        return encounterScene;
    }
}
