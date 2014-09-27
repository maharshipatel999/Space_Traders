/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.commerce;

import spacetrader.Resource;
import spacetrader.TechLevel;

/**
 *
 * @author nkaru_000
 */
public enum TradeGood {
    WATER     (30, 3, 4, 30, 50, 
               PriceIncreaseEvent.DROUGHT, Resource.LOTS_OF_WATER, Resource.DESERT, 
               TechLevel.MEDIEVAL, 0, 0),
    
    FURS      (250, 10, 10, 230, 280,
               PriceIncreaseEvent.COLD, Resource.RICH_FAUNA, Resource.LIFELESS, 
               TechLevel.PRE_AGRICULTURE, 0, 0),
    
    FOOD      (100, 5, 5, 90, 160,
               PriceIncreaseEvent.CROP_FAIL,Resource.RICH_SOIL, Resource.POOR_SOIL, 
               TechLevel.AGRICULTURE, 1, 0),
    
    ORE       (350, 20, 10, 350, 420, 
               PriceIncreaseEvent.WAR, Resource.MINERAL_RICH, Resource.MINERAL_POOR, 
               TechLevel.RENAISSANCE, 2, 2),
    
    GAMES     (250, -10, 5, 160, 270, 
               PriceIncreaseEvent.BOREDOM, Resource.ARTISTIC, Resource.NONE,
               TechLevel.POST_INDUSTRIAL, 3, 1),
    
    FIREARMS  (1250, -75, 100, 600, 1100, 
               PriceIncreaseEvent.WAR, Resource.WARLIKE, Resource.NONE, 
               TechLevel.INDUSTRIAL, 3, 1),
    
    MEDICINE  (650, -20, 10, 400, 700, 
               PriceIncreaseEvent.PLAGUE, Resource.LOTS_OF_HERBS, Resource.NONE, 
               TechLevel.POST_INDUSTRIAL, 4, 1),
    
    MACHINES  (900, -30, 5, 600, 800, 
               PriceIncreaseEvent.LACK_OF_WORKERS, Resource.NONE, Resource.NONE, 
               TechLevel.INDUSTRIAL, 4, 3),
    
    NARCOTICS (3500, -125, 150, 2000, 3000, 
               PriceIncreaseEvent.BOREDOM, Resource.WEIRD_MUSHROOMS, Resource.NONE, 
               TechLevel.INDUSTRIAL, 5, 0),
    
    ROBOTS    (5000, -150, 100, 3500, 5000, 
               PriceIncreaseEvent.LACK_OF_WORKERS, Resource.NONE, Resource.NONE, 
               TechLevel.HI_TECH, 6, 4);

    private int price, incPerLevel, variance, minTraderPrice, maxTraderPrice;
    private PriceIncreaseEvent incEvent;
    private Resource lowCondition, highCondition;
    private TechLevel bestLevel;
    private int minProduceLevel, minUseLevel;

    private TradeGood(  int price, 
                        int incPerLevel,
                        int variance,
                        int minTraderPrice,
                        int maxTraderPrice,
                        PriceIncreaseEvent incEvent,
                        Resource lowCondition,
                        Resource highCondition,
                        TechLevel bestLevel,
                        int minProduceLevel,
                        int minUseLevel
                    ) {
        this.price = price;
        this.incPerLevel = incPerLevel;
        this.variance = variance;
        this.minTraderPrice = minTraderPrice;
        this.maxTraderPrice = maxTraderPrice;
        this.incEvent = incEvent;
        this.lowCondition = lowCondition;
        this.highCondition = highCondition;
        this.bestLevel = bestLevel;
        this.minProduceLevel = minProduceLevel;
        this.minUseLevel = minUseLevel;
    }
    
    public int price() {
        return price;
    }

    public int incPerLevel() {
        return incPerLevel;
    }

    public int variance() {
        return variance;
    }

    public int minTraderPrice() {
        return minTraderPrice;
    }

    public int maxTraderPrice() {
        return maxTraderPrice;
    }

    public PriceIncreaseEvent incEvent() {
        return incEvent;
    }

    public Resource lowCondition() {
        return lowCondition;
    }

    public Resource highCondition() {
        return highCondition;
    }

    public TechLevel bestLevel() {
        return bestLevel;
    }

    public int minProduceLevel() {
        return minProduceLevel;
    }

    public int minUseLevel() {
        return minUseLevel;
    }
}