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
import spacetrader.planets.Planet;
import spacetrader.ships.ShipType;

/**
 * Represents an encounter with a pirate.
 *
 * @author Caleb
 */
public class PirateEncounter extends Encounter {

    public static final String BLACKMAIL_MSG = "The pirates are very "
            + "angry that they find no cargo on your ship. To stop them from "
            + "destroying you, you have no choice but to pay them an amount equal "
            + "to 5%% of your current worth.";

    public static final int ATTACK_PIRATE_SCORE = 0;
    public static final int KILL_PIRATE_SCORE = 1;
    public static final int PLUNDER_PIRATE_SCORE = -1;

    private static final int MIN_BLACKMAIL = 500;
    private static final int MAX_BLACKMAIL = 25000;

    /**
     * Creates a new pirate encounter.
     *
     * @param player the player of the game
     * @param clicks the distance from the destination the encounter occurred
     * @param source the origin planet
     * @param destination the destination planet
     */
    public PirateEncounter(Player player, int clicks, Planet source,
            Planet destination) {
        super(player, "/spacetrader/travel/PirateEncounterScreen.fxml", clicks, "Pirate", source, destination);
        
        this.plunderScore = PLUNDER_PIRATE_SCORE;

        //pirates are strong if the player is worth more
        int tries = 1 + player.getCurrentWorth() / 100000;
        double cargoModifier = 0.5;

        this.setOpponent(createShip(tries, ShipType.GNAT, cargoModifier));
        determineState();
    }

    /**
     * Determines the state of this encounter randomly, based on players current
     * statistics.
     */
    public final void determineState() {
        if (playerIsCloaked()) {
            return; //by default, state is ignore
        }

        final boolean strongEnemyShip = (getOpponent().getType().compareTo(ShipType.GRASSHOPPER) >= 0);
        final boolean playerShipIsWorse
                = getPlayer().getShip().getType().compareTo(getOpponent().getType()) < 0;

        final int randomScore = rand.nextInt(Reputation.ELITE.minRep());
        final int attackingProbability
                = (getPlayer().getReputationScore() * 4) / (1 + getOpponent().getType().ordinal());

        // Pirates will mostly attack, but they are cowardly: if your rep is too high, they tend to flee
        if (strongEnemyShip || playerShipIsWorse || (randomScore > attackingProbability)) {
            state = new AttackState(this);
        } else {
            state = new FleeState(this);
        }
    }

    /**
     * Checks to see if Pirate Ship can be used based on the type of police and
     * strength of Pirates in specific area
     *
     * @param type Type of Ship
     * @return whether or not Pirate Ship Type is legal
     */
    @Override
    public boolean isIllegalShipType(ShipType type) {
        final int PIRATE_STRENGTH = getDestination().getPoliticalSystem().strengthPirates();
        return type.pirate() < 0 || PIRATE_STRENGTH < type.pirate();
    }

    @Override
    public void increaseShipsKilled() {
        getPlayer().increasePirateKills();
        getPlayer().setPoliceRecordScore(getPlayer().getPoliceRecordScore() + KILL_PIRATE_SCORE);
    }

    @Override
    public int getOpponentBounty() {
        if (getPlayer().getPoliceRecord().compareTo(PoliceRecord.DUBIOUS) < 0) {
            return 0; //player can't get bounty if he has a bad police record
        } else {
            //the traderSkill won't be used, so put any value
            int bounty = getOpponent().currentShipPrice(-1);

            bounty /= 200;
            bounty /= 25;
            bounty *= 25;
            if (bounty <= 0) {
                bounty = 25;
            }
            if (bounty > 2500) {
                bounty = 2500;
            }

            return bounty;
        }
    }

    @Override
    protected boolean canBeScoopedFrom() {
        return true;
    }

    @Override
    protected boolean playerCanSurrender() {
        return true;
    }

    /**
     * Generally the police will only consider fleeing if they're really
     * damaged. If the trader is also really damaged they will sometimes flee,
     * otherwise they will most often flee, but occasionally they will
     * surrender.
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

        if (opponentHull < (originalOpponentHull * 2 / 3)) {
            if (playerHull < (originalPlayerHull * 2 / 3)) {
                chanceOfFleeing = 60;
            } else {
                nextState = new SurrenderState(this);
                chanceOfFleeing = 90;
            }
        } else {
            chanceOfFleeing = 0;
        }

        if (rand.nextInt(100) < chanceOfFleeing) {
            nextState = new FleeState(this);
        }
        
        return nextState;
    }
    
    /**
     * Determines the amount the pirates will blackmail the player if you
     * don't have any cargo for them to plunder.
     * @return 
     */
    protected int calculateBlackmail() {
        int blackmail = getPlayer().getCurrentWorth() / 20;
        blackmail = Tools.applyBounds(blackmail, MIN_BLACKMAIL, MAX_BLACKMAIL);
        return blackmail;
    }

    @Override
    public String toString() {
        return "-Pirate Encounter-\n" + super.toString();
    }
}
