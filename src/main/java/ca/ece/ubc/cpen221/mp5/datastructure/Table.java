package ca.ece.ubc.cpen221.mp5.datastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class:
 * 		Table - stores all "categories" or specific keys of the data structure.
 * 
 * Representation Invariant:
 * 		- 
 * 
 * Abstraction Function:
 * 		-
 * 
 */

public class Table {
  
    Map<String, List<Object>> masterData;
  	String ID;
  
  	/**
     * Table Constructor that takes a unique ID as an input.
     */
    public <T> Table(String ID) {
    	this.masterData = new HashMap<>();
      	this.ID = ID;
      	masterData.put(ID, new ArrayList<Object>());
    }

  	/**
     * Add a new entry or Modifies a current entry in the table.
     *
     * @param key, where:
     *      - it is not null.
     *      - it is a unique trait of the table type.
     * @param value, where:
     *      - it is not null.
     *      - it is a specific value type corresponding to the key.
     */
  	void addEntry(String key, String value) {
  		if (masterData.containsKey(key)){
  			masterData.get(key).add(value);
        } else {
        	List<Object> tempList = new ArrayList<Object>();
        	tempList.add(value);
        	masterData.put(key, tempList);
        }
  	}
    
  	/**
     * Removes an entry in the table.
     *
     * @param key, where:
     *	    - it is not null.
     *      - it is a unique trait of the table type.
     * 
     * @throws IllegalArgumentException, if:
     *      - the current table does not contain the key.
     */
  	void removeEntry(String key) {
     	if (!masterData.containsKey(key)) throw new IllegalArgumentException("Error: Table does not contain key");
    	masterData.remove(key);
  	}	
  
  	/**
     * Clears any existing entry in the table corresponding to the key.
     *
     * @param key, where:
     *	    - it is not null.
     *      - it is a unique trait of the table type.
     *
     * @throws IllegalArgumentException, if:
     *      - the current table does not contain the key.
     */
  	void clearEntry(String key){
      	if (!masterData.containsKey(key)) throw new IllegalArgumentException("Error: Table does not contain key");
    	masterData.get(key).clear();
  	}
  
  	/**
     * Determines whether or not a specific key exists within the table.
     *
     * @param key, where:
     *	    - it is not null.
     *      - it is a unique trait of the table type.
     *
     * @return true, if:
     *      - the key exists wihtin the table.
     *         false, if:
     *      - the key does not exist.
     */
  	boolean doesKeyExist(String key){
    	return masterData.containsKey(key);
    }
  
  	/**
     * Determines whether or not a value corresponding to a specific key exists within the table.
     *
     * @param value, where:
     *	    - it is not null.
     *      - it is a value corresponding to a specific key of the table.
     *
     * @return true, if:
     *      - the value exists wihtin the table.
     *         false, if:
     *      - the value does not exist.
     */
 	boolean doesValueExist(Object value) {
    	return masterData.values().stream().filter(list -> list.contains(value)).findFirst().isPresent();
    }
    
 	/**
     * Obtains all data (values) corresponding to a specific key in the table.
     *
     * @param key, where:
     *	    - it is not null.
     *      - it is a unique trait of the table type.
     *
     * @return a List of Objects, where:
     *      - the list contains all values corresponding to the key.
     *      - the list is not null.
     */
 	List<Object> getData(String key) {
 		return masterData.get(key);
 	}
 		
 	/**
     * Determines how many unique keys exist within the table.
     * 
     * @return an integer, where:
     * 	    - the number represents the size of the key set in the table. 
     */
 	int numEntries() {
 		return masterData.entrySet().size();
 	}
 	
 	/**
     * Determines how many unique values exist in the entire table.
     * 
     * @return an integer, where:
     * 	    - the number represents the total size of all the lists that correspond to a specific key.
     */
 	int numValues() {
 		int count = 0;
 		for (List<Object> list : masterData.values()) {
 			count += list.size();
 		}
 		    return count;
 		}
 		      
  }