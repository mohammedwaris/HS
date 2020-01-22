package com.silverbrain.hs.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.time.*;

public class VariableFactory implements IVariableFactory{
	
	
	StorageType storageType;

	public VariableFactory() {
		storageType = StorageType.MEMORY;
	}
	
	VariableFactory(StorageType storageType) {
		this.storageType = storageType;
	}
	
	Variable createIntegerVariable(String name, HSList<Integer> values) {
		Variable variable = new Variable(name, DataType.INTEGER, values);
		variable.storageType = storageType;
		return variable;
	}
	
	public Variable createIntegerVariable(String name, ArrayList<Integer> values) {
		HSList<Object> newValues = HSUtils.toHSList(values, storageType);
		Variable variable = new Variable(name, DataType.INTEGER, newValues);
		variable.storageType = storageType;
		return variable;
	}
	
	Variable createLongVariable(String name, HSList<Long> values) {
		Variable variable = new Variable(name, DataType.LONG, values);
		variable.storageType = storageType;
		return variable;
	}
	
	public Variable createLongVariable(String name, ArrayList<Long> values) {
		HSList<Object> newValues = HSUtils.toHSList(values, storageType);
		Variable variable = new Variable(name, DataType.LONG, newValues);
		variable.storageType = storageType;
		return variable;
	}
	
	Variable createDoubleVariable(String name, HSList<Double> values) {
		return new Variable(name, DataType.DOUBLE, values);
	}
	
	public Variable createDoubleVariable(String name, ArrayList<Double> values) {
		HSList<Object> newValues = HSUtils.toHSList(values, storageType);
		Variable variable = new Variable(name, DataType.DOUBLE, newValues);
		variable.storageType = storageType;
		return variable;
	}
	
	Variable createFloatVariable(String name, HSList<Float> values) {
		Variable variable = new Variable(name, DataType.FLOAT, values);
		variable.storageType = storageType;
		return variable;
	}
	
	public Variable createFloatVariable(String name, ArrayList<Float> values) {
		HSList<Object> newValues = HSUtils.toHSList(values, storageType);
		Variable variable = new Variable(name, DataType.FLOAT, newValues);
		variable.storageType = storageType;
		return variable;
	}
	
	Variable createBooleanVariable(String name, HSList<Boolean> values) {
		Variable variable = new Variable(name, DataType.BOOLEAN, values);
		variable.storageType = storageType;
		return variable;
	}
	
	public Variable createBooleanVariable(String name, ArrayList<Boolean> values) {
		HSList<Object> newValues = HSUtils.toHSList(values, storageType);
		Variable variable = new Variable(name, DataType.BOOLEAN, newValues);
		variable.storageType = storageType;
		return variable;
	}
	
	Variable createStringVariable(String name, HSList<String> values) {
		Variable variable = new Variable(name, DataType.STRING, values);
		variable.storageType = storageType;
		return variable;
	}
	
	public Variable createStringVariable(String name, ArrayList<String> values) {
		HSList<Object> newValues = HSUtils.toHSList(values, storageType);
		Variable variable = new Variable(name, DataType.STRING, newValues);
		variable.storageType = storageType;
		return variable;
	}
	
	Variable createLocalDateVariable(String name, HSList<LocalDate> values) {
		Variable variable = new Variable(name, DataType.LOCAL_DATE, values);
		variable.storageType = storageType;
		return variable;
	}
	
	public Variable createLocalDateVariable(String name, ArrayList<LocalDate> values) {
		HSList<Object> newValues = HSUtils.toHSList(values, storageType);
		Variable variable = new Variable(name, DataType.LOCAL_DATE, newValues);
		variable.storageType = storageType;
		return variable;
	}
	
	Variable createLocalTimeVariable(String name, HSList<LocalTime> values) {
		Variable variable = new Variable(name, DataType.LOCAL_TIME, values);
		variable.storageType = storageType;
		return variable;
	}
	
	public Variable createLocalTimeVariable(String name, ArrayList<LocalTime> values) {
		HSList<Object> newValues = HSUtils.toHSList(values, storageType);
		Variable variable = new Variable(name, DataType.LOCAL_TIME, newValues);
		variable.storageType = storageType;
		return variable;
	}
	
	Variable createLocalDateTimeVariable(String name, HSList<LocalDateTime> values) {
		Variable variable = new Variable(name, DataType.LOCAL_DATE_TIME, values);
		variable.storageType = storageType;
		return variable;
	}
	
	public Variable createLocalDateTimeVariable(String name, ArrayList<LocalDateTime> values) {
		HSList<Object> newValues = HSUtils.toHSList(values, storageType);
		Variable variable = new Variable(name, DataType.LOCAL_DATE_TIME, newValues);
		variable.storageType = storageType;
		return variable;
	}
	
	public Variable createVariable(String name, DataType dataType, HSList<?> values) {
		Variable variable = new Variable(name, dataType, values);
		variable.storageType = storageType;
		return variable;
	}
	
	public Variable createVariable(String name, DataType dataType, ArrayList<?> values) {
		HSList<Object> newValues = HSUtils.toHSList(values, storageType);
		Variable variable = new Variable(name, dataType, newValues);
		variable.storageType = storageType;
		return variable;
	}
	
	public Variable createVariable(String name, HSList<Object> values) {
		Informat informat = HSUtils.detectInformat(values);
		Variable variable = new Variable(name, HSUtils.getDataTypeByInformat(Informat.STRING), values);
		variable.storageType = storageType;		
		if(informat != Informat.STRING) {
			variable.convertValues(informat);
			variable.dataType = HSUtils.getDataTypeByInformat(informat);
		}
		return variable;
	}
	
	public Variable createVariable(String name, ArrayList<?> values) {

		Set types = new HashSet<Informat>();
		HSList newValues;// = HSUtils.toHSList(values, storageType);
		if(storageType == StorageType.DISK){
			newValues = new DList<String>();
		}else{
			newValues = new MList<String>();
		}
		
		Object value;
		for(int i=0;i<values.size();i++){
			value = values.get(i);
			if(value != null) {
				newValues.add(value.toString());
				types.add(HSUtils.detectInformat(value));
			}else{
				newValues.add(null);
			}
		}
		
		//System.out.println(types);
		
		Informat informat = null;
		if(types.size() == 1)
			informat = (Informat) types.iterator().next();
		else
			informat = Informat.STRING;
		
		Variable variable = new Variable(name, HSUtils.getDataTypeByInformat(Informat.STRING), newValues);
		variable.storageType = storageType;
		if(informat != Informat.STRING) {
			variable.convertValues(informat);
			variable.dataType = HSUtils.getDataTypeByInformat(informat);
		}
		return variable;
	}
	

	
	public Variable createVariableWithDuplicateValues(String name, DataType dataType, Object value, int size) {
		Variable variable = new Variable(name, dataType, HSUtils.createDuplicateValues(dataType, value, size, storageType));
		variable.storageType = storageType;
		return variable;
	}
	

	
	public Variable createVariableWithNullValues(String name, DataType dataType, int size) {
		Variable variable = new Variable(name, dataType, HSUtils.createNullValues(dataType, size, storageType));
		variable.storageType = storageType;
		return variable;
	}
	

	
	public Variable createEmptyVariable(String name, DataType dataType) {
		Variable variable = new Variable(name, dataType, HSUtils.createNullValues(dataType, 0, storageType));
		variable.storageType = storageType;
		return variable;
	}
}