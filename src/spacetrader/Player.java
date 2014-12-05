/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader;

import java.io.Serializable;
import spacetrader.SkillList.Skill;
import spacetrader.commerce.Cargo;
import spacetrader.commerce.Consumer;
import spacetrader.commerce.Wallet;
import spacetrader.exceptions.InsufficientFundsException;
import spacetrader.planets.Planet;
import spacetrader.ships.Gadget;
import spacetrader.ships.GadgetType;
import spacetrader.ships.PlayerShip;
import spacetrader.ships.ShipType;
import spacetrader.ships.Weapon;
import spacetrader.ships.WeaponType;
import spacetrader.system.InformationPresenter;
import spacetrader.system.MainController;
import spacetrader.system.SpaceTrader.Difficulty;

/**
 *
 * @author Seth
 */
public class Player extends Trader implements Consumer, Serializable {

    private int policeKills = 0; // Number of police ships killed
    private int traderKills = 0; // Number of trader ships killed
    private int pirateKills = 0; // Number of pirate ships killed
    private int policeRecordScore = 0; // 0 = Clean record
    private int reputationScore = 0; // 0 = Harmless

    private final Wallet wallet;
    private PlayerShip ship;
    private Planet location;
    private Difficulty difficulty;

    /**
     * Instantiates a new Player in the Game
     *
     * @param name name of the Player
     * @param pilot Pilot level
     * @param fighter Fighter level
     * @param trader Trader level
     * @param engineer Engineer level
     * @param investor Investor level
     */
    public Player(String name, int pilot, int fighter, int trader, int engineer,
            int investor) {
        super(name, pilot, fighter, trader, engineer, investor);
        wallet = new Wallet();
        ship = new PlayerShip(ShipType.GNAT, this);
        ship.getWeapons().addItem(new Weapon(WeaponType.PULSE));
        this.difficulty = Difficulty.NORMAL;
    }

    /**
     * The destination planet's prices should be recalculated. Determine if the
     * player can pay their mercenary, interest, and insurance costs. If not,
     * kick them back to the home screen. Adjust the player's police record and
     * reputation. Deduct fuel from the player's startingShip. Finally, go to
     * the warp screen.
     *
     * @param destination which planet the player is traveling to
     * @param source which planet the player is leaving
     * @param mainControl the game's main controller
     */
    public void departFromPlanet(Planet source, Planet destination,
            MainController mainControl) {
        InformationPresenter dialogs = InformationPresenter.getInstance();

        try {
            payDailyFees();
        } catch (InsufficientFundsException e) {
            String msgTitle, message;
            switch (e.getMessage()) {
                case "insurance":
                    msgTitle = "Unable to Pay Insurance";
                    message = "You do not have enough money to pay for your insurance!"
                            + "\n\nUntil you stop your insurance at the bank, or aquire more money,"
                            + " you will not be allowed to depart.";
                    break;
                case "mercenaries":
                    msgTitle = "Unable to Pay Crew Salaries";
                    message = "You do not have enough money to ";
                    break;
                case "debt":
                    msgTitle = "Too Much Debt";
                    message = Wallet.MAX_DEBT_MSG;
                    break;
                default: //should never happen
                    msgTitle = "Unknown Fee Error";
                    message = "You have a mysterious fee which can not be paid. Sorry.";
                    break;
            }
            //Go back to the Planet Home Screen
            dialogs.displayInfoMessage(null, msgTitle, message);
            mainControl.goToHomeScreen(getLocation());
        }

        if (getInsuranceCost() > 0) {
            wallet.incNoClaimDays();
        }

        getShip().fullyRepairShields();

        if (getDebt() > Wallet.DEBT_WARNING) {
            dialogs.displayWarningMessage(null, "Debt Warning!", "You get this warning because "
                    + "your debt has exceeded 75000 credits. If you don't pay "
                    + "back quickly, you may find yourself stuck in a system with"
                    + " no way to leave. You have been warned.");
        }

        //Decrease police record score if very high
        if (mainControl.getDays() % 3 == 0 && getPoliceRecord().compareTo(PoliceRecord.CLEAN) > 0) {
            this.policeRecordScore--;
        }

        //Increase police record score if very low
        if (getPoliceRecord().compareTo(PoliceRecord.DUBIOUS) < 0) {
            this.policeRecordScore++;
        }

        //Deduct fuel
        int distance = (int) Universe.distanceBetweenPlanets(source, destination);
        ship.removeFuel(distance);

        //Recalculate prices on destination planet so trader encounters work
        destination.getMarket().setAllPrices(this);

        //Start doing encounters!
        mainControl.goToWarpScreen(source, destination);
    }

    /**
     * This is like the "departFromPlanet" method, but is only used when
     * traveling via wormhole. Since this is the case, we will only be
     * recalculating the destination planet's prices. All other events do not
     * occur.
     *
     * @param destination which planet the player is traveling to
     * @param source which planet the player is leaving
     * @param mainControl the game's main controller
     */
    public void specialDepartFromPlanet(Planet source, Planet destination,
            MainController mainControl) {
        destination.getMarket().setAllPrices(this);
        mainControl.specialArrivalAtPlanet(destination);
    }

    /**
     * return Skill Player is using
     *
     * @param type Specific skill type of Player
     * @return amount of skill
     */
    public int getEffectiveSkill(Skill type) {
        final int SKILL_BONUS = 3;
        final int CLOAK_BONUS = 2;

        int level = Math.max(getSkill(type), ship.getCrewSkill(type));

        if (type == Skill.FIGHTER && ship.getGadgets().contains(new Gadget(GadgetType.TARGETING))) {
            level += SKILL_BONUS;
        }
        if (type == Skill.PILOT) {
            if (ship.getGadgets().contains(new Gadget(GadgetType.NAVIGATION))) {
                level += SKILL_BONUS;
            }
            if (ship.getGadgets().contains(new Gadget(GadgetType.CLOAK))) {
                level += CLOAK_BONUS;
            }
        }
        if (type == Skill.ENGINEER && ship.getGadgets().contains(new Gadget(GadgetType.AUTO_REPAIR))) {
            level += SKILL_BONUS;
        }

        //adapt skill for game difficulty
        if (difficulty == Difficulty.BEGINNER || difficulty == Difficulty.EASY) {
            level++;
        } else if (difficulty == Difficulty.IMPOSSIBLE) {
            level--;
            level = Math.max(0, level);
        }

        return level;
    }

    /**
     * Sets the game's difficulty.
     *
     * @param difficulty the game's difficulty
     */
    public void setGameDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Retrieve Ship of Player
     *
     * @return PlayerShip
     */
    public PlayerShip getShip() {
        return ship;
    }

    /**
     * assigns a ship to Player
     *
     * @param ship ship to assign to Player
     */
    public void setShip(PlayerShip ship) {
        this.ship = ship;
    }

    /**
     * Gets location of Planet
     *
     * @return location of planet
     */
    public Planet getLocation() {
        return this.location;
    }

    /**
     * Sets location of specific planet
     *
     * @param planet Planet to set the location of guys
     */
    public void setLocation(Planet planet) {
        this.location = planet;
    }

    /**
     * Gets the cargo of this player's ship's cargo.
     *
     * @return this player's cargo
     */
    public Cargo getCargo() {
        return ship.getCargo();
    }

    /**
     * get number of Police killed
     *
     * @return get number of Police player has killed
     */
    public int getPoliceKills() {
        return policeKills;
    }

    /**
     * increase the number of Police kills Player has done
     */
    public void increasePoliceKills() {
        policeKills++;
    }

    /**
     * get number of Traders killed
     *
     * @return get number of Traders player has killed
     */
    public int getTraderKills() {
        return traderKills;
    }

    /**
     * increase record of number of kills Player has
     */
    public void increaseTraderKills() {
        traderKills++;
    }

    /**
     * get number of Pirate Kills
     *
     * @return number of Pirate Kills
     */
    public int getPirateKills() {
        return pirateKills;
    }

    /**
     * increase record of number Pirate kills
     */
    public void increasePirateKills() {
        pirateKills++;
    }

    /**
     * get the record of Police Record
     *
     * @return number of Police Scores
     */
    public int getPoliceRecordScore() {
        return policeRecordScore;
    }

    /**
     * set Police Record Score for Player
     *
     * @param score set Police Record Score for Player
     */
    public void setPoliceRecordScore(int score) {
        policeRecordScore = score;
    }

    /**
     * get Player Reputation Score
     *
     * @return Player Reputation Score
     */
    public int getReputationScore() {
        return reputationScore;
    }

    /**
     * Sets this Player's reputation score.
     *
     * @param reputationScore the new reputation score
     */
    public void setReputationScore(int reputationScore) {
        this.reputationScore = reputationScore;
    }

    /**
     * get Police Record
     *
     * @return Police Record
     */
    public PoliceRecord getPoliceRecord() {
        return PoliceRecord.getPoliceRecord(policeRecordScore);
    }

    /**
     * get Rep. of Player
     *
     * @return reputation of Player
     */
    public Reputation getReputation() {
        return Reputation.getReputation(reputationScore);
    }

    /**
     * Calculates the player's current worth. This is determined by the amount
     * of money you own minus the money you owe. It also considers the value of
     * your spaceship and all the things in it.
     *
     * @return the player's current worth
     */
    public int getCurrentWorth() {
        return wallet.getCredits() - wallet.getDebt() + ship.currentShipPrice(getEffectiveSkill(Skill.TRADER));
    }

    /**
     * Pays the salaries for each of the mercenaries on the player's ship.
     */
    public void payMercenaries() {
        //TODO
    }

    /**
     * Pays all of the daily costs, such as insurance, mercenaries fees, and
     * interest rates. Throws InsufficientFundsExceptions depending on which fee
     * could not be paid.
     */
    public void payDailyFees() {
        //store the player's money so that if they don't have enough money to
        //pay one of their bills, their money can be restored to its original value.
        int originalPlayerMoney = wallet.getCredits();

        try {
            payInsurance();
        } catch (InsufficientFundsException e) {
            wallet.setCredits(originalPlayerMoney);
            throw new InsufficientFundsException("insurance");
        }

        try {
            payMercenaries();
        } catch (InsufficientFundsException e) {
            wallet.setCredits(originalPlayerMoney);
            throw new InsufficientFundsException("mercenaries");
        }

        try {
            payInterest();
        } catch (InsufficientFundsException e) {
            wallet.setCredits(originalPlayerMoney);
            throw e;
        }
    }

    @Override
    public void addCredits(int deposit) {
        wallet.addCredits(deposit);
    }

    @Override
    public void removeCredits(int withdrawal) {
        wallet.removeCredits(withdrawal);
    }

    @Override
    public void removeCreditsForced(int withdrawal) {
        wallet.removeCreditsForced(withdrawal);
    }

    @Override
    public int getCredits() {
        return wallet.getCredits();
    }

    @Override
    public void setCredits(int credits) {
        wallet.setCredits(credits);
    }

    @Override
    public int getDebt() {
        return wallet.getDebt();
    }

    @Override
    public void addDebt(int addition) {
        wallet.addDebt(addition);
    }

    @Override
    public void removeDebt(int removal) {
        wallet.removeDebt(removal);
    }

    @Override
    public void setInsuranceCost(int cost) {
        wallet.setInsuranceCost(cost);
    }

    @Override
    public int getInsuranceCost() {
        return wallet.getInsuranceCost();
    }

    @Override
    public int getNoClaimDays() {
        return wallet.getNoClaimDays();
    }
    
    /**
     * Sets the number of no claim days to zero.
     */
    public void resetNoClaimDays() {
        wallet.resetNoClaimDays();
    }

    @Override
    public void payInsurance() {
        wallet.payInsurance();
    }

    @Override
    public void payInterest() {
        wallet.payInterest();
    }

}
