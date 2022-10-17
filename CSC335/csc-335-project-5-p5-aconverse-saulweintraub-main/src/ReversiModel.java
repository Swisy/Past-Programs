import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Observable;

/**
 * 
 * @author Saul Weintraub
 * 
 * This class acts as the model for the Reversi game.
 *
 */

@SuppressWarnings("deprecation")
public class ReversiModel extends Observable {
	
	private char[][] board;
	private int whiteScore;
	private int blackScore;
	private char curTurn;
	
	/**
	 * This is the default constructor for ReversiModel that will create a new model to be used for
	 * a new game of reversi.
	 */
	public ReversiModel() {
		// Set default values for fields
		whiteScore = 2;
		blackScore = 2;
		curTurn = 'w';
		board = new char[8][8];
		
		// Fill every part of the board with 'x' to signify a blank space
		for(int row = 0; row <= 7; row++) {
			for(int col = 0; col <= 7; col ++) {
				board[row][col] = 'x';
			}
		}
		// Fill the middle of the board with white and black pieces
		board[3][3] = 'w';
		board[3][4] = 'b';
		board[4][4] = 'w';
		board[4][3] = 'b';
		
	}
	
	/**
	 * This is a constructor for ReversiModel that will load a saved game using ObjectInputStream.
	 * 
	 * This constructor will use the data found in the save file that was created using 
	 * ObjectOutputStream to create a ReversiBoard object. The ReversiBoard object will then be
	 * used to fill the fields of this object.
	 * 
	 * @param saveFileName The name of the save file that was created by ObjectOutputStream
	 */
	public ReversiModel(String saveFileName) {
		File myFile = new File(saveFileName);
		FileInputStream inStream = null;
		
		try {
			inStream = new FileInputStream(myFile);
			ObjectInputStream input = new ObjectInputStream(inStream);
			ReversiBoard savedBoard = (ReversiBoard) input.readObject();
			
			whiteScore = savedBoard.getWhiteScore();
			blackScore = savedBoard.getBlackScore();
			curTurn = savedBoard.getTurn();
			board = savedBoard.getBoard();
			
			input.close();
			inStream.close();
		} catch (Exception e) { }
		
	}
	
	/**
	 * This method will return the piece in the board at the coordinates.
	 * 
	 * 'b' means there is a black piece, 'w' means there is a white piece, and 'x' means that the
	 * board has no piece at the designated coordinates.
	 * 
	 * @param row The row of the board. 0 is the leftmost and 7 is the rightmost
	 * @param col The column of the board. 0 is the top and 7 is the bottom
	 * @return A char that signifies the piece on the board at the specified coordinates 
	 */
	public char getPiece(int row, int col) {
		return board[row][col];
	}
	
	/**
	 * This method will put a piece on the board.
	 * 
	 * This method will replace the value of board[row][col] with newPiece. This method will also
	 * update the whiteScore and blackScore fields and will call setChanged().
	 * 
	 * @param row The row of the board. 0 is the leftmost and 7 is the rightmost
	 * @param col The column of the board. 0 is the top and 7 is the bottom
	 * @param newPiece The color of the piece that is being placed on the board. 'b' or 'w'
	 */
	public void setPiece(int row, int col, char newPiece) {
		if(board[row][col] == newPiece) {
			// Don't change any scores
		} else if(newPiece == 'b') {
			if(board[row][col] == 'w') {
				whiteScore -= 1;
				blackScore += 1;
			} else {
				blackScore += 1;
			}
		} else if (newPiece == 'w') {
			if(board[row][col] == 'b') {
				whiteScore += 1;
				blackScore -= 1;
			} else {
				whiteScore += 1;
			}
		}
		board[row][col] = newPiece;
		setChanged();
	}
	
	/**
	 * This method will return the whiteScore, which is the amount of white pieces on the board.
	 * 
	 * @return The amount of white pieces on the board
	 */
	public int getWhiteScore() {
		return whiteScore;
	}
	
	/**
	 * This method will return the blackScore, which is the amount of black pieces on the board.
	 * 
	 * @return The amount of black pieces on the board
	 */
	public int getBlackScore() {
		return blackScore;
	}
	
	/**
	 * This method will return which colors turn it is.
	 * 
	 * @return The color whose turn it is. 'b' or 'w'
	 */
	public char getTurn() {
		return curTurn;
	}
	
	/**
	 * This method will switch curTurn from 'w' to 'b' or from 'b' to 'w'.
	 */
	public void switchTurn() {
		if(curTurn == 'w') {
			curTurn = 'b';
		} else {
			curTurn = 'w';
		}
	}
	
	/**
	 * This method creates an instance of ReversiBoard and calls notifyObservers().
	 */
	public void setUpNotifyObservers() {
		ReversiBoard reversiBoard = new ReversiBoard(board, curTurn);
		notifyObservers(reversiBoard);
	}
	
}