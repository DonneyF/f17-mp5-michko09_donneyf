package ca.ece.ubc.cpen221.mp5.yelp.deserlializers;

import ca.ece.ubc.cpen221.mp5.yelp.YelpReview;
import ca.ece.ubc.cpen221.mp5.yelp.YelpVotes;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class YelpReviewDeserializer extends JsonDeserializer<YelpReview> {

    private static final String USER_ID = "user_id";
    private static final String TYPE = "type";
    private static final String BUSINESS_ID = "business_id";
    private static final String VOTES = "votes";
    private static final String REVIEW_ID = "review_id";
    private static final String TEXT = "text";
    private static final String STARS = "stars";
    private static final String DATE = "date";

    public YelpReview deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        YelpReview review = new YelpReview(node.get(REVIEW_ID).asText());

        if(node.has(BUSINESS_ID)) review.setBusinessId(node.get(BUSINESS_ID).asText());
        if(node.has(DATE)) review.setDate(node.get(DATE).asText());
        if(node.has(STARS)) review.setStars(node.get(STARS).asInt());
        if(node.has(USER_ID)) review.setUserId(node.get(USER_ID).asText());
        if(node.has(TYPE)) review.setType(node.get(TYPE).asText());
        if(node.has(TEXT)) review.setText(node.get(TEXT).asText());

        ObjectMapper mapper = new ObjectMapper();
        if(node.has(VOTES))  review.setVotes(mapper.readValue(node.get(VOTES).toString(), YelpVotes.class));

        return review;
    }
}
