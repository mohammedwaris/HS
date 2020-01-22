package com.silverbrain.studio.content;

import java.util.*;

public class SharedData {

	Map<String, Object> sharedData;
	
	public SharedData() {
		sharedData = new HashMap<String, Object>();
	}
	
	public void store(String key, Object value) {
		sharedData.put(key, value);
	}
	
	public Object retrieve(String key) {
		return sharedData.get(key);
	}
}