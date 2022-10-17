package controller;
import model.MastermindModel;

/**
 * 
 * @author Saul Weintraub
 *
 * This class is the controller for the Mastermind game.
 * 
 * This class will have it's methods called upon by the Mastermind view in order to play the game.
 * This class will interact with the MastermindModel in order to receive the correct order of colors
 * that the user must guess in order to win the game.
 */
public class MastermindController {
	private MastermindModel model;
	// Only these methods may be public - you may not create any additional 
	// public methods (and NO public fields)
	
	public MastermindController(MastermindModel model) {
		this.model = model;
	}
 

	/**
	 * This method will determine if the guess is correct.
	 * 
	 * This method will compare each character of the guess string to the corresponding character of
	 * the answer located in the model to determine if the guess and the answer are equal.
	 * 
	 * @param guess a string that represents the user's guess.
	 * @return a boolean that represents if the guess is correct. true = yes, false = no.
	 * @throws MastermindIllegalLengthException will be thrown if the guess length is not 4.
	 * @throws MastermindIllegalColorException will be thrown if the guess contains illegal
	 * 		   characters that do not represent valid colors.
	 */
    public boolean isCorrect(String guess) throws MastermindIllegalLengthException, MastermindIllegalColorException {
    	testForExceptions(guess);
    	for(int i = 0; i < 4; i++) {
    		if(guess.charAt(i) != model.getColorAt(i)) {
    			return false;
    		}
    	}
    	return true;
    }


    /**
     * This method will determine how many colors were correctly guessed in the correct position.
     * 
     * This method will compare each character of the guess string to the corresponding character of
     * the answer located in the model to determine how many colors were correctly guessed.
     * 
     * @param guess a string that represents the user's guess.
     * @return an int that represents how many colors were correctly guessed in the right position.
     * @throws MastermindIllegalLengthException will be thrown if the guess length is not 4.
     * @throws MastermindIllegalColorException will be thrown if the guess contains illegal
     *         characters that do not represent valid colors.
     */
    public int getRightColorRightPlace(String guess) throws MastermindIllegalLengthException, MastermindIllegalColorException { 
    	testForExceptions(guess);
    	int count = 0;
    	for(int i = 0; i < 4; i++) {
    		if(guess.charAt(i) == model.getColorAt(i)) {
    			count += 1;
    		}
    	}
    	return count;
    }


    /**
     * This method will determine how many colors were correctly guessed but in the wrong position.
     * 
     * This method will look at each character of the guess and determine if that character is a
     * correct guess in an incorrect position, and will make sure that that color has not already
     * been counted as being correct. Once all four characters of the guess have been checked, the
     * method will return an int.
     * 
     * @param guess a string that represents the user's guess.
     * @return an int that represents how many colors were correctly guessed but in the wrong
     *         position. Each color can only be counted once.
     * @throws MastermindIllegalLengthException will be thrown if the guess length is not 4.
     * @throws MastermindIllegalColorException will be thrown if the guess contains illegal
     *         characters that do not represent valid colors.
     */
    public int getRightColorWrongPlace(String guess) throws MastermindIllegalLengthException, MastermindIllegalColorException {
    	testForExceptions(guess);
    	int count = 0;
    	String checked = "";
    	boolean colorFound;
    	boolean colorCorrect;
    	for(int i = 0; i < 4; i++) {
    		colorFound = false;
    		colorCorrect = false;
    		char guessChar = guess.charAt(i);
    		if((guessChar != model.getColorAt(i)) && (checked.indexOf(guessChar) == -1)) {
    			for(int j = 0; j < 4; j++) {
    				if(model.getColorAt(j) == guessChar) {
    					colorFound = true;
    					if(guess.charAt(j) == guessChar) {
    						colorCorrect = true;
    					}
    				}
    			}
    			if((colorFound == true) && (colorCorrect == false)) {
    				count += 1;
    			}
    		}
    		checked = checked + guessChar;
    	}
    	return count;
    }

    // Create as many private methods as you like
    
    /**
     * This method will test the user's guess and throw exceptions if the guess is not valid.
     * 
     * This method will test if the guess is exactly 4 characters long and will test if the guess
     * only contains valid characters. The valid characters are r, o, y, g, b, and p.
     * 
     * @param guess a string that represents the user's guess.
     * @throws MastermindIllegalLengthException will be thrown if the guess length is not 4.
     * @throws MastermindIllegalColorException will be thrown if the guess contains illegal
     *         characters that do not represent valid colors.
     */
    private void testForExceptions(String guess) throws MastermindIllegalLengthException, MastermindIllegalColorException {
    	// Test to see if guess is correct length
    	if(guess.length() != 4) {
    		throw new MastermindIllegalLengthException("Guess length equals " + guess.length());
    	}
    			
    	// Test to see if guess contains only valid colors
    	String validColors = "roygbp";
    	for(int i = 0; i < guess.length(); i++) {
    		char color = guess.charAt(i);
    		if(validColors.indexOf(color) == -1) {
    			throw new MastermindIllegalColorException(color + " is not a valid color");
    				}
    			}
    }

}
