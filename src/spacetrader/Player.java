/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader;

import java.io.Serializable;
import spacetrader.SkillList.Skill;
import spacetrader.commerce.Consumer;
import spacetrader.commerce.Wallet;
import spacetrader.exceptions.InsufficientFundsException;
import spacetrader.planets.Planet;
import spacetrader.ships.PlayerShip;
import spacetrader.ships.ShipType;
import spacetrader.ships.Weapon;
import spacetrader.ships.WeaponType;

/**
 *
 * @author Seth
 */
public class Player extends Trader implements Consumer, Serializable {

    private int policeKills = 0; // Number of police ships killed
    private int traderKills = 0; // Number of trader ships killed
    private int pirateKills = 0; // Number of pirate ships killed
    private int policeRecordScore = 0; // 0 = Clean record
    private int reputationScore = 0; // 0 = Harmless

    private final Wallet wallet;
    private PlayerShip ship;
    private Planet location;

    /**
     * Instantiates a new Player in the Game
     *
     * @param name name of the Player
     * @param pilot Pilot level
     * @param fighter Fighter level
     * @param trader Trader level
     * @param engineer Engineer level
     * @param investor Investor level
     */
    public Player(String name, int pilot, int fighter, int trader, int engineer,
            int investor) {
        super(name, pilot, fighter, trader, engineer, investor);
        wallet = new Wallet();
        ship = new PlayerShip(ShipType.GNAT);
        ship.getWeapons().addItem(new Weapon(WeaponType.PULSE));
    }

    /**
     * return Skill Player is using
     *
     * @param type Specific skill type of Player
     * @return amount of skill
     */
    public int getEffectiveSkill(Skill type) {
        return Math.max(getSkill(type), ship.getCrewSkill(type));
    }

    /**
     * Retrieve Ship of Player
     *
     * @return PlayerShip
     */
    public PlayerShip getShip() {
        return ship;
    }

    /**
     * assigns a ship to Player
     *
     * @param ship ship to assign to Player
     */
    public void setShip(PlayerShip ship) {
        this.ship = ship;
    }

    /**
     * Gets location of Planet
     *
     * @return location of planet
     */
    public Planet getLocation() {
        return this.location;
    }

    /**
     * Sets location of specific planet
     *
     * @param planet Planet to set the location of guys
     */
    public void setLocation(Planet planet) {
        this.location = planet;
    }

    /**
     * get number of Police killed
     *
     * @return get number of Police player has killed
     */
    public int getPoliceKills() {
        return policeKills;
    }

    /**
     * increase the number of Police kills Player has done
     */
    public void increasePoliceKills() {
        policeKills++;
    }

    /**
     * get number of Traders killed
     *
     * @return get number of Traders player has killed
     */
    public int getTraderKills() {
        return traderKills;
    }

    /**
     * increase record of number of kills Player has
     */
    public void increaseTraderKills() {
        traderKills++;
    }

    /**
     * get number of Pirate Kills
     *
     * @return number of Pirate Kills
     */
    public int getPirateKills() {
        return pirateKills;
    }

    /**
     * increase record of number Pirate kills
     */
    public void increasePirateKills() {
        pirateKills++;
    }

    /**
     * get the record of Police Record
     *
     * @return number of Police Scores
     */
    public int getPoliceRecordScore() {
        return policeRecordScore;
    }

    /**
     * set Police Record Score for Player
     *
     * @param score set Police Record Score for Player
     */
    public void setPoliceRecordScore(int score) {
        policeRecordScore = score;
    }

    /**
     * get Player Reputation Score
     *
     * @return Player Reputation Score
     */
    public int getReputationScore() {
        return reputationScore;
    }

    /**
     * Sets this Player's reputation score.
     *
     * @param reputationScore the new reputation score
     */
    public void setReputationScore(int reputationScore) {
        this.reputationScore = reputationScore;
    }

    /**
     * get Police Record
     *
     * @return Police Record
     */
    public PoliceRecord getPoliceRecord() {
        return PoliceRecord.getPoliceRecord(policeRecordScore);
    }

    /**
     * get Rep. of Player
     *
     * @return reputation of Player
     */
    public Reputation getReputation() {
        return Reputation.getReputation(reputationScore);
    }

    /**
     * Calculates the player's current worth. This is determined by the amount
     * of money you own minus the money you owe. It also considers the value of
     * your spaceship and all the things in it.
     *
     * @return the player's current worth
     */
    public int getCurrentWorth() {
        return wallet.getCredits() - wallet.getDebt() + ship.currentShipPrice();
    }

    /**
     * Pays the salaries for each of the mercenaries on the player's ship.
     */
    public void payMercenaries() {
        //TODO
    }

    /**
     * Pays all of the daily costs, such as insurance, mercenaries fees, and
     * interest rates. Throws InsufficientFundsExceptions depending on which fee
     * could not be paid.
     */
    public void payDailyFees() {
        //store the player's money so that if they don't have enough money to
        //pay one of their bills, their money can be restored to its original value.
        int originalPlayerMoney = wallet.getCredits();

        try {
            payInsuranceCost();
        } catch (InsufficientFundsException e) {
            wallet.setCredits(originalPlayerMoney);
            throw new InsufficientFundsException("insurance");
        }

        try {
            payMercenaries();
        } catch (InsufficientFundsException e) {
            wallet.setCredits(originalPlayerMoney);
            throw new InsufficientFundsException("mercenaries");
        }

        try {
            payInterest();
        } catch (InsufficientFundsException e) {
            wallet.setCredits(originalPlayerMoney);
            throw e;
        }
    }

    public Object getCargo() {
        return ship.getCargo();
    }

    @Override
    public void addCredits(int deposit) {
        wallet.addCredits(deposit);
    }

    @Override
    public void removeCredits(int withdrawal) {
        wallet.removeCredits(withdrawal);
    }

    @Override
    public void removeCreditsForced(int withdrawal) {
        wallet.removeCreditsForced(withdrawal);
    }

    @Override
    public int getCredits() {
        return wallet.getCredits();
    }

    @Override
    public void setCredits(int credits) {
        wallet.setCredits(credits);
    }

    @Override
    public int getDebt() {
        return wallet.getDebt();
    }

    @Override
    public void addDebt(int addition) {
        wallet.addDebt(addition);
    }

    @Override
    public void removeDebt(int removal) {
        wallet.removeDebt(removal);
    }
    
    @Override
    public void setInsuranceCost(int cost) {
        wallet.setInsuranceCost(cost);
    }

    @Override
    public int getInsuranceCost() {
        return wallet.getInsuranceCost();
    }
    
    @Override
    public void payInsuranceCost() {
        wallet.payInsuranceCost();
    }
    
    @Override
    public void payInterest() {
        wallet.payInterest();
    }

    
}
