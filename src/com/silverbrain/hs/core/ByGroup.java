package com.silverbrain.hs.core;

public class ByGroup {
	
	int start;
	int end;
	
	public ByGroup() {
		
	}
	
	public ByGroup(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	public void setStart(int start) {
		this.start = start;
	}
	
	public int getStart() {
		return start;
	}
	
	public void setEnd(int end) {
		this.end = end;
	}
	
	public int getEnd() {
		return end;
	}
	
	public void setAll(int start, int end) {
		this.start = start;
		this.end = end;
	}
}