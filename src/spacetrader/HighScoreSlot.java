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
public class HighScoreSlot implements Serializable {
    
    private String playerName;
    private int overallScore;
    private int daysLived;
    private int overallWorth;
    
    public HighScoreSlot(Player player) {
        this.playerName = player.getName();
        this.overallWorth = player.getCurrentWorth();
    }
    
}
