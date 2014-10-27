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
public class Weapon {
    
    private WeaponType type;
    
    public Weapon(WeaponType type) {
        this.type = type;
    }
    
    /**
     * Gets the name of this shield.
     * @return this weapons's name
     */
    public String getName() {
        return type.toString();
    }
    
    public WeaponType getType() {
        return type;
    }
}
