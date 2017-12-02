package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.interfaces.Votes;
import ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurant;
import ca.ece.ubc.cpen221.mp5.yelp.YelpReview;
import ca.ece.ubc.cpen221.mp5.yelp.YelpUser;
import ca.ece.ubc.cpen221.mp5.yelp.YelpVotes;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class YelpJsonTest {

    @Test
    public void test1(){
        // Test YelpUser object
        String json = "{\"url\": \"http://www.yelp.com/user_details?userid=_NH7Cpq3qZkByP5xR4gXog\", \"votes\": {\"funny\": 35, \"useful\": 21, \"cool\": 14}, " +
                "\"review_count\": 29, \"type\": \"user\", \"user_id\": \"_NH7Cpq3qZkByP5xR4gXog\", \"name\": \"Chris M.\", \"average_stars\": 3.89655172413793}\n";
        ObjectMapper mapper = new ObjectMapper();

        try{
            YelpUser user = mapper.readValue(json, YelpUser.class);
            YelpVotes votes = new YelpVotes();
            votes.setUseful(21);
            votes.setFunny(35);
            votes.setCool(14);

            assertEquals("http://www.yelp.com/user_details?userid=_NH7Cpq3qZkByP5xR4gXog",user.getWebsite());
            assertEquals(votes, user.getVotes());
            assertEquals(29, user.getReviewCount());
            assertEquals("user", user.getType());
            assertEquals("_NH7Cpq3qZkByP5xR4gXog", user.getUserId());
            assertEquals("Chris M.",user.getName());
            assertEquals(Math.round(3.89655172413793) , Math.round(user.getAverageStars()));
            assertTrue(user.toString().contains("Chris"));
        } catch (Exception e){
            fail(e.getMessage());
        }

    }

    @Test
    public void test2(){
        // Test YelpRestaurant object
        String json = "{\"open\": true, \"url\": \"http://www.yelp.com/biz/jasmine-thai-berkeley\", \"longitude\": -122.2602981, \"neighborhoods\": [\"UC Campus Area\"], " +
                "\"business_id\": \"BJKIoQa5N2T_oDlLVf467Q\", \"name\": \"Jasmine Thai\", \"categories\": [\"Thai\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\"," +
                " \"stars\": 3.0, \"city\": \"Berkeley\", \"full_address\": \"1805 Euclid Ave\\nUC Campus Area\\nBerkeley, CA 94709\", \"review_count\": 52, \"photo_url\": " +
                "\"http://s3-media2.ak.yelpcdn.com/bphoto/ZwTUUb-6jkuzMDBBsUV6Eg/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.8759615, \"price\": 2}\n";
        ObjectMapper mapper = new ObjectMapper();

        try{
            YelpRestaurant restaurant = mapper.readValue(json, YelpRestaurant.class);

            assertEquals(true,restaurant.isOpen());
            assertEquals("http://www.yelp.com/biz/jasmine-thai-berkeley", restaurant.getUrl());
            assertEquals(-122.2602981d, restaurant.getLongitude(), 0.001);
            assertEquals("UC Campus Area",restaurant.getNeighborhoods().get(0));
            assertEquals("BJKIoQa5N2T_oDlLVf467Q", restaurant.getBusinessId());
            assertEquals("Jasmine Thai", restaurant.getName());
            assertEquals("Thai", restaurant.getCategories().get(0));
            assertEquals("CA", restaurant.getState());
            assertEquals("business", restaurant.getType());
            assertEquals(3.0d, restaurant.getStars(), 0.1);
            assertEquals("Berkeley", restaurant.getCity());
            assertTrue(restaurant.getAddress().contains("1805 Euclid Ave"));
            assertEquals(52, restaurant.getReviewCount());
            assertEquals("http://s3-media2.ak.yelpcdn.com/bphoto/ZwTUUb-6jkuzMDBBsUV6Eg/ms.jpg", restaurant.getPhotoUrl());
            assertEquals("University of California at Berkeley", restaurant.getSchools().get(0));
            assertEquals(37.8759615, restaurant.getLatitude(), 0.1);
            assertEquals(2, restaurant.getPrice());
            assertEquals(restaurant.toString(), "Jasmine Thai");
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void test3(){
        // Test YelpRestaurant object
        String json = "{\"type\": \"review\", \"business_id\": \"1CBs84C-a-cuA3vncXVSAw\", \"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, " +
                "\"review_id\": \"0a-pCW4guXIlWNpVeBHChg\", \"text\": \"The pizza is terrible, but if you need a place to watch a game or just down some pitchers, " +
                "this place works.\\n\\nOh, and the pasta is even worse than the pizza.\", \"stars\": 2, \"user_id\": \"90wm_01FAIqhcgV_mPON9Q\", \"date\": \"2006-07-26\"}\n";
        ObjectMapper mapper = new ObjectMapper();

        try{
            YelpReview review = mapper.readValue(json, YelpReview.class);

            YelpVotes votes = new YelpVotes();
            votes.setUseful(0);
            votes.setFunny(0);
            votes.setCool(0);

            assertEquals("review", review.getType());
            assertEquals("1CBs84C-a-cuA3vncXVSAw", review.getBusinessId());
            assertEquals(votes, review.getVotes());
            assertEquals("0a-pCW4guXIlWNpVeBHChg", review.getReviewId());
            assertTrue(review.getText().contains("The pizza is terrible"));
            assertEquals(2, review.getStars());
            assertEquals("90wm_01FAIqhcgV_mPON9Q", review.getUserId());
            assertEquals("2006-07-26", review.getDate());
            assertTrue(review.toString().contains("pasta is even worse"));

        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void test4(){
        // Test object equality
        String user = "{\"url\": \"http://www.yelp.com/user_details?userid=_NH7Cpq3qZkByP5xR4gXog\", \"votes\": {\"funny\": 35, \"useful\": 21, \"cool\": 14}, " +
                "\"review_count\": 29, \"type\": \"user\", \"user_id\": \"_NH7Cpq3qZkByP5xR4gXog\", \"name\": \"Chris M.\", \"average_stars\": 3.89655172413793}\n";
        String review = "{\"type\": \"review\", \"business_id\": \"1CBs84C-a-cuA3vncXVSAw\", \"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, " +
                "\"review_id\": \"0a-pCW4guXIlWNpVeBHChg\", \"text\": \"The pizza is terrible, but if you need a place to watch a game or just down some pitchers, " +
                "this place works.\\n\\nOh, and the pasta is even worse than the pizza.\", \"stars\": 2, \"user_id\": \"90wm_01FAIqhcgV_mPON9Q\", \"date\": \"2006-07-26\"}\n";
        String restaurant = "{\"open\": true, \"url\": \"http://www.yelp.com/biz/jasmine-thai-berkeley\", \"longitude\": -122.2602981, \"neighborhoods\": [\"UC Campus Area\"], " +
                "\"business_id\": \"BJKIoQa5N2T_oDlLVf467Q\", \"name\": \"Jasmine Thai\", \"categories\": [\"Thai\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\"," +
                " \"stars\": 3.0, \"city\": \"Berkeley\", \"full_address\": \"1805 Euclid Ave\\nUC Campus Area\\nBerkeley, CA 94709\", \"review_count\": 52, \"photo_url\": " +
                "\"http://s3-media2.ak.yelpcdn.com/bphoto/ZwTUUb-6jkuzMDBBsUV6Eg/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.8759615, \"price\": 2}\n";
        ObjectMapper mapper = new ObjectMapper();

        try{
            YelpReview yelpReview = mapper.readValue(review, YelpReview.class);
            YelpRestaurant yelpRestaurant = mapper.readValue(restaurant, YelpRestaurant.class);
            YelpUser yelpUser = mapper.readValue(user, YelpUser.class);

            assertFalse(yelpReview.equals(yelpRestaurant));

            assertTrue(yelpReview.equals(mapper.readValue(review, YelpReview.class)));
            assertEquals(yelpReview.hashCode(), (mapper.readValue(review, YelpReview.class).hashCode()));

            assertTrue(yelpRestaurant.equals(mapper.readValue(restaurant, YelpRestaurant.class)));
            assertEquals(yelpRestaurant.hashCode(), (mapper.readValue(restaurant, YelpRestaurant.class).hashCode()));

            assertTrue(yelpUser.equals(mapper.readValue(user, YelpUser.class)));
            assertEquals(yelpUser.hashCode(), (mapper.readValue(user, YelpUser.class).hashCode()));
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void test5(){
        // Test invalid json. Should throw a JsonParseException
        String json = "{\"url\": \"http://www.yelp.com/us funny\": 35, \"useful\": 21, \"cool\": 14}, " +
                "\"review_count\": 2: 3.89655172413793}\n";
        ObjectMapper mapper = new ObjectMapper();
        try {
            YelpUser user = mapper.readValue(json, YelpUser.class);
        } catch (IOException e){
            assertTrue(e.getClass().equals(JsonParseException.class));
        }
    }

    @Test
    public void test6(){
        // Test Vote equality and more

        String user = "{\"url\": \"http://www.yelp.com/user_details?userid=_NH7Cpq3qZkByP5xR4gXog\", \"votes\": {\"funny\": 35, \"useful\": 21, \"cool\": 14}, " +
                "\"review_count\": 29, \"type\": \"user\", \"user_id\": \"_NH7Cpq3qZkByP5xR4gXog\", \"name\": \"Chris M.\", \"average_stars\": 3.89655172413793}\n";
        String review = "{\"type\": \"review\", \"business_id\": \"1CBs84C-a-cuA3vncXVSAw\", \"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, " +
                "\"review_id\": \"0a-pCW4guXIlWNpVeBHChg\", \"text\": \"The pizza is terrible, but if you need a place to watch a game or just down some pitchers, " +
                "this place works.\\n\\nOh, and the pasta is even worse than the pizza.\", \"stars\": 2, \"user_id\": \"90wm_01FAIqhcgV_mPON9Q\", \"date\": \"2006-07-26\"}\n";

        ObjectMapper mapper = new ObjectMapper();
        try {
            YelpReview yelpReview = mapper.readValue(review, YelpReview.class);
            YelpUser yelpUser = mapper.readValue(user, YelpUser.class);

            Votes userVotes = yelpUser.getVotes();
            Votes reviewVotes = yelpReview.getVotes();

            assertTrue(userVotes.getTotalVotes() > reviewVotes.getTotalVotes());

            assertFalse(userVotes.equals(reviewVotes));

            assertTrue(userVotes.toString().contains("35"));
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

}
