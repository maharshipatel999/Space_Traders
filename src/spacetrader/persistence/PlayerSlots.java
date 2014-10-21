/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.persistence;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author nkaru_000
 */
public class PlayerSlots implements Serializable {
    
    private List<Object> player1;
    private List<Object> player2;
    private List<Object> player3;
    
    public void setPlayer1(List<Object> objList) {
        this.player1 = objList;
    }
    
    public void setPlayer2(List<Object> objList) {
        this.player2 = objList;
    }
    
    public void setPlayer3(List<Object> objList) {
        this.player3 = objList;
    }
    
    public List<Object> getPlayer1() {
        return this.player1;
    }
    
    public List<Object> getPlayer2() {
        return this.player2;
    }
    
    public List<Object> getPlayer3() {
        return this.player3;
    }
}
