package spacetrader;

public class Firefly extends ShipType{

	public Firefly() {
		shipType = "FIREFLY";
		numGadgetSlots = 1;
		numWeaponSlots = 1;
		numShieldSlots = 1;
		parsecsPerTank = 17;
		hullStrength = 2;
		price = 800;
	}

}