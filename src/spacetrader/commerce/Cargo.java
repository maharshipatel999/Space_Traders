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
import spacetrader.exceptions.CargoIsFullException;
import spacetrader.exceptions.DepletedInventoryException;

/**
 * This class represents a holder for goods. It stores a mapping from each type
 * of TradeGood to its quantity. Cargo is the only way a quantity of TradeGoods
 * should ever be held.
 *
 * @author Naveena, Caleb
 * @version 1.5
 *
 */
public class Cargo implements Serializable {

    private final Map<TradeGood, Integer> tradeGoods;
    private final Map<TradeGood, Integer> costOfGoods;
    private int maxCapacity;
    private int count;

    /**
     * Creates a new Cargo with a specified number of available slots
     *
     * @param maxCapacity the initial max number of slots
     */
    public Cargo(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.tradeGoods = new HashMap<>();
        this.costOfGoods = new HashMap<>();
        for (TradeGood good : TradeGood.values()) {
            tradeGoods.put(good, 0);
            costOfGoods.put(good, 0);
        }
    }

    /**
     * Adds an item to the cargo. Adds the cost of that good to this TradeGood's
     * total cost If there is not enough space, an exception is thrown.
     *
     * @param tradeGood the good to add
     * @param quantity the amount of good to add
     * @param cost
     */
    public void addItem(TradeGood tradeGood, int quantity, int cost) {
        if (count + quantity > maxCapacity) {
            throw new CargoIsFullException();
        }
        int currQuantity = this.tradeGoods.get(tradeGood);
        this.tradeGoods.put(tradeGood, currQuantity + quantity);
        this.count += quantity;

        int newCost = this.costOfGoods.get(tradeGood) + (quantity * cost);
        this.costOfGoods.put(tradeGood, newCost);
    }

    /**
     * Adds the contents of another Cargo to the contents of this Cargo. If
     * there is not space for every addition, an exception is thrown and no
     * goods are added. otherCargo's contents are left unchanged
     *
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
            int newCost = this.costOfGoods.get(good) + otherCargo.costOfGoods.get(good);
            this.costOfGoods.put(good, newCost);
        }
        this.count += otherCargo.count;
    }

    /**
     * Removes an item from the cargo. Throws an exception if more goods are
     * removed than currently exist.
     *
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
        //adjust cost of this trade good
        if (quantity != 0) {
            int costPerGood = this.costOfGoods.get(tradeGood) / quantity;
            int newCost = this.costOfGoods.get(tradeGood) - (quantity * costPerGood);
            this.costOfGoods.put(tradeGood, newCost);
        }
        if (tradeGoods.get(tradeGood) == 0) {
            costOfGoods.put(tradeGood, 0);
        }
    }

    /**
     * Removes the contents of another Cargo from the contents of this Cargo. If
     * more goods are removed than currently exist, an exception is thrown and
     * no goods are removed. otherCargo's contents are left unchanged
     *
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
            //adjust cost of this trade good
            if (quantity > 0) {
                int costPerGood = this.costOfGoods.get(good) / quantity;
                int newCost = this.costOfGoods.get(good) - (quantity * costPerGood);
                this.costOfGoods.put(good, newCost);
            }
            if (tradeGoods.get(good) == 0) {
                costOfGoods.put(good, 0);
            }
        }
        this.count -= otherCargo.count;
    }

    /**
     * Removes all of the specified TradeGood type from the Cargo.
     *
     * @param good the good to remove
     */
    public void clearItem(TradeGood good) {
        this.count -= this.tradeGoods.get(good);
        this.tradeGoods.put(good, 0);
        this.costOfGoods.put(good, 0);
    }

    /**
     * Clears the entire Cargo by removing every good and setting count to 0.
     */
    public void clearAllItems() {
        for (TradeGood good : TradeGood.values()) {
            clearItem(good);
        }
    }

    /**
     * Increases the number of available slots.
     */
    public void increaseCapacity() {
        maxCapacity += 5;
    }

    /**
     * Decreases the number of available slots.
     */
    public void decreaseCapacity() {
        maxCapacity -= 5;
    }

    //REMOVE THIS
    /**
     * Checks if there are any empty cargo slots left based on ship's capacity
     *
     * @return true if cargo is full
     */
    public boolean isFull() {
        return (count == maxCapacity);
    }

    /**
     * Determines the number of cargo slots that are still open.
     *
     * @return the remaining unfilled slots
     */
    public int getRemainingCapacity() {
        return maxCapacity - count;
    }

    /**
     * Gets the total number of good stored in this Cargo
     *
     * @return the total count
     */
    public int getCount() {
        return count;
    }

    /**
     * Gets the max number of goods that can be held in this cargo.
     *
     * @return the max capacity
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Determines the quantity of goods stored in this Cargo for a specific type
     * of good.
     *
     * @param tradeGood good who's quantity is being checked
     * @return quantity of good
     */
    public int getQuantity(TradeGood tradeGood) {
        return this.tradeGoods.get(tradeGood);
    }

    /**
     * Determines the total worth of a kind of good stored in this Cargo based
     * on how much it was bought for.
     *
     * @param tradeGood good who's cost is being checked
     * @return cost of good
     */
    public int getCostOfGood(TradeGood tradeGood) {
        return this.costOfGoods.get(tradeGood);
    }

    /**
     * Determines the amount of money that was spent purchasing all the goods in
     * this Cargo. ie. the Cargo's total worth
     *
     * @return the cost of all goods in this Cargo
     */
    public int getCostOfAllGoods() {
        int worth = 0;
        for (TradeGood good : TradeGood.values()) {
            worth += costOfGoods.get(good);
        }
        return worth;
    }

    /**
     * Creates a list of the TradeGood types that this Cargo currently holds.
     *
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

    /**
     * Converts Cargo into a String representation
     * @return String representation of the Cargo
     */
    @Override
    public String toString() {
        String toString = "-Cargo Contents- (" + count + "/" + maxCapacity + ")\n";
        int goodCount = 0;
        for (TradeGood good : TradeGood.values()) {
            toString += good + ": " + tradeGoods.get(good) + ", ";
            goodCount++;
            if (goodCount == 4 || goodCount == 7) {
                toString += "\n";
            }
        }
        return toString;
    }
}
