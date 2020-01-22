package com.silverbrain.hs.core;

import java.util.*;
import java.util.function.*;

public interface IDataSet {
	
	
	//read only methods
	public String getLabel();
	public String getName();
	public ArrayList<String> getVariableNames();
	public int getVariableCount();
	public int getObservationCount();
	public IDataSet clone(); //return deep copy
	public IDataSet getVariableMetaData();
	public ArrayList<Variable> getVariables();
	public String getVariableName(Variable variable);
	public Variable getVariableByIndex(int position);
	public Variable getVariableByName(String variableName);
	public StorageType getStorageType();
	
	public void print();
	
	
	//update methods
	public IDataSet setLabel(String newLabel);
	public IDataSet setName(String newName);
	
	public IDataSet sort(String keys);
	public IDataSet sort(String keys, boolean out);
	
	public IDataSet where(Function<Observation, Boolean> function);
	public IDataSet where(BiFunction<Observation, Store, WhereData> bifunction);
	
	public IDataSet join(IDataSet rightDataSet, String keys, JoinType joinType);
	public IDataSet join(IDataSet rightDataSet, String keys, JoinType joinType, boolean out);
	
	public IDataSet duplicateObservations(String keys);
	
	public IDataSet uniqueObservations(String keys);
	
	public IDataSet randomObservations(int howMany);
	public IDataSet randomObservations(int howMany, boolean out); 
	
	public IDataSet top(int howMany, boolean out);
	public IDataSet top(int howMany);
	
	public IDataSet bottom(int howMany, boolean out);
	public IDataSet bottom(int howMany);
	
	public IDataSet concatenate(IDataSet dataSet);
	public IDataSet concatenate(IDataSet dataSet, boolean out);
	
	public IDataSet shuffle(boolean out);
	public IDataSet shuffle();
	
	//Aggregate methods
	public IDataSet count();
	public IDataSet count(ParameterBuilder.CountParameter params);
	public IDataSet max(ParameterBuilder.MaxParameter params);
	public IDataSet min(ParameterBuilder.MinParameter params);
	public IDataSet avg(ParameterBuilder.AvgParameter params);
	public IDataSet sum(ParameterBuilder.SumParameter params);
	public IDataSet aggregate(ParameterBuilder.AggregateParameter params);
	
	//Variable - read only methods
	public String getVariableLabel(String variableName);
	public DataType getVariableDataType(String variableName);
	public String getVariableFormattedValueAt(String variableName, int index);
	public Object getVariableValueAt(String variableName, int index);
	public boolean hasVariable(String variableName);
	public int getVariableIndex(String variableName);
	public void forEachVariable(Consumer<Variable> consumer);
	
	
	//Variable - update methods
	public IDataSet setVariableLabel(String variableName, String label);
	public IDataSet setVariableLabel(String variableName, String label, boolean out);
	
	public IDataSet setVariableFormat(String variableName, Function<Object, String> formatFunction);
	public IDataSet setVariableFormat(String variableName, Function<Object, String> formatFunction, boolean out);
	
	public IDataSet setVariableFormat(String variableName, Format format);
	public IDataSet setVariableFormat(String variableName, Format format, boolean out);
	
	public IDataSet removeVariableValueAt(String variableName, int index);
	public IDataSet removeVariableValueAt(String variableName, int index, boolean out);
	
	public IDataSet addCalculatedVariable(String variableName, DataType dataType, Function<Observation, Object> function);
	public IDataSet addCalculatedVariable(String variableName, DataType dataType, BiFunction<Observation, Store, VariableData> bifunction);
	
	public IDataSet copyVariable(String variableName, String newVariableName);

	public IDataSet addVariable(Variable variable) ;
	
	public IDataSet updateVariableDataType(String variableName, Informat informat);
	public IDataSet updateVariableDataType(String variableName, Informat informat, boolean out);
	
	public IDataSet updateVariableDataType(String variableName, DataType newDataType);
	public IDataSet updateVariableDataType(String variableName, DataType newDataType, boolean out);
	
	public IDataSet updateVariable(String variableName, Function<Observation, Object> function) ;
	public IDataSet updateVariable(String variableName, BiFunction<Observation, Store, VariableData> bifunction) ;
	
	public IDataSet replaceVariableValue(String variableName, Object oldValue, Object newValue);
	public IDataSet replaceVariableValue(String variableName, Object oldValue, Object newValue, boolean out);
	
	public IDataSet renameVariable(String variableNames);
	//public IDataSet renameVariable(String variableNames, boolean out);
	
	public IDataSet dropVariable(String variableNames);
	//public IDataSet dropVariable(String variableNames, boolean out);
	
	public IDataSet keepVariable(String variableNames);
	//public IDataSet keepVariable(String variableNames, boolean out);
	
	
	
	//Observation - read only methods
	public Observation getObservation(int observationIndex);
	public void forEachObservation(Consumer<Observation> consumer);
	
	
	//Observation - update methods
	public IDataSet addObservation(Object...values);
	public IDataSet addObservation(boolean out, Object...values);
	
	public IDataSet addObservation(Map<String, Object> observationMap);
	public IDataSet addObservation(Map<String, Object> observationMap, boolean out);
	
	public IDataSet removeObservation(int observationIndex);
	public IDataSet removeObservation(int observationIndex, boolean out);
	
	public IDataSet removeAllObservations(boolean out);
	public IDataSet removeAllObservations();
	
	
	
	
}