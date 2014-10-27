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
    
    public Player(String name, int pilot, int fighter, int trader, int engineer, int investor) {
        super(name, pilot, fighter, trader, engineer, investor);
        
        wallet = new Wallet();
        ship = new PlayerShip(ShipType.GNAT);
    }
    
    public int getEffectiveSkill(Skill type) {
        return Math.max(getSkill(type), ship.getCrewSkill(type));
    }
    
    public int getCredits() {
        return wallet.getCredits();
    }
    
    public Wallet getWallet() {
        return wallet;
    }
    
    public PlayerShip getShip() {
        return ship;
    }
    
    public Planet getLocation() {
        return this.location;
    }
    
    public void setLocation(Planet planet) {
        this.location = planet;
    }

    public int getDebt() {
        return debt;
    }

    public void setDebt(int debt) {
        this.debt = debt;
    }

    public int getPoliceKills() {
        return policeKills;
    }

    public void increasePoliceKills() {
        policeKills++;
    }

    public int getTraderKills() {
        return traderKills;
    }

    public void increaseTraderKills() {
        traderKills++;
    }

    public int getPirateKills() {
        return pirateKills;
    }

    public void increasePirateKills() {
        pirateKills++;
    }
    
    public int getPoliceRecordScore() {
        return policeRecordScore;
    }
    
    public void setPoliceRecordScore(int score) {
        policeRecordScore = score;
    }

    public int getReputationScore() {
        return reputationScore;
    }
    
    public PoliceRecord getPoliceRecord() {
        return PoliceRecord.getPoliceRecord(policeRecordScore);
    }
    
    public Reputation getReputation() {
        return Reputation.getReputation(reputationScore);
    }
    
    /**
     * Calculates the player's currrent worth. This is determined by the amount of money you own
     * minus the money you owe. It also considers the value of your spaceship and all the things in it.
     * @return the player's current worth
     */
    public int getCurrentWorth() {
        return wallet.getCredits() - debt + ship.currentShipPrice();
    }
    
    public void setShip(PlayerShip ship) {
        this.ship = ship;
    }

    public void payInterest() {
	int interest;

	if (debt > 0) {
            interest = Math.max(1, debt / 10);
            wallet.removeForcefully(interest);
	}
    }
}
