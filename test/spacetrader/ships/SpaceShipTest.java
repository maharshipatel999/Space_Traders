/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.ships;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import spacetrader.Mercenary;
import spacetrader.Player;
import spacetrader.SkillList;
import spacetrader.SkillList.Skill;
import spacetrader.commerce.Cargo;

/**
 *
 * @author Seth
 */
public class SpaceShipTest {

    public SpaceShipTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getCargo method, of class SpaceShip.
     */
    @Test
    public void testGetCargo() {
        System.out.println("getCargo");
        SpaceShip instance = null;
        Cargo expResult = null;
        Cargo result = instance.getCargo();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTank method, of class SpaceShip.
     */
    @Test
    public void testGetTank() {
        System.out.println("getTank");
        SpaceShip instance = null;
        FuelTank expResult = null;
        FuelTank result = instance.getTank();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getType method, of class SpaceShip.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        SpaceShip instance = null;
        ShipType expResult = null;
        ShipType result = instance.getType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getWeapons method, of class SpaceShip.
     */
    @Test
    public void testGetWeapons() {
        System.out.println("getWeapons");
        SpaceShip instance = null;
        EquipmentSlots<Weapon> expResult = null;
        EquipmentSlots<Weapon> result = instance.getWeapons();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getShields method, of class SpaceShip.
     */
    @Test
    public void testGetShields() {
        System.out.println("getShields");
        SpaceShip instance = null;
        EquipmentSlots<Shield> expResult = null;
        EquipmentSlots<Shield> result = instance.getShields();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGadgets method, of class SpaceShip.
     */
    @Test
    public void testGetGadgets() {
        System.out.println("getGadgets");
        SpaceShip instance = null;
        EquipmentSlots<Gadget> expResult = null;
        EquipmentSlots<Gadget> result = instance.getGadgets();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMaxHullStrength method, of class SpaceShip.
     */
    @Test
    public void testGetMaxHullStrength() {
        System.out.println("getMaxHullStrength");
        SpaceShip instance = null;
        int expResult = 0;
        int result = instance.getMaxHullStrength();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHullStrength method, of class SpaceShip.
     */
    @Test
    public void testGetHullStrength() {
        System.out.println("getHullStrength");
        SpaceShip instance = null;
        int expResult = 0;
        int result = instance.getHullStrength();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setHullStrength method, of class SpaceShip.
     */
    @Test
    public void testSetHullStrength() {
        System.out.println("setHullStrength");
        int newHull = 0;
        SpaceShip instance = null;
        instance.setHullStrength(newHull);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTotalWeaponStrength method, of class SpaceShip.
     */
    @Test
    public void testGetTotalWeaponStrength() {
        System.out.println("getTotalWeaponStrength");
        SpaceShip instance = null;
        int expResult = 0;
        int result = instance.getTotalWeaponStrength();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isCarryingIllegalGoods method, of class SpaceShip.
     */
    @Test
    public void testIsCarryingIllegalGoods() {
        System.out.println("isCarryingIllegalGoods");
        SpaceShip instance = null;
        boolean expResult = false;
        boolean result = instance.isCarryingIllegalGoods();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of shipPrice method, of class SpaceShip.
     */
    @Test
    public void testShipPrice() {
        System.out.println("shipPrice");
        Player player = null;
        SpaceShip instance = null;
        int expResult = 0;
        int result = instance.shipPrice(player);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of currentShipPriceWithoutCargo method, of class SpaceShip.
     */
    @Test
    public void testCurrentShipPriceWithoutCargo() {
        System.out.println("currentShipPriceWithoutCargo");
        SpaceShip instance = null;
        int expResult = 0;
        int result = instance.currentShipPriceWithoutCargo();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of currentShipPrice method, of class SpaceShip.
     */
    @Test
    public void testCurrentShipPrice() {
        System.out.println("currentShipPrice");
        SpaceShip instance = null;
        int expResult = 0;
        int result = instance.currentShipPrice();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

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

    /**
     * Test of fireMercenary method, of class SpaceShip.
     */
    @Test
    public void testFireMercenary() {
        System.out.println("fireMercenary");
        Mercenary trader = null;
        SpaceShip instance = null;
        boolean expResult = false;
        boolean result = instance.fireMercenary(trader);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCrew method, of class SpaceShip.
     */
    @Test
    public void testGetCrew() {
        System.out.println("getCrew");
        SpaceShip instance = null;
        Mercenary[] expResult = null;
        Mercenary[] result = instance.getCrew();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCrewSkill method, of class SpaceShip.
     */
    @Test
    public void testGetCrewSkill() {
        System.out.println("getCrewSkill");
        SkillList.Skill type = null;
        SpaceShip instance = null;
        int expResult = 0;
        int result = instance.getCrewSkill(type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class SpaceShip.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        SpaceShip instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
