/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import spacetrader.Player;
import static spacetrader.Tools.rand;
import spacetrader.commerce.TradeGood;
import spacetrader.planets.PoliticalSystem;
import spacetrader.ships.PlayerShip;
import spacetrader.ships.ShipType;

/**
 *
 * @author Caleb
 */
public class PoliceEncounterTest {

    private static Player player;
    private static PoliceEncounter instance;

    public PoliceEncounterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        player = new Player("Test", 3, 3, 3, 3, 3);
        instance = new PoliceEncounter(player, 5, 5);
        //Planet planet = new Planet("Earth", new Point(20, 20), TechLevel.EARLY_INDUSTRIAL, Resource.NONE, PoliticalSystem.DEMOCRACY);
    }

    @Test
    public void testInspectPlayer() {
        System.out.println("inspectPlayer");
        instance = new PoliceEncounter(player, 10, PoliticalSystem.DEMOCRACY.strengthPolice());

        //Test Maximum Fine
        storeIllegalGoods();
        int initialMoney = 1000000000;
        player.setCredits(initialMoney);

        assertTrue(instance.inspectPlayer());
        int expectedFine = 10000;
        assertEquals(initialMoney - expectedFine, player.getCredits());
        assertEquals(-1, player.getPoliceRecordScore());

        //Test Minimum Fine
        initialMoney = 0;
        player.setCredits(initialMoney);
        player.setShip(new PlayerShip(ShipType.FLEA));
        storeIllegalGoods();

        assertTrue(instance.inspectPlayer());
        expectedFine = 100;
        assertEquals(0, player.getCredits());
        assertEquals(Math.abs(initialMoney - expectedFine), player.getDebt());
        assertEquals(-2, player.getPoliceRecordScore());

        //Test Normal Fine
        player.setShip(new PlayerShip(ShipType.GNAT));
        storeIllegalGoods();
        initialMoney = 1000;
        player.setCredits(initialMoney);

        assertTrue(instance.inspectPlayer());
        expectedFine = 250;
        assertEquals(initialMoney - expectedFine, player.getCredits());
        assertEquals(-3, player.getPoliceRecordScore());
    }

    /**
     * Test of inspectPlayer method, of class PoliceEncounter.
     */
    @Test
    public void testInspectPlayerLegalGoods() {
        System.out.println("inspectPlayer Illegal Ship");
        instance = new PoliceEncounter(player, 10, PoliticalSystem.DEMOCRACY.strengthPolice());
        for (TradeGood good : TradeGood.values()) {
            if (good != TradeGood.FIREARMS && good != TradeGood.NARCOTICS) {
                player.getCargo().addItem(good, rand.nextInt(2) + 1, 30);
            }
        }
        assertFalse(instance.inspectPlayer());
        assertEquals(1, player.getPoliceRecordScore());
    }

    @Test
    public void testInspectPlayerIllegalGoods() {
        System.out.println("inspectPlayer Legal Ship");
        instance = new PoliceEncounter(player, 10, PoliticalSystem.DEMOCRACY.strengthPolice());

        player.getCargo().addItem(TradeGood.FIREARMS, 2, 5);
        assertTrue(instance.inspectPlayer());
        assertEquals(-1, player.getPoliceRecordScore());
        assertEquals(0, player.getCargo().getQuantity(TradeGood.FIREARMS));
        assertEquals(0, player.getCargo().getQuantity(TradeGood.NARCOTICS));

        player.getCargo().addItem(TradeGood.NARCOTICS, 3, 6);    
        assertTrue(instance.inspectPlayer());
        assertEquals(-2, player.getPoliceRecordScore());
        assertEquals(0, player.getCargo().getQuantity(TradeGood.FIREARMS));
        assertEquals(0, player.getCargo().getQuantity(TradeGood.NARCOTICS));

        player.getCargo().addItem(TradeGood.FIREARMS, 2, 5);
        player.getCargo().addItem(TradeGood.NARCOTICS, 3, 6);
        assertTrue(instance.inspectPlayer());
        assertEquals(-3, player.getPoliceRecordScore());
        assertEquals(0, player.getCargo().getQuantity(TradeGood.FIREARMS));
        assertEquals(0, player.getCargo().getQuantity(TradeGood.NARCOTICS));
    }

    private void storeIllegalGoods() {
        player.getCargo().addItem(TradeGood.FIREARMS, 5, 0);
    }
}
