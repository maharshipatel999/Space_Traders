/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.system;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import spacetrader.persistence.PlayerSlots;
import spacetrader.persistence.SerializableUtil;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class OverwriteScreenController extends SceneController implements Initializable {

    @FXML private Button player1;
    @FXML private Button player2;
    @FXML private Button player3;
    
    private PlayerSlots slots;
    
    private List<Object> setUpPlayer() {
        List<Object> objList = new ArrayList<>();
        objList.add(mainControl.game.getUniverse());
        objList.add(mainControl.game.getPlayer());
        objList.add(mainControl.game.getPlayer().getShip());
        return objList;
    }
    
    @FXML protected void overwritePlayer1(ActionEvent event) {
        List<Object> objList = setUpPlayer();
        slots.setPlayer1(objList);
        try {
            serialize();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    
    @FXML protected void overwritePlayer2(ActionEvent event) {
        List<Object> objList = setUpPlayer();
        slots.setPlayer2(objList);
        try {
            serialize();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    
    @FXML protected void overwritePlayer3(ActionEvent event) {
        List<Object> objList = setUpPlayer();
        slots.setPlayer3(objList);
        try {
            serialize();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    
    @FXML public void serialize() throws FileNotFoundException, IOException, ClassNotFoundException {
        try {
            SerializableUtil.serialize(slots, "saveFile.ser");
        } catch (FileNotFoundException e) {
            System.out.println("FNFException");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException");
            System.out.println(e.getMessage());
        }
        mainControl.goToHomeScreen(mainControl.game.getPlayer().getLocation());
     }
    
    @FXML protected void goBack(ActionEvent event) {
        mainControl.goToHomeScreen(mainControl.game.getPlayer().getLocation());
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        slots = new PlayerSlots();
    }    
    
}
