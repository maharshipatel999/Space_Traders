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

    @Override
    public String getName() {
        return type.toString();
    }
    
    @Override
    public String getEquipmentName() {
        return "Gadget";
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
     * Gets the type of this Gadget.
     *
     * @return the type of this Gadget
     */
    public GadgetType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
