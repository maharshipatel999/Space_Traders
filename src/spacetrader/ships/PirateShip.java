package spacetrader.ships;

import java.util.*;

//This should extends Spaceship
public class PirateShip {

    private int reputation;
    private int attack;
    private int health;
    private ArrayList<String> pirateCargo;
    private String shipType;

    /**
     * Creates an instance of a PirateShip.
     *
     * @param reputation the reputation of the pirate, which influences how
     * difficult this pirate will be
     * @param attack the attack power of this pirate
     * @param health the health of this pirate
     * @param pirateCargo the cargo this pirate holds
     */
    public PirateShip(int reputation, int attack, int health,
            ArrayList<String> pirateCargo) {
        this.reputation = reputation;
        this.attack = attack;
        this.health = health;
        this.pirateCargo = pirateCargo;

    }

    /**
     * Gets the reputation of this pirate.
     *
     * @return the reputation instance variable
     */
    public int getReputation() {
        return reputation;
    }

    /**
     * Gets the attack of this pirate.
     *
     * @return the attack instance variable
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Gets the health of this pirate.
     *
     * @return the health instance variable
     */
    public int getHealth() {
        return health;
    }

    /**
     * Gets the cargo of this pirate.
     *
     * @return an ArrayList of the Cargo this pirate holds
     */
    public ArrayList<String> getPirateCargo() {
        return pirateCargo;
    }

    /**
     * Decrease the amount of health this pirate has.
     *
     * @param hitpoints the amount of health to lose
     */
    public void loseHealth(int hitpoints) {
        health -= hitpoints;
    }

    /**
     * Determines if this pirate ship should attack or flee.
     *
     * @param ship the ship that we are facing
     * @return Fight if true, Flee if false
     */
    public boolean fightOrRun(PirateShip ship) {
        if (ship.reputation > this.reputation) {
            return true;
        }
        return false;
    }

}
