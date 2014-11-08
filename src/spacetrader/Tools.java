/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import java.util.Random;

/**
 * Holds useful static methods for computing different values.
 * @author Caleb Stokols
 */
public final class Tools {
    
    public static Random rand = new Random();
    
    public static int pickIndexFromWeightedList(int[] probs) {
        //calculate the sum of the probabilities
        int sumOfProbabilities = 0;
        for (int i = 0; i < probs.length; i++) {
            sumOfProbabilities += probs[i];
        }
        //pick random value
        int random = rand.nextInt(sumOfProbabilities);
      
        //determine which index to return based on the probability distributions
        int chosenIndex = 0;
        int sum = probs[0];
        while (random >= sum) {
            chosenIndex++;
            sum += probs[chosenIndex];
        }
        return chosenIndex;
    }
    
}
