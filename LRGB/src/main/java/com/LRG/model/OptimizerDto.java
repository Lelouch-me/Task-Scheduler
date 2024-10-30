package com.LRG.model;

import java.util.List;
import java.util.Map;

public class OptimizerDto {
	
	private List<String> tickers; //Does not include MONEY_MKT_FUNDS
	private Map<String, Object> inputTimeSeries; //Does not include MONEY_MKT_FUNDS
	private String optModule;
	private double initialValue;
	private double dailyMMFReturn;
	private double annRetCash;
	private double slippage;
	private String benchmark;
	private String showChart;
	private Map<String, String> outputStats;
	private Map<String, Object> outputTimeSeries;
	private List<Double> portfolioReturns;
	private double cVarLevel;
	private List<List<String>> frontier;
	private List<String> portfolios;
	private OptimizerRequest request;
	
	//Housekeeping
	private String requestId;
	private String deviceId;
	
	public List<String> getTickers() {
		return tickers;
	}

	public void setTickers(List<String> tickers) {
		this.tickers = tickers;
	}

	public Map<String, Object> getInputTimeSeries() {
		return inputTimeSeries;
	}

	public void setInputTimeSeries(Map<String, Object> inputTimeSeries) {
		this.inputTimeSeries = inputTimeSeries;
	}

	public String getOptModule() {
		return optModule;
	}

	public void setOptModule(String optModule) {
		this.optModule = optModule;
	}

	public double getInitialValue() {
		return initialValue;
	}

	public void setInitialValue(double initialValue) {
		this.initialValue = initialValue;
	}

	public double getDailyMMFReturn() {
		return dailyMMFReturn;
	}

	public void setDailyMMFReturn(double dailyMMFReturn) {
		this.dailyMMFReturn = dailyMMFReturn;
	}

	public double getAnnRetCash() {
		return annRetCash;
	}

	public void setAnnRetCash(double annRetCash) {
		this.annRetCash = annRetCash;
	}

	public String getBenchmark() {
		return benchmark;
	}

	public void setBenchmark(String benchmark) {
		this.benchmark = benchmark;
	}

	public Map<String, String> getOutputStats() {
		return outputStats;
	}

	public void setOutputStats(Map<String, String> outputStats) {
		this.outputStats = outputStats;
	}

	public Map<String, Object> getOutputTimeSeries() {
		return outputTimeSeries;
	}

	public void setOutputTimeSeries(Map<String, Object> outputTimeSeries) {
		this.outputTimeSeries = outputTimeSeries;
	}

	public List<Double> getPortfolioReturns() {
		return portfolioReturns;
	}

	public void setPortfolioReturns(List<Double> portfolioReturns) {
		this.portfolioReturns = portfolioReturns;
	}

	public double getcVarLevel() {
		return cVarLevel;
	}

	public void setcVarLevel(double cVarLevel) {
		this.cVarLevel = cVarLevel;
	}

	public List<List<String>> getFrontier() {
		return frontier;
	}

	public void setFrontier(List<List<String>> frontier) {
		this.frontier = frontier;
	}
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public OptimizerRequest getRequest() {
		return request;
	}

	public void setRequest(OptimizerRequest request) {
		this.request = request;
	}

	@Override
	public String toString() {
		return "OptimizerDto [tickers=" + tickers + ", inputTimeSeries="
				+ inputTimeSeries + ", optModule=" + optModule
				+ ", initialValue=" + initialValue + ", dailyMMFReturn="
				+ dailyMMFReturn + ", annRetCash=" + annRetCash
				+ ", benchmark=" + benchmark + ", outputStats=" + outputStats
				+ ", outputTimeSeries=" + outputTimeSeries
				+ ", portfolioReturns=" + portfolioReturns + ", cVarLevel="
				+ cVarLevel + ", frontier=" + frontier + ", requestId="
				+ requestId + ",slippage=" + slippage +",showChart=" +showChart+ ", deviceId=" + deviceId + "]";
	}

	public void setSlippage(double slippage) {
		this.slippage = slippage;
	}

	public double getSlippage() {
		return slippage;
	}

	public void setShowChart(String showChart) {
		this.showChart = showChart;
	}

	public String getShowChart() {
		return showChart;
	}

	public List<String> getPortfolios() {
		return portfolios;
	}

	public void setPortfolios(List<String> portfolios) {
		this.portfolios = portfolios;
	}

}
