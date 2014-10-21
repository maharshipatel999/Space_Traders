/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.system;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import spacetrader.Player;
import spacetrader.PlayerShip;
import spacetrader.Universe;
import spacetrader.persistence.PlayerSlots;
import spacetrader.persistence.SerializableUtil;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class ReloadGameScreenController extends SceneController implements Initializable {

    @FXML private Button game1;
    @FXML private Button game2;
    @FXML private Button game3;
    
    @FXML public void reloadGame1() throws IOException, ClassNotFoundException {
        PlayerSlots slots = (PlayerSlots) SerializableUtil.deserialize("saveFile.ser");
        List<Object> objects = slots.getPlayer1();
        setObjects(objects);
        mainControl.goToHomeScreen(mainControl.game.getPlayer().getLocation());
    }

    @FXML public void reloadGame2() throws IOException, ClassNotFoundException {
        PlayerSlots slots = (PlayerSlots) SerializableUtil.deserialize("saveFile.ser");
        List<Object> objects = slots.getPlayer2();
        setObjects(objects);
        mainControl.goToHomeScreen(mainControl.game.getPlayer().getLocation());
    }

    @FXML public void reloadGame3() throws IOException, ClassNotFoundException {
        PlayerSlots slots = (PlayerSlots) SerializableUtil.deserialize("saveFile.ser");
        List<Object> objects = slots.getPlayer3();
        setObjects(objects);
        mainControl.goToHomeScreen(mainControl.game.getPlayer().getLocation());
    }

    @FXML protected void goBack(ActionEvent e) {
        mainControl.goToWelcomeScreen();
    }

    private void setObjects(List<Object> objects) {
        mainControl.game.setUniverse((Universe) objects.get(0));
        mainControl.game.setPlayer((Player) objects.get(1));
        mainControl.game.getPlayer().setShip((PlayerShip) objects.get(2));
    }
       
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
