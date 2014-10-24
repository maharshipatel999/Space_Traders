/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.ships;

import spacetrader.planets.TechLevel;

/**
 *
 * @author nkaru_000
 */
public enum Gadget {
    
    EXTRA_CARGO        (TechLevel.MEDIEVAL, 200, "5 Extra Cargo Bays"),
    NAVIGATION         (TechLevel.POST_INDUSTRIAL, 1000, "Navigating System"),
    AUTO_REPAIR        (TechLevel.RENAISSANCE, 750, "Auto-Repair System"),
    TARGETING          (TechLevel.POST_INDUSTRIAL, 9000, "Targeting System"),
    CLOAK              (TechLevel.HI_TECH, 20000, "Cloaking Device");
    
    private TechLevel minTechLevel;
    private int price;
    private String name;
    
    private Gadget(TechLevel minTechLevel, int price, String name) {
        this.minTechLevel = minTechLevel;
        this.price = price;
        this.name = name;
    }
}
