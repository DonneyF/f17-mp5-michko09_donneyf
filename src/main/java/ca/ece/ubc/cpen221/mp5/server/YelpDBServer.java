package ca.ece.ubc.cpen221.mp5.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
		// Will implement this method
		return null;
	}
	
	private String addRestaurant(String command) {
		// Will implement this method
		return null;
	}
	
	private String addUser(String command) {
		// Will implement this method
		return null;
	}
	
	private String addReview(String command) {
		// Will implement this method
		return null;
	}
	
	/**
	 * Start a YelpDBServer running on the default port.
	 */
	public static void main(String[] args) {
		try {
			YelpDBServer server = new YelpDBServer(YELP_PORT);
			server.serve();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
