package ca.ece.ubc.cpen221.mp5;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class YelpServerTest {

    /*
     * Test the server functionality as well as concurrency
     */
    private static Thread server = new Thread(new Runnable() {
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

    @Test
    public void test1() {
        if(!server.isAlive()) server.start();
        final String[] serverResponse = new String[1];

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

        client.start();
        try {
            client.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            // Expected: User is created with proper name
            JsonNode node = new ObjectMapper().readTree(serverResponse[0].substring(serverResponse[0].indexOf("{"), serverResponse[0].lastIndexOf("}") + 1));
            assertEquals("Sathish G.", node.get("name").asText());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void test2() {
        if(!server.isAlive()) server.start();
        final String[] serverResponse = new String[1];

        Thread client = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    YelpClient yelpClient = new YelpClient("localhost", 4949);
                    yelpClient.sendRequest("ADDRESTAURANT {\"longitude\": -122.5122, \"name\": \"Test Restaurant\", \"latitude\": 37.98541, \"state\": \"CA\", \"price\": 1}");
                    serverResponse[0] = yelpClient.getReply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        client.start();
        try {

            client.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            // Test adding a restaurant
            //System.out.println(Arrays.asList(serverResponse));
            JsonNode node = new ObjectMapper().readTree(serverResponse[0].substring(serverResponse[0].indexOf("{"), serverResponse[0].lastIndexOf("}") + 1));
            assertEquals("Test Restaurant", node.get("name").asText());
            assertEquals(-122.5122, node.get("longitude").asDouble(), 0.1);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void test3() {
        if(!server.isAlive()) server.start();
        final String[] serverResponse = new String[1];

        Thread client = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    YelpClient yelpClient = new YelpClient("localhost", 4949);
                    yelpClient.sendRequest("ADDREVIEW {\"business_id\": \"1CBs84C-a-cuA3vncXVSAw\", \"text\": \"Some test string Test\", \"stars\": 3, \"user_id\": \"90wm_01FAIqhcgV_mPON9Q\"}");
                    serverResponse[0] = yelpClient.getReply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        client.start();
        try {

            client.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            // Test adding a restaurant
            JsonNode node = new ObjectMapper().readTree(serverResponse[0].substring(serverResponse[0].indexOf("{"), serverResponse[0].lastIndexOf("}") + 1));
            assertEquals("1CBs84C-a-cuA3vncXVSAw", node.get("business_id").asText());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void test4() {
        // Test adding a restaurant and retrieving it
        if(!server.isAlive()) server.start();
        final String[] serverResponse = new String[1];

        Thread client1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    YelpClient yelpClient = new YelpClient("localhost", 4949);
                    yelpClient.sendRequest("ADDRESTAURANT {\"longitude\": -122.5822, \"name\": \"Other Restaurant\", \"latitude\": 37.28541, \"state\": \"CA\", \"price\": 1}");
                    serverResponse[0] = yelpClient.getReply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread client2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    YelpClient yelpClient = new YelpClient("localhost", 4949);
                    yelpClient.sendRequest("ADDRESTAURANT {\"longitude\": -122.5822, \"name\": \"Other Restaurant\", \"latitude\": 37.28541, \"state\": \"CA\", \"price\": 1}");
                    serverResponse[0] = yelpClient.getReply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        client1.start();
        try {
            client1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            // Test adding a restaurant
            JsonNode node = new ObjectMapper().readTree(serverResponse[0].substring(serverResponse[0].indexOf("{"), serverResponse[0].lastIndexOf("}") + 1));
            String businessId = node.get("business_id").asText();

            YelpClient yelpClient = new YelpClient("localhost", 4949);
            yelpClient.sendRequest("GETRESTAURANT " + businessId);
            serverResponse[0] = yelpClient.getReply();

            assertTrue(serverResponse[0].contains("Other Restaurant"));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void test5() throws InterruptedException {
        // Test invalid strings
        if(!server.isAlive()) server.start();
        try {
            YelpClient yelpClient = new YelpClient("localhost", 4949);
            yelpClient.sendRequest("ADDRESTAURANT {\"longitude\": -122.5122, \"latitude\": 37.98541, \"state\": \"CA\", \"price\": 1}");
            assertTrue(yelpClient.getReply().contains("ERR: INVALID_RESTAURANT_STRING"));

            yelpClient.sendRequest("ADDREVIEW estyhiyu34wre");
            assertTrue(yelpClient.getReply().contains("INVALID_REVIEW_STRING"));

            yelpClient.sendRequest("ADDUSER {\"n\": \"Test\"}");
            assertTrue(yelpClient.getReply().contains("ERR: INVALID_USER_STRING"));

            yelpClient.sendRequest("ADDBUSINESS {\"longitude\": -122.5122,}");
            assertTrue(yelpClient.getReply().contains("ERR: ILLEGAL_REQUEST"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test6() {
        // Server query
        if(!server.isAlive()) server.start();
        final String[] serverResponse = new String[1];

        try {
            YelpClient yelpClient = new YelpClient("localhost", 4949);
            yelpClient.sendRequest("QUERY in(Telegraph Ave) && (category(Chinese) || category(Italian)) && price <= 2");
            serverResponse[0] = yelpClient.getReply();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(serverResponse[0]);
        assertTrue(serverResponse[0].length() > 5);
    }
}
