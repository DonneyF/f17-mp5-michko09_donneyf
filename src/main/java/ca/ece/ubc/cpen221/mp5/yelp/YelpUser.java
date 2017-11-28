package ca.ece.ubc.cpen221.mp5.yelp;

import ca.ece.ubc.cpen221.mp5.datastructure.Table;
import ca.ece.ubc.cpen221.mp5.datastructure.User;

public class YelpUser extends User {

	public YelpUser(Table table) {
		super(table);
	}
	
	/**
	 * Obtains the number of reviews conducted by the user.
	 * 
	 * @return an integer, which: - is not null. - is the number of completed reviews.
	 */
	int getReviewCount() {
		return (int) userTable.getData("review_count").get(0);
	}
	
	/**
	 * Obtains the votes made the user on a specific business, by category.
	 * 
	 * @return a ___________-, which: - is not null. - is the representation of the votes made by the user.
	 */
	// INPUT VOTES HERE
	
	/**
	 * Obtains the average star rating given by the user.
	 * 
	 * @return a double, which: - is not null. - is the value of the current average stars rating given by the user.
	 */
	double getAverageStars() {
		return (double) userTable.getData("average_stars").get(0);
	}
	
}
