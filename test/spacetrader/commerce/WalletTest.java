/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.commerce;

import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import spacetrader.exceptions.InsufficientFundsException;

/**
 *
 * @author maharshipatel999
 */
public class WalletTest {
    
    public WalletTest() {
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
     * Test of addCredits method, of class Wallet.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        int deposit = 0;
        Wallet instance = new Wallet();
        instance.addCredits(deposit);
        // TODO review the generated test code and removeCredits the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeCredits method, of class Wallet.
     */
    @Test
    public void testRemove() {
        Wallet instance = new Wallet();
        Random rand = new Random();
        
        //Test withdrawl < 0
        int withdrawal = -10;
        try {
            instance.removeCredits(withdrawal);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().equals("Cannot add negative credits"));
        }
        
        //Test credits < withdrawal
        instance = new Wallet();
        withdrawal = 1500;
        assertTrue(instance.getCredits() < withdrawal);
        try {
            instance.removeCredits(withdrawal);
            fail("Should have thrown an InsufficientFundsException");
        } catch (InsufficientFundsException e) { }
        
        //Test withdrawal = 0
        instance = new Wallet();
        withdrawal = 0;
        int initCredits = instance.getCredits();
        try {
            instance.removeCredits(withdrawal);
        } catch (IllegalArgumentException e) {
            fail("Should NOT have thrown an IllegalArgumentException");
        }
        assertEquals(instance.getCredits(), initCredits);
        
        //Test credits >= withdrawal
        instance = new Wallet();
        withdrawal = rand.nextInt(1001);
        assertTrue(instance.getCredits() >= withdrawal);
        initCredits = instance.getCredits();
        try {
            instance.removeCredits(withdrawal);
        } catch (Exception e) {
            fail("Should NOT have thrown an Exception");
        }
        assertEquals(instance.getCredits(), (initCredits - withdrawal));

        
    }

    /**
     * Test of removeCreditsForced method, of class Wallet.
     */
    @Test
    public void testRemoveForcefully() {
        System.out.println("removeForcefully");
        int withdrawal = 0;
        Wallet instance = new Wallet();
        instance.removeCreditsForced(withdrawal);
        // TODO review the generated test code and removeCredits the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCredits method, of class Wallet.
     */
    @Test
    public void testGetCredits() {
        System.out.println("getCredits");
        Wallet instance = new Wallet();
        int expResult = 0;
        int result = instance.getCredits();
        assertEquals(expResult, result);
        // TODO review the generated test code and removeCredits the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCredits method, of class Wallet.
     */
    @Test
    public void testSetCredits() {
        System.out.println("setCredits");
        int credits = 0;
        Wallet instance = new Wallet();
        instance.setCredits(credits);
        // TODO review the generated test code and removeCredits the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDebt method, of class Wallet.
     */
    @Test
    public void testGetDebt() {
        System.out.println("getDebt");
        Wallet instance = new Wallet();
        int expResult = 0;
        int result = instance.getDebt();
        assertEquals(expResult, result);
        // TODO review the generated test code and removeCredits the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addDebt method, of class Wallet.
     */
    @Test
    public void testIncreaseDebt() {
        System.out.println("increaseDebt");
        int addition = 0;
        Wallet instance = new Wallet();
        instance.addDebt(addition);
        // TODO review the generated test code and removeCredits the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeDebt method, of class Wallet.
     */
    @Test
    public void testDecreaseDebt() {
        System.out.println("decreaseDebt");
        int removal = 0;
        Wallet instance = new Wallet();
        instance.removeDebt(removal);
        // TODO review the generated test code and removeCredits the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
