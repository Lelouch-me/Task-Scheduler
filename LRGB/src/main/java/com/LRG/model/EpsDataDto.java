package com.LRG.model;

public class EpsDataDto {
	
	private String code;
	
	private String sector;
	
	private int year;
	
	private Double annualEps;
	
	public EpsDataDto() {
		super();
	}
	
	public EpsDataDto(String code, String sector, int year, Double annualEps) {
		this.code = code;
		this.sector = sector;
		this.year = year;
		this.annualEps = annualEps;		
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getSector() {
		return sector;
	}
	
	public void setSector(String sector) {
		this.sector = sector;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public Double getAnnualEps() {
		return annualEps;
	}
	
	public void setAnnualEps(Double annualEps) {
		this.annualEps = annualEps;
	}
}
