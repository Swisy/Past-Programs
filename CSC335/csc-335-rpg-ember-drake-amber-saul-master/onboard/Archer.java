package onboard;

import model.Team;

/**
 * This class defines stats for the Archer piece.
 * 
 * 
 * @author Drake Sitaraman
 *
 */
public class Archer extends Piece{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Defines stats for the Archer class. These can easily be changed
	 * without removing functionality from the overall game.
	 * 
	 * @param team is the team of the Archer as an Enum.
	 */
	public Archer(Team team) {
		super(); //Initialize Random
		
		health = 100;
		
		attackRange[0] = 10; //Minimum attack is 10
		attackRange[1] = 15; //Maximum attack is 15
		
		attackDistance = 2;
		
		defenseRange[0] = 5; //Can defend at least 5 damage
		defenseRange[1] = 10; //Can defend at most 10 damage
		
		moveDistanceRemaining = 2;
		
		isDefended = false;
		hasAttackedOrDefended = false;
		
		this.team = team;
		
		spriteFileName = "assets/archer_"+ this.team +".png";

	}

	@Override
	/**
	 * Resets move distance and reverts defend and attack
	 * flags back to false.
	 */
	public void resetTurn() {
		moveDistanceRemaining = 2;
		hasAttackedOrDefended = false;
		if (isRested) {
			health += Math.floor(Math.random()*(6)+5); // add between 5 and 10 health to character
			health = Math.min(health, 100);
		}
	}
	
}