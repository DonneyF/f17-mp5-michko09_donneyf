package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.datastructure.Table;
import ca.ece.ubc.cpen221.mp5.datastructure.User;
import ca.ece.ubc.cpen221.mp5.datastructure.Votes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

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
            File file = new File("data/usertest.json");

            BufferedReader br = new BufferedReader(new FileReader(file));

            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> map = mapper.readValue(br.readLine(), new TypeReference<Map<String, Object>>() {});

            Table<String> table = new Table(map);

            Votes votes = new Votes((HashMap) map.get("votes"));

            User user = new User(table, votes);

            assertEquals("Chris M.", user.getName());

            System.out.print(user.getVotes());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test4(){

    }
}
