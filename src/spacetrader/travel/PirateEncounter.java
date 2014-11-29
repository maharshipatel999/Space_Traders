/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import spacetrader.Player;
import spacetrader.Reputation;
import spacetrader.Tools;
import static spacetrader.Tools.rand;
import spacetrader.commerce.Cargo;
import spacetrader.commerce.TradeGood;
import spacetrader.planets.Planet;
import spacetrader.ships.ShipType;
import spacetrader.system.MainController;

/**
 * Represents an encounter with a pirate.
 *
 * @author Caleb
 */
public class PirateEncounter extends Encounter {

    private static final String PIRATE_BLACKMAIL_MSG = "The pirates are very "
            + "angry that they find no cargo on your ship. To stop them from "
            + "destroying you, you have no choice but to pay them an amount equal "
            + "to 5% of your current worth.";
    
    public static final int ATTACK_PIRATE_SCORE = 0;
    public static final int KILL_PIRATE_SCORE = 1;
    public static final int PLUNDER_PIRATE_SCORE = -1;
    
    private static final int MIN_BLACKMAIL = 500;
    private static final int MAX_BLACKMAIL = 25000;
    
    private final int pirateStrength;

    /**
     * Creates a new pirate encounter.
     *
     * @param player the player of the game
     * @param clicks the distance from the destination the encounter occurred
     * @param source the origin planet
     * @param destination the destination planet
     */
    public PirateEncounter(Player player, int clicks, Planet source, Planet destination) {
        super(player, "/spacetrader/travel/PirateEncounterScreen.fxml", clicks, "Pirate", source, destination);
        this.pirateStrength = destination.getPoliticalSystem().strengthPirates();
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
    protected void handleSurrender(MainController mainControl) {
        Player player = getPlayer();
        int totalCargo = player.getCargo().getCount();
        //Determine how much cargo the player has
        if (totalCargo <= 0) {
            int blackmail = Tools.applyBounds(player.getCurrentWorth() / 20, MIN_BLACKMAIL, MAX_BLACKMAIL);
            mainControl.displayInfoMessage(null, "Pirates found no cargo!", PIRATE_BLACKMAIL_MSG);
            player.removeCreditsForced(blackmail);
        } else {
            mainControl.displayInfoMessage(null, "You've been plundered!", "The pirates steal from you what they can carry, but at least you get out of it alive.");								
            Cargo otherCargo = getOpponent().getCargo();
            // Pirates steal everything					
            if (otherCargo.getRemainingCapacity() >= totalCargo) {
                player.getCargo().clearAllItems();
            } else {		
                // Pirates steal a lot
                while (otherCargo.getRemainingCapacity() > 0) {
                    int i = rand.nextInt(player.getCargo().getTradeGoods().size());
                    TradeGood good = player.getCargo().getTradeGoods().get(i);
                    player.getCargo().removeItem(good, 1);
                    otherCargo.addItem(good, 1, 0);
                }
            }
        }
        mainControl.setPlayerWasRaided();
    }

    @Override
    public String toString() {
        return "-Pirate Encounter-\n" + super.toString();
    }
}
