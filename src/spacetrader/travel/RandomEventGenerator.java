/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import java.util.ArrayList;
import java.util.Random;
import spacetrader.planets.Planet;
import spacetrader.Player;
import spacetrader.Universe;
import spacetrader.system.MainController;

/**
 * A utility class for creating random events.
 *
 * @author Seth
 */
public class RandomEventGenerator {

    private final Universe universe;
    private final Random random = new Random();
    private final ArrayList<RandomEvent> events = new ArrayList();
    private final int MAX_HULL_QUANTITY = 50;
    private final int MIN_HULL_QUANTITY = 10;

    private final int MAX_WALLET_QUANTITY = 300;
    private final int MIN_WALLET_QUANTITY = 10;

    private final int RANDOM_EVENT_CHANCE = 15;

    /**
     * Creates a new Random Event Generator and fills a list of all the possible
     * random events.
     *
     * @param player the player of the game
     * @param universe the game's universe
     * @param mainControl the main controller
     */
    public RandomEventGenerator(Player player, Universe universe, MainController mainControl) {
        this.universe = universe;
        events.add(new ChangeLocationEvent(player, "A giant space worm ate you and spat you out on a distant planet.", -1 * getRandomHullQuantity(), getRandomPlanet(), mainControl));
        events.add(new ChangeLocationEvent(player, "Your ship had a warp drive malfuntion and you found yourself on a distant planet.", 0, getRandomPlanet(), mainControl));
        events.add(new ChangeLocationEvent(player, "You stayed up too late watching your favorite soap opera, \"Lightyears Of Our Life\", and fell asleep at the helm. You now find yourself at a distant planet.", 0, getRandomPlanet(), mainControl));
        events.add(new ChangeLocationEvent(player, "Your stupid crewmate slipped on a banana peel and accidentally sat on the hyperdrive taking you to a distant planet.", 0, getRandomPlanet(), mainControl));
        events.add(new ChangeHullEvent(player, "Your ship was invaded by metal-eating space termites causing damage to your hull.", -1 * getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "Your ship passed through an asteroid field and was pelted by a barrage of space rocks causing damage to your hull.", -1 * getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "The sketchy hitchhiker you picked up last week went on a rampage causing damage to your hull.", -1 * getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "One of your crewmates burned popcorn causing the sprinklers to go off creating a short circuit creating a fire in the hull.", -1 * getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "Radiation from a nearby star caused damage to your hull.", -1 * getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "Your ship was ambushed by space pirates, but you were able to get away with minimal damage.", -1 * getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "Your ship crashed into a space cow, causing damage to the hull", -1 * getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "Poor craftsmanship of your hull plating caused your hull to take damage from the extreme temperatures of space.", -1 * getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "Your ship was rear ended by a space cop, causing damage to your hull.", -1 * getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "A garbage hauler ejected its load all over the hull of your ship.", -1 * getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "Some rowdy hooligans hit a space ball right into a window of your ship.", -1 * getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "Your poor parallel space parking skills proved to be inadequate once again; you crashed into every surrounding ship.", -1 * getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "You forgot to ask for permission for take off and crashed right into the hanger doors.", -1 * getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "Your space duct tape lost its adhesive quality causing everything on your ship to break.", -1 * getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "While trying to show off to your space girlfriend by doing barrel rolls and summersaults, you crash into a satellite.", -1 * getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "The hull repair man finally showed up... for once.", getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "That hitch hiker you picked up turned out to be a hull repair man, who paid you with his service.", getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "The space exterminator exterminated all the space termites in your hull.", getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "You won a beauty pageant and put that money towards fixing your hull.", getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "You found ₪100 lying on the space ground and used it to fix your hull.", getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "There was a scoutmob for Joe's Hull Repair Shop that you took advantage of.", getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "You found a roll of duct tape lying on the shelf and used it to repair your hull.", getRandomHullQuantity()));
        events.add(new ChangeHullEvent(player, "Your insurance company finally came through and paid to get your hull repaired.", getRandomHullQuantity()));
        events.add(new ChangeWalletEvent(player, "It's your lucky day! You found ₪ on the ground.", getRandomHullQuantity()));
        events.add(new ChangeWalletEvent(player, "JACKPOT!!! You won the space lottery!", 1500));
        events.add(new ChangeWalletEvent(player, "You got a part time job at the space dance club, don't quit your day job!", getRandomWalletQuantity()));
        events.add(new ChangeWalletEvent(player, "A space burglar broke into your space ship!", -1 * getRandomWalletQuantity()));
        events.add(new ChangeWalletEvent(player, "You have to pay child support for your space children.", -1 * getRandomWalletQuantity()));
        events.add(new ChangeWalletEvent(player, "You made poor investments in alien technology.", -1 * getRandomWalletQuantity()));
        events.add(new ChangeWalletEvent(player, "Space hackers hacked your bank account.", -1 * getRandomWalletQuantity()));
        events.add(new ChangeWalletEvent(player, "You made a great investment in the alien market!", getRandomWalletQuantity()));
        events.add(new ChangeWalletEvent(player, "You struck space oil on a moon!", getRandomWalletQuantity()));
        events.add(new ChangeWalletEvent(player, "You conquered an alien planet and stole their stuff!", getRandomWalletQuantity()));
        events.add(new ChangeWalletEvent(player, "You commandeered a pirate ship!", getRandomWalletQuantity()));
        events.add(new ChangeWalletEvent(player, "You founded a new space football league and become its first commissioner!", getRandomWalletQuantity()));
        events.add(new ChangeWalletEvent(player, "You birthed space twins! Congratulations!", getRandomWalletQuantity()));
        events.add(new ChangeWalletEvent(player, "You earned yourself a space promotion!", getRandomWalletQuantity()));
        events.add(new ChangeWalletEvent(player, "You double parked your spaceship and you were ticketed!", -1 * getRandomWalletQuantity()));
        events.add(new ChangeWalletEvent(player, "You started a popular chain of space restaurants serving space burgers!", getRandomWalletQuantity()));
        events.add(new ChangeWalletEvent(player, "You caught space herpes and now have to pay for treatment.", -1 * getRandomWalletQuantity()));
        events.add(new ChangeWalletEvent(player, "You won money at the space race tracks!", getRandomWalletQuantity()));
        events.add(new ChangeWalletEvent(player, "You became addicted to Space World of Warcraft and have to pay a monthly fee!", -1 * getRandomWalletQuantity()));
        events.add(new ChangeWalletEvent(player, "You lost a game of space poker with your fat space friends", -1 * getRandomWalletQuantity()));
    }

    /**
     * Generates a random amount for the hull to be damaged or repaired.
     *
     * @return a random amount of hull strength
     */
    private int getRandomHullQuantity() {
        return random.nextInt(1 + MAX_HULL_QUANTITY - MIN_HULL_QUANTITY) + MIN_HULL_QUANTITY;
    }

    /**
     * Generates a random amount to remove or add to the player's wallet.
     *
     * @return a random amount of money
     */
    private int getRandomWalletQuantity() {
        return random.nextInt(1 + MAX_WALLET_QUANTITY - MIN_WALLET_QUANTITY) + MIN_WALLET_QUANTITY;
    }

    /**
     * Gets a random planet in the universe for the player to transport to.
     *
     * @return a random planet
     */
    private Planet getRandomPlanet() {
        ArrayList<Planet> planets = universe.getPlanets();
        int randPlanet = random.nextInt(planets.size());
        return planets.get(randPlanet);  //FIX this should never return the player's current location, or previous location.
    }

    /**
     * Gets a new random event.
     *
     * @return a random event.
     */
    public RandomEvent getRandomEvent() {
        return events.get(random.nextInt(events.size()));
    }

    /**
     * Determines if an event should occur.
     *
     * @return true if an event should occur, false otherwise.
     */
    public boolean eventOccurs() {
        int percent = random.nextInt(100);
        return percent <= RANDOM_EVENT_CHANCE;
    }
}
