package onboard;

/**
 * 
 * This exception is thrown whenever attempting to remove
 * a tile from a BlockedTile or BlockedSeeThroughTile.
 * It is unchecked because this is a programmer error--
 * not checking whether the tile could be removed from.
 * 
 * @author Drake Sitarman
 *
 */
public class InvalidRemovalException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidRemovalException(String string) {
	}

}
