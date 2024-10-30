package com.LRG.model;

public class TreasuryBillDto {

	private String treasuryName;

	private Double yield;

	private String issuedDate;

	private String insertDate;

	public TreasuryBillDto(String treasuryName, Double yield, String issuedDate, String insertDate) {
		super();
		this.treasuryName = treasuryName;
		this.yield = yield;
		this.issuedDate = issuedDate;
		this.insertDate = insertDate;
	}

	public TreasuryBillDto() {
		super();
		// TODO Auto-generated constructor stub
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
