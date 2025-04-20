package land.test.inv.common;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface FileTypeConversion {
	
	String getMimeType();
	
	List<Map<String, Object>> parse(File parseData, List<String> columns, long skipRow);
	
	void write(List<String> columns, List<Object[]> datas, String path);
	
	// 檢查用於輸出檔案的資訊是否完整
	default boolean writeCheck(List<String> columns, List<Object[]> datas, String path, String fileExtension) {

		String newPath = path.toLowerCase();
		String extension = "." + fileExtension.toLowerCase();
		
		if (columns == null || datas == null || newPath == null) {
			return false;
		}
		if (datas.stream().anyMatch(values -> values.length != columns.size())) {
			return false;
		}
		if (!newPath.endsWith(extension)) {
			return false;
		}

		//System.out.println("pass3");
		
		return true;
	}

}
