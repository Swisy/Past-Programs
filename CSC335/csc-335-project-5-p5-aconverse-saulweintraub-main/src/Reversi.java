import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javafx.application.Application;

/**
 * 
 * @author Amber Converse
 * 
 * This class starts the program.
 *
 */

public class Reversi {
	public static void main(String[] args) throws IOException {
		Application.launch(ReversiView.class, args);
	}
}