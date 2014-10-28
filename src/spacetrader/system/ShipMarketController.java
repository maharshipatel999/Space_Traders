
package spacetrader.system;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import org.controlsfx.dialog.Dialogs;
import spacetrader.Player;
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
    
    private ShipType[] ships;
    private RadioButton[] shipsButtons;
    private Label[] prices;
    private int currentlySelectedShip;
    
    Player player;
    Planet planet;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setUpShipMarketScreen(Player player) {
        this.player = player;
        this.planet = player.getLocation();
        
        playerFunds.setText("₪" + player.getWallet().getCredits());
        
        ships = new ShipType[] {ShipType.BEETLE, ShipType.BUMBLEBEE, ShipType.FIREFLY, ShipType.FLEA, ShipType.GNAT,
            ShipType.GRASSHOPPER, ShipType.HORNET, ShipType.MOSQUITO, ShipType.TERMITE, ShipType.WASP};
        shipsButtons = new RadioButton[] {beetleRadioButton, bumblebeeRadioButton, fireflyRadioButton, fleaRadioButton,
            gnatRadioButton, grasshopperRadioButton, hornetRadioButton, mosquitoRadioButton,
            termiteRadioButton, waspRadioButton};
        prices = new Label[] {beetlePrice, bumblebeePrice, fireflyPrice, fleaPrice, gnatPrice, grasshopperPrice,
            hornetPrice, mosquitoPrice, termitePrice, waspPrice};
        
        beetlePrice.setText("₪" + ShipType.BEETLE.price());
        bumblebeePrice.setText("₪" + ShipType.BUMBLEBEE.price());
        fireflyPrice.setText("₪" + ShipType.FIREFLY.price());
        fleaPrice.setText("₪" + ShipType.FLEA.price());
        gnatPrice.setText("₪" + ShipType.GNAT.price());
        grasshopperPrice.setText("₪" + ShipType.GRASSHOPPER.price());
        hornetPrice.setText("₪" + ShipType.HORNET.price());
        mosquitoPrice.setText("₪" + ShipType.MOSQUITO.price());
        termitePrice.setText("₪" + ShipType.TERMITE.price());
        waspPrice.setText("₪" + ShipType.WASP.price());
        
    }
    
    @FXML protected void selectShip(ActionEvent event) {
        RadioButton src = (RadioButton) event.getSource();
        int counter = 0;
        for (RadioButton button : shipsButtons) {
            if (button.equals(src)) {
                shipPrice.setText("₪" + ships[counter].price());
                currentlySelectedShip = counter;
                deselectOtherButtons(counter);
            }
            counter++;
        }
    }
    
    private void deselectOtherButtons(int notThisButton) {
        for (int i = 0; i < ShipType.SIZE ; i++) {
            if (i != notThisButton) {
                shipsButtons[i].setSelected(false);
            }
        }
    }
    
    @FXML protected void processBuyShip(ActionEvent event) {
        if (player.getWallet().getCredits() > ships[currentlySelectedShip].price()) {
            player.getWallet().remove(ships[currentlySelectedShip].price());
            player.setShip(new PlayerShip(ships[currentlySelectedShip]));
            mainControl.goToShipMarket();
        }
    }
    
}
