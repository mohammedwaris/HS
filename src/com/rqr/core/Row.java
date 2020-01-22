package com.rqr.core;

public class Row {

	String[] values;
	String[] names;
	
	public Row(String[] names, String[] values) {
		this.names = names;
		this.values = values;
	}
	
	public StringProperty getStringProperty(String name) {
		int p = -1;
		for(int i=0;i<names.length;i++) {
			if(names[i].equalsIgnoreCase(name)) {
				p = i;
				break;
			}
		}
		
		StringProperty stringProperty = null;
		StringProperty sp = null;
		if(p >= 0) {
			stringProperty = new StringProperty(values[p]);
			sp = new StringProperty(stringProperty.getString());
			sp.bind(stringProperty);
		}
		
		 

		
		return sp;
		
	}
	
}