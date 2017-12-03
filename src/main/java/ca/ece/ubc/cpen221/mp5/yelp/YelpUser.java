package ca.ece.ubc.cpen221.mp5.yelp;

import ca.ece.ubc.cpen221.mp5.interfaces.User;
import ca.ece.ubc.cpen221.mp5.yelp.deserlializers.YelpUserDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * A Yelp user object. Stores information of a user registered on Yelp
 *
 * Representation Invariant: Type is "user" and userID is not null.
 *
 * Abstraction Function: AF(this) -> A user such that url contains a parsed form of name.
 */
@JsonDeserialize(using = YelpUserDeserializer.class)
public class YelpUser implements User {

	private String url;
	private int reviewCount;
	private final String type;
	private final String userId;
	private String name;
	private Double averageStars;
	private YelpVotes votes;

	/**
	 * Constructs the YelpUser
	 * @param userId is not null
	 */
	public YelpUser(String userId) {
		this.userId = userId;
		type = "user";
	}

	/**
	 * Get the url of the profile of the user
	 *
	 * @return the yelp url of the profile of the user
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Set the url of the profile of the user
	 *
	 * @param url contains "yelp.com"
	 */
	public void setUrl(String url) {
		if(!url.contains("yelp")) throw new IllegalArgumentException("User profile should be on yelp.com");
		this.url = url;
	}

	/**
	 * Gets the number of reviews that this user has made
	 *
	 * @return the number of reviews that this user has made
	 */
	public int getReviewCount() {
		return reviewCount;
	}

	/**
	 * Sets the number of reviews that this user has made
	 *
	 * @param reviewCount is > 0
	 */
	public void setReviewCount(int reviewCount) {
		if (reviewCount < 0) throw new IllegalArgumentException("Review count must be > 0");
		this.reviewCount = reviewCount;
	}

	/**
	 * Gets the type of this YelpUser
	 *
	 * @return "user"
	 */
	public String getType() {
		return type;
	}

	/**
	 * Gets the user ID of this user
	 *
	 * @return the user ID of this user
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Gets the name of this user
	 *
	 * @return the name of this user
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this user
	 *
	 * @param name is not null
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the average amount of stars this user has given
	 *
	 * @return the average amount of stars this user has given
	 */
	public Double getAverageStars() {
		return averageStars;
	}

	/**
	 * Sets the average amount of stars this user has given
	 *
	 * @param averageStars is greater than 1 and less than 5
	 */
	public void setAverageStars(Double averageStars) {
		if (averageStars > 5 || averageStars < 0) throw new IllegalArgumentException("Average review must be greater than 1 and less than 5");
		this.averageStars = averageStars;
	}

	/**
	 * Gets the votes this user has given
	 *
	 * @return a YelpVotes object representing the votes the user has given
	 */
	public YelpVotes getVotes() {
		return votes;
	}

	/**
	 * Sets the votes this user has given
	 *
	 * @param votes is not null
	 */
	public void setVotes(YelpVotes votes) {
		this.votes = votes;
	}

	/**
	 * Compare two User objects for equality
	 *
	 * @param other is not null
	 * @return true if this User and the other User represent the same user.
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
		return getUserId().hashCode();
	}

	/**
	 * Gets the string representation of this user
	 *
	 * @return the name of this user
	 */
	@Override
	public String toString(){
		return getName();
	}
}