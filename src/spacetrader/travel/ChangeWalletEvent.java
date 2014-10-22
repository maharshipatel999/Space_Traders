/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.travel;

import spacetrader.Player;

/**
 * Represents a random event that changes the player's wallet.
 * @author Seth
 */
public class ChangeWalletEvent extends RandomEvent {
    
    public ChangeWalletEvent(Player player, String msg, int quantityChange) {
        super(player, msg, quantityChange);
        if (quantityChange > 0) {
            this.message += "\n\n₪" + quantityChange + " was added to your wallet!";
        } else {
            this.message += "\n\n₪" + Math.abs(quantityChange) + " was removed from your wallet!";
        }
    }   
    // FIX: MIGHT THROW EXCEPTION
    
    @Override
    public void doEvent() {
        if (quantityChange > 0) {
            player.getWallet().add(quantityChange);
        } else {
            player.getWallet().removeForcefully(Math.abs(quantityChange));
        }
    }
}

