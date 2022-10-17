package tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import model.Team;
import onboard.BlockedSeeThroughTile;
import onboard.BlockedTile;
import onboard.InvalidMoveException;
import onboard.InvalidRemovalException;
import onboard.Knight;
import onboard.OpenTile;
import onboard.Pegasus;
import onboard.Tile;

public class TilesTests {
	/**
	 * Tests the setPiece method of Tile
	 * @throws InvalidMoveException 
	 */
	@Test
	public void testSetPiece() throws InvalidMoveException {
		Tile t = new OpenTile();
		t.setPiece(new Knight(Team.HUMAN));
		assertTrue(t.getPiece() instanceof Knight);
		assertThrows(InvalidMoveException.class, ()->{t.setPiece(null);});
	}
	
	/**
	 * Tests Tile's RemovePiece Method
	 * @throws InvalidMoveException 
	 * @throws InvalidRemovalException 
	 */
	@Test
	public void testRemovePiece() throws InvalidMoveException, InvalidRemovalException {
		Tile t = new OpenTile();
		t.setPiece(new Knight(Team.HUMAN));
		t.removePiece();
		assertEquals(null, t.getPiece());
		Tile b = new BlockedTile();
		assertThrows(InvalidRemovalException.class, ()->{b.removePiece();});
		b.setPiece(new Pegasus(Team.HUMAN));
		b.removePiece();
		assertEquals(null, b.getPiece());
	}
	
	/**
	 * Tests misc methods of Tile
	 * @throws InvalidMoveException 
	 */
	@Test
	public void testMiscMethods() throws InvalidMoveException {
		Tile o = new OpenTile();
		Tile b = new BlockedTile();
		assertFalse(o.hasPlayer());
		o.setPiece(new Knight(Team.COMPUTER));
		assertTrue(o.hasPlayer());
		assertTrue(o.isOpenTile());
		assertFalse(b.isOpenTile());
		assertEquals("assets/OpenTile.png", o.getImgPath());
		assertEquals("assets/BlockedTile.png", b.getImgPath());
	}
	
	/**
	 * Tests canShootThrough on various tiles
	 */
	@Test
	public void testCanShootThrough() {
		Tile o = new OpenTile();
		Tile b = new BlockedTile();
		Tile s = new BlockedSeeThroughTile();
		
		assertTrue(o.canShootThrough());
		assertFalse(b.canShootThrough());
		assertTrue(s.canShootThrough());
	}
	
	/**
	 * Tests canMoveInto method for both types of blocked tiles
	 */
	@Test
	public void testBlockedTiles() {
		Tile b = new BlockedTile();
		Tile s = new BlockedSeeThroughTile();
		Knight k = new Knight(Team.HUMAN);
		assertFalse(b.canMoveInto(k));
		assertFalse(s.canMoveInto(k));
		Pegasus p = new Pegasus(Team.HUMAN);
		assertTrue(b.canMoveInto(p));
		assertTrue(s.canMoveInto(p));
	}
}
