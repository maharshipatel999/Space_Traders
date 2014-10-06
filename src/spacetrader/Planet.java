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
    
    private final String name;
    private final Point location;
    private final TechLevel level;
    private final Resource resource;
    private final PoliticalSystem politSys;
    private PriceIncreaseEvent priceIncEvent;
    private Market market;
    
    public Planet(String name, Point location) {
        this.name = name;
        this.location = location;

        this.level = TechLevel.getRandomTechLevel();
        this.resource = Resource.getRandomResource();
        this.politSys = PoliticalSystem.getRandomPoliticalSystem();
        
        this.market = new Market(this);
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
    
    public void setPriceIncEvent(PriceIncreaseEvent priceIncEvent) {
        this.priceIncEvent = priceIncEvent;
    }
    
    public PriceIncreaseEvent getPriceIncEvent() {
        return this.priceIncEvent;
    }
     
    public Market getMarket() {
        return market;
    }

    @Override
    public String toString() {
        StringBuilder finStr = new StringBuilder();
        finStr.append(name + "\n");
        finStr.append("Location: (" +  location.getX() + ", " + location.getY() + ")\n");
        finStr.append("Tech Level: " + level + "\n");
        finStr.append("Resource: " + resource + "\n");
        finStr.append("Political System: " + politSys + "\n");
        return finStr.toString();
    }

}
