package ca.ece.ubc.cpen221.mp5.yelp.deserlializers;

import ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurant;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JSON Deserializer for YelpRestaurant using Jackson
 */
public class YelpRestaurantDeserializer extends JsonDeserializer<YelpRestaurant> {

    // String constants for JSON fields
    private static final String BUSINESS_ID = "business_id";
    private static final String STARS = "stars";
    private static final String URL = "url";
    private static final String REVIEW_COUNT = "review_count";
    private static final String NAME = "name";
    private static final String OPEN = "open";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private static final String NEIGHBORHOODS = "neighborhoods";
    private static final String CATEGORIES = "categories";
    private static final String CITY = "city";
    private static final String ADDRESS = "full_address";
    private static final String PHOTO_URL = "photo_url";
    private static final String SCHOOLS = "schools";
    private static final String PRICE = "price";
    private static final String STATE = "state";

    /**
     * Deserialize JSON to YelpRestaurant object
     * @param jp is not null. Contains fields specific to YelpRestaurant
     * @param ctxt is not null
     * @return a YelpRestaurant object initialized with the fields inside jp
     * @throws IOException if JsonParser is not in the correct format
     */
    public YelpRestaurant deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        YelpRestaurant restaurant = new YelpRestaurant(node.get(BUSINESS_ID).asText());
        if(node.has(OPEN)) restaurant.setOpen(node.get(OPEN).asBoolean());
        if(node.has(URL))  restaurant.setUrl(node.get(URL).asText());
        if(node.has(LATITUDE)) restaurant.setLatitude(node.get(LATITUDE).asDouble());
        if(node.has(LONGITUDE)) restaurant.setLongitude(node.get(LONGITUDE).asDouble());
        if(node.has(NAME)) restaurant.setName(node.get(NAME).asText());
        if(node.has(REVIEW_COUNT))  restaurant.setReviewCount(node.get(REVIEW_COUNT).asInt());
        if(node.has(PHOTO_URL)) restaurant.setPhotoUrl(node.get(PHOTO_URL).asText());
        if(node.has(STARS)) restaurant.setStars(node.get(STARS).asDouble());
        if(node.has(CITY)) restaurant.setCity(node.get(CITY).asText());
        if(node.has(ADDRESS)) restaurant.setAddress(node.get(ADDRESS).asText());
        if(node.has(PRICE)) restaurant.setPrice(node.get(PRICE).asInt());
        if(node.has(STATE)) restaurant.setState(node.get(STATE).asText());

        ObjectMapper mapper = new ObjectMapper();
        // Use to String to allow ObjectMapper to create lists
        if(node.has(SCHOOLS)) restaurant.setSchools(mapper.readValue(node.get(SCHOOLS).toString(), new TypeReference<ArrayList<String>>(){}));
        if(node.has(CATEGORIES))  restaurant.setCategories(mapper.readValue(node.get(CATEGORIES).toString(), new TypeReference<ArrayList<String>>(){}));
        if(node.has(NEIGHBORHOODS)) restaurant.setNeighborhoods(mapper.readValue(node.get(NEIGHBORHOODS).toString(), new TypeReference<ArrayList<String>>(){}));

        return restaurant;
    }
}
