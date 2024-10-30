package com.LRG.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="treasury_bill_bond_auction")
public class TreasuryBill {
	@Id
	@Column(name = "id", nullable = false)
	private int id;
	
	@Column(name = "treasury_name")
	private String treasuryName;
	
	@Column(name = "yield")
	private Double yield;
	
	@Column(name = "issued_date")
	private String issuedDate;
	
	@Column(name = "insert_date")
	private String insertDate;

	public TreasuryBill(int id, String treasuryName, Double yield, String issuedDate, String insertDate) {
		super();
		this.id = id;
		this.treasuryName = treasuryName;
		this.yield = yield;
		this.issuedDate = issuedDate;
		this.insertDate = insertDate;
	}

	public TreasuryBill() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTreasuryName() {
		return treasuryName;
	}

	public void setTreasuryName(String treasuryName) {
		this.treasuryName = treasuryName;
	}

	public Double getYield() {
		return yield;
	}

	public void setYield(Double yield) {
		this.yield = yield;
	}

	public String getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(String issuedDate) {
		this.issuedDate = issuedDate;
	}

	public String getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(String insertDate) {
		this.insertDate = insertDate;
	}
	
	
}
