package ca.ece.ubc.cpen221.mp5.yelp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class YelpRestaurantQuery {

    private Double rating;
    private String ratingEquality;
    private Integer price;
    private String priceEquality;
    private String category;
    private String neighborhood;
    private String name;

    public Double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getRatingEquality() {
        return ratingEquality;
    }

    public void setRatingEquality(String ratingEquality) {
        this.ratingEquality = ratingEquality;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPriceEquality() {
        return priceEquality;
    }

    public void setPriceEquality(String priceEquality) {
        this.priceEquality = priceEquality;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasPriceFilter() {
        return priceEquality != null;
    }

    public boolean hasRatingFilter() {
        return ratingEquality != null;
    }

    public boolean hasNeighborhoodFilter() {
        return neighborhood != null;
    }

    public boolean hasCategoryFilter() {
        return category != null;
    }

    public boolean hasNameFilter() {
        return name != null;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this).replaceAll(System.lineSeparator(), "")
                    .replaceAll(",", ", ").replaceAll("\":", "\": ");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
