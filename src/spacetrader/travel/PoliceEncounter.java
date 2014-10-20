/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.travel;

import spacetrader.Player;

/**
 *
 * @author Caleb
 */
public class PoliceEncounter extends Encounter {
    
    public static final int ATTACK_POLICE_SCORE = -3;
    public static final int KILL_POLICE_SCORE = -6;
    public static final int ATTACK_TRADER_SCORE = -2;
    public static final int PLUNDER_TRADER_SCORE = -2;
    public static final int KILL_TRADER_SCORE = -4;
    public static final int ATTACK_PIRATE_SCORE = 0;
    public static final int KILL_PIRATE_SCORE = 1;
    public static final int PLUNDER_PIRATE_SCORE = -1;
    public static final int TRAFFICKING = -1;
    public static final int FLEE_FROM_INSPECTION = -2;
    
    private static final int MINIMUM_FINE_AMOUNT = 100;
    private static final int MAXIMUM_FINE_AMOUNT = 10000;
    private static final int FINE_DECREASE = 4 * 10;
    private static final int FINE_ROUND = 50;
    
    public PoliceEncounter(Player player) {
        super(player, "/spacetrader/travel/PoliceEncounterScreen.fxml");
    }
    
    /**
     * Calculates the fine for carrying illegal goods
     * @param player
     * @return 
     */
    public void inspectPlayer() {
        //determine if player is carrying illegal goods
        if (getPlayer().getShip().isCarryingIllegalGoods()) {
            //calculate the player's fine
            int fine = getPlayer().getCurrentWorth() / FINE_DECREASE;
            //round the fine up to the nearest ROUND_AMOUNT
            if ((fine % FINE_ROUND) != 0) {
                fine += FINE_ROUND - (fine % FINE_ROUND);
            }
            fine = Math.min(fine, MAXIMUM_FINE_AMOUNT);
            fine = Math.max(fine, MINIMUM_FINE_AMOUNT);

            getPlayer().getWallet().removeForcefully(fine);

            //update police record
            int newRecord = getPlayer().getPoliceRecordScore() + TRAFFICKING;
            getPlayer().setPoliceRecordScore(newRecord);
        } else {
            int newRecord = getPlayer().getPoliceRecordScore() - TRAFFICKING;
            getPlayer().setPoliceRecordScore(newRecord);
        }
    }
    
    public int calculcateBribe() {
        int bribe = getPlayer().getCurrentWorth();
        bribe /= 10 + (10 * getPlayer().getLocation().getPoliticalSystem().bribeLevel());
        return bribe;
    }
}