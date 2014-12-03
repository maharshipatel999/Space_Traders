/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.ships;

import spacetrader.Player;
import spacetrader.SkillList;
import static spacetrader.Tools.rand;

/**
 *
 * @author Caleb
 */
public class PlayerShip extends SpaceShip {

    //player has no getters or setters. It is only used for internal calculations
    private final Player owner;
    private boolean hasEscapePod;

    /**
     * Creates a PlayerShip.
     *
     * @param type The type of ship that will be the Player's ship
     * @param player the owner of this ship
     */
    public PlayerShip(ShipType type, Player player) {
        super(type);
        this.owner = player;
        this.hasEscapePod = false;
    }

    /**
     * Sets the Player's ship to have an escape pod.
     */
    public void setEscapePod() {
        this.hasEscapePod = true;
    }

    /**
     * Checks if the Player's ship has an escape pod.
     *
     * @return the value of hasEscapePod
     */
    public boolean hasEscapePod() {
        return this.hasEscapePod;
    }

    /**
     * Repairs the player's ship a certain amount based on the player's engineer
     * skill level. First repairs the hull strength, and then (if there are
     * still repairs remaining) fixes the ship's shields.
     *
     * @param engineerSkill the player's engineer skill
     */
    public void autoRepairHullAndShields(int engineerSkill) {
        // Determine the amount of repairs to do based on the player's engineer
        // skill. It must be at least 1.
        int repairsRemaining = rand.nextInt(Math.max(1, engineerSkill)) / 2;

        // Repair the hull strength.
        repairsRemaining = this.setHullStrength(this.getHullStrength() + repairsRemaining);
        repairsRemaining = Math.max(0, repairsRemaining);

        // Shields are easier to repair.
        repairsRemaining = 2 * repairsRemaining;
        for (Shield shield : this.getShields()) {
            repairsRemaining = shield.setHealth(shield.getHealth() + repairsRemaining);
            repairsRemaining = Math.max(0, repairsRemaining);
        }
    }

    /**
     * Automatically repair a certain amount of the ship's hull by choosing a
     * random value based on the player's skill.
     *
     * @param engineerSkill the player's engineer skill value
     */
    public void autoRepairHull(int engineerSkill) {
        //make sure engineer skill is at least 1
        int engineerRepairs = rand.nextInt(Math.max(1, engineerSkill));
        this.setHullStrength(this.getHullStrength() + engineerRepairs);
    }

    /**
     * Fully repairs all the shields on this ship.
     */
    public void fullyRepairShields() {
        for (Shield shield : getShields()) {
            shield.setHealth(shield.getType().power());
        }
    }

    @Override
    public int getEffectiveSkill(SkillList.Skill type) {
        return this.owner.getEffectiveSkill(type);
    }
}
