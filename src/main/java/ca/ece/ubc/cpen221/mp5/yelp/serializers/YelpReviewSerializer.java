package ca.ece.ubc.cpen221.mp5.yelp.serializers;

import ca.ece.ubc.cpen221.mp5.yelp.YelpReview;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class YelpReviewSerializer extends JsonSerializer<YelpReview> {

    private static final String USER_ID = "user_id";
    private static final String BUSINESS_ID = "business_id";
    private static final String VOTES = "votes";
    private static final String REVIEW_ID = "review_id";
    private static final String TEXT = "text";
    private static final String STARS = "stars";
    private static final String DATE = "date";
    private static final String TYPE = "type";

    @Override
    public void serialize(YelpReview review, JsonGenerator jgen, SerializerProvider provider) throws IOException {

        jgen.writeStartObject();
        jgen.writeStringField(TYPE, review.getType());
        jgen.writeStringField(BUSINESS_ID, review.getBusinessId());
        jgen.writeObjectField(VOTES, review.getVotes());
        jgen.writeStringField(REVIEW_ID, review.getReviewId());
        jgen.writeStringField(TEXT, review.getText());
        jgen.writeNumberField(STARS, review.getStars());
        jgen.writeStringField(USER_ID, review.getUserId());
        jgen.writeStringField(DATE, review.getDate());
        jgen.writeEndObject();
    }
}
