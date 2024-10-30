package com.LRG.domain;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "economy_indicators")
public class EconomyIndicators {
	
	@Id
	@Column(name = "indicator")
	private String indicator;
	
	@Column(name = "month")
	private Date month;
	
	@Column(name = "value")
	private Double value;
	
	@Column(name = "update_date")
	private Date updateDate;

	public EconomyIndicators() {
		super();
	}

	public EconomyIndicators(String indicator, Date month, Double value, Date updateDate) {
		super();
		this.indicator = indicator;
		this.month = month;
		this.value = value;
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

	public Double getValue() {
		return value;
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
