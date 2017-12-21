package ca.ece.ubc.cpen221.mp5.statistics;

import ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurant;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * KMeans finds the clustering of restaurant groups in a database. In this case, clustering is achieved by
 * setting a defined number of centroids and merging restaurants close to the centroid location with them
 * to create unique and distinct clusters, until a final stable status has been achieved.
 *
 */
public class KMeans {

	/*
	 * Representation Invariant:
	 * 		- a restaurant must only be clustered to one unique centroid location, it cannot be shared.
	 * 		- in the final iteration, restaurants clustered to a specific centroid must be closer to it it terms
	 * 		  of distance than any other available centroid location.
	 * 		- the keys of currentClusters must be equal to the number of required clusters in the database.
	 * 		- the size of updatedCentroids must be equal to the number of required clusters.
	 * 		- lastClusterPoints must only contain values that have been attained as centroid locations.
	 *
	 * Abstraction Function:
	 * 		- using a known database, groups restaurants together to specific centroid locations using their
	 * 	      geographical locations as indicators.
	 * 	    - in other words, restaurants nearby one another in terms of location in the database are grouped together.
	 */
	private static final double MAX_VALUE = 9999999;

	private final Map<Point2D, Set<YelpRestaurant>> currentClusters;
	private final List<YelpRestaurant> allYelpRestaurants;
	private final List<Point2D> updatedCentroids;
	private final Map<YelpRestaurant, Point2D> allPoints;
	private final int numberOfClusters;
	private final List<Double> lastClusterPoints;

	/**
	 * Create a Constructor for KMeans
	 * 
	 * @param allRestaurants, which:
	 * 		- is not null.
	 * 	    - is the list of all restaurants currently in the database.
	 * @param k, which:
	 * 		- is not null.
	 * 	    - is the number of clusters to be made with the database restaurant groupings.
	 */
	public KMeans(List<YelpRestaurant> allRestaurants, int k) {
		// Initialize all instance variables
		this.numberOfClusters = k;
		this.currentClusters = new HashMap<>();
		this.lastClusterPoints = new ArrayList<>();
		this.updatedCentroids = new ArrayList<>();
		this.allYelpRestaurants = allRestaurants;
		this.allPoints = getAllPoints();

		// Used to later address an edge case where the number of groupings is the same number
		// as the number of restaurants.
		if(k != allRestaurants.size()){
			findClusters();
		}
	}

	/**
	 * Continuously finds new clusters until a final stable set of clusters has been reached.
	 */
	private void findClusters() {

		// First we get the initial grouping of YelpRestaurants
		getFirstClusters();

		// Loop until there are no more changes in the KMeans structure
		while (getNewCentroids()) {
			findNewClusters();
		}
	}

	/**
	 * Finds the initial set of cluster groups in the database. Is primarily used to set first
	 * centroid locations and group nearby restaurants to that location.
	 */
	private void getFirstClusters() {

		// We simply use the first number of YelpRestaurants in our list for starting
		for (int i = 0; currentClusters.keySet().size() < numberOfClusters; i++) {
			Point2D initialCentroid = allPoints.get(allYelpRestaurants.get(i));
			if (!currentClusters.containsKey(initialCentroid)) {
				currentClusters.put(initialCentroid, new HashSet<>());
			}
		}

		// For each YelpRestaurant, we compare their point relative to each center to find the
		// centroid that is the shortest distance away.
		for (YelpRestaurant place : allPoints.keySet()) {
			double minimumDistance = MAX_VALUE;
			Point2D point = allPoints.get(place);
			Point2D closestCentroid = null;

			for (Point2D centroid : currentClusters.keySet()) {
				double distance = getDistance(centroid.getX(), centroid.getY(), point.getX(), point.getY());
				if (distance < minimumDistance) {
					minimumDistance = distance;
					closestCentroid = centroid;
				}
			}

			currentClusters.get(closestCentroid).add(place);

		}

	}

	/**
	 * Finds all points in the database, using the YelpRestaurant's longitude and latitude.
	 */
	private Map<YelpRestaurant, Point2D> getAllPoints() {
		Map<YelpRestaurant, Point2D> allPoints = new HashMap<>();
		for (YelpRestaurant place : allYelpRestaurants) {
			// Use latitude and longitude for points, where:
			// X = latitude, Y = longitude.
			Point2D point = new Point2D.Double(place.getLatitude(), place.getLongitude());
			allPoints.put(place, point);
		}
		return allPoints;
	}

	/**
	 * After getFirstClusters() or findNewClusters() have been called, we calculate the locations of
	 * the new centroid points by taking summation of longitudinal and latitudinal values of each Restaurant
	 * in a cluster and dividing by the total number of restaurants. In other words, we find the the new
	 * average center location of the new cluster of Restaurants.
	 */
	private boolean getNewCentroids() {
		List<Point2D> newCentroids = new ArrayList<>();
		lastClusterPoints.clear();
		// Identifier to later check if any changes have been made to the cluster formation
		boolean hasChange = true;

		// We first iterate through the current clusters and find the new centroid location for each
		for (Point2D centroids : currentClusters.keySet()) {
			lastClusterPoints.add(centroids.getX());
			lastClusterPoints.add(centroids.getY());
			double newX = 0;
			double newY = 0;
			double totalYelpRestaurants = 0;

			for (YelpRestaurant clusteredYelpRestaurants : currentClusters.get(centroids)) {
				newX += clusteredYelpRestaurants.getLatitude();
				newY += clusteredYelpRestaurants.getLongitude();
				totalYelpRestaurants++;
			}

			Point2D newCentroidPoint = new Point2D.Double(newX / totalYelpRestaurants, newY / totalYelpRestaurants);
			newCentroids.add(newCentroidPoint);

		}

		updatedCentroids.clear();
		updatedCentroids.addAll(newCentroids);

		// We now check to see if there are any changes with the current set of centroid points with the previous
		// iteration's centroid points - if there are no changes, this is the final formation. Else, we there is a
		// change and we must keep finding new clusters.
		for (Point2D newCentroid : updatedCentroids) {
			if (lastClusterPoints.contains(newCentroid.getX()) && lastClusterPoints.contains(newCentroid.getY())) {
				hasChange = false;
			} else {
				hasChange = true;
				break;
			}
		}

		return hasChange;
	}

	/**
	 * After an initial distribution of centroid and clusters, finds new cluster groups within the database using
	 * the current centroid locations as reference. Restaurants are clustered to the centroid location they are
	 * closest to, which is determined by finding the distance between their geographical location and that point.
	 */
	private void findNewClusters() {
		// First we clear the current clusters so that we can create a new distribution of clusters
		currentClusters.clear();
		// Reinitialize the currentClusters Map using our updated centroid points
		for (Point2D centroid : updatedCentroids) {
			currentClusters.put(centroid, new HashSet<>());
		}

		// We now cluster restaurants together with the closest centroid location
		for (YelpRestaurant place : allPoints.keySet()) {
			double minimumDistance = MAX_VALUE;
			Point2D point = allPoints.get(place);
			Point2D closestCentroid = null;

			// We use the distance between centroid locations and the restaurant location to determine distribution
			for (Point2D centroid : currentClusters.keySet()) {
				double distance = getDistance(centroid.getX(), centroid.getY(), point.getX(), point.getY());
				if (distance < minimumDistance) {
					minimumDistance = distance;
					closestCentroid = centroid;
				}

			}

			if (closestCentroid != null) {
				currentClusters.get(closestCentroid).add(place);
			}

		}
	}

	/**
	 * Calculates the distance between two points, using their x and y values.
	 */
	private double getDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}

	/**
	 * Transforms the current final mapping of clusters and centroid locations to a format that can be
	 * used later to easily obtain data from. Also, accounts for the edge case where the number of required
	 * clusters is equal to the number of possible restaurants.
	 *
	 * @return a List<Map<String, Object>>, which:
	 * 		- is a formatted description of the attributes of each restaurant and centroid in the final cluster.
	 */
	public List<Map<String, Object>> toList(){
		List<Map<String, Object>> results = new ArrayList<>();

		int cluster = 0;
		if (numberOfClusters != allYelpRestaurants.size()) {
			for (Point2D point : currentClusters.keySet()) {
				for (YelpRestaurant restaurant : currentClusters.get(point)) {
					Map<String, Object> entry = new LinkedHashMap<>();
					entry.put("x", restaurant.getLatitude());
					entry.put("y", restaurant.getLongitude());
					entry.put("name", restaurant.getName());
					entry.put("cluster", cluster);
					entry.put("weight", 1.0);
					entry.put("centroid", point);
					results.add(entry);
				}
				cluster++;
			}
		} else {
			for (int i = 0; i < numberOfClusters; i++){
				Map<String, Object> entry = new LinkedHashMap<>();
				entry.put("x", allYelpRestaurants.get(i).getLatitude());
				entry.put("y", allYelpRestaurants.get(i).getLongitude());
				entry.put("name", allYelpRestaurants.get(i).getName());
				entry.put("cluster", i);
				entry.put("weight", 1.0);
				entry.put("centroid", new Point2D.Double((Double)entry.get("x"), (Double)entry.get("y")));
				results.add(entry);
			}
		}
		return results;
	}

	/**
	 * Takes a formatted version of the final cluster formation and writes it to a file format.
	 *
	 * @param pathname, which:
	 * 		- is not null;
	 * 	    - is the location in which to store the newly generated file.
	 */
	public void toFile(String pathname) throws IOException {
		List<Map<String, Object>> results = toList();
		for(Map map : results){
			map.remove("centroid");
		}
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(new File(pathname), results);
	}

	/**
	 * Takes a formatted version of the final cluster formation and converts it to JSON format.
	 *
	 * @return a String, which:
	 * 		- is the results of the clustering in JSON format.
	 */
	public String toJson() {
		List<Map<String, Object>> results = toList();
		for(Map map : results){
			map.remove("centroid");
		}
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			return objectMapper.writeValueAsString(results);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Obtains all centroid locations in the current clustering of the database restaurants. All centroid
	 * points consist of two values (longitude and latitude).
	 *
	 * @return a List<Point2D>, which:
	 * 		- is a list of 2D points, describing the locations of all current centroids.
	 */
	public List<Point2D> getCentroids(){
		List<Point2D> centroids = new ArrayList<>();
		if(numberOfClusters == allYelpRestaurants.size()){
			for (YelpRestaurant restaurant : allYelpRestaurants)
			centroids.add(new Point2D.Double(restaurant.getLongitude(), restaurant.getLatitude()));
		} else {
			centroids.addAll(currentClusters.keySet());
		}
		return centroids;
	}

	/**
	 * Returns a list of all sets o clustered restaurants (as requested in the MP Document).
	 *
	 * @return a List<Set<YelpRestaurant>>, which:
	 * 		- is a list containing sets of restaurants, each set representing a unique cluster.
	 */
	public List<Set<YelpRestaurant>> getListofSets() {
		List<Set<YelpRestaurant>> toReturn = new ArrayList<>();
		toReturn.addAll(currentClusters.values());

		return toReturn;
	}

}
