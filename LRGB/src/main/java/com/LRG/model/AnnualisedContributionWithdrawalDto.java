package com.LRG.model;

public class AnnualisedContributionWithdrawalDto {
	String name;
	String type;
	Integer startYear;
	Integer endYear;
	Double annualAmount;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getStartYear() {
		return startYear;
	}
	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}
	public Integer getEndYear() {
		return endYear;
	}
	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
	}
	public Double getAnnualAmount() {
		return annualAmount;
	}
	public void setAnnualAmount(Double annualAmount) {
		this.annualAmount = annualAmount;
	}	
}
