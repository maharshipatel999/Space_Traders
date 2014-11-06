/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.commerce;

import java.io.Serializable;
import spacetrader.exceptions.InsufficientFundsException;

/**
 * Represents the player's wallet. Keeps track of money and debt.
 *
 * @author Caleb
 */
public class Wallet implements Serializable {

    private int credits;
    private int debt;

    /**
     * Creates a new wallet with 1000 credits and 0 debt.
     */
    public Wallet() {
        credits = 1000;
        debt = 0;
    }

    /**
     * Determines if the wallet is out of money.
     *
     * @return
     */
    public boolean isEmpty() {
        return credits == 0;
    }

    /**
     * Adds a specified amount of money to this wallet.
     *
     * @throws IllegalArgumentException if deposit is negative
     * @param deposit the amount of money to add
     */
    public void add(int deposit) {
        if (deposit < 0) {
            throw new IllegalArgumentException("Cannot add negative credits");
        } else {
            credits += deposit;
        }
    }

    /**
     * Removes a specified amount of money from this wallet.
     *
     * @throws IllegalArgumentException if withdrawal is negative.
     * @throws InsufficientFundsException if the player does not have enough
     * money
     * @param withdrawal the amount of money to remove
     */
    public void remove(int withdrawal) {
        if (withdrawal < 0) {
            throw new IllegalArgumentException("Cannot add negative credits");
        } else if (credits < withdrawal) {
            throw new InsufficientFundsException();
        } else {
            credits -= withdrawal;
        }
    }

    /**
     * Removes a specified amount of money from this wallet. If the player does
     * not have enough money, the difference will become debt. Use this method
     * if you want to make sure removing money will not cause an exception
     *
     * @throws IllegalArgumentException if withdrawal is negative
     * @param withdrawal the amount of money to remove
     */
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

    /**
     * Gets the amount of money in this wallet.
     *
     * @return this wallet's total credits
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Sets the amount of credits in this wallet.
     *
     * @param credits the amount of credits in this wallet.
     */
    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * Gets the amount of debt this wallet has.
     *
     * @return this wallet's debt
     */
    public int getDebt() {
        return debt;
    }

    /**
     * Increases the amount of debt in this wallet by a specified amount.
     *
     * @throws IllegalArgumentException if addition is negative
     * @param addition amount of additional debt
     */
    public void increaseDebt(int addition) {
        if (addition < 0) {
            throw new IllegalArgumentException("Cannot add negative debt");
        } else {
            credits += addition;
        }
    }

    /**
     * Decreases the amount of debt in this wallet by a specified amount.
     *
     * @throws IllegalArgumentException if removal is negative
     * @param removal the amount of debt removed
     */
    public void decreaseDebt(int removal) {
        if (removal < 0) {
            throw new IllegalArgumentException("Cannot add remove negative debt");
        } else {
            credits -= removal;
            credits = (credits < 0) ? 0 : credits;
        }
    }
}
