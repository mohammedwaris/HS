package com.silverbrain.hs.core;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn.CellDataFeatures;

//import javafx.collections.FXCollections;
import javafx.collections.*;
import javafx.util.Callback;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleStringProperty;
import javafx.application.Platform;
import javafx.scene.layout.Priority;

import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import javafx.scene.control.ContextMenu;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

import javafx.stage.FileChooser;

import java.io.*;
import java.util.*;

//import com.aquafx_project.skin.*;
//import jfxtras.styles.jmetro8.*;

public class DataSetView extends Application implements EventHandler<ActionEvent> {

    static ArrayList<IDataSet> dataSets = new ArrayList<IDataSet>();

	TreeView treeView;
	TreeItem<String> treeItem;
	
    TabPane tabPane;
	MenuBar menuBar;
	Menu fileMenu;
	MenuItem addTableMenuItem;
	MenuItem newProjectMenuItem;
	MenuItem openProjectMenuItem;
	
	ImageView newProjectMenuItemImageView;
	ImageView openProjectMenuItemImageView;
	
	String chart;
	Stage primaryStage;
	
	FileChooser fileChooser;
	


    public static void main(String[] args)  {
		
		ArrayList<IDataSet> dts = new ArrayList<IDataSet>();
		
		if(args.length > 0) {
			IDataSet dt = null;
			
		
			for(int i=0;i<args.length;i++) {
				System.out.println(args[i]);
				if(args[i].trim().endsWith(".xdtable")) {
					dt = HS.readDataSet(args[i], true);
					dt.setName(args[i]);
				}
				else if(args[i].trim().endsWith(".dtable")) {
					dt = HS.readDataSet(args[i]);
					dt.setName(args[i]);
				}
			
				if(Objects.nonNull(dt))
					dts.add(dt);
			}
			setDataSets(dts);
		}	
		
		launch(args);
        
    }
	
	public static void setDataSets(ArrayList<IDataSet> dts) {
		dataSets = dts;
	}

    @Override
    public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		//setUserAgentStylesheet(STYLESHEET_CASPIAN);

		fileChooser = new FileChooser();
		buildMenu();
    	tabPane = new TabPane();

    	/*for(int i=0;i<dataSets.size();i++) {
    		HSTableView tableView = new HSTableView(dataSets.get(i));
			Tab tab = new Tab();
			tab.setText(String.format("%s [%s row(s) x %s column(s)]", dataSets.get(i).getName(), dataSets.get(i).getObservationCount(), dataSets.get(i).getVariableCount()));
			tab.setClosable(false);
			tab.setContent(tableView);
			tab.setContextMenu(new TabContextMenu(dataSets.get(i)));
			tableView.prefWidthProperty().bind(tabPane.widthProperty());
			tableView.prefHeightProperty().bind(tabPane.heightProperty());
			tabPane.getTabs().add(tab);
		}*/

		treeItem = new TreeItem("Current session");
		treeView = new TreeView();
		treeView.setRoot(treeItem);
		//treeView.setShowRoot(false);
		treeItem.setExpanded(true);
		treeView.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent mouseEvent) {            
				if(mouseEvent.getClickCount() == 2) {
					TreeItem<String> item = (TreeItem)treeView.getSelectionModel().getSelectedItem();
					System.out.println("Selected Text : " + item.getValue());

					if(!item.getValue().equalsIgnoreCase("current session")) {
						IDataSet dataSet = null;
						for(int i=0;i<dataSets.size();i++) {
							if(dataSets.get(i).getName().equalsIgnoreCase(item.getValue())) {
								dataSet = dataSets.get(i);
								break;
							}
						}
						HSTableView tableView = new HSTableView(dataSet);
						Tab tab = new Tab();
						tab.setText(String.format("%s [%s row(s) x %s column(s)]", dataSet.getName(), dataSet.getObservationCount(), dataSet.getVariableCount()));
						tab.setClosable(false);
						tab.setContent(tableView);
						tab.setContextMenu(new TabContextMenu(dataSet));
						tableView.prefWidthProperty().bind(tabPane.widthProperty());
						tableView.prefHeightProperty().bind(tabPane.heightProperty());
						tabPane.getTabs().add(tab);
					}
				}
			}
		});
		
		for(int i=0;i<dataSets.size();i++) {
			treeItem.getChildren().add(new TreeItem(dataSets.get(i).getName()));
		}
		
		
		
		HBox hbox = new HBox(treeView, tabPane);
		HBox.setHgrow(tabPane, Priority.ALWAYS);
		
        VBox vbox = new VBox(menuBar, hbox);
		VBox.setVgrow(hbox, Priority.ALWAYS);
		
		
        Scene scene = new Scene(vbox);
		
		//new JMetro(JMetro.Style.DARK).applyTheme(vbox);
		
        primaryStage.setScene(scene);
		primaryStage.setWidth(1000);
		primaryStage.setHeight(600);
		primaryStage.setTitle("HS Table View");
		primaryStage.show();
		


    }
	
	private void addNewTab(IDataSet dt) {
		HSTableView tableView = new HSTableView(dt);
		Tab tab = new Tab();
			tab.setText(String.format("%s [%s row(s) x %s column(s)]", dt.getName(), dt.getObservationCount(), dt.getVariableCount()));
			tab.setClosable(false);
			tab.setContent(tableView);
			tab.setContextMenu(new TabContextMenu(dt));
			tableView.prefWidthProperty().bind(tabPane.widthProperty());
			tableView.prefHeightProperty().bind(tabPane.heightProperty());
			tabPane.getTabs().add(tab);
	}
	
	private void buildMenu() {
		menuBar = new MenuBar();
		addTableMenuItem = new MenuItem("Add Table");
		
		newProjectMenuItem = new MenuItem("New Project");
		newProjectMenuItemImageView = new ImageView(new Image(getClass().getResourceAsStream("icons/new_project.png")));
		newProjectMenuItemImageView.setFitHeight(24);
		newProjectMenuItemImageView.setFitWidth(24);
		newProjectMenuItem.setGraphic(newProjectMenuItemImageView);
		
		openProjectMenuItem = new MenuItem("Open Project");
		openProjectMenuItemImageView = new ImageView(new Image(getClass().getResourceAsStream("icons/open_project.png")));
		openProjectMenuItemImageView.setFitHeight(24);
		openProjectMenuItemImageView.setFitWidth(24);
		openProjectMenuItem.setGraphic(openProjectMenuItemImageView);
		
		
		addTableMenuItem.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					
					File selectedFile = fileChooser.showOpenDialog(primaryStage);
					System.out.println(selectedFile.getPath());
					if(selectedFile != null) {
						IDataSet dataSet = null;
						if(selectedFile.getPath().endsWith(".csv")) {
							dataSet = HS.importCSV(new ParameterBuilder.ImportCSVParameter(selectedFile.getPath()));
							
						}
						else if(selectedFile.getPath().endsWith(".sas7bdat")) {
							dataSet = HS.importSAS(new ParameterBuilder.ImportSASParameter(selectedFile.getPath()));
						}
						else if(selectedFile.getPath().endsWith(".dtable")) {
							dataSet = HS.readDataSet(selectedFile.getPath());
						}
						else if(selectedFile.getPath().endsWith(".xdtable")) {
							dataSet = HS.readDataSet(selectedFile.getPath(), true);
						}
						dataSet.setName(selectedFile.getName());
						addNewTab(dataSet);
					}
				}
			});
		
		fileMenu = new Menu("File");
		fileMenu.getItems().add(newProjectMenuItem);
		fileMenu.getItems().add(openProjectMenuItem);
		fileMenu.getItems().add(new SeparatorMenuItem());
		fileMenu.getItems().add(addTableMenuItem);
		
		menuBar.getMenus().add(fileMenu); 
	}
	
	@Override
	public void handle(ActionEvent ae) {
		System.out.println("heeeo");
	}

    class TabContextMenu extends ContextMenu {
		
		IDataSet dataSet;
		VariableMetaDataDialog columnMetaDataDialog;
		
		TabContextMenu(IDataSet dataSet) {
			this.dataSet = dataSet;
			
			MenuItem showMetaDataMenuItem = new MenuItem("Show column properties");
			showMetaDataMenuItem.setOnAction(new EventHandler<ActionEvent>() {
 
				@Override
				public void handle(ActionEvent event) {
					columnMetaDataDialog = new VariableMetaDataDialog(dataSet.getVariableMetaData());
					columnMetaDataDialog.setTitle(dataSet.getName() + " - column properties");
					columnMetaDataDialog.show();
					//label.setText("Select Menu Item 1");
				}
			});
			getItems().addAll(showMetaDataMenuItem);
		}
	}

}