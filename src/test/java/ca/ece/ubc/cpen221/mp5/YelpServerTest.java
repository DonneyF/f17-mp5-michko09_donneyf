package ca.ece.ubc.cpen221.mp5;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class YelpServerTest {

    @Test
    public void test1() {

        final String[] serverResponse = new String[1];

        Thread server = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    YelpDBServer yelpDBServer = new YelpDBServer(4949);
                    yelpDBServer.serve();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread client = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    YelpClient yelpClient = new YelpClient("localhost", 4949);
                    yelpClient.sendRequest("ADDUSER {\"name\": \"Sathish G.\"}");
                    serverResponse[0] = yelpClient.getReply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        server.start();
        client.start();
        try {

            client.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            JsonNode node = new ObjectMapper().readTree(serverResponse[0].substring(serverResponse[0].indexOf("{"), serverResponse[0].lastIndexOf("}") + 1));
            assertEquals("Sathish G.", node.get("name").asText());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}
