package com.LRG.model;

import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
public class OptimizerResponse {
	
	//Used for individual chart calls
	private ChartData portfolioAllocation;
	private ChartData performance;
	private ChartData riskReturnProfile;
	private ChartData historicalAnnualReturn;
	private ChartData returnsDistribution;
	private ChartData rollingCorrelation;
	private ChartData regression;
	private ChartData allocationHistory;
	private ChartData correlationMatrix;
	private ChartData lossDistribution;
	private ChartData efficientFrontier;
	
	//Used for main optimizer call
	private boolean usingAllData = false;
	private Boolean portfolioSaved = null;
	private String errorType;
	private String errorCode;
	private String errorDescription;
	private boolean success = true;
	
	//Used for Retirement plan calculation
	private Map<String, String> optimizedWTMap;
	
	//Used for balanceSheet
	private Double portfolioReturn;
	
	
	public ChartData getPortfolioAllocation() {
		return portfolioAllocation;
	}

	public void setPortfolioAllocation(ChartData portfolioAllocation) {
		this.portfolioAllocation = portfolioAllocation;
	}

	public ChartData getPerformance() {
		return performance;
	}

	public void setPerformance(ChartData performance) {
		this.performance = performance;
	}

	public ChartData getRiskReturnProfile() {
		return riskReturnProfile;
	}

	public void setRiskReturnProfile(ChartData riskReturnProfile) {
		this.riskReturnProfile = riskReturnProfile;
	}

	public ChartData getHistoricalAnnualReturn() {
		return historicalAnnualReturn;
	}

	public void setHistoricalAnnualReturn(ChartData historicalAnnualReturn) {
		this.historicalAnnualReturn = historicalAnnualReturn;
	}

	public ChartData getReturnsDistribution() {
		return returnsDistribution;
	}

	public void setReturnsDistribution(ChartData returnsDistribution) {
		this.returnsDistribution = returnsDistribution;
	}

	public ChartData getRollingCorrelation() {
		return rollingCorrelation;
	}

	public void setRollingCorrelation(ChartData rollingCorrelation) {
		this.rollingCorrelation = rollingCorrelation;
	}

	public ChartData getRegression() {
		return regression;
	}

	public void setRegression(ChartData regression) {
		this.regression = regression;
	}

	public ChartData getAllocationHistory() {
		return allocationHistory;
	}

	public void setAllocationHistory(ChartData allocationHistory) {
		this.allocationHistory = allocationHistory;
	}

	public ChartData getCorrelationMatrix() {
		return correlationMatrix;
	}

	public void setCorrelationMatrix(ChartData correlationMatrix) {
		this.correlationMatrix = correlationMatrix;
	}

	public ChartData getLossDistribution() {
		return lossDistribution;
	}

	public void setLossDistribution(ChartData lossDistribution) {
		this.lossDistribution = lossDistribution;
	}

	public boolean isUsingAllData() {
		return usingAllData;
	}

	public void setUsingAllData(boolean usingAllData) {
		this.usingAllData = usingAllData;
	}

	public Boolean getPortfolioSaved() {
		return portfolioSaved;
	}

	public void setPortfolioSaved(Boolean portfolioSaved) {
		this.portfolioSaved = portfolioSaved;
	}

	public boolean getSuccess() {
		return success;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public ChartData getEfficientFrontier() {
		return efficientFrontier;
	}

	public void setEfficientFrontier(ChartData efficientFrontier) {
		this.efficientFrontier = efficientFrontier;
	}

	@Override
	public String toString() {
		return "OptimizerResponse [allocationHistory=" + allocationHistory
				+ ", correlationMatrix=" + correlationMatrix
				+ ", efficientFrontier=" + efficientFrontier + ", errorCode="
				+ errorCode + ", errorDescription=" + errorDescription
				+ ", errorType=" + errorType + ", historicalAnnualReturn="
				+ historicalAnnualReturn + ", lossDistribution="
				+ lossDistribution + ", performance=" + performance
				+ ", portfolioAllocation=" + portfolioAllocation
				+ ", regression=" + regression + ", returnsDistribution="
				+ returnsDistribution + ", riskReturnProfile="
				+ riskReturnProfile + ", rollingCorrelation="
				+ rollingCorrelation + ", success=" + success
				+ ", usingAllData=" + usingAllData + "]";
	}

	public Map<String, String> getOptimizedWTMap() {
		return optimizedWTMap;
	}

	public void setOptimizedWTMap(Map<String, String> optimizedWTMap) {
		this.optimizedWTMap = optimizedWTMap;
	}

	public Double getPortfolioReturn() {
		return portfolioReturn;
	}

	public void setPortfolioReturn(Double portfolioReturn) {
		this.portfolioReturn = portfolioReturn;
	}

}
