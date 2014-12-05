package spacetrader.system;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import spacetrader.Player;
import spacetrader.SkillList.Skill;
import spacetrader.planets.Planet;
import spacetrader.ships.PlayerShip;
import spacetrader.ships.ShipType;

/**
 * FXML Controller class
 *
 * @author Tejas
 */
public class ShipMarketController
        extends SceneController
        implements Initializable {

    @FXML
    private Label beetlePrice, bumblebeePrice, fireflyPrice,
            fleaPrice, gnatPrice, grasshopperPrice,
            hornetPrice, mosquitoPrice, termitePrice,
            waspPrice;
    @FXML
    private RadioButton beetleRadioButton, bumblebeeRadioButton,
            fireflyRadioButton, fleaRadioButton,
            gnatRadioButton, grasshopperRadioButton,
            hornetRadioButton, mosquitoRadioButton,
            termiteRadioButton, waspRadioButton;
    @FXML
    private Label playerFunds, shipPrice;
    @FXML
    private GridPane shipGrid;
    @FXML

    private ShipType[] shipTypes;
    private RadioButton[] shipsButtons;
    private Label[] prices;
    private int selectedShip;

    private int playerShipSellingPrice;

    private Player player;
    private Planet planet;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fleaRadioButton.setSelected(true);
        selectedShip = 0;
    }

    /**
     * Sets up the ship market
     *
     * @param player the player object we are dealing with
     */
    public void setUpShipMarketScreen(Player player) {
        this.player = player;
        this.planet = player.getLocation();

        shipTypes = ShipType.values();

        shipsButtons = new RadioButton[] {fleaRadioButton, gnatRadioButton,
            fireflyRadioButton, mosquitoRadioButton, bumblebeeRadioButton,
            beetleRadioButton, hornetRadioButton, grasshopperRadioButton,
            termiteRadioButton, waspRadioButton};
        prices = new Label[] {fleaPrice, gnatPrice, fireflyPrice, mosquitoPrice,
            bumblebeePrice, beetlePrice,
            hornetPrice, grasshopperPrice, termitePrice, waspPrice};

        this.playerShipSellingPrice = player.getShip()
                .currentShipPriceWithoutCargo(player.getEffectiveSkill(Skill.TRADER));

        for (Node node : shipGrid.getChildren()) {
            Integer column = GridPane.getColumnIndex(node);
            if (column == null) {
                column = 0;
            }
            Integer row = GridPane.getRowIndex(node);
            if (row == null) {
                row = 0;
            }
            if (planet.getLevel().ordinal() < shipTypes[row]
                    .minTechLevel().ordinal()) {
                node.setVisible(false);
            } else if (column == 2 | column == 1) {
                Tooltip shipToolTip = new Tooltip();
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/spacetrader/ships/ShipInfoPane.fxml"));
                try {
                    Pane pane = loader.load();
                    ((ShipInfoPaneController) loader.getController())
                            .setShipType(shipTypes[row]);
                    shipToolTip.setGraphic(pane);
                    if (column == 2) {
                        setTextBlack(pane);
                    }
                } catch (IOException e) {
                    Logger.getLogger(SpaceTrader.class.getName())
                            .log(Level.SEVERE, null, e);
                }
                if (column == 2) {
                    PopOver popup = new PopOver(shipToolTip.getGraphic());
                    node.setOnMouseEntered((e) -> popup.show(node));
                    node.setOnMouseExited((e) -> popup.hide(new Duration(150)));
                } else {
                    Tooltip.install(node, shipToolTip);
                }
            } else if (column == 1) {
                if (shipPurchasingPrice(shipTypes[row]) > player.getCredits()
                        + player.getShip().currentShipPriceWithoutCargo(player.getEffectiveSkill(Skill.TRADER))) {
                    shipsButtons[row].setDisable(true);
                }
            }
        }

        this.playerShipSellingPrice = player.getShip()
                .currentShipPriceWithoutCargo(player.getEffectiveSkill(Skill.TRADER));

        playerFunds.setText("₪" + player.getCredits());
        shipPrice.setText("₪" + (shipPurchasingPrice(shipTypes[0])
                - playerShipSellingPrice));

        //Set all the price labels
        for (int i = 0; i < shipTypes.length; i++) {
            prices[i].setText("₪" + (shipPurchasingPrice(shipTypes[i])
                    - playerShipSellingPrice));
        }
    }

    private void setTextBlack(Pane pane) {
        for (Node node : pane.getChildren()) {
            if (node instanceof Text) {
                ((Text) node).setFill(Color.BLACK);
            } else {
                if (node instanceof Pane) {
                    setTextBlack((Pane) node);
                }
            }
        }
    }

    /**
     * Processes the event where a ship is selected
     *
     * @param event mouse click on radio button
     */
    @FXML
    protected void selectShip(ActionEvent event) {
        RadioButton src = (RadioButton) event.getSource();
        int counter = 0;
        for (RadioButton button : shipsButtons) {
            if (button.equals(src)) {
                shipPrice.setText("₪" + (shipPurchasingPrice(shipTypes[counter])
                        - playerShipSellingPrice));
                shipsButtons[selectedShip].setSelected(false);
                selectedShip = counter;
            }
            counter++;
        }
    }

    /**
     * Processes the event where the user wants to buy the currently selected
     * ship.
     *
     * @param event mouse click on buy button
     */
    @FXML
    protected void processBuyShip(ActionEvent event) {
        if (shipTypes[selectedShip] == player.getShip().getType()) {
            mainControl.displayErrorMessage(null, "Ship Already In Possession", "You already "
                    + "own this type of ship! There is no benefit in buying it again");
            return;
        }
        
        int costOfPurchase = shipPurchasingPrice(shipTypes[selectedShip])
                - playerShipSellingPrice;
        if (player.getCredits() >= costOfPurchase) {
            String mastHead = String.format(
                    "Are you sure you want to buy a %s?",
                    shipTypes[selectedShip].toString());
            String shipMessage = String.format(
                    "If you click \"yes\", your current ship "
                    + "and all its equipment will be sold. "
                    + "Cargo and crew members will transfer to "
                    + "the new ship in so much as there is room.");
            boolean response = mainControl.displayYesNoConfirmation(
                    "Ship Purchase Confirmation", mastHead, shipMessage);
            if (!response) {
                return;
            }
            if (costOfPurchase < 0) {
                player.addCredits(-costOfPurchase);
            } else {
                player.removeCredits(costOfPurchase);
            }
            //All the old equipment on the player's ship will be sold.
            //It is already included in the calculation of "costOfPurchase".

            PlayerShip oldShip = player.getShip();
            player.setShip(new PlayerShip(shipTypes[selectedShip], player));
            player.getCargo().addCargoContents(oldShip.getCargo());

            //the escape pod on your old ship transfers to the new ship
            if (oldShip.hasEscapePod()) {
                player.getShip().setEscapePod();
            }

            mainControl.displayInfoMessage(null, "Purchase Successful!",
                    "Congratulations on your new ship! ");

            goBackToShipYardScreen();
        } else {
            mainControl.displayWarningMessage(null, "Acquire More Cash!",
                    "You do not have enough credits to purchase this ship!");
        }
    }

    /*
     * Goes back to the Ship Yard Screen
     */
    @FXML
    protected void goBackToShipYardScreen() {
        mainControl.goToShipYardScreen();
    }

    /**
     * Determines the price for buying a ship. The price is fluctuated from the
     * ship type's base price by the player's trader skill.
     *
     * @param type the type of ship
     * @return the price of this ship
     */
    private int shipPurchasingPrice(ShipType type) {
        double priceOfShip = type.price() * (100 - player.getEffectiveSkill(Skill.TRADER)) / 100.0;

        return (int) priceOfShip;
    }

}
