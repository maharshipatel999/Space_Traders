package spacetrader.ships;

//type, cargoBay, weaponSlots, shieldSlots, gadgetSlots, crew;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import spacetrader.planets.TechLevel;

//fuel, fuelCost, hullStrength, size;
//minTechLevel, price, repairCost;
//bounty, occurrence,   police, pirate, trader;
/**
 * Holds all the stats and information for each ship.
 *
 * @author Caleb Stokols
 */
public enum ShipType {

    // IMPORTANT: All of this information is currently current.
    // Do not edit this class without first consulting with the group.
    FLEA("Flea", 10, 0, 0, 0, 1,
            20, 1, 25, 0,
            TechLevel.EARLY_INDUSTRIAL, 2000, 1,
            5, 2, -1, -1, 0),
    GNAT("Gnat", 15, 1, 0, 1, 1,
            14, 2, 100, 1,
            TechLevel.INDUSTRIAL, 10000, 1,
            50, 28, 0, 0, 0),
    FIREFLY("Firefly", 20, 1, 1, 1, 1,
            17, 3, 100, 1,
            TechLevel.INDUSTRIAL, 25000, 1,
            75, 20, 0, 0, 0),
    MOSQUITO("Mosquito", 15, 2, 1, 1, 1,
            13, 5, 100, 1,
            TechLevel.INDUSTRIAL, 30000, 1,
            100, 20, 0, 1, 0),
    BUMBLEBEE("Bumblebee", 25, 1, 2, 2, 2,
            15, 7, 100, 2,
            TechLevel.INDUSTRIAL, 60000, 1,
            125, 15, 1, 1, 0),
    BEETLE("Beetle", 50, 0, 1, 1, 3,
            14, 10, 50, 2,
            TechLevel.INDUSTRIAL, 80000, 1,
            50, 3, -1, -1, 0),
    HORNET("Hornet", 20, 3, 2, 1, 2,
            16, 15, 150, 3,
            TechLevel.POST_INDUSTRIAL, 100000, 2,
            200, 6, 2, 3, 1),
    GRASSHOPPER("Grasshopper", 30, 2, 2, 3, 3,
            15, 15, 150, 3,
            TechLevel.POST_INDUSTRIAL, 150000, 3,
            300, 2, 3, 4, 2),
    TERMITE("Termite", 60, 1, 3, 2, 3,
            13, 20, 200, 4,
            TechLevel.HI_TECH, 225000, 4,
            300, 2, 4, 5, 3),
    WASP("Wasp", 35, 3, 2, 2, 3,
            14, 20, 200, 4,
            TechLevel.HI_TECH, 300000, 5,
            500, 2, 5, 6, 4);

    private final String type;
    private final int cargoBay, weaponSlots, shieldSlots, gadgetSlots, crew;
    private final int fuel, fuelCost, hullStrength, size; //size: determines how easy it is to hit this ship
    private final TechLevel minTechLevel;
    private final int price, repairCost; //repair-cost: cost of repairing 1 point of hull strength
    private final int bounty, occurrence; //occurence: percentage of the ships you meet
    private final int police, pirate, trader; //min strength required for a police/pirate/trader to have this ship
    public static final List<ShipType> VALUES
            = Collections.unmodifiableList(Arrays.asList(values()));
    public static final int SIZE = VALUES.size();

    ShipType(String type,
            int cargoBay,
            int weaponSlots,
            int shieldSlots,
            int gadgetSlots,
            int crew,
            int fuel,
            int fuelCost,
            int hullStrength,
            int size,
            TechLevel minTechLevel,
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

    @Override
    public String toString() {
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

    public int crew() {
        return crew;
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

    public int size() {
        return size;
    }

    public TechLevel minTechLevel() {
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

    public String spriteFile() {
        String fileName = Character.toLowerCase(type.charAt(0)) + type.substring(1);
        fileName = "/resources/images/ship_sprites/" + fileName + ".png";
        return fileName;
    }
}
