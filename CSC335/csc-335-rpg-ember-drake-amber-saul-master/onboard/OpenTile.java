package onboard;

/**
 * This Tile represents any tile that a player can have the ability to move into.
 * The tile may not be "open" if a player moves into it, but if that player vacates
 * the tile, then another player can move into it.
 * 
 * 
 * @author Drake Sitaraman
 *
 */
public class OpenTile extends Tile{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor, assumes there is no piece on it.
	 */
	public OpenTile() {
		this(null);
	}
	
	/**
	 * Alternate constructor to add a Piece standing on it by
	 * default.
	 * 
	 * @param piece is a Piece object.
	 */
	public OpenTile(Piece piece) {
		imgPath="assets/OpenTile.png";
		super.piece = piece;
	}
	

	@Override
	/**
	 * @return true because either there is nothing blocking a piece
	 * from doing so, or because you can shoot over the player.
	 */
	public boolean canShootThrough() {
		return true;
	}
	
}