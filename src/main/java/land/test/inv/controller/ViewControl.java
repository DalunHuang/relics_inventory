package land.test.inv.controller;

import java.io.IOException;
import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ViewControl {
	
	private final StringProperty clinetSelectedItem;
	
	public ViewControl() {
		clinetSelectedItem = new SimpleStringProperty("");
	}
	
	public StringProperty clinetSelectedItem() {
		return clinetSelectedItem;
	}
	
	public <T extends Pane> T paneLoader(String path, T pane, Object controller) {
		return paneLoader(path, pane, controller, null);
	}
	
	public <T extends Pane> T paneLoader(String path, T pane, Object controller, Object param) {
		if (pane == null) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
				loader.setController(controller);
				pane = loader.load();
				if (param != null) {
					pane.setUserData(param);					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pane;
	}
	
	public void showWindow(String path) {
		showWindow(path, null);
	}
	
	public void showWindow(String path, Object fxmlControl) {
		showWindow(null, path, fxmlControl);
	}
	
	public void showWindow(Stage showStage, String path, Object fxmlControl) {
		if (Optional.ofNullable(path).orElse("").isBlank()) return;
		FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
		if (fxmlControl != null) {
			loader.setController(fxmlControl);
		}
		Scene scene = null;
		try {
			scene = new Scene(loader.load());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Stage stage = Optional.ofNullable(showStage).orElse(new Stage());
		stage.setTitle("Spring + JavaFX FXML Demo");
    	stage.setScene(scene);
    	stage.show();
	}
	
	public void closeStage(Stage stage) {
		stage.close();
	}
	
}
