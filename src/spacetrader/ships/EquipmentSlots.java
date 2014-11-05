/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.ships;

import java.io.Serializable;
import java.util.ArrayList;
import spacetrader.exceptions.SlotsAreFullException;

/**
 * Represents a container for ship equipment. This is backed by an ArraylList.
 *
 * @author Caleb Stokols
 */
public class EquipmentSlots<T> implements Serializable {

    private ArrayList<T> list;
    private int numSlots;
    int size;

    /**
     * Creates new Equipments with a given initial capacity.
     *
     * @param numSlots the initial number of slots
     */
    public EquipmentSlots(int numSlots) {
        this.list = new ArrayList<T>();
        this.numSlots = numSlots;
        size = 0;
    }

    /**
     * Adds an item to the next open slot. If there are no open slots, throws an
     * exception.
     *
     * @param item the equipment to add to a slot.
     */
    public void addItem(T item) {
        if (isFull()) {
            throw new SlotsAreFullException();
        } else {
            list.add(item);
            size = size + 1;
        }
    }

    /**
     * Removes an item to the next open slot.
     *
     * @param item the equipment to add to a slot.
     */
    public void removeItem(T item) {
        if (isEmpty()) {
            throw new SlotsAreFullException();
        } else {
            list.remove(0);
            size = size - 1;
        }
    }

    /**
     * Gets the item at a slot specified by its index.
     *
     * @param index the index of the slot to looked at
     * @return the item at the given index
     */
    public T getItem(int index) {
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        } else {
            throw new IndexOutOfBoundsException("Index is out legal range");
        }
    }

    /**
     * Replaces a item in a slot specified by its index.
     *
     * @param index the index of the slot which should be replaced
     * @param item the item at the given index
     */
    public void replaceItem(int index, T item) {
        if (index > 0 && index < list.size()) {
            list.remove(index);
            list.add(index, item);
        } else {
            throw new IndexOutOfBoundsException("Index is out legal range");
        }

    }

    /**
     * Increases the number of available slots.
     */
    public void addSlot() {
        numSlots++;
    }

    /**
     * Decreases the number of available slots
     */
    public void removeSlot() {
        numSlots--;
    }

    /**
     * Determines the number of filled slots.
     *
     * @return the amount of filled slots.
     */
    public int getNumFilledSlots() {
        return size;
    }

    /**
     * Gets the total number of equipment slots.
     *
     * @return the total number of slots.
     */
    public int getNumSlots() {
        return numSlots;
    }

    /**
     * Determines if there are no remaining empty slots.
     *
     * @return true if every slot is full, otherwise false
     */
    public boolean isFull() {
        return size >= numSlots;
    }

    /**
     * Determines if arrayList is empty
     *
     * @return whether arrayList Empty or not
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Empties every slot.
     */
    public void clear() {
        list.clear();
    }
}
