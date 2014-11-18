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
public enum WeaponType {

    PULSE("Pulse Laser", 15, 2000, TechLevel.INDUSTRIAL, 50),
    BEAM("Beam Laser", 25, 12500, TechLevel.POST_INDUSTRIAL, 35),
    MILITARY("Military Laser", 35, 35000, TechLevel.HI_TECH, 15);

    private final String type;
    private final int power;
    private final int price;
    private final TechLevel minTechLevel;
    private final int chance;

    /**
     * Creates an enumeration value of WeaponType.
     *
     * @param type the type of this instance
     * @param power the power of this instance
     * @param price the cost to buy this type
     * @param minTechLevel the min TechLevel required to buy this type
     * @param chance The probability this type will do something
     */
    private WeaponType(String type, int power, int price,
            TechLevel minTechLevel, int chance) {
        this.type = type;
        this.power = power;
        this.price = price;
        this.minTechLevel = minTechLevel;
        this.chance = chance;
    }

    /**
     * Gets the power of this type.
     *
     * @return the power
     */
    public int power() {
        return power;
    }

    /**
     * Gets the price of this type.
     *
     * @return the price of this type
     */
    public int price() {
        return price;
    }

    /**
     * Gets the minTechLevel of this type.
     *
     * @return the minTechLevel needed to buy this type
     */
    public TechLevel minTechLevel() {
        return minTechLevel;
    }

    /**
     * Gets the probability this type will do something.
     *
     * @return the chance instance variable of this type
     */
    public int chance() {
        return chance;
    }

    /**
     * Gets the String representation of this object.
     *
     * @return the String format of this instance
     */
    @Override
    public String toString() {
        return type;
    }

}
