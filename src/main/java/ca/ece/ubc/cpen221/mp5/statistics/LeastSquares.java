package ca.ece.ubc.cpen221.mp5.statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.Collectors;

import ca.ece.ubc.cpen221.mp5.MP5Db;
import ca.ece.ubc.cpen221.mp5.yelp.YelpDb;
import ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurant;
import ca.ece.ubc.cpen221.mp5.yelp.YelpReview;

/**
 * This one is a little bit more tricky than the one before.
 * 
 * TODO:
 * - I've basically written the code that gives us all the parameters we need (a, b, and r^2 are found 
 *   using Sxx, Syy, and Sxy). We need to implement database however, as my lambda expressions depend 
 *   on those.
 * 
 * - Again, I have not been able to test the functionality of my methods but tracing through it it should
 * 	 work unless I've done something horribly wrong.
 * 
 * - The ToDoubleBiFunction getPredictorFunction(String user) needs some work. I was thinking along the lines of:
 * 		1. We get the input database and restaurant_id from the user.
 * 		2. We do our Sxx, Syy, Sxy methods on the input database and then using linear analysis, rating = a +/- bx,
 * 		   where a and b are constants found using the equations in the outline and x is the price (scaling factor)
 * 		   of the input restaurant we can find the output.
 *	 But I have yet to implenent this as I was unsure of how database works.
 */
public class LeastSquares {
	private List<Double> allStars;
	private List<Double> allPrices;
	private YelpDb database;
	private double SxxAverage;
	private double SyyAverage;

	public LeastSquares(YelpDb database) {
		this.allStars = new ArrayList<>();
		this.allPrices = new ArrayList<>();
		this.SxxAverage = 0;
		this.SyyAverage = 0;
		this.database = database;
	}

	/**
	 * What we will do:
	 * 1. First check to see if the User has more than one rating (if lower this will break the algorithm). If so, 
	 * 	  throw an IllegalArgumentException or return zero, specifying that this value is a special case.
	 * 
	 * 2. If the user passes Step 1, we will use a all the reviews of that user by filtering out the database.
	 * 
	 *    Reviews contain the rating that the user has given, but need to use business_id to get the prices of each 
	 *    restaurant the user has reviewed.
	 *    
	 * 3. Once the reviews that do not contain the user_id have been filtered out, we will do a systematic analysis 
	 * 	  review by review, obtaining the business price and the corresponding stars. Since this part only requires 
	 * 	  the summation of the the squares, we can put those values in a list, get the length of the list, find the 
	 * 	  squares, and then reduce by summation.
	 * 
	 * 4. We then simply use the given formulas to find r_squared.
	 * 
	 * Assuming we have access to the all tables (User, Restaurant, Review):
	 * 		- Create a copy of the review table.
	 * 		- Map the table to user_id.
	 * 		- Filter out reviews that do not match the input user_id.
	 * 		- Obtain the user stars.
	 * 
	 * 		- Obtain business_id from reviews.
	 * 		- Create copy of the restaurant table.
	 * 		- Map the table to business_id.
	 * 		- Filter out all business that do not contain the id's stored in list.
	 * 		- Obtain prices of each business.
	 * 
	 * 		- Find the median of each list and then use the square regression formula (reduce).
	 *		- Apply given formulas for r_squared.
	 * 	 
	 */

	public ToDoubleBiFunction getPredictorFunction(String user) {
		/**
		 * ToDoubleBiFunction<dataBase, restaurant_id> predict = (x,y) NEED DATABASE IMPLEMENTATION ON THIS
		 */
		if(database.getUserData(user).getReviewCount() <= 1) throw new IllegalArgumentException("User has one rating or less");

		List<YelpReview> reviews = database.getReviews().stream().filter(review -> review.getUserId().equals(user)).collect(Collectors.toList());

		allPrices = getPrices(user);

		allStars = getStars(user);

		double Sxy = Sxy();
		double Sxx = Sxx();
		double Syy = Syy();
		double b = Sxy / Sxx ;
		double r_squared = Math.pow(Sxy , 2) / (Syy * Sxx );
		double a = SyyAverage - b * SxxAverage;

		System.out.println(Sxy);

		// a*x + b where x is the price of the restuarant

		ToDoubleBiFunction<YelpDb, YelpRestaurant> predict = new ToDoubleBiFunction<YelpDb, YelpRestaurant>() {
			@Override
			public double applyAsDouble(YelpDb database, YelpRestaurant restaurant) {
				return a * restaurant.getPrice() + b;
			}
		};

		return predict;
	}
	
	/**
	 * Obtains a list of all the star ratings the user has given out.
	 * @param userId
	 */
	public List<Double> getStars(String userId) {
		/**
		 * Order:
		 * 1. Stream database.
		 * 2. Get the type of each entry.
		 * 3. Filter entries that are not reviews.
		 * 4. Get user_id of each review
		 * 5. Remove reviews that the target user did not write.
		 * 6. Find and sum all star ratings the user gave in the reviews.
		 * 
		 * allStars = database.stream().map(type -> database.getData("type").toString()).filter(type -> type.equals("review")).map(user_id -> database.getData("user_id").toString())
		 * 					.filter(user_id -> user_id.equals(user)).map(Double::(double) getData(stars).toString());
		 */

		// Get a list of reviews
		return database.getReviews().stream().filter(review -> review.getUserId().equals(userId)).map(YelpReview::getStars).map(stars -> (double) stars).collect(Collectors.toList());
	}
	
	/**
	 * Obtains a list of all prices associated with the business that the user has done 
	 * a review on.
	 */
	public List<Double> getPrices(String userId) {

		return database.getReviews().stream().filter(review -> review.getUserId().equals(userId)).map(YelpReview::getBusinessId)
				.map(businessID -> database.getRestaurantData(businessID).getPrice()).map(price -> (double) price).collect(Collectors.toList());
	}
	
	/**
	 * Obtains the result for Sxx defined in the outline.
	 * @return
	 */
	double Sxx() {
		double average = 0;
		double sum;
		
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
		sum = allPrices.stream().map(price -> Math.pow(price - SxxAverage, 2)).reduce(0.0, Double::sum);

		return sum;
	}
	
	/**
	 * Obtains the result for Syy defined in the outline.
	 * @return
	 */
	double Syy() {
		double average = 0;
		double sum = 0;
		
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
		sum = allStars.stream().map(star -> Math.pow(star - SyyAverage, 2)).reduce(0.0, Double::sum);
		
		return sum;
	}
	
	/**
	 * Obtains the result for Sxy defined in the outline.
	 * 
	 * @return
	 */
	double Sxy() {
		int sum = 0;
		
		// Simple for loop, just following algorithm shown in outline.
		for (int index = 0; index < allStars.size(); index++) {
			sum += (allPrices.get(index) - SxxAverage) * (allStars.get(index) - SyyAverage);
		}
		return sum;
	}

	
}