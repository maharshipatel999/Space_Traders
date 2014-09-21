/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author maharshipatel999
 */
public class SolarSystem {
    
    private final String name;
    private final Point location;
    private final int level;
    private final int resource;
    private final int polSys;
    private static ArrayList<Point> solSysLocations = new ArrayList<>(100);
    private static Random rand = new Random();
    
    SolarSystem(String name) {
        Point tempPoint = getPoint();
        while (solSysLocations.contains(tempPoint)) {
            tempPoint = getPoint();
        }
        this.location = tempPoint;
        this.level = rand.nextInt(8);
        this.resource = rand.nextInt(13);
        this.polSys = rand.nextInt(5);
        this.name = name;
    }

    private Point getPoint() {
        int x = rand.nextInt(490) + 10;
        int y = rand.nextInt(490) + 10;
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
