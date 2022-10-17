package onboard;

/**
 * 
 * A Blocked Tile is a tile that a player cannot move through, nor
 * can an archer shoot through. This would be something like
 * a wall or a mountain.
 * 
 * @author Drake Sitaraman
 *
 */
public class BlockedTile extends Tile{
	
	/**
	 * No arg constructor for Blocked Tile
	 */
	public BlockedTile() {
		super();
		imgPath = "assets/BlockedTile.png";
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
	 * @return false
	 */
	public boolean canShootThrough() {
		return false;
	}
	
}