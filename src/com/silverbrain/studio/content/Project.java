package com.silverbrain.studio.content;

import com.silverbrain.HaapusSession;

public abstract class Project {
	
	private HaapusSession haapusSession;
	private SharedData sharedData;
	
	public Project() {}
	
	void setHaapusSession(HaapusSession haapusSession){
		this.haapusSession = haapusSession;
	}
	
	public HaapusSession getHaapusSession() {
		return this.haapusSession;
	}
	
	public void setSharedData(SharedData sharedData) {
		this.sharedData = sharedData;
	}
	
	public SharedData getSharedData() {
		return this.sharedData;
	}
	
	public abstract void execute();
	
	protected void executeModule(Module module) {
		module.setHaapusSession(haapusSession);
		module.setSharedData(sharedData);
		module.execute();
	}

}