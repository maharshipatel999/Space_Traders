/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.travel;

import spacetrader.Player;
import spacetrader.SpaceShip;

/**
 *
 * @author Caleb
 */
public abstract class Encounter {
    
    private Player player;
    private SpaceShip opponent;
    private String encounterScene;
    
    public Encounter(Player player, String fxmlScene) {
        this.player = player;
        this.encounterScene = fxmlScene;
    }

    public Player getPlayer() {
        return player;
    }
    
    public String getFXMLScene() {
        return encounterScene;
    }
}
