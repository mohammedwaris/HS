package com.silverbrain.hs.core;

import java.time.*;
import java.time.format.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.util.Iterator;

import com.opencsv.*;
import com.epam.parso.*;
import com.epam.parso.impl.*;

import java.util.function.Consumer;
import java.util.function.BiConsumer;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.JsonToken;




import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.CellType;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.silverbrain.hs.core.io.IHSExcelReader;
import com.silverbrain.hs.core.io.HSExcelReader;

public class HS {

	{
		defaultOut = true;
		log = new Log();
	}
	
	static boolean defaultOut;
	static Log log;
	
	static ArrayList<IDataSet> dataSetListForTableView;
	
	
	private static String logMessage;
	
	long serialVersionUID = 313177427174559407L;
	
	public static boolean getDefaultOut() {
		return defaultOut;
	}
	
	public static void setDefaultOut(boolean dfo) {
		defaultOut = dfo;
	}
	
	public static IDataSet readDataSet(String filePath)  {
		return readDataSet(filePath, false);
	}
	
	public static IDataSet readDataSet(String filePath, boolean isCompressed)  {
		
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		GZIPInputStream gis = null;
		
		IDataSet dataSet = null;
		
		try {
			fis = new FileInputStream(new File(filePath));
		
			if(isCompressed == true){
				gis = new GZIPInputStream(fis);
				ois = new ObjectInputStream(gis);
			}else{
				ois = new ObjectInputStream(fis);
			}

			// Read datatable
			dataSet = (DataSet) ois.readObject();

			if(isCompressed == true){
				ois.close();
				gis.close();
			}else{
				ois.close();
			}

			fis.close();
			
		}catch(FileNotFoundException fe){
			logMessage = "file not found";
			//logMessage = "cannot read [" + filePath + "] - invalid DataSet format";
			writeLog(LogMessageType.ERROR, logMessage);
		
		}catch(ClassNotFoundException cnfe) {
			logMessage = "class not found";
			//logMessage = "cannot read [" + filePath + "] - invalid DataSet format";
			writeLog(LogMessageType.ERROR, logMessage);
		}catch(StreamCorruptedException sce) {
			logMessage = "cannot read [" + filePath + "] - invalid DataSet format";
			writeLog(LogMessageType.ERROR, logMessage);
		}catch(ClassCastException ie){
			//ie.printStackTrace();
			logMessage = "Class Cast error";
			//logMessage = "cannot read [" + filePath + "] - invalid DataSet format";
			writeLog(LogMessageType.ERROR, logMessage);
		}catch(IOException ie){
			ie.printStackTrace();
			logMessage = "IO error";
			//logMessage = "cannot read [" + filePath + "] - invalid DataSet format";
			writeLog(LogMessageType.ERROR, logMessage);
		}
			
		return dataSet;
	}//end of readDataSet()
	
	
	//public static DataSet importCSV(ParameterBuilder.ImportCSVParameter params) {
		//return importCSV(filePath, ',', true, null);
	//}

	public static IDataSet importCSV(ParameterBuilder.ImportCSVParameter params)  {

		String filePath = params.getFilePath();
		char separator = params.getSeparator();
		boolean hasHeader = params.getHeader();
		StorageType storageType = params.getStorageType();
		VariableFactory variableFactory = HS.getVariableFactory(storageType);
		ArrayList<Consumer<Double>> progressListeners = params.getProgressListeners();
		
		
		//long bytesRead = 0;
		//double progress = 0;
		
		Map<String, Informat> cparser = params.getParser();

		Timer timer = new Timer();
		timer.start();
		
		File file = new File(filePath);
		//long totalBytes = file.length();
		
		FileReader filereader = null;
		
		try {
			filereader = new FileReader(file);
		}catch(FileNotFoundException fe) {
			HS.writeStackTraceToLog(fe);
			return HSUtils.createEmptyDataSet();
		}

		CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();
		CSVReader csvReader = new CSVReaderBuilder(filereader).withCSVParser(parser).build();

		String[] line;
		ArrayList<Variable> newVariables = new ArrayList<Variable>();
		int ctr = 0;
		Informat informat = null;
		Variable variable = null;
		String name = null;
		try {
			while ((line = csvReader.readNext()) != null) {
				if(hasHeader == true && ctr == 0) {
					for(int i=0;i<line.length;i++) {
						informat = null;
						name = HSUtils.getValidVariableName(line[i].trim());
						if(cparser != null)
							informat = HSUtils.getInformatByVariableNameFromParser(name, cparser);
						if(informat != null) {
							if(informat != Informat.AUTO)
								newVariables.add(variableFactory.createEmptyVariable(name, HSUtils.getDataTypeByInformat(informat)));
							else
								newVariables.add(variableFactory.createEmptyVariable(name, DataType.STRING));
						}
						else
							newVariables.add(variableFactory.createEmptyVariable(name, DataType.STRING));
					}
				}else if(hasHeader ==  false && ctr == 0) {
					for(int i=0;i<line.length;i++) {
						informat = null;
						if(cparser != null)
							informat = HSUtils.getInformatByVariableNameFromParser("Variable_" + i, cparser);
						if(informat != null)
							newVariables.add(variableFactory.createEmptyVariable("Variable_" + i, HSUtils.getDataTypeByInformat(informat)));
						else
							newVariables.add(variableFactory.createEmptyVariable("Variable_" + i, DataType.STRING));
					}

				}
				if((hasHeader == true && ctr > 0) || (hasHeader == false && ctr >= 0)) {
					for(int i=0;i<newVariables.size();i++) {
						informat = null;
						variable = newVariables.get(i);
						if(cparser != null)
							informat = HSUtils.getInformatByVariableNameFromParser(newVariables.get(i).name, cparser);
						if(line[i].isEmpty()) 
							line[i] = null;
					
						if(informat != null && line[i] != null) {
							if(informat != Informat.AUTO)
								variable.values.add(HSUtils.convertValue(line[i].trim(), DataType.STRING, informat));
							else if(informat == Informat.AUTO){
								/*Informat inf = HSUtils.detectInformat(line[i]);
								System.out.println("inf: " + inf);
								variable.values.add(HSUtils.convertValue(line[i], DataType.STRING, inf));
								variable.dataType = HSUtils.getDataTypeByInformat(inf);*/
							}	
						}
						else {
							try {
							
								variable.values.add(line[i]);
							}catch(Exception ex) {
								//System.out.println(line[i]);
								//ex.printStackTrace();
							}
						}
					}//end of for
				}//end of if

				ctr += 1;
				
				
				//bytesRead += HSUtils.getObjectSizeInBytes(line);
				//System.out.println(csvReader.getLinesRead() + " " + bytesRead + "/" + totalBytes);
				//progress = (double)((bytesRead*100.00)/totalBytes);
				
				
				
						//for(int p=0;p<progressListeners.size();p++){
							//Consumer<Double> consumer = progressListeners.get(p);
							//consumer.accept(progress);
						//}
	
			}//end of while
		
			/*
			if(cparser == null){
				for(int i=0;i<newVariables.size();i++){
					variable = newVariables.get(i);
					Informat inferInformat = HSUtils.detectInformat(variable.values);
					//System.out.println(inferInformat);
					DataType dataType = HSUtils.getDataTypeByInformat(inferInformat);
					
					if(dataType != variable.dataType) {
						variable.convertValues(inferInformat);
						variable.dataType = dataType;
					}
					
				}
			}else{
			}*/
		}catch(IOException ie) {
			HS.writeStackTraceToLog(ie);
			return HSUtils.createEmptyDataSet();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
    	DataSet dataSet = new DataSet(storageType, newVariables);
		timer.stop();
		
		
		String logMessage = String.format("<%s> importCSV(\"%s\", '%s', %s, %s) execution took %s sec(s)", dataSet.getName(), filePath, separator, hasHeader, cparser, timer.timeElapsed());
		HS.writeLog(LogMessageType.NOTE, logMessage);
		
		
		
		return dataSet;
	}//end of importCSV() method

	public static void exportCSV(DataSet dataSet, ParameterBuilder.ExportCSVParameter params) {


		File file = null;
		FileWriter fileWriter = null;
		// writer = null;

		try {
			file = new File(params._filePath);
			fileWriter = new FileWriter(file);
			final CSVWriter writer = new CSVWriter(fileWriter, params._separator, 
                                         CSVWriter.NO_QUOTE_CHARACTER, 
                                         CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
                                         CSVWriter.DEFAULT_LINE_END); 
								
			Object objs[] = dataSet.getVariableNames().toArray();
			String headers[] = new String[objs.length];
			for(int i=0;i<objs.length;i++)
				headers[i] = (String)objs[i];
			
			writer.writeNext(headers);
			
			dataSet.forEachObservation(observation -> {
				String values[] = new String[observation.getSize()];
				for(int i=0;i<observation.getSize();i++){
					if(observation.getValue(i) != null)
						values[i] = observation.getValue(i).toString();
					else
						values[i] = "";
				}
				writer.writeNext(values);
			});
			
			fileWriter.close();
		}catch(IOException ex) {
			HS.writeStackTraceToLog(ex);
		}catch(Exception ex) {
			HS.writeStackTraceToLog(ex);
		}
	}
	
	
	
	//public static DataSet importSAS(String filePath) {
		//return readSAS(filePath, null);
	//}
	
	public static IDataSet importSAS(ParameterBuilder.ImportSASParameter params) {
		
		Timer timer = new Timer();
		timer.start();
		
		String filePath = params.getFilePath();
		Map<String, Informat> parser = params.getParser();
		StorageType storageType = params.getStorageType();
		VariableFactory variableFactory = new VariableFactory(storageType);
		
		//writeLog(System.lineSeparator());
		String logMessage = String.format("readSAS(\"%s\", %s) execution started", filePath, parser);
		writeLog(LogMessageType.INFO, logMessage);
		
		DataSet dataSet = null;
		
		
		try {
			FileInputStream fis = new FileInputStream(filePath);
			SasFileReader sasFileReader = new SasFileReaderImpl(fis);
		
			ArrayList<Variable> newVariables = new ArrayList<Variable>();
			Variable newVariable = null;
			DataType dataType = DataType.STRING;
			com.epam.parso.ColumnFormat format;
			String formatName;
			Informat informat;
			for(com.epam.parso.Column sasVariable:sasFileReader.getColumns()) {
				if(parser != null) {
					informat = HSUtils.getInformatByVariableNameFromParser(sasVariable.getName(), parser);
					if(informat != null){
						dataType = HSUtils.getDataTypeByInformat(informat);
					}else{
						if(sasVariable.getType() == Number.class){
							//if(sasVariable.getType() == Double.class)
								//System.out.println(true);
							//else 
								//System.out.println(false);  //sasVariable.getType() == Integer.class + " " + sasVariable.getType() == Long.class + " " + sasVariable.getType() == Double.class);
							format = sasVariable.getFormat();
							formatName = format.getName();
							if(formatName.equalsIgnoreCase("BEST")){
								if(format.getPrecision() > 0)
									dataType = DataType.DOUBLE;
								else
									dataType = DataType.LONG;
							}else if(formatName.equalsIgnoreCase("DATETIME"))
								dataType = DataType.LOCAL_DATE_TIME;
							else if(formatName.equalsIgnoreCase("TIME"))
								dataType = DataType.LOCAL_TIME;
							else if(formatName.equalsIgnoreCase("DATE") || formatName.equalsIgnoreCase("YYMMDD") || formatName.equalsIgnoreCase("DDMMYY") || formatName.equalsIgnoreCase("MMDDYY"))
								dataType = DataType.LOCAL_DATE;
							else {
								//if(format.getPrecision() > 0)
									dataType = DataType.DOUBLE;
								//else
									//dataType = DataType.INTEGER;
							}
						}else{
							dataType = DataType.STRING;
						}
					}
				}else {
					if(sasVariable.getType() == Number.class) {
						format = sasVariable.getFormat();
						formatName = format.getName();
						if(formatName.equalsIgnoreCase("BEST")){
							if(format.getPrecision() > 0)
								dataType = DataType.DOUBLE;
							else
								dataType = DataType.LONG;
						}else if(formatName.equalsIgnoreCase("DATETIME"))
							dataType = DataType.LOCAL_DATE_TIME;
						else if(formatName.equalsIgnoreCase("DATE") || formatName.equalsIgnoreCase("YYMMDD") || formatName.equalsIgnoreCase("DDMMYY") || formatName.equalsIgnoreCase("MMDDYY"))
							dataType = DataType.LOCAL_DATE;
						else {
							//if(format.getPrecision() > 0)
								dataType = DataType.DOUBLE;
							//else
								//dataType = DataType.INTEGER;
						}
					}else{
						dataType = DataType.STRING;
					}
				}
				newVariable = variableFactory.createEmptyVariable(sasVariable.getName(), dataType);
				newVariable.label = sasVariable.getLabel();
				newVariables.add(newVariable);
				/*System.out.println(sasVariable.getName() + " " + 
								sasVariable.getType() + " " + 
								sasVariable.getFormat() + " " + 
								sasVariable.getFormat().isEmpty() + " " + 
								sasVariable.getFormat().getName() + " " + 
								sasVariable.getFormat().getWidth() + " " + 
								sasVariable.getFormat().getPrecision());*/
			}//end of for(com.epam.parso.Variable sasVariable:sasFileReader.getVariables())
		
		
			Object values[];
			LocalDateTime localDateTime;
			LocalDate localDate;
			LocalTime localTime;
			Date date;
			while((values = sasFileReader.readNext()) != null) {
				for(int i=0;i<values.length;i++) {
					dataType = newVariables.get(i).dataType;
				
					if(Objects.nonNull(values[i])) {
						if(dataType == DataType.INTEGER){
							newVariables.get(i).values.add(Integer.parseInt(values[i].toString()));
						}else if(dataType == DataType.LONG){
							newVariables.get(i).values.add(Long.parseLong(values[i].toString()));
						}else if(dataType == DataType.DOUBLE){
							newVariables.get(i).values.add(Double.parseDouble(values[i].toString()));
						}else if(dataType == DataType.STRING){
							newVariables.get(i).values.add(values[i].toString());
						}else if(dataType == DataType.LOCAL_DATE){
							date = (Date)values[i];
							localDate = date.toInstant().atZone(ZoneId.of("GMT")).toLocalDate();
							newVariables.get(i).values.add(localDate);
						}else if(dataType == DataType.LOCAL_TIME){
							//date = (Date)values[i];
							localTime = Instant.ofEpochMilli((long)values[i]*1000).atZone(ZoneId.of("UTC")).toLocalTime();
							//localTime = date.toInstant().atZone(ZoneId.of("GMT")).toLocalTime();
							newVariables.get(i).values.add(localTime);
						}else if(dataType == DataType.LOCAL_DATE_TIME){
							date = (Date)values[i];
							localDateTime = date.toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime();
							newVariables.get(i).values.add(localDateTime);
						}else{
							newVariables.get(i).values.add(values[i]);
						}
					}else{
						newVariables.get(i).values.add(values[i]);
					}
				}
			}
		
			dataSet = new DataSet(newVariables);
		
			timer.stop();
			logMessage = String.format("readSAS(\"%s\", %s) took: %s sec(s)", filePath, parser, timer.timeElapsed());
			writeLog(LogMessageType.NOTE, logMessage);
		}catch(FileNotFoundException fe){
			logMessage = String.format("%s | [%s] file not found", getStackMessage(fe.getStackTrace()), filePath);
			writeLog(LogMessageType.ERROR, logMessage);
			logMessage = String.format("readSAS(\"%s\", %s) execution failed", filePath, parser);
			writeLog(LogMessageType.ERROR, logMessage);
			
			return null;
		}catch(IOException ie){
			logMessage = String.format("%s | I/O error in reading [%s]", filePath, getStackMessage(ie.getStackTrace()));
			writeLog(LogMessageType.ERROR, logMessage);
			logMessage = String.format("readSAS(\"%s\", %s) execution failed", filePath, parser);
			writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}catch(Exception e){
			logMessage = String.format("%s | unknown error in reading [%s]", getStackMessage(e.getStackTrace()), filePath);
			writeLog(LogMessageType.ERROR, logMessage);
			
			//writeStackToLog(e.getStackTrace());
			
			
			//writeLog(System.lineSeparator());
			
			logMessage = String.format("readSAS(\"%s\", %s) execution failed", filePath, parser);
			writeLog(LogMessageType.ERROR, logMessage);
			return null;
		}
		
		writeLog(System.lineSeparator());
		
		return dataSet;
	}
	
	public static void exportJSON(DataSet dataSet, String filePath) {
		
		String JSONFilePath;
		Variable variable;
		
		try {
			
			if(filePath.toUpperCase().endsWith(".JSON")) {
				JSONFilePath = filePath;
			}else{
				JSONFilePath = filePath + File.separator + dataSet.getName() + ".json";
			}
			
			JsonFactory jfactory = new JsonFactory();

			JsonGenerator jGenerator = jfactory.createJsonGenerator(
				new FileOutputStream(JSONFilePath), 
				JsonEncoding.UTF8
			);
			DefaultPrettyPrinter dpp = new DefaultPrettyPrinter();
			jGenerator.setPrettyPrinter(dpp);
			jGenerator.writeStartObject(); // {
			jGenerator.writeStringField("name", dataSet.getName());
			jGenerator.writeStringField("label", dataSet.getLabel());
			jGenerator.writeNumberField("variableCount", dataSet.getVariableCount());
			jGenerator.writeNumberField("observationCount", dataSet.getObservationCount());
			
			jGenerator.writeFieldName("variables");
			jGenerator.writeStartArray(); // [
			for(int i=0;i<dataSet.getVariableCount();i++) {
				variable = dataSet.getVariableByIndex(i);
				jGenerator.writeStartObject(); // {
				jGenerator.writeStringField("name", variable.getName());
				jGenerator.writeStringField("label", variable.getLabel());
				jGenerator.writeStringField("dataType", variable.getDataType().toString());
				if(Objects.isNull(variable.getFormat()))
					jGenerator.writeNullField("format");
				else
					jGenerator.writeStringField("format", variable.getFormat().toString());
			
				jGenerator.writeFieldName("values");
				jGenerator.writeStartArray(); // [
				Object value;
				for(int j=0;j<dataSet.getObservationCount();j++) {
					value = variable.values.get(dataSet.getSortIndexValueAt(j));
					if(Objects.isNull(value)) {
						jGenerator.writeNull();
						continue;
					}
					if(variable.getDataType() == DataType.INTEGER)
						jGenerator.writeNumber((int)value);
					else if(variable.getDataType() == DataType.LONG)
						jGenerator.writeNumber((long)value);
					else if(variable.getDataType() == DataType.FLOAT)
						jGenerator.writeNumber((float)value);
					else if(variable.getDataType() == DataType.DOUBLE)
						jGenerator.writeNumber((double)value);
					else if(variable.getDataType() == DataType.BOOLEAN)
						jGenerator.writeBoolean((boolean)value);
					else if(variable.getDataType() == DataType.CHARACTER)
						jGenerator.writeString(((Character)value).toString());
					else if(variable.getDataType() == DataType.STRING)
						jGenerator.writeString((String)value);
					else if(variable.getDataType() == DataType.LOCAL_DATE)
						jGenerator.writeObject(((LocalDate)value).toString());
					else if(variable.getDataType() == DataType.LOCAL_TIME)
						jGenerator.writeObject(((LocalTime)value).toString());
					else if(variable.getDataType() == DataType.LOCAL_DATE_TIME)
						jGenerator.writeObject(((LocalDateTime)value).toString());
					
				}
				jGenerator.writeEndArray(); // ]
				jGenerator.writeEndObject(); // }
			}
			jGenerator.writeEndArray(); // ]
			jGenerator.writeEndObject(); // }
			jGenerator.close();
			
			
 
        }catch (JsonGenerationException e) {
			e.printStackTrace();
		}catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
		
	}
	
	public static IDataSet importJSON(ParameterBuilder.ImportJSONParameter params) {
		
		String filePath = params.getFilePath();
		StorageType storageType = params.getStorageType();
		
		VariableFactory variableFactory = new VariableFactory(storageType);
		
		
		JsonFactory jsonfactory;
		JsonParser parser;
		DataSet dataSet = null;
		try {
			jsonfactory = new JsonFactory();
			parser = jsonfactory.createJsonParser(new File(filePath));
			String token;
			
			String name;
			String label;
			ArrayList<Variable> variables = new ArrayList<Variable>();
			Object value = null;
			HSList<Object> values = HSUtils.getHSList(storageType);
			String variableName = null;
			String variableLabel = null;
			String variableFormat = null;
			String variableDataType = null;
			String variableToken = null;
			Format format = null;
			DataType dataType = null;
			Variable variable = null;
			while (parser.nextToken() != JsonToken.END_OBJECT) {
				token = parser.getCurrentName();
				if("name".equals(token)) {
					parser.nextToken();
					name = parser.getText();
				}else if("label".equals(token)) {
					parser.nextToken();
					label = parser.getText();
				}else if("variables".equals(token)){
					parser.nextToken();
					while(parser.nextToken() != JsonToken.END_ARRAY) {
						while(parser.nextToken() != JsonToken.END_OBJECT) {
							variableToken = parser.getCurrentName();
							if("name".equals(variableToken)){
								parser.nextToken();
								variableName = parser.getText();
							}else if("label".equals(variableToken)){
								parser.nextToken();
								variableLabel = parser.getText();
							}else if("dataType".equals(variableToken)){
								parser.nextToken();
								variableDataType = parser.getText();
								dataType = HSUtils.getDataTypeByString(variableDataType);
							}else if("format".equals(variableToken)){
								parser.nextToken();
								variableFormat = parser.getText();
							}else if("values".equals(variableToken)){
								parser.nextToken();
								while(parser.nextToken() != JsonToken.END_ARRAY) {
									if(parser.currentToken() == JsonToken.VALUE_NULL) {
										values.add(null);
										continue;
									}
								
									if(dataType == DataType.INTEGER)
										value = parser.getIntValue();
									else if(dataType == DataType.LONG)
										value = parser.getLongValue();
									else if(dataType == DataType.FLOAT)
										value = parser.getFloatValue();
									else if(dataType == DataType.DOUBLE)
										value = parser.getNumberValue();
									else if(dataType == DataType.BOOLEAN)
										value = parser.getBooleanValue();
									else if(dataType == DataType.CHARACTER)
										value = parser.getText();
									else if(dataType == DataType.STRING)
										value = parser.getText();
									else if(dataType == DataType.LOCAL_DATE) {
										value = parser.getText();
									}
									else if(dataType == DataType.LOCAL_TIME)
										value = parser.getText();
									else if(dataType == DataType.LOCAL_DATE_TIME)
										value = parser.getText();
								
									values.add(value);
								}
							}
						}
						//System.out.println(variableName + " " + variableDataType + " " + variableLabel + " " + variableFormat + " " + values);
						variable = variableFactory.createVariable(variableName, dataType, values);
						variable.label = variableLabel;
						if(Objects.nonNull(variableFormat) && !variableFormat.isEmpty()) {
							if(dataType == DataType.LOCAL_DATE)
								format = Format.localDatePattern(variableFormat);
							//else if(dataType == DataType.LOCAL_TIME)
								//format = Format.localTimePattern(variableFormat);
							else if(dataType == DataType.LOCAL_DATE_TIME)
								format = Format.localDateTimePattern(variableFormat);
							
						}
						variable.format = format;
						variables.add(variable);
						values = HSUtils.getHSList(storageType);
					}
				}
			}
			dataSet = new DataSet(variables);
		}catch(IOException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return dataSet;
	}//end of importJSON()
	
	public static void addToTableView(DataSet... dataSets) {
		

		if(Objects.isNull(dataSetListForTableView))
			dataSetListForTableView = new ArrayList<IDataSet>();
		
		if(Objects.nonNull(dataSets)) {
			int size = dataSets.length;
			for(int i=0;i<size;i++) {
				if(Objects.nonNull(dataSets[i])) 
					dataSetListForTableView.add(dataSets[i].clone());
			}
		}
	}

	public static void showTableView() {
		showTableView(new DataSet[]{});
	}

	public static void showTableView(DataSet... dataSets) {
		addToTableView(dataSets);

		new Thread(new Runnable() {
			public void run() {
				DataSetView.setDataSets(dataSetListForTableView);
				DataSetView.main(new String[]{});
			}
		}).start();
	}
	
	public static ArrayList<DataSet> importExcel(ParameterBuilder.ImportExcelParameter params) {
		
		ArrayList<DataSet> dataSets = null;
		
		try {
			
			String filePath = params.getFilePath();
			ArrayList<ParameterBuilder.ImportExcelParameter.ImportSheetInfo> sheetsInfo = params.getSheetsInfo();
			StorageType storageType = params.getStorageType();
		
			VariableFactory variableFactory = new VariableFactory(storageType);
	
			IHSExcelReader excelReader = new HSExcelReader(filePath);
		
			if(Objects.isNull(sheetsInfo)) {
				sheetsInfo = new ArrayList<ParameterBuilder.ImportExcelParameter.ImportSheetInfo>();
				ParameterBuilder.ImportExcelParameter.ImportSheetInfo sheetInfo;
				for(int i=0;i<excelReader.getNumberOfSheets();i++) {
					sheetInfo = new ParameterBuilder.ImportExcelParameter.ImportSheetInfo(excelReader.getSheetName(i), true, 0);
					sheetsInfo.add(sheetInfo);
				}
			}
			
			dataSets = new ArrayList<DataSet>();
			
			for(int i=0;i<sheetsInfo.size();i++) {
				
				ArrayList<HSList> valuesList= new ArrayList<HSList>();
				ArrayList<String> colNames = new ArrayList<String>();
				Map<String, String> variableDataType = new HashMap<String, String>();
				final ParameterBuilder.ImportExcelParameter.ImportSheetInfo sheetInfo = sheetsInfo.get(i);
				excelReader.getSheet(sheetInfo.getSheetName()).forEachRow(observation -> {
					int colN = 0;
					for(Cell cell:observation) {
						if(observation.getRowNum() == sheetInfo.getStartFrom()) {
							if(sheetInfo.getHasHeader() == false) {
								valuesList.add(new MList<Object>());
								colNames.add("UNNAMED_COLUMN_"+colN);
								
								switch(cell.getCellType()) {
									case BOOLEAN:
										valuesList.get(colN).add(cell.getBooleanCellValue());
										break;
									case NUMERIC:
										valuesList.get(colN).add(cell.getNumericCellValue());
										break;
									case STRING:
										valuesList.get(colN).add(cell.getStringCellValue());
										break;
									case BLANK:
										valuesList.get(colN).add(null);
										break;
								}
								if(valuesList.get(colN).get(valuesList.get(colN).size()-1) != null)
									variableDataType.put("UNNAMED_COLUMN_"+colN, valuesList.get(colN).get(valuesList.get(colN).size()-1).getClass().getName());
							}else {
								switch(cell.getCellType()) {
									case BLANK:
										valuesList.add(new MList<Object>());
										colNames.add("UNNAMED_COLUMN_"+colN);
										break;
									default:
										valuesList.add(new MList<Object>());
										colNames.add(cell.getStringCellValue());
								}
							}
						}else if(observation.getRowNum() > sheetInfo.getStartFrom()) {
							switch(cell.getCellType()) {
								case BOOLEAN:
									valuesList.get(colN).add(cell.getBooleanCellValue());
									break;
								case NUMERIC:
									valuesList.get(colN).add(cell.getNumericCellValue());
									break;
								case STRING:
									valuesList.get(colN).add(cell.getStringCellValue());
									break;
								case BLANK:
									valuesList.get(colN).add(null);
									break;
							}
							if(valuesList.get(colN).get(valuesList.get(colN).size()-1) != null)
								variableDataType.put(colNames.get(colN), valuesList.get(colN).get(valuesList.get(colN).size()-1).getClass().getName());
						}
						colN++;
					}
				});
				System.out.println(colNames);
				System.out.println(variableDataType);
				ArrayList<Variable> variables = new ArrayList<Variable>();
				Variable variable;
				DataType dataType = DataType.STRING;;
				for(int j=0;j<valuesList.size();j++){
					if(variableDataType.get(j) != null) {
						if(variableDataType.get(j).endsWith("Double"))
							dataType = DataType.DOUBLE;
						else if(variableDataType.get(j).endsWith("Boolean"))
							dataType = DataType.BOOLEAN;
						else if(variableDataType.get(j).endsWith("String"))
							dataType = DataType.STRING;
						else
							dataType = DataType.STRING;
					}
					variable = variableFactory.createVariable(HSUtils.getValidVariableName(colNames.get(j)), dataType, valuesList.get(j));
					variable.label = colNames.get(j);
					variables.add(variable);
				}
				DataSet dataSet = new DataSet(variables);
				dataSet.setName(sheetInfo.getSheetName());
				dataSet.setLabel(sheetInfo.getSheetName());
				dataSets.add(dataSet);
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
		
		return dataSets;
	
	}
	
	
	public static VariableFactory getVariableFactory(StorageType storageType) {
		return new VariableFactory(storageType);
		
	}
	
	
	
	/***************************************************************/
	/****************Loggging methods
	/***************************************************************/
	
	public static void writeStackTraceToLog(Exception ex) {
		String msg;
		HS.writeLog(LogMessageType.ERROR, ex.toString());
		for(int i=0;i<ex.getStackTrace().length;i++) {
			msg = "\t at " + ex.getStackTrace()[i].toString();
			System.out.println(msg);
			HS.writeLog(LogMessageType.ERROR, msg);
		}
	}
	
	public static String getStackMessage(StackTraceElement ste[]) {
		StackTraceElement st = ste[ste.length-1];
		logMessage = String.format("%s > %s > %s > %s", st.getFileName(), st.getClassName(), st.getMethodName(), st.getLineNumber());
		return logMessage;
	}
	
	public static void writeLog(String msg) {

		if(Objects.isNull(log) || log.getMute() == true)
			return;

		log.print(msg);
		
	}
	
	public static void writeLog(LogMessageType type, String msg) {

		if(Objects.isNull(log) || log.getMute() == true)
			return;

		if(type == LogMessageType.WARNING) {
			log.warning(msg);
		}else if(type == LogMessageType.ERROR) {
			log.error(msg);
		}else if(type == LogMessageType.NOTE) {
			log.note(msg);
		}else {
			log.info(msg);
		}
		
	}
	
	static void setLogMute(boolean mute) {
		if(Objects.isNull(log))
			return;
		
		log.setMute(mute);
	}
	

	
	public static void openLog(String filePath) {
		if(Objects.isNull(log))
			log = new Log();
		
		log.open(filePath);
	}
	
	
	public static void closeLog() {
		HS.log.close();
	}
	
	/***************************************************************/
	/**************** end of Loggging methods
	/***************************************************************/
}