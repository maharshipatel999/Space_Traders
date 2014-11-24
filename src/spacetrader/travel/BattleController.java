/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.travel;

import spacetrader.Player;
import spacetrader.PoliceRecord;
import static spacetrader.Tools.rand;
import spacetrader.commerce.Cargo;
import spacetrader.commerce.TradeGood;
import spacetrader.system.SceneController;

/**
 *
 * @author Caleb
 */
public class BattleController extends SceneController {
    
    private static final String NO_SURRENDER_MSG = "If you are too big a "
            + "criminal, surrender is NOT an option anymore.";
    
    private static final String SURRENDER_MSG = "Your fine and time in prison "
            + "will depend on how big a criminal you are. Your fine will be taken "
            + "from your cash. If you don't have enough cash, the police will sell "
            + "your ship to get it. If you have debts, the police will pay them from "
            + "your credits (if you have enough) before you go to prison, because "
            + "otherwise the interest would be staggering.";
    
    private static final String PIRATE_BLACKMAIL_MSG = "The pirates are very "
            + "angry that they find no cargo on your ship. To stop them from "
            + "destroying you, you have no choice but to pay them an amount equal "
            + "to 5% of your current worth.";
    
    
    
    private Encounter encounter;
    private Player player;
    
    public void attemptToSurrender() {
        if (encounter instanceof PoliceEncounter) {
            if (player.getPoliceRecord().compareTo(PoliceRecord.PSYCHO) <= 0) {
                mainControl.displayAlertMessage("You May Not Surrender!", NO_SURRENDER_MSG);
            } else {
                boolean response = mainControl.displayYesNoConfirmation("Surrender?", "Do you really want to surrender?", SURRENDER_MSG);
                if (response) {
                    mainControl.playerGetsArrested();
                }
            }
        } else {  
            piratesPlunder();
        }
    }
    
    public void plunder() {
        encounter.updateRecordAfterPlunder();
        //TODO do plundering form
    }
    
    private void piratesPlunder() {
        int totalCargo = player.getCargo().getCount();
        if (totalCargo <= 0) {
            int blackmail = Math.min(25000, Math.max( 500, player.getCurrentWorth() / 20 ));
            mainControl.displayAlertMessage("Pirates found no cargo!", PIRATE_BLACKMAIL_MSG);
            player.removeCreditsForced(blackmail);
        } else {
            mainControl.displayAlertMessage("You've been plundered!", "The pirates steal from you what they can carry, but at least you get out of it alive.");								
            Cargo otherCargo = encounter.getOpponent().getCargo();
            // Pirates steal everything					
            if (otherCargo.getRemainingCapacity() >= totalCargo) {
                player.getCargo().clearAllItems();
            } else {		
                // Pirates steal a lot
                while (otherCargo.getRemainingCapacity() > 0) {
                    int i = rand.nextInt(player.getCargo().getTradeGoods().size());
                    TradeGood good = player.getCargo().getTradeGoods().get(i);
                    player.getCargo().removeItem(good, 1);
                    otherCargo.addItem(good, 1, 0);
                }
            }
        }
    }
}
