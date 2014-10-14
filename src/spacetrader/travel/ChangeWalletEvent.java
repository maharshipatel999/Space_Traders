/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.travel;

import spacetrader.Player;

/**
 *
 * @author Seth
 */
public class ChangeWalletEvent extends RandomEvent {
    public ChangeWalletEvent(Player player, String message, int quantityChange) {
    super(player, message, quantityChange);
}
    // FIX: MIGHT THROW EXCEPTION
    
    @Override
    public void doEvent() {
        player.getWallet().setCredits(player.getWallet().getCredits() + quantityChange);
        }
    }

