package ca.ece.ubc.cpen221.mp5;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * YelpClient is a server-client initiator which creates a socket that will be attached to a main server. Through this
 * client model a user will be able to input certain requests into the server that involve modifying or searching for
 * an element in the database.

 * YelpClient in this MP is mainly used for a local host server: Port 4949.
 *
 * Representation Invariant:
 *      - Once an instance of a client has been initialized with a specific port number, it must keep that port
 *        number until it has been terminated or disconnected from the server.
 *      - A request made by the client will have a response given by the server, whether it be a solution or an
 *        error message from improper request formatting.
 *      - socket, in and out are not null.
 *
 * Abstraction Function:
 *      - Not valid since this class does not "map" a specific value to a specific domain. It simply initiates a client.
 *
 * As with YelpDBServer, we are using the FibonnaciClient example Professor Sathish mentioned in the README file.
 */
public class YelpClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * Make a YelpClient and connect it to a server running on
     * hostname at the specified port.
     *
     * @throws IOException if can't connect
     */
    public YelpClient(String hostname, int port) throws IOException {
        socket = new Socket(hostname, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    /**
     * Send a request to the server. Requires this is "open". The valid commands for this
     * MP are specified below.
     *
     * @param command which,
     *      - is one of the five valid commands:
     *        1. ADDRESTAURANT + (details in JSON)
     *        2. ADDUSER + (details in JSON)
     *        3. ADDREVIEW + (details in JSON)
     *        4. GETRESTAURANT + Name
     *        5. QUERY + criteria
     *
     * @throws IOException if network or server failure
     */
    public void sendRequest(String command) throws IOException  {
        out.print(command + "\n");
        out.flush(); // important! make sure x actually gets sent
    }

    /**
     * Get a reply from the next request that was submitted.
     * Requires this is "open".
     *
     * @return the server response to the client command.
     *
     * @throws IOException if network or server failure
     */
    public String getReply() throws IOException {
        StringBuilder response = new StringBuilder();
        String line = in.readLine();

        if (line == null) {
            throw new IOException("connection terminated unexpectedly");
        } else {
            response.append(line);
            response.append("\n");
        }
        // Continue reading multi-line responses, if any
        while (in.ready()) {
            line = in.readLine();
            if(!line.trim().isEmpty()) {
                response.append(line);
                response.append("\n");
            }
        }
        return response.toString();
    }

    /**
     * Closes the client's connection to the server.
     * This client is now "closed". Requires this is "open".
     *
     * @throws IOException if close fails
     */
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    /**
     * Connects to a main YelpDBServer where the client can input certain commands and obtain
     * a response from the server, whether that be an approval message, possible solution, or error message.
     */
    public static void main(String[] args) {
        System.out.println("Enter the port number: ");
        Scanner scanner = new Scanner(System.in);
        String port = scanner.nextLine();
        int portNumber = Integer.parseInt(port);
        try {
            YelpClient client = new YelpClient("localhost", portNumber);

            System.out.println("Enter the command: ");
            String nextCommand = scanner.nextLine();
            while (!(nextCommand.isEmpty())) {
                client.sendRequest(nextCommand);
                String reply = client.getReply();
                System.out.println(reply);
                System.out.println("Input next Command: ");
                nextCommand = scanner.nextLine();
            }

            client.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}