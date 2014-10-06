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
    
    public void setPlanetInfo(Planet planet) {
        selectedPlanet = planet;
        planetName.setText(planet.getName());
        planetLevel.setText(planet.getLevel().type());
        planetGovt.setText(planet.getPoliticalSystem().type());
        policePresence.setText(determinePoliceAmount(planet));
        piratePresence.setText(determinePirateAmount(planet));
        
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
        
        
    }
    
    //I just used completely random numbers. This needs to be actually calculated.
    private String determinePoliceAmount(Planet planet) {
        double policeVariable = Math.random() * 21; //SHOULDNT BE RANDOM
        String policeAmount;
        if (policeVariable > 21) {
            policeAmount = "Swarms";
        } else if (policeVariable > 18) {
            policeAmount = "Abundant";
        } else if (policeVariable > 15) {
            policeAmount = "Many";
        } else if (policeVariable > 12) {
            policeAmount = "Moderate";
        } else if (policeVariable > 9) {
            policeAmount = "Some";
        } else if (policeVariable > 6) {
            policeAmount = "Few";
        } else if (policeVariable > 3) {
            policeAmount = "Minimal";
        } else {
            policeAmount = "Absent";
        }
        return policeAmount;
    }

    //I just used completely random numbers. This needs to be actually calculated.
    private String determinePirateAmount(Planet planet) {
        double pirateVariable = Math.random() * 21; //SHOULDNT BE RANDOM
        String pirateAmount;
        if (pirateVariable > 21) {
            pirateAmount = "Swarms";
        } else if (pirateVariable > 18) {
            pirateAmount = "Abundant";
        } else if (pirateVariable > 15) {
            pirateAmount = "Many";
        } else if (pirateVariable > 12) {
            pirateAmount = "Moderate";
        } else if (pirateVariable > 9) {
            pirateAmount = "Some";
        } else if (pirateVariable > 6) {
            pirateAmount = "Few";
        } else if (pirateVariable > 3) {
            pirateAmount = "Minimal";
        } else {
            pirateAmount = "Absent";
        }
        return pirateAmount;
    }
    
    @FXML protected void warpToPlanet(ActionEvent event) {
        mapControl.travelToPlanet();
    }
}
