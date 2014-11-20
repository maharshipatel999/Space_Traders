
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Transaction;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import spacetrader.ships.EquipmentSlots;
import spacetrader.ships.Equipment;
import spacetrader.exceptions.SlotsAreEmptyException;
import spacetrader.exceptions.SlotsAreFullException;
import spacetrader.ships.Gadget;
import spacetrader.ships.GadgetType;

/**
 *
 * @author Tejas Tests both addItem and removeItemOfSameType.
 */
public class addItemTester {

    EquipmentSlots<Equipment> slots;
    EquipmentSlots<Equipment> slots2;

    public addItemTester() {
        slots = new EquipmentSlots<Equipment>(3);
        slots2 = new EquipmentSlots<Equipment>(3);

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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    /**
     * Tests addition of one item addItem method.
     * Tests subtraction of one item removeItem method
     */
    @Test
    public void addOne() {
        slots.addItem(new Gadget(GadgetType.AUTO_REPAIR));
        assert (slots.getNumFilledSlots() == 1);
        assert (slots.getNumSlots() == 3);
    }

    /**
     * Test add two.
     */
    @Test
    public void addTwo() {
        slots.addItem(new Gadget(GadgetType.AUTO_REPAIR));
        slots.addItem(new Gadget(GadgetType.AUTO_REPAIR));
        assert (slots.getNumFilledSlots() == 2);
        assert (slots.getNumSlots() == 3);
    }

    /**
     * Test add three.
     */
    @Test
    public void addToFill() {
        slots.addItem(new Gadget(GadgetType.AUTO_REPAIR));
        slots.addItem(new Gadget(GadgetType.AUTO_REPAIR));
        slots.addItem(new Gadget(GadgetType.AUTO_REPAIR));
        assert (slots.getNumFilledSlots() == 3);
        assert (slots.getNumSlots() == 3);
    }

    /**
     * Test Exception for Adding.
     */
    @Test(expected = SlotsAreFullException.class)
    public void throwExceptionAdd() {
        slots.addItem(new Gadget(GadgetType.AUTO_REPAIR));
        slots.addItem(new Gadget(GadgetType.AUTO_REPAIR));
        slots.addItem(new Gadget(GadgetType.AUTO_REPAIR));
        slots.addItem(new Gadget(GadgetType.AUTO_REPAIR));
    }

    /**
     * Test empty.
     */
    @Test
    public void checkEmpty() {
        assert (slots.getNumFilledSlots() == 0);
        assert (slots.getNumSlots() == 3);
    }

    /**
     * Test Exception for Removing.
     */
    @Test(expected = SlotsAreEmptyException.class)
    public void throwExceptionRemove() {
        slots.removeItem(new Gadget(GadgetType.AUTO_REPAIR));
    }
 
    /**
     * Add then remove one. Should work.
     */
    @Test
    public void addAndRemove() {
        Gadget i = new Gadget(GadgetType.AUTO_REPAIR);
        slots.addItem(i);
        assert (slots.getNumFilledSlots() == 1);
        assert (slots.getNumSlots() == 3);
        slots.removeItem(i);
        assert (slots.getNumFilledSlots() == 0);
        assert (slots.getNumSlots() == 3);        
    }
}
