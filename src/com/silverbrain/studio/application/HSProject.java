package com.silverbrain.studio.application;

import com.silverbrain.studio.content.*;

import javax.tools.*;

//import com.haapus.HaapusSession;


public class HSProject {
	
	String projectPath;
	String projectFileName;
	String mainClassName;
	SharedData sharedData;
	Project project;
	Class mainClass;
	
	//HaapusSession haapusSession;
	
	public HSProject(String projectPath) {
		
		this.projectPath = projectPath;
		//this.mainClassName = mainClassName;
		
	}
	
	void setMainClassName(String mainClassName) {
		//this.mainClassName = mainClassName;
	}
	
	String getMainClassName() {
		return this.mainClassName;
	}
	
	void save() {
		
	}

	void close() {
		
	}
	
	static void open(String projectFilePath) {
	}
	
	public void init() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		
		//haapusSession = new HaapusSession();
		//haapusProject.setHaapusSession(haapusSession);
	}
	
	public void executeProject() throws Exception {
		
		projectPath = "/Users/mohammedwaris/Documents/haapus_backup";
		projectFileName = "MyProject.java";
		compileProject();
		
		mainClass = Class.forName(this.mainClassName);
		project = (Project) mainClass.newInstance();
		sharedData = new SharedData();
		
		try{
			//Haapus.openLog(mainClassName + ".log");
			project.setSharedData(sharedData);
			project.execute();
		}catch(Exception e){
			//StringWriter sw = new StringWriter();
			//e.printStackTrace(new PrintWriter(sw));
			//Haapus.writeLog(LogMessageType.ERROR, sw.toString());
			//Haapus.closeLog();
			//throw e;
		}finally{
			//Haapus.closeLog();
		}
	}

	private void compileProject() {
		String projectFile = projectPath + "/" + projectFileName;
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		compiler.run(null, null, null, projectFile);
	}
}