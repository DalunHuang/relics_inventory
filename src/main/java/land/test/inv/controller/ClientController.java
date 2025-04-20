package land.test.inv.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class ClientController implements Initializable {
	
	// === 控制頁面切換的元件 ===
	ViewControl viewControl = new ViewControl();
	@FXML private BorderPane client_parent;
	private final String sidebarPath = "/fxml/sidebar/client_sidebar.fxml";
	
	public static final String HISTORICALRELIC = "HISTORICALRELIC";
	private final String historicalRelicPath = "/fxml/task/museum/museum.fxml";
	
	public static final String DASHBOARD = "DASHBOARD";
	private final String dashboardPath = "/fxml/task/dashboard/dashboard.fxml";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientSidebarController clientSidebarController = new ClientSidebarController();
		AnchorPane sidebar = null;
		clientSidebarController.setViewControl(viewControl);
		client_parent.setLeft(viewControl.paneLoader(sidebarPath, sidebar, clientSidebarController));
		viewControl.clinetSelectedItem().addListener((observableValue, oldValue, newValue) -> {
				switch (newValue) {
				case HISTORICALRELIC:
					MuseumController museumController = new MuseumController();
					AnchorPane historicalRelicPane = null;
					client_parent.setCenter(viewControl.paneLoader(historicalRelicPath, historicalRelicPane, museumController));
					
					break;
				case DASHBOARD:
				default:
//					DashboardController dashboardController = new DashboardController();
//					AnchorPane dashboardPane = null;
//					client_parent.setCenter(viewControl.paneLoader(dashboardPath, dashboardPane, dashboardController));
					break;
				}
			});
		viewControl.clinetSelectedItem().set(HISTORICALRELIC);
	}

}
