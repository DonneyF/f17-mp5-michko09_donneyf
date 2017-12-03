package ca.ece.ubc.cpen221.mp5.yelp;

import ca.ece.ubc.cpen221.mp5.interfaces.Review;
import ca.ece.ubc.cpen221.mp5.yelp.deserlializers.YelpReviewDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * A Yelp review object. Stores information of a review created on Yelp
 *
 * Representation Invariant: Type is "review" and reviewId is not null
 *
 * Abstraction Function: AF(this) -> A review such that text pertains to businessId.
 */
@JsonDeserialize(using = YelpReviewDeserializer.class)
public class YelpReview implements Review {

	private final String type;
	private String businessId;
	private YelpVotes votes;
	private final String reviewId;
	private String text;
	private int stars;
	private String userId;
	private String date;

	/**
	 * Constructs the YelpReview
	 *
	 * @param reviewId is a unique ID representing this review
	 */
	public YelpReview(String reviewId){
		this.reviewId = reviewId;
		type = "review";
	}

	/**
	 * Gets the type of this YelpReview
	 *
	 * @return "review"
	 */
	public String getType() {
		return type;
	}


	/**
	 * Gets the business ID this review is assigned to
	 *
	 * @return a string representing the business ID this review is assigned to
	 */
	public String getBusinessId() {
		return businessId;
	}

	/**
	 * Sets the business ID this review is assigned to
	 *
	 * @param businessId is not null
	 */
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	/**
	 * Gets the votes this review has
	 *
	 * @return a YelpVotes object of this review
	 */
	public YelpVotes getVotes() {
		return votes;
	}

	/**
	 * Sets the votes this review has
	 *
	 * @param votes is not null
	 */
	public void setVotes(YelpVotes votes) {
		this.votes = votes;
	}

	/**
	 * Gets the review ID of this YelpReview
	 *
	 * @return a string representing the ID of this review
	 */
	@Override
	public String getReviewId() {
		return reviewId;
	}

	/**
	 * Gets the review content of this review
	 *
	 * @return a string containing the review content
	 */
	public String getText() {
		return text;
	}

	/**
	 * Set the review content of this review
	 *
	 * @param text is not null
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Gets the number of stars this review has given
	 *
	 * @return an integer representing the number of stars this review has given
	 */
	public int getStars() {
		return stars;
	}

	/**
	 * Sets the number of stars this review has given
	 *
	 * @param stars is between 1 and 5
	 */
	public void setStars(int stars) {
		if (stars > 5 || stars < 1) throw new IllegalArgumentException("Stars is greater than 5 or less than 1");
		this.stars = stars;
	}

	/**
	 * Gets the ID of the user that made this review
	 *
	 * @return a string representing the unique user ID that made this review
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the ID of the user that made this review
	 *
	 * @param userId is not null
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the date this review was created
	 *
	 * @return a string that represents the date that this review was created
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the date that this review was created
	 *
	 * @param date is not null
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Compare two Review objects for equality
	 *
	 * @param other is not null
	 *
	 * @return true if this Review and the other Review represent the same
	 *         review.
	 */
	@Override
	public boolean equals(Object other) {

		return other instanceof Review && (this.getReviewId().equals(((Review) other).getReviewId()));
	}

	/**
	 * Compute the hashCode for this Review object
	 *
	 * @return the hashCode for this Review object
	 */
	@Override
	public int hashCode() {
		return this.getReviewId().hashCode();
	}

	/**
	 * Gets the string representation of this review
	 *
	 * @return a string of the review contents for this review
	 */
	@Override
	public String toString(){
		return getText();
	}
}
