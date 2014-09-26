/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.system;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class MarketPlaceController implements Initializable {
    private int quantity1;
    private int quantity2;
    private int quantity3;
    private int quantity1Price = 100 ;
    private int quantity2Price = 200;
    private int quantity3Price = 300;
    private int netBalance = 0;
    
    @FXML private Text netBalanceText;
    
            

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    @FXML protected void increasequantity1(ActionEvent event) {
        quantity1++;
        quantity1Price *= quantity1;
        netBalance = quantity1Price + quantity2Price + quantity3Price;
        netBalanceText.setText("" + netBalance);
        }
    @FXML protected void increasequantity2(ActionEvent event) {
        quantity2++;
        quantity2Price *= quantity2;
        netBalance = quantity1Price + quantity2Price + quantity3Price;
        netBalanceText.setText("" + netBalance);
        }
    @FXML protected void increasequantity3(ActionEvent event) {
        quantity3++;
        quantity3Price *= quantity3;
        netBalance = quantity1Price + quantity2Price + quantity3Price;
        netBalanceText.setText("" + netBalance);
        }
    }
    }

    
}
