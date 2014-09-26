/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.commerce;

import spacetrader.TechLevel;

/**
 *
 * @author Caleb
 */
public class EnergyShield extends Shield {
    
    public EnergyShield() {
        //name, price, defense, min techlevel
        super("Energy Shield", 50, 25, TechLevel.EARLY_INDUSTRIAL);
    }
}
