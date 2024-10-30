package com.LRG.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "company_performance_matrix")
public class PerformanceMatrix {

	@Id
	@Column(name = "code", nullable = false)
	private String code;
	
	@Column(name = "ytd")
	private Double ytd;
	
	@Column(name = "one_year_avg_vol")
	private Double oneYearAvgVol;
	
	public PerformanceMatrix() {
		super();
	}
	
	public PerformanceMatrix(String code, Double ytd, Double oneYearAvgVol) {
		this.code = code;
		this.ytd = ytd;
		this.oneYearAvgVol = oneYearAvgVol;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public Double getYtd() {
		return ytd;
	}
	
	public void setYtd(Double ytd) {
		this.ytd = ytd;
	}
	
	public Double getOneYearAvgVol() {
		return oneYearAvgVol;
	}

	public void setOneYearAvgVol(Double oneYearAvgVol) {
		this.oneYearAvgVol = oneYearAvgVol;
	}
}
