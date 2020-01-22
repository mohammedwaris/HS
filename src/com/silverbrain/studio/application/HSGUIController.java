package com.silverbrain.studio.application;

import javafx.application.Platform;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.MenuBar;
import javafx.event.ActionEvent; 
import javafx.event.EventHandler; 

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import java.io.*;

class HSGUIController {
	
	HSGUI hsgui;

	VBox rootLayout;
	
	SplitPane splitPane;
	TabPane leftTabPane;
	TabPane rightTabPane;
	
	Tab projectTab;
	Tab dataTab;
	
	TreeView projectTreeView;
	TreeView dataTreeView;
	
	TreeItem projectTreeItem;
	TreeItem projectFileTreeItem;

	TreeItem moduleTreeItem;
	TreeItem taskTreeItem;

	ProjectTreeItemContextMenu projectTreeItemContextMenu;

	TreeItem dataTreeItem;
	
	HSMenuBar hsMenuBar;

	AppState appState;

	HSProject hsProject;
	
	HSGUIController(HSGUI hsgui) {
		this.hsgui = hsgui;


		hsMenuBar = HSMenuBar.getMenuBar(this);
		rootLayout = new VBox(hsMenuBar);

		appState = AppState.IDLE;

		hsMenuBar.update();

		
	}
	
	public void newProject() {
		leftTabPane = new TabPane();

		projectTreeView = new TreeView();
		
		projectFileTreeItem = new TreeItem("MyProject.java");
		projectTreeItem = new TreeItem("New_Project");
		projectTreeItem.setExpanded(true);
		projectTreeItem.getChildren().addAll(projectFileTreeItem);

		projectTreeView.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent mouseEvent) {            
				if(mouseEvent.getButton() == MouseButton.SECONDARY) {
					TreeItem<String> item = (TreeItem)projectTreeView.getSelectionModel().getSelectedItem();
					System.out.println("Selected Text : " + item.getValue());

					if(item == projectTreeItem) {
						projectTreeItemContextMenu = new ProjectTreeItemContextMenu();
						projectTreeItemContextMenu.show(projectTreeView, mouseEvent.getScreenX(), mouseEvent.getScreenY());
						
					}else{
						projectTreeItemContextMenu.hide();
					}
				}else{
						projectTreeItemContextMenu.hide();
				}
			}
		});
		//projectTreeItem.setContextMenu(new ProjectTreeItemContextMenu());

		
		projectTreeView.setRoot(projectTreeItem);
		
		projectTab = new Tab();
		projectTab.setText("Project");
		projectTab.setClosable(false);
		projectTab.setContent(projectTreeView);
		//projecttab.setContextMenu(new TabContextMenu(dataTables.get(i)));
		//tableView.prefWidthProperty().bind(tabPane.widthProperty());
		//tableView.prefHeightProperty().bind(tabPane.heightProperty());
		
		dataTreeItem = new TreeItem("DataTables");
		dataTreeView = new TreeView();
		dataTreeView.setRoot(dataTreeItem);
		
		dataTab = new Tab();
		dataTab.setText("Data");
		dataTab.setClosable(false);
		dataTab.setContent(dataTreeView);
		
		leftTabPane.getTabs().add(projectTab);
		leftTabPane.getTabs().add(dataTab);
		
		
		rightTabPane = new TabPane();
		HSCodeEditor codeEditor = new HSCodeEditor();
		
		Tab codeTab = new Tab();
		codeTab.setText("MyProject.java");
		codeTab.setClosable(true);
		codeTab.setContent(codeEditor.webview);
		//codeTab.setPadding(new Insets(5, 5, 5, 5));
		//projecttab.setContextMenu(new TabContextMenu(dataTables.get(i)));
		//tableView.prefWidthProperty().bind(tabPane.widthProperty());
		//tableView.prefHeightProperty().bind(tabPane.heightProperty());
		rightTabPane.getTabs().add(codeTab);
		
		
		
		splitPane = new SplitPane();
		//splitPane.setPadding(new Insets(10, 10, 10, 10));
		splitPane.getItems().addAll(leftTabPane, rightTabPane);

		rootLayout.getChildren().addAll(splitPane);
		VBox.setVgrow(splitPane, Priority.ALWAYS);

		appState = AppState.ACTIVE_NOT_SAVED;
		hsMenuBar.update();

		try {
		hsProject = new HSProject(createTempDir("HSProject").getPath());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void openProject() {

	}

	public void save() {

	}

	public void closeProject() {
		if(appState == AppState.ACTIVE_SAVED) {
			hsProject.close();
			splitPane.setVisible(true);

		}else if(appState == AppState.ACTIVE_NOT_SAVED) {
			Dialog dialog = new Dialog();
			dialog.show();
			dialog.addListener( buttonClicked -> {
				if(buttonClicked == Dialog.YES_BUTTON) {
					dialog.hide();
					hsProject.save();
					hsProject.close();
					splitPane.setVisible(true);
				}else if(buttonClicked == Dialog.NO_BUTTON) {
					dialog.hide();
					hsProject.close();
					splitPane.setVisible(true);
				}else if(buttonClicked == Dialog.CANCEL_BUTTON) {
					dialog.hide();
					dialog.hide();
				}
			});
		}
	}

	public static File createTempDir(String prefix) throws IOException
  	{
    String tmpDirStr = System.getProperty("java.io.tmpdir");
    if (tmpDirStr == null) {
      throw new IOException(
        "System property 'java.io.tmpdir' does not specify a tmp dir");
    }
    
    File tmpDir = new File(tmpDirStr);
    if (!tmpDir.exists()) {
      boolean created = tmpDir.mkdirs();
      if (!created) {
        throw new IOException("Unable to create tmp dir " + tmpDir);
      }
    }
    
    File resultDir = null;
    int suffix = (int)System.currentTimeMillis();
    int failureCount = 0;
    do {
      resultDir = new File(tmpDir, prefix + suffix % 10000);
      suffix++;
      failureCount++;
    }
    while (resultDir.exists() && failureCount < 50);
    
    if (resultDir.exists()) {
      throw new IOException(failureCount + 
        " attempts to generate a non-existent directory name failed, giving up");
    }
    boolean created = resultDir.mkdir();
    if (!created) {
      throw new IOException("Failed to create tmp directory");
    }
    
    return resultDir;
  }

	public VBox getRootLayout() {
		return this.rootLayout;
	}



	public void exit() {
		Platform.exit();
        System.exit(0);
	}


}