package com.LRG.model;

import java.math.BigDecimal;
import java.util.List;

//import org.codehaus.jackson.map.annotate.JsonSerialize;

//@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
public class OptimizerRequest {
	
	private List<TickerDto> inputTickers;
	private BigDecimal lookBack;
	private BigDecimal cVarLevel;
	private BigDecimal targetRet;
	private BigDecimal annualReturnOnCash;
	private BigDecimal slippage;
	private BigDecimal rebalance;
	private String benchmark;
	private String showChart;
	private String startDate;
	private String endDate;
	private String optimMode;
	private String optType;
	private long portfolioId;
	private double initialValue;
	private String runEfficientFrontier ;
	private String portfolioName ;
	private List<String> inputPortfolios;
	private Double portfolioCurrentValue;
	private Double portfolioCurrentCapital;
	
	//Used for Retirement plan calculation
	private Boolean isRetirementPlanOptimzation = false;
	
	public List<TickerDto> getInputTickers() {
		return inputTickers;
	}

	public void setInputTickers(List<TickerDto> inputTickers) {
		this.inputTickers = inputTickers;
	}

	public BigDecimal getLookBack() {
		return lookBack;
	}

	public void setLookBack(BigDecimal lookBack) {
		this.lookBack = lookBack;
	}

	public BigDecimal getcVarLevel() {
		return cVarLevel;
	}

	public void setcVarLevel(BigDecimal cVarLevel) {
		this.cVarLevel = cVarLevel;
	}

	public BigDecimal getTargetRet() {
		return targetRet;
	}

	public void setTargetRet(BigDecimal targetRet) {
		this.targetRet = targetRet;
	}

	public BigDecimal getAnnualReturnOnCash() {
		return annualReturnOnCash;
	}

	public void setAnnualReturnOnCash(BigDecimal annualReturnOnCash) {
		this.annualReturnOnCash = annualReturnOnCash;
	}

	public BigDecimal getRebalance() {
		return rebalance;
	}

	public void setRebalance(BigDecimal rebalance) {
		this.rebalance = rebalance;
	}

	public String getBenchmark() {
		return benchmark;
	}

	public void setBenchmark(String benchmark) {
		this.benchmark = benchmark;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getOptimMode() {
		return optimMode;
	}

	public void setOptimMode(String optimMode) {
		this.optimMode = optimMode;
	}

	public String getOptType() {
		return optType;
	}

	public void setOptType(String optType) {
		this.optType = optType;
	}

	public long getPortfolioId() {
		return portfolioId;
	}

	public void setPortfolioId(long portfolioId) {
		this.portfolioId = portfolioId;
	}

	public double getInitialValue() {
		return initialValue;
	}

	public void setInitialValue(double initialValue) {
		this.initialValue = initialValue;
	}

	public String getRunEfficientFrontier() {
		return runEfficientFrontier;
	}

	public void setRunEfficientFrontier(String runEfficientFrontier) {
		this.runEfficientFrontier = runEfficientFrontier;
	}

	public void setSlippage(BigDecimal slippage) {
		this.slippage = slippage;
	}

	public BigDecimal getSlippage() {
		return slippage;
	}

	public void setShowChart(String showChart) {
		this.showChart = showChart;
	}

	public String getShowChart() {
		return showChart;
	}
	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public List<String> getInputPortfolios() {
		return inputPortfolios;
	}

	public void setInputPortfolios(List<String> inputPortfolios) {
		this.inputPortfolios = inputPortfolios;
	}

	public Double getPortfolioCurrentValue() {
		return portfolioCurrentValue;
	}

	public void setPortfolioCurrentValue(Double portfolioCurrentValue) {
		this.portfolioCurrentValue = portfolioCurrentValue;
	}

	public Double getPortfolioCurrentCapital() {
		return portfolioCurrentCapital;
	}

	public void setPortfolioCurrentCapital(Double portfolioCurrentCapital) {
		this.portfolioCurrentCapital = portfolioCurrentCapital;
	}

	@Override
	public String toString() {
		return "OptimizerRequest [annualReturnOnCash=" + annualReturnOnCash
				+ ", benchmark=" + benchmark + ", cVarLevel=" + cVarLevel
				+ ", endDate=" + endDate + ", initialValue=" + initialValue
				+ ", inputTickers=" + inputTickers + ", lookBack=" + lookBack
				+ ", optType=" + optType + ", optimMode=" + optimMode
				+ ", portfolioId=" + portfolioId + ", portfolioName="
				+ portfolioName + ", rebalance=" + rebalance
				+ ", runEfficientFrontier=" + runEfficientFrontier
				+ ", showChart=" + showChart + ", slippage=" + slippage
				+ ", startDate=" + startDate + ", targetRet=" + targetRet + "]";
	}

	public Boolean getIsRetirementPlanOptimzation() {
		return isRetirementPlanOptimzation;
	}

	public void setIsRetirementPlanOptimzation(Boolean isRetirementPlanOptimzation) {
		this.isRetirementPlanOptimzation = isRetirementPlanOptimzation;
	}
	
}