/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class WelcomeScreenController implements Initializable {
    
    private MainController mainControl;
    
    /**
     * Gives this controller a reference to the MainController.
     * @param mainControl the Main Controller of SpaceTrader
     */
    public void setMainControl(MainController mainControl) {
        this.mainControl = mainControl;
    }
    
    @FXML protected void startNewGame(ActionEvent event) {
         mainControl.goToPlayerConfigScreen();
    }
         
    @FXML protected void reloadGame(ActionEvent event) {
         //TODO
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Nothing
    }
    
    
}
