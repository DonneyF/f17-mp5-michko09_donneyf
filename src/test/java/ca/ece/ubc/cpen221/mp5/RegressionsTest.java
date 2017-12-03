package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.yelp.YelpDb;
import org.junit.Test;

import java.util.function.ToDoubleBiFunction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RegressionsTest {

    @Test
    public void test1() {
        YelpDb db = new YelpDb("data/restaurants.json", "data/reviews.json", "data/users.json");

        // eKWi4sFfjnrx-r9KUuhVqA" is John B.
        // Prices of restaurants: [2.0, 4.0, 3.0, 2.0]
        // His reviews, respectively: [5.0, 4.0, 4.0, 2.0]
        double ratingAverage = (5 + 4 + 4 + 2 ) / 4;
        ToDoubleBiFunction<YelpDb, String> function = db.getPredictorFunction("Y61y11_nYsbc2UWjDh8ZEA");

        // 8PE1KtG_ZMxcgqCseHhmLA is Bongo Burger. Price: 1.
        // Expected rating is greater than his average.
        double projected = function.applyAsDouble(db, "8PE1KtG_ZMxcgqCseHhmLA");

        // Check range
        System.out.println(projected);
        assertTrue((projected >= 1.0) && (projected<= 5.0));
        assertTrue(projected > ratingAverage);
    }

    @Test
    public void test2() {
        YelpDb db = new YelpDb("data/restaurants.json", "data/reviews.json", "data/users.json");

        ToDoubleBiFunction<YelpDb, String> function = db.getPredictorFunction("xVocUszkZtAqCxgWak3xVQ");

        double projected = function.applyAsDouble(db, "gclB3ED6uk6viWlolSb_uA");

        // Expected 1.6 from hand calculations
        assertEquals(projected, 1.6, 0.1);
    }
}
