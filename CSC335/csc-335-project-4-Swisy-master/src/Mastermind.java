import javafx.application.Application;

/**
 * 
 * @author Saul Weintraub
 * 
 * This class is the Main class for the Mastermind game.
 * 
 * This class will launch either the text version of the game, or the GUI version of the game based
 * on the command line argument. If the argument is "-text", the text version of the game will launch.
 * If the argument is "-window", the GUI version of the game will launch. If another argument, or no
 * arguments are entered then the class will launch the gui version of the game by default.
 *
 */
public class Mastermind {

	public static void main(String[] args) {
		if(args.length == 0) {
			Application.launch(MastermindGUIView.class, args);
		} else if(args[0].equals("-text")) {
			MastermindTextView.textStart();
		} else if(args[0].equals("-window")) {
			Application.launch(MastermindGUIView.class, args);
		} else {
			Application.launch(MastermindGUIView.class, args);
		}
	}

}
