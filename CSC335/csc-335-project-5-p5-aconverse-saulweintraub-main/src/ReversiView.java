import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * 
 * @author Amber Converse
 * 
 * This class runs the graphics view and gui for the Reversi game.
 *
 */

@SuppressWarnings("deprecation")
public class ReversiView extends Application implements Observer {
	
	private BorderPane window;
	private MenuBar topGUI;
	private GridPane board;
	private Label bottomGUI;
	private ReversiBoard curBoard;
	
	private ReversiModel model;
	private ReversiController controller;

	/**
	 * This function sets up the initial stage, either blank or from
	 * a save_game.dat file.
	 * 
	 * @param primaryStage
	 */
	public void start(Stage primaryStage) {
		
		primaryStage.setOnCloseRequest(event -> {
			if (curBoard != null && !controller.isOver()) {
			    try {
			    	File newFile = new File("save_file.dat");
					newFile.createNewFile();
			    	FileOutputStream saveToFile = new FileOutputStream("save_file.dat");
				    ObjectOutputStream outputStream = new ObjectOutputStream(saveToFile);
					outputStream.writeObject(curBoard);
					outputStream.close();
				} catch (IOException e) {}
			}
		});
		
		File saveFile = new File("save_file.dat");
		if (saveFile.exists()) {
			model = new ReversiModel("save_file.dat");
			controller = new ReversiController(model);
		} else {
			model = new ReversiModel();
			controller = new ReversiController(model);
		}
		
		model.addObserver(this);
		
		window = new BorderPane();
		
		topGUI = new MenuBar();
		Menu fileMenu = new Menu("File");
		MenuItem newGame = new MenuItem("New Game");
		newGame.setOnAction((event) -> {
			File possibleSaveFile = new File("save_file.dat");
			if (possibleSaveFile.exists()) {
				possibleSaveFile.delete();
			}
			start(primaryStage);});
		fileMenu.getItems().add(newGame);
		topGUI.getMenus().add(fileMenu);
		
		window.setTop(topGUI);
		
		board = new GridPane();
		board.setPadding(new Insets(8,8,8,8));
		GridPane.setHalignment(board, HPos.CENTER);
		board.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
		setBoard();
		
		window.setCenter(board);
		
		Label bottomGUI = new Label("White: " + controller.getWhiteScore() +
							   " - Black: " + controller.getBlackScore());
		window.setBottom(bottomGUI);
		
		primaryStage.setTitle("Reversi");
		
		Scene scene = new Scene(window, 385, 430);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * Updates the board after a move.
	 * 
	 * @param o
	 * @param arg
	 */
	@Override
	public void update(Observable o, Object arg) {
		curBoard = (ReversiBoard) arg;
		setBoard();
		bottomGUI = new Label("White: " + controller.getWhiteScore() +
				   " - Black: " + controller.getBlackScore());
		window.setBottom(bottomGUI);
		if (controller.isOver()) {
			Alert endGameAlert = new Alert(Alert.AlertType.INFORMATION);
			endGameAlert.setTitle("Game over!");
			if (controller.getWhiteScore() > controller.getBlackScore()) {
				endGameAlert.setHeaderText("You won!");
			} else if (controller.getBlackScore() > controller.getWhiteScore()) {
				endGameAlert.setHeaderText("You lost!");
			} else {
				endGameAlert.setHeaderText("It was a tie!");
			}
			endGameAlert.setContentText("White had " + Integer.toString(controller.getWhiteScore()) +
										" pieces and Black had " + Integer.toString(controller.getBlackScore()) +
										" pieces.");
			File possibleSaveFile = new File("save_file.dat");
			if (possibleSaveFile.exists()) {
				possibleSaveFile.delete();
			}
			endGameAlert.showAndWait();
		}
		if (controller.getTurn() == 'b' && !controller.isOver()) {
			controller.computerTurn();
		}
	}
	
	private void setBoard() {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				StackPane pane = new StackPane();
				pane.setPadding(new Insets(2,2,2,2));
				
				pane.setBorder(new Border(new BorderStroke(Color.BLACK,
														   BorderStrokeStyle.SOLID,
														   CornerRadii.EMPTY,
														   new BorderWidths(1,1,1,1))));
				
				EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
			        @Override 
			        public void handle(MouseEvent e) { 
			        	int row = GridPane.getRowIndex(pane);
			        	int col = GridPane.getColumnIndex(pane);
			        	controller.humanTurn(row, col);
			        } 
			    };  
			      
			    pane.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
				
				Circle circle = new Circle();
				circle.setRadius(20);
				circle.setFill(controller.getPieceColor(row, col));
				
				pane.getChildren().add(circle);
				
				board.add(pane, col, row);
			}
		}
	}
	
}