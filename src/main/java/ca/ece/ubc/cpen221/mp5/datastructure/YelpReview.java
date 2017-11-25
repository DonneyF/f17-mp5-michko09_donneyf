package ca.ece.ubc.cpen221.mp5.datastructure;

public class YelpReview extends Review {
	
	public YelpReview(Table table) {
		super(table);
	}
	
	/**
	 * Obtains the votes made the user on a specific business, by category.
	 * 
	 * @return a ___________-, which: - is not null. - is the representation of the votes made by the user.
	 */
	// DO VOTES HERE
	
	/**
	 * Obtains the star rating given by the user.
	 * 
	 * @return an integer, which: - is not null. - is the value rating left by the user.
	 */
	int getStars() {
		return (int) reviewTable.getData("stars").get(0);
	}

}