package com.LRG.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "idx")
public class Index {

	
	@Column(name = "id", nullable = false)
	private String id;
	
	@Column(name = "date_time")
	private String dateTime;
	
	@Id
	@Column(name = "capital_value")
	private double capitalValue;
	
	@Column(name = "deviation")
	private double deviation;
	
	@Column(name = "deviation_percent")
	private double deviationPercent;
	
	public Index() {
		super();
	}
	
	public Index(String id, String dateTime, double capitalValue, double deviation, double deviationPercent) {
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
