package land.test.inv;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import land.test.inv.controller.ClientController;

/**
 * JavaFX FxApplication
 */
public class FxApplication extends Application {

	public static void runFxApp(String[] args) {
        launch(args);
    }

    @Override
    public void start(@SuppressWarnings("exports") Stage stage) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/layout/client/client.fxml"));
    	fxmlLoader.setController(new ClientController());
    	Parent root = fxmlLoader.load();
    	
    	Scene scene = new Scene(root);
    	stage.setTitle("文物");
    	stage.setScene(scene);
    	stage.show();
    }

}