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
        
        //picks a random TechLevel
        TechLevel[] levels = TechLevel.values();
        this.level = levels[rand.nextInt(levels.length)];
        
        //picks a random Resource
        Resource[] resources = Resource.values();
        this.resource = Resource.values()[rand.nextInt(resources.length)];
        
        //picks a random Political System
        PoliticalSystem[] systems = PoliticalSystem.values();
        this.politSys = systems[rand.nextInt(systems.length)];
        
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
