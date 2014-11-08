/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import spacetrader.Mercenary;
import spacetrader.Player;
import spacetrader.PoliceRecord;
import spacetrader.Tools;
import spacetrader.commerce.Cargo;
import spacetrader.commerce.TradeGood;
import spacetrader.planets.Planet;
import spacetrader.ships.Gadget;
import spacetrader.ships.GadgetType;
import spacetrader.ships.PlayerShip;
import spacetrader.ships.Shield;
import spacetrader.ships.ShieldType;
import spacetrader.ships.ShipType;
import spacetrader.ships.SpaceShip;
import spacetrader.ships.Weapon;
import spacetrader.ships.WeaponType;

/**
 * This class will handle all the probability of creating and handling
 * encounters.
 *
 * @author Caleb
 */
public class EncounterManager {

    private static final Random rand = new Random();

    private Queue<Encounter> encounters;
    private final int numTotalEncounters;
    private Player player;
    private Planet destination;

    /**
     * Creates queue with all Encounters for one transition period
     *
     * @param source source planet
     * @param destination destination planet
     * @param ship ship Player is using
     * @param player Player in the game
     */
    public EncounterManager(Planet source, Planet destination, PlayerShip ship, Player player) {
        this.player = player;
        this.destination = destination;
        
        encounters = new LinkedList<>(); //create the queue of encounters.
        
        int pirateLevels = destination.getPoliticalSystem().strengthPirates();
        int traderLevels = destination.getPoliticalSystem().strengthTraders();
        int policeLevels = destination.getPoliticalSystem().strengthPolice();
        
        int counter = pirateLevels + traderLevels + policeLevels;
        
        int pirateProportion = pirateLevels / counter;
        int traderProportion = traderLevels / counter;
        
        while (counter > 0) {
            double d = rand.nextDouble();
            if (d <= pirateProportion) {
                encounters.add(createPirateEncounter());
            } else if (pirateProportion < d && d <= (pirateProportion + traderProportion)) {
                encounters.add(createTraderEncounter());
            } else {
                encounters.add(createPoliceEncounter());
            }
            counter -= 5;
        }
        numTotalEncounters = encounters.size();
        //figure this out with an algorithm
    }
    
    /**
     * returns newest encounter and pops it off the ArrayList holding all
     * encounters
     *
     * @return latest encounter
     */
    public Encounter getNextEncounter() {
        return encounters.poll();
    }

    /**
     * returns the total number of encounters returned by LinkedList
     *
     * @return total encounters for warp
     */
    public int getNumTotalEncounters() {
        return numTotalEncounters;
    }

    /**
     * returns the number of encounters remaining in current Warp
     *
     * @return number encounters remaining
     */
    public int getEncountersRemaining() {
        return encounters.size();
    }

    /**
     * Creates a police encounter
     * @return Specific Police Encounter
     */
    private Encounter createPoliceEncounter() {
        PoliceEncounter encounter = new PoliceEncounter(player);

        int tries = 1;
        if (player.getPoliceRecord().ordinal() < PoliceRecord.CRIMINAL.ordinal()) {
            tries = 3;
        } else if (player.getPoliceRecord().ordinal() < PoliceRecord.VILLAIN.ordinal()) {
            tries = 5;
        }
        SpaceShip ship = createShip(encounter, tries, ShipType.GNAT);
        //Don't add any cargo, since police don't carry cargo
        encounter.setOpponent(ship);
        return encounter;
    }

    /**
     * Creates a pirate encounter
     * @return Specific Pirate Encounter
     */
    private Encounter createPirateEncounter() {
        PirateEncounter encounter = new PirateEncounter(player);

        //pirates are strong if the player is worth more
        int tries = 1 + player.getCurrentWorth() / 100000;
        SpaceShip ship = createShip(encounter, tries, ShipType.GNAT);
        
        //Add cargo
        int quantity = numCargoSlotsFilled(ship.getType().cargoBay()) / 2;
        ship.getCargo().addCargoContents(getRandomCargo(quantity));
        
        encounter.setOpponent(ship);
        return encounter;
    }

    /**
     * Creates a trader encounter
     * @return Specific Trader Encounter
     */
    private Encounter createTraderEncounter() {
        TraderEncounter encounter = new TraderEncounter(player);

        int tries = 1;
        SpaceShip ship = createShip(encounter, tries, ShipType.FLEA);

        //Add cargo
        int quantity = numCargoSlotsFilled(ship.getType().cargoBay());
        ship.getCargo().addCargoContents(getRandomCargo(quantity));

        encounter.setOpponent(ship);
        return encounter;
    }

    /**
     * Creates the space ship for the opponent
     *
     * @param encounter Encounter during space travel
     * @param tries number tries to pick up a ship for opponent
     * @param lowestShipType weakest ship type
     * @return SpaceShip to use for opponent
     */
    private SpaceShip createShip(Encounter encounter, int tries, ShipType lowestShipType) {
        ShipType[] shipTypes = ShipType.values();
        
        int[] shipDistribution = new int[shipTypes.length];
        for (int i = 0; i < shipTypes.length; i++) {
            shipDistribution[i] = shipTypes[i].occurrence();
        }

        //Pick a ship for the opponent "tries" number of times. The strongest one will be used.
        int bestShipIndex = lowestShipType.ordinal();
        for (int i = 0; i < tries; i++) {
            
            //Keep picking a ship until you get one that is legal for the current situation
            int index;
            do {
                index = Tools.pickIndexFromWeightedList(shipDistribution);
            } while (!encounter.isLegalShipType(shipTypes[index], destination.getPoliticalSystem()));
            
            bestShipIndex = Math.max(bestShipIndex, index); //if this chosen ship is stronger than the opponent's current ship, replace it
        }
        
        //Instantiate the Opponent's Ship
        ShipType oppShipType = shipTypes[bestShipIndex];
        SpaceShip opponentShip = new SpaceShip(oppShipType);
        
        //Determine # of attempts (must be at least 1)
        //If the player has more money, there's more attempts to pick powerful Equipment.
        tries = Math.max(1, (player.getCurrentWorth() / 150000));
        
        //Add gadgets
        for (Gadget gadget : determineShipGadgets(oppShipType.gadgetSlots(), tries)) {
            opponentShip.getGadgets().addItem(gadget);
        }
        //Add weapons
        for (Weapon weapon : determineShipWeapons(oppShipType.weaponSlots(), tries)) {
            opponentShip.getWeapons().addItem(weapon);
        }
        //Add shields
        for (Shield shield : determineShipShields(oppShipType.shieldSlots(), tries)) {
            opponentShip.getShields().addItem(shield);
        }
        
        //Set Hull Strength
        int hullStrength = getRandomHullStrength(
                !opponentShip.getShields().isEmpty(), opponentShip.getMaxHullStrength());
        opponentShip.setHullStrength(hullStrength);
        
        //Add Crew
        for (Mercenary mercenary : getRandomCrew(opponentShip)) {
            //set ship's mercenaries
        }
        
        return opponentShip;
    }
    
    private ArrayList<Integer> getRandomEquipment(int slotsToFill, int tries, int[] itemDistribution) {
        //Pick the indices of each equipment the ship will have
        ArrayList<Integer> equipmentIndices = new ArrayList<>();
        for (int i = 0; i < slotsToFill; i++) {
            int bestItemIndex = 0;
            for (int j = 0; j < tries; j++) {
                int index = Tools.pickIndexFromWeightedList(itemDistribution);
                if (!equipmentIndices.contains(index)) {
                    bestItemIndex = Math.max(bestItemIndex, index);
                }
            }
            equipmentIndices.add(bestItemIndex);
        }
        return equipmentIndices;
    }
    
    private ArrayList<Gadget> determineShipGadgets(int totalSlots, int tries) {
        GadgetType[] gadgetTypes = GadgetType.values();
        
        //Calculate Gadget Probaility Distribution
        int[] gadgetDistribution = new int[gadgetTypes.length];
        for (int i = 0; i < gadgetTypes.length; i++) {
            gadgetDistribution[i] = gadgetTypes[i].chance();
        }

        //Determine whether to fill an extra slot
        int extraSlot = 0;
        if (tries > 4) {
            extraSlot = 1;
        } else if (tries > 2) {
            extraSlot = rand.nextInt(2); //0 or 1
        }

        //Determine # of equipment slots to fill (CANNOT be more than totalSlots)
        int slotsToFill = Math.min(totalSlots, rand.nextInt(totalSlots + 1) + extraSlot);
        ArrayList<Integer> gadgetsIndices = getRandomEquipment(slotsToFill, tries, gadgetDistribution);
        
        //Create the Equipment Slots!
        ArrayList<Gadget> gadgetEquipment = new ArrayList<>(totalSlots);
        for (Integer index : gadgetsIndices) {
            gadgetEquipment.add(new Gadget(gadgetTypes[index]));
        }
        
        return gadgetEquipment;
    }
    
    private ArrayList<Weapon> determineShipWeapons(int totalSlots, int tries) {
        WeaponType[] weaponTypes = WeaponType.values();
        
        //Calculate Weapon Probaility Distribution
        int[] weaponDistribution = new int[weaponTypes.length];
        for (int i = 0; i < weaponTypes.length; i++) {
            weaponDistribution[i] = weaponTypes[i].chance();
        }
        
        //Determine whether to fill an extra slot
        int extraSlot = (tries > 3) ? rand.nextInt(2) : 0;
        
        //Determine # of equipment slots to fill, at least 1 if possible. (CANNOT be more than totalSlots)
        int slotsToFill = Math.min(totalSlots, 1 + rand.nextInt(totalSlots) + extraSlot);
        ArrayList<Integer> weaponIndices = getRandomEquipment(slotsToFill, tries, weaponDistribution);
        
        //Create the Equipment Slots!
        ArrayList<Weapon> weapons = new ArrayList<>(totalSlots);
        for (Integer index : weaponIndices) {
            weapons.add(new Weapon(weaponTypes[index]));
        }
        return weapons;

    }
    
    private ArrayList<Shield> determineShipShields(int totalSlots, int tries) {
        ShieldType[] shieldTypes = ShieldType.values();
        
        //Calculate Gadget Probaility Distribution
        int[] shieldDistribution = new int[shieldTypes.length];
        for (int i = 0; i < shieldTypes.length; i++) {
            shieldDistribution[i] = shieldTypes[i].chance();
        }

        //Determine whether to fill an extra slot
        int extraSlot = 0;
        if (tries > 3) {
            extraSlot = 1;
        } else if (tries > 1) {
            extraSlot = rand.nextInt(2); //0 or 1
        }

        //Determine # of equipment slots to fill (CANNOT be more than totalSlots)
        int slotsToFill = Math.min(totalSlots, rand.nextInt(totalSlots + 1) + extraSlot);
        ArrayList<Integer> shieldIndices = getRandomEquipment(slotsToFill, tries, shieldDistribution);
        
        //Create the Equipment Slots!
        ArrayList<Shield> shields = new ArrayList<>(totalSlots);
        for (Integer index : shieldIndices) {
            Shield shield = new Shield(shieldTypes[index]);
            shields.add(shield);
            
            //calculate random remaining shield health for shield
            int bestPower = 0;
            for (int i = 0; i < 5; i++) {
                bestPower = Math.max(bestPower, 1 + rand.nextInt(shieldTypes[index].power()));
            }
            shield.setHealth(bestPower);
        }
        
        return shields;
    }
    
    /**
     * Determines the number of goods an opponent ship's cargo should have
     * @param totalCargoSlots the number of available Cargo slots
     * @return the number of slots which should be filled
     */
    private int numCargoSlotsFilled(int totalCargoSlots) {
        final int MAX_AMOUNT = 15;
        final int MIN_AMOUNT = 3;
        int maxCargoBonus = totalCargoSlots - 5;

        if (maxCargoBonus > 0) {
            return Math.min(MIN_AMOUNT + rand.nextInt(maxCargoBonus), MAX_AMOUNT);
        } else {
            return 0; //if number of cargo bays is less than 5, amount of cargo is zero
        }
    }
    
    /**
     * Returns a Cargo filled with a specified quantity of random TradeGoods.
     * @param amountOfCargo how much cargo to add
     * @return a cargo filled with the specified amount of TradeGoods
     */
    private Cargo getRandomCargo(int amountOfCargo) {
        Cargo cargo = new Cargo(amountOfCargo);

        int slotsToFill = amountOfCargo;
        while (slotsToFill > 0) {
            TradeGood good = TradeGood.getRandomTradeGood();
            
            int amount = 1 + rand.nextInt(TradeGood.values().length - good.ordinal());
            amount = Math.min(slotsToFill, amount); //amount can't be greater than number of remaining slots
            
            cargo.addItem(good, amount, 0);
            slotsToFill -= amount;
	}
        return cargo;
    }
    
    private int getRandomHullStrength(boolean hasShields, int maxHullStrength) {
        final double BETTER_SHIELD_PROB = 0.8;
        
        int hullStrength;
        
	// If there are shields, the hull will probably be stronger
	if (hasShields && (rand.nextDouble() < BETTER_SHIELD_PROB)) {
            hullStrength = maxHullStrength;
        } else {
            int bestHull = 0;
            for (int i = 0; i < 5; i++) {
                bestHull = Math.max(bestHull, 1 + rand.nextInt(maxHullStrength));
            }
            hullStrength = bestHull;			
	}
        
        return hullStrength;
    }
    
    private ArrayList<Mercenary> getRandomCrew(SpaceShip ship) {
        // Set the crew. These may be duplicates, or even equal to someone aboard
	// the commander's ship, but who cares, it's just for the skills anyway.
        ArrayList<Mercenary> crew = new ArrayList<>();
        
        final int MAX_SKILL = 10;
        int pilot = 1 + rand.nextInt(MAX_SKILL);
        int fighter = 1 + rand.nextInt(MAX_SKILL);
        int trader = 1 + rand.nextInt(MAX_SKILL);
        int engineer = 1 + rand.nextInt(MAX_SKILL);
        int investor = 1 + rand.nextInt(MAX_SKILL);
        
        Mercenary commander = new Mercenary("Seth Davis", pilot, fighter, trader, engineer, investor);
        
        int numCrew = 1 + rand.nextInt(ship.getType().crew());
	for (int i = 1; i < numCrew; i++) {
            //crew.add(getRandomMercenary()); This is the correct line but it hasn't be implemented yet.
            crew.add(new Mercenary("John", 1, 1, 1, 1, 1)); //TEMPORARY
        }
        
        return crew;
    }
}
