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
public enum ShieldType {

    ENERGY("Energy shield", 100, 5000, TechLevel.INDUSTRIAL, 70),
    REFLECTIVE("Reflective shield", 200, 20000, TechLevel.POST_INDUSTRIAL, 30);

    private final String type;
    private final int power;
    private final int price;
    private final TechLevel minTechLevel;
    private final int chance;

    /**
     * Creates an enumeration value of ShieldType.
     *
     * @param type the type of this type
     * @param power the power of this type
     * @param price the amount this type will cost
     * @param minTechLevel the minimum TechLevel required to use this type
     * @param chance the probability this type will do something
     */
    private ShieldType(String type, int power, int price,
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
     * @return how much this type costs
     */
    public int price() {
        return price;
    }

    /**
     * Gets the minTechLevel of this type.
     *
     * @return the minTechLevel required to use this type
     */
    public TechLevel minTechLevel() {
        return minTechLevel;
    }

    /**
     * Gets the chance instance variable of this type.
     *
     * @return the chance this type will do something
     */
    public int chance() {
        return chance;
    }

    /**
     * the string representation of this object.
     *
     * @return the String name of this type
     */
    @Override
    public String toString() {
        return type;
    }
}
