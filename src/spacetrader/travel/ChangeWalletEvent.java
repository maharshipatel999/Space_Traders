/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import spacetrader.Player;

/**
 * Represents a random event that changes the player's wallet.
 *
 * @author Seth
 */
public class ChangeWalletEvent extends RandomEvent {

    /**
     * Creates a new ChangeWalletEvent.
     *
     * @param player the game's player
     * @param message the message that should be displayed to the player
     * @param quantityChange the change in wallet amount
     */
    public ChangeWalletEvent(Player player, String message, int quantityChange) {
        super(player, message, quantityChange);
        if (quantityChange > 0) {
            this.message += "\n\n₪" + quantityChange + " ware added to your wallet!";
        } else {
            this.message += "\n\n₪" + Math.abs(quantityChange) + " were removed from your wallet!";
        }
    }

    /**
     * Adds or removes money from Player's wallet depending on quantity change.
     */
    @Override
    public void doEvent() {
        if (quantityChange > 0) {
            player.addCredits(quantityChange);
        } else {
            player.removeCreditsForced(Math.abs(quantityChange));
        }
    }
}
