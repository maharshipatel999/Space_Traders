/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.ships;

/**
 *
 * @author Caleb
 */
public class PlayerShip extends SpaceShip {

    private boolean hasEscapePod;

    /**
     * Creates a PlayerShip
     * @param type The type of ship that will be the Player's ship
     */
    public PlayerShip(ShipType type) {
        super(type);

        hasEscapePod = false;
    }

    /**
     * Sets the Player's ship to have an escape pod
     */
    public void setEscapePod() {
        hasEscapePod = true;
    }
    
    /**
     * Checks if the Player's ship has an escape pod
     * @return the value of hasEscapePod
     */
    public boolean hasEscapePod() {
        return hasEscapePod;
    }
}
