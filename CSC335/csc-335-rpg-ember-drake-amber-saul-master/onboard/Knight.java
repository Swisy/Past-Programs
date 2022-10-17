package onboard;

import model.Team;

/**
 * This class defines stats for the Knight piece.
 * 
 * 
 * @author Drake Sitaraman
 *
 */
public class Knight extends Piece{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Defines stats for the Knight class. These can easily be changed
	 * without removing functionality from the overall game.
	 * 
	 * @param team is the team of the Knight as an Enum.
	 */
	public Knight(Team team) {
		super(); //Initialize Random
		
		health = 100;
		
		attackRange[0] = 20; //Lowest possible attack is 20.
		attackRange[1] = 30; //Highest possible attack is 30.
		
		attackDistance = 1;
		
		defenseRange[0] = 10; //Can defend as little as 10 damage
		defenseRange[1] = 20; //Can defend as high as 20 damage
		
		moveDistanceRemaining = 1; //i.e. can only move 1 tile
		
		isDefended = false;
		hasAttackedOrDefended = false;
		
		this.team = team;
		
		spriteFileName = "assets/knight_"+ this.team +".png";

	}
	
	@Override
	/**
	 * Resets move distance and reverts defend and attack
	 * flags back to false.
	 */
	public void resetTurn() {
		moveDistanceRemaining = 1;
		hasAttackedOrDefended = false;
		if (isRested) {
			health += Math.floor(Math.random()*(6)+5); // add between 5 and 10 health to character
			health = Math.min(health, 100);
		}
	}

	
}