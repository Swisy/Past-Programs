import java.util.HashMap;

import controller.MastermindController;
import controller.MastermindIllegalColorException;
import controller.MastermindIllegalLengthException;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.MastermindModel;

/**
 * 
 * @author Saul Weintraub
 * 
 * This class is one of the two views for the Mastermind game. This view uses a GUI to play the game.
 * 
 * This class uses javaFX15 in order to create a GUI that allows the user to play the Mastermind game.
 * In order to play the game, the user must click the black circles at the bottom of the window in order
 * to change their colors to the colors the user wants to guess. Once the user has changed the circles'
 * colors, the user must press the guess button in order to submit their guess. The user's guess will
 * appear in the main area of the window along with the guess number and a maximum of 4 small circles
 * that tell the user information about their guess. Each small black circle that appears means that
 * the user guessed a correct color and it is in the correct place. Each small white circle that
 * appears means that the user guessed a correct color, but that color is in the wrong place. After the
 * user has made 10 incorrect guesses, the game will be over and an alert box will pop up telling the
 * user that they have lost. If the user guesses the correct colors, an alert box will pop up telling
 * the user that they have won the game. After the user has either won or lost the game, they will be
 * unable to submit more guesses.
 *
 */
public class MastermindGUIView extends Application {
	private boolean gameOver = false;
	private int guessNum = 0;
	private MastermindController controller;
	private HashMap<Paint, String> colors = new HashMap<Paint, String>();
	
	
	private Circle circle0 = new Circle(20, Color.BLACK);
	private Circle circle1 = new Circle(20, Color.BLACK);
	private Circle circle2 = new Circle(20, Color.BLACK);
	private Circle circle3 = new Circle(20, Color.BLACK);
	private Button guessbtn = new Button("Guess");

	@Override
	public void start(Stage stage) {
		fillMap();
		// Construct the model (whose constructor builds the secret answer)
		MastermindModel model = new MastermindModel();
		// Construct the controller, passing in the model
		controller = new MastermindController(model);
		
		stage.setTitle("Mastermind");
		BorderPane borderPane = new BorderPane();
		GridPane gridPane = new GridPane();
		for (int i = 0; i < 5; i++) {
	         ColumnConstraints column = new ColumnConstraints();
	         column.setPercentWidth(20);
	         column.setHalignment(HPos.CENTER);
	         gridPane.getColumnConstraints().add(column);
	     }
		
		VBox vBox = new VBox(10);
		gridPane.setStyle("-fx-background-color:#f4f4f4");
		Insets inset = new Insets(5);
		
		gridPane.add(circle0, 0, 0);
		GridPane.setMargin(circle0, inset);
		gridPane.add(circle1, 1, 0);
		gridPane.add(circle2, 2, 0);
		gridPane.add(circle3, 3, 0);
		gridPane.add(guessbtn, 4, 0);
		
		circle0.setOnMouseClicked((event) -> {
			cycleColor(circle0);
		});
		circle1.setOnMouseClicked((event) -> {
			cycleColor(circle1);
		});
		circle2.setOnMouseClicked((event) -> {
			cycleColor(circle2);
		});
		circle3.setOnMouseClicked((event) -> {
			cycleColor(circle3);
		});
		guessbtn.setOnAction((event) -> {
			if (gameOver == false) {
				Alert pick = new Alert(AlertType.INFORMATION, "You must pick four colors");
				if(circle0.getFill().equals(Color.BLACK)) {
					pick.show();
				} else if(circle1.getFill().equals(Color.BLACK)) {
					pick.show();
				} else if(circle2.getFill().equals(Color.BLACK)) {
					pick.show();
				} else if(circle3.getFill().equals(Color.BLACK)) {
					pick.show();
				} else {
					guessNum += 1;
					try {
						guess(vBox);
					} catch (MastermindIllegalLengthException e) {
						e.printStackTrace();
					} catch (MastermindIllegalColorException e) {
						e.printStackTrace();
					}
					circle0.setFill(Color.BLACK);
					circle1.setFill(Color.BLACK);
					circle2.setFill(Color.BLACK);
					circle3.setFill(Color.BLACK);
				}
			}
		});
		
		inset = new Insets(10);
		
		borderPane.setCenter(vBox);
		BorderPane.setMargin(vBox, inset);
		borderPane.setBottom(gridPane);
		borderPane.setStyle("-fx-background-color:tan");
		Scene scene = new Scene(borderPane, 400, 600);
		stage.setScene(scene);
		stage.show();
		
	}
	
	/**
	 * This method will change the color of a circle node.
	 * 
	 * This method will change the passed circle's color to the next color in the cycle. The cycle
	 * of the colors is black -> red -> orange -> yellow -> green -> blue -> purple -> red...
	 * If the game has already ended then the color of the circle will not change.
	 * 
	 * @param circle A circle node whose fill color will be changed.
	 */
	private void cycleColor(Circle circle) {
		if(gameOver == false) {
			Paint color = circle.getFill();
			if(color.equals(Color.BLACK)) {
				circle.setFill(Color.RED);
			} else if(color.equals(Color.RED)) {
				circle.setFill(Color.ORANGE);
			} else if(color.equals(Color.ORANGE)) {
				circle.setFill(Color.YELLOW);
			} else if(color.equals(Color.YELLOW)) {
				circle.setFill(Color.GREEN);
			} else if(color.equals(Color.GREEN)) {
				circle.setFill(Color.BLUE);
			} else if(color.equals(Color.BLUE)) {
				circle.setFill(Color.PURPLE);
			} else if(color.equals(Color.PURPLE)) {
				circle.setFill(Color.RED);
			}
		}
	}
	
	/**
	 * This method will create a GridPane that will be put in the VBox that represents the guess history.
	 * 
	 * This method will create a GridPane that contains the guess number, 4 big circles that are the colors
	 * of the guess, and 0-4 small black or white circles that tell the user how many colors they guessed
	 * correctly in the right place, and how many colors they guessed correctly in the wrong place. A black
	 * circle means they guessed a correct color in the correct place, and a white circle means they guessed
	 * a correct color in the wrong place. After the GridPane has been added to the VBox, the method will
	 * check if the user's guess was correct and if the user ran out of guesses. If the guess was correct,
	 * an alert box will inform the user that they won the game. If the user ran out of guesses, an alert
	 * box will inform the user they lost the game.
	 * 
	 * @param vbox The VBox in the main BorderPlane that contains the guess history
	 * @throws MastermindIllegalLengthException will be thrown if the guess length is not 4.
	 * @throws MastermindIllegalColorException will be thrown if the guess contains illegal
	 * 		   characters that do not represent valid colors.
	 */
	private void guess(VBox vbox) throws MastermindIllegalLengthException, MastermindIllegalColorException {
		GridPane guesspane = new GridPane();
		for (int i = 0; i < 6; i++) {
	         ColumnConstraints column = new ColumnConstraints();
	         column.setPercentWidth(16.67);
	         column.setHalignment(HPos.CENTER);
	         guesspane.getColumnConstraints().add(column);
	     }
		Text number = new Text(String.valueOf(guessNum));
		number.setFont(new Font(20));
		guesspane.add(number, 0, 0);
		
		Insets inset = new Insets(10);
		
		Circle guess0 = new Circle(20, circle0.getFill());
		guesspane.add(guess0, 1, 0);
		Circle guess1 = new Circle(20, circle1.getFill());
		guesspane.add(guess1, 2, 0);
		Circle guess2 = new Circle(20, circle2.getFill());
		guesspane.add(guess2, 3, 0);
		Circle guess3 = new Circle(20, circle3.getFill());
		guesspane.add(guess3, 4, 0);
		
		GridPane feedbackpane = getFeedback();
		
		guesspane.add(feedbackpane, 5, 0);
		GridPane.setMargin(feedbackpane, inset);
		
		vbox.getChildren().add(guesspane);
		
		String guess = getGuess();
		if(controller.isCorrect(guess)) {
			gameOver = true;
			Alert win = new Alert(AlertType.INFORMATION, "You won!");
			win.show();
		} else if(guessNum == 10) {
			gameOver = true;
			Alert lose = new Alert(AlertType.INFORMATION, "Max number of guesses reached, you lose.");
			lose.show();
		}
	}
	
	/**
	 * This method will create a 2x2 GridPane that contains 0-4 black and/or white circles based on
	 * the accuracy of the user's guess.
	 * 
	 * This method will add a black circle to the GridPane for every correct color in the correct
	 * place and will add a white circle for every correct color in the wrong place. 
	 * 
	 * @return The 2x2 GridPane that contains 0-4 black and/or white circles
	 * @throws MastermindIllegalLengthException will be thrown if the guess length is not 4.
	 * @throws MastermindIllegalColorException will be thrown if the guess contains illegal
	 * 		   characters that do not represent valid colors.
	 */
	private GridPane getFeedback() throws MastermindIllegalLengthException, MastermindIllegalColorException {
		String guess = getGuess();
		int rightPlace = controller.getRightColorRightPlace(guess);
		int wrongPlace = controller.getRightColorWrongPlace(guess);
		int circles = rightPlace + wrongPlace;
		int x = 0;
		int y = 0;
		
		GridPane feedbackpane = new GridPane();
		feedbackpane.setHgap(5);
		feedbackpane.setVgap(5);
		
		for(int i = 0; i < circles; i++) {
			if(rightPlace > 0) {
				Circle right = new Circle(5, Color.BLACK);
				feedbackpane.add(right, y, x);
				rightPlace -= 1;
				// Change x and y positions
				if((x == 0)) {
					x = 1;
				} else if((x == 1) && (y == 0)) {
					x = 0;
					y = 1;
				}
			} else if(wrongPlace > 0) {
				Circle wrong = new Circle(5, Color.WHITE);
				feedbackpane.add(wrong, y, x);
				wrongPlace -= 1;
				// Change x and y positions
				if((x == 0)) {
					x = 1;
				} else if((x == 1) && (y == 0)) {
					x = 0;
					y = 1;
				}
			}
		}
		
		return feedbackpane;
	}
	
	/**
	 * This method will put Color, string pairs into the colors dictionary that will be used to
	 * convert the guess entered by the user on the gui to a string that can be used by the
	 * controller.
	 */
	private void fillMap() {
		colors.put(Color.RED, "r");
		colors.put(Color.ORANGE, "o");
		colors.put(Color.YELLOW, "y");
		colors.put(Color.GREEN, "g");
		colors.put(Color.BLUE, "b");
		colors.put(Color.PURPLE, "p");
	}
	
	/**
	 * This method will convert the user's guess entered on the gui to a string that can be used
	 * by the controller, and will return that string.
	 * 
	 * @return a 4 character string representation of the user's guess.
	 */
	private String getGuess() {
		String guess = colors.get(circle0.getFill());
		guess = guess + colors.get(circle1.getFill());
		guess = guess + colors.get(circle2.getFill());
		guess = guess + colors.get(circle3.getFill());
		
		return guess;
	}
}
