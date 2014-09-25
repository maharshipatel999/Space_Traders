/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.commerce;

import spacetrader.TechLevel;

/**
 *
 * @author nkaru_000
 */
public enum Shield {

    ENERGY        (TechLevel.EARLY_INDUSTRIAL, 50, 25, "Energy Shield"),
    REFLECTIVE    (TechLevel.POST_INDUSTRIAL, 100, 150, "Reflective Shield");
    
    private TechLevel minTechLevel;
    private int price;
    private int defense;
    private String name;
    
    private Shield(TechLevel minTechLevel, int price, int defense, String name) {
        this.minTechLevel = minTechLevel;
        this.price = price;
        this.defense = defense;
        this.name = name;
    }
}
