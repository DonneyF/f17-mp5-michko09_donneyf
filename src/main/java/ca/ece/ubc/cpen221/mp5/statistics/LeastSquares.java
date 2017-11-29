package ca.ece.ubc.cpen221.mp5.statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.ToDoubleBiFunction;

import ca.ece.ubc.cpen221.mp5.MP5Db;
import ca.ece.ubc.cpen221.mp5.datastructure.User;

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
public class LeastSquares implements MP5Db<Object> {
	private List<Double> allStars;
	private List<Double> allPrices;
	private List<String> allBusinesses;
	private double SxxAverage;
	private double SyyAverage;

	public LeastSquares() {
		this.allStars = new ArrayList<Double>();
		this.allPrices = new ArrayList<Double>();
		this.allBusinesses = new ArrayList<String>();
		this.SxxAverage = 0;
		this.SyyAverage = 0;
	}

	@Override
	public Set getMatches(String queryString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String kMeansClusters_json(int k) {
		// TODO Auto-generated method stub
		return null;
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
	@Override
	public ToDoubleBiFunction getPredictorFunction(String user) {
		/**
		 * ToDoubleBiFunction<dataBase, restaurant_id> predict = (x,y) NEED DATABASE IMPLEMENTATION ON THIS
		 */
		
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Obtains a list of all the star ratings the user has given out.
	 * @param user
	 */
	void getStars(String user) {
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
	}
	
	/**
	 * Obtains a list of all prices associated with the business that the user has done 
	 * a review on.
	 */
	void getPrices() {
		/**
		 * I have not been able to "write" this code in this class because I don't know what 
		 * datatype is going to be. From what we've talked I think its just going to be a massive
		 * list of tables.
		 * 
		 * Order:
		 * 1. Stream database.
		 * 2. Map each entry to their respective type (business, user, review)
		 * 3. Remove all entries that are not reviews.
		 * 4. Find the user_id of each review
		 * 5. Remove reviews that are not written by the user.
		 * 6. Find the corresponding business_id of each review and store in a list.
		 * 
		 * allBusinesses = database.stream().map(type -> database.getData("type").toString()).filter(type -> type.equals("review")).map(user_id -> database.getData(user_id).toString())
		 * 					.filter(user_id -> user_id.equals(user)).map(String::getData(business_id).toString());
		 * 
		 * Order:
		 * 1. Stream database.
		 * 2. Map each entry to its type.
		 * 3. Remove all entries that are not of type business.
		 * 4. Get the business_id of all remaining entries.
		 * 5. Remove businesses that are not contained within the list.
		 * 6. Get prices of remaining businesses and sum them.
		 * 
		 * allPrices = database.stream().map(type -> database.getData("type").toString()).filter(type -> type.equals("business")).map(business_id -> database.getData(business_id).toString())
		 * 					.filter(business_id -> allBusinesses.contains(business_id)).map(Double::(double) getData(price).toString()).collect(Collectors.toList());
		 */
	}
	
	/**
	 * Obtains the result for Sxx defined in the outline.
	 * @return
	 */
	double Sxx() {
		double average = 0;
		double sum = 0;
		
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
		sum = allPrices.stream().map(square -> Math.pow(square - SxxAverage, 2)).reduce(0.0, Double::sum);
		
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
		sum = allStars.stream().map(square -> Math.pow(square - SyyAverage, 2)).reduce(0.0, Double::sum);
		
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