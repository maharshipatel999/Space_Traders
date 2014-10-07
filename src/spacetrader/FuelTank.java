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
     * 
     * @param maxFuel 
     */
    public FuelTank(int maxFuel) {
        this.maxFuel = maxFuel;
        this.fuel = maxFuel;
    }
    
    /**
     * 
     * @return 
     */
    public int getFuelAmount() {
        return fuel;
    }
    
    /**
     * 
     * @return 
     */
    public int getMaxFuel() {
        return maxFuel;
    }
    
    /**
     * 
     * @param fuelAmount 
     */
    public void addFuel(int fuelAmount) {
        fuel += fuelAmount;
        if (fuel > maxFuel) {
            fuel = maxFuel;
        }
    }
    
    /**
     * 
     * @param fuelAmount 
     */
    public void removeFuel(int fuelAmount) {
        if (fuelAmount > fuel) {
            throw new InsufficientFuelException();
        }
        fuel -= fuelAmount;
    }
    
    public void increaseMaxFuel(int amount) {
        maxFuel += amount;
    }
    
    public void decreaseMaxFuel(int amount) {
        if (amount < maxFuel) {
            throw new IllegalArgumentException("Amount is too large.");
        }
        maxFuel -= amount;
    }
}
