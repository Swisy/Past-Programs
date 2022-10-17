package model;

import java.util.Random;

/**
 * 
 * @author Saul Weintraub
 *
 */
public class MastermindModel {
	//private variable(s) to store the answer
	private char[] colorAnswer = new char[4];
	
	// Used to randomly create the answer
	private char[] key = {'r', 'o', 'y', 'g', 'b', 'p'};
	// Only these methods may be public - you may not create any additional 
	// public methods (and NO public fields)
    public MastermindModel() { 
    	// Make the answer  
    	Random random = new Random();
    	int colorIndex;
    	// Get 4 random ints to use as indexes in the key array
    	for(int i = 0; i < 4; i++) {
    		colorIndex = random.nextInt(6);
    		colorAnswer[i] = key[colorIndex];
    	}
    }
    
    /**
     * This method is a special constructor to allow us to use JUnit to test our model.
     * 
     * Instead of creating a random solution, it allows us to set the solution from a 
     * String parameter.
     * 
     * 
     * @param answer A string that represents the four color solution
     */
    public MastermindModel(String answer) {
    	// Take answer and somehow store it as your answer. Make sure the getColorAt method 
    	// still works
    	answer = answer.toLowerCase();
    	for(int i = 0; i < 4; i++) {
    		colorAnswer[i] = answer.charAt(i);
    	}
    }


    public char getColorAt(int index) {
          /* Return color at position index as a char
           (first converted if stored as a number) */
    	return colorAnswer[index];
    }
    
    // Create as many private methods as you like

}
