package com.rqr.core;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.time.*;

public class Column implements Serializable {
	
	int index;
	String label;
	String name;
	DataType dataType; //read only property
	ArrayList<Object> values; //read only property
	Function<Object, String> formatFunction;
	Format format;

	private Column() {}

	Column(int index, String name, String label, DataType dataType, ArrayList<?> values) {
		this.index = index;
		this.name = name;
		this.label = label;
		this.dataType = dataType;
		for(int i=0;i<values.size();i++)
			this.values.add(values.get(i));
	}

	public Column(String name, DataType dataType, ArrayList<?> values) {
		String logMessage = null;
		
		if(values == null) {
			logMessage = String.format("column values is null");
			//Haapus.writeLog(LogMessageType.ERROR, logMessage);
			return;
		}
		
		try{
			if(Objects.isNull(name))
				throw new IllegalArgumentException();
			
			if(name.trim().isEmpty())
				throw new IllegalArgumentException();
			
			if(Util.isInvalidColumnName(name))
				throw new IllegalArgumentException();
			
		}catch(IllegalArgumentException ie) {
			logMessage = String.format("column creation failed");
			//Haapus.writeLog(LogMessageType.ERROR, logMessage);
			return;
		}
		
		if(dataType == null){
			logMessage = String.format("column datatype is null");
			//Haapus.writeLog(LogMessageType.ERROR, logMessage);
			return;
		}
		
		this.name = name.trim();
		this.dataType = dataType;
		this.label = "";
		//this.values = values;
		/*
		if(values instanceof DiskArrayList) {
			DiskArrayList diskArrayList = (DiskArrayList)values;
			this.values = diskArrayList;//new DiskArrayList(diskArrayList.trackers, diskArrayList.raFile);
		}else{
			MemoryArrayList memoryArrayList = (MemoryArrayList)values;
			this.values = new MemoryArrayList(memoryArrayList.list);
		}*/
		this.values = new ArrayList<Object>();
		for(int i=0;i<values.size();i++)
			this.values.add(values.get(i));
		/*
		if(dataType == DataType.INTEGER)
			classType = Integer.class;
		else if(dataType == DataType.LONG)
			classType = Long.class;
		else if(dataType == DataType.FLOAT)
			classType = Float.class;
		else if(dataType == DataType.DOUBLE)
			classType = Double.class;
		else if(dataType == DataType.BOOLEAN)
			classType = Boolean.class;
		else if(dataType == DataType.CHARACTER)
			classType = Character.class;
		else if(dataType == DataType.STRING)
			classType = String.class;
		else if(dataType == DataType.LOCAL_DATE)
			classType = LocalDate.class;
		else if(dataType == DataType.LOCAL_TIME)
			classType = LocalTime.class;
		else if(dataType == DataType.LOCAL_DATE_TIME)
			classType = LocalDateTime.class;*/
	}
		
	public int getIndex() {
		return this.index;
	}
	
	public String getName() {
		return this.name;
	}
		
	public DataType getDataType() {
		return this.dataType;
	}
	
	public Format getFormat() {
		return this.format;
	}
		
	public String getLabel() {
		return this.label;
	}
		
	public int getSize() {
		return this.values.size();
	}
		
	public Object getValue(int index) {
		Object value = null;
		if(index >=0 && index < this.values.size()) {
			value = this.values.get(index);
		}else{
			//String logMessage = String.format("");
			//Haapus.writeLog(LogMessageType.ERROR, logMessage);
		}
		return value;
	}
	
	public String toString() {
		return "Column[name:\"" + name + "\", dataType:" + dataType + ", values:" + values + ", label:\"" + label + "\"]";
	}
	
	/*
	Column getCopy() {
		
	
		HList<Object> newValues;
		
		if(storageType == StorageType.DISK)
			newValues = new DiskArrayList<Object>();
		else
			newValues = new MemoryArrayList<Object>();
			 
		for(int i=0;i<values.size();i++) {
			newValues.add(values.get(i));
		}
		
		Column column = new Column(name, dataType, newValues);
		column.formatFunction = this.formatFunction;
		column.format = this.format;
		column.label = this.label;
	
		return column;
	}//end of getCopy() method
	
	
	ArrayList<Object> getRandomValues(int n) {
		int ctr = 0;
		Object v;
		ArrayList<Object> list = new ArrayList<Object>();
		if(values.size() < n)
			n = values.size();
		do{
			v = values.get(ctr);
			if(v != null)
				list.add(v);
			ctr += 1;
		}while(ctr < n);
		
		return list;
	}
	void convertValues(Informat informat) {
		
		DataType fromDataType = this.dataType;
		
		for(int i=0;i<values.size();i++) {
			if(Objects.nonNull(values.get(i)))
				values.set(i, Util.convertValue(values.get(i), fromDataType, informat));
		}
	}

*/

}//end of Column class