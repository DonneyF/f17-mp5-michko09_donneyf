package ca.ece.ubc.cpen221.mp5.yelp;

import ca.ece.ubc.cpen221.mp5.MP5Database;
import ca.ece.ubc.cpen221.mp5.statistics.KMeans;
import ca.ece.ubc.cpen221.mp5.statistics.LeastSquares;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.Collectors;

/**
 * A Yelp database that stores information of restaurants, its reviews, and the users visiting these restaurants
 */
public class YelpDb extends MP5Database<YelpRestaurant> {

    /*
     * Representation Invariant:
     *      - users, restaurant, reviews, is not null
     *      - Each value in users, reviews, and restaurants contains its key in its unique ID
     *
     * Abstraction Function:
     *      - AF(this) -> a collection of YelpRestaurants, YelpReviews, and YelpUsers such that all objects are unique
     */
    // Each map contains a the unique ID of the element and corresponding object
    private final ConcurrentHashMap<String, YelpUser> users;
    private final ConcurrentHashMap<String, YelpRestaurant> restaurants;
    private final ConcurrentHashMap<String, YelpReview> reviews;
    private final List<String> IDs;
    private static ObjectNode genericUser;
    private static ObjectNode genericRestaurant;
    private static ObjectNode genericReview;

    // Default user JSON objects
    static {
        String defaultUser = "{\"url\": \"http://www.yelp.com/user_details?userid=\", \"votes\": {}, \"review_count\": 0, \"type\": \"user\", \"user_id\": \"42\", \"average_stars\": 0}";
        String defaultRestaurant = "{\"open\": true, \"url\": \"http://www.yelp.com/biz/\", \"type\": \"business\", \"review_count\": 0}";
        String defaultReview = "{\"type\": \"review\", \"votes\": {}, \"review_id\": \"0a-pCW4guXIlWNpVeBHChg\", \"text\": \"Some text\"}";

        ObjectMapper mapper = new ObjectMapper();
        try {
            genericUser = (ObjectNode) mapper.readTree(defaultUser);
            genericRestaurant = (ObjectNode) mapper.readTree(defaultRestaurant);
            genericReview = (ObjectNode) mapper.readTree(defaultReview);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid default string");
        }
    }

    /**
     * Constructs a YelpDb object
     *
     * @param restaurantFileName is not null
     * @param reviewsFileName    is not null
     * @param usersFileName      is not null
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
     * @param tclass   the class of either YelpRestaurants, YelpReviews, or YelpUsers
     * @return A map of Strings to Objects where the String is the unique identifier of YelpRestaurants, YelpReviews, or YelpUsers,
     * and Objects contain YelpRestaurants, YelpReviews, or YelpUsers, respectively
     */
    private ConcurrentHashMap parseJSON(String filePath, Class<?> tclass) {
        ConcurrentHashMap<String, Object> elements = new ConcurrentHashMap<>();
        try {
            // Get the file name
            File file = new File(filePath);
            String line;
            // Read a the file and get a line
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
                Object element = mapper.readValue(line, tclass);
                switch (tclass.getName()) {
                    case "ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurant":
                        elements.put(((YelpRestaurant) element).getBusinessId(), element);
                        break;
                    case "ca.ece.ubc.cpen221.mp5.yelp.YelpUser":
                        elements.put(((YelpUser) element).getUserId(), element);
                        break;
                    case "ca.ece.ubc.cpen221.mp5.yelp.YelpReview":
                        elements.put(((YelpReview) element).getReviewId(), element);
                        break;
                }
            }
            return elements;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON String in file");
        }
    }

    /**
     * Gets the string representation of this database
     *
     * @return the string representation of the set of YelpRestaurants, YelpReviews, and YelpUsers
     */
    @Override
    public String toString() {
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
    public YelpUser getUser(String userId) {
        return users.get(userId);
    }

    /**
     * Gets a restaurant from this database
     *
     * @param restaurantId represents a restaurant stored in this database
     * @return a YelpRestaurant that contains restaurantId
     */
    public YelpRestaurant getRestaurant(String restaurantId) {
        return restaurants.get(restaurantId);
    }

    /**
     * Gets a review from this database
     *
     * @param reviewId represents a review stored in this database
     * @return a YelpReview that contains reviewId
     */
    public YelpReview getReview(String reviewId) {
        return reviews.get(reviewId);
    }

    /**
     * Add a user to this database
     *
     * @param userJson is a String of JSON containing user data. userJson must contain an entry for "name".
     * @return the YelpUser created by this method with fields defaulted.
     */
    public YelpUser addUser(String userJson) {
        ObjectNode user = genericUser.deepCopy();

        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        try {
            // Verify JSON by adding to the ObjectNode. Copy statements from input to generic and set values
            ObjectNode input = (ObjectNode) mapper.readTree(userJson);

            // Parse user ID and url. Input must contain user's name
            if (!input.toString().contains("name"))
                throw new IllegalArgumentException("Input string not valid. Missing \"name\" field");

            if (input.has("user_id")){
                if (IDs.contains(input.get("user_id").asText())) throw new IllegalArgumentException("User ID already exists");
                input.put("url", user.get("url").asText() + input.get("user_id").asText());
            }
            else {
                // Get a random ID
                String id = generateRandomID();
                input.put("user_id", id);
                input.put("url", user.get("url").asText() + id);
            }
            user.setAll(input);

            YelpUser yelpUser = new ObjectMapper().readValue(user.toString(), YelpUser.class);
            yelpUser.setVotes(new YelpVotes());

            users.put(yelpUser.getUserId(), yelpUser);
            return yelpUser;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Add a restaurant to this database
     *
     * @param restaurantJson is a String of JSON containing restaurant data. userJson must contain an entry for "name".
     * @return the YelpRestaurant created by this method with fields defaulted.
     */
    public YelpRestaurant addRestaurant(String restaurantJson) {
        ObjectNode restaurant = genericRestaurant.deepCopy();

        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        try {
            // Verify JSON by adding to the ObjectNode. Copy statements from input to generic and set values
            ObjectNode input = (ObjectNode) mapper.readTree(restaurantJson);

            // Parse user ID and url. Input must contain business name
            if (!input.toString().contains("name"))
                throw new IllegalArgumentException("Input string not valid. Missing \"name\" field");

            // Check if business ID exists in the request. If not, use the random ID.
            if (input.has("business_id") && IDs.contains(input.get("business_id").asText()))
                throw new IllegalArgumentException("Business ID already exists");
            else {
                // Get a random ID
                String id = generateRandomID();
                input.put("business_id", id);
            }

            input.put("url", restaurant.get("url").asText() + input.get("name").asText().toLowerCase()
                    .replaceAll("\\s", "-").replaceAll("[^a-z0-9-]", ""));
            restaurant.setAll(input);

            YelpRestaurant yelpRestaurant = new ObjectMapper().readValue(restaurant.toString(), YelpRestaurant.class);

            restaurants.put(yelpRestaurant.getBusinessId(), yelpRestaurant);

            return yelpRestaurant;

        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Add a review to this database
     *
     * @param reviewJson is a String of JSON containing review data. userJson must contain an entry for "text".
     * @return the YelpReview created by this method with fields defaulted.
     */
    public YelpReview addReview(String reviewJson) {
        ObjectNode review = genericReview.deepCopy();

        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        try {
            // Verify JSON by adding to the ObjectNode. Copy statements from input to generic and set values
            ObjectNode input = (ObjectNode) mapper.readTree(reviewJson);
            // Parse user ID and url. Input must contain business name

            if (!input.toString().contains("text"))
                throw new IllegalArgumentException("Input string not valid. Missing \"text\" field");

            if (input.has("review_id") && IDs.contains(input.get("review_id").asText())) throw new IllegalArgumentException("Review ID already exists");
            else {
                // Get a random ID
                String id = generateRandomID();
                input.put("review_id", id);
            }

            review.setAll(input);

            YelpReview yelpReview = new ObjectMapper().readValue(input.toString(), YelpReview.class);
            reviews.put(yelpReview.getReviewId(), yelpReview);

            return yelpReview;

        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Perform a structured query and return the set of YelpRestaurants that matches the query
     *
     * @param queryString is not null
     * @return the set of YelpRestaurants that matches the query
     */
    public synchronized Set<YelpRestaurant> getMatches(String queryString) {
        return new YelpQueryParser(this).getMatches(queryString);
    }

    /**
     * Cluster objects into k clusters using k-means clustering
     *
     * @param k number of clusters to create (0 < k <= number of objects)
     * @return a String, in JSON format, that represents the clusters
     */
    public String kMeansClusters_json(int k) {
        return new KMeans(getRestaurants(), k).toJson();
    }

    /**
     * @param user represents a user_id in the database
     * @return a function that predicts the user's ratings for objects (of type
     * T) in the database of type MP5Db<T>. The function that is
     * returned takes two arguments: one is the database and other other
     * is a String that represents the id of an object of type T.
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

        IDs.add(potentialID);
        return potentialID;
    }
}
