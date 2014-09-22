package spacetrader;

public enum ShipType {
    FLEA        (10, 0, 0, 0, 20, 25, 2000, 5, 2, "Flea"),
    GNAT        (15, 1, 0, 1, 14, 100, 10000, 50, 28, "Gnat"),
    FIREFLY     (20, 1, 1, 1, 17, 100, 25000, 75, 20, "Firefly"),
    MOSQUITO    (15, 2, 1, 1, 13, 100, 30000, 100, 20, "Mosquito"),
    BUMBLEBEE   (25, 1, 2, 2, 15, 100, 60000, 125, 15, "Bumblebee"),
    BEETLE      (0, 0, 0, 0, 0, 0, 0, 0, 0, "Beetle"), //TODO
    HORNET      (0, 0, 0, 0, 0, 0, 0, 0, 0, "Hornet"), //TODO
    GRASSHOPPER (0, 0, 0, 0, 0, 0, 0, 0, 0, "Grasshopper"), //TODO
    TERMITE     (0, 0, 0, 0, 0, 0, 0, 0, 0, "Termite"), //TODO
    WASP        (0, 0, 0, 0, 0, 0, 0, 0, 0, "Wasp"); //TODO

    private final int cargoBay; //how much cargo the ship can carry
    private final int weaponSlots;
    private final int shieldSlots;
    private final int gadgetSlots;
    private final int parsecsPerTank;
    private final int hullStrength;
    private final int price;
    private final int bounty; //don't know what this is
    private final int occurrence; //don't know what this is
    private final String name;

    private ShipType(int cargo, int weaponSlots, int shieldSlots, int gadgetSlots,
                      int parsecs, int hullStrength, int price, int bounty, int occurrence, String name) {
        this.cargoBay = cargo;
        this.weaponSlots = weaponSlots;
        this.shieldSlots = shieldSlots;
        this.gadgetSlots = gadgetSlots;
        this.parsecsPerTank = parsecs;
        this.hullStrength = hullStrength;
        this.price = price;
        this.bounty = bounty;
        this.occurrence = occurrence;
        this.name = name;
    }
    
    public int numGadgetSlots(){
            return gadgetSlots;
    }

    public int numWeaponSlots() {
            return weaponSlots;
    }

    public int numShieldSlots() {
            return shieldSlots;
    }

    public int parsecsPerTank() {
            return parsecsPerTank;
    }

    public int hullStrength() {
            return hullStrength;
    }

    public int getPrice() {
            return price;
    }

}