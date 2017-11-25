package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.datastructure.Table;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TableTest {
    @Test
    public void test1(){
        Table<String> table = new Table<>("SomeID");
        table.addEntry("SomeID", "SomeValue");
        table.addEntry("AnotherID", "AnotherValue");
        assertTrue(table.containsEntry("SomeID"));
        table.clearEntry("SomeID");
        assertTrue(table.getData("SomeID").size() == 0);

        List<String> values = table.getData("AnotherID");
        assertTrue(values.size() == 1);

        assertTrue(table.numEntries() == 2);

        table.addEntry("AnotherID", "SomeOtherID");
        assertTrue(table.numValues() == 2);
    }
}