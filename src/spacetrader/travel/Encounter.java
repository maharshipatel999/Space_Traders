/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.travel;

import spacetrader.Player;

/**
 *
 * @author Caleb
 */
public class Encounter {
    
    Player player;
    
    public void Encounter(Player player) {
        this.player = player;
    }
    
    public Player getPlayer() {
        return player;
    }
}
