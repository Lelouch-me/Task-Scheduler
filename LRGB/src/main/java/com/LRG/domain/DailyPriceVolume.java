package com.LRG.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "daily_price_volume")
public class DailyPriceVolume {
	
	@Id
	@Column(name = "id", nullable = false)
	private int id;
	
	@Column(name = "ticker")
	private String ticker;
	
	@Column(name = "date")
	private String date;
	
	@Column(name = "close")
	private Double close;
	
	@Column(name = "volume")
	private Double volume;
	
	public DailyPriceVolume() {
		super();
	}
	
	public DailyPriceVolume(String ticker,String date,Double close,Double volume) {
		this.ticker = ticker;
		this.date = date;
		this.close = close;
		this.volume = volume;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public Double getClose() {
		return close;
	}

	public void setClose(Double close) {
		this.close = close;
	}
	
	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}
}
