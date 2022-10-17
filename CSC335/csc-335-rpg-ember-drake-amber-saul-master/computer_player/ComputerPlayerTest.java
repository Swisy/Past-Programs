package computer_player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import controller.StrategyGameController;
import model.BadSaveException;
import model.StrategyGameModel;
import onboard.Flyer;
import onboard.Piece;

/**
 * This test suite tests the proper behavior of the Computer
 * Player, when it is forced into making certain decisions.
 * These methods test the position where the computer piece 
 * is after a move, as well as its state (attacked or defended).
 * 
 * @author Drake Sitaraman
 *
 */
public class ComputerPlayerTest{
	
	@Test
	/**
	 * This test shows that the shortest path from CPU position (1,4)
	 * is Human position (4,1), distance 3. It is NOT human position (1,0), distance 4.
	 */
	public void test_standardPath() { //i.e. with nothing blocking it.
		StrategyGameModel m = null;
		try {
			m = new StrategyGameModel("./computer_player/test_levels/open.dat");
		} catch (BadSaveException e) {
			e.printStackTrace();
		}
		
		ComputerPlayer cpu = new ComputerPlayer(m);
		
		Piece p = m.getTile(1, 4).getPiece();
		cpu.moveTowardHumanPiece(p, 1, 4); 
		
		assertEquals(cpu.shortestRow, 4);
		assertEquals(cpu.shortestCol, 1);
		
		assertTrue(p.isDefended());
	}
	
	@Test
	/**
	 * This test shows that the program will not crash if
	 * the computer piece is alone in an empty level. This
	 * would never happen, but is an edge case that may
	 * improve coverage.
	 */
	public void test_Empty() {
		StrategyGameModel m = null;
		try {
			m = new StrategyGameModel("./computer_player/test_levels/empty.dat");
		} catch (BadSaveException e) {
			e.printStackTrace();
		}
		
		ComputerPlayer cpu = new ComputerPlayer(m);
		cpu.moveTowardHumanPiece(m.getTile(2, 2).getPiece(), 2, 2); 
		
		assertEquals(cpu.shortestRow, 0);
		assertEquals(cpu.shortestCol, 0);
		
		assertTrue(m.getTile(2, 2).getPiece().isDefended());
		
	}
	
	@Test
	/**
	 * This method tests that a Knight piece, which can only
	 * move 1 square, can move into a piece in range, then 
	 * attack (NOT defend).
	 */
	public void test_knightMoveAttack() {
		StrategyGameModel m = null;
		try {
			m = new StrategyGameModel("./computer_player/test_levels/knight_ma.dat");
		} catch (BadSaveException e) {
			e.printStackTrace();
		}
		
		ComputerPlayer cpu = new ComputerPlayer(m);
		Piece attacker = m.getTile(2, 3).getPiece();
		cpu.moveTowardHumanPiece(attacker, 2, 3); 
		
		assertEquals(cpu.shortestRow, 4);
		assertEquals(cpu.shortestCol, 1);
		
		assertFalse(attacker.isDefended());
		
		assertTrue(attacker.hasAttackedOrDefended());
		
		attacker.resetTurn();
		
		
		//Stays in place and attacks the same piece again.
		cpu.moveTowardHumanPiece(attacker, 3, 2); 
		assertEquals(cpu.shortestRow, 4);
		assertEquals(cpu.shortestCol, 1);
		
		assertFalse(attacker.isDefended());
		assertTrue(attacker.hasAttackedOrDefended());
		
		
	}
	
	
	@Test
	/**
	 * This method tests that a Knight piece, which can 
	 * move 3 squares, can move into a piece in range, then 
	 * attack (NOT defend).
	 */
	public void test_archerMoveAttack() {
		StrategyGameModel m = null;
		try {
			m = new StrategyGameModel("./computer_player/test_levels/archer_ma.dat");
		} catch (BadSaveException e) {
			e.printStackTrace();
		}
		
		ComputerPlayer cpu = new ComputerPlayer(m);
		Piece attacker = m.getTile(0, 4).getPiece();
		cpu.moveTowardHumanPiece(attacker, 0, 4); 
		
		
		assertFalse(attacker.isDefended());
		assertTrue(attacker.hasAttackedOrDefended());
		
	}
	
	
	@Test
	/**
	 * This method tests behavior for if a Computer Player
	 * is blocked by tiles which it cannot move through.
	 * The proper behavior is that it remains in the same
	 * square and defends.
	 */
	public void test_blockedPath() {
		StrategyGameModel m = null;
		try {
			m = new StrategyGameModel("./computer_player/test_levels/blocked.dat");
		} catch (BadSaveException e) {
			e.printStackTrace();
		}
		
		
		ComputerPlayer cpu = new ComputerPlayer(m);
		cpu.moveTowardHumanPiece(m.getTile(1, 4).getPiece(), 1, 4); 
		
		
		//Piece did not move anywhere
		assertTrue(m.getTile(1, 4).getPiece().isDefended());
	}
	
	
	
	@Test
	/**
	 * This method tests what happens when a Piece is blocked
	 * by allied computer Pieces (which it cannot move through).
	 * The proper decision is to stay in its spot and defend.
	 */
	public void test_blockedByAllies() {
		StrategyGameModel m = null;
		try {
			m = new StrategyGameModel("./computer_player/test_levels/blocked_allies.dat");
		} catch (BadSaveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ComputerPlayer cpu = new ComputerPlayer(m);
		
		Piece p = m.getTile(1, 4).getPiece();
		cpu.moveTowardHumanPiece(p, 1, 4); 
		
		
		assertTrue(p.isDefended());
	}
	
	@Test
	/**
	 * Basic test to test all pieces moving at once. This is better
	 * tested alongside the view.
	 */
	public void test_makeMove() {
		StrategyGameModel m = null;
		try {
			m = new StrategyGameModel("./computer_player/test_levels/blocked_allies.dat");
		} catch (BadSaveException e) {
			e.printStackTrace();
		}
		
		ComputerPlayer cpu = new ComputerPlayer(m);
		
		cpu.makeMove();
		
		assertEquals(cpu.piecesMoved, 6);
		

				
	}
	
	@Test
	/**
	 * This method tests a clear choice for the pegasus. A human Knight is 3
	 * squares away, on a track of no blocked tiles. A human pegasus is 2 
	 * sqyares away on a track with a blocked tile. The computer pegasus should
	 * move through the blocked tile.
	 */
	public void test_Pegasus() {
		StrategyGameModel m = null;
		try {
			m = new StrategyGameModel("./computer_player/test_levels/pegasus.dat");
		} catch (BadSaveException e) {
			e.printStackTrace();
		}
		
		ComputerPlayer cpu = new ComputerPlayer(m);
		
		Piece p = m.getTile(2, 4).getPiece();

		cpu.moveTowardHumanPiece(p, 2, 4); 
		
		assertEquals(cpu.shortestRow, 0);
		assertEquals(cpu.shortestCol, 4);
		
		assertFalse(p.isDefended());
		
		assertTrue(p.hasAttackedOrDefended());
		
	}
	

	

}