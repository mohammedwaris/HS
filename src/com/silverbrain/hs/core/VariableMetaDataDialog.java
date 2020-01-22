package com.silverbrain.hs.core;


import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.TableView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import javafx.collections.ObservableList;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Scene;

import javafx.beans.property.SimpleStringProperty;

public class VariableMetaDataDialog extends Stage {

	IDataSet dataSet;
	TableView tableView;
	ObservableList<ObservableList> tableViewData;
	
	VariableMetaDataDialog(IDataSet dataSet) {
		this.dataSet = dataSet;
		tableView = new TableView();
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		createTableView();
		
		//setTitle("MetaData");
		initStyle(StageStyle.UTILITY);
		setScene(new Scene(tableView));
		setWidth(600);
		setHeight(400);
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
	}
}