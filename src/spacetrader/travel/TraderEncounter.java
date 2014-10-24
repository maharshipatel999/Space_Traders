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
 * Represents an ecnounter with a trader.
 * @author Caleb
 */
public class TraderEncounter extends Encounter {
    
    /**
     * Creates a new TraderEncounter
     * @param player the player of the game
     */
    public TraderEncounter(Player player) {
        super(player, "/spacetrader/travel/TraderEncounterScreen.fxml");
    }
    
    @Override
    public boolean isLegalShipType(ShipType type, PoliticalSystem politics) {
        return type.trader() < 0 || politics.strengthTraders() < type.trader();
    }
    
}
