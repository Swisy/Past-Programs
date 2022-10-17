import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import controller.MastermindController;
import controller.MastermindIllegalColorException;
import controller.MastermindIllegalLengthException;
import model.MastermindModel;

/**
 * This class collects all of the test methods for our controller.
 * 
 * In eclipse, running it should run it under JUnit. Running the Mastermind class (since
 * it is our main class) will actually run the program. If you're not using eclipse,
 * you'll need to run this under JUnit 5. 
 * 
 * @author Saul Weintraub
 *
 */
class MastermindTest {

	/**
	 * Test method for {@link MastermindController#isCorrect(java.lang.String)}.
	 * @throws MastermindIllegalColorException 
	 * @throws MastermindIllegalLengthException 
	 */
	@Test
	void testIsCorrect() throws MastermindIllegalLengthException, MastermindIllegalColorException {
		//Build a model with a known answer, using our special testing constructor
		MastermindModel answer = new MastermindModel("rrrr");
		//Build the controller from the model
		MastermindController controllerUnderTest = new MastermindController(answer);
		
		//For a properly working controller, this should return true
		assertTrue(controllerUnderTest.isCorrect("rrrr"));
		//For a properly working controller, this should be false
		assertFalse(controllerUnderTest.isCorrect("oooo"));
		
		//Make as many more assertions as you feel you need to test the MastermindController.isCorrect method
		
		//Build a model with an unknown answer
		MastermindModel randomAnswer = new MastermindModel();
		//Build the controller from the model
		MastermindController randomController = new MastermindController(randomAnswer);
		
		//For a properly working controller, this should throw the exception
		assertThrows(MastermindIllegalColorException.class,
				() -> { randomController.isCorrect("xxxx"); });
		
		//For a properly working controller, this should throw the exception
				assertThrows(MastermindIllegalLengthException.class,
						() -> { randomController.isCorrect("rrrrr"); });
	}

	/**
	 * Test method for {@link MastermindController#getRightColorRightPlace(java.lang.String)}.
	 * @throws MastermindIllegalColorException 
	 * @throws MastermindIllegalLengthException 
	 */
	@Test
	void testGetRightColorRightPlace() throws MastermindIllegalLengthException, MastermindIllegalColorException {
		//Build a model with a known answer, using our special testing constructor
		MastermindModel answer = new MastermindModel("rrrr");
		//Build the controller from the model
		MastermindController controllerUnderTest = new MastermindController(answer);
		
		//For a properly working controller, this should return 4
		assertEquals(controllerUnderTest.getRightColorRightPlace("rrrr"), 4);
		
		//For a properly working controller, this should return 0
		assertEquals(controllerUnderTest.getRightColorRightPlace("oooo"), 0);
		
		//You'll need lots more of these to convince yourself your implementation is right
		
		//Build a model with an unknown answer
		MastermindModel randomAnswer = new MastermindModel();
		//Build the controller from the model
		MastermindController randomController = new MastermindController(randomAnswer);
		
		//For a properly working controller, this should throw the exception
				assertThrows(MastermindIllegalColorException.class,
						() -> { randomController.getRightColorRightPlace("xxxx"); });
				
		//For a properly working controller, this should throw the exception
				assertThrows(MastermindIllegalLengthException.class,
						() -> { randomController.getRightColorRightPlace("rrrrr"); });
	}

	/**
	 * Test method for {@link MastermindController#getRightColorWrongPlace(java.lang.String)}.
	 * @throws MastermindIllegalColorException 
	 * @throws MastermindIllegalLengthException 
	 */
	@Test
	void testGetRightColorWrongPlace() throws MastermindIllegalLengthException, MastermindIllegalColorException {
		//Build a model with a known answer, using our special testing constructor
		MastermindModel answer = new MastermindModel("rrgp");
		//Build the controller from the model
		MastermindController controllerUnderTest = new MastermindController(answer);
		
		//For a properly working controller, this should return 1
		assertEquals(controllerUnderTest.getRightColorWrongPlace("pooo"), 1);
				
		//For a properly working controller, this should return 2
		assertEquals(controllerUnderTest.getRightColorWrongPlace("oopg"), 2);
		
		//For a properly working controller, this should return 0
		assertEquals(controllerUnderTest.getRightColorWrongPlace("oooo"), 0);
		
		
		//Build a model with an unknown answer
		MastermindModel randomAnswer = new MastermindModel();
		//Build the controller from the model
		MastermindController randomController = new MastermindController(randomAnswer);
		
		//For a properly working controller, this should throw the exception
		assertThrows(MastermindIllegalColorException.class,
				() -> { randomController.getRightColorWrongPlace("xxxx"); });
		
		//For a properly working controller, this should throw the exception
		assertThrows(MastermindIllegalLengthException.class,
				() -> { randomController.getRightColorWrongPlace("rrrrr"); });
		
	}

}
