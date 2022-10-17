package onboard;

/**
 * 
 * This represents a tile such as a water or a fence. Pieces can
 * shoot over it, but cannot move into it.
 * 
 * @author Drake Sitaraman
 *
 */
public class BlockedSeeThroughTile extends Tile{
	
	/**
	 * No arg constructor for BlockedSeeThroughTile
	 */
	public BlockedSeeThroughTile() {
		super();
		imgPath="assets/BlockedSeeThroughTile.png";
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	/**
	 * Method for whether a piece can move into this tile.
	 * 
	 * Blocked tiles will only allow flying units to occpy them.
	 * @return true if the piece can move into the tile, false otherwise
	 */
	public boolean canMoveInto(Piece p) {
		return super.canMoveInto(p) && p instanceof Flyer;
	}

	@Override
	/**
	 * @return true
	 */
	public boolean canShootThrough() {
		return true;
	}
	
}