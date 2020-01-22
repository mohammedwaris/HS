package com.silverbrain.hs.core.io;

import org.apache.poi.ss.usermodel.Row;
import java.util.function.Consumer;

public interface IHSExcelReader {
	
	public IHSExcelSheet getSheet(String sheetName);
	public String getSheetName(int index);
	public int getNumberOfSheets();

	interface IHSExcelSheet {
		
		public String getSheetName();
		public int getPhysicalNumberOfRows();
		public void forEachRow(Consumer<Row> consumer);
	}
}