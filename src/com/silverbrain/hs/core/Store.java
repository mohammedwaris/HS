package com.silverbrain.hs.core;

import java.util.Map;
import java.util.HashMap;

public class Store {
	
	Map<String, Object> retain;
	
	public Store() {
		retain = new HashMap<String, Object>();
	}
	
	public void save(String key, Object value) {
		retain.put(key, value);
	}
	
	public Object retrieve(String key) {
		return retain.get(key);
	}
	
	public void delete(String key) {
		retain.remove(key);
	}
	
	public void deleteAll() {
		retain.clear();
	}
	
	public int getSize() {
		return retain.size();
	}
	
	//public void forEach
}