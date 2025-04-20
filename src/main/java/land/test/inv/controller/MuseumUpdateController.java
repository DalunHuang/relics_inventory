package land.test.inv.controller;

import java.net.URL;
import java.time.chrono.MinguoChronology;
import java.time.chrono.MinguoDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import land.test.inv.common.Utils;
import land.test.inv.dao.HistoricalRelicDao;
import land.test.inv.domain.HistoricalRelic;
import land.test.inv.dto.HistoricalRelicUpdateDto;
import land.test.inv.service.HistoricalRelicService;

public class MuseumUpdateController implements Initializable {

	private ViewControl viewControl;
	private Pane museumPane;
	
	private HistoricalRelicService historicalRelicService;
	
	// 更新資料欄位
	@FXML private Label id_of_relic;
	@FXML private TextField name_of_relic;
	@FXML private MenuButton primary_category_of_relic; //主項
	@FXML private MenuButton secondary_category_of_relic; //次項	
	@FXML private MenuButton era_of_relic;
	@FXML private Spinner<Integer> year_of_relic;
	@FXML private DatePicker obtained_of_relic;
	@FXML private Spinner<Integer> quantity_of_relic;
	@FXML private TextField provider_of_relic;
	@FXML private TextField memo_of_relic;
	
	@FXML private Label checkdata_id;
	@FXML private Label checkdata_name;
	@FXML private Label checkdata_pcategory;
	@FXML private Label checkdata_scategory;
	@FXML private Label checkdata_era;
	@FXML private Label checkdata_year;
	@FXML private Label checkdata_obtained;
	@FXML private Label checkdata_quantity;
	@FXML private Label checkdata_provider;
	@FXML private Label checkdata_memo;
		
	@FXML private Button handle_and_checkdata;
	@FXML private Button update_relic;
	@FXML private Button cancel_checkdata;
	@FXML private Label upd_result_msg;

	@FXML private Button back_mainpage;
	
	public MuseumUpdateController() {}

	public MuseumUpdateController(ViewControl viewControl, Pane museumPane) {
		this.viewControl = viewControl;
		this.museumPane = museumPane;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		historicalRelicService = new HistoricalRelicService();
		MuseumController.defaultEraOfRelic(era_of_relic);
		MuseumController.defaultCategory(primary_category_of_relic, secondary_category_of_relic);
		MuseumController.defaultObtainedToMinguo(obtained_of_relic);
		quantity_of_relic.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));
		year_of_relic.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));
		
		handle_and_checkdata.setOnAction(e -> handleAndCheckData(true));
		cancel_checkdata.setOnAction(e -> handleAndCheckData(false));
		update_relic.setOnAction(e -> updateRelic());
		
		back_mainpage.setOnAction(e -> mainPage());
		loadUserData();
		update_relic.setDisable(true);
		cancel_checkdata.setDisable(true);
	}
	
	private void mainPage() {
		viewControl.clinetSelectedItem().set(MuseumController.MUSEUM_SELECT);
	}
	
	private void handleAndCheckData(final boolean loadOrCancel) {
		
		if (loadOrCancel) {
			if (name_of_relic.getText().isBlank()) {
				upd_result_msg.setText("請輸入文物名稱");
				return;			
			}
			if (Utils.toInt(primary_category_of_relic.getUserData()) == 0) {
				upd_result_msg.setText("請輸入主項");			
				return;			
			}
			if (Utils.toInt(secondary_category_of_relic.getUserData()) == 0) {
				upd_result_msg.setText("請輸入次項");
				return;			
			}
			if (Optional.ofNullable(quantity_of_relic.getValue()).orElse(0) < 1) {
				upd_result_msg.setText("數量不可低於一或空白");
				return;
			}
			if (Optional.ofNullable(year_of_relic.getValue()).orElse(0) > 0 && 
					Utils.toInt(era_of_relic.getUserData()) == 0)
			{
				upd_result_msg.setText("請輸入年代");
				return;
			}
		}
		
		String defaultViewValue = "-";
		String blankValue = "";
		String idValue = defaultViewValue;
		String nameValue = defaultViewValue;
		String pcategoryValue = defaultViewValue;
		String scategoryValue = blankValue;
		String eraValue = defaultViewValue;
		String yearValue = blankValue;
		String obtainedValue = defaultViewValue;
		String quantityValue = defaultViewValue;
		String providerValue = defaultViewValue;
		String memoValue = defaultViewValue;
		
		if (loadOrCancel) {
			
			idValue = id_of_relic.getText();
			nameValue = name_of_relic.getText();
			pcategoryValue = primary_category_of_relic.getText();
			scategoryValue = secondary_category_of_relic.getText();
			
			if (Utils.toInt(era_of_relic.getUserData()) > 0) {
				eraValue = era_of_relic.getText();
			}
			
			Integer yearInt = Optional.ofNullable(year_of_relic.getValue()).orElse(0);
			if (yearInt > 0) {
				yearValue = yearInt + "年";				
			}
			
			if (obtained_of_relic.getValue() != null) {
				DateTimeFormatter formatter = DateTimeFormatter
						.ofPattern("Gy/M/d", Locale.TAIWAN)
						.withChronology(MinguoChronology.INSTANCE);
				obtainedValue = MinguoDate.from(obtained_of_relic.getValue()).format(formatter);
			}
			
			quantityValue = Utils.toString(quantity_of_relic.getValue());
			
			if (!provider_of_relic.getText().isBlank()) {
				providerValue = provider_of_relic.getText();
			} 
			
			if (!memo_of_relic.getText().isBlank()) {
				memoValue = memo_of_relic.getText();
			}
			
			inputsDisable(true);
			
		} else {
			inputsDisable(false);
		}
		
		
		checkdata_id.setText(idValue);
		checkdata_name.setText(nameValue);
		checkdata_pcategory.setText(pcategoryValue);
		checkdata_scategory.setText(scategoryValue);
		checkdata_era.setText(eraValue);
		checkdata_year.setText(yearValue);
		checkdata_obtained.setText(obtainedValue);
		checkdata_quantity.setText(quantityValue);
		checkdata_provider.setText(providerValue);
		checkdata_memo.setText(memoValue);
		
		upd_result_msg.setText(blankValue);
		
		handle_and_checkdata.setDisable(loadOrCancel);
		update_relic.setDisable(!loadOrCancel);
		cancel_checkdata.setDisable(!loadOrCancel);
		
	}
	
	private void inputsDisable(boolean disable) {
		id_of_relic.setDisable(disable);
		name_of_relic.setDisable(disable);
		primary_category_of_relic.setDisable(disable);
		secondary_category_of_relic.setDisable(disable);
		era_of_relic.setDisable(disable);
		year_of_relic.setDisable(disable);
		obtained_of_relic.setDisable(disable);
		quantity_of_relic.setDisable(disable);
		provider_of_relic.setDisable(disable);
		memo_of_relic.setDisable(disable);
	}
	
	private void updateRelic() {
		
		boolean updResult;
		boolean initHandleAndCheckData;
		String failMsg = "";
		
		try {
			
			Integer eraValue = Utils.toInt(era_of_relic.getUserData());
			Integer yearValue = year_of_relic.getValue();
			
			eraValue = eraValue == 0 ? null : eraValue;
			yearValue = yearValue == 0 ? null : yearValue;
			Integer quantityValue = Optional.ofNullable(
					quantity_of_relic.getValue()).orElse(0) == 0 ? null : quantity_of_relic.getValue();
			String providerValue = Optional.ofNullable(
					provider_of_relic.getText()).orElse("").isBlank() ? null : provider_of_relic.getText();
			String memoValue = Optional.ofNullable(
					memo_of_relic.getText()).orElse("").isBlank() ? null : memo_of_relic.getText();
			
			HistoricalRelicUpdateDto updParams = new HistoricalRelicUpdateDto (
					id_of_relic.getText(),
					name_of_relic.getText(),
					Utils.toInt(primary_category_of_relic.getUserData()),
					Utils.toInt(secondary_category_of_relic.getUserData()),
					eraValue,
					yearValue,
					obtained_of_relic.getValue(),
					quantityValue,
					providerValue,
					memoValue
					);
			historicalRelicService.update(updParams);
			updResult = true;
			initHandleAndCheckData = true;
			
		} catch (Exception e) {
			e.printStackTrace();
			updResult = false;
			initHandleAndCheckData = false;
			failMsg = e.getMessage();
			
		}
		
		if (updResult) {
			
			loadUserData();
			handleAndCheckData(initHandleAndCheckData);
			inputsDisable(false);
			handle_and_checkdata.setDisable(false);
			update_relic.setDisable(true);
			cancel_checkdata.setDisable(true);
			
		}
		
		upd_result_msg.setText(updResult ? "更新成功！" : "更新失敗: " + failMsg);
		
	}
	
	private void loadUserData() {
		
		HistoricalRelic historicalRelic = historicalRelicService.selectById(String.valueOf(this.museumPane.getUserData()));
		
		this.id_of_relic.setText(historicalRelic.getRelicId());
		this.name_of_relic.setText(historicalRelic.getRelicName());
		menuUpdate(era_of_relic, historicalRelic.getEra());
		menuUpdate(primary_category_of_relic, historicalRelic.getPrimaryCategory());
		menuUpdate(secondary_category_of_relic, historicalRelic.getSecondaryCategory());
		this.year_of_relic.getValueFactory().setValue(historicalRelic.getYear());
		this.obtained_of_relic.setValue(Utils.minguoLocalDate(
				historicalRelic.getObtained(), HistoricalRelicDao.RELIC_MINGUO_FORMATTER));
		this.quantity_of_relic.getValueFactory().setValue(historicalRelic.getQuantity());
		this.provider_of_relic.setText(historicalRelic.getProvider());
		this.memo_of_relic.setText(historicalRelic.getMemo());
		
	}
	
	private void menuUpdate(MenuButton mb, Integer updVal) {
		Integer targetVal = updVal == null ? 0 : updVal;
		mb.getItems().forEach(item -> {
			Integer code = Utils.toInt(item.getUserData());
			if (code.equals(targetVal)) {
				item.fire();
			}
		});
	}

}
