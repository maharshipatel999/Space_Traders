/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader;

import java.awt.Point;
import java.util.Random;
import spacetrader.commerce.Market;
import spacetrader.commerce.PriceIncreaseEvent;

/**
 * Represents a Solar System the player can visit.
 *
 * @author maharshipatel999
 */
public class Planet {

    private final Random rand = new Random();
    private final String[] activity =
    {
        "Absent",
        "Minimal",
        "Few",
        "Some",
        "Moderate",
        "Many",
        "Abundant",
        "Swarms"
    };

    private final String name;
    private final Point location;
    private final TechLevel level;
    private final Resource resource;
    private final PoliticalSystem politSys;
    private PriceIncreaseEvent priceIncEvent;
    private int priceIncDuration;
    private Market market;

    private boolean visited;

    public Planet(String name, Point location) {
        this(name, location, TechLevel.getRandomTechLevel(),
                Resource.getRandomResource(),
                PoliticalSystem.getRandomPoliticalSystem());
    }

    public Planet(String name, Point location, TechLevel level,
            Resource resource, PoliticalSystem politSys) {
        this.name = name;
        this.location = location;
        this.level = level;
        this.resource = resource;
        this.politSys = politSys;

        this.market = new Market(this);
        this.visited = false;
    }

    public String getName() {
        return this.name;
    }

    public Point getLocation() {
        return this.location;
    }

    public TechLevel getLevel() {
        return this.level;
    }

    public Resource getResource() {
        return this.resource;
    }

    public PoliticalSystem getPoliticalSystem() {
        return this.politSys;
    }
    
    public void setRandomPriceIncEvent() {
        this.priceIncEvent = PriceIncreaseEvent.getRandomPriceEvent();
        this.setPriceIncDuration(PriceIncreaseEvent.setRandomPriceIncDuration());
    }

    public int getPriceIncDuration() {
        return this.priceIncDuration;
    }
    
    public void setPriceIncDuration(int priceIncDuration) {
        this.priceIncDuration = priceIncDuration;
    }
    
    public PriceIncreaseEvent getPriceIncEvent() {
        return this.priceIncEvent;
    }

    public Market getMarket() {
        return market;
    }

    public boolean isVisited() {
        return visited;
    }
    
    public void setVisited() {
        visited = true;
    }
    
    private int calculateStrengthOfPolice(PoliceRecord record) {
        int strength = politSys.strengthPolice();
        if (record.ordinal() < PoliceRecord.VILLAIN.ordinal()) {
            strength *= 3;
        } else if (record.ordinal() < PoliceRecord.CRIMINAL.ordinal()) {
            strength *= 2;
        }
        
        return strength;
    }
    
    /**
     * Determine the amount of police that will be on the planet
     * @return description of how many police can be expected to be on the planet
     */
    //I just used completely random numbers. This needs to be actually calculated.
    public String expectedAmountOfPolice() {
        double policeVariable =  Math.random() * 21; //SHOULDNT BE RANDOM
        
        int activityIndex = (int) policeVariable / activity.length;
        return activity[activityIndex];
    }

    /**
     * Determine the amount of pirates that will be on the planet
     * @return description of how many pirates can be expected to be on the planet
     */
    //I just used completely random numbers. This needs to be actually calculated.
    public String expectedAmountOfPirates() {
        double pirateVariable = Math.random() * 21; //SHOULDNT BE RANDOM
        
        int activityIndex = (int) pirateVariable / activity.length;
        return activity[activityIndex];
    }

    @Override
    public String toString() {
        StringBuilder finStr = new StringBuilder();
        finStr.append(name + "\n");
        finStr.append("Location: (" + location.getX() + ", " + location.getY() + ")\n");
        finStr.append("Tech Level: " + level + "\n");
        finStr.append("Resource: " + resource + "\n");
        finStr.append("Political System: " + politSys + "\n");
        finStr.append("Price Increase Event: " + priceIncEvent + "\n");
        return finStr.toString();
    }

}
