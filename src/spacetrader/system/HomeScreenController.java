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
import spacetrader.Player;
import spacetrader.commerce.PriceIncreaseEvent;
import spacetrader.planets.Planet;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class HomeScreenController extends SceneController implements Initializable {

    @FXML
    private Button marketPlaceButton, shipYard, bankButton;
    @FXML
    private Label planetName, planetInfo;
    @FXML
    private Button saveGame;

    private Planet planet;
    private Player player;

    /**
     * Customizes text on homes screen based on player and planet names
     *
     * @param player the game's player
     * @param planet planet that player is currently on
     */
    public void setUpHomeScreen(Player player, Planet planet) {
        this.planet = planet;
        this.player = player;

        String description = String
                .format("\nLocation: (%d, %d)\nTech Level: %s\nGovernment: %s\nSpecial Resources: %s",
                        (int) planet.getLocation().getX(), (int) planet.getLocation().getY(),
                        planet.getLevel().type(),
                        planet.getPoliticalSystem().type(),
                        planet.getResource().type());
        if (planet.getPriceIncEvent() != PriceIncreaseEvent.NONE) {
            description += "\n\nThis planet is currently "
                    + "suffering from an abnormality:\n"
                    + planet.getPriceIncEvent().desc();
        }

        planetName.setText(planet.getName());
        planetInfo.setText(description);
    }

    /**
     * Changes scene to planet's marketplace
     *
     * @param event mouseclick on marketplace button
     */
    @FXML
    protected void goToMarket(ActionEvent event) {
        //((Node) event.getSource()).setCursor(Cursor.WAIT);
        mainControl.goToMarketScreen(planet);
    }

    /**
     * Changes scene to space map
     *
     * @param event mouseclick on travel button
     */
    @FXML
    protected void goToSpace(ActionEvent event) {
        if (player.getDebt() > 10000) {
            mainControl.displayAlertMessage("Restricted from travel!",
                    "You are not allowed to travel because your debt is too high!");
        } else {
            mainControl.goToSpaceMapScreen(planet);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    protected void goToShipYardScreen() {
        mainControl.goToShipYardScreen();
    }

    @FXML
    public void goToOverwrite() {
        mainControl.goToOverwriteScreen();
    }

    @FXML
    public void goToFinanceScreen() {
        mainControl.goToFinanceScreen(planet);
    }
}
