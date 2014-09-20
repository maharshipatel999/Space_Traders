package spacetrader;

import java.util.*;

public class PirateShip {
	int reputation, attack, health;
	ArrayList<String> pirateCargo;
	String shipType;
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
	
	public boolean fightOrRun(PirateShip s){
		if(s.reputation > reputation) {
			return true;
		}
		return false;
	}
	
}