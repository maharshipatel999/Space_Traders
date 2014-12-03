/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import spacetrader.Player;
import spacetrader.SkillList.Skill;
import spacetrader.Tools;
import spacetrader.planets.Planet;
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

        IGNORE, FLEE, ATTACK, INSPECTION, BUY, SELL, SURRENDER;
    }

    private final Player player;
    private final String encounterScene;
    private final String encounterName;
    private SpaceShip opponent;
    protected EncounterState state;
    protected int clicksFromDest;
    private final Planet source;
    private final Planet destination;

    protected int plunderScore;

    /**
     * Creates a new Encounter. Encounters have a player, and an fxmlscene.
     *
     * @param player the player of the game
     * @param fxmlScene the scene which should be displayed with this encounter.
     * @param clicksFromDest distance from destination
     * @param encounterName the name of the opponent you're encountering
     * @param source the origin planet
     * @param destination the destination planet
     */
    public Encounter(Player player, String fxmlScene, int clicksFromDest,
            String encounterName, Planet source, Planet destination) {
        this.player = player;
        this.encounterScene = fxmlScene;
        this.clicksFromDest = clicksFromDest;
        this.encounterName = encounterName;
        this.source = source;
        this.destination = destination;

        state = new IgnoreState(this); //defaults to ignore, important
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
     * Sets the state of this encounter.
     *
     * @param state the state for this encounter
     */
    public void setState(EncounterState state) {
        this.state = state;
    }

    /**
     * Sets the state of this encounter.
     *
     * @return the state of this encounter
     */
    public EncounterState getState() {
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
     * Gets the planet the player is departing from.
     *
     * @return the source planet
     */
    public Planet getSource() {
        return this.source;
    }

    /**
     * Gets the planet the player is traveling towards.
     *
     * @return the destination planet
     */
    public Planet getDestination() {
        return this.destination;
    }

    /**
     * Gets the message that should be displayed when there is an encounter.
     *
     * @return the encounter message
     */
    public String getEncounterMessage() {
        return String.format("A %s %s is spotted %d clicks from your destination, %s!",
                encounterName.toLowerCase(), opponent.getType().toString(),
                clicksFromDest, getDestination().getName());
    }

    /**
     * Gets the message that should be displayed when this the opponent ignores
     * the player.
     *
     * @return the ignore message
     */
    public String getIgnoreMessage() {
        return getEncounterMessage() + "\n\nIt ignores you.";
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
     *
     */
    public abstract void increaseShipsKilled();

    /**
     * Gets the opponent ship's bounty, the amount the player gets for
     * destroying it. Or zero if they player doesn't get a bounty for destroying
     * it.
     *
     * @return the opponent ship's bounty or 0 if it has none
     */
    public int getOpponentBounty() {
        return 0; //by default, encounters do not haves bounties.
    }

    /**
     * Determines if this kind of encounter allows the player to scoop from the
     * opponent ship upon its defeat.
     *
     * @return true if the player can scoop in this encounter
     */
    protected abstract boolean canBeScoopedFrom();

    /**
     * Determines if the player is able to surrender in this kind of encounter.
     *
     * @return true if the player can surrender
     */
    protected abstract boolean playerCanSurrender();

    /**
     * Determines if the player is too intimidated by the player and must flee.
     *
     * @return true if the player flees
     */
    protected boolean opponentIntimidated() {
        return false;
    }

    /**
     * Determines if the encounter state needs to change after the player's last
     * action. If so, it will be changed.
     *
     * @param originalPlayerHull the player's hull before damage this round
     * @param originalOpponentHull the opponent's hull before damage this round
     * @return the state to change to, or null if current state shouldn't change
     */
    protected abstract EncounterState determineStateChange(
            int originalPlayerHull, int originalOpponentHull);

    /**
     * Updates the players Police Record from plundering an opponent ship.
     */
    public void updateRecordAfterPlunder() {
        int updatedScore = player.getPoliceRecordScore() + plunderScore;
        player.setPoliceRecordScore(updatedScore);
    }

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
     * Creates the space ship for the opponent.
     *
     * @param tries number tries to pick up a ship for opponent
     * @param lowestShipType weakest ship type
     * @param cargoModifier
     * @return SpaceShip to use for opponent
     */
    protected SpaceShip createShip(int tries, ShipType lowestShipType,
            double cargoModifier) {
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
