/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.travel;

import spacetrader.Planet;
import spacetrader.Player;

/**
 *
 * @author Seth
 */
public class ChangeLocationEvent extends RandomEvent {
    private Planet planet;
    /**
     * constructor for ChangeLocationEvent 
     * @param player
     * @param message
     * @param quantityChange
     * @param planet 
     */
    public ChangeLocationEvent(Player player, String message, int quantityChange, Planet planet) {
        super(player, message, quantityChange);
        this.planet = planet;
    }
    /**
     * override doEvent method in Random event
     * sets player's location to new planet
     * changes hull strength
     */
    @Override
    public void doEvent() {
        player.getShip().setHullStrength(player.getShip().getHullStrength() + quantityChange);
        player.setLocation(planet);
    }
}
