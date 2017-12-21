package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.yelp.YelpDb;
import ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurant;
import ca.ece.ubc.cpen221.mp5.yelp.YelpReview;
import ca.ece.ubc.cpen221.mp5.yelp.YelpUser;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class YelpDbTest {

    private static final YelpDb db = new YelpDb("data/restaurants.json", "data/reviews.json", "data/users.json");

    @Test
    public void test1() {
        // Test contents as well as getters and setters
        String output = db.toString();
        assertTrue(output.contains("es all over the walls about how the government is trying to oppres")
                && output.contains("Top Dog 2")
                && output.contains("Renata C."));

        YelpUser newUser = db.addUser("{\"name\": \"Sathish G.\"}");
        YelpRestaurant newRestaurant = db.addRestaurant("{\"name\": \"Sathish G.\"}");
        YelpReview newReview = db.addReview("{\"text\": \"Sathish G.\"}");

        List<YelpUser> users = db.getUsers();
        List<YelpReview> reviews = db.getReviews();
        List<YelpRestaurant> restaurants = db.getRestaurants();

        assertTrue(users.contains(newUser));
        assertTrue(restaurants.contains(newRestaurant));
        assertTrue(reviews.contains(newReview));

        assertEquals(db.getUser(newUser.getUserId()).getName(), "Sathish G.");
        assertEquals(db.getRestaurant(newRestaurant.getBusinessId()).getName(), "Sathish G.");
        assertEquals(db.getReview(newReview.getReviewId()).getText(), "Sathish G.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test2(){
        // Test illegal user
        YelpUser newUser = db.addUser("{\"nasa\": \"Sathish G.\"}");
        newUser.getAverageStars();
   }

    @Test(expected = IllegalArgumentException.class)
    public void test3(){
        // Test illegal restaurant
        YelpRestaurant newRestaurant = db.addRestaurant("{\"na23545me\": \"Sathish G.\"}");
        newRestaurant.setPrice(4);
   }

    @Test(expected = IllegalArgumentException.class)
    public void test4(){
        // Test illegal review
        YelpReview newReview = db.addReview("{\"tewesrdgtfxt\": \"Sathish G.\"}");
        newReview.setBusinessId("5325");
    }

    @Test
    public void test5(){
        // Test JSON String
        String json = db.kMeansClusters_json(40);

        // From https://stackoverflow.com/questions/767759/occurrences-of-substring-in-a-string
        int clusterIndex = 0;
        int count = 0;
        while(clusterIndex != -1){

            clusterIndex= json.indexOf("cluster",clusterIndex);

            if(clusterIndex!= -1){
                count ++;
                clusterIndex += "cluster".length();
            }
        }
        // 135 restaurants. Some delta if the db was mutated
        assertEquals(count, 135, 2);

        // Last instance of "cluster" should have a value of 39
        assertTrue(json.substring(json.lastIndexOf("cluster"), json.lastIndexOf("cluster") + 15).contains("39"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test6(){
        // Test existing review
        YelpReview newReview = db.addReview("{\"business_id\": \"1CBs84C-a-cuA3vncXVSAw\", \"review_id\": \"hbwE4C_uMpj95xay-e919g\", \"text\": \"Some test string Test\", \"stars\": 3, \"user_id\": \"90wm_01FAIqhcgV_mPON9Q\"}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test7(){
        // Test existing restaurant
        YelpRestaurant yelpRestaurant = db.addRestaurant("{\"business_id\": \"qHmamQPCAKkia9X0uryA8g\", \"longitude\": -122.5122, \"name\": \"Test Restaurant\", \"latitude\": 37.98541, \"state\": \"CA\", \"price\": 1}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test8(){
        // Test existing user
        YelpUser yelpUser = db.addUser("{\"user_id\": \"a8XQk3YbBKBgctzszG_3Ng\", \"name\": \"Sathish G.\"}");
    }

    @Test
    public void test9(){
        // Test some user ID
        YelpUser yelpUser = db.addUser("{\"user_id\": \"a8XQk3YbBKB2ctzszG_3Ng\", \"name\": \"Sathish G.\"}");
        assertEquals("http://www.yelp.com/user_details?userid=a8XQk3YbBKB2ctzszG_3Ng", yelpUser.getUrl());
    }
}
