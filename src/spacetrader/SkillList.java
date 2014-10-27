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
 * @author Caleb
 */
public class SkillList implements Serializable {

    public enum Skill {
        PILOT, FIGHTER, TRADER, ENGINEER, INVESTOR;
    }
    
    public static int MAX_SKILL = 10;
    
    private final Map<Skill, Integer> skills;

    public SkillList() {
        this(0, 0, 0, 0, 0);
    }

    public SkillList(int pilot, int fighter, int trader, int engineer, int investor) {
        skills = new HashMap<>();
        
        skills.put(Skill.PILOT, pilot);
        skills.put(Skill.FIGHTER, fighter);
        skills.put(Skill.TRADER, trader);
        skills.put(Skill.ENGINEER, engineer);
        skills.put(Skill.INVESTOR, investor);
    }

    public int getSkill(Skill skill) {
        return skills.get(skill);
    }
    
    public void setSkill(Skill skill, int value) {
        skills.put(skill, value);
    }    
}
