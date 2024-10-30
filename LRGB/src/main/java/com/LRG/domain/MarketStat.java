package com.LRG.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "market_stat")
public class MarketStat {
	
	@Id
	@Column(name = "code", nullable = false)
	private String code;

	@Column(name = "LTP")
	private Double ltp;
	
	@Column(name = "high")
	private Double high;
	
	@Column(name = "low")
	private Double low;
	
	@Column(name = "close")
	private Double close;
	
	@Column(name = "YCP")
	private Double ycp;
	
	@Column(name = "total_trades")
	private int totalTrades;
	
	@Column(name = "total_volume")
	private int totalVolume;
	
	@Column(name = "total_value")
	private Double totalValue;
	
	@Column(name = "public_total_trades")
	private int publicTotalTrades;
	
	@Column(name = "public_total_volume")
	private int publicTotalVolume;

	@Column(name = "public_total_value")
	private Double publicTotalValue;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code")
	@Fetch(FetchMode.JOIN)
	private Company company;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code")
	@Fetch(FetchMode.JOIN)
	private DailyCompanyInfo dailyCompanyInfo;
	
	public MarketStat() {
		super();
	}
	
	public MarketStat(String code, Double ltp, Double high, Double low, Double close, Double ycp, int totalTrades, 
			int totalVolume, Double totalValue, int publicTotalTrades, int publicTotalVolume, Double publicTotalValue) {
		super();
		this.code = code;
		this.ltp = ltp;
		this.high = high;
		this.low = low;
		this.close = close; 
		this.ycp = ycp;
		this.totalTrades = totalTrades;
		this.totalVolume = totalVolume;
		this.totalValue = totalValue;
		this.publicTotalTrades = publicTotalTrades; 
		this.publicTotalVolume = publicTotalVolume;
		this.publicTotalValue = publicTotalValue;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public Double getLtp() {
		return ltp;
	}
	
	public void setLtp(Double ltp) {
		this.ltp = ltp;
	}
	
	public Double getHigh() {
		return high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}
	
	public Double getLow() {
		return low;
	}
	
	public void setLow(Double low) {
		this.low = low;
	}
	
	public Double getClose() {
		return close;
	}
	
	public void setClose(Double close) {
		this.close = close;
	}
	
	public Double getYcp() {
		return ycp;
	}

	public void setYcp(Double ycp) {
		this.ycp = ycp;
	}
	
	public int getTotalTrades() {
		return totalTrades;
	}
	
	public void setTotalTrades(int totalTrades) {
		this.totalTrades = totalTrades;
	}
	
	public int getTotalVolume() {
		return totalVolume;
	}
	
	public void setTotalVolume(int totalVolume) {
		this.totalVolume = totalVolume;
	}
	
	public Double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}
	
	public int getPublicTotalTrades() {
		return publicTotalTrades;
	}
	
	public void setPublicTotalTrades(int publicTotalTrades) {
		this.publicTotalTrades = publicTotalTrades;
	}
	
	public int getPublicTotalVolume() {
		return publicTotalVolume;
	}
	
	public void setPublicTotalVolume(int publicTotalVolume) {
		this.publicTotalVolume = publicTotalVolume;
	}
	
	public Double getPublicTotalValue() {
		return publicTotalValue;
	}

	public void setPublicTotalValue(Double publicTotalValue) {
		this.publicTotalValue = publicTotalValue;
	}
	
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	public DailyCompanyInfo getDailyCompanyInfo() {
		return dailyCompanyInfo;
	}

	public void setDailyCompanyInfo(DailyCompanyInfo dailyCompanyInfo) {
		this.dailyCompanyInfo = dailyCompanyInfo;
	}
}
