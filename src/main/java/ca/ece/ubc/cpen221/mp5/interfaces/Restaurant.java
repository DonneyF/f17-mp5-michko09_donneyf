package ca.ece.ubc.cpen221.mp5.interfaces;

import java.util.List;

/**
 * A Restaurant interface. Supports storing information of stars, food categories, number of reviews, and its price category
 */
public interface Restaurant extends Business {


    /**
     * Gets the rating of this restaurant
     *
     * @return a number between 1 and 5 representing the rating of this restaurant
     */
    double getStars();

    /**
     * Gets the food categories this restaurant belongs to
     *
     * @return A list of food categories this restaurant belongs to
     */
    List getCategories();

    /**
     * Gets the number of reviews this restaurant received
     *
     * @return an integer representing then number of reviews this restaurant received
     */
    int getReviewCount();

    /**
     * Gets the price category of this restaurant
     *
     * @return an integer between 1 and 5 that represents the price category of this restaurant
     */
    int getPrice();
}
