package com.rqr.core;

import java.util.ArrayList;
import java.time.*;

public class ColumnFactory {
	


	public ColumnFactory() {
		
	}
	

	
	/*
	Column createIntegerColumn(String name, HList<Integer> values) {
		Column column = new Column(name, DataType.INTEGER, values);
		column.storageType = storageType;
		return column;
	}
	
	public Column createIntegerColumn(String name, ArrayList<Integer> values) {
		HList<Object> newValues = Util.toHList(values, storageType);
		Column column = new Column(name, DataType.INTEGER, newValues);
		column.storageType = storageType;
		return column;
	}
	
	Column createLongColumn(String name, HList<Long> values) {
		Column column = new Column(name, DataType.LONG, values);
		column.storageType = storageType;
		return column;
	}
	
	public Column createLongColumn(String name, ArrayList<Long> values) {
		HList<Object> newValues = Util.toHList(values, storageType);
		Column column = new Column(name, DataType.LONG, newValues);
		column.storageType = storageType;
		return column;
	}
	
	Column createDoubleColumn(String name, HList<Double> values) {
		return new Column(name, DataType.DOUBLE, values);
	}
	
	public Column createDoubleColumn(String name, ArrayList<Double> values) {
		HList<Object> newValues = Util.toHList(values, storageType);
		Column column = new Column(name, DataType.DOUBLE, newValues);
		column.storageType = storageType;
		return column;
	}
	
	Column createFloatColumn(String name, HList<Float> values) {
		Column column = new Column(name, DataType.FLOAT, values);
		column.storageType = storageType;
		return column;
	}
	
	public Column createFloatColumn(String name, ArrayList<Float> values) {
		HList<Object> newValues = Util.toHList(values, storageType);
		Column column = new Column(name, DataType.FLOAT, newValues);
		column.storageType = storageType;
		return column;
	}
	
	Column createBooleanColumn(String name, HList<Boolean> values) {
		Column column = new Column(name, DataType.BOOLEAN, values);
		column.storageType = storageType;
		return column;
	}
	
	public Column createBooleanColumn(String name, ArrayList<Boolean> values) {
		HList<Object> newValues = Util.toHList(values, storageType);
		Column column = new Column(name, DataType.BOOLEAN, newValues);
		column.storageType = storageType;
		return column;
	}
	*/
	public Column createStringColumn(String name,ArrayList<String> values) {
		Column column = new Column(name, DataType.STRING, values);
		return column;
	}
	/*
	public Column createStringColumn(String name, ArrayList<String> values) {
		HList<Object> newValues = Util.toHList(values, storageType);
		Column column = new Column(name, DataType.STRING, newValues);
		column.storageType = storageType;
		return column;
	}
	
	Column createLocalDateColumn(String name, HList<LocalDate> values) {
		Column column = new Column(name, DataType.LOCAL_DATE, values);
		column.storageType = storageType;
		return column;
	}
	
	public Column createLocalDateColumn(String name, ArrayList<LocalDate> values) {
		HList<Object> newValues = Util.toHList(values, storageType);
		Column column = new Column(name, DataType.LOCAL_DATE, newValues);
		column.storageType = storageType;
		return column;
	}
	
	Column createLocalTimeColumn(String name, HList<LocalTime> values) {
		Column column = new Column(name, DataType.LOCAL_TIME, values);
		column.storageType = storageType;
		return column;
	}
	
	public Column createLocalTimeColumn(String name, ArrayList<LocalTime> values) {
		HList<Object> newValues = Util.toHList(values, storageType);
		Column column = new Column(name, DataType.LOCAL_TIME, newValues);
		column.storageType = storageType;
		return column;
	}
	
	Column createLocalDateTimeColumn(String name, HList<LocalDateTime> values) {
		Column column = new Column(name, DataType.LOCAL_DATE_TIME, values);
		column.storageType = storageType;
		return column;
	}
	
	public Column createLocalDateTimeColumn(String name, ArrayList<LocalDateTime> values) {
		HList<Object> newValues = Util.toHList(values, storageType);
		Column column = new Column(name, DataType.LOCAL_DATE_TIME, newValues);
		column.storageType = storageType;
		return column;
	}
	
	Column createColumn(String name, DataType dataType, HList<?> values) {
		Column column = new Column(name, dataType, values);
		column.storageType = storageType;
		return column;
	}
	
	public Column createColumn(String name, DataType dataType, ArrayList<?> values) {
		HList<Object> newValues = Util.toHList(values, storageType);
		Column column = new Column(name, dataType, newValues);
		column.storageType = storageType;
		return column;
	}
	
	Column createColumn(String name, HList<Object> values) {
		Informat informat = Util.detectInformat(values);
		Column column = new Column(name, Util.getDataTypeByInformat(Informat.STRING), values);
		column.storageType = storageType;		
		if(informat != Informat.STRING) {
			column.convertValues(informat);
			column.dataType = Util.getDataTypeByInformat(informat);
		}
		return column;
	}
	
	*/
	
	/*
	public Column createColumn(String name, ArrayList<Object> values) {
		ArrayList<Object> newValues = Util.toHList(values, storageType);
		Informat informat = Util.detectInformat(newValues);
		Column column = new Column(name, Util.getDataTypeByInformat(Informat.STRING), newValues);
		column.storageType = storageType;
		if(informat != Informat.STRING) {
			column.convertValues(informat);
			column.dataType = Util.getDataTypeByInformat(informat);
		}
		return column;
	}*/
	
	/*
	Column createColumnWithDuplicateValues(String name, DataType dataType, Object value, int size) {
		Column column = new Column(name, dataType, Util.createDuplicateValues(dataType, value, size));
		column.storageType = storageType;
	}*/
	
	/*
	public Column createColumnWithDuplicateValues(String name, DataType dataType, Object value, int size) {
		Column column = new Column(name, dataType, Util.createDuplicateValues(dataType, value, size, storageType));
		column.storageType = storageType;
		return column;
	}*/
	
	/*
	Column createColumnWithNullValues(String name, DataType dataType, int size, StorageType storageType) {
		Column column = new Column(name, dataType, Util.createNullValues(dataType, size, storageType));
		column.storageType = storageType;
		return column;
	}*/
	/*
	public Column createColumnWithNullValues(String name, DataType dataType, int size) {
		Column column = new Column(name, dataType, Util.createNullValues(dataType, size, storageType));
		column.storageType = storageType;
		return column;
	}
	*/
	/*
	Column createEmptyColumn(String name, DataType dataType) {
		Column column = new Column(name, dataType, Util.createNullValues(dataType, 0, storageType));
		column.storageType = storageType;
		return column;
	}*/
	/*
	public Column createEmptyColumn(String name, DataType dataType) {
		Column column = new Column(name, dataType, Util.createNullValues(dataType, 0, storageType));
		column.storageType = storageType;
		return column;
	}*/
}