package ca.ece.ubc.cpen221.mp5.yelp.deserlializers;

import ca.ece.ubc.cpen221.mp5.yelp.YelpReview;
import ca.ece.ubc.cpen221.mp5.yelp.YelpVotes;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Jackson deserializer for a YelpReview
 */
public class YelpReviewDeserializer extends JsonDeserializer<YelpReview> {

    // String constants for JSON fields
    private static final String USER_ID = "user_id";
    private static final String BUSINESS_ID = "business_id";
    private static final String VOTES = "votes";
    private static final String REVIEW_ID = "review_id";
    private static final String TEXT = "text";
    private static final String STARS = "stars";
    private static final String DATE = "date";

    /**
     * Deserialize JSON to YelpReview object
     * @param jp is not null. Contains fields specific to YelpReview
     * @param ctxt is not null
     * @return a YelpReview object initialized with the fields inside jp
     * @throws IOException if JsonParser is not in the correct format
     */
    public YelpReview deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        ObjectMapper mapper = new ObjectMapper();
        // Required to change non-ASCII characters
        mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);

        YelpReview review = new YelpReview(node.get(REVIEW_ID).asText());

        if(node.has(BUSINESS_ID)) review.setBusinessId(node.get(BUSINESS_ID).asText());
        if(node.has(DATE)) review.setDate(node.get(DATE).asText());
        if(node.has(STARS)) review.setStars(node.get(STARS).asInt());
        if(node.has(USER_ID)) review.setUserId(node.get(USER_ID).asText());
        if(node.has(TEXT)) review.setText(node.get(TEXT).asText());
        if(node.has(VOTES))  review.setVotes(mapper.readValue(node.get(VOTES).toString(), YelpVotes.class));

        return review;
    }
}
