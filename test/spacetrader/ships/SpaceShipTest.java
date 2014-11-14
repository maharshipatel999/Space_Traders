/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.ships;

import static org.junit.Assert.*;
import org.junit.Test;
import spacetrader.Mercenary;
import spacetrader.SkillList;
import spacetrader.SkillList.Skill;

/**
 *
 * @author Seth
 */
public class SpaceShipTest {

    /**
     * Test of hireMercenary method, of class SpaceShip.
     */
    @Test
    public void testHireMercenary() {

        System.out.println("hireMercenary");

        Mercenary merc1 = new Mercenary("Seth", 9, 1, 1, 1, 1);
        Mercenary merc2 = new Mercenary("Caleb", 1, 9, 1, 1, 1);
        Mercenary merc3 = new Mercenary("Naveena", 1, 1, 9, 1, 1);
        Mercenary merc4 = new Mercenary("Idan", 10, 10, 10, 10, 10);

        // add 1 mercenary
        SpaceShip ship = new SpaceShip(ShipType.WASP);
        boolean result = ship.hireMercenary(merc1);
        assertTrue(result);
        testSkills(ship, new SkillList(9, 1, 1, 1, 1));

        // add a second  mercenary
        result = ship.hireMercenary(merc2);
        assertTrue(result);
        testSkills(ship, new SkillList(9, 9, 1, 1, 1));

        // add a third mercenary
        result = ship.hireMercenary(merc3);
        assertTrue(result);
        testSkills(ship, new SkillList(9, 9, 9, 1, 1));

        // add an illegal fourth member
        result = ship.hireMercenary(merc4);
        assertFalse(result);
        testSkills(ship, new SkillList(9, 9, 9, 1, 1));

    }

    private void testSkills(SpaceShip ship, SkillList skills) {
        for (Skill s : Skill.values()) {
            assertEquals(ship.getCrewSkill(s), skills.getSkill(s));
        }
    }
}
