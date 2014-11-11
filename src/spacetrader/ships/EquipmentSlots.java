/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.ships;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import spacetrader.exceptions.SlotsAreEmptyException;
import spacetrader.exceptions.SlotsAreFullException;

/**
 * Represents a container for ship equipment. This is backed by an ArraylList.
 *
 * @author Caleb Stokols
 * @param <T>
 */
public class EquipmentSlots<T extends Equipment> implements Serializable, Iterable<T> {

    private final ArrayList<T> list;
    private int numSlots;
    int size;

    /**
     * Creates new Equipments with a given initial capacity.
     *
     * @param numSlots the initial number of slots
     */
    public EquipmentSlots(int numSlots) {
        this.list = new ArrayList<>();
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
     * removes an item of the same type from the equipment slot
     *
     * @param item the item to be removed
     */
    public void removeItemOfSameType(T item) {
        T listItem = null;
        for (int i = 0; i < list.size(); i++) {
            if (item.equals(list.get(i))) {
                listItem = list.get(i);
                break;
            }
        }
        if (item != null) {
            removeItem(listItem);
        }
    }

    /**
     * Removes an item to the next open slot.
     *
     * @param item the equipment to add to a slot.
     */
    public void removeItem(T item) {
        if (isEmpty()) {
            throw new SlotsAreEmptyException();
        } else {
            list.remove(item);
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
        if (isFull()) {
            return list.get(index);
        } else {
            throw new IndexOutOfBoundsException("Index is out legal range");
        }
    }

    /**
     * Determines if these EquipmentSlots contains the specified item.
     *
     * @param item the item that is being searched for
     * @return true if this EquipmentSlots contains this item.
     */
    public boolean contains(T item) {
        return list.contains(item);
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
            throw new IndexOutOfBoundsException("Index is out of legal range");
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

    /**
     * Gets an iterator over the equipment slots
     *
     * @return the iterator over equipment
     */
    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    /**
     * Generates string representation of this object
     *
     * @return the equipment slots string
     */
    @Override
    public String toString() {
        String toString = "(" + size + "/" + numSlots + "): ";
        for (T item : list) {
            toString += item.toString() + ", ";
        }
        return toString;
    }
}
