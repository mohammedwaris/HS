package com.silverbrain.hs.core;

public class VariableData {

	private Object value;
	private Store store;
	
	public VariableData(Object value, Store store) {
		this.value = value;
		this.store = store;
	}
	
	public Object getValue() {
		return value;
	}
	
	public Store getStore() {
		return store;
	}
}