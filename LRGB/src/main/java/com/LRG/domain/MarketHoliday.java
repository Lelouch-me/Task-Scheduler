package com.LRG.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "market_holidays")
public class MarketHoliday {

	@Id
	@Column(name = "id", nullable = false)
	private int id;
	
	@Column(name = "holiday_date")
	private String holidayDate;
	
	public MarketHoliday() {
		super();
	}
	
	public MarketHoliday(String holidayDate) {
		this.holidayDate = holidayDate;
	}
	
	public String getHolidayDate() {
		return holidayDate;
	}
	
	public void setHolidayDate(String holidayDate) {
		this.holidayDate = holidayDate;
	}
}
