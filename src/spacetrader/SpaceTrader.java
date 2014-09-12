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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author nkaru_000
 */
public class SpaceTrader extends Application {
    
    Stage stage;
    
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
        
        stage = primaryStage;
        Scene scene = new Scene(root, 300, 250);     
        primaryStage.setTitle("Space Traders!");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        newGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Handle new game start here
                configScreen(root);
            }
        });
    }

    public void configScreen(VBox currentScreen) {
        
                currentScreen.getChildren().clear();
                TextField name = new TextField();
                name.setText("Type in Your Name.");
                Label difficulty = new Label();
                difficulty.setText("Select Difficulty: ");
                
                Button easy = new Button();
                easy.setText("Easy Mode");
                easy.setOnAction(new difficultyButtonHandler());
                
                Button medium = new Button();
                medium.setText("Medium Mode");
                medium.setOnAction(new difficultyButtonHandler());
                
                Button hard = new Button();
                hard.setText("Hard Mode");
                hard.setOnAction(new difficultyButtonHandler());
                
                Button ultra = new Button();
                ultra.setText("Ultra Mode");
                ultra.setOnAction(new difficultyButtonHandler());
                
                currentScreen.getChildren().add(name);
                currentScreen.getChildren().add(difficulty);
                currentScreen.getChildren().add(easy);
                currentScreen.getChildren().add(medium);
                currentScreen.getChildren().add(hard);
                currentScreen.getChildren().add(ultra);
    }
    
    private class difficultyButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            try {
                //Changes the scene to the Character Dialog
                Pane characterPane = (Pane) FXMLLoader.load(SpaceTrader.class.getResource("CharacterDialog.fxml"));
                Scene characterScene = new Scene(characterPane);
                stage.setScene(characterScene);
                stage.sizeToScene();
                stage.show();
            } catch (IOException e) {
                Logger.getLogger(SpaceTrader.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
