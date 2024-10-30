package com.LRG.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "daily_company_info")
public class DailyCompanyInfo {
	
	@Id
	@Column(name = "code", nullable = false)
	private String code;
	
	@Column(name = "w52_price_range")
	private String priceRange;
	
	@Column(name = "authorized_capital")
	private Double authorizedCapital;
	
	@Column(name = "paidup_capital")
	private Double paidupCapital;
	
	@Column(name = "out_shares")
	private Double outShares;
	
	@Column(name = "audited_pe")
	private Double auditedPe;
	
	@Column(name = "sponsor_director")
	private Double sponsorDirector;
	
	@Column(name = "govt")
	private Double govt;
	
	@Column(name = "institute")
	private Double institute;
	
	@Column(name = "foreign")
	private Double foreign;
	
	@Column(name = "public")
	private Double publicc;
	
	@Column(name = "issue_price")
	private Double issuePrice;
	
	@Column(name = "dse30")
	private String dse30;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code")
	@Fetch(FetchMode.JOIN)
	private Company company;
	
	@OneToMany(fetch = FetchType.EAGER,
			cascade = CascadeType.ALL)
	@JoinColumn(name = "code")
	@Fetch(FetchMode.JOIN)
	private List<DividendInfo> dividendInfo;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code")
	@Fetch(FetchMode.JOIN)
	private MarketStat mkstat;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code")
	@Fetch(FetchMode.JOIN)
	private PerformanceMatrix performanceMatrix;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code")
	@Fetch(FetchMode.JOIN)
	private MarketData marketData;
	
	public DailyCompanyInfo() {
		super();
	}
	
	public DailyCompanyInfo(String code,String priceRange,Double authorizedCapital,Double paidupCapital,Double outShares,Double auditedPe,
			Double sponsorDirector,Double govt,Double institute,Double foreign,Double publicc,Double issuePrice,String dse30) {
		this.code = code;
		this.priceRange = priceRange;
		this.authorizedCapital = authorizedCapital;
		this.paidupCapital = paidupCapital;
		this.outShares = outShares;
		this.auditedPe = auditedPe;
		this.sponsorDirector = sponsorDirector;
		this.govt = govt;
		this.institute = institute;
		this.foreign = foreign;
		this.publicc = publicc;
		this.issuePrice = issuePrice;
		this.dse30 = dse30;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getPriceRange() {
		return priceRange;
	}

	public void setPriceRange(String priceRange) {
		this.priceRange = priceRange;
	}
	
	public Double getAuthorizedCapital() {
		return authorizedCapital;
	}
	
	public void setAuthorizedCapital(Double authorizedCapital) {
		this.authorizedCapital = authorizedCapital;
	}
	
	public Double getPaidupCapital() {
		return paidupCapital;
	}

	public void setPaidupCapital(Double paidupCapital) {
		this.paidupCapital = paidupCapital;
	}
	
	public Double getOutShares() {
		return outShares;
	}
	
	public void setOutShares(Double outShares) {
		this.outShares = outShares;
	}
	
	public Double getAuditedPe() {
		return auditedPe;
	}
	
	public void setAuditedPe(Double auditedPe) {
		this.auditedPe = auditedPe;
	}
	
	public Double getSponsorDirector() {
		return sponsorDirector;
	}

	public void setSponsorDirector(Double sponsorDirector) {
		this.sponsorDirector = sponsorDirector;
	}
	
	public Double getGovt() {
		return govt;
	}
	
	public void setGovt(Double govt) {
		this.govt = govt;
	}
	
	public Double getInstitute() {
		return institute;
	}

	public void setInstitute(Double institute) {
		this.institute = institute;
	}
	
	public Double getForeign() {
		return foreign;
	}
	
	public void setForeign(Double foreign) {
		this.foreign = foreign;
	}
	
	public Double getPublicc() {
		return publicc;
	}

	public void setPublicc(Double publicc) {
		this.publicc = publicc;
	}
	
	public Double getIssuePrice() {
		return issuePrice;
	}

	public void setIssuePrice(Double issuePrice) {
		this.issuePrice = issuePrice;
	}
	
	public String getDse30() {
		return dse30;
	}
	
	public void setDse30(String dse30) {
		this.dse30 = dse30;
	}
	
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	public List<DividendInfo> getDividendInfo() {
		return dividendInfo;
	}

	public void setDividendInfo(List<DividendInfo> dividendInfo) {
		this.dividendInfo = dividendInfo;
	}
	
	public MarketStat getMkstat() {
		return mkstat;
	}

	public void setMkstat(MarketStat mkstat) {
		this.mkstat = mkstat;
	}
	
	public MarketData getMarketData() {
		return marketData;
	}

	public void setMarketData(MarketData marketData) {
		this.marketData = marketData;
	}
	
	public PerformanceMatrix getPerformanceMatrix() {
		return performanceMatrix;
	}

	public void setPerformanceMatrix(PerformanceMatrix performanceMatrix) {
		this.performanceMatrix = performanceMatrix;
	}
}
