package com.silverbrain.hs.core;

import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

public class Format {
	
	static ArrayList<Format> formatList = new ArrayList<Format>();
	
	public static Format ISO_DATE = Format.localDatePattern("yyyy-MM-dd");
	public static Format ISO_LOCAL_DATE = Format.localDatePattern("yyyy-MM-dd");

	String pattern;
	
	DateTimeFormatter formatter;
	
	boolean isDatePattern;
	boolean isTimePattern;
	boolean isDateTimePattern;
	boolean isIntegerPattern;
	boolean isDoublepattern;
	
	
	
	Format() {
		
	}
	
	public static Format localDatePattern(String pattern) {
		
		Format format;
		format = getFormatIfFormatterAlreadyExists(pattern);
		if(format == null) {
			format = new Format();
			format.pattern = pattern;
			format.formatter = DateTimeFormatter.ofPattern(pattern);
			formatList.add(format);
			format.isDatePattern = true;
		}
		
		return format;
	
	}
	
	public static Format localTimePattern(String pattern) {
		
		Format format;
		format = getFormatIfFormatterAlreadyExists(pattern);
		if(format == null) {
			format = new Format();
			format.pattern = pattern;
			format.formatter = DateTimeFormatter.ofPattern(pattern);
			formatList.add(format);
			format.isTimePattern = true;
		}
		
		return format;
	
	}
	
	public static Format localDateTimePattern(String pattern) {
		
		Format format;
		format = getFormatIfFormatterAlreadyExists(pattern);
		if(format == null) {
			format = new Format();
			format.pattern = pattern;
			format.formatter = DateTimeFormatter.ofPattern(pattern);
			formatList.add(format);
			format.isDateTimePattern = true;
		}
		
		return format;
	
	}
	
	private static Format getFormatIfFormatterAlreadyExists(String pattern) {
		Format fmt = null;
		for(Format format:formatList) {
			if(format.pattern.equals(pattern)) {
				fmt = format;
				break;
			}
		}
		return fmt;
	}
	
	public String toString() {
		return pattern;
	}

}