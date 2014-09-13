/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author nkaru_000
 */
public class SpaceTrader extends Application {
    
    Stage stage;
    
    @Override
    public void start(Stage primaryStage) {
        //Set up stage
        stage = primaryStage;
        primaryStage.setTitle("Space Traders!");
        //Set scene to the welcome screen.
        try {
            Pane pane = FXMLLoader.load(getClass().getResource("WelcomeScreen.fxml"));
            stage.setScene(new Scene(pane));
            primaryStage.show();
        } catch (IOException e) {
            Logger.getLogger(SpaceTrader.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
