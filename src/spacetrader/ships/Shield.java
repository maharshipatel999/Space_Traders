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
public class Shield {
    
    private ShieldType type;
    private int powerRemaining;
    
    public Shield(ShieldType type) {
        this.type = type;
        powerRemaining = type.power();
    }

    /**
     * Gets the name of this shield.
     * @return this shield's name
     */
    public String getName() {
        return type.toString();
    }

    /**
     * Gets the defense of this shield.
     * @return the shield's defense
     */
    public int getPowerRemaining() {
        return type.power();
    }
}
