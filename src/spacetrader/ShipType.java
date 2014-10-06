package spacetrader;
  /*
    private final int fuel, fuelCost, hullStrength, crew, size;
    private final int minTechLevel, price, repairCost;
    private final int bounty, occurrence;
    private final int police, pirate, trader;
    */
public enum ShipType {
    FLEA        ("Flea", 10, 0, 0, 0,
                 20, 1, 25, 1, 0,
                 4,2000,1,
                 5,2,
                 -1,-1,0),
    GNAT        ("Gnat", 15, 1, 0, 1,
                 14, 2, 100, 1, 1,
                 5,10000,1,
                 50,28,
                 0,0,0),
    FIREFLY     ("Firefly", 20, 1, 1, 1,
                 17, 3, 100, 1, 1,
                 5,25000,1,
                 75,20,
                 0,0,0),
    MOSQUITO    ("Mosquito", 15, 2, 1, 1,
                 13, 5, 100, 1, 1,
                 5,30000,1,
                 100,20,
                 0,1,0),
    BUMBLEBEE   ("Bumblebee", 25, 1, 2, 2,
                 15, 7, 100, 2, 2,
                 5,0,1,
                 125,15,
                 0,1,0),
    BEETLE      ("Beetle", 0, 0, 0, 0,
                 20, 25, 2000, 1, 2,
                 0,0,1,
                 0,0,
                 0,0,0),
    HORNET      ("Hornet", 0, 0, 0, 0,
                 20, 25, 2000, 1, 2,
                 0,0,1,
                 0,0,
                 0,0,0),
    GRASSHOPPER ("Grasshopper", 0, 0, 0, 0,
                 20, 25, 2000, 1, 2,
                 0,0,1,
                 0,0,
                 0,0,0),
    TERMITE     ("Termite", 0, 0, 0, 0,
                 20, 25, 2000, 5, 2,
                 0,0,0,
                 0,0,
                 0,0,0),
    WASP        ("Wasp", 0, 0, 0, 0,
                 20, 25, 2000, 5, 2,
                 0,0,0,
                 0,0,
                 0,0,0);

    private final String type;
    private final int cargoBay, weaponSlots, shieldSlots, gadgetSlots;
    private final int fuel, fuelCost, hullStrength, crew, size;
    private final int minTechLevel, price, repairCost;
    private final int bounty, occurrence;
    private final int police, pirate, trader;
    

    ShipType(   String type,
                int cargoBay,
                int weaponSlots,
                int shieldSlots,
                int gadgetSlots,
                int fuel,
                int fuelCost,
                int hullStrength,
                int crew,
                int size,
                int minTechLevel,
                int price,
                int repairCost,
                int bounty,
                int occurrence,
                int police,
                int pirate,
                int trader) {
        this.type = type;
        this.cargoBay = cargoBay;
        this.weaponSlots = weaponSlots;
        this.shieldSlots = shieldSlots;
        this.gadgetSlots = gadgetSlots;
        this.fuel = fuel;
        this.fuelCost = fuelCost;
        this.hullStrength = hullStrength;
        this.crew = crew;
        this.size = size;
        this.minTechLevel = minTechLevel;
        this.price = price;
        this.repairCost = repairCost;
        this.bounty = bounty;
        this.occurrence = occurrence;
        this.police = police;
        this.pirate = pirate;
        this.trader = trader;
        
    }
    
    public String type() {
        return type;
    }

    public int cargoBay() {
        return cargoBay;
    }

    public int weaponSlots() {
        return weaponSlots;
    }

    public int shieldSlots() {
        return shieldSlots;
    }

    public int gadgetSlots() {
        return gadgetSlots;
    }

    public int fuel() {
        return fuel;
    }

    public int fuelCost() {
        return fuelCost;
    }

    public int hullStrength() {
        return hullStrength;
    }

    public int crew() {
        return crew;
    }

    public int size() {
        return size;
    }

    public int minTechLevel() {
        return minTechLevel;
    }

    public int price() {
        return price;
    }

    public int repairCost() {
        return repairCost;
    }

    public int bounty() {
        return bounty;
    }

    public int occurrence() {
        return occurrence;
    }

    public int police() {
        return police;
    }

    public int pirate() {
        return pirate;
    }

    public int trader() {
        return trader;
    }
}