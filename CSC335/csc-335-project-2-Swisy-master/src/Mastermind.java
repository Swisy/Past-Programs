import java.util.Scanner;

import controller.MastermindController;
import model.MastermindModel;

/**
 * 
 * @author Saul Weintraub
 *
 * This class is the view for the Mastermind game.
 * 
 * For every game of Mastermind, this class will ask the user for guesses until the user has entered
 * 10 valid guesses, or the user has won the game. In order to win the game the user must correctly
 * guess the correct colors in the correct order which is defined by the MastermindModel. After
 * every valid guess, the amount of colors correctly guessed in the right position and the amount of
 * colors correctly guessed in the wrong position will be displayed. If the user enters an invalid
 * guess then an exception will be handled by the class and the user will be asked to enter another
 * guess.
 */
public class Mastermind {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		// This class represents the view, it should be how users play the game
		System.out.println("Welcome to Mastermind!");
		System.out.print("Would you like to play? Type 'yes' to play: ");
		String play = scanner.nextLine();
		// while the user wants to play:
		while(play.equals("yes")) {
			// Construct the model (whose constructor builds the secret answer)
			MastermindModel model = new MastermindModel();
			// Construct the controller, passing in the model
			MastermindController controller = new MastermindController(model);
		    // Run a single game
			play = singleGame(scanner, controller);
			
		}
		scanner.close();
	}
	
	/**
	 * This method will run a single game of Mastermind.
	 * 
	 * This method will ask a maximum of 10 valid guesses from the user, and after every guess will
	 * check to see if the user correctly guessed the answer. Every turn this method will call
	 * another method named singleTurn() which will gather and print statistics based on the user's
	 * guess.
	 * 
	 * @param scanner a scanner that will be used to get guesses from the user.
	 * @param controller the controller class that holds the game model and has methods that are
	 *        used to run the game.
	 * @return a string that determines if another game will be played. = "yes" to play again.
	 */
	private static String singleGame(Scanner scanner, MastermindController controller) {
		boolean won = false;
		// Read up to ten user inputs
		for(int i = 1; i <= 10; i++) {
			// Run a single turn
			won = singleTurn(scanner, controller, i);
			if (won == true){
				break;
			}
		}
		// Determine win or loss
		if(won == true) {
			System.out.println("Congratulations you won!");
		}
		else {
			System.out.println("Sorry you lose!");
		}
		// Ask to play again
		System.out.print("Type 'yes' to play again ");
		String play = scanner.nextLine();
		return play;
	}

	/**
	 * This method will run a single turn of Mastermind.
	 * 
	 * This method will use the scanner to get an input from the user that represents their guess.
	 * A valid guess is a 4 character long string that contains only the characters r, o, y, g, b,
	 * and p. Upper case versions of these characters is supported but are not treated differently.
	 * The method contains a try catch block which will handle the exceptions created if a user
	 * enters a guess that is either not 4 characters long, or a guess that contains illegal
	 * characters. If a user enters an invalid guess then the user will be asked to enter another
	 * guess. Once a valid guess is entered, the method will print out how many colors were guessed
	 * in the correct position and how many colors were correctly guessed in the wrong position.
	 * 
	 * @param scanner a scanner used to get guesses from the user.
	 * @param controller the controller class that holds the game model and has methods that are
	 *        used to run the game.
	 * @param turn an int representing the current turn number. Can be 1-10.
	 * @return a boolean that represents if the game has been won. true = yes, false = no.
	 */
	private static boolean singleTurn(Scanner scanner, MastermindController controller, int turn) {
		boolean won = false;
		// Get guess
		System.out.print("Enter guess number " + turn + ": ");
		String guess = scanner.nextLine();
		guess = guess.toLowerCase();
		
		// Handle Exceptions
		try {
			// Check whether or not the input is correct (by asking the controller)
			if(controller.isCorrect(guess) == true) {
				won = true;
				return won;
			}
			// If not, display the relevant statistics  (by asking the controller)
			else {
				int correctPlace = controller.getRightColorRightPlace(guess);
				int wrongPlace = controller.getRightColorWrongPlace(guess);
				System.out.println("Colors in the correct place: " + correctPlace);
				System.out.println("Colors correct but in the wrong position: " + wrongPlace);
			}
		} catch(controller.MastermindIllegalLengthException badLength) {
			System.out.println(badLength.toString());
			won = singleTurn(scanner, controller, turn);
		} catch(controller.MastermindIllegalColorException badColor) {
			System.out.println(badColor.toString());
			won = singleTurn(scanner, controller, turn);
		}
		
		return won;
	}
}
