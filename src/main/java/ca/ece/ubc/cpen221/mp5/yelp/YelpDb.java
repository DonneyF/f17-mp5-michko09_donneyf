package ca.ece.ubc.cpen221.mp5.yelp;


import ca.ece.ubc.cpen221.mp5.interfaces.MP5Database;
import ca.ece.ubc.cpen221.mp5.statistics.LeastSquares;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.function.ToDoubleBiFunction;

public class YelpDb extends MP5Database<YelpRestaurant> {

    // Each map contains a the unique ID of the element and corresponding object
    private final Map<String, YelpUser> users;
    private final Map<String, YelpRestaurant> restaurants;
    private final Map<String, YelpReview> reviews;

    public YelpDb(String restaurantFileName, String reviewsFileName, String usersFileName) {
        users = parseJSON(usersFileName, YelpUser.class);
        restaurants = parseJSON(restaurantFileName, YelpRestaurant.class);
        reviews = parseJSON(reviewsFileName, YelpReview.class);
    }

    private Map parseJSON(String filePath, Class<?> tclass) {
        Map<String, Object> elements = new HashMap<>();
        try {
            // Get the file name
            File file = new File(filePath);
            String line;
            // Read a the file and get a line
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                ObjectMapper mapper = new ObjectMapper();
                Object element = mapper.readValue(line, tclass);
                switch (tclass.getName()){
                    case "ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurant":
                        elements.put(((YelpRestaurant)element).getBusinessId(), element);
                        break;
                    case "ca.ece.ubc.cpen221.mp5.yelp.YelpUser":
                        elements.put(((YelpUser)element).getUserId(), element);
                        break;
                    case "ca.ece.ubc.cpen221.mp5.yelp.YelpReview":
                        elements.put(((YelpReview)element).getReviewId(), element);
                        break;
                }
            }
            return elements;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString(){
        return reviews.toString() + restaurants.toString() + users.toString();
    }

    public List<YelpRestaurant> getRestaurants() {
        return new LinkedList<>(restaurants.values());
    }
    
    public List<YelpReview> getReviews() {
        return new LinkedList<>(reviews.values());
    }
    
    public List<YelpUser> getUsers() {
        return new LinkedList<>(users.values());
    }

    public YelpUser getUserData(String userId){
        return users.get(userId);
    }

    public YelpRestaurant getRestaurantData(String restaurantId){
        return restaurants.get(restaurantId);
    }

    public YelpReview getReviewData(String reviewId){
        return reviews.get(reviewId);
    }

    public void addUser(String userJson){
        try {
            YelpUser user = new ObjectMapper().readValue(userJson, YelpUser.class);
            users.put(user.getUserId(), user);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addRestaurant(String restaurantJson){
        try {
            YelpRestaurant restaurant = new ObjectMapper().readValue(restaurantJson, YelpRestaurant.class);
            restaurants.put(restaurant.getBusinessId(), restaurant);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Set getMatches(String queryString) {
        return null;
    }

    public String kMeansClusters_json(int k) {
        return null;
    }

    public ToDoubleBiFunction<YelpDb, String> getPredictorFunction(String user) {
        LeastSquares leastSquares = new LeastSquares(this);
        return leastSquares.getPredictorFunction(user);
    }
}
