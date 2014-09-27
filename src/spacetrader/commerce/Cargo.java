/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.commerce;

import java.util.HashMap;
import java.util.Map.Entry;
import spacetrader.ShipType;

/**
 *
 * @author nkaru_000
 */
public class Cargo {
    
    private final HashMap<TradeGood, Integer> tradeGoods;
    private ShipType shipType;
    
    public Cargo(ShipType shipType) {
        this.tradeGoods = new HashMap<>();
        this.shipType = shipType;
        //add enum items to the map
    }
    
    public void addItem(TradeGood tradeGood, int quantity) {
        if (isFull()) {
            //TODO
        }
        int currentQuantity = getQuantityOfGood(tradeGood);
        int newQuantity = currentQuantity + quantity;
        this.tradeGoods.put(tradeGood, newQuantity);
    } 
    
    public void addMultipleItems(HashMap<TradeGood, Integer> newGoods) {
        if (isFull()) {
            //TODO
        }
        for (Entry<TradeGood, Integer> item : newGoods.entrySet()) {
            addItem(item.getKey(), item.getValue());
        }
    }
    
    public void removeItem(TradeGood tradeGood, int quantity) {
        int currentQuantity = getQuantityOfGood(tradeGood);
        int newQuantity = currentQuantity - quantity;
        if (newQuantity < 0) {
            //TODO
        }
        this.tradeGoods.put(tradeGood, newQuantity);
    }
    
    public void removeMulitpleItems(HashMap<TradeGood, Integer> goods) {
        for (Entry<TradeGood, Integer> item : goods.entrySet()) {
            removeItem(item.getKey(), item.getValue());
        }
    }
    
    public boolean isFull() {
        return (tradeGoods.size() >= shipType.numCargoSlots());
    }
    
    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }
    
    public int getQuantityOfGood(TradeGood tradeGood) {
        return this.tradeGoods.get(tradeGood);
    }
}
