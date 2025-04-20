package land.test.inv.common;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import land.test.inv.dto.BatchResult;
import org.apache.tika.Tika;

public class FileProcessor {
	
	// 將檔案資要轉化成entityList
	public static <T> List<T> parse(
			File parseData,
			List<String> columns,
			Function<Map<String, Object>, T> mapToEntity,
			int skipRow
	) {
		List<Map<String, Object>> parsingResult = null;
		List<T> batchList = null;
		Tika tika = new Tika();
		
		try {
			String mimeType = tika.detect(parseData);
			parsingResult = FileConversion.mimeType(mimeType).getTypeConversion().parse(parseData, columns, skipRow);
			batchList = parsingResult.stream().map(mapToEntity).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
			batchList = Collections.emptyList();
		}

		return batchList;

	}

	// 產生報表
	public static void generateReport(List<String> columns, List<Object[]> datas, String mimeType, String path) {
		FileConversion.mimeType(mimeType).getTypeConversion().write(columns, datas, path);
	}
	
}
