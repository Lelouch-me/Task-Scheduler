package com.LRG.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "adjusted_price")
public class AdjustedPrice {

	@Id
	@Column(name = "id", nullable = false)
	private int id;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "adjstd_close")
	private Double adjstdClose;
	
	@Column(name = "volume")
	private Double volume;
	
	@Column(name = "date")
	private String date;
	
	public AdjustedPrice() {
		super();
	}
	
	public AdjustedPrice(String code, Double adjstdClose, Double volume, String date) {
		this.code = code;
		this.adjstdClose = adjstdClose;
		this.volume = volume;
		this.date = date;		
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public Double getAdjstdClose() {
		return adjstdClose;
	}
	
	public void setAdjstdClose(Double adjstdClose) {
		this.adjstdClose = adjstdClose;
	}
	
	public Double getVolume() {
		return volume;
	}
	
	public void setVolume(Double volume) {
		this.volume = volume;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
}
