
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.exceptions;

/**
 *
 * @author Seth
 */
public class CargoIsFullException extends RuntimeException {

    /**
     * Creates a new instance of <code>InsufficientFundsException</code> without
     * detail message.
     */
    public CargoIsFullException() {
    }

    /**
     * Constructs an instance of <code>InsufficientFundsException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CargoIsFullException(String msg) {
        super("Wallet lacks sufficient funds.");
    }
}
