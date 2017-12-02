package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.interfaces.Table;
import ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurant;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

public class TablesTest {

    @Test
    public void test1(){
        Table<String> table = new Table<>();
        table.addEntry("SomeID", "SomeValue");
        table.addEntry("AnotherID", "AnotherValue");
        assertTrue(table.containsEntry("SomeID"));
        table.clearEntry("SomeID");
        assertTrue(table.getData("SomeID").size() == 0);

        assertTrue(table.numEntries() == 2);

        table.addEntry("AnotherID", "SomeOtherID");
        assertTrue(table.numValues() == 2);
    }

    @Test
    public void test3(){
        try {
            Map<String, Object> map = new HashMap<>();
            //You can convert any Object.
            String[] value1 = new String[]{"value11", "value12", "value13"};
            String[] value2 = new String[]{"value21", "value22", "value23"};
            map.put("key1", value1);
            map.put("key2", value2);
            map.put("key3", "string1");
            map.put("key4", "string2");

            String json = new ObjectMapper().writeValueAsString(map);
            System.out.println(json);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void test2(){

        try {
            File file = new File("data/restaurants.json");

            BufferedReader br = new BufferedReader(new FileReader(file));


            ObjectMapper mapper = new ObjectMapper();
            //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
/*            SimpleModule module = new SimpleModule();
            module.addDeserializer(YelpRestaurant.class, new YelpUserDeserializer());

            mapper.registerModule(module);*/
            String line = br.readLine();
            YelpRestaurant restaurant = mapper.readValue(line, YelpRestaurant.class);

            System.out.println(line);

            //assertEquals("Chris M.", user.getSchools());

            System.out.print(restaurant.getLatitude());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test4(){
        String defaultRestaurant = "{\"open\": true, \"url\": \"http://www.yelp.com/\", \"neighborhoods\": [], " +
                "\"business_id\": \"gclB3ED6uk6viWlolSb_uA\", \"name\": \"Cafe 3\", \"categories\": [], \"type\": " +
                "\"business\", \"review_count\": 0, \"schools\": []}";

        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);

        String possibleJSONString = "{\"name\": \"Sathish G.\", \"open\": false}";

        try {
            ObjectNode genericRestaurant = (ObjectNode) mapper.readTree(defaultRestaurant);
            ObjectNode restaurant = genericRestaurant.deepCopy();
            // Verify JSON by adding to the ObjectNode. Copy statements from input to generic and set values
            ObjectNode input = (ObjectNode) mapper.readTree(possibleJSONString);
            input.put("url", "http://www.yelp.com/biz/" + input.get("name").asText().
                    replaceAll("\\s", "-").replaceAll("[^a-zA-Z0-9\\\\s\\-]","").toLowerCase());           restaurant.setAll(input);
            System.out.println(restaurant);
            assertEquals(restaurant.get("name").asText(), "Sathish G.");
            assertFalse(restaurant.get("open").asBoolean());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void test5(){
        System.out.println(UUID.randomUUID().toString().replaceAll("[\\-]","").substring(0, 22));
    }
}
