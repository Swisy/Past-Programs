package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

import org.junit.jupiter.api.Test;


import onboard.BlockedTile;
import onboard.OpenTile;
import onboard.Tile;

/**
 * This class contains test cases for the model
 * @author Ember Chan
 *
 */
public class TestModel {

	private class ObsTest implements Observer {
		
		public boolean updated = false;

		@Override
		public void update(Observable o, Object arg) {
			updated = true;
		}
		
	}
	
	/**
	 * Tests loading bad saves with the model
	 */
	@Test
	public void testBadSave() {
		assertThrows(BadSaveException.class, ()->{
			new StrategyGameModel("There is no save here!");
		});
		
		saveState(null, "testSaveNull.dat");
		assertThrows(BadSaveException.class, ()->{
			new StrategyGameModel("testSaveNull.dat");
		});
		
		makeTestSave2();
		assertThrows(BadSaveException.class, ()->{
			new StrategyGameModel("testSave2.dat");
		});
		
		makeTestSave3();
		assertThrows(BadSaveException.class, ()->{
			new StrategyGameModel("testSave3.dat");
		});
		
		makeTestSave4();
		assertThrows(BadSaveException.class, ()->{
			new StrategyGameModel("testSave4.dat");
		});
	}
	
	/**
	 * Tests the model's getTurn() & nextTurn() method
	 * @throws BadSaveException 
	 */
	@Test
	public void testTurn() throws BadSaveException {
		makeTestSave1();
		StrategyGameModel m = new StrategyGameModel("testSave1.dat");
		assertEquals(Team.COMPUTER, m.getTurn());
		m.nextTurn();
		assertEquals(Team.HUMAN, m.getTurn());
		m.nextTurn();
		assertEquals(Team.COMPUTER, m.getTurn());
	}
	
	/**
	 * Tests model's getBackgroundFileImage
	 * @throws BadSaveException 
	 */
	@Test
	public void testBGFile() throws BadSaveException {
		makeTestSave1();
		StrategyGameModel m = new StrategyGameModel("testSave1.dat");
		assertEquals("testPath", m.getBackgroundImageFileName());
	}
	
	/**
	 * Tests the model's getState method()
	 * @throws BadSaveException 
	 */
	@Test
	public void testGetState() throws BadSaveException {
		makeTestSave1();
		StrategyGameModel m = new StrategyGameModel("testSave1.dat");
		StrategyGameState s = m.getState();
		assertTrue(s.board[0][0] instanceof BlockedTile);
		assertTrue(s.board[0][1] == null);
		assertTrue(s.board[0][2]== null);
		assertTrue(s.board[1][0] == null);
		assertTrue(s.board[1][1] instanceof OpenTile);
		assertTrue(s.board[1][2] == null);
		assertTrue(s.board[2][0] == null);
		assertTrue(s.board[2][1] == null);
		assertTrue(s.board[2][2] == null);
		assertEquals("testPath", s.backgroundImageFileName);
		assertEquals(Team.COMPUTER, s.currentTurn);
	}
	
	/**
	 * Tests the model's getTile method
	 * @throws BadSaveException
	 */
	@Test
	public void testGetTile() throws BadSaveException {
		makeTestSave1();
		StrategyGameModel m = new StrategyGameModel("testSave1.dat");
		assertTrue(m.getTile(0, 0) instanceof BlockedTile);
		assertTrue(m.getTile(0, 1) == null);
		assertTrue(m.getTile(0, 2) == null);
		assertTrue(m.getTile(1, 0) == null);
		assertTrue(m.getTile(1, 1) instanceof OpenTile);
		assertTrue(m.getTile(1, 2) == null);
		assertTrue(m.getTile(2, 0) == null);
		assertTrue(m.getTile(2, 1) == null);
		assertTrue(m.getTile(2, 2) == null);
	}
	
	/**
	 * Tests the setUpNotifyObservers() method of the model
	 * @throws BadSaveException
	 */
	@Test
	public void testNotify() throws BadSaveException{
		ObsTest o = new ObsTest();
		assertFalse(o.updated);
		makeTestSave1();
		StrategyGameModel m = new StrategyGameModel("testSave1.dat");
		m.addObserver(o);
		assertFalse(o.updated);
		m.setUpNotifyObservers();
		assertTrue(o.updated);
	}
	
	/**
	 * Saves the given StrategyGameState to the given filepath
	 * @param state the StrategyGameState to save
	 * @param filepath the filepath to save the state to, as a String
	 */
	private void saveState(StrategyGameState state, String filepath) {
		try (ObjectOutputStream objOut =
				new ObjectOutputStream(new FileOutputStream(filepath))){
			objOut.writeObject(state);
		} catch (IOException e) {
			System.out.println(e);
			throw new RuntimeException();
		}
	}
	
	/**
	 * Makes a save that has some null tiles
	 */
	private void makeTestSave1() {
		StrategyGameState save = new StrategyGameState();
		save.backgroundImageFileName = "testPath";
		save.currentTurn = Team.COMPUTER;
		save.board = new Tile[3][3];
		save.board[1][1] = new OpenTile();
		save.board[0][0] = new BlockedTile();
		saveState(save, "testSave1.dat");
	}
	
	/**
	 * Makes a save with no board
	 */
	private void makeTestSave2() {
		StrategyGameState save = new StrategyGameState();
		save.backgroundImageFileName = "testPath";
		save.currentTurn = Team.COMPUTER;
		saveState(save, "testSave2.dat");
	}
	
	/**
	 * Makes a save with 0 height
	 */
	private void makeTestSave3() {
		StrategyGameState save = new StrategyGameState();
		save.backgroundImageFileName = "testPath";
		save.currentTurn = Team.COMPUTER;
		save.board = new Tile[10][0];
		saveState(save, "testSave3.dat");
	}
	
	/**
	 * Makes a save with 0 width and height
	 */
	private void makeTestSave4() {
		StrategyGameState save = new StrategyGameState();
		save.backgroundImageFileName = "testPath";
		save.currentTurn = Team.COMPUTER;
		save.board = new Tile[0][0];
		saveState(save, "testSave4.dat");
	}
	
}
