package onboard;

import java.io.Serializable;

/**
 * This class is the abstract implementation of a Tile. A "Tile"
 * is a piece on the 2D array stored by the model. It has several
 * characteristics, such as whether a piece can stand on it,
 * whether a piece can move through it, whether a piece can shoot through
 * it, or whether it is occupied or not. These are all handled
 * by their respective derived classes.
 * 
 * @author Drake Sitaraman
 *
 */
public abstract class Tile implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Piece piece; //Piece who is standing on the tile
	protected String imgPath;
	
	/**
	 * @return The Piece object at the tile or null if one does not exist.
	 */
	public Piece getPiece() {
		return this.piece;
	}
	
	/**
	 * A short way to determine move validity. Set the piece if the tile is free
	 * to move into. Throw an Exception if there is an existing piece on the tile
	 * or the tile is either a BlockedTile or BlockedSeeThroughTile
	 *  
	 * @param newPiece is the Piece object to set as the field.
	 * @throws InvalidMoveException if the piece is not able to move into
	 * the tile at this particular time.
	 */
	public void setPiece(Piece newPiece) throws InvalidMoveException {
		if(this.canMoveInto(newPiece)) {
			this.piece = newPiece;
		} else {
			throw new InvalidMoveException("Can't move the piece there! Either an invalid tile or a piece is already there");
		}
	}
	
	/**
	 * Set the piece to null.
	 * 
	 * @throws InvalidRemovalException 
	 */
	public void removePiece() throws InvalidRemovalException {
		if(!(this.piece instanceof Flyer || this instanceof OpenTile)) {
			throw new InvalidRemovalException("Can't remove piece from this tile");
		}
		this.piece = null;
	}
	
	public void removeComputerPiece() {
		this.piece = null;
	}
	
	public void setComputerPiece(Piece newPiece) {
		this.piece = newPiece;
	}
	
	/**
	 * @return true if the Tile has a player there. false if not.
	 */
	public boolean hasPlayer() {
		return piece != null;
	}
	
	public boolean isOpenTile() {
		return this instanceof OpenTile;
	}
	
	/**
	 * Returns whether the piece p can move into the given tile
	 * @param p - the piece attempting to move into the tile
	 * @return - Whether p can move into the tile, as a boolean
	 */
	public boolean canMoveInto(Piece p) {
		return piece==null; 
	}
	
	/**
	 * Abstract method for determining if a piece can move into it.
	 * 
	 * @return false if a BlockedSeeThroughTile. true otherwise.
	 */
	public abstract boolean canShootThrough();
	
	/**
	 * Returns the filepath of the image for this tile
	 * @return the filepath of the image for this tile, as a String
	 */
	public String getImgPath() {
		return this.imgPath;
	}

}