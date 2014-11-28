/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.ships;

import spacetrader.Player;
import spacetrader.SkillList.Skill;
import static spacetrader.Tools.rand;

/**
 *
 * @author Caleb
 */
public class PlayerShip extends SpaceShip {

    private boolean hasEscapePod;

    /**
     * Creates a PlayerShip.
     *
     * @param type The type of ship that will be the Player's ship
     */
    public PlayerShip(ShipType type) {
        super(type);

        hasEscapePod = false;
    }

    /**
     * Sets the Player's ship to have an escape pod.
     */
    public void setEscapePod() {
        hasEscapePod = true;
    }

    /**
     * Checks if the Player's ship has an escape pod.
     *
     * @return the value of hasEscapePod
     */
    public boolean hasEscapePod() {
        return hasEscapePod;
    }
    
    /**
     * Repairs the player's ship a certain amount based on the player's engineer
     * skill level. First repairs the hull strength, and then (if there are still
     * repairs remaining) fixes the ship's shields.
     *
     * @param player the game's player
     */
    public void autoRepair(Player player) {
        // Determine the amount of repairs to do based on the player's engineer
        // skill. It must be at least 1.
        int engineerSkill = Math.max(1, player.getEffectiveSkill(Skill.ENGINEER));
        int repairsRemaining = rand.nextInt(engineerSkill) / 2;
        
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
}
