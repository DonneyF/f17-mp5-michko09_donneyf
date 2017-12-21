package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.statistics.KMeans;
import ca.ece.ubc.cpen221.mp5.yelp.YelpDb;
import ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class KMeansTest {

    private static final YelpDb db = new YelpDb("data/restaurants.json", "data/reviews.json", "data/users.json");

    @Test
    public void test1(){
        // Test regular case of 40 clusters
        ArrayList<YelpRestaurant> list = new ArrayList<>(db.getRestaurants());
        KMeans kMeans = new KMeans(new ArrayList<>(list), 40);

        // Get clusters, containing a list of Maps representing each restaurant.
        List<Map<String, Object>> result = kMeans.toList();

        List<Point2D> centroids = kMeans.getCentroids();

        for(Map<String, Object> restaurant : result){
            Point2D restaurantLocation = new Point2D.Double((Double)restaurant.get("x"), (Double)restaurant.get("y"));
            Point2D restaurantCentroid = (Point2D) restaurant.get("centroid");
            // Distance from this restaurant to any other centroid must be larger than the distance from this restauraunt to
            // its cluster's centroid
            for(Point2D centroid : centroids){
                if (!centroid.equals(restaurantCentroid)) {
                    assertTrue(centroid.distance(restaurantLocation) > restaurantCentroid.distance(restaurantCentroid));
                }
            }
        }
    }

    @Test
    public void test2(){
        // Edge case: number of clusters is equal to the number of restaurants
        ArrayList<YelpRestaurant> list = new ArrayList<>(db.getRestaurants());
        KMeans kMeans = new KMeans(new ArrayList<>(list), list.size());

        // Get clusters, containing a list of Maps representing each restaurant.
        List<Map<String, Object>> result = kMeans.toList();

        List<Point2D> centroids = kMeans.getCentroids();

        for(Map<String, Object> restaurant : result){
            Point2D restaurantLocation = new Point2D.Double((Double)restaurant.get("x"), (Double)restaurant.get("y"));
            Point2D restaurantCentroid = (Point2D) restaurant.get("centroid");
            // Distance from this restaurant to any other centroid must be larger than the distance from this restauraunt to
            // its cluster's centroid
            for(Point2D centroid : centroids){
                if (!centroid.equals(restaurantCentroid)) {
                    assertTrue(centroid.distance(restaurantLocation) > restaurantCentroid.distance(restaurantCentroid));
                }
            }
        }
    }

    @Test
    public void test3(){
        // Ensure JSON results are of correct size
        ArrayList<YelpRestaurant> list = new ArrayList<>(db.getRestaurants());
        KMeans kMeans = new KMeans(new ArrayList<>(list), 40);

        // Get clusters, containing a list of Maps representing each restaurant.
        try {
            kMeans.toFile("data/results.json");
            File file = new File("data/results.json");

            List<Map<String, Object>> results = new ObjectMapper().readValue(file, new TypeReference<List<Map>>(){});
            // No clusters equal to or greater than 40, since clusters start numbering at 0
            for (Map<String, Object> map : results) {
                    assertTrue((Integer) map.get("cluster") < 40);
            }
        } catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
