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
import spacetrader.Resource;

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
     * Price is equal to (the base price) + (incPerLevel * (Planet Tech Level - minProduceLevel)) + (variance).
     * If a good is not sold on this planet, its price is -1.
     */
    private void setAllPrices() {
        sellPrices = new HashMap<>();
        buyPrices = new HashMap<>();
        PriceIncreaseEvent incEvent = PriceIncreaseEvent.getRandomPriceEvent();
        planet.setPriceIncEvent(incEvent);
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
                    buyPrice = (int) (buyPrice * 2.5);
                }
            } else { 
                sellPrice = -1;
            }
            buyPrices.put(good, buyPrice);
            sellPrices.put(good, sellPrice);
        }
    }
    
    /**
     * Creates a stock of all the goods sold on this Planet.
     * If the price of a good is greater than zero, it will be added
     * to the market's stock.
     */
    private void setMarketStock() {
        Random rand = new Random();
        for (TradeGood good : TradeGood.values()) {
            if (buyPrices.get(good) >= 0) {
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
    
    public Planet getPlanet() {
        return planet;
    }
    
}
