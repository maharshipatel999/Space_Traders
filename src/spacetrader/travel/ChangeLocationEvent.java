/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.travel;

import spacetrader.planets.Planet;
import spacetrader.Player;
import spacetrader.system.MainController;

/**
 * Represents a random event that changes the player's location
 * @author Seth
 */
public class ChangeLocationEvent extends RandomEvent {
    
    private Planet planet;
    private MainController mainControl;
    
    /**
     * constructor for ChangeLocationEvent 
     * @param player
     * @param message
     * @param quantityChange
     * @param planet 
     * @param mainControl 
     */
    public ChangeLocationEvent(Player player, String message, int quantityChange, Planet planet, MainController mainControl) {
        super(player, message, quantityChange);
        this.message += "\n\nYou are now at the planet " + planet.getName() + ".";
        this.planet = planet;
        this.mainControl = mainControl;
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
        mainControl.goToHomeScreen(planet);
    }
}
