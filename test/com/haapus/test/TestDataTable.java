package com.haapus.test;

import org.junit.*;
//import org.junit.;

import java.util.*;
import com.haapus.*;
import java.time.*;

public class TestDataTable {
	
	DataTable cityTempTable;
	
	public void builtCityTempTable() {
		ArrayList<String> cities = new ArrayList<String>();
        cities.add("Mumbai");
        cities.add("Moscow");
        cities.add("New York");
        cities.add("Paris");
        cities.add("Shangai");
        cities.add("Moscow");
        cities.add("New York");

        ArrayList<Double> minTemps = new ArrayList<Double>();
        minTemps.add(10.2);
        minTemps.add(-5.4);
        minTemps.add(-2.2);
        minTemps.add(1.5);
        minTemps.add(11.6);
        minTemps.add(15.6);
        minTemps.add(9.6);

        ArrayList<Double> maxTemps = new ArrayList<Double>();
        maxTemps.add(40.5);
        maxTemps.add(27.2);
        maxTemps.add(25.8);
        maxTemps.add(32.9);
        maxTemps.add(35.1);
        maxTemps.add(38.3);
        maxTemps.add(33.2);

        ArrayList<Integer> years = new ArrayList<Integer>();
        years.add(2010);
        years.add(2010);
        years.add(2010);
        years.add(2010);
        years.add(2010);
        years.add(2011);
        years.add(2011);

        Column cityColumn = Column.create("City", DataType.STRING, cities);
        Column minTempColumn = Column.create("MinTemp", DataType.DOUBLE, minTemps);
        Column maxTempColumn = Column.create("MaxTemp", DataType.DOUBLE, maxTemps);
        Column yearColumn = Column.create("Year", DataType.INTEGER, years);

        ArrayList<Column> columns = new ArrayList<Column>();
        columns.add(cityColumn);
        columns.add(minTempColumn);
        columns.add(maxTempColumn);
        columns.add(yearColumn);
		
		cityTempTable = new DataTable(columns);

	}
	
	@Before
	public void setUp() {
		builtCityTempTable();
	}

    @Test
    public void test_getRowCount() {

        // assert statements
        Assert.assertEquals(7, cityTempTable.getRowCount());

    }
	
	@Test
    public void test_getColumnCount() {
        Assert.assertEquals(4, cityTempTable.getColumnCount());
    }
	
	@Test
	public void test_where() {
		DataTable newTable = cityTempTable.where(rowData -> {
													Row row = rowData.getRow();
													boolean keep = false;
													if((int)row.getValue("year") == 2011)
														keep = true;
			
													return new WhereData(keep, null);
												});
												
		//Total row should be 2
		Assert.assertEquals(2, newTable.getRowCount());
		
		//Total column should 4
		Assert.assertEquals(4, newTable.getColumnCount());
		
		//Year column in first row should have 2011
		Assert.assertEquals(2011, newTable.getRow(0).getValue("year"));
		
		//Year column in second row should have 2011
		Assert.assertEquals(2011, newTable.getRow(1).getValue("year"));
		
		//Both table should be different objects.
		Assert.assertNotEquals(newTable, cityTempTable);
	}
	
	@Test
	public void test_getCopy() {
		DataTable newTable = cityTempTable.getCopy();
		
		//Total row should be 2
		Assert.assertEquals(7, newTable.getRowCount());
		
		//Total column should 4
		Assert.assertEquals(4, newTable.getColumnCount());
		
		//Year column in first row should have 2011
		Assert.assertEquals(2010, newTable.getRow(0).getValue("year"));
		
		//Year column in second row should have 2011
		Assert.assertEquals(2011, newTable.getRow(6).getValue("year"));
		
		//Both table should be different objects.
		Assert.assertNotEquals(newTable, cityTempTable);
	}
	
	@Test
	public void test_sort() {
		
		
		Instant start = Instant.now();
 
		DataTable newTable = cityTempTable.sort("city year");
		
		Instant finish = Instant.now();
 
		double timeElapsed = Duration.between(start, finish).toMillis();  //in millis
		
		System.out.println("sort() method took: " + timeElapsed + " msec");
		//Both table should be different objects.
		Assert.assertNotNull(newTable);
		
		//Both table should be different objects.
		Assert.assertNotEquals(newTable, cityTempTable);
		
		//Total row should be 2
		Assert.assertEquals(7, newTable.getRowCount());
		
		//Total column should 4
		Assert.assertEquals(4, newTable.getColumnCount());
		
		//City column in first row should have Moscow
		Assert.assertEquals("Moscow", newTable.getRow(0).getValue("City"));
		
		//Year column in first row should have 2010
		Assert.assertEquals(2010, newTable.getRow(0).getValue("Year"));
		
		//Year column in second row should have 2011
		Assert.assertEquals(2011, newTable.getRow(1).getValue("Year"));
	}
}