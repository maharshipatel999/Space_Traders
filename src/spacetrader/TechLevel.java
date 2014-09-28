/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

/**
 *
 * @author Caleb Stokols
 */
public enum TechLevel {
    PRE_AGRICULTURE(0),
    AGRICULTURE(1),
    MEDIEVAL(2),
    RENAISSANCE(3),
    EARLY_INDUSTRIAL(4),
    INDUSTRIAL(5),
    POST_INDUSTRIAL(6),
    HI_TECH(7);
    
    private final int levelNum;
    
    TechLevel(int levelNum) {
        this.levelNum = levelNum;
    }
    
    public int getLevelNumber() {
        return this.levelNum;
    }
    
}
