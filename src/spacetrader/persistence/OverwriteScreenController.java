/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.persistence;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import spacetrader.Player;
import spacetrader.system.SceneController;
import spacetrader.system.SpaceTrader;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class OverwriteScreenController extends SceneController implements Initializable {

    @FXML
    private Button player1, player2, player3;

    private PlayerSlots slots;
    private SpaceTrader game;

    /**
     * Gives this controller access to the entire game.
     *
     * @param game
     */
    public void setUpSaveScreen(SpaceTrader game) {
        this.game = game;

        try {
            slots = (PlayerSlots) SerializableUtil.deserialize("saveFile.ser");
            if (slots.getPlayer1() != null) {
                Player player = (Player) slots.getPlayer1().get(1);
                String gameLabel = String.format("Game 1\n\nPlayer: %s\nCredits: ₪%d", player.getName(), player.getCredits());
                player1.setText(gameLabel);
            }
            if (slots.getPlayer2() != null) {
                Player player = (Player) slots.getPlayer2().get(1);
                String gameLabel = String.format("Game 2\n\nPlayer: %s\nCredits: ₪%d", player.getName(), player.getCredits());
                player2.setText(gameLabel);
            }
            if (slots.getPlayer3() != null) {
                Player player = (Player) slots.getPlayer3().get(1);
                String gameLabel = String.format("Game 3\n\nPlayer: %s\nCredits: ₪%d", player.getName(), player.getCredits());
                player3.setText(gameLabel);
            }
        } catch (IOException | ClassNotFoundException e) {
            //Do nothing since its perfectly acceptable for their not to be any savefiles yet.
        }
    }

    /**
     * returns list with all settings of Player.
     *
     * @return list of all settings of Player in game
     */
    private List<Object> setUpPlayer() {
        List<Object> objList = new ArrayList<>();
        objList.add(game.getUniverse());
        objList.add(game.getPlayer());
        objList.add(game.getPlayer().getShip());
        return objList;
    }

    /**
     * overwrites data in Slot 1 (where Player 1 would be located).
     *
     * @param event overwrite data in Slot 1
     */
    @FXML
    protected void overwritePlayer1(ActionEvent event) throws IOException, ClassNotFoundException {
        List<Object> objList = setUpPlayer();
        slots.setPlayer1(objList);
        serialize();
    }

    /**
     * overwrites data in Slot 2 (where Player 2 would be located).
     *
     * @param event overwrite data in Slot 1
     */
    @FXML
    protected void overwritePlayer2(ActionEvent event) {
        List<Object> objList = setUpPlayer();
        slots.setPlayer2(objList);
        serialize();

    }

    /**
     * overwrites data in Slot 3 (where Player 3 would be located).
     *
     * @param event overwrite data in Slot 1
     */
    @FXML
    protected void overwritePlayer3(ActionEvent event) {
        List<Object> objList = setUpPlayer();
        slots.setPlayer3(objList);
        serialize();
    }

    /**
     * Serializes game into save file.
     */
    @FXML
    public void serialize() {
        try {
            SerializableUtil.serialize(slots, "saveFile.ser");
            mainControl.displaySaveProgress("Overwrite Save File", "Saving...", "Game Successfully Saved!");
        } catch (IOException e) {
            mainControl.displayErrorMessage("Saving Failed", "Game Could Not Be Saved", "The game unfortunately was unable to be saved.");
            Logger.getLogger(OverwriteScreenController.class.getName()).log(Level.SEVERE, null, e);
        } 
    }

    /**
     * goes back to home screen.
     *
     * @param event
     */
    @FXML
    protected void goBack() {
        mainControl.goToHomeScreen(game.getPlayer().getLocation());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        slots = new PlayerSlots();
    }

}
