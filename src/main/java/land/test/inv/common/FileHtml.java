package land.test.inv.common;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FileHtml implements FileTypeConversion {
	
	@Override
	public String getMimeType() {
		return "text/html";
	}

	@Override
	public List<Map<String, Object>> parse(File parseData, List<String> columns, long skipRow) {
		File htmlFile = parseData;
		List<String> columnNames = columns;
		List<Map<String, Object>> datas;
		try {
			Document doc = Jsoup.parse(htmlFile, StandardCharsets.UTF_8.name());
			Element table = doc.select("table").first();
			if (table == null) {
				throw new NoSuchElementException(htmlFile.getName() + " 文件中未找到 table 元素");
			}
			Elements rows = table.select("tr");
			if (rows.isEmpty()) {
				throw new NoSuchElementException(htmlFile.getName() + " 文件中未找到 tr 元素");
			}
			datas = StreamSupport.stream(rows.spliterator(), false)
					.skip(skipRow)
					.map(row -> {
						Map<String, Object> rowMap = new HashMap<String, Object>();
						Elements cells = row.select("th, td");
						for (int i = 0; i < columnNames.size(); i++) {
							String key = columnNames.get(i);
							String val = cells.get(i).text();
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
	    
	    if (!writeCheck(columns, datas, path, FileConversion.HTML.name())) {
			return;
		}
	    
	    try {

	        Document doc = Jsoup.parse("<html></html>");
	        Element body = doc.body();
			body = doc.appendElement("body");
	        Element table = body.appendElement("table");
	        Element headerRow = table.appendElement("tr");

	        for (String colName : columns) {
	            headerRow.appendElement("th").text(colName);
	        }
	        for (Object[] row : datas) {
	            Element dataRow = table.appendElement("tr");
	            for (Object cell : row) {
	                dataRow.appendElement("td").text(String.valueOf(cell));
	            }
	        }
	        Files.write(Paths.get(path), doc.html().getBytes(StandardCharsets.UTF_8));
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
}
