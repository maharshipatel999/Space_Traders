/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import spacetrader.Player;
import spacetrader.ships.SpaceShip;
import spacetrader.system.SceneController;

/**
 *
 * @author Caleb
 */
public class BattleController extends SceneController {

    @FXML
    private ImageView playerShipSprite, opponentShipSprite;
    @FXML
    private TextFlow battleText;
    @FXML
    private Label playerHullLabel, opponentHullLabel;
    @FXML
    private ProgressBar playerHullBar, opponentHullBar;
    @FXML
    private HBox buttonBar;
    @FXML
    private Button fleeButton, attackButton, surrenderButton;
    @FXML
    private Text mainText;
    @FXML
    private Label opponentActionText;

    private Encounter encounter;
    private Player player;
    private SpaceShip opponent;
    
    private boolean opponentGotHit;
    private boolean playerGotHit;

    public void setUpBattle(Encounter encounter) {
        this.encounter = encounter;
        this.player = encounter.getPlayer();
        this.opponent = encounter.getOpponent();
        this.playerShipSprite.setImage(new Image(encounter.getPlayer().getShip().getType().spriteFile()));
        this.opponentShipSprite.setImage(new Image(encounter.getOpponent().getType().spriteFile()));
        
        mainText.setText(encounter.getEncounterMessage());
        /*battleText.getChildren().add(new Text(
                String.format("You've engaged fire with a %s %s.", encounter.getName(), encounter.getOpponent().getType().toString())));*/

        updatePlayerHull();
        updateOpponentHull();
        updateEnemyAction();
    }

    public void updateOpponentHull() {
        int opponentHealth = opponent.getHullStrength() + opponent.getShieldHealth();
        int opponentMaxHealth = opponent.getMaxHullStrength() + opponent.getMaxShieldHealth();
        
        double opponentHull = (double) opponentHealth / opponentMaxHealth;
        opponentHullBar.setProgress(opponentHull);
        opponentHullLabel.setText((int) (100 * opponentHull) + "%");
    }
    
    public void updatePlayerHull() {
        int playerHealth = player.getShip().getHullStrength() + player.getShip().getShieldHealth();
        int playerMaxHealth = player.getShip().getMaxHullStrength() + player.getShip().getMaxShieldHealth();
        
        double playerHull = (double) playerHealth / playerMaxHealth;
        playerHullBar.setProgress(playerHull);
        playerHullLabel.setText((int) (100 * playerHull) + "%");
    }
    
    public void updateEnemyAction() {
        String nextActionText = encounter.state.getNextActionText();
        opponentActionText.setText(nextActionText);
    }
    
    public void resetBattleText() {
        battleText.getChildren().clear();
    }

    public void setOpponentGotHit(boolean opponentGotHit) {
        this.opponentGotHit = opponentGotHit;
    }

    public void setPlayerGotHit(boolean playerGotHit) {
        this.playerGotHit = playerGotHit;
    }

    public boolean isOpponentGotHit() {
        return opponentGotHit;
    }

    public boolean isPlayerGotHit() {
        return playerGotHit;
    }
    
    public void displayActionText(String text) {
        battleText.getChildren().add(new Text(String.format(text, encounter.getName())));
    }
}
