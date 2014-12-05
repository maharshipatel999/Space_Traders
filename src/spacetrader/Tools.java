/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader;

import java.util.Random;

/**
 * Holds useful static methods for computing different values.
 *
 * @author Caleb Stokols
 */
public final class Tools {

    public static final Random rand = new Random();

    /**
     * Returns pickIndexFromWeightedList(probs, sumOfProbs) where sumOfProbs is
     * the sum of all the values in probs.
     *
     * @param probs an array of probabilities
     * @return an index between 0 and probs.length - 1.
     */
    public static int pickIndexFromWeightedList(int[] probs) {
        //calculate the sum of the probabilities
        int sumOfProbabilities = 0;
        for (int i = 0; i < probs.length; i++) {
            sumOfProbabilities += probs[i];
        }

        return pickIndexFromWeightedList(probs, sumOfProbabilities);
    }

    /**
     * Returns a randomly chosen index inclusively between -1 and probs.length,
     * based on the weights of each index as specified in the array probs.
     * Specifically, there is a (100 * probs[i] / total) percent likelihood of
     * returning any index i between 0 and probs.length. If total is greater
     * than the sum of all the values in probs, then there is chance that none
     * of the indices in the array will be chosen, in that case -1 will be
     * returned.
     *
     * @param probs an array specifying the relative probability of each index
     * @param total the denominator of the probability of any number being
     * chosen (should be greater or equal to the sum of the values in probs)
     * @return an integer value between -1 (inclusive) and probs.length
     * (exclusive)
     */
    public static int pickIndexFromWeightedList(int[] probs, int total) {
        //pick random value
        int random = rand.nextInt(total);

        //determine which index to return based on the probability distributions
        int chosenIndex = 0;
        int sum = probs[0];
        //if total is greater than the sum of all the values in probs, then
        //not checking that the index is in bounds would cause an
        //IndexOutOfBoundsException
        while (random >= sum) {
            if (chosenIndex == probs.length - 1) {
                return -1; //random is greater than the sum of all the values in probs
            }
            chosenIndex++;
            sum += probs[chosenIndex];
        }
        return chosenIndex;
    }
    
    
    /**
     * Applies provided bounds to a specified value. If value is less than minValue,
     * this will return minValue. If value is greater than maxValue, this will
     * return maxValue. Otherwise it will return the original value.
     * 
     * @param <T> a value that is comparable
     * @param value the value which needs to be bounded.
     * @param minValue the minimum value the value can be
     * @param maxValue the maximum value the value can be
     * @return a value guaranteed to be between minValue and maxValue, inclusively
     */
    public static <T extends Comparable> T applyBounds(T value, T minValue, T maxValue) {
        if (value.compareTo(minValue) < 0) {
            return minValue;
        } else if (value.compareTo(maxValue) > 0) {
            return maxValue;
        } else {
            return value;
        }
    }
    
    

}
