package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import computer_player.ComputerPlayer;
import model.StrategyGameModel;
import model.Team;
import onboard.FriendlyFireException;
import onboard.InvalidMoveException;
import onboard.InvalidRemovalException;
import onboard.OutOfMovesException;
import onboard.Pegasus;
import onboard.Piece;
import onboard.Tile;
/**
 * 
 * @author Saul Weintraub
 *
 */

public class StrategyGameController{
	StrategyGameModel model;
	ComputerPlayer computer_player;

	/**
	 * This is the constructor for the controller.
	 * 
	 * @param model The model that the controller will use to perform its methods
	 */
	public StrategyGameController(StrategyGameModel model) {
		this.model = model;
		computer_player = new ComputerPlayer(model);
	}
	
	/**
	 * This method will return if there is a piece at the specified tile.
	 * 
	 * @param row The y axis coordinate of the board
	 * @param col The x axis coordinate of the board
	 * @return true if there is a piece at the coordinates, else false
	 */
	public boolean hasPlayer(int row, int col) {
		Tile tile = model.getTile(row, col);
		return tile.hasPlayer();
	}
	
	/**
	 * This method returns the background image for the tile at row, col.
	 * 
	 * @param row The y axis coordinate of the board
	 * @param col The x axis coordinate of the board
	 * @return String : a background image path for the tile at row, col
	 */
	public String getTileBackground(int row, int col) {
		return model.getTile(row, col).getImgPath();
	}
	
	/**
	 * This method returns the file name of the sprite for the piece at the given coordinates.
	 * 
	 * @param row The y axis coordinate of the board
	 * @param col The x axis coordinate of the board
	 * @return The filename of the sprite for the piece
	 */
	public String getPieceSprite(int row, int col) {
		if(hasPlayer(row, col)) {
			Tile tile = model.getTile(row, col);
			Piece piece = tile.getPiece();
			return piece.getSpriteFileName();
		}
		return "";
	}
	
	/**
	 * This method returns the health of the piece at the coordinates.
	 * 
	 * @param row The y axis coordinate of the board
	 * @param col The x axis coordinate of the board
	 * @return The health of the piece located at the specified coordinates.
	 */
	public int getPieceHealth(int row, int col) {
		if(hasPlayer(row, col)) {
			Tile tile = model.getTile(row, col);
			Piece piece = tile.getPiece();
			return piece.getHealth();
		}
		return 0;
	}
	/**
	 * This method will return which team the piece at the specified coordinates belongs too.
	 * 
	 * @param row The y axis coordinate of the board
	 * @param col The x axis coordinate of the board
	 * @return The enum that represents which team the piece belongs to. COMPUTER or HUMAN
	 */
	public Team getPieceTeam(int row, int col) {
		if(hasPlayer(row, col)) {
			Tile tile = model.getTile(row, col);
			Piece piece = tile.getPiece();
			return piece.getTeam();
		}
		return null;
	}
	/**
	 * This method will return how much movement the piece at the specified coordinates has left.
	 * 
	 * @param row The y axis coordinate of the board
	 * @param col The x axis coordinate of the board
	 * @return The amount of movement the piece at the coordinates has left
	 */
	public int getMoveDistanceRemaining(int row, int col) {
		if(hasPlayer(row, col)) {
			Tile tile = model.getTile(row, col);
			Piece piece = tile.getPiece();
			return piece.getMoveDistanceRemaining();
		}
		return 0;
	}
	
	/**
	 * This method will return true if the piece at the coordinates has attacked or defended.
	 * 
	 * @param row The y axis coordinate of the board
	 * @param col The x axis coordinate of the board
	 * @return Whether or not the piece has attacked or defended
	 */
	public boolean hasAttackedOrDefended(int row, int col) {
		if(hasPlayer(row, col)) {
			Tile tile = model.getTile(row, col);
			Piece piece = tile.getPiece();
			return piece.hasAttackedOrDefended();
		}
		return false;
	}
	
	/**
	 * This method will return whether or not the piece at the coordinates is currently defending.
	 * 
	 * @param row The y axis coordinate of the board
	 * @param col The x axis coordinate of the board
	 * @return If the piece is currently defending.
	 */
	public boolean isDefended(int row, int col) {
		if(hasPlayer(row, col)) {
			Tile tile = model.getTile(row, col);
			Piece piece = tile.getPiece();
			return piece.isDefended();
		}
		return false;
	}
	
	/**
	 * This method will return whether or not the piece at the coordinates is currently resting.
	 * 
	 * @param row The y axis coordinate of the board
	 * @param col The x axis coordinate of the board
	 * @return If the piece is currently resting.
	 */
	public boolean isRested(int row, int col) {
		if(hasPlayer(row, col)) {
			Tile tile = model.getTile(row, col);
			Piece piece = tile.getPiece();
			return piece.isRested();
		}
		return false;
	}
	
	/**
	 * This method will return whose turn it currently is in the game.
	 * 
	 * @return whose turn it is. HUMAN or COMPUTER
	 */
	public Team getTurn() {
		return model.getTurn();
	}
	

	
	/**
	 * This method will simulate an attack for the game. The piece at the "by" coordinates will
	 * attack the piece at the "against" coordinates if it is a valid attack. The method will
	 * then notify the observer for the view.
	 * 
	 * @param byRow The y coordinate of the attacker
	 * @param byCol The x coordinate of the attacker
	 * @param againstRow The y coordinate of the attacked
	 * @param againstCol The x coordinate of the attacked
	 * @throws InvalidRemovalException 
	 * @throws FriendlyFireException 
	 */
	public void attack(int byRow, int byCol, int againstRow, int againstCol) throws FriendlyFireException, InvalidRemovalException {
		if (!model.getTile(byRow, byCol).getPiece().hasAttackedOrDefended()) {
			List<int[]> possible = getValidAttacks(byRow, byCol);
			int[] against = {againstRow, againstCol};
			if(isInList(possible, against)) {
				Piece by = model.getTile(byRow, byCol).getPiece();
				Tile againstTile = model.getTile(againstRow, againstCol);
				by.attack(againstTile);
				model.setUpNotifyObservers();
			}
		}
	}
	
	/**
	 * This method will move the piece at the "from" coordinates to the tile at the "to" coordinates
	 * if it is a valid move.
	 * 
	 * @param fromRow The y coordinate of the current location of the piece
	 * @param fromCol The x coordinate of the current location of the piece
	 * @param toRow The y coordinate of where the piece wants to move to
	 * @param toCol The x coordinate of where the piece wants to move to
	 * @throws InvalidRemovalException 
	 * @throws InvalidMoveException 
	 * @throws OutOfMovesException 
	 */
	public void move(int fromRow, int fromCol, int toRow, int toCol) throws InvalidRemovalException, InvalidMoveException, OutOfMovesException {
		List<int[]> possible = getValidMoves(fromRow, fromCol);
		int[] to= {toRow, toCol};
		if(isInList(possible, to)) {
			Tile toTile = model.getTile(toRow, toCol);
			Tile fromTile = model.getTile(fromRow, fromCol);
			Piece fromPiece = fromTile.getPiece();
			fromPiece.move(findShortestMove(fromRow, fromCol, toRow, toCol));
			toTile.setPiece(fromPiece);
			fromTile.removePiece();
			model.setUpNotifyObservers();
		}
	}
	
	/**
	 * This method will find the least amount of movement required for a piece to move to a
	 * specified tile.
	 * 
	 * @param fromRow The y coordinate of the current location of the piece
	 * @param fromCol The x coordinate of the current location of the piece
	 * @param toRow The y coordinate of where the piece wants to move to
	 * @param toCol The x coordinate of where the piece wants to move to
	 * @return The least amount of movement necessary to move to the designated tile
	 */
	private int findShortestMove(int fromRow, int fromCol, int toRow, int toCol) {
		Piece piece = model.getTile(fromRow, fromCol).getPiece();
		int distanceRemaining = piece.getMoveDistanceRemaining();
		List<Integer> distances = new ArrayList<Integer>();
		int[] destination = {toRow, toCol};
		
		findShortestMoveHelper(fromRow, fromCol, destination, 0, distanceRemaining, distances);
		distances.sort(null);
		
		return distances.get(0);
	}
	
	/**
	 * This method is a helper method that will help findShorestMove().
	 * 
	 * @param curRow The current y coordinate
	 * @param curCol The current x coordinate
	 * @param destination The array of the destination
	 * @param curDistance How far currently traveled
	 * @param distanceRemaining How much the piece is able to move
	 * @param distances A list of all possible distances that is being populated recursively
	 */
	private void findShortestMoveHelper(int curRow, int curCol, int[] destination, int curDistance, int distanceRemaining, List<Integer> distances) {
		int width = getBoardWidth();
		int length = getBoardLength();
		if((curRow >= 0) && (curRow < length) && (curCol >= 0) && (curCol < width)) {
			int[] curCoord = {curRow, curCol};
			if(Arrays.equals(curCoord, destination)) {
				distances.add(curDistance);
			}
			if (distanceRemaining > curDistance) {
				findShortestMoveHelper(curRow + 1, curCol, destination, curDistance + 1, distanceRemaining, distances);
				findShortestMoveHelper(curRow + 1, curCol + 1, destination, curDistance + 1, distanceRemaining, distances);
				findShortestMoveHelper(curRow + 1, curCol - 1, destination, curDistance + 1, distanceRemaining, distances);
				findShortestMoveHelper(curRow, curCol - 1, destination, curDistance + 1, distanceRemaining, distances);
				findShortestMoveHelper(curRow, curCol + 1, destination, curDistance + 1, distanceRemaining, distances);
				findShortestMoveHelper(curRow - 1, curCol, destination, curDistance + 1, distanceRemaining, distances);
				findShortestMoveHelper(curRow - 1, curCol + 1, destination, curDistance + 1, distanceRemaining, distances);
				findShortestMoveHelper(curRow - 1, curCol - 1, destination, curDistance + 1, distanceRemaining, distances);
			}
		}
	}
	
	/**
	 * This method will cause the piece located at the specified coordinates to defend.
	 * 
	 * @param row The y axis coordinate of the piece
	 * @param col The x axis coordinate of the piece
	 */
	public void defend(int row, int col) {
		if(hasPlayer(row, col) && !model.getTile(row, col).getPiece().hasAttackedOrDefended()) {
			Tile tile = model.getTile(row, col);
			Piece piece = tile.getPiece();
			piece.defend();
		}
		model.setUpNotifyObservers();
	}
	
	/**
	 * This method will cause the piece located at the specified coordinates to rest.
	 * 
	 * @param row The y axis coordinate of the piece
	 * @param col The x axis coordinate of the piece
	 */
	public void rest(int row, int col) {
		if (hasPlayer(row, col) && !model.getTile(row, col).getPiece().hasAttackedOrDefended()) {
			model.getTile(row, col).getPiece().rest();
		}
		model.setUpNotifyObservers();
	}

	public void computerTurn() {
		computer_player.makeMove();
		nextTurn();
	}
	
	
	/**
	 * This method will create a list of the coordinates a piece can attack and then return that
	 * list.
	 * 
	 * @param row The y axis coordinate of the board
	 * @param col The x axis coordinate of the board
	 * @return A list of all the coordinates the piece can attack
	 */
	public List<int[]> getValidAttacks(int row, int col){
		List<int[]> possible = new ArrayList<int[]>();
		if (!model.getTile(row, col).getPiece().hasAttackedOrDefended()) {
			Piece piece = model.getTile(row, col).getPiece();
			int range = piece.getAttackDistance();
			Team team = piece.getTeam();
			
			getValidAttacksHelper(row, col, range, team, possible);
		}
		return possible;
	}
	
	/**
	 * This method is a helper method for getValidAttacks(). This method will populate a list
	 * of coordinates that the piece is able to attack.
	 * 
	 * @param row The y axis coordinate of the board
	 * @param col The x axis coordinate of the board
	 * @param range The attack range of the piece
	 * @param team The team of the piece
	 * @param possible A list of the coordinates the piece can attack
	 */
	private void getValidAttacksHelper(int row, int col, int range, Team team, List<int[]> possible) {
		boolean pegasus = false;
		int width = getBoardWidth();
		int length = getBoardLength();
		// If the row and col are valid
		if((row >= 0) && (row < length) && (col >= 0) && (col < width)) {
			if(model.getTile(row, col).hasPlayer()) {
				if(model.getTile(row, col).getPiece() instanceof Pegasus) {
					pegasus = true;
				}
			}
			// If the tile is able to be shot through
			if(model.getTile(row, col).canShootThrough() | pegasus) {
				if(model.getTile(row, col).hasPlayer()) {
					if(!model.getTile(row, col).getPiece().getTeam().equals(team)) {
						int[] coord = {row, col};
						if(!isInList(possible, coord)) {
							possible.add(coord);
						}
					}
				}
				if(range > 0) {
					getValidAttacksHelper(row + 1, col, range - 1, team, possible);
					getValidAttacksHelper(row + 1, col, range - 1, team, possible);
					getValidAttacksHelper(row + 1, col - 1, range - 1, team, possible);
					getValidAttacksHelper(row - 1, col, range - 1, team, possible);
					getValidAttacksHelper(row - 1, col + 1, range - 1, team, possible);
					getValidAttacksHelper(row - 1, col - 1, range - 1, team, possible);
					getValidAttacksHelper(row, col + 1, range - 1, team, possible);
					getValidAttacksHelper(row, col - 1, range - 1, team, possible);
				}
			}
		}
	}
	
	/**
	 * This method will create a list of the coordinates a piece can move to and then return that
	 * list.
	 * 
	 * @param row The y axis coordinate of the board
	 * @param col The x axis coordinate of the board
	 * @return A list of all the coordinates the piece can move to
	 */
	public List<int[]> getValidMoves(int row, int col){
		Piece p = model.getTile(row, col).getPiece();
		
		List<int[]> possible = new ArrayList<int[]>();
		if(p == null) {
			return possible;
		}
		
		int remaining = p.getMoveDistanceRemaining();
		getValidMovesHelper(row, col, remaining, p, possible);
		
		return possible;
	}
	
	//TODO simplify this by a lot, make it similar to getValidAttacksHelper
	/**
	 * This method is a helper method for getValidMoves(). This method will populate a list
	 * of coordinates that the piece is able to move to.
	 * 
	 * @param row The y axis coordinate of the piece
	 * @param col The x axis coordinate of the piece
	 * @param remaining How many tiles the piece is able to move
	 * @param p The piece that is being moved
	 * @param possible A list of all the coordinates the piece can move to
	 */
	private void getValidMovesHelper(int row, int col, int remaining, Piece p, List<int[]> possible) {
		int width = getBoardWidth();
		int length = getBoardLength();
		if(remaining > 0) {
			remaining -= 1;
		/* move up
		 * if(can move up):
		 * possible.add([row - 1, col])
		 * getValidMovesHelper(row - 1, col, remaining - 1, possible)
		*/
			if(((row - 1) >= 0) && ((row - 1) < length) && (col >= 0) && (col < width)) {
				if(model.getTile(row - 1, col).canMoveInto(p)) {
					int[] coordinate = {row - 1, col};
					if(!isInList(possible, coordinate)) {
						possible.add(coordinate);
					}
					getValidMovesHelper(row - 1, col, remaining, p, possible);
				}
			}
		
		/* move up right
		 * if(can move up right):
		 * possible.add([row - 1, col + 1])
		 * getValidMovesHelper(row - 1, col + 1, remaining - 1, possible)
		*/
			if(((row - 1) >= 0) && ((row - 1) < length) && ((col + 1) >= 0) && ((col + 1) < width)) {
				if(model.getTile(row - 1, col + 1).canMoveInto(p)) {
					int[] coordinate = {row - 1, col + 1};
					if(!isInList(possible, coordinate)) {
						possible.add(coordinate);
					}
					getValidMovesHelper(row - 1, col + 1, remaining, p, possible);
				}
			}
			
		/* move right
		 * if(can move right):
		 * possible.add([row, col + 1])
		 * getValidMovesHelper(row, col + 1, remaining - 1, possible)
		*/
			if((row >= 0) && (row < length) && ((col + 1) >= 0) && ((col + 1) < width)) {
				if(model.getTile(row, col + 1).canMoveInto(p)) {
					int[] coordinate = {row, col + 1};
					if(!isInList(possible, coordinate)) {
						possible.add(coordinate);
					}
					getValidMovesHelper(row, col + 1, remaining, p, possible);
				}
			}
			
		/* move down right
		 * if(can move down right):
		 * possible.add([row + 1, col + 1])
		 * getValidMovesHelper(row + 1, col + 1, remaining - 1, possible)
		*/
			if(((row + 1) >= 0) && ((row + 1) < length) && ((col + 1) >= 0) && ((col + 1) < width)) {
				if(model.getTile(row + 1, col + 1).canMoveInto(p)) {
					int[] coordinate = {row + 1, col + 1};
					if(!isInList(possible, coordinate)) {
						possible.add(coordinate);
					}
					getValidMovesHelper(row + 1, col + 1, remaining, p, possible);
				}
			}	
		
		/* move down
		 * if(can move down):
		 * possible.add([row + 1, col])
		 * getValidMovesHelper(row + 1, col, remaining - 1, possible) 
		*/
			if(((row + 1) >= 0) && ((row + 1) < length) && (col >= 0) && (col < width)) {
				if(model.getTile(row + 1, col).canMoveInto(p)) {
					int[] coordinate = {row + 1, col};
					if(!isInList(possible, coordinate)) {
						possible.add(coordinate);
					}
					getValidMovesHelper(row + 1, col, remaining, p, possible);
				}
			}
			
		
		/* move down left
		 * if(can move down left):
		 * possible.add([row + 1, col - 1])
		 * getValidMovesHelper(row + 1, col - 1, remaining - 1, possible) 
		*/
			if(((row + 1) >= 0) && ((row + 1) < length) && ((col - 1) >= 0) && ((col - 1) < width)) {
				if(model.getTile(row + 1, col - 1).canMoveInto(p)) {
					int[] coordinate = {row + 1, col - 1};
					if(!isInList(possible, coordinate)) {
						possible.add(coordinate);
					}
					getValidMovesHelper(row + 1, col - 1, remaining, p, possible);
				}
			}
			
		
		/* move left
		 * if(can move left):
		 * possible.add([row, col - 1])
		 * getValidMovesHelper(row, col - 1, remaining - 1, possible)
		 */
			if((row >= 0) && (row < length) && ((col - 1) >= 0) && ((col - 1) < width)) {
				if(model.getTile(row, col - 1).canMoveInto(p)) {
					int[] coordinate = {row, col - 1};
					if(!isInList(possible, coordinate)) {
						possible.add(coordinate);
					}
					getValidMovesHelper(row, col - 1, remaining, p, possible);
				}
			}
			
		
		/* move up left
		 * if(can move up left):
		 * possible.add([row - 1, col - 1])
		 * getValidMovesHelper(row - 1, col - 1, remaining - 1, possible)
		 */
			if(((row - 1) >= 0) && ((row - 1) < length) && ((col - 1) >= 0) && ((col - 1) < width)) {
				if(model.getTile(row - 1, col - 1).canMoveInto(p)) {
					int[] coordinate = {row - 1, col - 1};
					if(!isInList(possible, coordinate)) {
						possible.add(coordinate);
					}
					getValidMovesHelper(row - 1, col - 1, remaining, p, possible);
				}
			}
			
		}
	}
	
	/**
	 * This method will return whether or not an int[] is located inside of a List<int[]>.
	 * 
	 * @param list A List<int[]>
	 * @param array The int[] that is being tested if it is in the list
	 * @return true if the array is in the list, else false.
	 */
	private boolean isInList(List<int[]> list, final int[] array){
		for(int[] item : list){
			if(Arrays.equals(item, array)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method will determine if the game is over. If one or both teams doesn't have any pieces
	 * remaining then the game is over.
	 * 
	 * @return if the game is over
	 */
	public boolean isOver() {
		boolean hasHuman = false;
		boolean hasComputer = false;
		int width = getBoardWidth();
		int length = getBoardLength();
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < length; y++) {
				Tile tile = model.getTile(x, y);
				if(tile.hasPlayer()) {
					Piece piece = tile.getPiece();
					if(piece.getTeam() == Team.HUMAN) {
						hasHuman = true;
					} else if(piece.getTeam() == Team.COMPUTER) {
						hasComputer = true;
					}
				}
				if((hasHuman) && (hasComputer)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * This method will return the winner of the game. This method will only be correct if isOver()
	 * is true.
	 * 
	 * @return The winner of the game
	 */
	public Team getWinner() {
		int width = getBoardWidth();
		int length = getBoardLength();
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < length; y++) {
				Tile tile = model.getTile(x, y);
				if(tile.hasPlayer()) {
					Piece piece = tile.getPiece();
					if(piece.getTeam() == Team.HUMAN) {
						return Team.HUMAN;
					} else if(piece.getTeam() == Team.COMPUTER) {
						return Team.COMPUTER;
					}
				}
			}
		}
		
		// IF NEITHER TEAMS HAVE ANY PIECES:
		return Team.COMPUTER;
	}
	
	/**
	 * This method will return the width of the board
	 * 
	 * @return the width of the board
	 */
	public int getBoardWidth() {
		return model.getBoardWidth();
	}
	
	/**
	 * This method will return the length of the board
	 * 
	 * @return the length of the board
	 */
	public int getBoardLength() {
		return model.getBoardHeight();
	}
	
	/**
	 * This method will return the file name of the background image of the board.
	 * 
	 * @return the file name of the background image of the board
	 */
	public String getBackgroundImageFileName() {
		return model.getBackgroundImageFileName();
	}
	
	/**
	 * This method will advance the game to the next turn.
	 * 
	 */
	public void nextTurn() {
		for (int row = 0; row < model.getBoardHeight(); row++) {
			for (int col = 0; col < model.getBoardWidth(); col++) {
				if (model.getTile(row,col).hasPlayer()) {
					model.getTile(row, col).getPiece().resetTurn();
				}
				
			}
		}
		model.nextTurn();
		model.setUpNotifyObservers();
		if (model.getTurn() == Team.COMPUTER) {
			computerTurn();
		}
	}
	
	/**
	 * This method will create a save file containing the current game data.
	 * 
	 * @param saveFileName The name of the save file that will be created.
	 */
	public void saveGame(String saveFileName) {
		try {
	    	File newFile = new File(saveFileName);
			newFile.createNewFile();
	    	FileOutputStream saveToFile = new FileOutputStream(saveFileName);
		    ObjectOutputStream outputStream = new ObjectOutputStream(saveToFile);
			outputStream.writeObject(model.getState());
			outputStream.close();
		} catch (IOException e) {}
	}

}