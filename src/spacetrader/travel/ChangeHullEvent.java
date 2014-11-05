/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import spacetrader.Player;

/**
 * Represents a random event that changes the player's hull
 *
 * @author Seth
 */
public class ChangeHullEvent extends RandomEvent {

    /**
     * Constructor for ChangeHullEvent
     *
     * @param player
     * @param message
     * @param quantityChange
     */
    public ChangeHullEvent(Player player, String message, int quantityChange) {
        super(player, message, quantityChange);

        if (quantityChange > 0) {
            if (player.getShip().getHullStrength() + quantityChange >= player.getShip().getMaxHullStrength()) {
                this.message += "\n\n" + "Your ship was completely repaired!";
            } else {
                this.message += "\n\n" + quantityChange + " repairs were done on your ship!";
            }
        } else {
            this.message += "\n\n" + "Your hull received " + Math.abs(quantityChange) + " damage.";
        }
    }

    /**
     * Sets hullstrength to hullstreth + quantity change
     */
    @Override
    public void doEvent() {
        player.getShip().setHullStrength(player.getShip().getHullStrength() + quantityChange);
    }
}
