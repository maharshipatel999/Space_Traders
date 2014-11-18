/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.system;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import spacetrader.Player;
import spacetrader.exceptions.InsufficientFundsException;
import spacetrader.planets.Planet;
import spacetrader.planets.TechLevel;
import spacetrader.ships.FuelTank;
import spacetrader.ships.GadgetType;
import spacetrader.ships.ShieldType;
import spacetrader.ships.ShipType;
import spacetrader.ships.WeaponType;

/**
 * FXML Controller class.
 *
 * @author nkaru_000
 */
public class ShipYardScreenController
        extends SceneController
        implements Initializable {

    @FXML
    private Label walletAmt;

    @FXML
    private Label fuelAmountLabel, hullAmountLabel;
    @FXML
    private ProgressBar fuelProgress, hullProgress;
    @FXML
    private Button incFuelButton, maxFuelButton;
    @FXML
    private Button incHullButton, maxHullButton;
    @FXML
    private Label fuelStatus, hullStatus;

    @FXML
    private Label shipDesc, equipmentDesc, escapePodText;
    @FXML
    private Button saleShips, saleEquipment, buyEscapePodButton;

    @FXML
    private Button backButton;

    private Player player;
    private Planet planet;

    /**
     * Calculates the minimum required tech level in order to have a equipment
     * market.
     *
     * @return the minimum required tech level to sell or buy equipment
     */
    private static TechLevel minimumRequiredTechLevel() {
        int minLevel = TechLevel.values().length; //one greater than the max
        for (GadgetType type : GadgetType.values()) {
            minLevel = Math.min(minLevel, type.minTechLevel().ordinal());
        }
        for (ShieldType type : ShieldType.values()) {
            minLevel = Math.min(minLevel, type.minTechLevel().ordinal());
        }
        for (WeaponType type : WeaponType.values()) {
            minLevel = Math.min(minLevel, type.minTechLevel().ordinal());
        }
        return TechLevel.values()[minLevel];
    }

    /**
     * Sets up the ship yard. Selects which buttons to reveal/hide and
     * enable/disable. Sets the hull and full progress bars and player money.
     *
     * @param player
     */
    public void setUpShipYardScreen(Player player) {
        this.player = player;
        this.planet = player.getLocation();
        int amount = player.getWallet().getCredits();

        walletAmt.setText("Wallet: ₪" + amount);
        updateFuelBar();
        updateHullBar();
        setFuelButtons();
        setHullButtons();

        //Makes it the default button on the screen.
        maxFuelButton.requestFocus();

        //Determine if this planet sells ships, equipment, or escape pods.
        if (player.getLocation().getLevel().compareTo(ShipType.FLEA.minTechLevel()) >= 0) {
            shipDesc.setText(
                    "There are several ships currently avaible for purchase!");
        } else {
            shipDesc.setText("There are no ships for sale.");
            saleShips.setDisable(true);
        }

        if (planet.getLevel().compareTo(minimumRequiredTechLevel()) >= 0) {
            equipmentDesc.setText(
                    "There are several equipment available for purchase!");
        } else {
            equipmentDesc.setText("This planet does not sell ship equipment.");
            saleEquipment.setDisable(true);
        }
        updateEscapePodText();
    }

    private void updateEscapePodText() {
        if (player.getShip().hasEscapePod()) {
            escapePodText.setText("Your ship already has an escape pod.");
            buyEscapePodButton.setDisable(true);
        } else if (player.getCredits() < 2000) {
            escapePodText.setText(
                    "You need at least ₪2000 to buy an escape pod");
            buyEscapePodButton.setDisable(true);
        } else {
            escapePodText.setText("An escape pod is for sale!");
        }
    }

    /**
     * Updates the fuel progress bar.
     */
    private void updateFuelBar() {
        int currFuel = player.getShip().getTank().getFuelAmount();
        int maxFuel = player.getShip().getTank().getMaxFuel();
        fuelProgress.setProgress((double) currFuel / maxFuel);
        fuelAmountLabel.setText(currFuel + "/" + maxFuel);
    }

    /**
     * Updates the hull strength progress bar.
     */
    private void updateHullBar() {
        double currStrength = player.getShip().getHullStrength();
        double maxStrength = player.getShip().getMaxHullStrength();
        hullProgress.setProgress(currStrength / maxStrength);
        hullAmountLabel.setText((100.0 * currStrength / maxStrength) + "%");
    }

    /**
     * Updates the buttons for buying fuel.
     */
    private void setFuelButtons() {
        int currFuel = player.getShip().getTank().getFuelAmount();
        int maxFuel = player.getShip().getTank().getMaxFuel();
        int fuelCost = player.getShip().getType().fuelCost();

        //Disable the player's ability to buy fuel if their fuel tank is full.
        if (currFuel == maxFuel) {
            fuelStatus.setVisible(true);
            fuelStatus.setText("Fuel Tank is completely full!");
            incFuelButton.setVisible(false);
            maxFuelButton.setVisible(false);
        } else {
            fuelStatus.setVisible(false);
            incFuelButton.setVisible(true);
            maxFuelButton.setVisible(true);

            maxFuelButton.setText("Fill Entire Tank: ₪"
                    + ((maxFuel - currFuel) * fuelCost));
            int refillAmt = (maxFuel - currFuel == 1) ? 1 : 2;
            incFuelButton.setText("Buy " + refillAmt
                    + " Unit" + (refillAmt > 1 ? "s" : "") + " of Fuel: ₪" + refillAmt * fuelCost);
        }
    }

    /**
     * Updates the buttons for fixing the hull.
     */
    private void setHullButtons() {
        int currHull = player.getShip().getHullStrength();
        int maxHull = player.getShip().getMaxHullStrength();
        int repairCost = player.getShip().getType().repairCost();

        //Disable the player's ability to repair 
        //the ship if their hull is completely fixed.
        if (currHull == maxHull) {
            hullStatus.setVisible(true);
            hullStatus.setText(
                    "Your ship's hull strength is completely fixed!");
            incHullButton.setVisible(false);
            maxHullButton.setVisible(false);
        } else {
            fuelStatus.setVisible(false);
            incFuelButton.setVisible(true);
            maxFuelButton.setVisible(true);

            maxHullButton.setText("Repair All Damage: ₪" + ((maxHull - currHull) * repairCost));

            double damageFraction = (maxHull - currHull) / (double) maxHull;
            int repairAmount = (damageFraction >= .10)
                    ? (maxHull / 10) : (maxHull - currHull);
            incHullButton.setText("Repair " + (((double) repairAmount / maxHull) * 100)
                    + "% of Hull: ₪" + (repairAmount * repairCost));
        }
    }

    /**
     * Adds 1 or 2 more fuel to the player's tank.
     */
    @FXML
    void increaseFuel() {
        int fuelAmount = (incFuelButton.getText().contains("1")) ? 1 : 2;
        buyFuel(fuelAmount);
    }

    /**
     * Completely fills the player's tank.
     */
    @FXML
    void increaseToMaxFuel() {
        FuelTank tank = player.getShip().getTank();
        int fuelDiff = tank.getMaxFuel() - tank.getFuelAmount();
        buyFuel(fuelDiff);
    }

    /**
     * Adds more fuel to the player's tank and removes the appropriate amount of
     * money from the player's wallet. If the player does not have enough money,
     * it will display an alert message.
     *
     * @param fuelAmount amoutn of fuel to buy
     */
    private void buyFuel(int fuelAmount) {
        try {
            int fuelCost = player.getShip().getType().repairCost();

            //Update Player's Wallet and Ship's Fuel Tank
            //This can throw an InsufficientFundsException
            player.getWallet().remove(fuelAmount * fuelCost);
            player.getShip().getTank().addFuel(fuelAmount);

            //Update ShipYardScreen
            int amount = player.getWallet().getCredits();
            walletAmt.setText("Wallet: ₪" + amount);
            updateFuelBar();
            setFuelButtons();
        } catch (InsufficientFundsException e) {
            mainControl.displayAlertMessage("Insufficient Funds",
                    "You do not have enough money to buy more fuel!");
        }
    }

    @FXML
    void increaseHullStrength() {
        int maxHull = player.getShip().getMaxHullStrength();
        int currHull = player.getShip().getHullStrength();
        double damageFraction = (maxHull - currHull) / (double) maxHull;
        int repairAmount = (damageFraction >= .10)
                ? (maxHull / 10) : (maxHull - currHull);
        repairHull(repairAmount);
    }

    @FXML
    void increaseToMaxHullStrength() {
        int maxStrength = player.getShip().getMaxHullStrength();
        int currStrength = player.getShip().getHullStrength();
        int hullDifference = maxStrength - currStrength;
        repairHull(hullDifference);
    }

    /**
     * Repairs hull by desired amount.
     *
     * @param hullAmount amount to repair by
     */
    private void repairHull(int hullAmount) {
        try {
            int repairCost = player.getShip().getType().repairCost();

            //Update Player's Wallet and Ship's Hull Strength
            int currStrength = player.getShip().getHullStrength();
            player.getWallet().remove(hullAmount * repairCost); //This can throw an InsufficientFundsException
            player.getShip().setHullStrength(currStrength + hullAmount);

            //Update ShipYardScreen
            int amount = player.getWallet().getCredits();
            walletAmt.setText("Wallet: ₪" + amount);
            updateHullBar();
            setHullButtons();
        } catch (InsufficientFundsException e) {
            mainControl.displayAlertMessage("Insufficient Funds", "You do not have enough money to buy more fuel!");
        }
    }

    @FXML
    protected void goToShipMarketScreen() {
        mainControl.goToShipMarket();
    }

    @FXML
    protected void goBackToHomeScreen() {
        mainControl.goToHomeScreen(player, player.getLocation());
    }

    @FXML
    protected void goToEquipmentScreen() {
        mainControl.goToEquipmentMarket();
    }

    @FXML
    protected void buyEscapePod() {
        String escapePodMessage
                = "Are you sure you want to buy an escape pod for ₪2000?";
        boolean response = mainControl.displayYesNoConfirmation(
                "Ship Purchase Confirmation", null, escapePodMessage);
        if (response) {
            player.getShip().setEscapePod();
            mainControl.displayAlertMessage("Transaction Successful",
                    "You successfully bought a new escape pod!");
            updateEscapePodText();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
