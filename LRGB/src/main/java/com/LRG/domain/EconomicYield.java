package com.LRG.domain;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "economic_yield")
public class EconomicYield {
	
	@Id
	@Column(name = "id")
	private int id;
	
	@Column(name = "month")
	private Date month;
	
	@Column(name = "91-Day T-bill")
	private Double nine1DayTbill;
	
	@Column(name = "182-Day T-bill")
	private Double one82DayTbill;
	
	@Column(name = "364-Day T-bill")
	private Double three64DayTbill;
	
	@Column(name = "5 yr T-bond")
	private Double fiveYrTbond;
	
	@Column(name = "10 yr T-bond")
	private Double tenYrTbond;
	
	@Column(name = "15 yr T-bond")
	private Double fifteenYrTbond;
	
	@Column(name = "20 yr T-bond")
	private Double twentyYrTbond;

	public EconomicYield() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EconomicYield(Date month, Double nine1DayTbill, Double one82DayTbill, Double three64DayTbill,
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
