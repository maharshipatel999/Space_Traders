/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.system;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import spacetrader.Planet;
import spacetrader.Player;
import spacetrader.Universe;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class HomeScreenController implements Initializable {

    private MainController mainControl;
    private Universe universe;
    
    @FXML private Button marketPlaceButton;
    @FXML private Label planetName;
    @FXML private Label playerName;
    
    /**
     * Gives this controller a reference to the MainController.
     * @param mainControl the Main Controller of SpaceTrader
     */
    public void setMainControl(MainController mainControl) {
        this.mainControl = mainControl;
    }
    
    /**
     * Customizes text on homescreen based on player and planet names
     * @param player player that is currently playing
     * @param planet planet that player is currently on
     */
    public void setUpHomeScreen(Player player, Planet planet) {
        planetName.setText(planet.toString());
        playerName.setText(player.getName());
    }
    
    /**
     * Changes scene to planet's marketplace
     * @param event mouseclick on marketplace button
     */
    @FXML protected void goToMarket(ActionEvent event) {
        mainControl.goToMarketScreen(mainControl.game.getPlayer().getLocation());
    }
    
    /**
     * Changes scene to space map
     * @param event mouseclick on travel button
     */
    @FXML protected void goToSpace(ActionEvent event) {
        mainControl.goToSpaceMapScreen(mainControl.game.getPlayer().getLocation());
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
