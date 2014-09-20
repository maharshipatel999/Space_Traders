package spacetrader;

public abstract class ShipType{
	int numGadgetSlots, numWeaponSlots, numShieldSlots, parsecsPerTank, hullStrength, price;
	String shipType;

	public int getNumGadgetSlots(){
		return numGadgetSlots;
	}
	
	public int getNumWeaponSlots() {
		return numWeaponSlots;
	}
	
	public int getNumShieldSlots() {
		return numShieldSlots;
	}
	
	public int getParsecsPerTank() {
		return parsecsPerTank;
	}
	
	public int getHullStrength() {
		return hullStrength;
	}
	
	public int getPrice() {
		return price;
	}

}