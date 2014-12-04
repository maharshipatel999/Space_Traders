/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.system;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.net.URL;
import javafx.scene.media.AudioClip;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import spacetrader.Player;
import spacetrader.ships.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.util.ArrayList;
import javafx.scene.image.Image;
import java.io.*;
import java.util.Random;
import javax.imageio.ImageIO;
import javafx.scene.input.KeyEvent;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import spacetrader.system.SceneController;
import javafx.scene.Scene;
import javafx.event.EventHandler;
import spacetrader.planets.Planet;

/**
 * FXML Controller class
 *
 * @author Tejas
 */
public class MeteorScreenController extends SceneController implements Initializable {

    private Player player;
    private ShipType shipType;
    private Scene currScene;
    private int health;
    private int totalHealth;
    private ArrayList<ImageView> boulders;
    private ArrayList<ImageView> gameObjects;
    private boolean win, lose;
    @FXML
    private Text playerName, playerHealth, playerTotalHealth, playerShipType;

    @FXML
    private ImageView blastOne, blastTwo, winner, death, playership, boulderOne, boulderTwo, boulderThree, boulderFour, boulderFive, boulderSix, boulderSeven, boulderEight, boulderNine, boulderTen, boulderEleven, boulderTwelve, boulderThirteen, boulderFourteen, boulderFifteen, boulderSixteen, boulderSeventeen, boulderEighteen, boulderNineteen, boulderTwenty, boulderTwentyOne;
    
    Planet source, destination;
    
    int blastCounterOne, blastCounterTwo;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        blastCounterOne = 11;
        blastOne.setVisible(false);
        blastTwo.setVisible(false);
        win = false;
        lose = false;
        totalHealth = 100;
        health = 100;
        
        
        gameObjects = new ArrayList<ImageView>();
        boulders = new ArrayList<ImageView>();
        boulders.add(boulderOne);
        boulders.add(boulderTwo);
        boulders.add(boulderThree);
       boulders.add(boulderFour);
        boulders.add(boulderFive);
        boulders.add(boulderSix);
        boulders.add(boulderSeven);
        boulders.add(boulderEight);
        boulders.add(boulderNine);
        boulders.add(boulderTen);
        boulders.add(boulderEleven);
        boulders.add(boulderTwelve);
        boulders.add(boulderThirteen);
        boulders.add(boulderFourteen);
        boulders.add(boulderFifteen);
        boulders.add(boulderSixteen);
        boulders.add(boulderSeventeen);
        boulders.add(boulderEighteen);
        boulders.add(boulderNineteen);
        boulders.add(boulderTwenty);
        boulders.add(boulderTwentyOne);
        death.setVisible(false);
        winner.setVisible(false);
        gameObjects.addAll(boulders);
        gameObjects.add(playership);
        Random r = new Random();    
        for(int i = 0; i < boulders.size(); i++) {
            boulders.get(i).setVisible(true);
            //boulders.get(i).setLayoutX(200 + Math.abs(25*(r.nextInt()%13)));
            //boulders.get(i).setLayoutY(25 + Math.abs(25*(r.nextInt()%13)));            
            TranslateTransition tt = new TranslateTransition();
            tt.setDuration(Duration.millis(3000));
            tt.setNode(boulders.get(i));
            tt.setFromX(boulders.get(i).getTranslateX());
            tt.setToX(boulders.get(i).getTranslateX() + 200 + Math.abs(25*(r.nextInt()%13)));
            tt.setFromY(boulders.get(i).getTranslateY());
            tt.setToY(boulders.get(i).getTranslateY() + 25 + Math.abs(25*(r.nextInt()%13)));
            tt.play();
            System.out.println("Boulder " + i + " X is " + boulders.get(i).getTranslateX());
            System.out.println("Boulder " + i + " Y is " + boulders.get(i).getTranslateY());
        }   
    }    
    
    public void setPlayer(Player p, Planet src, Planet dest) {
        player = p;
        shipType = p.getShip().getType();
        totalHealth = p.getShip().getMaxHullStrength();
        health = p.getShip().getHullStrength();
        source = src;
        destination = dest;
        playership.setImage(new Image(p.getShip().getType().spriteFile()));
        TranslateTransition tt = new TranslateTransition();
        tt.setDuration(Duration.millis(3000));
        tt.setNode(playership);
        tt.setFromY(playership.getTranslateY());
        tt.setToY(playership.getTranslateY() + 25);
        tt.play();
        

        updateStats();
    }  
    
    private void updateStats() {
        
        playerName.setText(player.getName());
        playerShipType.setText(player.getShip().getType().name());
        playerTotalHealth.setText("" + totalHealth);
        playerHealth.setText("" + health);
    }
    
    public void setMeteorScreen(Scene s) {
        currScene = s;
    }
    

    
    public void runGame() {

        currScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String keyPressed = keyEvent.getCode().toString();
                doMoveSequence(keyPressed);
                interact();
                updateHealth();
                if(win == true) {
                    System.out.println("You win Jim!");
                    //winner.setVisible(true);
                    int i = 0;
                    URL kl = getClass().getResource("proceed.mp3");
                    Media someSound = new Media(kl.toString());
                    MediaPlayer mp = new MediaPlayer(someSound);
                    mp.play();

                    mainControl.displayInfoMessage("LANDING", "Prepare for landing on Planet... ", destination.getName());
                    mainControl.arriveAtPlanet(destination);
                    
                } else if (lose == true) {
                    System.out.println("You lose Joe!");
                    death.setVisible(true);
                    URL l = getClass().getResource("explosion.mp3");
                    Media somSound = new Media(l.toString());
                    MediaPlayer mp = new MediaPlayer(somSound);
                    mp.play();
                    player.setCredits(player.getCredits()/4);
                    player.getShip().setHullStrength(player.getShip().getMaxHullStrength()/20);
                    mainControl.displayInfoMessage("CRASH!!", "You have Crashed.  ", "You lose 3/4 of your credits and your hull strength has sharply decreased. You have been rescured by citizens of the nearby planet " + destination.getName() + ". \nCREDITS: " + player.getCredits() + "\nHULL STRENGTH: " + player.getShip().getHullStrength());
                    mainControl.arriveAtPlanet(destination);
                }
            }
            
        }
        );
    }

    public void updateHealth() {
        playerHealth.setText("" + health);
       
    }
    
    
    public void interact() {
                    boolean hit = false;

        for(int i = 0; i < boulders.size(); i++) {
            double centerBoulderX = boulders.get(i).getTranslateX();
            double centerBoulderY = boulders.get(i).getTranslateY();
            if(playership.getTranslateX() < centerBoulderX + 10 && playership.getTranslateX() > centerBoulderX -10 && playership.getTranslateY() > centerBoulderY -10 && playership.getTranslateY() < centerBoulderY + 10 && boulders.get(i).isVisible()) {
                player.getShip().setHullStrength(player.getShip().getHullStrength()-10);
                health = health - 10;
                boulders.get(i).setVisible(false);
                hit = true;
            }
            
       }                    
            if(hit) {
                System.out.println("hit!");
                blastOne.setVisible(true);
                blastTwo.setVisible(true);
            }  else {
                System.out.println("not!");

                blastOne.setVisible(false);
                blastTwo.setVisible(false);                

            }
        
        if(playership.getTranslateX() > 525) {
            win = true;
        }
        if(player.getShip().getHullStrength() <= 0) {
            lose = true;
        }
        
        
    }
    
    public void doMoveSequence(String s) {
        String a = s.toUpperCase();
        
        try {
            Thread.sleep(30);
        } catch (Exception e) {
            
        }
        TranslateTransition tt = new TranslateTransition();
        boolean moved = false;
        if(a.equals("W")) {
            if(playership.getTranslateY() > 45) {
                tt.setDuration(Duration.millis(30));
                tt.setNode(playership);
                tt.setFromY(playership.getTranslateY());
                tt.setToY(playership.getTranslateY() - 25.00);
                tt.play();
                moved = true;
            }
        } else if (a.equals("A")) {
            if(playership.getTranslateX() > 10) {
                tt.setDuration(Duration.millis(30));
                tt.setNode(playership);
                tt.setFromX(playership.getTranslateX());
                tt.setToX(playership.getTranslateX() - 25.00);
                tt.play(); 
                moved = true; 
            }    
        } else if (a.equals("S")) {
            if(playership.getTranslateY() < 357) {
                tt.setDuration(Duration.millis(30));
                tt.setNode(playership);
                tt.setFromY(playership.getTranslateY());
                tt.setToY(playership.getTranslateY() + 25.00);
                tt.play();
                moved = true;
            }
        } else if (a.equals("D")) {
            tt.setDuration(Duration.millis(30));
            tt.setNode(playership);
            tt.setFromX(playership.getTranslateX());
            tt.setToX(playership.getTranslateX() + 25.00);
            tt.play();
            moved = true;
            
        }
        System.out.println(playership.getTranslateX());
        if(moved) {
            moveBoulders();
        }

    }
    
    public void moveBoulders() {
        for(int i = 0; i < boulders.size(); i++) {
           Random r = new Random();
           int XspotDesired = r.nextInt()%8;
           double xDisplacement = 0;
           double yDisplacement = 0;
           double xDistance = 0;
           double yDistance = 0;
           int xChoice = 0;
           if(XspotDesired <= 0) {
               xDisplacement = boulders.get(i).getTranslateX() - playership.getTranslateX();
               xChoice = 1;
           } else if (XspotDesired < 4) {
               xDisplacement = boulders.get(i).getTranslateX() - playership.getTranslateX() - 25;
               xChoice = 2;
           } else {
               xDisplacement = boulders.get(i).getTranslateX() - playership.getTranslateX() + 25;
               xChoice = 3;
           } 
           int YspotDesired = r.nextInt()%3;
           if(xChoice !=1) {
               YspotDesired += 3;
           }
           if(YspotDesired <= 0) {
               yDisplacement = boulders.get(i).getTranslateY() - playership.getTranslateY() + 25;
           } else if (YspotDesired < 4) {
               yDisplacement = boulders.get(i).getTranslateY() - playership.getTranslateY() - 25;
           } else {
               yDisplacement = boulders.get(i).getTranslateY() - playership.getTranslateY();
           } 
           xDistance = Math.abs(xDisplacement);
           yDistance = Math.abs(yDisplacement);
           boolean conditionalStatement = false;
           if(i >= boulders.size() * 2/3) {
               conditionalStatement = xDistance <= yDistance;
           } else {
               conditionalStatement = xDistance >= yDistance;
           }
           if(conditionalStatement) {
               System.out.println("Spaceship X " + playership.getTranslateX());
               System.out.println("Boulder " + i + ": " + boulders.get(i).getTranslateX());

               TranslateTransition tt = new TranslateTransition();
               if(xDisplacement <= 0) {
                    tt.setDuration(Duration.millis(30));
                    tt.setNode(boulders.get(i));
                    tt.setFromX(boulders.get(i).getTranslateX());
                    tt.setToX(boulders.get(i).getTranslateX() + 25.00);
                    tt.play();
               } else {
                    tt.setDuration(Duration.millis(30));
                    tt.setNode(boulders.get(i));
                    tt.setFromX(boulders.get(i).getTranslateX());
                    tt.setToX(boulders.get(i).getTranslateX() - 25.00);
                    tt.play();                   
               }   
           } else {
               System.out.println("Spaceship Y " + playership.getTranslateY());
               System.out.println("Boulder " + i + ": " + boulders.get(i).getTranslateY());

               TranslateTransition tt = new TranslateTransition();
               if(yDisplacement <= 0) {
                    tt.setDuration(Duration.millis(30));
                    tt.setNode(boulders.get(i));
                    tt.setFromY(boulders.get(i).getTranslateY());
                    tt.setToY(boulders.get(i).getTranslateY() + 25.00);
                    tt.play();                  
               } else {
                     tt.setDuration(Duration.millis(30));
                    tt.setNode(boulders.get(i));
                    tt.setFromY(boulders.get(i).getTranslateY());
                    tt.setToY(boulders.get(i).getTranslateY() - 25.00);
                    tt.play();                    
               }                
           }
        }
    }
    
    
}
