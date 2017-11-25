package ca.ece.ubc.cpen221.mp5.statistics;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.ece.ubc.cpen221.mp5.datastructure.Restaurant;
import ca.ece.ubc.cpen221.mp5.datastructure.Table;

public class KMeans {
	public static final double MAX_VALUE = 9999999;
	
	Map<Point2D, Set<Restaurant>> currentClusters;
	List<Set<String>> clusters;
	List<Restaurant> allRestaurants;
	List<Point2D> updatedCentroids;
	//List<Point2D> centroids;
	Map<Restaurant, Point2D> allPoints;
	int numberOfClusters;

	/**
	 * Create a Constructor for KMeans
	 * 
	 * @param placeHolder
	 * @param k
	 */
	public KMeans(List<Restaurant> placeHolder, int k) {
		this.numberOfClusters = k;
		this.currentClusters = new HashMap<Point2D, Set<Restaurant>>();
		this.updatedCentroids = new ArrayList<Point2D>();
		this.allRestaurants = placeHolder;
		//this.centroids = new ArrayList<Point2D>();
		this.clusters = new ArrayList<Set<String>>();
		this.allPoints = getAllPoints();
		
		findClusters();
	}
	
	/**
	 * Method to keep finding new clusters until the final one has been reached.
	 */
	private void findClusters() {
		// First we get the initial grouping of restaurants
		getFirstClusters();
		getNewCentroids();
		// Set up the condition for the while loop
		getNewCentroids();
		
		// Loop until there are no more changes in the KMeans structure
		while(findNewClusters()) {
			getNewCentroids();
		}
		
	}

	/**
	 * Gets the first set of clusters in the database.
	 */
	private void getFirstClusters() {
		int count = 0;

		// We simply use the first number of Restaurants in our list for starting
		while (count < numberOfClusters) {
			//centroids.add(allPoints.get(count));
			Point2D initialCentroid = allPoints.get(count);
			Set<Restaurant> clusters = new HashSet<Restaurant>();
			currentClusters.put(initialCentroid, clusters);
			count++;
		}
		
		// For each restaurant, we compare their point relative to each center to find the 
		// centroid that is the shortest distance away. 
		for (Restaurant place : allPoints.keySet()) {
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
	 * Finds all points in the database, using the restaurant's longitude and latitude.
	 */
	private Map<Restaurant, Point2D> getAllPoints() {
		Map<Restaurant, Point2D> allPoints = new HashMap<Restaurant, Point2D>();
		
		for (Restaurant place : allRestaurants) {
			// Use longitude and latitude for points
			Point2D point = new Point2D.Double(place.getLongitude(), place.getLatitude());
			allPoints.put(place, point);
		}
		
		return allPoints;
	}
	
	/**
	 * Finds all new centroid locations in the database.
	 */
	private void getNewCentroids() {
		List<Point2D> newCentroids = new ArrayList<Point2D>();
		
		for (Point2D centroids : currentClusters.keySet()) {
			double newX = 0;
			double newY = 0;
			double totalRestaurants = 0;
			
			for (Restaurant clusteredRestaurants : currentClusters.get(centroids)) {
				newX += clusteredRestaurants.getLongitude();
				newY += clusteredRestaurants.getLatitude();
				totalRestaurants++;
			}
			
			Point2D newCentroidPoint = new Point2D.Double(newX / totalRestaurants, newY/ totalRestaurants);
			newCentroids.add(newCentroidPoint);
		}
		
		currentClusters.clear();
		updatedCentroids.clear();
		updatedCentroids.addAll(newCentroids);
	}
	
	/**
	 * Finds all new clusters resulting from the change in centroid locations.
	 */
	public boolean findNewClusters() {
		boolean noChange = true;
		
		for (Point2D centroid : updatedCentroids) {
			Set<Restaurant> clusters = new HashSet<Restaurant>();
			currentClusters.put(centroid, clusters);
		}
		
		for (Restaurant place : allPoints.keySet()) {
			double minimumDistance = MAX_VALUE;
			Point2D point = allPoints.get(place);
			Point2D closestCentroid = null;
			
			for (Point2D centroid : currentClusters.keySet()) {
				double distance = getDistance(centroid.getX(), centroid.getY(), point.getX(), point.getY());
				if (distance < minimumDistance) {
					minimumDistance = distance;
					closestCentroid = centroid;
					noChange = false;
				}
			}
			
			currentClusters.get(closestCentroid).add(place);
			
		}
		
		return noChange;
	}

	/**
	 * Calculates the distance between two points.
	 */
	private double getDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
}