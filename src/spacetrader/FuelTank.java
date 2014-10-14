/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import spacetrader.exceptions.InsufficientFuelException;

/**
 *
 * @author Caleb
 */
public class FuelTank {
    
    private int fuel;
    private int maxFuel;
    
    /**
     * Creates a new fuel tank with a max fuel amount.
     * @param maxFuel the max fuel amount for this tank.
     */
    public FuelTank(int maxFuel) {
        this.maxFuel = maxFuel;
        this.fuel = maxFuel;
    }
    
    /**
     * Gets the current remaining amount of fuel.
     * @return this fueltank's current fuel amount
     */
    public int getFuelAmount() {
        return fuel;
    }
    
    /**
     * Gets the max amount of fuel this tank can hold.
     * @return the maximum capacity of this fueltank
     */
    public int getMaxFuel() {
        return maxFuel;
    }
    
    /**
     * Adds a specified amount of fuel to this tank.
     * @param fuelAmount the amount of fuel to add
     */
    public void addFuel(int fuelAmount) {
        fuel += fuelAmount;
        if (fuel > maxFuel) {
            fuel = maxFuel;
        }
    }
    
    /**
     * Removes a specified amount of fuel from this tank.
     * Throws an exception if fuelAmount is too large.
     * @param fuelAmount the amount of fuel to remove
     */
    public void removeFuel(int fuelAmount) {
        if (fuelAmount > fuel) {
            throw new InsufficientFuelException();
        }
        fuel -= fuelAmount;
    }
    
    /**
     * Increases the max fuel of this tank by a specified amount.
     * @param amount how much to increase the max fuel by
     */
    public void increaseMaxFuel(int amount) {
        maxFuel += amount;
    }
    
    /**
     * Decreases the max fuel of this tank by a specified amount
     * @param amount how much to decrease the max fuel by
     */
    public void decreaseMaxFuel(int amount) {
        if (amount < maxFuel) {
            throw new IllegalArgumentException("Amount is too large.");
        }
        maxFuel -= amount;
    }
}
