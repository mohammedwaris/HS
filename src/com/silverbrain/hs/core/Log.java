package com.silverbrain.hs.core;

import java.time.*;
import java.io.*;
import java.util.Objects;

class Log {
	
	private String filePath;
	private File file;
	private FileWriter fileWriter;
	private boolean mute;
	private String fullMessage;
	
	Log() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try{
					fileWriter.flush();
					fileWriter.close();
				}catch(IOException ex){
					//ignore
				}
			}
		});
	}
	
	public void finalize() {
		try{
			fileWriter.flush();
			fileWriter.close();
		}catch(IOException ex){
			//ignore
		}
	}
	
	void open(String filePath) {
		
		try {
			if(Objects.nonNull(fileWriter))
				fileWriter.close();
		}catch(IOException ie) {
			//throw ie;
		}
		
		try {
			this.filePath = filePath;
			file = new File(filePath);
			fileWriter = new FileWriter(file);
			mute = false;
		}catch(IOException ie){
			
		}
	}
	
	void close() {
		try{
			fileWriter.flush();
			fileWriter.close();
		}catch(Exception e) {
			
		}
	}

	void error(String msg) {
		try{
			fullMessage = "[" + LocalDateTime.now() + "] " + "ERROR: " +   msg + System.lineSeparator();
			System.out.print(fullMessage);
			fileWriter.write(fullMessage);
			fileWriter.flush();
		}catch(IOException ie){
			
		}
	}
	
	void warning(String msg) {
		try{
			fileWriter.write("[" + LocalDateTime.now() + "] "  + "WARNING: " + msg + System.lineSeparator());
			fileWriter.flush();
		}catch(IOException ie){
			
		}
	}
	
	void note(String msg) {
		if(mute)
			return;

		try{
			fullMessage = "[" + LocalDateTime.now() + "] " + "NOTE: " + msg + System.lineSeparator();
			System.out.println(fullMessage);
			fileWriter.write(fullMessage);
			fileWriter.flush();
		}catch(IOException ie){
			
		}
	}
	
	void info(String msg) {
		try{
			fullMessage = "[" + LocalDateTime.now() +"] " + "INFO: " + msg + System.lineSeparator();
			System.out.print(fullMessage);
			fileWriter.write(fullMessage);
			fileWriter.flush();
		}catch(IOException ie){
			
		}
	}
	
	void print(String msg) {
		try{
			fullMessage = msg;
			System.out.print(fullMessage);
			fileWriter.write(fullMessage);
			fileWriter.flush();
		}catch(IOException ie){
			
		}
	}
	
	void setMute(boolean mute) {
		this.mute = mute;
	}
	
	boolean getMute() {
		return this.mute;
	}
}