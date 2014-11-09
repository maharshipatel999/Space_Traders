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

    public PlayerShip(ShipType type) {
        super(type);
        
        hasEscapePod = false;
    }
    
    public void setEscapePod() {
        hasEscapePod = true;
    }
    
    public boolean hasEscapePod() {
        return hasEscapePod;
    }
}
