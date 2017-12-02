package ca.ece.ubc.cpen221.mp5.yelp;

import ca.ece.ubc.cpen221.mp5.interfaces.User;
import ca.ece.ubc.cpen221.mp5.yelp.deserlializers.YelpUserDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = YelpUserDeserializer.class)
public class YelpUser implements User {

	private String url;
	private int reviewCount;
	private String type;
	private final String userId;
	private String name;
	private Double averageStars;
	private YelpVotes votes;

	public YelpUser(String userId){
		this.userId = userId;
	}

	public String getWebsite() {
		return url;
	}

	public void setWebsite(String url) {
		this.url = url;
	}

	public int getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

	@Override
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getUserId() {
		return userId;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getAverageStars() {
		return averageStars;
	}

	public void setAverageStars(Double averageStars) {
		this.averageStars = averageStars;
	}

	@Override
	public YelpVotes getVotes() {
		return votes;
	}

	public void setVotes(YelpVotes votes) {
		this.votes = votes;
	}

	/**
	 * Obtains the number of reviews conducted by the user.
	 *
	 * @return an integer, which: - is not null. - is the number of completed reviews.
	 */

	/**
	 * Obtains the average star rating given by the user.
	 *
	 * @return a double, which: - is not null. - is the value of the current average stars rating given by the user.
	 */

	
	/**
	 * Obtains the votes made the user on a specific business, by category.
	 * 
	 * @return a ___________-, which: - is not null. - is the representation of the votes made by the user.
	 */

	/**
	 * Compare two User objects for equality
	 *
	 * @param other
	 * @return true if this User and the other User represent the same
	 *         user.
	 */
	@Override
	public boolean equals(Object other) {
		return other instanceof User && (this.getUserId().equals(((YelpUser) other).getUserId()));
	}

	/**
	 * Compute the hashCode for this User object
	 *
	 * @return the hashCode for this User object
	 */
	@Override
	public int hashCode() {
		return this.getName().hashCode() * this.getWebsite().hashCode() * 7;
	}

	@Override
	public String toString(){
		return getUserId();
	}
}