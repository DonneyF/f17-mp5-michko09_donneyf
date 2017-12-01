package ca.ece.ubc.cpen221.mp5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;

import ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurant;
import ca.ece.ubc.cpen221.mp5.yelp.YelpDb;
import ca.ece.ubc.cpen221.mp5.yelp.YelpReview;
import ca.ece.ubc.cpen221.mp5.yelp.YelpUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	private static final String getYelpRestaurant = "GETYelpRestaurant";
	private static final String addYelpRestaurant = "ADDYelpRestaurant";
	private static final String addUser = "ADDUSER";
	private static final String addReview = "ADDREVIEW";
	
	// Instance fields needed to create a database
	private YelpDb database;
	private List<String> IDs;
	private static final String YelpRestaurantsData = "data/restaurants.json";
	private static final String reviewsData = "data/reviews.json";
	private static final String usersData = "data/users.json";
	
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
				//System.err.println("request: " + line);
				try {
					String outcome = determineOperation(line);
					// compute answer and send back to client
					/**
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
	 * 		- is one of four command options for the YelpDB:
	 * 			1. GETYelpRestaurant - obtains the details of a YelpRestaurant using its unique ID.
	 * 			2. ADDUSER - adds a user to the database, with all details included.
	 * 			3. ADDYelpRestaurant - adds a YelpRestaurant to the database, with all details included.
	 * 			4. ADDREVIEW - adds a review to the database, with all details included.
	 */
	String determineOperation(String command) {
		if (command.length() >= 13) {
			if (command.substring(0, 13).equals(getYelpRestaurant)) {
				return getYelpRestaurant(command);
			} else if (command.substring(0, 13).equals(addYelpRestaurant)) {
				return addYelpRestaurant(command);
			} else {
				return "ERR: ILLEGAL_REQUEST";
			}
		} else if (command.length() >= 7) {
			if (command.substring(0, 7).equals(addUser)) {
				return addUser(command);
			} else if (command.substring(0, 9).equals(addReview)) {
				return addReview(command);
			} else {
				return "ERR: ILLEGAL_REQUEST";
			}
		} else {
			return "ERR: ILLEGAL_REQUEST";
		}
	}
	
	/**
	 * 
	 * @param command
	 */
	private String getYelpRestaurant(String command) {
		String[] splitCommand = command.split(" ");
		boolean YelpRestaurantExists = false;
		
		String JSONString = null;
		
		if (splitCommand.length == 1) {
			return "ERR: INVALID_YelpRestaurant_STRING";
		}
		
		List<YelpRestaurant> allYelpRestaurants = database.getRestaurants();
		
		for (YelpRestaurant unique : allYelpRestaurants) {
			if (unique.getBusinessId().equals(splitCommand[1])) {
				YelpRestaurantExists = true;
				try {
					ObjectMapper mapper = new ObjectMapper();
					JSONString = mapper.writeValueAsString(unique);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (!(YelpRestaurantExists)) {
			return "ERR: NO_SUCH_YelpRestaurant";
		} 
		
		return JSONString;
	}
	
	private String addYelpRestaurant(String command) {
		String JSONString = null;
		
		if (command.split(" ").length == 1) {
			return "ERR: INVALID_YelpRestaurant_STRING";
		}

		// Remove ADDRESTAURANT
		String possibleJSONString = command.substring(14);

		ObjectMapper mapper = new ObjectMapper();
		try {
			YelpRestaurant yelpRestaurant = mapper.readValue(possibleJSONString, YelpRestaurant.class);
		} catch (Exception e){
			e.printStackTrace();
		}
		// FIRST WE JSON PARSE THIS STRING
		/**
		 * IF ERROR IN PARSING:
		 */
		if (true) {
			return "ERR: INVALID_YelpRestaurant_STRING";
		}
		// map = JSON Parsed table thingy of this.
		
		List<YelpRestaurant> allYelpRestaurants = database.getRestaurants();
		
		//Table<String> table = new Table((HashMap<String, String>) map);
        //allYelpRestaurants.add(new YelpRestaurant(table));
        
		return "YelpRestaurant has been added to the database.";
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
		
		List<YelpUser> allUsers = database.getUsers();
				
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
		
		List<YelpReview> allReviews = database.getReviews();
				
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

	public String generateRandomID() {
		String potentialID =  UUID.randomUUID().toString().substring(0, 22);

		while(!IDs.contains(potentialID)) {
			potentialID = UUID.randomUUID().toString().substring(0, 22);
		}

		return potentialID;
	}

}
