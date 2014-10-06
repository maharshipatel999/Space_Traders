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
    
    public void setLocation(Planet location) {
        this.location = location;
    }
}
