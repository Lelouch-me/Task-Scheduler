package com.LRG.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "company")
public class Company {

	@Id
	@Column(name = "code", nullable = false)
	private String ticker;
	
	@Column(name = "sector")
	private String sector;
	
	@Column(name = "company_name")
	private String companyName;
	
	@Column(name = "year_end")
	private String yearEnd;
	
	@Column(name = "eps")
	private Double eps;
	
	@Column(name = "total_debt")
	private Double totalDebt;
	
	@Column(name = "income_per_share")
	private Double incomePerShare;
	
	@Column(name = "equities_per_share")
	private Double equitiesPerShare;
	
	@Column(name = "sales_per_share")
	private Double salesPerShare;
	
	@Column(name = "tot_expos_or_risk_weighted_asset")
	private Double totExposOrRiskWeightedAsset;
	
	@Column(name = "tier1_capital")
	private Double tier1Capital;
	
	@Column(name = "last_update_time_pb")
	private String lastUpdateTimePb;
	
	public Company() {
		super();
	}
	
	public Company(String ticker,String sector,String companyName,String yearEnd,Double incomePerShare,Double equitiesPerShare,
			Double salesPerShare, String lastUpdateTimePb, Double eps, Double totalDebt, Double totExposOrRiskWeightedAsset,
			Double tier1Capital) {
		this.ticker = ticker;
		this.sector = sector;
		this.companyName = companyName;
		this.yearEnd = yearEnd;
		this.incomePerShare = incomePerShare;
		this.equitiesPerShare = equitiesPerShare;
		this.salesPerShare = salesPerShare;
		this.lastUpdateTimePb = lastUpdateTimePb;
		this.eps = eps;
		this.totalDebt = totalDebt;
		this.totExposOrRiskWeightedAsset = totExposOrRiskWeightedAsset;
		this.tier1Capital = tier1Capital;
		
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	
	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getYearEnd() {
		return yearEnd;
	}

	public void setYearEnd(String yearEnd) {
		this.yearEnd = yearEnd;
	}
	
	public Double getIncomePerShare() {
		return incomePerShare;
	}

	public void setIncomePerShare(Double incomePerShare) {
		this.incomePerShare = incomePerShare;
	}
	
	public Double getEquitiesPerShare() {
		return equitiesPerShare;
	}

	public void setEquitiesPerShare(Double equitiesPerShare) {
		this.equitiesPerShare = equitiesPerShare;
	}
	
	public Double getSalesPerShare() {
		return salesPerShare;
	}

	public void setSalesPerShare(Double salesPerShare) {
		this.salesPerShare = salesPerShare;
	}
	
	public String getLastUpdateTimePb() {
		return lastUpdateTimePb;
	}

	public void setLastUpdateTimePb(String lastUpdateTimePb) {
		this.lastUpdateTimePb = lastUpdateTimePb;
	}
	
	public Double getEps() {
		return eps;
	}

	public void setEps(Double eps) {
		this.eps = eps;
	}
	
	public Double getTotalDebt() {
		return totalDebt;
	}

	public void setTotaldebt(Double totalDebt) {
		this.totalDebt = totalDebt;
	}
	
	public Double getTotExposOrRiskWeightedAsset() {
		return totExposOrRiskWeightedAsset;
	}

	public void setTotExposOrRiskWeightedAsset(Double totExposOrRiskWeightedAsset) {
		this.totExposOrRiskWeightedAsset = totExposOrRiskWeightedAsset;
	}
	
	public Double getTier1Capital() {
		return tier1Capital;
	}

	public void setTier1Capital(Double tier1Capital) {
		this.tier1Capital = tier1Capital;
	}
}
