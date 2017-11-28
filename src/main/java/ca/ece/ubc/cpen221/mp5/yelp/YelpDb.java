package ca.ece.ubc.cpen221.mp5.yelp;

import ca.ece.ubc.cpen221.mp5.datastructure.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class YelpDb {

    private Set<User> users;
    private Set<Restaurant> restaurants;
    private Set<Review> reviews;

    public YelpDb(String restaurantFileName, String reviewsFileName, String usersFileName) {
        users = new HashSet<>();
        restaurants = new HashSet<>();
        reviews = new HashSet<>();

        for (Map map : parseJSON(restaurantFileName)) {
            Table<String> table = new Table((HashMap<String, String>) map);
            restaurants.add(new Restaurant(table));
        }

        for (Map map : parseJSON(reviewsFileName)) {
            Table<String> table = new Table((HashMap<String, String>) map);
            reviews.add(new Review(table));
            Votes votes = new Votes((HashMap) map.get("votes"));
            reviews.add(new Review(table, votes));
        }

        for (Map map : parseJSON(usersFileName)) {
            Table<String> table = new Table((HashMap<String, String>) map);
            Votes votes = new Votes((HashMap) map.get("votes"));
            users.add(new User(table, votes));
        }
    }

    private List<Map> parseJSON(String filePath) {
        List<Map> parsedMaps = new ArrayList<>();
        try {
            // Get the file name
            File file = new File(filePath);
            String line = null;
            // Read a the file and get a line
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> map = mapper.readValue(line, new TypeReference<Map<String, Object>>() {});
                parsedMaps.add(map);
            }
            return parsedMaps;
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

    public Set getRestaurants(){
        return restaurants;
    }
}
