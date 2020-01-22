package com.silverbrain.hs.core;

import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

import java.util.Objects;

public class Informat {
	
	static ArrayList<Informat> informatList = new ArrayList<Informat>();
	
	public static final Informat INTEGER = Informat.integerPattern();
	public static final Informat LONG = Informat.longPattern();
	public static final Informat FLOAT = Informat.floatPattern();
	public static final Informat DOUBLE = Informat.doublePattern();
	public static final Informat STRING = Informat.stringPattern();
	public static final Informat CHARACTER = Informat.characterPattern();
	public static final Informat BOOLEAN = Informat.booleanPattern();
	public static final Informat LOCAL_DATE = Informat.localDatePattern("local_date");
	public static final Informat LOCAL_TIME = Informat.localTimePattern("local_time");
	public static final Informat LOCAL_DATE_TIME = Informat.localDateTimePattern("local_date_time");
	
	public static final Informat AUTO = Informat.autoPattern();
	

	String _pattern;
	
	DateTimeFormatter formatter;
	
	boolean isDatePattern;
	boolean isTimePattern;
	boolean isDateTimePattern;
	boolean isIntegerPattern;
	boolean isDoublePattern;
	boolean isStringPattern;
	boolean isLongPattern;
	boolean isFloatPattern;
	boolean isCharacterPattern;
	boolean isBooleanPattern;
	
	boolean isAutoPattern;
	
	
	
	
	Informat() {
	}
	
	static Informat autoPattern() {
		Informat informat;
		informat = getInformatIfFormatterAlreadyExists("auto");
		if(informat == null) {
			informat = new Informat();
			informat._pattern = "auto";
			//informat.formatter = DateTimeFormatter.ofPattern(_pattern);
			informatList.add(informat);
			informat.isAutoPattern = true;
		}
		return informat;
	}
	
	static Informat stringPattern() {
		Informat informat;
		informat = getInformatIfFormatterAlreadyExists("string");
		if(informat == null) {
			informat = new Informat();
			informat._pattern = "string";
			//informat.formatter = DateTimeFormatter.ofPattern(_pattern);
			informatList.add(informat);
			informat.isStringPattern = true;
		}
		return informat;
	}
	
	static Informat integerPattern() {
		Informat informat;
		informat = getInformatIfFormatterAlreadyExists("integer");
		if(informat == null) {
			informat = new Informat();
			informat._pattern = "integer";
			//informat.formatter = DateTimeFormatter.ofPattern(_pattern);
			informatList.add(informat);
			informat.isIntegerPattern = true;
		}
		return informat;
	}
	
	static Informat longPattern() {
		Informat informat;
		informat = getInformatIfFormatterAlreadyExists("long");
		if(informat == null) {
			informat = new Informat();
			informat._pattern = "long";
			//informat.formatter = DateTimeFormatter.ofPattern(_pattern);
			informatList.add(informat);
			informat.isLongPattern = true;
		}
		return informat;
	}
	
	static Informat floatPattern() {
		Informat informat;
		informat = getInformatIfFormatterAlreadyExists("float");
		if(informat == null) {
			informat = new Informat();
			informat._pattern = "float";
			//informat.formatter = DateTimeFormatter.ofPattern(_pattern);
			informatList.add(informat);
			informat.isFloatPattern = true;
		}
		return informat;
	}
	
	static Informat doublePattern() {
		Informat informat;
		informat = getInformatIfFormatterAlreadyExists("double");
		if(informat == null) {
			informat = new Informat();
			informat._pattern = "double";
			//informat.formatter = DateTimeFormatter.ofPattern(_pattern);
			informatList.add(informat);
			informat.isDoublePattern = true;
		}
		return informat;
	}
	
	static Informat characterPattern() {
		Informat informat;
		informat = getInformatIfFormatterAlreadyExists("character");
		if(informat == null) {
			informat = new Informat();
			informat._pattern = "character";
			//informat.formatter = DateTimeFormatter.ofPattern(_pattern);
			informatList.add(informat);
			informat.isCharacterPattern = true;
		}
		return informat;
	}
	
	static Informat booleanPattern() {
		Informat informat;
		informat = getInformatIfFormatterAlreadyExists("boolean");
		if(informat == null) {
			informat = new Informat();
			informat._pattern = "boolean";
			//informat.formatter = DateTimeFormatter.ofPattern(_pattern);
			informatList.add(informat);
			informat.isBooleanPattern = true;
		}
		return informat;
	}
	
	public static Informat localDatePattern(String _pattern) {
		
		Informat informat;
		informat = getInformatIfFormatterAlreadyExists(_pattern);
		if(informat == null) {
			informat = new Informat();
			informat._pattern = _pattern;
			if(!_pattern.isEmpty() && !_pattern.equals("local_date"))
				informat.formatter = DateTimeFormatter.ofPattern(_pattern);
			informatList.add(informat);
			informat.isDatePattern = true;
		}
		
		return informat;
	}
	
	public static Informat localTimePattern(String _pattern) {
		
		Informat informat;
		informat = getInformatIfFormatterAlreadyExists(_pattern);
		if(informat == null) {
			informat = new Informat();
			informat._pattern = _pattern;
			if(!_pattern.isEmpty() && !_pattern.equals("local_time"))
				informat.formatter = DateTimeFormatter.ofPattern(_pattern);
			informatList.add(informat);
			informat.isTimePattern = true;
		}
		
		return informat;
	}
	
	public static Informat localDateTimePattern(String _pattern) {
		
		Informat informat;
		informat = getInformatIfFormatterAlreadyExists(_pattern);
		if(informat == null) {
			informat = new Informat();
			informat._pattern = _pattern;
			if(!_pattern.isEmpty() && !_pattern.equals("local_date_time"))
				informat.formatter = DateTimeFormatter.ofPattern(_pattern);
			informatList.add(informat);
			informat.isDateTimePattern = true;
		}
		
		return informat;
	}

	public String toString() {

		String str = "Informat[";

		if(isDatePattern){
			str += "LOCAL_DATE";
		}else if(isTimePattern){
			str += "LOCAL_TIME";
		}else if(isDateTimePattern){
			str += "LOCAL_DATE_TIME";
		}else{
			str += _pattern;
		}

		if(Objects.nonNull(_pattern) && !_pattern.isEmpty())
			str += ", " + _pattern;
		str += "]";

		return str;
	}
	
	private static Informat getInformatIfFormatterAlreadyExists(String _pattern) {
		Informat infmt = null;
		for(Informat informat:informatList) {
			if(informat._pattern.equals(_pattern)) {
				infmt = informat;
				break;
			}
		}
		return infmt;
	}
}

