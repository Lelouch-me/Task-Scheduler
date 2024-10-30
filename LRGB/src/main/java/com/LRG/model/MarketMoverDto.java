package com.LRG.model;

public class MarketMoverDto {

	private String code;
	
	private Double price_change;
	
	private Double priceChangePercent;
	
	private Double contribution;
	
	private Double points;
	
	private Double marketcap;
	
	private Double lastMcap;
	
	private String sector;
	
	public MarketMoverDto() {
		super();
	}
	
	public MarketMoverDto(String code, Double price_change, Double priceChangePercent, Double contribution, Double points,
			Double marketcap, String sector, Double lastMcap) {
		this.code = code;
		this.price_change = price_change;
		this.priceChangePercent = priceChangePercent;
		this.contribution = contribution;
		this.points = points;
		this.marketcap = marketcap;
		this.lastMcap = lastMcap;
		this.sector = sector;
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
	
	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}
}
