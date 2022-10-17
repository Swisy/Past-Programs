package model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import onboard.Piece;
import onboard.Tile;

/**
 * This class acts as the data accessor for the level editor
 * 
 * @author Ember Chan
 *
 */
public class LevelEditorModel {
	private StrategyGameState level;
	public static final int SIZE = 8;
	public Object selection;
	
	/**
	 * No argument constructor for LevelEditorModel
	 * 
	 * Creates a new LevelEditorModel with an empty board
	 */
	public LevelEditorModel() {
		level = new StrategyGameState();
		level.backgroundImageFileName = "custom";
		level.board = new Tile[SIZE][SIZE];
		level.currentTurn = Team.HUMAN;
	}
	
	/**
	 * Sets the given space on the board to the given tile
	 * @param row - the row of the space on board to set, as an int
	 * @param col - the column of the space on board to set, as an int
	 * @param tile - the Tile to set the space on the board to
	 */
	public void setTile(int row, int col, Tile tile) {
		level.board[row][col] = tile;
	}
	
	/**
	 * Returns the tile at the given row and column
	 * @param row - the row of the tile to get, as an int
	 * @param col - the column of the tile to get, as an int
	 * @return - the Tile at the row and column. Will return null if there is no tile
	 * at that position
	 */
	public Tile getTile(int row, int col) {
		return level.board[row][col]; 
	}
	
	/**
	 * Saves the current level to the levels folder with the given name
	 * @param levelName - the name of level to put into the levels folder
	 * @throws SaveFailureException if there was an issue saving the level
	 * @throws InvalidLevelException if the the level isn't playable or a bad filename was given
	 */
	public void saveLevel(String levelName) throws SaveFailureException, InvalidLevelException {
		if(levelName == "") {
			throw new InvalidLevelException();
		}
		int humanPieces = 0;
		int computerPieces = 0;
		for(int i = 0; i<SIZE; i++) {
			for (int j = 0; j<SIZE; j++) {
				if(level.board[i][j] == null) {
					throw new InvalidLevelException();
				}
				Piece p = level.board[i][j].getPiece();
				if(p!= null) {
					if(p.getTeam().equals(Team.HUMAN)) {
						humanPieces++;
					} else {
						computerPieces++;
					}
				}
			}
		}
		if(humanPieces == 0 || computerPieces == 0) {
			throw new InvalidLevelException();
		}
		
		try (ObjectOutputStream objOut =
				new ObjectOutputStream(new FileOutputStream("levels/"+levelName+".dat"))){
			objOut.writeObject(level);
		} catch (IOException e) {
			throw new SaveFailureException();
		}
	}
	
	
}
