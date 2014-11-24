/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import spacetrader.Player;
import spacetrader.Reputation;
import static spacetrader.Tools.rand;
import spacetrader.ships.ShipType;

/**
 * Represents an encounter with a pirate.
 *
 * @author Caleb
 */
public class PirateEncounter extends Encounter {

    public static final int ATTACK_PIRATE_SCORE = 0;
    public static final int KILL_PIRATE_SCORE = 1;
    public static final int PLUNDER_PIRATE_SCORE = -1;
    
    private final int pirateStrength;

    /**
     * Creates a new pirate encounter.
     *
     * @param player the player of the game
     * @param clicks
     * @param pirateStrength
     */
    public PirateEncounter(Player player, int clicks, int pirateStrength) {
        super(player, "/spacetrader/travel/PirateEncounterScreen.fxml", clicks, "Pirate");
        this.pirateStrength = pirateStrength;
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
            state = State.ATTACK;
        } else {
            state = State.FLEE;
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
        return type.pirate() < 0 || pirateStrength < type.pirate();
    }

    @Override
    public String toString() {
        return "-Pirate Encounter-\n" + super.toString();
    }
}
