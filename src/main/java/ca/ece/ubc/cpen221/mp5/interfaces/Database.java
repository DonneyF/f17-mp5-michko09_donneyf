package ca.ece.ubc.cpen221.mp5.interfaces;

import java.util.HashMap;
import java.util.Map;

public class Database<T> {

    private Map<String, Table> database;

    public Database() {
        database = new HashMap<>();
    }

    public Database(Database otherDatabase) {
        database = otherDatabase.database;
    }

    public void setTable(String identifier, Table table){
        database.put(identifier,table);
    }

    public void setEntry(String identifier, String key, T value){
        database.get(identifier).addEntry(key, value);
    }

    public boolean tableExists(Table table){
        return database.values().contains(table);
    }

    public Table getTable(String identifier){
        return database.get(identifier);
    }

    @Override
    public String toString() {
        return database.toString();
    }

    public int size() {
        return database.keySet().size();
    }


}
