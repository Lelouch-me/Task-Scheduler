package com.LRG.model;

public class TradeDto {
	
	private long totalTrades;
	
	private long totalVolume;
	
	private double totalValue;
	
	public TradeDto() {
		super();
	}
	
	public TradeDto(long totalTrades, long totalVolume, Double totalValue) {
		this.totalTrades = totalTrades;
		this.totalVolume = totalVolume;
		this.totalValue = totalValue;
	}
	
	public long getTotalTrades() {
		return totalTrades;
	}
	
	public void setTotalTrades(long totalTrades) {
		this.totalTrades = totalTrades;
	}
	
	public long getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolumet(long totalVolume) {
		this.totalVolume = totalVolume;
	}
	
	public Double getTotalValue() {
		return totalValue;
	}
	
	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}
}
