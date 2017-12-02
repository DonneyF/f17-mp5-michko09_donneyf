package ca.ece.ubc.cpen221.mp5.yelp;

import ca.ece.ubc.cpen221.mp5.MP5Database;
import ca.ece.ubc.cpen221.mp5.statistics.KMeans;
import ca.ece.ubc.cpen221.mp5.statistics.LeastSquares;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.Collectors;

public class YelpDb extends MP5Database<YelpRestaurant> {

    // Each map contains a the unique ID of the element and corresponding object
    private final Map<String, YelpUser> users;
    private final Map<String, YelpRestaurant> restaurants;
    private final Map<String, YelpReview> reviews;
    private final List<String> IDs;
    private static ObjectNode genericUser;
    private static ObjectNode genericRestaurant;
    private static ObjectNode genericReview;

    // Default user JSON objects
    static {
        String defaultUser = "{\"url\": \"http://www.yelp.com/\", \"votes\": {}, \"review_count\": 0, \"type\": \"user\", \"user_id\": \"42\", \"name\": \"John Doe\", \"average_stars\": 0}";

        String defaultRestaurant = "{\"open\": true, \"url\": \"http://www.yelp.com/\", \"neighborhoods\": [], \"business_id\": \"gclB3ED6uk6viWlolSb_uA\", \"name\": \"Cafe 3\", \"categories\": [], \"type\": \"business\", \"review_count\": 0, \"schools\": []}";

        String defaultReview = "{\"type\": \"review\", \"votes\": {}, \"review_id\": \"0a-pCW4guXIlWNpVeBHChg\", \"text\": \"Some text\"}";

        ObjectMapper mapper = new ObjectMapper();
        try {
            genericUser = (ObjectNode) mapper.readTree(defaultUser);
            genericRestaurant = (ObjectNode) mapper.readTree(defaultRestaurant);
            genericReview = (ObjectNode) mapper.readTree(defaultReview);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public YelpDb(String restaurantFileName, String reviewsFileName, String usersFileName) {
        users = parseJSON(usersFileName, YelpUser.class);
        restaurants = parseJSON(restaurantFileName, YelpRestaurant.class);
        reviews = parseJSON(reviewsFileName, YelpReview.class);

        // Get a list of unique IDs.
        IDs = restaurants.values().parallelStream().map(YelpRestaurant::getBusinessId).collect(Collectors.toList());
        IDs.addAll(reviews.values().parallelStream().map(YelpReview::getReviewId).collect(Collectors.toList()));
        IDs.addAll(users.values().parallelStream().map(YelpUser::getUserId).collect(Collectors.toList()));

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
            return null;
        }
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

    public YelpUser addUser(String userJson){
        ObjectNode user = genericUser.deepCopy();

        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        try {
            // Verify JSON by adding to the ObjectNode. Copy statements from input to generic and set values
            ObjectNode input = (ObjectNode) mapper.readTree(userJson);
            // Get a random ID
            String id = generateRandomID();
            // Parse user ID and url. Input must contain business name
            if (!input.toString().contains("name")) throw new IllegalArgumentException();
            input.put("user_id", id);
            user.setAll(input);

            YelpUser yelpUser = new ObjectMapper().readValue(input.toString(), YelpUser.class);
            users.put(id, yelpUser);

            return yelpUser;

        } catch (Exception e){
            throw new IllegalArgumentException("Input string not valid. Missing \"name\" field");
        }
    }

    public YelpRestaurant addRestaurant(String restaurantJson){
        ObjectNode restaurant = genericRestaurant.deepCopy();

        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        try {
            // Verify JSON by adding to the ObjectNode. Copy statements from input to generic and set values
            ObjectNode input = (ObjectNode) mapper.readTree(restaurantJson);
            // Get a random ID
            String id = generateRandomID();
            // Parse user ID and url. Input must contain business name
            if (!input.toString().contains("name")) throw new IllegalArgumentException();
            input.put("business_id", id);
            restaurant.setAll(input);

            YelpRestaurant yelpRestaurant = new ObjectMapper().readValue(input.toString(), YelpRestaurant.class);
            restaurants.put(id, yelpRestaurant);

            return yelpRestaurant;

        } catch (Exception e) {
            throw new IllegalArgumentException("Input string not valid. Missing \"name\" field");
        }
    }

    public YelpReview addReview(String reviewJson){
        ObjectNode review = genericReview.deepCopy();

        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        try {
            // Verify JSON by adding to the ObjectNode. Copy statements from input to generic and set values
            ObjectNode input = (ObjectNode) mapper.readTree(reviewJson);
            // Get a random ID
            String id = generateRandomID();
            // Parse user ID and url. Input must contain business name
            if (!input.toString().contains("text")) throw new IllegalArgumentException();
            input.put("review_id", id);
            review.setAll(input);

            YelpReview yelpReview = new ObjectMapper().readValue(input.toString(), YelpReview.class);
            reviews.put(id, yelpReview);

            return yelpReview;

        } catch (Exception e){
            throw new IllegalArgumentException("Input string not valid. Missing \"text\" field");
        }
    }

    public Set getMatches(String queryString) {
        return null;
    }

    public String kMeansClusters_json(int k) {
       return new KMeans(getRestaurants(), k).toJson();
    }

    public ToDoubleBiFunction<YelpDb, String> getPredictorFunction(String user) {
        LeastSquares leastSquares = new LeastSquares(this);
        return leastSquares.getPredictorFunction(user);
    }

    private String generateRandomID() {
        String potentialID;
        do {
            potentialID = UUID.randomUUID().toString().replaceAll("[\\-]", "").substring(0, 22);
        } while (IDs.contains(potentialID));

        return potentialID;
    }
}
