
package spacetrader.commerce;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import spacetrader.Resource;
import static spacetrader.Resource.values;

/**
+ *
+ * @author Seth
+ */
public enum PriceIncreaseEvent {
    DROUGHT(),
    COLD(),
    CROP_FAIL(),
    WAR(),
    BOREDOM(),
    PLAGUE(),
    LACK_OF_WORKERS(),
    NONE();
    
    private static final List<PriceIncreaseEvent> VALUES =
        Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    
    
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