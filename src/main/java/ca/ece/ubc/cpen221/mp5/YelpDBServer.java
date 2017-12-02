package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.yelp.YelpDb;
import ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurant;
import ca.ece.ubc.cpen221.mp5.yelp.YelpReview;
import ca.ece.ubc.cpen221.mp5.yelp.YelpUser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * YelpDBServer is a multithreaded server which takes an input command and
 * returns the results relating to the input. If no results are found or if
 * the input request is not valid then it throws an IllegalArgumentException.
 * <p>
 * This server will accept requests in the form:
 * Request ::=
 * <p>
 * MAKE SURE TO WRITE THIS.
 */
public class YelpDBServer {

    private ServerSocket serverSocket;

    // All valid commands for this server
    private static final String getYelpRestaurant = "GETRESTAURANT";
    private static final String addYelpRestaurant = "ADDRESTAURANT";
    private static final String addUser = "ADDUSER";
    private static final String addReview = "ADDREVIEW";

    // Instance fields needed to create a database
    private YelpDb database;
    private List<String> IDs;
    private static final String YelpRestaurantsData = "data/restaurants.json";
    private static final String reviewsData = "data/reviews.json";
    private static final String usersData = "data/users.json";
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
     * Creates a YelpDBServer that will listen for connections on port.
     *
     * @param port, where:
     *              - it is not null.
     *              - it is a port number, from 0 <= port <= 65535.
     * @throws IOException, if:
     *                      - the port is not a valid port number.
     */
    public YelpDBServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        database = new YelpDb(YelpRestaurantsData, reviewsData, usersData);

        // Get a list of unique IDs.
        IDs = database.getRestaurants().parallelStream().map(YelpRestaurant::getBusinessId).collect(Collectors.toList());
        IDs.addAll(database.getReviews().parallelStream().map(YelpReview::getReviewId).collect(Collectors.toList()));
        IDs.addAll(database.getUsers().parallelStream().map(YelpUser::getUserId).collect(Collectors.toList()));
    }

    /**
     * Runs the server as it listens for input connections and handles them.
     * This method is taken from the FibonacciServerMulti.java that Professor Sathish
     * has provided as reference.
     *
     * @throws IOException, if:
     *                      - the main server socket is broken.s
     */
    public void serve() throws IOException {
        while (true) {
            // block until a client connects
            final Socket socket = serverSocket.accept();
            // create a new thread to handle that client
            Thread handler = new Thread(new Runnable() {
                public void run() {
                    try {
                        try {
                            handle(socket);
                        } finally {
                            socket.close();
                        }
                    } catch (IOException ioe) {
                        // this exception wouldn't terminate serve(),
                        // since we're now on a different thread, but
                        // we still need to handle it
                        ioe.printStackTrace();
                    }
                }
            });
            // start the thread
            handler.start();
        }
    }

    /**
     * Handle one client connection. Returns when client disconnects.
     *
     * @param socket socket where client is connected
     * @throws IOException if connection encounters an error
     */
    private void handle(Socket socket) throws IOException {
        System.err.println("client connected");

        // get the socket's input stream, and wrap converters around it
        // that convert it from a byte stream to a character stream,
        // and that buffer it so that we can read a line at a time
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));

        // similarly, wrap character=>bytestream converter around the
        // socket output stream, and wrap a PrintWriter around that so
        // that we have more convenient ways to write Java primitive
        // types to it.
        PrintWriter out = new PrintWriter(new OutputStreamWriter(
                socket.getOutputStream()), true);

        try {
            // each request is a single line containing a number
            for (String line = in.readLine(); line != null; line = in
                    .readLine()) {
                System.err.println("request: " + line);
                try {
                    String outcome = determineOperation(line);
                    // compute answer and send back to client
                    /*
                     * NEED TO FIX OUTPUT LINES
                     */
                    System.err.println("reply: " + outcome);
                    out.println(outcome);
                } catch (NumberFormatException e) { // NEED TO FIX THIS LINE
                    // complain about ill-formatted request
                    System.err.println("reply: err");
                    out.print("err\n");
                }
                // important! our PrintWriter is auto-flushing, but if it were
                // not:
                // out.flush();
            }
        } finally {
            out.close();
            in.close();
        }
    }

    /**
     * Determine what kind of operation the input command is from the user.
     *
     * @param command, which:
     *                 - is one of four command options for the YelpDB:
     *                 1. GETYelpRestaurant - obtains the details of a YelpRestaurant using its unique ID.
     *                 2. ADDUSER - adds a user to the database, with all details included.
     *                 3. ADDYelpRestaurant - adds a YelpRestaurant to the database, with all details included.
     *                 4. ADDREVIEW - adds a review to the database, with all details included.
     */
    private String determineOperation(String command) {
        if (command.contains(getYelpRestaurant)) return getYelpRestaurant(command);
        else if (command.contains(addYelpRestaurant)) return addYelpRestaurant(command);
        else if (command.contains(addUser)) return addUser(command);
        else if (command.contains(addReview)) return addReview(command);
        else return "ERR: ILLEGAL_REQUEST";
    }

    /**
     * @param command
     */
    private String getYelpRestaurant(String command) {

        // Check if the input string is a valid command. Must contain at least one word.
        String[] splitCommand = command.split(" ");

        if (splitCommand.length == 1) {
            return "ERR: INVALID_YelpRestaurant_STRING";
        }

        // Get the restaurant and parse the result to a string
        List<YelpRestaurant> allYelpRestaurants = database.getRestaurants();
        System.out.println(allYelpRestaurants.size());
        for (YelpRestaurant unique : allYelpRestaurants) {
            if (unique.getBusinessId().equals(splitCommand[1])) {
                try {
                    ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
                    return mapper.writeValueAsString(unique);
                } catch (JsonProcessingException e) {
                    return "ERR: " + e.getMessage();
                }
            }
        }

        return "ERR: NO_SUCH_YelpRestaurant";
    }

    private String addYelpRestaurant(String command) {

        if (command.split(" ").length == 1) {
            return "ERR: INVALID_YelpRestaurant_STRING";
        }
        // Remove ADDRESTAURANT
        String possibleJSONString = command.substring(addYelpRestaurant.length());

        // Add restaurant to database
        ObjectNode restaurant = genericRestaurant.deepCopy();
        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        try {
            // Verify JSON by adding to the ObjectNode. Copy statements from input to generic and set values
            ObjectNode input = (ObjectNode) mapper.readTree(possibleJSONString);
            // Get a random ID
            String id = generateRandomID();
            // Parse business ID and url. Input must contain business name
            if (!input.toString().contains("name")) return "ERR: INVALID_YelpRestaurant_STRING_NO_NAME";
            input.put("business_id", id);
            input.put("url", "http://www.yelp.com/biz/" + input.get("name").asText().
                    replaceAll("\\s", "-").replaceAll("[^a-zA-Z0-9\\\\s\\-]", "").toLowerCase());
            restaurant.setAll(input);
            database.addRestaurant(restaurant.toString());
            return "RESTAURANT_ADD_SUCCESS: " + restaurant.toString();
        } catch (Exception e) {
            if (e.getClass().equals(JsonParseException.class)) {
                return "ERR: INVALID_YelpRestaurant_STRING";
            } else {
                return "ERR: " + e.getMessage();
            }
        }
    }

    private String addUser(String command) {
        String[] splitCommand = command.split(" ");

        if (splitCommand.length == 1) {
            return "ERR: INVALID_USER_STRING";
        }

        String possibleJSONString = command.substring(addUser.length());

        ObjectNode user = genericUser.deepCopy();

        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        try {
            // Verify JSON by adding to the ObjectNode. Copy statements from input to generic and set values
            ObjectNode input = (ObjectNode) mapper.readTree(possibleJSONString);
            // Get a random ID
            String id = generateRandomID();
            // Parse user ID and url. Input must contain business name
            if (!input.toString().contains("name")) return "ERR: INVALID_YelpUser_STRING_NO_NAME";
            input.put("user_id", id);
            input.put("url", "http://www.yelp.com/user_details?userid=" + id);

            user.setAll(input);
            database.addUser(user.toString());

            return "USER_ADD_SUCCESS: " + user.toString();
        } catch (Exception e) {
            if (e.getClass().equals(JsonParseException.class)) {
                return "ERR: INVALID_YelpUser_STRING";
            } else {
                e.printStackTrace();
                return e.getMessage();
            }
        }
    }

    private String addReview(String command) {
        String[] splitCommand = command.split(" ");

        String JSONString = null;

        if (splitCommand.length == 1) {
            return "ERR: INVALID_REVIEW_STRING";
        }

        String possibleJSONString = command.substring(addReview.length());

        ObjectNode review = genericReview.deepCopy();

        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        try {
            // Verify JSON by adding to the ObjectNode. Copy statements from input to generic and set values
            ObjectNode input = (ObjectNode) mapper.readTree(possibleJSONString);
            // Get a random ID
            String id = generateRandomID();
            // Parse user ID and url. Input must contain business name
            if (!input.toString().contains("text")) return "ERR: INVALID_YelpReview_STRING_NO_TEXT";
            input.put("review_id", id);

            review.setAll(input);
            database.addUser(review.toString());

            return "REVIEW_ADD_SUCCESS: " + review.toString();
        } catch (Exception e) {
            if (e.getClass().equals(JsonParseException.class)) {
                return "ERR: INVALID_YelpRestaurant_STRING";
            } else return "ERR: " + e.getMessage();
        }
    }

    /**
     * Start a YelpDBServer running on the default port.
     */
    public static void main(String[] args) {
        System.out.println("Enter the port number: ");
        Scanner scanner = new Scanner(System.in);
        String port = scanner.nextLine();
        scanner.close();
        int portNumber = Integer.parseInt(port);
        try {
            YelpDBServer server = new YelpDBServer(portNumber);
            server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateRandomID() {
        String potentialID;
        do {
            potentialID = UUID.randomUUID().toString().replaceAll("[\\-]", "").substring(0, 22);
        } while (IDs.contains(potentialID));

        return potentialID;
    }

}
