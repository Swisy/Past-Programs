package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import controller.StrategyGameController;
import model.BadSaveException;
import model.StrategyGameModel;
import model.Team;
import onboard.FriendlyFireException;
import onboard.InvalidMoveException;
import onboard.InvalidRemovalException;
import onboard.OutOfMovesException;
import onboard.Piece;

public class StrategyGameControllerTest {

	@Test
	public void BasicControllerTest() throws BadSaveException, InvalidRemovalException, InvalidMoveException {
		StrategyGameModel model = new StrategyGameModel("levels/level_1.dat");
		StrategyGameController controller = new StrategyGameController(model);
		List<int[]> validMoves = controller.getValidMoves(7, 0);
		assertEquals(validMoves.size(), 1);
		List<int[]> validAttacks = controller.getValidAttacks(7, 0);
		assertEquals(validAttacks.size(), 0);
		assertTrue(controller.hasPlayer(7, 1));
		assertEquals(controller.getTileBackground(0, 0), "assets/OpenTile.png");
		assertEquals(controller.getPieceSprite(0, 4), "assets/horseman_COMPUTER.png");
		assertEquals(controller.getPieceSprite(0, 0), "");
		assertEquals(controller.getPieceHealth(0, 0), 0);
		assertEquals(controller.getPieceHealth(0, 2), 100);
		assertEquals(controller.getPieceTeam(0, 0), null);
		assertEquals(controller.getPieceTeam(0, 2), Team.COMPUTER);
		assertEquals(controller.getPieceTeam(7, 0), Team.HUMAN);
		assertEquals(controller.getMoveDistanceRemaining(0, 2), 2);
		assertEquals(controller.getMoveDistanceRemaining(0, 0), 0);
		assertFalse(controller.hasAttackedOrDefended(7, 0));
		assertFalse(controller.hasAttackedOrDefended(0, 0));
		assertFalse(controller.isDefended(7, 0));
		controller.defend(7, 0);
		assertTrue(controller.isDefended(7, 0));
		assertTrue(controller.hasAttackedOrDefended(7,0));
		assertFalse(controller.isDefended(0, 0));
		assertFalse(controller.isRested(7, 1));
		controller.rest(7, 1);
		assertTrue(controller.isRested(7, 1));
		assertTrue(controller.hasAttackedOrDefended(7,1));
		assertFalse(controller.isRested(0, 0));
		assertEquals(controller.getTurn(), Team.HUMAN);
		assertFalse(controller.isOver());
		assertEquals(controller.getBoardLength(), 8);
		assertEquals(controller.getBoardWidth(), 8);
		assertEquals(controller.getBackgroundImageFileName(), "assets/level_1_background.png");
		
		Piece computer = model.getTile(0, 2).getPiece();
		
		// Human Wins
		model.getTile(0, 2).removePiece();
		model.getTile(0, 3).removePiece();
		model.getTile(0, 4).removePiece();
		assertTrue(controller.isOver());
		assertEquals(controller.getWinner(), Team.HUMAN);
		
		// Computer Wins
		model.getTile(0, 2).setPiece(computer);
		model.getTile(7, 0).removePiece();
		model.getTile(7, 1).removePiece();
		model.getTile(7, 2).removePiece();
		model.getTile(7, 3).removePiece();
		assertTrue(controller.isOver());
		assertEquals(controller.getWinner(), Team.COMPUTER);
		
		model.getTile(0, 2).removePiece();
		assertTrue(controller.isOver());
		assertEquals(controller.getWinner(), Team.COMPUTER);
	}
	/*
	 * Piece locations (row, col):
	 *   Human pieces: Archer (6,2), Pegasus(6,3), Knight(6,4), Knight(7,4)
	 *   Computer pieces: Archer(4,0), Knight(4,1), Pegasus(4,3), Knight(3,4), Knight(4,4)
	 * 
	 * Tile locations (row, col):
	 *   BlockedTile: (0,2), (2,2), (5,2), (5,3), (7,2)
	 *   BlockedSeeThroughTile: (0,0), (0,1), (1,0), (1,1), (1,2), (2,0), (2,1), (3,0), 
	 *                          (3,1), (3,2), (5,0), (5,1)
	 */
	@Test
	public void MoveControllerTest() throws BadSaveException, OutOfMovesException, InvalidRemovalException, InvalidMoveException {
		StrategyGameModel model = new StrategyGameModel("levels/ControllerTest1.dat");
		StrategyGameController controller = new StrategyGameController(model);
		// Getting a move list for a tile with no piece should return an empty list
		List<int[]> noMoves = controller.getValidMoves(0, 0);
		assertTrue(noMoves.isEmpty());
		
		// Archer should be able to move to (7,3), (6,1), (6,0), (7,1), (7,0)
		List<int[]> humanArcherMoves = controller.getValidMoves(6, 2);
		assertEquals(humanArcherMoves.size(), 5);
		assertTrue(coordIsInList(humanArcherMoves, 7, 3));
		assertTrue(coordIsInList(humanArcherMoves, 6, 1));
		assertTrue(coordIsInList(humanArcherMoves, 6, 0));
		assertTrue(coordIsInList(humanArcherMoves, 7, 1));
		assertTrue(coordIsInList(humanArcherMoves, 7, 0));
		
		// Pegasus should be able to move to (3,1), (3,2), (3,3), (3,5), (3,6), (4,2), (4,5),
		//                                   (4,6), (5,0), (5,1), (5,2), (5,3), (5,4), (5,5), 
		//                                   (5,6), (6,0), (6,1), (6,5), (6,6), (7,0), (7,1), 
		//                                   (7,2), (7,3), (7,5), (7,6)
		List<int[]> humanPegasusMoves = controller.getValidMoves(6, 3);
		assertEquals(humanPegasusMoves.size(), 25);
		assertTrue(coordIsInList(humanPegasusMoves, 3, 1));
		assertTrue(coordIsInList(humanPegasusMoves, 3, 2));
		assertTrue(coordIsInList(humanPegasusMoves, 3, 3));
		assertTrue(coordIsInList(humanPegasusMoves, 3, 5));
		assertTrue(coordIsInList(humanPegasusMoves, 3, 6));
		assertTrue(coordIsInList(humanPegasusMoves, 4, 2));
		assertTrue(coordIsInList(humanPegasusMoves, 4, 5));
		assertTrue(coordIsInList(humanPegasusMoves, 4, 6));
		assertTrue(coordIsInList(humanPegasusMoves, 5, 0));
		assertTrue(coordIsInList(humanPegasusMoves, 5, 1));
		assertTrue(coordIsInList(humanPegasusMoves, 5, 2));
		assertTrue(coordIsInList(humanPegasusMoves, 5, 3));
		assertTrue(coordIsInList(humanPegasusMoves, 5, 4));
		assertTrue(coordIsInList(humanPegasusMoves, 5, 5));
		assertTrue(coordIsInList(humanPegasusMoves, 5, 6));
		assertTrue(coordIsInList(humanPegasusMoves, 6, 0));
		assertTrue(coordIsInList(humanPegasusMoves, 6, 1));
		assertTrue(coordIsInList(humanPegasusMoves, 6, 5));
		assertTrue(coordIsInList(humanPegasusMoves, 6, 6));
		assertTrue(coordIsInList(humanPegasusMoves, 7, 0));
		assertTrue(coordIsInList(humanPegasusMoves, 7, 1));
		assertTrue(coordIsInList(humanPegasusMoves, 7, 2));
		assertTrue(coordIsInList(humanPegasusMoves, 7, 3));
		assertTrue(coordIsInList(humanPegasusMoves, 7, 5));
		assertTrue(coordIsInList(humanPegasusMoves, 7, 6));
		
		// Trying to perform an invalid move should do nothing
		controller.move(6, 2, 0, 0);
		assertTrue(controller.hasPlayer(6, 2));
		assertFalse(controller.hasPlayer(0, 0));
		
		// Moving the archer at (6,2) to (6,0)
		controller.move(6, 2, 6, 0);
		assertTrue(controller.hasPlayer(6, 0));
		assertFalse(controller.hasPlayer(6, 2));
		
		controller.nextTurn();
	}
	
	/*
	 * Piece locations (row, col):
	 *   Human pieces: Archer (6,2), Pegasus(6,3), Knight(6,4), Knight(7,4)
	 *   Computer pieces: Archer(4,0), Knight(4,1), Pegasus(4,3), Knight(3,4), Knight(4,4)
	 * 
	 * Tile locations (row, col):
	 *   BlockedTile: (0,2), (2,2), (5,2), (5,3), (7,2)
	 *   BlockedSeeThroughTile: (0,0), (0,1), (1,0), (1,1), (1,2), (2,0), (2,1), (3,0), 
	 *                          (3,1), (3,2), (5,0), (5,1)
	 */
	@Test
	public void AttackControllerTest() throws BadSaveException, OutOfMovesException, InvalidRemovalException, InvalidMoveException, FriendlyFireException {
		StrategyGameModel model = new StrategyGameModel("levels/ControllerTest1.dat");
		StrategyGameController controller = new StrategyGameController(model);
		
		// Archer at (6,2) should be able to attack (4,1), (4,0)
		List<int[]> archerAttackList = controller.getValidAttacks(6, 2);
		assertEquals(archerAttackList.size(), 2);
		assertTrue(coordIsInList(archerAttackList, 4, 1));
		assertTrue(coordIsInList(archerAttackList, 4, 0));
		
		// A defended piece should have no valid attacks
		controller.defend(6, 2);
		List<int[]> defendedArcherAttackList = controller.getValidAttacks(6, 2);
		assertTrue(defendedArcherAttackList.isEmpty());
		
		// Edgecase to make sure pegasus on blocked tiles can attack and be attacked
		controller.move(6, 3, 5, 3);
		assertFalse(controller.getValidAttacks(5, 3).isEmpty());
		assertTrue(coordIsInList(controller.getValidAttacks(4, 3), 5, 3));
		
		// Attacks should deal damage
		int prevPegasusHealth = controller.getPieceHealth(4, 3);
		controller.attack(5, 3, 4, 3);
		int curPegasusHealth = controller.getPieceHealth(4, 3);
		assertTrue(curPegasusHealth < prevPegasusHealth);
	}
	
	
	
	
	// HELPER METHODS FOR TESTS
	/**
	 * This method will return whether or not a coordinate array is located inside of a List<int[]>.
	 * 
	 * @param list A List<int[]>
	 * @param row The first element of the coord array
	 * @param col The second element of the coord array
	 * @return true if the coordinate is in the list, else false.
	 */
	private boolean coordIsInList(List<int[]> list, int row, int col){
		final int[] array = {row, col};
		for(int[] item : list){
			if(Arrays.equals(item, array)){
				return true;
			}
		}
		return false;
	}
}
