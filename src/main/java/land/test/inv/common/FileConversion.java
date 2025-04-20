package land.test.inv.common;

public enum FileConversion {
	
	HTML(new FileHtml()),
	CSV(new FileCsv()),
	XLS(new FileExcelXls()),
	XLSX(new FileExcelXlsx()),
	ODS(new FileOds());
	
	private FileTypeConversion conversion;
	
	private FileConversion(FileTypeConversion conversion) {
		this.conversion = conversion;
	}
	
	public FileTypeConversion getTypeConversion() {
		return this.conversion;
	}
	
	public static FileConversion mimeType(String mimeType) {
		
		for (FileConversion fileMimeType : FileConversion.values()) {
			if (fileMimeType.getTypeConversion().getMimeType().equals(mimeType)) {
				return fileMimeType;
			}
		}
		
		throw new IllegalArgumentException("Unexpected value: " + mimeType);
		
	}

}
