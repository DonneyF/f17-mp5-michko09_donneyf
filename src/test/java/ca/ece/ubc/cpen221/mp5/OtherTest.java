package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.yelp.YelpDb;
import ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurant;
import ca.ece.ubc.cpen221.mp5.yelp.YelpVotes;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.io.*;
import java.util.UUID;

import static org.junit.Assert.*;

public class OtherTest {

    @Test
    public void test2() {

        try {
            // ObjectMapper test
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
    public void test4() {
        // Testing entire string parse test with default JSON
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
                    replaceAll("\\s", "-").replaceAll("[^a-zA-Z0-9\\\\s\\-]", "").toLowerCase());
            restaurant.setAll(input);
            assertEquals(restaurant.get("name").asText(), "Sathish G.");
            assertFalse(restaurant.get("open").asBoolean());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test5() {
        // ID generator
        YelpDb db = new YelpDb("data/restaurants.json", "data/reviews.json", "data/users.json");

        String id = UUID.randomUUID().toString().replaceAll("[\\-]", "").substring(0, 22);

        assertTrue(db.getRestaurant(id) == null);
        assertTrue(db.getUser(id) == null);
        assertTrue(db.getReview(id) == null);
    }

    @Test
    public void test6() {
        YelpVotes yelpVotes1 = new YelpVotes();
        YelpVotes yelpVotes2 = new YelpVotes();

        yelpVotes1.setCool(5);
        yelpVotes2.setCool(3);

        assertFalse(yelpVotes1.equals(yelpVotes2));
        assertFalse(yelpVotes1.hashCode() == yelpVotes2.hashCode());
    }

    @Test
    public void test7() {
        String port = "458\n";
        // Use a PrintStream to contain all console outputs
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(out));
        String[] emptyString = {};

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.setIn(new ByteArrayInputStream(port.getBytes()));
                YelpDBServer.main(emptyString);
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.setIn(new ByteArrayInputStream(port.getBytes()));
                YelpClient.main(emptyString);
            }
        });

        try {
            t1.start();
            Thread.sleep(4000);
            t2.start();
            Thread.sleep(500);
            t2.interrupt();
            t1.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.setErr(System.err);
            System.setOut(System.out);
        }
    }

}
