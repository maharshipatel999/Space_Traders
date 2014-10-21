/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.commerce;

import java.io.Serializable;
import spacetrader.exceptions.InsufficientFundsException;

/**
 *
 * @author Seth
 */

public class Wallet implements Serializable {
    
    private int credits;
    private int debt;
    
    public Wallet() {
        credits = 1000;
        debt = 0;
    }
    
    public boolean isEmpty() {
        return credits == 0;
    }
    
    public void add(int deposit) {
        if (deposit < 0) {
            throw new IllegalArgumentException("Cannot add negative credits");
        } else {
            credits += deposit;
        }
    }
    
    public void remove(int withdrawal) {
        if (withdrawal < 0) {
            throw new IllegalArgumentException("Cannot add negative credits");
        } else if (credits < withdrawal) {
            throw new InsufficientFundsException();
        } else {
            credits -= withdrawal;
        }
    }
    
    public void removeForcefully(int withdrawal) {
        if (withdrawal < 0) {
            throw new IllegalArgumentException("Cannot add negative credits");
        } else {
            credits -= withdrawal;
            if (credits < 0) {
                debt += Math.abs(credits);
                credits = 0;
            }
        }
    }
    
    public int getCredits() {
        return credits;
    }
    
    public void setCredits(int credits) {
        this.credits = credits;
    }
    
    public int getDebt() {
        return debt;
    }
    
    public void increaseDebt(int addition) {
        if (addition < 0) {
            throw new IllegalArgumentException("Cannot add negative debt");
        } else {
            credits += addition;
        }
    }
    
    public void decreaseDebt(int removal) {
        if (removal < 0) {
            throw new IllegalArgumentException("Cannot add remove negative debt");
        } else {
            credits -= removal;
            credits = (credits < 0) ? 0 : credits;
        }
    }
}
