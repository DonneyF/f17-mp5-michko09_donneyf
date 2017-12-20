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
    private static final String YelpRestaurantsData = "data/restaurants.json";
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
        database = new YelpDb(YelpRestaurantsData, reviewsData, usersData);
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

    private String addYelpRestaurant(String command) {
        // Check if command is of right format
        if (command.split(" ").length == 1) return "ERR: INVALID_RESTAURANT_STRING";

        // Remove ADDRESTAURANT and get the string to parse
        String possibleJSONString = command.substring(addYelpRestaurant.length());
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
        String possibleJSONString = command.substring(addUser.length());

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
        String possibleJSONString = command.substring(addReview.length());
        System.out.println(possibleJSONString);

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