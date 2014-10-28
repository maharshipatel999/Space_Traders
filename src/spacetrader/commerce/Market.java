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
import spacetrader.Player;
import spacetrader.PoliceRecord;
import spacetrader.SkillList;
import spacetrader.SkillList.Skill;
import spacetrader.planets.Planet;
import spacetrader.planets.Resource;

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
     * @param player
     */
    public Market(Planet planet) {
        this.planet = planet;
        stock = new Cargo(1000);
        sellPrices = new HashMap<>();
        buyPrices = new HashMap<>();
        appxPrices = new HashMap<>();
        
        setApproximatePrices();
        setMarketStock();
    }
    
    /**
     * Sets the Map sell/buy prices so that each TradeGood maps to its sell/buy price on this planet.
     * Price is equal to (the base price) + (incPerLevel * (Planet Tech Level - minProduceLevel)) + (variance).
     * If a good is not sold on this planet, its price is -1.
     * @param player
     */
    public void setAllPrices(Player player) {
        setApproximatePrices();
        
        for (TradeGood good : TradeGood.values()) {
            Random rand = new Random();

            int buyPrice = appxPrices.get(good);
            int sellPrice = -1;
            if (buyPrice >= 0) {
                //check if there is a priceIncreaseEvent
                //each trade good has a PriceIncreaseEvent, so we don't need to check if its none
                if (planet.getPriceIncEvent() == good.incEvent()) {
                    buyPrice = (int) (buyPrice * 1.5);
                }
                
                int variance1 = rand.nextInt(good.variance() + 1);
                int variance2 = rand.nextInt(good.variance() + 1);
                
                buyPrice += variance1 - variance2;
            
                sellPrice = buyPrice;

                //adjust for intermediary if player is a criminal
                if (player.getPoliceRecord().ordinal() < PoliceRecord.DUBIOUS.ordinal()) {
                    sellPrice *= 0.9;
                }

                // BuyPrice = SellPrice + 1 to 12% (depending on trader skill (minimum is 1, max 12))
                buyPrice *= 1.03 + ((SkillList.MAX_SKILL - player.getEffectiveSkill(Skill.TRADER)) / 100.0);
                if (buyPrice <= sellPrice) {
                    buyPrice = sellPrice + 1;
                }
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

            if (((good == TradeGood.FIREARMS && !planet.getPoliticalSystem().firearmsOK()) ||
                    (good == TradeGood.NARCOTICS && !planet.getPoliticalSystem().drugsOK() ||
                    planet.getLevel().ordinal() < good.minProduceLevel()))) {
                appxPrice = -1;
            } else {
                appxPrice += good.incPerLevel() * (planet.getLevel().ordinal() - good.minProduceLevel());
                
                //if the good is highly wanted, increase the price
                if (good == planet.getPoliticalSystem().wanted()) {
                    appxPrice *= (4.0 / 3);
                }
                
                //high trader activity decreases the price
                appxPrice *= (100 - (2 * planet.getPoliticalSystem().strengthTraders())) / 100.0;
                
                //large systems have high production which decreases prices
                appxPrice *= (100 - planet.getSize()) / 100.0;
                
                
                //checks the resource of the planet and applies a price accordingly
                if (planet.isVisited()) {
                    if ((planet.getResource() == good.highCondition()) && good.highCondition() != Resource.NONE) {
                        appxPrice *= (4.0 / 3);
                    } else if ((planet.getResource() == good.lowCondition()) && good.lowCondition() != Resource.NONE) {
                        appxPrice *= (3.0 / 4);
                    }
                }
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
        for (TradeGood good : TradeGood.values()) {
            int quantity = 9 + rand.nextInt(5);
            quantity -= Math.abs(good.bestLevel().ordinal() - planet.getLevel().ordinal());
            quantity *= (1 + planet.getSize());

            // Because of the enormous profits possible, there shouldn't be too many robots or narcotics available
            if (good == TradeGood.ROBOTS || good == TradeGood.NARCOTICS) {
                quantity = 1 + ((quantity * 3) / 4);
            }
            //if the price is expecially expensive, there should be less of it and vice versa
            if (planet.getResource() == good.lowCondition() && good.lowCondition() != Resource.NONE) {
                quantity = (quantity * 4) / 3;
            } else if (planet.getResource() == good.highCondition() && good.highCondition() != Resource.NONE) {
                quantity = (quantity * 3) / 4;
            }
            //when there is a price increase event, the quantity should be very low
            if (planet.getPriceIncEvent() == good.incEvent())
                quantity /= 5;

           quantity -= rand.nextInt(10) + rand.nextInt(10);
           
           if (quantity < 0) {
               quantity = 0;
           }
           if (appxPrices.get(good) < 0) {
               quantity = -1;
           }
           stock.addItem(good, quantity, 0);
        }
    }
    /**
     * Gets the selling price of a specific TradeGood.
     * @param good the TradeGood to find the price off
     * @return the price of a good on this planet.
     */
    public int getSellPrice(TradeGood good) {
        return sellPrices.get(good);
    }
    
    /**
     * Gets the buying price of a specific TradeGood.
     * @param good the TradeGood to find the price off
     * @return the price of a good on this planet.
     */
    public int getBuyPrice(TradeGood good) {
        return buyPrices.get(good);
    }
    
    /**
     * Gets the approximate price of a specific TradeGood.
     * @param good the TradeGood to find the price off
     * @return the price of a good on this planet.
     */
    public int getAppxPrice(TradeGood good) {
        return appxPrices.get(good);
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
