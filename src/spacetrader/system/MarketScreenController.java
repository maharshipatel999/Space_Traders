/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.system;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import spacetrader.planets.Planet;
import spacetrader.Player;
import spacetrader.commerce.Cargo;
import spacetrader.commerce.Market;
import spacetrader.commerce.TradeGood;
import spacetrader.commerce.Transaction;
import spacetrader.exceptions.CargoIsFullException;
import spacetrader.exceptions.DepletedInventoryException;
import spacetrader.exceptions.InsufficientFundsException;
import spacetrader.exceptions.NegativeQuantityException;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class MarketScreenController extends SceneController implements Initializable {

    @FXML
    private Label playerFunds, moneyRemaining, cargoSlots, priceEvent;
    @FXML
    private Label totalPurchase, totalSale;
    @FXML
    private GridPane buyGrid, sellGrid;
    @FXML
    private Label stock0, stock1, stock2, stock3, stock4,
            stock5, stock6, stock7, stock8, stock9;
    @FXML
    private Label goodBuy0, goodBuy1, goodBuy2, goodBuy3, goodBuy4,
            goodBuy5, goodBuy6, goodBuy7, goodBuy8, goodBuy9;
    @FXML
    private Label priceBuy0, priceBuy1, priceBuy2, priceBuy3, priceBuy4,
            priceBuy5, priceBuy6, priceBuy7, priceBuy8, priceBuy9;
    @FXML
    private TextField numBuy0, numBuy1, numBuy2, numBuy3, numBuy4,
            numBuy5, numBuy6, numBuy7, numBuy8, numBuy9;
    @FXML
    private Label cost0, cost1, cost2, cost3, cost4,
            cost5, cost6, cost7, cost8, cost9;
    @FXML
    private Label inventory0, inventory1, inventory2, inventory3, inventory4,
            inventory5, inventory6, inventory7, inventory8, inventory9;
    @FXML
    private Label goodSell0, goodSell1, goodSell2, goodSell3, goodSell4,
            goodSell5, goodSell6, goodSell7, goodSell8, goodSell9;
    @FXML
    private Label priceSell0, priceSell1, priceSell2, priceSell3, priceSell4,
            priceSell5, priceSell6, priceSell7, priceSell8, priceSell9;
    @FXML
    private TextField numSell0, numSell1, numSell2, numSell3, numSell4,
            numSell5, numSell6, numSell7, numSell8, numSell9;
    @FXML
    private Label revenue0, revenue1, revenue2, revenue3, revenue4,
            revenue5, revenue6, revenue7, revenue8, revenue9;

    private Label[] stocks, goodBuys, priceBuys, costs,
            inventorys, goodSells, priceSells, revenues;
    private TextField[] numBuys, numSells;

    private Transaction cashier;
    private List<TradeGood> buyGoods, sellGoods;

    private enum Change {

        INCREASE, DECREASE, CUSTOM
    }
    private boolean viewIsInitialized = false;

    private Player player;
    private Market market;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //alertText.setFill(Color.TRANSPARENT);
    }

    /**
     * Sets up the market screen for this planet. Shows planet information,
     * resource, and price increase event. Enables user to buy/sell goods on
     * this planet.
     *
     * @param planet the planet who's market we are at
     * @param player the user, who may purchase/sell goods at this market
     */
    public void setUpMarketScreen(Planet planet, Player player) {
        this.player = player;
        this.market = planet.getMarket();
        cashier = new Transaction(market, player.getCargo(), player);

        stocks = new Label[]{stock0, stock1, stock2, stock3, stock4,
            stock5, stock6, stock7, stock8, stock9};
        goodBuys = new Label[]{goodBuy0, goodBuy1, goodBuy2, goodBuy3, goodBuy4,
            goodBuy5, goodBuy6, goodBuy7, goodBuy8, goodBuy9};
        priceBuys = new Label[]{priceBuy0, priceBuy1, priceBuy2, priceBuy3, priceBuy4,
            priceBuy5, priceBuy6, priceBuy7, priceBuy8, priceBuy9};
        numBuys = new TextField[]{numBuy0, numBuy1, numBuy2, numBuy3, numBuy4,
            numBuy5, numBuy6, numBuy7, numBuy8, numBuy9};
        costs = new Label[]{cost0, cost1, cost2, cost3, cost4,
            cost5, cost6, cost7, cost8, cost9};
        inventorys = new Label[]{inventory0, inventory1, inventory2, inventory3, inventory4,
            inventory5, inventory6, inventory7, inventory8, inventory9};
        goodSells = new Label[]{goodSell0, goodSell1, goodSell2, goodSell3, goodSell4,
            goodSell5, goodSell6, goodSell7, goodSell8, goodSell9};
        priceSells = new Label[]{priceSell0, priceSell1, priceSell2, priceSell3, priceSell4,
            priceSell5, priceSell6, priceSell7, priceSell8, priceSell9};
        numSells = new TextField[]{numSell0, numSell1, numSell2, numSell3, numSell4,
            numSell5, numSell6, numSell7, numSell8, numSell9};
        revenues = new Label[]{revenue0, revenue1, revenue2, revenue3, revenue4,
            revenue5, revenue6, revenue7, revenue8, revenue9};

        buyGoods = market.getStock().getTradeGoods();
        sellGoods = player.getCargo().getTradeGoods();

        //Remove appropriate amount of rows from grid
        if (buyGoods.size() < 10) {
            deleteGridRows(buyGoods.size(), buyGrid);
        }
        if (sellGoods.size() < 10) {
            deleteGridRows(sellGoods.size(), sellGrid);
        }

        setUpPanels(player.getCargo());
        playerFunds.setText("₪" + player.getCredits());
        cargoSlots.setText(player.getCargo().getCount() + "/" + player.getCargo().getMaxCapacity());
        priceEvent.setText(planet.getPriceIncEvent().desc());
        updateNetBalance();
        viewIsInitialized = true;
        checkForDisabling(buyGrid);

    }

    /**
     * Deletes rows of the grid so that there is only the specified number
     * remaining
     *
     * @param numRows how many rows not to delete
     */
    private void deleteGridRows(int numRows, GridPane grid) {
        ObservableList<Node> children = grid.getChildren();
        ArrayList<Node> toRemove = new ArrayList<>();
        for (Node node : children) {
            Integer row = GridPane.getRowIndex(node);
            if (row != null && row > numRows) {
                toRemove.add(node);
            }
        }
        children.removeAll(toRemove);
    }

    /**
     * Shows the price of each TradeGood
     *
     * @param cargo the player's cargo, which contains his/her goods
     */
    private void setUpPanels(Cargo cargo) {
        for (int i = 0; i < buyGoods.size(); i++) {
            TradeGood good = buyGoods.get(i);

            stocks[i].setText("" + market.getStock().getQuantity(good));
            goodBuys[i].setText("" + good.toString());
            priceBuys[i].setText("₪" + market.getBuyPrice(good));
        }

        for (int i = 0; i < sellGoods.size(); i++) {
            TradeGood good = sellGoods.get(i);

            inventorys[i].setText("" + cargo.getQuantity(good));
            goodSells[i].setText("" + good.toString());
            priceSells[i].setText("₪" + market.getSellPrice(good));
        }
    }

    /**
     * Increases the quantity to be bought
     *
     * @param event mouse click on the "^" button (on Buy screen)
     */
    @FXML
    protected void increaseBuyQuantity(ActionEvent event) {
        Integer row = GridPane.getRowIndex((Node) event.getSource());
        if (row != null) {
            modifyBuyQuantity(row - 1, Change.INCREASE);
        }
    }

    /**
     * Decreases the quantity to be bought
     *
     * @param event mouse click on the down arrow head button (on Buy screen)
     */
    @FXML
    protected void decreaseBuyQuantity(ActionEvent event) {
        Integer row = GridPane.getRowIndex((Node) event.getSource());
        if (row != null) {
            modifyBuyQuantity(row - 1, Change.DECREASE);
        }
    }

    /**
     * Increase the quantity to sell
     *
     * @param event mouse click on the "^" button (on Sell screen)
     */
    @FXML
    protected void increaseSellQuantity(ActionEvent event) {
        Integer row = GridPane.getRowIndex((Node) event.getSource());
        if (row != null) {
            modifySellQuantity(row - 1, Change.INCREASE);
        }
    }

    /**
     * Decreases the quantity to be sold
     *
     * @param event mouse click on the down arrow head button (on Sell screen)
     */
    @FXML
    protected void decreaseSellQuantity(ActionEvent event) {
        Integer row = GridPane.getRowIndex((Node) event.getSource());
        if (row != null) {
            modifySellQuantity(row - 1, Change.DECREASE);
        }
    }

    private void modifyBuyQuantity(int index, Change type) {
        TradeGood good = buyGoods.get(index);
        int oldQuantity = cashier.getQuantityBought(good);
        try {
            int newQuantity;
            if (type == Change.INCREASE) {
                newQuantity = oldQuantity + 1;
            } else if (type == Change.DECREASE) {
                newQuantity = oldQuantity - 1;
            } else {
                newQuantity = Integer.parseInt(numBuys[index].getText());
            }
            cashier.changeBuyQuantity(good, newQuantity);
            updateBuyText(index);
            updateNetBalance();
            checkForDisabling(buyGrid);
        } catch (NumberFormatException e) {
            displayAlert("Please input a number!");
        } catch (DepletedInventoryException e) {
            displayAlert("We are sold out of that item! Please try another selection.");
        } catch (CargoIsFullException e) {
            displayAlert("Your cargo is full! Please open up more slots.");
        } catch (InsufficientFundsException e) {
            displayAlert("You do not have enough money to buy this item!");
        } catch (NegativeQuantityException e) {
            displayAlert("Quantities must be greater than zero!");
        }
    }

    private void modifySellQuantity(int index, Change type) {
        TradeGood good = sellGoods.get(index);
        int oldQuantity = cashier.getQuantitySold(good);
        try {
            int newQuantity;
            if (type == Change.INCREASE) {
                newQuantity = oldQuantity + 1;
            } else if (type == Change.DECREASE) {
                newQuantity = oldQuantity - 1;
            } else {
                newQuantity = Integer.parseInt(numSells[index].getText());
            }
            cashier.changeSellQuantity(good, newQuantity);
            updateSellText(index);
            updateNetBalance();
            checkForDisabling(sellGrid);
        } catch (NumberFormatException e) {
            displayAlert("Please input a number!");
        } catch (DepletedInventoryException e) {
            displayAlert("You have no more of that item to sell! Please try another selection.");
        } catch (NegativeQuantityException e) {
            displayAlert("Quantities must be greater than zero!");
        }
    }

    /**
     * Updates the transaction with the latest selection of trade goods. Shows
     * how many sprints will be subtracted from the player's total if the
     * transaction occurs.
     *
     * @param index the index of the good we are calculating into the
     * transaction
     */
    private void updateBuyText(int index) {
        TradeGood good = buyGoods.get(index);
        int quantity = cashier.getQuantityBought(good);
        int oldStock = market.getStock().getQuantity(good);
        stocks[index].setText("" + (oldStock - quantity));
        numBuys[index].setText("" + quantity);
        costs[index].setText("₪" + (quantity * market.getBuyPrice(good)));
        totalPurchase.setText("– ₪" + cashier.getTotalCost());
    }

    /**
     * Updates the transaction with the latest selection of trade goods. Shows
     * how many sprints will be added to the player's total if the transaction
     * occurs.
     *
     * @param index the index of the good we are calculating into the
     * transaction
     */
    private void updateSellText(int index) {
        TradeGood good = sellGoods.get(index);
        int quantity = cashier.getQuantitySold(good);
        int oldInventory = player.getCargo().getQuantity(good);
        inventorys[index].setText("" + (oldInventory - quantity));
        numSells[index].setText("" + quantity);
        revenues[index].setText("₪" + (quantity * market.getBuyPrice(good)));
        totalSale.setText("+ ₪" + cashier.getTotalRevenue());
    }

    /**
     * Updates the player's net balance.
     */
    private void updateNetBalance() {
        moneyRemaining.setText(cashier.getRemainingBalance() + " credits");
        cargoSlots.setText(player.getCargo().getCount() + "/" + player.getCargo().getMaxCapacity());

    }

    /**
     * Displays an alert for the user.
     *
     * @param message the message to be displayed
     */
    private void displayAlert(String message) {
        mainControl.displayWarningMessage(null, "Market Warning!", message);
    }

    /**
     * Checks to see if any buttons need to be disabled.
     */
    private void checkForDisabling(GridPane grid) {
        final int DEC_COLUMN = 4;
        final int INC_COLUMN = 5;
        Tooltip moneyAlert = new Tooltip("You do not have enough money to buy this item");
        Tooltip stockAlert = new Tooltip("This item is out of stock");
        Tooltip cargoAlert = new Tooltip("You have no more of this item to sell");
        Tooltip negativeAlert = new Tooltip("The quantity cannot be decreased any further");

        ObservableList<Node> children = grid.getChildren();
        for (Node node : children) {
            Integer column = GridPane.getColumnIndex(node);
            Integer row = GridPane.getRowIndex(node);
            if (column != null && row != null) {
                if (column == DEC_COLUMN) {
                    int quantity;
                    if (grid == buyGrid) {
                        quantity = cashier.getQuantityBought(buyGoods.get(row - 1));
                    } else {
                        quantity = cashier.getQuantitySold(sellGoods.get(row - 1));
                    }
                    if (quantity == 0) {
                        node.setDisable(true);
                        Tooltip.install(node, negativeAlert);
                    } else {
                        node.setDisable(false);
                        Tooltip.uninstall(node, negativeAlert);
                    }
                } else if (column == INC_COLUMN) {
                    int price, quantity;
                    if (grid == buyGrid) {
                        TradeGood good = buyGoods.get(row - 1);
                        price = market.getBuyPrice(good);
                        quantity = market.getStock().getQuantity(good) - cashier.getQuantityBought(good);
                    } else {
                        TradeGood good = sellGoods.get(row - 1);
                        price = -1; //if selling, price doesn't matter
                        quantity = player.getCargo().getQuantity(good) - cashier.getQuantitySold(good);
                    }
                    if (quantity <= 0) {
                        node.setDisable(true);
                        if (grid == buyGrid) {
                            Tooltip.install(node, stockAlert);
                        } else {
                            Tooltip.install(node, cargoAlert);
                        }
                    } else if (price > cashier.getRemainingBalance()) {
                        node.setDisable(true);
                        Tooltip.install(node, moneyAlert);
                    } else {
                        node.setDisable(false);
                        Tooltip.uninstall(node, stockAlert);
                        Tooltip.uninstall(node, cargoAlert);
                        Tooltip.uninstall(node, moneyAlert);
                    }
                }
            }
        }
    }

    /**
     * Checks for disabling when the buy tab is changed
     *
     * @param e the event that causes this action to occur
     */
    @FXML
    protected void buyTabChanged(Event e) {
        if (viewIsInitialized) {
            checkForDisabling(buyGrid);
            checkForDisabling(sellGrid);
        }
    }

    /**
     * Completes the transaction and refreshes the market screen
     *
     * @param event mouse click on "Complete Transaction" button
     */
    @FXML
    protected void completeTransaction(ActionEvent event) {
        cashier.complete();
        mainControl.goToMarketScreen(market.getPlanet());
    }

    /**
     * Goes back to the home screen
     *
     * @param event mouse click on "Back" button
     */
    @FXML
    protected void goBack(ActionEvent event) {
        mainControl.goToHomeScreen(player.getLocation());
    }
}
