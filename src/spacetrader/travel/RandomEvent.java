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
public abstract class RandomEvent {
    private String message;
    protected int quantityChange; 
    protected Player player;
    
    public RandomEvent(Player player, String message, int quantityChange) {
        this.player = player;
        this.message = message;
        this.quantityChange = quantityChange;
    }
      public String getMessage() {
        return message;
    }
    public abstract void doEvent();
}
