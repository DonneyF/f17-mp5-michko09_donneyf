package ca.ece.ubc.cpen221.mp5.yelp;

import ca.ece.ubc.cpen221.mp5.interfaces.Business;
import ca.ece.ubc.cpen221.mp5.interfaces.Restaurant;
import ca.ece.ubc.cpen221.mp5.yelp.deserlializers.YelpRestaurantDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

/**
 * A Yelp Restaurant object. Stores information for a restaurant registered with Yelp
 * <p>
 * Representation Invariant: businessId is not null and type is business
 * <p>
 * Abstraction Function: AF(this) -> A Restaurant such that: neighborhood, longitude, latitude, address, city, and schools are abstractly located in state
 */
@JsonDeserialize(using = YelpRestaurantDeserializer.class)
public class YelpRestaurant implements Restaurant {

    // Non-JSON fields: businessId, reviewCount, photoUrl
    private boolean open;
    private String url;
    private double longitude;
    private double latitude;
    private List<String> neighborhoods;
    private final String businessId;
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

    /**
     * Constructor
     *
     * @param businessId is not null and contains some alphanumeric characters, dashes, or underscores
     */
    public YelpRestaurant(String businessId) {
        this.businessId = businessId;
        this.type = "business";
    }

    /**
     * Checks if the restaurant is open
     *
     * @return True if the business is open. False otherwise.
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Sets the state of open state of the restaurant
     *
     * @param open is not null
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * Gets the restaurant's Yelp url
     *
     * @return a string containing a url such that yelp.com is in the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url of the website.
     *
     * @param url contains yelp.com
     */
    public void setUrl(String url) {
        if (!url.contains("yelp.com")) throw new IllegalArgumentException("Url is not a yelp website");
        this.url = url;
    }

    /**
     * Gets the longitude of the restaurant
     *
     * @return the longitude of the restaurant
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the restaurant
     *
     * @param longitude is between 180 and -180
     */
    public void setLongitude(double longitude) {
        if (longitude > 180 || longitude < -180) throw new IllegalArgumentException("Illegal longitude");
        this.longitude = longitude;
    }

    /**
     * Gets the latitude of the restaurant
     *
     * @return the latitude of the restaurant
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the restaurant
     *
     * @param latitude is between 90 and -90
     */
    public void setLatitude(double latitude) {
        if (latitude > 180 || latitude < -180) throw new IllegalArgumentException("Illegal latitude");
        this.latitude = latitude;
    }

    /**
     * Gets the neighborhoods this restaurant belongs to
     *
     * @return a list of neighborhoods this restaurant belongs to
     */
    public List<String> getNeighborhoods() {
        return neighborhoods;
    }

    /**
     * Sets the neighborhoods this restaurant belongs to
     *
     * @param neighborhoods is not null
     */
    public void setNeighborhoods(List<String> neighborhoods) {
        this.neighborhoods = new ArrayList<>(neighborhoods);
    }

    /**
     * Gets the ID of this restaurant
     *
     * @return a string of tis restaurant's business ID
     */
    @Override
    public String getBusinessId() {
        return businessId;
    }

    /**
     * Gets the name of this restaurant
     *
     * @return the name of this restaurant
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this restaurant
     *
     * @param name is not null
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the food categories this restaurant belongs to
     *
     * @return A list of food categories this restaurant belongs to
     */
    public List<String> getCategories() {
        return categories;
    }

    /**
     * Sets the categories that this restaurant belongs to
     *
     * @param categories is not null
     */
    public void setCategories(List<String> categories) {
        this.categories = new ArrayList<>(categories);
    }

    /**
     * Gets the geographic state of this restaurant
     *
     * @return the geographic state of this restaurant
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state of this restaurant
     *
     * @param state is not null
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Gets the type of this restaurant
     *
     * @return "business"
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the rating of this restaurant
     *
     * @return a number between 1 and 5 representing the rating of this restaurant
     */
    public double getStars() {
        return stars;
    }

    /**
     * Sets the rating of this restaurant
     *
     * @param stars is between 1 and 5
     */
    public void setStars(double stars) {
        if (stars > 5 || stars < 1) throw new IllegalArgumentException("Stars is greater than 5 or less than 1");
        this.stars = stars;
    }

    /**
     * Gets the location of this restaurant
     *
     * @return a String representing the city where this restaurant is located
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city of this restaurant
     *
     * @param city abstractly contains this restaurant
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the address of this restaurant
     *
     * @return a string containing the full address of this restaurant
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of this restaurant
     *
     * @param address is a complete address containing street, city, state (if applicable) and postal code (if applicable)
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the number of reviews this restaurant received
     *
     * @return an integer representing then number of reviews this restaurant received
     */
    public int getReviewCount() {
        return reviewCount;
    }

    /**
     * Sets the number of reviews this restaurant received
     *
     * @param reviewCount is greater than 0
     */
    public void setReviewCount(int reviewCount) {
        if (reviewCount < 0) throw new IllegalArgumentException("Cannot have negative review count");
        this.reviewCount = reviewCount;
    }

    /**
     * Gets the Yelp photo URL of this restaurant
     *
     * @return a url hosted on yelp.com where the featured image of this restaurant can be found
     */
    public String getPhotoUrl() {
        return photoUrl;
    }

    /**
     * Sets the photo url of this restaurant
     *
     * @param photoUrl contains yelp.com
     */
    public void setPhotoUrl(String photoUrl) {
        if (!photoUrl.contains("yelp")) throw new IllegalArgumentException("Url must be hosted on Yelp");
        this.photoUrl = photoUrl;
    }

    /**
     * Gets the school names that this restaurant is near to
     *
     * @return A list of strings representing schools that this restaurant is near to
     */
    public List<String> getSchools() {
        return schools;
    }

    /**
     * Sets the schools that this restaurant is near to
     *
     * @param schools is not null and each string in schools abstractly represents the name of the school
     */
    public void setSchools(List<String> schools) {
        this.schools = new ArrayList<>(schools);
    }

    /**
     * Gets the price category of this restaurant
     *
     * @return an integer between 1 and 5 that represents the price category of this restaurant
     */
    public int getPrice() {
        return price;
    }

    /**
     * Sets the price category of this school
     *
     * @param price is between 1 and 5
     */
    public void setPrice(int price) {
        if (price > 5 || price < 1) throw new IllegalArgumentException("Price is greater than 5 or less than 1");
        this.price = price;
    }

    /**
     * Compare two Business objects for equality
     *
     * @param other is not null
     * @return true if this Business and the other Business represent the same business.
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof Business && (this.getBusinessId().equals(((Business) other).getBusinessId()));
    }

    /**
     * Compute the hashCode for this Business object
     *
     * @return the hashCode for this Business object
     */
    @Override
    public int hashCode() {
        return (int) (getLatitude() * getLongitude());
    }

    /**
     * Gets the string representation of this restaurant
     *
     * @return the name of this restaurant
     */
    @Override
    public String toString() {
        return getName();
    }
}
