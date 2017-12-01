package ca.ece.ubc.cpen221.mp5.statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.Collectors;

import ca.ece.ubc.cpen221.mp5.yelp.YelpDb;
import ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurant;
import ca.ece.ubc.cpen221.mp5.yelp.YelpReview;

public class LeastSquares {
	private List<Double> allStars;
	private List<Double> allPrices;
	private YelpDb database;
	private double SxxAverage;
	private double SyyAverage;

	public LeastSquares(YelpDb database) {
		this.allStars = new ArrayList<>();
		this.allPrices = new ArrayList<>();
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

		System.out.println("R^2: " + r_squared);

		// a + x * b where x is the price of the restuarant

		return new ToDoubleBiFunction<YelpDb, YelpRestaurant>() {
			@Override
			public double applyAsDouble(YelpDb database1, YelpRestaurant restaurant) {
				return a + restaurant.getPrice() * b;
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