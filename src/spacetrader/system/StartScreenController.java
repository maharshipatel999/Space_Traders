/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.system;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import spacetrader.Player;
import spacetrader.commerce.TradeGood;
import spacetrader.system.SceneController;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class StartScreenController extends SceneController implements Initializable {

    @FXML private GridPane inventory;
    
    private Stage startStage;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    /**
     * Display all the player's current information onto the Start Screen.
     * @param player 
     */
    public void setUpPlayerStats(Player player, Stage myStage) {
        this.startStage = myStage;
        
        List<TradeGood> tradeGoodList = player.getShip().getCargo().getTradeGoods();
        for (int i = 0; i < tradeGoodList.size(); i++) {
            TradeGood good = tradeGoodList.get(i);
            int quantity = player.getShip().getCargo().getQuantity(good);
            if (quantity > 0) {
                inventory.addRow(1 + i, new Label(tradeGoodList.get(i).type()), new Label("" + quantity));
            }
        }
    }
    
    @FXML protected void closeWindow(ActionEvent event) {
        startStage.close();
    }
    
}
