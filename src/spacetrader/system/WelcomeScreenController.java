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
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class WelcomeScreenController extends SceneController implements Initializable {

    @FXML
    private Button easterEgg;
    @FXML
    private Button reloadButton;
    @FXML
    private Button highScores;

    @FXML
    protected void startNewGame(ActionEvent event) {
        mainControl.goToPlayerConfigScreen();
    }

    @FXML
    protected void reloadGame(ActionEvent event) {
        mainControl.goToReloadScreen();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        easterEgg.setOpacity(0);
    }

    //This is for testing purposes only. Allows tester to jump ahead
    //to FirstScreen with default character configuration.
    @FXML
    protected void layEggs(ActionEvent event) {
        mainControl.displayAdminCheats();
    }
    
    @FXML
    public void goToHighScoreScreen() {
        mainControl.goToHighScoreScreen();
    }
    
    
}
