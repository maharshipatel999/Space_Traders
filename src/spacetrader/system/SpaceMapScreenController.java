/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.system;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.controlsfx.control.MasterDetailPane;
import spacetrader.Planet;
import spacetrader.Player;
import spacetrader.Universe;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class SpaceMapScreenController implements Initializable {
    @FXML private MasterDetailPane mapScreen;
    @FXML private Text fuelRemaingText;
    
    
    private MainController mainControl;
    private MapDetailController infoControl;
    private MapPane planetMap;
    private Pane planetInfo;
    
    private Planet currentPlanet;
    private int fuelAmount;
    private int maxFuelAmount;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        planetMap = new MapPane();
        mapScreen.setMasterNode(planetMap);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/spacetrader/MapDetailPane.fxml"));
        try {
            planetInfo = loader.load();
        } catch (IOException e) {
            Logger.getLogger(SpaceTrader.class.getName()).log(Level.SEVERE, null, e);
        }
        infoControl = loader.getController();
        infoControl.setMapControl(this);
        
        mapScreen.setDetailNode(planetInfo);
    }
    
    /**
     * Gives this controller a reference to the MainController.
     * @param mainControl the Main Controller of SpaceTrader
     */
    public void setMainControl(MainController mainControl) {
        this.mainControl = mainControl;
    }
    
    
    /**
     * Adds all the planets to the map.
     * @param player
     * @param currentPlanet the planet the player currently is
     * @param planets the planets this map will display.
     */
    public void setUpMap(Player player, Planet currentPlanet, ArrayList<Planet> planets) {
        this.currentPlanet = currentPlanet;
        this.fuelAmount = player.getShip().getTank().getFuelAmount();
        this.maxFuelAmount = player.getShip().getTank().getMaxFuel();
        fuelRemaingText.setText("Fuel Remaining : " + fuelAmount + "/" + maxFuelAmount);
        planetMap.addPlanets(planets);
        
        mapScreen.setDividerPosition((planetInfo.getPrefWidth()) / mapScreen.getWidth());
    }
    
    /**
     * Determines if a specified planet is in range of the current planet given the player's current
     * fuel amount and spaceship type.
     * @param planet the planet we are trying to travel to
     * @return true if the planet is in range
     */
    public boolean isInRangeOf(Planet planet) {
        return Universe.distanceBetweenPlanets(currentPlanet, planet) < fuelAmount;
    }
    
    public void travelToPlanet() {
        mainControl.goToWarpScreen(this.currentPlanet, planetMap.selectedPlanet);
    }
    
    /**
     * !!! When the pane is revealed the right part of the map is unviewable!!! need to fix
     * Reveals the detail pane with the specified planet's information.
     * @param planet selected planet
     */
    private void showPlanetInfo(Planet planet) {
        mapScreen.setShowDetailNode(true);
        infoControl.setPlanetInfo(planet);
    }
    
    /**
     * Hides the detail pane.
     */
    private void hidePlanetInfo() {
        mapScreen.setShowDetailNode(false);
    }
    @FXML protected void backToPlanet(ActionEvent event) {
            mainControl.goToHomeScreen(currentPlanet);
        }
    @Override
    public String toString() {
        String toString = String.format("Size:(%f, %f)", mapScreen.getWidth(), mapScreen.getHeight());
        return toString;
    }
    
    /**
     * Represents the map of all the planets. Scales the Universe to the size
     * of the map display. The map is draggable within the master pane.
     */
    private class MapPane extends Pane {
        public static final double MAP_WIDTH = 1800;
        public static final double MAP_HEIGHT = 1200;
        public static final double LEFT_MARGIN = 40, RIGHT_MARGIN = 90;
        public static final double TOP_MARGIN = 40, BOTTOM_MARGIN = 40;
        public static final double PLANET_RADIUS = 8;
        
        private final MapDragContext dragContext = new MapDragContext();
        
        private Rectangle background;
        private Planet selectedPlanet;
        private Map<Planet, Circle> planetIcons;

        public MapPane() {
            addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> {
                //remember initial mouse cursor coordinates and node position
                dragContext.mouseX = event.getSceneX();
                dragContext.mouseY = event.getSceneY();
                dragContext.x = this.getTranslateX();
                dragContext.y = this.getTranslateY();
            });
            
            addEventHandler(MouseEvent.MOUSE_DRAGGED, (event) -> {
                //Get the exact translated X and doubleY coordinate
                double tempX = dragContext.x + event.getSceneX() - dragContext.mouseX;
                double tempY = dragContext.y + event.getSceneY() - dragContext.mouseY;

                //Set the positon of mapSpace Pane after
                if (tempX <= 0 && tempX >= (mapScreen.getWidth() - background.getWidth())) {
                    dragContext.x = tempX;
                    this.setTranslateX(dragContext.x);
                }
                if (tempY <= 0 && tempY >= (mapScreen.getHeight() - background.getHeight())) {
                    dragContext.y = tempY;
                    this.setTranslateY(dragContext.y);
                }
                //Set current Mouse X and Y position
                dragContext.mouseX = event.getSceneX();
                dragContext.mouseY = event.getSceneY();
            });
            
           addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
                if(event.getButton().equals(MouseButton.PRIMARY)
                                        && event.getClickCount() == 2) {
                    hidePlanetInfo();
                    if (selectedPlanet != null) {
                        planetIcons.get(selectedPlanet).setFill(Color.GREEN);
                        selectedPlanet = null;
                    }
                }
            });
        }

        private void addPlanets(ArrayList<Planet> planets) {
            background = new Rectangle(MAP_WIDTH + LEFT_MARGIN + RIGHT_MARGIN,
                                       MAP_HEIGHT + TOP_MARGIN + BOTTOM_MARGIN,
                                       Color.BLACK);
            this.getChildren().add(background);

            planetIcons = new HashMap<>();
            
            for (Planet planet : planets) {
                //assign each planet a location that is scaled to the size of the map
                double planetX = (MAP_WIDTH * (planet.getLocation().getX() / Universe.WIDTH)) + LEFT_MARGIN; //remove magic number
                double planetY = (MAP_HEIGHT * (planet.getLocation().getY() / Universe.HEIGHT)) + TOP_MARGIN;

                //create an icon for each planet
                Circle planetIcon = new Circle(planetX, planetY, PLANET_RADIUS, Color.GREEN);
                planetIcon.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) ->
                    {
                        if (selectedPlanet == null) {
                            showPlanetInfo(planet);
                            planetIcon.setFill(Color.RED);
                        } else if (planet != selectedPlanet) {
                            showPlanetInfo(planet);
                            planetIcon.setFill(Color.RED);
                            planetIcons.get(selectedPlanet).setFill(Color.GREEN);
                        }
                        selectedPlanet = planet;
                    });
                planetIcons.put(planet, planetIcon); 

                //Create text for the name of each planet.
                double textX = planetX - (PLANET_RADIUS / 2) - (5 * planet.getName().length() / 2);
                double textY = planetY - PLANET_RADIUS - 4;
                Text nameText = new Text(textX, textY, planet.getName());
                nameText.setFill(Color.WHITE);

                this.getChildren().add(planetIcon);
                this.getChildren().add(nameText);
                
                //Create flight radius
                if (planet == currentPlanet) {
                    double maxTravelDistance = (MAP_WIDTH * (fuelAmount / (double) Universe.WIDTH));
                    planetIcon.setFill(Color.BLUE);
                    Circle flightRadius = new Circle(planetIcon.getCenterX(), planetIcon.getCenterY(), maxTravelDistance, Color.TRANSPARENT);
                    flightRadius.setOpacity(.6);
                    flightRadius.setStroke(Color.LAWNGREEN);
                    this.getChildren().add(flightRadius);
                }
            }
        }     
    
        @Override
        public String toString() {
            String toString = String.format("Size:(%f, %f), RectSize:(%f, %f)", getWidth(), getHeight(), background.getWidth(), background.getHeight());
            toString += String.format(" TranslateX,Y:(%f, %f), LayoutX,Y:(%f, %f)", getTranslateX(), getTranslateY(), getLayoutX(), getLayoutY());
            toString += String.format("%nDragContext x,y:(%f, %f), MouseX,Y:(%f, %f)", dragContext.x, dragContext.y, dragContext.mouseX, dragContext.mouseY);
            return toString;
        }
        
        private final class MapDragContext {
            public double mouseX;
            public double mouseY;
            public double x;
            public double y;
        }
    }
}
