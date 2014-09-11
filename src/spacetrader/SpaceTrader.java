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
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

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

        
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(8);
        root.getChildren().add(newGame);
        root.getChildren().add(reload);
        
        Scene scene = new Scene(root, 300, 250);     
        primaryStage.setTitle("Space Traders!");
        primaryStage.setScene(scene);
        
        primaryStage.show();
                newGame.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                //Handle new game start here
                root.getChildren().clear();
                TextField name = new TextField();
                name.setText("Type in Your Name.");
                Label difficulty = new Label();
                difficulty.setText("Select Difficulty: ");
                Button easy = new Button();
                easy.setText("Easy Mode");
                Button medium = new Button();
                medium.setText("Medium Mode");
                Button hard = new Button();
                hard.setText("Hard Mode");
                Button ultra = new Button();
                ultra.setText("Ultra Mode");
                root.getChildren().add(name);
                root.getChildren().add(difficulty);
                root.getChildren().add(easy);
                root.getChildren().add(medium);
                root.getChildren().add(hard);
                root.getChildren().add(ultra);
                
            }
        });
                
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
