/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import java.io.Serializable;
import spacetrader.system.SpaceTrader;

/**
 *
 * @author nkaru_000
 */
public class HighScoreList implements Serializable {
    
    private static HighScoreSlot slot1;
    private static HighScoreSlot slot2;
    private static HighScoreSlot slot3;
    
    public void setSlot1(HighScoreSlot slot1) {
        this.slot1 = slot1;
    }
    
    public void setSlot2(HighScoreSlot slot2) {
        this.slot1 = slot2;
    }
    
    public void setSlot3(HighScoreSlot slot3) {
        this.slot1 = slot3;
    }
    
    public HighScoreSlot getSlot1() {
        return this.slot1;
    }
    
    public HighScoreSlot getSlot2() {
        return this.slot2;
    }
    
    public HighScoreSlot getSlot3() {
        return this.slot3;
    }
    
    public boolean updateSlots(SpaceTrader game, int score) {
        if (slot1 == null) {
            slot1 = new HighScoreSlot(game, score);
            System.out.println("setting the first score slot");
            return true;
        } else if (slot2 == null) {
            slot2 = new HighScoreSlot(game, score);
            return true;
        } else if (slot3 == null) {
            slot3 = new HighScoreSlot(game, score);
            return true;
        }
        if (score >= slot1.getScore()) {
            slot3 = slot2;
            slot2 = slot1;
            slot1 = new HighScoreSlot(game, score);
            return true;
        } else if (score >= slot2.getScore()) {
            slot3 = slot2;
            slot2 = new HighScoreSlot(game, score);
            return true;
        } else if (score >= slot3.getScore()) {
            slot3 = new HighScoreSlot(game, score);
            return true;
        } else {
            return false;
        }
    }
}
