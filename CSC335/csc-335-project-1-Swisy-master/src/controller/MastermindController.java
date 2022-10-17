package controller;
import model.MastermindModel;

/**
 * 
 * @author Saul Weintraub
 *
 */
public class MastermindController {
	private MastermindModel model;
	// Only these methods may be public - you may not create any additional 
	// public methods (and NO public fields)
	
	public MastermindController(MastermindModel model) {
		this.model = model;
	}
 

    public boolean isCorrect(String guess) {
    	for(int i = 0; i < 4; i++) {
    		if(guess.charAt(i) != model.getColorAt(i)) {
    			return false;
    		}
    	}
    	return true;
    }


    public int getRightColorRightPlace(String guess) { 
    	int count = 0;
    	for(int i = 0; i < 4; i++) {
    		if(guess.charAt(i) == model.getColorAt(i)) {
    			count += 1;
    		}
    	}
    	return count;
    }


    public int getRightColorWrongPlace(String guess) {
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

}
