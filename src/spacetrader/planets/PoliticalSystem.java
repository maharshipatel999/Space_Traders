package spacetrader.planets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import spacetrader.commerce.TradeGood;


/**
 * 
 * @author Caleb Stokols
*/
public enum PoliticalSystem {
    ANARCHY      ("Anarchy",          0, 0, 7, 1, TechLevel.PRE_AGRICULTURE,  TechLevel.INDUSTRIAL,       7, true,  true,  TradeGood.FOOD),
    CAPITALIST   ("Capitalist State", 2, 3, 2, 7, TechLevel.EARLY_INDUSTRIAL, TechLevel.HI_TECH,          1, true,  true,  TradeGood.ORE),
    COMMUNIST    ("Communist State",  6, 6, 4, 4, TechLevel.AGRICULTURE,      TechLevel.INDUSTRIAL,       5, true,  true,  null),
    CONFEDERACY  ("Confederacy",      5, 4, 3, 5, TechLevel.AGRICULTURE,      TechLevel.POST_INDUSTRIAL,  3, true,  true,  TradeGood.GAMES),
    CORPORATE    ("Corporate State",  2, 6, 2, 7, TechLevel.EARLY_INDUSTRIAL, TechLevel.HI_TECH,          2, true,  true,  TradeGood.ROBOTS),
    CYBERNETIC   ("Cybernetic State", 0, 7, 7, 5, TechLevel.POST_INDUSTRIAL,  TechLevel.HI_TECH,          0, false, false, TradeGood.ORE),
    DEMOCRACY    ("Democracy",        4, 3, 2, 5, TechLevel.RENAISSANCE,      TechLevel.HI_TECH,          2, true,  true,  TradeGood.GAMES),
    DICTATORSHIP ("Dictatorship",     3, 4, 5, 3, TechLevel.PRE_AGRICULTURE,  TechLevel.HI_TECH,          2, true,  true,  null),
    FASCIST      ("Fascist State",    7, 7, 7, 1, TechLevel.EARLY_INDUSTRIAL, TechLevel.HI_TECH,          0, false, true,  TradeGood.MACHINES),
    FEUDAL       ("Feudal State",     1, 1, 6, 2, TechLevel.PRE_AGRICULTURE,  TechLevel.RENAISSANCE,      6, true,  true,  TradeGood.FIREARMS), 
    MILITARY     ("Military State",   7, 7, 0, 6, TechLevel.MEDIEVAL,         TechLevel.HI_TECH,          0, false, true,  TradeGood.ROBOTS),
    MONARCHY     ("Monarchy",         3, 4, 3, 4, TechLevel.PRE_AGRICULTURE,  TechLevel.INDUSTRIAL,       4, true,  true,  TradeGood.MEDICINE),
    PACIFIST     ("Pacifist State",   7, 2, 1, 5, TechLevel.PRE_AGRICULTURE,  TechLevel.RENAISSANCE,      1, true,  false, null), 
    SOCIALIST    ("Socialist State",  4, 2, 5, 3, TechLevel.PRE_AGRICULTURE,  TechLevel.INDUSTRIAL,       6, true,  true,  null),
    SATORI       ("State of Satori",  0, 1, 1, 1, TechLevel.PRE_AGRICULTURE,  TechLevel.AGRICULTURE,      0, false, false, null),
    TECHNOCRACY  ("Technocracy",      1, 6, 3, 6, TechLevel.EARLY_INDUSTRIAL, TechLevel.HI_TECH,          2, true,  true,  TradeGood.WATER),
    THEOCRACY    ("Theocracy",        5, 6, 1, 4, TechLevel.PRE_AGRICULTURE,  TechLevel.EARLY_INDUSTRIAL, 0, true,  true,  TradeGood.NARCOTICS);

    private final String type;
    private final int reactionIllegal; // Reaction level of illegal goods 0 = total acceptance (determines how police reacts if they find you carry them)
    private final int strengthPolice; // Strength level of police force 0 = no police (determines occurrence rate)
    private final int strengthPirates; // Strength level of pirates 0 = no pirates
    private final int strengthTraders; // Strength levcel of traders 0 = no traders
    private final TechLevel minTechLevel; // Mininum tech level needed
    private final TechLevel maxTechLevel; // Maximum tech level where this is found
    private final int bribeLevel; // Indicates how easily someone can be bribed 0 = unbribeable/high bribe costs
    private final boolean drugsOK; // Drugs can be traded (if not, people aren't interested or the governemnt is too strict)
    private final boolean firearmsOK; // Firearms can be traded (if not, people aren't interested or the governemnt is too strict)
    private final TradeGood wanted; // Tradeitem requested in particular in this type of government

    private static final List<PoliticalSystem> VALUES =
                    Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    private PoliticalSystem(String type,
                            int reactionIllegal,
                            int strengthPolice,
                            int strengthPirates,
                            int strengthTraders,
                            TechLevel minTechLevel,
                            TechLevel maxTechLevel,
                            int bribeLevel,
                            boolean drugsOK,
                            boolean firearmsOK,
                            TradeGood wanted) {
        this.type = type;
        this.reactionIllegal = reactionIllegal;
        this.strengthPolice = strengthPolice;
        this.strengthPirates = strengthPirates;
        this.strengthTraders = strengthTraders;
        this.minTechLevel = minTechLevel;
        this.maxTechLevel = maxTechLevel;
        this.bribeLevel = bribeLevel;
        this.drugsOK = drugsOK;
        this.firearmsOK = firearmsOK;
        this.wanted = wanted;
    }

    public String type() {
        return type;
    }

    public int reactionIllegal() {
        return reactionIllegal;
    }

    public int strengthPolice() {
        return strengthPolice;
    }

    public int strengthPirates() {
        return strengthPirates;
    }

    public int strengthTraders() {
        return strengthTraders;
    }

    public TechLevel minTechLevel() {
        return minTechLevel;
    }

    public TechLevel maxTechLevel() {
        return maxTechLevel;
    }

    public int bribeLevel() {
        return bribeLevel;
    }

    public boolean drugsOK() {
        return drugsOK;
    }

    public boolean firearmsOK() {
        return firearmsOK;
    }

    public TradeGood wanted() {
        return wanted;
    }

    /**
     *  Gets a random PoliticalSystem
     *  @return the political system being assigned
     */
    public static PoliticalSystem getRandomPoliticalSystem() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
        
}