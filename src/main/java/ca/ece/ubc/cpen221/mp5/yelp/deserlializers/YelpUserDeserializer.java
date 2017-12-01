package ca.ece.ubc.cpen221.mp5.yelp.deserlializers;

import ca.ece.ubc.cpen221.mp5.yelp.YelpUser;
import ca.ece.ubc.cpen221.mp5.yelp.YelpVotes;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class YelpUserDeserializer extends JsonDeserializer<YelpUser> {

    private static final String USER_ID = "user_id";
    private static final String TYPE = "type";
    private static final String URL = "url";
    private static final String REVIEW_COUNT = "review_count";
    private static final String NAME = "name";
    private static final String AVERAGE_STARS = "average_stars";
    private static final String VOTES = "votes";

    public YelpUser deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);

        YelpUser user = new YelpUser();
        user.setWebsite(node.get(URL).asText());
        user.setType(node.get(TYPE).asText());
        user.setUserId(node.get(USER_ID).asText());
        user.setAverageStars(node.get(AVERAGE_STARS).asDouble());
        user.setName(node.get(NAME).asText());
        user.setReviewCount(node.get(REVIEW_COUNT).asInt());

        ObjectMapper mapper = new ObjectMapper();
        user.setVotes(mapper.readValue(node.get(VOTES).toString(), YelpVotes.class));

        return user;
    }
}
