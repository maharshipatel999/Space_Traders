/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.travel;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import spacetrader.system.MainController;

/**
 * Represents the abstract state of an encounter
 *
 * @author Caleb
 */
public abstract class EncounterState {

    public static final int BUTTON_WIDTH = 115;
    public static final int BUTTON_HEIGHT = 31;

    protected final Encounter encounter;
    protected final MainController mainControl = null;

    public EncounterState(Encounter encounter) {
        this.encounter = encounter;
    }

    /**
     * Create a button with the provided text, format it, and then add it to the
     * provided pane.
     *
     * @param text the text to display on the button
     * @param buttonBar the pane to add the button to
     * @return the button that was created
     */
    protected static Button addButton(String text, Pane buttonBar) {
        Button button = new Button(text);
        button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setFont(new Font(15));
        button.setCursor(Cursor.HAND);

        buttonBar.getChildren().add(button);
        return button;
    }

    /**
     * Adds buttons to the provided hbox for each action the player is able to
     * do in this EncounterState.
     *
     * @param sceneControl the controller for the current view
     * @param buttonBar the hbox which needs its buttons refreshed
     */
    public abstract void setButtons(EncounterScreenController sceneControl,
            HBox buttonBar);

    /**
     * Gets the text representing the opponent's next action, which will be
     * displayed on the View.
     *
     * @return the opponent's next action
     */
    public abstract String getNextActionText();
    
    public void playerAttacks(EncounterScreenController encounterControl, boolean playerIntimidated) {
        throw new UnsupportedOperationException("Player cannot attack in current state.");
    }

    public void playerFlees(EncounterScreenController encounterControl) {
        throw new UnsupportedOperationException("Player cannot flee in current state.");
    }

    public void playerSurrenders() {
        throw new UnsupportedOperationException("Player cannot surrender in current state.");
    }

    public void playerTrades() {
        throw new UnsupportedOperationException("Player cannot trade in current state.");
    }

    public void playerSubmitsToInspection() {
        throw new UnsupportedOperationException("Player cannot trade in current state.");
    }
}
