/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.system;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class HighScoreScreenController extends SceneController implements Initializable {

    @FXML
    private Label slot1;
    private Label slot2;
    private Label slot3;
    
    /**
     * goes back to welcome screen.
     * @param e 
     */
    @FXML
    protected void goBack(ActionEvent e) {
        mainControl.goToWelcomeScreen();
    }
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    
    
}
