package land.test.inv.dto;

import java.util.ArrayList;
import java.util.List;

public class BatchResult {
	
	private int totalRecords = 0;
	private int successCount = 0;
	private int failureCount = 0;
	private List<String> errorMessages = new ArrayList<String>();
	
	public void totalRecord() {
		this.totalRecords++;
	}
	
	public void totalRecord(int totalRecord) {
		this.totalRecords += totalRecord;
	}
	
	public void success() {
		this.successCount++;
	}
	
	public void success(int successCount) {
		this.successCount += successCount;
	}
	
	public void failure() {
		this.failureCount++;
	}
	
	public void failure(int failureCount) {
		this.failureCount += failureCount;
	}
	
	public void errorMessage(String message) {
		this.errorMessages.add(message);
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public int getFailureCount() {
		return failureCount;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}
	
	@Override
	public String toString() {
		return "總筆數：";
	}

	
}
