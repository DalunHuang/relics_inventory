package land.test.inv.controller;

import java.net.URL;
import java.time.chrono.MinguoChronology;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import land.test.inv.domain.RelicCategorys;
import land.test.inv.service.EraService;
import land.test.inv.service.RelicCategoryService;

public class MuseumController implements Initializable {
	
	private final ViewControl viewControl = new ViewControl();
	@FXML private BorderPane museumPane;
	
	private final String museumPath = "/fxml/task/museum/";
	private final String updatePath = museumPath + "museum_upd.fxml";
	private final String selectPath = museumPath + "museum_main.fxml";
	public static final String MUSEUM_UPDATE = "MUSEUM_UPDATE";
	public static final String MUSEUM_SELECT = "MUSEUM_SELECT";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		viewControl.clinetSelectedItem().addListener((observableValue, oldValue, newValue) -> {
			switch (newValue) {
			case MUSEUM_UPDATE:
				MuseumUpdateController updateController = new MuseumUpdateController(viewControl, museumPane);
				AnchorPane updatePane = null;
				museumPane.setCenter(viewControl.paneLoader(updatePath, updatePane, updateController));
				break;
			case MUSEUM_SELECT:
			default:
				MuseumMainController selectController = new MuseumMainController(viewControl, museumPane);
				AnchorPane selectPane = null;
				museumPane.setCenter(viewControl.paneLoader(selectPath, selectPane, selectController));
				break;
			}
		});
		viewControl.clinetSelectedItem().set(MUSEUM_SELECT);
	}
	
	//初始化年代選單
	static void defaultEraOfRelic(final MenuButton mb) {
		
		final EraService eraService = new EraService();
		
		Integer eraOfRelicDefaultUserData = 0;
		mb.setUserData(eraOfRelicDefaultUserData);
		MenuItem defaultItem = new MenuItem("--");
		defaultItem.setOnAction(e -> {
			mb.setText(defaultItem.getText());
			mb.setUserData(eraOfRelicDefaultUserData);
		});
		mb.getItems().add(defaultItem);
		eraService.select().forEach(eraItem -> {
			MenuItem item = new MenuItem(eraItem.getEraName());
			item.setUserData(eraItem.getEraId());
			item.setOnAction(event -> {
				mb.setText(item.getText());
				mb.setUserData(item.getUserData());
			});
			mb.getItems().add(item);
		});
		
	}
	
	//初始化類別選單
	static void defaultCategory(final MenuButton pmb, final MenuButton smb) {
		
		final RelicCategorys relicCategorys = RelicCategoryService.getCategorys();
		
		Integer relicCategoryDefaultUserData = 0;
		pmb.setUserData(relicCategoryDefaultUserData);
		smb.setUserData(relicCategoryDefaultUserData);
		MenuItem rcpMenuItem = new MenuItem("選擇主項");
		MenuItem rcsMenuItem = new MenuItem("選擇次項");
		rcsMenuItem.setOnAction(e -> {
			smb.setText(rcsMenuItem.getText());
			smb.setUserData(relicCategoryDefaultUserData);
		});
		smb.getItems().add(rcsMenuItem);
		rcpMenuItem.setOnAction(e -> {
			pmb.setText(rcpMenuItem.getText());
			pmb.setUserData(relicCategoryDefaultUserData);
			smb.setText(rcsMenuItem.getText());
			smb.setUserData(relicCategoryDefaultUserData);
			smb.getItems().clear();
			smb.getItems().add(rcsMenuItem);
		});
		pmb.getItems().add(rcpMenuItem);
		
		List<RelicCategorys> primaryCategorys = relicCategorys.getChildNodes();
		
		primaryCategorys.forEach(pcategory -> {
			MenuItem pitem = new MenuItem(pcategory.getCatgoryName());
			pitem.setUserData(pcategory.getCatgoryCode());
			pitem.setOnAction(pevent -> {
				pmb.setText(pitem.getText());
				pmb.setUserData(pitem.getUserData());
				List<RelicCategorys> secondaryCategory = pcategory.getChildNodes();
				smb.getItems().clear();
				smb.setText("選擇次項");
				smb.setUserData(relicCategoryDefaultUserData);
				MenuItem sdedafaultItem = new MenuItem("選擇次項");
				sdedafaultItem.setUserData(relicCategoryDefaultUserData);
				sdedafaultItem.setOnAction(sevent -> {
					smb.setText(sdedafaultItem.getText());
					smb.setUserData(sdedafaultItem.getUserData());
				});
				smb.getItems().add(sdedafaultItem);
				secondaryCategory.forEach(scategory -> {
					if (scategory.getCatgoryCode() != 0) {
						MenuItem sitem = new MenuItem(scategory.getCatgoryName());
						sitem.setUserData(scategory.getCatgoryCode());
						sitem.setOnAction(sevent -> {
							smb.setText(sitem.getText());
							smb.setUserData(sitem.getUserData());						
						});
						smb.getItems().add(sitem);
					}
				});
			});
			pmb.getItems().add(pitem);
		});
		
	}
	
	static void defaultObtainedToMinguo(DatePicker obtained) {
		obtained.setChronology(MinguoChronology.INSTANCE);
	}

	static void  defaulfObtainedListener(DatePicker obtained) {

		obtained.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
			if (Optional.ofNullable(newValue).orElse("").isBlank()) {
				obtained.setValue(null);
			}
		});

	}

}
