/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import spacetrader.Player;
import spacetrader.PoliceRecord;
import spacetrader.Reputation;
import static spacetrader.Tools.rand;
import spacetrader.commerce.TradeGood;
import spacetrader.ships.ShipType;

/**
 * Represents an encounter with the police.
 *
 * @author Caleb
 */
public class PoliceEncounter extends Encounter {

    public static final int ATTACK_POLICE_SCORE = -3;
    public static final int KILL_POLICE_SCORE = -6;
    public static final int ATTACK_TRADER_SCORE = -2;
    public static final int PLUNDER_TRADER_SCORE = -2;
    public static final int KILL_TRADER_SCORE = -4;
    public static final int ATTACK_PIRATE_SCORE = 0;
    public static final int KILL_PIRATE_SCORE = 1;
    public static final int PLUNDER_PIRATE_SCORE = -1;
    public static final int TRAFFICKING = -1;
    public static final int FLEE_FROM_INSPECTION = -2;

    private static final int MINIMUM_FINE_AMOUNT = 100;
    private static final int MAXIMUM_FINE_AMOUNT = 10000;
    private static final int FINE_DECREASE = 4 * 10;
    private static final int FINE_ROUND = 50;
    
    
    private static final double BAD_RECORD_CHANCE_OF_INSPECT = 1;
    private static final double CLEAN_RECORD_CHANCE_OF_INSPECT = .10;
    private static final double LAWFUL_RECORD_CHANCE_OF_INSPECT = .025; 

    private final int policeStrength;
    
    /**
     * Creates a new police encounter.
     *
     * @param player the player of the game
     */
    public PoliceEncounter(Player player, int policeStrength) {
        super(player, "/spacetrader/travel/PoliceEncounterScreen.fxml");
        this.policeStrength = policeStrength;
        
        int tries = 1;
        if (player.getPoliceRecord().compareTo(PoliceRecord.CRIMINAL) < 0) {
            tries = 3;
        } else if (player.getPoliceRecord().compareTo(PoliceRecord.VILLAIN) < 0) {
            tries = 5;
        }
        double cargoModifer = 0; //police should not have any cargo

        this.setOpponent(createShip(tries, ShipType.GNAT, cargoModifer));
        this.determineState();
    }
    
    /**
     * Determines the state of this encounter randomly, based on players
     * current statistics.
     */
    public final void determineState() {
        if (playerIsCloaked()) {
            return; //by default, state is ignore
        }
        final PoliceRecord playerRecord = getPlayer().getPoliceRecord();
        final boolean playerShipIsWorse =
                getPlayer().getShip().getType().compareTo(getOpponent().getType()) < 0;
        final boolean hasAverageRep =
                getPlayer().getReputation().compareTo(Reputation.AVERAGE) < 0;
        final int randomScore = rand.nextInt(Reputation.ELITE.minRep());
        final int attackingProbability =
                getPlayer().getReputationScore() / (1 + getOpponent().getType().ordinal());

        double chanceOfInspection = 0;

        if (playerRecord.ordinal() < PoliceRecord.DUBIOUS.ordinal()) {
            //If you're a criminal, the police will tend to attack
            if (!opponentIsCloaked() && getOpponent().getTotalWeaponStrength() <= 0) {
                state = State.FLEE;
            }
            if (hasAverageRep || randomScore > attackingProbability) {
                state = State.ATTACK;
            } else if (!opponentIsCloaked()) {
                state = State.FLEE;
            }

            // if you're suddenly stuck in a lousy ship, Police won't flee even if you have a fearsome reputation.
            if (state == State.FLEE && playerShipIsWorse) {
                state = State.ATTACK;
            }
        } else if (playerRecord.ordinal() < PoliceRecord.CLEAN.ordinal()) {
            chanceOfInspection = BAD_RECORD_CHANCE_OF_INSPECT;
        } else if (playerRecord.ordinal() < PoliceRecord.LAWFUL.ordinal()) {
            chanceOfInspection = CLEAN_RECORD_CHANCE_OF_INSPECT;
        } else {
            chanceOfInspection = LAWFUL_RECORD_CHANCE_OF_INSPECT;
        }
        
        if (rand.nextDouble() < chanceOfInspection) {
            state = State.INSPECTION;
        } 
    }

    /**
     * Checks to see if Police Ship can be used based on the type of police and
     * strength of Police in specific area
     *
     * @param type Type of Ship
     * @return whether or not Police Ship Type is legal
     */
    @Override
    public boolean isIllegalShipType(ShipType type) {
        return type.police() < 0 || policeStrength < type.police();
    }

    /**
     * Determines if the player is carrying illegal goods. If he is, fines him
     * and takes the illegal goods. If not, the player is free to go. Updates
     * the player's police record accordingly.
     *
     * @return true if the player was caught carrying illegal goods, false
     * otherwise
     */
    public boolean inspectPlayer() {
        //determine if player is carrying illegal goods
        if (getPlayer().getShip().isCarryingIllegalGoods()) {
            //calculate the player's fine
            int fine = getPlayer().getCurrentWorth() / FINE_DECREASE;
            //round the fine up to the nearest ROUND_AMOUNT
            if ((fine % FINE_ROUND) != 0) {
                fine += FINE_ROUND - (fine % FINE_ROUND);
            }
            fine = Math.min(fine, MAXIMUM_FINE_AMOUNT);
            fine = Math.max(fine, MINIMUM_FINE_AMOUNT);

            getPlayer().getWallet().removeForcefully(fine);

            //remove the illegal goods
            getPlayer().getShip().getCargo().clearItem(TradeGood.FIREARMS);
            getPlayer().getShip().getCargo().clearItem(TradeGood.NARCOTICS);

            //update police record
            int newRecord = getPlayer().getPoliceRecordScore() + TRAFFICKING;
            getPlayer().setPoliceRecordScore(newRecord);
            return true;
        } else {
            int newRecord = getPlayer().getPoliceRecordScore() - TRAFFICKING;
            getPlayer().setPoliceRecordScore(newRecord);
            return false;
        }
    }

    /**
     * Calculates the amount of bribe, police on the player's planet will ask
     * for.
     *
     * @return the bribe the police are asking for
     */
    public int calculcateBribe() {
        int bribe = getPlayer().getCurrentWorth();
        bribe /= 10 + (10 * getPlayer().getLocation().getPoliticalSystem().bribeLevel()); //This should be the destination planet, not the current planet.
        return bribe;
    }

    @Override
    public String toString() {
        return "-Police Encounter-\n" + super.toString();
    }
}
