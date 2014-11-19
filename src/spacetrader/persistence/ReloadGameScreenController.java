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
import javafx.scene.control.Button;
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
    
    @FXML
    private Button game1, game2, game3;
    
    private PlayerSlots slots;

    /**
     * Gives this controller access to the entire game.
     *
     * @param game
     */
    public void setUpReloadScreen(SpaceTrader game) {
        this.game = game;
        
        try {
            slots = (PlayerSlots) SerializableUtil.deserialize("saveFile.ser");
            if (slots.getPlayer1() != null) {
                Player player = (Player) slots.getPlayer1().get(1);
                String gameLabel = String.format("Game 1\n\nPlayer: %s\nCredits: ₪%d", player.getName(), player.getCredits());
                game1.setText(gameLabel);
                game1.setDisable(false);
            }
            if (slots.getPlayer2() != null) {
                Player player = (Player) slots.getPlayer2().get(1);
                String gameLabel = String.format("Game 2\n\nPlayer: %s\nCredits: ₪%d", player.getName(), player.getCredits());
                game2.setText(gameLabel);
                game2.setDisable(false);
            }
            if (slots.getPlayer3() != null) {
                Player player = (Player) slots.getPlayer3().get(1);
                String gameLabel = String.format("Game 3\n\nPlayer: %s\nCredits: ₪%d", player.getName(), player.getCredits());
                game3.setText(gameLabel);
                game3.setDisable(false);
            }
        } catch (IOException | ClassNotFoundException e) {
            //Do nothing since its perfectly acceptable for their not to be any savefiles yet.
        }
    }

    /**
     * Reloads game1 and sets objects.
     * 
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    @FXML
    public void reloadGame1() throws IOException, ClassNotFoundException {
        List<Object> objects = slots.getPlayer1();
        setObjects(objects);
        goToHome();
    }

    /**
     * Reloads game2 and sets objects.
     * 
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    @FXML
    public void reloadGame2() throws IOException, ClassNotFoundException {
        List<Object> objects = slots.getPlayer2();
        setObjects(objects);
        goToHome();
    }

    /**
     * Reloads game3 and sets objects.
     * 
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    @FXML
    public void reloadGame3() throws IOException, ClassNotFoundException {
        List<Object> objects = slots.getPlayer3();
        setObjects(objects);
        goToHome();
    }

    /**
     * Goes back to home screen and displays progress.
     */
    private void goToHome() {
        mainControl.displaySaveProgress("Loading Save File", "Loading...", "Game Successfully Loaded!");
    }

    /**
     * goes back to welcome screen.
     * @param e 
     */
    @FXML
    protected void goBack(ActionEvent e) {
        mainControl.goToWelcomeScreen();
    }

    /**
     * sets objects back for selected player.
     * @param objects
     */
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
