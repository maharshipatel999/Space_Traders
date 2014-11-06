/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.planets;

/**
 *
 * @author maharshipatel999
 */
public class Wormhole {

    private Planet source;
    private Planet destination;

    public Wormhole(Planet src, Planet dest) {
        source = src;
        destination = dest;
    }

    public Planet getSource() {
        return this.source;
    }

    public Planet getDestination() {
        return this.destination;
    }

    public String toString() {
        return "Wormhole " + this.source.getLocation() + " " + this.destination.getLocation();
    }

}
