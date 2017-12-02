package ca.ece.ubc.cpen221.mp5.statistics;

import ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurant;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class KMeans {
	private static final double MAX_VALUE = 9999999;

	private Map<Point2D, Set<YelpRestaurant>> currentClusters;
	// private List<Set<String>> clusters;
	private List<YelpRestaurant> allYelpRestaurants;
	private List<Point2D> updatedCentroids;
	// List<Point2D> centroids;
	private Map<YelpRestaurant, Point2D> allPoints;
	private int numberOfClusters;

	//private HashMap<Point2D, Set<YelpRestaurant>> lastCluster;
	private List<Double> lastClusterPoints;

	/**
	 * Create a Constructor for KMeans
	 * 
	 * @param placeHolder
	 * @param k
	 */
	public KMeans(List<YelpRestaurant> placeHolder, int k) {
		this.numberOfClusters = k;
		this.currentClusters = new HashMap<>();
		//this.lastCluster = new HashMap<>();
		this.lastClusterPoints = new ArrayList<>();
		this.updatedCentroids = new ArrayList<>();
		this.allYelpRestaurants = placeHolder;
		// this.centroids = new ArrayList<Point2D>();
		// this.clusters = new ArrayList<Set<String>>();
		this.allPoints = getAllPoints();
		if(k != placeHolder.size()){
			findClusters();
		}
	}

	/**
	 * Method to keep finding new clusters until the final one has been reached.
	 */
	private void findClusters() {

		// First we get the initial grouping of YelpRestaurants
		getFirstClusters();

		// getNewCentroids();

		// Set up the condition for the while loop

		// Loop until there are no more changes in the KMeans structure
		while (!(getNewCentroids())) {
			findNewClusters();
		}

	}

	/**
	 * Gets the first set of clusters in the database.
	 */
	private void getFirstClusters() {

		// We simply use the first number of YelpRestaurants in our list for starting
		for (int i = 0; currentClusters.keySet().size() < numberOfClusters; i++) {
			// centroids.add(allPoints.get(count));
			Point2D initialCentroid = allPoints.get(allYelpRestaurants.get(i));
			if (!currentClusters.containsKey(initialCentroid)) {
				currentClusters.put(initialCentroid, new HashSet<>());
			}
		}

		// For each YelpRestaurant, we compare their point relative to each center to find
		// the
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
	 * Finds all points in the database, using the YelpRestaurant's longitude and
	 * latitude.
	 */
	private Map<YelpRestaurant, Point2D> getAllPoints() {
		Map<YelpRestaurant, Point2D> allPoints = new HashMap<>();
		for (YelpRestaurant place : allYelpRestaurants) {
			// Use longitude and latitude for points
			Point2D point = new Point2D.Double(place.getLatitude(), place.getLongitude());
			allPoints.put(place, point);
		}
		return allPoints;
	}

	/**
	 * Finds all new centroid locations in the database.
	 */
	private boolean getNewCentroids() {
		List<Point2D> newCentroids = new ArrayList<>();
		lastClusterPoints.clear();
		boolean hasChange = true;

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

			// Make a last Map reference
			// Point2D pointCopy = new Point2D.Double(centroids.getX(), centroids.getY());
			// Set<YelpRestaurant> listCopy = new HashSet<YelpRestaurant>();
			// listCopy.addAll(currentClusters.get(centroids));
			// lastCluster.put(pointCopy, listCopy);

		}

		updatedCentroids.clear();
		updatedCentroids.addAll(newCentroids);

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
	 * Finds all new clusters resulting from the change in centroid locations.
	 */
	private void findNewClusters() {
		// boolean noChange = true;
		currentClusters.clear();
		// Reinitialize the currentClusters Map
		for (Point2D centroid : updatedCentroids) {
			// System.out.println(centroid.getX());
			// System.out.println(centroid.getY());
			currentClusters.put(centroid, new HashSet<>());
		}

		// Same code as above, perhaps we could just make this a method?
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

			if (closestCentroid != null) {
				currentClusters.get(closestCentroid).add(place);
				// noChange = false;
			}

		}

		// System.out.println(currentClusters.values());

		// if (!(currentClusters.values().equals(lastCluster.values()))) {
		// return false;
	}

	// return noChange;

	/**
	 * Calculates the distance between two points.
	 */
	private double getDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}

	public void parseResultsToJson() {
		List<Map<String, Object>> results = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();

		//private Map<Point2D, Set<YelpRestaurant>> currentClusters;

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
				results.add(entry);
			}
		}

		try {
			objectMapper.writeValue(new File("data/results.json"), results);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
