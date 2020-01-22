package com.rqr.core;

import java.util.ArrayList;

public class StringProperty {

	String string;
	ArrayList<StringProperty> bindedStringProperties;
	
	public StringProperty() {
		this.string = "";
		this.bindedStringProperties = new ArrayList<StringProperty>();
	}
	
	public StringProperty(String string) {
		this.string = string;
		this.bindedStringProperties = new ArrayList<StringProperty>();
	}
	
	public void bind(StringProperty stringProperty){
		bind(stringProperty, false);
	}
	
	public void bind(StringProperty stringProperty, boolean twoWay){
		stringProperty.setString(string);
		bindedStringProperties.add(stringProperty);
		if(twoWay == true)
			stringProperty.bind(this);
	}
	
	public void setString(String string) {
		this.string = string;
		for(StringProperty stringProperty:bindedStringProperties) {
			stringProperty.string = string;
		}
	}
	
	public String getString() {
		return this.string;
	}
	
	public String toString() {
		return string;
	}
}