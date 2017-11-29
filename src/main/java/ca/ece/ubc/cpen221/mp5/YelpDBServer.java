package ca.ece.ubc.cpen221.mp5;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import ca.ece.ubc.cpen221.mp5.datastructure.Restaurant;
import ca.ece.ubc.cpen221.mp5.datastructure.Review;
import ca.ece.ubc.cpen221.mp5.datastructure.Table;
import ca.ece.ubc.cpen221.mp5.datastructure.User;
import ca.ece.ubc.cpen221.mp5.datastructure.Votes;
import ca.ece.ubc.cpen221.mp5.yelp.YelpDb;

/**
 * YelpDBServer is a multithreaded server which takes an input command and 
 * returns the results relating to the input. If no results are found or if 
 * the input request is not valid then it throws an IllegalArgumentException.
 * 
 * This server will accept requests in the form:
 * 		Request ::=
 * 
 * MAKE SURE TO WRITE THIS.
 */
public class YelpDBServer {
	
	// Default port used for local servers
	public static final int YELP_PORT = 4949;
	
	private ServerSocket serverSocket;
	
	// All valid commands for this server
	private static final String getRestaurant = "GETRESTAURANT";
	private static final String addRestaurant = "ADDRESTAURANT";
	private static final String addUser = "ADDUSER";
	private static final String addReview = "ADDREVIEW";
	
	// Instance fields needed to create a database
	private YelpDb database;
	private String restaurantsData = "data/restaurants.json";
	private String reviewsData = "data/reviews.json";
	private String usersData = "data/users.json";
	
	/**
	 * Creates a YelpDBServer that will listen for connections on port.
	 * 
	 * @param port, where:
	 * 		- it is not null.
	 * 		- it is a port number, from 0 <= port <= 65535.
	 * 
	 * @throws IOException, if:
	 * 		- the port is not a valid port number.
	 */
	public YelpDBServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		database = new YelpDb(restaurantsData, reviewsData, usersData);
	}

	/**
	 * Runs the server as it listens for input connections and handles them.
	 * This method is taken from the FibonacciServerMulti.java that Professor Sathish 
	 * has provided as reference.
	 * 
	 * @throws IOException, if:
	 * 			- the main server socket is broken.s
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
	 * @param socket
	 *            socket where client is connected
	 * @throws IOException
	 *             if connection encounters an error
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
					/**
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
	 * 		- is one of four command options for the YelpDB:
	 * 			1. GETRESTAURANT - obtains the details of a restaurant using its unique ID.
	 * 			2. ADDUSER - adds a user to the database, with all details included.
	 * 			3. ADDRESTAURANT - adds a restaurant to the database, with all details included.
	 * 			4. ADDREVIEW - adds a review to the database, with all details included.
	 */
	String determineOperation(String command) {
		if (command.substring(0, 13).equals(getRestaurant)) {
			return getRestaurant(command);
		} else if (command.substring(0, 13).equals(addRestaurant)) {
			return addRestaurant(command);
		} else if (command.substring(0, 7).equals(addUser)) {
			return addUser(command);
		} else if (command.substring(0, 9).equals(addReview)) {
			return addReview(command);
		} else {
			throw new IllegalArgumentException("Error: Not a valid operation.");
		}
	}
	
	/**
	 * 
	 * @param command
	 */
	private String getRestaurant(String command) {
		String[] splitCommand = command.split(" ");
		boolean restaurantExists = false;
		
		String JSONString = null;
		
		if (splitCommand.length == 1) {
			return "ERR: INVALID_RESTAURANT_STRING";
		}
		
		Set<Restaurant> allRestaurants = database.getRestaurants();
		
		for (Restaurant unique : allRestaurants) {
			if (unique.getBusinessID().equals(splitCommand[1])) {
				restaurantExists = true;
				// RETURN IN JSON FORMAT
			}
		}
		
		if (!(restaurantExists)) {
			return "ERR: NO_SUCH_RESTAURANT";
		} 
		
		return JSONString;
	}
	
	private String addRestaurant(String command) {
		String[] splitCommand = command.split(" ");

		String JSONString = null;
		
		if (splitCommand.length == 1) {
			return "ERR: INVALID_RESTAURANT_STRING";
		}
		
		String possibleJSONString = command.substring(14);
		// FIRST WE JSON PARSE THIS STRING
		/**
		 * IF ERROR IN PARSING:
		 */
		if (true) {
			return "ERR: INVALID_RESTAURANT_STRING";
		}
		// map = JSON Parsed table thingy of this.
		
		Set<Restaurant> allRestaurants = database.getRestaurants();
		
		//Table<String> table = new Table((HashMap<String, String>) map);
        //allRestaurants.add(new Restaurant(table));
        
		return "Restaurant has been added to the database.";
	}
	
	private String addUser(String command) {
		String[] splitCommand = command.split(" ");

		String JSONString = null;
		
		if (splitCommand.length == 1) {
			return "ERR: INVALID_USER_STRING";
		}
		
		String possibleJSONString = command.substring(9);
		// FIRST WE JSON PARSE THIS STRING
		/**
		 * IF ERROR IN PARSING:
		 */
		if (true) {
			return "ERR: INVALID_USER_STRING";
		}
	
		// map = JSON Parsed table thingy of this.
		
		Set<User> allUsers = database.getUsers();
				
		//Table<String> table = new Table((HashMap<String, String>) map);
		//Votes votes = new Votes((HashMap) map.get("votes"));
		//allUsers.add(new User(table, votes));
		        
		return "User has been added to the database.";
	}
	
	private String addReview(String command) {
		String[] splitCommand = command.split(" ");

		String JSONString = null;
		
		if (splitCommand.length == 1) {
			return "ERR: INVALID_REVIEW_STRING";
		}
		
		String possibleJSONString = command.substring(9);
		// FIRST WE JSON PARSE THIS STRING
		/**
		 * IF ERROR IN PARSING:
		 */
		if (true) {
			return "ERR: INVALID_REVIEW_STRING";
		}
	
		// map = JSON Parsed table thingy of this.
		
		Set<Review> allReviews = database.getReviews();
				
		//Table<String> table = new Table((HashMap<String, String>) map);
		//Votes votes = new Votes((HashMap) map.get("votes"));
		//allReviews.add(new Review(table, votes));
		  
		return "Review has been added to the database.";
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
