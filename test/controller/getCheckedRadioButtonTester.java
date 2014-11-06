/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import javafx.scene.control.RadioButton;
import java.util.ArrayList;
import spacetrader.system.GadgetScreenController;
/**
 *
 * @author Tejas
 */
public class getCheckedRadioButtonTester {
    ArrayList<RadioButton> rButtonArray= new ArrayList<RadioButton>();;
    ArrayList<RadioButton> rButtonArray2= new ArrayList<RadioButton>();;
    ArrayList<RadioButton> rButtonArray3= new ArrayList<RadioButton>();;
    RadioButton rb1, rb2, rb3, rb4, rb5, rb6, rb7, rb8, rb9;
    GadgetScreenController g = new GadgetScreenController();
    
    public getCheckedRadioButtonTester() {
        rb1 = new RadioButton();
        rb2 = new RadioButton();
        rb3 = new RadioButton();
        rb4 = new RadioButton();
        rb5 = new RadioButton();
        rb6 = new RadioButton();
        rb7 = new RadioButton();
        rb8 = new RadioButton();
        rb9 = new RadioButton();  
        rb6.setSelected(true);
        rb8.setSelected(true);
        rb9.setSelected(true);  
        rButtonArray.add(rb1);
        rButtonArray.add(rb2);
        rButtonArray.add(rb3);
        rButtonArray.add(rb4);
        rButtonArray.add(rb5);
        rButtonArray.add(rb6);
        rButtonArray.add(rb7);
        rButtonArray.add(rb8);
        rButtonArray.add(rb9);
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
    @Test
    public void testNull() {
    g.setRadioButtons(rButtonArray);
    RadioButton r = g.getCheckedRadioButton();
    assertEquals(r, null);
    }
}
