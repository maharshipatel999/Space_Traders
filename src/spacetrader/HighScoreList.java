/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import java.io.Serializable;

/**
 *
 * @author nkaru_000
 */
public class HighScoreList implements Serializable {
    
    private HighScoreSlot slot1;
    private HighScoreSlot slot2;
    private HighScoreSlot slot3;
    
    public void setSlot1(HighScoreSlot slot1) {
        this.slot1 = slot1;
    }
    
    public void setSlot2(HighScoreSlot slot2) {
        this.slot1 = slot2;
    }
    
    public void setSlot3(HighScoreSlot slot3) {
        this.slot1 = slot3;
    }
}
