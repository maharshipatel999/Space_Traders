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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
 *
 * @author Caleb
 */
public class InformationPresenter {
    private Stage stage;
    
    public InformationPresenter(Stage stage) {
        this.stage = stage;
    }
    
    /**
     * Display alert message based on passed in string.
     *
     * @param header title of state
     * @param message alert message
     */
    public void displayAlertMessage(String header, String message) {
        /*Alert dialog = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
        dialog.setTitle("Information");
        dialog.setResizable(true);
        dialog.setHeaderText(header);
        dialog.initStyle(StageStyle.UTILITY);*/

        Dialogs.create()
                .owner(stage)
                .title(header)
                //.masthead(null)
                .message(message)
                .showInformation();
    }
    
    /**
     * Display yes-no confirmation.
     *
     * @param optionsTitle title of state
     * @param mastHead
     * @param message message to display
     * @return true if the player confirmed yes, otherwise false
     */
    public boolean displayYesNoConfirmation(String optionsTitle,
            String mastHead, String message) {
        
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.NO, ButtonType.YES);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle(optionsTitle);
        if (mastHead != null) {
            dialog.setHeaderText(mastHead);
        }
        Optional<ButtonType> result = dialog.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;

        /*Action response = Dialogs.create()
                .owner(stage)
                .title(optionsTitle)
                .masthead(mastHead)
                .message(message)
                .actions(Dialog.Actions.YES, Dialog.Actions.NO)
                .showConfirm();
        return response;*/
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
                .owner(stage)
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
    public void displayAdminCheats(AdminCheats cheats, Stage cheatStage, EventHandler<ActionEvent> doneAction) {
        ObservableList<PropertySheet.Item> list = FXCollections.observableArrayList();
        try {
            for (String var : new String[]{"initialCredits", "techLevel", "politicalSystem", "resource", "startingShip", "policeRecord", "reputation", "debugMode"}) {
                list.add(new BeanProperty(cheats, new PropertyDescriptor(var, cheats.getClass())));
            }
        } catch (IntrospectionException ex) {
            Logger.getLogger(WelcomeScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Create Stage
        cheatStage.initOwner(stage);
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
    public PopOver createPopOver(String message) {
        Label text = new Label(message);
        text.setPadding(new Insets(15, 15, 15, 15));

        PopOver popup = new PopOver(text);
        popup.getContentNode().setUserData(Boolean.FALSE);
        return popup;
    }
    
    public void showTextOnHover(Node node, String message) {
        PopOver popup = createPopOver(message);
        node.setOnMouseEntered((e) -> popup.show(node));
        node.setOnMouseExited((e) -> popup.hide(new Duration(200)));
    }
}
