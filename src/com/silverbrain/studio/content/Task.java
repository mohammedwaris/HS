package com.silverbrain.studio.content;

import com.silverbrain.HaapusSession;


public abstract class Task {

	private HaapusSession haapusSession;
	private SharedData sharedData;
	
	void setHaapusSession(HaapusSession haapusSession) {
		this.haapusSession = haapusSession;
	}
	
	protected HaapusSession getHaapusSession() {
		return this.haapusSession;
	}
	
	void setSharedData(SharedData sharedData) {
		this.sharedData = sharedData;
	}
	
	protected SharedData getSharedData() {
		return this.sharedData;
	}
	
	public abstract void execute();

}