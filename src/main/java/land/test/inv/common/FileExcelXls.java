package land.test.inv.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class FileExcelXls implements FileTypeConversion {
	
	@Override
	public String getMimeType() {
		return "application/vnd.ms-excel";
	}

	@Override
	public List<Map<String, Object>> parse(File parseData, List<String> columns, long skipRow) {
		File xlsFile = parseData;
		List<String> columnNames = columns;
		List<Map<String, Object>> datas;
		try (InputStream fis = new FileInputStream(xlsFile);
				Workbook workbook = (Workbook) new HSSFWorkbook(fis)) 
		{
			Sheet sheet = workbook.getSheetAt(0);
			datas = StreamSupport.stream(sheet.spliterator(), false)
					.skip(skipRow)
					.map(cells -> {
						Map<String, Object> rowMap = new HashMap<String, Object>();
						for (int i = 0; i < columnNames.size(); i++) {
							String key = columnNames.get(i);
							Object val = Utils.cellValueToObject(cells.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
							rowMap.put(key, val);
						}
						return rowMap;
					}).collect(Collectors.toList());
		} catch (IOException | NullPointerException | IndexOutOfBoundsException e) {
			e.printStackTrace();
			datas = Collections.emptyList();
		}
		return datas;
	}

	@Override
	public void write(List<String> columns, List<Object[]> datas, String path) {
		List<String> columnNames = columns;
		List<Object[]> rowValues = datas;
		String newPath = path;
		
		if (!writeCheck(columnNames, rowValues, newPath, FileConversion.XLS.name())) {
			return;
		}
		
		try (Workbook workbook = new HSSFWorkbook()) {
		    Sheet sheet = workbook.createSheet("Sheet1");
		    Row headerRow = sheet.createRow(0);
		    for (int i = 0; i < columnNames.size(); i++) {
		    	headerRow.createCell(i).setCellValue(columnNames.get(i));;
		    }
		    for (int i = 1, j = 0; j < rowValues.size(); i++, j++) {
		    	Row dataRow = sheet.createRow(i);
		    	Object[] values = rowValues.get(j);
		    	for (int k = 0; k < values.length; k++) {
		    		Object objVal = values[k];
		    		String cell = objVal.getClass().getSimpleName();
		    		switch (cell) {
		    		case "Boolean":
		    			Boolean boolVal = Utils.transform(objVal, Boolean.class);
		    			dataRow.createCell(k, CellType.BOOLEAN).setCellValue(boolVal);
		    			break;
		    		case "Long": 
		    			Long longVal = Utils.transform(objVal, Long.class);
		    			dataRow.createCell(k, CellType.NUMERIC).setCellValue(longVal);
		    			break;
		    		case "Double": 
		    			Double doubleVal = Utils.transform(objVal, Double.class);
		    			dataRow.createCell(k, CellType.NUMERIC).setCellValue(doubleVal);
		    			break;
		    		case "Integer":
		    			Integer intVal = Utils.transform(objVal, Integer.class);
		    			dataRow.createCell(k, CellType.NUMERIC).setCellValue(intVal);
		    			break;
		    		case "LocalDate":
		    			LocalDate ldVal = Utils.transform(objVal, LocalDate.class);
		    			dataRow.createCell(k, CellType.NUMERIC).setCellValue(ldVal);
		    			break;
		    		case "LocalDateTime":
		    			LocalDateTime ldtVal = Utils.transform(objVal, LocalDateTime.class);
		    			dataRow.createCell(k, CellType.NUMERIC).setCellValue(ldtVal);
		    			break;
		    		case "String":
		    			String strVal = Utils.transform(objVal, String.class);
		    			dataRow.createCell(k, CellType.STRING).setCellValue(strVal);
		    			break;
		    		default:
		    			dataRow.createCell(k, CellType.BLANK);		    			
		    			break;
		    		}
		    	}
		    }
		    try (FileOutputStream fos = new FileOutputStream(newPath)) {
		        workbook.write(fos);
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}

}
