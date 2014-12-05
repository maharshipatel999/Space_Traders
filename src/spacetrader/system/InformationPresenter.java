/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.system;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.dialog.Dialogs;
import org.controlsfx.property.BeanProperty;

/**
 * A Singleton class that displays dialogs.
 * 
 * @author Caleb
 */
public class InformationPresenter {

    private static InformationPresenter instance;
    private Stage stage;
    
    public static InformationPresenter getInstance() {
        if (instance == null) {
            instance = new InformationPresenter();
        }
        return instance;
    }
    
    /**
     * Sets the stage the InformationPresenter will use.
     * 
     * @param stage a stage that will own created windows.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    /**
     * Gets his InformationPresenter's stage
     */ 
    private Stage getStage() {
        if (stage == null) {
            throw new IllegalStateException("Stage has not been set yet.");
        } else {
            return stage;
        }
    }

    /**
     * Display information message based on passed in string.
     *
     * @param msgTitle the value of msgTitle
     * @param header title of state
     * @param message alert message
     * @param args Arguments referenced by the format specifiers in the message
     * string. If there are more arguments than format specifiers, the extra 
     * arguments are ignored. The number of arguments is variable and may be zero.
     */
    public void displayInfoMessage(String msgTitle, String header,
            String message, Object ... args) {
        displayMessage(AlertType.INFORMATION,
            (msgTitle != null ? msgTitle : "Information"), header, message, args);
    }

    /**
     * Display warning message based on passed in string.
     *
     * @param msgTitle the value of msgTitle
     * @param header title of state
     * @param message alert message
     * @param args Arguments referenced by the format specifiers in the message
     * string. If there are more arguments than format specifiers, the extra 
     * arguments are ignored. The number of arguments is variable and may be zero.
     */
    public void displayWarningMessage(String msgTitle, String header,
            String message, Object ... args) {
        displayMessage(AlertType.WARNING,
                msgTitle != null ? msgTitle : "Warning", header, message);
    }

    /**
     * Display error message based on passed in string.
     *
     * @param msgTitle the value of msgTitle
     * @param header title of state
     * @param message alert message
     * @param args Arguments referenced by the format specifiers in the message
     * string. If there are more arguments than format specifiers, the extra 
     * arguments are ignored. The number of arguments is variable and may be zero.
     */
    public void displayErrorMessage(String msgTitle, String header,
            String message, Object ... args) {
        displayMessage(AlertType.ERROR,
                msgTitle != null ? msgTitle : "Error", header, message);
    }
    
    private void displayMessage(AlertType alertType, String msgTitle,
            String header, String message, Object ... args) {
        Alert dialog = createAlert(alertType, String.format(message, args));
        dialog.setTitle(msgTitle);
        if (header != null) {
            dialog.setHeaderText(header);
        }
        dialog.showAndWait();
    }

    /**
     * Display a dialog with options.
     *
     * @param optionsTitle title of state
     * @param mastHead
     * @param message message to display
     * @param buttonNames the buttons that the player can choose from
     * @return true if the player confirmed yes, otherwise false
     */
    public Optional<ButtonType> displayOptionsDialog(String optionsTitle,
            String mastHead, String message, String... buttonNames) {
        ButtonType[] buttons = new ButtonType[buttonNames.length];
        int size = 0;
        for (String name : buttonNames) {
            buttons[size++] = new ButtonType(name);
        }

        return displayOptionsDialog(optionsTitle, mastHead, message, buttons);
    }

    /**
     * Display a dialog with options.
     *
     * @param optionsTitle title of state
     * @param mastHead
     * @param message message to display
     * @param buttons the buttons that the player can choose from
     * @return true if the player confirmed yes, otherwise false
     */
    public Optional<ButtonType> displayOptionsDialog(String optionsTitle,
            String mastHead, String message, ButtonType... buttons) {
        Alert dialog = createAlert(Alert.AlertType.CONFIRMATION, message, buttons);
        dialog.setTitle(optionsTitle);
        if (mastHead != null) {
            dialog.setHeaderText(mastHead);
        }

        Optional<ButtonType> result = dialog.showAndWait();
        return result;
    }
    
    /**
     * Display yes-no confirmation.
     *
     * @param optionsTitle title of state
     * @param mastHead the header
     * @param message message to display
     * @param args Arguments referenced by the format specifiers in the message
     * string. If there are more arguments than format specifiers, the extra
     * arguments are ignored. The number of arguments is variable and may be
     * zero.
     * @return true if the player confirmed yes, otherwise false
     */
    public boolean displayYesNoConfirmation(String optionsTitle, String mastHead,
            String message, Object... args) {
        message = String.format(message, args);
        Optional<ButtonType> result = InformationPresenter.getInstance().displayOptionsDialog(
                optionsTitle, mastHead, message, ButtonType.NO, ButtonType.YES);
        return result.isPresent() && result.get() == ButtonType.YES;
    }

    /**
     * Display custom confirmation.
     *
     * @param optionsTitle title of state
     * @param mastHead the header
     * @param message message to display
     * @param buttonNames the names of the buttons the player can choose from
     * @return true if the player confirmed yes, otherwise false
     */
    public String displayCustomConfirmation(String optionsTitle, String mastHead,
            String message, String... buttonNames) {
        Optional<ButtonType> result = InformationPresenter.getInstance().displayOptionsDialog(
                optionsTitle, mastHead, message, buttonNames);
        if (!result.isPresent()) {
            return "";
        }
        for (String name : buttonNames) {
            if (name.equals(result.get().getText())) {
                return name;
            }
        }
        return "";

    }

    /**
     * Creates a new Alert.
     *
     * @param alertType the type of alert
     * @param contentText the message
     * @param buttons the buttons the user can choose from
     * @return the new Alert
     */
    public Alert createAlert(AlertType alertType, String contentText,
            ButtonType... buttons) {
        Label content = new Label(contentText);
        content.setMaxWidth(Double.MAX_VALUE);
        content.setMaxHeight(Double.MAX_VALUE);
        content.getStyleClass().add("content");
        content.setWrapText(true);
        content.setPrefWidth(360);
        content.setPadding(new Insets(10, 0, 0, 10));

        Alert dialog = new Alert(alertType, contentText, buttons);
        Group labelGroup = new Group(content);
        dialog.getDialogPane().setContent(labelGroup);
        dialog.initStyle(StageStyle.UTILITY);

        return dialog;
    }

    /**
     * Display progress bar.
     *
     * @param progressTitle title of state
     * @param service
     */
    public void displayProgess(String progressTitle,
            Service service) {
        Dialogs.create()
                .owner(getStage())
                .title(progressTitle)
                .showWorkerProgress(service);

        service.start();
    }

    /**
     * This is for displaying save progress to the player
     *
     * @param progressTitle the popup window title
     * @param updateMessage the message that is displayed over the progress bar
     * @param finishMessage the message displayed when finished
     * @param doneEvent the action to do on completion of progress bar
     */
    public void displaySaveProgress(String progressTitle, String updateMessage,
            String finishMessage, EventHandler<WorkerStateEvent> doneEvent) {
        Service<Void> saveService = new Service<Void>() {
            @Override
            public Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    public Void call() throws InterruptedException {
                        updateMessage(updateMessage);
                        updateProgress(0, 100);
                        for (int i = 0; i < 100; i++) {
                            Thread.sleep(10);
                            updateProgress(i + 1, 100);
                        }
                        updateMessage(finishMessage);
                        return null;
                    }
                };
            }
        };
        saveService.setOnSucceeded(doneEvent);
        displayProgess(progressTitle, saveService);
    }

    /**
     * Shows options to set different values of the game.
     *
     * @param cheats the AdminCheats object which will be used
     * @param cheatStage the stage that the pop-up will be presented in
     * @param doneAction the action to do when the done button is pressed
     */
    public void displayAdminCheats(AdminCheats cheats, Stage cheatStage,
            EventHandler<ActionEvent> doneAction) {
        ObservableList<PropertySheet.Item> list = FXCollections.observableArrayList();
        try {
            for (String var : new String[] {"initialCredits", "techLevel",
                "politicalSystem", "resource", "startingShip", "policeRecord",
                "reputation", "numMercenaries", "debugMode"}) {
                list.add(new BeanProperty(cheats, new PropertyDescriptor(var, cheats.getClass())));
            }
        } catch (IntrospectionException ex) {
            Logger.getLogger(WelcomeScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Create Stage
        cheatStage.initOwner(getStage());
        cheatStage.initModality(Modality.WINDOW_MODAL);

        //Create Pane
        VBox pane = new VBox();
        pane.setAlignment(Pos.BOTTOM_RIGHT);
        
        //Create Property Sheet
        PropertySheet propertySheet = new PropertySheet(list);
        propertySheet.setModeSwitcherVisible(false);
        propertySheet.setSearchBoxVisible(false);

        //Create Done Button
        Button doneButton = new Button("Done");
        doneButton.setOnAction(doneAction);
        doneButton.setDefaultButton(true);

        //add nodes to pane, and set the pane to the stage's scene.
        pane.getChildren().addAll(propertySheet, doneButton);
        Scene scene = new Scene(pane);
        
        cheatStage.setScene(scene);
        cheatStage.show();
    }

    /**
     * Creates a PopOver with the given text.
     *
     * @param message the text to be displayed
     * @return a new PopOver with the provided message
     */
    public PopOver createTextPopOver(String message) {
        Label text = new Label(message);
        text.setPadding(new Insets(10, 15, 10, 15));

        PopOver popup = new PopOver(text);
        popup.getContentNode().setUserData(Boolean.FALSE);
        return popup;
    }

    /**
     * Creates a new popover that displays the provided message. Sets the
     * popover to display whenever the mouse hovers over the provided node.
     * Returns the new popover
     *
     * @param node the node which displays the text when the mouse enters it
     * @param message the text to display on the popover
     * @return the newly created PopOver
     */
    public PopOver showTextOnHover(Node node, String message) {
        PopOver popup = createTextPopOver(message);
        //uses addEventHandler, so as not to overwrite pre-existing event handlers
        node.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> popup.show(node));
        node.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> popup.hide(new Duration(200)));
        return popup;
    }

    /**
     * Creates a new popover that displays the provided content node. Sets the
     * popover to display whenever the mouse hovers over the provided node.
     *
     * @param node the node which displays the text when the mouse enters it
     * @param content the content for the PopOver that is displayed
     * @return the newly created PopOver
     */
    public PopOver showNodeOnHover(Node node, Node content) {
        PopOver popup = new PopOver(content);
        node.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> popup.show(node));
        node.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> popup.hide(new Duration(200)));
        return popup;
    }

}
