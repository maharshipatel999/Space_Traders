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
    WATER     ("Water", 30, 3, 4, 30, 50, 1,
               PriceIncreaseEvent.DROUGHT, Resource.LOTS_OF_WATER, Resource.DESERT, 
               TechLevel.MEDIEVAL, 0, 0),
    
    FURS      ("Furs", 250, 10, 10, 230, 280, 5,
               PriceIncreaseEvent.COLD, Resource.RICH_FAUNA, Resource.LIFELESS, 
               TechLevel.PRE_AGRICULTURE, 0, 0),
    
    FOOD      ("Food", 100, 5, 5, 90, 160, 5,
               PriceIncreaseEvent.CROP_FAIL,Resource.RICH_SOIL, Resource.POOR_SOIL, 
               TechLevel.AGRICULTURE, 1, 0),
    
    ORE       ("Ore", 350, 20, 10, 350, 420, 10,
               PriceIncreaseEvent.WAR, Resource.MINERAL_RICH, Resource.MINERAL_POOR, 
               TechLevel.RENAISSANCE, 2, 2),
    
    GAMES     ("Games", 250, -10, 5, 160, 270, 5, 
               PriceIncreaseEvent.BOREDOM, Resource.ARTISTIC, Resource.NONE,
               TechLevel.POST_INDUSTRIAL, 3, 1),
    
    FIREARMS  ("Firearms", 1250, -75, 100, 600, 1100, 25,
               PriceIncreaseEvent.WAR, Resource.WARLIKE, Resource.NONE, 
               TechLevel.INDUSTRIAL, 3, 1),
    
    MEDICINE  ("Medicine", 650, -20, 10, 400, 700, 25,
               PriceIncreaseEvent.PLAGUE, Resource.LOTS_OF_HERBS, Resource.NONE, 
               TechLevel.POST_INDUSTRIAL, 4, 1),
    
    MACHINES  ("Machines", 900, -30, 5, 600, 800, 25,
               PriceIncreaseEvent.LACK_OF_WORKERS, Resource.NONE, Resource.NONE, 
               TechLevel.INDUSTRIAL, 4, 3),
    
    NARCOTICS ("Narcotics", 3500, -125, 150, 2000, 3000, 50,
               PriceIncreaseEvent.BOREDOM, Resource.WEIRD_MUSHROOMS, Resource.NONE, 
               TechLevel.INDUSTRIAL, 5, 0),
    
    ROBOTS    ("Robots", 5000, -150, 100, 3500, 5000, 100,
               PriceIncreaseEvent.LACK_OF_WORKERS, Resource.NONE, Resource.NONE, 
               TechLevel.HI_TECH, 6, 4);

    private String type;
    private int price, incPerLevel, variance, minTraderPrice, maxTraderPrice, roundoff; //roundoff: roundoff price for trade in orbit
    private PriceIncreaseEvent incEvent;
    private Resource lowCondition, highCondition;
    private TechLevel bestLevel;
    private int minProduceLevel, minUseLevel; 

    private TradeGood(  String type,
                        int price, 
                        int incPerLevel,
                        int variance,
                        int minTraderPrice,
                        int maxTraderPrice,
                        int roundoff,
                        PriceIncreaseEvent incEvent,
                        Resource lowCondition,
                        Resource highCondition,
                        TechLevel bestLevel,
                        int minProduceLevel,
                        int minUseLevel
                    ) {
        this.type = type;
        this.price = price;
        this.incPerLevel = incPerLevel;
        this.variance = variance;
        this.minTraderPrice = minTraderPrice;
        this.maxTraderPrice = maxTraderPrice;
        this.roundoff = roundoff;
        this.incEvent = incEvent;
        this.lowCondition = lowCondition;
        this.highCondition = highCondition;
        this.bestLevel = bestLevel;
        this.minProduceLevel = minProduceLevel;
        this.minUseLevel = minUseLevel;
    }
    
    public String type() {
        return type;
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
    
    public int roundoff() {
        return roundoff;
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