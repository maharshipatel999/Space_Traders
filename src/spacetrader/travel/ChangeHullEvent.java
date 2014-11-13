/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import spacetrader.Player;

/**
 * Represents a random event that changes the player's hull.
 *
 * @author Seth
 */
public class ChangeHullEvent extends RandomEvent {

    /**
     * Constructor for ChangeHullEvent
     *
     * @param player the game's player
     * @param message the message that should be displayed to the user
     * @param quantityChange the change in hull strength
     */
    public ChangeHullEvent(Player player, String message, int quantityChange) {
        super(player, message, quantityChange);
        this.message += "\n\n";
        if (quantityChange > 0) {
            if (player.getShip().getHullStrength() + quantityChange >= player.getShip().getMaxHullStrength()) {
                this.quantityChange = player.getShip().getMaxHullStrength() - player.getShip().getHullStrength();
                this.message += "Your ship was completely repaired!";
            } else {
                this.message += quantityChange + " repairs were done on your ship!";
            }
        } else {
            this.message += "Your hull received " + Math.abs(quantityChange) + " damage.";
        }
    }

    /**
     * Sets hullstrength to hullstrength + quantity change.
     */
    @Override
    public void doEvent() {
        player.getShip().setHullStrength(player.getShip().getHullStrength() + quantityChange);
    }
}
