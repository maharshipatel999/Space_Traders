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
import javafx.scene.control.Button;
import spacetrader.Player;
import spacetrader.planets.Planet;
import spacetrader.ships.PlayerShip;
import spacetrader.ships.ShieldType;
import spacetrader.ships.WeaponType;
import spacetrader.ships.GadgetType;
import spacetrader.ships.Shield;
import spacetrader.ships.Weapon;
import spacetrader.ships.Gadget;
import spacetrader.planets.TechLevel;
import javafx.scene.control.RadioButton;
import spacetrader.exceptions.TooManyRadioButtonsCheckedException;
import java.util.ArrayList;

/**
 * FXML Controller class
 *
 * @author maharshipatel999
 */
public class GadgetScreenController 
    extends SceneController 
    implements Initializable {

    @FXML
    private Label weapon1Text, weapon2Text, weapon3Text,
            shield1Text, shield2Text, shield3Text, gadget1Text,
            gadget2Text, gadget3Text, funds;

    @FXML
    private RadioButton weapon1RBCurrentInventory, weapon2RBCurrentInventory,
            weapon3RBCurrentInventory, shield1RBCurrentInventory,
            shield2RBCurrentInventory, shield3RBCurrentInventory, 
            gadget1RBCurrentInventory, gadget2RBCurrentInventory, 
            gadget3RBCurrentInventory;

    @FXML
    private RadioButton pulseLaserRBBuy, beamLaserRBBuy, militaryLaserRBBuy,
            energyShieldRBBuy, reflectiveShieldRBBuy, extraCargoBaysRBBuy,
            navigatingSystemRBBuy, autoRepairSystemRBBuy, targetingSystemRBBuy,
            cloakingDeviceRBBuy;

    @FXML
    private Label pulseLaserBuy;
    @FXML
    private Label beamLaserBuy;
    @FXML
    private Label militaryLaserBuy;
    @FXML
    private Label energyShieldBuy;
    @FXML
    private Label reflectiveShieldBuy;
    @FXML
    private Label extraCargoBaysBuy;
    @FXML
    private Label navigatingSystemBuy;
    @FXML
    private Label autoRepairSystemBuy;
    @FXML
    private Label targetingSystemBuy;
    @FXML
    private Label cloakingDeviceBuy;
    @FXML
    private Label desc;

    @FXML
    private Label name;
    @FXML
    private Label type;
    @FXML
    private Label buyPrice;
    @FXML
    private Label sellPrice;
    @FXML
    private Label power;
    @FXML
    private Label charge;

    @FXML
    private Button buyButton;
    @FXML
    private Button sellButton;
    @FXML
    private Button backButton;

    private Player player;
    private Planet planet;
    private PlayerShip ship;

    private RadioButton[] allBuySellButtons;
    private int selectedEquipment;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO    
        selectedEquipment = 0;
        gadget1RBCurrentInventory.setSelected(true);

    }

    /**
     * Sets up equipment market screen. Sets visibility of slots
     * 
     * @param player player who will use the market
     */
    public void setUpEquipmentMarketScreen(Player player) {
        this.player = player;
        this.planet = player.getLocation();
        this.ship = player.getShip();
        String empty = "EMPTY";
        setFunds();
        allBuySellButtons = new RadioButton[]{pulseLaserRBBuy, beamLaserRBBuy,
            militaryLaserRBBuy, energyShieldRBBuy, reflectiveShieldRBBuy,
            extraCargoBaysRBBuy, navigatingSystemRBBuy, autoRepairSystemRBBuy,
            targetingSystemRBBuy, cloakingDeviceRBBuy,
            weapon1RBCurrentInventory, weapon2RBCurrentInventory,
            weapon3RBCurrentInventory, shield1RBCurrentInventory,
            shield2RBCurrentInventory, shield3RBCurrentInventory,
            gadget1RBCurrentInventory, gadget2RBCurrentInventory,
            gadget3RBCurrentInventory};

        int numGadgetSlots = ship.getGadgets().getNumSlots();
        int numShieldSlots = ship.getShields().getNumSlots();
        int numWeaponSlots = ship.getWeapons().getNumSlots();
        int numGadgetSlotsFilled = ship.getGadgets().getNumFilledSlots();
        int numShieldSlotsFilled = ship.getShields().getNumFilledSlots();
        int numWeaponSlotsFilled = ship.getWeapons().getNumFilledSlots();
        if (numWeaponSlots == 3) {
            if (numWeaponSlotsFilled == 3) {
                weapon3Text.setText(ship.getWeapons().getItem(2).getName());
                weapon2Text.setText(ship.getWeapons().getItem(1).getName());
                weapon1Text.setText(ship.getWeapons().getItem(0).getName());
            } else if (numWeaponSlotsFilled == 2) {
                weapon3Text.setText(empty);
                weapon2Text.setText(ship.getWeapons().getItem(1).getName());
                weapon1Text.setText(ship.getWeapons().getItem(0).getName());
            } else if (numWeaponSlotsFilled == 1) {
                weapon3Text.setText(empty);
                weapon2Text.setText(empty);
                weapon1Text.setText(ship.getWeapons().getItem(0).getName());
            } else {
                weapon3Text.setText(empty);
                weapon2Text.setText(empty);
                weapon1Text.setText(empty);
            }
        } else if (numWeaponSlots == 2) {
            weapon3RBCurrentInventory.setVisible(false);
            weapon3Text.getParent().setVisible(false);
            if (numWeaponSlotsFilled == 2) {
                weapon2Text.setText(ship.getWeapons().getItem(1).getName());
                weapon1Text.setText(ship.getWeapons().getItem(0).getName());
            } else if (numWeaponSlotsFilled == 1) {
                weapon2Text.setText(empty);
                weapon1Text.setText(ship.getWeapons().getItem(0).getName());
            } else {
                weapon2Text.setText(empty);
                weapon1Text.setText(empty);
            }
        } else if (numWeaponSlots == 1) {
            weapon3RBCurrentInventory.setVisible(false);
            weapon3Text.getParent().setVisible(false);
            weapon2RBCurrentInventory.setVisible(false);
            weapon2Text.getParent().setVisible(false);
            if (numWeaponSlotsFilled == 1) {
                weapon1Text.setText(ship.getWeapons().getItem(0).getName());
            } else {
                weapon1Text.setText(empty);
            }
        } else {
            weapon3RBCurrentInventory.setVisible(false);
            weapon3Text.getParent().setVisible(false);
            weapon2RBCurrentInventory.setVisible(false);
            weapon2Text.getParent().setVisible(false);
            weapon1RBCurrentInventory.setVisible(false);
            weapon1Text.getParent().setVisible(false);
        }

        if (numShieldSlots == 3) {
            if (numShieldSlotsFilled == 3) {
                shield3Text.setText(ship.getShields().getItem(2).getName());
                shield2Text.setText(ship.getShields().getItem(1).getName());
                shield1Text.setText(ship.getShields().getItem(0).getName());
            } else if (numShieldSlotsFilled == 2) {
                shield3Text.setText(empty);
                shield2Text.setText(ship.getShields().getItem(1).getName());
                shield1Text.setText(ship.getShields().getItem(0).getName());
            } else if (numShieldSlotsFilled == 1) {
                shield3Text.setText(empty);
                shield2Text.setText(empty);
                shield1Text.setText(ship.getShields().getItem(0).getName());
            } else {
                shield3Text.setText(empty);
                shield2Text.setText(empty);
                shield1Text.setText(empty);
            }
        } else if (numShieldSlots == 2) {
            shield3RBCurrentInventory.setVisible(false);
            shield3Text.getParent().setVisible(false);
            if (numShieldSlotsFilled == 2) {
                shield2Text.setText(ship.getShields().getItem(1).getName());
                shield1Text.setText(ship.getShields().getItem(0).getName());
            } else if (numShieldSlotsFilled == 1) {
                shield2Text.setText(empty);
                shield1Text.setText(ship.getShields().getItem(0).getName());
            } else {
                shield2Text.setText(empty);
                shield1Text.setText(empty);
            }
        } else if (numShieldSlots == 1) {
            shield3RBCurrentInventory.setVisible(false);
            shield3Text.getParent().setVisible(false);
            shield2RBCurrentInventory.setVisible(false);
            shield2Text.getParent().setVisible(false);
            if (numShieldSlotsFilled == 1) {
                shield1Text.setText(ship.getShields().getItem(0).getName());
            } else {
                shield1Text.setText(empty);
            }
        } else {
            shield3RBCurrentInventory.setVisible(false);
            shield3Text.getParent().setVisible(false);
            shield2RBCurrentInventory.setVisible(false);
            shield2Text.getParent().setVisible(false);
            shield1RBCurrentInventory.setVisible(false);
            shield1Text.getParent().setVisible(false);
        }

        if (numGadgetSlots == 3) {
            if (numGadgetSlotsFilled == 3) {
                gadget3Text.setText(ship.getGadgets().getItem(2).getName());
                gadget2Text.setText(ship.getGadgets().getItem(1).getName());
                gadget1Text.setText(ship.getGadgets().getItem(0).getName());
            } else if (numGadgetSlotsFilled == 2) {
                gadget3Text.setText(empty);
                gadget2Text.setText(ship.getGadgets().getItem(1).getName());
                gadget1Text.setText(ship.getGadgets().getItem(0).getName());
            } else if (numGadgetSlotsFilled == 1) {
                gadget3Text.setText(empty);
                gadget2Text.setText(empty);
                gadget1Text.setText(ship.getGadgets().getItem(0).getName());
            } else {
                gadget3Text.setText(empty);
                gadget2Text.setText(empty);
                gadget1Text.setText(empty);
            }
        } else if (numGadgetSlots == 2) {
            gadget3RBCurrentInventory.setVisible(false);
            gadget3Text.getParent().setVisible(false);
            if (numGadgetSlotsFilled == 2) {
                gadget2Text.setText(ship.getGadgets().getItem(1).getName());
                gadget1Text.setText(ship.getGadgets().getItem(0).getName());
            } else if (numGadgetSlotsFilled == 1) {
                gadget2Text.setText(empty);
                gadget1Text.setText(ship.getGadgets().getItem(0).getName());
            } else {
                gadget2Text.setText(empty);
                gadget1Text.setText(empty);
            }
        } else if (numGadgetSlots == 1) {
            gadget3RBCurrentInventory.setVisible(false);
            gadget3Text.getParent().setVisible(false);
            gadget2RBCurrentInventory.setVisible(false);
            gadget2Text.getParent().setVisible(false);
            if (numGadgetSlotsFilled == 1) {
                gadget1Text.setText(ship.getGadgets().getItem(0).getName());
            } else {
                gadget1Text.setText(empty);
            }
        } else {
            gadget3RBCurrentInventory.setVisible(false);
            gadget3Text.getParent().setVisible(false);
            gadget2RBCurrentInventory.setVisible(false);
            gadget2Text.getParent().setVisible(false);
            gadget1RBCurrentInventory.setVisible(false);
            gadget1Text.getParent().setVisible(false);
        }

        TechLevel techLevel = planet.getLevel();
        //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~");
        //System.out.println(planet.getLevel().toString());

        switch (techLevel) {
            case PRE_AGRICULTURE:
            case AGRICULTURE:
                extraCargoBaysRBBuy.setVisible(false);
                extraCargoBaysBuy.setVisible(false);
            case MEDIEVAL:
                autoRepairSystemRBBuy.setVisible(false);
                autoRepairSystemBuy.setVisible(false);
            case RENAISSANCE:
            case EARLY_INDUSTRIAL:
                energyShieldBuy.setVisible(false);
                energyShieldRBBuy.setVisible(false);
                pulseLaserBuy.setVisible(false);
                pulseLaserRBBuy.setVisible(false);
            case INDUSTRIAL:
                navigatingSystemRBBuy.setVisible(false);
                navigatingSystemBuy.setVisible(false);
                targetingSystemRBBuy.setVisible(false);
                targetingSystemBuy.setVisible(false);
                reflectiveShieldRBBuy.setVisible(false);
                reflectiveShieldBuy.setVisible(false);
                beamLaserRBBuy.setVisible(false);
                beamLaserBuy.setVisible(false);
            case POST_INDUSTRIAL:
                cloakingDeviceRBBuy.setVisible(false);
                militaryLaserRBBuy.setVisible(false);
                cloakingDeviceBuy.setVisible(false);
                militaryLaserBuy.setVisible(false);
            case HI_TECH: ;

        }
    }//set up method!   

    /**
     * Processes the event where a ship is selected
     *
     * @param event mouse click on radio buttton
     */
    @FXML
    protected void selectEquipment(ActionEvent event) {
        RadioButton src = (RadioButton) event.getSource();
        for (RadioButton button : allBuySellButtons) {
            if (!button.equals(src)) {
                button.setSelected(false);
            } else {
                if (button.getId().equals("pulseLaserRBBuy")) {
                    fillDescription(WeaponType.PULSE);
                } else if (button.getId().equals("beamLaserRBBuy")) {
                    fillDescription(WeaponType.BEAM);
                } else if (button.getId().equals("militaryLaserRBBuy")) {
                    fillDescription(WeaponType.MILITARY);
                } else if (button.getId().equals("energyShieldRBBuy")) {
                    fillDescription(ShieldType.ENERGY);
                } else if (button.getId().equals("reflectiveShieldRBBuy")) {
                    fillDescription(ShieldType.REFLECTIVE);
                } else if (button.getId().equals("extraCargoBaysRBBuy")) {
                    fillDescription(GadgetType.EXTRA_CARGO);
                } else if (button.getId().equals("navigatingSystemRBBuy")) {
                    fillDescription(GadgetType.NAVIGATION);
                } else if (button.getId().equals("autoRepairSystemRBBuy")) {
                    fillDescription(GadgetType.AUTO_REPAIR);
                } else if (button.getId().equals("targetingSystemRBBuy")) {
                    fillDescription(GadgetType.TARGETING);
                } else if (button.getId().equals("cloakingDeviceRBBuy")) {
                    fillDescription(GadgetType.CLOAK);
                } else if (button.getId().equals("weapon1RBCurrentInventory")) {
                    weaponCargoToDescription(weapon1Text);
                } else if (button.getId().equals("weapon2RBCurrentInventory")) {
                    weaponCargoToDescription(weapon2Text);
                } else if (button.getId().equals("weapon3RBCurrentInventory")) {
                    weaponCargoToDescription(weapon3Text);
                } else if (button.getId().equals("shield1RBCurrentInventory")) {
                    weaponCargoToDescription(shield1Text);
                } else if (button.getId().equals("shield2RBCurrentInventory")) {
                    weaponCargoToDescription(shield2Text);
                } else if (button.getId().equals("shield3RBCurrentInventory")) {
                    weaponCargoToDescription(shield3Text);
                } else if (button.getId().equals("gadget1RBCurrentInventory")) {
                    weaponCargoToDescription(gadget1Text);
                } else if (button.getId().equals("gadget2RBCurrentInventory")) {
                    weaponCargoToDescription(gadget2Text);
                } else if (button.getId().equals("gadget3RBCurrentInventory")) {
                    weaponCargoToDescription(gadget3Text);
                }

            }
        }
    }

    /**
     * Fills descriptions with weapon string representation
     *
     * @param l label to change
     */
    public void weaponCargoToDescription(Label l) {
        if (l.getText().equals(WeaponType.PULSE.toString())) {
            fillDescription(WeaponType.PULSE);
        } else if (l.getText().equals(WeaponType.BEAM.toString())) {
            fillDescription(WeaponType.BEAM);
        } else if (l.getText().equals(WeaponType.MILITARY.toString())) {
            fillDescription(WeaponType.MILITARY);
        } else if (l.getText().equals(ShieldType.ENERGY.toString())) {
            fillDescription(ShieldType.ENERGY);
        } else if (l.getText().equals(ShieldType.REFLECTIVE.toString())) {
            fillDescription(ShieldType.REFLECTIVE);
        } else if (l.getText().equals(GadgetType.EXTRA_CARGO.toString())) {
            fillDescription(GadgetType.EXTRA_CARGO);
        } else if (l.getText().equals(GadgetType.NAVIGATION.toString())) {
            fillDescription(GadgetType.NAVIGATION);
        } else if (l.getText().equals(GadgetType.AUTO_REPAIR.toString())) {
            fillDescription(GadgetType.AUTO_REPAIR);
        } else if (l.getText().equals(GadgetType.TARGETING.toString())) {
            fillDescription(GadgetType.TARGETING);
        } else if (l.getText().equals(GadgetType.CLOAK.toString())) {
            fillDescription(GadgetType.CLOAK);
        }

    }

    @FXML
    protected void backToShipyard(ActionEvent event) {
        mainControl.goToShipYardScreen();
    }

    private void fillDescription(ShieldType s) {
        name.setText(s.toString());
        type.setText("SHIELD");
        buyPrice.setText("₪" + s.price());
        sellPrice.setText("₪" + (s.price() * .75));
        String val = "" + s.power();
        power.setText(val);
        charge.setText("N/A");

    }

    private void fillDescription(WeaponType w) {
        name.setText(w.toString());
        type.setText("WEAPON");
        buyPrice.setText("₪" + w.price());
        sellPrice.setText("₪" + (w.price() * .75));
        String val = "" + w.power();
        power.setText(val);
        charge.setText("N/A");

    }

    private void fillDescription(GadgetType g) {
        name.setText(g.toString());
        type.setText("GADGET");
        buyPrice.setText("₪" + g.price());
        sellPrice.setText("₪" + (g.price() * .75));
        power.setText("N/A");
        charge.setText("N/A");
    }

    public void setRadioButtons(ArrayList<RadioButton> r) {
        for (int i = 0; i < r.size(); i++) {
            allBuySellButtons[i] = r.get(i);
        }
    }

    public RadioButton getCheckedRadioButton() {
        RadioButton r = null;
        int counter = 0;
        for (RadioButton button : allBuySellButtons) {
            if (button.isSelected()) {
                r = button;
                counter++;
            }
        }
        if (counter > 1) {
            throw new TooManyRadioButtonsCheckedException("Too Many Radio Buttons Checked");
        }
        return r;
    }

    @FXML
    protected void pressBuyButton(ActionEvent event) {

        boolean wrongButton = false;
        RadioButton src = (RadioButton) getCheckedRadioButton();
        if (src.getId().equals("weapon1RBCurrentInventory")
                || src.getId().equals("weapon2RBCurrentInventory")
                || src.getId().equals("weapon3RBCurrentInventory")
                || src.getId().equals("shield1RBCurrentInventory")
                || src.getId().equals("shield2RBCurrentInventory")
                || src.getId().equals("shield3RBCurrentInventory")
                || src.getId().equals("gadget1RBCurrentInventory")
                || src.getId().equals("gadget2RBCurrentInventory")
                || src.getId().equals("gadget3RBCurrentInventory")) {
            wrongButton = true;
        }

        if (!wrongButton) {
            boolean buy = false;
            boolean bought = false;
            //System.out.println("You got: " + (player.getWallet().getCredits() + "They got: " + Double.parseDouble(buyPrice.getText().substring(1))));
            if ((player.getWallet().getCredits() >= Double.parseDouble(buyPrice.getText().substring(1)))) {
                buy = true;
            } else {
                mainControl.displayAlertMessage("Transaction Unsuccessful", null, "You do not have enough money to make this purchase.");
            }
            // do a check for space
            if (type.getText().equals("GADGET") && buy) {

                if (!ship.getGadgets().isFull()) {
                    if (name.getText().equals("5 Extra Cargo Bays")) {
                        player.getShip().getGadgets().addItem(new Gadget(GadgetType.EXTRA_CARGO));
                        player.getShip().getCargo().increaseCapacity();
                        //System.out.println("Name: " + ship.getGadgets().getItem(0).getName());

                        bought = true;
                    } else if (name.getText().equals("Auto-Repair System")) {
                        player.getShip().getGadgets().addItem(new Gadget(GadgetType.AUTO_REPAIR));
                        bought = true;
                    } else if (name.getText().equals("Targeting System")) {
                        player.getShip().getGadgets().addItem(new Gadget(GadgetType.TARGETING));
                        bought = true;
                    } else if (name.getText().equals("Cloaking Device")) {
                        player.getShip().getGadgets().addItem(new Gadget(GadgetType.CLOAK));
                        bought = true;
                    } else if (name.getText().equals("Navigating System")) {
                        player.getShip().getGadgets().addItem(new Gadget(GadgetType.NAVIGATION));
                        bought = true;
                    }
                } else {
                    mainControl.displayAlertMessage("Transaction Unsuccessful", null, "Need more Gadget Space");
                }

            } else if (type.getText().equals("WEAPON") && buy) {
                if (!ship.getWeapons().isFull()) {
                    if (name.getText().equals("Pulse laser")) {
                        player.getShip().getWeapons().addItem(new Weapon(WeaponType.PULSE));
                        bought = true;
                    } else if (name.getText().equals("Beam laser")) {
                        player.getShip().getWeapons().addItem(new Weapon(WeaponType.BEAM));
                        bought = true;
                    } else {
                        player.getShip().getWeapons().addItem(new Weapon(WeaponType.MILITARY));
                        bought = true;
                    }
                } else {
                    mainControl.displayAlertMessage("Transaction Unsuccessful", null, "Need more Weapon Space");
                }
            } else if (type.getText().equals("SHIELD") && buy) {
                if (!ship.getShields().isFull()) {
                    if (name.getText().equals("Energy shield")) {
                        player.getShip().getShields().addItem(new Shield(ShieldType.ENERGY));
                        bought = true;
                    } else {
                        player.getShip().getShields().addItem(new Shield(ShieldType.REFLECTIVE));
                        bought = true;
                    }
                } else {
                    mainControl.displayAlertMessage("Transaction Unsuccessful", null, "Need more Shield Space");
                }
            }

            if (bought) {
                player.getWallet().setCredits((int) ((player.getWallet().getCredits() - Double.parseDouble(buyPrice.getText().substring(1)))));
                //setUpEquipmentMarketScreen(player);
                refreshValues();
                mainControl.displayAlertMessage("Transaction Successful", null, "Successfully Bought 1 " + name.getText());
            } else if (buy) {
                mainControl.displayAlertMessage("Unsuccessful Transaction", null, "Acquire more space to be able to purchase " + name.getText());
            }

            setFunds();

        } else {
            mainControl.displayAlertMessage("Wrong Button Pressed", null, "Click Buy to BUY and Sell to SELL. Derps");
        }

    }

    private void setFunds() {
        funds.setText("₪" + player.getWallet().getCredits());
    }

    @FXML
    protected void pressSellButton(ActionEvent event) {

        boolean rightButton = false;
        RadioButton src = (RadioButton) getCheckedRadioButton();
        if (src.getId().equals("weapon1RBCurrentInventory")
                || src.getId().equals("weapon2RBCurrentInventory")
                || src.getId().equals("weapon3RBCurrentInventory")
                || src.getId().equals("shield1RBCurrentInventory")
                || src.getId().equals("shield2RBCurrentInventory")
                || src.getId().equals("shield3RBCurrentInventory")
                || src.getId().equals("gadget1RBCurrentInventory")
                || src.getId().equals("gadget2RBCurrentInventory")
                || src.getId().equals("gadget3RBCurrentInventory")) {
            rightButton = true;
        }

        if (rightButton) {

            boolean sold = false;
            if (type.getText().equals("GADGET")) {

                if (name.getText().equals("5 Extra Cargo Bays")) {
                    player.getShip().getGadgets().removeItemOfSameType(new Gadget(GadgetType.EXTRA_CARGO));
                    player.getShip().getCargo().decreaseCapacity();

                } else if (name.getText().equals("Auto-Repair System")) {
                    player.getShip().getGadgets().removeItemOfSameType(new Gadget(GadgetType.AUTO_REPAIR));
                } else if (name.getText().equals("Targeting System")) {
                    player.getShip().getGadgets().removeItemOfSameType(new Gadget(GadgetType.TARGETING));
                } else if (name.getText().equals("Cloaking Device")) {
                    player.getShip().getGadgets().removeItemOfSameType(new Gadget(GadgetType.CLOAK));
                } else if (name.getText().equals("Navigating System")) {
                    player.getShip().getGadgets().removeItemOfSameType(new Gadget(GadgetType.NAVIGATION));
                }
                sold = true;
            } else if (type.getText().equals("WEAPON")) {
                if (name.getText().equals("Pulse laser")) {
                    player.getShip().getWeapons().removeItemOfSameType(new Weapon(WeaponType.PULSE));
                } else if (name.getText().equals("Beam laser")) {
                    player.getShip().getWeapons().removeItemOfSameType(new Weapon(WeaponType.BEAM));
                } else if (name.getText().equals("Military laser")) {
                    player.getShip().getWeapons().removeItemOfSameType(new Weapon(WeaponType.MILITARY));
                }
                sold = true;
            } else if (type.getText().equals("SHIELD")) {
                if (name.getText().equals("Energy shield")) {
                    player.getShip().getShields().removeItemOfSameType(new Shield(ShieldType.ENERGY));
                } else if (name.getText().equals("Reflective shield")) {
                    player.getShip().getShields().removeItemOfSameType(new Shield(ShieldType.REFLECTIVE));
                }
                sold = true;
            }

            if (sold == true) {
                player.getWallet().setCredits((int) ((player.getWallet().getCredits() + Double.parseDouble(sellPrice.getText().substring(1)))));
            }

            //setUpEquipmentMarketScreen(player);
            refreshValues();
            setFunds();
            int i = 0;
            while (i < player.getShip().getGadgets().getNumFilledSlots()) {
                //System.out.println(player.getShip().getGadgets().getItem(i).getName());
                i++;
            }
            //System.out.println(name.getText());

        } else {
            mainControl.displayAlertMessage("Wrong Button Pressed", null, "Click Buy to BUY and Sell to SELL. Derps");

        }

    }

    private void refreshValues() {
        int numGadgetSlots = ship.getGadgets().getNumSlots();
        int numShieldSlots = ship.getShields().getNumSlots();
        int numWeaponSlots = ship.getWeapons().getNumSlots();
        int numGadgetSlotsFilled = ship.getGadgets().getNumFilledSlots();
        int numShieldSlotsFilled = ship.getShields().getNumFilledSlots();
        int numWeaponSlotsFilled = ship.getWeapons().getNumFilledSlots();
        String empty = "EMPTY";
        if (numWeaponSlots == 3) {
            if (numWeaponSlotsFilled == 3) {
                weapon3Text.setText(ship.getWeapons().getItem(2).getName());
                weapon2Text.setText(ship.getWeapons().getItem(1).getName());
                weapon1Text.setText(ship.getWeapons().getItem(0).getName());
            } else if (numWeaponSlotsFilled == 2) {
                weapon3Text.setText(empty);
                weapon2Text.setText(ship.getWeapons().getItem(1).getName());
                weapon1Text.setText(ship.getWeapons().getItem(0).getName());
            } else if (numWeaponSlotsFilled == 1) {
                weapon3Text.setText(empty);
                weapon2Text.setText(empty);
                weapon1Text.setText(ship.getWeapons().getItem(0).getName());
            } else {
                weapon3Text.setText(empty);
                weapon2Text.setText(empty);
                weapon1Text.setText(empty);
            }
        } else if (numWeaponSlots == 2) {
            weapon3RBCurrentInventory.setVisible(false);
            weapon3Text.setVisible(false);
            if (numWeaponSlotsFilled == 2) {
                weapon2Text.setText(ship.getWeapons().getItem(1).getName());
                weapon1Text.setText(ship.getWeapons().getItem(0).getName());
            } else if (numWeaponSlotsFilled == 1) {
                weapon2Text.setText(empty);
                weapon1Text.setText(ship.getWeapons().getItem(0).getName());
            } else {
                weapon2Text.setText(empty);
                weapon1Text.setText(empty);
            }
        } else if (numWeaponSlots == 1) {
            weapon3RBCurrentInventory.setVisible(false);
            weapon3Text.setVisible(false);
            weapon2RBCurrentInventory.setVisible(false);
            weapon2Text.setVisible(false);
            if (numWeaponSlotsFilled == 1) {
                weapon1Text.setText(ship.getWeapons().getItem(0).getName());
            } else {
                weapon1Text.setText(empty);
            }
        } else {
            weapon3RBCurrentInventory.setVisible(false);
            weapon3Text.setVisible(false);
            weapon2RBCurrentInventory.setVisible(false);
            weapon2Text.setVisible(false);
            weapon1RBCurrentInventory.setVisible(false);
            weapon1Text.setVisible(false);
        }
        

        if (numShieldSlots == 3) {
            if (numShieldSlotsFilled == 3) {
                shield3Text.setText(ship.getShields().getItem(2).getName());
                shield2Text.setText(ship.getShields().getItem(1).getName());
                shield1Text.setText(ship.getShields().getItem(0).getName());
            } else if (numShieldSlotsFilled == 2) {
                shield3Text.setText(empty);
                shield2Text.setText(ship.getShields().getItem(1).getName());
                shield1Text.setText(ship.getShields().getItem(0).getName());
            } else if (numShieldSlotsFilled == 1) {
                shield3Text.setText(empty);
                shield2Text.setText(empty);
                shield1Text.setText(ship.getShields().getItem(0).getName());
            } else {
                shield3Text.setText(empty);
                shield2Text.setText(empty);
                shield1Text.setText(empty);
            }
        } else if (numShieldSlots == 2) {
            shield3RBCurrentInventory.setVisible(false);
            shield3Text.setVisible(false);
            if (numShieldSlotsFilled == 2) {
                shield2Text.setText(ship.getShields().getItem(1).getName());
                shield1Text.setText(ship.getShields().getItem(0).getName());
            } else if (numShieldSlotsFilled == 1) {
                shield2Text.setText(empty);
                shield1Text.setText(ship.getShields().getItem(0).getName());
            } else {
                shield2Text.setText(empty);
                shield1Text.setText(empty);
            }
        } else if (numShieldSlots == 1) {
            shield3RBCurrentInventory.setVisible(false);
            shield3Text.setVisible(false);
            shield2RBCurrentInventory.setVisible(false);
            shield2Text.setVisible(false);
            if (numShieldSlotsFilled == 1) {
                shield1Text.setText(ship.getShields().getItem(0).getName());
            } else {
                shield1Text.setText(empty);
            }
        } else {
            shield3RBCurrentInventory.setVisible(false);
            shield3Text.setVisible(false);
            shield2RBCurrentInventory.setVisible(false);
            shield2Text.setVisible(false);
            shield1RBCurrentInventory.setVisible(false);
            shield1Text.setVisible(false);
        }

        if (numGadgetSlots == 3) {
            if (numGadgetSlotsFilled == 3) {
                gadget3Text.setText(ship.getGadgets().getItem(2).getName());
                gadget2Text.setText(ship.getGadgets().getItem(1).getName());
                gadget1Text.setText(ship.getGadgets().getItem(0).getName());
            } else if (numGadgetSlotsFilled == 2) {
                gadget3Text.setText(empty);
                gadget2Text.setText(ship.getGadgets().getItem(1).getName());
                gadget1Text.setText(ship.getGadgets().getItem(0).getName());
            } else if (numGadgetSlotsFilled == 1) {
                gadget3Text.setText(empty);
                gadget2Text.setText(empty);
                gadget1Text.setText(ship.getGadgets().getItem(0).getName());
            } else {
                gadget3Text.setText(empty);
                gadget2Text.setText(empty);
                gadget1Text.setText(empty);
            }
        } else if (numGadgetSlots == 2) {
            gadget3RBCurrentInventory.setVisible(false);
            gadget3Text.setVisible(false);
            if (numGadgetSlotsFilled == 2) {
                gadget2Text.setText(ship.getGadgets().getItem(1).getName());
                gadget1Text.setText(ship.getGadgets().getItem(0).getName());
            } else if (numGadgetSlotsFilled == 1) {
                gadget2Text.setText(empty);
                gadget1Text.setText(ship.getGadgets().getItem(0).getName());
            } else {
                gadget2Text.setText(empty);
                gadget1Text.setText(empty);
            }
        } else if (numGadgetSlots == 1) {
            gadget3RBCurrentInventory.setVisible(false);
            gadget3Text.setVisible(false);
            gadget2RBCurrentInventory.setVisible(false);
            gadget2Text.setVisible(false);
            if (numGadgetSlotsFilled == 1) {
                gadget1Text.setText(ship.getGadgets().getItem(0).getName());
            } else {
                gadget1Text.setText(empty);
            }
        } else {
            gadget3RBCurrentInventory.setVisible(false);
            gadget3Text.setVisible(false);
            gadget2RBCurrentInventory.setVisible(false);
            gadget2Text.setVisible(false);
            gadget1RBCurrentInventory.setVisible(false);
            gadget1Text.setVisible(false);
        }
        
    }

}
