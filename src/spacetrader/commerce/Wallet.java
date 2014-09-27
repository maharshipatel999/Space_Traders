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
    public int add(int credits) {
        if (credits < 0) {
            throw new IllegalArgumentException("Cannot add negative credits");
        } 
        return this.credits += credits;
    }
    public int remove(int credits) {
        if (credits < 0) {
            throw new IllegalArgumentException("Cannot add negative credits");
        }
        if (this.credits < credits) {
            throw new InsufficientFundsException();
        }
        return this.credits -= credits;
    }
}
