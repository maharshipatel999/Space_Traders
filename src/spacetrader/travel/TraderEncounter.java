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
import spacetrader.commerce.Market;
import spacetrader.commerce.TradeGood;
import spacetrader.planets.Planet;
import spacetrader.ships.ShipType;
import spacetrader.ships.SpaceShip;

/**
 * Represents an encounter with a trader.
 *
 * @author Caleb
 */
public class TraderEncounter extends Encounter {

    public static final int ATTACK_TRADER_SCORE = -2;
    public static final int PLUNDER_TRADER_SCORE = -2;
    public static final int KILL_TRADER_SCORE = -4;

    public static final String ATTACK_CONFIRM_MSG
            = "Attacking another trader is a felony and will worsen your relationship"
            + "with the police.";

    private final Market destinationMarket;

    /**
     * Creates a new TraderEncounter
     *
     * @param player the player of the game
     * @param clicks the distance from the destination the encounter occurred
     * @param source the origin planet
     * @param destination the destination planet
     */
    public TraderEncounter(Player player, int clicks, Planet source,
            Planet destination) {
        super(player, "/spacetrader/travel/TraderEncounterScreen.fxml", clicks, "Trader", source, destination);
        
        this.destinationMarket = destination.getMarket();
        this.plunderScore = PLUNDER_TRADER_SCORE;

        int tries = 1;
        double cargoModifier = 1;

        this.setOpponent(createShip(tries, ShipType.FLEA, cargoModifier));
        determineState();
    }

    /**
     * Determines the state of this encounter randomly, based on players current
     * statistics.
     */
    public final void determineState() {
        boolean isCriminal = (getPlayer().getPoliceRecord().compareTo(PoliceRecord.CRIMINAL) <= 0);

        boolean tooIntimidating = isCriminal && playerReputationTooHigh();

        // If you're a criminal, traders tend to flee if you've got at least some reputation
        if (!playerIsCloaked() && !opponentIsCloaked() && tooIntimidating) {
            state = new FleeState(this);
        } else if (rand.nextInt(1000) < 100) { // Will there be trade in orbit?
            if (!getPlayer().getCargo().isFull() && hasTradeableGoods(new TradeState(this, false))) {
                state = new TradeState(this, false);
            } else if (hasTradeableGoods(new TradeState(this, true))) {
                // we fudge on whether the trader has capacity to carry the stuff he's buying.
                state = new TradeState(this, true);
            }
        }
    }

    /**
     * Randomly calculates if the player's reputation is high enough to
     * intimidate the opponent
     *
     * @return true if the trader is intimidated
     */
    public boolean playerReputationTooHigh() {
        int randomScore = rand.nextInt(Reputation.ELITE.minRep());
        int weightedRepScore = (getPlayer().getReputationScore() * ShipType.values().length) / (1 + getOpponent().getType().ordinal());

        return (randomScore <= weightedRepScore);
    }

    /**
     * Checks to see if the specific Trader Ship is legal in current Planet
     *
     * @param type Type of Ship
     * @return Whether Trader Ship is legal
     */
    @Override
    public boolean isIllegalShipType(ShipType type) {
        final int TRADER_STRENGTH = getDestination().getPoliticalSystem().strengthTraders();
        return type.trader() < 0 || TRADER_STRENGTH < type.trader();
    }

    @Override
    public void increaseShipsKilled() {
        getPlayer().increaseTraderKills();
        getPlayer().setPoliceRecordScore(getPlayer().getPoliceRecordScore() + KILL_TRADER_SCORE);
    }

    @Override
    public int getOpponentBounty() {
        return 0; //traders have no bounty
    }

    @Override
    protected boolean canBeScoopedFrom() {
        return true;
    }

    @Override
    protected boolean playerCanSurrender() {
        return false;
    }

    /**
     * Determines if a space ship has goods that can be sold/bought in the
     * system the player is in/traveling to.
     *
     * @param ship the ship which has a cargo
     * @param state whether the player is buying or selling
     * @return true if the ship has sellable/purchasable goods
     */
    private boolean hasTradeableGoods(TradeState state) {
        for (TradeGood good : TradeGood.values()) {
            if (isTradeable(good, state)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if a particular good is tradeable given whether the trader is
     * buying or selling.
     */
    private boolean isTradeable(TradeGood good, TradeState state) {
        SpaceShip ship = (state.isTraderBuying()) ? getPlayer().getShip() : getOpponent();

        boolean isTradeable = false;
        if (ship.getCargo().getQuantity(good) > 0) {
            boolean badRecord = getPlayer().getPoliceRecord().compareTo(PoliceRecord.DUBIOUS) < 0;
            boolean isLegalGood = (good != TradeGood.FIREARMS) && (good != TradeGood.NARCOTICS);

            // Criminals can only buy illegal goods; noncriminals can only buy legal goods.
            boolean playerCanTrade = badRecord ? !isLegalGood : isLegalGood;

            //if trader is buying, good must be sellable; if selling, good must be buyable
            boolean traderCanTrade = (state.isTraderBuying()) ? destinationMarket.isSellable(good)
                    : destinationMarket.isBuyable(good);

            isTradeable = (playerCanTrade && traderCanTrade);
        }

        return isTradeable;
    }

    /**
     * Finds a trade good that is on a given ship that can be sold in a given
     * system.
     *
     * @return a randomly chosen tradeable good
     */
    public TradeGood getRandomTradeableItem() {
        TradeGood good = null;
        while (good == null) {
            TradeGood potentialGood = TradeGood.getRandomTradeGood();

            if (isTradeable(potentialGood, (TradeState) getState())) {
                good = potentialGood;
            }
        }
        // if we didn't succeed in picking randomly, we'll pick sequentially. We can do this, because
        // this routine is only called if there are tradeable goods.
        /*if (good == null) {
         j = 0;
         foundTradeableGood = true;
         while (foundTradeableGood)
         {
         // see lengthy comment above.
         if (isTradeable(TradeGood.values()[j], getEncounterState())) {
         foundTradeableGood = false; 
         } else {
         j++;
         if (j == MAXTRADEITEM) {
         // this should never happen!
         foundTradeableGood = false;
         }
         }
         }
         }*/
        return good;
    }

    /**
     * Determines a randomly chosen price for the trader to offer for a good.
     *
     * @param good the TradeGood which is being traded
     * @return a price for the good to be traded
     */
    public int getRandomPrice(TradeGood good) {
        int price;
        if (((TradeState) getState()).isTraderBuying()) {
            price = destinationMarket.getSellPrice(good);
        } else {
            price = destinationMarket.getBuyPrice(good);
        }

        if (good == TradeGood.NARCOTICS || good == TradeGood.FIREARMS) {
            double lessLikelyModifer;
            double moreLikelyModifer;
            if (((TradeState) getState()).isTraderBuying()) {
                lessLikelyModifer = 0.8;
                moreLikelyModifer = 1.1;
            } else {
                lessLikelyModifer = 1.1;
                moreLikelyModifer = 0.8;
            }
            price *= (rand.nextInt(100) <= 45) ? (lessLikelyModifer) : (moreLikelyModifer);
        } else {
            double lessLikelyModifer;
            double moreLikelyModifer;
            if (((TradeState) getState()).isTraderBuying()) {
                lessLikelyModifer = 0.9;
                moreLikelyModifer = 1.1;
            } else {
                lessLikelyModifer = 1.1;
                moreLikelyModifer = 0.9;
            }
            price *= (rand.nextInt(100) <= 10) ? (lessLikelyModifer) : (moreLikelyModifer);
        }

        price /= good.roundoff();
        if (((TradeState) getState()).isTraderBuying()) {
            price++;
        }
        price *= good.roundoff();

        //enfore price limits
        price = Math.max(price, good.minTraderPrice());
        price = Math.min(price, good.maxTraderPrice());
        return price;
    }

    @Override
    public String toString() {
        return "-Trader Encounter-\n" + super.toString();
    }

    @Override
    protected boolean opponentIntimidated() {
        boolean opponentHasNoWeapons = (getOpponent().getTotalWeaponStrength() <= 0);
        return opponentHasNoWeapons || playerReputationTooHigh();
    }

    /**
     * Generally if the trader was really damaged, he'll usually surrender but
     * sometimes he'll flee. If the trader was slightly damaged, he'll flee more
     * if the player was also damaged. The trader never flees if the player
     * wasn't damaged.
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
            nextState = new SurrenderState(this);
            chanceOfFleeing = 40;
        } else if (opponentHull < (originalOpponentHull * 9 / 10)) {
            if (playerHull < (originalPlayerHull * 2 / 3)) {
                chanceOfFleeing = 20;
            } else if (playerHull < (originalPlayerHull * 9 / 10)) {
                chanceOfFleeing = 60;
            } else {
                chanceOfFleeing = 100;
            }
        } else {
            chanceOfFleeing = 0; //trader wasn't damaged so never flee
        }

        if (rand.nextInt(100) < chanceOfFleeing) {
            nextState = new FleeState(this);
        }
        
        return nextState;
    }
}
