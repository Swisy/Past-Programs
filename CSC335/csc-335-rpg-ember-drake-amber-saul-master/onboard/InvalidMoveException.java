package onboard;

/**
 * This method is called whenever attempting to make an invalid move.
 * This can be trying to move into a Blocked tile or trying to move
 * into a tile where a piece already exists. This is an unchecked
 * exception, because the programmer can check this with an if statement.
 * 
 * 
 * @author Drake Sitaraman
 *
 */
public class InvalidMoveException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidMoveException(String string) {
	}

}
