package ca.ece.ubc.cpen221.mp5.yelp.serializers;

import ca.ece.ubc.cpen221.mp5.yelp.YelpUser;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class YelpUserSerializer extends JsonSerializer<YelpUser> {

    private static final String USER_ID = "user_id";
    private static final String URL = "url";
    private static final String REVIEW_COUNT = "review_count";
    private static final String NAME = "name";
    private static final String AVERAGE_STARS = "average_stars";
    private static final String VOTES = "votes";
    private static final String TYPE = "type";

    @Override
    public void serialize(YelpUser user, JsonGenerator jgen, SerializerProvider provider) throws IOException{

        jgen.writeStartObject();
        jgen.writeStringField(URL, user.getUrl());
        jgen.writeObjectField(VOTES,user.getVotes());
        jgen.writeNumberField(REVIEW_COUNT, user.getReviewCount());
        jgen.writeStringField(TYPE, user.getType());
        jgen.writeStringField(USER_ID, user.getUserId());
        jgen.writeStringField(NAME, user.getName());
        jgen.writeNumberField(AVERAGE_STARS, user.getAverageStars());
        jgen.writeEndObject();
    }
}
