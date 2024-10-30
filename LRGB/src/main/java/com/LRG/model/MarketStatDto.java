package com.LRG.model;

public class MarketStatDto {

	private String code;

	private Double ltp;
	
	private Double high;
	
	private Double low;
	
	private Double close;
	
	private Double ycp;
	
	private int totalTrades;
	
	private int totalVolume;
	
	private Double totalValue;
	
	private int publicTotalTrades;
	
	private Double publicTotalVolume;

	private Double publicTotalValue;
	
	private Double change;
	
	private Double changePercent;
	
	private String sector;
	
	private Double pe;
	
	private Double pb;
	
	private Double ps;
	
	private Double auditedPe;
	
	private Double outShares;
	
	private Double discountOrPremium;
	
	private Double issuePrice;
	
	private Double eps;
	
	private Double de;
	
	private Double totalDebt;
	
	private Double equitiesPerShare;
	
	private boolean isDse30;
	
	private Double epsYoy;
	
	private Double epsCagr;
	
	private Double dvdYield;
	
	private Double totExposOrRiskWeightedAsset;
	
	private Double tier1Capital;
	
	private Double salesPerShare;
	
	private Double fairValue;
	
	private String reportDate;
	
	public MarketStatDto() {
		super();
	}
	
	public MarketStatDto(String code, Double ltp, Double high, Double low, Double close, Double ycp, int totalTrades, int totalVolume, 
			Double totalValue, int publicTotalTrades, Double publicTotalVolume, Double publicTotalValue, Double change, Double changePercent, 
			String sector, Double pe, Double pb, Double ps, Double auditedPe, Double outShares, Double discountOrPremium, Double issuePrice,
			Double eps, Double de, Double totalDebt, Double equitiesPerShare, boolean isDse30, Double epsYoy, Double epsCagr, Double dvdYield,
			Double totExposOrRiskWeightedAsset, Double tier1Capital, Double salesPerShare, Double fairValue, String reportDate) {
		super();
		this.code = code;
		this.ltp = ltp;
		this.high = high;
		this.low = low;
		this.close = close; 
		this.ycp = ycp;
		this.totalTrades = totalTrades;
		this.totalVolume = totalVolume;
		this.totalValue = totalValue;
		this.publicTotalTrades = publicTotalTrades; 
		this.publicTotalVolume = publicTotalVolume;
		this.publicTotalValue = publicTotalValue;
		this.change = change;
		this.changePercent = changePercent;
		this.sector = sector;
		this.pe = pe;
		this.pb = pb;
		this.ps = ps;
		this.auditedPe = auditedPe;
		this.outShares = outShares;
		this.discountOrPremium = discountOrPremium;
		this.issuePrice = issuePrice;
		this.eps = eps;
		this.de = de;
		this.totalDebt = totalDebt;
		this.equitiesPerShare = equitiesPerShare;
		this.isDse30 = isDse30;
		this.epsYoy = epsYoy;
		this.epsCagr = epsCagr;
		this.dvdYield = dvdYield;
		this.totExposOrRiskWeightedAsset = totExposOrRiskWeightedAsset;
		this.tier1Capital = tier1Capital;
		this.salesPerShare = salesPerShare;
		this.fairValue = fairValue;
		this.reportDate = reportDate;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public Double getLtp() {
		return ltp;
	}
	
	public void setLtp(Double ltp) {
		this.ltp = ltp;
	}
	
	public Double getHigh() {
		return high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}
	
	public Double getLow() {
		return low;
	}
	
	public void setLow(Double low) {
		this.low = low;
	}
	
	public Double getClose() {
		return close;
	}
	
	public void setClose(Double close) {
		this.close = close;
	}
	
	public Double getYcp() {
		return ycp;
	}

	public void setYcp(Double ycp) {
		this.ycp = ycp;
	}
	
	public int getTotalTrades() {
		return totalTrades;
	}
	
	public void setTotalTrades(int totalTrades) {
		this.totalTrades = totalTrades;
	}
	
	public int getTotalVolume() {
		return totalVolume;
	}
	
	public void setTotalVolume(int totalVolume) {
		this.totalVolume = totalVolume;
	}
	
	public Double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}
	
	public int getPublicTotalTrades() {
		return publicTotalTrades;
	}
	
	public void setPublicTotalTrades(int publicTotalTrades) {
		this.publicTotalTrades = publicTotalTrades;
	}
	
	public Double getPublicTotalVolume() {
		return publicTotalVolume;
	}
	
	public void setPublicTotalVolume(Double publicTotalVolume) {
		this.publicTotalVolume = publicTotalVolume;
	}
	
	public Double getPublicTotalValue() {
		return publicTotalValue;
	}

	public void setPublicTotalValue(Double publicTotalValue) {
		this.publicTotalValue = publicTotalValue;
	}
	
	public Double getChange() {
		return change;
	}

	public void setChange(Double change) {
		this.change = change;
	}
	
	public Double getChangePercent() {
		return changePercent;
	}

	public void setChangePercent(Double changePercent) {
		this.changePercent = changePercent;
	}
	
	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
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
	
	public Double getAuditedPe() {
		return auditedPe;
	}

	public void setAuditedPe(Double auditedPe) {
		this.auditedPe = auditedPe;
	}
	
	public Double getOutShares() {
		return outShares;
	}
	
	public void setOutShares(Double outShares) {
		this.outShares = outShares;
	}
	
	public Double getDiscountOrPremium() {
		return discountOrPremium;
	}
	
	public void setDiscountOrPremium(Double discountOrPremium) {
		this.discountOrPremium = discountOrPremium;
	}
	
	public Double getIssuePrice() {
		return issuePrice;
	}
	
	public void setIssuePrice(Double issuePrice) {
		this.issuePrice = issuePrice;
	}
	
	public Double getEps() {
		return eps;
	}

	public void setEps(Double eps) {
		this.eps = eps;
	}
	
	public Double getDe() {
		return de;
	}

	public void setDe(Double de) {
		this.de = de;
	}
	
	public Double getTotalDebt() {
		return totalDebt;
	}

	public void setTotalDebt(Double totalDebt) {
		this.totalDebt = totalDebt;
	}
	
	public Double getEquitiesPerShare() {
		return equitiesPerShare;
	}

	public void setEquitiesPerShare(Double equitiesPerShare) {
		this.equitiesPerShare = equitiesPerShare;
	}
	
	public boolean getIsDse30() {
		return isDse30;
	}

	public void setIsDse30(boolean isDse30) {
		this.isDse30 = isDse30;
	}
	
	public Double getEpsYoy() {
		return epsYoy;
	}

	public void setEpsYoy(Double epsYoy) {
		this.epsYoy = epsYoy;
	}
	
	public Double getEpsCagr() {
		return epsCagr;
	}

	public void setEpsCagr(Double epsCagr) {
		this.epsCagr = epsCagr;
	}
	
	public Double getDvdYield() {
		return dvdYield;
	}

	public void setDvdYield(Double dvdYield) {
		this.dvdYield = dvdYield;
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
	
	public Double getSalesPerShare() {
		return salesPerShare;
	}

	public void setSalesPerShare(Double salesPerShare) {
		this.salesPerShare = salesPerShare;
	}
	
	public Double getFairValue() {
		return fairValue;
	}

	public void setFairValue(Double fairValue) {
		this.fairValue = fairValue;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
}
