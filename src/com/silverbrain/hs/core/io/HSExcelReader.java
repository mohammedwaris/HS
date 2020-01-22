package com.silverbrain.hs.core.io;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.github.pjfanning.xlsx.StreamingReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import java.util.function.Consumer;

public class HSExcelReader implements IHSExcelReader {

	String filePath;
	Workbook workbook;
	Map<String, HSExcelSheet> excelSheets;
	
	public HSExcelReader(String filePath) throws FileNotFoundException, IOException {
		this.filePath = filePath;
		if(filePath.toLowerCase().endsWith(".xls")) {
			workbook = new HSSFWorkbook(new FileInputStream(new File(filePath)));
		}else if(filePath.toLowerCase().endsWith(".xlsx")) {
			workbook = StreamingReader.builder().open(new FileInputStream(new File(filePath)));
		}
		
		this.excelSheets = new HashMap<String, HSExcelSheet>();
		
		for(int i=0;i<workbook.getNumberOfSheets();i++){
			excelSheets.put(workbook.getSheetName(i), new HSExcelSheet(workbook.getSheetAt(i)));
		}

	}
	
	public String getSheetName(int index) {
		return this.workbook.getSheetName(index);
	}
	
	public int getNumberOfSheets() {
		return this.workbook.getNumberOfSheets();
	}
	
	public HSExcelSheet getSheet(String sheetName) {
		return this.excelSheets.get(sheetName);
	}
	
	
	public class HSExcelSheet implements IHSExcelSheet {
		
		Sheet sheet;
		
		public HSExcelSheet(Sheet sheet) {
			this.sheet = sheet;
		}
		
		public String getSheetName() {
			return this.sheet.getSheetName();
		}
		
		public int getPhysicalNumberOfRows() {
			return this.sheet.getPhysicalNumberOfRows();
		}
		
		public int getColumnCount() {
			return -1;
		}
		
		public ArrayList<String> getColumnNames() {
			return null;
		}
		
		public void forEachRow(Consumer<Row> consumer) {
			for (Row r : sheet) {
				consumer.accept(r);
			}
		}
		
		
	}
	
	
	
}