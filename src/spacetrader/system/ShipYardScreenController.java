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
import spacetrader.ships.FuelTank;
import spacetrader.ships.ShipType;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class ShipYardScreenController extends SceneController implements Initializable {

    @FXML private Label walletAmt;
    @FXML private ProgressBar fuelProgress;
    @FXML private Button fuelInc;
    @FXML private Button fillTank;
    @FXML private ProgressBar hullProgress;
    @FXML private Button fix10;
    @FXML private Button repairAll;
    @FXML private Label numShips;
    @FXML private Button saleShips;
    @FXML private Button backButton;
    @FXML private Label escapePodText;
    @FXML private Button buyEscapePodButton;
    
    @FXML private Label fuelAmount;
    @FXML private Label hullAmount;
    
    private Player player;
    
    
       public void setUpShipYardScreen(Player player) {
        this.player = player;
        int amount = player.getWallet().getCredits();
        walletAmt.setText("Wallet: ₪" + amount);
        updateFuel();
        updateHull();
        setFuelButtons();
        setHullButtons();
        if (player.getCredits() < 2000) {
             escapePodText.setText("You need at least ₪2000 to buy an escape pod");
             buyEscapePodButton.setDisable(true);
        } else {
            escapePodText.setText("An escape pod is for sale!");
        }
        
        if (player.getLocation().getLevel().ordinal() < ShipType.FLEA.minTechLevel().ordinal()) {
            numShips.setText("This planet's tech level is too low.");
            saleShips.setDisable(true);
        } else {
            numShips.setText("Ships are for sale!");
        }
        
        fuelAmount.setText(player.getShip().getTank().getFuelAmount() + "/" + player.getShip().getTank().getMaxFuel());
        hullAmount.setText((100.0 * player.getShip().getHullStrength() / (double) player.getShip().getMaxHullStrength()) + "%");
    }
    
    private void updateFuel() {
        int currFuel = player.getShip().getTank().getFuelAmount();
        int maxFuel = player.getShip().getTank().getMaxFuel();
        fuelProgress.setProgress((double) currFuel / (double) maxFuel);
        fuelAmount.setText(player.getShip().getTank().getFuelAmount() + "/" + player.getShip().getTank().getMaxFuel());
    }
    
    private void setFuelButtons() {
        int currFuel = player.getShip().getTank().getFuelAmount();
        int maxFuel = player.getShip().getTank().getMaxFuel();
        if (currFuel == maxFuel) {
            fuelInc.setText("Fuel tank is full");
            fillTank.setText("Fuel tank is full");
            fuelInc.setDisable(true);
            fillTank.setDisable(true);
        } else if ((maxFuel - currFuel) == 1) {
            fuelInc.setText("Add 1 fuel: ₪" + player.getShip().getType().fuelCost());
            fillTank.setText("Fill entire tank: ₪" + ((maxFuel - currFuel) * player.getShip().getType().fuelCost()));
        } else {
            fuelInc.setText("Add 2 fuel: ₪" + (2 * player.getShip().getType().fuelCost()));
            fillTank.setText("Fill entire tank: ₪" + ((maxFuel - currFuel) * player.getShip().getType().fuelCost()));
        }
    }
    
    private void updateHull() {
        int currStrength = player.getShip().getHullStrength();
        int maxStrength = player.getShip().getMaxHullStrength();
        hullProgress.setProgress((double) currStrength / (double) maxStrength);
        hullAmount.setText((100.0 * player.getShip().getHullStrength() / (double) player.getShip().getMaxHullStrength()) + "%");
    }
    
    private void setHullButtons() {
        int currStrength = player.getShip().getHullStrength();
        int maxStrength = player.getShip().getMaxHullStrength();
        if (currStrength == maxStrength) {
            fix10.setText("No damage");
            repairAll.setText("No damage");
            fix10.setDisable(true);
            repairAll.setDisable(true);
        } else {
            int tenPercent = maxStrength / 10;
            fix10.setText("Fix hull 10%: ₪" + (player.getShip().getType().repairCost() * tenPercent));
            repairAll.setText("Repair all damage: ₪" + player.getShip().getType().repairCost());
        }
    }
    @FXML void increaseFuel() {
        if (fuelInc.getText().startsWith("Add 1 fuel")) {
            player.getShip().getTank().addFuel(1);
            player.getWallet().remove(player.getShip().getType().fuelCost());
        }
        player.getShip().getTank().addFuel(2);
        player.getWallet().remove(2 * player.getShip().getType().fuelCost());
        int amount = player.getWallet().getCredits();
        walletAmt.setText("Wallet: ₪" + amount);
        setFuelButtons();
        updateFuel();
    }
    
    @FXML void increaseToMaxFuel() {
        FuelTank tank = player.getShip().getTank();
        int fuelDiff = tank.getMaxFuel() - tank.getFuelAmount();
        tank.addFuel(fuelDiff);
        player.getWallet()
                .remove(fuelDiff * player.getShip().getType().fuelCost());
        int amount = player.getWallet().getCredits();
        walletAmt.setText("Wallet: ₪" + amount);
        setFuelButtons();
        updateFuel();
    }
    
    @FXML void increaseHullStrength() {
        int maxStrength = player.getShip().getMaxHullStrength();
        int tenPercent = maxStrength / 10;
        int currStrength = player.getShip().getHullStrength();
        player.getShip().setHullStrength(currStrength + tenPercent);
        player.getWallet()
                .remove((player.getShip().getType().repairCost() * tenPercent));
        int amount = player.getWallet().getCredits();
        walletAmt.setText("Wallet: ₪" + amount);
        setHullButtons();
        updateHull();
    }
    
    @FXML void increaseToMaxHullStrength() {
        int maxStrength = player.getShip().getMaxHullStrength();
        int currStrength = player.getShip().getHullStrength();
        int fuelDiff = maxStrength - currStrength;
        int percent = fuelDiff / 10;
        player.getShip().setHullStrength(maxStrength);
        player.getWallet()
                .remove((player.getShip().getType().repairCost() * percent));
        int amount = player.getWallet().getCredits();
        walletAmt.setText("Wallet: ₪" + amount);
        setHullButtons();
        updateHull();
    }
    
    @FXML protected void goToShipMarketScreen() {
        mainControl.goToShipMarket();
    }
    
    @FXML protected void goBackToHomeScreen() {
        mainControl.goToHomeScreen(player.getLocation());
    }
    @FXML protected void buyEscapePod() {
        player.getShip().setEscapePod();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    
}