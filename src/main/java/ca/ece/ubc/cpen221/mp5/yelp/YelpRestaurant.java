package ca.ece.ubc.cpen221.mp5.yelp;

import ca.ece.ubc.cpen221.mp5.interfaces.Business;
import ca.ece.ubc.cpen221.mp5.yelp.deserlializers.YelpRestaurantDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(using = YelpRestaurantDeserializer.class)
public class YelpRestaurant implements Business {

	// Non-JSON fields: businessId, reviewCount, photoUrl
	private boolean open;
	private String url;
	private double longitude;
	private double latitude;
	private List<String> neighborhoods;
	private String businessId;
	private String name;
	private List<String> categories;
	private String state;
	private String type;
	private double stars;
	private String city;
	private String address;
	private int reviewCount;
	private String photoUrl;
	private List<String> schools;
	private int price;

	@Override
	public boolean isOpen() {
		return open;
	}

	@Override
	public void setOpen(boolean open) {
		this.open = open;
	}

	@Override
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public List<String> getNeighborhoods() {
		return neighborhoods;
	}

	public void setNeighborhoods(List<String> neighborhoods) {
		this.neighborhoods = neighborhoods;
	}

	@Override
	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	@Override
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getStars() {
		return stars;
	}

	public void setStars(double stars) {
		this.stars = stars;
	}

	@Override
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

	@Override
	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public List<String> getSchools() {
		return schools;
	}

	public void setSchools(List<String> schools) {
		this.schools = schools;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * Obtains the neighborhoods nearby the restaurant.
	 *
	 * @return a List of Strings, which: - is not null. - is a list of neighborhood names.
	 */

	/**
	 * Obtains all categories of services offered by the restaurant.
	 *
	 * @return a List of Strings, which: - is not null. - is a list of service types.
	 */

	/**
	 * Obtains the average star rating of the restaurant.
	 *
	 * @return a double, which: - is not null. - is the value of the current average stars rating of the restaurant.
	 */

	/**
	 * Obtains the number of reviews conducted on the restaurant.
	 *
	 * @return an integer, which: - is not null. - is the number of completed reviews.
	 */

	/**
	 * Obtains the names of the schools that have the restaurant located inside them.
	 *
	 * @return a double, which: - is not null. - is the value of the schools containing the restaurant.
	 */

	/**
	 * Obtains the customer's evaluation of pricing of the restaurant.
	 *
	 * @return an integer, which: - is not null. - is the score regarding pricing of the restaurant menu.
	 */

	/**
	 * Compare two Business objects for equality
	 *
	 * @param other
	 * @return true if this Business and the other Business represent the same
	 *         business.
	 */
	@Override
	public boolean equals(Object other) {
		return other instanceof Business && (this.getBusinessId().equals(((Business)other).getBusinessId()));
	}

	/**
	 * Compute the hashCode for this Business object
	 *
	 * @return the hashCode for this Business object
	 */
	@Override
	public int hashCode() {
		return (int) (this.getLatitude() * this.getLongitude() * 7);
	}

	@Override
	public String toString(){
		return getBusinessId();
	}
}
