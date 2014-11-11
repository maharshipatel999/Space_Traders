/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.ships;

/**
 *
 * @author Caleb
 */
public class Shield extends Equipment {

    private ShieldType type;
    private int health;

    /**
     * Creates a Shield object
     * @param type the type of shield this object will be
     */
    public Shield(ShieldType type) {
        this.type = type;
        this.health = type.power();
    }

    /**
     * Gets the name of this Shield
     * @return the name of the this Object's ShieldType
     */
    @Override
    public String getName() {
        return type.toString();
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
     * Sets the power remaining of this shield.
     *
     * @param health the power remaining
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Gets the type of this Shield
     * @return The type of this shield
     */
    public ShieldType getType() {
        return type;
    }

    /**
     * Returns a string representation of this object
     * @return the string format of this object
     */
    @Override
    public String toString() {
        return type.toString();
    }
}
