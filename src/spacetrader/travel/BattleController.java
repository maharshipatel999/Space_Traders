/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.travel;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import spacetrader.Player;
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
    
    private Encounter encounter;
    private Player player;
    
    public void setUpBattle(Encounter encounter) {
        this.encounter = encounter;
        this.playerShipSprite.setImage(new Image(encounter.getPlayer().getShip().getType().spriteFile()));
        this.opponentShipSprite.setImage(new Image(encounter.getOpponent().getType().spriteFile()));
        battleText.getChildren().add(new Text("You've engaged fire with a " + encounter.getName() + "."));
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
        mainControl.goBackToWarpScreen();
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
