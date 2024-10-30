package com.LRG.model;

import java.util.List;

public class DailyCompanyInfoDto {

	private String ticker;
	
	private String priceRange;
	
	private Double authorizedCapital;

	private Double paidupCapital;

	private Double outShares;

	private Double auditedPe;

	private Double sponsorDirector;

	private Double govt;

	private Double institute;

	private Double foreign;

	private Double publicc;
	
	private String sector;
	
	private String companyName;
	
	private Double ltp;
	
	private Double ycp;
	
	private Double dividendYield;
	
	private Double ytd;
	
	private Double oneYearPriceReturn;
	
	private Double oneYearTotalReturn;
	
	private Double oneYearAvgVol;
	
	private Double marketcap;
	
	private List<DividendInfoDto> dividendInfo;
	
	private Double freeFloat;
	
	private boolean midYear;
	
	private Double pe;
	
	private Double pb;
	
	private Double ps;
	
	private String lastUpdateTimePb;
	
	public DailyCompanyInfoDto() {
		super();
	}
	
	public DailyCompanyInfoDto(String ticker,String priceRange,Double authorizedCapital,Double paidupCapital,Double outShares,
			Double auditedPe,Double sponsorDirector,Double govt,Double institute,Double foreign,Double publicc,String sector,
			String companyName,List<DividendInfoDto> dividendInfo,Double ltp,Double dividendYield,Double ytd,Double oneYearPriceReturn,
			Double oneYearTotalReturn,Double oneYearAvgVol,Double marketcap,Double freeFloat,boolean midYear, Double pe, Double pb, 
			Double ps, Double ycp, String lastUpdateTimePb) {
		this.ticker = ticker;
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
		this.sector = sector;
		this.companyName = companyName;
		this.dividendInfo = dividendInfo;
		this.ltp = ltp;
		this.ycp = ycp;
		this.dividendYield = dividendYield;
		this.ytd = ytd;
		this.oneYearPriceReturn = oneYearPriceReturn;
		this.oneYearTotalReturn = oneYearTotalReturn;
		this.oneYearAvgVol = oneYearAvgVol;
		this.marketcap = marketcap;
		this.freeFloat = freeFloat;
		this.midYear = midYear;
		this.pe = pe;
		this.pb = pb;
		this.ps = ps;
		this.lastUpdateTimePb = lastUpdateTimePb;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public void setTicker(String ticker) {
		this.ticker = ticker;
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
	
	public List<DividendInfoDto> getDividendInfo() {
		return dividendInfo;
	}

	public void setDividendInfo(List<DividendInfoDto> dividendInfo) {
		this.dividendInfo = dividendInfo;
	}
	
	public Double getLtp() {
		return ltp;
	}
	
	public void setLtp(Double ltp) {
		this.ltp = ltp;
	}
	
	public Double getDividendYield() {
		return dividendYield;
	}
	
	public void setDividendYield(Double dividendYield) {
		this.dividendYield = dividendYield;
	}
	
	public Double getYtd() {
		return ytd;
	}
	
	public void setYtd(Double ytd) {
		this.ytd = ytd;
	}
	
	public Double getOneYearPriceReturn() {
		return oneYearPriceReturn;
	}
	
	public void setOneYearPriceReturn(Double oneYearPriceReturn) {
		this.oneYearPriceReturn = oneYearPriceReturn;
	}
	
	public Double getOneYearTotalReturn() {
		return oneYearTotalReturn;
	}
	
	public void setOneYearTotalReturn(Double oneYearTotalReturn) {
		this.oneYearTotalReturn = oneYearTotalReturn;
	}
	
	public Double getOneYearAvgVol() {
		return oneYearAvgVol;
	}
	
	public void setOneYearAvgVol(Double oneYearAvgVol) {
		this.oneYearAvgVol = oneYearAvgVol;
	}
	
	public Double getMarketcap() {
		return marketcap;
	}
	
	public void setMarketcap(Double marketcap) {
		this.marketcap = marketcap;
	}
	
	public Double getFreeFloat() {
		return freeFloat;
	}
	
	public void setFreeFloat(Double freeFloat) {
		this.freeFloat = freeFloat;
	}
	
	public boolean getMidYear() {
		return midYear;
	}
	
	public void setMidYear(boolean midYear) {
		this.midYear = midYear;
	}
	
	public Double getPe() {
		return pe;
	}

	public void setPe(Double pe) {
		this.pe = pe;
	}
	
	public Double getPb() {
		return pb;
	}

	public void setPb(Double pb) {
		this.pb = pb;
	}
	
	public Double getPs() {
		return ps;
	}

	public void setPs(Double ps) {
		this.ps = ps;
	}
	
	public Double getYcp() {
		return ycp;
	}
	
	public void setYcp(Double ycp) {
		this.ycp = ycp;
	}
	
	public String getLastUpdateTimePb() {
		return lastUpdateTimePb;
	}

	public void setLastUpdateTimePb(String lastUpdateTimePb) {
		this.lastUpdateTimePb = lastUpdateTimePb;
	}
}
