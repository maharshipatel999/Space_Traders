/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.commerce;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import spacetrader.exceptions.CargoIsFullException;
import spacetrader.exceptions.DepletedInventoryException;

/**
 * This class represents a holder for goods.
 * It stores a mapping from each type of TradeGood to its quantity.
 * Cargo is the only way a quantity of TradeGoods should ever be held.
 * @author Naveena, Caleb
 * @version 1.5
 *
 */
public class Cargo implements Serializable {
    
    private final Map<TradeGood, Integer> tradeGoods;
    private int maxCapacity;
    private int count;
    
    /**
     * Creates a new Cargo with a specified number of available slots
     * @param maxCapacity the initial max number of slots
     */
    public Cargo(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.tradeGoods = new HashMap<>();
        for (TradeGood good : TradeGood.values()) {
            tradeGoods.put(good, 0);
        }
    }
    
    /**
     * Adds an item to the cargo.
     * If there is not enough space, an exception is thrown.
     * @param tradeGood the good to add
     * @param quantity the amount of good to add
     */
    public void addItem(TradeGood tradeGood, int quantity) {
        if (count + quantity > maxCapacity) {
            throw new CargoIsFullException();
        }
        int currQuantity = this.tradeGoods.get(tradeGood);
        this.tradeGoods.put(tradeGood, currQuantity + quantity);
        this.count += quantity;
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
     * Adds the contents of another Cargo to the contents of this Cargo.
     * If there is not space for every addition, an exception
     * is thrown and no goods are added.
     * otherCargo's contents are left unchanged
     * @param otherCargo the cargo who's contents shall be added
     */
    public void addCargoContents(Cargo otherCargo) {
        if (this.count + otherCargo.count > maxCapacity) {
            throw new CargoIsFullException();
        }
        for (TradeGood good : TradeGood.values()) {
            int currQuantity = this.tradeGoods.get(good);
            int quantity = otherCargo.tradeGoods.get(good);
            this.tradeGoods.put(good, currQuantity + quantity);
        }
        this.count += otherCargo.count;
    }
    
    /**
     * Removes an item from the cargo.
     * Throws an exception if more goods are removed than currently exist.
     * @param tradeGood the good to remove
     * @param quantity the amount of good to remove
     */
    public void removeItem(TradeGood tradeGood, int quantity) {
        int currQuantity = this.tradeGoods.get(tradeGood);
        if (quantity > currQuantity) {
            throw new DepletedInventoryException();
        }
        this.tradeGoods.put(tradeGood, currQuantity - quantity);
        this.count -= quantity;
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
     * Removes the contents of another Cargo from the contents of this Cargo.
     * If more goods are removed than currently exist, an exception
     * is thrown and no goods are removed.
     * otherCargo's contents are left unchanged
     * @param otherCargo the cargo who's contents shall be removed
     */
    public void removeCargoContents(Cargo otherCargo) {
        for (TradeGood good : TradeGood.values()) {
            if (otherCargo.tradeGoods.get(good) > this.tradeGoods.get(good)) {
                throw new DepletedInventoryException();
            }
        }
        for (TradeGood good : TradeGood.values()) {
            int currQuantity = this.tradeGoods.get(good);
            int quantity = otherCargo.tradeGoods.get(good);
            this.tradeGoods.put(good, currQuantity - quantity);
        }
        this.count -= otherCargo.count;
    }
    
    /**
     * Removes all of the specified TradeGood type from the Cargo.
     * @param good the good to remove
     */
    public void clearItem(TradeGood good) {
        this.count -= this.tradeGoods.get(good);
        this.tradeGoods.put(good, 0);
    }
    
    /**
     * Clears the entire Cargo by removing every good and setting count to 0.
     */
    public void clearAllItems() {
        for (TradeGood good : TradeGood.values()) {
            tradeGoods.put(good, 0);
        }
        this.count = 0;
    }
    
    /**
     * Increases the number of available slots.
     */
    public void increaseCapacity() {
        maxCapacity++;
    }
    
    /**
     * Decreases the number of available slots.
     */
    public void decreaseCapacity() {
        maxCapacity--;
    }
    
    //REMOVE THIS
    /**
     * Checks if there are any empty cargo slots left based on ship's capacity
     * @return true if cargo is full
     */
    public boolean isFull() {
        return (count == maxCapacity);
    }
    
    /**
     * Determines the number of cargo slots that are still open.
     * @return the remaining unfilled slots
     */
    public int getRemainingCapacity() {
        return maxCapacity - count;
    }
    
    /**
     * Gets the total number of good stored in this Cargo
     * @return the total count
     */
    public int getCount() {
        return count;
    }
    
    /**
     * Gets the max number of goods that can be held in this cargo.
     * @return the max capacity
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }
    
    /**
     * Determines the quantity of goods stored in this Cargo for a specific
     * type of good.
     * @param tradeGood good who's quantity is being checked
     * @return quantity of good
     */
    public int getQuantity(TradeGood tradeGood) {
        return this.tradeGoods.get(tradeGood);
    }
    
    /**
     * Creates a list of the TradeGood types that this Cargo currently holds.
     * @return an ordered list of all the TradeGoods this cargo holds
     */
    public List<TradeGood> getTradeGoods() {
        ArrayList<TradeGood> goods = new ArrayList<>();
        for (TradeGood good : TradeGood.values()) {
            if (this.tradeGoods.get(good) > 0) {
                goods.add(good);
            }
        }
        return goods;
    }
}
