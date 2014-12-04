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
public class HighScoreSlot implements Serializable {
    
    private String playerName;
    private int overallScore;
    private int daysLived;
    private int overallWorth;
    
    public HighScoreSlot(SpaceTrader game, int score) {
        this.playerName = game.getPlayer().getName();
        this.overallWorth = game.getPlayer().getCurrentWorth();
        this.daysLived = game.getDays();
        this.overallScore = score;
    }
    
    public String getName() {
        return this.playerName;
    }
    
    public int getScore() {
        return this.overallScore;
    }
    
    public int getDaysLived() {
        return this.daysLived;
    }
    
    public int getWorth() {
        return this.overallWorth;
    }
}
