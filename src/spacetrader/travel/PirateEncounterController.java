/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import static spacetrader.Tools.rand;
import spacetrader.commerce.Cargo;
import spacetrader.commerce.TradeGood;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class PirateEncounterController extends EncounterScreenController implements Initializable {

    private BattleController battleControl;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }
    
    @Override
    public void setEncounter(Encounter encounter) {
        super.setEncounter(encounter);
    }
    
    @Override
    protected void surrenderPressed() {
        Cargo playerCargo = encounter.getPlayer().getCargo();
        int playerCargoAmount = playerCargo.getCount();
        //Determine how much cargo the player has
        if (playerCargoAmount <= 0) {
            int blackmail = ((PirateEncounter) encounter).calculateBlackmail();
            encounter.getPlayer().removeCreditsForced(blackmail);
            mainControl.displayInfoMessage(null, "Pirates found no cargo!", PirateEncounter.BLACKMAIL_MSG);
        } else {
            mainControl.displayInfoMessage(null, "You've been plundered!", "The pirates steal from you what they can carry, but at least you get out of it alive.");								
            Cargo otherCargo = encounter.getOpponent().getCargo();
            // Pirates steal everything					
            if (otherCargo.getRemainingCapacity() >= playerCargoAmount) {
                playerCargo.clearAllItems();
            } else {		
                // Pirates steal a lot
                while (otherCargo.getRemainingCapacity() > 0) {
                    int i = rand.nextInt(playerCargo.getTradeGoods().size());
                    TradeGood good = playerCargo.getTradeGoods().get(i);
                    playerCargo.removeItem(good, 1);
                    otherCargo.addItem(good, 1, 0);
                }
            }
        }
        mainControl.setPlayerWasRaided();
        finishEncounter();
    }
}
