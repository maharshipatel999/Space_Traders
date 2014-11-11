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
     * Checks if these two items are equal to each other
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
     * Generates a hash code for this object
     *
     * @return the hash code of this object
     */
    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
}
