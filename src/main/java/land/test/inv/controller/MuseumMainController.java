package land.test.inv.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import land.test.inv.common.FileConversion;
import land.test.inv.common.FileProcessor;
import land.test.inv.common.Utils;
import land.test.inv.dto.HistoricalRelicInfo;
import land.test.inv.dto.HistoricalRelicSelectDto;
import land.test.inv.service.HistoricalRelicService;

public class MuseumMainController implements Initializable {
	
	private ViewControl viewControl;
	private Pane museumPane;
	@FXML public Button add_relic;
	//Button updRelic;
	
	private HistoricalRelicService historicalRelicService;

	// select 
	@FXML private TextField id_of_relic; //ID
	@FXML private TextField name_of_relic; //名稱
	@FXML private MenuButton primary_category_of_relic; //主項
	@FXML private MenuButton secondary_category_of_relic; //次項	
	@FXML private MenuButton era_of_relic; //年代
	@FXML private Spinner<Integer> year_of_relic; //年歷幾年
	@FXML private DatePicker obtained_of_relic; //獲得時間
	@FXML private TextField provider_of_relic; //提供者
	
	@FXML private CheckBox sort_primary; //主項排序
	@FXML private CheckBox sort_secondary; //次項排序

	@FXML private Button select_relic;
	
	@FXML private TableView<HistoricalRelicInfo> result_view;
	@FXML private TableColumn<HistoricalRelicInfo, String> relicid_column;
	@FXML private TableColumn<HistoricalRelicInfo, String> relicname_column;
	@FXML private TableColumn<HistoricalRelicInfo, String> p_category_name_column;
	@FXML private TableColumn<HistoricalRelicInfo, String> s_category_name_column;
	@FXML private TableColumn<HistoricalRelicInfo, String> era_and_year_column;
	@FXML private TableColumn<HistoricalRelicInfo, String> obtained_column;
	@FXML private TableColumn<HistoricalRelicInfo, String> quantity_column;
	@FXML private TableColumn<HistoricalRelicInfo, String> provider_column;
	@FXML private TableColumn<HistoricalRelicInfo, String> memo_column;
	@FXML private TableColumn<HistoricalRelicInfo, Button> edit_column;
	
	@FXML private Label total_msg;
	@FXML private Button export_xls;
	@FXML private Button export_xlsx;
	@FXML private Button export_csv;
	@FXML private Button export_ods;

	public MuseumMainController() {}
	
	public MuseumMainController(ViewControl viewControl, Pane museumPane) {
		this.viewControl = viewControl;
		this.museumPane = museumPane;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// Service層實例化
		historicalRelicService = new HistoricalRelicService();
		
		// 初始化年代選單
		MuseumController.defaultEraOfRelic(era_of_relic);
		
		// 初始化年輸入框
		year_of_relic.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));
		
		// 初始化項目選單
		MuseumController.defaultCategory(primary_category_of_relic, secondary_category_of_relic);
		
		// 初始化日期選單
		MuseumController.defaultObtainedToMinguo(obtained_of_relic);
		MuseumController.defaulfObtainedListener(obtained_of_relic);
		
		// 初始化欄位
		result_view.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		result_view.widthProperty().addListener((obs, oldVal, newVal) -> {
		    double tableWidth = newVal.doubleValue();
		    relicid_column.setPrefWidth(tableWidth * 0.07);
		    relicname_column.setPrefWidth(tableWidth * 0.25);
		    p_category_name_column.setPrefWidth(tableWidth * 0.10);
		    s_category_name_column.setPrefWidth(tableWidth * 0.10);
		    era_and_year_column.setPrefWidth(tableWidth * 0.07);
		    obtained_column.setPrefWidth(tableWidth * 0.075);
		    quantity_column.setPrefWidth(tableWidth * 0.045);
		    provider_column.setPrefWidth(tableWidth * 0.10);
		    memo_column.setPrefWidth(tableWidth * 0.12);
		    edit_column.setPrefWidth(tableWidth * 0.05);
		});
		
		setColumn(relicid_column, HistoricalRelicInfo::relicId);
		setColumn(relicname_column, HistoricalRelicInfo::relicName);
		setColumn(p_category_name_column, HistoricalRelicInfo::pCategoryName);
		setColumn(s_category_name_column, HistoricalRelicInfo::sCategoryName);
		setColumn(era_and_year_column, HistoricalRelicInfo::eraAndYear);
		setColumn(obtained_column, HistoricalRelicInfo::obtained);
		setColumn(quantity_column, HistoricalRelicInfo::quantity);
		setColumn(provider_column, HistoricalRelicInfo::provider);
		setColumn(memo_column, HistoricalRelicInfo::memo);
		columnDefault(edit_column);
		edit_column.setCellFactory(column -> new TableCell<HistoricalRelicInfo, Button>() {
			private final Button btn = new Button("編輯");
			{
				btn.setOnAction(event -> {
					museumPane.setUserData(getTableView().getItems().get(getIndex()).relicId());
					viewControl.clinetSelectedItem().set(MuseumController.MUSEUM_UPDATE);
				});
				setAlignment(Pos.CENTER);
			}
			
			@Override
			protected void updateItem(Button item, boolean empty) {
				super.updateItem(item, empty);
				setGraphic(empty ? null : btn);
			}
		});

		// 初始化搜尋鍵
		select_relic.setOnAction(e -> select());

		// 初始化匯出按鈕
		String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_relics.";
		FileConversion csv = FileConversion.CSV;
		export_csv.setOnAction(e -> generateReport(csv.getTypeConversion().getMimeType(), fileName + csv.name().toLowerCase()));
		FileConversion xls = FileConversion.XLS;
		export_xls.setOnAction(e -> generateReport(xls.getTypeConversion().getMimeType(), fileName + xls.name().toLowerCase()));
		FileConversion xlsx = FileConversion.XLSX;
		export_xlsx.setOnAction(e -> generateReport(xlsx.getTypeConversion().getMimeType(), fileName + xlsx.name().toLowerCase()));
		FileConversion ods = FileConversion.ODS;
		export_ods.setOnAction(e -> generateReport(ods.getTypeConversion().getMimeType(), fileName + ods.name().toLowerCase()));

	}
	
	private void columnDefault(TableColumn<HistoricalRelicInfo, ?> column) {
		column.setReorderable(false);
		column.setResizable(false);
		column.setEditable(false);
	}
	
	private void setColumn(
			TableColumn<HistoricalRelicInfo, String> column,
			Function<HistoricalRelicInfo, String> extractor) 
	{
		columnDefault(column);
		column.setCellValueFactory(CellValue -> {
			return new SimpleStringProperty(Optional.ofNullable(extractor.apply(CellValue.getValue())).orElse(""));
		});
		column.setCellFactory(cell -> new TableCell<HistoricalRelicInfo, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(item);
				}
				setAlignment(Pos.CENTER);
			}
		});
	}

	private void select() {
		HistoricalRelicSelectDto params = new HistoricalRelicSelectDto(
				id_of_relic.getText(),
				name_of_relic.getText(),
				Utils.toInt(primary_category_of_relic.getUserData()),
				Utils.toInt(secondary_category_of_relic.getUserData()),
				Utils.toInt(era_of_relic.getUserData().toString()),
				year_of_relic.getValue(),
				obtained_of_relic.getValue(),
				provider_of_relic.getText(),
				sort_primary.isSelected(),
				sort_secondary.isSelected()
				);
		
		
		List<HistoricalRelicInfo> items = historicalRelicService.selectWithInfo(params);
		result_view.getItems().clear();
		result_view.getItems().addAll(items);
		result_view.setUserData(params);
		
		total_msg.setText("共 " + result_view.getItems().size() + " 項文物");;
		
	}

	private void generateReport(String mimeType, String fileName) {

		HistoricalRelicSelectDto params = Utils.transform(result_view.getUserData(), HistoricalRelicSelectDto.class);
		List<String> columns = List.of("文物代號", "文物名稱", "主項", "次項", "年份", "取得日期", "數量", "提供者", "備註");
		String path = "./exportFiles/" + fileName;

		System.out.println("pass1");

		if (params == null) {
			return;
		}

		List<Object[]> datas = historicalRelicService.selectWithInfo(params).stream()
				.map(info -> {
					Object[] row = new Object[columns.size()];
					row[0] = info.relicId();
					row[1] = info.relicName();
					row[2] = info.pCategoryName();
					row[3] = info.sCategoryName();
					row[4] = info.eraAndYear();
					row[5] = info.obtained();
					row[6] = info.quantity();
					row[7] = info.provider();
					row[8] = info.memo();
					return row;
				}).collect(Collectors.toList());

		FileProcessor.generateReport(columns, datas, mimeType, path);

	}
	
}
