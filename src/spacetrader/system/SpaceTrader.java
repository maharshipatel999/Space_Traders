/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.system;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import spacetrader.HighScoreList;
import spacetrader.Player;
import spacetrader.Universe;
import spacetrader.planets.Planet;
import spacetrader.ships.PlayerShip;
import spacetrader.ships.Weapon;
import spacetrader.ships.WeaponType;

/**
 * This class keeps references to the game's Universe and Player, as well as the
 * Main Controller.
 *
 * @author nkaru_000, Caleb Stokols
 */
public class SpaceTrader extends Application {
    public static Debug debugStatus = Debug.OFF;

    private Universe universe;
    private Player player;
    private MainController mainControl;
    private int days;
    private Difficulty difficulty;
    
    public enum Difficulty {
        BEGINNER, EASY, NORMAL, HARD, IMPOSSIBLE;
    }
    
    public enum Debug {
        OFF, TRADER_ENCOUNTER, POLICE_ENCOUNTER, PIRATE_ENCOUNTER, METEORS
    }

    @Override
    public void start(Stage primaryStage) {
        InformationPresenter.getInstance().setStage(primaryStage);
        mainControl = new MainController(this, primaryStage);
        difficulty = Difficulty.NORMAL;
        days = 0;
        mainControl.goToWelcomeScreen();
    }
    
    /**
     * Sets the game's universe to a new universe and the game's player to a
     * specified Player.
     *
     * @param universe the universe of the game
     * @param player the player of the game
     */
    public void setUpGame(Universe universe, Player player) {
        this.universe = universe;
        this.player = player;
        this.days = 0;
        this.universe.getHomePlanet().movePlayerLocation(player, mainControl);
        this.mainControl.goToHomeScreen(universe.getHomePlanet());
    }
    
    /**
     * Starts the game using user-defined values
     *
     * @param cheats the holder of the user-defined values
     */
    public void setUpGameWithCheats(AdminCheats cheats) {
        debugStatus = cheats.getDebugMode();
        Planet homePlanet = new Planet("Pallet", new Point2D(Universe.WIDTH / 2, Universe.HEIGHT / 2),
                cheats.getTechLevel(), cheats.getResource(), cheats.getPoliticalSystem());
        
        Player cheatPlayer = new Player("LubMaster", 3, 3, 3, 3, 3);
        cheatPlayer.setCredits(cheats.getInitialCredits());
        cheatPlayer.setReputationScore(cheats.getReputation().minRep());
        cheatPlayer.setPoliceRecordScore(cheats.getPoliceRecord().minScore());
        
        cheatPlayer.setShip(new PlayerShip(cheats.getStartingShip(), cheatPlayer));
        if (cheatPlayer.getShip().getType().weaponSlots() > 0) {
            cheatPlayer.getShip().getWeapons().addItem(new Weapon(WeaponType.PULSE));
        }

        setUpGame(new Universe(homePlanet), cheatPlayer);
    }

    /**
     * Gets the main controller of the game
     *
     * @return the main controller of this game
     */
    public MainController getMainController() {
        return this.mainControl;
    }

    /**
     * Gets the universe for this game.
     *
     * @return this game's universe
     */
    public Universe getUniverse() {
        return universe;
    }

    /**
     * Sets the universe for this game.
     *
     * @param universe this game's universe, cannot be null
     */
    public void setUniverse(Universe universe) {
        if (universe == null) {
            throw new IllegalArgumentException("Universe cannot be null.");
        }
        this.universe = universe;
    }

    /**
     * Gets the main player of this game.
     *
     * @return this game's player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the main player of this game.
     *
     * @param player this game's player, cannot be null
     */
    public void setPlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        this.player = player;
    }

    /**
     * Resets the number of days that have passed to zero.
     */
    public void resetDays() {
        days = 0;
    }

    /**
     * Gets the current number of days that have passed.
     *
     * @return the number of days that have passed.
     */
    public int getDays() {
        return days;
    }

    /**
     * Increase the number of days playing.
     */
    public void increaseDays() {
        days++;
    }

    /**
     * Gets the game's difficulty level.
     * 
     * @return the game's difficulty level
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the game difficulty level.
     * 
     * @param difficulty the new difficulty level for this game.
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
