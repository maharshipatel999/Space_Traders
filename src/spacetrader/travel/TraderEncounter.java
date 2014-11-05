/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import spacetrader.Player;
import spacetrader.planets.PoliticalSystem;
import spacetrader.ships.ShipType;

/**
 * Represents an encounter with a trader.
 *
 * @author Caleb
 */
public class TraderEncounter extends Encounter {

    /**
     * Creates a new TraderEncounter
     *
     * @param player the player of the game
     */
    public TraderEncounter(Player player) {
        super(player, "/spacetrader/travel/TraderEncounterScreen.fxml");
    }

    /**
     * Checks to see if the specific Trader Ship is legal in current Planet
     * @param type Type of Ship
     * @param politics Politics of current Planet
     * @return Whether Trader Ship is legal
     */
    @Override
    public boolean isLegalShipType(ShipType type, PoliticalSystem politics) {
        return type.trader() < 0 || politics.strengthTraders() < type.trader();
    }

}
