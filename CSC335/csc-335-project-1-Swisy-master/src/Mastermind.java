import java.util.Scanner;

import controller.MastermindController;
import model.MastermindModel;

/**
 * 
 * @author Saul Weintraub
 *
 */
public class Mastermind {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String guess;
		// This class represents the view, it should be how users play the game
		System.out.println("Welcome to Mastermind!");
		System.out.print("Would you like to play? Type 'yes' to play: ");
		String play = scanner.nextLine();
		// while the user wants to play:
		while(play.equals("yes")) {
			boolean won = false;
			// Construct the model (whose constructor builds the secret answer)
			MastermindModel model = new MastermindModel();
			// Construct the controller, passing in the model
			MastermindController controller = new MastermindController(model);
		    // Read up to ten user inputs
			for(int i = 1; i <= 10; i++) {
				System.out.print("Enter guess number " + i + ": ");
				guess = scanner.nextLine();
				guess = guess.toLowerCase();
				// Check whether or not the input is correct (by asking the controller)
				if(controller.isCorrect(guess) == true) {
					won = true;
					break;
				}
				// If not, display the relevant statistics  (by asking the controller)
				else {
					int correctPlace = controller.getRightColorRightPlace(guess);
					int wrongPlace = controller.getRightColorWrongPlace(guess);
					System.out.println("Colors in the correct place: " + correctPlace);
					System.out.println("Colors correct but in the wrong position: " + wrongPlace);
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
			System.out.print("Type 'yes' to play again");
			play = scanner.nextLine();
		}
	}

}
