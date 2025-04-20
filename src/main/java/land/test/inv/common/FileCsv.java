package land.test.inv.common;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class FileCsv implements FileTypeConversion {
	
	@Override
	public String getMimeType() {
		return "text/csv";
	}
	
	@Override
	public List<Map<String, Object>> parse(File parseData, List<String> columns, long skipRow) {
		File csvFile = parseData;
		List<String> columnNames = columns;
		List<Map<String, Object>> datas;
		try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
			datas = StreamSupport.stream(reader.spliterator(), false)
					.skip(skipRow)
					.map(row -> {
						Map<String,Object> rowMap = new HashMap<String, Object>();
						for (int i = 0; i < columnNames.size(); i++) {
							String key = columnNames.get(i);
							String val = row[i];
							rowMap.put(key, val);
						}
						return rowMap;
					})
					.collect(Collectors.toList());
		} catch (IOException | NullPointerException | IndexOutOfBoundsException e) {
			e.printStackTrace();
			datas = Collections.emptyList();
		}
		return datas;
	}

	@Override
	public void write(List<String> columns, List<Object[]> datas, String path) {

		//System.out.println(path);
		if (!writeCheck(columns, datas, path, FileConversion.CSV.name())) {
			return;
		}
		System.out.println("start write");
		try (OutputStreamWriter osw = new OutputStreamWriter(
				new FileOutputStream(path), StandardCharsets.UTF_8))
		{
			osw.write('\uFEFF');
			try (CSVWriter writer = new CSVWriter(osw))	{
				String[] header = columns.toArray(String[]::new);
				writer.writeNext(header);
				for (int i = 0; i < datas.size(); i++) {
					String[] values = Arrays.asList(datas.get(i)).stream()
							.map(String::valueOf)
							.toArray(String[]::new) ;
					writer.writeNext(values);
				}
			}
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
		}

	}

}
