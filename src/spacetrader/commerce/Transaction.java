/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.commerce;

import spacetrader.exceptions.CargoIsFullException;
import spacetrader.exceptions.DepletedInventoryException;
import spacetrader.exceptions.InsufficientFundsException;
import spacetrader.exceptions.NegativeQuantityException;

/**
 * Used by MarketScreenController to keep track of current transactions.
 * 
 * @author Caleb Stokols
 */
public class Transaction {
    
    private final Market market;
    private final Cargo playerCargo;
    private final Wallet playerMoney;
    private Cargo purchases;
    private Cargo sales;
    private int totalCost;
    private int totalRevenue;
    private int remainingBalance;
    
    /**
     * Creates a new Transaction. A transaction is a controller class used to help the MarketScreenController.
     * The transaction keeps track of all current purchases, sales, and player funds.
     * @param market a planet's market
     * @param playerCargo the player's cargo
     * @param playerMoney the player's wallet
     */
    public Transaction(Market market, Cargo playerCargo, Wallet playerMoney) {
        this.market = market;
        this.playerCargo = playerCargo;
        this.playerMoney = playerMoney;
        this.remainingBalance = playerMoney.getCredits();
        purchases = new Cargo(playerCargo.getMaxCapacity());
        sales = new Cargo(playerCargo.getMaxCapacity());
    }
    
    /**
     * Updates the quantity the player would like to purchase of a specified TradeGood.
     * @param good the TradeGood which is being changed
     * @param newQuantity the new quantity of that TradeGood
     */
    public void changeBuyQuantity(TradeGood good, int newQuantity) throws DepletedInventoryException {
        //Check if quantity is negative
        if (newQuantity < 0) {
            throw new NegativeQuantityException("Cannot buy a negative quantity of goods");
        }
        //Check to make sure market is not selling more than its stock
        if (newQuantity > market.getStock().getQuantity(good)) {
            throw new DepletedInventoryException("Cannot sell more goods than the market owns");
        }
        int quantityChange = newQuantity - purchases.getQuantity(good);
        int newTotalCount = playerCargo.getCount() + purchases.getCount() + quantityChange;
        
         //Check if there is enough cargo space in the player's spaceship
        if (newTotalCount > playerCargo.getMaxCapacity()) {
            throw new CargoIsFullException("Cannot purchase more goods than cargo can carry");
        }
        int purchaseCost = quantityChange * market.getBuyPrice(good);
        
        //Check if player has a large enough remaining balance
        if (remainingBalance < purchaseCost) {
            throw new InsufficientFundsException("Player does not have enough credits to make this purchase.");
        }
        purchases.clearItem(good);
        purchases.addItem(good, newQuantity, market.getBuyPrice(good));
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
            throw new NegativeQuantityException("Cannot sell a negative quantity of goods");
        }
        //Check to make sure player is not selling more than he owns.
        if (newQuantity > playerCargo.getQuantity(good)) {
            throw new DepletedInventoryException("Cannot sell more goods than the player owns");
        }
        sales.clearItem(good);
        sales.addItem(good, newQuantity, market.getSellPrice(good));
        updateTotalRevenue();
    }
    
    public void complete() {
        market.getStock().removeCargoContents(purchases);
        market.getStock().addCargoContents(sales);
        try {
            playerCargo.removeCargoContents(sales); //we should make it so you cannot buy and sell the same good
            playerCargo.addCargoContents(purchases);
        } catch (DepletedInventoryException e) {
            playerCargo.addCargoContents(purchases);
            playerCargo.removeCargoContents(sales);
        }
        playerMoney.add(totalRevenue);
        playerMoney.remove(totalCost);
    }
    
    /**
     * Gets the purchased quantity of the specified good.
     * @param good the TradeGood being bought
     * @return quantity bought of good
     */
    public int getQuantityBought(TradeGood good) {
        return purchases.getQuantity(good);
    }
    
    /**
     * Gets the sold quantity of the specified good.
     * @param good the TradeGood being sold
     * @return quantity sold of good
     */
    public int getQuantitySold(TradeGood good) {
        return sales.getQuantity(good);
    }
    
    private void updateTotalCost() {
        totalCost = 0;
        for (TradeGood good : TradeGood.values()) {
            totalCost += purchases.getQuantity(good) * market.getBuyPrice(good);
        }
        updateBalance();
    }
    
    private void updateTotalRevenue() {
        totalRevenue = 0;
        for (TradeGood good : TradeGood.values()) {
            totalRevenue += sales.getQuantity(good) * market.getSellPrice(good);
        }
        updateBalance();
    }
    
    private void updateBalance() {
        remainingBalance = playerMoney.getCredits() - totalCost + totalRevenue;
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
