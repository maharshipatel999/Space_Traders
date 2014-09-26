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
public enum Weapon {
    //mtl, price, damage
    PULSE        (TechLevel.EARLY_INDUSTRIAL, 100, 50, "Pulse Laser"),
    BEAM         (TechLevel.POST_INDUSTRIAL, 200, 100, "Beam Laser"),
    MILITARY     (TechLevel.HI_TECH, 400, 400, "Military Laser");
    
    private TechLevel minTechLevel;
    private int price;
    private int damage;
    private String name;
    
    private Weapon(TechLevel minTechLevel, int price, int damage, String name) {
        this.minTechLevel = minTechLevel;
        this.price = price;
        this.damage = damage;
        this.name = name;
    }
    
}
