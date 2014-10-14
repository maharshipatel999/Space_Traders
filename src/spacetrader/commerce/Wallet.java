/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.commerce;

import spacetrader.exceptions.InsufficientFundsException;

/**
 *
 * @author Seth
 */
public class Wallet {
    private int credits;
    
    public Wallet() {
        credits = 1000;
    }
    
    public boolean isEmpty() {
        return credits == 0;
    }
    
    public int add(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot add negative credits");
        } else {
            return credits += amount;
        }
    }
    
    public int remove(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot add negative credits");
        } else if (credits < amount) {
            throw new InsufficientFundsException();
        } else {
            return credits -= amount;
        }
    }
    
    public int getCredits() {
        return credits;
    }
    public void setCredits(int credits) {
        this.credits = credits;
    }
}
