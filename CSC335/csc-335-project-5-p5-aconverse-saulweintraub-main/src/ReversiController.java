
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javafx.scene.paint.Color;

/**
 * 
 * @author Amber Converse and Saul Weintraub
 * 
 * This class acts as the controller for a Reversi game.
 */

public class ReversiController {
	
	private ReversiModel model;
	
	/**
	 * This is the default constructor for ReversiController.
	 * 
	 * @param model A ReversiModel object that will be used to store the game information
	 */
	public ReversiController(ReversiModel model) {
		this.model = model;
	}

	/**
	 * This method performs a turn at the given pos (row, col).
	 * If the turn if illegal, nothing happens. Otherwise, the
	 * turn is performed and affected pieces are flipped.
	 * 
	 * If placing a tile at the specified coordinates is a valid move, then this method will 
	 * perform the move and update the model. After the move has been performed, the method will
	 * change turns if the opponent has any legal moves it can perform.
	 * 
	 * @param row The row where the piece will be placed
	 * @param col The column where the piece will be placed
	 */
	public void humanTurn(int row, int col) {
		if (isLegal(row, col, model.getTurn())) {
			model.setPiece(row, col, model.getTurn());
			for (int rowIncr = -1; rowIncr < 2; rowIncr++) {
				for (int colIncr = -1; colIncr < 2; colIncr++) {
					
					int[] flipToPos = getFlipPos(row, col, rowIncr, colIncr);

					int curRow = row;
					int curCol = col;
					while (curRow != flipToPos[0] || curCol != flipToPos[1]) {
						model.setPiece(curRow, curCol, model.getTurn());
						curRow += rowIncr;
						curCol += colIncr;
					}
				}
			}
			if (hasLegalMoves(getOppositeTurn())) {
				model.switchTurn();
			}
		}
		model.setUpNotifyObservers();
	}
	
	/**
	 * This method counts the number of pieces that would be flipped if a piece was placed at the
	 * specified coordinates.
	 * 
	 * If placing a piece at the specified coordinates is a legal move then this method will return
	 * the number of pieces that would be flipped by placing a piece at the specified coordinates.
	 * If it is not a valid move, then this method will return 0.
	 * 
	 * @param row The row where the piece will be placed
	 * @param col The column where the piece will be placed
	 * @return The number of pieces that would be flipped by the move. 0 if the move is invalid
	 */
	public int dummyHumanTurn(int row, int col) {
		int flipCount = 0;
		if (isLegal(row, col, model.getTurn())) {
			flipCount++;
			for (int rowIncr = -1; rowIncr < 2; rowIncr++) {
				for (int colIncr = -1; colIncr < 2; colIncr++) {
					int[] flipToPos = getFlipPos(row, col, rowIncr, colIncr);
					int curRow = row;
					int curCol = col;
					while (curRow != flipToPos[0] || curCol != flipToPos[1]) {
						if (curRow != row || curCol != col) {
							flipCount++;
						}
						curRow += rowIncr;
						curCol += colIncr;
					}
				}
			}
		}
		return flipCount;
	}
	
	/**
	 * This method will perform the computer's turn.
	 * 
	 * This method will find the move that will get the computer the most points and will perform
	 * that move. If there is more than one move that will get the computer the most points, then
	 * the computer will randomly select on of those moves.
	 */
	public void computerTurn() {
		Map<Integer, int[]> moves = new HashMap<Integer, int[]>();
		int greatest = 0;
		Random random = new Random();
		
		// Fill the map with possible moves
		
		// Iterate through the board
		for(int row = 0; row <= 7; row++) {
			for(int col = 0; col <= 7; col++) {
				// If there is no piece at the coordinates
				if(model.getPiece(row, col) == 'x') {
					// If the move is legal
					if(isLegal(row, col, model.getTurn())) {
						int points = dummyHumanTurn(row, col);
						if(points > greatest) {
							greatest = points;
						}
						int[] coordinates = {row, col};
						// If there is already a move in the dictionary with the same number of points
						if(moves.containsKey(points)) {
							// Randomly decide to replace the previous mapping
							if(random.nextInt(2) == 1) {
								moves.put(points, coordinates);
							}
						} else {
							moves.put(points, coordinates);
						}
					}
				}
			}
		}
		
		// Perform the move
		int[] coordinates = moves.get(greatest);
		humanTurn(coordinates[0], coordinates[1]);
	}
	
	/**
	 * This method will return whether or not placing a piece at the specified row and column is a
	 * legal move.
	 * 
	 * This method will check if the move is legal in every direction. If the move is legal in
	 * at least one direction, then this method will return true. If the move is legal in no
	 * directions, then this method will return false.
	 * 
	 * @param row The row of the piece that will be placed
	 * @param col The column of the piece that will be placed
	 * @param player The current player. 'b' or 'w'
	 * @return If the move is legal
	 */
	private boolean isLegal(int row, int col, char player) {
		if (row >= 0 && row < 8 && col >= 0 && col < 8) {
			for (int rowIncr = -1; rowIncr < 2; rowIncr++) {
				for (int colIncr = -1; colIncr < 2; colIncr++) {
					if (legal(row, col, rowIncr, colIncr, player)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * This method will return whether or not the specified player has any legal moves it can
	 * perform.
	 * 
	 * This method will iterate through every position on the board and test if it is legal to
	 * place a piece there. If at least one position can have a piece placed then the method
	 * returns true.
	 * 
	 * @param player The player that is being checked if they have legal moves
	 * @return If the player has any legal moves they can perform
	 */
	private boolean hasLegalMoves(char player) {
		for(int row = 0; row <= 7; row++) {
			for(int col = 0; col <= 7; col++) {
				if(isLegal(row, col, player)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * This method will check to see if placing a piece at the chosen coordinates would result in a
	 * legal move in the chosen direction.
	 * 
	 * This method will check if placing a piece at the chosen coordinates would flip any pieces in
	 * the chosen direction. This method will call upon legalHelper to recursively check if a piece
	 * of the same color as the current player is at the end of a chain of opposite colored pieces
	 * in the chosen direction.
	 * 
	 * @param row The row of the piece that would be placed
	 * @param col The column of the piece that would be placed
	 * @param rowOffset Decides the direction of the check. 1 = down, -1 = up
	 * @param colOffset Decides the direction of the check. 1 = right, -1 = left
	 * @param player The current player. 'b' or 'w'
	 * @return If the move would be legal in the chosen direction
	 */
	private boolean legal(int row, int col, int rowOffset, int colOffset, char player) {
		// Make a variable opposite that stores the color opposite the player
		char opposite;
		if(player == 'b') {
			opposite = 'w';
		} else {
			opposite = 'b';
		}
		
		// Make sure there is no piece at the specified location
		if(model.getPiece(row, col) != 'x') {
			return false;
		}
		
		if(((row + rowOffset) > 7) || ((row + rowOffset) < 0)) {
			return false;
		} else if(((col + colOffset) > 7) || ((col + colOffset) < 0)) {
			return false;
		}
		
		if(model.getPiece(row + rowOffset, col + colOffset) == opposite) {
			return legalHelper(row, col, rowOffset, colOffset, player, opposite);
		} else {
			return false;
		}
	}
	
	/**
	 * This method is a helper method which will recursively check if the pieces in the chosen
	 * direction are made up of opposite colored pieces followed by a piece of the player's color.
	 *
	 * @param row The row of the piece that would be placed
	 * @param col The column of the piece that would be placed
	 * @param rowOffset Decides the direction of the check. 1 = down, -1 = up
	 * @param colOffset Decides the direction of the check. 1 = right, -1 = left
	 * @param player The current player. 'b' or 'w'
	 * @param opposite The opposite of the current player. 'b' or 'w'
	 * @return If the move would be legal in the chosen direction
	 */
	private boolean legalHelper(int row, int col, int rowOffset, int colOffset, char player, char opposite) {
		row += rowOffset;
		col += colOffset;
		if((row > 7) || (row < 0) || (col > 7) || (col < 0)) {
			return false;
		}
		if(model.getPiece(row, col) == 'x'){
			return false;
		}
		// If there is a piece at the bottom that is the same color as the current turn, return true
		if(model.getPiece(row, col) == player) {
			return true;
		}
		return legalHelper(row, col, rowOffset, colOffset, player, opposite);
	}
	
	/**
	 * This method will test if the game is over.
	 * 
	 * If neither players are able to make any valid moves, then the game is over.
	 * 
	 * @return True or false depending on if the game is over
	 */
	public boolean isOver() {
		if(hasLegalMoves('b') || hasLegalMoves('w')) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * This method will get the Color of a piece on the board that can be used by javafx.
	 * 
	 * This method will return the color white or black depending on the piece at the coordinates.
	 * If there is no piece at the coordinates, the method will return transparent.
	 * 
	 * @param row The row of the piece
	 * @param col The column of the piece
	 * @return The color of the piece at the coordinates that can be used by javafx
	 */
	public Color getPieceColor(int row, int col) {
		if(model.getPiece(row, col) == 'w') {
			return Color.WHITE;
		} else if(model.getPiece(row, col) == 'b') {
			return Color.BLACK;
		} else {
			return Color.TRANSPARENT;
		}
	}
	
	/**
	 * This method will return the number of white pieces on the board.
	 * 
	 * @return The number of white pieces on the board
	 */
	public int getWhiteScore() {
		return model.getWhiteScore();
	}
	
	/**
	 * This method will return the number of black pieces on the board.
	 * 
	 * @return The number of black pieces on the board
	 */
	public int getBlackScore() {
		return model.getBlackScore();
	}
	
	/**
	 * This method will return whose turn it is.
	 * 
	 * @return Whose turn it is. 'b' or 'w'
	 */
	public char getTurn() {
		return model.getTurn();
	}
	
	/**
	 * This method will return the opposite of who's turn it is.
	 * 
	 * @return Whose turn it isn't. 'b' or 'w'
	 */
	private char getOppositeTurn() {
		if(model.getTurn() == 'w') {
			return 'b';
		} else {
			return 'w';
		}
	}
	
	/**
	 * This function takes a given piece at position (row, col) and increments to the
	 * end of the board in the direction based on rowIncr and colIncr to see to what point
	 * pieces will be flipped.
	 * 
	 * @param row
	 * @param col
	 * @param rowIncr
	 * @param colIncr
	 * @return int[]: an array of coordinate pairs in the format [row, col]
	 */
	private int[] getFlipPos(int row, int col, int rowIncr, int colIncr) {
		int newRow = row + rowIncr;
		int newCol = col + colIncr;
		while (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8 &&
				model.getPiece(newRow, newCol) == getOppositeTurn()) {
			newRow += rowIncr;
			newCol += colIncr;
		}
		if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8 &&
			model.getPiece(newRow, newCol) == model.getTurn()) {
			return new int[] {newRow, newCol};
		}
		return new int[] {row, col};
	}

}