package com.LRG.model;

import java.util.Date;

public class EconomicIndicatorsDto {
	private String indicator;
	private Date month;
	private Date lastMonth;
	private Double value;
	private Double prevoiusValue;
	private Date updateDate;
	
	
	public EconomicIndicatorsDto() {
		super();
	}

	public EconomicIndicatorsDto(String indicator, Date month, Date lastMonth, Double value,Double prevoiusValue, Date updateDate) {
		super();
		this.indicator = indicator;
		this.month = month;
		this.lastMonth = lastMonth;
		this.value = value;
		this.prevoiusValue=prevoiusValue;
		this.updateDate = updateDate;
	}


	public String getIndicator() {
		return indicator;
	}


	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}


	public Date getMonth() {
		return month;
	}


	public void setMonth(Date month) {
		this.month = month;
	}
	


	public Date getLastMonth() {
		return lastMonth;
	}

	public void setLastMonth(Date lastMonth) {
		this.lastMonth = lastMonth;
	}

	public Double getValue() {
		return value;
	}
	

	public Double getPrevoiusValue() {
		return prevoiusValue;
	}


	public void setPrevoiusValue(Double prevoiusValue) {
		this.prevoiusValue = prevoiusValue;
	}


	public void setValue(Double value) {
		this.value = value;
	}


	public Date getUpdateDate() {
		return updateDate;
	}


	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
