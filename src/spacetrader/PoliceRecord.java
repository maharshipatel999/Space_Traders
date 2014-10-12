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
 *
 * @author Caleb
 */
public enum PoliceRecord {
    PSYCHO   ("Psycho",   -100),
    VILLAIN  ("Villain",  -70),
    CRIMINAL ("Criminal", -30),
    CROOK    ("Crook",    -10),
    DUBIOUS  ("Dubious",  -5),
    CLEAN    ("Clean",     0),
    LAWFUL   ("Lawful",    5),
    TRUSTED  ("Trusted",   10),
    LIKED    ("Liked",     25),
    HERO     ("Hero",      75);
    
    private final String name;
    private final int minScore;
    
    private static final List<PoliceRecord> VALUES =
                    Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();

    private PoliceRecord(String name, int minScore) {
        this.name = name;
        this.minScore = minScore;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    /**
     * Determines the PoliceRecord level associated with a given police record score.
     * @param policeRecordScore the score who's police record level should be found
     * @return the PoliceRecord level of the policeRecordScore
     */
    public static PoliceRecord getPoliceRecord(int policeRecordScore) {
        PoliceRecord record = VALUES.get(0);
        int i = 0;
        while (i < SIZE && policeRecordScore >= VALUES.get(i).minScore) {
            record = VALUES.get(i);
            i++;
        }
        return record;
    }
}
