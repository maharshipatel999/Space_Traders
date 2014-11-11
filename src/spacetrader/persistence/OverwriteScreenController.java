/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import spacetrader.system.SceneController;
import spacetrader.system.SpaceTrader;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class OverwriteScreenController extends SceneController implements Initializable {

    private PlayerSlots slots;
    private SpaceTrader game;

    /**
     * Gives this controller access to the entire game.
     *
     * @param game
     */
    public void setUpSaveScreen(SpaceTrader game) {
        this.game = game;
    }

    private List<Object> setUpPlayer() {
        List<Object> objList = new ArrayList<>();
        objList.add(game.getUniverse());
        objList.add(game.getPlayer());
        objList.add(game.getPlayer().getShip());
        return objList;
    }

    @FXML
    protected void overwritePlayer1(ActionEvent event) {
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

    @FXML
    protected void overwritePlayer2(ActionEvent event) {
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

    @FXML
    protected void overwritePlayer3(ActionEvent event) {
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

    @FXML
    public void serialize() throws FileNotFoundException, IOException, ClassNotFoundException {
        try {
            SerializableUtil.serialize(slots, "saveFile.ser");
        } catch (FileNotFoundException e) {
            System.out.println("FNFException");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException");
            System.out.println(e.getMessage());
        }

        mainControl.displaySaveProgress("Overwrite Save File", "Saving...", "Game Successfully Saved!");
    }

    @FXML
    protected void goBack(ActionEvent event) {
        mainControl.goToHomeScreen(game.getPlayer(), game.getPlayer().getLocation());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        slots = new PlayerSlots();
    }

}
