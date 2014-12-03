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
 * Represents a random event that changes the player's location.
 *
 * @author Seth
 */
public class ChangeLocationEvent extends RandomEvent {

    private Planet planet;
    private MainController mainControl;

    /**
     * Create a new ChangeLocationEvent.
     *
     * @param player the game's player
     * @param message the message that should be displayed to the user
     * @param quantityChange the change in hull strength
     * @param planet the planet the player should be transported to
     * @param mainControl the game's main controller
     */
    public ChangeLocationEvent(Player player, String message, int quantityChange, Planet planet, MainController mainControl) {
        super(player, message, quantityChange);
        this.message += "\n\nYou are now at the planet " + planet.getName() + ".";
        this.planet = planet;
        this.mainControl = mainControl;
    }

    /**
     * Sets player's location to new planet and changes hull strength.
     */
    @Override
    public void doEvent() {
        player.getShip().setHullStrength(player.getShip().getHullStrength() + quantityChange);
        mainControl.specialArrivalAtPlanet(planet);
    }
}
