package com.silverbrain.hs.core;

import java.util.*;
import java.time.*;

public class Observation implements Comparator<Observation> {
	
	
	public Object values[];
	public DataType dataTypes[];
	public Boolean firstValues[];
	public Boolean lastValues[];
	public String names[];
	public int index;
	
	
	private Observation() {}
	
	public Observation(int index, String names[], DataType dataTypes[], Object values[], Boolean firstValues[], Boolean lastValues[]) {
		this.index = index;
		this.names = names;
		this.dataTypes = dataTypes;
		this.values = values;
		this.firstValues = firstValues;
		this.lastValues = lastValues;
	}
	
	public int getIndex(){
		return this.index;
	}
	
	public int getVariableCount() {
		return this.names.length;
	}
	
	public String getVariableName(int variablePosition) {
		String variableName = null;
		if(variablePosition >=0 && variablePosition <= getSize() - 1)
			variableName = names[variablePosition];
		return variableName;
	}
	
	public int getVariablePosition(String variableName) {
		int position = -1;
		for(int i=0;i<this.names.length;i++){
			if(names[i].equalsIgnoreCase(variableName.trim())) {
				position = i;
				break;
			}
		}
		return position;
	}
	
	public Object getValue(String variableName) {
		Object value = this.values[getVariablePosition(variableName)];
		return value;
	}
	
	public Object getValue(int variablePosition) {
		Object value = this.values[variablePosition];
		return value;
	}
	
	public DataType getDataType(String variableName) {
		DataType dataType = this.dataTypes[getVariablePosition(variableName)];
		return dataType;
	}
	
	public DataType getDataType(int variablePosition) {
		DataType dataType = this.dataTypes[variablePosition];
		return dataType;
	}
	
	public Boolean getFirstValue(String variableName){
		Boolean firstValue = this.firstValues[getVariablePosition(variableName)];
		return firstValue;
	}
	
	public Boolean getLastValue(String variableName){
		Boolean lastValue = this.lastValues[getVariablePosition(variableName)];
		return lastValue;
	}
	
	public Boolean getFirstValue(int variablePosition){
		Boolean firstValue = this.firstValues[variablePosition];
		return firstValue;
	}
	
	public Boolean getLastValue(int variablePosition){
		Boolean lastValue = this.lastValues[variablePosition];
		return lastValue;
	}
	
	public String getStringValue(String variableName) {
		String value = (String)this.values[getVariablePosition(variableName)];
		return value;
	}
	
	public Integer getIntegerValue(String variableName) {
		Integer value = (Integer)this.values[getVariablePosition(variableName)];
		return value;
	}
	
	public Long getLongValue(String variableName) {
		Long value = (Long)this.values[getVariablePosition(variableName)];
		return value;
	}
	
	public LocalDate getLocalDateValue(String variableName) {
		LocalDate value = (LocalDate)this.values[getVariablePosition(variableName)];
		return value;
	}
	
	public int getSize() {
		int size = -1;
		if(Objects.nonNull(names))
			size = names.length;
		return size;
	}
	
	/*
	
	
	public Object getValue(int variablePosition) {
		int i = 0;
		Object value = null;
		for (Map.Entry<String, Object> entry : values.entrySet()) {
			if(i == variablePosition) {
				value = entry.getValue();
				break;
			}
			i += 1;
		}
		return value;
	}

	public Integer getIntegerValue(String variableName) {
		variableName = variableName.trim().toLowerCase();
		return (Integer)this.values.get(variableName);
	}
	
	public Long getLongValue(String variableName) {
		variableName = variableName.trim().toLowerCase();
		return (Long)this.values.get(variableName);
	}
	
	public Float getFloatValue(String variableName) {
		variableName = variableName.trim().toLowerCase();
		return (Float)this.values.get(variableName);
	}
	
	public Double getDoubleValue(String variableName) {
		variableName = variableName.trim().toLowerCase();
		return (Double)this.values.get(variableName);
	}
	
	public Boolean getBooleanValue(String variableName) {
		variableName = variableName.trim().toLowerCase();
		return (Boolean)this.values.get(variableName);
	}
	
	public Character getCharacterValue(String variableName) {
		variableName = variableName.trim().toLowerCase();
		return (Character)this.values.get(variableName);
	}

	public String getStringValue(String variableName) {
		variableName = variableName.trim().toLowerCase();
		return (String)this.values.get(variableName);
	}
	
	public LocalDate getLocalDateValue(String variableName) {
		variableName = variableName.trim().toLowerCase();
		return (LocalDate)this.values.get(variableName);
	}
	
	public LocalTime getLocalTimeValue(String variableName) {
		variableName = variableName.trim().toLowerCase();
		return (LocalTime)this.values.get(variableName);
	}
	
	public LocalDateTime getLocalDateTimeValue(String variableName) {
		variableName = variableName.trim().toLowerCase();
		return (LocalDateTime)this.values.get(variableName);
	}
	
	public DataType getDataType(String variableName) {
		variableName = variableName.trim().toLowerCase();
		return this.dataTypes.get(variableName);
	}
	
	public DataType getDataType(int variablePosition) {
		int i = 0;
		DataType dataType = null;
		for (Map.Entry<String, DataType> entry : dataTypes.entrySet()) {
			if(i == variablePosition) {
				dataType = entry.getValue();
				break;
			}
			i += 1;
		}
		return dataType;
	}
	
	
	
	
	
	public String toString() {
		
		String str = "[Row(index:" + getIndex() + "):[";
		Iterator<Map.Entry<String, Object>> vit = values.entrySet().iterator();
		Iterator<Map.Entry<String, DataType>> dit = dataTypes.entrySet().iterator();
		Iterator<Map.Entry<String, Boolean>> fit = firstValues.entrySet().iterator();
		Iterator<Map.Entry<String, Boolean>> lit = firstValues.entrySet().iterator();

		while (vit.hasNext()) {
			Map.Entry<String, Object> value = vit.next();
			Map.Entry<String, DataType> dataType = dit.next();
			Map.Entry<String, Boolean> firstValue = fit.next();
			Map.Entry<String, Boolean> lastValue = lit.next();
			str = str + value.getKey() + ":(dataType: " + dataType.getValue() + ", Value: " + value.getValue() + ", first: " + firstValue.getValue() + ", last: " + lastValue.getValue() +")";
			if(vit.hasNext())
				str = str + ", ";
			else
				str = str + "]]\n";
			
		}
		return str;
	}
	*/
	
	public String toString() {
		String data = "";
		
		for(int i=0;i<getSize();i++) {
			data += names[i] + " ";
		}
		data += names.toString() + values + dataTypes + firstValues + lastValues;
		return data;
	}
	public int compare(Observation a, Observation b) 
    { 
        return a.getIndex() - b.getIndex(); 
    } 
}