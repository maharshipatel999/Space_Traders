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
import spacetrader.commerce.PriceIncreaseEvent;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class HomeScreenController extends SceneController implements Initializable {

    private Planet planet;
    
    @FXML private Button marketPlaceButton;
    @FXML private Label planetName;
    
    /**
     * Customizes text on homes screen based on player and planet names
     * @param planet planet that player is currently on
     */
    public void setUpHomeScreen(Planet planet) {
        this.planet = planet;
        String description = String.format("%s\n\nLocation: (%d, %d)\nTech Level: %s\nGovernment: %s\nSpecial Resources: %s",
                planet.getName(), planet.getLocation().x, planet.getLocation().y, planet.getLevel().type(), planet.getPoliticalSystem().type(), planet.getResource().type());
        if (planet.getPriceIncEvent() != PriceIncreaseEvent.NONE) {
            description += "\n\nThis planet is currently suffering an abdormality:\n" + planet.getPriceIncEvent().desc();
        }
        
        planetName.setText(description);
    }
    
    /**
     * Changes scene to planet's marketplace
     * @param event mouseclick on marketplace button
     */
    @FXML protected void goToMarket(ActionEvent event) {
        mainControl.goToMarketScreen(planet);
    }
    
    /**
     * Changes scene to space map
     * @param event mouseclick on travel button
     */
    @FXML protected void goToSpace(ActionEvent event) {
        mainControl.goToSpaceMapScreen(planet);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
