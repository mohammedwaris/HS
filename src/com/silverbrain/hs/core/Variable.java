package com.silverbrain.hs.core;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.time.*;

public class Variable implements Serializable {
	
	int index;
	String label;
	String name;
	DataType dataType; //read only property
	HSList<Object> values; //read only property
	Function<Object, String> formatFunction;
	Format format;
	Class<?> classType;
	StorageType storageType;

	private Variable() {}

	Variable(int index, String name, String label, DataType dataType, HSList<?> values) {
		this.index = index;
		this.name = name;
		this.label = label;
		this.dataType = dataType;
		for(int i=0;i<values.size();i++)
			this.values.add(values.get(i));
	}

	public Variable(String name, DataType dataType, HSList<?> values) {
		String logMessage = null;
		
		if(values == null) {
			logMessage = String.format("column values is null");
			//HS.writeLog(LogMessageType.ERROR, logMessage);
			return;
		}
		
		try{
			if(Objects.isNull(name))
				throw new IllegalArgumentException();
			
			if(name.trim().isEmpty())
				throw new IllegalArgumentException();
			
			/*if(HSUtils.isInvalidColumnName(name))
				throw new IllegalArgumentException();*/
			
		}catch(IllegalArgumentException ie) {
			logMessage = String.format("column creation failed");
			//HS.writeLog(LogMessageType.ERROR, logMessage);
			return;
		}
		
		if(dataType == null){
			logMessage = String.format("column datatype is null");
			//HS.writeLog(LogMessageType.ERROR, logMessage);
			return;
		}
		
		this.name = name.trim();
		this.dataType = dataType;
		this.label = "";
	
		
		
		this.values = (HSList<Object>)values;

		
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
			classType = LocalDateTime.class;
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
			String logMessage = String.format("");
			//HS.writeLog(LogMessageType.ERROR, logMessage);
		}
		return value;
	}
	
	public String toString() {
		return "Variable[name:\"" + name + "\", dataType:" + dataType + ", values:" + values + ", label:\"" + label + "\"]";
	}
	
	Variable getCopy() {
			
		HSList<Object> newValues;
		
		if(storageType == StorageType.DISK)
			newValues = new DList<Object>();
		else
			newValues = new MList<Object>();
			 
		for(int i=0;i<values.size();i++) {
			newValues.add(values.get(i));
		}
		
		Variable variable = new Variable(name, dataType, newValues);
		variable.formatFunction = this.formatFunction;
		variable.format = this.format;
		variable.label = this.label;
	
		return variable;
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
			//if(Objects.nonNull(values.get(i)))
				//values.set(i, HSUtils.convertValue(values.get(i), fromDataType, informat));
		}
	}
	
	int getCountWithoutNull() {
		int totalCount = this.values.size();
		int nullCount = 0;
		for(int i=0;i<totalCount;i++) {
			if(Objects.isNull(this.values.get(i)))
				nullCount += 1;
		}
		return (totalCount - nullCount);
	}


}//end of Variable class