/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.system;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import spacetrader.Planet;
import spacetrader.Universe;

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
        //TODO
    }
    
    public void travel(Planet source, Planet destination) {
        this.dest = destination;
        update();
        mainControl.takeTurn(dest, (int) Universe.distanceBetweenPlanets(source, destination));
    }
    
    public void update() {
        try {
            Thread.sleep(1200);
        } catch (Exception e) {
           //CATCH 
        }     
    }
}
