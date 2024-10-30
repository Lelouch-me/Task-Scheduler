package com.LRG.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "dividend_info")
public class DividendInfo {

	@Column(name = "code", nullable = false)
	private String code;
	
	@Column(name = "cash_dividend")
	private Double cashDividend;
	
	@Column(name = "stock_dividend")
	private Double stockDividend;
		
	@Column(name = "dps")
	private Double dps;
	
	@Id
	@Column(name = "year")
	private int year;
	
	public DividendInfo() {
		super();
	}
	
	public DividendInfo(String code,Double cashDividend,Double stockDividend,Double dps,int year) {
		this.code = code;
		this.cashDividend = cashDividend;
		this.stockDividend = stockDividend;
		this.dps = dps;
		this.year = year;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
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
