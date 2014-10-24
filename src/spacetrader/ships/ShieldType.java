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

    ENERGY        ("Energy shield",     100, 5000,  TechLevel.INDUSTRIAL,      70),
    REFLECTIVE    ("Reflective shield", 200, 20000, TechLevel.POST_INDUSTRIAL, 30);
    
    private final String type;
    private final int power;
    private final int price;
    private final TechLevel minTechLevel;
    private final int chance;
    
    private ShieldType(String type, int power, int price, TechLevel minTechLevel, int chance) {
        this.type = type;
        this.power = power;
        this.price = price;
        this.minTechLevel = minTechLevel;
        this.chance = chance;    
    }
    
    public int power() {
        return power;
    }

    public int price() {
        return price;
    }

    public TechLevel minTechLevel() {
        return minTechLevel;
    }

    public int chance() {
        return chance;
    }
    
    @Override
    public String toString() {
        return type;
    }
}
