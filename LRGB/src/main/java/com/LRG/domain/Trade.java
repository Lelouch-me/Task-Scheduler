package com.LRG.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "trade")
public class Trade {

	@Id
	@Column(name = "id", nullable = false)
	private int id;
	
	@Column(name = "total_trades")
	private long totalTrades;
	
	@Column(name = "TOTAL_VOLUME")
	private long totalVolume;
	
	@Column(name = "TOTAL_VALUE")
	private double totalValue;
	
	public Trade() {
		super();
	}
	
	public Trade(int id, long totalTrades, long totalVolume, Double totalValue) {
		this.id = id;
		this.totalTrades = totalTrades;
		this.totalVolume = totalVolume;
		this.totalValue = totalValue;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
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
