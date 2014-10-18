/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import spacetrader.SkillList.Skill;

/**
 * Represents a character in the game that has a name and skills.
 * @author Caleb Stokols
 */
public class Trader {
    
    private final String name;
    private final SkillList skillList;
    
    /**
     * Creates a new Trader with specified name and skills
     * @param name the name of this Trader
     * @param pilot this Trader's pilot skill
     * @param fighter this Trader's fighter skill
     * @param trader this Trader's trader skill
     * @param engineer this Trader's engineer skill
     * @param investor this Trader's investor skill
     */
    public Trader(String name, int pilot, int fighter, int trader, int engineer, int investor) {
        this.name = name;
        this.skillList = new SkillList(pilot, fighter, trader, engineer, investor);
    }
    
    /**
     * Gets the name of this trader.
     * @return this trader's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the specified skill of this trader.
     * @param skill the player's skill that is being inquired
     * @return the value of the player's specified skill
     */
    public int getSkill(Skill skill) {
        return skillList.getSkill(skill);
    }
}
