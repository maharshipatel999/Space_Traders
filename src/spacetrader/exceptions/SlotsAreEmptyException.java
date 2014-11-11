/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.exceptions;

/**
 *
 * @author Tejas
 */
public class SlotsAreEmptyException extends RuntimeException {

    /**
     * default constructor to instantiate SlotsAreEmptyException.
     */
    public SlotsAreEmptyException() {
    }

    /**
     * Constructs an instance of SlotsAreEmpty Exception with the specified
     * message.
     *
     * @param msg the detail message.
     */
    public SlotsAreEmptyException(final String msg) {
        super(msg);
    }
}
