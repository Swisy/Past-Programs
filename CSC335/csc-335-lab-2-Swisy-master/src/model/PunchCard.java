package model;

public class PunchCard {
	private int numVisits = 0;
	
	/*
	 * This method will add 1 to the number of visits
	 */
	public void addVisit() {
		numVisits += 1;
	}
	
	/*
	 * This method will test to see if the current visit is a multiple of the int n.
	 * The method will return true if it is and false if it is not.
	 */
	public boolean isNthVisit(int n) {
		if(numVisits % n == 0) {
			return true;
		}
		return false;
	}
}
