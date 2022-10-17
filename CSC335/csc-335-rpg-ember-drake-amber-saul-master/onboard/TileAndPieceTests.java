package onboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import model.Team;

/**
 * This test suite tests all files in the "onboard" package, which
 * mainly includes anything that extends Tile or Piece.
 * 
 * 
 * @author Drake Sitaraman
 *
 */
public class TileAndPieceTests{
	
	Tile[][] testGrid;
	
	/**
	 * Places tiles on the board and tests their boolean conditions.
	 * Each other test method gets the same tile array for their
	 * tests.
	 * 
	 * @return The 2D tile array for use by the other test methods.
	 * @throws InvalidMoveException 
	 * @throws InvalidRemovalException 
	 */
	private Tile[][] test_placePieces() throws InvalidMoveException, InvalidRemovalException {
		Tile[][] testGrid = new Tile[5][5];
		for(int i = 0; i<5; i++) {
			for(int j = 0; j<5; j++) {
				testGrid[i][j] = new OpenTile();
				//No piece there initially, can move and shoot through it
				assertNull(testGrid[i][j].getPiece());
				assertTrue(testGrid[i][j].canMoveInto(new Knight(Team.HUMAN)));
				assertTrue(testGrid[i][j].canShootThrough());
			}	
		}
		
		//Test Blocked Tile--no piece, can't move through, can't shoot through
		testGrid[2][2] = new BlockedTile();
		assertFalse(testGrid[2][2].canMoveInto(new Knight(Team.HUMAN)));
		assertFalse(testGrid[2][2].canShootThrough());
		assertNull(testGrid[2][2].getPiece());
		
		
		//Test Blocked See Through tile--no piece, can't move through, can shoot through
		testGrid[3][2] = new BlockedSeeThroughTile();
		assertFalse(testGrid[3][2].canMoveInto(new Knight(Team.HUMAN)));
		assertTrue(testGrid[3][2].canShootThrough());
		assertNull(testGrid[3][2].getPiece());
		
		Knight k1 = new Knight(Team.HUMAN);
		Knight k2 = new Knight(Team.HUMAN);
		Knight k3 = new Knight(Team.COMPUTER);
		
		//Test OpenTile, alternate constructor + booleans--can shoot through, can't move into
		testGrid[0][0] = new OpenTile(k1);
		assertEquals(testGrid[0][0].getPiece(), k1);
		assertTrue(testGrid[0][0].canShootThrough());
		assertFalse(testGrid[0][0].canMoveInto(new Knight(Team.HUMAN)));
		
		testGrid[0][1].setPiece(k2);
		
		assertEquals(testGrid[0][1].getPiece(), k2);
		
		testGrid[1][0].setPiece(k3);
		
		assertEquals(testGrid[1][0].getPiece(), k3);
		
		
		Archer a1 = new Archer(Team.HUMAN);
		Archer a2 = new Archer(Team.COMPUTER);
		
		//Piece already there--can't place a piece
		assertThrows(InvalidMoveException.class, ()->{ testGrid[0][0].setPiece(a1);});                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
		
		//BlockedTile--can't place a piece
		assertThrows(InvalidMoveException.class, ()->{ testGrid[2][2].setPiece(a1);}); 
		
		
		//BlockedSeeThroughTile--can't place a piece
		assertThrows(InvalidMoveException.class, ()->{ testGrid[3][2].setPiece(a1);}); 
		
		//Set archers
		testGrid[2][1].setPiece(a1);
		assertEquals(testGrid[2][1].getPiece(), a1);
		
		testGrid[1][2].setPiece(a2);
		assertEquals(testGrid[1][2].getPiece(), a2);
		
		//Set Knight and remove it.
		testGrid[4][4].setPiece(new Knight(Team.HUMAN));
		assertNotNull(testGrid[4][4].getPiece());
		testGrid[4][4].removePiece();
		assertNull(testGrid[4][4].getPiece());
		
		//Can't remove a piece from a BlockedTile
		assertThrows(InvalidRemovalException.class, ()->{testGrid[2][2].removePiece();});
		
		//Can't remove a piece from a BlockedSeeThroughTile
		assertThrows(InvalidRemovalException.class, ()->{testGrid[3][2].removePiece();});
		
		return testGrid;
	}
	
	@Test
	/**
	 * This method tests attacks, without defending, and make
	 * sure they fall within a certain range. It also shows
	 * that a player cannot attack a member of the same team.
	 * It also shows that a Piece can "die".
	 * 
	 * This test does NOT show attack ranges, or ability to shoot through
	 * characters and BlockedSeeThroughTiles. This is handled by the controller.
	 */
	public void testAttack() throws InvalidMoveException, InvalidRemovalException, FriendlyFireException {
		Tile[][] testGrid = test_placePieces(); 
		
		Piece humanKnight1 = testGrid[0][0].getPiece();
		Piece humanKnight2 = testGrid[0][1].getPiece();
		Piece computerKnight = testGrid[1][0].getPiece();
		int knightMaxHealth = computerKnight.getHealth();
		
		Piece computerArcher = testGrid[1][2].getPiece();
		
		humanKnight1.attack(testGrid[1][0]);
		
		//Between 70 and 80 as of now
		assertTrue(computerKnight.getHealth() >= (knightMaxHealth - humanKnight1.attackRange[1])
				&& computerKnight.getHealth() <=(knightMaxHealth - humanKnight1.attackRange[0]));
		
		assertThrows(FriendlyFireException.class, 
				()->{humanKnight2.attack(testGrid[0][0]);});
		
		
		computerArcher.attack(testGrid[0][0]);
		
		//Between 85 and 90 as of now
		assertTrue(humanKnight1.getHealth() >= (knightMaxHealth - computerArcher.attackRange[1]) 
				&& humanKnight1.getHealth() <=(knightMaxHealth - computerArcher.attackRange[0]));
		
		while(testGrid[1][0].getPiece() != null) {
			humanKnight1.attack(testGrid[1][0]);
		}
		
		assertNull(testGrid[1][0].getPiece());//computerKnight "dies"
		
	}
	
	@Test
	/**
	 * This method tests a knight attacking an archer while defending
	 * and an archer attacking a knight while defending. This shows
	 * that defense should be between the highest attack - lowest defense
	 * and the lowest attack - highest defense (or 0 if one of these is negative).
	 * 
	 * Again, this does NOT test whether pieces are in range of one another.
	 * This is done by the controller.
	 */
	public void testDefend() throws InvalidMoveException, InvalidRemovalException, FriendlyFireException {
		Tile[][] testGrid = test_placePieces(); 
		
		Piece humanKnight2 = testGrid[0][1].getPiece();
		
		int knightMaxHealth = humanKnight2.getHealth();
		
		Piece computerArcher = testGrid[1][2].getPiece();
		
		int archerMaxHealth = computerArcher.getHealth();
		
		humanKnight2.defend();
		computerArcher.attack(testGrid[0][1]);
		
		//Between 95 and 100 as of now
		assertTrue(humanKnight2.getHealth() >= knightMaxHealth 
				- Math.max(0, computerArcher.attackRange[1] - humanKnight2.defenseRange[0])
						
				&& humanKnight2.getHealth() <= knightMaxHealth 
				- Math.max(0, computerArcher.attackRange[0] - humanKnight2.defenseRange[1]) );
		
		
		computerArcher.defend();
		
		assertTrue(computerArcher.getHealth() >= archerMaxHealth
				- Math.max(0, humanKnight2.attackRange[1] - computerArcher.defenseRange[0])
				
				&& computerArcher.getHealth() >= archerMaxHealth
				- Math.max(0, humanKnight2.attackRange[0] - computerArcher.defenseRange[1]));
	}
	
	@Test
	/**
	 * This method tests Piece's ability to move and that, when out of moves,
	 * an OutOfMovesException is thrown. This does NOT actually move the pieces
	 * in the grid. This is handled by the controller.
	 */
	public void testMove() throws InvalidMoveException, InvalidRemovalException, OutOfMovesException {
		Tile[][] testGrid = test_placePieces(); 
		
		Piece humanKnight2 = testGrid[0][1].getPiece();
		humanKnight2.move(1);
		assertEquals(humanKnight2.getMoveDistanceRemaining(), 0);
		
		assertThrows(OutOfMovesException.class, ()->{humanKnight2.move(1);});
		
		
		Piece humanArcher = testGrid[2][1].getPiece();
		humanArcher.move(1);
		assertEquals(humanArcher.getMoveDistanceRemaining(), 1);
		
		assertThrows(OutOfMovesException.class, ()->{humanKnight2.move(3);});
		
		humanArcher.move(1);
		assertEquals(humanArcher.getMoveDistanceRemaining(), 0);
		
		humanKnight2.resetTurn();
		assertEquals(humanKnight2.getMoveDistanceRemaining(), 1);
		
		humanArcher.resetTurn();
		assertEquals(humanArcher.getMoveDistanceRemaining(), 2);
		
		
		
	}
	
	
	@Test
	/**
	 * Tests that Pieces have proper initial conditions.
	 */
	public void testInitialConditions() throws InvalidMoveException, InvalidRemovalException {
		Tile[][] testGrid = test_placePieces();
		Piece humanKnight1 = testGrid[0][0].getPiece();
		
		assertFalse(humanKnight1.isDefended());
		assertFalse(humanKnight1.hasAttackedOrDefended());
		assertEquals(((Knight)humanKnight1).attackDistance, humanKnight1.getAttackDistance());
		assertEquals(humanKnight1.getTeam(), Team.HUMAN);
		assertEquals(((Knight)humanKnight1).health, humanKnight1.getHealth());
		assertEquals(humanKnight1.getSpriteFileName(), "assets/knight_HUMAN.png");
		assertEquals(((Knight)humanKnight1).moveDistanceRemaining, humanKnight1.getMoveDistanceRemaining());
			
	}
	
	@Test
	/**
	 * Tests the rest action
	 */
	public void testRest() throws InvalidMoveException, InvalidRemovalException, OutOfMovesException, FriendlyFireException {
		Tile[][] testGrid = test_placePieces(); 
		
		Piece humanKnight2 = testGrid[0][1].getPiece();
		
		int knightMaxHealth = humanKnight2.getHealth();
		
		Piece computerArcher = testGrid[1][2].getPiece();
		
		
		humanKnight2.rest();
		computerArcher.attack(testGrid[0][1]);
		assertTrue(humanKnight2.isRested());
		
		
		assertTrue(humanKnight2.getHealth() >= knightMaxHealth 
				- Math.max(0, (computerArcher.attackRange[1])*1.5)
						
				&& humanKnight2.getHealth() <= knightMaxHealth 
				- Math.max(0, (computerArcher.attackRange[0])*1.5) );
		
	}
	
	@Test
	/**
	 * Test the setTeam() method for pieces
	 */
	public void testSetTeam() {
		Knight k = new Knight(Team.HUMAN);
		assertEquals(Team.HUMAN, k.getTeam());
		k.setTeam(Team.COMPUTER);
		assertEquals(Team.COMPUTER, k.getTeam());
		k.setTeam(Team.COMPUTER);
		assertEquals(Team.COMPUTER, k.getTeam());
		k.setTeam(Team.HUMAN);
		assertEquals(Team.HUMAN, k.getTeam());
	}
	
	/**
	 * Tests to make sure that the rest action heals the given piece
	 * @param p The piece to test the rest action on
	 * @throws InvalidMoveException 
	 * @throws InvalidRemovalException 
	 * @throws FriendlyFireException 
	 */
	private void testRestOnPiece(Piece p ) throws InvalidMoveException, FriendlyFireException, InvalidRemovalException {
		p.setTeam(Team.HUMAN);
		p.resetTurn();
		OpenTile t = new OpenTile();
		t.setPiece(p);
		Archer a = new Archer(Team.COMPUTER);
		a.attack(t);
		
		int oldHp = p.getHealth();
		p.rest();
		p.resetTurn();
		assertTrue(p.getHealth()>oldHp);
	}
	
	@Test
	/**
	 * Tests for rest healing various pieces
	 */
	public void testRestOnPieces() throws InvalidMoveException, FriendlyFireException, InvalidRemovalException {
		testRestOnPiece(new Knight(Team.HUMAN));
		testRestOnPiece(new Archer(Team.HUMAN));
		testRestOnPiece(new ArmoredKnight(Team.HUMAN));
		testRestOnPiece(new Pegasus(Team.HUMAN));
		testRestOnPiece(new Horseman(Team.HUMAN));
	}
	

}