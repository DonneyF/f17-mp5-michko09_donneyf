package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.yelp.YelpDb;
import ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurant;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.*;

public class OtherTest {

    @Test
    public void test2(){

        try {
            File file = new File("data/restaurants.json");

            BufferedReader br = new BufferedReader(new FileReader(file));

            ObjectMapper mapper = new ObjectMapper();
            String line = br.readLine();
            YelpRestaurant restaurant = mapper.readValue(line, YelpRestaurant.class);

            assertEquals("Cafe 3", restaurant.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test4(){
        String defaultRestaurant = "{\"open\": true, \"url\": \"http://www.yelp.com/\", \"neighborhoods\": [], " +
                "\"business_id\": \"gclB3ED6uk6viWlolSb_uA\", \"name\": \"Cafe 3\", \"categories\": [], \"type\": " +
                "\"business\", \"review_count\": 0, \"schools\": []}";

        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);

        String possibleJSONString = "{\"name\": \"Sathish G.\", \"open\": false}";

        try {
            ObjectNode genericRestaurant = (ObjectNode) mapper.readTree(defaultRestaurant);
            ObjectNode restaurant = genericRestaurant.deepCopy();
            // Verify JSON by adding to the ObjectNode. Copy statements from input to generic and set values
            ObjectNode input = (ObjectNode) mapper.readTree(possibleJSONString);
            input.put("url", "http://www.yelp.com/biz/" + input.get("name").asText().
                    replaceAll("\\s", "-").replaceAll("[^a-zA-Z0-9\\\\s\\-]","").toLowerCase());
            restaurant.setAll(input);
            assertEquals(restaurant.get("name").asText(), "Sathish G.");
            assertFalse(restaurant.get("open").asBoolean());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void test5(){
        YelpDb db = new YelpDb("data/restaurants.json", "data/reviews.json", "data/users.json");

        String id = UUID.randomUUID().toString().replaceAll("[\\-]","").substring(0, 22);

        assertTrue(db.getRestaurantData(id) == null);
        assertTrue(db.getUserData(id) == null);
        assertTrue(db.getReviewData(id) == null);
    }
}
