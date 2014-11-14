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
import static spacetrader.Tools.rand;
import spacetrader.planets.Planet;
import spacetrader.ships.PlayerShip;
import spacetrader.ships.Shield;
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

        checkForEncounter();
    }

    /**
     * Checks if there is an encounter. If there is, that encounter is executed.
     * If there is not, the player arrives at the destination planet.
     */
    private void checkForEncounter() {
        if (encounters.getEncountersRemaining() > 0) {
            pauseScreen();
            Encounter encounter = encounters.getNextEncounter();

            if (encounter.getState() == Encounter.State.IGNORE) {
                String alertTitle = "Uneventful " + encounter.getName() + " Encounter!";
                String message = encounter.getIgnoreMessage(destination.getName());
                mainControl.displayAlertMessage(alertTitle, null, message);
                continueTraveling();
            } else if (encounter.getState() == Encounter.State.INSPECTION) {
                mainControl.goToEncounterScreen(encounter);
            } else {
                mainControl.displayAlertMessage("Encounter!", null, encounter + "\n");
                continueTraveling();
            }
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
        //repairShip(player);
        checkForEncounter();
    }

    /**
     * Repairs the player's ship a certain amount based on the player's engineer
     * skill level.
     *
     * @param player the game's player
     */
    private void repairShip(Player player) {
        PlayerShip ship = player.getShip();

        // Engineer may do some repairs
        int repairs = rand.nextInt(player.getEffectiveSkill(Skill.ENGINEER)) / 2;
        ship.setHullStrength(ship.getHullStrength() + repairs);
        if (ship.getHullStrength() > ship.getMaxHullStrength()) {
            repairs = ship.getHullStrength() - ship.getMaxHullStrength();
            ship.setHullStrength(ship.getMaxHullStrength());
        } else {
            repairs = 0;
        }
        // Shields are easier to repair
        repairs = 2 * repairs;
        for (Shield shield : ship.getShields()) {
            shield.setHealth(shield.getHealth() + repairs);
            if (shield.getHealth() > shield.getType().power()) {
                repairs = shield.getHealth() - shield.getType().power();
                shield.setHealth(shield.getType().power());
            } else {
                repairs = 0;
            }
        }
    }

    /**
     * Shows setUpWarping animation screen for a short period of time.
     */
    private void animateShip() {
        /*TranslateTransition tt = new TranslateTransition(Duration.millis(30000), shipSprite);
         final float TRANSLATE_FACTOR = 300f;
         tt.setByX(5.005f * TRANSLATE_FACTOR);
         tt.setByY(-1f * TRANSLATE_FACTOR);
         tt.play();*/
    }
}
