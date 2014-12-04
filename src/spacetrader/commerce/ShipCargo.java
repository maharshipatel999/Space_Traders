/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.commerce;

import spacetrader.ships.Gadget;
import spacetrader.ships.GadgetType;
import spacetrader.ships.SpaceShip;

/**
 * Represents Cargo that goes on a ship. A ShipCargo's max capacity can be
 * increased by an extra-cargo gadget.
 *
 * @author Caleb
 */
public class ShipCargo extends Cargo {

    private final SpaceShip ship;

    /**
     * Creates a new ShipCargo with a max capacity and ship that holds it.
     * 
     * @param maxCapacity the number of cargo slots
     * @param ship the ship this ShipCargo is on
     */
    public ShipCargo(int maxCapacity, SpaceShip ship) {
        super(maxCapacity);
        this.ship = ship;
    }

    @Override
    public int getMaxCapacity() {
        int capacity = super.getMaxCapacity();
        if (ship.getGadgets().contains(new Gadget(GadgetType.EXTRA_CARGO))) {
            capacity += Gadget.EXTRA_CARGO_AMT;
        }

        return capacity;
    }
}
