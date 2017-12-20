package ca.ece.ubc.cpen221.mp5.yelp.serializers;

import ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurant;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class YelpRestaurantSerializer extends JsonSerializer<YelpRestaurant> {

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
    private static final String TYPE = "type";

    @Override
    public void serialize(YelpRestaurant restaurant, JsonGenerator jgen, SerializerProvider provider) throws IOException {

        jgen.writeStartObject();
        jgen.writeBooleanField(OPEN,restaurant.isOpen());
        jgen.writeStringField(URL,restaurant.getUrl());
        jgen.writeNumberField(LONGITUDE,restaurant.getLongitude());
        if(restaurant.getNeighborhoods() != null) jgen.writeObjectField(NEIGHBORHOODS,restaurant.getNeighborhoods());
        jgen.writeStringField(BUSINESS_ID,restaurant.getBusinessId());
        jgen.writeStringField(NAME,restaurant.getName());
        if (restaurant.getCategories() != null) jgen.writeObjectField(CATEGORIES,restaurant.getCategories());
        jgen.writeStringField(STATE,restaurant.getState());
        jgen.writeStringField(TYPE,restaurant.getType());
        jgen.writeNumberField(STARS,restaurant.getStars());
        jgen.writeStringField(CITY,restaurant.getCity());
        jgen.writeStringField(ADDRESS,restaurant.getAddress());
        jgen.writeNumberField(REVIEW_COUNT,restaurant.getReviewCount());
        jgen.writeStringField(PHOTO_URL,restaurant.getPhotoUrl());
        if (restaurant.getSchools() != null) jgen.writeStringField(SCHOOLS,restaurant.getSchools().toString());
        jgen.writeNumberField(LATITUDE,restaurant.getLatitude());
        jgen.writeNumberField(PRICE,restaurant.getPrice());
        jgen.writeEndObject();
    }
}
