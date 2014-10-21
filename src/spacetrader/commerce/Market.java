/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.commerce;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import spacetrader.Planet;
import spacetrader.Resource;

/**
 *
 * @author Caleb
 */
public class Market implements Serializable {
    
    private Planet planet;
    private Map<TradeGood, Integer> sellPrices;
    private Map<TradeGood, Integer> buyPrices;
    private Map<TradeGood, Integer> appxPrices;
    private Cargo stock;
    
    /**
     * Constructor
     * @param planet the planet to be assigned an instance of Market
     */
    public Market(Planet planet) {
        this.planet = planet;
        stock = new Cargo(1000);
        sellPrices = new HashMap<>();
        buyPrices = new HashMap<>();
        appxPrices = new HashMap<>();
        
        setAllPrices();
        setApproximatePrices();
        setMarketStock();
    }
    
    /**
     * Sets the Map sell/buy prices so that each TradeGood maps to its sell/buy price on this planet.
     * Price is equal to (the base price) + (incPerLevel * (Planet Tech Level - minProduceLevel)) + (variance).
     * If a good is not sold on this planet, its price is -1.
     */
    public void setAllPrices() {
        for (TradeGood good : TradeGood.values()) {
            Random rand = new Random();
            int variance = rand.nextInt(good.variance() + 1);
            int buyPrice = good.price();
            int sellPrice = good.price();

            if (planet.getLevel().ordinal() >= good.minProduceLevel()) {
                buyPrice += good.incPerLevel() * (planet.getLevel().ordinal() - good.minProduceLevel());
                buyPrice += variance;
                //checks the resource of the planet and applies a price accordingly
                if ((planet.getResource() == good.highCondition()) && good.highCondition() != Resource.NONE) {
                    buyPrice = (int) (buyPrice * 1.5);
                } else if ((planet.getResource() == good.lowCondition()) && good.lowCondition() != Resource.NONE) {
                    buyPrice = (int) (buyPrice * 0.5);
                }
                //check if there is a priceIncreaseEvent
                //each good has a PriceIncreaseEvent, so we don't need to check if its none
                if (planet.getPriceIncEvent() == good.incEvent()) {
                    buyPrice = (int) (buyPrice * 2.5);
                }
            } else {
                buyPrice = -1;
            }

            if (planet.getLevel().ordinal() >= good.minUseLevel()) {
                sellPrice += good.incPerLevel() * (planet.getLevel().ordinal() - good.minProduceLevel());
                sellPrice += variance;
                //checks the resource of the planet and applies a price accordingly
                if ((planet.getResource() == good.highCondition()) && good.highCondition() != Resource.NONE) {
                    sellPrice = (int) (sellPrice * 1.5);
                } else if ((planet.getResource() == good.lowCondition()) && good.lowCondition() != Resource.NONE) {
                    sellPrice = (int) (sellPrice * 0.5);
                }
                //check if there is a priceIncreaseEvent
                //each good has a PriceIncreaseEvent, so we don't need to check if its none
                if (planet.getPriceIncEvent() == good.incEvent()) {
                    sellPrice = (int) (sellPrice * 2.5);
                }
            } else { 
                sellPrice = -1;
            }
            buyPrices.put(good, buyPrice);
            sellPrices.put(good, sellPrice);
        }
    }
    
    /**
     * Sets the approximate price for each TradeGood, which will be used
     * in the space map to aid the user in determining which planet he/she
     * should travel to.
     */
    private void setApproximatePrices() {
        for (TradeGood good : TradeGood.values()) {
            int appxPrice = good.price();
            if (planet.getLevel().ordinal() >= good.minUseLevel()) {
                appxPrice += good.incPerLevel() * (planet.getLevel().ordinal() - good.minProduceLevel());
                //checks the resource of the planet and applies a price accordingly
//                if ((planet.getResource() == good.highCondition()) && good.highCondition() != Resource.NONE) {
//                    sellPrice = (int) (sellPrice * 1.5);
//                } else if ((planet.getResource() == good.lowCondition()) && good.lowCondition() != Resource.NONE) {
//                    sellPrice = (int) (sellPrice * 0.5);
//                }
            } else { 
                appxPrice = -1;
            }
            appxPrices.put(good, appxPrice);
        }
    }
    
    
    /**
     * Creates a stock of all the goods sold on this Planet.
     * If the price of a good is greater than zero, it will be added
     * to the market's stock.
     */
    private void setMarketStock() {
        Random rand = new Random();
        int amountToAdd;
        for (TradeGood good : TradeGood.values()) {
            amountToAdd = rand.nextInt(7) + 12; //can be changed 
            if (buyPrices.get(good) >= 0) {
                if (planet.getLevel() == good.bestLevel()) {
                    stock.addItem(good, amountToAdd * 2);
                } else {
                    stock.addItem(good, amountToAdd);  
                }
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
     * Gets the approximate prices of all the TradeGoods as a Map.
     * @return the approximate prices of goods on this planet.
     */
    public Map<TradeGood, Integer> getAppxPrices() {
        return appxPrices;
    }
    
    /**
     * Gets the current stock of the Market.
     * @return the market's stock
     */
    public Cargo getStock() {
        return stock;
    }
    
    /**
     * Gets the planet this Market is assigned to
     * @return the planet
     */
    public Planet getPlanet() {
        return planet;
    }
    
}
