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
import java.util.Random;
import spacetrader.Player;

/**
 * This class will handle all the probability of creating and handling encounters.
 * @author Caleb
 */
public class EncounterManager {
    
    private Queue<Encounter> encounters;
    private final int numTotalEncounters;
    private Player player;
    
    /**
     * Creates queue with all Encounters for one transition period
     * @param source source planet
     * @param destination destination planet
     * @param ship ship Player is using
     * @param player Player in the game
     */
    public EncounterManager(Planet source, Planet destination, PlayerShip ship, Player player) {
        encounters = new LinkedList<>();
        this.player = player;
        int pirateLevels = destination.getPoliticalSystem().strengthPirates();
        int traderLevels = destination.getPoliticalSystem().strengthTraders();
        int policeLevels = destination.getPoliticalSystem().strengthPolice();
        int counter = pirateLevels + traderLevels + policeLevels;
        int pirateProportion = pirateLevels / counter;
        int traderProportion = traderLevels / counter;
        Random rand = new Random();
        while(counter > 0) {
            double d = rand.nextDouble();
            if (d <= pirateProportion) {
                encounters.add(new PirateEncounter(player));
            } else if (pirateProportion < d && d <= (pirateProportion + traderProportion)) {
                encounters.add(new TraderEncounter(player));
            } else {
                encounters.add(new PoliceEncounter(player));
            }      
            counter -= 5;
        }
        numTotalEncounters = encounters.size();
         //figure this out with an algorithm
    }

    
    /**
     * returns newest encounter and pops it off the ArrayList holding all encounters
     * @return latest encounter
     */
    public Encounter getNextEncounter() {
        return encounters.poll();
    }
    
    /**
     * returns the total number of encounters returned by LinkedList
     * @return total encounters for warp
     */
    public int getNumTotalEncounters() {
        return numTotalEncounters;
    }
    
    /**
     * returns the number of encounters remaining in current Warp
     * @return number encounters remaining
     */
    public int getEncountersRemaining() {
        return encounters.size();
    }
}
