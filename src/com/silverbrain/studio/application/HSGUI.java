package com.silverbrain.studio.application;

import javafx.application.Application;


import javafx.scene.Scene;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;



import javafx.geometry.Insets;

import javafx.event.EventHandler;

public class HSGUI extends Application {
	
	
	
	
	HSGUIController controller;
	
	@Override
    public void start(Stage primaryStage) {
		
		controller = new HSGUIController(this);
		
		Scene scene = new Scene(controller.getRootLayout());
	
        primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("haapus.png")));
		primaryStage.setWidth(1000);
		primaryStage.setHeight(600);
		primaryStage.setTitle("Haapus Studio");
		primaryStage.show();
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				controller.exit();
			}
		});
	}
	
	@Override
	public void stop() {
		System.out.println("Stop() called.");
	}
	
	public static void main(String[] args)  {
		launch(args);
	}
}