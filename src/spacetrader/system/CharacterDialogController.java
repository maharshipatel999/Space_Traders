/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.system;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import spacetrader.Player;

/**
 * FXML Controller class.
 *
 * @author Caleb Stokols
 */
public class CharacterDialogController
    extends SceneController
    implements Initializable {

    /**
     * skill points for piloting.
     */
    private transient int pilotSkill;
    /**
     * skill points for fighting.
     */
    private transient int fighterSkill;
    /**
     * skill points for trading.
     */
    private transient int traderSkill;
    /**
     * skill points for engineering.
     */
    private transient int engineerSkill;
    /**
     * skill points for investing.
     */
    private transient int investorSkill;

    @FXML
    private TextField nameText;
    @FXML
    private Text pilotSkillText, fighterSkillText, traderSkillText,
            engineerSkillText, investorSkillText;
    @FXML
    private Text skillPointsRemaining, confirmMessage;
    @FXML
    private Label pilotLabel, fighterLabel, traderLabel, engineerLabel, investorLabel;
    @FXML
    private ChoiceBox difficultyChoiceBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        difficultyChoiceBox.setItems(
                FXCollections.observableArrayList(
                        "Easy", "Medium", "Hard", "Ultra"));
        difficultyChoiceBox.setValue("Medium");
        confirmMessage.setFill(Color.TRANSPARENT);
    }
    
    @Override
    public void setMainControl(MainController mainControl) {
        super.setMainControl(mainControl);
        
        InformationPresenter.getInstance()
                .showTextOnHover(pilotLabel, "Determines how well you pilot your ship.\n " +
                "A good pilot can easily flee, dodge attacks,\n " +
                "and stay on the tail of fleeing ships.");
        InformationPresenter.getInstance()
                .showTextOnHover(fighterLabel, "Determines how well you handle your weapons.\n" +
                "A good fighter is great at hitting other ships.");
        InformationPresenter.getInstance()
                .showTextOnHover(traderLabel, "Determines what prices you must pay for trade goods, ships and equipment.\n" +
                "A good trader can reduce prices up to 10%.");
        InformationPresenter.getInstance()
                .showTextOnHover(engineerLabel, "Determines how well you keep your ship in shape.\n" +
                "A good engineer protects the hull and shields in a fight,\n" +
                "repairs them quicker during travel, and may even enhance his ship's weaponry.");
        InformationPresenter.getInstance()
                .showTextOnHover(investorLabel, "Determines how well you do in investing in the galactic markets.\n" +
                "Since stock prices and interest rates are volatile, \n" +
                "an investor could make a killing or lose his savings. Be wary.");
    }

    /**
     * Determines if there are still points left to allocate.
     *
     * @return true if there are still skill points left to allocate.
     */
    private boolean skillPointsRemaining() {
        return (pilotSkill + fighterSkill + traderSkill
                + engineerSkill + investorSkill) < 15;
    }

    /**
     * Updates the "points remaining" text in the View.
     */
    private void adjustPointsRemainingText() {
        int points = 15 - (pilotSkill + fighterSkill + traderSkill
                + engineerSkill + investorSkill);
        skillPointsRemaining.setText(((Integer) points).toString());
    }

    /**
     * Increase the pilot skill count when + is clicked.
     *
     * @param event the event which occurred
     */
    @FXML
    protected void increasePilot(final ActionEvent event) {
        if (skillPointsRemaining()) {
            pilotSkill++;
            pilotSkillText.setText(((Integer) pilotSkill).toString());
        }
        adjustPointsRemainingText();
    }

    /**
     * Decrease the pilot skill count when - is clicked.
     *
     * @param event the event which occurred
     */
    @FXML
    protected void decreasePilot(final ActionEvent event) {
        if (pilotSkill > 0) {
            pilotSkill--;
            pilotSkillText.setText(((Integer) pilotSkill).toString());
        }
        adjustPointsRemainingText();
    }

    /**
     * Increase the fighter skill count when + is clicked.
     *
     * @param event the event which occurred
     */
    @FXML
    protected void increaseFighter(final ActionEvent event) {
        if (skillPointsRemaining()) {
            fighterSkill++;
            fighterSkillText.setText(((Integer) fighterSkill).toString());
        }
        adjustPointsRemainingText();
    }

    /**
     * Decrease the fighter skill count when - is clicked.
     *
     * @param event the event which occurred
     */
    @FXML
    protected void decreaseFighter(final ActionEvent event) {
        if (fighterSkill > 0) {
            fighterSkill--;
            fighterSkillText.setText(((Integer) fighterSkill).toString());
        }
        adjustPointsRemainingText();
    }

    /**
     * Increase the trader skill count when + is clicked.
     *
     * @param event the event which occurred
     */
    @FXML
    protected void increaseTrader(final ActionEvent event) {
        if (skillPointsRemaining()) {
            traderSkill++;
            traderSkillText.setText(((Integer) traderSkill).toString());
        }
        adjustPointsRemainingText();
    }

    /**
     * Decrease the trader skill count when - is clicked.
     *
     * @param event the event which occurred
     */
    @FXML
    protected void decreaseTrader(final ActionEvent event) {
        if (traderSkill > 0) {
            traderSkill--;
            traderSkillText.setText(((Integer) traderSkill).toString());
        }
        adjustPointsRemainingText();
    }

    /**
     * Increase the engineer skill count when + is clicked.
     *
     * @param event the event which occurred
     */
    @FXML
    protected void increaseEngineer(final ActionEvent event) {
        if (skillPointsRemaining()) {
            engineerSkill++;
            engineerSkillText.setText(((Integer) engineerSkill).toString());
        }
        adjustPointsRemainingText();
    }

    /**
     * Decrease the engineer skill count when - is clicked.
     *
     * @param event the event which occurred
     */
    @FXML
    protected void decreaseEngineer(final ActionEvent event) {
        if (engineerSkill > 0) {
            engineerSkill--;
            engineerSkillText.setText(((Integer) engineerSkill).toString());
        }
        adjustPointsRemainingText();
    }

    /**
     * Increase the investor skill count when + is clicked.
     *
     * @param event the event which occurred
     */
    @FXML
    protected void increaseInvestor(final ActionEvent event) {
        if (skillPointsRemaining()) {
            investorSkill++;
            investorSkillText.setText(((Integer) investorSkill).toString());
        }
        adjustPointsRemainingText();
    }

    /**
     * Decrease the investor skill count when - is clicked.
     *
     * @param event the event which occurred
     */
    @FXML
    protected void decreaseInvestor(final ActionEvent event) {
        if (investorSkill > 0) {
            investorSkill--;
            investorSkillText.setText(((Integer) investorSkill).toString());
        }
        adjustPointsRemainingText();
    }

    @FXML
    protected void cancelDialogScreen(final ActionEvent event) {
        mainControl.goToWelcomeScreen();
    }

    @FXML
    protected void okDialogScreen(final ActionEvent event) {
        final String name = nameText.getCharacters().toString();
        if ("".equals(name)) {
            confirmMessage.setText("Please enter your name!");
            confirmMessage.setFill(Color.RED);
        } else if (skillPointsRemaining()) {
            confirmMessage.setText("Allocate all skill points!");
            confirmMessage.setFill(Color.RED);
        } else {
            final Player player = new Player(name, pilotSkill, fighterSkill,
                    traderSkill, engineerSkill, investorSkill);
            mainControl.setUpGame(player);

            confirmMessage.setText("Greetings Space Trader!");
            confirmMessage.setFill(Color.GREEN);
        }
    }

}
