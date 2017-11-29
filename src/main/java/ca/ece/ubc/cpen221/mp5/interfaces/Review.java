package ca.ece.ubc.cpen221.mp5.interfaces;

public interface Review {
	
	/**
	 * Obtains the type of the data structure.
	 * 
	 * @return a string, which: - is not null. - is the type of the data structure.
	 */
	String getType();
	
	/**
	 * Obtains the ID of the business.
	 * 
	 * @return a string, which: - is not null. - is the ID of the specific
	 *         business under review.
	 */
	String getBusinessId();
	
	/**
	 * Obtains the ID of the business.
	 * 
	 * @return a string, which: - is not null. - is the ID of the specific
	 *         business under review.
	 */
	String getReviewId();
	
	/**
	 * Obtains the text review written by the user.
	 * 
	 * @return a string, which: - is not null. - is the written response left by the user.
	 */
	String getText();
	
	/**
	 * Obtains the ID of the user who created the review.
	 * 
	 * @return a string, which: - is not null. - is the ID of the specific
	 *         user.
	 */
	String getUserId();
	
	/**
	 * Obtains the date at which the review was made.
	 * 
	 * @return a string, which: - is not null. - is the date of the review in the format: YYYY-MM-DD.
	 */
	String getDate();

}
