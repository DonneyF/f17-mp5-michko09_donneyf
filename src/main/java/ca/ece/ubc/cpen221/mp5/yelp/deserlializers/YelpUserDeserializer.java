package ca.ece.ubc.cpen221.mp5.yelp.deserlializers;

import ca.ece.ubc.cpen221.mp5.yelp.YelpUser;
import ca.ece.ubc.cpen221.mp5.yelp.YelpVotes;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Jackson deserializer for YelpUser
 */
public class YelpUserDeserializer extends JsonDeserializer<YelpUser> {

    // String constants for json fields
    private static final String USER_ID = "user_id";
    private static final String URL = "url";
    private static final String REVIEW_COUNT = "review_count";
    private static final String NAME = "name";
    private static final String AVERAGE_STARS = "average_stars";
    private static final String VOTES = "votes";

    /**
     * Deserialize JSON to YelpReview object
     * @param jp is not null. Contains fields specific to YelpReview
     * @param ctxt is not null
     * @return a YelpReview object initialized with the fields inside jp
     * @throws IOException if JsonParser is not in the correct format
     */
    public YelpUser deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        ObjectMapper mapper = new ObjectMapper();

        YelpUser user = new YelpUser(node.get(USER_ID).asText());
        if(node.has(URL)) user.setUrl(node.get(URL).asText());
        if(node.has(AVERAGE_STARS)) user.setAverageStars(node.get(AVERAGE_STARS).asDouble());
        if(node.has(NAME)) user.setName(node.get(NAME).asText());
        if(node.has(REVIEW_COUNT)) user.setReviewCount(node.get(REVIEW_COUNT).asInt());

        if(node.has(VOTES)) user.setVotes(mapper.readValue(node.get(VOTES).toString(), YelpVotes.class));

        return user;
    }
}
