package com.silverbrain.hs.core;

public class WhereData {

	private boolean value;
	private Store store;
	
	public WhereData(boolean value, Store store) {
		this.value = value;
		this.store = store;
	}
	
	public boolean getValue() {
		return value;
	}
	
	public Store getStore() {
		return store;
	}
}