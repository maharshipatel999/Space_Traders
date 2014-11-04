/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.system;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import spacetrader.Player;
import spacetrader.planets.Planet;
import spacetrader.ships.FuelTank;

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
    @FXML private Button backToHomeScreen;
    
    private Player player;
    private Planet planet;
    
    public void setUpShipYardScreen(Player player) {
        this.player = player;
        this.planet = player.getLocation();
        int amount = player.getWallet().getCredits();
        walletAmt.setText("Wallet: ₪" + amount);
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
        fillTank.setText("Fill entire tank: ₪" + ((maxFuel - currFuel) * player.getShip().getType().fuelCost()));
        int currStrength = player.getShip().getHullStrength();
        int maxStrength = player.getShip().getMaxHullStrength();
        if (currStrength == maxStrength) {
            System.out.println("equal each other hull");
            fix10.setText("No damage");
            repairAll.setText("No damage");
            fix10.setDisable(true);
            repairAll.setDisable(true);
        }
        int tenPercent = maxStrength / 10;
        fix10.setText("Fix hull 10%: ₪" + (player.getShip().getType().repairCost() * tenPercent));
        repairAll.setText("Repair all damage: ₪" + player.getShip().getType().repairCost());
        numShips.setText("Ships are for sale");
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
    }
    
    @FXML void increaseToMaxFuel() {
        FuelTank tank = player.getShip().getTank();
        int fuelDiff = tank.getMaxFuel() - tank.getFuelAmount();
        tank.addFuel(fuelDiff);
        player.getWallet()
                .remove(fuelDiff * player.getShip().getType().fuelCost());
        int amount = player.getWallet().getCredits();
        walletAmt.setText("Wallet: ₪" + amount);
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
    }
    
    @FXML void increaseToMaxHullStrength() {
        int maxStrength = player.getShip().getMaxHullStrength();
        int currStrength = player.getShip().getHullStrength();
        int fuelDiff = maxStrength - currStrength;
        player.getShip().setHullStrength(maxStrength);
        player.getWallet()
                .remove((player.getShip().getType().repairCost() * fuelDiff));
        int amount = player.getWallet().getCredits();
        walletAmt.setText("Wallet: ₪" + amount);
    }
    
    @FXML protected void goToShipMarketScreen() {
        mainControl.goToShipMarket();
    }
    
    @FXML protected void goToEquipmentScreen() {
        mainControl.goToEquipmentMarket();
    }
    
    @FXML protected void goBackToHomeScreen() {
        mainControl.goToHomeScreen(planet);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    
}
