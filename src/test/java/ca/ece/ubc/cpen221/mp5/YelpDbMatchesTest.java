package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.yelp.YelpDb;
import ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurant;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class YelpDbMatchesTest {

    private static final YelpDb db = new YelpDb("data/restaurants.json", "data/reviews.json", "data/users.json");
    private static final List<YelpRestaurant> allRestaurants = db.getRestaurants();

    /*
     * Testing methodology:
     *      - Construct the database and aqcuire a list of all restaurants to check against
     *      - Get a set of restaurants from the query
     *      - For every condition to check, we use an if-statement. Example: in(Telegraph Ave)
     *      - If there is an &&, we go down one level of nesting for the if-statement, because the analysis must satisfy both if-statements
     *      - If there is an ||, we compare the on the same level of nesting
     *      - We check the total number of matches as well as ensuring that the potential match exists on the set that we acquired earlier
     */

    @Test
    public void test1() {
        Set<YelpRestaurant> restaurantSet = db.getMatches("in(Telegraph Ave) && (category(Chinese) || category(Italian)) && price <= 2");

        int numMatches = 0;
        for (YelpRestaurant currentRestaurant : allRestaurants) {
            if (currentRestaurant.getNeighborhoods().contains("Telegraph Ave")) {
                if (currentRestaurant.getCategories().contains("Chinese") || currentRestaurant.getCategories().contains("Italian")) {
                    if (currentRestaurant.getPrice() <= 2) {
                        numMatches++;
                        assertTrue(restaurantSet.contains(currentRestaurant));
                    }
                }
            }
        }
        assertEquals(numMatches, restaurantSet.size());
    }

    @Test
    public void test2() {
        Set<YelpRestaurant> restaurantSet = db.getMatches("in(Telegraph Ave) && (category(Chinese) || category(Italian)) " +
                "&& (price = 2 || price = 4)");

        int numMatches = 0;
        for (YelpRestaurant currentRestaurant : allRestaurants) {
            if (currentRestaurant.getNeighborhoods().contains("Telegraph Ave")) {
                if (currentRestaurant.getCategories().contains("Chinese") || currentRestaurant.getCategories().contains("Italian")) {
                    if (currentRestaurant.getPrice() == 2 || currentRestaurant.getPrice() == 4) {
                        numMatches++;
                        assertTrue(restaurantSet.contains(currentRestaurant));
                    }
                }
            }
        }
        assertEquals(numMatches, restaurantSet.size());
    }

    @Test
    public void test3() {
        Set<YelpRestaurant> restaurantSet = db.getMatches("(in(Telegraph Ave) || in(UBC)) && (category(Chinese) || category(Italian) " +
                "|| category(Korean)) && (price = 2 || price = 4 || price = 5)");

        int numMatches = 0;

        for (YelpRestaurant currentRestaurant : allRestaurants) {
            if (currentRestaurant.getNeighborhoods().contains("Telegraph Ave") || currentRestaurant.getNeighborhoods().contains("UBC")) {
                if (currentRestaurant.getCategories().contains("Chinese") || currentRestaurant.getCategories().contains("Italian") || currentRestaurant.getCategories().contains("Korean")) {
                    if (currentRestaurant.getPrice() == 2 || currentRestaurant.getPrice() == 4 || currentRestaurant.getPrice() == 5) {
                        numMatches++;
                        assertTrue(restaurantSet.contains(currentRestaurant));
                    }
                }
            }
        }
        assertEquals(numMatches, restaurantSet.size());
    }

    @Test
    public void test4() {
        Set<YelpRestaurant> restaurantSet = db.getMatches("(name(Boston's Pizza) || in(UBC)) && (category(Chinese) || category(Italian) " +
                "|| category(Korean)) && (price = 2 || price = 4 || price = 5 || rating = 5)");

        int numMatches = 0;
        for (YelpRestaurant currentRestaurant : allRestaurants) {
            if (currentRestaurant.getName().equals("Boston's Pizza") || currentRestaurant.getNeighborhoods().contains("UBC")) {
                if (currentRestaurant.getCategories().contains("Chinese") || currentRestaurant.getCategories().contains("Italian") || currentRestaurant.getCategories().contains("Korean")) {
                    if (currentRestaurant.getPrice() == 2 || currentRestaurant.getPrice() == 4 || currentRestaurant.getPrice() == 5 || currentRestaurant.getStars() == 5) {
                        numMatches++;
                        assertTrue(restaurantSet.contains(currentRestaurant));
                    }
                }
            }
        }
        assertEquals(numMatches, restaurantSet.size());
    }

    @Test
    public void test5() {
        Set<YelpRestaurant> restaurantSet = db.getMatches("category(Chinese) && (rating = 4.5 || rating = 2)");

        int numMatches = 0;
        for (YelpRestaurant currentRestaurant : allRestaurants) {
            if (currentRestaurant.getCategories().contains("Chinese")) {
                if (currentRestaurant.getStars() == 4.5 || currentRestaurant.getStars() == 2) {
                    numMatches++;
                    assertTrue(restaurantSet.contains(currentRestaurant));
                }
            }
        }
        assertEquals(numMatches, restaurantSet.size());
    }
    @Test
    public void test6() {
        Set<YelpRestaurant> restaurantSet = db.getMatches("category(Chinese & Italian) || price = 5 || rating = 5");

        int numMatches = 0;
        for (YelpRestaurant currentRestaurant : allRestaurants) {
            if ((currentRestaurant.getCategories().contains("Chinese") && currentRestaurant.getCategories().contains("Italian"))
                    || currentRestaurant.getPrice() == 5 || currentRestaurant.getStars() == 5) {
                    numMatches++;
                    assertTrue(restaurantSet.contains(currentRestaurant));
            }
        }
        assertEquals(numMatches, restaurantSet.size());
    }
}

