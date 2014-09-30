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
 * @author Caleb Stokols, Seth Davis
 */
public enum Resource {
    NONE("None"),
    MINERAL_RICH("Mineral Rich"),
    MINERAL_POOR("Mineral Poor"),
    DESERT("Desert"),
    LOTS_OF_WATER("Lack of Water"),
    RICH_SOIL("Rich Soil"),
    POOR_SOIL("Poor Soil"),
    RICH_FAUNA("Rich Fauna"),
    LIFELESS("Lifeless"),
    WEIRD_MUSHROOMS("Weird Mushrooms"),
    LOTS_OF_HERBS("Lots of Herbs"),
    ARTISTIC("Artistic"),
    WARLIKE("Warlike");
    
    private static final List<Resource> VALUES =
        Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    private String type;
    
    Resource(String type) {
        this.type = type;
    }
    
    public String type() {
        return type;
    }
    
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