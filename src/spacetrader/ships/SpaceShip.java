/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.ships;

import java.io.Serializable;
import java.util.ArrayList;
import spacetrader.Mercenary;
import spacetrader.Player;
import spacetrader.SkillList;
import spacetrader.SkillList.Skill;
import spacetrader.commerce.Cargo;
import spacetrader.commerce.TradeGood;

/**
 *
 * @author Caleb Stokols
 */
public class SpaceShip implements Serializable {

    private final ShipType type;
    private final Cargo cargo;
    private final FuelTank tank;
    private final EquipmentSlots<Weapon> weapons;
    private final EquipmentSlots<Shield> shields;
    private final EquipmentSlots<Gadget> gadgets;
    private final ArrayList<Mercenary> crew;
    private final SkillList crewSkills;

    private int maxHullStrength;
    private int hullStrength;

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

    public Cargo getCargo() {
        return cargo;
    }

    public FuelTank getTank() {
        return tank;
    }

    public ShipType getType() {
        return type;
    }

    public EquipmentSlots<Weapon> getWeapons() {
        return weapons;
    }

    public EquipmentSlots<Shield> getShields() {
        return shields;
    }

    public EquipmentSlots<Gadget> getGadgets() {
        return gadgets;
    }

    public int getMaxHullStrength() {
        return maxHullStrength;
    }

    public int getHullStrength() {
        return hullStrength;
    }

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
     * Determines if this ship is carrying firearms or narcotics.
     *
     * @return true if this ship is carrying firearms or narcotics, false
     * otherwise
     */
    public boolean isCarryingIllegalGoods() {
        int firearms = cargo.getQuantity(TradeGood.FIREARMS);
        int narcotics = cargo.getQuantity(TradeGood.NARCOTICS);
        return firearms + narcotics > 0;
    }

    /**
     * This method should be put in the shipYard Determines the price of this
     * ship
     *
     * @param player the player of the game
     * @return the price of this ship
     */
    public int shipPrice(Player player) {
        int basePrice = type.price() * (100 - player.getEffectiveSkill(Skill.TRADER)) / 100;

        //change basePrice more based on its contentes
        return basePrice;
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
        currentPrice -= (getTank().getMaxFuel() - getTank().getFuelAmount());

        //add reduced cost of equipment
        for (int i = 0; i < weapons.getNumFilledSlots(); i++) {
            currentPrice += weapons.getItem(i).getType().price() * .75;
        }
        for (int i = 0; i < shields.getNumFilledSlots(); i++) {
            currentPrice += weapons.getItem(i).getType().price() * .75;
        }
        //for (int i = 0; i < gadgets.getNumFilledSlots(); i++) {
        //    currentPrice += gadgets.getItem(i).getType().price() * .75;
        //}

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
     * Returns a new array of this ship's crew.
     *
     * @return an array of this ship's crew
     */
    public Mercenary[] getCrew() {
        return crew.toArray(new Mercenary[crew.size()]);
    }

    public int getCrewSkill(Skill type) {
        return crewSkills.getSkill(type);
    }

    private void calculateHighestCrewSkills() {
        for (Mercenary person : crew) {
            for (Skill type : Skill.values()) {
                int highestValue = Math.max(person.getSkill(type), crewSkills.getSkill(type));
                crewSkills.setSkill(type, highestValue);
            }
        }
    }

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

}
