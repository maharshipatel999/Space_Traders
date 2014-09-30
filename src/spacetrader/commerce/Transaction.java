/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.commerce;

import java.util.HashMap;
import java.util.Map;
import spacetrader.exceptions.CargoIsFullException;
import spacetrader.exceptions.DepletedInventoryException;
import spacetrader.exceptions.InsufficientFundsException;

/**
 * Used by MarketScreenController to keep track of current transactions.
 * 
 * @author Caleb Stokols
 */
public class Transaction {
    
    private final Market market;
    private final Cargo playerCargo;
    private final int initialCredits;
    private Map<TradeGood, Integer> purchases;
    private Map<TradeGood, Integer> sales;
    private int totalCost;
    private int totalRevenue;
    private int remainingBalance;
    
    public Transaction(Market market, Cargo playerCargo, int initialCredits) {
        this.market = market;
        this.playerCargo = playerCargo;
        this.initialCredits = initialCredits;
        this.remainingBalance = initialCredits;
        purchases = new HashMap<>();
        sales = new HashMap<>();
    }
    
    /**
     * Updates the quantity the player would like to purchase of a specified TradeGood.
     * @param good the TradeGood which is being changed
     * @param newQuantity the new quantity of that TradeGood
     */
    public void changeBuyQuantity(TradeGood good, int newQuantity) throws DepletedInventoryException {
        //Check if quantity is negative
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Cannot buy a negative quantity of goods");
        }
        //Find out relative change in cost from this purchase
        int currQuantity = purchases.containsKey(good) ? purchases.get(good) : 0;
        int changeInQuantity = newQuantity - currQuantity;
        int purchaseCost = changeInQuantity * market.getBuyPrices().get(good);
        //Check if player has a large enough remaining balance
        if (remainingBalance < purchaseCost) {
            throw new InsufficientFundsException("Player does not have enough credits to make this purchase.");
        }
        //Check to make sure market is not selling more than its stock
        if (newQuantity > market.getStock().getQuantity(good)) {
            throw new DepletedInventoryException("Cannot sell more goods than the market owns");
        }
        if (playerCargo.getMaxCapacity() - playerCargo.getCount() < newQuantity) {
            throw new CargoIsFullException("Cannot purchase more goods than cargo can carry");
        }
        purchases.put(good, newQuantity);
        updateTotalCost();
    }
    
     /**
     * Updates the quantity the player would like to sell of a specified TradeGood.
     * @param good the TradeGood which is being changed
     * @param newQuantity the new quantity of that TradeGood
     */
    public void changeSellQuantity(TradeGood good, int newQuantity) throws DepletedInventoryException {
        //Check if quantity is negative
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Cannot sell a negative quantity of goods");
        }
        //Check to make sure player is not selling more than he owns.
        if (newQuantity > playerCargo.getQuantity(good)) {
            throw new DepletedInventoryException("Cannot sell more goods than the player owns");
        }
        sales.put(good, newQuantity);
        updateTotalRevenue();
    }
    
    public void complete() {
        
        market.getStock().removeMultipleItems(purchases);
        market.getStock().addMultipleItems(sales);
        
        playerCargo.removeMultipleItems(sales);
        playerCargo.addMultipleItems(purchases);
    }
    
    public int getBuyQuantityOfGood(TradeGood good) {
        return purchases.containsKey(good) ? purchases.get(good) : 0;
    }
    
    public int getSellQuantityOfGood(TradeGood good) {
        return sales.containsKey(good) ? sales.get(good) : 0;
    }
    
    private void updateTotalCost() {
        totalCost = 0;
        for (TradeGood good : purchases.keySet()) {
            totalCost += purchases.get(good) * market.getBuyPrices().get(good);
        }
        updateBalance();
    }
    
    private void updateTotalRevenue() {
        totalRevenue = 0;
        for (TradeGood good : sales.keySet()) {
            totalRevenue += sales.get(good) * market.getSellPrices().get(good);
        }
        updateBalance();
    }
    
    private void updateBalance() {
        remainingBalance = initialCredits - totalCost + totalRevenue;
    }

    /**
     * Determines the total cost of all the player's purchases.
     * @return total cost of goods purchased
     */
    public int getTotalCost() {
        return totalCost;
    }

    /**
     * Determines the total revenue the player will make after selling his goods.
     * @return total revenue of goods sold
     */
    public int getTotalRevenue() {
        return totalRevenue;
    }

    /**
     * Determines what the player's remaining balance will be after all 
     * transactions have been made.
     * @return the player's remaining balance
     */
    public int getRemainingBalance() {
        return remainingBalance;
    }
}
