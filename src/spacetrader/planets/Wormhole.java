/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.planets;

import java.io.Serializable;

/**
 *
 * @author maharshipatel999
 */
public class Wormhole implements Serializable {

    private Planet source;
    private Planet destination;

    /**
     * constructor for wormhole
     *
     * @param src - source planet
     * @param dest - destination planet
     */
    public Wormhole(Planet src, Planet dest) {
        source = src;
        destination = dest;
    }

    /**
     * get source planet
     *
     * @return source planet
     */
    public Planet getSource() {
        return this.source;
    }

    /**
     * get destination planet
     *
     * @return destination planet
     */
    public Planet getDestination() {
        return this.destination;
    }

    @Override
    public String toString() {
        return "Wormhole " + this.source.getLocation() + " " + this.destination.getLocation();
    }

}
