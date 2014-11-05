/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.ships;

import java.util.ArrayList;
import spacetrader.Mercenary;
import spacetrader.SkillList;
import spacetrader.SkillList.Skill;

/**
 *
 * @author Caleb
 */
public class PlayerShip extends SpaceShip {

    private final ArrayList<Mercenary> crew;
    private final SkillList crewSkills;
    private boolean hasEscapePod;

    public PlayerShip(ShipType type) {
        super(type);
        this.crew = new ArrayList<>();
        this.crewSkills = new SkillList();
        hasEscapePod = false;
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

    public void setEscapePod() {
        hasEscapePod = true;
    }

    private void calculateHighestCrewSkills() {
        for (Mercenary person : crew) {
            for (Skill type : Skill.values()) {
                int highestValue = Math.max(person.getSkill(type), crewSkills.getSkill(type));
                crewSkills.setSkill(type, highestValue);
            }
        }
    }

}
