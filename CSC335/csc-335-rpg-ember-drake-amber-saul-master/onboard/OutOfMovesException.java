package onboard;

/**
 * 
 * This exception is thrown whenever a piece attempts
 * to move a certain distance without enough moves
 * remaining. This is unchecked because this can
 * be determined through the Piece class's public methods.
 * 
 * @author Drake Sitaraman
 *
 */
public class OutOfMovesException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OutOfMovesException(String string) {
	}

}
