/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.ships;

import spacetrader.planets.TechLevel;

/**
 *
 * @author nkaru_000
 */
public enum GadgetType {

    EXTRA_CARGO(TechLevel.EARLY_INDUSTRIAL, 2500, 35, "5-Slot Cargo Expansion"),
    // Increases engineer's effectivity
    AUTO_REPAIR(TechLevel.INDUSTRIAL, 7500, 20, "Auto-Repair System"),
     // Increases pilot's effectivity
    NAVIGATION(TechLevel.POST_INDUSTRIAL, 15000, 20, "Navigating System"),
     // Increases fighter's effectivity
    TARGETING(TechLevel.POST_INDUSTRIAL, 25000, 20, "Targeting System"),
    // If you have a good engineer, less pirates and police will notice you
    CLOAK(TechLevel.HI_TECH, 100000, 5, "Cloaking Device"); 

    private final TechLevel minTechLevel;
    private final int price;
    private final int chance;
    private final String name;

    /**
     * Creates an enumeration value of GadgetType.
     *
     * @param minTechLevel the minTechLevel this gadget type can be used on
     * @param price the price of this gadget type
     * @param chance the probability this gadget type has of increasing a
     * certain attribute of the player
     * @param name the name of this gadget type
     */
    private GadgetType(TechLevel minTechLevel, int price, int chance,
            String name) {
        this.minTechLevel = minTechLevel;
        this.price = price;
        this.chance = chance;
        this.name = name;
    }

    /**
     * Gets the price of this Gadget Type.
     *
     * @return the price
     */
    public int price() {
        return price;
    }

    /**
     * Gets the probability this gadget type has of increasing a certain
     * attribute of the player.
     *
     * @return the chance attribute of this gadget type
     */
    public int chance() {
        return chance;
    }

    /**
     * Gets the minTechLevel of this Gadget Type.
     *
     * @return the minimum TechLevel to use this Gadget Type
     */
    public TechLevel minTechLevel() {
        return minTechLevel;
    }

    /**
     * The string representation of this object.
     *
     * @return the name of this GadgetType
     */
    @Override
    public String toString() {
        return name;
    }
}
