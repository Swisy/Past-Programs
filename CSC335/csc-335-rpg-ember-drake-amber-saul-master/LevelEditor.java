import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.InvalidLevelException;
import model.LevelEditorModel;
import model.SaveFailureException;
import model.Team;
import onboard.Archer;
import onboard.ArmoredKnight;
import onboard.BlockedSeeThroughTile;
import onboard.BlockedTile;
import onboard.Horseman;
import onboard.InvalidMoveException;
import onboard.InvalidRemovalException;
import onboard.Knight;
import onboard.OpenTile;
import onboard.Pegasus;
import onboard.Piece;
import onboard.Tile;

/**
 * Class for viewing and controlling the level editor
 * 
 * @author Ember Chan
 *
 */
public class LevelEditor {

	private StrategyGameView mainView;
	private Scene scene;
	private LevelEditorModel model;
	private BorderPane root;
	private Set<StackPane> selectables;
	
	private static int TILE_SIZE = 64;
	private static final Tile[] TILE_SAMPLES = new Tile[] {
		new OpenTile(), new BlockedTile(), new BlockedSeeThroughTile()
	};
	private static final Piece[] PIECE_SAMPLES = new Piece[] {
		new Knight(Team.HUMAN), new Knight(Team.COMPUTER),
		new Archer(Team.HUMAN), new Archer(Team.COMPUTER),
		new Horseman(Team.HUMAN), new Horseman(Team.COMPUTER),
		new ArmoredKnight(Team.HUMAN), new ArmoredKnight(Team.COMPUTER),
		new Pegasus(Team.HUMAN), new Pegasus(Team.COMPUTER)
	};
	private static final String NULL_TILE_IMG_PATH = "assets/NullTile.png";
	
	/**
	 * Creates a new LevelEditor on the given scene
	 * @param scene - the scene to display the level editor on
	 */
	public LevelEditor(StrategyGameView mainView) {
		selectables = new HashSet<StackPane>();
		
		this.mainView = mainView;
		model = new LevelEditorModel();
		root = new BorderPane();
		this.scene = new Scene(root);
		initMenu();
		update();
		initTilesBar();
		initPiecesBar();
		mainView.stage.setScene(scene);
	}
	
	/**
	 * Updates the scene with the current state of the model
	 */
	private void update() {
		//Map is represented by a GridPane of StackPanes
		//Each stackpane has space for a tile image and a piece image
		
		GridPane map = new GridPane();
		for (int row = 0; row < LevelEditorModel.SIZE; row++) {
			for(int col = 0; col < LevelEditorModel.SIZE; col++) {
				StackPane sp = new StackPane();
				Tile tile = model.getTile(row, col);
				
				//Add tile image
				if(tile == null) {
					sp.getChildren().add(new ImageView(NULL_TILE_IMG_PATH));
					makeModifiable(sp, row, col);
					map.add(sp, col, row);
					continue;
				}
				ImageView tileImg = new ImageView(tile.getImgPath());
				sp.getChildren().add(tileImg);
				
				//add piece image
				if(tile.getPiece()!= null) {
					ImageView pieceImg = new ImageView(tile.getPiece().getSpriteFileName());
					pieceImg.setFitWidth(TILE_SIZE);
					pieceImg.setFitHeight(TILE_SIZE);
					sp.getChildren().add(pieceImg);
				}
				
				makeModifiable(sp, row, col);
				map.add(sp, col, row);
			}
		}
		root.setCenter(map);
	}
	
	
	
	/**
	 * Initializes the menu to the top of root
	 */
	private void initMenu() {
		Menu menu = new Menu("Menu");
		
		//Save level button
		MenuItem saveLevel = new MenuItem("Save Level");
		saveLevel.setOnAction((event)->{saveGame();});
		menu.getItems().add(saveLevel);
		
		//Return to main menu button
		MenuItem returnToMainMenu = new MenuItem("Return to Main Menu");
		returnToMainMenu.setOnAction((event)->{mainView.returnToMainMenu();});
		menu.getItems().add(returnToMainMenu);
		
		root.setTop(new MenuBar(menu));
	}
	
	/**
	 * Initializes the tiles bar to the right of the screen
	 */
	private void initTilesBar() {
		//TODO Encapsulate with scrollpane if we get that many tiles
		HBox tilesBar = new HBox();
		
		
		StackPane eraser = new StackPane(new ImageView(NULL_TILE_IMG_PATH));
		makeSpSelectable(eraser, null);
		tilesBar.getChildren().add(eraser); 

		for(Tile t:TILE_SAMPLES) {
			ImageView tileImg = new ImageView(t.getImgPath());
			StackPane sp = new StackPane();
			sp.getChildren().add(tileImg);
			makeSpSelectable(sp, t);
			tilesBar.getChildren().add(sp);
		}
		
		root.setBottom(tilesBar);
	}
	
	/**
	 * Initializes the pieces bar to the left of the screen
	 */
	private void initPiecesBar() {
		//TODO Encapsulate with scrollpane if we get that many pieces
		ScrollPane scroller = new ScrollPane();
		scroller.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroller.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scroller.setPrefHeight(100);
		scroller.setPrefWidth(100);
		VBox piecesBar = new VBox();
		
		
		for(Piece p:PIECE_SAMPLES) {
			ImageView pieceImg = new ImageView(p.getSpriteFileName());
			pieceImg.setFitWidth(TILE_SIZE);
			pieceImg.setFitHeight(TILE_SIZE);
			StackPane sp = new StackPane();
			sp.getChildren().add(pieceImg);
			makeSpSelectable(sp, p);
			piecesBar.getChildren().add(sp);
		}
		scroller.setContent(piecesBar);
		root.setLeft(scroller);
	}
	
	/**
	 * Makes the given StackPane selectable by the user
	 * @param sp the StackPane to be made selectable
	 * @param selection what should be selected when the user clicks the StackPane, as 
	 * an object. Will update model.selection to this object when clicked
	 */
	private void makeSpSelectable(StackPane sp, Object selection) {
		sp.setPadding(new Insets(5, 5, 5, 5));
		sp.setOnMouseClicked((event)->{
			for(StackPane o : selectables) {
				o.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
			}
			sp.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
			model.selection = selection;
		});
		selectables.add(sp);
	}
	

	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	private void makeModifiable(StackPane sp, int row, int col) {
		sp.setOnMouseClicked((event)->{
			try {
				if(model.selection == null) {
					model.setTile(row, col, null);
				} else if (model.selection instanceof Tile) {

					model.setTile(row, col, (Tile) model.selection.getClass().newInstance());

				} else if(model.getTile(row, col) instanceof OpenTile) {
					Piece samplePiece = (Piece) model.selection;
					Constructor[] ctors = samplePiece.getClass().getDeclaredConstructors();
					Constructor ctor = null;
					for (int i = 0; i<ctors.length; i++) {
						if (ctors[i].getParameterCount() == 1) {
							ctor = ctors[i];
						}
					}
					Piece newPiece = (Piece) ctor.newInstance(samplePiece.getTeam());
					newPiece.setTeam(samplePiece.getTeam());
					if(model.getTile(row, col).getPiece()!=null) {
						model.getTile(row, col).removePiece();
					}
					model.getTile(row, col).setPiece(newPiece);
				}
			} catch (InstantiationException | IllegalAccessException | InvalidMoveException | IllegalArgumentException | 
					InvocationTargetException | InvalidRemovalException e) {
				Alert a = new Alert(AlertType.ERROR);
				a.setContentText("Fatal Error; Returning to main menu");
				a.showAndWait();
				mainView.returnToMainMenu();
			}
			
			update();
		});
	}
	
	/**
	 * Prompts the user for the name of their custom level and 
	 * saves the game
	 */
	private void saveGame() {
		TextInputDialog saveInput = new TextInputDialog("name your level");
		saveInput.showAndWait();
		try {
			model.saveLevel(saveInput.getEditor().getText());
		} catch (SaveFailureException e) {
			Alert a = new Alert(AlertType.ERROR);
			a.setContentText("Error While Saving Level");
			a.showAndWait();
		} catch (InvalidLevelException e) {
			Alert a = new Alert(AlertType.ERROR);
			a.setContentText("Error: Invalid level\n"
					+ "Make sure you gave it a name.\n"
					+ "Make sure there are no empty tiles.\n"
					+ "Make sure there is at least one piece for each player.");
			a.showAndWait();
		}
		
	}
}
