package com.kkr.app.model;

public class TaskLogDto {
	private Long taskId;
	private int totalRecordCount;
	private int notIngestedRecordCount;
	private int ingestedRecordCount;
	private int tickerFullRefreshCount;
	private String errorMessage;
	
	public TaskLogDto(Long taskId, String errorMessage){
		this.taskId = taskId;
		this.errorMessage = errorMessage;
	}
	
	public TaskLogDto(Long taskId, int newRecordCount, int duplicateRecordCount, int ingestedRecordCount, int tickerFullRefresh, String errorMessage){
		this.taskId = taskId;
		this.totalRecordCount = newRecordCount;
		this.notIngestedRecordCount = duplicateRecordCount;
		this.ingestedRecordCount = ingestedRecordCount;
		this.tickerFullRefreshCount = tickerFullRefresh;
		this.errorMessage = errorMessage;
	}
	
	public Long getTaskId() {
		return taskId;
	}
	
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	
	public int getTotalRecordCount() {
		return totalRecordCount;
	}
	
	public void setTotalRecordCount(int newRecordCount) {
		this.totalRecordCount = newRecordCount;
	}
	
	public int getNotIngestedRecordCount() {
		return notIngestedRecordCount;
	}
	
	public void setNotIngestedRecordCount(int duplicateRecordCount) {
		this.notIngestedRecordCount = duplicateRecordCount;
	}
	
	public int getIngestedRecordCount() {
		return ingestedRecordCount;
	}
	
	public void setIngestedRecordCount(int ingestedRecordCount) {
		this.ingestedRecordCount = ingestedRecordCount;
	}
	
	public int getTickerFullRefreshCount() {
		return tickerFullRefreshCount;
	}

	public void setTickerFullRefreshCount(int tickerFullRefreshCount) {
		this.tickerFullRefreshCount = tickerFullRefreshCount;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "TaskLogDto [taskId=" + taskId + ", newRecordCount=" + totalRecordCount + ", duplicateRecordCount="
						+ notIngestedRecordCount + ", ingestedRecordCount=" + ingestedRecordCount + ", errorMessage="
						+ errorMessage + "]";
	}
}
