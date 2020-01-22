package com.silverbrain.hs.core;

public enum DataType {
	
	//Size: 4 bytes, Range: −2,147,483,648 to 2,147,483,647
	INTEGER,
	
	//Size: 8 bytes, Range: 9,223,372,036,854,775,808 to 9,223,372,036,854,755,807
	LONG,
	
	//Size: 4 bytes, Range: 3.4e−038 to 3.4e+038
	FLOAT,
	
	//Size: 8 bytes, Range: 1.7e−308 to 1.7e+308
	DOUBLE,
	
	//Size: 2 bytes, Range: 0 to 65,535
	CHARACTER,
	
	//Size: 1 bit, Values: true or false;
	BOOLEAN,
	
	STRING,
	
	LOCAL_DATE,
	
	LOCAL_TIME,
	
	LOCAL_DATE_TIME
	
}