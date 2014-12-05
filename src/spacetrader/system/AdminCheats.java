/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.system;

import spacetrader.Mercenary;
import spacetrader.PoliceRecord;
import spacetrader.Reputation;
import spacetrader.planets.PoliticalSystem;
import spacetrader.planets.Resource;
import spacetrader.planets.TechLevel;
import spacetrader.ships.ShipType;
import spacetrader.system.SpaceTrader.Debug;

/**
 * Holds the values for all the testing options.
 * Every instance variable must have a getter and setter.
 * 
 * @author Caleb
 */
public class AdminCheats {
    private int initialCredits;
    private TechLevel techLevel;
    private PoliticalSystem politicalSystem;
    private Resource resource;
    private ShipType startingShip;
    private PoliceRecord policeRecord;
    private Reputation reputation;
    private int numMercenaries;
    private Debug debugMode;

    public AdminCheats() {
        techLevel = TechLevel.INDUSTRIAL;
        politicalSystem = PoliticalSystem.DEMOCRACY;
        resource = Resource.NONE;
        initialCredits = 5000;
        startingShip = ShipType.GNAT;
        reputation = Reputation.HARMLESS;
        policeRecord = PoliceRecord.CLEAN;
        numMercenaries = 0;
        debugMode = Debug.OFF;

    }

    public TechLevel getTechLevel() {
        return techLevel;
    }

    public void setTechLevel(TechLevel techLevel) {
        this.techLevel = techLevel;
    }

    public PoliticalSystem getPoliticalSystem() {
        return politicalSystem;
    }

    public void setPoliticalSystem(PoliticalSystem politicalSystem) {
        this.politicalSystem = politicalSystem;
    }

    public int getInitialCredits() {
        return initialCredits;
    }

    public void setInitialCredits(int initialCredits) {
        this.initialCredits = initialCredits;
    }

    public ShipType getStartingShip() {
        return startingShip;
    }

    public void setStartingShip(ShipType startingShip) {
        this.startingShip = startingShip;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public PoliceRecord getPoliceRecord() {
        return policeRecord;
    }

    public void setPoliceRecord(PoliceRecord policeRecord) {
        this.policeRecord = policeRecord;
    }

    public Reputation getReputation() {
        return reputation;
    }

    public void setReputation(Reputation reputation) {
        this.reputation = reputation;
    }

    public Debug getDebugMode() {
        return debugMode;
    }

    public int getNumMercenaries() {
        return numMercenaries;
    }

    public void setNumMercenaries(int numMercenaries) {
        this.numMercenaries = numMercenaries;
    }
    

    public void setDebugMode(Debug debugMode) {
        this.debugMode = debugMode;
    }
}
