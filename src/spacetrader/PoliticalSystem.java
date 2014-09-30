package spacetrader;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import static spacetrader.Resource.values;

/**
 * 
 * @author Caleb Stokols
*/
public enum PoliticalSystem {
    ANARCHY      (5, 80, 5, 1, 1, 0, 0, false, "Anarchy"),
    CAPITALIST   (0, 0, 0, 0, 0, 0, 0, false, "Capitalist State"), //TODO
    COMMUNIST    (70, 60, 10, 60, 130, 0, 0, true, "Communist State"),
    CONFEDERACY  (0, 0, 0, 0, 0, 0, 0, false, "Confederacy"), //TODO
    CORPORATE    (0, 0, 0, 0, 0, 0, 0, false, "Corporate State"), //TODO
    CYBERNETIC   (80, 80, 10, 20, 0, 0, 80, false, "Cybernetic State"),
    DEMOCRACY    (0, 0, 0, 0, 0, 0, 0, false, "Democracy"), //TODO
    DICTATORSHIP (0, 0, 0, 0, 0, 0, 0, false, "Dictatorship"), //TODO
    FASCIST      (0, 0, 0, 0, 0, 0, 0, false, "Fascist State"), //TODO
    FEUDAL       (0, 0, 0, 0, 0, 0, 0, false, "Feudal State"), //TODO
    MILITARY     (100, 0, 80, 60, 140, 100, 0, false, "Military State"),
    MONARCHY     (0, 0, 0, 0, 0, 0, 0, false, "Monarchy"), //TODO
    PACIFIST     (0, 0, 0, 0, 0, 0, 0, false, "Pacifist State"), //TODO
    SOCIALIST    (0, 0, 0, 0, 0, 0, 0, false, "Socialist State"), //TODO
    SATORI       (0, 0, 0, 0, 0, 0, 0, false, "State of Satori"), //TODO
    TECHNOCRACY  (0, 0, 0, 0, 0, 0, 0, false, "Technocracy"), //TODO
    THEOCRACY    (0, 0, 0, 0, 0, 0, 0, false, "Theocracy"); //TODO
    
	// percent, meaning percent increase from the norm
	private final double policePercent;
        private final double piratePercent;
        private final double traderPercent;
        private final double bribePercent;
        private final double generalPriceIncreasePercent;
        private final double hiTechPriceIncreasePercent;
        private final double essentialGoodsPriceIncreasePercent;
	private final boolean hasForbiddenGoods;
	private final String name;
        private static final List<PoliticalSystem> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();
	
        PoliticalSystem(double police, double pirate, double trader,
                         double bribe, double general, double hiTech,
                         double essential, boolean forbidden, String name) {
            policePercent = police;
            piratePercent = pirate;
            traderPercent = trader;
            bribePercent = bribe;
            generalPriceIncreasePercent = general;
            hiTechPriceIncreasePercent = hiTech;
            essentialGoodsPriceIncreasePercent = essential;
            hasForbiddenGoods = forbidden;
            this.name = name;
        }
        
	public double policeMultiplier(){
            return 1 + (policePercent / 100);
	}
        
	public double pirateMultiplier(){
            return 1 + (piratePercent / 100);
	}
        
	public double traderMultiplier(){
            return 1 + (traderPercent / 100);
	}
        
	public double bribeMultiplier(){
            return 1 + (bribePercent / 100);
	}
	
	public double generalPriceMultiplier(){
            return 1 + (generalPriceIncreasePercent / 100);
	}
	
	public double hiTechPriceMultipier(){
            return 1 + (hiTechPriceIncreasePercent / 100);
	}
	
	public double essentialGoodsPriceMultiplier(){
            return 1 + (essentialGoodsPriceIncreasePercent / 100);
	}
        
        public boolean hasForbidden(){
            return hasForbiddenGoods;
	}

        /**
         *  Gets a random PoliticalSystem
         *  @return the political system being assigned
         */
        public static PoliticalSystem getRandomPoliticalSystem() {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
        
}