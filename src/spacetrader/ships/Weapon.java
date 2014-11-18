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
public class Weapon extends Equipment {

    private WeaponType type;

    /**
     * Creates an instance of Weapon.
     *
     * @param type the type of weapon to be created
     */
    public Weapon(WeaponType type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return type.toString();
    }
    
    @Override
    public String getEquipmentName() {
        return "Weapon";
    }

    @Override
    public int getBasePrice() {
        return type.price();
    }
    
    @Override
    public TechLevel getMinTechLevel() {
        return type.minTechLevel();
    }

    /**
     * Gets the power of this weapon.
     *
     * @return the power of this weapon's type
     */
    public int getPower() {
        return type.power();
    }

    /**
     * Gets the type of this weapon.
     *
     * @return the type of this instance
     */
    public WeaponType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
