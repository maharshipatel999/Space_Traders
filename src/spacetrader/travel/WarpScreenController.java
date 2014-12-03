/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import spacetrader.Player;
import spacetrader.SkillList.Skill;
import spacetrader.planets.Planet;
import spacetrader.system.SceneController;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class WarpScreenController extends SceneController implements Initializable {

    @FXML
    private Text destinationText;
    @FXML
    private ImageView shipSprite;

    @FXML
    private AnchorPane warpPane;

    public int travelRemaining;
    public EncounterManager encounters;

    private Planet source;
    private Planet destination;
    private Player player;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    /**
     * Travels from one planet to another.
     *
     * @param source the planet we are at
     * @param destination the planet we are going to
     * @param player the player
     */
    public void setUpWarping(Planet source, Planet destination, Player player) {
        this.source = source;
        this.destination = destination;
        this.player = player;

        encounters = new EncounterManager(source, destination, player.getShip(), player);
        animateShip();
        checkForEncounter();
    }

    /**
     * Checks if there is an encounter. If there is, that encounter is executed.
     * If there is not, the player arrives at the destination planet.
     */
    private void checkForEncounter() {
        if (encounters.getEncountersRemaining() > 0) {
            //pauseScreen();
            Encounter encounter = encounters.getNextEncounter();

            /*if (encounter.getState() == Encounter.State.IGNORE) {
                String alertTitle = "Uneventful " + encounter.getName() + " Encounter!";
                String message = encounter.getIgnoreMessage();
                String result = mainControl.displayCustomConfirmation(alertTitle, null, message, "Ignore", "Attack");
                if (result.equals("Attack")) {
                    mainControl.goToBattleScreen(encounter);
                } else if (result.equals("Ignore")) {
                    continueTraveling();
                }
            } else {*/
                mainControl.goToEncounterScreen(encounter);
            //}
        } else {
            pauseScreen();
            mainControl.arriveAtPlanet(source, destination);
        }
    }

    /**
     * Pauses the screen for a small amount of time.
     */
    private void pauseScreen() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(WarpScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Continues setUpWarping sequence for Player where he or she left off.
     */
    public void continueTraveling() {
        animateShip();
        player.getShip().autoRepairHullAndShields(player.getEffectiveSkill(Skill.ENGINEER));
        checkForEncounter();
    }

    public Planet getSource() {
        return source;
    }

    public Planet getDestination() {
        return destination;
    }
    
    public void setRaided() {
        encounters.setRaided();
    }
    

    /**
     * Shows setUpWarping animation screen for a short period of time.
     */
    private void animateShip() {
        //TranslateTransition tt = new TranslateTransition(Duration.millis(30000), shipSprite);
         /*final float TRANSLATE_FACTOR = 300f;
         tt.setByX(5.005f * TRANSLATE_FACTOR);
         tt.setByY(-1f * TRANSLATE_FACTOR);
         tt.play();*/
        /*tt.setByX(100);
        tt.play();
        /*
        for (int i = 0; i < 100; i++) {
            double nextX = shipSprite.getTranslateX() + 1;
            shipSprite.setTranslateX(nextX);
            */try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(WarpScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        //}
    }
}
