/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import java.awt.Point;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Represents a Solar System the player can visit.
 * 
 * @author maharshipatel999
 */
public class SolarSystem {
    
    private final String name;
    private final Point location;
    private final int level;
    private final int resource;
    private final int polSys;
    private final static Set<Point> solSysLocations = new HashSet<>(100);
    private final static Random rand = new Random();
    
    public SolarSystem(String name) {
        Point myLocation;
        do {
            myLocation = randomPoint();
        } while (solSysLocations.contains(myLocation));
        solSysLocations.add(myLocation);
        
        this.location = myLocation;
        this.level = rand.nextInt(8);
        this.resource = rand.nextInt(13);
        this.polSys = rand.nextInt(5);
        this.name = name;
    }

    /**
     * Creates a new random point between MAX_LOC (exclusive) and MIN_LOC (inclusive).
     * @return a new random point. 
     */
    private Point randomPoint() {
        final int MAX_LOC = 500;
        final int MIN_LOC = 10;
        int x = rand.nextInt(MAX_LOC - MIN_LOC) + MIN_LOC;
        int y = rand.nextInt(MAX_LOC - MIN_LOC) + MIN_LOC;
        Point tempPoint = new Point(x, y);
        return tempPoint;
    }

    public String getName() {
        return this.name;
    }

    public Point getLocation() {
        return this.location;
    }

    public int getLevel() {
        return this.level;
    }

    public int getResource() {
        return this.resource;
    }

    public int getPoliticalSystem() {
        return this.polSys;
    }

    @Override
    public String toString() {
        StringBuilder finStr = new StringBuilder();
        finStr.append(name + "\n");
        finStr.append("Location: " + location.toString() + "\n");
        finStr.append("Tech Level: " + level + "\n");
        finStr.append("Resource: " + resource + "\n");
        finStr.append("Political System: " + polSys + "\n");
        return finStr.toString();
    }

}
