/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Describes all possible Reputation a Player can hold
 * @author Caleb
 */
public enum Reputation {

    HARMLESS        ("Harmless",        0),
    MOSTLY_HARMLESS ("Mostly Harmless", 10),
    POOR            ("Poor",            20),
    AVERAGE         ("Average",         40),
    ABOVE_AVERAGE   ("Above Average",   80),
    COMPETENT       ("Competent",       150),
    DANGEROUS       ("Dangerous",       300),
    DEADLY          ("Deadly",          600),
    ELITE           ("Elite",           1500);

    private final String name;
    private final int minRep;

    private static final List<Reputation> VALUES
            = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();

    /**
     * Instantiates new Reputation
     *
     * @param name name of the Reputation
     * @param minRep minimum Reputation
     */
    private Reputation(String name, int minRep) {
        this.name = name;
        this.minRep = minRep;
    }

    /**
     * Returns String representation of Reputation
     *
     * @return String representation of Player
     */
    @Override
    public String toString() {
        return name;
    }

    public int minRep() {
        return minRep;
    }

    /**
     * Determines the Reputation level associated with a given reputation-score.
     *
     * @param reputationScore the score who's reputation level should be found
     * @return the Reputation level of the reputationScore
     */
    public static Reputation getReputation(int reputationScore) {
        Reputation rep;
        rep = VALUES.get(0);
        int i = 0;
        while (i < SIZE && reputationScore >= VALUES.get(i).minRep) {
            rep = VALUES.get(i);
            i++;
        }
        return rep;
    }
}
