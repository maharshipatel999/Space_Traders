/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import java.util.LinkedList;
import java.util.ListIterator;
import spacetrader.Player;
import spacetrader.Tools;
import spacetrader.planets.Planet;
import spacetrader.ships.PlayerShip;
import spacetrader.ships.ShipType;
import spacetrader.system.MainController;
import spacetrader.system.MainController.Debug;
import spacetrader.travel.Encounter.State;

/**
 * This class will handle all the probability of creating and handling
 * encounters.
 *
 * @author Caleb
 */
public class EncounterManager {

    private final LinkedList<Encounter> encounters;
    private final int numTotalEncounters;

    private final Player player;
    private final Planet source;
    private final Planet destination;

    private boolean raided = false;
    private boolean inspected = false;

    /**
     * Creates queue with all Encounters for one transition period.
     *
     * @param source source planet
     * @param destination destination planet
     * @param ship ship Player is using
     * @param player the game's player
     */
    public EncounterManager(Planet source, Planet destination, PlayerShip ship, Player player) {
        this.player = player;
        this.source = source;
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
            
            if (MainController.debugStatus != Debug.OFF) {
                switch (MainController.debugStatus) {
                    case POLICE_ENCOUNTER:
                        encounter = createPoliceEncounter(TOTAL_CLICKS - i);
                        break;
                    case PIRATE_ENCOUNTER:
                        encounter = createPirateEncounter(TOTAL_CLICKS - i);
                        break;
                    case TRADER_ENCOUNTER:
                        encounter = createTraderEncounter(TOTAL_CLICKS - i);
                        break;
                    default:
                        break;
                }
            } else {
                if (index == 0) {
                    encounter = createPirateEncounter(TOTAL_CLICKS - i);
                } else if (index == 1) {
                    encounter = createPoliceEncounter(TOTAL_CLICKS - i);
                } else if (index == 2) {
                    encounter = createTraderEncounter(TOTAL_CLICKS - i);
                }
            }

            if (checkIfAddEncounter(encounter)) {
                encounters.add(encounter);
            }
        }
        numTotalEncounters = encounters.size();
    }
    
    private boolean checkIfAddEncounter(Encounter encounter) {
        boolean addEncounter = false;
        
        if (encounter != null) {
            final boolean ignoredAndInvisible
                    = encounter.getState() == State.IGNORE && encounter.opponentIsCloaked();
            if (!ignoredAndInvisible) {
                addEncounter = true;
            }
            if (encounter.getState() == State.INSPECTION) {
                this.inspected = true;
            }
        }
        return addEncounter;
    }

    /**
     * Creates a new Police encounter. Returns null if it is an inspection
     * encounter and the player has already been inspected.
     *
     * @param param the distance to the destination planet
     * @return a new Police Encounter or null
     */
    private Encounter createPoliceEncounter(int clicks) {
        PoliceEncounter encounter = new PoliceEncounter(player, clicks, source, destination);

        if (encounter.getState() == State.INSPECTION && inspected) {
            return null;
        } else {
            return encounter;
        }
    }

    /**
     * Creates a new Pirate encounter
     *
     * @return Specific Pirate Encounter
     */
    private Encounter createPirateEncounter(int clicks) {
        return new PirateEncounter(player, clicks, source, destination);
    }

    /**
     * Creates a new Trader encounter.
     *
     * @return Specific Trader Encounter
     */
    private Encounter createTraderEncounter(int clicks) {
        return new TraderEncounter(player, clicks, source, destination);
    }

    /**
     * Gets the next encounter and pops it off the ArrayList holding all
     * encounters.
     *
     * @return the next encounter for the player, null if encounter should not be shown
     */
    public Encounter getNextEncounter() {
        return encounters.poll();
    }

    /**
     * Gets the total number of encounters this EncounterManager made.
     *
     * @return total encounters for warp
     */
    public int getNumTotalEncounters() {
        return numTotalEncounters;
    }

    /**
     * Gets the number of encounters remaining in current Warp.
     *
     * @return number of encounters remaining
     */
    public int getEncountersRemaining() {
        return encounters.size();
    }
    
    public void setRaided() {
        if (!raided) {
            raided = true;

            //Replace all the PirateEncounters with PoliceEncounters.
            ListIterator<Encounter> iterator = encounters.listIterator();
            while(iterator.hasNext()) {
                Encounter encounter = iterator.next();
                if (encounter instanceof PirateEncounter) {
                    iterator.remove();
                    Encounter police = createPoliceEncounter(encounter.clicksFromDest);
                    if (checkIfAddEncounter(police)) {
                        iterator.add(police);
                    }
                }
            }
        }
    }
}
