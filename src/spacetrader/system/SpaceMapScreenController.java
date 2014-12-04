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
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.MasterDetailPane;
import org.controlsfx.control.PopOver;
import spacetrader.Player;
import spacetrader.Universe;
import spacetrader.planets.Planet;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class SpaceMapScreenController extends SceneController implements Initializable {

    @FXML
    private MasterDetailPane mapScreen;
    @FXML
    private Text fuelRemaingText;
    @FXML
    private Label helpfulPanelInfoText;
    
    private static final Color SELECTED_PLNT_COLOR = Color.RED;
    private static final Color WORMHOLE_COLOR = Color.YELLOW;
    public static final Color UNVISITED_PLNT_COLOR = Color.GREEN;
    public static final Color VISITED_PLNT_COLOR = Color.DARKCYAN;
    public static final Color CURR_PLNT_COLOR = Color.BLUE;

    private MapDetailController infoControl;
    private HiddenSidesPane mapSidesPane;
    private MapPane planetMap;
    private Pane planetInfo;

    protected Planet currentPlanet;
    private Player player;
    private int fuelAmount;
    private int maxFuelAmount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        planetMap = new MapPane();
        mapSidesPane = new HiddenSidesPane();

        mapScreen.setMasterNode(mapSidesPane);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/spacetrader/MapDetailPane.fxml"));
        try {
            planetInfo = loader.load();
        } catch (IOException e) {
            Logger.getLogger(SpaceTrader.class.getName()).log(Level.SEVERE, null, e);
        }
        infoControl = loader.getController();
        infoControl.setMapControl(this);

        mapScreen.setDetailNode(planetInfo);
        
        helpfulPanelInfoText.setVisible(false);
        
        mapSidesPane.setContent(planetMap);
        mapSidesPane.setBottom(new PlanetColorKey());
        mapSidesPane.setAnimationDelay(Duration.ZERO);
        mapSidesPane.setTriggerDistance(-1);
    }

    /**
     * Adds all the planets to the map.
     *
     * @param player the player of the game
     * @param currentPlanet the planet the player currently is
     * @param planets the planets this map will display.
     */
    public void setUpMap(Player player, Planet currentPlanet, ArrayList<Planet> planets) {
        this.player = player;
        this.currentPlanet = currentPlanet;
        this.fuelAmount = player.getShip().getFuelAmount();
        this.maxFuelAmount = player.getShip().getMaxFuel();
        fuelRemaingText.setText("Fuel Remaining : " + fuelAmount + "/" + maxFuelAmount);
        planetMap.addPlanets(planets);

        mapScreen.setDividerPosition((planetInfo.getPrefWidth()) / mapScreen.getPrefWidth());

        centerOnPlanet(currentPlanet);
    }

    /**
     * Determines if a specified planet is in range of the current planet given
     * the player's current fuel amount and spaceship type.
     *
     * @param planet the planet we are trying to travel to
     * @return true if the planet is in range
     */
    public boolean isInRangeOf(Planet planet) {
        return Universe.distanceBetweenPlanets(currentPlanet, planet) < fuelAmount;
    }

    /**
     * Goes to the warp screen traveling to the selected planet.
     */
    public void travelToPlanet() {
        player.departFromPlanet(this.currentPlanet, planetMap.selectedPlanet, mainControl);
    }

    /**
     * !!! When the pane is revealed the right part of the map is unviewable!!!
     * need to fix Reveals the detail pane with the specified planet's
     * information.
     *
     * @param planet selected planet
     */
    private void showPlanetInfo(Planet planet) {
        double localX = planetMap.planetIcons.get(planet).getCenterX();
        Point2D sceneLoc = planetMap.planetIcons.get(planet).localToScene(localX, 0);
        
        //if the planet would be covered by the info pane, put it on the right
        if (sceneLoc.getX() < (planetInfo.getPrefWidth())) {
            mapSidesPane.setLeft(null);
            mapSidesPane.setRight(planetInfo);
            mapSidesPane.setPinnedSide(Side.RIGHT);
        } else {
            mapSidesPane.setRight(null);
            mapSidesPane.setLeft(planetInfo);
            mapSidesPane.setPinnedSide(Side.LEFT);
        }
        //mapScreen.setShowDetailNode(true);
        infoControl.setPlanetInfo(planet, player);
        helpfulPanelInfoText.setVisible(true);
    }

    /**
     * Hides the detail pane.
     */
    private void hidePlanetInfo() {
        mapSidesPane.setPinnedSide(null);
        //mapScreen.setShowDetailNode(false);
        helpfulPanelInfoText.setVisible(false);
    }

    @FXML
    protected void backToPlanet(ActionEvent event) {
        mainControl.goToHomeScreen(currentPlanet);
    }
    
    @FXML
    protected void viewColorKey(ActionEvent event) {
        PlanetColorKey colorKeyPane = new PlanetColorKey();
        PopOver popup = new PopOver(colorKeyPane);
        popup.setCornerRadius(20);
        popup.detach();
        popup.setDetachedTitle("Map Key");
        double x = (mapScreen.getWidth() / 2) + (colorKeyPane.getPrefWidth() / 2);
        double y = -300;
        popup.show(getScene().getRoot(), x);
        //colorKey.set
    }

    /**
     * Centers the map on a given planet.
     *
     * @param planet the planet to center the map on
     */
    private void centerOnPlanet(Planet planet) {
        double planetX = planetMap.planetIcons.get(planet).getCenterX();
        double planetY = planetMap.planetIcons.get(planet).getCenterY();

        double translateX = -1 * planetX + (mapScreen.getPrefWidth() / 2);
        double translateY = -1 * planetY + (mapScreen.getPrefHeight() / 2);

        if (translateX > 0) {
            translateX = 0;
        } else if (translateX < (mapScreen.getPrefWidth() - planetMap.background.getWidth())) {
            translateX = mapScreen.getPrefWidth() - planetMap.background.getWidth();
        }
        if (translateY > 0) {
            translateY = 0;
        } else if (translateY < (mapScreen.getHeight() - planetMap.background.getHeight())) {
            translateY = mapScreen.getHeight() - planetMap.background.getHeight();
        }
        planetMap.setTranslateX(translateX);
        planetMap.setTranslateY(translateY);
        planetMap.dragContext.x = translateX;
        planetMap.dragContext.y = translateY;
    }

    @Override
    public String toString() {
        String toString = String.format("Size:(%f, %f)", mapScreen.getWidth(), mapScreen.getHeight());
        return toString;
    }

    /**
     * Represents the map of all the planets. Scales the Universe to the size of
     * the map display. The map is draggable within the master pane.
     */
    private class MapPane extends Pane {

        public static final double MAP_WIDTH = 2700;
        public static final double MAP_HEIGHT = 1980;
        public static final double LEFT_MARGIN = 40, RIGHT_MARGIN = 90;
        public static final double TOP_MARGIN = 40, BOTTOM_MARGIN = 40;
        
        public static final double PLANET_RADIUS = 10;
        public static final double WORMHOLE_RADIUS = 5;

        private final MapDragContext dragContext = new MapDragContext();

        private Rectangle background;
        private Planet selectedPlanet;
        private Map<Planet, Circle> planetIcons;
        private PopOver flightRadiusPopUp;

        /**
         * Constructor.
         */
        public MapPane() {
            this.setCursor(Cursor.OPEN_HAND);
            this.setOnMousePressed((event) -> {
                this.setCursor(Cursor.CLOSED_HAND);
                //remember initial mouse cursor coordinates and node position
                dragContext.mouseX = event.getSceneX();
                dragContext.mouseY = event.getSceneY();
                dragContext.x = this.getTranslateX();
                dragContext.y = this.getTranslateY();
                
                flightRadiusPopUp.hide(new Duration(200));
            });
            this.setOnMouseReleased((e) -> this.setCursor(Cursor.OPEN_HAND));
            this.setOnMouseExited((e) -> this.setCursor(Cursor.OPEN_HAND));
            
            
            this.setOnMouseDragged((event) -> {
                //Get the exact translated X and doubleY coordinate
                double tempX = dragContext.x + event.getSceneX() - dragContext.mouseX;
                double tempY = dragContext.y + event.getSceneY() - dragContext.mouseY;

                //Set the positon of mapSpace Pane after
                if (tempX <= 0 && tempX >= (mapScreen.getPrefWidth() - background.getWidth())) {
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

            this.setOnMouseClicked((event) -> {
                if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                    hidePlanetInfo();
                    if (selectedPlanet != null) {
                        planetIcons.get(selectedPlanet).setFill(
                                getUnselectedPlanetColor(selectedPlanet));
                        selectedPlanet = null;
                    }
                }
            });
        }

        /**
         * Adds planets into the MapPane
         *
         * @param planets the list of planets to be added
         */
        private void addPlanets(ArrayList<Planet> planets) {
            // *** Add Black Background ***
            background = new Rectangle(MAP_WIDTH + LEFT_MARGIN + RIGHT_MARGIN,
                    MAP_HEIGHT + TOP_MARGIN + BOTTOM_MARGIN,
                    Color.BLACK);
            this.getChildren().add(background);
            
            // *** Add Stars ***
            final int STAR_IMAGE_WIDTH = (int) (new ImageView("/resources/images/starfield.png")).getImage().getWidth();
            final int EXCESS_SPACE = ((int) (MAP_WIDTH + LEFT_MARGIN + RIGHT_MARGIN)) % STAR_IMAGE_WIDTH;
            
            FlowPane starsPane = new FlowPane();
            starsPane.setPrefWidth(MAP_WIDTH + LEFT_MARGIN + RIGHT_MARGIN + EXCESS_SPACE);
            starsPane.setPrefHeight(MAP_HEIGHT + TOP_MARGIN + BOTTOM_MARGIN);
            for (int i = 0; i < 16; i++) {
                starsPane.getChildren().add(new ImageView("/resources/images/starfield.png"));
            }
            this.getChildren().add(starsPane);
            
            //This is used for adding the flight radius in the correct position
            //It needs to be above the background and underneath all the planets
            int firstPlanetIndex = this.getChildren().size();

            // *** Add Planets ***
            this.planetIcons = new HashMap<>();
            for (Planet planet : planets) {
                //assign each planet a location that is scaled to the size of the map
                double planetX = (MAP_WIDTH * (planet.getLocation().getX() / Universe.WIDTH)) + LEFT_MARGIN;
                double planetY = (MAP_HEIGHT * (planet.getLocation().getY() / Universe.HEIGHT)) + TOP_MARGIN;

                //create an icon for each planet
                Circle planetIcon = new Circle(planetX, planetY,
                        PLANET_RADIUS, getUnselectedPlanetColor(planet));
                this.planetIcons.put(planet, planetIcon);
                
                planetIcon.setOnMousePressed((event) -> {
                    flightRadiusPopUp.hide(new Duration(200));
                    this.setCursor(Cursor.HAND);
                    if (selectedPlanet != null) {
                        //reset the color of the previously selected planet
                        Color regColor = getUnselectedPlanetColor(selectedPlanet);
                        planetIcons.get(selectedPlanet).setFill(regColor);
                    }
                    if (planet != currentPlanet && planet != selectedPlanet) {
                        showPlanetInfo(planet);
                        planetIcon.setFill(SELECTED_PLNT_COLOR);
                        selectedPlanet = planet;
                    } else {
                        hidePlanetInfo();
                        selectedPlanet = null;
                    }
                    event.consume();
                });
                
                
                //This prevents the map from dragging when you move the mouse
                //after pressing on a planet
                planetIcon.setOnMouseDragged((event) -> event.consume());

                //So that after you click on a planet, the cursor is still a pointing hand
                planetIcon.setOnMouseReleased((event) -> {
                    this.setCursor(Cursor.HAND);
                    event.consume();
                });

                planetIcon.setOnMouseEntered((e) -> this.setCursor(Cursor.HAND));
                planetIcon.setOnMouseExited((e) -> this.setCursor(Cursor.OPEN_HAND));

                // *** Add Wormholes ***
                if (planet.getWormhole() != null) {
                    Circle wormholeIcon = new Circle(planetX + PLANET_RADIUS + 10, planetY + PLANET_RADIUS + 2,
                            WORMHOLE_RADIUS, getUnselectedPlanetColor(planet));
                    wormholeIcon.setFill(WORMHOLE_COLOR);
                    this.getChildren().add(wormholeIcon);
                    
                    Pane miniMapPane = null;
                    FXMLLoader loader = new FXMLLoader(getClass()
                            .getResource("/spacetrader/planets/WormholeMap.fxml"));
                    try {
                        miniMapPane = loader.load();
                    } catch (IOException e) {
                        Logger.getLogger(SpaceTrader.class.getName()).log(Level.SEVERE, null, e);
                    }
                    ((WormholeMiniMapController) loader.getController()).setWormholeMap(planets, planet);
                    
                    PopOver wormholePopOver = new PopOver(miniMapPane);
                    planetIcon.addEventHandler(MouseEvent.MOUSE_ENTERED, (event) -> {
                        wormholePopOver.show(wormholeIcon);
                    });

                    planetIcon.addEventHandler(MouseEvent.MOUSE_EXITED, (event) -> {
                        wormholePopOver.hide(new Duration(150));
                    });
                    
                    wormholeIcon.setOnMouseEntered((e) -> this.setCursor(Cursor.HAND));
                    wormholeIcon.setOnMouseExited((e) -> this.setCursor(Cursor.OPEN_HAND));
                }

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
                    planetIcon.setFill(CURR_PLNT_COLOR);
                    Circle flightRadius = new Circle(planetIcon.getCenterX(), planetIcon.getCenterY(), maxTravelDistance, Color.TRANSPARENT);
                    flightRadius.setOpacity(0.6);
                    flightRadius.setStroke(Color.LAWNGREEN);
                    flightRadiusPopUp = InformationPresenter.getInstance()
                            .createTextPopOver("This is your current max range of travel.");
                    flightRadius.setOnMouseEntered((e) -> {
                        if ((Boolean) flightRadiusPopUp.getContentNode().getUserData() == false) {
                            flightRadiusPopUp.show(flightRadius);
                            flightRadiusPopUp.getContentNode().setUserData(Boolean.TRUE);
                        }
                    });
                    //add right above the background, so that the circle is beneath all the planets, but is still visible
                    this.getChildren().add(firstPlanetIndex, flightRadius);
                }
            }
        }
        
        /**
         * Determines the color of a planet that is not selected.
         *
         * @param p the planet
         * @return the color of that planet
         */
        private Color getUnselectedPlanetColor(Planet p) {
            Color color = p.isVisited() ? VISITED_PLNT_COLOR : UNVISITED_PLNT_COLOR;
            if (!SpaceMapScreenController.this.isInRangeOf(p)) {
                color = color.darker();
            }
            return color;
        }

        @Override
        public String toString() {
            String toString = String.format("Size:(%f, %f), RectSize:(%f, %f)",
                    getWidth(), getHeight(), 
                    background.getWidth(),
                    background.getHeight());
            toString += String.format(" TranslateX,Y:(%f, %f), LayoutX,Y:(%f, %f)",
                    getTranslateX(),
                    getTranslateY(),
                    getLayoutX(), getLayoutY());
            toString += String.format("%nDragContext x,y:(%f, %f), MouseX,Y:(%f, %f)",
                    dragContext.x, dragContext.y,
                    dragContext.mouseX, dragContext.mouseY);
            return toString;
        }

        /**
         * A class for holding the values used for dragging.
         */
        private final class MapDragContext {

            public double mouseX;
            public double mouseY;
            public double x;
            public double y;
        }
    }
    
    
    /**
     * A GridPane that displays the Map Key.
     */
    private class PlanetColorKey extends GridPane {
        private PlanetColorKey() {
            this.setPrefWidth(400);
            this.setPadding(new Insets(15, 15, 15, 15));
            this.setHgap(15);
            this.setVgap(10);
            Circle[] icons = new Circle[]{
                new Circle(MapPane.PLANET_RADIUS, CURR_PLNT_COLOR),
                new Circle(MapPane.PLANET_RADIUS, SELECTED_PLNT_COLOR),
                new Circle(MapPane.PLANET_RADIUS, UNVISITED_PLNT_COLOR),
                new Circle(MapPane.PLANET_RADIUS, UNVISITED_PLNT_COLOR.darker()),
                new Circle(MapPane.PLANET_RADIUS, VISITED_PLNT_COLOR),
                new Circle(MapPane.PLANET_RADIUS, VISITED_PLNT_COLOR.darker()),
                new Circle(MapPane.WORMHOLE_RADIUS, WORMHOLE_COLOR)
            };
            String[] descriptions = new String[]{
                "Your current location",
                "The selected planet",
                "Unvisited & In Range",
                "Unvisited & Out of Range", 
                "Visited & In Range",
                "Visited & Out of Range",
                "A wormhole (If you're on its local planet, you can use it to travel to another wormhole)"
            };
            for (int i = 0; i < icons.length; i++) {
                Label desc = new Label(descriptions[i]);
                desc.setWrapText(true);
                this.addRow(i, icons[i], desc);
            }
        }
    }
}
