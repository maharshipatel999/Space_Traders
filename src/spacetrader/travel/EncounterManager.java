/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.travel;

import java.util.LinkedList;
import java.util.Queue;
import spacetrader.Planet;
import spacetrader.PlayerShip;

/**
 * This class will handle all the probability of creating and handling encounters.
 * @author Caleb
 */
public class EncounterManager {
    
    private Queue<Encounter> encounters;
    private final int numTotalEncounters;
    
    public EncounterManager(Planet source, Planet destination, PlayerShip ship) {
        encounters = new LinkedList<Encounter>();
        
        numTotalEncounters = 5; //figure this out with an algorithm
    }
    
    public Encounter getNextEncounter() {
        return encounters.poll();
    }
    
    public int getNumTotalEncounters() {
        return numTotalEncounters;
    }
    
    public int getEncountersRemaining() {
        return encounters.size();
    }
}
