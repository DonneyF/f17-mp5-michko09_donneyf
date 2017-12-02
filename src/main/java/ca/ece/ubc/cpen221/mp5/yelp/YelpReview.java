package ca.ece.ubc.cpen221.mp5.yelp;

import ca.ece.ubc.cpen221.mp5.interfaces.Review;
import ca.ece.ubc.cpen221.mp5.yelp.deserlializers.YelpReviewDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = YelpReviewDeserializer.class)
public class YelpReview implements Review {

	private String type;
	private String businessId;
	private YelpVotes votes;
	private final String reviewId;
	private String text;
	private int stars;
	private String userId;
	private String date;

	public YelpReview(String reviewId){
		this.reviewId = reviewId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public YelpVotes getVotes() {
		return votes;
	}

	public void setVotes(YelpVotes votes) {
		this.votes = votes;
	}

	@Override
	public String getReviewId() {
		return reviewId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Obtains the votes made the user on a specific business, by category.
	 *
	 * @return a ___________-, which: - is not null. - is the representation of the votes made by the user.
	 */

	/**
	 * Obtains the star rating given by the user.
	 *
	 * @return an integer, which: - is not null. - is the value rating left by the user.
	 */

	/**
	 * Compare two Review objects for equality
	 *
	 * @param other
	 * @return true if this Review and the other Review represent the same
	 *         review.
	 */
	@Override
	public boolean equals(Object other) {

		if (other instanceof Review) {
			Review otherDoc = (Review) other;
			return (this.getReviewId().equals(otherDoc.getReviewId()));
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
		return this.getReviewId().hashCode() * 7;
	}

	@Override
	public String toString(){
		return getText();
	}
}
