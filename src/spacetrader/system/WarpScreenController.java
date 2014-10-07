/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.system;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import spacetrader.Planet;
import spacetrader.Universe;
import javafx.scene.shape.*;
import javafx.animation.Transition.*;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class WarpScreenController implements Initializable {

    @FXML private Text destination;
    private MainController mainControl;
    private Planet dest;
    
    /**
     * Gives this controller a reference to the MainController.
     * @param mainControl the Main Controller of SpaceTrader
     */
    public void setMainControl(MainController mainControl) {
        this.mainControl = mainControl;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    

    
    public void travel(Planet destination) {
        dest = destination;
        update();
        arriveAtPlanet();
    }
    

    
    public void update() {
     try{
         Thread.sleep(1000);
     } catch (Exception e) {
         
     }     


    }
    
    protected void arriveAtPlanet() {
        //Pick a random planet to start off game
        mainControl.goToMarketScreen(dest);
    }
    
}
