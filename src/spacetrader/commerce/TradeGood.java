/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.commerce;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import spacetrader.planets.Resource;
import spacetrader.planets.TechLevel;

/**
 *
 * @author nkaru_000
 */
public enum TradeGood {

    WATER("Water", 30, 3, 4, 30, 50, 1,
            PriceIncreaseEvent.DROUGHT, Resource.LOTS_OF_WATER, Resource.DESERT,
            TechLevel.MEDIEVAL, 0, 0),
    FURS("Furs", 250, 10, 10, 230, 280, 5,
            PriceIncreaseEvent.COLD, Resource.RICH_FAUNA, Resource.LIFELESS,
            TechLevel.PRE_AGRICULTURE, 0, 0),
    FOOD("Food", 100, 5, 5, 90, 160, 5,
            PriceIncreaseEvent.CROP_FAIL, Resource.RICH_SOIL, Resource.POOR_SOIL,
            TechLevel.AGRICULTURE, 1, 0),
    ORE("Ore", 350, 20, 10, 350, 420, 10,
            PriceIncreaseEvent.WAR, Resource.MINERAL_RICH, Resource.MINERAL_POOR,
            TechLevel.RENAISSANCE, 2, 2),
    GAMES("Games", 250, -10, 5, 160, 270, 5,
            PriceIncreaseEvent.BOREDOM, Resource.ARTISTIC, Resource.NONE,
            TechLevel.POST_INDUSTRIAL, 3, 1),
    FIREARMS("Firearms", 1250, -75, 100, 600, 1100, 25,
            PriceIncreaseEvent.WAR, Resource.WARLIKE, Resource.NONE,
            TechLevel.INDUSTRIAL, 3, 1),
    MEDICINE("Medicine", 650, -20, 10, 400, 700, 25,
            PriceIncreaseEvent.PLAGUE, Resource.LOTS_OF_HERBS, Resource.NONE,
            TechLevel.POST_INDUSTRIAL, 4, 1),
    MACHINES("Machines", 900, -30, 5, 600, 800, 25,
            PriceIncreaseEvent.LACK_OF_WORKERS, Resource.NONE, Resource.NONE,
            TechLevel.INDUSTRIAL, 4, 3),
    NARCOTICS("Narcotics", 3500, -125, 150, 2000, 3000, 50,
            PriceIncreaseEvent.BOREDOM, Resource.WEIRD_MUSHROOMS, Resource.NONE,
            TechLevel.INDUSTRIAL, 5, 0),
    ROBOTS("Robots", 5000, -150, 100, 3500, 5000, 100,
            PriceIncreaseEvent.LACK_OF_WORKERS, Resource.NONE, Resource.NONE,
            TechLevel.HI_TECH, 6, 4);

    private final String type;
    private final int price, incPerLevel, variance, minTraderPrice, maxTraderPrice, roundoff; //roundoff: roundoff price for trade in orbit
    private final PriceIncreaseEvent incEvent;
    private final Resource lowCondition, highCondition;
    private final TechLevel bestLevel;
    private final int minProduceLevel, minUseLevel;

    private static final List<TradeGood> VALUES
            = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    /**
     * Creates a new Trade Good.
     *
     * @param type toString of trade good
     * @param price price of trade good
     * @param incPerLevel increase in price per level
     * @param variance variance in price
     * @param minTraderPrice minimum price to sell for
     * @param maxTraderPrice maximum price to sell product for
     * @param roundoff roundoff of price
     * @param incEvent PriceIncreaseEvent of this Trade Good
     * @param lowCondition Resource condition if Trade Good low price
     * @param highCondition Resource condition if Trade Good high price
     * @param bestLevel Tech Level of Planet
     * @param minProduceLevel minimum Produce Level of Trade Good
     * @param minUseLevel minimum usage level of Trade Good
     */
    private TradeGood(String type,
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

    /**
     * gets toString of Trade Good.
     *
     * @return toString of Trade Good
     */
    @Override
    public String toString() {
        return type;
    }

    /**
     * Gets base price of Trade Good.
     *
     * @return price of Trade Good
     */
    public int price() {
        return price;
    }

    /**
     * Gets amount to increase the price of this good for each TechLevel above
     * the minimum produce level.
     *
     * @return increase in price per level
     */
    public int incPerLevel() {
        return incPerLevel;
    }

    /**
     * Gets variance of price of TradeGood.
     *
     * @return variance of price of TradeGood
     */
    public int variance() {
        return variance;
    }

    /**
     * Gets minimum price Trader will sell or buy this good for.
     *
     * @return the smallest price a trader will offer
     */
    public int minTraderPrice() {
        return minTraderPrice;
    }

    /**
     * Gets maximum price Trader will sell or buy this good for.
     *
     * @return the largest price a trader will offer
     */
    public int maxTraderPrice() {
        return maxTraderPrice;
    }

    /**
     * Gets round-off of the TradeGood.
     *
     * @return round-off of TradeGood
     */
    public int roundoff() {
        return roundoff;
    }

    /**
     * Gets the event which dramatically raises the price of this good
     *
     * @return the PriceIncreaseEvent which raises the price of this good
     */
    public PriceIncreaseEvent incEvent() {
        return incEvent;
    }

    /**
     * Gets resource condition which causes this good to be cheap.
     *
     * @return resource which lowers the price of this good
     */
    public Resource lowCondition() {
        return lowCondition;
    }

    /**
     * Gets resource condition which causes this good to be expensive.
     *
     * @return resource which raises the price of this good
     */
    public Resource highCondition() {
        return highCondition;
    }

    /**
     * Gets the TechLevel in which this good is most bountiful.
     *
     * @return the tech level that sells the most of this good
     */
    public TechLevel bestLevel() {
        return bestLevel;
    }

    /**
     * Gets the minimum required tech level that a good will be buyable on a planet.
     *
     * @return min tech level to buy a good on a planet
     */
    public int minProduceLevel() {
        return minProduceLevel;
    }

    /**
     * Gets the minimum required TechLevel that a good will be sellable on a planet.
     *
     * @return min tech level to sell a good on a planet
     */
    public int minUseLevel() {
        return minUseLevel;
    }

    /**
     * Gets a random TradeGood.
     *
     * @return a TradeGood determined at random
     */
    public static TradeGood getRandomTradeGood() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
