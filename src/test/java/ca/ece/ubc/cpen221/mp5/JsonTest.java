package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.statistics.KMeans;
import ca.ece.ubc.cpen221.mp5.yelp.YelpDb;
import ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurant;
import org.junit.Test;

import java.util.*;

public class JsonTest {
	/*
    @Test
    public void test1() {
        try {

            File file = new File("data/usertest.json");

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            //while ((line = br.readLine()) != null) {

                Map<String,HashMap> map = new HashMap<>();

                ObjectMapper mapper = new ObjectMapper();

                //map = mapper.readValue(line, new TypeReference<HashMap>(){});
                JsonNode node = mapper.readTree(line);

                //System.out.println(map);
                //HashMap hashMap = (HashMap) map.get("votes");

                //System.out.println(line);
                System.out.println(node);

/*            Iterator<String> fieldNames = node.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                System.out.println(fieldName);// prints title, message, errors,
                // total,
                // total_pages, page, limit, dataset
            }*/
	/*
            System.out.println(node.get("url").asText());

            JsonNode votes = node.get("votes");

            System.out.println(node.getNodeType());

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    */
    @Test
    public void test2(){
        YelpDb db = new YelpDb("data/restaurants.json", "data/reviews.json", "data/users.json");
        ArrayList<YelpRestaurant> list = new ArrayList(db.getRestaurants());
        System.out.println(list);

        //System.out.println(restaurant.getNeighborhoods());
        KMeans kMeans = new KMeans(new ArrayList<>(list), 2);

        kMeans.parseResultsToJson();
    }
    
    @Test
    public void test3() {
    	
    }

}
