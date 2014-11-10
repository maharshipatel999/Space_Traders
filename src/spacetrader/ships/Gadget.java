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

    public Gadget(GadgetType type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return type.toString();
    }

    public GadgetType getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return type.toString();
    }
}
