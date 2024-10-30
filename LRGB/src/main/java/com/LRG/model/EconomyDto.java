package com.LRG.model;

public class EconomyDto {
	
	private int id;
	private String indicator;
	private String period;
	private String updateDate;
	private Double currentFY;
	private Double previousFY;
	private Double change;
		
	public EconomyDto() {
		super();
	}

	public EconomyDto(String indicator, String period, String updateDate, Double currentFY, Double previousFY, Double change) {
		super();
		
		this.indicator = indicator;
		this.period = period;
		this.updateDate=updateDate;
		this.currentFY=currentFY;
		this.previousFY=previousFY;
		this.change=change;		
	}
	
	public EconomyDto(int id,String indicator, String period, String updateDate, Double currentFY, Double previousFY, Double change) {
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
