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
     *  Gets a random Resource. There is a 2 in 3 chance the resource
     *  will be NONE. If the resource is not NONE, there is an equal
     *  probability among all other resources.
     * 
     *  @return Resource - the resource being assigned
     */
    public static Resource getRandomResource() {
        int probOfNone = RANDOM.nextInt(3);
        if (probOfNone % 2 == 1) {
            return VALUES.get(RANDOM.nextInt(12) + 1);
        } else {
            return Resource.NONE;
        }
    }
    
}