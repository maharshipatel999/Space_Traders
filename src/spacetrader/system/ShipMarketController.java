
package spacetrader.system;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import spacetrader.Player;
import spacetrader.commerce.Cargo;
import spacetrader.planets.Planet;
import spacetrader.ships.PlayerShip;
import spacetrader.ships.ShipType;

/**
 * FXML Controller class
 *
 * @author Tejas
 */
public class ShipMarketController extends SceneController implements Initializable {

    
    @FXML private Label beetlePrice, bumblebeePrice, fireflyPrice, fleaPrice, gnatPrice, grasshopperPrice,
            hornetPrice, mosquitoPrice, termitePrice, waspPrice;
    @FXML private RadioButton beetleRadioButton, bumblebeeRadioButton, fireflyRadioButton, fleaRadioButton,
            gnatRadioButton, grasshopperRadioButton, hornetRadioButton, mosquitoRadioButton,
            termiteRadioButton, waspRadioButton;
    @FXML private Label playerFunds, shipPrice;
    
    private ShipType[] shipTypes;
    private RadioButton[] shipsButtons;
    private Label[] prices;
    private int currentlySelectedShip;
    
    private int playerShipSellingPrice;
    
    Player player;
    Planet planet;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fleaRadioButton.setSelected(true);
        currentlySelectedShip = 0;
    }
    
    public void setUpShipMarketScreen(Player player) {
        this.player = player;
        this.planet = player.getLocation();
        
        this.playerShipSellingPrice = player.getShip().currentShipPriceWithoutCargo();
        
        shipTypes = ShipType.values();
        
        playerFunds.setText("₪" + player.getWallet().getCredits());
        shipPrice.setText("₪" + (shipTypes[0].price() - playerShipSellingPrice));
        
        shipsButtons = new RadioButton[] {fleaRadioButton, gnatRadioButton, fireflyRadioButton, mosquitoRadioButton,
            bumblebeeRadioButton, beetleRadioButton, hornetRadioButton, grasshopperRadioButton,
            termiteRadioButton, waspRadioButton};
        prices = new Label[] {fleaPrice, gnatPrice, fireflyPrice, mosquitoPrice, bumblebeePrice, beetlePrice,
            hornetPrice, grasshopperPrice, termitePrice, waspPrice};
        
        //Set all the price labels
        for (int i = 0; i < shipTypes.length ; i++) {
            prices[i].setText("₪" + (shipTypes[i].price() - playerShipSellingPrice));
        }
    }
    
    @FXML protected void selectShip(ActionEvent event) {
        RadioButton src = (RadioButton) event.getSource();
        int counter = 0;
        for (RadioButton button : shipsButtons) {
            if (button.equals(src)) {
                shipPrice.setText("₪" + (shipTypes[counter].price() - playerShipSellingPrice));
                shipsButtons[currentlySelectedShip].setSelected(false);
                currentlySelectedShip = counter;
            }
            counter++;
        }
    }
    
    /*
    private void deselectOtherButtons(int notThisButton) {
        for (int i = 0; i < ShipType.VALUES.size() ; i++) {
            if (i != notThisButton) {
                shipsButtons[i].setSelected(false);
            }
        }
    }
    */
    
    @FXML protected void processBuyShip(ActionEvent event) {
        int costOfPurchase = shipTypes[currentlySelectedShip].price() - playerShipSellingPrice;
        if (player.getWallet().getCredits() >= costOfPurchase) {
            player.getWallet().setCredits(costOfPurchase);
            Cargo oldCargo = player.getShip().getCargo();
            player.setShip(new PlayerShip(shipTypes[currentlySelectedShip]));
            player.getShip().getCargo().addCargoContents(oldCargo);
            mainControl.goToShipMarket();
        } else {
            mainControl.displayAlertMessage("Acquire More Cash!", "You do not have enough credits to purchase this ship!");
        }
    }
    
}
