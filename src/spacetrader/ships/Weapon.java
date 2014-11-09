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
public class Weapon extends Equipment {

    private WeaponType type;

    public Weapon(WeaponType type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return type.toString();
    }
    
    public int getPower() {
        return type.power();
    }

    public WeaponType getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return type.toString();
    }
}
