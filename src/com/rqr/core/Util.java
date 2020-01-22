package com.rqr.core;

import java.util.ArrayList;

public class Util {

	public static ArrayList<Object> createList(Object... objectData) {
		ArrayList<Object> list = new ArrayList<Object>();
		for(Object data: objectData)
			list.add(data);
		
		return list;
	}
	
	public static boolean isInvalidColumnName(String columnName) {
		
		boolean invalid = false;
		for(int i=0; i<columnName.length(); i++) {
			char c = columnName.charAt(i);
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
}