/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.ships;

import java.util.ArrayList;
import javafx.geometry.Point2D;
import spacetrader.Mercenary;
import spacetrader.Tools;
import static spacetrader.Tools.rand;
import spacetrader.Universe;
import spacetrader.commerce.Cargo;
import spacetrader.commerce.TradeGood;
import spacetrader.planets.Planet;

/**
 * Represents an opponent ship that the player would fight in an encounter. Has
 * a randomly determined equipment, cargo, current hull strength, and crew.
 *
 * @author Caleb Stokols
 */
public class OpponentShip extends SpaceShip {

    /**
     * Creates an opponent ship and randomly gives it an assortment of
     * equipment, cargo, and crew. Its hull strength is diminished a random
     * amount as well.
     *
     * @param type
     * @param difficulty determines # of attempts to select powerful equipment
     * @param cargoModifier affects the # of cargo slots to fill
     */
    public OpponentShip(ShipType type, int difficulty, double cargoModifier) {
        super(type);

        //Add Gadgets
        for (Gadget gadget : determineShipGadgets(difficulty)) {
            getGadgets().addItem(gadget);
        }
        //Add Weapons
        for (Weapon weapon : determineShipWeapons(difficulty)) {
            getWeapons().addItem(weapon);
        }
        //Add Shields
        for (Shield shield : determineShipShields(difficulty)) {
            getShields().addItem(shield);
        }

        //Add Crew
        for (Mercenary mercenary : getRandomCrew()) {
            this.hireMercenary(mercenary);
        }

        //Add Cargo
        int quantity = (int) (numCargoSlotsFilled() * cargoModifier);
        getCargo().addCargoContents(getRandomCargo(quantity));

        //Set Hull Strength (must be called after shields have been added)
        setHullStrength(getRandomHullStrength());
    }

    /**
     * Randomly selects equipment based on the various probabilities of each.
     *
     * @param slotsToFill the number of equipment which to select
     * @param tries the number of attempts to pick more powerful equipment
     * @param itemDistribution the relative likelihood of each equipment
     * @return a list of indices of the equipment that were chosen
     */
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

    /**
     * Determines which gadgets to add to this ship.
     *
     * @param tries the number of attempts to pick more powerful gadgets
     * @return a list of Gadgets
     */
    private ArrayList<Gadget> determineShipGadgets(int tries) {
        GadgetType[] gadgetTypes = GadgetType.values();
        int totalSlots = getType().gadgetSlots();

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
        int slotsToFill;
        if (totalSlots == 0) {
            slotsToFill = 0;
        } else {
            slotsToFill = Math.min(totalSlots, rand.nextInt(totalSlots + 1) + extraSlot);
        }
        ArrayList<Integer> gadgetsIndices = getRandomEquipment(slotsToFill, tries, gadgetDistribution);

        //Create the Equipment Slots!
        ArrayList<Gadget> gadgetEquipment = new ArrayList<>(totalSlots);
        for (Integer index : gadgetsIndices) {
            gadgetEquipment.add(new Gadget(gadgetTypes[index]));
        }

        return gadgetEquipment;
    }

    /**
     * Determines which weapons to add to this ship.
     *
     * @param tries the number of attempts to pick more powerful weapons
     * @return a list of Weapons
     */
    private ArrayList<Weapon> determineShipWeapons(int tries) {
        WeaponType[] weaponTypes = WeaponType.values();
        int totalSlots = getType().weaponSlots();

        //Calculate Weapon Probaility Distribution
        int[] weaponDistribution = new int[weaponTypes.length];
        for (int i = 0; i < weaponTypes.length; i++) {
            weaponDistribution[i] = weaponTypes[i].chance();
        }

        //Determine whether to fill an extra slot
        int extraSlot = (tries > 3) ? rand.nextInt(2) : 0;

        //Determine # of equipment slots to fill, at least 1 if possible. (CANNOT be more than totalSlots)
        int slotsToFill;
        if (totalSlots == 0) {
            slotsToFill = 0;
        } else {
            slotsToFill = Math.min(totalSlots, 1 + rand.nextInt(totalSlots) + extraSlot);
        }
        ArrayList<Integer> weaponIndices = getRandomEquipment(slotsToFill, tries, weaponDistribution);

        //Create the Equipment Slots!
        ArrayList<Weapon> weapons = new ArrayList<>(totalSlots);
        for (Integer index : weaponIndices) {
            weapons.add(new Weapon(weaponTypes[index]));
        }
        return weapons;

    }

    /**
     * Determines which shields to add to this ship.
     *
     * @param tries the number of attempts to pick more powerful shields
     * @return a list of Shields
     */
    private ArrayList<Shield> determineShipShields(int tries) {
        ShieldType[] shieldTypes = ShieldType.values();
        int totalSlots = getType().shieldSlots();

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
        int slotsToFill;
        if (totalSlots == 0) {
            slotsToFill = 0;
        } else {
            slotsToFill = Math.min(totalSlots, rand.nextInt(totalSlots + 1) + extraSlot);
        }
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
     * Determines the number of goods an opponent ship's cargo should have.
     *
     * @return the number of slots which should be filled
     */
    private int numCargoSlotsFilled() {
        final int MAX_AMOUNT = 15;
        final int MIN_AMOUNT = 3;
        int maxCargoBonus = getType().cargoBay() - 5;

        if (maxCargoBonus > 0) {
            return Math.min(MIN_AMOUNT + rand.nextInt(maxCargoBonus), MAX_AMOUNT);
        } else {
            return 0; //if number of cargo bays is less than 5, amount of cargo is zero
        }
    }

    /**
     * Returns a Cargo filled with a specified quantity of random TradeGoods.
     *
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

    /**
     * Gets a random current hull strength for this ship.
     *
     * @return a current hull strength for this ship
     */
    private int getRandomHullStrength() {
        final double BETTER_SHIELD_PROB = 0.8;

        int currHull;

        // If there are shields, the hull will probably be stronger
        if (!getShields().isEmpty() && (rand.nextDouble() < BETTER_SHIELD_PROB)) {
            currHull = getMaxHullStrength();
        } else {
            int bestHull = 0;
            for (int i = 0; i < 5; i++) {
                bestHull = Math.max(bestHull, 1 + rand.nextInt(getMaxHullStrength()));
            }
            currHull = bestHull;
        }

        return currHull;
    }

    /**
     * Chooses a random crew for this ship.
     *
     * @return a list of Mercenaries
     */
    private ArrayList<Mercenary> getRandomCrew() {
        // Get the crew. These may be duplicates, or even equal to someone aboard
        // the commander's ship, but who cares, it's just for the skills anyway.
        ArrayList<Mercenary> crew = new ArrayList<>();

        final int MAX_SKILL = 10;
        int pilot = 1 + rand.nextInt(MAX_SKILL);
        int fighter = 1 + rand.nextInt(MAX_SKILL);
        int trader = 1 + rand.nextInt(MAX_SKILL);
        int engineer = 1 + rand.nextInt(MAX_SKILL);
        int investor = 1 + rand.nextInt(MAX_SKILL);
        Planet planet = new Planet("sethville", new Point2D(1,1));
        Mercenary commander = new Mercenary("Seth Davis", pilot, fighter, trader, engineer, investor, planet);

        int numCrew = 1 + rand.nextInt(getType().crew());
        for (int i = 1; i < numCrew; i++) {
            //crew.add(getRandomMercenary()); This is the correct line but it hasn't be implemented yet.
            crew.add(new Mercenary("John", 1, 1, 1, 1, 1, planet)); //TEMPORARY
        }

        return crew;
    }

}
