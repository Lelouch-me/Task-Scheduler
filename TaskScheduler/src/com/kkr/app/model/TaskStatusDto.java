package com.kkr.app.model;

import java.util.List;
import java.util.Map;

public class TaskStatusDto {

	private Boolean dataLoadingStatus = false;
	private List<String> tickersWithErrorList;
	private List<String> tickersVariationList;
	private int totalTickers;
	private int successfulTickersCount;
	private String loaderCompletionTime;
	private String loaderLoadStatus;
	private String loaderMovementStatus;
	//private Map<String, TickerDto> masterMap;
	private String loaderStartTime;
	private String lastRunStatus;
	
	public String getLastRunStatus() {
		return lastRunStatus;
	}

	public void setLastRunStatus(String lastRunStatus) {
		this.lastRunStatus = lastRunStatus;
	}

	public Boolean getDataLoadingStatus() {
		return dataLoadingStatus;
	}
	
	public void setDataLoadingStatus(Boolean dataLoadingStatus) {
		this.dataLoadingStatus = dataLoadingStatus;
	}
	
	public List<String> getTickersWithErrorList() {
		return tickersWithErrorList;
	}
	
	public void setTickersWithErrorList(List<String> tickersWithErrorList) {
		this.tickersWithErrorList = tickersWithErrorList;
	}
	
	public List<String> getTickersVariationList() {
		return tickersVariationList;
	}
	
	public void setTickersVariationList(List<String> tickersVariationList) {
		this.tickersVariationList = tickersVariationList;
	}
	
	public int getTotalTickers() {
		return totalTickers;
	}
	
	public void setTotalTickers(int totalTickers) {
		this.totalTickers = totalTickers;
	}
	
	public int getSuccessfulTickersCount() {
		return successfulTickersCount;
	}
	
	public void setSuccessfulTickersCount(int successfulTickersCount) {
		this.successfulTickersCount = successfulTickersCount;
	}

	public String getLoaderCompletionTime() {
		return loaderCompletionTime;
	}

	public void setLoaderCompletionTime(String loaderCompletionTime) {
		this.loaderCompletionTime = loaderCompletionTime;
	}

	public String getLoaderLoadStatus() {
		return loaderLoadStatus;
	}

	public void setLoaderLoadStatus(String loaderLoadStatus) {
		this.loaderLoadStatus = loaderLoadStatus;
	}

	public String getLoaderMovementStatus() {
		return loaderMovementStatus;
	}

	public void setLoaderMovementStatus(String loaderMovementStatus) {
		this.loaderMovementStatus = loaderMovementStatus;
	}
	
//	public Map<String, TickerDto> getMasterMap() {
//		return masterMap;
//	}
//
//	public void setMasterMap(Map<String, TickerDto> masterMap) {
//		this.masterMap = masterMap;
//	}

	public String getLoaderStartTime() {
		return loaderStartTime;
	}

	public void setLoaderStartTime(String loaderStartTime) {
		this.loaderStartTime = loaderStartTime;
	}
}
