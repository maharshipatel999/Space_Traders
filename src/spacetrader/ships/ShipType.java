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
    //size: determines how easy it is to hit this ship
    private final int fuel, fuelCost, hullStrength, size;
    private final TechLevel minTechLevel;
    //repair-cost: cost of repairing 1 point of hull strength
    private final int price, repairCost;
    //occurence: percentage of the ships you meet
    private final int bounty, occurrence;
    //min strength required for a police/pirate/trader to have this ship
    private final int police, pirate, trader;
    public static final List<ShipType> VALUES
            = Collections.unmodifiableList(Arrays.asList(values()));
    public static final int SIZE = VALUES.size();

    /**
     * Creates an enumeration value of ShipType.
     *
     * @param type the type of this ship
     * @param cargoBay the amount of cargo this ship can hold
     * @param weaponSlots the amount of weapon slots this ship has
     * @param shieldSlots the amount of shield slots this ship has
     * @param gadgetSlots the amount of gadget slots this ship has
     * @param crew the amount of crew members this ship has
     * @param fuel the amount of fuel this ship holds
     * @param fuelCost the cost of fuel for this ship
     * @param hullStrength the strength of this ship's hull
     * @param size the relative size of this ship
     * @param minTechLevel the min TechLevel to buy this ship
     * @param price the cost to buy this ship
     * @param repairCost the cost to repair this ship
     * @param bounty the bounty value of this ship
     * @param occurrence the probability this ship has to meet other ships
     * @param police the min strength a police needs to have this type
     * @param pirate the min strength a pirate needs to have this type
     * @param trader the min strength a trader needs to have this type
     */
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

    /**
     * Gets the String representation of this object.
     *
     * @return the String of the type of this object
     */
    @Override
    public String toString() {
        return type;
    }

    /**
     * Gets the amount of cargo this ship type can hold.
     *
     * @return the cargo of the ship
     */
    public int cargoBay() {
        return cargoBay;
    }

    /**
     * Gets the amount of weapons slots this ship had.
     *
     * @return number of weapon slots
     */
    public int weaponSlots() {
        return weaponSlots;
    }

    /**
     * Gets the amount of shield slots this ship has.
     *
     * @return number of shield slots
     */
    public int shieldSlots() {
        return shieldSlots;
    }

    /**
     * Gets the amount of gadget slots this ship has.
     *
     * @return number of gadget slots
     */
    public int gadgetSlots() {
        return gadgetSlots;
    }

    /**
     * Gets the number of crew this ship can have.
     *
     * @return number of crew
     */
    public int crew() {
        return crew;
    }

    /**
     * Gets the amount of fuel this ship can have.
     *
     * @return amount of fuel
     */
    public int fuel() {
        return fuel;
    }

    /**
     * Gets the cost of fuel for this ship.
     *
     * @return cost of fuel
     */
    public int fuelCost() {
        return fuelCost;
    }

    /**
     * Gets the max hull strength of this ship.
     *
     * @return hull strength of this ship
     */
    public int hullStrength() {
        return hullStrength;
    }

    /**
     * Gets the size of this ship.
     *
     * @return size of this ship
     */
    public int size() {
        return size;
    }

    /**
     * Gets the minimum TechLevel to buy this ship type.
     *
     * @return minTechLevel instance variable of this ship type
     */
    public TechLevel minTechLevel() {
        return minTechLevel;
    }

    /**
     * Gets the base price of this ship type.
     *
     * @return price of this ship type
     */
    public int price() {
        return price;
    }

    /**
     * Gets the cost to repair 1 unit of hull on this ship.
     *
     * @return cost of repair
     */
    public int repairCost() {
        return repairCost;
    }

    /**
     * Gets the bounty value of this ship.
     *
     * @return bounty value
     */
    public int bounty() {
        return bounty;
    }

    /**
     * Gets the probability this ship has of running into other ships.
     *
     * @return occurrence instance variable
     */
    public int occurrence() {
        return occurrence;
    }

    /**
     * Gets the min strength a police needs to have this type.
     *
     * @return min strength for a police
     */
    public int police() {
        return police;
    }

    /**
     * Gets the min strength a pirate needs to have this type.
     *
     * @return min strength for a pirate
     */
    public int pirate() {
        return pirate;
    }

    /**
     * Gets the min strength a trader needs to have this type.
     *
     * @return min strength for a trader
     */
    public int trader() {
        return trader;
    }

    /**
     * Gets the image of this ship.
     *
     * @return the String of where the image of this ship type is located
     */
    public String spriteFile() {
        String fileName = Character.toLowerCase(type.charAt(0)) +
                type.substring(1);
        fileName = "/resources/images/ship_sprites/" + fileName + ".png";
        return fileName;
    }
}
