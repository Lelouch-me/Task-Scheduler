package com.LRG.model;

public class IndexDto {

	private String id;
	
	private String dateTime;
	
	private double capitalValue;
	
	private double deviation;
	
	private double deviationPercent;
	
	public IndexDto() {
		super();
	}
	
	public IndexDto(String id, String dateTime, double capitalValue, double deviation, double deviationPercent) {
		this.id = id;
		this.dateTime = dateTime;
		this.capitalValue = capitalValue;
		this.deviation = deviation;
		this.deviationPercent = deviationPercent;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getDateTime() {
		return dateTime;
	}
	
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	public double getCapitalValue() {
		return capitalValue;
	}

	public void setCapitalValue(double capitalValue) {
		this.capitalValue = capitalValue;
	}
	
	public Double getDeviation() {
		return deviation;
	}
	
	public void setDeviation(Double deviation) {
		this.deviation = deviation;
	}
	
	public Double getDeviationPercent() {
		return deviationPercent;
	}
	
	public void setDeviationPercent(Double deviationPercent) {
		this.deviationPercent = deviationPercent;
	}
}
