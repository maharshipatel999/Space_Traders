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
public class InsufficientFuelException extends RuntimeException {

    /**
     * Creates a new instance of <code>InsufficientFuelsException</code> without
     * detail message.
     */
    public InsufficientFuelException() {
    }

    /**
     * Constructs an instance of <code>InsufficientFuelsException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public InsufficientFuelException(String msg) {
        super(msg);
    }
}
