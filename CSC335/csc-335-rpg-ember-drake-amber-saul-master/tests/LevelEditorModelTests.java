package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import model.InvalidLevelException;
import model.LevelEditorModel;
import model.SaveFailureException;
import model.Team;
import onboard.BlockedTile;
import onboard.InvalidMoveException;
import onboard.Knight;
import onboard.OpenTile;

public class LevelEditorModelTests {
	
	/**
	 * Tests the LevelEditorModel's getTile() method
	 */
	@Test
	public void testGetTile() {
		LevelEditorModel m = new LevelEditorModel();
		m.setTile(1, 3, new OpenTile());
		m.setTile(0, 0, new BlockedTile());
		m.setTile(4, 4, new OpenTile());
		
		assertTrue(m.getTile(0, 0) instanceof BlockedTile);
		assertTrue(m.getTile(4, 4) instanceof OpenTile);
		assertTrue(m.getTile(1, 3) instanceof OpenTile);
		assertEquals(null, m.getTile(0, 1));
		assertEquals(null, m.getTile(2, 2));
		assertEquals(null, m.getTile(5, 3));
	}
	
	/**
	 * Tests the save funciton of the LevelEditorModel
	 * @throws InvalidMoveException 
	 * @throws InvalidLevelException 
	 * @throws SaveFailureException 
	 */
	@Test
	public void testSaveLevel() throws InvalidMoveException, SaveFailureException, InvalidLevelException {
		LevelEditorModel m = new LevelEditorModel();
		for(int i = 0; i<LevelEditorModel.SIZE; i++) {
			for(int j = 0; j<LevelEditorModel.SIZE; j++) {
				m.setTile(i, j, new OpenTile());
			}
		}
		m.getTile(0,0).setPiece(new Knight(Team.HUMAN));
		m.getTile(5, 5).setPiece(new Knight(Team.COMPUTER));
		m.saveLevel("test.dat");
		assertThrows(InvalidLevelException.class, ()->{m.saveLevel("");});
		m.setTile(0, 1, null);
		assertThrows(InvalidLevelException.class, ()->{m.saveLevel("test.dat");});
		m.setTile(0, 1, new OpenTile());
		m.setTile(0, 0, new OpenTile());
		assertThrows(InvalidLevelException.class, ()->{m.saveLevel("test.dat");});
	}
}
