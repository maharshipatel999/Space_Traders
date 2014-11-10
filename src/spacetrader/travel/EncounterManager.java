/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import spacetrader.Player;
import spacetrader.Tools;
import spacetrader.planets.Planet;
import spacetrader.ships.PlayerShip;
import spacetrader.ships.ShipType;
import spacetrader.travel.Encounter.State;

/**
 * This class will handle all the probability of creating and handling
 * encounters.
 *
 * @author Caleb
 */
public class EncounterManager {

    private static final Random rand = new Random();

    private final Queue<Encounter> encounters;
    private final int numTotalEncounters;

    private final Player player;
    private final Planet destination;

    private boolean raided = false;
    private boolean inspected = false;

    /**
     * Creates queue with all Encounters for one transition period
     *
     * @param source source planet
     * @param destination destination planet
     * @param ship ship Player is using
     * @param player Player in the game
     */
    public EncounterManager(Planet source, Planet destination, PlayerShip ship, Player player) {
        this.player = player;
        this.destination = destination;
        
        int probabilityDenominator = 40; //the larger this number, the more unlikely encounters are
        if (player.getShip().getType() == ShipType.FLEA) { //encounters are half as likely if you're in a flea
            probabilityDenominator *= 2;
        }
        //set up encounter type distribution
        int[] encounterProbs = new int[3];
        encounterProbs[0] = destination.getPoliticalSystem().strengthPirates();
        encounterProbs[1] = destination.calculateStrengthOfPolice(player.getPoliceRecord());
        encounterProbs[2] = destination.getPoliticalSystem().strengthTraders();
        
        encounters = new LinkedList<>(); //create the queue of encounters.

        final int TOTAL_CLICKS = 21;
        
        for (int i = 0; i < TOTAL_CLICKS; i++) {
            int index = Tools.pickIndexFromWeightedList(encounterProbs, probabilityDenominator);
            if (index == 0 && raided) {
                index = 1;
            }
            
            Encounter encounter = null;
            if (index == 0) {
                encounter = createPoliceEncounter(TOTAL_CLICKS - i);
            } else if (index == 1) {
                encounter = createPirateEncounter(TOTAL_CLICKS - i);
            } else if (index == 2) {
                encounter = createTraderEncounter(TOTAL_CLICKS - i);
            }
            
            if (encounter != null) {
                boolean ignoredAndInvisible =
                        encounter.getState() == State.IGNORE && encounter.opponentIsCloaked();
                if (!ignoredAndInvisible) {
                    encounters.add(encounter);
                }
                if (encounter.getState() == State.INSPECTION) {
                    inspected = true;
                }
            }
        }
        numTotalEncounters = encounters.size();
    }

    /**
     * Creates a police encounter
     *
     * @return Specific Police Encounter
     */
    private Encounter createPoliceEncounter(int clicks) {
        int police = destination.getPoliticalSystem().strengthPolice();
        PoliceEncounter encounter = new PoliceEncounter(player, clicks, police);

        if (encounter.getState() == State.INSPECTION && inspected) {
            return null;
        } else {
            return encounter;
        }
    }

    /**
     * Creates a pirate encounter
     *
     * @return Specific Pirate Encounter
     */
    private Encounter createPirateEncounter(int clicks) {
        int pirates = destination.getPoliticalSystem().strengthPirates();
        return new PirateEncounter(player, clicks, pirates);
    }

    /**
     * Creates a trader encounter
     *
     * @return Specific Trader Encounter
     */
    private Encounter createTraderEncounter(int clicks) {
        int traders = destination.getPoliticalSystem().strengthTraders();
        return new TraderEncounter(player, clicks, traders, destination.getMarket());
    }
    
    /**
     * returns newest encounter and pops it off the ArrayList holding all
     * encounters
     *
     * @return latest encounter
     */
    public Encounter getNextEncounter() {
        return encounters.poll();
    }

    /**
     * returns the total number of encounters returned by LinkedList
     *
     * @return total encounters for warp
     */
    public int getNumTotalEncounters() {
        return numTotalEncounters;
    }

    /**
     * returns the number of encounters remaining in current Warp
     *
     * @return number encounters remaining
     */
    public int getEncountersRemaining() {
        return encounters.size();
    }
}
