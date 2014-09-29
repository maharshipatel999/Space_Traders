/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.commerce;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import spacetrader.Planet;

/**
 *
 * @author Caleb
 */
public class Market {
    
    private Planet planet;
    private Map<TradeGood, Integer> sellPrices;
    private Map<TradeGood, Integer> buyPrices;
    private Cargo stock;
    
    public Market(Planet planet) {
        this.planet = planet;
        stock = new Cargo(1000);
        
        setAllPrices();
        setMarketStock();
    }
    
    /**
     * Sets the Map sell/buy prices so that each TradeGood maps to its sell/buy price on this planet.
     * If a good is not sold on this planet, its price is -1.
     */
    private void setAllPrices() {
        sellPrices = new HashMap<>();
        buyPrices = new HashMap<>();
        for (TradeGood good : TradeGood.values()) {
            sellPrices.put(good, calculatePrice(good));
            buyPrices.put(good, calculatePrice(good));
        }
    }
    
    /**
     * Calculates the price of a TradeGood on the current planet using
     * the TradeGood enum attributes.
     * @param a TradeGood to sell
     * @return the price of the TradeGood or -1 if it is not sold on this planet
     */
    private int calculatePrice(TradeGood good) {
        Random rand = new Random();
        //(the base price) + (incPerLevel * (Planet Tech Level - minProduceLevel)) + (variance).
        
        int price = good.price();
        if (planet.getLevel().ordinal() >= good.minProduceLevel()) {
            price += good.incPerLevel() * (planet.getLevel().ordinal() - good.minProduceLevel());
            price += rand.nextInt(good.variance());
        } else { 
            //the controller won't let user change quantity of good if the price is < 0
            price = -1; //the good cannot be bought or sold at that planet
        }
        return price;
    }
    
    /**
     * Creates a stock of all the goods sold on this Planet.
     * If the price of a good is greater than zero, it will be added
     * to the market's stock.
     */
    private void setMarketStock() {
        Random rand = new Random();
        for (TradeGood good : TradeGood.values()) {
            if (sellPrices.get(good) >= 0) {
                stock.addItem(good, rand.nextInt(15) + 10); //still needs to be changed
            }
        }
    }
    
    /**
     * Gets the prices of all the TradeGoods as a Map.
     * @return the prices of goods on this planet.
     */
    public Map<TradeGood, Integer> getSellPrices() {
        return sellPrices;
    }
    
    /**
     * Gets the buy prices of all the TradeGoods as a Map.
     * @return the prices of goods on this planet.
     */
    public Map<TradeGood, Integer> getBuyPrices() {
        return buyPrices;
    }
    
    /**
     * Gets the current stock of the Market.
     * @return the market's stock
     */
    public Cargo getStock() {
        return stock;
    }
    
    
}
