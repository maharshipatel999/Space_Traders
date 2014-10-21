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

    private PlayerSlots slots;
    @FXML private Button player1;
    @FXML private Button player2;
    @FXML private Button player3;
    
    private List<Object> setUpPlayer() {
        List<Object> objList = new ArrayList<>();
        objList.add(mainControl.game.getUniverse());
        objList.add(mainControl.game.getPlayer());
        objList.add(mainControl.game.getPlayer().getShip());
        return objList;
    }
    
    @FXML public void overwritePlayer1() {
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
    
    @FXML public void overwritePlayer2() {
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
    
    @FXML public void overwritePlayer3() {
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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        slots = new PlayerSlots();
    }    
    
}
