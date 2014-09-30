/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.exceptions;

/**
 *
 * @author Caleb Stokols
 */
public class NegativeQuantityException extends RuntimeException {

    /**
     * Creates a new instance of <code>NegativeQuantityException</code> without
     * detail message.
     */
    public NegativeQuantityException() {
    }

    /**
     * Constructs an instance of <code>NegativeQuantityException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NegativeQuantityException(String msg) {
        super(msg);
    }
}
