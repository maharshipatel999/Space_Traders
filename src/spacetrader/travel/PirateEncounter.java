/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.travel;

import spacetrader.Player;
import spacetrader.PoliticalSystem;
import spacetrader.ShipType;

/**
 * Represents an encounter with a pirate.
 * @author Caleb
 */
public class PirateEncounter extends Encounter {
    
    /**
     * Creates a new pirate encounter.
     * @param player the player of the game
     */
    public PirateEncounter(Player player) {
        super(player, "/spacetrader/travel/PirateEncounterScreen.fxml");
    }
    
    @Override
    public boolean isLegalShipType(ShipType type, PoliticalSystem politics) {
        return type.pirate() < 0 || politics.strengthPirates() < type.pirate();
    }
}
