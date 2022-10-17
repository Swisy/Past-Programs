import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;

/**
 * 
 * @author Amber Converse and Saul Weintraub
 * 
 * This class contains the testing suite for the ReversiController and ReversiModel classes
 * 
 * NOTE FOR GRADER: It is impossible to guarantee 100% coverage for the computerTurn() function
 * since it is random whether or not a certain branch is covered, so you might not see a 100%
 * coverage for ReversiController every time you run the test.
 *
 */
public class ReversiTest {
	
	/**
	 * This functions tests the model.
	 */
	@Test
	void testModel() {
		
		// Test empty constructor
		ReversiModel model = new ReversiModel();
		ReversiModel secondModel = new ReversiModel();
		assertEquals(model.getWhiteScore(), secondModel.getWhiteScore());
		
		
		// Test getters and setters
		assertEquals(model.getWhiteScore(), 2);
		assertEquals(model.getBlackScore(), 2);
		assertEquals(model.getPiece(3, 3), 'w');
		assertEquals(model.getPiece(3, 4), 'b');
		model.setPiece(3, 3, 'b');
		assertEquals(model.getPiece(3, 3), 'b');
		assertEquals(model.getBlackScore(), 3);
		assertEquals(model.getWhiteScore(), 1);
		assertEquals(model.getTurn(), 'w');
		model.switchTurn();
		assertEquals(model.getTurn(), 'b');
		
		// Test save game constructor
		@SuppressWarnings("unused")
		ReversiModel model1 = new ReversiModel("thisisnotafile.txt");
	}
	
	/**
	 * This function tests the controller
	 */
	@Test
	void testController() {
		
		/* Test Board:
		 *   0 1 2 3 4 5 6 7
		 * 0 x x x x x x x x
		 * 1 x x x x x x x x
		 * 2 x x x x x x x x
		 * 3 x x x w b x x x
		 * 4 x x x b w x x x
		 * 5 x x x x x x x x
		 * 6 x x x x x x x x
		 * 7 x x x x x x x x
		 */
		
		// Test constructor
		ReversiModel model = new ReversiModel();
		ReversiController controller = new ReversiController(model);
		
		// Test humanTurn (check using the model)
		
		/* Test Board after turn:
		 *   0 1 2 3 4 5 6 7
		 * 0 x x x x x x x x
		 * 1 x x x x x x x x
		 * 2 x x x x w x x x
		 * 3 x x x w w x x x
		 * 4 x x x b w x x x
		 * 5 x x x x x x x x
		 * 6 x x x x x x x x
		 * 7 x x x x x x x x
		 */
		assertEquals(controller.getTurn(), 'w');
		assertEquals(controller.getPieceColor(3, 3), Color.WHITE);
		assertEquals(controller.getPieceColor(4, 3), Color.BLACK);
		assertEquals(controller.getPieceColor(0, 0), Color.TRANSPARENT);
		controller.humanTurn(2, 4);
		assertEquals(controller.getWhiteScore(), 4);
		assertEquals(controller.getBlackScore(), 1);
		assertEquals(model.getPiece(2, 4), 'w');
		assertEquals(model.getPiece(3, 4), 'w');
		
		/* Test Board after turn:
		 *   0 1 2 3 4 5 6 7
		 * 0 x x x x x x x x
		 * 1 x x x x x x x x
		 * 2 x x x x w b x x
		 * 3 x x x w b x x x
		 * 4 x x x b w x x x
		 * 5 x x x x x x x x
		 * 6 x x x x x x x x
		 * 7 x x x x x x x x
		 */
		controller.humanTurn(2, 5);
		assertEquals(controller.getWhiteScore(), 3);
		assertEquals(controller.getBlackScore(), 3);
		assertEquals(model.getPiece(2, 5), 'b');
		assertEquals(model.getPiece(3, 4), 'b');
		
		/* Test Board after turn:
		 *   0 1 2 3 4 5 6 7
		 * 0 x x x x x x x x
		 * 1 x x x x x x x x
		 * 2 x x x x w b x x
		 * 3 x x x w w w x x
		 * 4 x x x b w x x x
		 * 5 x x x x x x x x
		 * 6 x x x x x x x x
		 * 7 x x x x x x x x
		 */
		controller.humanTurn(3, 5);
		assertEquals(controller.getWhiteScore(), 5);
		assertEquals(controller.getBlackScore(), 2);
		assertEquals(model.getPiece(3, 5), 'w');
		assertEquals(model.getPiece(3, 4), 'w');
		
		ReversiModel model1 = new ReversiModel("testing/save_file_1.dat");
		ReversiController controller1 = new ReversiController(model1);
		
		// current board should be: { {'x','x','x','x','x','x','x','x'},
		// 							 {'x','x','x','x','x','x','x','x'},
		// 							 {'x','x','x','x','x','x','x','x'},
		// 							 {'x','x','x','b','b','x','x','x'},
		// 							 {'x','x','b','b','b','w','x','x'},
		// 							 {'x','x','x','x','x','x','x','x'},
		// 							 {'x','x','x','x','x','x','x','x'},
		// 							 {'x','x','x','x','x','x','x','x'} }
		
		controller1.humanTurn(4, 1);
		
		char[][] expectedBoard1 = {  {'x','x','x','x','x','x','x','x'},
									 {'x','x','x','x','x','x','x','x'},
									 {'x','x','x','x','x','x','x','x'},
									 {'x','x','x','b','b','x','x','x'},
									 {'x','w','w','w','w','w','x','x'},
									 {'x','x','x','x','x','x','x','x'},
									 {'x','x','x','x','x','x','x','x'},
									 {'x','x','x','x','x','x','x','x'}  };
		
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				assertEquals(model1.getPiece(row, col), expectedBoard1[row][col]);
			}
		}
		
		controller1.humanTurn(4, 1);
		controller1.humanTurn(-1, 0);
		controller1.humanTurn(8, 0);
		controller1.humanTurn(0, -1);
		controller1.humanTurn(0, 8);
		
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				assertEquals(model1.getPiece(row, col), expectedBoard1[row][col]);
			}
		}
		
		controller1.humanTurn(5, 3);
		
		char[][] expectedBoard2 = {  {'x','x','x','x','x','x','x','x'},
									 {'x','x','x','x','x','x','x','x'},
									 {'x','x','x','x','x','x','x','x'},
									 {'x','x','x','b','b','x','x','x'},
								 	 {'x','w','w','b','w','w','x','x'},
								 	 {'x','x','x','b','x','x','x','x'},
									 {'x','x','x','x','x','x','x','x'},
									 {'x','x','x','x','x','x','x','x'}  };
		
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				assertEquals(model1.getPiece(row, col), expectedBoard2[row][col]);
			}
		}
		
		controller1.humanTurn(3, 5);

		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				assertEquals(model1.getPiece(row, col), expectedBoard2[row][col]);
			}
		}
		
		ReversiModel model3 = new ReversiModel("testing/save_file_2.dat");
		ReversiController controller3 = new ReversiController(model3);
		
		// current board should be: {  {'w','w','w','w','w','w','w','w'},
		// 							   {'w','w','w','w','w','w','w','w'},
		// 							   {'w','w','w','w','w','w','w','w'},
		//							   {'w','w','w','w','w','w','w','w'},
		// 							   {'w','w','w','w','w','w','w','w'},
		// 							   {'w','w','w','w','w','w','w','w'},
		// 							   {'w','w','w','w','w','w','b','x'},
		// 							   {'w','w','w','w','w','w','b','x'}  }
		
		char[][] expectedBoard3 = {  {'w','w','w','w','w','w','w','w'},
				 					 {'w','w','w','w','w','w','w','w'},
				 					 {'w','w','w','w','w','w','w','w'},
				 					 {'w','w','w','w','w','w','w','w'},
				 					 {'w','w','w','w','w','w','w','w'},
				 					 {'w','w','w','w','w','w','w','w'},
				 					 {'w','w','w','w','w','w','w','w'},
				 					 {'w','w','w','w','w','w','w','w'}  };
			
		controller3.humanTurn(6, 7);
		controller3.humanTurn(7, 7);
		
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				assertEquals(model3.getPiece(row, col), expectedBoard3[row][col]);
			}
		}
		
		ReversiModel model4 = new ReversiModel("testing/save_file_3.dat");
		ReversiController controller4 = new ReversiController(model4);
		
		// current board should be: {  {'w','w','w','w','w','w','x','w'},
		// 							   {'x','b','w','w','w','w','b','w'},
		// 							   {'w','w','w','w','w','w','w','w'},
		//							   {'w','w','w','w','w','w','w','w'},
		// 							   {'w','w','w','w','w','w','w','w'},
		// 							   {'w','w','w','w','w','w','w','w'},
		// 							   {'w','b','w','w','w','w','b','x'},
		// 							   {'w','x','w','w','w','w','w','w'}  }
		
		char[][] expectedBoard4 = {  {'w','w','w','w','w','w','w','w'},
				 					 {'w','w','w','w','w','w','w','w'},
				 					 {'w','w','w','w','w','w','w','w'},
				 					 {'w','w','w','w','w','w','w','w'},
				 					 {'w','w','w','w','w','w','w','w'},
				 					 {'w','w','w','w','w','w','w','w'},
				 					 {'w','w','w','w','w','w','w','w'},
				 					 {'w','w','w','w','w','w','w','w'}  };
			
		controller4.humanTurn(1, 0);
		controller4.humanTurn(7, 1);
		controller4.humanTurn(6, 7);
		controller4.humanTurn(0, 6);
		
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				assertEquals(model4.getPiece(row, col), expectedBoard4[row][col]);
			}
		}
		
		// Test dummyHumanTurn
		assertEquals(controller.dummyHumanTurn(2, 3), 3);
		
		ReversiModel model2 = new ReversiModel("testing/save_file_1.dat");
		ReversiController controller2 = new ReversiController(model2);
		
		// current board should be: { {'x','x','x','x','x','x','x','x'},
		// 							 {'x','x','x','x','x','x','x','x'},
		// 							 {'x','x','x','x','x','x','x','x'},
		// 							 {'x','x','x','b','b','x','x','x'},
		// 							 {'x','x','b','b','b','w','x','x'},
		// 							 {'x','x','x','x','x','x','x','x'},
		// 							 {'x','x','x','x','x','x','x','x'},
		// 							 {'x','x','x','x','x','x','x','x'} }
		
		assertEquals(controller2.dummyHumanTurn(4, 1), 4);
		controller2.humanTurn(4, 1);
		
		assertEquals(controller2.dummyHumanTurn(4, 1), 0);
		controller2.humanTurn(4, 1);
		
		assertEquals(controller2.dummyHumanTurn(5, 3), 2);
		controller2.humanTurn(5, 3);
		
		assertEquals(controller2.dummyHumanTurn(3, 5), 0);
		controller2.humanTurn(3, 5);
		
		// Test computerTurn (check using the model)
		controller.computerTurn();
		assertEquals(controller.getWhiteScore(), 3);
		assertEquals(controller.getBlackScore(), 5);
		
		// Test isOver
		assertFalse(controller.isOver());
		model.setPiece(2, 3, 'w');
		model.setPiece(3, 3, 'w');
		model.setPiece(4, 3, 'w');
		model.setPiece(2, 4, 'w');
		model.setPiece(3, 4, 'w');
		model.setPiece(4, 4, 'w');
		model.setPiece(2, 5, 'w');
		model.setPiece(3, 5, 'w');
		model.setPiece(4, 5, 'w');
		assertTrue(controller.isOver());
		
	}
	
}
