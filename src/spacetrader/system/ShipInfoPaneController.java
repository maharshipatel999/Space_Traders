/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.system;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import spacetrader.ships.ShipType;

/**
 * FXML Controller class.
 *
 * @author Seth
 */
public class ShipInfoPaneController implements Initializable {

    @FXML
    private Text nameText, sizeText;
    @FXML
    private Text cargoSlotsText, fuelCapacityText, hullStrengthText;
    @FXML
    private Text weaponSlotsText, shieldSlotsText, gadgetSlotsText;
    @FXML
    private Text crewQuartersText;
    @FXML
    private ImageView shipImage;

    private static final String[] SIZES = new String[] {"Tiny", "Small", "Medium", "Large", "Huge"};
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Sets the pane's text to match the specified ship type's information.
     * 
     * @param ship type to set to
     */
    public void setShipType(ShipType ship) {
        nameText.setText(ship.toString());
        
        String size = SIZES[ship.size()];
        sizeText.setText(size);
        cargoSlotsText.setText(String.valueOf(ship.cargoBay()));
        fuelCapacityText.setText(String.valueOf(ship.fuel()));
        hullStrengthText.setText(String.valueOf(ship.hullStrength()));
        weaponSlotsText.setText(String.valueOf(ship.weaponSlots()));
        shieldSlotsText.setText(String.valueOf(ship.shieldSlots()));
        gadgetSlotsText.setText(String.valueOf(ship.gadgetSlots()));
        crewQuartersText.setText(String.valueOf(ship.crew()));
        shipImage.setImage(new Image(ship.spriteFile()));
    }
}
