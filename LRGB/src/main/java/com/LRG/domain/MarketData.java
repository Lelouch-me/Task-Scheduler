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
@Table(name = "market_data")
public class MarketData {

	@Id
	@Column(name = "code", nullable = false)
	private String code;
	
	@Column(name = "price_change")
	private Double price_change;
	
	@Column(name = "price_change_percent")
	private Double priceChangePercent;
	
	@Column(name = "contribution")
	private Double contribution;
	
	@Column(name = "points")
	private Double points;
	
	@Column(name = "marketcap")
	private Double marketcap;
	
	@Column(name = "last_mcap")
	private Double lastMcap;
	
	@Column(name = "value")
	private Double value;
	
	@Column(name = "dvd_yield")
	private Double dvdYield;
	
	@Column(name = "one_year_price_return")
	private Double oneYearPriceReturn;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code")
	@Fetch(FetchMode.JOIN)
	private Company company;
	
	
	public MarketData() {
		super();
	}
	
	public MarketData(String code, Double price_change, Double priceChangePercent, Double contribution, Double points,
			Double marketcap, Double lastMcap, Double value, Double dvdYield, Double oneYearPriceReturn) {
		this.code = code;
		this.price_change = price_change;
		this.priceChangePercent = priceChangePercent;
		this.contribution = contribution;
		this.points = points;
		this.marketcap = marketcap;
		this.lastMcap = lastMcap;
		this.value = value;
		this.dvdYield = dvdYield;
		this.oneYearPriceReturn = oneYearPriceReturn;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public Double getPriceChange() {
		return price_change;
	}
	
	public void setPriceChange(Double price_change) {
		this.price_change = price_change;
	}
	
	public Double getPriceChangePercent() {
		return priceChangePercent;
	}

	public void setPriceChangePercent(Double priceChangePercent) {
		this.priceChangePercent = priceChangePercent;
	}
	
	public Double getContribution() {
		return contribution;
	}
	
	public void setContribution(Double contribution) {
		this.contribution = contribution;
	}
	
	public Double getPoints() {
		return points;
	}
	
	public void setPoints(Double points) {
		this.points = points;
	}
	
	public Double getMarketcap() {
		return marketcap;
	}

	public void setMarketcap(Double marketcap) {
		this.marketcap = marketcap;
	}
	
	public Double getLastMcap() {
		return lastMcap;
	}

	public void setLastMcap(Double lastMcap) {
		this.lastMcap = lastMcap;
	}
	
	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	public Double getDvdYield() {
		return dvdYield;
	}

	public void setDvdYield(Double dvdYield) {
		this.dvdYield = dvdYield;
	}
	
	public Double getOneYearPriceReturn() {
		return oneYearPriceReturn;
	}

	public void setOneYearPriceReturn(Double oneYearPriceReturn) {
		this.oneYearPriceReturn = oneYearPriceReturn;
	}
}
