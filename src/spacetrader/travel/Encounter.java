/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import spacetrader.Player;
import spacetrader.SkillList.Skill;
import spacetrader.Tools;
import spacetrader.ships.Gadget;
import spacetrader.ships.GadgetType;
import spacetrader.ships.OpponentShip;
import spacetrader.ships.ShipType;
import spacetrader.ships.SpaceShip;

/**
 * Represents an abstract Encounter.
 *
 * @author Caleb
 */
public abstract class Encounter {

    public static enum State {

        IGNORE, FLEE, ATTACK, INSPECTION, BUY, SELL;
    }

    private final Player player;
    private final String encounterScene;
    private final String encounterName;
    private SpaceShip opponent;
    protected State state;
    protected int clicksFromDest;

    /**
     * Creates a new Encounter. Encounters have a player, and an fxmlscene.
     *
     * @param player the player of the game
     * @param fxmlScene the scene which should be displayed with this encounter.
     * @param clicksFromDest distance from destination
     * @param encounterName the name of the opponent you're encountering
     */
    public Encounter(Player player, String fxmlScene, int clicksFromDest, String encounterName) {
        this.player = player;
        this.encounterScene = fxmlScene;
        this.clicksFromDest = clicksFromDest;
        this.encounterName = encounterName;
        this.state = State.IGNORE; //defaults to ignore
    }

    /**
     * Gets the player having this encounter.
     *
     * @return the encounter's player
     */
    protected Player getPlayer() {
        return player;
    }

    /**
     * Sets the opponent space ship of this encounter
     *
     * @param opponent the space ship who the player is encountering
     */
    public void setOpponent(SpaceShip opponent) {
        this.opponent = opponent;
    }

    /**
     * Gets the opponent of this encounter
     *
     * @return the encounter's opponent
     */
    public SpaceShip getOpponent() {
        return opponent;
    }

    /**
     * Gets the fxml scene associated with this encounter. For instance, if this
     * is a PoliceEncounter, it will return the "PoliceEncounterScreen.fxml".
     *
     * @return fxml scene of this encounter's associated view
     */
    public String getFXMLScene() {
        return encounterScene;
    }

    /**
     * Gets the state of this encounter.
     *
     * @return this encounter's state
     */
    public State getState() {
        return state;
    }

    /**
     * Gets the name of this encounter.
     *
     * @return this encounter's name
     */
    public String getName() {
        return encounterName;
    }

    /**
     * Gets the distance from the destination of this encounters.
     *
     * @return how many clicks from the destination this encounter occurs
     */
    public int getClicksFromDest() {
        return clicksFromDest;
    }

    /**
     * Gets the message that should be displayed when this the opponent ignores
     * the player.
     *
     * @param destination the planet the player is traveling to
     * @return the ignore message
     */
    public String getIgnoreMessage(String destination) {
        return String.format("A %s %s is spotted %d clicks from your destination, %s!\n\nIt ignores you.",
                encounterName.toLowerCase(), opponent.getType().toString(), clicksFromDest, destination);
    }

    /**
     * Determines if this encounter allows the opponent to have the specified
     * type of space ship.
     *
     * @param type
     * @return true if its legal
     */
    protected abstract boolean isIllegalShipType(ShipType type);

    /**
     * Determines if the opponent can see the player.
     *
     * @return true if the player is cloaked
     */
    public boolean playerIsCloaked() {
        return player.getShip().getGadgets().contains(new Gadget(GadgetType.CLOAK))
                && player.getEffectiveSkill(Skill.ENGINEER) > opponent.getCrewSkill(Skill.ENGINEER);
    }

    /**
     * Determines if the player can see the opponent.
     *
     * @return true if the player is cloaked
     */
    public boolean opponentIsCloaked() {
        return opponent.getGadgets().contains(new Gadget(GadgetType.CLOAK))
                && opponent.getCrewSkill(Skill.ENGINEER) > player.getEffectiveSkill(Skill.ENGINEER);
    }

    /**
     * Creates the space ship for the opponent
     *
     * @param tries number tries to pick up a ship for opponent
     * @param lowestShipType weakest ship type
     * @param cargoModifier
     * @return SpaceShip to use for opponent
     */
    protected SpaceShip createShip(int tries, ShipType lowestShipType, double cargoModifier) {
        ShipType[] shipTypes = ShipType.values();

        int[] shipDistribution = new int[shipTypes.length];
        for (int i = 0; i < shipTypes.length; i++) {
            shipDistribution[i] = shipTypes[i].occurrence();
        }

        //Pick a ship for the opponent "tries" number of times. The strongest one will be used.
        int bestShipIndex = lowestShipType.ordinal();
        for (int i = 0; i < tries; i++) {

            //Keep picking a ship until you get one that is legal for the current situation
            int index;
            do {
                index = Tools.pickIndexFromWeightedList(shipDistribution);
            } while (this.isIllegalShipType(shipTypes[index]));

            bestShipIndex = Math.max(bestShipIndex, index); //if this chosen ship is stronger than the opponent's current ship, replace it
        }

        //If the player has more money, the opponent's difficulty increases
        int opponentDifficulty = Math.max(1, (player.getCurrentWorth() / 150000)); //should be at least 1

        //Instantiate the Opponent's Ship
        OpponentShip opponentShip = new OpponentShip(shipTypes[bestShipIndex], opponentDifficulty, cargoModifier);
        return opponentShip;
    }

    @Override
    public String toString() {
        return "State: " + state + "\n" + opponent;
    }
}
