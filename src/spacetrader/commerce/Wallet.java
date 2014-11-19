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
public class Wallet implements Serializable, Consumer {

    public static final int INITIAL_CREDITS = 1000;
    public static final int MAX_DEBT = 100000;

    private int credits;
    private int debt;
    private int insuranceCost;

    /**
     * Creates a new wallet with 1000 credits and 0 debt.
     */
    public Wallet() {
        this.credits = INITIAL_CREDITS;
        this.debt = 0;
        this.insuranceCost = 0;
    }

    /**
     * Adds a specified amount of money to this wallet.
     *
     * @throws IllegalArgumentException if deposit is negative
     * @param deposit the amount of money to addCredits
     */
    @Override
    public void addCredits(int deposit) {
        if (deposit < 0) {
            throw new IllegalArgumentException("Cannot add negative credits");
        } else {
            credits += deposit;
        }
    }

    /**
     * Removes a specified amount of money from this wallet.
     *
     * @param withdrawal the amount of money to removeCredits
     */
    @Override
    public void removeCredits(int withdrawal) {
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
     * @param withdrawal the amount of money to removeCredits
     */
    @Override
    public void removeCreditsForced(final int withdrawal) {
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
    @Override
    public int getCredits() {
        return credits;
    }

    /**
     * Sets the amount of credits in this wallet.
     *
     * @param credits the amount of credits in this wallet.
     */
    @Override
    public void setCredits(int credits) {
        if (credits < 0) {
            throw new InsufficientFundsException("Cannot have negative credits");
        }
        this.credits = credits;
    }

    /**
     * Gets the amount of debt this wallet has.
     *
     * @return this wallet's debt
     */
    @Override
    public int getDebt() {
        return debt;
    }

    /**
     * Increases the amount of debt in this wallet by a specified amount.
     *
     * @throws IllegalArgumentException if addition is negative
     * @param addition amount of additional debt
     */
    @Override
    public void addDebt(final int addition) {
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
    @Override
    public void removeDebt(int removal) {
        if (removal < 0) {
            throw new IllegalArgumentException("Cannot remove negative debt");
        } else {
            credits -= removal;
            credits = (credits < 0) ? 0 : credits;
        }
    }

    /**
     * sets insurance cost in wallet.
     * @param cost cost of insurance
     */
    @Override
    public void setInsuranceCost(final int cost) {
        this.insuranceCost = cost;
    }

    /**
     * gets insurance cost from wallet.
     * @return insurance cost
     */
    @Override
    public int getInsuranceCost() {
        return this.insuranceCost;
    }

    @Override
    public void payInsuranceCost() {
        if ((insuranceCost > 0)) {
            this.removeCredits(insuranceCost);
        }
    }

    @Override
    public void payInterest() {
        if (debt > MAX_DEBT) {
            throw new InsufficientFundsException("debt");
        } else if (debt > 0) {
            int interest = Math.max(1, debt / 10);
            this.removeCreditsForced(interest);
        }
    }
}
