package com.LRG.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "eps_data")
public class EpsData {

	@Id
	@Column(name = "id", nullable = false)
	private int id;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "sector")
	private String sector;
	
	@Column(name = "year")
	private int year;
	
	@Column(name = "annual_eps")
	private Double annualEps;
	
	public EpsData() {
		super();
	}
	
	public EpsData(String code, String sector, int year, Double annualEps) {
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
