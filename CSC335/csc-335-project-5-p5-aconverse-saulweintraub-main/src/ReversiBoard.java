import java.io.Serializable;

/**
 * 
 * @author Amber Converse
 *
 */

public class ReversiBoard implements Serializable {
	
	private char[][] board;
	private int whiteScore;
	private int blackScore;
	private char curTurn;
	
	/**
	 * Initializes the board based on an 8x8 array of characters.
	 * 
	 * @param board
	 */
	public ReversiBoard(char[][] board, char curTurn) {
		this.board = board;
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (this.board[row][col] == 'w') {
					whiteScore++;
				}
				if (this.board[row][col] == 'b') {
					blackScore++;
				}
			}
		}
		this.curTurn = curTurn;
	}
	
	/**
	 * returns the board
	 * 
	 * @return char[][]: an 8x8 array representing a game board
	 */
	public char[][] getBoard() {
		return board;
	}
	
	/**
	 * returns the current whiteScore
	 * 
	 * @return int: whiteScore
	 */
	public int getWhiteScore() {
		return whiteScore;
	}
	
	/**
	 * returns the current blackScore
	 * 
	 * @return int: whiteScore
	 */
	public int getBlackScore() {
		return blackScore;
	}
	
	/**
	 * returns the current turn
	 * 
	 * @return char: the current turn
	 */
	public char getTurn() {
		return curTurn;
	}
	
}