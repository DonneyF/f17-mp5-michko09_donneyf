package ca.ece.ubc.cpen221.mp5.statistics;

import ca.ece.ubc.cpen221.mp5.yelp.YelpDb;
import ca.ece.ubc.cpen221.mp5.yelp.YelpReview;

import java.util.LinkedList;
import java.util.List;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.Collectors;

/**
 * LeastSquares utilizes the rating history of a specific user to create a customize function that predicts his/her
 * future ratings on a different restaurant using data in a current database in the future. Also uses linear regression
 * analysis to model the behavior as a linear function.
 *
 * Representation Invariant:
 * 		- Unless more elements are added into the regression analysis, a specific user only has one linear function
 * 		  to predict his/her future ratings.
 * 		- SxxAverage and SyyAverage must be positive.
 * 		- Sxx() and Syy() can only return positive doubles.
 * 		- Sxx(), Syy(), and Sxy() can return no more than one unique value anytime it is called for a specific user,
 * 		  unless input database conditions are changed.
 * 		- allStars and allPrices must be of the same size.
 *
 * Abstraction Function:
 * 		- Takes into account the users history in a database, specifically what ratings he/she gave to a restaurant
 * 		  with a specific price, and translates that behavior into a function that can predict what rating the user
 * 		  will give to a specific restaurant in the future.
 */
public class LeastSquares {
	// Outline instance fields used in this class
	private List<Double> allStars;
	private List<Double> allPrices;
	private final YelpDb database;
	private double SxxAverage;
	private double SyyAverage;

	/**
	 * Creates a LeastSquares constructor that initializes major instance fields.
	 *
	 * @param database, which:
	 * 		- is not null.
	 * 	    - is a correctly structured Yelp database containing information of users, reviews, and restaurants.
	 */
	public LeastSquares(YelpDb database) {
		this.allStars = new LinkedList<>();
		this.allPrices = new LinkedList<>();
		this.database = database;
	}

	/**
	 * Predicts the future ratings that a user will give to a particular restaurant through analyzing the user's past
	 * behavior in reviews. Uses two main categories in the creating of the prediction - the restaurant's price and
	 * the corresponding user's rating of that restaurant.
	 *
	 * @param user, which:
	 * 		- is not null;
	 * 	    - the user ID of a particular user in the current database.
	 *
	 * @return a ToDoubleBiFunction, which:
	 * 		- is a function that will take two inputs, a database and a restaurant ID, and will calculate the predicted
	 * 		  rating of that restaurant using the linear regression analysis.
	 */
	public ToDoubleBiFunction getPredictorFunction(String user) {
		// Edge case where there is not sufficient data to undergo a linear regression analysis on the user.
		if(database.getUser(user).getReviewCount() <= 1) throw new IllegalArgumentException("User has one rating or less");

		// We then stream through the database to find the prices of each restaurant the user has reviewed
		allPrices = database.getReviews().parallelStream().filter(review -> review.getUserId().equals(user)).
				map(YelpReview::getBusinessId).map(businessID -> database.getRestaurant(businessID).
				getPrice()).map(price -> (double) price).collect(Collectors.toList());

		// We stream the database to find the corresponding stars the user has given to the restaurants
		allStars = database.getReviews().parallelStream().filter(review -> review.getUserId().equals(user)).
				map(YelpReview::getStars).map(stars -> (double) stars).collect(Collectors.toList());

		// Sxy must be computed last. All the algorithms and instance fields are given in the MP5 Outline.
		double Sxx = Sxx();
		double Syy = Syy();
		double Sxy = Sxy();
		double b = Sxy / Sxx ;
		double r_squared = Math.pow(Sxy , 2) / (Syy * Sxx );
		double a = SyyAverage - b * SxxAverage;

		// a + x * b where x is the price of the restuarant

		return new ToDoubleBiFunction<YelpDb, String>() {
			/**
			 * Predicts the a user's expected rating on a restaurant;
			 * @param database is not null and contains a restaurant with restaurantId as its ID and an associated price
			 * @param restaurantId represents the restaurant to generate a prediction for
			 * @return a rating between 1 and 5 that represents the user's expected rating for this restaurant in this database.
			 */
			@Override
			public double applyAsDouble(YelpDb database, String restaurantId) {
				return a + database.getRestaurant(restaurantId).getPrice() * b;
			}

		};
	}
	
	/**
	 * Obtains the result for Sxx defined in the outline. The algorithm is as follows:
	 * 		summation of (x_i-mean(x))^2, where:
	 * 		- x_i is a price of a restaurant the user has reviewed
	 * 		- mean(x) is the average value of all the prices of restaurants the user has reviewed.
	 *
	 * @return a double, which:
	 * 		- is the final value resulting from the summation.
	 */
	private double Sxx() {
		double average = 0;
		
		for (Double price : allPrices) {
			average += price;
		}
		
		SxxAverage = average / allPrices.size();

		return allPrices.parallelStream().map(price -> Math.pow(price - SxxAverage, 2)).mapToDouble(Double::doubleValue).sum();
	}
	
	/**
	 * Obtains the result for Syy defined in the outline. The algorithm is as follows:
	 * 		summation of (y_i-mean(y))^2, where:
	 * 		- y_i is a star the user has given to the restaurant in a review.
	 * 		- mean(y) is the average value of all the stars of restaurants the user has reviewed.
	 *
	 * @return a double, which:
	 * 		- is the final value resulting from the summation.
	 */
	private double Syy() {
		double average = 0;

		for (Double star : allStars) {
			average += star;
		}
		
		SyyAverage = average / allStars.size();

		return allStars.parallelStream().map(star -> Math.pow(star - SyyAverage, 2)).mapToDouble(Double::doubleValue).sum();
	}
	
	/**
	 * Obtains the result for Sxy defined in the outline. The algorithm is as follows:
	 * 		summation of (x_i-mean(x))*(y_i-mean(y)), where:
	 * 		- x_i is a price of a restaurant the user has reviewed
	 * 		- y_i is a star the user has given to the same restaurant in a review.
	 * 		- mean(x) is the average value of all the prices of restaurants the user has reviewed.
	 * 		- mean(y) is the average value of all the stars of restaurants the user has reviewed.
	 * 
	 * @return a double, which:
	 * 		- is the final value resulting from the summation.
	 */
	private double Sxy() {
		double sum = 0;
		
		// Simple for loop, just following algorithm shown in outline.
		for (int index = 0; index < allStars.size(); index++) {
			sum += (allPrices.get(index) - SxxAverage) * (allStars.get(index) - SyyAverage);

		}
		return sum;
	}
	
}