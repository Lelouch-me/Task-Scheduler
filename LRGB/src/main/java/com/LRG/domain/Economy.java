package com.LRG.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "economy")
public class Economy {
	
	@Id
	@Column(name = "id", nullable = false)
	private int id;
	
	@Column(name = "indicator")
	private String indicator;
	
	@Column(name = "period")
	private String period;
	
	@Column(name = "update_date")
	private String updateDate;
		
	@Column(name = "currentFY")
	private Double currentFY;
	
	@Column(name = "previousFY")
	private Double previousFY;
	
	@Column(name = "change")
	private Double change;
	
	public Economy() {
		super();
	}

	public Economy(String indicator, String period, String updateDate, Double currentFY, Double previousFY, Double change) {
		super();
		
		this.indicator = indicator;
		this.period = period;
		this.updateDate=updateDate;
		this.currentFY=currentFY;
		this.previousFY=previousFY;
		this.change=change;
		
	}
	
	public Economy(int id,String indicator, String period, String updateDate, Double currentFY, Double previousFY, Double change) {
		super();
		
		this.id=id;
		this.indicator = indicator;
		this.period = period;
		this.updateDate=updateDate;
		this.currentFY=currentFY;
		this.previousFY=previousFY;
		this.change=change;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIndicator() {
		return indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public Double getCurrentFY() {
		return currentFY;
	}

	public void setCurrentFY(Double currentFY) {
		this.currentFY = currentFY;
	}

	public Double getPreviousFY() {
		return previousFY;
	}

	public void setPreviousFY(Double previousFY) {
		this.previousFY = previousFY;
	}

	public Double getChange() {
		return change;
	}

	public void setChange(Double change) {
		this.change = change;
	}
}
