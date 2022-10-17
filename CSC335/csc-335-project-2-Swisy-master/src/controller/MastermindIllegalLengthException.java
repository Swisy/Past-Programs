package controller;


public class MastermindIllegalLengthException extends Exception{
	public MastermindIllegalLengthException(String message) {
		super(message);
	}
	
	public String toString() {
		return "Guess length is not equal to 4. " + getMessage();
	}

}
