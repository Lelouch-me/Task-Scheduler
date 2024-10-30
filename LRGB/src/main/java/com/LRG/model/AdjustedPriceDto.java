package com.LRG.model;

public class AdjustedPriceDto {
	
	private String code;
	
	private Double adjstdClose;
	
	private Double volume;

	private String date;
	
	public AdjustedPriceDto() {
		super();
	}
	
	public AdjustedPriceDto(String code, Double adjstdClose, Double volume, String date) {
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
	
	public void setLink(Double adjstdClose) {
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
