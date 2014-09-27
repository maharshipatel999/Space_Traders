/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import spacetrader.commerce.Gadget;
import spacetrader.commerce.Shield;
import spacetrader.commerce.Weapon;

/**
 *
 * @author Caleb
 */
public abstract class SpaceShip {
    
    //private Cargo cargo;
    private EquipmentSlots<Weapon> weapons;
    private EquipmentSlots<Shield> shields;
    private EquipmentSlots<Gadget> gadgets;
    private ShipType type;
    
    public SpaceShip(ShipType type) {
        this.type = type;
        weapons = new EquipmentSlots<>(type.numWeaponSlots());
        shields = new EquipmentSlots<>(type.numShieldSlots());
        gadgets = new EquipmentSlots<>(type.numGadgetSlots());
        
        //cargo = new Cargo();
    }
}
