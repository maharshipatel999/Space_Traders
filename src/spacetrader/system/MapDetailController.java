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
import javafx.scene.text.Text;
import spacetrader.Planet;
import spacetrader.commerce.TradeGood;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class MapDetailController implements Initializable {
  
    @FXML private Text planetName;
    @FXML private Text planetLevel;
    @FXML private Text planetGovt;
    @FXML private Text policePresence;
    @FXML private Text piratePresence;
    @FXML private Text planetResources;
    @FXML private Text outOfRangeText;
    @FXML private VBox planetInformation;
    @FXML private VBox averagePriceList;
    @FXML private HBox priceDisplayButtons;
    @FXML private ToggleGroup prices;
    @FXML private Button warpButton;
    @FXML private Label appxWaterPrice, appxFursPrice, appxFoodPrice, appxOrePrice, appxGamesPrice,
            appxFirearmsPrice, appxMedicinePrice, appxMachinesPrice, appxNarcoticsPrice, appxRobotsPrice;
    
    private SpaceMapScreenController mapControl;
    private Planet selectedPlanet;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //todo
    }
    
    /**
     * Gives this controller a reference to the SpaceMapScreenController.
     * @param mainControl the map controller
     */
    public void setMapControl(SpaceMapScreenController mapControl) {
        this.mapControl = mapControl;
    }
    
    /**
     * Fills in the sidebar with information from the planet selected
     * @param planet the selected planet
     */
    public void setPlanetInfo(Planet planet) {
        selectedPlanet = planet;
        planetName.setText(planet.getName());
        planetLevel.setText(planet.getLevel().type());
        planetGovt.setText(planet.getPoliticalSystem().type());
        policePresence.setText(planet.expectedAmountOfPolice());
        piratePresence.setText(planet.expectedAmountOfPirates());
        
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
            //TODO: set up average price list
            
        } else {
            averagePriceList.setDisable(true);
            priceDisplayButtons.setDisable(true);
            warpButton.setDisable(true);
            if (!planetInformation.getChildren().contains(outOfRangeText)) {
                planetInformation.getChildren().add(outOfRangeText);
            }
        }
        setApproximatePrices();
        
    }
    
    /**
     * Sets approximate prices of each good in map detail sidebar
     */
    private void setApproximatePrices() {
        Label[] appxPricesLabels  = new Label[] { appxWaterPrice, appxFursPrice, appxFoodPrice,
            appxOrePrice, appxGamesPrice, appxFirearmsPrice, appxMedicinePrice,
            appxMachinesPrice,appxNarcoticsPrice, appxRobotsPrice };
        for (int i = 0; i < TradeGood.values().length; i++) {
            TradeGood good = TradeGood.values()[i];
            int appxPrice = selectedPlanet.getMarket().getAppxPrices().get(good);
            if (appxPrice >= 0) {
                appxPricesLabels[i].setText("₪" + appxPrice);
            } else {
                appxPricesLabels[i].setText("-----");
            }
        }
    }
    
    /**
     * Warps the player to the selected planet
     * @param event the event to occue
     */
    @FXML protected void warpToPlanet(ActionEvent event) {
        mapControl.travelToPlanet();
    }
}
