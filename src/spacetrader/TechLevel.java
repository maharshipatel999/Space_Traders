/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Caleb Stokols
 */
public enum TechLevel {
    PRE_AGRICULTURE("Pre-Agriculture"),
    AGRICULTURE("Agriculture"),
    MEDIEVAL("Medieval"),
    RENAISSANCE("Renaissance"),
    EARLY_INDUSTRIAL("Early-Industrial"),
    INDUSTRIAL("Industrial"),
    POST_INDUSTRIAL("Post-Industrial"),
    HI_TECH("High Tech");
    
    private static final List<TechLevel> VALUES =
        Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    private final String type;
    
    TechLevel(String type) {
        this.type = type;
    }
    
    /**
     *  Gets a random Tech Level
     *  @return the tech level being assigned
     */
    public static TechLevel getRandomTechLevel() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
    
    public String type() {
        return type;
    }
            
}