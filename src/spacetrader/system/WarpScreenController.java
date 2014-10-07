/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.system;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import spacetrader.Planet;
import spacetrader.Universe;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class WarpScreenController implements Initializable {

    @FXML private Text destinationText;
    @FXML private ImageView shipSprite;
    
    private MainController mainControl;
    private Planet dest;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }
    
    /**
     * Gives this controller a reference to the MainController.
     * @param mainControl the Main Controller of SpaceTrader
     */
    public void setMainControl(MainController mainControl) {
        this.mainControl = mainControl;
    }

    public void travel(Planet source, Planet destination) {
        this.dest = destination;
        animateShip();
        mainControl.takeTurn(dest, (int) Universe.distanceBetweenPlanets(source, destination));
    }
    
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
