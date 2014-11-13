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
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import spacetrader.planets.Planet;
import spacetrader.commerce.TradeGood;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class MapDetailController implements Initializable {

    @FXML
    private Text planetName;
    @FXML
    private Text planetLevel;
    @FXML
    private Text planetGovt;
    @FXML
    private Text policePresence;
    @FXML
    private Text piratePresence;
    @FXML
    private Text planetResources;
    @FXML
    private Text outOfRangeText;
    @FXML
    private VBox planetInformation;
    @FXML
    private VBox averagePriceList;
    @FXML
    private HBox priceDisplayButtons;
    @FXML
    private ToggleGroup prices;
    @FXML
    private Button warpButton;
    @FXML
    private Label appxWaterPrice, appxFursPrice, appxFoodPrice,
            appxOrePrice, appxGamesPrice,
            appxFirearmsPrice, appxMedicinePrice,
            appxMachinesPrice, appxNarcoticsPrice,
            appxRobotsPrice;

    private Label[] appxPricesLabels;

    private SpaceMapScreenController mapControl;
    private Planet selectedPlanet;
    private boolean showAbsolutePrices;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showAbsolutePrices = true;
    }

    /**
     * Gives this controller a reference to the SpaceMapScreenController.
     *
     * @param mapControl the map controller
     */
    public void setMapControl(SpaceMapScreenController mapControl) {
        this.mapControl = mapControl;
    }

    /**
     * Fills in the sidebar with information from the planet selected
     *
     * @param planet the selected planet
     */
    public void setPlanetInfo(Planet planet) {
        selectedPlanet = planet;
        planetName.setText(planet.getName());
        planetLevel.setText(planet.getLevel().type());
        planetGovt.setText(planet.getPoliticalSystem().type());
        policePresence.setText(planet.expectedAmountOfPolice());
        piratePresence.setText(planet.expectedAmountOfPirates());

        this.appxPricesLabels = new Label[]{appxWaterPrice,
            appxFursPrice, appxFoodPrice,
            appxOrePrice, appxGamesPrice, appxFirearmsPrice,
            appxMedicinePrice,
            appxMachinesPrice, appxNarcoticsPrice,
            appxRobotsPrice};

        if (planet.isVisited()) {
            planetResources.setText(planet.getResource().type());
        } else {
            planetResources.setText("Unknown");
        }

        if (mapControl.isInRangeOf(planet)) {
            averagePriceList.setDisable(false);
            priceDisplayButtons.setDisable(false);
            warpButton.setDisable(false);
            planetInformation.getChildren().remove(outOfRangeText);
            setApproximatePrices();

        } else {
            averagePriceList.setDisable(true);
            priceDisplayButtons.setDisable(true);
            warpButton.setDisable(true);
            if (!planetInformation.getChildren().contains(outOfRangeText)) {
                planetInformation.getChildren().add(outOfRangeText);
            }
            hideApproximatePrices();
        }

    }

    /**
     * Hide price labels.
     */
    public void hideApproximatePrices() {
        for (int i = 0; i < TradeGood.values().length; i++) {
            appxPricesLabels[i].setText("-----");
            changeFont(appxPricesLabels[i], false);
        }
    }

    /**
     * Sets approximate prices of each good in map detail sidebar
     */
    private void setApproximatePrices() {
        for (int i = 0; i < TradeGood.values().length; i++) {
            TradeGood good = TradeGood.values()[i];
            int absolutePrice = selectedPlanet.getMarket().getAppxPrice(good);
            int currentPlanetPrice = mapControl.currentPlanet
                    .getMarket().getBuyPrice(good);
            changeFont(appxPricesLabels[i], false);
            //if the selected planet sells this good
            if (absolutePrice >= 0) {
                //determine whether to show absolute prices or relative prices
                if (showAbsolutePrices) {
                    this.appxPricesLabels[i].setText("₪" + absolutePrice);
                } else {
                    int relativePrice = absolutePrice - currentPlanetPrice;
                    appxPricesLabels[i].setText(
                            ((relativePrice >= 0) ? "+ ₪" : "- ₪")
                                    + Math.abs(relativePrice));
                    if (currentPlanetPrice < 0) {
                        appxPricesLabels[i].setText("-----");
                    }
                }
            } else {
                appxPricesLabels[i].setText("-----");
            }
            if (absolutePrice > currentPlanetPrice && currentPlanetPrice >= 0) {
                changeFont(appxPricesLabels[i], true);
            }
        }
    }

    /**
     * Sets the font bold if isBold is true, otherwise the font is normal
     *
     * @param text text of label
     * @param isBold true if the font should be bold
     */
    private void changeFont(Label text, boolean isBold) {
        String fontFamily = text.getFont().getFamily();
        double fontSize = text.getFont().getSize();
        FontWeight style = isBold ? FontWeight.BOLD : FontWeight.NORMAL;
        text.setFont(Font.font(fontFamily, style, fontSize));
    }

    @FXML
    protected void absolutePressed(ActionEvent event) {
        if (!showAbsolutePrices) {
            showAbsolutePrices = true;
            setApproximatePrices();
        }
    }

    @FXML
    protected void relativePressed(ActionEvent event) {
        if (showAbsolutePrices) {
            showAbsolutePrices = false;
            setApproximatePrices();
        }
    }

    /**
     * Warps the player to the selected planet
     *
     * @param event the event to occue
     */
    @FXML
    protected void warpToPlanet(ActionEvent event) {
        mapControl.travelToPlanet();
    }
}
