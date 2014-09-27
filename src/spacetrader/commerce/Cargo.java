/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.commerce;

import java.util.HashMap;
import java.util.Map.Entry;
import spacetrader.ShipType;

/**
 * This is the Cargo class that holds TradeGoods
 * @author Naveena
 * @version 1.0
 *
 */
public class Cargo {
    
    private final HashMap<TradeGood, Integer> tradeGoods;
    private ShipType shipType;
    private int count;
    
    public Cargo(ShipType shipType) {
        this.tradeGoods = new HashMap<>();
        this.shipType = shipType;
        //add enum items to the map
    }
    
    /**
     * Adds an item to the cargo if not full
     * @param tradeGood the good to add
     * @param quantity the amount of good to add
     */
    public void addItem(TradeGood tradeGood, int quantity) {
        if (isFull()) {
            //TODO
        }
        int currentQuantity = getQuantityOfGood(tradeGood);
        int newQuantity = currentQuantity + quantity;
        this.tradeGoods.put(tradeGood, newQuantity);
        count += quantity;
    } 
    
    /**
     * Adds multiple items to the cargo if not full
     * @param newGoods map of goods and their quantities to add
     */
    public void addMultipleItems(HashMap<TradeGood, Integer> newGoods) {
        if (isFull()) {
            //TODO
        }
        for (Entry<TradeGood, Integer> item : newGoods.entrySet()) {
            addItem(item.getKey(), item.getValue());
        }
    }
    
    /**
     * Removes an item to the cargo if not empty/insufficient amount of goods
     * @param tradeGood the good to remove
     * @param quantity the amount of good to remove
     */
    public void removeItem(TradeGood tradeGood, int quantity) {
        int currentQuantity = getQuantityOfGood(tradeGood);
        int newQuantity = currentQuantity - quantity;
        if (newQuantity < 0) {
            //TODO
        }
        this.tradeGoods.put(tradeGood, newQuantity);
        count -= quantity;
    }
    
    /**
     * Removes multiple items from the cargo if not empty
     * @param goods map of goods and their quantities to remove
     */
    public void removeMultipleItems(HashMap<TradeGood, Integer> goods) {
        for (Entry<TradeGood, Integer> item : goods.entrySet()) {
            removeItem(item.getKey(), item.getValue());
        }
    }
    
    /**
     * Checks if there are any empty cargo slots left based on ship's capacity
     * @return true if cargo is full
     */
    public boolean isFull() {
        return (count == this.shipType.numCargoSlots());
    }
    
    /**
     * Sets type of ship which determines cargo capacity
     * @param shipType type of ship player has
     */
    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }
    
    /**
     * Gets quantity of good currently in cargo
     * @param tradeGood good that needs quantity check
     * @return quantity of good
     */
    public int getQuantityOfGood(TradeGood tradeGood) {
        return this.tradeGoods.get(tradeGood);
    }
}
