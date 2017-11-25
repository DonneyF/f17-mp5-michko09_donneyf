package ca.ece.ubc.cpen221.mp5.datastructure;

public class Review {
	Table reviewTable;
	
	public Review(Table table) {
		this.reviewTable = table;
	}
	
	/**
	 * Obtains the type of the data structure.
	 * 
	 * @return a string, which: - is not null. - is the type of the data structure.
	 */
	String getType() {
		return (String) reviewTable.getData("type").get(0);
	}
	
	/**
	 * Obtains the ID of the business.
	 * 
	 * @return a string, which: - is not null. - is the ID of the specific
	 *         business under review.
	 */
	String getBusinessID() {
		return (String) reviewTable.getData("business_id").get(0);
	}
	
	/**
	 * Obtains the ID of the business.
	 * 
	 * @return a string, which: - is not null. - is the ID of the specific
	 *         business under review.
	 */
	String getReviewID() {
		return (String) reviewTable.getData("review_id").get(0);
	}
	
	/**
	 * Obtains the votes made the user on a specific business, by category.
	 * 
	 * @return a ___________-, which: - is not null. - is the representation of the votes made by the user.
	 */
	// DO VOTES HERE
	
	/**
	 * Obtains the text review written by the user.
	 * 
	 * @return a string, which: - is not null. - is the written response left by the user.
	 */
	String getText() {
		return (String) reviewTable.getData("text").get(0);
	}
	
	/**
	 * Obtains the star rating given by the user.
	 * 
	 * @return an integer, which: - is not null. - is the value rating left by the user.
	 */
	int getStars() {
		return (int) reviewTable.getData("stars").get(0);
	}
	
	/**
	 * Obtains the ID of the user who created the review.
	 * 
	 * @return a string, which: - is not null. - is the ID of the specific
	 *         user.
	 */
	String getUserID() {
		return (String) reviewTable.getData("user_id").get(0);
	}
	
	/**
	 * Obtains the date at which the review was made.
	 * 
	 * @return a string, which: - is not null. - is the date of the review in the format: YYYY-MM-DD.
	 */
	String getDate() {
		return (String) reviewTable.getData("user_id").get(0);
	}
	
	/**
	 * Compare two Review objects for equality
	 * 
	 * @param other
	 * @return true if this Review and the other Review represent the same
	 *         review.
	 */
	@Override
	public boolean equals(Object other) {

		if (other instanceof User) {
			Review otherDoc = (Review) other;
			return (this.getReviewID().equals(otherDoc.getReviewID()));
		} else {
			return false;
		}
	}
	
	/**
	 * Compute the hashCode for this Review object
	 * 
	 * @return the hashCode for this Review object
	 */
	@Override
	public int hashCode() {
		return (int) (this.getStars() * 7);
	}

}
