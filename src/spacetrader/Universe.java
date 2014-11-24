/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javafx.geometry.Point2D;
import spacetrader.planets.Planet;
import spacetrader.planets.PoliticalSystem;
import spacetrader.planets.Resource;
import spacetrader.planets.TechLevel;
import spacetrader.planets.Wormhole;

/**
 *
 * @author maharshipatel999, Caleb Stokols
 */
public class Universe implements Serializable {

    private final Random rand = new Random();

    public static final int WIDTH = 150;
    public static final int HEIGHT = 110;
    private static final int PLANET_MAX_AMOUNT = 130;
    private static final int PLANET_MIN_AMOUNT = 110;
    private static final int MIN_DISTANCE = 6;
    private static final int MAX_DISTANCE = 13;

    //the # of planets spawned in special locations
    private static final int INITIAL_PLANET_NUM = 17;
    private static final int MAX_ADJ_PLANETS = 4;

    private final ArrayList<Planet> planets;
    private final Set<String> planetNames = new HashSet<>();
    private final Set<Point2D> planetLocations = new HashSet<>(100);
    private final ArrayList<Planet> wormholePlanets;

    //The region of the universe where planets currently exist
    /**
     * Creates the Universe with a semi-randomly chosen amount of Planets. Each
     * planet has a randomly chosen name and randomly chosen location.
     */
    public Universe() {
        this(new Planet("Pallet", new Point2D(WIDTH / 2, HEIGHT / 2),
                TechLevel.AGRICULTURE, Resource.NONE, PoliticalSystem.DEMOCRACY));
    }

    /**
     * Creates the Universe with a semi-randomly chosen amount of Planets. Each
     * planet has a randomly chosen name and randomly chosen location.
     *
     * @param homePlanet the planet which the player should start on.
     */
    public Universe(Planet homePlanet) {
        int planetAmount = rand.nextInt(PLANET_MAX_AMOUNT - PLANET_MIN_AMOUNT) + PLANET_MIN_AMOUNT;
        planets = new ArrayList<>(planetAmount);

        //Add Home Planet
        planets.add(homePlanet);
        planetNames.add(homePlanet.getName());
        this.addNewPlanetLocation(homePlanet.getLocation());
        Map<Planet, Integer> numAdjPlanets = new HashMap<>();
        numAdjPlanets.put(homePlanet, 0);

        //Create all the planets!
        for (int i = 0; i < planetAmount; i++) {
            //pick random name
            String name;
            do {
                name = generateRandomName();
            } while (planetNames.contains(name));
            planetNames.add(name);

            //pick random location
            Point2D location;
            Planet randomPlanet; //the next planet will be spawned next to this one
            int timesTried = 0; //this prevents the game from timing out
            do {
                do {
                    int num = rand.nextInt(planets.size());
                    //Makes sure that planets added early on have a full # of adjacent planets
                    if (timesTried < 10 && i > 15 && i % 7 == 0) {
                        timesTried++;
                        //start at the first planet, and if the planet isn't full, add another
                        for (int j = 0; numAdjPlanets.get(planets.get(j)) > MAX_ADJ_PLANETS; j++) {
                            num = j;
                        }
                    }
                    randomPlanet = planets.get(num);
                } while (numAdjPlanets.get(randomPlanet) > MAX_ADJ_PLANETS);

                location = generateRandomLocation(randomPlanet);

            } while (planetLocations.contains(location) || !isOptimalDistance(location, numAdjPlanets));
            this.addNewPlanetLocation(location);
            //increase the number of adjacent planets of the randomPlanet.
            if (planets.size() >= INITIAL_PLANET_NUM) {
                numAdjPlanets.put(randomPlanet, numAdjPlanets.get(randomPlanet) + 1);
            }

            //create planet
            Planet planet = new Planet(name, location);
            planet.setRandomPriceIncEvent();
            planets.add(planet);
            numAdjPlanets.put(planet, 0);
        }

        //assign wormholes
        wormholePlanets = new ArrayList<>(9);
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                double w1 = ((double) (j - 1) / 3) * WIDTH;
                double w2 = ((double) j / 3) * WIDTH;
                double h1 = ((double) (i - 1) / 3) * HEIGHT;
                double h2 = ((double) i / 3) * HEIGHT;
                //System.out.println(w1 + " " + w2 + " " + h1 + " " + h2);
                boolean setinal = true;
                while (setinal) {
                    Planet randPlanet = planets.get(rand.nextInt(planets.size()));
                    double planetX = randPlanet.getLocation().getX();
                    double planetY = randPlanet.getLocation().getY();
                    if (planetX > w1 && planetX < w2 && planetY > h1 && planetY < h2) {
                        wormholePlanets.add(randPlanet);
                        setinal = false;
                    }
                }
            }
        }
        for (int i = 0; i < wormholePlanets.size(); i++) {
            int destInt;
            do {
                destInt = rand.nextInt(9);
            } while (destInt == i);
            Wormhole curHole = new Wormhole(wormholePlanets.get(i), wormholePlanets.get(destInt));
            wormholePlanets.get(i).setWormhole(curHole);
        }
    }
    // add mercenaries

    /**
     * Finds the planet with the specified name.
     *
     * @param name the name of the planet to find
     * @return the planet with the specified name, or null if not found.
     */
    public Planet getPlanet(String name) {
        if (planetNames.contains(name)) {
            for (Planet planet : planets) {
                if (planet.getName().equals(name)) {
                    return planet;
                }
            }
        }
        return null;
    }

    private boolean isOptimalDistance(Point2D point, Map<Planet, Integer> numAdjPlanets) {
        boolean isNearAnotherPlanet = false;
        Set<Planet> closePlanets = new HashSet<>();
        
        for (Planet planet : planets) {
            Point2D p = planet.getLocation();
            if (point.distance(p.getX(), p.getY()) < MIN_DISTANCE) {
                return false;
            }
            if (point.distance(p.getX(), p.getY()) <= MAX_DISTANCE) {
                if (numAdjPlanets.get(planet) > MAX_ADJ_PLANETS) {
                    return false;
                } else {
                    isNearAnotherPlanet = true;
                    closePlanets.add(planet);
                }
            }
        }
        for (Planet planet : closePlanets) {
            numAdjPlanets.put(planet, numAdjPlanets.get(planet) + 1);
        }
        return isNearAnotherPlanet || planets.size() < INITIAL_PLANET_NUM;
    }

    public void updatePriceEvent(Planet p) {
        p.setRandomPriceIncEvent();
    }

    /**
     * Gets a list of all the planets in the universe.
     *
     * @return list of all planets in universe
     */
    public ArrayList<Planet> getPlanets() {
        return planets;
    }

    public static double distanceBetweenPlanets(Planet p1, Planet p2) {
        return p1.getLocation().distance(p2.getLocation());
    }

    private void addNewPlanetLocation(Point2D point) {
        planetLocations.add(point);
    }

    /**
     * Creates a new random point between MAX_LOC (exclusive) and MIN_LOC
     * (inclusive).
     *
     * @param adjPlanet the planet which the new planet should be in range of
     * @return a new random point.
     */
    private Point2D generateRandomLocation(Planet adjPlanet) {
        if (planets.size() < INITIAL_PLANET_NUM) {
            //planets.size() - 1 is used below because the home planet is added 
            //before this is ever called
            int x = MIN_DISTANCE / 2 - rand.nextInt(MIN_DISTANCE)
                    + (WIDTH * (1 + ((planets.size() - 1) % 4)) / 5);
            int y = MIN_DISTANCE / 2 - rand.nextInt(MIN_DISTANCE)
                    + ((HEIGHT * (1 + (planets.size() - 1) / 4)) / 5);
            return new Point2D(x, y);
        } else {
            Point2D randPlanetLoc = adjPlanet.getLocation();
            //pick random legal distance
            int dx = rand.nextInt(MAX_DISTANCE - MIN_DISTANCE) + MIN_DISTANCE;
            int dy = rand.nextInt(MAX_DISTANCE - MIN_DISTANCE) + MIN_DISTANCE;
            //randomly negate the value
            dx *= rand.nextBoolean() ? 1 : -1;
            dy *= rand.nextBoolean() ? 1 : -1;
            
            Point2D loc = randPlanetLoc.add(dx, dy);
            //apply bounds
            loc = new Point2D(Math.min(loc.getX(), WIDTH), Math.min(loc.getY(), HEIGHT));
            loc = new Point2D(Math.max(loc.getX(), 0), Math.max(loc.getY(), 0));
            return loc;
        }
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
        final String[] jprefix = {"j", "sj", "jr", "jw"};

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
