package ca.ece.ubc.cpen221.mp5.datastructure;

import java.util.ArrayList;
import java.util.List;

public class Restaurant extends Business {
	
	private Table restaurantTable;
	
	public Restaurant(Table table) {
		super(table);
		this.restaurantTable = table;
	}
	
	/**
	 * Obtains the neighborhoods nearby the restaurant.
	 * 
	 * @return a List of Strings, which: - is not null. - is a list of neighborhood names.
	 */
	List<String> getNeighborhoods() {
		List<String> finalList = new ArrayList<String>();
		List<Object> neighborhoods = restaurantTable.getData("neighborhoods");
		
		for (Object names : neighborhoods) {
			finalList.add(names.toString());
		}
		
		return finalList;
	}
	
	/**
	 * Obtains all categories of services offered by the restaurant.
	 * 
	 * @return a List of Strings, which: - is not null. - is a list of service types.
	 */
	List<String> getCategories() {
		List<String> finalList = new ArrayList<String>();
		List<Object> categories = restaurantTable.getData("categories");
		
		for (Object names : categories) {
			finalList.add(names.toString());
		}
		
		return finalList;
	}
	
	/**
	 * Obtains the average star rating of the restaurant.
	 * 
	 * @return a double, which: - is not null. - is the value of the current average stars rating of the restaurant.
	 */
	double getStars() {
		return (double) restaurantTable.getData("stars").get(0);
	}
	
	/**
	 * Obtains the number of reviews conducted on the restaurant.
	 * 
	 * @return an integer, which: - is not null. - is the number of completed reviews.
	 */
	int getReviewCount() {
		return (int) restaurantTable.getData("review_count").get(0);
	}
	
	/**
	 * Obtains the names of the schools that have the restaurant located inside them.
	 * 
	 * @return a double, which: - is not null. - is the value of the schools containing the restaurant.
	 */
	List<String> getSchools() {
		List<String> finalList = new ArrayList<String>();
		List<Object> schools = restaurantTable.getData("schools");
		
		for (Object names : schools) {
			finalList.add(names.toString());
		}
		
		return finalList;
	}
	
	/**
	 * Obtains the customer's evalution of pricing of the restaurant.
	 * 
	 * @return an integer, which: - is not null. - is the score regarding pricing of the restaurant menu.
	 */
	int getPrice() {
		return (int) restaurantTable.getData("price").get(0);
	}
	
}
