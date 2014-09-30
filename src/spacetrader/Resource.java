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
import static spacetrader.TechLevel.values;

/**
 *
 * @author Caleb Stokols, Seth Davis
 */
public enum Resource {
    NONE(),
    MINERAL_RICH(),
    MINERAL_POOR(),
    DESERT(),
    LOTS_OF_WATER(),
    RICH_SOIL(),
    POOR_SOIL(),
    RICH_FAUNA(),
    LIFELESS(),
    WEIRD_MUSHROOMS(),
    LOTS_OF_HERBS(),
    ARTISTIC(),
    WARLIKE();
    
    private static final List<Resource> VALUES =
        Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    
    /**
     *  Gets a random Resource
     *  @return the resource being assigned
     */
    public static Resource getRandomResource() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
    
}


