/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * List that contains Skills and corresponding Skill Values of Player
 * 
 * @author Caleb
 */
public class SkillList implements Serializable {

    /**
     * denotes all possible skill aspects for Player
     */
    public enum Skill {

        PILOT, FIGHTER, TRADER, ENGINEER, INVESTOR;
    }

    public static int MAX_SKILL = 10;

    private final Map<Skill, Integer> skills;

    /**
     * instantiates List of Skills where all Skills have Skill Values of 0
     */
    public SkillList() {
        this(0, 0, 0, 0, 0);
    }

    /**
     * Instantiates List of Skill with specified Skill Values 
     * @param pilot Skill value of Pilot
     * @param fighter Skill value of Fighter
     * @param trader Skill value of Trader
     * @param engineer Skill value of Engineer
     * @param investor  Skill value of Investor
     */
    public SkillList(int pilot, int fighter, int trader, int engineer, int investor) {
        skills = new HashMap<>();

        skills.put(Skill.PILOT, pilot);
        skills.put(Skill.FIGHTER, fighter);
        skills.put(Skill.TRADER, trader);
        skills.put(Skill.ENGINEER, engineer);
        skills.put(Skill.INVESTOR, investor);
    }

    /**
     * returns amount skill value of a specific Skill
     * @param skill Skill to get Skill value of
     * @return desired skill value
     */
    public int getSkill(Skill skill) {
        return skills.get(skill);
    }

    /**
     * sets specific value to a skill
     * @param skill Skill to alter the value of
     * @param value Value to assign skill
     */
    public void setSkill(Skill skill, int value) {
        skills.put(skill, value);
    }
}
