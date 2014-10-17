/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import spacetrader.commerce.Wallet;

/**
 *
 * @author Seth
 */
public class Player {
    
    private final String name;
    private final int pilotSkill;
    private final int fighterSkill;
    private final int traderSkill;
    private final int engineerSkill;
    private final int investorSkill;
    
    private final Wallet wallet;
    private final SpaceShip ship;
    private Planet location;
    
    private int debt = 0; // Current Debt
    private int policeKills = 0; // Number of police ships killed
    private int traderKills = 0; // Number of trader ships killed
    private int pirateKills = 0; // Number of pirate ships killed
    private int policeRecordScore = 0; // 0 = Clean record
    private int reputationScore = 0; // 0 = Harmless
    
    public Player(String name, int pilot, int fighter, int trader, int engineer, int investor) {
        this.name = name;
        this.pilotSkill = pilot;
        this.fighterSkill = fighter;
        this.traderSkill = trader;
        this.engineerSkill = engineer;
        this.investorSkill = investor;
        
        wallet = new Wallet();
        ship = new SpaceShip(ShipType.GNAT);
    }
    
    public String getName() {
        return name;
    }
    
    public int getPilotSkill() {
        return pilotSkill;
    }

    public int getFighterSkill() {
        return fighterSkill;
    }

    public int getTraderSkill() {
        return traderSkill;
    }

    public int getEngineerSkill() {
        return engineerSkill;
    }

    public int getInvestorSkill() {
        return investorSkill;
    }
    
    public Wallet getWallet() {
        return wallet;
    }
    
    public SpaceShip getShip() {
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
     * @return 
     */
    public int getCurrentWorth() {
        int worth = wallet.getCredits() - debt;
        return worth;
    }
}
