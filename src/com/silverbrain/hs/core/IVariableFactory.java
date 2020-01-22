package com.silverbrain.hs.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.time.*;

public interface IVariableFactory {

	
	public Variable createIntegerVariable(String name, ArrayList<Integer> values);
	

	
	public Variable createLongVariable(String name, ArrayList<Long> values); 
	

	
	public Variable createDoubleVariable(String name, ArrayList<Double> values); 
	

	
	public Variable createFloatVariable(String name, ArrayList<Float> values); 
	

	
	public Variable createBooleanVariable(String name, ArrayList<Boolean> values); 
	

	
	public Variable createStringVariable(String name, ArrayList<String> values); 
	

	
	public Variable createLocalDateVariable(String name, ArrayList<LocalDate> values); 
	

	
	public Variable createLocalTimeVariable(String name, ArrayList<LocalTime> values); 
	

	
	public Variable createLocalDateTimeVariable(String name, ArrayList<LocalDateTime> values);
	
	public Variable createVariable(String name, DataType dataType, HSList<?> values); 
	
	public Variable createVariable(String name, DataType dataType, ArrayList<?> values); 
	
	public Variable createVariable(String name, HSList<Object> values); 
	
	public Variable createVariable(String name, ArrayList<?> values); 
	

	
	public Variable createVariableWithDuplicateValues(String name, DataType dataType, Object value, int size); 

	
	public Variable createVariableWithNullValues(String name, DataType dataType, int size); 
	

	
	public Variable createEmptyVariable(String name, DataType dataType); 
}