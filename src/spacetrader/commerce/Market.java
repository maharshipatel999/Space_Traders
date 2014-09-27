/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.commerce;

import java.util.HashMap;
import java.util.Map;
import spacetrader.Planet;

/**
 *
 * @author Caleb
 */
public class Market {
    
    private Planet planet;
    private Map<TradeGood, Integer> prices;
    private Cargo stock;
    
    public Market(Planet planet) {
        this.planet = planet;
        
        setAllPrices();
        setMarketStock();
    }
    
    /**
     * Sets the Map prices so that each TradeGood maps to its price on this planet.
     * If a good is not sold on this planet, its price is zero.
     */
    private void setAllPrices() {
        prices = new HashMap<>();
        for (TradeGood good : TradeGood.values()) {
            prices.put(good, calculatePrice(good));
        }
    }
    
    /**
     * Calculates the price of a TradeGood on the current planet using
     * the TradeGood enum attributes.
     * @param a TradeGood to sell
     * @return the price of the TradeGood or 0 if it is not sold on this planet
     */
    private int calculatePrice(TradeGood good) {
        return 1; //TODO calculate price of good
        //(the base price) + (incPerLevel * (Planet Tech Level - minProduceLevel)) + (variance).
    }
    
    /**
     * Creates a stock of all the goods sold on this Planet.
     * If the price of a good is greater than zero, it will be added
     * to the market's stock.
     */
    private void setMarketStock() {
        for (TradeGood good : TradeGood.values()) {
            if (prices.get(good) > 0) {
                stock.addItem(good, 10); //should not be 10
            }
        }
    }
    
    /**
     * Gets the prices of all the TradeGoods as a Map.
     * @return the prices of goods on this planet.
     */
    public Map<TradeGood, Integer> getPrices() {
        return prices;
    }
    
    /**
     * Gets the current stock of the Market.
     * @return the market's stock
     */
    public Cargo getStock() {
        return stock;
    }
    
    
}
