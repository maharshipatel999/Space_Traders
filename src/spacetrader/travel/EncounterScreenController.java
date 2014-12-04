/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import spacetrader.Mercenary;
import spacetrader.Player;
import spacetrader.PoliceRecord;
import spacetrader.SkillList;
import static spacetrader.Tools.rand;
import spacetrader.commerce.Cargo;
import spacetrader.commerce.TradeGood;
import spacetrader.commerce.Wallet;
import spacetrader.exceptions.InsufficientFundsException;
import spacetrader.ships.PlayerShip;
import spacetrader.ships.ShipType;
import spacetrader.system.SceneController;
import spacetrader.system.SpaceTrader;

/**
 * Represents a controller for an encounter screen.
 *
 * @author Caleb
 */
public abstract class EncounterScreenController extends SceneController {

    private static final String NO_WEAPONS_HRLP_MSG
            = "You either are flying a ship without any weapon slots, so your only "
            + "option is to flee from fights, or you haven't bought any weapons yet."
            + " Sorry, no weapons, no attacking.";

    protected Encounter encounter;

    @FXML
    protected BorderPane borderPane;
    @FXML
    private HBox buttonBar;

    private BattleController battleControl;

    /**
     * Sets the encounter for this encounter controller.
     *
     * @param encounter
     */
    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
        borderPane.setCenter(loadBattlePane());
        encounter.getState().setButtons(this, buttonBar);
    }

    /**
     * Loads the battle pane from its fxml file. Initializes all the text on the
     * battle pane.
     *
     * @return the newly created pane
     */
    protected Pane loadBattlePane() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/spacetrader/travel/BattleScreen.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            Logger.getLogger(SpaceTrader.class.getName()).log(Level.SEVERE, null, e);
        }
        this.battleControl = loader.getController();
        this.battleControl.setUpBattle(encounter);

        return pane;
    }

    protected void setState(EncounterState state) {
        encounter.setState(state);
        state.setButtons(this, buttonBar);
    }

    /**
     * Initiates Player attack sequence (Attack Pressed).
     */
    @FXML
    protected void attackPressed() {
        if (encounter.getPlayer().getShip().getTotalWeaponStrength() <= 0) {
            mainControl.displayErrorMessage(null, "No Weapons", "You can't attack without weapons!");
            finishEncounter();
        } else {
            battleControl.resetBattleText();
            encounter.getState().playerAttacks(this, encounter.opponentIntimidated());
            battleControl.updateEnemyAction();
        }
    }

    @FXML
    protected void fleePressed() {
        battleControl.resetBattleText();
        encounter.getState().playerFlees(this);
        battleControl.updateEnemyAction();
    }

    /**
     * opponent attacks player attacks check for destroyed ships check if
     * players escape update encounter state update battle pane
     */
    /*public void executeRound(boolean playerFlees) {
     final int originalPlayerHull = encounter.getPlayer().getShip().getHullStrength();
     final int originalOpponentHull = encounter.getOpponent().getHullStrength();
        
     battleControl.resetBattleText();
     if (encounter.getState() == Encounter.State.ATTACK) {
     opponentAttacks(playerFlees);
     }
     if (!playerFlees) {
     playerAttacks();
     }
     checkIfShipsDestroyed();
        
     if (playerFlees) {
     checkIfPlayerEscapes();
     } else if (encounter.getState() == Encounter.State.FLEE || encounter.getState() == Encounter.State.SURRENDER) {
     checkIfEnemyEscapes();
     }
     checkIfChangeState(originalPlayerHull, originalOpponentHull);
     encounter.getEncounterState().setButtons(this, buttonBar);
        
     battleControl.updateEnemyAction();
     }*/
    protected abstract void surrenderPressed();

    protected void ignorePressed() {
        finishEncounter();
    }

    protected void plunderPressed() {
        encounter.updateRecordAfterPlunder();
        //TODO do plundering form
        mainControl.displayInfoMessage("You plundered!", "Plundering!", "You plunder the opponent.");
        finishEncounter();
    }

    public void opponentAttacks(boolean playerFlees) {
        boolean playerGotHit;
        playerGotHit = encounter.getOpponent().fireAtShip(encounter.getPlayer().getShip(), playerFlees);
        if (playerGotHit) {
            battleControl.displayActionText("The %s ship hits you.\n");
            if (encounter.getPlayer().getShip().getHullStrength() < 0) {
                encounter.getPlayer().getShip().setHullStrength(0);
            }
            battleControl.updatePlayerHull();
        } else {
            //In the original game this gets displayed if (state != FleeState && !playerGotHit)
            //I think that's a mistake though since the opponent only attacks if in the AttackState
            battleControl.displayActionText("The %s ship missed you.\n");
        }

        battleControl.setPlayerGotHit(playerGotHit);
    }

    public void playerAttacks() {
        boolean opponentGotHit;
        boolean enemyFlees = (encounter.getState() instanceof FleeState);
        opponentGotHit = encounter.getPlayer().getShip().fireAtShip(encounter.getOpponent(), enemyFlees);
        if (opponentGotHit) {
            battleControl.displayActionText("You hit the %s ship.\n");
            if (encounter.getOpponent().getHullStrength() < 0) {
                encounter.getOpponent().setHullStrength(0);
            }
            battleControl.updateOpponentHull();
        } else {
            battleControl.displayActionText("You missed the %s ship.\n");
        }

        battleControl.setOpponentGotHit(opponentGotHit);
    }

    public void checkIfShipsDestroyed() {
        final boolean playerDestroyed = encounter.getPlayer().getShip().getHullStrength() <= 0;
        final boolean opponentDestroyed = encounter.getOpponent().getHullStrength() <= 0;

        if (playerDestroyed) {
            if (encounter.getPlayer().getShip().hasEscapePod()) {
                escapeWithPod();
            } else if (opponentDestroyed) {
                //TODO
                mainControl.displayInfoMessage(null, "Both Destroyed", "You and your opponent have managed to destroy each other.");
                mainControl.endGame(true);
                this.getScene().getWindow().hide();
            } else {
                //TODO
                mainControl.displayInfoMessage(null, "You Lose", "Your ship has been destroyed by your opponent.");
                mainControl.endGame(true);
                this.getScene().getWindow().hide();
            }
        } else if (opponentDestroyed) {
            int bounty = encounter.getOpponentBounty();
            if (bounty > 0) {
                encounter.getPlayer().addCredits(bounty);
                mainControl.displayInfoMessage("Bounty Earned", "You destroyed the pirate ship.", "You earned a bounty of ₪%d.", bounty);
                mainControl.displayInfoMessage(null, null, "BountyBountyLabel");
            } else {
                //TODO
                mainControl.displayInfoMessage(null, "You Win", "You have destroyed your opponent.");
                finishEncounter();
            }
            encounter.increaseShipsKilled();
            if (encounter.canBeScoopedFrom()) {
                scoop(encounter.getPlayer().getCargo());
            }

            //calculate new reputation
            int reputation = encounter.getPlayer().getReputationScore();
            reputation += 1 + (encounter.getOpponent().getType().ordinal() / 2);
            encounter.getPlayer().setReputationScore(reputation);
        }
    }

    public void checkIfPlayerEscapes() {
        //must be at least 1
        final int ENEMY_PILOT_SKILL = Math.max(1, encounter.getOpponent().getEffectiveSkill(SkillList.Skill.PILOT));
        final int PILOT_MODIFIER = 4; //amount to multiply random pilot number by

        if ((rand.nextInt(7) + (encounter.getPlayer().getEffectiveSkill(SkillList.Skill.PILOT) / 3)) * 2
                >= rand.nextInt(ENEMY_PILOT_SKILL) * PILOT_MODIFIER) {
            if (battleControl.isPlayerGotHit()) {
                //TODO
                mainControl.displayInfoMessage(null, "You escaped", "You got hit, but still managed to escape.");
                finishEncounter();
            } else {
                //TODO
                mainControl.displayInfoMessage(null, "Escaped!", "You have managed to escape your opponent.");
                finishEncounter();
            }
        } else {
            battleControl.displayActionText("The %s ship is still following you.\n");
        }
    }

    public void checkIfEnemyEscapes() {
        //must be at least 1
        final int PLAYER_PILOT_SKILL = Math.max(1, encounter.getPlayer().getEffectiveSkill(SkillList.Skill.PILOT));
        final int PILOT_MODIFIER = 4; //amount to multiply random pilot number by

        if (rand.nextInt(PLAYER_PILOT_SKILL) * PILOT_MODIFIER
                <= rand.nextInt((7 + encounter.getOpponent().getEffectiveSkill(SkillList.Skill.PILOT) / 3)) * 2) {
            //TODO
            mainControl.displayInfoMessage(null, "Opponent Escaped", "Your opponent has managed to escape.");
            finishEncounter();
        } else {
            battleControl.displayActionText("The %s ship didn't get away.\n");
        }
    }

    public void checkIfChangeState(int originalPlayerHull,
            int originalOpponentHull) {
        //Determine whether the opponent's actions must be changed
        if (encounter.getOpponent().getHullStrength() < originalOpponentHull) {
            EncounterState nextState = encounter.determineStateChange(originalPlayerHull, originalOpponentHull);
            if (nextState != null) {
                encounter.setState(nextState);
                nextState.setButtons(this, buttonBar);
            }
        }
    }

    /**
     * Determines if the player will take a canaster from a destroyed ship. Uses
     * dialogs to allow the player to choose if they want the item, and which
     * item to dump from their own ship if their cargo bays are full.
     *
     * @param playerCargo the player's cargo
     */
    protected void scoop(Cargo playerCargo) {
        // Chance 50% to pick something up on Normal level
        if (rand.nextBoolean() != true) {
            return;
        }

        // More chance to pick up a cheap good
        TradeGood good = TradeGood.getRandomTradeGood();
        if (good.ordinal() >= 5) {
            good = TradeGood.getRandomTradeGood();
        }
        String message = String.format("ship, labeled %s, drifts", good.toString());
        String response = mainControl.displayCustomConfirmation(null, "Scoop", message, "Pick It Up", "Let It Go");

        if (!response.equals("Pick It Up")) {
            return;
        }

        if (playerCargo.isFull()) {
            String noScoopMessage = "You have no open cargo bays";
            response = mainControl.displayCustomConfirmation(null, "Unable to Scoop", noScoopMessage, "Make Room", "Let It Go");
            if (response.equals("Let It Go")) {
                return;
            }

            //Ask player which item they want to discard.
            String cargoString = "Your Current Cargo:\n";
            for (TradeGood playerGood : playerCargo.getTradeGoods()) {
                cargoString += playerGood.toString() + " (" + playerCargo.getQuantity(playerGood) + ")\n";
            }
            Dialog<TradeGood> dumpDialog = new ChoiceDialog<>(null, playerCargo.getTradeGoods());
            dumpDialog.setContentText(cargoString);
            dumpDialog.setHeaderText("Which cargo would you like to dump?");
            dumpDialog.setOnCloseRequest(e -> {
                if (mainControl.displayYesNoConfirmation(
                        null, "Are you want to cancel?",
                        "If you selected yes, you will not be able to get the "
                        + "opponent's good.")) {
                } else {
                    e.consume();
                }
            });
            dumpDialog.showAndWait();
            TradeGood goodToDump = dumpDialog.getResult();
            playerCargo.removeItem(goodToDump, 1);
        }

        if (!playerCargo.isFull()) {
            playerCargo.addItem(good, 1, 0);
        } else {
            //TODO
            mainControl.displayInfoMessage(null, null, "NoDumpNoScoopAlert");
        }
    }

    /**
     * Happens when the player gets arrested by the police. Fines and imprisons
     * the player.
     */
    public void playerGetsArrested() {
        Player player = encounter.getPlayer();
        int negPoliceScore = -player.getPoliceRecordScore();

        int fine = ((1 + (((player.getCurrentWorth() * Math.min(80, negPoliceScore)) / 100) / 500)) * 500);
        int daysImprisoned = Math.max(30, negPoliceScore);

        mainControl.displayInfoMessage(null, "You've been arrested!", "At least you survived.");
        mainControl.displayInfoMessage(null, "Conviction", "Your fine and number of days in prison are based on"
                + " the kind of criminal you were found to be.");

        String conviction = String.format("You are convicted to %d days in prison and a fine of ₪%d.", daysImprisoned, fine);
        mainControl.displayInfoMessage(null, "Conviction Determined", conviction);

        //Remove Illegal Goods
        if (player.getShip().isCarryingIllegalGoods()) {
            mainControl.displayInfoMessage(null, "Illegal Goods Impounded", "The police also impound all of the illegal goods you have on board.");
            player.getCargo().clearItem(TradeGood.NARCOTICS);
            player.getCargo().clearItem(TradeGood.FIREARMS);
        }
        //Remove Insurance
        if (player.getInsuranceCost() <= 0) {
            mainControl.displayInfoMessage(null, "Insurance Lost", "Since you cannot pay your insurance while you're in prison, it is retracted.");
            player.setInsuranceCost(0);
            //NoClaim = 0; reset no-claim to zero
        }
        //Remove Crew
        Mercenary[] crew = player.getShip().getCrew();
        if (crew.length > 0) {
            mainControl.displayInfoMessage(null, "Mercenaries Leave", "You can't pay your mercenaries "
                    + "while you are imprisoned, and so they have sought new employment.");
            for (int i = 0; i < crew.length; i++) {
                player.getShip().fireMercenary(crew[i]);
            }
        }
        //close the encounter window
        this.getScene().getWindow().hide();

        //TODO Handle arriving at planet. set prices, change quantities, shuffle status
        //go to the new planet'
        mainControl.specialArrivalAtPlanet(encounter.getDestination());

        //Increment Days
        for (int i = 0; i < daysImprisoned; i++) {
            mainControl.increaseDays();
        }

        //Pay the fine
        if (player.getCredits() >= fine) {
            player.removeCredits(fine);
        } else {
            //Sell the player's ship
            player.addCredits(player.getShip().currentShipPrice(player.getEffectiveSkill(SkillList.Skill.TRADER)));

            if (player.getCredits() >= fine) {
                player.removeCredits(fine);
            } else {
                player.setCredits(0);
            }
            mainControl.displayInfoMessage(null, "Ship Sold", "The Space Corps needs cash to make"
                    + " you pay for the damages you did. Your ship is the only "
                    + "valuable possession you have.");
            mainControl.displayInfoMessage(null, "Flea Received", "It's standard practice for the"
                    + " police to leave a condemned criminal with at least the "
                    + "means to leave the solar system.");

            //CreateFlea();
            player.setShip(new PlayerShip(ShipType.FLEA, player));
        }

        //Update their Police Record
        player.setPoliceRecordScore(PoliceRecord.DUBIOUS.minScore());

        //Pay off as much as the player's debt as possible.
        int debt = player.getDebt();
        if (player.getDebt() > 0) {
            if (player.getCredits() >= debt) {
                player.removeCredits(debt);
                player.removeDebt(debt);
            } else {
                player.removeDebt(player.getCredits());
                player.setCredits(0);
            }
        }

        //Pay Interest on all the days imprisoned.
        try {
            for (int i = 0; i < daysImprisoned; ++i) {
                player.payInterest();
            }
        } catch (InsufficientFundsException e) {
            mainControl.displayInfoMessage(null, "Too Much Debt", Wallet.MAX_DEBT_MSG);
        }
    }

    /**
     * Occurs when the player's ship is destroyed but he has an escape pod.
     */
    public void escapeWithPod() {
        Player player = encounter.getPlayer();

        mainControl.displayInfoMessage(null, "Escape Pod Activated", "Just before the final demise of your ship, your escape pod gets activated and ejects you."
                + "After a few days, the Space Corps picks you up and drops you off at a nearby space port.");

        //close encounter window
        this.getScene().getWindow().hide();

        mainControl.specialArrivalAtPlanet(encounter.getDestination());

        if (player.getInsuranceCost() > 0) {
            //TODO
            mainControl.displayInfoMessage(null, "Insurance Pays", " Since your ship was insured, the bank pays you the total worth of the destroyed ship.");
            player.addCredits(player.getShip().currentShipPriceWithoutCargo(player.getEffectiveSkill(SkillList.Skill.TRADER)));
        }
        mainControl.displayInfoMessage(null, "Flea Built", "In 3 days and with 500 credits, you manage to convert your pod into a Flea.\n");

        player.removeCreditsForced(500);

        //increment days
        for (int i = 0; i < 3; i++) {
            mainControl.increaseDays();
        }

        //Create Flea
        player.setShip(new PlayerShip(ShipType.FLEA, player));

        //do SystemInformationForm
    }

    public void finishEncounter() {
        this.getScene().getWindow().hide();
        mainControl.goBackToWarpScreen();
    }
}
