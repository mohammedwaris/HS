package com.rqr.core;

import java.util.*;
import java.util.function.*;

public class RQR {

	String name;
	ArrayList<Column> columns;
	ArrayList<Integer> sortIndex = new ArrayList<Integer>();
	static Map<String, RQR> rqrs = new HashMap<String, RQR>();
	
	RQR() {
		
	}
	
	int getRowCount() {
		return sortIndex.size();
	}
	
	int getColumnCount() {
		return columns.size();
	}
	
	public static void newDataTable(String name, ArrayList<Column> columns) {
		RQR rqr = new RQR();
		rqrs.put(name, rqr);
		rqr.name = name;
		rqr.columns = columns;
		for(int i=0;i<columns.get(0).values.size();i++) {
			rqr.sortIndex.add(i);
		}
	}
	
	public static void processDataTable(String name, Consumer<Row> consumer) {
		RQR rqr = rqrs.get(name);
		
		Row row;
		String[] values= null;
		String[] names;
		names = new String[rqr.getColumnCount()];
		for(int i=0;i<rqr.getColumnCount();i++) {
			//System.out.print(rqr.columns.get(i).name + " ");
			names[i] = rqr.columns.get(i).name;
		}
		
		for(int i=0;i<rqr.getRowCount();i++) {
			values = new String[rqr.getColumnCount()];
			for(int j=0;j<rqr.getColumnCount();j++) {
				//System.out.print(rqr.columns.get(j).values.get(i) + " ");
				values[i] = (String)rqr.columns.get(j).values.get(i);
			}
			//System.out.println();
			row = new Row(names, values);
			consumer.accept(row);
			System.out.println("up: " + values[1]);
		}
		
		
	}
	
	public static void print(String name) {
		RQR rqr = rqrs.get(name);
		for(int i=0;i<rqr.getColumnCount();i++) {
			System.out.print(rqr.columns.get(i).name + " ");
		}
		System.out.println();
		
		for(int i=0;i<rqr.getRowCount();i++) {
			for(int j=0;j<rqr.getColumnCount();j++) {
				System.out.print(rqr.columns.get(j).values.get(i) + " ");
			}
			System.out.println();
		}
		
	}
	
}
