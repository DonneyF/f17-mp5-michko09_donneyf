package ca.ece.ubc.cpen221.mp5.statistics;

import ca.ece.ubc.cpen221.mp5.yelp.YelpDb;
import ca.ece.ubc.cpen221.mp5.yelp.YelpReview;

import java.util.LinkedList;
import java.util.List;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.Collectors;

public class LeastSquares {
	private List<Double> allStars;
	private List<Double> allPrices;
	private final YelpDb database;
	private double SxxAverage;
	private double SyyAverage;

	public LeastSquares(YelpDb database) {
		this.allStars = new LinkedList<>();
		this.allPrices = new LinkedList<>();
		this.database = database;
	}


	public ToDoubleBiFunction getPredictorFunction(String user) {
		/**
		 * ToDoubleBiFunction<dataBase, restaurant_id> predict = (x,y) NEED DATABASE IMPLEMENTATION ON THIS
		 */
		if(database.getUserData(user).getReviewCount() <= 1) throw new IllegalArgumentException("User has one rating or less");

		allPrices = database.getReviews().parallelStream().filter(review -> review.getUserId().equals(user)).
				map(YelpReview::getBusinessId).map(businessID -> database.getRestaurantData(businessID).
				getPrice()).map(price -> (double) price).collect(Collectors.toList());


		allStars = database.getReviews().parallelStream().filter(review -> review.getUserId().equals(user)).
				map(YelpReview::getStars).map(stars -> (double) stars).collect(Collectors.toList());


		// Sxy must be computed last.
		double Sxx = Sxx();
		double Syy = Syy();
		double Sxy = Sxy();
		double b = Sxy / Sxx ;
		double r_squared = Math.pow(Sxy , 2) / (Syy * Sxx );
		double a = SyyAverage - b * SxxAverage;

		System.out.println(allPrices.toString() + allStars.toString());

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
				return a + database.getRestaurantData(restaurantId).getPrice() * b;
			}

		};
	}
	
	/**
	 * Obtains the result for Sxx defined in the outline.
	 * @return
	 */
	private double Sxx() {
		double average = 0;
		
		for (Double price : allPrices) {
			average += price;
		}
		
		SxxAverage = average / allPrices.size();

		/**
		 * Order:
		 * 1. Stream the list.
		 * 2. Find the summation result of the formula (x(i) - x(mean))^2 and map it.
		 * 3. Reduce the list using addition.
		 */
		return allPrices.parallelStream().map(price -> Math.pow(price - SxxAverage, 2)).mapToDouble(Double::doubleValue).sum();
	}
	
	/**
	 * Obtains the result for Syy defined in the outline.
	 * @return
	 */
	private double Syy() {
		double average = 0;

		for (Double star : allStars) {
			average += star;
		}
		
		SyyAverage = average / allStars.size();
		
		/**
		 * Order:
		 * 1. Stream the list.
		 * 2. Find the summation result of the formula (x(i) - x(mean))^2 and map it.
		 * 3. Reduce the list using addition.
		 */
		return allStars.parallelStream().map(star -> Math.pow(star - SyyAverage, 2)).mapToDouble(Double::doubleValue).sum();
	}
	
	/**
	 * Obtains the result for Sxy defined in the outline.
	 * 
	 * @return
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