package com.LRG.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
public class RetirementPlanningDto {

	private Integer retirementAge;
	private Integer investorAge;
	private String gender;
	private String spouse;
	private Integer spouseAge;
	private Double inflationrate;
	private Double initialAssets;
	private Double shortTerm;
	private Double mediumTerm;
	private Double longTerm;
	private Map<String, String> portfolioFixedWtOutput;
	private List<TickerDto> tickerDtoList;
	private Double expectedShortfall;
	private Double shortfallProbability;
	private Double targetReturn;
	private Double targetVolatility;
	private Double avgAssetValue;
	private String lifeExpectancy;
	private BigDecimal userTotalBalanceSheetAsset;
	private String contributionWithdrawalType;
	private List<AnnualisedContributionWithdrawalDto> annualisedContributionWithdrawal;
	
	private int optimizationCount;
	private Double acceptableShortfall;
	
	//Computation Fields
	private List<Double> annualContributionList;
	private List<Double> annualWithdrawlList;
	
	//output JSON elements
	private String annualisedContributionWithdrawalJson;
	private List<List<Double>> assetValuePerSimulationList;
	private String errorType;
	private String errorCode;
	private String errorDescription;
	private Boolean success;
	
	public RetirementPlanningDto() {
		super();
	}

	public Integer getRetirementAge() {
		return retirementAge;
	}

	public void setRetirementAge(Integer retirementAge) {
		this.retirementAge = retirementAge;
	}

	public Integer getInvestorAge() {
		return investorAge;
	}

	public void setInvestorAge(Integer investorAge) {
		this.investorAge = investorAge;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSpouse() {
		return spouse;
	}

	public void setSpouse(String spouse) {
		this.spouse = spouse;
	}

	public Integer getSpouseAge() {
		return spouseAge;
	}

	public void setSpouseAge(Integer spouseAge) {
		this.spouseAge = spouseAge;
	}

	public Double getInflationrate() {
		return inflationrate;
	}

	public void setInflationrate(Double inflationrate) {
		this.inflationrate = inflationrate;
	}

	public Double getInitialAssets() {
		return initialAssets;
	}

	public void setInitialAssets(Double initialAssets) {
		this.initialAssets = initialAssets;
	}

	public Double getShortTerm() {
		return shortTerm;
	}

	public void setShortTerm(Double shortTerm) {
		this.shortTerm = shortTerm;
	}

	public Double getMediumTerm() {
		return mediumTerm;
	}

	public void setMediumTerm(Double mediumTerm) {
		this.mediumTerm = mediumTerm;
	}

	public Double getLongTerm() {
		return longTerm;
	}

	public void setLongTerm(Double longTerm) {
		this.longTerm = longTerm;
	}

	public Map<String, String> getPortfolioFixedWtOutput() {
		return portfolioFixedWtOutput;
	}

	public void setPortfolioFixedWtOutput(Map<String, String> portfolioFixedWtOutput) {
		this.portfolioFixedWtOutput = portfolioFixedWtOutput;
	}

	public String getContributionWithdrawalType() {
		return contributionWithdrawalType;
	}

	public void setContributionWithdrawalType(String contributionWithdrawalType) {
		this.contributionWithdrawalType = contributionWithdrawalType;
	}

	public List<AnnualisedContributionWithdrawalDto> getAnnualisedContributionWithdrawal() {
		return annualisedContributionWithdrawal;
	}

	public void setAnnualisedContributionWithdrawal(
			List<AnnualisedContributionWithdrawalDto> annualisedContributionWithdrawal) {
		this.annualisedContributionWithdrawal = annualisedContributionWithdrawal;
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

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public List<TickerDto> getTickerDtoList() {
		return tickerDtoList;
	}

	public void setTickerDtoList(List<TickerDto> tickerDtoList) {
		this.tickerDtoList = tickerDtoList;
	}

	public Double getExpectedShortfall() {
		return expectedShortfall;
	}

	public void setExpectedShortfall(Double expectedShortfall) {
		this.expectedShortfall = expectedShortfall;
	}

	public Double getShortfallProbability() {
		return shortfallProbability;
	}

	public void setShortfallProbability(Double shortfallProbability) {
		this.shortfallProbability = shortfallProbability;
	}

	public Double getTargetReturn() {
		return targetReturn;
	}

	public void setTargetReturn(Double targetReturn) {
		this.targetReturn = targetReturn;
	}

	public Double getTargetVolatility() {
		return targetVolatility;
	}

	public void setTargetVolatility(Double targetVolatility) {
		this.targetVolatility = targetVolatility;
	}

	public Double getAvgAssetValue() {
		return avgAssetValue;
	}

	public void setAvgAssetValue(Double avgAssetValue) {
		this.avgAssetValue = avgAssetValue;
	}

	public String getLifeExpectancy() {
		return lifeExpectancy;
	}

	public void setLifeExpectancy(String lifeExpectancy) {
		this.lifeExpectancy = lifeExpectancy;
	}

	public int getOptimizationCount() {
		return optimizationCount;
	}

	public void setOptimizationCount(int optimizationCount) {
		this.optimizationCount = optimizationCount;
	}

	public Double getAcceptableShortfall() {
		return acceptableShortfall;
	}

	public void setAcceptableShortfall(Double acceptableShortfall) {
		this.acceptableShortfall = acceptableShortfall;
	}

	public String getAnnualisedContributionWithdrawalJson() {
		return annualisedContributionWithdrawalJson;
	}

	public void setAnnualisedContributionWithdrawalJson(
			String annualisedContributionWithdrawalJson) {
		this.annualisedContributionWithdrawalJson = annualisedContributionWithdrawalJson;
	}

	public List<Double> getAnnualContributionList() {
		return annualContributionList;
	}

	public void setAnnualContributionList(List<Double> annualContributionList) {
		this.annualContributionList = annualContributionList;
	}

	public List<Double> getAnnualWithdrawlList() {
		return annualWithdrawlList;
	}

	public void setAnnualWithdrawlList(List<Double> annualWithdrawlList) {
		this.annualWithdrawlList = annualWithdrawlList;
	}

	public List<List<Double>> getAssetValuePerSimulationList() {
		return assetValuePerSimulationList;
	}

	public void setAssetValuePerSimulationList(
			List<List<Double>> assetValuePerSimulationList) {
		this.assetValuePerSimulationList = assetValuePerSimulationList;
	}

	public BigDecimal getUserTotalBalanceSheetAsset() {
		return userTotalBalanceSheetAsset;
	}

	public void setUserTotalBalanceSheetAsset(BigDecimal userTotalBalanceSheetAsset) {
		this.userTotalBalanceSheetAsset = userTotalBalanceSheetAsset;
	}

}
