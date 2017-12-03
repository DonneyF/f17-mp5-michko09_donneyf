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
   }

    @Test(expected = IllegalArgumentException.class)
    public void test3(){
        // Test illegal restaurant
        YelpRestaurant newRestaurant = db.addRestaurant("{\"na23545me\": \"Sathish G.\"}");
   }

    @Test(expected = IllegalArgumentException.class)
    public void test4(){
        // Test illegal review
        YelpReview newReview = db.addReview("{\"tewesrdgtfxt\": \"Sathish G.\"}");
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
}
