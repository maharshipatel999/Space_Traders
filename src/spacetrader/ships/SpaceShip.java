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
import spacetrader.Tools;
import static spacetrader.Tools.rand;
import spacetrader.commerce.Cargo;
import spacetrader.commerce.ShipCargo;
import spacetrader.commerce.TradeGood;

/**
 *
 * @author Caleb Stokols
 */
public abstract class SpaceShip implements Iterable<Equipment>, Serializable {

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
        this.cargo = new ShipCargo(type.cargoBay(), this);
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
     * Sets the hull strength of this ship. The hull strength can't be set
     * higher than the ship's max hull strength.
     *
     * @param newHull the new hull strength
     * @return the amount the new hull strength is over the max hull strength
     */
    public int setHullStrength(int newHull) {
        if (newHull > maxHullStrength) {
            hullStrength = maxHullStrength;
        } else {
            hullStrength = newHull;
        }
        return newHull - maxHullStrength;
    }

    /**
     * Gets the size of this ship.
     *
     * @return an integer representing the size of this ship
     */
    public int getSize() {
        return type.size();
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
     * This ship attempts to fire at the other ship. If this ship hits the
     * opponent, the opponent will be damaged. Otherwise, nothing will happen.
     *
     * @param defender the opponent ship to fire at
     * @param defenderIsFleeing specifies if the opponent is currently
     * attempting to flee
     * @return true if this ship hit the opponent, false if it missed
     */
    public boolean fireAtShip(SpaceShip defender, boolean defenderIsFleeing) {
        final int MIN_DEX = 5; //minimum dexterity of the defender
        final int FLEEING_BONUS = 2; //multiplier for defender fleeing
        final int STRENGTH = getTotalWeaponStrength();
        
        // A ship without weapons cannot attack.
        if (STRENGTH <= 0) {
            return false;
        }

        //Calculate the attacker's accuracy
        int attackerAccuracy = this.getEffectiveSkill(Skill.FIGHTER) + defender.getSize();
        attackerAccuracy = Math.max(1, attackerAccuracy); //must be at least 1

        //Calculate the defender's dexterity
        int defenderDexterity = MIN_DEX + (defender.getEffectiveSkill(Skill.PILOT) / 2);
        defenderDexterity *= defenderIsFleeing ? FLEEING_BONUS : 1; //fleeing ships are harder to it

        // If the attacker is faster than the defender, the hit is successful
        boolean hit = rand.nextInt(attackerAccuracy) >= rand.nextInt(defenderDexterity);
        
        int damage;  
        if (hit) {
            final double STRENGTH_MODIFIER = 1 + (2 * this.getEffectiveSkill(Skill.ENGINEER) / 100.0);
            damage = rand.nextInt((int) (STRENGTH * STRENGTH_MODIFIER));
            defender.takeDamage(damage);
        } else {
            damage = 0;
        }

        return damage > 0;
    }

    /**
     * This ship takes a certain amount of damage as a result of enemy fire. The
     * amount of damage done to the hull is dependent upon the hull strength of
     * the ship.
     *
     * @param damage the amount of damage inflicted to this ship
     */
    public void takeDamage(int damage) {
        // First deal damage to the shields
        for (Shield shield : shields) {
            if (damage > 0 && shield.getHealth() > 0) {
                int originalHealth = shield.getHealth();
                int newHealth = originalHealth - damage;
                if (newHealth <= 0) {
                    shield.setHealth(0);
                    damage -= originalHealth;
                } else {
                    shield.setHealth(newHealth);
                    damage = 0;
                }
            }
        }
        // If both shields are destroyed, deal damage to the hull
        if (damage > 0) {
            damage -= rand.nextInt(Math.max(1, getEffectiveSkill(Skill.ENGINEER)));
            damage = Tools.applyBounds(damage, 1, getMaxHullStrength() / 2);

            this.setHullStrength(getHullStrength() - damage);
        }
    }

    /**
     * Determines if this ship is carrying firearms or narcotics.
     *
     * @return true if this ship is carrying firearms or narcotics, false
     *         otherwise
     */
    public boolean isCarryingIllegalGoods() {
        int firearms = cargo.getQuantity(TradeGood.FIREARMS);
        int narcotics = cargo.getQuantity(TradeGood.NARCOTICS);
        return ((firearms + narcotics) > 0);
    }

    /**
     * Used in calculating the worth of this ship
     *
     * @param traderSkill the player's trader skill
     * @return the selling price of this ship
     */
    public int currentShipPriceWithoutCargo(int traderSkill) {
        //trade-in value is 3/4 original price
        int currentPrice = (type.price() * 3) / 4;

        //subtract repair costs
        currentPrice -= (maxHullStrength - hullStrength) * type.repairCost();

        //subtract cost to fill tank with fuel
        currentPrice -= (getMaxFuel() - getFuelAmount()) * type.fuelCost();

        //add reduced cost of equipment
        for (Equipment item : this) {
            currentPrice += item.getSellPrice(traderSkill);
        }

        return currentPrice;
    }

    /**
     * Calculated the worth of this ship
     *
     * @param traderSkill the player's trader skill
     * @return this ship's current worth
     */
    public int currentShipPrice(int traderSkill) {
        return currentShipPriceWithoutCargo(traderSkill) + cargo.getCostOfAllGoods();
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
            trader.getHomePlanet().setMercenary(null);
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
            trader.getHomePlanet().setMercenary(trader);
        }
        return fired;
    }

    /**
     * Returns the max number of crew this ship can carry.
     *
     * @return the max capacity for mercenaries on this ship
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
     * @param type the type of skill to look at
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
     * Returns the highest skill score for a specific type of skill out of
     * everyone on this ship.
     *
     * @param type the type of skill to look up
     * @return the highest skill out of everyone on this ship
     */
    public abstract int getEffectiveSkill(Skill type);

    /**
     * Returns an iterator that will iterate through all this ship's equipment
     * in the order of weapons, shields, gadgets.
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
