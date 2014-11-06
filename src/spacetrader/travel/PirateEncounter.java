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
 * Represents an encounter with a pirate.
 *
 * @author Caleb
 */
public class PirateEncounter extends Encounter {

    /**
     * Creates a new pirate encounter.
     *
     * @param player the player of the game
     */
    public PirateEncounter(Player player) {
        super(player, "/spacetrader/travel/PirateEncounterScreen.fxml");
    }

    /**
     * Checks to see if Pirate Ship can be used based on the type of police and
     * strength of Pirates in specific area
     *
     * @param type Type of Ship
     * @param politics Political System of specific planet
     * @return whether or not Pirate Ship Type is legal
     */
    @Override
    public boolean isLegalShipType(ShipType type, PoliticalSystem politics) {
        return type.pirate() < 0 || politics.strengthPirates() < type.pirate();
    }
}
