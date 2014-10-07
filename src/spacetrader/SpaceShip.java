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
public class SpaceShip {
    
    private ShipType type;
    private Cargo cargo;
    private EquipmentSlots<Weapon> weapons;
    private EquipmentSlots<Shield> shields;
    private EquipmentSlots<Gadget> gadgets;
    private FuelTank tank;
    
    public SpaceShip(ShipType type) {
        this.type = type;
        this.cargo = new Cargo(type.cargoBay());
        this.weapons = new EquipmentSlots<>(type.weaponSlots());
        this.shields = new EquipmentSlots<>(type.shieldSlots());
        this.gadgets = new EquipmentSlots<>(type.gadgetSlots());
        this.tank = new FuelTank(type.fuel());
    }
    
    public Cargo getCargo() {
        return cargo;
    }

    public FuelTank getTank() {
        return tank;
    }
    
    
}
