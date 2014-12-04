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
import javafx.scene.control.Label;
import spacetrader.HighScoreList;
import spacetrader.HighScoreSlot;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class HighScoreScreenController extends SceneController implements Initializable {
    
    @FXML private Label slot1;
    @FXML private Label slot2;
    @FXML private Label slot3;
    @FXML private Button backButton;
    private HighScoreList list;
    
    /**
     * goes back to welcome screen.
     * @param e 
     */
    @FXML
    protected void goBack(ActionEvent e) {
        mainControl.goToWelcomeScreen();
    }
    
    public void setUpHighScoreScreen(HighScoreList list) {
        HighScoreSlot s1 = null;
        HighScoreSlot s2 = null;
        HighScoreSlot s3 = null;
        slot1.setText("No high score");
        slot2.setText("No high score");
        slot3.setText("No high score");
        if (list != null) {
            s1 = list.getSlot1();
            s2 = list.getSlot2();
            s3 = list.getSlot3();
            if (s1 != null) {
                slot1.setText(s1.getName() + "\n"
                            + "Survived " + s1.getDaysLived() + " days\n"
                            + "Worth " + s1.getWorth() + " credits\n"
                            + "Overall score of " + s1.getScore()); 
            }
            if (s2 != null) {
                slot2.setText(s2.getName() + "\n"
                            + "Survived " + s2.getDaysLived() + " days\n"
                            + "Worth " + s2.getWorth() + " credits\n"
                            + "Overall score of " + s2.getScore());    
            }
            if (s3 != null) {
                slot3.setText(s3.getName() + "\n"
                            + "Survived " + s3.getDaysLived() + " days\n"
                            + "Worth " + s3.getWorth() + " credits\n"
                            + "Overall score of " + s3.getScore());    
            }
        }
        
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    
    
}
