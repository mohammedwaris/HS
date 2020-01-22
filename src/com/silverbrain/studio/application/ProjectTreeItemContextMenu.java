package com.silverbrain.studio.application;

import javafx.scene.control.ContextMenu;


import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

import javafx.scene.control.MenuItem;

class ProjectTreeItemContextMenu extends ContextMenu {
		
		//DataTable dataTable;
		//ColumnMetaDataDialog columnMetaDataDialog;
		
		ProjectTreeItemContextMenu() {
			//this.dataTable = dataTable;
			
			MenuItem showMetaDataMenuItem = new MenuItem("Show column properties");
			showMetaDataMenuItem.setOnAction(new EventHandler<ActionEvent>() {
 
				@Override
				public void handle(ActionEvent event) {
					//columnMetaDataDialog = new ColumnMetaDataDialog(dataTable.getColumnMetaData());
					//columnMetaDataDialog.setTitle(dataTable.getName() + " - column properties");
					//columnMetaDataDialog.show();
					//label.setText("Select Menu Item 1");
				}
			});
			getItems().addAll(showMetaDataMenuItem);
		}
	}