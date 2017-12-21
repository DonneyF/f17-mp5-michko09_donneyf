package ca.ece.ubc.cpen221.mp5;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Adapted from GSathish's FibonacciServer
 *
 * YelpClient is a client that sends requests to the YelpServer
 * and interprets its replies.
 * A new YelpClient is "open" until the close() method is called,
 * at which point it is "closed" and may not be used further.
 */
public class YelpClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    // Rep invariant: socket, in, out != null

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
     * Send a request to the server. Requires this is "open".
     *
     * @param command to find Fibonacci(x)
     * @throws IOException if network or server failure
     */
    public void sendRequest(String command) {
        out.print(command + "\n");
        out.flush(); // important! make sure x actually gets sent
    }

    /**
     * Get a reply from the next request that was submitted.
     * Requires this is "open".
     *
     * @return the YelpDBServer query response
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
     * Use a YelpServer to find the first N Fibonacci numbers.
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