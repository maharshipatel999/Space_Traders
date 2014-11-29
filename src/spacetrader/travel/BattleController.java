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
import static spacetrader.Tools.rand;
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

    private Encounter encounter;
    private Player player;
    private SpaceShip opponent;

    public void setUpBattle(Encounter encounter) {
        this.encounter = encounter;
        this.player = encounter.getPlayer();
        this.opponent = encounter.getOpponent();
        this.playerShipSprite.setImage(new Image(encounter.getPlayer().getShip().getType().spriteFile()));
        this.opponentShipSprite.setImage(new Image(encounter.getOpponent().getType().spriteFile()));
        battleText.getChildren().add(new Text(
                String.format("You've engaged fire with a %s %s.", encounter.getName(), encounter.getOpponent().getType().toString())));

        updateHullLabels();
        
        if (encounter instanceof TraderEncounter) {
            buttonBar.getChildren().remove(surrenderButton);
        }
    }

    private void updateHullLabels() {
        int playerHealth = player.getShip().getHullStrength() + player.getShip().getShieldHealth();
        int playerMaxHealth = player.getShip().getMaxHullStrength() + player.getShip().getMaxShieldHealth();
        
        int opponentHealth = opponent.getHullStrength() + opponent.getShieldHealth();
        int opponentMaxHealth = opponent.getMaxHullStrength() + opponent.getMaxShieldHealth();
        
        double playerHull = (double) playerHealth / playerMaxHealth;
        double opponentHull = (double) opponentHealth / opponentMaxHealth;
        playerHullBar.setProgress(playerHull);
        opponentHullBar.setProgress(opponentHull);
        playerHullLabel.setText((int) (100 * playerHull) + "%");
        opponentHullLabel.setText((int) (100 * opponentHull) + "%");
    }

    public void attemptToSurrender() {
        encounter.handleSurrender(mainControl);
    }

    public void plunder() {
        encounter.updateRecordAfterPlunder();
        //TODO do plundering form
    }

    @FXML
    protected void attackPressed() {
        //TODO
        if (rand.nextBoolean()) {
            opponent.takeDamage(8);
        } else {
            player.getShip().takeDamage(8);
        }
        
        if (player.getShip().getHullStrength() <= 0) {
            mainControl.displayInfoMessage(null, "Game Over", "You died!");
            mainControl.goBackToWarpScreen();
        } else if (opponent.getHullStrength() <= 0) {
            mainControl.displayInfoMessage(null, "Enemy Destroyed", "You have "
                    + "defeated your opponent. Good job!");
            mainControl.goBackToWarpScreen();
        }
        
        updateHullLabels();
    }

    @FXML
    protected void surrenderPressed() {
        //TODO
        mainControl.goBackToWarpScreen();

    }

    @FXML
    protected void fleePressed() {
        //TODO
        mainControl.goBackToWarpScreen();
    }
}
