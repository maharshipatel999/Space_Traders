/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.system;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import spacetrader.Universe;
import spacetrader.planets.Planet;

/**
 * FXML Controller class
 *
 * @author maharshipatel999
 */
public class WormholeMiniMapController implements Initializable {

    @FXML private Rectangle miniMap;
    @FXML private Pane miniMapPane;
    private static final double MINIMAP_WIDTH = 300;
    private static final double MINIMAP_HEIGHT = 220;
    private static final double MARGIN = 5;
    private static final Color PLANETCOLOR = new Color(.549, .203, .686, 1);
    private static final Color SRCCOLOR = new Color(.015, .470, 1, 1);
    private static final Color DESTCOLOR = new Color(.968, .403, .266, 1);
    private static final Color LINECOLOR = new Color(.639, .615, .662, 1);
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    /**
     * Sets the wormhole map pane for the selected planet
     * 
     * @param wormholePlanet the planet whose map we will make
     */
    public void setWormholeMap(ArrayList<Planet> planets, Planet wormholePlanet) {
        double startX = 0;
        double startY = 0;
        double endX = 0;
        double endY = 0;
        for (Planet planet : planets) {
            double planetX = planet.getLocation().getX() * 2 + MARGIN;
            double planetY = planet.getLocation().getY() * 2 + MARGIN;
            
            Circle planetCircle;
            if (planet == wormholePlanet || planet == wormholePlanet.getWormhole().getDestination()) {
                planetCircle = new Circle(planetX, planetY, 5);
                if (planet == wormholePlanet) {
                    planetCircle.setFill(SRCCOLOR);
                    startX = planetX;
                    startY = planetY;
                } else {
                    planetCircle.setFill(DESTCOLOR);
                    endX = planetX;
                    endY = planetY;
                }
            } else {
                planetCircle = new Circle(planetX, planetY, 2);
                planetCircle.setFill(PLANETCOLOR);
            }
            miniMapPane.getChildren().add(planetCircle);  
        }
        Line wormholeLine = new Line(startX, startY, endX, endY);
        wormholeLine.setStroke(LINECOLOR);
        wormholeLine.getStrokeDashArray().addAll(5d, 5d);
        miniMapPane.getChildren().add(wormholeLine);
    }
    
    
}
