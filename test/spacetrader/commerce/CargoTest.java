/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.commerce;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author nkaru_000
 */
public class CargoTest {
    
    private Cargo cargo;
    
    public CargoTest() {
        cargo = new Cargo(220);
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
     * Test of addItem method, of class Cargo.
     */
    @Test
    public void addCargoContentsToEmptyCargo() {
        Cargo otherCargo = new Cargo(110);
        int q = 2; 
        int p = 5;
        for (TradeGood t : TradeGood.values()) {
            otherCargo.addItem(t, q, p);
            q = q + 2;
            p = p + 5;
        }
        cargo.addCargoContents(otherCargo);
        assertEquals(cargo.getCount(), otherCargo.getCount());
        for (TradeGood t : TradeGood.values()) {
            assertEquals(otherCargo.getCostOfGood(t), cargo.getCostOfGood(t));
        }
        for (TradeGood t : TradeGood.values()) {
            assertEquals(otherCargo.getQuantity(t), cargo.getQuantity(t));
        }
    }
    
    @Test
    public void addCargoContentsToExistingCargo() {
        Cargo otherCargo = new Cargo(110);
        int q = 2; 
        int p = 5;
        for (TradeGood t : TradeGood.values()) {
            otherCargo.addItem(t, q, p);
            q = q + 2;
            p = p + 5;
        }
        cargo.addCargoContents(otherCargo);
        cargo.addCargoContents(otherCargo);
        System.out.println(cargo.getCount());
        assertEquals(cargo.getCount(), (otherCargo.getCount() * 2));
        for (TradeGood t : TradeGood.values()) {
            assertEquals(cargo.getCostOfGood(t), (otherCargo.getCostOfGood(t) * 2));
        }
        for (TradeGood t : TradeGood.values()) {
            assertEquals((otherCargo.getQuantity(t)) * 2, cargo.getQuantity(t));
        }
    }

}