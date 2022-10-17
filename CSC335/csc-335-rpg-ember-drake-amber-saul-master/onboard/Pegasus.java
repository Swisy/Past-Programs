package onboard;

import model.Team;

/**
 * This class represents the Pegasus piece.
 * 
 * The Pegusus is a piece that can fly over obstacles, but has low damage and defense.
 * 
 * @author Ember Chan
 *
 */
public class Pegasus extends Flyer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for a new Pegasus
	 * @param team - the team the new pegasus is on
	 */
	public Pegasus(Team team) {
		super();
		
		health = 100;
		
		attackRange[0] = 10; //Lower bound for attack damage
		attackRange[1] = 15; //Upper bound for attack damage
		
		attackDistance = 1; //Melee unit
		
		defenseRange[0] = 0; //Lower bound for defense
		defenseRange[1] = 10; //Upper bound for defense
		
		moveDistanceRemaining = 3; //Can moves 3 tiles per turn - very mobile character
		
		isDefended = false;
		hasAttackedOrDefended = false;
		
		this.team = team;
		
		spriteFileName = "assets/pegasus_"+ this.team +".png";

	}

	/**
	 * Restores expended movement and action
	 * resources when the piece is reset after
	 * a turn. 
	 */
	@Override
	public void resetTurn() {
		moveDistanceRemaining = 3;
		hasAttackedOrDefended = false;
		if (isRested) {
			health += Math.floor(Math.random()*(6)+5); // add between 5 and 10 health to character
			health = Math.min(health, 100);
		}
	}

}
