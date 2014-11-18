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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.StackPane;
import spacetrader.Player;
import spacetrader.SkillList.Skill;
import spacetrader.exceptions.InsufficientFundsException;
import spacetrader.exceptions.SlotsAreFullException;
import spacetrader.planets.Planet;
import spacetrader.ships.Equipment;
import spacetrader.ships.EquipmentSlots;
import spacetrader.ships.Gadget;
import spacetrader.ships.GadgetType;
import spacetrader.ships.PlayerShip;
import spacetrader.ships.Shield;
import spacetrader.ships.ShieldType;
import spacetrader.ships.Weapon;
import spacetrader.ships.WeaponType;

/**
 * FXML Controller class
 *
 * @author maharshipatel999
 */
public class GadgetScreenController
        extends SceneController
        implements Initializable {

    @FXML
    private Label weapon1Text, weapon2Text, weapon3Text, shield1Text,
            shield2Text, shield3Text, gadget1Text, gadget2Text, gadget3Text;

    @FXML
    private RadioButton weapon1RB, weapon2RB, weapon3RB,
            shield1RB, shield2RB, shield3RB, gadget1RB, gadget2RB, gadget3RB;

    @FXML
    private RadioButton pulseLaserRBBuy, beamLaserRBBuy, militaryLaserRBBuy,
            energyShieldRBBuy, reflectiveShieldRBBuy, extraCargoBaysRBBuy,
            navigatingSystemRBBuy, autoRepairSystemRBBuy, targetingSystemRBBuy,
            cloakingDeviceRBBuy;

    @FXML
    private Label pulseLaserBuy, beamLaserBuy, militaryLaserBuy,
            energyShieldBuy, reflectiveShieldBuy, extraCargoBaysBuy,
            navigatingSystemBuy, autoRepairSystemBuy, targetingSystemBuy,
            cloakingDeviceBuy;

    @FXML
    private Label funds;

    @FXML
    private Label descText, nameText, typeText,
            priceTextLabel, priceTextAmount, powerText, chargeText;

    @FXML
    private Button buyButton, sellButton, backButton;

    private Player player;
    private Planet planet;

    private EquipmentIcon selectedIcon;

    private List<List<EquipmentSlotIcon>> inventorySlotIcons;
    private List<List<BuyableEquipmentIcon>> buyableEquipmentIcons;
    private EquipmentSlots[] shipEquipmentSlots;

    private final WeaponType[] weaponTypes = WeaponType.values();
    private final ShieldType[] shieldTypes = ShieldType.values();
    private final GadgetType[] gadgetTypes = GadgetType.values();

    private static final int NUM_TYPES = 3;
    private static final int MAX_SLOTS = 3;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inventorySlotIcons = new ArrayList<>();
        this.buyableEquipmentIcons = new ArrayList<>();
        for (int i = 0; i < NUM_TYPES; i++) {
            this.inventorySlotIcons.add(new ArrayList<>());
            this.buyableEquipmentIcons.add(new ArrayList<>());
        }
    }

    /**
     * Sets up equipment market screen. Sets visibility of slots.
     *
     * @param player player who will use the market
     */
    public void setUpEquipmentMarketScreen(Player player) {
        this.player = player;
        this.planet = player.getLocation();

        //Create temporary arrays for holding the fxids.
        Label[] equipmentSlotTexts = new Label[] {
            weapon1Text, weapon2Text, weapon3Text,
            shield1Text, shield2Text, shield3Text,
            gadget1Text, gadget2Text, gadget3Text};

        RadioButton[] equipmentSlotButtons = new RadioButton[] {
            weapon1RB, weapon2RB, weapon3RB, shield1RB,
            shield2RB, shield3RB, gadget1RB, gadget2RB, gadget3RB};

        Label[] buyableWeaponTexts = new Label[] {
            pulseLaserBuy, beamLaserBuy, militaryLaserBuy};
        Label[] buyableShieldTexts = new Label[] {
            energyShieldBuy, reflectiveShieldBuy};
        Label[] buyableGadgetTexts = new Label[] {
            extraCargoBaysBuy, autoRepairSystemBuy, navigatingSystemBuy,
            targetingSystemBuy, cloakingDeviceBuy};

        RadioButton[] buyableWeaponButtons = new RadioButton[] {
            pulseLaserRBBuy, beamLaserRBBuy, militaryLaserRBBuy};
        RadioButton[] buyableShieldButtons = new RadioButton[] {
            energyShieldRBBuy, reflectiveShieldRBBuy};
        RadioButton[] buyableGadgetButtons = new RadioButton[] {
            extraCargoBaysRBBuy, autoRepairSystemRBBuy, navigatingSystemRBBuy,
            targetingSystemRBBuy, cloakingDeviceRBBuy};

        //Set up the current inventory slot icons.
        for (int i = 0; i < (NUM_TYPES * MAX_SLOTS); i++) {
            inventorySlotIcons.get(i / MAX_SLOTS).add(new EquipmentSlotIcon(equipmentSlotTexts[i], equipmentSlotButtons[i]));
        }

        //Set up buyable equipment selection icons.
        for (int i = 0; i < weaponTypes.length; i++) {
            Weapon newWeapon = new Weapon(weaponTypes[i]);
            buyableEquipmentIcons.get(0).add(new BuyableEquipmentIcon(
                    buyableWeaponTexts[i], buyableWeaponButtons[i], newWeapon));
        }
        for (int i = 0; i < shieldTypes.length; i++) {
            Shield newShield = new Shield(shieldTypes[i]);
            buyableEquipmentIcons.get(1).add(new BuyableEquipmentIcon(
                    buyableShieldTexts[i], buyableShieldButtons[i], newShield));
        }
        for (int i = 0; i < gadgetTypes.length; i++) {
            Gadget newGadget = new Gadget(gadgetTypes[i]);
            buyableEquipmentIcons.get(2).add(new BuyableEquipmentIcon(
                    buyableGadgetTexts[i], buyableGadgetButtons[i], newGadget));
        }

        //Set up a holder for the ship's equipment slots.
        PlayerShip ship = player.getShip();
        this.shipEquipmentSlots = new EquipmentSlots[] {
            ship.getWeapons(), ship.getShields(), ship.getGadgets()
        };

        //Show the equipment that can be bought on this planet.
        for (int i = 0; i < NUM_TYPES; i++) {
            buyableEquipmentIcons.get(i).forEach(icon -> icon.determineVisiblity());
        }

        //Show the player's inventory and current money.
        updateInventorySlots();
        updateFundsText();
        buyButton.setDisable(true);
        sellButton.setDisable(true);
    }

    /**
     * Processes the event where a ship is selected.
     *
     * @param event mouse click on radio button
     */
    @FXML
    protected void selectEquipment(ActionEvent event) {
        RadioButton src = (RadioButton) event.getSource();

        if (selectedIcon != null) {
            selectedIcon.selectButton.setSelected(false);
        }

        /**
         * Finds the button that was selected, and sets selectedIcon to its
         * corresponding icon object.
         */
        for (int i = 0; i < NUM_TYPES; i++) {
            for (EquipmentIcon icon : inventorySlotIcons.get(i)) {
                if (icon.selectButton == src) {
                    selectedIcon = icon;
                    buyButton.setDisable(true);
                    sellButton.setDisable(false);
                }
            }
            for (EquipmentIcon icon : buyableEquipmentIcons.get(i)) {
                if (icon.selectButton == src) {
                    selectedIcon = icon;
                    buyButton.setDisable(false);
                    sellButton.setDisable(true);
                }
            }
        }
        updateDescription();
    }

    /**
     * Handles when the player presses the buy button. If the player has enough
     * money and enough free slots, the transaction is successful, otherwise he
     * is given an error message.
     *
     * @param event the mouse clicks the buy button
     */
    @FXML
    protected void pressBuyButton(ActionEvent event) {
        int cost = buyingPrice(selectedIcon.item);
        int initialCredits = player.getWallet().getCredits();

        //the default pop-up for the player
        String msgTitle = "Transaction Unsuccessful";
        String msgText = "Purchase Failure";

        try {
            player.getWallet().remove(cost);

            int type;
            //Finds the type of the icon searching through each list.
            for (type = 0; type < NUM_TYPES; type++) {
                if (buyableEquipmentIcons.get(type).indexOf(selectedIcon) >= 0) {
                    break;
                }
            }
            shipEquipmentSlots[type].addItem(selectedIcon.item.clone()); //gives the player a copy of that item.

            if (selectedIcon.item.equals(new Gadget(GadgetType.EXTRA_CARGO))) {
                player.getShip().getCargo().increaseCapacity();
            }

            msgTitle = "Transaction Successful";
            msgText = String.format("You successfully bought a %s for ₪%d!", nameText.getText(), cost);
        } catch (SlotsAreFullException e) {
            //Since its possible that the player's wallet was successfully
            //decremented, we must reset it back to its original value.
            player.getWallet().setCredits(initialCredits);
            msgText = "Acquire more equipment slots to be able to purchase a " + nameText.getText() + ".";
        } catch (InsufficientFundsException e) {
            msgText = String.format("You do not have enough money to purchase a %s.%n%nIt costs ₪%d, but you only have ₪%d.",
                    nameText.getText(), cost, initialCredits);
        } finally {
            mainControl.displayAlertMessage(msgTitle, msgText);
        }

        updateInventorySlots();
        updateFundsText();
    }

    /**
     * Handles when the player presses the sell button. Removes the item from
     * the player's inventory and refreshes the screen.
     *
     * @param event the mouse clicks the sell button
     */
    @FXML
    protected void pressSellButton(ActionEvent event) {
        int type, slot = -1;
        //Finds the type and icon slot by searching through each list.
        for (type = 0; type < NUM_TYPES; type++) {
            slot = inventorySlotIcons.get(type).indexOf(selectedIcon);
            if (slot >= 0) {
                break;
            }
        }
        shipEquipmentSlots[type].removeAtIndex(slot);

        if (selectedIcon.item.equals(new Gadget(GadgetType.EXTRA_CARGO))) {
            player.getShip().getCargo().decreaseCapacity();
        }

        int revenue = sellingPrice(selectedIcon.item);
        player.getWallet().add(revenue);

        String msgTitle = "Transaction Successful";
        String msgText = String.format("You successfully sold your %s for ₪%d!", nameText.getText(), revenue);
        mainControl.displayAlertMessage(msgTitle, msgText);

        sellButton.setDisable(true);

        updateInventorySlots();
        updateFundsText();
    }

    /**
     * Returns the player to the ship yard.
     */
    @FXML
    protected void backToShipyard() {
        mainControl.goToShipYardScreen();
    }

    /**
     * Updates the equipment that is shown in the player's equipment slots in
     * the view.
     */
    private void updateInventorySlots() {
        //set the visibility of current inventory equipment slots.
        for (int equipType = 0; equipType < NUM_TYPES; equipType++) {
            List<EquipmentSlotIcon> equipIcons = inventorySlotIcons.get(equipType);
            EquipmentSlots equipmentSlots = shipEquipmentSlots[equipType];

            int slot = 0;
            for (int i = 0; i < equipmentSlots.getNumFilledSlots(); i++) {
                if (equipmentSlots.getItem(i) != null) {
                    equipIcons.get(slot++).setSlotContents(equipmentSlots.getItem(i));
                }
            }
            while (slot < equipmentSlots.getNumSlots()) {
                equipIcons.get(slot++).setSlotContents(null);
            }
            while (slot < equipIcons.size()) {
                equipIcons.get(slot++).updateIcon();
            }
        }
    }

    /**
     * Updates the text that shows the player's current amount of credits.
     */
    private void updateFundsText() {
        funds.setText("₪" + player.getWallet().getCredits());
    }

    private void updateDescription() {
        if (selectedIcon == null) {
            return;
        }
        Equipment item = selectedIcon.item;

        typeText.setText(item.getEquipmentName());
        nameText.setText(item.getName());
        if (sellButton.isDisable()) {
            priceTextLabel.setText("Buy Price:");
            priceTextAmount.setText("₪" + buyingPrice(item));
        } else {
            priceTextLabel.setText("Sell Price:");
            priceTextAmount.setText("₪" + sellingPrice(item));
        }
        powerText.setText("N/A");
        chargeText.setText("N/A");

        if (item instanceof Weapon) {
            powerText.setText(String.valueOf(((Weapon) item).getPower()));
        } else if (item instanceof Shield) {
            powerText.setText(String.valueOf(((Shield) item).getHealth()));
        }
    }

    /**
     * Price of buying a good.
     */
    private int buyingPrice(Equipment equip) {
        return equip.getBuyPrice(player.getEffectiveSkill(Skill.TRADER));
    }

    /**
     * Price of selling a good.
     */
    private int sellingPrice(Equipment equip) {
        return equip.getSellPrice(player.getEffectiveSkill(Skill.TRADER));
    }

    /**
     * Represents an icon for an equipment in the View. An EquipmentIcon can be
     * selected with its radio button.
     */
    private class EquipmentIcon {

        protected boolean isVisible;
        protected Label text;
        protected RadioButton selectButton;
        protected Equipment item;

        /**
         *
         * @param isVisible
         * @param text
         * @param selectButton
         * @param item
         */
        private EquipmentIcon(boolean isVisible, Label text,
                RadioButton selectButton, Equipment item) {
            this.isVisible = isVisible;
            this.text = text;
            this.selectButton = selectButton;
            this.item = item;
        }
    }

    /**
     * A class that represents one of the equipment slot icons in a player's
     * current inventory.
     */
    private class EquipmentSlotIcon extends EquipmentIcon {

        private static final String emptyText = "EMPTY SLOT";

        /**
         *
         * @param selectButton
         * @param text
         */
        private EquipmentSlotIcon(Label text, RadioButton selectButton) {
            super(false, text, selectButton, null);
        }

        private void setSlotContents(Equipment item) {
            this.isVisible = true;
            this.item = item;
            updateIcon();
        }

        private void updateIcon() {
            if (!isVisible) {
                ((StackPane) text.getParent()).setVisible(false);
                selectButton.setVisible(false);
            } else {
                ((StackPane) text.getParent()).setVisible(true);
                if (item != null) {
                    text.setText(item.getName());
                    selectButton.setVisible(true);
                } else {
                    text.setText(emptyText);
                    selectButton.setVisible(false);
                }
            }
        }
    }

    /**
     * A class that represents one of the equipment slot icons in a player's
     * current inventory.
     */
    private class BuyableEquipmentIcon extends EquipmentIcon {

        /**
         *
         * @param selectButton
         * @param text
         */
        private BuyableEquipmentIcon(Label text, RadioButton selectButton,
                Equipment item) {
            super(false, text, selectButton, item);
        }

        private void determineVisiblity() {
            if (planet.getLevel().compareTo(item.getMinTechLevel()) >= 0) {
                isVisible = true;
                text.setText(item.getName());
            } else {
                isVisible = false;
                text.setVisible(false);
                selectButton.setVisible(false);
            }
        }
    }
}
