/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.travel;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import spacetrader.planets.Planet;
import spacetrader.Universe;
import spacetrader.system.SceneController;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class WarpScreenController extends SceneController implements Initializable {

    @FXML private Text destinationText;
    @FXML private ImageView shipSprite;
    
    @FXML private AnchorPane warpPane;

    private Planet dest;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    /**
     * Travels from one planet to another.
     * @param source the planet we are at
     * @param destination the planet we are going to
     */
    public void travel(Planet source, Planet destination) {
        this.dest = destination;
        animateShip();
        mainControl.takeTurn(dest, (int) Universe.distanceBetweenPlanets(source, destination));
    }
    
    /**
     * Shows travel animation screen for a short period of time.
     */
    private void animateShip() {
        /*TranslateTransition tt = new TranslateTransition(Duration.millis(30000), shipSprite);
        final float TRANSLATE_FACTOR = 300f;
        tt.setByX(5.005f * TRANSLATE_FACTOR);
        tt.setByY(-1f * TRANSLATE_FACTOR);
        tt.play();*/
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            //TODO
        }
    }
}
