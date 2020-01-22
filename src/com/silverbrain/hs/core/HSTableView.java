package com.silverbrain.hs.core;

import javafx.collections.*;

import javafx.scene.control.TableView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Region;
import javafx.scene.control.Button;
import javafx.scene.Group;
import javafx.scene.layout.Priority;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxListCell;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.util.ArrayList;

import javafx.event.ActionEvent; 
import javafx.event.EventHandler; 


import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.Bindings;

public class HSTableView extends AnchorPane implements EventHandler<ActionEvent> {
	
	IDataSet dataSet;
	TableView tableView;
	HBox hbox;
	TreeView<String> treeView;
	ListView<Item> listView;
	SplitPane splitPane;
	
	Button selectButton;
	Button deselectButton;
	Button moveUpButton;
	Button moveDownButton;
	Button moveFirstButton;
	Button moveLastButton;
	
	TextField filterTextField; 
	Button filterButton;
	Button resetFilterButton;
	
	ObservableList<ObservableList> tableViewData;
	
	public HSTableView(IDataSet dataSet) {
		super();
		this.dataSet = dataSet;
		
		selectButton = new Button("Select All");
		deselectButton = new Button("Deselect All");
		moveUpButton = new Button("Move Up");
		moveDownButton = new Button("Move Down");
		moveFirstButton = new Button("Move First");
		moveLastButton = new Button("Move Last");
		selectButton.setMaxWidth(Double.MAX_VALUE);
		deselectButton.setMaxWidth(Double.MAX_VALUE);
		moveUpButton.setMaxWidth(Double.MAX_VALUE);
		moveDownButton.setMaxWidth(Double.MAX_VALUE);
		moveFirstButton.setMaxWidth(Double.MAX_VALUE);
		moveLastButton.setMaxWidth(Double.MAX_VALUE);
		selectButton.setOnAction(this);
		deselectButton.setOnAction(this);
		moveUpButton.setOnAction(this);
		moveDownButton.setOnAction(this);
		moveFirstButton.setOnAction(this);
		moveLastButton.setOnAction(this);
		
		VBox vbox1 = new VBox(10);
		VBox vbox2 = new VBox(10);
		VBox vbox3 = new VBox(10);
		vbox1.getChildren().addAll(selectButton, deselectButton);
		vbox2.getChildren().addAll(moveUpButton, moveDownButton);
		vbox3.getChildren().addAll(moveFirstButton, moveLastButton);
		//vbox1.setStyle("-fx-border-color: yellow;");
		vbox1.setAlignment(Pos.CENTER);
		vbox2.setAlignment(Pos.CENTER);
		vbox3.setAlignment(Pos.CENTER);
		
		HBox hbox1 = new HBox(10);
		hbox1.getChildren().addAll(vbox1, vbox2, vbox3);
		hbox1.setPadding(new Insets(10, 10, 10, 10));
		//hbox1.setStyle("-fx-border-color: red;");
		hbox1.setAlignment(Pos.CENTER);
		//hbox1.setPrefWidth(260);
		

		
		//StackPane stackPane = new StackPane();
		//stackPane.getChildren().add(hbox1);
		//stackPane.setStyle("-fx-border-color: green;");
		//stackPane.setPrefHeight(300);
		//stackPane.setAlignment(Pos.CENTER);


		
		listView = new ListView<Item>();
		createColumnListView();
		//listView.setPrefWidth(260);
		//listView.setPrefWidth(200);
		
		VBox columnVBox = new VBox();
		
		
		columnVBox.getChildren().addAll(listView, hbox1);
		//columnVBox.setMinWidth(260);
		//columnVBox.setPrefWidth(260);
		//columnVBox.setMaxWidth(260);
		VBox.setVgrow(listView, Priority.ALWAYS);
		
		
		tableView = new TableView();
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		//tableView.setTableMenuButtonVisible(true);
		
		filterTextField = new TextField();
		filterButton = new Button("Filter");
		resetFilterButton = new Button("Reset");
		filterButton.setOnAction(this);
		resetFilterButton.setOnAction(this);
		
		HBox hbox2 = new HBox(5, filterTextField, filterButton, resetFilterButton);
		hbox2.setPadding(new Insets(5, 5, 5, 5));
		VBox tableVBox = new VBox(tableView, hbox2);
		VBox.setVgrow(tableView, Priority.ALWAYS);
		HBox.setHgrow(filterTextField, Priority.ALWAYS);
		
		splitPane = new SplitPane();
		
		

		
		
		
		
		
		
		
		
		
			 
		createTableView();
		tableView.setMinWidth(350);
		//createVisibleColumnTreeView();
		
		
		
		splitPane.getItems().addAll(columnVBox, tableVBox);
		//splitPane.setDividerPosition(1, 0.3);
		//setVgrow(hbox, Priority.ALWAYS);
		
		//HBox.setHgrow(listView, Priority.ALWAYS);
		//HBox.setHgrow(tableView, Priority.ALWAYS);
		
		this.getChildren().add(splitPane);
		
		//this.setPadding(new Insets(5, 5, 5, 5));
		
		this.setTopAnchor(splitPane, 5.0);
		this.setBottomAnchor(splitPane, 5.0);
		this.setRightAnchor(splitPane, 5.0);
		this.setLeftAnchor(splitPane, 5.0);
		
		//this.setPrefWidth(600);
		//this.setPrefHeight(300);
		
		//hbox.prefWidthProperty().bind(this.widthProperty());
		//hbox.prefHeightProperty().bind(this.heightProperty());
		
		
	}
	
	
	
	public void createVisibleColumnTreeView() {
		TreeItem<String> rootItem = new TreeItem<String> ("Columns");
        rootItem.setExpanded(true);
        for (int i = 1; i < 6; i++) {
            TreeItem<String> item = new TreeItem<String> ("Message" + i);            
            rootItem.getChildren().add(item);
        }        
        treeView = new TreeView<String> (rootItem);
	}
	
	public void createColumnListView() {
		
		ArrayList<String> variableNames = dataSet.getVariableNames();
        for (int i=0; i<variableNames.size(); i++) {
            Item item = new Item(variableNames.get(i), i, true);
			
            // observe item's on property and display message if it changes:
            item.onProperty().addListener((obs, wasOn, isNowOn) -> {
				
				updateColumnView(item.getName(), wasOn, isNowOn);
                //System.out.println(item.getName() + " changed on state from "+wasOn+" to "+isNowOn);
            });
			
            listView.getItems().add(item);
        }

		
		Callback<Item, ObservableValue<Boolean>> prop = new Callback<Item, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(Item item) {
                return item.onProperty();
            }
			
        };

		Callback<ListView<Item>, ListCell<Item>> propy = CheckBoxListCell.forListView(prop);
		
		listView.setCellFactory(propy);
		
		Callback<ListView<Item>, ListCell<Item>> cellFactory = listView.getCellFactory();

		listView.setCellFactory(c -> {
			
			CheckBoxListCell<Item> cell = (CheckBoxListCell<Item>) cellFactory.call(c);
			cell.setTooltip(new Tooltip("Tooltip"));
			//System.out.println(cell);
			return cell;
		});
		
		
		
		/*
		listView.getItems().addListener(new ListChangeListener() {
			public void onChanged(ListChangeListener.Change c){
				
				ObservableList<Item> items = listView.getItems();
				ObservableList<TableColumn> tableColumns = tableView.getColumns();
				
				boolean operationDone = false;
				for(int i=0;i<items.size(); i++) {
					for(int j=0;j<tableColumns.size();j++) {
						if(items.get(i).getName().equals(tableColumns.get(j).getId())) {
							System.out.println("ch");
							if(i != j){
								TableColumn tableColumn = tableColumns.get(j);
								tableColumns.remove(j);
								tableColumns.add(i, tableColumn);
							
								//Item item = items.get(j);
								//items.remove(j);
								//items.add(i, item);
								//listView.getSelectionModel().select(i);
							}
							operationDone = true;
							break;
						}
					}//end of j
					if(operationDone == true)
						break;
				}//end of i
	
			}//end of onChanged() method
		});*/
	}
	
	public void updateColumnView(String columnName, boolean wasState, boolean nowState) {
		String id = null;
		for(int i=0;i<tableView.getColumns().size();i++) {
			id = ((TableColumn)tableView.getColumns().get(i)).getId();
			if(columnName.equalsIgnoreCase(id))
				((TableColumn)tableView.getColumns().get(i)).setVisible(nowState);
		}
	}
	
	public void createTableView() {
		for(int i=0 ; i<dataSet.getVariableCount(); i++){
            //We are using non property style for making dynamic table
            final int j = i;                
            TableColumn col = new TableColumn(dataSet.getVariableName(dataSet.getVariables().get(i)));
            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
                                        public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                                            return new SimpleStringProperty(param.getValue().get(j).toString());                        
                                        }                    
                                    });
			col.setId(dataSet.getVariableName(dataSet.getVariables(
			).get(i)));
			if(dataSet.getVariables().get(i).dataType == DataType.INTEGER || 
				dataSet.getVariables().get(i).dataType == DataType.LONG || 
				dataSet.getVariables().get(i).dataType == DataType.FLOAT ||
				dataSet.getVariables().get(i).dataType == DataType.DOUBLE ||
				dataSet.getVariables().get(i).dataType == DataType.LOCAL_DATE ||
				dataSet.getVariables().get(i).dataType == DataType.LOCAL_TIME ||
				dataSet.getVariables().get(i).dataType == DataType.LOCAL_DATE_TIME)
				col.setStyle( "-fx-alignment: CENTER-RIGHT;");
			//col.setSortable(false);
            tableView.getColumns().addAll(col); 
        }
		
		
		tableViewData = FXCollections.observableArrayList(); 
		ObservableList<String> record = null;
        for(int i=0;i<dataSet.getObservationCount();i++) {
            //Iterate Row
            record = FXCollections.observableArrayList();
            for(int j=0 ; j<dataSet.getVariableCount(); j++) {
            //Iterate Column
					Object value = dataSet.getVariableValueAt(dataSet.getVariableName(dataSet.getVariables().get(j)), i);
                    //Object value = dataSet.getVariables().get(j).getValues().get(dataSet.getSortIndex().get(i));
                    String strVal = "";
                    if(value == null)
                        record.add("");
                    else {
						strVal = dataSet.getVariableFormattedValueAt(dataSet.getVariableName(dataSet.getVariables().get(j)), i);
                        //strVal = dataSet.getVariables().get(j).getFormattedValue(value);
                        record.add(strVal);
                    }
            }

            tableViewData.add(record);
        }
        //FINALLY ADDED TO TableView
        tableView.setItems(tableViewData);
		
		tableView.getColumns().addListener(new ListChangeListener() {
			public void onChanged(ListChangeListener.Change c){
				
				ObservableList<Item> items = listView.getItems();
				ObservableList<TableColumn> tableColumns = tableView.getColumns();
				
				boolean operationDone = false;
				for(int i=0;i<tableColumns.size(); i++) {
					for(int j=0;j<items.size();j++) {
						if(tableColumns.get(i).getId().equals(items.get(j).getName())) {
							
							if(i != j){
								//System.out.println("i: " + i + " j: " + j + " " );
								Item item = items.get(j);
								items.set(j, items.get(i));
								items.set(i, item);
								//if(j>i)
									//listView.getSelectionModel().select(i);
//else
									//listView.getSelectionModel().select(i-1);
							
							}
							//operationDone = true;
							break;
						}
					}//end of j
					//if(operationDone == true)
						//break;
				}//end of i
				if(listView.getSelectionModel().getSelectedIndex() > -1)
					listView.getSelectionModel().select(listView.getSelectionModel().getSelectedIndex());
	
			}//end of onChanged() method
		});
	}
	
	public void handle(ActionEvent ae) {
		
        if(ae.getSource() == selectButton){
			
			ObservableList<Item> items = listView.getItems();
			for(Item item:items) {
				item.setOn(true);
			}
			
		}else if(ae.getSource() == deselectButton){
			ObservableList<Item> items = listView.getItems();
			for(Item item:items) {
				item.setOn(false);
			}
		}else if(ae.getSource() == moveUpButton){
			ObservableList<Item> items = listView.getItems();
			ObservableList<TableColumn> tableColumns = tableView.getColumns();
			
			Item selectedItem = listView.getSelectionModel().getSelectedItem();
			
			if(Objects.nonNull(selectedItem)) {
				for(int i=0;i<items.size(); i++) {
					if(items.get(i) == selectedItem){
						if(i!=0) {
							Item item = items.get(i);
							items.remove(i);
							items.add(i-1, item);
							listView.getSelectionModel().select(i-1);
							
							
							TableColumn tableColumn = tableColumns.get(i);
							tableColumns.remove(i);
							tableColumns.add(i-1, tableColumn);
						}
						break;
					}
				}
			}	
			
		}else if(ae.getSource() == moveDownButton){
			ObservableList<Item> items = listView.getItems();
			ObservableList<TableColumn> tableColumns = tableView.getColumns();
			
			Item selectedItem = listView.getSelectionModel().getSelectedItem();
			
			if(Objects.nonNull(selectedItem)) {
				for(int i=0;i<items.size(); i++) {
					if(items.get(i) == selectedItem){
						if(i!=items.size()-1) {
							Item item = items.get(i);
							items.remove(i);
							items.add(i+1, item);
							listView.getSelectionModel().select(i+1);
							
							TableColumn tableColumn = tableColumns.get(i);
							tableColumns.remove(i);
							tableColumns.add(i+1, tableColumn);
						}
						break;
					}
				}
			}	
		}else if(ae.getSource() == moveFirstButton){
			ObservableList<Item> items = listView.getItems();
			ObservableList<TableColumn> tableColumns = tableView.getColumns();
			
			Item selectedItem = listView.getSelectionModel().getSelectedItem();
			
			if(Objects.nonNull(selectedItem)) {
				for(int i=0;i<items.size(); i++) {
					if(items.get(i) == selectedItem){
						if(i!=0) {
							Item item = items.get(i);
							items.remove(i);
							items.add(0, item);
							listView.getSelectionModel().select(0);
							
							TableColumn tableColumn = tableColumns.get(i);
							tableColumns.remove(i);
							tableColumns.add(0, tableColumn);
						}
						break;
					}
				}
			}	
			
			
		}else if(ae.getSource() == moveLastButton){
			ObservableList<Item> items = listView.getItems();
			ObservableList<TableColumn> tableColumns = tableView.getColumns();
			
			Item selectedItem = listView.getSelectionModel().getSelectedItem();
			
			if(Objects.nonNull(selectedItem)) {
				for(int i=0;i<items.size(); i++) {
					if(items.get(i) == selectedItem){
						if(i!=items.size()-1) {
							Item item = items.get(i);
							items.remove(i);
							items.add(item);
							listView.getSelectionModel().select(items.size()-1);
							
							TableColumn tableColumn = tableColumns.get(i);
							tableColumns.remove(i);
							tableColumns.add(tableColumn);
						}
						break;
					}
				}
			}	
		}else if(ae.getSource() == filterButton){
			String conditionScript = filterTextField.getText();
			if(conditionScript.trim().isEmpty())
				return;
			
			try{
				applyFilter(conditionScript);
			}catch(ScriptException se) {
				System.out.println("Exception occured: " + se.getMessage());
			}catch(ClassCastException se) {
			}catch(Exception e) {
			}
		}else if(ae.getSource() == resetFilterButton){
			tableView.setItems(tableViewData);
			filterTextField.setText("");
		}
    }//end of handle() method 
	
	
	public void applyFilter(String conditionScript) throws ScriptException, ClassCastException, Exception {
		//ObservableList<ObservableList> data = tableView.getItems();
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		ObservableList<ObservableList> newData = FXCollections.observableArrayList(); 
		
		ObservableList<String> newRecord = null;
		Object result;
		Bindings bindings = engine.createBindings();

		for(ObservableList<String> record : tableViewData) {
			result = false;
			newRecord = FXCollections.observableArrayList();
			 
			 
			for(int i=0;i<record.size();i++) {
				 Variable variable = dataSet.getVariableByIndex(i);
				 
				 String value = record.get(i);
				 bindings.put(variable.name, value);
				 newRecord.add(value);
			 }
			 try {
				result = engine.eval(conditionScript, bindings);
				//System.out.println(result);
				if((boolean)result == true)
					newData.add(newRecord);
			 }catch(ScriptException se){
				 throw se;
			 }catch(ClassCastException cce){
				 throw cce;
			 }catch(Exception e){
				 throw e;
			 }
			 
		 }
		System.out.println("Filter applied");
		 tableView.setItems(newData);
	}
	
	public static class Item {
        private final StringProperty name = new SimpleStringProperty();
        private final BooleanProperty on = new SimpleBooleanProperty();
		
		private int position;

        public Item(String name, int position, boolean on) {
			super();
            setName(name);
            setOn(on);
			this.position = position;
        }
		
		public final int getPosition() {
			return this.position;
		}
		
		public final void setPosition(int position) {
			this.position = position;
		}

        public final StringProperty nameProperty() {
            return this.name;
        }

        public final String getName() {
            return this.nameProperty().get();
        }

        public final void setName(final String name) {
            this.nameProperty().set(name);
        }

        public final BooleanProperty onProperty() {
            return this.on;
        }

        public final boolean isOn() {
            return this.onProperty().get();
        }

        public final void setOn(final boolean on) {
            this.onProperty().set(on);
        }

        @Override
        public String toString() {
            return getName();
        }

    }
	

	
}