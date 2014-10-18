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
    
    private final ShipType type;
    private final Cargo cargo;
    private final FuelTank tank;
    private final EquipmentSlots<Weapon> weapons;
    private final EquipmentSlots<Shield> shields;
    private final EquipmentSlots<Gadget> gadgets;
    
    private int maxHullStrength;
    private int hullStrength;
    
    public SpaceShip(ShipType type) {
        this.type = type;
        this.cargo = new Cargo(type.cargoBay());
        this.tank = new FuelTank(type.fuel());
        this.weapons = new EquipmentSlots<>(type.weaponSlots());
        this.shields = new EquipmentSlots<>(type.shieldSlots());
        this.gadgets = new EquipmentSlots<>(type.gadgetSlots());
        
        this.maxHullStrength = type.hullStrength();
        this.hullStrength = maxHullStrength;
    }
    
    public Cargo getCargo() {
        return cargo;
    }

    public FuelTank getTank() {
        return tank;
    }

    public ShipType getType() {
        return type;
    }

    public EquipmentSlots<Weapon> getWeapons() {
        return weapons;
    }

    public EquipmentSlots<Shield> getShields() {
        return shields;
    }

    public EquipmentSlots<Gadget> getGadgets() {
        return gadgets;
    }

    public int getMaxHullStrength() {
        return maxHullStrength;
    }

    public int getHullStrength() {
        return hullStrength;
    }
    public void setHullStrength(int hullStrength) {
        this.hullStrength = hullStrength;
    }
    
    
}
