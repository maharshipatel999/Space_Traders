/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.commerce;

/**
 * Represents the interface for something that has money, debt, and can buy
 * insurance. This class is implemented by the Wallet class and Player class.
 *
 * @author Caleb
 */
public interface Consumer {

    /**
     * Gets the amount of money this Consumer has.
     *
     * @return this wallet's total credits
     */
    int getCredits();

    /**
     * Adds a specified amount of money to this Consumer.
     *
     * @throws IllegalArgumentException if deposit is negative
     * @param deposit the amount of money to addCredits
     */
    void addCredits(int deposit);

    /**
     * Removes a specified amount of money from this Consumer.
     *
     * @param withdrawal the amount of money to removeCredits
     */
    void removeCredits(int withdrawal);

    /**
     * Removes a specified amount of money from this Consumer. If the player
     * does not have enough money, the difference will become debt. Use this
     * method if you want to make sure removing money will not cause an
     * exception
     *
     * @throws IllegalArgumentException if withdrawal is negative
     * @param withdrawal the amount of money to removeCredits
     */
    void removeCreditsForced(final int withdrawal);

    /**
     * Sets the amount of credits this Consumer has.
     *
     * @param credits the amount of credits in this wallet.
     */
    void setCredits(int credits);

    /**
     * Gets the amount of debt this Consumer has.
     *
     * @return this wallet's debt
     */
    int getDebt();

    /**
     * Increases the amount of debt this Consumer has by a specified amount.
     *
     * @throws IllegalArgumentException if addition is negative
     * @param addition amount of additional debt
     */
    void addDebt(final int addition);

    /**
     * Decreases the amount of debt of this Consumer by a specified amount.
     *
     * @throws IllegalArgumentException if removal is negative
     * @param removal the amount of debt removed
     */
    void removeDebt(int removal);

    /**
     * Gets this Consumer's cost of insurance.
     *
     * @return insurance cost
     */
    int getInsuranceCost();

    /**
     * Sets this Consumer's cost of insurance. If this value is zero, the
     * Consumer does not have insurance.
     *
     * @param cost cost of insurance
     */
    void setInsuranceCost(final int cost);

    /**
     * Pays daily cost of insurance. Can throw exception if player does not have
     * enough money.
     */
    void payInsurance();

    /**
     * Gets the number of days the consumer has had insurance with out a claim.
     *
     * @return the number of days of no-claim
     */
    int getNoClaimDays();

    /**
     * Pays interest on the player's debt.
     */
    void payInterest();

}
