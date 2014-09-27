/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import spacetrader.commerce.Cargo;
import spacetrader.commerce.Gadget;
import spacetrader.commerce.Shield;
import spacetrader.commerce.Weapon;

/**
 *
 * @author Caleb Stokols
 */
public abstract class SpaceShip {
    
    private ShipType type;
    private Cargo cargo;
    private EquipmentSlots<Weapon> weapons;
    private EquipmentSlots<Shield> shields;
    private EquipmentSlots<Gadget> gadgets;
    private FuelTank tank;
    
    public SpaceShip(ShipType type) {
        this.type = type;
        Cargo cargo = new Cargo(type.numCargoSlots());
        weapons = new EquipmentSlots<>(type.numWeaponSlots());
        shields = new EquipmentSlots<>(type.numShieldSlots());
        gadgets = new EquipmentSlots<>(type.numGadgetSlots());
        tank = new FuelTank(type.fuel());

    }
}
