/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader;

import spacetrader.planets.Planet;
import spacetrader.ships.PlayerShip;
import spacetrader.ships.ShipType;
import java.io.Serializable;
import spacetrader.SkillList.Skill;
import spacetrader.commerce.Wallet;

/**
 *
 * @author Seth
 */
public class Player extends Trader implements Serializable {

    private int debt = 0; // Current Debt
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
     * @param name name of the Player
     * @param pilot Pilot level 
     * @param fighter Fighter level
     * @param trader Trader level
     * @param engineer Engineer level
     * @param investor Investor level
     */
    public Player(String name, int pilot, int fighter, int trader, int engineer, int investor) {
        super(name, pilot, fighter, trader, engineer, investor);
        wallet = new Wallet();
        ship = new PlayerShip(ShipType.GNAT);
    }

    /**
     * return Skill Player is using
     * @param type Specific skill type of Player
     * @return amount of skill
     */
    public int getEffectiveSkill(Skill type) {
        return Math.max(getSkill(type), ship.getCrewSkill(type));
    }

    /**
     * Retrieves Player Credits in Player Wallet
     * @return credits in Player Wallet
     */
    public int getCredits() {
        return wallet.getCredits();
    }

    /**
     * get Wallet
     * @return wallet
     */
    public Wallet getWallet() {
        return wallet;
    }

    /**
     * Retrieve Ship of Player
     * @return PlayerShip
     */
    public PlayerShip getShip() {
        return ship;
    }

    /**
     * assigns a ship to Player
     * @param ship ship to assign to Player
     */
    public void setShip(PlayerShip ship) {
        this.ship = ship;
    }

    /**
     * Gets location of Planet
     * @return location of planet
     */
    public Planet getLocation() {
        return this.location;
    }

    /**
     * Sets location of specific planet
     * @param planet Planet to set the location of guys
     */
    public void setLocation(Planet planet) {
        this.location = planet;
    }

    /**
     * gets Debt of Player
     * @return debt of Player
     */
    public int getDebt() {
        return debt;
    }

    /**
     * sets debt of the Player
     * @param debt debt to set of Player
     */
    public void setDebt(int debt) {
        this.debt = debt;
    }

    /**
     * get number of Police killed
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
     * @return get number of Traders player has ckilled
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
     * @return number of Police Scores
     */
    public int getPoliceRecordScore() {
        return policeRecordScore;
    }

    /**
     * set Police Record Score for Player
     * @param score set Police Record Score for Player
     */
    public void setPoliceRecordScore(int score) {
        policeRecordScore = score;
    }

    /**
     * get Player Reputation Score
     * @return Player Reputation Score
     */
    public int getReputationScore() {
        return reputationScore;
    }

    /**
     * get Police Record 
     * @return Police Record
     */
    public PoliceRecord getPoliceRecord() {
        return PoliceRecord.getPoliceRecord(policeRecordScore);
    }

    /**
     * get Rep. of Player
     * @return reputation of Player
     */
    public Reputation getReputation() {
        return Reputation.getReputation(reputationScore);
    }

    /**
     * Calculates the player's currrent worth. This is determined by the amount
     * of money you own minus the money you owe. It also considers the value of
     * your spaceship and all the things in it.
     *
     * @return the player's current worth
     */
    public int getCurrentWorth() {
        return wallet.getCredits() - debt + ship.currentShipPrice();
    }

    /**
     * Pays interest with Player
     */
    public void payInterest() {
        int interest;

        if (debt > 0) {
            interest = Math.max(1, debt / 10);
            wallet.removeForcefully(interest);
        }
    }
}
