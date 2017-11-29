package ca.ece.ubc.cpen221.mp5.yelp;

import ca.ece.ubc.cpen221.mp5.interfaces.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class YelpDb {

    private List<User> users;
    private List<YelpRestaurant> restaurants;
    private List<Review> reviews;

    public YelpDb(String restaurantFileName, String reviewsFileName, String usersFileName) {
        users = parseJSON(usersFileName, YelpUser.class);
        restaurants = parseJSON(restaurantFileName, YelpRestaurant.class);
        reviews = parseJSON(reviewsFileName, YelpReview.class);
    }

    private List parseJSON(String filePath, Class<?> tclass) {
        List<Object> elements = new ArrayList<>();
        try {
            // Get the file name
            File file = new File(filePath);
            String line;
            // Read a the file and get a line
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                ObjectMapper mapper = new ObjectMapper();
                Object element = mapper.readValue(line, tclass);
                elements.add(element);
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

    public int size(){
        return reviews.size() + restaurants.size() + users.size();
    }

    public List<YelpRestaurant> getRestaurants() {
        return restaurants;
    }
    
    public List<Review> getReviews() {
    	return reviews;
    }
    
    public List<User> getUsers() {
    	return users;
    }
}
