/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;

/**
 *
 * @author Caleb
 */
public abstract class SceneController {

    protected MainController mainControl;
    private Scene rootScene;

    /**
     * Gives this controller a reference to the MainController.
     *
     * @param mainControl the Main Controller of SpaceTrader
     */
    public void setMainControl(MainController mainControl) {
        this.mainControl = mainControl;
    }
    
    /**
     * Gets the Scene associated with this controller.
     * @param scene the scene for this Controller
     */
    public void setScene(Scene scene) {
        this.rootScene = scene;
        rootScene.getStylesheets().add("/spacetrader/systemStyle.css");
    }
    
    /**
     * Gets the Scene associated with this controller.
     * @return this Controller's Scene
     */
    public Scene getScene() {
        return rootScene;
    }
    
    @FXML
    protected void openStartScreen(ActionEvent event) {
        mainControl.goToStartScreen();
    }
}