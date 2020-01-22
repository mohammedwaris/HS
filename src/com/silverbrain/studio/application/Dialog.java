package com.silverbrain.studio.application;


import java.util.function.*;
import javafx.scene.Scene;

import javafx.stage.Stage;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;



import javafx.event.ActionEvent; 
import javafx.event.EventHandler; 

public class Dialog extends Stage implements EventHandler<ActionEvent> {

	public static String YES_BUTTON = "1";
	public static String NO_BUTTON = "2";
	public static String CANCEL_BUTTON = "3";
	
	Label txt;

	Button yesButton;
	Button noButton;
	Button cancelButton;

	Consumer<String> consumer;


	Dialog() {

		txt = new Label("Save project before closing?");

		yesButton = new Button("Yes");
		noButton = new Button("No");
		cancelButton = new Button("Cancel");
		yesButton.setOnAction(this);
		noButton.setOnAction(this);
		cancelButton.setOnAction(this);

		HBox hbox = new HBox(yesButton, noButton, cancelButton);
		VBox vbox = new VBox(txt, hbox);


		Scene scene = new Scene(vbox);
		setWidth(300);
		setHeight(200);
	
        this.setScene(scene);
	}

	public void addListener(Consumer<String> consumer) {
		this.consumer = consumer;
	} 

	public void handle(ActionEvent ae) {
		if(ae.getSource() == yesButton)
			consumer.accept(YES_BUTTON);
		else if(ae.getSource() == noButton)
			consumer.accept(NO_BUTTON);
		else if(ae.getSource() == cancelButton)
			consumer.accept(CANCEL_BUTTON);
	}
}