package com.LRG.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "interim_financial_performance")
public class InterimFinancialData {

	@Id
	@Column(name = "id", nullable = false)
	private int id;
	
	@Column(name = "code", nullable = false)
	private String code;
	
	@Column(name = "period")
	private String period;
	
	@Column(name = "turnover")
	private Double turnover;
	
	@Column(name = "profit_from_cont_op")
	private Double profitFromContOp;
	
	@Column(name = "profit_for_period")
	private Double profitForPeriod;
	
	@Column(name = "tot_compr_income_for_period")
	private Double totComprIncomeForPeriod;
	
	@Column(name = "eps_basic")
	private Double epsBasic;
	
	@Column(name = "eps_diluted")
	private Double epsDiluted;
	
	@Column(name = "eps_basic_cont_op")
	private Double epsBasicContOp;
	
	@Column(name = "eps_diluted_cont_op")
	private Double epsDilutedContOp;
	
	@Column(name = "price_per_share_at_period_end")
	private Double pricePerShareAtPeriodEnd;
	
	public InterimFinancialData() {
		super();
	}
	
	public InterimFinancialData(String code,String period,Double turnover,Double profitFromContOp,Double profitForPeriod,
			Double totComprIncomeForPeriod,Double epsBasic,Double epsDiluted,Double epsBasicContOp,Double epsDilutedContOp,
			Double pricePerShareAtPeriodEnd) {
		this.code = code;
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
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
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
