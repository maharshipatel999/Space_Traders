/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.ships;

/**
 *
 * @author nkaru_000
 */
public class Gadget extends Equipment {

    private GadgetType type;

    /**
     * Creates a new Gadget with a type.
     *
     * @param type the type of this gadget
     */
    public Gadget(GadgetType type) {
        this.type = type;
    }

    /**
     * Gets the name of this Gadget.
     *
     * @return the name of this Gadget
     */
    @Override
    public String getName() {
        return type.toString();
    }

    /**
     * Gets the type of this Gadget.
     *
     * @return the type of this Gadget
     */
    public GadgetType getType() {
        return type;
    }

    /**
     * Gets the string representation of this object.
     *
     * @return the string of this Gadget
     */
    @Override
    public String toString() {
        return type.toString();
    }
}
