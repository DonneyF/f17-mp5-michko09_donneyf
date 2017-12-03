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

/**
 * A Yelp database that stores information of restaurants, its reviews, and the users visiting these restaurants
 *
 * Representation Invariant:
 *      - users, restaurant, reviews, is not null
 *      - Each value in users, reviews, and restaurants contains its key in its unique ID
 *
 * Abstraction Function:
 *      - AF(this) -> a collection of YelpRestaurants, YelpReviews, and YelpUsers such that all objects are unique
 */
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

    /**
     * Constructs a YelpDb object
     *
     * @param restaurantFileName is not null
     * @param reviewsFileName is not null
     * @param usersFileName is not null
     */
    public YelpDb(String restaurantFileName, String reviewsFileName, String usersFileName) {
        users = parseJSON(usersFileName, YelpUser.class);
        restaurants = parseJSON(restaurantFileName, YelpRestaurant.class);
        reviews = parseJSON(reviewsFileName, YelpReview.class);

        // Get a list of unique IDs.
        IDs = restaurants.values().parallelStream().map(YelpRestaurant::getBusinessId).collect(Collectors.toList());
        IDs.addAll(reviews.values().parallelStream().map(YelpReview::getReviewId).collect(Collectors.toList()));
        IDs.addAll(users.values().parallelStream().map(YelpUser::getUserId).collect(Collectors.toList()));

    }

    /**
     * Parses a file and returns a map of objects parsed by JSON
     *
     * @param filePath is not null and contains information of either a set of YelpRestaurants, YelpReviews, or YelpUsers
     * @param tclass the class of either YelpRestaurants, YelpReviews, or YelpUsers
     * @return A map of Strings to Objects where the String is the unique identifier of YelpRestaurants, YelpReviews, or YelpUsers,
     *          and Objects contain YelpRestaurants, YelpReviews, or YelpUsers, respectively
     */
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

    /**
     * Gets the string representation of this database
     *
     * @return the string representation of the set of YelpRestaurants, YelpReviews, and YelpUsers
     */
    @Override
    public String toString(){
        return restaurants.toString() + reviews.toString() + users.toString();
    }

    /**
     * Gets the YelpRestaurants stored in this database
     *
     * @return a List of YelpRestaurants
     */
    public List<YelpRestaurant> getRestaurants() {
        return new LinkedList<>(restaurants.values());
    }

    /**
     * Gets the YelpReviews stored in this database
     *
     * @return a List of YelpReviews
     */
    public List<YelpReview> getReviews() {
        return new LinkedList<>(reviews.values());
    }

    /**
     * Gets the YelpUsers stored in this database
     *
     * @return a List of YelpUsers
     */
    public List<YelpUser> getUsers() {
        return new LinkedList<>(users.values());
    }

    /**
     * Gets a user from this database
     *
     * @param userId represents a user stored in this database
     * @return a YelpUser that contains userId
     */
    public YelpUser getUser(String userId){
        return users.get(userId);
    }

    /**
     * Gets a restaurant from this database
     *
     * @param restaurantId represents a restaurant stored in this database
     * @return a YelpRestaurant that contains restaurantId
     */
    public YelpRestaurant getRestaurant(String restaurantId){
        return restaurants.get(restaurantId);
    }

    /**
     * Gets a review from this database
     *
     * @param reviewId represents a review stored in this database
     * @return a YelpReview that contains reviewId
     */
    public YelpReview getReview(String reviewId){
        return reviews.get(reviewId);
    }

    /**
     * Add a user to this database
     *
     * @param userJson is a String of JSON containing user data. userJson must contain an entry for "name".
     * @return the YelpUser created by this method with fields defaulted.
     */
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

    /**
     * Add a restaurant to this database
     *
     * @param restaurantJson is a String of JSON containing restaurant data. userJson must contain an entry for "name".
     * @return the YelpRestaurant created by this method with fields defaulted.
     */
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

    /**
     * Add a review to this database
     *
     * @param reviewJson is a String of JSON containing review data. userJson must contain an entry for "text".
     * @return the YelpReview created by this method with fields defaulted.
     */
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

    /**
     * Perform a structured query and return the set of objects that matches the
     * query
     *
     * @param queryString is not null
     * @return the set of objects that matches the query
     */
    public Set getMatches(String queryString) {
        return null;
    }

    /**
     * Cluster objects into k clusters using k-means clustering
     *
     * @param k
     *            number of clusters to create (0 < k <= number of objects)
     * @return a String, in JSON format, that represents the clusters
     */
    public String kMeansClusters_json(int k) {
       return new KMeans(getRestaurants(), k).toJson();
    }

    /**
     * @param user
     *            represents a user_id in the database
     * @return a function that predicts the user's ratings for objects (of type
     *         T) in the database of type MP5Db<T>. The function that is
     *         returned takes two arguments: one is the database and other other
     *         is a String that represents the id of an object of type T.
     */
    public ToDoubleBiFunction<YelpDb, String> getPredictorFunction(String user) {
        LeastSquares leastSquares = new LeastSquares(this);
        return leastSquares.getPredictorFunction(user);
    }

    /**
     * Generates a random ID
     *
     * @return a String containing alphanumeric characters of length 23.
     */
    private String generateRandomID() {
        String potentialID;
        do {
            potentialID = UUID.randomUUID().toString().replaceAll("[\\-]", "").substring(0, 22);
        } while (IDs.contains(potentialID));

        return potentialID;
    }
}
