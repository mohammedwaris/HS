package com.silverbrain.hs.core;

import java.util.*;
import java.io.*;
import com.opencsv.*;
import java.util.zip.*;
import java.time.*;
import java.time.format.*;
import com.epam.parso.*;
import com.epam.parso.impl.*;
import java.util.function.*;



public class DataSet implements IDataSet, Serializable{
	
	
	private String label;
	private String name;
	private ArrayList<Variable> variables;
	private ArrayList<DataSet.FLVariable> flVariables;
	private HSList<Integer> sortIndex;
	private String logMessage;
	private StorageType storageType;
	
	
	long serialVersionUID = 313177427174559407L;
	
	private DataSet() {
		variables = new ArrayList<Variable>();
		flVariables = new ArrayList<DataSet.FLVariable>();
		sortIndex = new MList<Integer>();
	}
	
	public DataSet(Variable... variables) {
		this(StorageType.MEMORY, variables);
	}
	
	DataSet(StorageType storageType, Variable... variables) {
		this(storageType, new ArrayList<Variable>(Arrays.asList(variables)));
	}
	
	public DataSet(ArrayList<Variable> variables) {
		this(StorageType.MEMORY, variables);
	}

	DataSet(StorageType storageType, ArrayList<Variable> variables) {
			
			this.storageType = storageType;
			
			//ArrayList<Variable> newVariables = new ArrayList<Variable>();
			//for(int i=0;i<variables.size(); i++) { 
				//newVariables.add(variables.get(i).getCopy());
			//}
			
			ArrayList<Integer> observationCounts = new ArrayList<Integer>();
			for(Variable variable:variables) {
				if(variable.storageType == StorageType.DISK)
					this.storageType = StorageType.DISK;
				observationCounts.add(variable.values.size());
			}
			
			int maxObservation = 0;
			for(int observationCount:observationCounts) {
				if(maxObservation < observationCount)
					maxObservation = observationCount;
			}
			
			for(int i=0;i<variables.size();i++){
				while(variables.get(i).values.size() != maxObservation)
					variables.get(i).values.add(null);	
			}
			
			this.variables = variables;
			

			this.sortIndex = new MList<Integer>();
			
			for(int i=0;i<maxObservation;i++) {
				sortIndex.add(i);
			}

			this.flVariables = new ArrayList<DataSet.FLVariable>();
			
			this.label = "";
	}
	
	@Override
	public String getLabel() {
		return this.label;
	}

	@Override
	public IDataSet setLabel(String label) {
		DataSet dataSet = this;
		dataSet.label = label;
		return dataSet;
	}
	
	@Override
	public String getName() {
		String name = this.name;
		if(Objects.isNull(name))
			name = "DataSet" + hashCode();
		return name;
	}
	
	@Override
	public IDataSet setName(String name) {
		DataSet dataSet = this;
		dataSet.name = name;
		return dataSet;
	}
	
	@Override
	public StorageType getStorageType() {
		return this.storageType;
	}
	
	@Override
	public ArrayList<String> getVariableNames() {
		ArrayList<String> variableNames = new ArrayList<String>();
		for(Variable variable: variables)
			variableNames.add(variable.name);
		return variableNames;
	}

	@Override
	public int getVariableIndex(String variableName) {
		int c = -1;
		int size = variables.size();
		for(int i=0;i<size;i++) {
			if(getVariableByName(variableName) == variables.get(i)) {
				c = i;
				break;
			}
		}
		return c;
	}

	@Override
	public int getVariableCount() {
		int variableCount = variables.size();
		return variableCount;
	}

	@Override
	public int getObservationCount() {
		int observationCount = sortIndex.size();
		return observationCount;
	}

	public Variable getVariableByName(String variableName) {
		Variable variable = null;
		variableName = variableName.trim();
		int size = variables.size();
		for(int i=0;i<size;i++) {
			if(variables.get(i).name.equalsIgnoreCase(variableName)) {
				variable = variables.get(i);
				break;
			}
		}
		return variable;
	}
	
	public Variable getVariableByIndex(int position) {
		Variable variable = null;
		int size = variables.size();
		for(int i=0;i<size;i++) {
			if(i == position) {
				variable = variables.get(i);
				break;
			}
		}
		return variable;
	}
	
	private DataSet.FLVariable getFirstFLVariableByName(String variableName) {
		FLVariable flCol = null;
		int size = flVariables.size();
		for(int i=0;i<size;i++) {
			if(flVariables.get(i).name.equalsIgnoreCase("first." + variableName.trim())) {
				flCol = flVariables.get(i);
				break;
			}
		}
		return flCol;
	}
	
	private DataSet.FLVariable getLastFLVariableByName(String variableName) {
		FLVariable flCol = null;
		int size = flVariables.size();
		for(int i=0;i<size;i++) {
			if(flVariables.get(i).name.equalsIgnoreCase("last." + variableName.trim())) {
				flCol = flVariables.get(i);
				break;
			}
		}
		return flCol;
	}
	
	//need to check this method
	public ArrayList<Variable> getVariables() {
		return this.variables;
	}
	
	//need to check this too
	private void setVariables(ArrayList<Variable> variables) {
		this.variables =  variables;
	}
	
	@Override
	public IDataSet duplicateObservations(String key) {

		//HS.setLogMute(true);
		
		key = key.trim();
		throwExceptionIfKeyEmpty(key);

		if(key.equals("*")) 
			key = String.join(" ", getVariableNames().toArray(new String[0]));

		key = key.replaceAll(" +", " "); //replace all multiple spaces with single space
		String keys[] = key.split(" ");
		String sortKeys[] = new String[keys.length];
		for(int i=0;i<keys.length;i++) {
			sortKeys[i] = keys[i].trim();
			throwExceptionIfKeyNotPresent(sortKeys[i]);
		}
		
		DataSet dataSet = this;
		
		
		dataSet.sort(key, false);
		
		dataSet.where((observation) -> {
			
			Boolean firstValue = observation.getFirstValue(sortKeys[sortKeys.length-1]); 
			Boolean lastValue = observation.getLastValue(sortKeys[sortKeys.length-1]);
			
			boolean keep = true;
			if(firstValue == true && lastValue == true)
				keep = false;
			else if(firstValue == true && lastValue == false)
				keep = false;
			
			return keep;
		});
		
		//HS.setLogMute(false);
		
		
		return dataSet;
	}//end of duplicateObservations() method
	
	@Override
	public IDataSet uniqueObservations(String key) {
		
		DataSet dataSet = this;
		
		String keys[] = null;
		String fsortKeys[] = null;
		try{
			key = key.trim();
			dataSet.throwExceptionIfKeyEmpty(key);

			if(key.equals("*")) 
				key = String.join(" ", getVariableNames().toArray(new String[0]));

			key = key.replaceAll(" +", " "); //replace all multiple spaces with single space
			keys = key.split(" ");
			fsortKeys = new String[keys.length];
			for(int i=0;i<keys.length;i++) {
				fsortKeys[i] = keys[i].trim();
				dataSet.throwExceptionIfKeyNotPresent(fsortKeys[i]);
			}
		}catch(IllegalArgumentException ie) {
			HS.writeStackTraceToLog(ie);
			return null;
		}
		
		final String sortKeys[] = fsortKeys;
			
		//HS.setLogMute(true);
		dataSet.sort(key, false);
		//HS.setLogMute(false);
		
		
		
		//HS.setLogMute(true);
		dataSet.where(observation -> {
			
			Boolean firstValue = observation.getFirstValue(sortKeys[sortKeys.length-1]);
			Boolean lastValue = observation.getLastValue(sortKeys[sortKeys.length-1]);
			
			boolean keep = false;
			if(firstValue == true && lastValue == true)
				keep = true;
			else if(firstValue == true && lastValue == false)
				keep = true;
			
			return keep;
		});
		
		//HS.setLogMute(false);
		
		return dataSet;
	}//end of uniqueObservations() method
	

	public IDataSet qsort(String keys) {
		
		Timer timer = new Timer();		
		timer.start();

		
		// dataSet = null;
		
		DataSet dataSet = this;
		

		keys = keys.trim().replaceAll(" +", " ").replaceAll(" :", ":").replaceAll(": ", ":");
		String a_keys[] = keys.split(" ");
		String sortKeys[] = new String[a_keys.length];
		

		for(int i=0;i<a_keys.length;i++) {
			if(a_keys[i].endsWith(":desc") || a_keys[i].endsWith(":asc"))
				sortKeys[i] = a_keys[i].substring(0, a_keys[i].indexOf(":")).trim();
			else
				sortKeys[i] = a_keys[i].trim();
		}
		
		for(int i=0;i<sortKeys.length;i++){
			if(dataSet.hasVariable(sortKeys[i]) == false){
				logMessage = String.format("invalid sort key -%s", keys);
				HS.writeLog(LogMessageType.ERROR, logMessage);
				return null;
			}
		}
		
		int observationCount = dataSet.getObservationCount();
		//Sorting for the first key
		qsortVariable(dataSet, dataSet.getVariableByName(sortKeys[0]), 0, observationCount, a_keys[0].endsWith(":desc"));
		//return dataSet;
		//Sorting for rest of the keys
		ArrayList<ArrayList<ByGroup>> byGroupsList = new ArrayList<ArrayList<ByGroup>>();
		for(int n=1;n<=sortKeys.length;n++) {
			ByGroup byGroup = null;
			ArrayList<ByGroup> byGroupList = null;
			
			byGroupList = new ArrayList<ByGroup>();
			
			byGroupsList.add(byGroupList);
			
			int ctr=0, start=0, end=0,duplicate=0;
			boolean equal = false;
			
			Variable prevVariable = null;
			
			while(ctr < observationCount-1) {
				for(int s=n-1;s>=0;s--){
					equal = true;
					prevVariable = dataSet.getVariableByName(sortKeys[s]);
					int cmp = HSUtils.compareValue(prevVariable.values.get(dataSet.sortIndex.get(ctr)), prevVariable.values.get(dataSet.sortIndex.get(ctr+1)), prevVariable.dataType);
					if(cmp != 0) {
						equal = false;
						break;
					}
				}

				//If its a last record
				if(ctr+2 == observationCount) {
					if(equal == true) {
						start = end;
						duplicate += 1;
						end = start + duplicate + 1;
				
						byGroup = new ByGroup(start, end);
						byGroupList.add(byGroup);
						if((end-start) > 1 && n > 0 && n < sortKeys.length)
							qsortVariable(dataSet, dataSet.getVariableByName(sortKeys[n]), start, end, a_keys[n].endsWith(":desc"));
					}else if(equal == false) {
						start = end;
						end = start + duplicate + 1;
					
						byGroup = new ByGroup(start, end);
						byGroupList.add(byGroup);
						if((end-start) > 1 && n > 0 && n < sortKeys.length)
							qsortVariable(dataSet, dataSet.getVariableByName(sortKeys[n]), start, end, a_keys[n].endsWith(":desc"));
						start = end;
						end = observationCount;
						
						byGroup = new ByGroup(start, end);
						byGroupList.add(byGroup);
						if((end-start) > 1 && n > 0 && n < sortKeys.length)
							qsortVariable(dataSet, dataSet.getVariableByName(sortKeys[n]), start, end, a_keys[n].endsWith(":desc"));
					}	
				}else{
					if(equal == true) {
							duplicate += 1;
					}else if(equal == false){
						start = end;
						end = start + duplicate + 1;
						
						byGroup = new ByGroup(start, end);
						byGroupList.add(byGroup);
						if((end-start) > 1 && n > 0 && n < sortKeys.length)
							qsortVariable(dataSet, dataSet.getVariableByName(sortKeys[n]), start, end, a_keys[n].endsWith(":desc"));
						duplicate = 0;
					}
				}
				ctr += 1;
			}//end while
		}//end for n
		
		dataSet.flVariables.clear();
		FLVariable fVariable = null, lVariable = null;
		int n = 1;
		HSList<Boolean> values = null;
		int start, end;
		for(ArrayList<ByGroup> byGroupList: byGroupsList){
			StorageType variableStorageType = dataSet.getVariableByName(sortKeys[n-1].toLowerCase()).storageType;
			if(variableStorageType == StorageType.DISK) 
				values = new DList<Boolean>();
			else
				values = new MList<Boolean>();
			
			fVariable = new DataSet.FLVariable("first." + sortKeys[n-1].toLowerCase(), values);
			fVariable.fillFalse(observationCount);
			if(variableStorageType == StorageType.DISK) 
				values = new DList<Boolean>();
			else
				values = new MList<Boolean>();
			lVariable = new DataSet.FLVariable("last." + sortKeys[n-1].toLowerCase(), values);
			lVariable.fillFalse(observationCount);
			for(ByGroup byGroup:byGroupList) {
				start = byGroup.getStart();
				end = byGroup.getEnd();
				//System.out.println("start: " + start + " end: " + end + " n: " + n);
				//if(end-start >= 1 && n >= 1 && n < sortKeys.length)
					//msortVariable(dataSet, dataSet.getVariableByName(sortKeys[n]), start, end, a_keys[n].endsWith(":desc"));
				fVariable.values.set(dataSet.sortIndex.get(start), true);
				lVariable.values.set(dataSet.sortIndex.get(end-1), true);	
				
				
			}
			dataSet.flVariables.add(fVariable);		
			dataSet.flVariables.add(lVariable);
			n += 1;
		}
		
		timer.stop();
		
		//String logMsg = String.format("qsort(\"%s\", %s) took: %s sec(s)", keys, out, timer.timeElapsed());
		String logMsg = String.format("qsort(\"%s\", %s) took: %s sec(s)", keys, timer.timeElapsed());
		HS.writeLog(LogMessageType.NOTE, logMsg);
		
		
		return dataSet;
	}//end of qsort() method
	
	private void qsortVariable(DataSet dataSet, Variable variable, int start, int end, boolean desc) {
		int cmp;
		boolean sorted = true;
		//final int size = variable.values.size() - 1;
		for(int i=start;i<end-1;i++) {
			cmp = HSUtils.compareValue(variable.values.get(dataSet.sortIndex.get(i)), variable.values.get(dataSet.sortIndex.get(i+1)), variable.dataType);
			if(desc == true && cmp < 0) {
				sorted = false;
				break;
			}
			else if(desc == false && cmp > 0) {
				sorted = false;
				break;
			}
		}
		if(sorted == false)
			quickSort(dataSet, variable, start, end-1, desc);
	}//end of qsortVariable()
	
	private void quickSort(DataSet dataSet, Variable variable, int p, int r, boolean desc)  
    {  		
		while (r - p >= 1) {
			int q = quickSortPartition(dataSet, variable, p, r, desc);

			if (q - p <= (r - p) / 2) {
				// the left-hand partition is smaller; sort it recursively
				quickSort(dataSet, variable, p, q - 1, desc);
				// update p so as to sort the right-hand partition iteratively
				p = q + 1;
			} else {
				// the right-hand partition is smaller; sort it recursively
				quickSort(dataSet, variable, q + 1, r, desc);
				// update r so as to sort the left-hand partition iteratively
				r = q - 1;
			}
		}
    } 

	private int quickSortPartition(DataSet dataSet, Variable variable, int low, int high, boolean desc) {
		Object pivot = variable.values.get(dataSet.getSortIndexValueAt(high));
        int i = (low-1);
		int temp;
        for (int j=low; j<high; j++) 
        { 
			int cmp = HSUtils.compareValue(variable.values.get(dataSet.getSortIndexValueAt(j)), 
									pivot, 
									variable.dataType);
           
			if ((desc == false && cmp < 0) || (desc == true && cmp > 0)) 
            { 
                i++; 
				temp = dataSet.sortIndex.get(i);
                dataSet.sortIndex.set(i, dataSet.sortIndex.get(j));
                dataSet.sortIndex.set(j, temp);
            }
        } 
		
		temp = dataSet.sortIndex.get(i+1);
        dataSet.sortIndex.set(i+1, dataSet.sortIndex.get(high));
        dataSet.sortIndex.set(high, temp);
  
        return i+1; 
	}
	
	public DataSet getFLVariables() {
	/*	ArrayList<Variable> newVariables = new ArrayList<Variable>();
		Variable variable;
		String variableName, flVariableName;
		for(int i=0;i<variables.size();i++){
			variableName = variables.get(i).name;
			
			if(i%2 == 0) {
				variableName = flVariableName.substring(flVariableName.indexOf(".")+1);
				System.out.println(variableName);
				variable = getVariableByName(variableName);
				newVariables.add(variable.getCopy());
			}
			newVariables.add(Variable.create(flVariableName.replace(".","_"), DataType.BOOLEAN, flVariables.get(i).values));
		}
		DataSet dataSet = new DataSet(newVariables);*/
		
		return null;
	}

	@Override
	public IDataSet sort(String keys) {
		return sort(keys, HS.defaultOut);
	}
	
	@Override
	public IDataSet sort(String keys, boolean out) {
		
		Timer timer = new Timer();		
		timer.start();
	
		DataSet dataSet = this;
		if(out)
			dataSet = this.getCopy();

		keys = keys.trim().replaceAll(" +", " ").replaceAll(" :", ":").replaceAll(": ", ":");
		String a_keys[] = keys.split(" ");
		String sortKeys[] = new String[a_keys.length];
		

		for(int i=0;i<a_keys.length;i++) {
			if(a_keys[i].endsWith(":desc") || a_keys[i].endsWith(":asc"))
				sortKeys[i] = a_keys[i].substring(0, a_keys[i].indexOf(":")).trim();
			else
				sortKeys[i] = a_keys[i].trim();
		}
		
		for(int i=0;i<sortKeys.length;i++){
			if(dataSet.hasVariable(sortKeys[i]) == false){
				
				logMessage = String.format("invalid sort key - %s", keys);
				HS.writeLog(LogMessageType.ERROR, logMessage);

			}
		}
		

		//Sorting for the first key
		sortVariable(dataSet, dataSet.getVariableByName(sortKeys[0]), 0, dataSet.getObservationCount(), a_keys[0].endsWith(":desc"));

		//Sorting for rest of the keys
		ArrayList<HashMap<String, Integer>> groupList = null;
		ArrayList<ArrayList<HashMap<String, Integer>>> groupLists = new ArrayList<ArrayList<HashMap<String, Integer>>>();
		HashMap<String, Integer> groupMap = null;
		for(int n=1;n<=sortKeys.length;n++) {
			groupList = new ArrayList<HashMap<String, Integer>>();
			groupLists.add(groupList);
			int ctr=0, start=0, end=0,duplicate=0;
			boolean equal = false;
			
			Variable prevVariable = null;
			
			while(ctr < dataSet.getObservationCount()-1) {
				for(int s=n-1;s>=0;s--){
					equal = true;
					prevVariable = dataSet.getVariableByName(sortKeys[s]);
					int cmp = HSUtils.compareValue(prevVariable.values.get(dataSet.sortIndex.get(ctr)), prevVariable.values.get(dataSet.sortIndex.get(ctr+1)), prevVariable.dataType);
					if(cmp != 0) {
						equal = false;
						break;
					}
				}

				//If its a last record
				if(ctr+2 == dataSet.getObservationCount()) {
					if(equal == true) {
						start = end;
						duplicate += 1;
						end = start + duplicate + 1;
						//System.out.println(start + " " + end);
						groupMap = new HashMap<String, Integer>();
						groupMap.put("start", start);
						groupMap.put("end", end);
						groupList.add(groupMap);
						if(n > 0 && n < sortKeys.length)
							sortVariable(dataSet, dataSet.getVariableByName(sortKeys[n]), start, end, a_keys[n].endsWith(":desc"));
					}else if(equal == false) {
						start = end;
						end = start + duplicate + 1;
						//System.out.println(start + " " + end);
						groupMap = new HashMap<String, Integer>();
						groupMap.put("start", start);
						groupMap.put("end", end);
						groupList.add(groupMap);
						if(n > 0 && n < sortKeys.length)
							sortVariable(dataSet, dataSet.getVariableByName(sortKeys[n]), start, end, a_keys[n].endsWith(":desc"));
						start = end;
						end = getObservationCount();
						//System.out.println(start + " " + end);
						groupMap = new HashMap<String, Integer>();
						groupMap.put("start", start);
						groupMap.put("end", end);
						groupList.add(groupMap);
						if(n > 0 && n < sortKeys.length)
							sortVariable(dataSet, dataSet.getVariableByName(sortKeys[n]), start, end, a_keys[n].endsWith(":desc"));
					}	
				}else{
					if(equal == true) {
							duplicate += 1;
					}else if(equal == false){
						start = end;
						end = start + duplicate + 1;
						//System.out.println(start + " " + end);
						groupMap = new HashMap<String, Integer>();
						groupMap.put("start", start);
						groupMap.put("end", end);
						groupList.add(groupMap);
						if(n > 0 && n < sortKeys.length)
							sortVariable(dataSet, dataSet.getVariableByName(sortKeys[n]), start, end, a_keys[n].endsWith(":desc"));
						duplicate = 0;
					}
				}
				ctr += 1;
			}//end while
		}//end for n
		
		//System.out.println("----------");
		this.flVariables.clear();
		Variable variable = null;
		DataSet.FLVariable flVariable = null;
		HSList<Boolean> values = null;
		for(int i=0;i<sortKeys.length;i++){
			variable = dataSet.getVariableByName(sortKeys[i]);
			if(variable.storageType == StorageType.DISK)
				values = new DList<Boolean>();
			else
				values = new MList<Boolean>();
			flVariable = new DataSet.FLVariable("first." + variable.name.toLowerCase(), values);
			flVariable.fillNull(dataSet.getObservationCount());
			dataSet.flVariables.add(flVariable);
			if(variable.storageType == StorageType.DISK)
				values = new DList<Boolean>();
			else
				values = new MList<Boolean>();
			flVariable = new DataSet.FLVariable("last." + variable.name.toLowerCase(), values);
			flVariable.fillNull(dataSet.getObservationCount());
			dataSet.flVariables.add(flVariable);
		}
		FLVariable fCol=null, lCol = null;
		int st, en;
		int n = 1;
		for(ArrayList<HashMap<String, Integer>> gList:groupLists){
			fCol = dataSet.getFirstFLVariableByName(sortKeys[n-1]);
			lCol = dataSet.getLastFLVariableByName(sortKeys[n-1]);
			fCol.fillFalse();
			lCol.fillFalse();
			for(HashMap<String, Integer> hm: gList) { 
			
				st = hm.get("start");
				fCol.values.set(sortIndex.get(st), true);
				
				en = hm.get("end");
				lCol.values.set(sortIndex.get(en-1), true);	
				
				
			}
			//System.out.println("--------next group-------");
			n += 1;
		}
		
		timer.stop();
		
		logMessage = String.format("sort(\"%s\", %s) took: %s sec(s)", keys, out, timer.timeElapsed());
		HS.writeLog(LogMessageType.NOTE, logMessage);
		
		return dataSet;
	}//end of sort() method

	private void sortVariable(DataSet dataSet, Variable variable, int start, int end, boolean desc) {

		Object sFirst, sSecond;
		int cmp;
		int k;
		for(int i=start;i<end;i++) {
			for(int j=i+1;j<end;j++) {
				sFirst = variable.values.get(dataSet.sortIndex.get(i));
				sSecond = variable.values.get(dataSet.sortIndex.get(j));
				cmp = HSUtils.compareValue(sFirst, sSecond, variable.dataType);
				if(desc == true){
					if(cmp < 0) {
						k = dataSet.sortIndex.get(i);
						dataSet.sortIndex.set(i, dataSet.sortIndex.get(j));
						dataSet.sortIndex.set(j, k);
					}
				}else{
					if(cmp > 0) {
						k = dataSet.sortIndex.get(i);
						dataSet.sortIndex.set(i, dataSet.sortIndex.get(j));
						dataSet.sortIndex.set(j, k);
					}
				}
			}//end for j
		}//end for i

	}//end of sortVariable()
	
	
	
	@Override
	public IDataSet dropVariable(String variableName) throws IllegalArgumentException {
		
		Timer timer = new Timer();
		timer.start();
		
		throwExceptionIfVariableNameEmpty(variableName);
		
		DataSet dataSet = this;
		
		
		variableName = variableName.trim();
		String variableNames[] = variableName.split(" ");

		ArrayList<Variable> cols = new ArrayList<Variable>();
		for(int i=0;i<variableNames.length;i++) {
			throwExceptionIfVariableNameInvalid(variableNames[i]);
			throwExceptionIfVariableNotPresent(variableNames[i]);
			Variable col = dataSet.getVariableByName(variableNames[i].trim());
			cols.add(col);
		}
		
		for(int i=0;i<cols.size();i++){
			dataSet.variables.remove(cols.get(i));
			if(Objects.nonNull(dataSet.getFirstFLVariableByName(cols.get(i).name)))
				dataSet.flVariables.remove(dataSet.getFirstFLVariableByName(cols.get(i).name));
			if(Objects.nonNull(dataSet.getLastFLVariableByName(cols.get(i).name)))
				dataSet.flVariables.remove(dataSet.getLastFLVariableByName(cols.get(i).name));
		}
		
		timer.stop();
		//logMessage = String.format("dropVariable(\"%s\", %s) took %s sec(s)", variableName, out, timer.timeElapsed());
		logMessage = String.format("dropVariable(\"%s\", %s) took %s sec(s)", variableName, timer.timeElapsed());
		HS.writeLog(LogMessageType.NOTE, logMessage);
		
		return dataSet;
		
	}//end of dropVariable() method
	

	
	@Override
	public IDataSet keepVariable(String variableName) throws IllegalArgumentException {
		
		Timer timer = new Timer();
		timer.start();
		
		DataSet dataSet = this;

		if(Objects.isNull(variableName)) {
			logMessage = String.format("[%s] keepVariable(%s, %s) execution started", dataSet.getName(), variableName);
		}else{
			logMessage = String.format("[%s] keepVariable(\"%s\", %s) execution started", dataSet.getName(), variableName);
		}
		HS.writeLog(LogMessageType.INFO, logMessage);
		
		ArrayList<Variable> cols = null;
		try {
			dataSet.throwExceptionIfVariableNameNull(variableName);
			dataSet.throwExceptionIfVariableNameEmpty(variableName);
			variableName = variableName.trim();
			String variableNames[] = variableName.split(" ");

			cols = new ArrayList<Variable>();
			for(int i=0;i<variableNames.length;i++) {
				dataSet.throwExceptionIfVariableNameInvalid(variableNames[i]);
				dataSet.throwExceptionIfVariableNotPresent(variableNames[i]);
				Variable col = dataSet.getVariableByName(variableNames[i].trim());
				cols.add(col);
			}
		}catch(IllegalArgumentException ie) {
			if(Objects.isNull(variableName))
				logMessage = String.format("[%s] keepVariable(%s, %s) execution failed", dataSet.getName(), variableName) + System.lineSeparator();
			else
				logMessage = String.format("[%s] keepVariable(\"%s\", %s) execution failed", dataSet.getName(), variableName) + System.lineSeparator();
			
			HS.writeLog(LogMessageType.INFO, logMessage);
			return null;
		}
		
		boolean match = false;
		ArrayList<Variable> variablesToDelete = new ArrayList<Variable>();
		for(int i=0;i<dataSet.variables.size();i++){
			match = false;
			for(int j=0;j<cols.size();j++) {
				if(dataSet.variables.get(i) == cols.get(j)) {
					match = true;
				}
			}
			if(!match) variablesToDelete.add(dataSet.variables.get(i));
		}
		
		int beforeVariableCount = dataSet.getVariableCount();
		
		logMessage = String.format("[%s] has %s variable(s)", dataSet.getName(), beforeVariableCount);
		HS.writeLog(LogMessageType.INFO, logMessage);
		
		for(int i=0;i<variablesToDelete.size();i++){
			dataSet.variables.remove(variablesToDelete.get(i));
			if(Objects.nonNull(dataSet.getFirstFLVariableByName(variablesToDelete.get(i).name)))
				flVariables.remove(dataSet.getFirstFLVariableByName(variablesToDelete.get(i).name));
			if(Objects.nonNull(dataSet.getLastFLVariableByName(variablesToDelete.get(i).name)))
				flVariables.remove(dataSet.getLastFLVariableByName(variablesToDelete.get(i).name));
		}
		int afterVariableCount = dataSet.getVariableCount();
		

		
		timer.stop();
		
		logMessage = String.format("[%s] %s variable(s) deleted", dataSet.getName(), (beforeVariableCount-afterVariableCount));
		HS.writeLog(LogMessageType.INFO, logMessage);
		
		logMessage = String.format("[%s] has now %s variable(s)", dataSet.getName(), afterVariableCount);
		HS.writeLog(LogMessageType.INFO, logMessage);
		
		logMessage = String.format("[%s] keepVariable(\"%s\", %s) took %s sec(s)", dataSet.getName(), variableName, timer.timeElapsed()) + System.lineSeparator();
		HS.writeLog(LogMessageType.INFO, logMessage);
		
		return dataSet;

	}//end of keepVariable() method
	
	@Override
	public IDataSet clone() {
		return this.getCopy();
	}
	
	private DataSet getCopy()  {
		
		DataSet dataSet = null;
		
		ArrayList<Variable> newVariables = new ArrayList<Variable>();
		
		for(Variable variable: variables)
			newVariables.add(variable.getCopy());
		
		ArrayList<FLVariable> newFLVariables = new ArrayList<FLVariable>();
		for(FLVariable flVariable:flVariables)
			newFLVariables.add(flVariable.getCopy());
		
		
		dataSet = new DataSet(newVariables);
		dataSet.flVariables = newFLVariables;
		
		HSList<Integer> newSortIndex;
		newSortIndex = new MList<Integer>();
		
		for(int i=0;i<sortIndex.size();i++) {
			newSortIndex.add(sortIndex.get(i));
		}
		
		dataSet.label = label;
		dataSet.sortIndex = newSortIndex;
		dataSet.setName(getName());
	
		
		return dataSet;
	}//end of getCopy() method


	
	@Override
	public IDataSet renameVariable(String variableName) {
		
		Timer timer = new Timer();
		timer.start();
		
		try {
			throwExceptionIfVariableNameEmpty(variableName);
		}catch(IllegalArgumentException ie){
			logMessage = String.format("");
			HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}
		
		
		variableName = variableName.trim();
		variableName = variableName.replaceAll(" +", " ");
		variableName = variableName.replaceAll(" =", "=");
		variableName = variableName.replaceAll("= ", "=");

		String variableNames[] = variableName.split(" ");

		ArrayList<String> from = new ArrayList<String>();
		ArrayList<String> to = new ArrayList<String>();
		for(int i=0;i<variableNames.length;i++) {
			from.add(variableNames[i].split("=")[0]);
			to.add(variableNames[i].split("=")[1]);
		}

		try {
			for(int i=0;i<from.size();i++){
				throwExceptionIfVariableNotPresent(from.get(i));
			}
		}catch(IllegalArgumentException ie){
			logMessage = String.format("");
			HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}
		
		try{
			for(int i=0;i<to.size();i++){
				throwExceptionIfVariableAlreadyPresent(to.get(i));
				throwExceptionIfVariableNameInvalid(to.get(i));
			}
		}catch(IllegalArgumentException ie){
			logMessage = String.format("");
			HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}
		
		DataSet dataSet = this;
		


		FLVariable fCol, lCol;
		Variable variable;
		for(int i=0;i<from.size();i++){
			variable = dataSet.getVariableByName(from.get(i));
			variable.name = to.get(i);
			fCol = dataSet.getFirstFLVariableByName("first." + from.get(i));
			if(Objects.nonNull(fCol))
				fCol.name = "first." + to.get(i).toLowerCase();
			lCol = dataSet.getFirstFLVariableByName("last." + from.get(i));
			if(Objects.nonNull(lCol))
				lCol.name = "first." + to.get(i).toLowerCase();
		}
		
		timer.stop();
		//logMessage = String.format("renameVariable(\"%s\", %s) took %s sec(s)", variableName, timer.timeElapsed());
		HS.writeLog(LogMessageType.NOTE, logMessage);

		return dataSet;
		
	}//end of renameVariable() method
	

	public static void writeDataSet(IDataSet dataSet, String filePath) throws FileNotFoundException, IOException {
		writeDataSet(dataSet, filePath, false);
	}
	
	public static void writeDataSet(IDataSet dataSet, String filePath, boolean compress) throws FileNotFoundException, IOException {
		
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		GZIPOutputStream gos = null;
		

		fos = new FileOutputStream(new File(filePath));
			
		if(compress == true) {
			gos = new GZIPOutputStream(fos);
			oos = new ObjectOutputStream(gos);
		}else{
			oos = new ObjectOutputStream(fos);
		}
			
		// Write datatable to file
		oos.writeObject(dataSet);

		if(compress == true){
			oos.close();
			gos.close();
		}else{
			oos.close();
		}
			
		fos.close();
	}
	
	@Override
	public IDataSet where(Function<Observation, Boolean> function) {
		return where((observation, store) -> {
			return new WhereData(function.apply(observation), store);
		});
	}
	
	/*
	@Override
	public void where(BiFunction<Observation, Store, WhereData> bifunction) {
		return where(bifunction, HS.defaultOut);
	}*/
	
	@Override
	public IDataSet where(BiFunction<Observation, Store, WhereData> bifunction) {
		
		Timer timer = new Timer();
		timer.start();
		
		DataSet dataSet = this;
		
		//if(out)
			//dataSet = this.getCopy();

		HSList<Integer> sIndex = new MList<Integer>();

		
		Observation observation = null;
		boolean keep = false;
		
		ArrayList<Integer> observationIdToDelete = new ArrayList<Integer>();
		int ctr = 0;
		Store store = new Store();
		WhereData whereData = null;
		Object values[] = null; 
		DataType dataTypes[] = null; 
		Boolean firstValues[] = null; 
		Boolean lastValues[] = null; 
		String names[] = null;
		Boolean fVal, lVal;
		Variable variable;
		
		int variableSize = dataSet.variables.size();
		for(int i=0;i<dataSet.getObservationCount();i++) {
			values = new Object[variableSize];
			dataTypes = new DataType[variableSize];
			firstValues = new Boolean[variableSize];
			lastValues = new Boolean[variableSize];
			names = new String[variableSize];
			fVal = null;
			lVal = null;
			for(int j=0;j<dataSet.getVariableCount();j++) {
				variable = dataSet.variables.get(j);
				values[j] = variable.values.get(dataSet.sortIndex.get(i));
				if(Objects.nonNull(dataSet.getFirstFLVariableByName(variable.name)))
					fVal = dataSet.getFirstFLVariableByName(variable.name).values.get(dataSet.sortIndex.get(i));
				firstValues[j] = fVal;
				
				if(Objects.nonNull(dataSet.getLastFLVariableByName(variable.name)))
					lVal = dataSet.getLastFLVariableByName(variable.name).values.get(dataSet.sortIndex.get(i));
				lastValues[j] = lVal;
				dataTypes[j] = variable.dataType;
				names[j] = variable.name;
			}//end of j
			observation = new Observation(i, names, dataTypes, values, firstValues, lastValues);
			whereData = bifunction.apply(observation, store);
			store = whereData.getStore();
			if(whereData.getValue() == false){
				observationIdToDelete.add(dataSet.getSortIndexValueAt(i));
			}else{
				sIndex.add(ctr);
				ctr += 1;
			}
			
		}//end of i
		
		Collections.sort(observationIdToDelete);
		for(int i=observationIdToDelete.size()-1;i>=0;i--){
			for(int j=0;j<dataSet.getVariableCount();j++){
				dataSet.variables.get(j).values.remove((int)observationIdToDelete.get(i));
			}
		}
		
		dataSet.setSortIndex(sIndex);
		
		timer.stop();
		//logMessage = String.format("where(%s, %s) took %s sec(s)", "whereFunction", out, timer.timeElapsed());
		//HS.writeLog(LogMessageType.NOTE, logMessage);
		
		return dataSet;
		
	}//end of where() method
	

	@Override
	public IDataSet addCalculatedVariable(String newVariableName, DataType dataType, Function<Observation, Object> function) throws IllegalArgumentException {
		return addCalculatedVariable(newVariableName, dataType, (observation, store) -> {
			return new VariableData(function.apply(observation), store);
		});
	}
	
	@Override
	public IDataSet addCalculatedVariable(String newVariableName, DataType dataType, BiFunction<Observation, Store, VariableData> bifunction) throws IllegalArgumentException {
		

		
		throwExceptionIfVariableNameEmpty(newVariableName);
		throwExceptionIfVariableNameInvalid(newVariableName);
		throwExceptionIfDataTypeInvalid(dataType);
		throwExceptionIfVariableAlreadyPresent(newVariableName);

		newVariableName = newVariableName.trim();
		
		DataSet dataSet = this;
		
		
		VariableFactory variableFactory = null;
		if(dataSet.storageType == StorageType.DISK)
			variableFactory = new VariableFactory(StorageType.DISK);
		else
			variableFactory = new VariableFactory(StorageType.MEMORY);
		
		Object values[];
		DataType dataTypes[];
		Boolean firstValues[];
		Boolean lastValues[];
		String names[];
		Observation observation = null;
		Variable variable;
		Object objVal;
		HSList<Object> newVariableValues = null;
		Variable newVariable = null;
		Store store = new Store();
		VariableData variableData = null;
		//ObservationData observationData = null;
		Boolean fVal, lVal;
		newVariableValues = HSUtils.createNullValues(dataType, getObservationCount());
		int variableSize = dataSet.variables.size();
		for(int i=0;i<dataSet.getObservationCount();i++){
			values = new Object[variableSize];
			dataTypes = new DataType[variableSize];
			firstValues = new Boolean[variableSize];
			lastValues = new Boolean[variableSize];
			names = new String[variableSize];
			fVal = null;
			lVal = null;
			for(int j=0;j<dataSet.getVariableCount();j++) {
				variable = dataSet.variables.get(j);
				values[j] = variable.values.get(sortIndex.get(i));
				dataTypes[j] = variable.dataType;
				if(Objects.nonNull(dataSet.getFirstFLVariableByName(variable.name)))
					fVal = dataSet.getFirstFLVariableByName(variable.name).values.get(dataSet.sortIndex.get(i));
				firstValues[j] = fVal;
				if(Objects.nonNull(dataSet.getLastFLVariableByName(variable.name)))
				    lVal = dataSet.getLastFLVariableByName(variable.name).values.get(dataSet.sortIndex.get(i));
				lastValues[j] = lVal;
				names[j] = variable.name;
				observation = new Observation(i, names, dataTypes, values, firstValues, lastValues);
				//observationData = new ObservationData(observation, retain);
			}//end of for j
			variableData = bifunction.apply(observation, store);
			store = variableData.getStore();
			newVariableValues.set(dataSet.sortIndex.get(i), variableData.getValue());
		}//end of for i
		newVariable = variableFactory.createVariable(newVariableName, dataType, newVariableValues);
		dataSet.variables.add(newVariable);
		
		
		
		return dataSet;
		
	}//end of addCalculatedVariable()
	
	public IDataSet updateVariable(String variableName, Function<Observation, Object> function) throws IllegalArgumentException {
		return updateVariable(variableName, (observation, store) -> {
			return new VariableData(function.apply(observation), store);
		});
	}
	
	public IDataSet updateVariable(String variableName, BiFunction<Observation, Store, VariableData> bifunction) throws IllegalArgumentException {
		

		
		throwExceptionIfVariableNameEmpty(variableName);
		throwExceptionIfVariableNameInvalid(variableName);
		throwExceptionIfVariableNotPresent(variableName);
		
		variableName = variableName.trim();
		
		DataSet dataSet = this;
		

		
		Variable upVariable = dataSet.getVariableByName(variableName);
		Object values[];
		DataType dataTypes[];
		Boolean firstValues[];
		Boolean lastValues[];
		String names[];
		Observation observation = null;
		Variable variable;
		//Object objVal;

		Store store = new Store();
		VariableData variableData = null;
		//ObservationData observationData = null;
		Boolean fVal, lVal;
		int variableSize = dataSet.variables.size();
		for(int i=0;i<dataSet.getObservationCount();i++){
			values = new Object[variableSize];
			dataTypes = new DataType[variableSize];
			firstValues = new Boolean[variableSize];
			lastValues = new Boolean[variableSize];
			names = new String[variableSize];
			fVal = null;
			lVal = null;
			for(int j=0;j<dataSet.getVariableCount();j++) {
				variable = dataSet.variables.get(j);
				values[j] = variable.values.get(sortIndex.get(i));
				dataTypes[j] = variable.dataType;
				if(Objects.nonNull(dataSet.getFirstFLVariableByName(variable.name)))
					fVal = dataSet.getFirstFLVariableByName(variable.name).values.get(dataSet.sortIndex.get(i));
				firstValues[j] = fVal;
				if(Objects.nonNull(dataSet.getLastFLVariableByName(variable.name)))
				    lVal = dataSet.getLastFLVariableByName(variable.name).values.get(dataSet.sortIndex.get(i));
				lastValues[j] = lVal;
				names[j] = variable.name;
				observation = new Observation(i, names, dataTypes, values, firstValues, lastValues);
				//System.out.println("fff" + this);
				//observationData = new ObservationData(observation, retain);
			}//end of for j
			
			variableData = bifunction.apply(observation, store);
			//System.out.println(ccData.getValue() + " " + ccData.getData());
			store = variableData.getStore();
			upVariable.values.set(dataSet.sortIndex.get(i), variableData.getValue());
		
		}//end of for i
		
		
		
		return dataSet;
	}
	

	
	public IDataSet copyVariable(String variableName, String newVariableName) throws IllegalArgumentException {

		
		try {
		throwExceptionIfVariableNameNull(variableName);
		throwExceptionIfVariableNameNull(newVariableName);
		throwExceptionIfVariableNameEmpty(variableName);
		throwExceptionIfVariableNameEmpty(newVariableName);
		throwExceptionIfVariableNameInvalid(variableName);
		throwExceptionIfVariableNameInvalid(newVariableName);
		throwExceptionIfVariableNotPresent(variableName);
		throwExceptionIfVariableAlreadyPresent(newVariableName);
		}catch(IllegalArgumentException ie) {
			HS.writeStackTraceToLog(ie);
			return null;
		}
		
		DataSet dataSet = this;
		


		Variable variable = getVariableByName(variableName).getCopy();
		variable.name = newVariableName;
		dataSet.variables.add(variable);
		

		
		return dataSet;
	}
	
	
	public IDataSet addVariable(Variable variable) throws IllegalArgumentException {
		
		
		throwExceptionIfVariableAlreadyPresent(variable.name);
		
		DataSet dataSet = this;
		


		if(variable.values.size() < dataSet.getObservationCount()-1) {
			int maxObservation = dataSet.getObservationCount();
			while(variable.values.size() != maxObservation)
				variable.values.add(null);	

		}else if(variable.values.size() > dataSet.getObservationCount()-1){
			int maxObservation = dataSet.getObservationCount();
			while(variable.values.size() != maxObservation)
				variable.values.remove((int)(variable.values.size()-1));	
		}
		
		Object value = null;
		for(int i=0;i<variable.values.size();i++){
			value = variable.values.get(dataSet.getSortIndexValueAt(i));
			variable.values.set(dataSet.getSortIndexValueAt(i), variable.values.get(i));
			variable.values.set(i, value);
		}
		
		dataSet.variables.add(variable);
		
		
		
		return dataSet;
	}
	
	@Override
	public Observation getObservation(int index) throws IndexOutOfBoundsException {
		
		String msg = null;
		
		if(index < 0 || index >= getObservationCount()){
			msg = "Index: " + index + ", Size: " + getObservationCount();
			throw new IndexOutOfBoundsException(msg);
		}
		
		int variableSize = variables.size();
		Object values[] = new Object[variableSize];
		DataType dataTypes[] = new DataType[variableSize];
		Boolean firstValues[] = new Boolean[variableSize];
		Boolean lastValues[] = new Boolean[variableSize];
		String names[] = new String[variableSize];
		Boolean fVal, lVal;
		for(int i = 0;i<getVariableCount();i++){
			fVal = null;
			lVal = null;
			Variable variable = variables.get(i);
			values[i] = variable.values.get(sortIndex.get(index));
			//System.out.println(val);
			if(Objects.nonNull(getFirstFLVariableByName(variable.name)))
			    fVal = getFirstFLVariableByName(variable.name).values.get(sortIndex.get(index));
			firstValues[i] = fVal;
			if(Objects.nonNull(getLastFLVariableByName(variable.name)))
				lVal = getLastFLVariableByName(variable.name).values.get(sortIndex.get(index));
			lastValues[i] = lVal;
			dataTypes[i] = variable.dataType;
			names[i] = variable.name;
		}
		
		return new Observation(index, names, dataTypes, values, firstValues, lastValues);
	}
	
	public IDataSet join(IDataSet rightDataSet, String keys, JoinType joinType) {
		return join(rightDataSet, keys, joinType, HS.defaultOut);
	}
	public IDataSet join(IDataSet rightDataSet, String keys, JoinType joinType, boolean out) {
		
		Timer timer = new Timer();
		timer.start();
		
		DataSet emptyDataSet = new DataSet();
		
		DataSet leftDataSet = this;
		
		if(out)
			leftDataSet = this.getCopy();
		
		//String msg = null;
		VariableFactory variableFactory = new VariableFactory(StorageType.MEMORY);
		HSList<Integer> sIndex = new MList<Integer>();

		
		
		
		if(joinType == JoinType.LEFT) {
			if(leftDataSet.getStorageType() == StorageType.DISK) {
				variableFactory = new VariableFactory(StorageType.DISK);
				
			}
		}else if(joinType == JoinType.RIGHT){
			if(rightDataSet.getStorageType() == StorageType.DISK) {
				variableFactory = new VariableFactory(StorageType.DISK);
				
			}
		}else if(joinType == JoinType.INNER || joinType == JoinType.FULL){
			if(leftDataSet.getStorageType() == StorageType.DISK || rightDataSet.getStorageType() == StorageType.DISK) {
				variableFactory = new VariableFactory(StorageType.DISK);
				
			}
		}
		
		
		try {
			throwExceptionIfKeyEmpty(keys);
		}catch(IllegalArgumentException ie) {
			//logMessage = String.format("[%s] join(%s, %s, %s, %s) execution failed", leftDataSet.getName(), rightDataSet.getName(), keys, joinType, out);
			HS.writeStackTraceToLog(ie);
			return emptyDataSet;
		}
		
		keys = keys.trim().replaceAll(" +", " ");
		String a_keys[] = keys.split(" ");
		
		for(String key:a_keys){
			key = key.trim();
			try {
				throwExceptionIfKeyInvalid(key);
			}catch(IllegalArgumentException ie) {
				//logMessage = String.format("[%s] join(%s, %s, %s, %s) execution failed", leftDataSet.getName(), rightDataSet.getName(), keys, joinType, out);
				HS.writeStackTraceToLog(ie);
				return emptyDataSet;
			}
			
			try {
				throwExceptionIfKeyNotPresent(key);
				((DataSet)rightDataSet).throwExceptionIfKeyNotPresent(key);
			}catch(IllegalArgumentException ie) {
				//logMessage = String.format("[%s] join(%s, %s, %s, %s) execution failed", leftDataSet.getName(), rightDataSet.getName(), keys, joinType, out);
				HS.writeStackTraceToLog(ie);
				return emptyDataSet;
				//logMessage = String.format("[%s] - key variable does not exist in one of the datatable", key);
				//HS.writeLog(LogMessageType.ERROR, logMessage);
				//return emptyDataSet;
			}
		}
		
		

		
		for(String leftVariableName:leftDataSet.getVariableNames()){
			for(String rightVariableName:rightDataSet.getVariableNames()){
				boolean keyVariable = false;
				for(String key:a_keys){
					if(key.equalsIgnoreCase(leftVariableName)) {
						keyVariable = true;
						break;
					}
				}
				if(keyVariable == false){
					if(leftVariableName.equalsIgnoreCase(rightVariableName)){
						//logMessage = String.format("[%s] join(%s, %s, %s, %s) execution failed", leftDataSet.getName(), rightDataSet.getName(), keys, joinType, out);
						//HS.writeLog(LogMessageType.ERROR, logMessage + "key variable not present");
						return emptyDataSet;
					}
				}else{
					break;
				}
			}
		}
		
		ArrayList<Variable> newVariables = new ArrayList<Variable>();
		Variable newVariable = null;
		String variableName = null;
		DataType dataType;
		for(Variable variable: leftDataSet.variables){
			variableName = variable.name;
			dataType = variable.dataType;
			newVariable = variableFactory.createVariableWithNullValues(variableName, dataType, 0);
			newVariable.formatFunction = variable.formatFunction;
			newVariable.format = variable.format;
			newVariables.add(newVariable);
		}//end of foreach loop
		
		
		
		boolean match;
		for(Variable variable: rightDataSet.getVariables()){
			variableName = variable.name;
			dataType = variable.dataType;
			match = false;
			for(int i=0;i<a_keys.length;i++){
				if(a_keys[i].equalsIgnoreCase(variableName)) {
					match = true;
					break;
				}
			}
			if(match == false){
				newVariable = variableFactory.createVariableWithNullValues(variableName, dataType, 0);
				newVariable.formatFunction = variable.formatFunction;
				newVariable.format = variable.format;
				newVariables.add(newVariable);
			}//end of if(match == false)
		}//end of foreach loop
	
	
		
		int ctr = 0;
		boolean keyMatch = false;
		boolean isKeyVariable = false;
		int cmp = 0;
		Variable leftDTVariable = null;
		Variable rightDTVariable = null;
		boolean keyMatchInPass = false;
		ArrayList<Integer> cmps = null;
		
		for(int i=0;i<leftDataSet.getObservationCount();i++){
			for(int j=0;j<rightDataSet.getObservationCount();j++){
				keyMatch = true;
				cmps = new ArrayList<Integer>();
				for(int k=0;k<a_keys.length;k++){
					leftDTVariable = leftDataSet.getVariableByName(a_keys[k]);
					rightDTVariable = rightDataSet.getVariableByName(a_keys[k]);
					cmp = HSUtils.compareValue(leftDTVariable.values.get(i), rightDTVariable.values.get(j), leftDTVariable.dataType);
					cmps.add(cmp);
				}
				for(int g=0;g<cmps.size();g++){
					if(cmps.get(g) != 0){
						keyMatch = false;
						break;
					}
				}

				if(keyMatch == true){
					//copy values from key variable
					for(Variable nCol: newVariables){
						for(int k=0;k<a_keys.length;k++) {
							if(nCol.name.equalsIgnoreCase(a_keys[k])){
								leftDTVariable = leftDataSet.getVariableByName(a_keys[k]);
								nCol.values.add(leftDTVariable.values.get(i));
							}
						}
					}
					
					//copy values from none key variable
					for(Variable nCol: newVariables){
						isKeyVariable = false;
						for(int k=0;k<a_keys.length;k++){
							if(nCol.name.equalsIgnoreCase(a_keys[k])) {
								isKeyVariable = true;
								break;
							}
						}
						if(isKeyVariable == false){
							if(leftDataSet.hasVariable(nCol.name))
								nCol.values.add(leftDataSet.getVariableByName(nCol.name).values.get(i));
							if(rightDataSet.hasVariable(nCol.name))
								nCol.values.add(rightDataSet.getVariableByName(nCol.name).values.get(j));
						}
					}
					sIndex.add(ctr);
					ctr += 1;
				}//end of if(keyMatch == true)
				
			}//end of for j
		}//end of for i
		
		
		
		if(joinType == JoinType.RIGHT || joinType == JoinType.FULL){
			for(int i=0;i<rightDataSet.getObservationCount();i++){
				keyMatchInPass = false;
				for(int j=0;j<leftDataSet.getObservationCount();j++){
					keyMatch = true;
					cmps = new ArrayList<Integer>();
					for(int k=0;k<a_keys.length;k++){
						leftDTVariable = leftDataSet.getVariableByName(a_keys[k]);
						rightDTVariable = rightDataSet.getVariableByName(a_keys[k]);
						cmp = HSUtils.compareValue(leftDTVariable.values.get(j), rightDTVariable.values.get(i), leftDTVariable.dataType);
						cmps.add(cmp);
					}
					for(int g=0;g<cmps.size();g++){
						if(cmps.get(g) != 0){
							keyMatch = false;
							break;
						}
					}
					if(keyMatch == true)
						keyMatchInPass = true;
				}//end of j
				if(keyMatchInPass == false) { //&& j == leftDataSet.getObservationCount()-1){
					//copy values from key variable
					for(Variable nCol: newVariables){
						for(int k=0;k<a_keys.length;k++) {
							if(nCol.name.equalsIgnoreCase(a_keys[k])){
								rightDTVariable = rightDataSet.getVariableByName(a_keys[k]);
								nCol.values.add(rightDTVariable.values.get(i));
							}
						}
					}
					//copy values from none key variable
					for(Variable nCol: newVariables){
						isKeyVariable = false;
						for(int k=0;k<a_keys.length;k++){
							if(nCol.name.equalsIgnoreCase(a_keys[k])) {
								isKeyVariable = true;
								break;
							}
						}
						if(isKeyVariable == false){
							if(leftDataSet.hasVariable(nCol.name))
								nCol.values.add(null);
							if(rightDataSet.hasVariable(nCol.name))
								nCol.values.add(rightDataSet.getVariableByName(nCol.name).values.get(i));
						}
					}
					sIndex.add(ctr);
					ctr += 1;
				}//end of if(keyMatchInPass == false)
			}//end of i
		}//end of if(joinType == JoinType.RIGHT || joinType == JoinType.FULL)
			
		if(joinType == JoinType.LEFT || joinType == JoinType.FULL){
			for(int i=0;i<leftDataSet.getObservationCount();i++){
				keyMatchInPass = false;
				for(int j=0;j<rightDataSet.getObservationCount();j++){
					keyMatch = true;
					cmps = new ArrayList<Integer>();
					for(int k=0;k<a_keys.length;k++){
						leftDTVariable = leftDataSet.getVariableByName(a_keys[k]);
						rightDTVariable = rightDataSet.getVariableByName(a_keys[k]);
						cmp = HSUtils.compareValue(leftDTVariable.values.get(i), rightDTVariable.values.get(j), leftDTVariable.dataType);
						cmps.add(cmp);
					}
					
					for(int g=0;g<cmps.size();g++){
						if(cmps.get(g) != 0){
							keyMatch = false;
							break;
						}
					}
				
					if(keyMatch == true){
						keyMatchInPass = true;
					}
					
				}//end of j
				if(keyMatchInPass == false) {
					//copy values from key variable
					for(Variable nCol: newVariables){
						for(int k=0;k<a_keys.length;k++) {
							if(nCol.name.equalsIgnoreCase(a_keys[k])){
								leftDTVariable = leftDataSet.getVariableByName(a_keys[k]);
								nCol.values.add(leftDTVariable.values.get(i));
							}
						}
					}
					//copy values from none key variable
					for(Variable nCol: newVariables){
						isKeyVariable = false;
						for(int k=0;k<a_keys.length;k++){
							if(nCol.name.equalsIgnoreCase(a_keys[k])) {
								isKeyVariable = true;
								break;
							}
						}
						if(isKeyVariable == false){
							if(leftDataSet.hasVariable(nCol.name))
								nCol.values.add(leftDataSet.getVariableByName(nCol.name).values.get(i));
							if(rightDataSet.hasVariable(nCol.name))
								nCol.values.add(null);
						}
					}
					sIndex.add(ctr);
					ctr += 1;
				}//end of if(keyMatchInPass == false)
			}//end of i
		}//end of if(joinType == JoinType.LEFT || joinType == JoinType.FULL)
		
		leftDataSet.variables = newVariables;
		leftDataSet.sortIndex = sIndex;
		
		timer.stop();
		//logMessage = String.format("join() took %s sec(s)", timer.timeElapsed());
		//HS.writeLog(LogMessageType.NOTE, logMessage);
		
		return leftDataSet;
		
	}//end of join() method
	
	
	
	//interface method
	public boolean hasVariable(String variableName) {
		
		variableName = variableName.trim();
		boolean has = false;
		
		if(getVariableByName(variableName) != null)
			has = true;
		
		return has;
	}

	private HSList<Integer> getSortIndex() {
	
	return sortIndex;
	}
	
	private void setSortIndex(HSList<Integer> sortIndex) {
		this.sortIndex = sortIndex;
	}
	
	int getSortIndexValueAt(int index){
		return (int)getSortIndex().get(index);
	}
	
	
	public Object getVariableValueAt(String variableName, int index) {
		
		variableName = variableName.trim();
		String msg = null;
		
		try {
			throwExceptionIfVariableNotPresent(variableName);
		}catch(IllegalArgumentException ie) {
			//logMessage = String.format("[%s]- variable does not exist", variableName);
			//HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}
		
		try {
			throwExceptionIfIndexOutOfBounds(index, 0, getObservationCount()-1);
		}catch(IndexOutOfBoundsException ie) {
			//logMessage = String.format("[%s]- index out of bound", index);
			//HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}
		

		Variable variable = getVariableByName(variableName);
		//System.out.println(variable.classType);
		return variable.values.get(getSortIndexValueAt(index));
	}
	
	/*
	private void addVariableValue(String variableName, Object value) {
		
		variableName = variableName.trim();
		String msg = null;
		
		if(hasVariable(variableName) == false){
			msg = "[" + variableName + "]" + " - variable does not exist";
			throw new VariableNameException(msg);
		}
		
		DataSet.FLVariable fCol, lCol;
		
		Variable variable = getVariableByName(variableName);
		
		fCol = getFirstFLVariableByName(variableName);
		if(Objects.nonNull(fCol))
			fCol.values.add(null);
			
		lCol = getLastFLVariableByName(variableName);
		if(Objects.nonNull(lCol))
			lCol.values.add(null);
		
		variable.values.add(value);
		
	}
	*/
	
	@Override
	public IDataSet removeVariableValueAt(String variableName, int index) {
		return removeVariableValueAt(variableName, index, HS.defaultOut);
	}
	
	@Override
	public IDataSet removeVariableValueAt(String variableName, int index, boolean out) {
		
		if(Objects.nonNull(variableName))
			variableName = variableName.trim();

		try {
			throwExceptionIfVariableNameNull(variableName);
			throwExceptionIfVariableNameEmpty(variableName);
			throwExceptionIfVariableNameInvalid(variableName);
			throwExceptionIfVariableNotPresent(variableName);
		}catch(IllegalArgumentException ie) {
			//logMessage = String.format("error in setVariableFormat()");
			//HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}

		try{
			throwExceptionIfIndexOutOfBounds(index, 0, getObservationCount()-1);
		}catch(IndexOutOfBoundsException ie){
			//logMessage = String.format("");
			//HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}
		
		DataSet dataSet = null;
		
		if(out)
			dataSet = this.getCopy();
		else
			dataSet = this;
		
		
		Variable variable = dataSet.getVariableByName(variableName);
		variable.values.set(dataSet.getSortIndexValueAt(index), null);
		return dataSet;
	}
	
	@Override
	public IDataSet setVariableFormat(String variableName,Format format) {
		return setVariableFormat(variableName, format, HS.defaultOut);
	}

	@Override
	public IDataSet setVariableFormat(String variableName, Format format, boolean out) {
		
		if(Objects.nonNull(variableName))
			variableName = variableName.trim();

		try {
			throwExceptionIfVariableNameNull(variableName);
			throwExceptionIfVariableNameEmpty(variableName);
			throwExceptionIfVariableNameInvalid(variableName);
			throwExceptionIfVariableNotPresent(variableName);
		}catch(IllegalArgumentException ie) {
			//logMessage = String.format("error in setVariableFormat()");
			//HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}
		
		DataSet dataSet = null;
		
		if(out)
			dataSet = this.getCopy();
		else
			dataSet = this;
		
		Variable variable = dataSet.getVariableByName(variableName);
		variable.format = format;
		
		return dataSet;
	}
	
	@Override
	public IDataSet setVariableFormat(String variableName,Function<Object, String> formatFunction) {
		return setVariableFormat(variableName, formatFunction, HS.defaultOut);
	}

	@Override
	public IDataSet setVariableFormat(String variableName,Function<Object, String> formatFunction, boolean out) {
		
		if(Objects.nonNull(variableName))
			variableName = variableName.trim();

		try {
			throwExceptionIfVariableNameNull(variableName);
			throwExceptionIfVariableNameEmpty(variableName);
			throwExceptionIfVariableNameInvalid(variableName);
			throwExceptionIfVariableNotPresent(variableName);
		}catch(IllegalArgumentException ie) {
			//logMessage = String.format("error in setVariableFormat()");
			//HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}
		
		DataSet dataSet = null;
		
		if(out)
			dataSet = this.getCopy();
		else
			dataSet = this;
		
		Variable variable = dataSet.getVariableByName(variableName);
		variable.formatFunction = formatFunction;
		
		return dataSet;
	}
	
	@Override
	public IDataSet removeObservation(int index) {
		return removeObservation(index, HS.defaultOut);
	}
	
	@Override
	public IDataSet removeObservation(int index, boolean out) {
		

		try {
			throwExceptionIfIndexOutOfBounds(index, 0, getObservationCount()-1);
		}catch(IndexOutOfBoundsException ie) {
			//logMessage = String.format(ie.getMessage());
			//HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}

		DataSet dataSet = null;
		
		if(out)
			dataSet = this.getCopy();
		else
			dataSet = this;
		
		
		
		DataSet.FLVariable fCol, lCol;
		for(int i=0;i<dataSet.variables.size();i++) {
			dataSet.variables.get(i).values.remove((int)dataSet.sortIndex.get(index));
			fCol = dataSet.getFirstFLVariableByName(dataSet.variables.get(i).name);
			if(Objects.nonNull(fCol))
				fCol.values.remove((int)dataSet.sortIndex.get(index));
			lCol = dataSet.getLastFLVariableByName(dataSet.variables.get(i).name);
			if(Objects.nonNull(lCol))
				lCol.values.remove((int)dataSet.sortIndex.get(index));
		}
		
		int ctr = 0;
		HSList<Integer> sIndex = new MList<Integer>();
		
		for(int i=0;i<dataSet.getSortIndex().size();i++) {
			if(dataSet.getSortIndexValueAt(i) != dataSet.getSortIndexValueAt(index)){
				sIndex.add(ctr);
				ctr += 1;
			}
		}
		
		dataSet.setSortIndex(sIndex);
		
		return dataSet;
		
	}//end of removeObservation() method
	
	@Override
	public IDataSet addObservation(Object...values) {
		return addObservation(HS.getDefaultOut(), values);
	}
	
	@Override
	public IDataSet addObservation(boolean out, Object...values) {
		

		String msg = null;

		try {
			if(values.length == 0) { 
				msg = values + " - 0 values";
				throw new IllegalArgumentException(msg);
			}else if(values.length > getVariableCount()) {
				msg = values + " - too many values";
				throw new IllegalArgumentException(msg);
			}
		}catch(IllegalArgumentException ie) {
			//logMessage = String.format(ie.getMessage());
			//HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}
		
		DataSet dataSet = null;
		
		if(out)
			dataSet = this.getCopy();
		else
			dataSet = this;
		
		Variable variable;
		for(int i=0;i<dataSet.variables.size();i++) {
			variable = dataSet.variables.get(i);
			if(Objects.nonNull(values[i])) {
				if(variable.dataType == DataType.INTEGER)
					variable.values.add((int)values[i]);
				else if(variable.dataType == DataType.LONG)
					variable.values.add((long)values[i]);
				else if(variable.dataType == DataType.FLOAT)
					variable.values.add((float)values[i]);
				else if(variable.dataType == DataType.DOUBLE)
					variable.values.add((double)values[i]);
				else if(variable.dataType == DataType.BOOLEAN)
					variable.values.add((boolean)values[i]);
				else if(variable.dataType == DataType.CHARACTER)
					variable.values.add((char)values[i]);
				else if(variable.dataType == DataType.STRING)
					variable.values.add((String)values[i]);
				else if(variable.dataType == DataType.LOCAL_DATE)
					variable.values.add((LocalDate)values[i]);
				else if(variable.dataType == DataType.LOCAL_TIME)
					variable.values.add((LocalTime)values[i]);
				else if(variable.dataType == DataType.LOCAL_DATE_TIME)
					variable.values.add((LocalDateTime)values[i]);
				else{
					variable.values.add(values[i]);
				}
			}else{
				variable.values.add(null);
			}
			
		}
		
		dataSet.getSortIndex().add(getObservationCount());
		return dataSet;
	}
	
	@Override
	public IDataSet addObservation(Map<String, Object> observationMap) {
		return addObservation(observationMap, HS.defaultOut);
	}
	
	@Override
	public IDataSet addObservation(Map<String, Object> observationMap, boolean out) {
		
		String msg = null;

		try {
			if(observationMap.size() == 0) { 
				msg = observationMap + " - observation map has 0 values";
				throw new IllegalArgumentException(msg);
			}
			if(observationMap.size() > getVariableCount()) {
				msg = observationMap + " - observation map has too many values";
				throw new IllegalArgumentException(msg);
			}
		}catch(IllegalArgumentException ie) {
			//logMessage = String.format(ie.getMessage());
			//HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}
		
		Iterator<Map.Entry<String, Object>> it = observationMap.entrySet().iterator();
		Map.Entry<String, Object> entry = null;
		while (it.hasNext()) {
			entry = it.next();
			try {
				throwExceptionIfVariableNotPresent(entry.getKey().trim());
			}catch(IllegalArgumentException ie) {
				//logMessage = String.format("");
				//HS.writeLog(LogMessageType.ERROR, logMessage);
				return null;
			}
		}

		DataSet dataSet = null;
		
		if(out)
			dataSet = this.getCopy();
		else
			dataSet = this;
		
		for(Variable variable:dataSet.variables) {
			variable.values.add(null);
		}

		dataSet.getSortIndex().add(dataSet.getObservationCount());
		
		it = observationMap.entrySet().iterator();
		String variableName = null;
		Object variableValue = null;
		entry = null;
		while (it.hasNext()) {
			entry = it.next();
			variableName = entry.getKey();
			variableValue = entry.getValue();
			dataSet.getVariableByName(variableName).values.set(dataSet.getSortIndexValueAt(dataSet.getObservationCount()-1), variableValue);
		}
		
		return dataSet;
	}
	
	@Override
	public IDataSet removeAllObservations() {
		return removeAllObservations(true);
	}
	
	@Override
	public IDataSet removeAllObservations(boolean out) {
		
		DataSet dataSet = null;
		
		if(out)
			dataSet = this.getCopy();
		else
			dataSet = this;
		
		for(int i=0;i<dataSet.variables.size();i++) {
			dataSet.variables.get(i).values.clear();
		}
		
		dataSet.sortIndex.clear();
		
		return dataSet;
	}

	
	
	@Override
	public String getVariableFormattedValueAt(String variableName, int index) {
		
		if(Objects.nonNull(variableName))
			variableName = variableName.trim();

		try {
			throwExceptionIfVariableNameNull(variableName);
			throwExceptionIfVariableNameEmpty(variableName);
			throwExceptionIfVariableNameInvalid(variableName);
			throwExceptionIfVariableNotPresent(variableName);
		}catch(IllegalArgumentException ie) {
			logMessage = String.format("error in getVariableFormmattedValueAt()");
			HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}
		
		try {
			throwExceptionIfIndexOutOfBounds(index, 0, getObservationCount()-1);
		}catch(IndexOutOfBoundsException ie) {
			logMessage = String.format("");
			HS.writeLog(LogMessageType.ERROR, logMessage);
		}

		
		Variable variable = getVariableByName(variableName);
		
		Object value = getVariableValueAt(variableName, index);
		String v = null;

		if(Objects.nonNull(variable.formatFunction)) 
			v = variable.formatFunction.apply(value);
		else if(Objects.nonNull(variable.format)){
			if(variable.format.isDatePattern == true){
				v = ((LocalDate)value).format(variable.format.formatter);
			}
		}
		else {
			if(Objects.isNull(value))
				v = "";
			else if(value.getClass().getName().equalsIgnoreCase("java.lang.String"))
				v = (String)value;
			else
				v = value.toString();
		}
		
		return v;
	}
	
	public String getVariableName(Variable variable) {

		String variableName = "";
		
		if(Objects.nonNull(variable) && hasVariable(variable.name)){
			variableName = variable.name;
		}
		return variableName;
	}
	
	@Override
	public DataType getVariableDataType(String variableName) {
		
		if(Objects.nonNull(variableName))
			variableName = variableName.trim();

		try {
			throwExceptionIfVariableNameNull(variableName);
			throwExceptionIfVariableNameEmpty(variableName);
			throwExceptionIfVariableNameInvalid(variableName);
			throwExceptionIfVariableNotPresent(variableName);
		}catch(IllegalArgumentException ie) {
			logMessage = String.format("error in getVariableDataType()");
			HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}
		
		Variable variable = getVariableByName(variableName);
		return variable.dataType;
	}
	
	@Override
	public String getVariableLabel(String variableName) {
		
		if(Objects.nonNull(variableName))
			variableName = variableName.trim();

		try {
			throwExceptionIfVariableNameNull(variableName);
			throwExceptionIfVariableNameEmpty(variableName);
			throwExceptionIfVariableNameInvalid(variableName);
			throwExceptionIfVariableNotPresent(variableName);
		}catch(IllegalArgumentException ie) {
			logMessage = String.format("error in getVariableLabel()");
			HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}
		
		Variable variable = getVariableByName(variableName);
		return variable.label;
	}
	
	@Override
	public IDataSet setVariableLabel(String variableName, String label) {
		return setVariableLabel(variableName, label, HS.defaultOut);
	}
	
	@Override
	public IDataSet setVariableLabel(String variableName, String label, boolean out) {
		
		if(Objects.nonNull(variableName))
			variableName = variableName.trim();

		try {
			throwExceptionIfVariableNameNull(variableName);
			throwExceptionIfVariableNameEmpty(variableName);
			throwExceptionIfVariableNameInvalid(variableName);
			throwExceptionIfVariableNotPresent(variableName);
		}catch(IllegalArgumentException ie) {
			logMessage = String.format("error in setVariableLabel()");
			HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}
	
		if(Objects.isNull(label))
			label = "";

		DataSet dataSet = this;
		
		if(out) {
			dataSet = this.getCopy();
		}
		else
			dataSet = this;
		
		Variable variable = dataSet.getVariableByName(variableName);
		variable.label = label;
		
		return dataSet;
	}

	private IDataSet _aggregate(String variableName, String groupBy, String newVariableName, Aggregate aggregateFunction)  {

		String msg = null;

		if(aggregateFunction != Aggregate.COUNT) {
			variableName = variableName.trim();
			try {
				throwExceptionIfVariableNameEmpty(variableName);
			}catch(IllegalArgumentException ie) {
				logMessage = String.format("");
				HS.writeLog(LogMessageType.ERROR, logMessage);
				return null;
			}
			
			try {
				throwExceptionIfVariableNotPresent(variableName);
			}catch(Exception ie) {
				logMessage = String.format("[%s] - does not exist", variableName);
				HS.writeLog(LogMessageType.ERROR, logMessage);
				return null;
			}
		}

		if(aggregateFunction == Aggregate.SUM || aggregateFunction == Aggregate.AVG || aggregateFunction == Aggregate.MIN || aggregateFunction == Aggregate.MAX) {
			if(getVariableByName(variableName).dataType == DataType.STRING || getVariableByName(variableName).dataType == DataType.BOOLEAN){
				logMessage = String.format("[%s] is not numeric", variableName);
				HS.writeLog(LogMessageType.ERROR, logMessage);
				return null;
			}
		}

		newVariableName = newVariableName.trim();
		
		try {
			throwExceptionIfVariableNameEmpty(newVariableName);
		}catch(IllegalArgumentException ie){
			logMessage = String.format("aggregate function variable name is empty");
			HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}
		
		try {
			throwExceptionIfVariableNameInvalid(newVariableName);
		}catch(IllegalArgumentException ie) {
			logMessage = String.format("aggregate function variable name is invalid");
			HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}
		
		try {
			throwExceptionIfVariableAlreadyPresent(newVariableName);
		}catch(IllegalArgumentException ie){
			logMessage = String.format("[%s] - aggregate variable name already exist in the table", newVariableName);
			HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}

		if(Objects.nonNull(groupBy))
			groupBy = groupBy.trim();
		
		

		try {
			throwExceptionIfVariableNameNull(groupBy);
			throwExceptionIfVariableNameEmpty(groupBy);
			String a_groupBy[] = groupBy.split(" ");
			for(int i=0;i<a_groupBy.length;i++) {
				throwExceptionIfVariableNotPresent(a_groupBy[i]);
			}
		}catch(IllegalArgumentException ie) {
			logMessage = String.format("error in aggregate method");
			HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}

		String a_groupBy[] = groupBy.split(" ");

		DataSet dataSet = getCopy();
		final String colName = variableName;
		DataType tempDataType = null;

		dataSet.sort(groupBy);
		if(aggregateFunction == Aggregate.COUNT) {
			tempDataType = DataType.INTEGER;
		}else if(aggregateFunction == Aggregate.SUM || aggregateFunction == Aggregate.MIN || aggregateFunction == Aggregate.MAX) {
			tempDataType = getVariableByName(colName).dataType;
		}else if(aggregateFunction == Aggregate.AVG) {
			tempDataType = DataType.DOUBLE;
		}

		final DataType newDataType = tempDataType;

		dataSet.addCalculatedVariable(newVariableName, newDataType, (observation, store) -> {
			//Observation observation = observationData.getObservation();
			//Retain retain = observationData.getRetain();
			Store retain = store;

			Object retainValue = null;
			Integer counter;
			Object outValue = null;
			if(observation.getIndex() == 0){
				if(newDataType == DataType.DOUBLE)
					retainValue = 0.0;
				else if(newDataType == DataType.FLOAT)
					retainValue = 0.0f;
				else if(newDataType == DataType.INTEGER)
					retainValue = 0;
				else if(newDataType == DataType.LONG)
					retainValue = 0L;
				counter = 0;
			}else{
				counter = (int)retain.retrieve("counter"); //(Integer)((ArrayList)data).get(0);
				if(newDataType == DataType.DOUBLE)	
					retainValue = Double.parseDouble(retain.retrieve("retainValue").toString()); //(Double)((ArrayList)data).get(1);
				else if(newDataType == DataType.INTEGER)	
					retainValue = (int)retain.retrieve("retainValue");
				else if(newDataType == DataType.LONG)	
					retainValue = (long)retain.retrieve("retainValue");
				else if(newDataType == DataType.FLOAT)	
					retainValue = (float)retain.retrieve("retainValue");
				else if(newDataType == DataType.STRING)	
					retainValue = (String)retain.retrieve("retainValue");
				else if(newDataType == DataType.CHARACTER)	
					retainValue = (char)retain.retrieve("retainValue");
				else if(newDataType == DataType.BOOLEAN)	
					retainValue = (boolean)retain.retrieve("retainValue");
				else if(newDataType == DataType.LOCAL_DATE)	
					retainValue = (LocalDate)retain.retrieve("retainValue");
				else if(newDataType == DataType.LOCAL_TIME)	
					retainValue = (LocalTime)retain.retrieve("retainValue");
				else if(newDataType == DataType.LOCAL_DATE_TIME)	
					retainValue = (LocalDateTime)retain.retrieve("retainValue");
			}

			Object variableValue = null;

			if(aggregateFunction == Aggregate.SUM || aggregateFunction == Aggregate.AVG || aggregateFunction == Aggregate.MIN || aggregateFunction == Aggregate.MAX) {

				Object v = observation.getValue(colName);

				if(v != null) {
					if(getVariableByName(colName).dataType == DataType.INTEGER)
						variableValue = (int)v; //Double.parseDouble(v.toString());
					else if(getVariableByName(colName).dataType == DataType.LONG)
						variableValue = (long)v;
					else if(getVariableByName(colName).dataType == DataType.FLOAT)
						variableValue = (float)v;
					else if(getVariableByName(colName).dataType == DataType.DOUBLE)
						variableValue = (double)v;
					else if(getVariableByName(colName).dataType == DataType.LOCAL_DATE)
						variableValue = (LocalDate)v;
					else 
						variableValue = (Double)v;
				}
			}

			//first = 1 and last = 1
			if(observation.getFirstValue(a_groupBy[a_groupBy.length-1]) == true && observation.getLastValue(a_groupBy[a_groupBy.length-1]) == true) {
				if(aggregateFunction == Aggregate.SUM) {
					counter = 1;
					if(variableValue != null) {
						retainValue = variableValue;
						if(newDataType == DataType.INTEGER) 
							outValue = (int)retainValue;
						else if(newDataType == DataType.LONG) 
							outValue = Long.parseLong(retainValue.toString());
						else if(newDataType == DataType.DOUBLE)
							outValue = (double)retainValue;
						else if(newDataType == DataType.FLOAT)
							outValue = Float.parseFloat(retainValue.toString());
						else if(newDataType == DataType.LOCAL_DATE)
							outValue = (LocalDate)retainValue;
					}else{
						outValue = 0;
					}
				}else if (aggregateFunction == Aggregate.AVG) {
					counter = 1;
					if(variableValue != null)
						retainValue = variableValue;
					else
						retainValue = 0.0;
					
					outValue = Double.parseDouble(retainValue.toString());
				}
				else if(aggregateFunction == Aggregate.COUNT) {
					counter = 1;
					outValue = (int)counter;
				}else if(aggregateFunction == Aggregate.MIN) {
					counter = 1;
					if(variableValue != null) {
						retainValue = variableValue;
						if(newDataType == DataType.INTEGER) 
							outValue = Integer.parseInt(retainValue.toString());
						else if(newDataType == DataType.LONG) 
							outValue = Long.parseLong(retainValue.toString());
						else if(newDataType == DataType.DOUBLE)
							outValue = (double)retainValue;
						else if(newDataType == DataType.FLOAT)
							outValue = Float.parseFloat(retainValue.toString());
						else if(newDataType == DataType.LOCAL_DATE)
							outValue = (LocalDate)retainValue;
					}else{
						outValue = null;
					}
				}else if(aggregateFunction == Aggregate.MAX) {
					counter = 1;
					if(variableValue != null) {
						retainValue = variableValue;
						if(newDataType == DataType.INTEGER) 
							outValue = Integer.parseInt(retainValue.toString());
						else if(newDataType == DataType.LONG) 
							outValue = Long.parseLong(retainValue.toString());
						else if(newDataType == DataType.DOUBLE)
							outValue = (double)retainValue;
						else if(newDataType == DataType.FLOAT)
							outValue = Float.parseFloat(retainValue.toString());
					}else{
						outValue = null;
					}
				}
			//first = 1 and last = 0
			}else if(observation.getFirstValue(a_groupBy[a_groupBy.length-1]) == true && observation.getLastValue(a_groupBy[a_groupBy.length-1]) == false) {
				if(aggregateFunction == Aggregate.SUM) { 
					if(newDataType == DataType.INTEGER) {
						if(variableValue != null)
							retainValue = variableValue;
						else
							retainValue = 0;
					}
					counter = 1;
					outValue = retainValue;
				}else if (aggregateFunction == Aggregate.AVG) {
					if(variableValue != null)
						retainValue = variableValue;
					else
						retainValue = 0.0;
					counter = 1;
				}
				else if(aggregateFunction == Aggregate.COUNT) {
					//fValue = 1;
					counter = 1;
				}else if(aggregateFunction == Aggregate.MIN) {
					
					counter = 1;
					if(variableValue != null)
						retainValue = variableValue;
				}else if(aggregateFunction == Aggregate.MAX) {
					
					counter = 1;
					if(variableValue != null)
						retainValue = variableValue;
				}
			//first = 0 and last = 1
			}else if(observation.getFirstValue(a_groupBy[a_groupBy.length-1]) == false && observation.getLastValue(a_groupBy[a_groupBy.length-1]) == true) {
				if(aggregateFunction == Aggregate.SUM) { 
					if(variableValue != null) {
						if(newDataType == DataType.DOUBLE)	
							retainValue = (double)retainValue + (double)variableValue;
						else if(newDataType == DataType.INTEGER)	
							retainValue = (int)retainValue + (int)variableValue;
					}
					counter += 1;

					outValue = retainValue;

				}else if(aggregateFunction == Aggregate.MIN) {
					if(variableValue != null && HSUtils.compareValue(variableValue, retainValue, newDataType) < 0)
						retainValue = variableValue;

					if(newDataType == DataType.INTEGER) 
						outValue = ((Integer)retainValue).intValue();
					else if(newDataType == DataType.LONG) 
						outValue = Long.parseLong(retainValue.toString());
					else if(newDataType == DataType.DOUBLE)
						outValue = (double) retainValue;
					else if(newDataType == DataType.FLOAT)
						outValue = Float.parseFloat(retainValue.toString());
					else if(newDataType == DataType.LOCAL_DATE)
							outValue = (LocalDate)retainValue;
				}else if(aggregateFunction == Aggregate.MAX) {
					if(variableValue != null && HSUtils.compareValue(variableValue, retainValue, newDataType) > 0)
						retainValue = variableValue;

					if(newDataType == DataType.INTEGER) 
						outValue = ((Integer)retainValue).intValue();
					else if(newDataType == DataType.LONG) 
						outValue = Long.parseLong(retainValue.toString());
					else if(newDataType == DataType.DOUBLE)
						outValue = (double) retainValue;
					else if(newDataType == DataType.FLOAT)
						outValue = Float.parseFloat(retainValue.toString());
				}else if(aggregateFunction == Aggregate.AVG) {
					if(variableValue != null)
						retainValue = (double)retainValue + Double.parseDouble(variableValue.toString());
					counter += 1;
					retainValue = (double)retainValue/(double)counter;
					outValue = (double)retainValue;
				}else if(aggregateFunction == Aggregate.COUNT) {
					counter += 1;
					outValue = (int)counter;
				}
			}else {
				if(aggregateFunction == Aggregate.SUM) { 
					if(newDataType == DataType.INTEGER)
						retainValue = (int)retainValue + (int)variableValue;
					else if(newDataType == DataType.DOUBLE)
						retainValue = (double)retainValue + (double)variableValue;
					counter += 1;
				}else if(aggregateFunction == Aggregate.AVG) {
					if(variableValue != null)
						retainValue = (double)retainValue + (double)variableValue;
					counter += 1;
				}else if(aggregateFunction == Aggregate.COUNT) {
					counter += 1;
				}else if(aggregateFunction == Aggregate.MIN) {
					if(variableValue != null && HSUtils.compareValue(variableValue, retainValue, newDataType) < 0)
						retainValue = variableValue;
					counter += 1;
				}else if(aggregateFunction == Aggregate.MAX) {
					if(variableValue != null && HSUtils.compareValue(variableValue, retainValue, newDataType) > 0)
						retainValue = variableValue;
					counter += 1;
				}
			}


			retain.save("counter", counter);
			retain.save("retainValue", retainValue);
			
			return new VariableData(outValue, retain);
		});

		
		dataSet.where((observation, store) -> {
			//Observation observation = observationData.getObservation();
			//Retain retain = observationData.getRetain();

			boolean keep = false;
			if(observation.getFirstValue(a_groupBy[a_groupBy.length-1]) == true && observation.getLastValue(a_groupBy[a_groupBy.length-1]) == true) {
				keep = true;
			}else if(observation.getFirstValue(a_groupBy[a_groupBy.length-1]) == false && observation.getLastValue(a_groupBy[a_groupBy.length-1]) == true) {
				keep = true;;
			}

			return new WhereData(keep, store);
		});

		return dataSet;
	}
	
	@Override
	public IDataSet count() {
		int count = this.getObservationCount();
		IVariableFactory variableFactory =  HSUtils.getVariableFactory(this.storageType);
		Variable newVariable = variableFactory.createVariableWithDuplicateValues("_COUNT_", DataType.INTEGER, count, count);
		DataSet dataSet = this.getCopy();
		dataSet.addVariable(newVariable);
		return dataSet;
	}
	
	@Override
	public IDataSet count(ParameterBuilder.CountParameter params) { //String groupBy, String countVariableName) {
		
		String groupBy = params.getGroupBy();
		String countVariableName = params.getCountVariableName();
		if(Objects.isNull(countVariableName) || countVariableName.isEmpty())
			countVariableName = "_COUNT_BY_" + HSUtils.getValidVariableName(groupBy);
		
		groupBy = groupBy.trim().replaceAll(" +", " ");
		return _aggregate(null, groupBy, countVariableName.trim(), Aggregate.COUNT);
	}
	
	@Override
	public IDataSet sum(ParameterBuilder.SumParameter params) { 
	
		String variableName = params.getVariableName();
		String groupBy = params.getGroupBy();
		String sumVariableName = params.getSumVariableName();
		
		IDataSet dataSet = null;
		if(Objects.isNull(groupBy) || groupBy.isEmpty()) {
			int count = getObservationCount();
			Variable variable = getVariableByName(variableName);
			Object s=null, v = null;
			if(variable.dataType == DataType.INTEGER)
				s = 0;
			else if(variable.dataType == DataType.LONG)
				s = 0L;
			else if(variable.dataType == DataType.FLOAT)
				s = 0.0f;
			else if(variable.dataType == DataType.DOUBLE)
				s = 0.0;
			for(int i=0;i<count;i++){
				v = variable.values.get(i);
				if(v != null) {
					if(variable.dataType == DataType.INTEGER)
						s = (int)s + (int)v;
					else if(variable.dataType == DataType.LONG)
						s = (long)s + (long)v;
					else if(variable.dataType == DataType.FLOAT)
						s = (float)s + (float)v;
					else if(variable.dataType == DataType.DOUBLE)
						s = (double)s + (double)v;
				}
			}
			if(Objects.isNull(sumVariableName) || sumVariableName.isEmpty())
				sumVariableName = "_SUM_" + variableName.trim();
			final Object sum = s;
			dataSet = this.getCopy();
			dataSet.addCalculatedVariable(sumVariableName, variable.dataType, observation -> {
														return sum;
													});
		}else{
			if(Objects.isNull(sumVariableName) || sumVariableName.isEmpty())
				sumVariableName = "_SUM_" + variableName.trim() + "_BY_" + HSUtils.getValidVariableName(groupBy);
			groupBy = groupBy.trim().replaceAll(" +", " ");
			dataSet =  _aggregate(variableName.trim(), groupBy, sumVariableName, Aggregate.SUM);
		}
		return dataSet;
	}//end of sum() method

	
	@Override
	public IDataSet avg(ParameterBuilder.AvgParameter params) { 
	
		String variableName = params.getVariableName();
		String groupBy = params.getGroupBy();
		String avgVariableName = params.getAvgVariableName();
		boolean includeNull = params.getIncludeNull();
		
		DataSet dataSet = null;
		if(Objects.isNull(groupBy) || groupBy.isEmpty()) {
			int count;
			if(includeNull == false)
				count = getObservationCount();
			else {
				Variable variable = getVariableByName(variableName);
				count = variable.getCountWithoutNull();
			}
			Variable variable = getVariableByName(variableName);
			Object s = 0.0, v = null;
			for(int i=0;i<count;i++){
				v = variable.values.get(i);
				if(v != null) {
					if(variable.dataType == DataType.INTEGER)
						s = (double)s + (int)v;
					else if(variable.dataType == DataType.LONG)
						s = (double)s + (long)v;
					else if(variable.dataType == DataType.FLOAT)
						s = (double)s + (float)v;
					else if(variable.dataType == DataType.DOUBLE)
						s = (double)s + (double)v;
				}
			}
			if(Objects.isNull(avgVariableName) || avgVariableName.isEmpty())
				avgVariableName = "_AVG_" + variableName.trim();
			final Object avg = (double) (((double)s)/count);
			dataSet = this.getCopy();
			dataSet.addCalculatedVariable(avgVariableName, DataType.DOUBLE, observation -> {
														return avg;
													});
		}else{
			if(Objects.isNull(avgVariableName) || avgVariableName.isEmpty())
				avgVariableName = "_AVG_" + variableName.trim() + "_BY_" + HSUtils.getValidVariableName(groupBy);
			groupBy = groupBy.trim().replaceAll(" +", " ");
			dataSet = (DataSet) _aggregate(variableName.trim(), groupBy, avgVariableName, Aggregate.AVG);
		}
		return dataSet;
	}//end of avg() method
	
	@Override
	public IDataSet max(ParameterBuilder.MaxParameter params) { 
	
		String variableName = params.getVariableName();
		String groupBy = params.getGroupBy();
		String maxVariableName = params.getMaxVariableName();
		
		IDataSet dataSet = null;
		if(Objects.isNull(groupBy) || groupBy.isEmpty()) {
			int observationCount = getObservationCount();
			Variable variable = getVariableByName(variableName);
			Object max = null, v = null;
			int ctr = 0;
			do {
				max = variable.values.get(ctr);
				ctr++;
			}while(ctr < observationCount && max == null);
			
			int cmp = 0;
			for(int i=ctr;i<observationCount;i++){
				v = variable.values.get(i);
				if(v != null) {
					cmp = HSUtils.compareValue(max, v, variable.dataType);
					if(cmp < 0) 
						max = v;
					
				}
			}
			if(Objects.isNull(maxVariableName) || maxVariableName.isEmpty())
				maxVariableName = "_MAX_" + variableName.trim();
			final Object maxValue = max;
			dataSet = this.getCopy();
			dataSet.addCalculatedVariable(maxVariableName, variable.dataType, observation -> {
				return maxValue;
			});
		}else{
			if(Objects.isNull(maxVariableName) || maxVariableName.isEmpty())
				maxVariableName = "_MAX_" + variableName.trim() + "_BY_" + HSUtils.getValidVariableName(groupBy);
			groupBy = groupBy.trim().replaceAll(" +", " ");
			dataSet =  _aggregate(variableName.trim(), groupBy, maxVariableName, Aggregate.MAX);
		}
		return dataSet;
	}//end of max() method
	
	@Override
	public IDataSet min(ParameterBuilder.MinParameter params) { 
	
		String variableName = params.getVariableName();
		String groupBy = params.getGroupBy();
		String minVariableName = params.getMinVariableName();
		
		DataSet dataSet = null;
		if(Objects.isNull(groupBy) || groupBy.isEmpty()) {
			int observationCount = getObservationCount();
			Variable variable = getVariableByName(variableName);
			Object min = null, v = null;
			int ctr = 0;
			do {
				min = variable.values.get(ctr);
				ctr++;
			}while(ctr < observationCount && min == null);

			int cmp = 0;
			for(int i=ctr;i<observationCount;i++){
				v = variable.values.get(i);
				if(v != null) {
					cmp = HSUtils.compareValue(min, v, variable.dataType);
					if(cmp > 0) 
						min = v;
					
				}
			}
			if(Objects.isNull(minVariableName) || minVariableName.isEmpty())
				minVariableName = "_MIN_" + variableName.trim();
			final Object minValue = min;
			dataSet = this.getCopy();
			dataSet.addCalculatedVariable(minVariableName, variable.dataType, observation -> {
				return minValue;
			});
		}else{
			if(Objects.isNull(minVariableName) || minVariableName.isEmpty())
				minVariableName = "_MIN_" + variableName.trim() + "_BY_" + HSUtils.getValidVariableName(groupBy);
			groupBy = groupBy.trim().replaceAll(" +", " ");
			dataSet =  (DataSet) _aggregate(variableName.trim(), groupBy, minVariableName, Aggregate.MIN);
		}
		return dataSet;
	}//end of min() method


	@Override
	public IDataSet aggregate(ParameterBuilder.AggregateParameter params) {
		
		String variableName = params.getVariableName();
		String groupBy = params.getGroupBy();
		groupBy = groupBy == null ? "" : groupBy;
		boolean sum = params.getSum();
		boolean count = params.getCount();
		boolean avg = params.getAvg();
		boolean includeNull = params.getIncludeNull();
		boolean max = params.getMax();
		boolean min = params.getMin();
		
		String countVariableName = params.getCountVariableName();
		String sumVariableName = params.getSumVariableName();
		String avgVariableName = params.getAvgVariableName();
		String minVariableName = params.getMinVariableName();
		String maxVariableName = params.getMaxVariableName();
		
		DataSet dataSet = this.getCopy();
		
		ArrayList<IDataSet> dataSets = new ArrayList<IDataSet>();
		IDataSet dt = null;
		if(sum == true) {
			if(Objects.isNull(sumVariableName) || sumVariableName.isEmpty()) {
				if(Objects.isNull(groupBy) || groupBy.isEmpty())	
					sumVariableName = "_SUM_" + variableName.trim();
				else
					sumVariableName = "_SUM_" + variableName.trim() + "_BY_" + HSUtils.getValidVariableName(groupBy);
			}
			dt = dataSet.sum(new ParameterBuilder.SumParameter(variableName).
					groupBy(groupBy).
					sumVariableName(sumVariableName)
			);
			dt.keepVariable(groupBy + " " + sumVariableName);
			dataSets.add(dt);
		}
		if(min == true) {
			if(Objects.isNull(minVariableName) || minVariableName.isEmpty()) {
				if(Objects.isNull(groupBy) || groupBy.isEmpty()) {
					minVariableName = "_MIN_" + variableName.trim();
					
				}else {
					minVariableName = "_MIN_" + variableName.trim() + "_BY_" + HSUtils.getValidVariableName(groupBy);
					
				}
			}
			dt = dataSet.min(new ParameterBuilder.MinParameter(variableName).groupBy(groupBy));
			dt.keepVariable(groupBy + " " + minVariableName);
			dataSets.add(dt);
		}
		if(max == true) {
			if(Objects.isNull(maxVariableName) || maxVariableName.isEmpty()) {
				if(Objects.isNull(groupBy) || groupBy.isEmpty()) {
					maxVariableName = "_MAX_" + variableName.trim();
					
				}else {
					maxVariableName = "_MAX_" + variableName.trim() + "_BY_" + HSUtils.getValidVariableName(groupBy);
					
				}
			}
			dt = dataSet.max(new ParameterBuilder.MaxParameter(variableName).groupBy(groupBy));
			dt.keepVariable(groupBy + " " + maxVariableName);
			dataSets.add(dt);
		}
		if(avg == true) {
			if(Objects.isNull(avgVariableName) || avgVariableName.isEmpty()) {
				if(Objects.isNull(groupBy) || groupBy.isEmpty()) {
					avgVariableName = "_AVG_" + variableName.trim();
					
				}else {
					avgVariableName = "_AVG_" + variableName.trim() + "_BY_" + HSUtils.getValidVariableName(groupBy);
					
				}
			}
			dt = dataSet.avg(new ParameterBuilder.AvgParameter(variableName).
						groupBy(groupBy).
						avgVariableName(avgVariableName).
						includeNull(includeNull)
			);
			dt.keepVariable(groupBy + " " + avgVariableName);
			dataSets.add(dt);
		}
		if(count == true) {
			if(Objects.isNull(countVariableName) || countVariableName.isEmpty()) {
				if(Objects.isNull(groupBy) || groupBy.isEmpty())
					countVariableName = "_COUNT_";
				else
					countVariableName = "_COUNT_" + variableName.trim() + "_BY_" + HSUtils.getValidVariableName(groupBy);
			}
			if(Objects.isNull(groupBy) || groupBy.isEmpty()) {
				dt = dataSet.count();
			}else{
				dt = dataSet.count(new ParameterBuilder.CountParameter(groupBy).
					countVariableName(countVariableName)
				);
			}
			dt.keepVariable(groupBy + " " + countVariableName);
			dataSets.add(dt);
		}
			
			
		
		for(int i=1;i<dataSets.size();i++){
			dataSets.get(0).join(dataSets.get(i), groupBy, JoinType.INNER, false);
		}
		
		dataSet = (DataSet) dataSets.get(0);
		
		return dataSet;
	}
	
	@Override
	public IDataSet getVariableMetaData() {
		VariableFactory variableFactory = new VariableFactory();
		
		ArrayList<Variable> newVariables = new ArrayList<Variable>();
		Variable nameVariable = variableFactory.createVariableWithNullValues("Name", DataType.STRING, 0);
		Variable dataTypeVariable = variableFactory.createVariableWithNullValues("DataType", DataType.STRING, 0);
		Variable formatVariable = variableFactory.createVariableWithNullValues("Format", DataType.BOOLEAN, 0);
		Variable labelVariable = variableFactory.createVariableWithNullValues("Label" , DataType.STRING, 0);
		newVariables.add(nameVariable);
		newVariables.add(dataTypeVariable);
		newVariables.add(formatVariable);
		newVariables.add(labelVariable);
		
		for(int i=0;i<variables.size();i++) {
			newVariables.get(0).values.add(variables.get(i).name);
			newVariables.get(1).values.add(variables.get(i).dataType);
			if(Objects.nonNull(variables.get(i).formatFunction))
				newVariables.get(2).values.add(true);
			else
				newVariables.get(2).values.add(false);
			newVariables.get(3).values.add(variables.get(i).label);
		}
		
		IDataSet dataSet = new DataSet(newVariables);
		
		return dataSet;
	}
	
	@Override
	public void forEachObservation(Consumer<Observation> consumer) {
		
		Object values[];
		DataType dataTypes[];
		Boolean firstValues[];
		Boolean lastValues[];
		String names[];
		Variable variable;
		int variableSize = getVariableCount();
		for(int i=0;i<getObservationCount();i++){
			values = new Object[variableSize];
			dataTypes = new DataType[variableSize];
			firstValues = new Boolean[variableSize];
			lastValues = new Boolean[variableSize];
			names = new String[variableSize];
			for(int j=0;j<getVariableCount();j++) {
				variable = variables.get(j);
				values[j] = variable.values.get(i);
				dataTypes[j] = variable.dataType;
				names[j] = variable.name;
				if(Objects.nonNull(getFirstFLVariableByName(variable.name)))
					firstValues[j] = getFirstFLVariableByName(variable.name).values.get(i);
				else
					firstValues[j] = null;
				if(Objects.nonNull(getLastFLVariableByName(variable.name)))
					lastValues[j] = getLastFLVariableByName(variable.name).values.get(i);
				else
					lastValues[j] = null;
			}
			consumer.accept(new Observation(i, names, dataTypes, values, firstValues, lastValues));
		}
	}
	
	@Override
	public void forEachVariable(Consumer<Variable> consumer) {
		for(int i=0;i<getVariableCount();i++) {
			consumer.accept(new Variable(i, variables.get(i).name, variables.get(i).label, variables.get(i).dataType, variables.get(i).values));
		}
	}
	
	public void printTop() {
		printTop(10);
	}
	
	public void printTop(int howMany) {
		if(howMany > getObservationCount())
			howMany = getObservationCount();
		
		print(0, howMany);
	}
	
	public void printBottom() {
		printBottom(10);
	}
	
	public void printBottom(int howMany) {
		if(howMany > getObservationCount())
			howMany = getObservationCount();
		
		if(howMany > 0)
			print(getObservationCount() - howMany, howMany);
		else
			print(getObservationCount()-1, howMany);	
	}
	
	public void print() {
		print(0, getObservationCount());
	}
	
	public void print(int startIndex, int howMany) {
		System.out.println(getAsString(startIndex, howMany));
	}
	
	public String getAsString(int startIndex, int howMany) {
		
		int lastIndex = (startIndex + howMany) - 1;
		
		try {
			if(startIndex < 0 || startIndex >= getObservationCount())
				throwExceptionIfIndexOutOfBounds(startIndex, 0, getObservationCount()-1);
			else if(lastIndex >= getObservationCount())
				throwExceptionIfIndexOutOfBounds(lastIndex, 0, getObservationCount()-1);
		}catch(IndexOutOfBoundsException ie){
			logMessage = String.format("<%s> print(%s, %s) execution failed", getName(), startIndex, howMany);
			HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}
			
		
		String toPrint = System.lineSeparator();
		
		int variableCount = getVariableCount();
		Variable variable;
		String variableName;
		for(int i=0;i<variableCount;i++) {
			variable = variables.get(i);
			variableName = variables.get(i).name;
			if(variableName.length() > getVariableWidth(variable))
				toPrint += variableName + " ";
			else
				toPrint += variableName + HSUtils.repeat(' ', getVariableWidth(variable) - variableName.length() + 1);
		}
		
		toPrint += System.lineSeparator();
		
		for(int i=0;i<variableCount;i++){
			variable = variables.get(i);
			variableName = variables.get(i).name;

			
			if(variableName.length() > getVariableWidth(variable))
				toPrint += HSUtils.repeat('=', variableName.length()) + " ";
			else
				toPrint += HSUtils.repeat('=', getVariableWidth(variable)) + " ";
		}
		
		toPrint += System.lineSeparator();
		
		String value;
		for(int i=startIndex;i<=lastIndex;i++){
			for(int j=0;j<getVariableCount();j++){
				variable = variables.get(j);
				variableName = variable.name;
				value = getVariableFormattedValueAt(variableName, i);
				if(variableName.length() > getVariableWidth(variable))
					toPrint += value + HSUtils.repeat(' ', variableName.length() - value.length() + 1);
				else
					toPrint += value + HSUtils.repeat(' ', getVariableWidth(variable) - value.length() + 1);
			}
			toPrint += System.lineSeparator();
		}
		
		return toPrint;
	}
	
	private int getVariableWidth(Variable variable) {

		int width = -1;
		int wd;
		for(int i=0;i<variable.values.size();i++){
			if(variable.dataType == DataType.STRING) {
				wd = getVariableFormattedValueAt(variable.name, i).length(); 
				if(wd > width)
					width = wd;
			}
		}
		return width;
	}
	
	public void printToLogTop() {
		printToLogTop(10);
	}
	
	public void printToLogTop(int howMany) {
		if(howMany > getObservationCount())
			howMany = getObservationCount();
		
		printToLog(0, howMany);
	}
	
	public void printToLogBottom() {
		printToLogBottom(10);
	}
	
	public void printToLogBottom(int howMany) {
		if(howMany > getObservationCount())
			howMany = getObservationCount();
		
		if(howMany > 0)
			printToLog(getObservationCount() - howMany, howMany);	
		else
			printToLog(getObservationCount()-1, howMany);
	}
	
	public void printToLog() {
		printToLog(0, getObservationCount());
	}
	
	public void printToLog(int startIndex, int howMany) {
		HS.writeLog(LogMessageType.INFO, getAsString(startIndex, howMany));
	}
	

	
	/*
	private void saveHTML(String filePath) {
		
		File file = new File(filePath);
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            
        } catch (Exception e) {
            e.printStackTrace();
        }/*finally{
            //close resources
            try {
                fr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		
		String data = "<html> \n" + 
					   "<head>" + 
					   "<style>" +
					   "table.blueTable {" +
							"border: 1px solid #1C6EA4;" +
							"background-color: #EEEEEE;" +
  "width: 100%;" +
  "text-align: left;" +
  "border-collapse: collapse;" +
"}" +
"table.blueTable td, table.blueTable th {" +
  "border: 1px solid #AAAAAA;" +
  "padding: 3px 2px;" +
"}" +
"table.blueTable tbody td {" + 
  "font-size: 13px;" +
"}" +
"table.blueTable tr:nth-child(even) {" +
  "background: #D0E4F5;" +
"}" +
"table.blueTable thead {" +
  "background: #1C6EA4;" +
  "background: -moz-linear-gradient(top, #5592bb 0%, #327cad 66%, #1C6EA4 100%);" +
  "background: -webkit-linear-gradient(top, #5592bb 0%, #327cad 66%, #1C6EA4 100%);" +
  "background: linear-gradient(to bottom, #5592bb 0%, #327cad 66%, #1C6EA4 100%);" +
  "border-bottom: 2px solid #444444;" +
"}" +
"table.blueTable thead th {" +
  "font-size: 15px;" +
  "font-weight: bold;" +
  "color: #FFFFFF;" +
  "border-left: 2px solid #D0E4F5;" +
"}" +
"table.blueTable thead th:first-child {" +
 " border-left: none;" +
"}" +

"table.blueTable tfoot {" +
  "font-size: 14px;" +
  "font-weight: bold;" +
  "color: #FFFFFF;" +
  "background: #D0E4F5;" +
  "background: -moz-linear-gradient(top, #dcebf7 0%, #d4e6f6 66%, #D0E4F5 100%);" + 
  "background: -webkit-linear-gradient(top, #dcebf7 0%, #d4e6f6 66%, #D0E4F5 100%);" +
  "background: linear-gradient(to bottom, #dcebf7 0%, #d4e6f6 66%, #D0E4F5 100%);" +
  "border-top: 2px solid #444444;" +
"}" +
"table.blueTable tfoot td {" +
  "font-size: 14px;" +
"}" +
"table.blueTable tfoot .links {" +
  "text-align: right;" +
"}" +
"table.blueTable tfoot .links a{" +
  "display: inline-block;" +
  "background: #1C6EA4;" +
  "color: #FFFFFF;" +
  "padding: 2px 8px;" +
  "border-radius: 5px;" +
"}" +
					   "</style>" +
					   "</head> \n" +
					   "<body> \n" +
					   "<table class=\"bluetable\" border=\"1\"> \n";
		try{fr.write(data);}catch(Exception e){}
		for(Variable variable:variables) {
			data = "<th>" + variable.name + "</th> \n";
			try{fr.write(data);}catch(Exception e){}
		}
		
		String align = "left";
		Variable variable = null;
		for(int i=0;i<getObservationCount();i++) {
			data = "<tr>" + System.lineSeparator();
			try{fr.write(data);}catch(Exception e){}
			for(int j=0;j<getVariableCount();j++) {
				variable = variables.get(j);
				if(variable.dataType == DataType.INTEGER || 
					variable.dataType == DataType.LONG ||
					variable.dataType == DataType.FLOAT ||
					variable.dataType == DataType.DOUBLE ||
					variable.dataType == DataType.LOCAL_DATE ||
					variable.dataType == DataType.LOCAL_TIME ||
					variable.dataType == DataType.LOCAL_DATE_TIME)
					align = "right";
				else
					align = "left";
				if(variables.get(j).values.get(i) != null) {
					String d = String.format("<td align=\"%s\"> %s </td>", align, variable.values.get(i)) + System.lineSeparator();
					data = d;
					//System.out.println(d);
					try{fr.write(data);fr.flush();}catch(Exception e){}
				}
					//data += 	"<td align='" + align + "'>" + variables.get(j).values.get(i) + "</td> \n";
				else {
					data = 	"<td>" + "&nbsp;" + "</td> \n";
					try{fr.write(data);fr.flush();}catch(Exception e){}
				}
			}
			data = "</tr> \n";
			try{fr.write(data);}catch(Exception e){}
		}
		
		data = "</table> \n" +
				"</body> \n" +
				"</html> \n";
		try{fr.write(data);}catch(Exception e){}
		//finally{
            //close resources
            try {
                fr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
       // }
		//System.out.println(data);
		
		
	}*/
	
	@Override
	public IDataSet concatenate(IDataSet dataSet) {
		return concatenate(dataSet, HS.defaultOut);
	}
	
	
	
	@Override
	public IDataSet shuffle() {
		return shuffle(HS.defaultOut);
	}
	
	@Override
	public IDataSet shuffle(boolean out) {
		
		Timer timer = new Timer();
		timer.start();
		
		logMessage = String.format("<%s> shuffle(%s) execution started", getName(), out);
		HS.writeLog(LogMessageType.INFO, logMessage);

		DataSet dataSet = this;
		
		if(out) { 
			dataSet = this.getCopy();
			dataSet.setName(this.getName() + "_copy");
			logMessage = String.format("copy of <%s> is created with name <%s>", this.getName(), dataSet.getName());
			HS.writeLog(LogMessageType.NOTE, logMessage);
		}
		dataSet.sortIndex.shuffle();
		
		timer.stop();
		
		//logMessage = String.format("<%s> has now [%s observation(s) x %s variable(s)]", dataSet.getName(), dataSet.getObservationCount(), dataSet.getVariableCount());
		//writeLog(LogMessageType.NOTE, logMessage);
		
		logMessage = String.format("<%s> shuffle(%s) execution took %s sec(s)", dataSet.getName(), out, timer.timeElapsed());
		HS.writeLog(LogMessageType.NOTE, logMessage);
		
		return dataSet;
		
	}
	
	@Override
	public IDataSet top(int howMany) {
		return top(howMany, HS.defaultOut);
	}
	
	@Override
	public IDataSet top(int howMany, boolean out) {
		
		throwExceptionIfIndexOutOfBounds(howMany, 0, getObservationCount()-1);
		
		DataSet dataSet = null;
		
		if(out)
			dataSet = this.getCopy();
		else
			dataSet = this;
		
		DataSet newDataSet = dataSet.getCopy();
		
		dataSet.removeAllObservations(false);
		
		Observation observation;
		Object observationValues[];
		final int variableCount = dataSet.getVariableCount();
		for(int i=0;i<howMany;i++){
			observation = newDataSet.getObservation(i);
			observationValues = new Object[variableCount];
			for(int j=0;j<variableCount;j++){
				observationValues[j] = observation.getValue(j);
			}
			dataSet.addObservation(false, observationValues);
		}
		
		return dataSet;
	}
	
	@Override
	public IDataSet bottom(int howMany) {
		return bottom(howMany, HS.defaultOut);
	}
	
	@Override
	public IDataSet bottom(int howMany, boolean out) {
		
		throwExceptionIfIndexOutOfBounds(howMany, 0, getObservationCount()-1);
		
		DataSet dataSet = null;
		
		if(out)
			dataSet = this.getCopy();
		else
			dataSet = this;
		
		DataSet newDataSet = dataSet.getCopy();
		
		dataSet.removeAllObservations(false);
		
		Observation observation;
		Object observationValues[];
		final int variableCount = dataSet.getVariableCount();
		for(int i=newDataSet.getObservationCount()-howMany;i<newDataSet.getObservationCount();i++){
			observation = newDataSet.getObservation(i);
			observationValues = new Object[variableCount];
			for(int j=0;j<variableCount;j++){
				observationValues[j] = observation.getValue(j);
			}
			dataSet.addObservation(false, observationValues);
		}
		
		return dataSet;
	}
	
	@Override
	public IDataSet replaceVariableValue(String variableName, Object oldValue, Object newValue) {
		return replaceVariableValue(variableName, oldValue, newValue, HS.defaultOut);
	}
	
	@Override
	public IDataSet replaceVariableValue(String variableName, Object oldValue, Object newValue, boolean out) {
		
		DataSet dataSet = null;
		
		if(out)
			dataSet = this.getCopy();
		else
			dataSet = this;
		
		Variable variable;
		Object value;
		for(int i=0;i<dataSet.getObservationCount();i++){
			variable = dataSet.getVariableByName(variableName);
			value = variable.values.get(sortIndex.get(i));
			if(HSUtils.compareValue(value, oldValue, variable.dataType) == 0) {
				variable.values.set(sortIndex.get(i), newValue);
			}
		}
		
		return dataSet;
	}
	
	
	@Override
	public IDataSet updateVariableDataType(String variableName, DataType newDataType) throws IllegalArgumentException, NumberFormatException {
		return updateVariableDataType(variableName, newDataType, HS.defaultOut);
	}
	
	@Override
	public IDataSet updateVariableDataType(String variableName, DataType newDataType, boolean out) throws IllegalArgumentException, NumberFormatException {
		
		DataSet dataSet = null;
		
		if(out)
			dataSet = this.getCopy();
		else
			dataSet = this;
		
		Variable variable = dataSet.getVariableByName(variableName);
		//System.out.println("cn " + HSUtils.isDataTypeConversionValid(variable.values, variable.dataType, newDataType));
		Informat informat = HSUtils.isDataTypeConversionValid(variable.values, variable.dataType, newDataType);
		System.out.println(informat);
		if(informat != null) {
			 //= HSUtils.getInformatByDataType(newDataType);
			//Informat informat = HSUtils.inferInformat(variable.values);
			
			variable.convertValues(informat);
			variable.dataType = newDataType;
			
		}else{
			if(variable.dataType == DataType.STRING) {
				if(newDataType == DataType.INTEGER || newDataType == DataType.LONG ||
					newDataType == DataType.FLOAT || newDataType == DataType.DOUBLE)
					throw new NumberFormatException("DataType cannot be updated");
			}
		}
		
		return dataSet;
	}
	
	@Override
	public IDataSet updateVariableDataType(String variableName, Informat informat) throws IllegalArgumentException, NumberFormatException {
		return updateVariableDataType(variableName, informat, HS.defaultOut);
	}
	
	@Override
	public IDataSet updateVariableDataType(String variableName, Informat informat, boolean out) throws IllegalArgumentException, NumberFormatException {
		
		DataSet dataSet = null;
		
		if(out)
			dataSet = this.getCopy();
		else
			dataSet = this;
		
		Variable variable = dataSet.getVariableByName(variableName);
		if(variable.dataType == DataType.STRING) {
			if(informat == Informat.AUTO) {
				ArrayList<Object> list = variable.getRandomValues(10);
				Informat detectedInformat = null;
				for(int i=0;i<list.size();i++) {
					detectedInformat = HSUtils.detectInformat(list.get(i));
					if(detectedInformat != null)
						break;
				}
				System.out.println(detectedInformat);
				if(detectedInformat != null) {
					variable.convertValues(detectedInformat);
					variable.dataType = HSUtils.getDataTypeByInformat(detectedInformat);
				}
			}
			else{
				variable.convertValues(informat);
				variable.dataType = HSUtils.getDataTypeByInformat(informat);
			}
		}
		
		return dataSet;
	}
	
	@Override
	public IDataSet randomObservations(int howMany) {
		return randomObservations(howMany, HS.defaultOut);
	}
	
	@Override
	public IDataSet randomObservations(final int howMany, final boolean out) {
		
		Timer timer = new Timer();
		timer.start();
		
		logMessage = String.format("<%s> getRandomObservations(%s, %s) execution started", getName(), howMany, out);
		HS.writeLog(LogMessageType.INFO, logMessage);
		
		try {
			throwExceptionIfIndexOutOfBounds(howMany, 1, getObservationCount());
		}catch(IndexOutOfBoundsException ie) {
			logMessage = String.format("<%s> getRandomObservations(%s, %s) execution failed", getName(), howMany, out);
			HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}
		
		DataSet dataSet = this;
		
		if(out) {
			dataSet = this.getCopy();
			dataSet.setName(this.getName() + "_copy");
			logMessage = String.format("copy of <%s> is created with name <%s>", this.getName(), dataSet.getName());
			HS.writeLog(LogMessageType.NOTE, logMessage);
		}
		
		dataSet.sortIndex.shuffle();
		
		DataSet newDataSet = dataSet.getCopy();
		
		dataSet.removeAllObservations(false);
		
		Observation observation;
		Object observationValues[];
		final int variableCount = dataSet.getVariableCount();
		for(int i=0;i<howMany;i++){
			observation = newDataSet.getObservation(i);
			observationValues = new Object[variableCount];
			for(int j=0;j<variableCount;j++){
				observationValues[j] = observation.getValue(j);
			}
			dataSet.addObservation(false, observationValues);
		}
		
		timer.stop();
		
		logMessage = String.format("<%s> has now [%s observation(s) x %s variable(s)]", dataSet.getName(), dataSet.getObservationCount(), dataSet.getVariableCount());
		HS.writeLog(LogMessageType.NOTE, logMessage);
		
		logMessage = String.format("<%s> getRandomObservations(%s, %s) execution took %s sec(s)", dataSet.getName(), howMany, out, timer.timeElapsed());
		HS.writeLog(LogMessageType.NOTE, logMessage);
		
		
		
		return dataSet;
	}
	
	@Override
	public IDataSet concatenate(IDataSet dataSet, boolean out) {
		
		try{
			throwExceptionIfDataSetNull(dataSet);
		}catch(IllegalArgumentException ie) {
			logMessage = String.format("Error in concatenate()");
			HS.writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}
		
		DataSet firstDataSet = out ? this.getCopy() : this;
		
		DataSet secondDataSet = (DataSet)dataSet;
		
		VariableFactory variableFactory;
		if(firstDataSet.storageType == StorageType.DISK)
			variableFactory = new VariableFactory(StorageType.DISK);
		else
			variableFactory = new VariableFactory(StorageType.MEMORY);
		
		Variable firstDataSetVariable, secondDataSetVariable;
		Variable newVariable;
		for(int i=0;i<secondDataSet.variables.size();i++){
			if(!firstDataSet.hasVariable(secondDataSet.variables.get(i).name)) {
				newVariable = variableFactory.createVariableWithNullValues(secondDataSet.variables.get(i).name, secondDataSet.variables.get(i).dataType, firstDataSet.getObservationCount());
				firstDataSet.variables.add(newVariable);
			}
		}
		
		for(int i=0;i<secondDataSet.getObservationCount();i++) {
			for(int j=0;j<firstDataSet.getVariableCount();j++) {
				firstDataSetVariable = firstDataSet.variables.get(j);
				if(secondDataSet.hasVariable(firstDataSetVariable.name)) {
					secondDataSetVariable = secondDataSet.getVariableByName(firstDataSet.variables.get(j).name);
					if(firstDataSetVariable.dataType == DataType.STRING)
						firstDataSet.variables.get(j).values.add(secondDataSetVariable.values.get(i).toString());
					else if(firstDataSetVariable.dataType == DataType.INTEGER && secondDataSetVariable.dataType == DataType.DOUBLE)
						firstDataSet.variables.get(j).values.add(((Double)secondDataSetVariable.values.get(i)).intValue());
					else if(firstDataSetVariable.dataType == DataType.INTEGER && secondDataSetVariable.dataType == DataType.LONG)
						firstDataSet.variables.get(j).values.add(Integer.parseInt(((Long)secondDataSetVariable.values.get(i)).toString()));
					else if(firstDataSetVariable.dataType == DataType.LONG && secondDataSetVariable.dataType == DataType.DOUBLE)
						firstDataSet.variables.get(j).values.add(((Double)secondDataSetVariable.values.get(i)).longValue());
					else if(firstDataSetVariable.dataType == DataType.LONG && secondDataSetVariable.dataType == DataType.INTEGER)
						firstDataSet.variables.get(j).values.add(Long.parseLong(((Integer)secondDataSetVariable.values.get(i)).toString()));
					else if(firstDataSetVariable.dataType == DataType.DOUBLE && secondDataSetVariable.dataType == DataType.INTEGER)
						firstDataSet.variables.get(j).values.add(Double.parseDouble(((Integer)secondDataSetVariable.values.get(i)).toString()));
					else if(firstDataSetVariable.dataType == DataType.DOUBLE && secondDataSetVariable.dataType == DataType.LONG)
						firstDataSet.variables.get(j).values.add(Double.parseDouble(((Long)secondDataSetVariable.values.get(i)).toString()));
					else
						firstDataSet.variables.get(j).values.add(secondDataSetVariable.values.get(i));
				}
				else
					firstDataSet.variables.get(j).values.add(null);
			}
			firstDataSet.sortIndex.add(firstDataSet.getObservationCount());	
		}
			
		return firstDataSet;
	}//end of concatenate() method
	
	public String toString() {

		String toPrint = "";
		
		//int max;
		String strVal = null;
		Object objVal = null;
		String hLine = "=";
		Variable variable;
		int colWidth = 13;
		if(variables.size() <= 5) {
			for(int i=0;i<variables.size();i++){
				toPrint += String.format("%-13s ", HSUtils.truncString(variables.get(i).name));
			}
			toPrint += System.lineSeparator();
			for(int i=0;i<variables.size();i++){
				//for(int j=0;j<HSUtils.truncString(variables.get(i).name).length();j++)
				for(int j=0;j<colWidth;j++)
					toPrint += hLine;
				//for(int k=0;k<14-HSUtils.truncString(variables.get(i).name).length();k++)
					toPrint += " ";
			}
			toPrint += System.lineSeparator();
			//int tObservation = getObservationCount();
			if(getObservationCount() <= 20) {
				for(int i=0;i<getObservationCount();i++){
					for(int j=0;j<getVariableCount();j++){
						//System.out.println("L " + variables.get(j).name);
						variable = variables.get(j);
						objVal = variables.get(j).values.get(sortIndex.get(i));
						if(objVal == null)
							strVal = "";
						else
							strVal = getVariableFormattedValueAt(variables.get(j).name, i); //objVal.toString();
							if(variable.dataType == DataType.STRING)
								toPrint += String.format("%-13s ", HSUtils.truncString(strVal));
							else
								toPrint += String.format("%13s ", HSUtils.truncString(strVal));
					}
					toPrint += System.lineSeparator();
				}
			}else{
				for(int i=0;i<10;i++){
					for(int j=0;j<getVariableCount();j++){
						objVal = variables.get(j).values.get(sortIndex.get(i));
						if(objVal == null)
							strVal = "";
						else
							strVal = getVariableFormattedValueAt(variables.get(j).name, i);
							toPrint += String.format("%-13s ", HSUtils.truncString(strVal));
					}
					toPrint += System.lineSeparator();
				}
				for(int i=0;i<variables.size();i++){
					toPrint += String.format("%-13s ", "...");
				}
				toPrint += System.lineSeparator();
				for(int i=getObservationCount()-10;i<getObservationCount();i++){
					for(int j=0;j<getVariableCount();j++){
						objVal = variables.get(j).values.get(sortIndex.get(i));
						if(objVal == null)
							strVal = "";
						else
							strVal = getVariableFormattedValueAt(variables.get(j).name, i);
							toPrint += String.format("%-13s ", HSUtils.truncString(strVal));
					}
					toPrint += System.lineSeparator();
				}
			}
		}else{ //variables are more than 5

			for(int i=0;i<3;i++){
				toPrint += String.format("%-13s ", HSUtils.truncString(variables.get(i).name));
			}
			toPrint += " ... ";
			int st = variables.size()-2;
			for(int i=st;i<=st+1;i++){
				toPrint += String.format("%-13s ", HSUtils.truncString(variables.get(i).name));
			}
			
			toPrint += System.lineSeparator();
			for(int i=0;i<3;i++){
				for(int j=0;j<HSUtils.truncString(variables.get(i).name).length();j++)
					toPrint += hLine;
				for(int k=0;k<14-HSUtils.truncString(variables.get(i).name).length();k++)
					toPrint += " ";
			}
			toPrint += "     ";
			for(int i=st;i<=st+1;i++){
				for(int j=0;j<HSUtils.truncString(variables.get(i).name).length();j++)
					toPrint += hLine;
				for(int k=0;k<14-HSUtils.truncString(variables.get(i).name).length();k++)
					toPrint += " ";
			}
			toPrint += System.lineSeparator();
			
			//int tObservation = getObservationCount();
			//observations are less than = 20
			if(getObservationCount() <= 20) {
				for(int i=0;i<getObservationCount();i++){
					for(int j=0;j<3;j++){
						objVal = variables.get(j).values.get(sortIndex.get(i));
						if(objVal == null)
							strVal = "";
						else
							strVal = getVariableFormattedValueAt(variables.get(j).name, i);
						toPrint += String.format("%-13s ", HSUtils.truncString(strVal));
					}
				
					toPrint += " ... ";
					for(int j=st;j<=st+1;j++){
						objVal = variables.get(j).values.get(sortIndex.get(i));
						if(objVal == null)
							strVal = "";
						else
							strVal = getVariableFormattedValueAt(variables.get(j).name, i);
						toPrint += String.format("%-13s ", HSUtils.truncString(strVal));
					}
					toPrint += System.lineSeparator();
				}
			}else{
				for(int i=0;i<10;i++){
					for(int j=0;j<3;j++){
						objVal = variables.get(j).values.get(sortIndex.get(i));
						if(objVal == null)
							strVal = "";
						else
							strVal = getVariableFormattedValueAt(variables.get(j).name, i);
						toPrint += String.format("%-13s ", HSUtils.truncString(strVal));
					}
				
					toPrint += " ... ";
					for(int j=st;j<=st+1;j++){
						objVal = variables.get(j).values.get(sortIndex.get(i));
						if(objVal == null)
							strVal = "";
						else
							strVal = getVariableFormattedValueAt(variables.get(j).name, i);
						toPrint += String.format("%-13s ", HSUtils.truncString(strVal));
					}
					toPrint += System.lineSeparator();
				}
				for(int i=0;i<3;i++){
					toPrint += String.format("%-13s ", "...");
				}
				toPrint += " ... ";
				for(int i=0;i<2;i++){
					toPrint += String.format("%-13s ", "...");
				}
				toPrint += System.lineSeparator();
				for(int i=getObservationCount()-10;i<getObservationCount();i++){
					for(int j=0;j<3;j++){
						objVal = variables.get(j).values.get(sortIndex.get(i));
						if(objVal == null)
							strVal = "";
						else
							strVal = getVariableFormattedValueAt(variables.get(j).name, i);
						toPrint += String.format("%-13s ", HSUtils.truncString(strVal));
					}
				
					toPrint += " ... ";
					for(int j=st;j<=st+1;j++){
						objVal = variables.get(j).values.get(sortIndex.get(i));
						if(objVal == null)
							strVal = "";
						else
							strVal = getVariableFormattedValueAt(variables.get(j).name, i);
						toPrint += String.format("%-13s ", HSUtils.truncString(strVal));
					}
					toPrint += System.lineSeparator();
				}
			}
		}
			
		toPrint += String.format("[%s observation(s) x %s variable(s)]" , getObservationCount(), getVariableCount()) + System.lineSeparator();

		
		return toPrint;
	}
	
	public Variable variable(String variableName) {
		return getVariableByName(variableName);
	}
	
	public void add(Object... v) {
		Object value = null;
		Variable variable;
		for(int i=0;i<getObservationCount();i++){
			for(int j=0;j<v.length;j++){
				System.out.println(v[j].getClass());
				if(v[j].getClass().toString().endsWith("com.silverbrain.core.Variable")) {
						variable = (Variable)v[j];
					if(variable.dataType == DataType.INTEGER && j==0){
						value = (int)variable.values.get(i);
					}else{
						value = (int)value + (int)variable.values.get(i);
					}
					
				}
			}
			
			System.out.println("value: " + value);
		}
	}
	
	/********************* FLVariable class ***********************/
	private static class FLVariable {

		private String name;
		private HSList<Boolean> values;
		private StorageType storageType;
		
		private FLVariable(){}
	
		private FLVariable(String name, HSList<Boolean> values) {
			this(name, values, StorageType.MEMORY);
		}
		private FLVariable(String name, HSList<Boolean> values, StorageType storageType) {
			this.name = name;
			this.values = values;
			this.storageType = storageType;
		}
	
		private void fillFalse(){
			for(int i=0;i<values.size();i++){
				values.set(i, false);
			}
		}
		
		private void fillFalse(int size) {
			values.clear();
			for(int i=0;i<size;i++){
				values.add(false);
			}
		}
	
		private void fillNull(){
			for(int i=0;i<values.size();i++){
				values.set(i, null);
			}
		}
		
		private void fillNull(int size){
			values.clear();
			for(int i=0;i<size;i++){
				values.add(null);
			}
		}
		
		private FLVariable getCopy() {
			
			HSList<Boolean> newValues;
			
			if(storageType == StorageType.DISK)
				newValues = new DList<Boolean>();
			else
				newValues = new MList<Boolean>();
			 
			for(int i=0;i<this.values.size();i++) {
				newValues.add(this.values.get(i));
			}
			
			FLVariable flVariable = null;
		

			flVariable = new FLVariable(this.name, newValues, storageType);

			
			return flVariable;
		}//end of getCopy() method
	
	}//end of FLVariable class
	
	/************ PIVOT = Vertical to horizontal structure ********************/
	public IDataSet pivot() {
		return null;
	}

	/********* UNPIVOT = Horizontal to vertical structure **********************/
	public IDataSet unpivot(String variableNames, String categoryNames) {
		return unpivot(variableNames, categoryNames, HS.defaultOut);
	}
	
	public IDataSet unpivot(String variableNames, String categoryNames, boolean out) {
		
		String a_variableNames[] = variableNames.split(" ");
		String a_categoryNames[] = categoryNames.split(" ");
		
		DataSet tempDataSet = this.getCopy();
		
		DataSet dataSet = null;
		
		if(out)
			dataSet = this.getCopy();
		else
			dataSet = this;
		
		VariableFactory variableFactory = null;
		
		if(dataSet.storageType == StorageType.DISK)
			variableFactory = new VariableFactory(StorageType.DISK);
		else
			variableFactory = new VariableFactory();
		
		dataSet.keepVariable(variableNames);
		dataSet.removeAllObservations(false);
		dataSet.addVariable(variableFactory.createEmptyVariable("_CATEGORY_", DataType.STRING));
		dataSet.addVariable(variableFactory.createEmptyVariable("_VALUE_", DataType.STRING));
		
		Map<String, Object> observationMap = null;
		Variable variable = null;
		Object value = null;
		for(int i=0;i<tempDataSet.getObservationCount();i++) {
			observationMap = new HashMap<String, Object>();
			for(int k=0;k<a_variableNames.length;k++){
				variable = tempDataSet.getVariableByName(a_variableNames[k]);
				value = variable.values.get(tempDataSet.sortIndex.get(i));
				observationMap.put(variable.name, value);
			}
			for(int k=0;k<a_categoryNames.length;k++){
				variable = tempDataSet.getVariableByName(a_categoryNames[k]);
				value = variable.values.get(tempDataSet.sortIndex.get(i));
				observationMap.put("_CATEGORY_", variable.name);
				observationMap.put("_VALUE_", value);
				dataSet.addObservation(observationMap);		
			}	
		}
		
		return dataSet;
	}
	
	
	
	
	


	
	/*************************************************************************************/
	/**************** Exceptions 
	/*************************************************************************************/
	void throwExceptionIfDataTypeInvalid(DataType dataType) throws IllegalArgumentException {
		String msg = null;
		if(!(dataType instanceof DataType)) { 
			msg = "[{" + dataType + "} variable data type is invalid]";
			throw new IllegalArgumentException(msg);
		}
	}
	
	
	void throwExceptionIfIndexOutOfBounds(int index, int min, int max) throws IndexOutOfBoundsException {
		if(index < min || index > max) {
			logMessage = String.format("<%s> range is %s to %s, supplied value is %s", getName(), min, max, index);
			HS.writeLog(LogMessageType.ERROR, logMessage);
			throw new IndexOutOfBoundsException(logMessage);
		}

	}
	
	void throwExceptionIfDataSetNull(IDataSet dataSet) {
		if(Objects.isNull(dataSet)) {
			logMessage = String.format("DataSet is null");
			HS.writeLog(LogMessageType.ERROR, logMessage);
			throw new IllegalArgumentException(logMessage);
		}
	}
	
	void throwExceptionIfKeyEmpty(String key) {
		if(key.trim().isEmpty()){
			logMessage = String.format("[%s] {%s} key is empty", getName(), key);
			HS.writeLog(LogMessageType.ERROR, logMessage);
			throw new IllegalArgumentException(logMessage);	
		}
	}
	
	
	void throwExceptionIfKeyInvalid(String key) {
		if(HSUtils.isInvalidVariableName(key.trim())){
			logMessage = String.format("[%s] {%s} key is invalid", getName(), key);
			HS.writeLog(LogMessageType.ERROR, logMessage);
			throw new IllegalArgumentException(logMessage);	
		}
	}
	
	void throwExceptionIfKeyNotPresent(String key) throws IllegalArgumentException {
		if(getVariableByName(key) == null){
			logMessage = String.format("[%s] {%s} key not present in the table", getName(), key);
			HS.writeLog(LogMessageType.ERROR, logMessage);
			throw new IllegalArgumentException(logMessage);	
		}
	}
	
	
	void throwExceptionIfVariableNameEmpty(String variableName) throws IllegalArgumentException {
		if(variableName.trim().isEmpty()){
			logMessage = String.format("[%s] {%s} variable name empty", getName(), variableName);
			HS.writeLog(LogMessageType.ERROR, logMessage);
			throw new IllegalArgumentException(logMessage);	
		}
	}
	
	void throwExceptionIfVariableNameInvalid(String variableName) throws IllegalArgumentException {
		if(HSUtils.isInvalidVariableName(variableName.trim())) {
			logMessage = String.format("[%s] {%s} variable name is invalid", getName(), variableName);
			HS.writeLog(LogMessageType.ERROR, logMessage);
			throw new IllegalArgumentException(logMessage);	
		}
	}
	
	void throwExceptionIfVariableNotPresent(String variableName) throws IllegalArgumentException {
		if(hasVariable(variableName.trim()) == false) {
			logMessage = String.format("[%s] {%s} variable not present in the table", getName(), variableName);
			HS.writeLog(LogMessageType.ERROR, logMessage);
			throw new IllegalArgumentException(logMessage);	
		}
	}
	
	void throwExceptionIfVariableAlreadyPresent(String variableName) throws IllegalArgumentException {
		if(hasVariable(variableName.trim()) == true) {
			logMessage = String.format("[%s] {%s} variable already present in the table", getName(), variableName);
			HS.writeLog(LogMessageType.ERROR, logMessage);
			throw new IllegalArgumentException(logMessage);	
		}
	}
	
	void throwExceptionIfVariableNameNull(String variableName) throws IllegalArgumentException {
		if(Objects.isNull(variableName)) {
			logMessage = String.format("[%s] {%s} variable name null", getName(), variableName);
			HS.writeLog(LogMessageType.ERROR, logMessage);
			throw new IllegalArgumentException(logMessage);	
		}
	}
	
}//end of DataSet class