package com.silverbrain.hs.core;

import java.util.*;





public class Prog {
	
	//public static MeanParameterBuilder meanParameterBuilder(DataSet dataSet, String columnNames) {
		//return new MeanParameterBuilder(dataSet, columnNames);
	//}

	public static DataSet means(DataSet dataSet, String columnNames) {
		
		//String columnNames = meanParameter.columnNames;
		//DataSet dataSet = meanParameter.dataSet;
		
		String a_columnNames[] = columnNames.split(" ");
		 
			int nL;
			double sumL;
			double maxL;
			double minL;
			double value;
			double meanL;
			double stdL;
			int n[] = new int[a_columnNames.length];
			double sum[] = new double[a_columnNames.length];
			double mean[] = new double[a_columnNames.length];
			double max[] = new double[a_columnNames.length];
			double min[] = new double[a_columnNames.length];
			double std[] = new double[a_columnNames.length];
			ArrayList<Double> values = new ArrayList<Double>();
			Variable column;
			for(int i=0;i<a_columnNames.length;i++) {
				column = dataSet.getVariableByName(a_columnNames[i]);
				nL = 0;
				sumL = 0;
				minL = Double.parseDouble(column.values.get(0).toString());
				maxL = Double.parseDouble(column.values.get(0).toString());
				value = 0;
				meanL = 0;
				values.clear();
				for(int j=0;j<column.values.size();j++){
					if(column.values.get(j) != null) {
						nL += 1;
						if(column.dataType == DataType.INTEGER) {
							value = Double.parseDouble(column.values.get(j).toString());
							sumL += value;
							if(minL > value)
								minL = value;
							if(maxL < value)
								maxL = value;
						}
						else if(column.dataType == DataType.DOUBLE) {
							value =  (Double)column.values.get(j);
							sumL += value;
							if(minL > value)
								minL = value;
							if(maxL < value)
								maxL = value;
						}
						values.add(value);
					}
				}//end of j
				double s = 0;
				for(int j=0;j<values.size();j++){
					s = s + Math.pow(values.get(j) - (sumL/nL), 2);
				}
				stdL = Math.sqrt(s/nL);
				
				n[i] = nL;
				sum[i] = sumL;
				mean[i] = sumL/nL;
				max[i] = maxL;
				min[i] = minL;
				std[i] = stdL;
			}//end of i
		
		ArrayList<Integer> nValues = new ArrayList<Integer>();
		ArrayList<String> columnNameValues = new ArrayList<String>();
		ArrayList<Double> meanValues = new ArrayList<Double>();
		ArrayList<Double> maxValues = new ArrayList<Double>();
		ArrayList<Double> minValues = new ArrayList<Double>();
		ArrayList<Double> stdValues = new ArrayList<Double>();
		
		for(int i=0;i<a_columnNames.length;i++){
			columnNameValues.add(a_columnNames[i]);
			nValues.add(n[i]);
			meanValues.add(mean[i]);
			minValues.add(min[i]);
			maxValues.add(max[i]);
			stdValues.add(std[i]);
		}
		
		ArrayList<Variable> newVariables = new ArrayList<Variable>();
		//newVariables.add(DataSet.createVariable("Variable", DataType.STRING, columnNameValues));
		//newVariables.add(DataSet.createVariable("N", DataType.INTEGER, nValues));
		//newVariables.add(DataSet.createVariable("Mean", DataType.DOUBLE, meanValues));
		//newVariables.add(DataSet.createVariable("Minimum", DataType.DOUBLE, minValues));
		//newVariables.add(DataSet.createVariable("Maximum", DataType.DOUBLE, maxValues));
		//newVariables.add(DataSet.createVariable("Std_Dev", DataType.DOUBLE, stdValues));
	
		DataSet newDataSet = new DataSet(newVariables);
		
		return newDataSet;
	}
	
	public void report(DataSet dataSet) {
	}
	
	public void freq(DataSet dataSet) {
	}
	
	/*
	static class MeanParameter {
	
	DataSet dataSet;
	String columnNames;
	String xlassD;
	String byD;
	
	private MeanParameter(MeanParameterBuilder meanParameterBuilder) {
		dataSet = meanParameterBuilder.dataSet;
		columnNames = meanParameterBuilder.columnNames;
		xlassD = meanParameterBuilder.xlassD;
		byD = meanParameterBuilder.byD;
	}
	
	public String toString() {
		String data = "dataSet" + dataSet;
		return data;
	}
}

static class MeanParameterBuilder {

	DataSet dataSet;
	String columnNames;
	String xlassD;
	String byD;

	
	public MeanParameterBuilder(DataSet dataSet, String columnNames) {
		this.dataSet = dataSet;
		this.columnNames = columnNames;
	}
	
	public void xlass(String xlassD) {
		this.xlassD = xlassD;
	}
	
	public void by(String byD) {
		this.byD = byD;
	}

	
	public MeanParameter build() {
		return new MeanParameter(this);
	}
}
*/
}