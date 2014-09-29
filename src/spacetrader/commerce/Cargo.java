/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.commerce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import spacetrader.exceptions.CargoIsFullException;

/**
 * This is the Cargo class that holds TradeGoods
 * @author Naveena
 * @version 1.0
 *
 */
public class Cargo {
    
    private final Map<TradeGood, Integer> tradeGoods;
    private int numSlots;
    private int count;
    
    public Cargo(int numSlots) {
        this.numSlots = numSlots;
        this.tradeGoods = new HashMap<>();
        for (TradeGood good : TradeGood.values()) {
            tradeGoods.put(good, 0);
        }
    }
    
    /**
     * Adds an item to the cargo if not full
     * @param tradeGood the good to add
     * @param quantity the amount of good to add
     */
    public void addItem(TradeGood tradeGood, int quantity) {
        if (isFull()) {
            throw new CargoIsFullException();
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
    public void addMultipleItems(Map<TradeGood, Integer> newGoods) {
        if (isFull()) {
            throw new CargoIsFullException();
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
    public void removeMultipleItems(Map<TradeGood, Integer> goods) {
        for (Entry<TradeGood, Integer> item : goods.entrySet()) {
            removeItem(item.getKey(), item.getValue());
        }
    }
    
    /**
     * Increases the number of available slots.
     */
    public void addSlot() {
        numSlots++;
    }
    
    /**
     * Decreases the number of available slots
     */
    public void removeSlot() {
        numSlots--;
    }
    /**
     * Checks if there are any empty cargo slots left based on ship's capacity
     * @return true if cargo is full
     */
    public boolean isFull() {
        return (count == numSlots);
    }
    
    public int availableSlots() {
        return numSlots - count;
    }
    
    public int slotsFilled() {
        return count;
    }
    
    /**
     * Gets quantity of good currently in cargo
     * @param tradeGood good that needs quantity check
     * @return quantity of good
     */
    public int getQuantityOfGood(TradeGood tradeGood) {
        return this.tradeGoods.get(tradeGood);
    }
    
    /**
     * 
     * @return an ordered list of all the TradeGoods this cargo holds
     */
    public List<TradeGood> getGoodList() {
        ArrayList<TradeGood> goods = new ArrayList<>();
        for (TradeGood good : TradeGood.values()) {
            if (tradeGoods.get(good) > 0) {
                goods.add(good);
            }
        }
        return goods;
    }
}
