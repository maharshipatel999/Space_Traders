/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import spacetrader.Player;

/**
 * Abstract class that instantiates a random event
 *
 * @author Seth
 */
public abstract class RandomEvent {

    protected String message;
    protected int quantityChange;
    protected Player player;

    /**
     * Constructor for instantiating a new Random Event
     *
     * @param player Player in game
     * @param message Specific random event message
     * @param quantityChange Quantity of change that occurs to an aspect of the
     * Player
     */
    public RandomEvent(Player player, String message, int quantityChange) {
        this.player = player;
        this.message = message;
        this.quantityChange = quantityChange;
    }

    /**
     * get message value
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Carry out Random Event
     */
    public abstract void doEvent();
}
