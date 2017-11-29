package ca.ece.ubc.cpen221.mp5.yelp.deserlializers;

import ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurant;
import ca.ece.ubc.cpen221.mp5.yelp.YelpReview;
import ca.ece.ubc.cpen221.mp5.yelp.YelpUser;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

public class YelpRestaurantDeserializer extends JsonDeserializer<YelpRestaurant> {

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

    public YelpRestaurant deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);

        YelpRestaurant restaurant = new YelpRestaurant();
        restaurant.setOpen(node.get(OPEN).asBoolean());
        restaurant.setUrl(node.get(URL).asText());
        restaurant.setLatitude(node.get(LATITUDE).asDouble());
        restaurant.setLongitude(node.get(LONGITUDE).asDouble());
        restaurant.setName(node.get(NAME).asText());
        restaurant.setBusinessId(node.get(BUSINESS_ID).asText());
        restaurant.setReviewCount(node.get(REVIEW_COUNT).asInt());
        restaurant.setPhotoUrl(node.get(PHOTO_URL).asText());
        restaurant.setStars(node.get(STARS).asDouble());
        restaurant.setCity(node.get(CITY).asText());
        restaurant.setAddress(node.get(ADDRESS).asText());
        restaurant.setPrice(node.get(PRICE).asInt());

        ObjectMapper mapper = new ObjectMapper();
        // Use to String to allow ObjectMapper to create lists
        restaurant.setSchools(mapper.readValue(node.get(SCHOOLS).toString(), new TypeReference<ArrayList<String>>(){}));
        restaurant.setCategories(mapper.readValue(node.get(CATEGORIES).toString(), new TypeReference<ArrayList<String>>(){}));
        restaurant.setNeighborhoods(mapper.readValue(node.get(NEIGHBORHOODS).toString(), new TypeReference<ArrayList<String>>(){}));

        return restaurant;
    }
}
