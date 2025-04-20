package land.test.inv.common;

import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableCell;
import org.odftoolkit.odfdom.doc.table.OdfTableRow;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FileOds implements FileTypeConversion {
	
	@Override
	public String getMimeType() {
		return "application/vnd.oasis.opendocument.spreadsheet";
	}

	@Override
	public List<Map<String, Object>> parse(File parseData, List<String> columns, long skipRow) {
		List<Map<String, Object>> datas;

		try (OdfSpreadsheetDocument ods = OdfSpreadsheetDocument.loadDocument(parseData)) {

			List<OdfTable> tables = ods.getSpreadsheetTables();

			if (tables.isEmpty()) return Collections.emptyList();

			OdfTable sheet = tables.get(0);
			List<OdfTableRow> rows = sheet.getRowList();

			datas = IntStream.range(0, sheet.getRowCount())
					.skip(skipRow)
					.mapToObj(rowIndex -> {
						Map<String, Object> rowData = new HashMap<>();
						OdfTableRow row = rows.get(rowIndex);
						for (int i = 0; i < columns.size(); i++) {
							rowData.put(columns.get(i), row.getCellByIndex(i).getStringValue());
						}
						return rowData;
					}).collect(Collectors.toList());

		} catch (Exception e) {
			e.printStackTrace();
			datas = Collections.emptyList();
		}

		return datas;
	}

	@Override
	public void write(List<String> columns, List<Object[]> datas, String path) {
	    
	    if (!writeCheck(columns, datas, path, FileConversion.ODS.name())) {
			return;
		}

		try (OdfSpreadsheetDocument ods = OdfSpreadsheetDocument.newSpreadsheetDocument()) {

			OdfTable sheet = ods.getSpreadsheetTables().get(0);
			sheet.setTableName("Sheet1");

			// 加入標頭
			OdfTableRow headerRow = sheet.getRowByIndex(0);
			for (int col = 0; col < columns.size(); col++) {
				headerRow.getCellByIndex(col).setStringValue(columns.get(col));
			}

			datas.forEach(rowData -> {
				OdfTableRow row = sheet.appendRow();
				for (int col = 0; col < columns.size(); col++) {
					row.getCellByIndex(col).setStringValue(Optional.ofNullable(Utils.toString(rowData[col])).orElse(""));
				}
			});

			ods.save(new File(path));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
