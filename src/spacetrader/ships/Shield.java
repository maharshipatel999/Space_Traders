/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.ships;

import spacetrader.planets.TechLevel;

/**
 *
 * @author Caleb
 */
public class Shield extends Equipment {

    private ShieldType type;
    private int health;

    /**
     * Creates a Shield object.
     *
     * @param type the type of shield this object will be
     */
    public Shield(ShieldType type) {
        this.type = type;
        this.health = type.power();
    }

    @Override
    public String getName() {
        return type.toString();
    }
    
    @Override
    public String getEquipmentName() {
        return "Shield";
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
     * Gets the defense of this shield.
     *
     * @return the shield's defense
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets the power remaining of this shield. The health can't be set higher
     * than the shield's power.
     *
     * @param health the power remaining
     * @return the amount the new health is over the max health
     */
    public int setHealth(int health) {
        if (health > type.power()) {
            this.health = type.power();
        } else {
            this.health = health;
        }
        return health - type.power();
    }

    /**
     * Gets the type of this Shield.
     *
     * @return The type of this shield
     */
    public ShieldType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
