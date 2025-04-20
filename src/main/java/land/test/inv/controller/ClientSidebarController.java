package land.test.inv.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ClientSidebarController implements Initializable {
	
	ViewControl viewControl;
	
	@FXML public Button dashboard_btn;
	@FXML public Button historicalrelic_btn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dashboard_btn.setOnAction(e -> taskDashboard());
		historicalrelic_btn.setOnAction(e -> taskHistoricalRelic());
	}
	
	private void taskDashboard() {
		viewControl.clinetSelectedItem().set(ClientController.DASHBOARD);
	}
	
	private void taskHistoricalRelic() {
		viewControl.clinetSelectedItem().set(ClientController.HISTORICALRELIC);
	}
	
	public void setViewControl(ViewControl viewControl) {
		this.viewControl = viewControl;
	}

}
