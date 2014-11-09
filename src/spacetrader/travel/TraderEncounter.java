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

/**
 * Represents an encounter with a trader.
 *
 * @author Caleb
 */
public class TraderEncounter extends Encounter {

    private final int traderStrength;
    private final Market destinationMarket;
    
    /**
     * Creates a new TraderEncounter
     *
     * @param player the player of the game
     * @param traderStrength
     * @param destinationMarket
     */
    public TraderEncounter(Player player, int traderStrength, Market destinationMarket) {
        super(player, "/spacetrader/travel/TraderEncounterScreen.fxml");
        this.traderStrength = traderStrength;
        this.destinationMarket = destinationMarket;
        
        int tries = 1;
        double cargoModifier = 1;
        
        this.setOpponent(createShip(tries, ShipType.FLEA, cargoModifier));
        determineState();
    }

    /**
     * Determines the state of this encounter randomly, based on players
     * current statistics.
     */
    public final void determineState() {
        boolean isCriminal = (getPlayer().getPoliceRecord().compareTo(PoliceRecord.CRIMINAL) <= 0);
        
        int randomScore = rand.nextInt(Reputation.ELITE.minRep());
        int fleeingProbability = (getPlayer().getReputationScore() * 10) / (1 + getOpponent().getType().ordinal());
        
        boolean tooIntimidating = isCriminal && (randomScore <= fleeingProbability);
        
        // If you're a criminal, traders tend to flee if you've got at least some reputation
        if (!playerIsCloaked() && !opponentIsCloaked() && tooIntimidating) {
            state = State.FLEE;
        } else if (rand.nextInt(1000) < 100) { // Will there be trade in orbit?
            if (!getPlayer().getShip().getCargo().isFull() && hasTradeableGoods(getOpponent(), false, getPlayer())) {
                state = State.SELL;
            }
            // we fudge on whether the trader has capacity to carry the stuff he's buying.
            if (hasTradeableGoods(getPlayer().getShip(), true, getPlayer()) && state != State.SELL) {
                state = State.BUY;
            }
        }
    }
    
    private boolean hasTradeableGoods(SpaceShip ship, boolean traderBuying, Player player) {
	for (TradeGood good : TradeGood.values()) {
            // trade only if trader is selling and the item has a buy price on the local system
            // or if trader is buying and there is a sell price on the local system.
            if (ship.getCargo().getQuantity(good) > 0) {
                boolean isTradeable;
                if (traderBuying) {
                    isTradeable = destinationMarket.getSellPrice(good) > 0;
                } else {
                    isTradeable = destinationMarket.getBuyPrice(good) > 0;
                }

                // Criminals can only buy or sell illegal goods, Noncriminals cannot buy or sell such items.
                boolean badRecord = player.getPoliceRecord().compareTo(PoliceRecord.DUBIOUS) < 0;
                boolean legalGood = (good != TradeGood.FIREARMS) && (good != TradeGood.NARCOTICS);

                if ((badRecord && legalGood) || (!badRecord && !legalGood)) {
                    isTradeable = false;
                }

                if (isTradeable) {
                    return true;
                }
            }
        }
	return false;
    }

    /**
     * Checks to see if the specific Trader Ship is legal in current Planet
     * @param type Type of Ship
     * @return Whether Trader Ship is legal
     */
    @Override
    public boolean isIllegalShipType(ShipType type) {
        return type.trader() < 0 || traderStrength < type.trader();
    }
    
    @Override
    public String toString() {
        return "-Trader Encounter-\n" + super.toString();
    }
}
