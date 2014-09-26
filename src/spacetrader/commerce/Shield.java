/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.commerce;

import spacetrader.TechLevel;

/**
 *
 * @author Caleb
 */
public abstract class Shield {
    
    private String name;
    private int price;
    private int defense;
    private TechLevel minTechLevel;
    
    public Shield(String name, int price, int defense, TechLevel minTechLevel) {
        this.name = name;
        this.price = price;
        this.defense = defense;
        this.minTechLevel = minTechLevel;
    }

    /**
     * Gets the name of this shield.
     * @return the shield's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the base price for this shield.
     * @return the base price
     */
    public int getPrice() {
        return price;
    }

    /**
     * Gets the defense of this shield.
     * @return the shield's defense
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Gets the minumum tech level this item can be sold at.
     * @return the minimum tech level
     */
    public TechLevel getMinTechLevel() {
        return minTechLevel;
    }
}
