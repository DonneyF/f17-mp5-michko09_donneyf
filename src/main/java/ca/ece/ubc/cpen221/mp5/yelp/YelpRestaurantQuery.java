package ca.ece.ubc.cpen221.mp5.yelp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A data type that stores Yelp Restaurant query information
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class YelpRestaurantQuery {

    /*
     * Representation Invariant:
     *      - If ratingEquality or priceEquality is not null, then it must be either ">", "<" "=", "<=", or ">=".
     *      - rating and price must be greater than 1 and less than 5.
     *
     * Abstraction Function:
     *      AF(this) -> A set of restrictions placed on a set of YelpRestaurants
     */

    private Double rating;
    private String ratingEquality;
    private Integer price;
    private String priceEquality;
    private String category;
    private String neighborhood;
    private String name;

    /**
     * Get the rating this query should filter
     * @return a double between 1 and 5
     */
    public Double getRating() {
        return rating;
    }

    /**
     * Set the rating this query should filter
     * @param rating is between 1 and 5
     */
    public void setRating(double rating) {
        if (rating > 5 || rating < 1) throw new IllegalArgumentException("Rating is greater than 5 or less than 1");
        this.rating = rating;
    }

    /**
     * Get the equality associated with this query's rating filter
     * @return one of ">", "<" "=", "<=", or ">=".
     */
    public String getRatingEquality() {
        return ratingEquality;
    }

    /**
     * Set the equality associated with this query's rating filter
     * @param ratingEquality is one of ">", "<" "=", "<=", or ">=".
     */
    public void setRatingEquality(String ratingEquality) {
        this.ratingEquality = ratingEquality;
    }

    /**
     * Get the price this query should filter
     * @return a number between 1 and 5
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * Set the price this query should filter
     * @param price a number between 1 and 5
     */
    public void setPrice(int price) {
        if (price > 5 || price < 1) throw new IllegalArgumentException("Price is greater than 5 or less than 1");
        this.price = price;
    }

    /**
     * Set the equality associated with this query's price filter
     * @return one of ">", "<" "=", "<=", or ">=".
     */
    public String getPriceEquality() {
        return priceEquality;
    }

    /**
     * Set the equality associated with this query's price filter
     * @param priceEquality is one of ">", "<" "=", "<=", or ">=".
     */
    public void setPriceEquality(String priceEquality) {
        this.priceEquality = priceEquality;
    }

    /**
     * Get the category this query should filter
     * @return a category string
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set the category this query should filter
     * @param category is not null
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Get the neighborhood this query should filter
     * @return a neighborhood string
     */
    public String getNeighborhood() {
        return neighborhood;
    }

    /**
     * Set the neighborhood this query should filter
     * @param neighborhood is not null
     */
    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    /**
     * Get the name of the restaurant this query should filter
     * @return a name string
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the restaurant this query should filter
     * @param name is not null
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Checks if this query has a filter for the price
     * @return true if the query has a filter for the price, false otherwise
     */
    public boolean hasPriceFilter() {
        return priceEquality != null;
    }

    /**
     * Checks if this query has a filter for the rating
     * @return true if the query has a filter for the rating, false otherwise
     */
    public boolean hasRatingFilter() {
        return ratingEquality != null;
    }

    /**
     * Checks if this query has a filter for the neighborhood
     * @return true if the query has a filter for the neighborhood, false otherwise
     */
    public boolean hasNeighborhoodFilter() {
        return neighborhood != null;
    }

    /**
     * Checks if this query has a filter for the category
     * @return true if the query has a filter for the category, false otherwise
     */
    public boolean hasCategoryFilter() {
        return category != null;
    }

    /**
     * Checks if this query has a filter for the restaurant name
     * @return true if the query has a filter for the restaurant name, false otherwise
     */
    public boolean hasNameFilter() {
        return name != null;
    }

    /**
     * Get the JSON string representation of this query
     * @return a string containing a key-value relationship between the queries and the value to filter
     */
    @Override
    public synchronized String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this).replaceAll(System.lineSeparator(), "")
                    .replaceAll(",", ", ").replaceAll("\":", "\": ");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
