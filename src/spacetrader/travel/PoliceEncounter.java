/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import spacetrader.Player;
import spacetrader.PoliceRecord;
import spacetrader.Reputation;
import spacetrader.Tools;
import static spacetrader.Tools.rand;
import spacetrader.commerce.TradeGood;
import spacetrader.planets.Planet;
import spacetrader.ships.ShipType;

/**
 * Represents an encounter with the police.
 *
 * @author Caleb
 */
public class PoliceEncounter extends Encounter {

    public static final int ATTACK_POLICE_SCORE = -3;
    public static final int KILL_POLICE_SCORE = -6;

    public static final int TRAFFICKING_PENALTY = -1;
    public static final int FLEE_FROM_INSPECTION = -2;

    private static final int MINIMUM_FINE_AMOUNT = 100;
    private static final int MAXIMUM_FINE_AMOUNT = 10000;
    private static final int FINE_DECREASE = 4 * 10;
    private static final int FINE_ROUND = 50;

    private static final double BAD_RECORD_CHANCE_OF_INSPECT = 1;
    private static final double CLEAN_RECORD_CHANCE_OF_INSPECT = .10;
    private static final double LAWFUL_RECORD_CHANCE_OF_INSPECT = .025;

    /**
     * Creates a new police encounter.
     *
     * @param player the player of the game
     * @param clicks the distance from the destination the encounter occurred
     * @param source the origin planet
     * @param destination the destination planet
     */
    public PoliceEncounter(Player player, int clicks, Planet source,
            Planet destination) {
        super(player, "/spacetrader/travel/PoliceEncounterScreen.fxml", clicks, "Police", source, destination);

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
     * Determines the state of this encounter randomly, based on players current
     * statistics.
     */
    public final void determineState() {
        if (playerIsCloaked()) {
            return; //by default, state is ignore
        }
        final PoliceRecord playerRecord = getPlayer().getPoliceRecord();
        final boolean playerShipIsWorse
                = getPlayer().getShip().getType().compareTo(getOpponent().getType()) < 0;
        final boolean hasAverageRep
                = getPlayer().getReputation().compareTo(Reputation.AVERAGE) < 0;
        final int randomScore = rand.nextInt(Reputation.ELITE.minRep());
        final int attackingProbability
                = getPlayer().getReputationScore() / (1 + getOpponent().getType().ordinal());

        double chanceOfInspection = 0;

        if (playerRecord.ordinal() < PoliceRecord.DUBIOUS.ordinal()) {
            //If you're a criminal, the police will tend to attack
            if (!opponentIsCloaked() && getOpponent().getTotalWeaponStrength() <= 0) {
                state = new FleeState(this);
            }
            if (hasAverageRep || randomScore > attackingProbability) {
                state = new AttackState(this);
            } else if (!opponentIsCloaked()) {
                state = new FleeState(this);
            }

            // if you're suddenly stuck in a lousy ship, Police won't flee even if you have a fearsome reputation.
            if (state instanceof FleeState && playerShipIsWorse) {
                state = new AttackState(this);
            }
        } else if (playerRecord.ordinal() < PoliceRecord.CLEAN.ordinal()) {
            chanceOfInspection = BAD_RECORD_CHANCE_OF_INSPECT;
        } else if (playerRecord.ordinal() < PoliceRecord.LAWFUL.ordinal()) {
            chanceOfInspection = CLEAN_RECORD_CHANCE_OF_INSPECT;
        } else {
            chanceOfInspection = LAWFUL_RECORD_CHANCE_OF_INSPECT;
        }

        if (rand.nextDouble() < chanceOfInspection) {
            state = new InspectionState(this);
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
        final int POLICE_STRENGTH = getDestination().getPoliticalSystem().strengthPolice();
        return type.police() < 0 || POLICE_STRENGTH < type.police();
    }

    @Override
    public void increaseShipsKilled() {
        getPlayer().increasePoliceKills();
        getPlayer().setPoliceRecordScore(getPlayer().getPoliceRecordScore() + KILL_POLICE_SCORE);
    }

    @Override
    protected boolean canBeScoopedFrom() {
        return false;
    }

    @Override
    protected boolean playerCanSurrender() {
        return true;
    }

    /**
     * Determines if the player is carrying illegal goods. If he is, fines him
     * and takes the illegal goods. If not, the player is free to go. Updates
     * the player's police record accordingly.
     *
     * @return true if the player was caught carrying illegal goods, false
     *         otherwise
     */
    public boolean inspectPlayer() {
        //determine if player is carrying illegal goods
        boolean carryingIllegals = getPlayer().getShip().isCarryingIllegalGoods();
        int newRecord = getPlayer().getPoliceRecordScore();

        if (carryingIllegals) {
            newRecord += TRAFFICKING_PENALTY;

            //fine the player
            getPlayer().removeCreditsForced(determineFine());

            //remove the illegal goods
            getPlayer().getCargo().clearItem(TradeGood.FIREARMS);
            getPlayer().getCargo().clearItem(TradeGood.NARCOTICS);
        } else {
            newRecord -= TRAFFICKING_PENALTY;
        }
        //update police record
        getPlayer().setPoliceRecordScore(newRecord);

        return carryingIllegals;
    }

    /**
     * Determined the amount to fine the player for carrying illegal goods.
     *
     * @return the amount of the fine
     */
    /**
     * Determined the amount to fine the player for carrying illegal goods.
     *
     * @return the amount of the fine
     */
    public int determineFine() {
        int fine = getPlayer().getCurrentWorth() / FINE_DECREASE;
        //round the fine up to the nearest ROUND_AMOUNT
        if ((fine % FINE_ROUND) != 0) {
            fine += FINE_ROUND - (fine % FINE_ROUND);
        }
        return Tools.applyBounds(fine, MINIMUM_FINE_AMOUNT, MAXIMUM_FINE_AMOUNT);
    }

    /**
     * Calculates the amount of bribe, police on the player's planet will ask
     * for.
     *
     * @param destination
     * @return the bribe the police are asking for
     */
    public int calculcateBribe(Planet destination) {
        final int MIN_BRIBE = 100;
        final int MAX_BRIBE = 10000;
        final int ROUND_BRIBE = 100;  //what place to round the bribe to

        int bribe = getPlayer().getCurrentWorth();
        bribe /= 10 + (10 * destination.getPoliticalSystem().bribeLevel());

        //round up to nearest hundred place
        if (bribe % ROUND_BRIBE != 0) {
            bribe += (ROUND_BRIBE - (bribe % ROUND_BRIBE));
        }
        //enforce upper and lower bounds
        return Tools.applyBounds(bribe, MIN_BRIBE, MAX_BRIBE);
    }
    
    /**
     * This is called whenever the player attacks the police. Lowers the player's
     * police record.
     */
    protected void updateRecordFromAttacking() {
        //the player's police record should be no greater than the crook score.
        int updatedRecord = Math.min(getPlayer().getPoliceRecordScore(), PoliceRecord.CROOK.minScore());
        getPlayer().setPoliceRecordScore(updatedRecord + PoliceEncounter.ATTACK_POLICE_SCORE);
    }

    /**
     * Updates the player's police record after they flee from an inspection.
     */
    protected void updateRecordFleeFromInspection() {
        int updatedRecord;
        if (getPlayer().getPoliceRecord().compareTo(PoliceRecord.DUBIOUS) >= 0) {
            updatedRecord = PoliceRecord.DUBIOUS.minScore() - 1;
        } else {
            int currentRecord = getPlayer().getPoliceRecordScore();
            updatedRecord = currentRecord + PoliceEncounter.FLEE_FROM_INSPECTION;
        }
        getPlayer().setPoliceRecordScore(updatedRecord);
    }

    /**
     * Generally the police will only consider fleeing if they're really
     * damaged. If the trader is also really damaged, they will sometimes flee,
     * otherwise they will always flee.
     *
     * @return the next state, or null
     */
    @Override
    protected EncounterState determineStateChange(int originalPlayerHull,
            int originalOpponentHull) {
        EncounterState nextState = null;
        int playerHull = getPlayer().getShip().getHullStrength();
        int opponentHull = getOpponent().getHullStrength();

        int chanceOfFleeing; //percent probability that opponent flees

        if (opponentHull < (originalOpponentHull / 2)) {
            if (playerHull < (originalPlayerHull / 2)) {
                chanceOfFleeing = 40;
            } else {
                chanceOfFleeing = 100;
            }
        } else {
            chanceOfFleeing = 0;
        }
        if (rand.nextInt(100) < chanceOfFleeing) {
            nextState = new FleeState(this);
        }
        return nextState;
    }

    @Override
    public String toString() {
        return "-Police Encounter-\n" + super.toString();
    }
}
