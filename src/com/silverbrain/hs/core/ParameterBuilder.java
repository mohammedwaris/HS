package com.silverbrain.hs.core;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.BiConsumer;

public class ParameterBuilder {

	public static class ImportCSVParameter {
		
		String _filePath;
		char _separator;
		boolean _header;
		StorageType _storageType;
		Map<String, Informat> _parser;
		ArrayList<Consumer<Double>> _progressListeners;
		
		
		public ImportCSVParameter(String _filePath) {
			this._filePath = _filePath;
			this._separator = ',';
			this._header = true;
			this._parser = null;
			this._storageType = StorageType.MEMORY;
			this._progressListeners = new ArrayList<Consumer<Double>>();
		}
		
		public String getFilePath() {
			return this._filePath;
		}
		
		public ImportCSVParameter separator(char _separator) {
			this._separator = _separator;
			return this;
		}
		
		public char getSeparator() {
			return this._separator;
		}
		
		public ImportCSVParameter header(boolean _header) {
			this._header = _header;
			return this;
		}
		public boolean getHeader() {
			return this._header;
		}
		
		public ImportCSVParameter parser(Map<String, Informat> _parser) {
			this._parser = _parser;
			return this;
		}
		
		public Map<String, Informat> getParser() {
			return this._parser;
		}
		
		public ImportCSVParameter storageType(StorageType storageType) {
			this._storageType = storageType;
			return this;
		}
		
		public StorageType getStorageType() {
			return this._storageType;
		}
		
		public ImportCSVParameter progressListener(Consumer<Double> _progressListener) {
			_progressListeners.add(_progressListener);
			return this;
		}
		
		public ArrayList<Consumer<Double>> getProgressListeners() {
			return this._progressListeners;
		}
		

	}
	
	public static class ExportCSVParameter {
		
		String _filePath;
		char _separator;
		
		public ExportCSVParameter(String _filePath) {
			this._filePath = _filePath;
			this._separator = ',';
			//this._hasHeader = true;
			//this._parser = null;
		}
		
		public ExportCSVParameter separator(char _separator) {
			this._separator = _separator;
			return this;
		}
	}
	
	public static class ImportJSONParameter {
		String _filePath;
		StorageType _storageType;
		
		public ImportJSONParameter(String _filePath) {
			this._filePath = _filePath;
			this._storageType = StorageType.MEMORY;
		}
		
		public ImportJSONParameter storageType(StorageType _storageType) {
			this._storageType = _storageType;
			return this;
		}
		
		public String getFilePath() {
			return this._filePath;
		}
		
		public StorageType getStorageType() {
			return this._storageType;
		}
	}
	
	public static class ImportSASParameter {
		String _filePath;
		StorageType _storageType;
		Map<String, Informat> _parser;
		
		public ImportSASParameter(String _filePath) {
			this._filePath = _filePath;
			this._storageType = StorageType.MEMORY;
		}
		
		public ImportSASParameter parser(Map<String, Informat> _parser) {
			this._parser = _parser;
			return this;
		}
		
		public ImportSASParameter storageType(StorageType _storageType) {
			this._storageType = _storageType;
			return this;
		}
		
		public String getFilePath() {
			return this._filePath;
		}
		
		public StorageType getStorageType() {
			return this._storageType;
		}
		
		public Map<String, Informat> getParser() {
			return this._parser;
		}
	}
	
	public static class SumParameter {
		
		String _variableName;
		String _groupBy;
		String _sumVariableName;
		
		public SumParameter(String _variableName) {
			this._variableName = _variableName;
			this._groupBy = null;
			this._sumVariableName = null;
		}
		
		public String getVariableName() {
			return this._variableName;
		}
		
		public SumParameter groupBy(String _groupBy) {
			this._groupBy = _groupBy;
			return this;
		}
		
		public String getGroupBy() {
			return this._groupBy;
		}
		
		public SumParameter sumVariableName(String _sumVariableName) {
			this._sumVariableName = _sumVariableName;
			return this;
		}
		
		public String getSumVariableName() {
			return this._sumVariableName;
		}
		
	}
	
	public static class AvgParameter {
		
		String _variableName;
		String _groupBy;
		String _avgVariableName;
		boolean _includeNull;
		
		public AvgParameter(String _variableName) {
			this._variableName = _variableName;
			this._groupBy = null;
			this._avgVariableName = null;
			this._includeNull = false;
		}
		
		public String getVariableName() {
			return this._variableName;
		}
		
		public AvgParameter groupBy(String _groupBy) {
			this._groupBy = _groupBy;
			return this;
		}
		
		public String getGroupBy() {
			return this._groupBy;
		}
		
		public AvgParameter avgVariableName(String _avgVariableName) {
			this._avgVariableName = _avgVariableName;
			return this;
		}
		
		public String getAvgVariableName() {
			return this._avgVariableName;
		}
		
		public AvgParameter includeNull(boolean _includeNull) {
			this._includeNull = _includeNull;
			return this;
		}
		
		public boolean getIncludeNull() {
			return this._includeNull;
		}
		
	}//end of AvgParameter class
	
	public static class CountParameter {
		
		String _groupBy;
		String _countVariableName;
		
		public CountParameter(String _groupBy) {
			this._groupBy = _groupBy;
			this._countVariableName = null;
		}
		
		public String getGroupBy() {
			return this._groupBy;
		}
		
		public CountParameter countVariableName(String _countVariableName) {
			this._countVariableName = _countVariableName;
			return this;
		}
		
		public String getCountVariableName() {
			return this._countVariableName;
		}
	}//end of CountParamter class
	
	public static class MinParameter {
		
		String _variableName;
		String _groupBy;
		String _minVariableName;
		
		public MinParameter(String _variableName) {
			this._variableName = _variableName;
			this._groupBy = null;
			this._minVariableName = null;
		}
		
		public String getVariableName() {
			return this._variableName;
		}
		
		public MinParameter groupBy(String _groupBy) {
			this._groupBy = _groupBy;
			return this;
		}
		
		public String getGroupBy() {
			return this._groupBy;
		}
		
		public MinParameter minVariableName(String _minVariableName) {
			this._minVariableName = _minVariableName;
			return this;
		}
		
		public String getMinVariableName() {
			return this._minVariableName;
		}
		
	}//end of MinParameter class
	
	public static class MaxParameter {
		
		String _variableName;
		String _groupBy;
		String _maxVariableName;
		
		public MaxParameter(String _variableName) {
			this._variableName = _variableName;
			this._groupBy = null;
			this._maxVariableName = null;
		}
		
		public String getVariableName() {
			return this._variableName;
		}
		
		public MaxParameter groupBy(String _groupBy) {
			this._groupBy = _groupBy;
			return this;
		}
		
		public String getGroupBy() {
			return this._groupBy;
		}
		
		public MaxParameter maxVariableName(String _minVariableName) {
			this._maxVariableName = _maxVariableName;
			return this;
		}
		
		public String getMaxVariableName() {
			return this._maxVariableName;
		}
		
	}//end of MaxParameter class
	
	public static class AggregateParameter {
		
		String _variableName;
		String _groupBy;
		boolean _sum;
		boolean _avg;
		boolean _count;
		boolean _max;
		boolean _min;
		boolean _includeNull;
		String _sumVariableName;
		String _countVariableName;
		String _maxVariableName;
		String _minVariableName;
		String _avgVariableName;
		
		public AggregateParameter(String _variableName) {
			this._variableName = _variableName;
			this._groupBy = null;
			this._sum = true;
			this._count = true;
			this._max = true;
			this._min = true;
			this._avg = true;
			this._includeNull = false;
			this._sumVariableName = null;
			this._avgVariableName = null;
			this._maxVariableName = null;
			this._minVariableName = null;
			this._countVariableName = null;
			
		}
		
		public String getVariableName() {
			return this._variableName;
		}
		
		public AggregateParameter groupBy(String _groupBy) {
			this._groupBy = _groupBy;
			return this;
		}
		
		public String getGroupBy() {
			return this._groupBy;
		}
		
		public AggregateParameter sum(boolean _sum) {
			this._sum = _sum;
			return this;
		}
		
		public boolean getSum() {
			return this._sum;
		}
		
		public AggregateParameter count(boolean _count) {
			this._count = _count;
			return this;
		}
		
		public boolean getCount() {
			return this._count;
		}
		
		public AggregateParameter max(boolean _max) {
			this._max = _max;
			return this;
		}
		
		public boolean getMax() {
			return this._max;
		}
		
		public AggregateParameter min(boolean _min) {
			this._min = _min;
			return this;
		}
		
		public boolean getMin() {
			return this._min;
		}
		
		public AggregateParameter avg(boolean _avg) {
			return avg(_avg, false);
		}
		
		public AggregateParameter avg(boolean _avg, boolean includeNull) {
			this._avg = _avg;
			this._includeNull = _includeNull;
			return this;
		}
		
		public boolean getAvg() {
			return this._avg;
		}
		
		public boolean getIncludeNull() {
			return this._includeNull;
		}
		
		public AggregateParameter sumVariableName(String _sumVariableName) {
			this._sumVariableName = _sumVariableName;
			return this;
		}
		
		public String getSumVariableName() {
			return this._sumVariableName;
		}
		
		public AggregateParameter countVariableName(String _countVariableName) {
			this._countVariableName = _countVariableName;
			return this;
		}
		
		public String getCountVariableName() {
			return this._countVariableName;
		}
		
		public AggregateParameter avgVariableName(String _avgVariableName) {
			this._avgVariableName = _avgVariableName;
			return this;
		}
		
		public String getAvgVariableName() {
			return this._avgVariableName;
		}
		
		public AggregateParameter maxVariableName(String _maxVariableName) {
			this._maxVariableName = _maxVariableName;
			return this;
		}
		
		public String getMaxVariableName() {
			return this._maxVariableName;
		}
		
		public AggregateParameter minVariableName(String _minVariableName) {
			this._minVariableName = _minVariableName;
			return this;
		}
		
		public String getMinVariableName() {
			return this._minVariableName;
		}
		
		
	}//end of AggregateParameter class
	
	
	public static class ImportExcelParameter {
		
		String _filePath;
		ArrayList<ImportSheetInfo> sheetsInfo;
		StorageType _storageType;
		
		public ImportExcelParameter(String _filePath) {
			this._filePath = _filePath;
			this.sheetsInfo = null;
			this._storageType = StorageType.MEMORY;
		}
		
		public String getFilePath() {
			return this._filePath;
		}
		
		public ImportExcelParameter sheetInfo(String sheetName) {
			ImportSheetInfo sheetInfo = new ImportSheetInfo(sheetName, true, 0);
			if(sheetsInfo == null)
				sheetsInfo = new ArrayList<ImportSheetInfo>();
			sheetsInfo.add(sheetInfo);
			return this;
		}
		
		public ImportExcelParameter sheetInfo(String sheetName, boolean hasHeader) {
			ImportSheetInfo sheetInfo = new ImportSheetInfo(sheetName, hasHeader, 0);
			if(sheetsInfo == null)
				sheetsInfo = new ArrayList<ImportSheetInfo>();
			sheetsInfo.add(sheetInfo);
			return this;
		}
		
		public ImportExcelParameter sheetInfo(String sheetName, boolean hasHeader, int startFrom) {
			ImportSheetInfo sheetInfo = new ImportSheetInfo(sheetName, hasHeader, startFrom);
			if(sheetsInfo == null)
				sheetsInfo = new ArrayList<ImportSheetInfo>();
			sheetsInfo.add(sheetInfo);
			return this;
		}
		
		public ArrayList<ImportSheetInfo> getSheetsInfo() {
			return this.sheetsInfo;
		}
		
		public ImportExcelParameter storageType(StorageType _storageType) {
			this._storageType = _storageType;
			return this;
		}
		
		public StorageType getStorageType() {
			return this._storageType;
		}
		
		public static class ImportSheetInfo {
			String sheetName;
			boolean hasHeader;
			int startFrom;
			
			public ImportSheetInfo(String sheetName, boolean hasHeader, int startFrom) {
				this.sheetName = sheetName;
				this.hasHeader = hasHeader;
				this.startFrom = startFrom;
			}
			
			public String getSheetName() {
				return this.sheetName;
			}
			
			public boolean getHasHeader() {
				return this.hasHeader;
			}
			
			public int getStartFrom() {
				return this.startFrom;
			}
			
		}
		
	}
	
	
}//end of ParameterBuilder class