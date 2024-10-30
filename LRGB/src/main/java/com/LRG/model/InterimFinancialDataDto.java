package com.LRG.model;

public class InterimFinancialDataDto {

	private String ticker;
	
	private String period;

	private Double turnover;

	private Double profitFromContOp;

	private Double profitForPeriod;

	private Double totComprIncomeForPeriod;

	private Double epsBasic;

	private Double epsDiluted;

	private Double epsBasicContOp;

	private Double epsDilutedContOp;

	private Double pricePerShareAtPeriodEnd;
	
	public InterimFinancialDataDto() {
		super();
	}
	
	public InterimFinancialDataDto(String ticker,String period,Double turnover,Double profitFromContOp,Double profitForPeriod,
			Double totComprIncomeForPeriod,Double epsBasic,Double epsDiluted,Double epsBasicContOp,Double epsDilutedContOp,
			Double pricePerShareAtPeriodEnd) {
		this.ticker = ticker;
		this.period = period;
		this.turnover = turnover;
		this.profitFromContOp = profitFromContOp;
		this.profitForPeriod = profitForPeriod;
		this.totComprIncomeForPeriod = totComprIncomeForPeriod;
		this.epsBasic = epsBasic;
		this.epsDiluted = epsDiluted;
		this.epsBasicContOp = epsBasicContOp;
		this.epsDilutedContOp = epsDilutedContOp;
		this.pricePerShareAtPeriodEnd = pricePerShareAtPeriodEnd;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	
	public Double getTurnover() {
		return turnover;
	}
	
	public void setTurnover(Double turnover) {
		this.turnover = turnover;
	}
	
	public Double getProfitFromContOp() {
		return profitFromContOp;
	}

	public void setProfitFromContOp(Double profitFromContOp) {
		this.profitFromContOp = profitFromContOp;
	}
	
	public Double getProfitForPeriod() {
		return profitForPeriod;
	}
	
	public void setProfitForPeriod(Double profitForPeriod) {
		this.profitForPeriod = profitForPeriod;
	}
	
	public Double getTotComprIncomeForPeriod() {
		return totComprIncomeForPeriod;
	}
	
	public void setTotComprIncomeForPeriod(Double totComprIncomeForPeriod) {
		this.totComprIncomeForPeriod = totComprIncomeForPeriod;
	}
	
	public Double getEpsBasic() {
		return epsBasic;
	}

	public void setEpsBasic(Double epsBasic) {
		this.epsBasic = epsBasic;
	}
	
	public Double getEpsDiluted() {
		return epsDiluted;
	}
	
	public void setEpsDiluted(Double epsDiluted) {
		this.epsDiluted = epsDiluted;
	}
	
	public Double getEpsBasicContOp() {
		return epsBasicContOp;
	}

	public void setEpsBasicContOp(Double epsBasicContOp) {
		this.epsBasicContOp = epsBasicContOp;
	}
	
	public Double getEpsDilutedContOp() {
		return epsDilutedContOp;
	}
	
	public void setEpsDilutedContOp(Double epsDilutedContOp) {
		this.epsDilutedContOp = epsDilutedContOp;
	}
	
	public Double getPricePerShareAtPeriodEnd() {
		return pricePerShareAtPeriodEnd;
	}

	public void setPricePerShareAtPeriodEnd(Double pricePerShareAtPeriodEnd) {
		this.pricePerShareAtPeriodEnd = pricePerShareAtPeriodEnd;
	}
}
