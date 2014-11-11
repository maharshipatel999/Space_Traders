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
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import spacetrader.Player;
import spacetrader.exceptions.InsufficientFundsException;
import spacetrader.planets.Planet;
import spacetrader.planets.TechLevel;
import spacetrader.ships.FuelTank;
import spacetrader.ships.ShipType;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class ShipYardScreenController extends SceneController implements Initializable {

    @FXML private Label walletAmt;
    
    @FXML private Label fuelAmountLabel;
    @FXML private ProgressBar fuelProgress;
    @FXML private Button incFuelButton;
    @FXML private Button maxFuelButton;
    @FXML private Label fuelStatus;
    
    @FXML private Label hullAmountLabel;
    @FXML private ProgressBar hullProgress;
    @FXML private Button incHullButton;
    @FXML private Button maxHullButton;
    @FXML private Label hullStatus;

    @FXML private Label shipDesc;
    @FXML private Label equipmentDesc;
    @FXML private Button saleShips;
    @FXML private Button saleEquipment;
    @FXML private Label escapePodText;
    @FXML private Button buyEscapePodButton;
    
    @FXML private Button backButton;

    private Player player;
    private Planet planet;
    
       public void setUpShipYardScreen(Player player) {
        this.player = player;
        this.planet = player.getLocation();
        int amount = player.getWallet().getCredits();
        
        walletAmt.setText("Wallet: ₪" + amount);
        updateFuelBar();
        updateHullBar();
        setFuelButtons();
        setHullButtons();
        
        maxFuelButton.requestFocus(); //Makes it the default button on the screen.
        
        //Determine if this planet sells ships, equipment, or escape pods.
        if (player.getLocation().getLevel().ordinal() < ShipType.FLEA.minTechLevel().ordinal()) {
            shipDesc.setText("There are no ships for sale.");
            saleShips.setDisable(true);
        } else {
            shipDesc.setText("There are several ships currently avaible for purchase!");
        }
        
        if (planet.getLevel().equals(TechLevel.AGRICULTURE) || planet.getLevel().equals(TechLevel.PRE_AGRICULTURE)) {
            equipmentDesc.setText("This planet does not sell ship equipment.");
            saleEquipment.setDisable(true);
        } else {
            equipmentDesc.setText("There are several equipment available for purchase!");
        }
        updateEscapePodText();
    }

    private void updateEscapePodText() {
        if (player.getShip().hasEscapePod()) {
            escapePodText.setText("Your ship already has an escape pod.");
            buyEscapePodButton.setDisable(true);
        } else if (player.getCredits() < 2000) {
            escapePodText.setText("You need at least ₪2000 to buy an escape pod");
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
            
            maxFuelButton.setText("Fill Entire Tank: ₪" + ((maxFuel - currFuel) * fuelCost));
            int refillAmt = (maxFuel - currFuel == 1) ? 1 : 2;
            incFuelButton.setText("Buy " + refillAmt + " Unit" + (refillAmt > 1 ? "s" : "") + " of Fuel: ₪" + refillAmt * fuelCost);
        }
    }
    
    /**
     * Updates the buttons for fixing the hull.
     */
    private void setHullButtons() {
        int currHull = player.getShip().getHullStrength();
        int maxHull = player.getShip().getMaxHullStrength();
        int repairCost = player.getShip().getType().repairCost();
        
        //Disable the player's ability to repair the ship if their hull is completely fixed.
        if (currHull == maxHull) {
            hullStatus.setVisible(true);
            hullStatus.setText("Your ship's hull strength is completely fixed!");
            incHullButton.setVisible(false);
            maxHullButton.setVisible(false);
        } else {
            fuelStatus.setVisible(false);
            incFuelButton.setVisible(true);
            maxFuelButton.setVisible(true);
            
            maxHullButton.setText("Repair All Damage: ₪" + ((maxHull - currHull) * repairCost));
            
            double damageFraction = (maxHull - currHull) / (double) maxHull;
            int repairAmount = (damageFraction >= .10) ? (maxHull / 10) : (maxHull - currHull);
            incHullButton.setText("Repair " + ((repairAmount / maxHull) * 100) + "% of Hull: ₪" + (repairAmount * repairCost));
        }
    }
    
    /**
     * Adds 1 or 2 more fuel to the player's tank.
     */
    @FXML void increaseFuel() {
        int fuelAmount = (incFuelButton.getText().contains("1")) ? 1 : 2;
        buyFuel(fuelAmount);
    }
    
    /**
     * Completely fills the player's tank.
     */
    @FXML void increaseToMaxFuel() {
        FuelTank tank = player.getShip().getTank();
        int fuelDiff = tank.getMaxFuel() - tank.getFuelAmount();
        buyFuel(fuelDiff);
    }
    
    /**
     * Adds more fuel to the player's tank and removes the appropriate amount
     * of money from the player's wallet. If the player does not have enough money,
     * it will display an alert message.
     */
    private void buyFuel(int fuelAmount) {
        try {
            int fuelCost = player.getShip().getType().repairCost();

            //Update Player's Wallet and Ship's Fuel Tank
            player.getWallet().remove(fuelAmount * fuelCost); //This can throw an InsufficientFundsException
            player.getShip().getTank().addFuel(fuelAmount);
            
            //Update ShipYardScreen
            int amount = player.getWallet().getCredits();
            walletAmt.setText("Wallet: ₪" + amount);
            updateFuelBar();
            setFuelButtons();
        } catch (InsufficientFundsException e) {
            mainControl.displayAlertMessage("Insufficient Funds", "You do not have enough money to buy more fuel!");
        }
    }
    
    @FXML void increaseHullStrength() {
        int maxHull = player.getShip().getMaxHullStrength();
        int currHull = player.getShip().getHullStrength();
        double damageFraction = (maxHull - currHull) / (double) maxHull;
        int repairAmount = (damageFraction >= .10) ? (maxHull / 10) : (maxHull - currHull);
        repairHull(repairAmount);
    }

    @FXML
    void increaseToMaxHullStrength() {
        int maxStrength = player.getShip().getMaxHullStrength();
        int currStrength = player.getShip().getHullStrength();
        int hullDifference = maxStrength - currStrength;
        repairHull(hullDifference);
    }
    
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
        if (planet.getLevel().equals(TechLevel.AGRICULTURE) || planet.getLevel().equals(TechLevel.PRE_AGRICULTURE)) {
            mainControl.displayAlertMessage("UNDER CONSTRUCTION", "Equipment Market Unavailable. Planet has not yet achieved a high enough tech level to offer Equipment Goods.");
        } else {
            mainControl.goToEquipmentMarket();
        }
    }
    
    @FXML protected void buyEscapePod() {
         String escapePodMessage = "Are you sure you want to buy an escape pod for ₪2000?";
        Action response = mainControl.displayYesNoComfirmation("Ship Purchase Confirmation", null, escapePodMessage);
            if (response == Dialog.Actions.YES) {
                player.getShip().setEscapePod();
                mainControl.displayAlertMessage("Transaction Successful", "You successfully bought a new escape pod!");
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
