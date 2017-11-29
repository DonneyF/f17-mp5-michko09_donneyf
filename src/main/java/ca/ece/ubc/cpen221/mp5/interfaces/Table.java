package ca.ece.ubc.cpen221.mp5.interfaces;

import java.util.*;

/**
 * Class:
 * Table - stores all "categories" or specific keys of the data structure.
 * <p>
 * Representation Invariant:
 * -
 * <p>
 * Abstraction Function:
 * -
 */

public class Table<T> {

    private Map<String, List<T>> masterData;

    /**
     * Table Constructor that takes a unique ID as an input.
     */
    public Table() {
        this.masterData = new HashMap<>();
    }

    public Table(Map<String, T> map) {
        this.masterData = new HashMap<>();
        for (String key : map.keySet()) {
            List<T> list = new ArrayList<>();
            list.add(map.get(key));
            masterData.put(key, list);
        }
    }

    /**
     * Table Constructor that takes a another table to clone.
     */
    public Table(Table copyOf) {
        this.masterData = copyOf.masterData;
    }

    /**
     * Add a new entry or Modifies a current entry in the table.
     *
     * @param key,   where:
     *               - it is not null.
     *               - it is a unique trait of the table type.
     * @param value, where:
     *               - it is not null.
     *               - it is a specific value type corresponding to the key.
     */
    public void addEntry(String key, T value) {
        if (masterData.containsKey(key)) {
            masterData.get(key).add(value);
        } else {
            List<T> tempList = new ArrayList<>();
            tempList.add(value);
            masterData.put(key, tempList);
        }
    }

    /**
     * Removes an entry in the table.
     *
     * @param key, where:
     *             - it is not null.
     *             - it is a unique trait of the table type.
     * @throws IllegalArgumentException, if:
     *                                   - the current table does not contain the key.
     */
    public void removeEntry(String key) {
        if (!masterData.containsKey(key)) throw new IllegalArgumentException("Error: Table does not contain key");
        masterData.remove(key);
    }

    /**
     * Clears any existing entry in the table corresponding to the key.
     *
     * @param key, where:
     *             - it is not null.
     *             - it is a unique trait of the table type.
     * @throws IllegalArgumentException, if:
     *                                   - the current table does not contain the key.
     */
    public void clearEntry(String key) {
        if (!masterData.containsKey(key)) throw new IllegalArgumentException("Error: Table does not contain key");
        masterData.get(key).clear();
    }

    /**
     * Determines whether or not a specific key exists within the table.
     *
     * @param key, where:
     *             - it is not null.
     *             - it is a unique trait of the table type.
     * @return true, if:
     * - the key exists wihtin the table.
     * false, if:
     * - the key does not exist.
     */
    public boolean containsEntry(String key) {
        return masterData.containsKey(key);
    }

    /**
     * Determines whether or not a value corresponding to a specific key exists within the table.
     *
     * @param value, where:
     *               - it is not null.
     *               - it is a value corresponding to a specific key of the table.
     * @return true, if:
     * - the value exists wihtin the table.
     * false, if:
     * - the value does not exist.
     */
    public boolean containsValue(Object value) {
        return masterData.values().stream().filter(list -> list.contains(value)).findFirst().isPresent();
    }

    /**
     * Obtains all data (values) corresponding to a specific key in the table.
     *
     * @param key, where:
     *             - it is not null.
     *             - it is a unique trait of the table type.
     * @return a List of Objects, where:
     * - the list contains all values corresponding to the key.
     * - the list is not null.
     */
    public List<T> getData(String key) {
        return masterData.get(key);
    }

    /**
     * Determines how many unique keys exist within the table.
     *
     * @return an integer, where:
     * - the number represents the size of the key set in the table.
     */
    public int numEntries() {
        return masterData.entrySet().size();
    }

    /**
     * Determines how many unique values exist in the entire table.
     *
     * @return an integer, where:
     * - the number represents the total size of all the lists that correspond to a specific key.
     */
    public int numValues() {
        int count = 0;
        for (List<T> list : masterData.values()) {
            count += list.size();
        }
        return count;
    }

    public Set getValues() {
        Set<T> set = new HashSet<>();

        for (List list : masterData.values()){
            set.addAll(list);
        }
        return set;
    }

    public String getID() {
    	return masterData.keySet().toString();
    }
}