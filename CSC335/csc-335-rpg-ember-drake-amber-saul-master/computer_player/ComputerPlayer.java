package computer_player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import controller.StrategyGameController;
import model.StrategyGameModel;
import model.Team;
import onboard.BlockedSeeThroughTile;
import onboard.BlockedTile;
import onboard.Flyer;
import onboard.FriendlyFireException;
import onboard.InvalidMoveException;
import onboard.InvalidRemovalException;
import onboard.OutOfMovesException;
import onboard.Pegasus;
import onboard.Piece;
import onboard.Tile;

/**
 * This class implements the logic for the Computer player.
 * The main logic involves a Breadth-First search to find
 * the path for the ComputerPlayer to move each of their
 * pieces toward or away from the nearest human piece.
 * 
 * 
 * @author Drake Sitaraman
 *
 */
public class ComputerPlayer{
	
	private StrategyGameModel model;
	
	private ArrayList<Piece> pieces;
	private HashMap<Piece, Coordinate> pieceToCoord;
	
	private final int boardHeight;
	private final int boardWidth;
	
	//Constants used for the BFS
	private final int UP = 0;
	private final int DOWN = 1;
	private final int LEFT = 2;
	private final int RIGHT = 3;
	private final int UP_RIGHT = 4;
	private final int UP_LEFT = 5;
	private final int DOWN_RIGHT = 6;
	private final int DOWN_LEFT = 7;
	
	//These two fields are for testing shortest path.
	protected int shortestRow;
	protected int shortestCol;
	
	//This field is for testing makeMove.
	protected int piecesMoved;
	
	/**
	 * Initializes the model, controller, constants for board height and width,
	 * and gets references to each of the Computer Player's pieces.
	 * 
	 * @param controller
	 * @param model
	 */
	public ComputerPlayer(StrategyGameModel model) {
		this.model = model;
		this.boardHeight = model.getBoardHeight();
		this.boardWidth = model.getBoardWidth();

		
		
	}
	
	/**
	 * This method loops through all pieces and calls the functions
	 * necessary to move all of them.
	 */
	public void makeMove() {
		this.getPieces();
		
		HashSet<Integer> indicesSeen = new HashSet<Integer>();
		piecesMoved = 0;
		for(int i = 0; i<pieces.size(); i++) {
			Piece currPiece = pieceDecider(indicesSeen);
			Coordinate currLoc = pieceToCoord.get(currPiece);
			
			moveTowardHumanPiece(currPiece, currLoc.row, currLoc.col);
			
			piecesMoved++;
		}
	}
	
	/**
	 * This method generates a random integer between 0
	 * and the number of pieces on the board - 1. It does
	 * this until a unique number has been generated for this turn.
	 * It then gets the proper Piece object based on this index
	 * from the ArrayList.
	 * 
	 * @param indicesSeen is a HashSet of integers representing
	 * the indices already seen for this turn. This ensures
	 * that the computer player moves all its pieces.
	 * 
	 * @return a random Piece object from the pieces ArrayList
	 */
	private Piece pieceDecider(HashSet<Integer> indicesSeen) {
		Random rand = new Random();
		int randNum;
		do {
			randNum = rand.nextInt(pieces.size());
		} while(indicesSeen.contains(randNum));
		
		indicesSeen.add(randNum);
		
		return pieces.get(randNum);
	}
	
	
	/**
	 * This method gets references to each of the Computer Player's pieces.
	 * The method ensures that the pieces are alive (piece!=null), and
	 * the piece actually belongs to the computer.
	 */
	private void getPieces() {
		pieces = new ArrayList<Piece>();
		pieceToCoord = new HashMap<Piece, Coordinate>();
		for(int row = 0; row<boardHeight; row++) {
			for(int col = 0; col<boardWidth; col++) {
				Tile tile = model.getTile(row, col);
				Piece piece = tile.getPiece();
				if(piece != null && piece.getTeam().equals(Team.COMPUTER)) {
					pieces.add(piece);
					pieceToCoord.put(piece, new Coordinate(row, col));
				}
			}
		}
		
	}
	
	/**
	 * This method converts human-readable constants into their equivalent
	 * coordinates. This is useful for looping through each direction
	 * in the BFS.
	 * 
	 * @param fromRow is an int representing the current relative row.
	 * @param fromCol is an int representing the current relative column.
	 * @param direction is an int representing a constant direction 0-7.
	 * These are all private static final constants declared at the top
	 * of the program and used in the switch statement.
	 * 
	 * @return A Coordinate object representing a (row,col) coordinate
	 * pair, which is a direction away from the current row and column.
	 */
	private Coordinate getCoordinate(int fromRow, int fromCol, int direction) {
		switch(direction) {
			case(UP): return new Coordinate(fromRow-1, fromCol);
			case(DOWN): return new Coordinate(fromRow+1, fromCol);
			case(LEFT): return new Coordinate(fromRow, fromCol-1);
			case(RIGHT): return new Coordinate(fromRow, fromCol+1);
			case(UP_RIGHT): return new Coordinate(fromRow-1, fromCol+1);
			case(UP_LEFT): return new Coordinate(fromRow-1, fromCol-1);
			case(DOWN_RIGHT): return new Coordinate(fromRow+1, fromCol+1);
		} //else DOWN_LEFT
		
		return new Coordinate(fromRow+1, fromCol-1);
	}
	/**
	 * This method gets a Tile object from the model from a row
	 * and column ints as input.
	 * 
	 * @param row is an int representing the row.
	 * @param col is an int representing the column
	 * @return The Tile object in the 2D array if it exists, or
	 * null if it is out of bounds.
	 */
	private Tile coordToTile(int row, int col) {
		
		//Out of bounds
		if(col < 0 || col >= boardWidth || row<0 || row >= boardHeight) {
			return null;
		}
		
		return model.getTile(row, col);
	}
	
	/**
	 * This method moves a piece towards a human. This is done, first,
	 * by calling the shortest path method. Then, the computer advances
	 * as many tiles along that path as it can, until it gets within its
	 * attack range. Once this happens, the Piece attacks the human
	 * piece found from the shortest path algorithm. If the Piece
	 * is too far away, it defends.
	 * 
	 * @param piece is a Piece object representing the computer player's current
	 * piece.
	 * 
	 * @param compPieceRow is the row as an int, where the current piece is.
	 * @param compPieceCol is the col as an int, where the current piece is.
	 */
	protected void moveTowardHumanPiece(Piece piece, int compPieceRow, int compPieceCol) {

		Stack<Coordinate> shortestPath = shortestPath(compPieceRow, compPieceCol, piece);
		
		//This will probably never happen
		if(shortestPath == null) {
			piece.defend();
			return;
		}
		
		int currRow = compPieceRow;
		int currCol = compPieceCol;
		
		
		
		while(shortestPath.size() > piece.getAttackDistance() 
				&& piece.getMoveDistanceRemaining() > 0) {
			Coordinate nextMove = shortestPath.pop();
			Tile toMove = coordToTile(nextMove.row, nextMove.col);
			Tile prevMove = coordToTile(currRow, currCol);
			
			if(!toMove.canMoveInto(piece) || 
					(nextMove.row == currRow && currCol == nextMove.col)) {
				break;
			}

			prevMove.removeComputerPiece();
			toMove.setComputerPiece(piece);
			currRow = nextMove.row;
			currCol = nextMove.col;
			piece.moveComputer(1);
			model.setUpNotifyObservers();
				
			
		}
				
		
		
		if(shortestPath.size() > piece.getAttackDistance() ) {
			piece.defend();
			model.setUpNotifyObservers();
			return;
		}
		
	
		//Pop until only the enemy's coordinates remains on the stack.
		while(shortestPath.size() > 1) {
			shortestPath.pop();
		}
		
		
		Coordinate attack = shortestPath.pop();
		

		
		
		try {
			piece.attack(coordToTile(attack.row, attack.col));
			
		
		//This will never happen, but defend if it does.
		} catch (FriendlyFireException | InvalidRemovalException e1) {
			piece.defend();
		}
		
		model.setUpNotifyObservers();
	}

	
	/**
	 * This method represents a common algorithm for a Breadth-First
	 * search. Each Coordinate on the board is represented
	 * using a "Coordinate" inner class. Each Coordinate visited
	 * will enqueue all 8 directions, assuming the tile
	 * is not out of bounds.
	 * 
	 * @param fromRow is the row as an int where the computer piece is.
	 * @param fromCol is the col as an int where the computer piece is.
	 * @return The shortest path from the computer piece's coordinates
	 * to the nearest human piece's coordinates as a Stack.
	 */
	private Stack<Coordinate> shortestPath(int fromRow, int fromCol, Piece piece) {
		
		Queue<Coordinate> q = new LinkedList<Coordinate>();
		HashSet<Coordinate> coordinatesVisited = new HashSet<Coordinate>();
		
		//Keep track of how you got to a particular coordinate.
		Coordinate[][] pathTo = new Coordinate[boardHeight][boardWidth];
		
		Coordinate start = new Coordinate(fromRow, fromCol);

		q.add(start);
		coordinatesVisited.add(start);
		
		while(!q.isEmpty()) {
			Coordinate c = q.remove();
			coordinatesVisited.add(c);
			
			
			//Visit all 8 directions.
			for(int i = 0; i<8; i++) {
				
				Coordinate adjacent = getCoordinate(c.row, c.col, i);
				
				
				Tile tile = coordToTile(adjacent.row, adjacent.col);
				
				
				
				//If out of bounds or already visited 
				if(tile == null  
						|| coordinatesVisited.contains(adjacent)) {
					continue;
					
				}
				
				if(!(tile.isOpenTile())) {
					if(!(piece instanceof Flyer)) {
						continue;
					}
				}
				
				coordinatesVisited.add(adjacent);
				q.add(adjacent);
				
				pathTo[adjacent.row][adjacent.col] = c;
				
				
				Piece onAdjacent = tile.getPiece();
				
		
				

				//Human found, start executing path.
				if(onAdjacent != null && onAdjacent.getTeam().equals(Team.HUMAN)) {
					return getPath(pathTo, adjacent);
				}
				
				
								
			}
		}
		
		return null; //No human on board, but if that were the case, this method would not have been called.
		
	}
	
	/**
	 * When this method is called, a shortest path has been found. The
	 * path is stored in the 2D Coordinate array, pathTo. 
	 * 
	 * @param pathTo is a 2D Coordinate array where a [row][col] array
	 * which represents which Coordinate the computer would have to
	 * move through to row,col.
	 * 
	 * @param finalCoord is the Coordinate representing the human's location
	 * on the board.
	 * 
	 * @return A conversion from the 2D array to a one-dimensional stack, where
	 * the top of the stack is the first possible human move and the bottom
	 * of the stack is where the nearest human piece is.
	 */
	private Stack<Coordinate> getPath(Coordinate[][] pathTo, Coordinate finalCoord){
		Stack<Coordinate> shortestPath = new Stack<Coordinate>();
		
		shortestRow = finalCoord.row;
		shortestCol = finalCoord.col;

		shortestPath.push(finalCoord);
		Coordinate currCoord = pathTo[finalCoord.row][finalCoord.col];
		
		while(currCoord != null) {
			shortestPath.push(currCoord);
			currCoord = pathTo[currCoord.row][currCoord.col];
		}
		
		shortestPath.pop(); //Top of the stack is the current piece--pop, otherwise pieces would always stay still
		
		return shortestPath;
	}
	
	/**
	 * Simple class representing a (row, col) tuple. Useful
	 * for determining visited Coordinates in the BFS.
	 * 
	 * @author Drake Sitaraman
	 *
	 */
	private class Coordinate {
		public int row;
		public int col;

		
		public Coordinate(int row, int col) {
			this.row = row;
			this.col = col;
		}
		
		/**
		 * Two Coordinates are equal if their row and col are equal.
		 */
		public boolean equals(Object other) {
			//This will never happen.
			if(!(other instanceof Coordinate)) {
				return false;
			}
			
			Coordinate o = (Coordinate) other;
			
			return ((this.row == o.row) && (this.col == o.col));
		}
		
		/**
		 * Return a HashCode, so visited Coordinates can be represented
		 * in a HashSet.
		 */
		public int hashCode() {
			return (row+col)/2;
		}
		
		public String toString() {
			return "(" + row + ", " + col + ")";
		}
	}
}