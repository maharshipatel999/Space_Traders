/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.exceptions;

/**
 *
 * @author Caleb
 */
public class DepletedInventoryException extends RuntimeException {

    /**
     * Creates a new instance of <code>DepletedInventoryException</code> without
     * detail message.
     */
    public DepletedInventoryException() {
    }

    /**
     * Constructs an instance of <code>DepletedInventoryException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public DepletedInventoryException(final String msg) {
        super(msg);
    }
}
