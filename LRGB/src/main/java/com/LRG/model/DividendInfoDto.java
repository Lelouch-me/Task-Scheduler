package com.LRG.model;

public class DividendInfoDto {

	private String ticker;

	private Double cashDividend;

	private Double stockDividend;

	private Double dps;

	private int year;
	
	public DividendInfoDto() {
		super();
	}
	
	public DividendInfoDto(String ticker,Double cashDividend,Double stockDividend,Double dps,int year) {
		this.ticker = ticker;
		this.cashDividend = cashDividend;
		this.stockDividend = stockDividend;
		this.dps = dps;
		this.year = year;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	
	public Double getCashDividend() {
		return cashDividend;
	}
	
	public void setCashDividend(Double cashDividend) {
		this.cashDividend = cashDividend;
	}
	
	public Double getStockDividend() {
		return stockDividend;
	}
	
	public void setStockDividend(Double stockDividend) {
		this.stockDividend = stockDividend;
	}
	
	public Double getDps() {
		return dps;
	}
	
	public void setDps(Double dps) {
		this.dps = dps;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
}
