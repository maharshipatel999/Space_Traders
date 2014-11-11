package spacetrader.commerce;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * PriceIncreaseEvent class. Has all events that increase Price
 *
 * @author Seth
 */
public enum PriceIncreaseEvent {

    DROUGHT("Suffering from a drought!"),
    COLD("Suffering from a cold spell!"),
    CROP_FAIL("Suffering from a crop failure!"),
    WAR("At war!"),
    BOREDOM("Suffering from extreme boredom!"),
    PLAGUE("Ravaged by plague!"),
    LACK_OF_WORKERS("Lacking enough workers!"),
    NONE("Under no particular pressure.");

    private String desc;

    private static final List<PriceIncreaseEvent> VALUES
            = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    static int maxRandomPrice = 5;
    static int maxRandomPriceDuration = 10;

    /**
     * Creates a price increase event with description.
     *
     * @param desc Description of PriceIncreaseEvent
     */
    PriceIncreaseEvent(final String desc) {
        this.desc = desc;

    }

    /**
     * gets description of specific Price Increase Event.
     *
     * @return description of PriceIncreaseEvent
     */
    public String desc() {
        return desc;
    }

    /**
     * Determines if there is a price increase event. There is a 1 in 5 chance
     * that there will be a price increase event. If there is a price increase
     * event, there is an equal probability as to which price increase event
     * will occur
     *
     * @return PriceIncreaseEvent - the event that occurs
     */
    public static PriceIncreaseEvent getRandomPriceEvent() {

        int probOfNone = RANDOM.nextInt(maxRandomPrice);
        if (probOfNone % (maxRandomPrice - 1) == 1) {
            return VALUES.get(RANDOM.nextInt(SIZE - 1));
        } else {
            return PriceIncreaseEvent.NONE;
        }
    }

    /**
     * Determines a random duration that the price increase event will last.
     * This number will be between 1 - 10.
     *
     * @return int - the length of the event
     */
    public static int setRandomPriceIncDuration() {
        return RANDOM.nextInt(maxRandomPriceDuration) + 1;
    }

}
