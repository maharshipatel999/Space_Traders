/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class WelcomeScreenController {
    
    @FXML protected void startNewGame(ActionEvent event) {
         Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
         try {
            Pane pane = FXMLLoader.load(getClass().getResource("CharacterDialog.fxml"));
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(SpaceTrader.class.getName()).log(Level.SEVERE, null, e);
        }
    }
         
    @FXML protected void reloadGame(ActionEvent event) {
         //TODO
    }
    
    
}
