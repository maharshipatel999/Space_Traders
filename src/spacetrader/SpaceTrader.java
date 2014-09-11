/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author nkaru_000
 */
public class SpaceTrader extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Button newGame = new Button();
        newGame.setText("Start a new game!");
        Button reload = new Button();
        reload.setText("Reload a previous game!");
        newGame.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                //Handle new game start here
            }
        });
        
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(8);
        root.getChildren().add(newGame);
        root.getChildren().add(reload);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Space Traders!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
