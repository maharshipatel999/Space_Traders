/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.ships;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import spacetrader.Mercenary;
import spacetrader.SkillList;
import spacetrader.SkillList.Skill;
import spacetrader.commerce.Cargo;
import spacetrader.commerce.TradeGood;

/**
 *
 * @author Caleb Stokols
 */
public class SpaceShip implements Iterable<Equipment>, Serializable {

    private final ShipType type;
    private final Cargo cargo;
    private final FuelTank tank;
    private final EquipmentSlots<Weapon> weapons;
    private final EquipmentSlots<Shield> shields;
    private final EquipmentSlots<Gadget> gadgets;
    private final ArrayList<Mercenary> crew;
    private final SkillList crewSkills;

    private final int maxHullStrength;
    private int hullStrength;

    /**
     * Creates an instance of SpaceShip with a specified type
     *
     * @param type the type of ship to create
     */
    public SpaceShip(ShipType type) {
        this.type = type;
        this.cargo = new Cargo(type.cargoBay());
        this.tank = new FuelTank(type.fuel());
        this.weapons = new EquipmentSlots<>(type.weaponSlots());
        this.shields = new EquipmentSlots<>(type.shieldSlots());
        this.gadgets = new EquipmentSlots<>(type.gadgetSlots());
        this.crew = new ArrayList<>();
        this.crewSkills = new SkillList();

        this.maxHullStrength = type.hullStrength();
        this.hullStrength = maxHullStrength;
    }

    /**
     * Gets the cargo of this ship
     *
     * @return the cargo of this instance
     */
    public Cargo getCargo() {
        return cargo;
    }

    /**
     * Gets the this ship's type
     *
     * @return this ship's ShipType
     */
    public ShipType getType() {
        return type;
    }

    /**
     * Gets the weapons of this ship
     *
     * @return this ship's weapon slots
     */
    public EquipmentSlots<Weapon> getWeapons() {
        return weapons;
    }

    /**
     * Gets the shields of this ship
     *
     * @return this ship's shields slots
     */
    public EquipmentSlots<Shield> getShields() {
        return shields;
    }

    /**
     * Gets the gadgets of this ship
     *
     * @return this ship's gadgets slots
     */
    public EquipmentSlots<Gadget> getGadgets() {
        return gadgets;
    }

    /**
     * Gets this ship's max hull strength
     *
     * @return the max hull strength of this ship
     */
    public int getMaxHullStrength() {
        return maxHullStrength;
    }

    /**
     * Gets the current hull strength of this ship
     *
     * @return the hull strength of this instance
     */
    public int getHullStrength() {
        return hullStrength;
    }

    /**
     * Sets the hull strength of this ship
     *
     * @param newHull the new hull strength
     */
    public void setHullStrength(int newHull) {
        this.hullStrength = newHull;
        if (this.hullStrength > maxHullStrength) {
            this.hullStrength = maxHullStrength;
        }
    }

    /**
     * Calculates the total weapon strength of this ship which is the sum of the
     * power of each weapon on this ship
     *
     * @return the total weapon strength of this ship.
     */
    public int getTotalWeaponStrength() {
        int total = 0;
        for (Weapon weapon : weapons) {
            total += weapon.getPower();
        }
        return total;
    }

    /**
     * Determines the total possible shield health of this ship which is the sum
     * of the power of all the shields equipped.
     *
     * @return the max total shield health of this ship
     */
    public int getMaxShieldHealth() {
        int totalHealth = 0;
        for (Shield shield : shields) {
            totalHealth += shield.getType().power();
        }
        return totalHealth;
    }

    /**
     * Determines the current shield health of this ship which is the sum of the
     * health of all the shields equipped.
     *
     * @return the current total shield health of this ship
     */
    public int getShieldHealth() {
        int totalHealth = 0;
        for (Shield shield : shields) {
            totalHealth += shield.getHealth();
        }
        return totalHealth;
    }

    /**
     * Determines if this ship is carrying firearms or narcotics.
     *
     * @return true if this ship is carrying firearms or narcotics, false
     * otherwise
     */
    public boolean isCarryingIllegalGoods() {
        int firearms = cargo.getQuantity(TradeGood.FIREARMS);
        int narcotics = cargo.getQuantity(TradeGood.NARCOTICS);
        return ((firearms + narcotics) > 0);
    }

    /**
     * Used in calculating the worth of this ship
     *
     * @return the selling price of this ship
     */
    public int currentShipPriceWithoutCargo() {
        //trade-in value is 3/4 original price
        int currentPrice = (type.price() * 3) / 4;

        //subtract repair costs
        currentPrice -= (maxHullStrength - hullStrength) * type.repairCost();

        //subtract cost to fill tank with fuel
        currentPrice -= (getMaxFuel() - getFuelAmount());

        //add reduced cost of equipment
        for (int i = 0; i < weapons.getNumFilledSlots(); i++) {
            currentPrice += weapons.getItem(i).getSellPrice(0);
        }
        for (int i = 0; i < shields.getNumFilledSlots(); i++) {
            currentPrice += weapons.getItem(i).getSellPrice(0);
        }
        for (int i = 0; i < gadgets.getNumFilledSlots(); i++) {
            currentPrice += gadgets.getItem(i).getSellPrice(0);
        }

        return currentPrice;
    }

    /**
     * Calculated the worth of this ship
     *
     * @return this ship's current worth
     */
    public int currentShipPrice() {
        return currentShipPriceWithoutCargo() + cargo.getCostOfAllGoods();
    }

    /**
     * Adds a mercenary to this ship's crew if there is still room on this ship.
     *
     * @param trader the Mercenary to add to this ship's crew
     * @return true if there is space for the mercenary, false otherwise
     */
    public boolean hireMercenary(Mercenary trader) {
        boolean hired = false;
        if (crew.size() < getType().crew()) {
            crew.add(trader);
            hired = true;
            calculateHighestCrewSkills();
        }
        return hired;
    }

    /**
     * Removes a mercenary from this ship's crew if he is on the ship.
     *
     * @param trader the Mercenary to remove from this ship's crew
     * @return true if the mercenary was able to be removed, false otherwise
     */
    public boolean fireMercenary(Mercenary trader) {
        boolean fired = false;
        if (crew.contains(trader)) {
            crew.remove(trader);
            fired = true;
            calculateHighestCrewSkills();
        }
        return fired;
    }
    
    /**
     * Returns the max number of crew this ship can carry.
     */
    public int getMaxNumCrew() {
        return type.crew();
    }

    /**
     * Returns a new array of this ship's crew.
     *
     * @return an array of this ship's crew
     */
    public Mercenary[] getCrew() {
        return crew.toArray(new Mercenary[crew.size()]);
    }

    /**
     * Gets the skill of this ship's crew
     *
     * @param type the type of skill we want to look at
     * @return the specific skill of this crew
     */
    public int getCrewSkill(Skill type) {
        return crewSkills.getSkill(type);
    }

    /**
     * Gets the highest skills of the crew
     */
    private void calculateHighestCrewSkills() {
        for (Mercenary person : crew) {
            for (Skill type : Skill.values()) {
                int highestValue = Math.max(person.getSkill(type), crewSkills.getSkill(type));
                crewSkills.setSkill(type, highestValue);
            }
        }
    }
    
    /**
     * Returns an iterator that will iterate through all this ship's 
     * equipment in the order of weapons, shields, gadgets.
     */
    @Override
    public Iterator<Equipment> iterator() {
        List<Equipment> itemList = new ArrayList<>();
        for (Weapon item : weapons) {
            itemList.add((Equipment) item);
        }
        for (Shield item : shields) {
            itemList.add((Equipment) item);
        }
        for (Gadget item : gadgets) {
            itemList.add((Equipment) item);
        }
        return itemList.iterator();
    }

    /**
     * Returns a String representation of this object
     *
     * @return the string format of this instance
     */
    @Override
    public String toString() {
        String toString = "Ship Type: " + type.toString() + "\n";
        toString += "Fuel: " + tank.getFuelAmount() + "/" + tank.getMaxFuel() + "\n";
        toString += "Huel Strength: " + hullStrength + "/" + maxHullStrength + "\n";
        toString += cargo + "\n";
        toString += "-Weapons " + weapons + "\n";
        toString += "-Shields " + shields + "\n";
        toString += "-Gadgets " + gadgets;
        return toString;
    }

    /**
     * Gets the current remaining amount of fuel.
     *
     * @return this ship's current fuel amount
     */
    public int getFuelAmount() {
        return tank.getFuelAmount();
    }

    /**
     * Gets the max amount of fuel this ship can hold.
     *
     * @return the maximum fuel capacity of this ship
     */
    public int getMaxFuel() {
        return tank.getMaxFuel();
    }

    /**
     * Adds a specified amount of fuel to this ship's fuel tank.
     *
     * @param fuelAmount the amount of fuel to add
     */
    public void addFuel(int fuelAmount) {
        tank.addFuel(fuelAmount);
    }

    /**
     * Removes a specified amount of fuel from this ship's fuel tank.
     *
     * @param fuelAmount the amount of fuel to remove
     */
    public void removeFuel(int fuelAmount) {
        tank.removeFuel(fuelAmount);
    }
}
