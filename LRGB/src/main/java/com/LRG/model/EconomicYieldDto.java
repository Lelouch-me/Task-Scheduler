package com.LRG.model;

import java.sql.Date;

public class EconomicYieldDto {
	
	private Date month;
	private Double nine1DayTbill;
	private Double one82DayTbill;
	private Double three64DayTbill;
	private Double fiveYrTbond;
	private Double tenYrTbond;
	private Double fifteenYrTbond;
	private Double twentyYrTbond;
	
	
	public EconomicYieldDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EconomicYieldDto(Date month, Double nine1DayTbill, Double one82DayTbill, Double three64DayTbill,
			Double fiveYrTbond, Double tenYrTbond, Double fifteenYrTbond, Double twentyYrTbond) {
		super();
		this.month = month;
		this.nine1DayTbill = nine1DayTbill;
		this.one82DayTbill = one82DayTbill;
		this.three64DayTbill = three64DayTbill;
		this.fiveYrTbond = fiveYrTbond;
		this.tenYrTbond = tenYrTbond;
		this.fifteenYrTbond = fifteenYrTbond;
		this.twentyYrTbond = twentyYrTbond;
	}

	public Date getMonth() {
		return month;
	}

	public void setMonth(Date month) {
		this.month = month;
	}

	public Double getNine1DayTbill() {
		return nine1DayTbill;
	}

	public void setNine1DayTbill(Double nine1DayTbill) {
		this.nine1DayTbill = nine1DayTbill;
	}

	public Double getOne82DayTbill() {
		return one82DayTbill;
	}

	public void setOne82DayTbill(Double one82DayTbill) {
		this.one82DayTbill = one82DayTbill;
	}

	public Double getThree64DayTbill() {
		return three64DayTbill;
	}

	public void setThree64DayTbill(Double three64DayTbill) {
		this.three64DayTbill = three64DayTbill;
	}

	public Double getFiveYrTbond() {
		return fiveYrTbond;
	}

	public void setFiveYrTbond(Double fiveYrTbond) {
		this.fiveYrTbond = fiveYrTbond;
	}

	public Double getTenYrTbond() {
		return tenYrTbond;
	}

	public void setTenYrTbond(Double tenYrTbond) {
		this.tenYrTbond = tenYrTbond;
	}

	public Double getFifteenYrTbond() {
		return fifteenYrTbond;
	}

	public void setFifteenYrTbond(Double fifteenYrTbond) {
		this.fifteenYrTbond = fifteenYrTbond;
	}

	public Double getTwentyYrTbond() {
		return twentyYrTbond;
	}

	public void setTwentyYrTbond(Double twentyYrTbond) {
		this.twentyYrTbond = twentyYrTbond;
	}

}
