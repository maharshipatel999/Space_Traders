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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.controlsfx.control.PopOver;
import spacetrader.Mercenary;
import static spacetrader.Mercenary.createRandomMercenary;
import spacetrader.Player;
import spacetrader.SkillList;
import spacetrader.SkillList.Skill;

/**
 * FXML Controller class
 *
 * @author Seth
 */
public class CrewInfoScreenController
extends SceneController
implements Initializable {
    @FXML 
    private HBox crewBox1, crewBox2, crewBox3;
    
    @FXML
    private HBox mercenaryForHire;
    
    @FXML
    private Label merc1, merc2,  merc3;
    
    @FXML
    private Label noMercForHire;
    
    @FXML
    private Label mercForHire;
   
    private HBox[] crewBoxes;
    private Label[] crewLabels;
    
    private Player player;
    private Mercenary[] crew;
    
    
    
    public void setUpCrewInfoScreen(Player player) {
        this.player = player;
        this.crewBoxes = new HBox[] {crewBox1, crewBox2, crewBox3};
        this.crewLabels = new Label[] {merc1, merc2, merc3};
        displayCrewInfo();
    }
    
    /**
     * Add pop overs to mercenary labels
     */
    private void setPopOvers() {
        for (int i = 0; i < player.getShip().getCrew().length; i++) {
            Mercenary merc = player.getShip().getCrew()[i];
            Pane mercPopOver = this.createMercInfoPane(merc);
            InformationPresenter.getInstance()
                    .showNodeOnHover(crewLabels[i], mercPopOver);
        }
        Mercenary merc = player.getLocation().getMercenary();
        if (merc != null) {
            Pane mercPopOver = this.createMercInfoPane(merc);
            InformationPresenter.getInstance()
                    .showNodeOnHover(mercForHire, mercPopOver);
        }
        
    }
    
    /**
     * Makes a pane that displays mercenary information.
     * 
     * @param merc the mercenary who's info is being displayed
     * @return a popover pane displaying mercenary info
     */
    public Pane createMercInfoPane(Mercenary merc) {
        final int PANE_SPACING = 20;
        final int NAME_SIZE = 25;
        final int LABEL_SIZE = 20;
        
        //create and format vbox
        VBox box = new VBox(PANE_SPACING);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10, 10, 10, 10));
        //add mercenary name label
        Label nameLabel = new Label(merc.getName());
        nameLabel.setFont(new Font(NAME_SIZE));
        //add home planet label
        Label originLabel = new Label("Home Planet: " + merc.getHomePlanet().getName());
        originLabel.setFont(new Font(LABEL_SIZE));
        
        //add skills labels
        box.getChildren().addAll(nameLabel, originLabel);
        for (int i = 0; i < Skill.values().length; i++) {
            Skill skill = Skill.values()[i];
            Label label = new Label(skill.type() + ": " + merc.getSkill(skill));
            label.setFont(new Font(LABEL_SIZE));
            box.getChildren().add(label);
        }
        //Add daily wage label
        Label wageLabel = new Label("Daily Wage: ₪" + merc.calculateDailyWage(merc));
        wageLabel.setFont(new Font(LABEL_SIZE));
        box.getChildren().add(wageLabel);
        
        return box;
    }
    
    /**
     * Updates the labels and buttons for all the crew and the mercenary for hire.
     */
    public void displayCrewInfo() {
        
        crew = player.getShip().getCrew();
        int crewSize = crew.length;
        if (player.getLocation().getMercenary() == null) {
            mercenaryForHire.setVisible(false);
        } else {
            mercForHire.setText(player.getLocation().getMercenary().getName());
            noMercForHire.setVisible(false);
        }
        for(int i = 0; i < crewSize; i++) {
            crewBoxes[i].setVisible(true);
            crewLabels[i].setText(crew[i].getName());
        }
        for (int i = crewSize; i < crewBoxes.length; i++) {
            crewBoxes[i].setVisible(false);
        }
        this.setPopOvers();
        
    }
    @FXML
    protected void fireMerc1() {
        player.getShip().fireMercenary(crew[0]);
        displayCrewInfo();
    }
     @FXML
    protected void fireMerc2() {
         player.getShip().fireMercenary(crew[1]);
        displayCrewInfo();
    }
     @FXML
    protected void fireMerc3() {
        player.getShip().fireMercenary(crew[2]);
        displayCrewInfo();
    }
     @FXML
    protected void hireMerc() {
        boolean response = false;
        String hireMercMessage
        = "Are you sure you want to hire this mercenary for a daily wage of"
        + " ₪"+ player.getLocation().getMercenary().calculateDailyWage(player.getLocation().getMercenary());
        if (player.getCredits() > player.getLocation().getMercenary().calculateDailyWage(player.getLocation().getMercenary())) {
            response = mainControl.displayYesNoConfirmation(
            "Mercenary Hire Confirmation", null, hireMercMessage);
            if (response) {
                player.getShip().hireMercenary(player.getLocation().getMercenary());
                displayCrewInfo();
            }
        } else {
            mainControl.displayErrorMessage("Error", "You don't have enough credits to hire this mercenary","You don't have enough credits to hire this mercenary" , null);
        }
    }
    @FXML
    protected void goBackToShipYardScreen() {
        mainControl.goToShipYardScreen();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
