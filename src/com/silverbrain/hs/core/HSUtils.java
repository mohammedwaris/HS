package com.silverbrain.hs.core;

import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ObjectUtils;

public class HSUtils {

	static HSList<Object> createNullValues(DataType dType, int size) {
		return createNullValues(dType, size, StorageType.MEMORY);
	}

	static HSList<Object> createNullValues(DataType dType, int size, StorageType storageType) {
		
		HSList<Object> values = HSUtils.getHSList(storageType);
		for(int i=0;i<size;i++) {
			values.add(null);
		}
		return values;
	}
	
	static HSList<Object> createDuplicateValues(DataType dType, Object value, int size, StorageType storageType) {
	
		HSList<Object> values = HSUtils.getHSList(storageType);
		for(int i=0;i<size;i++){
			values.add(value);
		}
		return values;
	}
	
	static String truncString(String str) {
		if(str.length() > 13){
			str = str.substring(0, 10) + "...";
		}
		return str;
	}
	
	public static ArrayList<Object> createList(Object... objectData) {
		ArrayList<Object> list = new ArrayList<Object>();
		for(Object data: objectData)
			list.add(data);
		
		return list;
	}
	
	public static ArrayList<Integer> createIntegerList(Integer... intData) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(Integer data: intData)
			list.add(data);
		
		return list;
	}
	
	public static ArrayList<Long> createLongList(long... longData) {
		ArrayList<Long> list = new ArrayList<Long>();
		for(Long data: longData)
			list.add(data);
		
		return list;
	}
	
	public static ArrayList<Float> createFloatList(float... floatData) {
		ArrayList<Float> list = new ArrayList<Float>();
		for(Float data: floatData)
			list.add(data);
		
		return list;
	}
	
	
	
	public static ArrayList<Double> createDoubleList(double... doubleData) {
		ArrayList<Double> list = new ArrayList<Double>();
		for(Double data: doubleData)
			list.add(data);
		
		return list;
	}
	
	public static ArrayList<String> createStringList(String... stringData) {
		ArrayList<String> list = new ArrayList<String>();
		for(String data: stringData)
			list.add(data);
		
		return list;
	}
	
	public static ArrayList<LocalDate> createLocalDateList(Object... localDateData) {
		ArrayList<LocalDate> list = new ArrayList<LocalDate>();
		for(Object localDate: localDateData)
			list.add((LocalDate)localDate);
		
		return list;
	}
	
	public static ArrayList<Variable> createVariableList(Variable... variableData) {
		ArrayList<Variable> list = new ArrayList<Variable>();
		for(Variable data: variableData)
			list.add(data);
		
		return list;
	}
	
	static boolean isInvalidVariableName(String variableName) {
		
		boolean invalid = false;
		for(int i=0; i<variableName.length(); i++) {
			char c = variableName.charAt(i);
			if(!(c >=47 && c <=57) && !(c>=65 && c<=90) && !(c>=97 && c<=122) && c!=95) {
				invalid = true;
			}else if(i==0 && (c >=47 && c <=57)){
				invalid = true;
			}
			
			if(invalid)
				break;
		}
		return invalid;
	}
	
	static String getValidVariableName(String variableName) {
		
		StringBuilder newName = new StringBuilder(variableName.trim());
		for(int i=0; i<variableName.length(); i++) {
			char c = variableName.charAt(i);
			if(!(c >=47 && c <=57) && !(c>=65 && c<=90) && !(c>=97 && c<=122) && c!=95) {
				newName.setCharAt(i, '_');
			}
		}
		return newName.toString();
		
	}
	
	public static Object read(String value, Informat informat) {
		Object retValue = null;
		if(informat.isDatePattern == true) {
			LocalDate localDate = LocalDate.parse(value, informat.formatter);
			retValue = localDate;
		}
			
			
		return retValue;
		
	}

	static DateTimeFormatter detectLocalDateFormatter(Object value){

			ArrayList<DateTimeFormatter> formatters = new ArrayList<DateTimeFormatter>();
			
			formatters.add(Informat.localDatePattern("MM/dd/[yyyy][yy]").formatter);
			formatters.add(Informat.localDatePattern("dd/MM/[yyyy][yy]").formatter);
			//formatters.add(Informat.isDatePattern("[yyyy][yy] MM dd").formatter)
			formatters.add(Informat.localDatePattern("dd-LLL-[yyyy][yy]").formatter);
			formatters.add(DateTimeFormatter.ISO_LOCAL_DATE);

			int ctr = -1;
			DateTimeFormatter formatterFound = null;
			String strValue = null;
			boolean hasException = false;
			for(int i=0;i<formatters.size();i++) {
				
					if(Objects.nonNull(value)) {
						//System.out.println(strValue);
						strValue = (String)value;
						try {
							LocalDate.parse(strValue, formatters.get(i));
							hasException = false;
							ctr = i;
							break;
						}catch(Exception e){
							//System.out.println(e.getMessage());
							hasException = true;
							continue;
						}	
					}
			}
				

			if(hasException == false) 
				formatterFound = formatters.get(ctr);
			
			
			return formatterFound;
	}

	static DateTimeFormatter detectLocalTimeFormatter(Object value){

			ArrayList<DateTimeFormatter> formatters = new ArrayList<DateTimeFormatter>();
			formatters.add(DateTimeFormatter.ISO_LOCAL_TIME);

			int ctr = -1;
			DateTimeFormatter formatterFound = null;
			String strValue = null;
			boolean hasException = false;
			for(int i=0;i<formatters.size();i++) {
				
					if(Objects.nonNull(value)) {
						strValue = (String)value;
						//System.out.println(strValue);
						try {
							LocalTime.parse(strValue, formatters.get(i));
							hasException = false;
							ctr = i;
							break;
						}catch(Exception e){
							//System.out.println(e.getMessage());
							hasException = true;
							continue;
						}	
					}
			}
				

			if(hasException == false) 
				formatterFound = formatters.get(ctr);
			
			
			return formatterFound;
	}

	static DateTimeFormatter detectLocalDateTimeFormatter(Object value){

			ArrayList<DateTimeFormatter> formatters = new ArrayList<DateTimeFormatter>();
		formatters.add(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		formatters.add(Informat.localDateTimePattern("yyyy-MM-dd HH:mm:[ss]").formatter);

			int ctr = -1;
			DateTimeFormatter formatterFound = null;
			String strValue = null;
			boolean hasException = false;
			for(int i=0;i<formatters.size();i++) {
				
					if(Objects.nonNull(value)) {
						strValue = (String)value.toString();
						//System.out.println(strValue);
						try {
							LocalDateTime.parse(strValue, formatters.get(i));
							hasException = false;
							ctr = i;
							break;
						}catch(Exception e){
							//System.out.println(e.getMessage());
							hasException = true;
							continue;
						}	
					}
			}
				

			if(hasException == false) 
				formatterFound = formatters.get(ctr);
			
			
			return formatterFound;
	}
	
	/*
	public static DateTimeFormatter detectLocalDateFormatter(ArrayList<?> values){
			
			
			ArrayList<DateTimeFormatter> formatters = new ArrayList<DateTimeFormatter>();
			
			formatters.add(Informat.localDatePattern("MM/dd/[yyyy][yy]").formatter);
			formatters.add(Informat.localDatePattern("dd/MM/[yyyy][yy]").formatter);
			//formatters.add(Informat.isDatePattern("[yyyy][yy] MM dd").formatter)
			formatters.add(Informat.localDatePattern("dd-LLL-[yyyy][yy]").formatter);
			formatters.add(DateTimeFormatter.ISO_LOCAL_DATE);

			int ctr = 0;
			DateTimeFormatter formatterFound = null;
			String strValue = null;
			boolean hasException = false;
			do {
				for(int i=0;i<values.size();i++) {
					if(Objects.nonNull(values.get(i))) {
						strValue = values.get(i).toString();
						//System.out.println(strValue);
						try {
							LocalDate.parse(strValue, formatters.get(ctr));
						}catch(Exception e){
							//System.out.println(e.getMessage());
							hasException = true;
							break;
						}	
					}
				}
				if(hasException == false) {
					formatterFound = formatters.get(ctr);
					break;
				}else if(ctr >= formatters.size()) {
					break;
				}
				hasException = false;
				ctr += 1;
			}while(true);
			
			return formatterFound;
	}
	*/
	
	/*
	public static DateTimeFormatter detectLocalTimeFormatter(ArrayList<?> values){
			ArrayList<DateTimeFormatter> formatters = new ArrayList<DateTimeFormatter>();
			formatters.add(DateTimeFormatter.ISO_LOCAL_TIME);

			int ctr = 0;
			DateTimeFormatter formatterFound = null;
			String strValue = null;
			boolean hasException = false;
			do {
				for(int i=0;i<values.size();i++) {
					if(Objects.nonNull(values.get(i))) {
						strValue = values.get(i).toString();
						//System.out.println(strValue);
						try {
							LocalTime.parse(strValue, formatters.get(ctr));
						}catch(Exception e){
							hasException = true;
							break;
						}	
					}
				}
				if(hasException == false) {
					formatterFound = formatters.get(ctr);
					break;
				}else if(ctr >= formatters.size()) {
					break;
				}
				hasException = false;
				ctr += 1;
			}while(true);
			
			return formatterFound;
	}*/
	
	/*
	public static DateTimeFormatter detectLocalDateTimeFormatter(ArrayList<?> values){

										
		ArrayList<DateTimeFormatter> formatters = new ArrayList<DateTimeFormatter>();
		formatters.add(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		formatters.add(Informat.localDateTimePattern("yyyy-MM-dd HH:mm:[ss]").formatter);
			
			int ctr = 0;
			DateTimeFormatter formatterFound = null;
			String strValue = null;
			boolean hasException = false;
			do {
				for(int i=0;i<values.size();i++) {
					if(Objects.nonNull(values.get(i))) {
						strValue = values.get(i).toString();
						//System.out.println(strValue);
						try {
							LocalDateTime.parse(strValue, formatters.get(ctr));
						}catch(Exception e){
							hasException = true;
							//System.out.println("ha " + e.getMessage());
							break;
						}	
					}
				}
				if(hasException == false) {
					//System.out.println("formatter found");
					formatterFound = formatters.get(ctr);
					break;
				}else if(ctr >= formatters.size()) {
					break;
				}
				hasException = false;
				ctr += 1;
			}while(true);
			
			return formatterFound;
	}*/
	
	
	
	static Informat detectInformat(Object value) {
		
		Informat informat = Informat.BOOLEAN;
		
		
		if(Objects.nonNull(value)) {
			String val = value.toString().trim();
			if(
				//(!val.equals("1") && !val.equals("0")) && 
				(!val.equalsIgnoreCase("t") && !val.equalsIgnoreCase("f")) &&
				(!val.equalsIgnoreCase("true") && !val.equalsIgnoreCase("false")) ||
				val.isEmpty()
			) {
					informat = null;
			}
		}
		
		if(informat == null){
			informat = Informat.INTEGER;
			try {
				if(Objects.nonNull(value))
					Integer.parseInt(value.toString());
			}catch(Exception ex){
				informat = null;
			}
		}
		
		if(informat == null){
			informat = Informat.DOUBLE;
			try {
				if(Objects.nonNull(value))
					Double.parseDouble(value.toString());
			}catch(Exception e){
					informat = null;
			}
		}
		

		if(informat == null) {
			informat = Informat.LOCAL_DATE_TIME;
			DateTimeFormatter formatter = detectLocalDateTimeFormatter(value);
			if(formatter == null) {
				informat = null;
			}else {
				informat.formatter = formatter;
			}
		}


		if(informat == null) {
			informat = Informat.LOCAL_DATE;
			DateTimeFormatter formatter = detectLocalDateFormatter(value);
			if(formatter == null) {
				informat = null;
			}else{
				informat.formatter = formatter;
			}
		}
		
		if(informat == null) {
			informat = Informat.LOCAL_TIME;
			DateTimeFormatter formatter = detectLocalTimeFormatter(value);
			if(formatter == null) {
				informat = null;
			}else{
				informat.formatter = formatter;
			}
		}
		
		if(informat == null) {
			informat = Informat.STRING;
		}
		
		return informat;
	}
	
	static Informat isDataTypeConversionValid(Object value, DataType fromDataType, DataType toDataType) {
		
		Informat informat = null;
		boolean valid = true;
		
		if(fromDataType == DataType.STRING) {
			String strValue;
			if(toDataType == DataType.INTEGER) {
				informat = Informat.INTEGER;
				try {
					if(Objects.nonNull(value)) {
						strValue = (String)value;
						if(strValue.indexOf(".") > 0) {
							strValue = strValue.substring(0, strValue.indexOf("."));
						}else if(strValue.indexOf(".") == 0 && StringUtils.isNumeric(strValue.substring(strValue.indexOf(".")+1))) {
							strValue = "0";
						}
						if(StringUtils.isNumeric(strValue)) {
							Integer.parseInt(strValue);
						}
					}
				}catch(Exception ex){
					informat = null;
					valid = false;
				}
			}else if(toDataType == DataType.LONG) {
				informat = Informat.LONG;
				try {
					if(Objects.nonNull(value)) {
						strValue = (String)value;
						if(strValue.indexOf(".") > 0) {
							strValue = strValue.substring(0, strValue.indexOf("."));
						}else if(strValue.indexOf(".") == 0 && StringUtils.isNumeric(strValue.substring(strValue.indexOf(".")+1))) {
							strValue = "0";
						}
							
						if(StringUtils.isNumeric(strValue)) {
							Long.parseLong(strValue);
						}
					}
				}catch(Exception e){
					informat = null;
					valid = false;
				}
			}else if(toDataType == DataType.FLOAT) {
				informat = Informat.FLOAT;
				try {
					if(Objects.nonNull(value)) {
						strValue = (String)value;
						Float.parseFloat(strValue);
					}
				}catch(Exception e){
					informat = null;
					valid = false;
				}
			}else if(toDataType == DataType.DOUBLE) {
				informat = Informat.DOUBLE;
				try {
					if(Objects.nonNull(value)) {
						strValue = (String)value;
						Double.parseDouble(strValue);
					}
				}catch(Exception e){
					informat = null;
					valid = false;
				}
			}else if(toDataType == DataType.LOCAL_DATE) {
				informat = Informat.LOCAL_DATE;
				DateTimeFormatter formatter = null;
				if(Objects.nonNull(value)) {
					strValue = (String)value;
					formatter = detectLocalDateFormatter(strValue);
					if(formatter== null) {
						informat = null;
						valid = false;
					}
				}
				if(informat != null)
					informat.formatter = formatter;
			}else if(toDataType == DataType.LOCAL_TIME) {
				informat = Informat.LOCAL_TIME;
				DateTimeFormatter formatter = null;
				if(Objects.nonNull(value)) {
					strValue = (String)value;
					formatter = detectLocalTimeFormatter(strValue);
					if(formatter == null) {
						informat = null;
						valid = false;
					}
				}
				if(informat != null)
					informat.formatter = formatter;
			}else if(toDataType == DataType.LOCAL_DATE_TIME) {
				informat = Informat.LOCAL_DATE_TIME;
				DateTimeFormatter formatter = null;
				if(Objects.nonNull(value)) {
					strValue = (String)value;
					formatter = detectLocalDateTimeFormatter(strValue);
					if(formatter == null) {
						informat = null;
						valid = false;
					}
				}
				if(informat != null)
					informat.formatter = formatter;
			}
		}

		/*
		if(toDataType == DataType.BOOLEAN) {
			dataType = DataType.BOOLEAN;
			for(int i=0;i<values.size();i++) {
				String value = values.get(i).toString().trim();
				if(Objects.nonNull(values.get(i)) && !value.isEmpty()) {
					if(
						(!value.equals("1") && !value.equals("0")) && 
						(!value.equalsIgnoreCase("t") && !value.equalsIgnoreCase("f")) &&
						(!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false"))
					) {
						dataType = null;
						break;
					}
				}
			}
		}else if(toDataType == DataType.INTEGER) {
			if(fromDataType == DataType.FLOAT || fromDataType == DataType.DOUBLE ||
				fromDataType == DataType.LOCAL_DATE || fromDataType == DataType.LOCAL_TIME ||
				fromDataType == DataType.LOCAL_DATE_TIME)
				return true; 
			
			dataType = DataType.INTEGER;
			for(int i=0;i<values.size();i++) {
				try {
					if(Objects.nonNull(values.get(i)))
						Integer.parseInt(values.get(i).toString());
				}catch(Exception e){
					dataType = null;
					break;
				}
			}
		}else if(toDataType == DataType.DOUBLE){
			dataType = DataType.DOUBLE;
			for(int i=0;i<values.size();i++) {
				try {
					if(Objects.nonNull(values.get(i)))
						Double.parseDouble(values.get(i).toString());
				}catch(Exception e){
					dataType = null;
					break;
				}
			}
		}else if(toDataType == DataType.LONG){
			dataType = DataType.LONG;
			for(int i=0;i<values.size();i++) {
				try {
					if(Objects.nonNull(values.get(i)))
						Long.parseLong(values.get(i).toString());
				}catch(Exception e){
					dataType = null;
					break;
				}
			}
		}else if(toDataType == DataType.FLOAT){
			dataType = DataType.FLOAT;
			for(int i=0;i<values.size();i++) {
				try {
					if(Objects.nonNull(values.get(i)))
						Float.parseFloat(values.get(i).toString());
				}catch(Exception e){
					dataType = null;
					break;
				}
			}
		}else if(toDataType == DataType.STRING) {
			dataType = DataType.STRING;
		}*/
		
		
		return informat;
		
	}
	
	/*
	static void convertValues(ArrayList<Object> values, DataType fromDataType, DataType toDataType) {
		for(int i=0;i<values.size();i++) {
			if(fromDataType == DataType.INTEGER){
				Integer value = (int)values.get(i);
				if(toDataType == DataType.LONG) {
					values.set(i, Long.parseLong(value.toString()));
				}else if(toDataType == DataType.FLOAT){
					values.set(i, Float.parseFloat(value.toString()));
				}else if(toDataType == DataType.DOUBLE){
					values.set(i, Double.parseDouble(value.toString()));
				}else if(toDataType == DataType.STRING){
					values.set(i, value.toString());
				}
			}else if(fromDataType == DataType.LONG){
				Long value = (long)values.get(i);
				if(toDataType == DataType.INTEGER) {
					values.set(i, value.intValue());
				}else if(toDataType == DataType.FLOAT){
					values.set(i, (float)value);
				}else if(toDataType == DataType.DOUBLE){
					values.set(i, (double)value);
				}else if(toDataType == DataType.STRING){
					values.set(i, value.toString());
				}
			}else if(fromDataType == DataType.FLOAT){
				Float value = (float)values.get(i);
				if(toDataType == DataType.INTEGER) {
					values.set(i, value.intValue());
				}else if(toDataType == DataType.LONG){
					values.set(i, Long.parseLong(value.toString()));
				}else if(toDataType == DataType.DOUBLE){
					values.set(i, (double)value);
				}else if(toDataType == DataType.STRING){
					values.set(i, value.toString());
				}
			}else if(fromDataType == DataType.DOUBLE){
				Double value = (double)values.get(i);
				if(toDataType == DataType.LONG) {
					values.set(i, Long.parseLong(value.toString()));
				}else if(toDataType == DataType.FLOAT){
					values.set(i, Float.parseFloat(value.toString()));
				}else if(toDataType == DataType.INTEGER){
					values.set(i, value.intValue());
				}else if(toDataType == DataType.STRING){
					values.set(i, value.toString());
				}
			}else if(fromDataType == DataType.STRING){
				String value = values.get(i).toString();
				if(toDataType == DataType.LONG) {
					values.set(i, Long.parseLong(value));
				}else if(toDataType == DataType.FLOAT){
					values.set(i, Float.parseFloat(value));
				}else if(toDataType == DataType.DOUBLE){
					values.set(i, Double.parseDouble(value));
				}else if(toDataType == DataType.INTEGER){
					values.set(i, Integer.parseInt(value));
				}
			}
		}
	}*/
	
	static Informat getInformatByVariableNameFromParser(String name, Map<String, Informat> parser) {
		
		Informat informat = parser.get(name);
		return informat;
		
	}
	
	static Informat getInformatByDataType(DataType dataType) {
		
		Informat informat = null;
		
		if(dataType == DataType.INTEGER)
			informat = Informat.INTEGER;
		else if(dataType == DataType.LONG)
			informat = Informat.LONG;
		else if(dataType == DataType.FLOAT)
			informat = Informat.FLOAT;
		else if(dataType == DataType.DOUBLE)
			informat = Informat.DOUBLE;
		else if(dataType == DataType.CHARACTER)
			informat = Informat.CHARACTER;
		else if(dataType == DataType.BOOLEAN)
			informat = Informat.BOOLEAN;
		else if(dataType == DataType.STRING)
			informat = Informat.STRING;
		else if(dataType == DataType.LOCAL_DATE)
			informat = Informat.LOCAL_DATE;
		else if(dataType == DataType.LOCAL_TIME)
			informat = Informat.LOCAL_TIME;
		else if(dataType == DataType.LOCAL_DATE_TIME)
			informat = Informat.LOCAL_DATE_TIME;
		
		return informat;
	}
	
	static DataType getDataTypeByInformat(Informat informat) {
		
		DataType dataType = null;
		
		if(informat.isIntegerPattern)
			dataType = DataType.INTEGER;
		else if(informat.isLongPattern)
			dataType = DataType.LONG;
		else if(informat.isFloatPattern)
			dataType = DataType.FLOAT;
		else if(informat.isDoublePattern)
			dataType = DataType.DOUBLE;
		else if(informat.isStringPattern)
			dataType = DataType.STRING;
		else if(informat.isCharacterPattern)
			dataType = DataType.CHARACTER;
		else if(informat.isBooleanPattern)
			dataType = DataType.BOOLEAN;
		else if(informat.isDatePattern == true)
			dataType = DataType.LOCAL_DATE;
		else if(informat.isTimePattern == true)
			dataType = DataType.LOCAL_TIME;
		else if(informat.isDateTimePattern == true)
			dataType = DataType.LOCAL_DATE_TIME;
		
		return dataType;
	}
	
	public static Object convertValue(Object value, DataType fromDataType, Informat informat) {
		
		Object retValue = null;
		DataType toDataType = HSUtils.getDataTypeByInformat(informat);
		
		if(fromDataType == toDataType)
			return value;
		
		try {
		if(fromDataType == DataType.STRING) {
			String strValue = ((String)value).trim();
			if(toDataType == DataType.INTEGER) {
				if(strValue.indexOf(".") > 0 && 
					StringUtils.isNumeric(strValue.substring(strValue.indexOf(".")+1)) && 
					StringUtils.isNumeric(strValue.substring(0, strValue.indexOf(".")))
				) {
					retValue = Integer.parseInt(strValue.substring(0, strValue.indexOf(".")));
				}else if(strValue.indexOf(".") == 0 && StringUtils.isNumeric(strValue.substring(strValue.indexOf(".")+1))) {
					retValue = 0;
				}else{
					retValue = Integer.parseInt(strValue);
				}
			}else if(toDataType == DataType.LONG){
				if(strValue.indexOf(".") > 0 && 
					StringUtils.isNumeric(strValue.substring(strValue.indexOf(".")+1)) && 
					StringUtils.isNumeric(strValue.substring(0, strValue.indexOf(".")))
				) {
					retValue = Long.parseLong(strValue.substring(0, strValue.indexOf(".")));
				}else if(strValue.indexOf(".") == 0 && StringUtils.isNumeric(strValue.substring(strValue.indexOf(".")+1))) {
					retValue = 0L;
				}else{
					try {
						retValue = Long.parseLong(strValue);
					}catch(NumberFormatException nf){
						retValue = null;
					}
				}
			}else if(toDataType == DataType.FLOAT){
				retValue = Float.parseFloat(strValue);
			}else if(toDataType == DataType.DOUBLE){
				try{
					retValue = Double.parseDouble(strValue);
				}catch(NumberFormatException nf) {
					retValue = null;
				}
			}else if(toDataType == DataType.BOOLEAN){
				if(strValue.equalsIgnoreCase("true"))
					retValue = true;
				else if(strValue.equalsIgnoreCase("false"))
					retValue = false;
				else if(strValue.equalsIgnoreCase("t"))
					retValue = true;
				else if(strValue.equalsIgnoreCase("f"))
					retValue = false;
				else if(strValue.equalsIgnoreCase("1"))
					retValue = true;
				else if(strValue.equalsIgnoreCase("0"))
					retValue = false;
				else {
					//need to throw exception
				}
			}else if(toDataType == DataType.CHARACTER){
				retValue = strValue.charAt(0);
			}else if(toDataType == DataType.LOCAL_DATE){
				if(informat.formatter != null)
					retValue = LocalDate.parse(strValue, informat.formatter);
			}else if(toDataType == DataType.LOCAL_TIME){
				if(informat.formatter != null)
					retValue = LocalTime.parse(strValue, informat.formatter);
			}else if(toDataType == DataType.LOCAL_DATE_TIME){
				if(informat.formatter != null)
					retValue = LocalDateTime.parse(strValue, informat.formatter);
			}
		}
		
		}catch(Exception ex) {
			//HS.writeStackTraceToLog(ex);
		}
		
		return retValue;
	}
	
	public static IDataSet createEmptyDataSet() {
		return null; //new DataSet();
	}
	
	static DataType getDataTypeByString(String dataTypeName) {
		DataType dataType = null;
		if(dataTypeName.equalsIgnoreCase("INTEGER"))
			dataType = DataType.INTEGER;
		else if(dataTypeName.equalsIgnoreCase("LONG"))
			dataType = DataType.LONG;
		else if(dataTypeName.equalsIgnoreCase("FLOAT"))
			dataType = DataType.FLOAT;
		else if(dataTypeName.equalsIgnoreCase("DOUBLE"))
			dataType = DataType.DOUBLE;
		else if(dataTypeName.equalsIgnoreCase("BOOLEAN"))
			dataType = DataType.BOOLEAN;
		else if(dataTypeName.equalsIgnoreCase("CHARACTER"))
			dataType = DataType.CHARACTER;
		else if(dataTypeName.equalsIgnoreCase("STRING"))
			dataType = DataType.STRING;
		else if(dataTypeName.equalsIgnoreCase("LOCAL_DATE"))
			dataType = DataType.LOCAL_DATE;
		else if(dataTypeName.equalsIgnoreCase("LOCAL_TIME"))
			dataType = DataType.LOCAL_TIME;
		else if(dataTypeName.equalsIgnoreCase("LOCAL_DATE_TIME"))
			dataType = DataType.LOCAL_DATE_TIME;
		
		return dataType;
	}
	
	public static IVariableFactory getVariableFactory(StorageType storageType) {
		return null;// new VariableFactory(storageType);
		
	}
	
	static int getObjectSizeInBytes(Object obj) {
		ByteArrayOutputStream bos = null;
		ObjectOutputStream out = null;
		byte[] bytes = null;
		try {
			bos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bos);   
			out.writeObject(obj);
			bytes = bos.toByteArray();
		}catch(Exception ex){
			//HS.writeStackTraceToLog(ex);
		}finally {
			try {
				out.close();
				bos.close();
			} catch (IOException ex) {
				// ignore close exception
			}
		}
		
		return bytes.length;
	}
	
	static HSList toHSList(ArrayList<?> values, StorageType storageType) {
		
		HSList<Object> newValues = null;
		if(storageType == StorageType.DISK)
			newValues = new DList<Object>();
		else
			newValues = new MList<Object>();
		
		for(int i=0;i<values.size();i++) {
			newValues.add(values.get(i));
		}
		
		return newValues;
	}
	
	static HSList getHSList(StorageType storageType) {
		
		HSList<Object> newValues = null;
		if(storageType == StorageType.DISK)
			newValues = new DList<Object>();
		else
			newValues = new MList<Object>();
		
		return newValues;
	}
	
	public static int compareValue(Object value1, Object value2, DataType dy) {
		
		int cmp = 0;
		
		boolean hasNull = false;
		if(value1 == null && value2 != null){
			cmp = -1;
			hasNull = true;
		}else if(value1 != null && value2 == null){
			cmp = 1;
			hasNull = true;
		}else if(value1 == null && value2 == null) {
			cmp = 0;
			hasNull = true;
		}
		
		if(hasNull == true){
			return cmp;
		}
		
		if(dy == DataType.INTEGER){
			int v1 = (int)value1;
			int v2 = (int)value2;
			if(v1 < v2)
				cmp = -1;
			else if(v1 > v2)
				cmp = 1;
			else
				cmp = 0;
		}else if(dy == DataType.LONG){
			long v1 = (long)value1;
			long v2 = (long)value2;
			if(v1 < v2)
				cmp = -1;
			else if(v1 > v2)
				cmp = 1;
			else
				cmp = 0;
		}else if(dy == DataType.FLOAT){
			float v1 = (float)value1;
			float v2 = (float)value2;
			if(v1 < v2)
				cmp = -1;
			else if(v1 > v2)
				cmp = 1;
			else
				cmp = 0;
		}else if(dy == DataType.DOUBLE){
			double v1 = (double)value1;
			double v2 = (double)value2;
			if(v1 < v2)
				cmp = -1;
			else if(v1 > v2)
				cmp = 1;
			else
				cmp = 0;
		}else if(dy == DataType.STRING){
			String v1 = (String)value1;
			String v2 = (String)value2;
			v1 = v1.trim();
			v2 = v2.trim();
			if(v1.compareTo(v2) < 0)
				cmp = -1;
			else if(v1.compareTo(v2) > 0)
				cmp = 1;
			else
				cmp = 0;
		}else if(dy == DataType.CHARACTER){
			char v1 = (char)value1;
			char v2 = (char)value2;
			if(v1 < v2)
				cmp = -1;
			else if(v1 > v2)
				cmp = 1;
			else
				cmp = 0;
		}else if(dy == DataType.BOOLEAN){
			boolean v1 = (boolean)value1;
			boolean v2 = (boolean)value2;
			if(v1==false && v2==true)
				cmp = -1;
			else if(v1==true && v2==false)
				cmp = 1;
			else
				cmp = 0;
		}else if(dy == DataType.LOCAL_DATE){
			LocalDate v1 = (LocalDate)value1;
			LocalDate v2 = (LocalDate)value2;
			if(v1.isBefore(v2))
				cmp = -1;
			else if(v1.isAfter(v2))
				cmp = 1;
			else
				cmp = 0;
		}else if(dy == DataType.LOCAL_TIME){
			LocalTime v1 = (LocalTime)value1;
			LocalTime v2 = (LocalTime)value2;
			if(v1.isBefore(v2))
				cmp = -1;
			else if(v1.isAfter(v2))
				cmp = 1;
			else
				cmp = 0;
		}else if(dy == DataType.LOCAL_DATE_TIME){
			LocalDateTime v1 = (LocalDateTime)value1;
			LocalDateTime v2 = (LocalDateTime)value2;
			if(v1.isBefore(v2))
				cmp = -1;
			else if(v1.isAfter(v2))
				cmp = 1;
			else
				cmp = 0;
		}
		
		return cmp;
	}//end of compareValue() method
	
	static String repeat(char c, int n) {
		String str = "";
		for(int i=0;i<n;i++){
			str += c;
		}
		return str;
	}
	
	public static Object getFirstNonNull(Object... obj) {
		return ObjectUtils.firstNonNull(obj);
	}		
	
	/*
	public static IDataSet getSampleDataSet(String dbName_tableName) {
		
		IDataSet dataSet = null;
		VariableFactory cf = getVariableFactory(StorageType.MEMORY);
		String[] tb = dbName_tableName.split("\\.");
		String dbName = tb[0].trim();
		String tableName = tb[1].trim();
		if(dbName.equalsIgnoreCase("college")) {
			
			if(tableName.equalsIgnoreCase("student")){
				dataSet = new DataSet(
					cf.createIntegerVariable("StudentID", createIntegerList(1,2,3,4,5,6)),
					cf.createStringVariable("StudentName", createStringList("Alice", "Bob", "Chris", "David", "Eve", "Fred"))
				);
			}else if(tableName.equalsIgnoreCase("supervisor")) {
				dataSet = new DataSet(
					cf.createIntegerVariable("SupervisorID", createIntegerList(1,2,3,4)),
					cf.createStringVariable("SupervisorName", createStringList("Black", "White", "Brown", "Green"))
				);
			}else if(tableName.equalsIgnoreCase("course")) {
				dataSet = new DataSet(
					cf.createIntegerVariable("CourseID", createIntegerList(1,2,3,4)),
					cf.createStringVariable("CourseName", createStringList("Maths", "Physics", "Chemistry", "Computers"))
				);
			}else if(tableName.equalsIgnoreCase("lecturer")) {
				dataSet = new DataSet(
					cf.createIntegerVariable("LecturerID", createIntegerList(1,2,3)),
					cf.createStringVariable("LecturerName", createStringList("Gauss", "Brunel", "Shanon"))
				);
			}else if(tableName.equalsIgnoreCase("course_lecturer")) {
				dataSet = new DataSet(
					cf.createIntegerVariable("CourseID", createIntegerList(1,2,3,4)),
					cf.createIntegerVariable("LecturerID", createIntegerList(1,2,2,3))
				);
			}else if(tableName.equalsIgnoreCase("student_course_supervisor")) {
				dataSet = new DataSet(
					cf.createIntegerVariable("StudentID", createIntegerList(1,1,1,2,2,3,3,3,4,4,5,5,5,6)),
					cf.createIntegerVariable("CourseID", createIntegerList(1,3,2,1,2,2,3,4,4,2,1,4,3,4)),
					cf.createIntegerVariable("SupervisorID", createIntegerList(1,3,3,2,3,4,3,2,1,4,2,2,4,1))
				);
			}
		}
		
		return dataSet;
			
	}*/
	

}