
package spacetrader.commerce;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
+ *
+ * @author Seth
+ */
public enum PriceIncreaseEvent {
    DROUGHT("Drought!"),
    COLD("It's too cold!"),
    CROP_FAIL("Crops are failing!"),
    WAR("War!"),
    BOREDOM("People are bored!"),
    PLAGUE("The plague!"),
    LACK_OF_WORKERS("Not enough workers!"),
    NONE("None");
    
    private static final List<PriceIncreaseEvent> VALUES =
        Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    private String desc;
    
    PriceIncreaseEvent(String desc) {
        this.desc = desc;
    }
    
    public String desc() {
        return desc;
    }
    
    
    /**
     * Determines if there is a price increase event. There is a
     * 1 in 5 chance that there will be a price increase event.
     * If there is a price increase event, there is an equal
     * probability as to which price increase event will occur
     *
     *  @return PriceIncreaseEvent - the event that occurs
     */
    public static PriceIncreaseEvent getRandomPriceEvent() {
        int probOfNone = RANDOM.nextInt(5);
        if (probOfNone % 4 == 1) {
            return VALUES.get(RANDOM.nextInt(7));
        } else {
            return PriceIncreaseEvent.NONE;
        }
    }
    
}