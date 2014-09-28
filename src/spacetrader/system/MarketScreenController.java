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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import spacetrader.Planet;
import spacetrader.Player;
import spacetrader.commerce.TradeGood;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class MarketScreenController implements Initializable {
    private int buyQuantity1;
    private int buyQuantity2;
    private int buyQuantity3;
    private int buyQuantity1Price = 100 ;
    private int buyQuantity2Price = 200;
    private int buyQuantity3Price = 300;
    private int sellQuantity1;
    private int sellQuantity2;
    private int sellQuantity3;
    private int sellQuantityPrice1 = 50 ;
    private int sellQuantityPrice2= 100;
    private int sellQuantityPrice3 = 350;
    private int netBalance = 0;
    private Player player;
    private Planet planet;
    private MainController mainControl;
    
    @FXML private Label priceWaterSell;
    @FXML private Label priceFursSell;
    @FXML private Label priceFoodSell;
    @FXML private Label priceOreSell;
    @FXML private Label priceGamesSell;
    @FXML private Label priceFirearmsSell;
    @FXML private Label priceMedicineSell;
    @FXML private Label priceMachinesSell;
    @FXML private Label priceNarcoticsSell;
    @FXML private Label priceRobotsSell;
    @FXML private Label priceWaterBuy;
    @FXML private Label priceFursBuy;
    @FXML private Label priceFoodBuy;
    @FXML private Label priceOreBuy;
    @FXML private Label priceGamesBuy;
    @FXML private Label priceFirearmsBuy;
    @FXML private Label priceMedicineBuy;
    @FXML private Label priceMachinesBuy;
    @FXML private Label priceNarcoticsBuy;
    @FXML private Label priceRobotsBuy;
    @FXML private Text netBalanceText;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        netBalanceText = new Text();
        netBalanceText.setText("0 Credits");
        //for every item in the planet's market's stock, there should be 
        //one corresponding line in the marketscreen view.
    }
    
    @FXML protected void goToFirstScreen(ActionEvent event) {
        mainControl.goToFirstScreen();
    }
    
    public void setModel(Planet planet, Player player) {
         this.player = player;
         this.planet = planet;
     }
    
    public void setMainControl(MainController mainControl) {
        this.mainControl = mainControl;
    }
    
    @FXML protected void setSellPrices() {
        for (TradeGood good : TradeGood.values()) {
        switch (good) {
            case WATER:
                priceWaterSell.setText(planet.getMarket().getSellPrices().get(TradeGood.WATER).toString());
                break;
            case FURS:
                priceFursSell.setText(planet.getMarket().getSellPrices().get(TradeGood.FURS).toString());
                break;
            case FOOD:
                priceFoodSell.setText(planet.getMarket().getSellPrices().get(TradeGood.FOOD).toString());
                break;
            case ORE:
                priceOreSell.setText(planet.getMarket().getSellPrices().get(TradeGood.ORE).toString());
                break;
            case GAMES:
                priceGamesSell.setText(planet.getMarket().getSellPrices().get(TradeGood.GAMES).toString());
                break;
            case FIREARMS:
                priceFirearmsSell.setText(planet.getMarket().getSellPrices().get(TradeGood.FIREARMS).toString());
                break;
            case MEDICINE:
                priceMedicineSell.setText(planet.getMarket().getSellPrices().get(TradeGood.MEDICINE).toString());
                break;
            case MACHINES:
                priceMachinesSell.setText(planet.getMarket().getSellPrices().get(TradeGood.MACHINES).toString());
                break;
            case NARCOTICS:
                priceNarcoticsSell.setText(planet.getMarket().getSellPrices().get(TradeGood.NARCOTICS).toString());
                break;      
            case ROBOTS:
                priceRobotsSell.setText(planet.getMarket().getSellPrices().get(TradeGood.ROBOTS).toString());
                break;
            }
        }
    }
    
    @FXML protected void setBuyPrices() {
        for (TradeGood good : TradeGood.values()) {
        switch (good) {
            case WATER:
                priceWaterBuy.setText(planet.getMarket().getBuyPrices().get(TradeGood.WATER).toString());
                break;
            case FURS:
                priceFursBuy.setText(planet.getMarket().getBuyPrices().get(TradeGood.FURS).toString());
                break;
            case FOOD:
                priceFoodBuy.setText(planet.getMarket().getBuyPrices().get(TradeGood.FOOD).toString());
                break;
            case ORE:
                priceOreBuy.setText(planet.getMarket().getBuyPrices().get(TradeGood.ORE).toString());
                break;
            case GAMES:
                priceGamesBuy.setText(planet.getMarket().getBuyPrices().get(TradeGood.GAMES).toString());
                break;
            case FIREARMS:
                priceFirearmsBuy.setText(planet.getMarket().getBuyPrices().get(TradeGood.FIREARMS).toString());
                break;
            case MEDICINE:
                priceMedicineBuy.setText(planet.getMarket().getBuyPrices().get(TradeGood.MEDICINE).toString());
                break;
            case MACHINES:
                priceMachinesBuy.setText(planet.getMarket().getBuyPrices().get(TradeGood.MACHINES).toString());
                break;
            case NARCOTICS:
                priceNarcoticsBuy.setText(planet.getMarket().getBuyPrices().get(TradeGood.NARCOTICS).toString());
                break;      
            case ROBOTS:
                priceRobotsBuy.setText(planet.getMarket().getBuyPrices().get(TradeGood.ROBOTS).toString());
                break;
            }
        }
    }
    
    private void updateNetBalance() {
        
    }
    
    @FXML protected void increaseBuyQuantity1(ActionEvent event) {
        buyQuantity1++;
        buyQuantity1Price *= buyQuantity1;
        netBalance = (buyQuantity1Price + buyQuantity2Price + 
        buyQuantity3Price) + (sellQuantityPrice1 - 
        sellQuantityPrice2 - sellQuantityPrice3);
        netBalanceText.setText(netBalance + " credits");
    }
    @FXML protected void decreaseBuyQuantity1(ActionEvent event) {
        if(buyQuantity1 > 0) {
            buyQuantity1--;
            buyQuantity1Price *= buyQuantity1;
            netBalance = (buyQuantity1Price + buyQuantity2Price + 
            buyQuantity3Price) + (sellQuantityPrice1 - 
            sellQuantityPrice2 - sellQuantityPrice3);
            netBalanceText.setText(netBalance + " credits");
        }
    }
    @FXML protected void increaseBuyQuantity2(ActionEvent event) {
        buyQuantity2++;
        buyQuantity2Price *= buyQuantity2;
        netBalance = (buyQuantity1Price + buyQuantity2Price + 
        buyQuantity3Price) + (sellQuantityPrice1 - 
        sellQuantityPrice2 - sellQuantityPrice3);
        netBalanceText.setText(netBalance + " credits");
    }
    @FXML protected void decreaseBuyQuantity2(ActionEvent event) {
        if(buyQuantity2 > 0) {
            buyQuantity2--;
            buyQuantity2Price *= buyQuantity2;
            netBalance = (buyQuantity1Price + buyQuantity2Price + 
            buyQuantity3Price) + (sellQuantityPrice1 - 
            sellQuantityPrice2 - sellQuantityPrice3);
            netBalanceText.setText(netBalance + " credits");
        }
    }
    @FXML protected void increaseBuyQuantity3(ActionEvent event) {
        buyQuantity3++;
        buyQuantity3Price *= buyQuantity3;
        netBalance = (buyQuantity1Price + buyQuantity2Price + 
        buyQuantity3Price) + (sellQuantityPrice1 - 
        sellQuantityPrice2 - sellQuantityPrice3);
        netBalanceText.setText(netBalance + " credits");
    }
    @FXML protected void decreaseBuyQuantity(ActionEvent event) {
        if(buyQuantity3 > 0) {
            buyQuantity3--;
            buyQuantity3Price *= buyQuantity3;
            netBalance = (buyQuantity1Price + buyQuantity2Price + 
            buyQuantity3Price) + (sellQuantityPrice1 - 
            sellQuantityPrice2 - sellQuantityPrice3);
            netBalanceText.setText(netBalance + " credits");
        }
    }
    @FXML protected void increaseSellQuantity1(ActionEvent event) {
        sellQuantity1++;
        sellQuantityPrice1 *= sellQuantity1;
        netBalance = (buyQuantity1Price + buyQuantity2Price + 
        buyQuantity3Price) + (sellQuantityPrice1 - 
        sellQuantityPrice2 - sellQuantityPrice3);
        netBalanceText.setText(netBalance + " credits");
    }
    @FXML protected void decreaseSellQuantity1(ActionEvent event) {
        if (sellQuantity1 > 0) {
            sellQuantity1--;    
            sellQuantityPrice1 *= sellQuantity1;
            netBalance = (buyQuantity1Price + buyQuantity2Price + 
                buyQuantity3Price) + (sellQuantityPrice1 - 
                sellQuantityPrice2 - sellQuantityPrice3);
            netBalanceText.setText("" + netBalance);
        }
    }
    @FXML protected void increaseSellQuantity2(ActionEvent event) {
        sellQuantity1++;
        sellQuantityPrice2 *= sellQuantity2;
        netBalance = (buyQuantity1Price + buyQuantity2Price + 
                buyQuantity3Price) + (sellQuantityPrice1 - 
                sellQuantityPrice2 - sellQuantityPrice3);
        netBalanceText.setText("" + netBalance);
        }
    @FXML protected void decreaseSellQuantity2(ActionEvent event) {
        if (sellQuantity2 > 0) {
            sellQuantity2--;    
            sellQuantityPrice2 *= sellQuantity2;
            netBalance = (buyQuantity1Price + buyQuantity2Price + 
                buyQuantity3Price) + (sellQuantityPrice1 - 
                sellQuantityPrice2 - sellQuantityPrice3);
            netBalanceText.setText("" + netBalance);
        }
    }
    @FXML protected void increaseSellQuantity3(ActionEvent event) {
        sellQuantity3++;
        sellQuantityPrice3 *= sellQuantity3;
        netBalance = (buyQuantity1Price + buyQuantity2Price + 
                buyQuantity3Price) + (sellQuantityPrice1 - 
                sellQuantityPrice2 - sellQuantityPrice3);
        netBalanceText.setText("" + netBalance);
    }
    @FXML protected void decreaseSellQuantity3(ActionEvent event) {
        if (sellQuantity3 > 0) {
            sellQuantity3--;    
            sellQuantityPrice3 *= sellQuantity3;
            netBalance = (buyQuantity1Price + buyQuantity2Price + 
                buyQuantity3Price) + (sellQuantityPrice1 - 
                sellQuantityPrice2 - sellQuantityPrice3);
            netBalanceText.setText("" + netBalance);
        }
    }
        
    @FXML protected void buyButtonPressed(ActionEvent event) {
        // buy the specified items
    }
}
    

    

