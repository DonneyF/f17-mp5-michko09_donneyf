package ca.ece.ubc.cpen221.mp5.statistics;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.ece.ubc.cpen221.mp5.datastructure.Table;

public class KMeans {
	Map<String, Table> placeHolder;
	Set<String> allIDs;
	List<Set<String>> clusters;
	int numberOfClusters;
	
	/**
	 * Create a Constructor for KMeans
	 * @param placeHolder
	 * @param k
	 */
	public KMeans(Map<String, Table> placeHolder, int k) {
		this.numberOfClusters = k;
		this.placeHolder = placeHolder;
		this.allIDs = placeHolder.keySet();
		this.clusters = List<HashSet<String>>();
	}
	
	/**
	 * 
	 */
	//private getFirstClusters() {
		
	//}
}
