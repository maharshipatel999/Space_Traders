/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.persistence;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import spacetrader.Player;
import spacetrader.ships.PlayerShip;
import spacetrader.Universe;
import spacetrader.system.SceneController;
import spacetrader.system.SpaceTrader;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class ReloadGameScreenController extends SceneController implements Initializable {
    
    private SpaceTrader game;
    
    /**
     * Gives this controller access to the entire game.
     * @param game
     */
    public void setUpReloadScreen(SpaceTrader game) {
        this.game = game;
    }
    
    
    @FXML public void reloadGame1() throws IOException, ClassNotFoundException {
        PlayerSlots slots = (PlayerSlots) SerializableUtil.deserialize("saveFile.ser");
        List<Object> objects = slots.getPlayer1();
        setObjects(objects);
        goToHome();
    }

    @FXML public void reloadGame2() throws IOException, ClassNotFoundException {
        PlayerSlots slots = (PlayerSlots) SerializableUtil.deserialize("saveFile.ser");
        List<Object> objects = slots.getPlayer2();
        setObjects(objects);
        goToHome();
    }

    @FXML public void reloadGame3() throws IOException, ClassNotFoundException {
        PlayerSlots slots = (PlayerSlots) SerializableUtil.deserialize("saveFile.ser");
        List<Object> objects = slots.getPlayer3();
        setObjects(objects);
        goToHome();
    }
    
    private void goToHome() {
        mainControl.displaySaveProgress("Loading Save File", "Loading...", "Game Successfully Loaded!");
    }

    @FXML protected void goBack(ActionEvent e) {
        mainControl.goToWelcomeScreen();
    }

    private void setObjects(List<Object> objects) {
        game.setUniverse((Universe) objects.get(0));
        game.setPlayer((Player) objects.get(1));
        game.getPlayer().setShip((PlayerShip) objects.get(2));
    }
       
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
