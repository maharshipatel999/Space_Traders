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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.controlsfx.control.MasterDetailPane;
import spacetrader.Planet;
import spacetrader.Universe;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class SpaceMapScreenController implements Initializable {

    @FXML private MasterDetailPane mapScreen;
    
    private MainController mainControl;
    private ArrayList<Planet> planets;
    private MapPane planetMap;
    private Pane planetInfo;
    
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
        
        mapScreen.setDetailNode(planetInfo);
    }
    
    /**
     * Gives this controller a reference to the MainController.
     * @param mainControl the Main Controller of SpaceTrader
     */
    public void setMainControl(MainController mainControl) {
        this.mainControl = mainControl;
    }
    
    public void setUpMap(ArrayList<Planet> planets) {
        this.planets = planets;
        planetMap.addPlanets();
        mapScreen.setDividerPosition((planetInfo.getPrefWidth()) / mapScreen.getWidth());
    }
    
    private void showPlanetInfo(Planet planet) {
        /*Circle icon = planetMap.planetIcons.get(planet);
        System.out.println(icon.getCenterX() + ", " +  planetInfo.getPrefWidth() + ", " +  Math.abs(planetMap.getTranslateX()) + ", " +  mapScreen.getWidth());
        if (icon.getCenterX() + planetInfo.getPrefWidth() > Math.abs(planetMap.getTranslateX()) + mapScreen.getWidth()) {
            planetMap.dragContext.x -= planetInfo.getPrefWidth();
            TranslateTransition tt = new TranslateTransition(Duration.millis(100), planetMap);
            tt.setByX(-1 * planetInfo.getPrefWidth());
            tt.play();
            wasShifted = true;
        }*/
        System.out.println("\n" + planetMap.getTranslateX() + ", " + planetMap.getLayoutX());
        mapScreen.setShowDetailNode(true);
        System.out.println(planetMap.getTranslateX() + ", " + planetMap.getLayoutX());
        //planetMap.dragContext.x = planetMap.getTranslateX();
    }
    
    private void hidePlanetInfo() {
        mapScreen.setShowDetailNode(false);
        //planetMap.dragContext.x = planetMap.getTranslateX();
        /*if (wasShifted) {
            planetMap.dragContext.x += planetInfo.getPrefWidth();
            TranslateTransition tt = new TranslateTransition(Duration.millis(100), planetMap);
            tt.setByX(planetInfo.getPrefWidth());
            tt.play();
            wasShifted = false;
        }*/
        
    }
    
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

                //printLocation(event);
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

        private void addPlanets() {
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

                Text nameText = new Text(planetX + PLANET_RADIUS, planetY - PLANET_RADIUS, planet.getName());
                nameText.setFill(Color.WHITE);

                this.getChildren().add(planetIcon);
                this.getChildren().add(nameText);
            }
        }

        //for testing
        private void printLocation(MouseEvent event) {
            System.out.printf("%n%f, %f, %f, %f%n", this.getWidth(), this.getHeight(), this.getPrefWidth(), this.getPrefHeight());
            System.out.printf("%f, %f, %f, %f%n", mapScreen.getWidth(), mapScreen.getHeight(), mapScreen.getPrefWidth(), mapScreen.getPrefHeight());
            System.out.printf("%f, %f%n", background.getWidth(), background.getHeight());
            System.out.printf("%f, %f, %f, %f%n", this.getTranslateX(), this.getTranslateY(), this.getLayoutX(), this.getLayoutY());
            System.out.printf("%f, %f%n", this.dragContext.x, this.dragContext.y);
            System.out.printf("%f, %f, Scene: %f, %f, Screen: %f, %f%n", event.getX(), event.getY(), event.getSceneX(), event.getSceneY(), event.getScreenX(), event.getScreenY());

        }
    
        private final class MapDragContext {
            public double mouseX;
            public double mouseY;
            public double x;
            public double y;
        }
    }
}
