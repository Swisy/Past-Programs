import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import controller.StrategyGameController;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.BadSaveException;
import model.StrategyGameModel;
import model.Team;
import onboard.FriendlyFireException;
import onboard.InvalidMoveException;
import onboard.InvalidRemovalException;
import onboard.OutOfMovesException;


/**
 * 
 * @author Amber Converse
 * 
 * This class encapsulates a single Strategy Game with its
 * related scene.
 */
@SuppressWarnings("deprecation")
public class StrategyGameLevelView implements Observer {
	
	BorderPane gameWindow;
	GridPane map;
	Text message;
	
	int mapWidthPixel;
	int mapHeightPixel;
	
	StrategyGameModel model;
	StrategyGameController controller;
	StrategyGameView mainView;
	
	boolean saveUpToDate;
	String levelFileName;
	
	/**
	 * This constructor constructs the Strategy Game gui (not including the top bar).
	 * 
	 * @param mainView (StrategyGameView) : should be the main view object.
	 * @param gameWindow (BorderPane) : an already constructed gameWindow with the top GUI already constructed
	 * @param levelFileName (String) : a string representing the name of the level/save file to be constructed.
	 * @throws BadSaveException 
	 */
	public StrategyGameLevelView(StrategyGameView mainView, BorderPane gameWindow, String levelFileName) throws BadSaveException {
		
		this.mainView = mainView;
		this.gameWindow = gameWindow;
		this.levelFileName = levelFileName;
		
		saveUpToDate = true;
			
		// Set up model and controller
		model = new StrategyGameModel(levelFileName);
		model.addObserver(this);
		controller = new StrategyGameController(model);
				
		// Bottom GUI
		HBox bottomGUI = new HBox();
		
		message = new Text();
		message.setFont(Font.font("Palatino", 15));
		bottomGUI.getChildren().add(message);
		HBox.setMargin(message, new Insets(10,10,10,10));
		Button nextTurn = new Button("Next Turn");
		nextTurn.setOnAction((event) -> {
			controller.nextTurn();
			});
		bottomGUI.getChildren().add(nextTurn);
		HBox.setMargin(nextTurn, new Insets(10,10,10,10));
		
		bottomGUI.setPrefHeight(50);
		bottomGUI.setAlignment(Pos.CENTER_RIGHT);
		
		gameWindow.setBottom(bottomGUI);
				
		// Set up the map
		setBoard();
	}
	
	/**
	 * Sets up the board
	 */
	private void setBoard() {
		map = new GridPane();
		Image mapBackground = new Image("assets/custom_background.png");
		if (!controller.getBackgroundImageFileName().equals("custom")) {
			mapBackground = new Image(controller.getBackgroundImageFileName());
		}
		
		mapWidthPixel = (int) mapBackground.getWidth();
		mapHeightPixel = (int) mapBackground.getHeight();
		map.setAlignment(Pos.BASELINE_CENTER);
		map.setPrefSize(mapWidthPixel, mapHeightPixel);;
		map.setBackground(new Background(new BackgroundImage(mapBackground,
							BackgroundRepeat.NO_REPEAT, 
							BackgroundRepeat.NO_REPEAT, 
							BackgroundPosition.CENTER, 
							BackgroundSize.DEFAULT)));
		this.message.setText("");
		VBox center = new VBox();
		center.setPrefSize(mapWidthPixel, mapHeightPixel);
		center.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
		
		
		for (int row = 0; row < controller.getBoardLength(); row++) {
			for (int col = 0; col < controller.getBoardWidth(); col++) {
				GridPane tile = new GridPane();
				setTile(tile, row, col, true);
				map.add(tile, col, row);
			}
		}
		center.getChildren().add(map);
		center.setAlignment(Pos.CENTER);;
		gameWindow.setCenter(center);
	}
	
	/**
	 * Sets up an individual tile at row, col. Will include on-click actions if withEventFilters
	 * is true.
	 * 
	 * @param tile (GridPane) : already constructed empty GridPane to be populated with GUI elements
	 * @param row (int) : the row of the tile on the map
	 * @param col (int) : the column of the tile on the map
	 * @param withEventFilters (boolean) : if true, on-click menus will be added to pieces
	 */
	private void setTile(GridPane tile, int row, int col, boolean withEventFilters) {
		
		int tileWidth = Math.floorDiv(mapWidthPixel, controller.getBoardWidth());
		int tileHeight = Math.floorDiv(mapHeightPixel, controller.getBoardLength());
		
		tile.setPrefSize(tileWidth, tileHeight);
		tile.setMaxSize(tileWidth, tileHeight);
		tile.setBorder(new Border(new BorderStroke(Color.BLACK,
				   BorderStrokeStyle.SOLID,
				   CornerRadii.EMPTY,
				   new BorderWidths(1,1,1,1))));
		if (controller.getBackgroundImageFileName().equals("custom")) {
			tile.setBackground(new Background(new BackgroundImage(new Image(controller.getTileBackground(row, col)), 
					BackgroundRepeat.NO_REPEAT, 
					BackgroundRepeat.NO_REPEAT, 
					BackgroundPosition.DEFAULT, 
					new BackgroundSize(100, 100, true, true, false, true))));
		} else {
			tile.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		}
		
		tile.setAlignment(Pos.BOTTOM_CENTER);
		
		if (controller.hasPlayer(row, col)) {
			
			if (controller.isDefended(row, col)) {
				tile.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
			}
			if (controller.isRested(row, col)) {
				tile.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
			}
			
			if (controller.getPieceTeam(row, col) == controller.getTurn() && withEventFilters) {
				// On-Click Menu
				final int pieceRow = row;
				final int pieceCol = col;
				ContextMenu onClickMenu = new ContextMenu();
				MenuItem attack = new MenuItem("Attack");
				attack.setDisable(controller.hasAttackedOrDefended(row, col));
				attack.setOnAction((event) -> {
						showAttacks(pieceRow, pieceCol);
					});
				
				MenuItem defend = new MenuItem("Defend");
				defend.setOnAction((event) -> {
						controller.defend(pieceRow, pieceCol);
					});
				defend.setDisable(controller.hasAttackedOrDefended(row, col));
				MenuItem move = new MenuItem("Move");
				move.setOnAction((event) -> {
						showMoves(pieceRow, pieceCol);
					});
				move.setDisable(controller.getMoveDistanceRemaining(row, col) == 0);
				MenuItem rest = new MenuItem("Rest");
				rest.setOnAction((event) -> {
						controller.rest(pieceRow, pieceCol);
					});
				rest.setDisable(controller.hasAttackedOrDefended(row, col));
				onClickMenu.getItems().addAll(attack, defend, move, rest);
				
				EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
			        @Override 
			        public void handle(MouseEvent e) { 
			        	int row = GridPane.getRowIndex(tile);
			        	int col = GridPane.getColumnIndex(tile);
			        	if (e.isSecondaryButtonDown()) {
				    		showMoves(row, col);
				    	} else {
				    		onClickMenu.show(tile, e.getScreenX(), e.getScreenY());
				    	}
			        }
			    };  
			    tile.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
				
			} else if (withEventFilters) {
				
				EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
			        @Override 
			        public void handle(MouseEvent e) { 
			        	displayMessage("This is not your piece.");
			        } 
			    };  
			    
			    tile.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
			}
			
			
			// Display character
		    ImageView sprite = new ImageView();
		    sprite.setImage(new Image(controller.getPieceSprite(row, col)));
		    sprite.setFitWidth(tileWidth);
		    sprite.setFitHeight(tileHeight * 5/6);
		    sprite.setPreserveRatio(true);
		    if (controller.getMoveDistanceRemaining(row, col) <= 0) {
		    	sprite.setOpacity(0.7);
		    }
			tile.add(sprite, 0, 0);
			GridPane.setHalignment(sprite, HPos.CENTER);
			
			// Display Health
			GridPane healthBar = new GridPane();
			Rectangle positive = new Rectangle((controller.getPieceHealth(row, col) / 100.0) * (tileWidth * 5/6), tileHeight * 1/6);
			positive.setFill(Color.LIMEGREEN);
			Rectangle negative = new Rectangle((1 - (controller.getPieceHealth(row, col) / 100.0)) * (tileWidth * 5/6), tileHeight * 1/6); 
			negative.setFill(Color.DARKRED);
			healthBar.add(positive, 0, 0);
			healthBar.add(negative, 1, 0);
			GridPane.setHalignment(positive, HPos.CENTER);
			GridPane.setHalignment(negative, HPos.CENTER);
			tile.add(healthBar, 0, 1);
			GridPane.setHalignment(healthBar, HPos.CENTER);
			
		}
	}
	
	/**
	 * Highlights all valid attacks for the piece at row, col in red.
	 * 
	 * @param row (int) : the row of the piece on the map
	 * @param col (int) : the column of the piece on the map
	 */
	private void showAttacks(int row, int col) {
		
		List<int[]> validAttacks = controller.getValidAttacks(row, col);
		
		if (validAttacks.size() == 0) {
			displayMessage("No valid attacks for this piece.");
			return;
		}
		
		for (int mapRow = 0; mapRow < controller.getBoardLength(); mapRow++) {
			for (int mapCol = 0; mapCol < controller.getBoardWidth(); mapCol++) {
				
				GridPane tile = new GridPane();
				setTile(tile, mapRow, mapCol, false);
				
				int[] curCoordinates = {mapRow, mapCol};
				
				boolean isAttack = false;
				for (int[] coord : validAttacks) {
					if (coord[0] == curCoordinates[0] && coord[1] == curCoordinates[1]) {
						isAttack = true;
					}
				}
				
				if (isAttack) {
					
					tile.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(5), Insets.EMPTY)));
					
					final int againstRow = mapRow;
					final int againstCol = mapCol;
					
					EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
				        @Override 
				        public void handle(MouseEvent e) { 
				        	try {
								controller.attack(row, col, againstRow, againstCol);
							} catch (FriendlyFireException e1) {
								displayMessage("This is your teammate! You cannot attack them!");
							} catch (InvalidRemovalException e1) {
								displayMessage("Invalid attack");
							}
				        } 
				    };  
				    
				    tile.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
				    
				} else {
					EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
				        @Override 
				        public void handle(MouseEvent e) { 
				        	setBoard();
				        }
				    };  
				    
				    tile.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
				}
				
				map.add(tile, mapCol, mapRow);
			}
		}
	}
	
	/**
	 * Highlights all valid moves for the piece at row, col in yellow.
	 * 
	 * @param row (int) : the row of the piece on the map
	 * @param col (int) : the column of the piece on the map
	 */
	private void showMoves(int row, int col) {

		List<int[]> validMoves = controller.getValidMoves(row, col);
		
		if (validMoves.size() == 0) {
			displayMessage("No valid moves for this piece.");
			return;
		}
		
		for (int mapRow = 0; mapRow < controller.getBoardLength(); mapRow++) {
			for (int mapCol = 0; mapCol < controller.getBoardWidth(); mapCol++) {
				
				GridPane tile = new GridPane();
				setTile(tile, mapRow, mapCol, false);
				
				int[] curCoordinates = {mapRow, mapCol};
				
				boolean isMove = false;
				for (int[] coord : validMoves) {
					if (coord[0] == curCoordinates[0] && coord[1] == curCoordinates[1]) {
						isMove = true;
					}
				}
				
				if (isMove) {
					
					tile.setBackground(new Background(new BackgroundFill(Color.YELLOW, new CornerRadii(5), Insets.EMPTY)));
					
					final int toRow = mapRow;
					final int toCol = mapCol;
					
					EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
				        @Override 
				        public void handle(MouseEvent e) { 
				        	try {
								controller.move(row, col, toRow, toCol);
							} catch (InvalidRemovalException e1) {
								displayMessage("You cannot move here.");
							} catch (InvalidMoveException e1) {
								displayMessage("You cannot move here.");
							} catch (OutOfMovesException e1) {
								displayMessage("This piece is out of moves.");
							}
				        } 
				    };  
				    
				    tile.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
				    
				} else {
					EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
				        @Override 
				        public void handle(MouseEvent e) { 
				        	setBoard();
				        }
				    };  
				    
				    tile.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
				}
				
				map.add(tile, mapCol, mapRow);
			}
		}
	}
	
	/**
	 * Updates the board, called by the Observable model. Ends the
	 * game if the last move resulted in a completed game.
	 */
	@Override
	public void update(Observable o, Object arg) {
		setBoard();
		if (controller.isOver()) {
			if (controller.getWinner() == Team.HUMAN) {
				
				mainView.displayNextLevelMenu(levelFileName);
		        
			} else {
				
				mainView.displayRetryMenu(levelFileName);
			}
		}
		saveUpToDate = false;
	}
	
	/**
	 * Displays the given message on the bottom GUI. Used for soft
	 * errors messages.
	 * 
	 * @param message (string) : the message to be displayed
	 */
	private void displayMessage(String message) {
		this.message.setText(message);
	}
	
	/**
	 * Returns the dimensions of the map in pixels.
	 * 
	 * @return int[] : a two-element array containing the width as the first
	 * element and the height as the second element.
	 */
	public int[] getMapDimensions() {
		int[] dimensions = {mapWidthPixel, mapHeightPixel};
		return dimensions;
	}
	
	/**
	 * Sets saveUpToDate to true to record that a save was just made.
	 */
	public void setSaveUpToDate() {
		saveUpToDate = true;
	}
	
	/**
	 * Returns whether any moves have been made since the last save.
	 * 
	 * @return
	 */
	public boolean isSaveUpToDate() {
		return saveUpToDate;
	}

}
