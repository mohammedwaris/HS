package com.silverbrain.hs.core;

class Timer {

	long startTime;
	long stopTime;
	String timeElapsed;

	void start() {
		startTime = System.currentTimeMillis();
	}
	
	void stop() {
		stopTime = System.currentTimeMillis();
	}
	
	String timeElapsed() {
		long elapsed = stopTime - startTime;
		//System.out.println(String.format("%.002f", elapsed/1000.00));
		//if(elapsed < 0.01)
			//timeElapsed = (elapsed*1000)/1000.00;
		//else
			timeElapsed = String.format("%.002f", elapsed/1000.00);
		return timeElapsed;
	}
}