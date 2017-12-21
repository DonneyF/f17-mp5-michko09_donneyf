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
        //System.err.println("client connected");

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
                //System.err.println("request: " + line);
                try {
                    String outcome = determineOperation(line);
                    // compute answer and send back to client
                    /*
                     * NEED TO FIX OUTPUT LINES
                     */
                    //System.err.println("reply: " + outcome);
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
        if (command.contains(getRestaurantCommand)) return getRestaurant(command);
        else if (command.contains(addRestaurantCommand)) return addRestaurant(command);
        else if (command.contains(addUserCommand)) return addUser(command);
        else if (command.contains(addReviewCommand)) return addReview(command);
        else if (command.contains(queryCommand)) return query(command);
        else return "ERR: ILLEGAL_REQUEST";
    }

    /**
     * @param command
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
            return "ERR: INVALID QUERY";
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