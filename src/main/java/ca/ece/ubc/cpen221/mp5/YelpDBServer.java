package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.yelp.YelpDb;
import ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurant;
import ca.ece.ubc.cpen221.mp5.yelp.YelpReview;
import ca.ece.ubc.cpen221.mp5.yelp.YelpUser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * YelpDBServer is a multithreaded server which takes an input command from a client and returns the results
 * relating to the input. If no results are found or if the input request is not valid then it throws an
 * appropriate error message. Since YelpDBServer is a multithreaded server multiple clients are able to connect
 * to the server and send requests at the same time.
 *
 * YelpDBServer in this MP is mainly used for a local host server: Port 4949.
 *
 * Representation Invariant:
 *      - Once an instance of a server has been initialized with a specific port number, it must keep that port
 *        number until it has been terminated or shut down.
 *      - A request made by the client will have a response given by the server, whether it be a solution or an
 *        error message from improper request formatting.
 *
 * Abstraction Function:
 *      - Not valid since this class does not "map" a specific value to a specific domain. It simply initiates a server.
 *
 * As with YelpClient, we are using the FibonnaciMultiServer example Professor Sathish mentioned in the README file.
 */
public class YelpDBServer {

    private ServerSocket serverSocket;

    // All valid commands for this server
    private static final String getRestaurantCommand = "GETRESTAURANT";
    private static final String addRestaurantCommand = "ADDRESTAURANT";
    private static final String addUserCommand = "ADDUSER";
    private static final String addReviewCommand = "ADDREVIEW";
    private static final String queryCommand = "QUERY";

    // Instance fields needed to create a database
    private YelpDb database;
    private static final String restaurantData = "data/restaurants.json";
    private static final String reviewsData = "data/reviews.json";
    private static final String usersData = "data/users.json";

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
        database = new YelpDb(restaurantData, reviewsData, usersData);
    }

    /**
     * Runs the server as it listens for input connections and handles them.
     *
     * @throws IOException, if:
     *                      - the main server socket is broken.s
     */
    public void serve() throws IOException {
        while (true) {
            // Block until a client connects
            final Socket socket = serverSocket.accept();
            // Create a new thread to handle that client
            Thread handler = new Thread(new Runnable() {
                public void run() {
                    try {
                        try {
                            handle(socket);
                        } finally {
                            socket.close();
                        }
                    } catch (IOException ioe) {
                        // Print the steps leading to the exception
                        ioe.printStackTrace();
                    }
                }
            });
            // Start the thread
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

        // Read the input stream from the socket and wrap it to convert the stream from
        // a byte stream to a character stream. We then buffer the stream so that we
        // read one line at a time.
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));

        // We repeat the same process of the output stream of the socket, except in
        // this case we wrap it in a PrintWriter.
        PrintWriter out = new PrintWriter(new OutputStreamWriter(
                socket.getOutputStream()), true);

        try {
            // Read each request made by the client(s)
            for (String line = in.readLine(); line != null; line = in
                    .readLine()) {
                try {
                    // Determine the operation type of the request
                    String outcome = determineOperation(line);

                    // Return the result stemming from the operation, or an error
                    // message if the input request is invalid
                    out.println(outcome);
                } catch (NumberFormatException e) {
                    // complain about ill-formatted request
                    System.err.println("reply: err");
                    out.print("err\n");
                }
            }
        } finally {
            out.close();
            in.close();
        }
    }

    /**
     * Determines what kind of operation the input command is from the user.
     *
     * @param command, which:
     *                 - is one of four command options for the YelpDB:
     *                 1. GETYelpRestaurant - obtains the details of a YelpRestaurant using its unique ID.
     *                 2. ADDUSER - adds a user to the database, with all details included.
     *                 3. ADDYelpRestaurant - adds a YelpRestaurant to the database, with all details included.
     *                 4. ADDREVIEW - adds a review to the database, with all details included.
     *                 5. QUERY - searches for a restaurant in the database, keeping in mind specific criteria.
     *
     * @return a string, which:
     *                 - is the result from the input request, or an error message if the input string is invalid.
     */
    private String determineOperation(String command) {
        if (command.contains(getRestaurantCommand)) return getRestaurant(command);
        else if (command.contains(addRestaurantCommand)) return addRestaurant(command);
        else if (command.contains(addUserCommand)) return addUser(command);
        else if (command.contains(addReviewCommand)) return addReview(command);
        else if (command.contains(queryCommand)) return query(command);
        else return "ERR: ILLEGAL_REQUEST";
    }

    /**
     * Takes in a client request, which specifically asks to find a certain restaurant in the database.
     *
     * @param command, which:
     *      - is not null.
     *      - is a string with the keyword: GETRESTAURANT.
     *
     * @return a string, which:
     *      - is the restaurant result from the database search.
     *      - or an error message if no such restaurant exists or the input command was of invalid structure.
     */
    private String getRestaurant(String command) {
        // Check if the input string is a valid command. Must contain at least one word.
        if (command.split(" ").length == 1) return "ERR: INVALID_RESTAURANT_STRING";

        // Get the restaurant and parse the result to a string
        List<YelpRestaurant> allYelpRestaurants = database.getRestaurants();

        for (YelpRestaurant unique : allYelpRestaurants) {
            if (unique.getBusinessId().equals(command.split(" ")[1])) {
                try {
                    ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
                    return mapper.writeValueAsString(unique);
                } catch (JsonProcessingException e) {
                    return "ERR: " + e.getMessage();
                }
            }
        }
        return "ERR: NO_SUCH_RESTAURANT";
    }

    /**
     * Takes in a client request, which specifically asks to add a certain restaurant to the database.
     *
     * @param command, which:
     *      - is not null.
     *      - is a string with the keyword: ADDRESTAURANT.
     *
     * @return a string, which:
     *      - "RESTAURANT_ADD_SUCCESS: + (details)" if the operation was successful.
     *      - or an error message if the command was of invalid structure.
     */
    private String addRestaurant(String command) {
        // Check if command is of right format
        if (command.split(" ").length == 1) return "ERR: INVALID_RESTAURANT_STRING";

        // Remove ADDRESTAURANT and get the string to parse
        String possibleJSONString = command.substring(addRestaurantCommand.length());
        // Check required fields
        if(!possibleJSONString.contains("name")) return "ERR: INVALID_RESTAURANT_STRING";
        if(!possibleJSONString.contains("latitude")) return "ERR: INVALID_RESTAURANT_STRING";
        if(!possibleJSONString.contains("longitude")) return "ERR: INVALID_RESTAURANT_STRING";
        try {
            YelpRestaurant yelpRestaurant = database.addRestaurant(possibleJSONString);

            return "RESTAURANT_ADD_SUCCESS: " + new ObjectMapper().writeValueAsString(yelpRestaurant).replaceAll(System.lineSeparator(), "")
                    .replaceAll(",",", ").replaceAll("\":", "\": ");
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getClass().equals(JsonParseException.class)) {
                return "ERR: INVALID_RESTAURANT_STRING";
            } else {
                return "ERR: " + e.getMessage();
            }
        }
    }

    /**
     * Takes in a client request, which specifically asks to add a certain user to the database.
     *
     * @param command, which:
     *      - is not null.
     *      - is a string with the keyword: ADDUSER.
     *
     * @return a string, which:
     *      - "USER_ADD_SUCCESS: + (details)" if the operation was successful.
     *      - or an error message if the command was of invalid structure.
     */
    private String addUser(String command) {
        // Check if command is of right format
        if (command.split(" ").length == 1) return "ERR: INVALID_USER_STRING";

        // Remove ADDUSER and get the JSON string to parse
        String possibleJSONString = command.substring(addUserCommand.length());

        // Check required fields
        if(!possibleJSONString.contains("name")) return "ERR: INVALID_USER_STRING";

        try {
            YelpUser yelpUser = database.addUser(possibleJSONString);

            return "USER_ADD_SUCCESS: " + new ObjectMapper().writeValueAsString(yelpUser).replaceAll(System.lineSeparator(), "")
                    .replaceAll(",",", ").replaceAll("\":", "\": ");
        } catch (Exception e) {
            if (e.getClass().equals(JsonParseException.class)) {
                return "ERR: INVALID_USER_STRING";
            } else {
                e.printStackTrace();
                return e.getMessage();
            }
        }
    }

    /**
     * Takes in a client request, which specifically asks to add a certain review to the database.
     *
     * @param command, which:
     *      - is not null.
     *      - is a string with the keyword: ADDREVIEW.
     *
     * @return a string, which:
     *      - "REVIEW_ADD_SUCCESS: + (details)" if the operation was successful.
     *      - or an error message if the command was of invalid structure.
     */
    private String addReview(String command) {
        // Check if legitimate command
        if (command.split(" ").length == 1) return "ERR: INVALID_REVIEW_STRING";

        // Remove ADDREVIEW and get JSON string to parse
        String possibleJSONString = command.substring(addReviewCommand.length());

        // Check required fields
        if (!possibleJSONString.contains("business_id")) return "ERR: INVALID_REVIEW_STRING";
        if (!possibleJSONString.contains("text")) return "ERR: INVALID_REVIEW_STRING";
        if (!possibleJSONString.contains("stars")) return "ERR: INVALID_REVIEW_STRING";
        if (!possibleJSONString.contains("user_id")) return "ERR: INVALID_REVIEW_STRING";

        try {
        YelpReview yelpReview = database.addReview(possibleJSONString);

        return "REVIEW_ADD_SUCCESS:" + new ObjectMapper().writeValueAsString(yelpReview).replaceAll(System.lineSeparator(), "")
                .replaceAll(",",", ").replaceAll("\":", "\": ");
        } catch (Exception e) {
            if (e.getClass().equals(JsonParseException.class)) {
                return "ERR: INVALID_RESTAURANT_STRING";
            } else return "ERR: " + e.getMessage();
        }
    }

    /**
     * Takes in a client request, which is a specific query request.
     *
     * @param command, which:
     *      - is not null.
     *      - is a string with the keyword: QUERY.
     *
     * @return a string, which:
     *      - is a list of all possible restaurants satisfying the conditions of the input query request, in JSON format.
     *      - or an error message if the command was of invalid structure or there are no possible matches in the database.
     */
    private String query(String command){
        if(command.split( " ").length == 1) return "ERR: INVALID_QUERY";

        String queryString = command.substring(queryCommand.length());

        // Capture system.err to get ANTLR errors
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setErr(new PrintStream(out));

        Set<YelpRestaurant> matches = database.getMatches(queryString);

        if (out.toString().length() != 0) {
            return "ERR: INVALID_QUERY";
        }

        // Bring back system err
        System.setErr(System.err);

        if (matches.isEmpty()) return "ERR: NO_MATCH";

        try {
            StringBuilder stringBuilder = new StringBuilder();
            ObjectMapper mapper = new ObjectMapper();
            for(YelpRestaurant restaurant : matches) {
                stringBuilder.append(mapper.writeValueAsString(restaurant).replaceAll(System.lineSeparator(), "")
                        .replaceAll(",", ", ").replaceAll("\":", "\": "));
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        } catch (JsonProcessingException e) {
            return "ERR: INVALID_QUERY";
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

}