/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.travel;

import spacetrader.Player;
import spacetrader.PoliticalSystem;
import spacetrader.ships.ShipType;
import spacetrader.ships.SpaceShip;

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
     * Sets the opponent space ship of this encounter
     * @param opponent the space ship who the player is encountering
     */
    public void setOpponent(SpaceShip opponent) {
        this.opponent = opponent;
    }
    
    /**
     * Gets the opponent of this encounter
     * @return the encounter's opponent
     */
    public SpaceShip getOpponent() {
        return opponent;
    }
    
    /**
     * Gets the fxml scene associated with this encounter. For instance, if this
     * is a PoliceEncounter, it will return the "PoliceEncounterScreen.fxml".
     * @return fxml scene of this encounter's associated view
     */
    public String getFXMLScene() {
        return encounterScene;
    }
    
    /**
     * Determines if this encounter allows the opponent to have the specified 
     * type of space ship.
     * @param <error>
     * @return true if its legal
     */
    public abstract boolean isLegalShipType(ShipType type, PoliticalSystem system);
}
