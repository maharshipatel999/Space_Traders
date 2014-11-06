package spacetrader.ships;

import java.util.*;

//This should extends Spaceship
public class PirateShip {

    private int reputation;
    private int attack;
    private int health;
    private ArrayList<String> pirateCargo;
    private String shipType;

    public PirateShip(int reputation, int attack, int health, ArrayList<String> pirateCargo) {
        this.reputation = reputation;
        this.attack = attack;
        this.health = health;
        this.pirateCargo = pirateCargo;

    }

    public int getReputation() {
        return reputation;
    }

    public int getAttack() {
        return attack;
    }

    public int getHealth() {
        return health;
    }

    public ArrayList<String> getPirateCargo() {
        return pirateCargo;
    }

    public void loseHealth(int hitpoints) {
        health -= hitpoints;
    }

    public boolean fightOrRun(PirateShip ship) {
        if (ship.reputation > this.reputation) {
            return true;
        }
        return false;
    }

}
