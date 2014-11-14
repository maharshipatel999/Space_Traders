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
public abstract class Equipment {

    /**
     * Returns the name of this Equipment.
     *
     * @return this Equipment's name
     */
    public abstract String getName();

    /**
     * Gets the base price of this equipment as directly and solely determined
     * by the type of the equipment.
     *
     * @return the base price
     */
    public abstract int getBasePrice();

    /**
     * Determines the price of buying this equipment, is affected by the
     * player's trader skill.
     *
     * @param traderSkill the trader skill level of the player buying this good
     * @return the price of buying this equipment
     */
    public int getBuyPrice(int traderSkill) {
        return (int) (getBasePrice() * (100 - traderSkill) / 100.0);
    }

    /**
     * Determines the price of selling this equipment, is affected by the
     * player's trader skill.
     *
     * @param traderSkill the trader skill level of the player selling this good
     * @return the price of selling this equipment
     */
    public int getSellPrice(int traderSkill) {
        return (getBuyPrice(traderSkill) * 3) / 4;
    }

    /**
     * Checks if these two items are equal to each other.
     *
     * @param o the other item
     * @return true or false depending on equivalence
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Equipment) {
            Equipment other = (Equipment) o;
            return this.getName().equals(other.getName());
        } else {
            return false;
        }
    }

    /**
     * Generates a hash code for this object.
     *
     * @return the hash code of this object
     */
    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
}
