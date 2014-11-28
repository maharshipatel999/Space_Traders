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
import spacetrader.ships.ShipType;
import spacetrader.ships.SpaceShip;
import spacetrader.system.MainController;

/**
 * Represents an encounter with a trader.
 *
 * @author Caleb
 */
public class TraderEncounter extends Encounter {
    
    public static final int ATTACK_TRADER_SCORE = -2;
    public static final int PLUNDER_TRADER_SCORE = -2;
    public static final int KILL_TRADER_SCORE = -4;
    
    public static final String ATTACK_CONFIRM_MSG = 
            "Attacking another trader is a felony and will worsen your relationship"
            + "with the police.";

    private final int traderStrength;
    protected final Market destinationMarket;

    /**
     * Creates a new TraderEncounter
     *
     * @param player the player of the game
     * @param clicks
     * @param traderStrength
     * @param destinationMarket
     */
    public TraderEncounter(Player player, int clicks, int traderStrength, Market destinationMarket) {
        super(player, "/spacetrader/travel/TraderEncounterScreen.fxml", clicks, "Trader");
        this.traderStrength = traderStrength;
        this.destinationMarket = destinationMarket;
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
            state = State.FLEE;
        } else if (rand.nextInt(1000) < 100) { // Will there be trade in orbit?
            if (!getPlayer().getCargo().isFull() && hasTradeableGoods(State.SELL)) {
                state = State.SELL;
            }
            // we fudge on whether the trader has capacity to carry the stuff he's buying.
            if (hasTradeableGoods(State.BUY) && state != State.SELL) {
                state = State.BUY;
            }
        }
    }
    
    /**
     * Randomly calculates if the player's reputation is high enough to intimidate
     * the opponent
     * 
     * @return true if the trader is intimidated
     */
    public boolean playerReputationTooHigh() {
        int randomScore = rand.nextInt(Reputation.ELITE.minRep());
        int fleeingProbability = (getPlayer().getReputationScore() * 10) / (1 + getOpponent().getType().ordinal());
        
        return (randomScore <= fleeingProbability);
    }
    
    /**
     * Checks to see if the specific Trader Ship is legal in current Planet
     *
     * @param type Type of Ship
     * @return Whether Trader Ship is legal
     */
    @Override
    public boolean isIllegalShipType(ShipType type) {
        return type.trader() < 0 || traderStrength < type.trader();
    }
    
    @Override
    protected void handleSurrender(MainController mainControl) {
        throw new UnsupportedOperationException("You cannot surrender to a Trader");
    }

    /**
     * Determines if a space ship has goods that can be sold/bought in the
     * system the player is in/traveling to.
     *
     * @param ship the ship which has a cargo
     * @param state whether the player is buying or selling
     * @return true if the ship has sellable/purchasable goods
     */
    private boolean hasTradeableGoods(State state) {
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
    private boolean isTradeable(TradeGood good, State state) {
        SpaceShip ship = (state == State.BUY) ? getPlayer().getShip() : getOpponent();

        boolean isTradeable = false;
        if (ship.getCargo().getQuantity(good) > 0) {
            boolean badRecord = getPlayer().getPoliceRecord().compareTo(PoliceRecord.DUBIOUS) < 0;
            boolean isLegalGood = (good != TradeGood.FIREARMS) && (good != TradeGood.NARCOTICS);

            // Criminals can only buy illegal goods; noncriminals can only buy legal goods.
            boolean playerCanTrade = badRecord ? !isLegalGood : isLegalGood;

            //if trader is buying, good must be sellable; if selling, good must be buyable
            boolean traderCanTrade = (state == State.BUY) ? destinationMarket.isSellable(good)
                    : destinationMarket.isBuyable(good);

            isTradeable = (playerCanTrade && traderCanTrade);
        }
        
        return isTradeable;
    }
    
    /**
     * Finds a trade good that is on a given ship that can be sold in a given system.
     * 
     * @return a randomly chosen tradeable good
     */
    public TradeGood getRandomTradeableItem() {
        TradeGood good = null;
        while (good == null) {
            TradeGood potentialGood = TradeGood.getRandomTradeGood();
            
            if (isTradeable(potentialGood, getState())) {
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
                if (isTradeable(TradeGood.values()[j], getState())) {
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
        if (getState() == State.BUY) {
            price = destinationMarket.getSellPrice(good);
        } else {
            price = destinationMarket.getBuyPrice(good);
        }
        
        if (good == TradeGood.NARCOTICS || good == TradeGood.FIREARMS) {
            if (getState() == State.BUY) {
                price *= (rand.nextInt(100) <= 45) ? (0.8) : (1.1);
            } else {
                price *= (rand.nextInt(100) <= 45) ? (1.1) : (0.8);
            }
        } else {
            if (getState() == State.BUY) {
                price *= (rand.nextInt(100) <= 10) ? (0.9) : (1.1);
            } else {
                price *= (rand.nextInt(100) <= 10) ? (1.1) : (0.9);
            }
        }
        
        price /= good.roundoff();
        if (getState() == State.BUY) {
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
}
