package com.silverbrain;
/*
import com.silverbrain.hs.core.Library;
import com.silverbrain.hs.core.DataTable;
import com.silverbrain.hs.core.Column;
import com.silverbrain.hs.core.LogMessageType;
import com.silverbrain.hs.core.Haapus;
import com.silverbrain.hs.core.ParameterBuilder;*/

import java.util.*;
import java.io.*;

public class HaapusSession {
	/*
	ArrayList<DataTable> dataTableList;
	ArrayList<Library> libraryList;
	//Log log;
	
	HaapusSession() {
		dataTableList = new ArrayList<DataTable>();
		libraryList = new ArrayList<Library>();
		//log = new Log();
		//Haapus.log = log;
	}
	
	public DataTable createDataTable(ArrayList<Column> columns) {
		DataTable dataTable = new DataTable(columns);
		dataTableList.add(dataTable);
		return dataTable;
	}
	
	public DataTable createDataTableFromCSV(Library library, String csvFileName) {
		DataTable dataTable = null;
		try{
			dataTable = Haapus.importCSV(new ParameterBuilder.ImportCSVParameter(library.getDirPath() + File.separator + csvFileName));
			dataTableList.add(dataTable);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return dataTable;
	}
	
	public DataTable createDataTableFromSAS(Library library, String sasFileName) {
		DataTable dataTable = null;
		//try{
			dataTable = Haapus.importSAS(new ParameterBuilder.ImportSASParameter(library.getDirPath() + File.separator + sasFileName));
			dataTableList.add(dataTable);
		//}catch(Exception e) {
			//System.out.println(e.getMessage());
		//}
		return dataTable;
	}
	
	public Library createLibrary(String dirPath) {
		Library library = null;
		try{
			library = new Library(dirPath);
			libraryList.add(library);
			Haapus.writeLog(LogMessageType.INFO, String.format("[%s] library created", dirPath));
		}catch(Exception e) {
			Haapus.writeLog(LogMessageType.ERROR, e.getMessage() + ", library creation failed");
		}
		return library;
	}
	
	*/
	
	/*
	public Log getLog() {
		return log;
	}*/
	
}