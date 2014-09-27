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
public class ReflectiveShield extends Shield {
    
    public ReflectiveShield() {
        //name, price, defense, min techlevel
        super("Reflective Shield", 100, 150, TechLevel.POST_INDUSTRIAL);
    }
}
