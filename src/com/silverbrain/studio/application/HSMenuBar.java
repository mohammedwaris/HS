package com.silverbrain.studio.application;

import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.util.ArrayList;

public class HSMenuBar extends MenuBar implements EventHandler<ActionEvent> {
	
	Menu fileMenu;
	Menu editMenu;
	Menu helpMenu;
	Menu codeMenu;
	
	MenuItem newProjectMenuItem;
	MenuItem openProjectMenuItem;
	MenuItem closeProjectMenuItem;
	MenuItem saveMenuItem;
	MenuItem saveAsMenuItem;
	MenuItem saveCopyAsMenuItem;
	MenuItem exportMenuItem;
	MenuItem printMenuItem;
	MenuItem exitMenuItem;
	
	MenuItem undoMenuItem;
	MenuItem redoMenuItem;
	MenuItem copyMenuItem;
	MenuItem pasteMenuItem;
	MenuItem projectSettingsMenuItem;

	MenuItem compileMenuItem;
	MenuItem runMenuItem;
	
	MenuItem aboutMenuItem;
	
	HSGUIController controller = null;
	
	ArrayList<MenuItem> menuItemList = null;
	
	static HSMenuBar hsMenuBar;
	
	private HSMenuBar(HSGUIController controller) {
		
		super();

		this.controller = controller;
		menuItemList = new ArrayList<MenuItem>();
		
		buildFileMenu();
		buildEditMenu();
		buildCodeMenu();
		buildHelpMenu();
	}
	
	public static HSMenuBar getMenuBar(HSGUIController controller) {
		if(hsMenuBar == null)
			hsMenuBar = new HSMenuBar(controller);
		
		return hsMenuBar;
	}
	
	
	public void update() {
		
		if(controller.appState == AppState.IDLE) {
			disableAllMenuItem();
			enableMenuItem(newProjectMenuItem, openProjectMenuItem, exitMenuItem);
		}else if(controller.appState == AppState.ACTIVE_NOT_SAVED) {
			disableAllMenuItem();


			enableMenuItem(newProjectMenuItem, openProjectMenuItem, closeProjectMenuItem, 
							saveMenuItem, saveAsMenuItem, saveCopyAsMenuItem, exportMenuItem,
							printMenuItem);

			enableMenuItem(projectSettingsMenuItem);

			enableMenuItem(compileMenuItem, runMenuItem);
		}else if(controller.appState == AppState.ACTIVE_SAVED) {
			disableAllMenuItem();


			enableMenuItem(newProjectMenuItem, openProjectMenuItem, closeProjectMenuItem, 
							saveAsMenuItem, saveCopyAsMenuItem, exportMenuItem,
							printMenuItem);

			enableMenuItem(projectSettingsMenuItem);

			enableMenuItem(compileMenuItem, runMenuItem);
		}
	}
	
	private void enableMenuItem(MenuItem... menuItems) {
		for(MenuItem menuItem: menuItems) {
			menuItem.setDisable(false);
		}
	}
	
	private void disableMenuItem(MenuItem... menuItems) {
		for(MenuItem menuItem: menuItems) {
			menuItem.setDisable(true);
		}
	}
	
	void disableAllMenuItem() {
		for(MenuItem menuItem:menuItemList) {
			if(menuItem != aboutMenuItem && menuItem != exitMenuItem)
				menuItem.setDisable(true);
		}
	}
	
	private void buildHelpMenu() {
		
		MenuItem aboutMenuItem= new HSMenuItem(this, "About Haapus Studio", "haapus.png");
		
		helpMenu = new Menu("Help");
		helpMenu.getItems().add(aboutMenuItem);
		
		getMenus().add(helpMenu);
	}

	private void  buildCodeMenu() {
		compileMenuItem = new HSMenuItem(this, "Compile", "compile.png");
		runMenuItem = new HSMenuItem(this, "Run", "run.png");

		codeMenu = new Menu("Code");

		codeMenu.getItems().add(compileMenuItem);
		codeMenu.getItems().add(runMenuItem);

		getMenus().add(codeMenu);
	}
	
	private void buildEditMenu() {
		
		undoMenuItem = new HSMenuItem(this, "Undo", "undo.png");
		redoMenuItem = new HSMenuItem(this, "Redo", "redo.png");
		copyMenuItem = new HSMenuItem(this, "Copy", "copy.png");
		pasteMenuItem = new HSMenuItem(this, "Paste", "paste.png");
		projectSettingsMenuItem = new HSMenuItem(this, "Project Settings", "project_settings.png");
		
		editMenu = new Menu("Edit");
		
		editMenu.getItems().add(undoMenuItem);
		editMenu.getItems().add(redoMenuItem);
		editMenu.getItems().add(new SeparatorMenuItem());
		editMenu.getItems().add(copyMenuItem);
		editMenu.getItems().add(pasteMenuItem);
		editMenu.getItems().add(new SeparatorMenuItem());
		editMenu.getItems().add(projectSettingsMenuItem);
		

		getMenus().add(editMenu);
		

		
	}
	
	private void buildFileMenu() {
		
		newProjectMenuItem = new HSMenuItem(this, "New Project", "new_project.png");
		openProjectMenuItem = new HSMenuItem(this, "Open Project", "open_project.png");
		closeProjectMenuItem = new HSMenuItem(this, "Close Project", "close_project.png");
		saveMenuItem = new HSMenuItem(this, "Save", "save.png");
		saveAsMenuItem = new HSMenuItem(this, "Save As", "save_as.png");
		saveCopyAsMenuItem = new HSMenuItem(this, "Save Copy As", "save_copy_as.png");
		exportMenuItem = new HSMenuItem(this, "Export", "export.png");
		printMenuItem = new HSMenuItem(this, "Print", "print.png");
		exitMenuItem = new HSMenuItem(this, "Exit", "exit.png");

		fileMenu = new Menu("File");
		fileMenu.getItems().add(newProjectMenuItem);
		fileMenu.getItems().add(openProjectMenuItem);
		fileMenu.getItems().add(closeProjectMenuItem);
		
		fileMenu.getItems().add(new SeparatorMenuItem());
		
		fileMenu.getItems().add(saveMenuItem);
		fileMenu.getItems().add(saveAsMenuItem);
		fileMenu.getItems().add(saveCopyAsMenuItem);
		
		fileMenu.getItems().add(new SeparatorMenuItem());
		
		fileMenu.getItems().add(exportMenuItem);
		
		fileMenu.getItems().add(new SeparatorMenuItem());
		
		fileMenu.getItems().add(printMenuItem);
		fileMenu.getItems().add(new SeparatorMenuItem());
		fileMenu.getItems().add(exitMenuItem);
		
		getMenus().add(fileMenu);
	}
	
	@Override
	public void handle(ActionEvent ae) {
		
		if(ae.getSource() == exitMenuItem){
			controller.exit();
		}else if(ae.getSource() == newProjectMenuItem) {
			controller.newProject();
		}else if(ae.getSource() == closeProjectMenuItem) {
			controller.closeProject();
		}
	}

	class HSMenuItem extends MenuItem {
		
		HSMenuItem(HSMenuBar menuBar, String name, String imageName) {
			super(name);

			String imagePath = "icons/" + imageName;
			try{
				System.out.println(imagePath + " " + getClass().getResourceAsStream(imagePath));
			}catch(Exception e) {
				System.out.println("issue in: " + imagePath);
			}
			ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
			imageView.setFitHeight(24);
			imageView.setFitWidth(24);
			setGraphic(imageView);
			setOnAction(menuBar);
			
			menuItemList.add(this);
		}
	}
}