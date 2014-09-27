/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import spacetrader.commerce.PriceIncreaseEvent;

/**
 *
 * @author maharshipatel999, Caleb Stokols
 */
public class Universe {
    
    private final Random rand = new Random();
    
    private final int PLANET_MAX_AMOUNT = 120;
    private final int PLANET_MIN_AMOUNT = 100;
    private final int WIDTH = 150;
    private final int HEIGHT = 100;
    
    private ArrayList<Planet> planets;
    private Set<String> planetNames = new HashSet<>();
    private Set<Point> planetLocations = new HashSet<>(100);
    
    /**
     * Creates the Universe with a semi-randomly chosen amount of Planets.
     * Each planet has a randomly chosen name and randomly chosen location.
     */
    public Universe() {
        int planetAmount = rand.nextInt(PLANET_MAX_AMOUNT - PLANET_MIN_AMOUNT) + PLANET_MIN_AMOUNT;
        planets = new ArrayList<>(planetAmount);
        
        //Create all the planets!
        for (int i = 0; i < planetAmount; i++) {
            //pick random name
            String name;
            do {
                name = generateRandomName();
            } while (planetNames.contains(name));
            planetNames.add(name);
            
            //pick random location
            Point location; 
            do {
                location = generateRandomLocation();
            } while (planetLocations.contains(location));
            planetLocations.add(location);
            
            //create planet
            Planet planet = new Planet(name, location);
            planets.add(planet);
        }
    }    
    public void updatePriceEvent(Planet p) {
        PriceIncreaseEvent[] priceIncEvents = PriceIncreaseEvent.values();
         p.setPriceIncEvent(priceIncEvents[rand.nextInt(priceIncEvents.length)]);
    }
    /**
     * Gets a list of all the planets in the universe.
     * @return list of all planets in universe
     */
    public ArrayList<Planet> getPlanets() {
        return planets;
    }
    
    /**
     * Creates a new random point between MAX_LOC (exclusive) and MIN_LOC (inclusive).
     * @return a new random point. 
     */
    private Point generateRandomLocation() {
        int x = rand.nextInt(WIDTH);
        int y = rand.nextInt(HEIGHT);
        return new Point(x, y);
    }
    
    /**
     * Creates a random name for a planet. Names can start with a Greek letter
     * and/or end with a single-digit number.
     */
    private String generateRandomName() {
        final String[] greekLetters = {
            "Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta",
            "Theta", "Iota", "Kappa", "Lambda", "Mu", "Nu", "Xi", "Xi",
            "Omicron", "Pi", "Rho", "Sigma", "Tau", "Upsilon", "Phi", "Chi",
            "Psi", "Omega"
        };
        final char[] preFixLetters = {
            'b', 'c', 'd', 'f', 'g', 'h', 'k', 'l', 'm', 'n', 'p', 'r', 's',
            't', 'v', 'w', 'x', 'y', 'z'
        };
        final char[] letters = {
            'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'r',
            's', 't', 'v', 'w', 'x', 'y', 'z'
        };
        final String[] vowels = {
            "a", "e", "i", "o", "u", "ou", "ee", "ow", "aw", "oy", "ea", 
            "eye", "eu", "oa", "ai", "ie", "aye", "oo", "ii", "io", "ia", "uo",
            "oe", "eo"};
        final String[] jprefix = { "j", "sj", "jr", "jw" };
        
        //if (rand.nextInt(100) < 30) {
        //        name += "s";
        //}
        
        final int NUMBER_SUFFIX_PROBABILITY = 50;
        final int GREEK_SUFFIX_PROBABILITY = 45;
        
        final int CLUSTER_MAX_1 = 2; //the max and min length for each consonant cluster
        final int CLUSTER_MIN_1 = 0;
        
        final int CLUSTER_MAX_2 = 2;
        final int CLUSTER_MIN_2 = 1;
        
        final int CLUSTER_MAX_3 = 2;
        final int CLUSTER_MIN_3 = 0;
        
        final int J_PREFIX_PROBABILITY = 5; //probability that prefix will have a j in it
        final int SECOND_SYLLABLE_PROBABILITY = 80;
        final int THIRD_SYLLABLE_PROBABILITY = 6;
        
        String name = "";
        
        //First Consonant Cluster
        int clusterLength = rand.nextInt(CLUSTER_MAX_1 - CLUSTER_MIN_1) + CLUSTER_MIN_1;
        if (clusterLength > 0) {
            if (rand.nextInt(100) < J_PREFIX_PROBABILITY) {
                name += jprefix[rand.nextInt(jprefix.length)];
            } else {
                for (int i = 0; i < clusterLength; i++) {
                    int ltrIndex = rand.nextInt(preFixLetters.length);
                    name += preFixLetters[ltrIndex];
                }
            }
        }
        //First Vowel Cluster
        name += vowels[rand.nextInt(vowels.length)];
        
        //Second Consonant Cluster
        clusterLength = rand.nextInt(CLUSTER_MAX_2 - CLUSTER_MIN_2) + CLUSTER_MIN_2;
        for (int i = 0; i < clusterLength; i++) {
            int ltrIndex = rand.nextInt(letters.length);
            name += letters[ltrIndex];
        }
        
        //Second Syllable
        if (rand.nextInt(100) < SECOND_SYLLABLE_PROBABILITY) {
            clusterLength = rand.nextInt(CLUSTER_MAX_3 - CLUSTER_MIN_3) + CLUSTER_MIN_3;
            if (clusterLength > 0) {
                //Second Vowel Cluster
                name += vowels[rand.nextInt(vowels.length)];
                //Second Consonant Cluster
                for (int i = 0; i < clusterLength; i++) {
                    int ltrIndex = rand.nextInt(letters.length);
                    name += letters[ltrIndex];
                }
            }
            //Third Vowel Cluster
            if (rand.nextInt(100) < THIRD_SYLLABLE_PROBABILITY) {
                name += vowels[rand.nextInt(vowels.length)];
            }
        }
        
        //extra single digit number
        if (rand.nextInt(100) < NUMBER_SUFFIX_PROBABILITY) {
                name += " " + (rand.nextInt(8) + 1);
        }

        //uppercase the first letter of name
        name = Character.toUpperCase(name.charAt(0)) + name.substring(1);

        if (rand.nextInt(100) < GREEK_SUFFIX_PROBABILITY) {
            name = greekLetters[rand.nextInt(greekLetters.length)] + "-" + name;
        }
        if (rand.nextInt(400) < 1) {
            name = "Lubstown,USA";
        }
        
        return name;
    }
}
