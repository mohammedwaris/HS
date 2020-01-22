package com.silverbrain.hs.core;

import java.io.*;

public class Library {

	String dirPath;
	File dir;
	
	public Library(String dirPath) throws IllegalArgumentException {
		File tempdir = new File(dirPath);
		if(!tempdir.isDirectory()) {
			throw new IllegalArgumentException(String.format("[%s] is not a directory", dirPath));
		}else{
			this.dirPath = dirPath;
			this.dir = tempdir;
		}
			
	}
	
	public String getDirPath() {
		return this.dirPath;
	}
}