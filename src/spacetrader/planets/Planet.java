/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.planets;

import java.io.Serializable;
import java.util.Random;
import javafx.geometry.Point2D;
import spacetrader.PoliceRecord;
import spacetrader.commerce.Market;
import spacetrader.commerce.PriceIncreaseEvent;

/**
 * Represents a Solar System the player can visit.
 *
 * @author maharshipatel999
 */
public class Planet implements Serializable {

    private final Random rand = new Random();
    private final String[] activity
            = {
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
    private final Point2D location;
    private final TechLevel level;
    private final Resource resource;
    private final PoliticalSystem politSys;
    private final int size;
    private PriceIncreaseEvent priceIncEvent;
    private int priceIncDuration;
    private Market market;
    private Wormhole wormhole;

    private boolean visited;

    /**
     * Constructor for Planet
     *
     * @param name - name of planet
     * @param location - location of planet
     */
    public Planet(String name, Point2D location) {
        this(name, location, TechLevel.getRandomTechLevel(),
                Resource.getRandomResource(),
                PoliticalSystem.getRandomPoliticalSystem());
    }

    /**
     *
     * @param name - name of planet
     * @param location - locations of planet
     * @param level - tech level of planet
     * @param resource - main resource of planet
     * @param politSys - political system of planet
     */
    public Planet(String name, Point2D location, TechLevel level,
            Resource resource, PoliticalSystem politSys) {
        this.name = name;
        this.location = location;
        this.level = level;
        this.resource = resource;
        this.politSys = politSys;
        this.priceIncEvent = PriceIncreaseEvent.NONE;
        this.size = rand.nextInt(5); //this should probably be moved to the Universe class

        this.market = new Market(this);
        this.wormhole = null;
        this.visited = false;
    }

    /**
     * gets the name of the planet
     *
     * @return the name of the planet
     */
    public String getName() {
        return this.name;
    }

    /**
     * gets locations
     *
     * @return location of the planet
     */
    public Point2D getLocation() {
        return this.location;
    }

    /**
     * get tech level of planet
     *
     * @return the tech level of planet
     */
    public TechLevel getLevel() {
        return this.level;
    }

    /**
     * get resource
     *
     * @return resource of the planet
     */
    public Resource getResource() {
        return this.resource;
    }

    /**
     * get political system
     *
     * @return political system
     */
    public PoliticalSystem getPoliticalSystem() {
        return this.politSys;
    }

    /**
     * Returns the size of this planet, a value between 0 and 4.
     *
     * @return this planet's size
     */
    public int getSize() {
        return size;
    }

    /**
     * set the price increase event
     *
     * @param priceIncEvent - price increase event for a planet
     */
    public void setPriceIncEvent(PriceIncreaseEvent priceIncEvent) {
        this.priceIncEvent = priceIncEvent;
        this.setPriceIncDuration(PriceIncreaseEvent.setRandomPriceIncDuration());
    }

    /**
     * Set random price increase event
     */
    public void setRandomPriceIncEvent() {
        this.priceIncEvent = PriceIncreaseEvent.getRandomPriceEvent();
        this.setPriceIncDuration(PriceIncreaseEvent.setRandomPriceIncDuration());
    }

    /**
     * get price increase event duration
     *
     * @return price increase event duration
     */
    public int getPriceIncDuration() {
        return this.priceIncDuration;
    }

    /**
     * set price increase event duration
     *
     * @param priceIncDuration - the duration of a price increase event
     */
    public void setPriceIncDuration(int priceIncDuration) {
        this.priceIncDuration = priceIncDuration;
    }

    /**
     * get price increase event
     *
     * @return price increase event
     */
    public PriceIncreaseEvent getPriceIncEvent() {
        return this.priceIncEvent;
    }

    /**
     * get market
     *
     * @return market
     */
    public Market getMarket() {
        return market;
    }

    /**
     * check if planet has been vis
     *
     * @return if a planet has been visted
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * set visited for planet
     */
    public void setVisited() {
        visited = true;
    }

    /**
     * get wormhole for a planet
     *
     * @return wormhole for planet
     */
    public Wormhole getWormhole() {
        return this.wormhole;
    }

    /**
     * set wormhole for planet
     *
     * @param wormhole - the wormhole of a planet
     */
    public void setWormhole(Wormhole wormhole) {
        this.wormhole = wormhole;
    }

    /**
     * Calculate strength of police
     *
     * @param record - player's police record
     * @return the strength of the police
     */
    public int calculateStrengthOfPolice(PoliceRecord record) {
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
     *
     * @param record the player's police record
     * @return description of how many police can be expected to be on the
     * planet the player's police record
     */
    public String expectedAmountOfPolice(PoliceRecord record) {
        int policeVariable = calculateStrengthOfPolice(record);

        int activityIndex = Math.min(policeVariable, activity.length - 1);
        return activity[activityIndex];
    }

    /**
     * Determine the amount of pirates that will be on the planet
     *
     * @return description of how many pirates can be expected to be on the
     * planet
     */
    public String expectedAmountOfPirates() {
        int pirateVariable = this.politSys.strengthPirates();

        int activityIndex = Math.min(pirateVariable, activity.length - 1);
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
