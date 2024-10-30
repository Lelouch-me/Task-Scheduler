package com.LRG.model;

import java.math.BigDecimal;

import com.LRG.Utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class TickerDto implements Comparable<TickerDto> {

	private String symbol;
	private String name;
	private BigDecimal totalReturn;
	private BigDecimal filter;
	private Double noOfShares;
	private Double minWt;
	private Double maxWt;
	private Double fixedWt;
	private Double passiveWt;
	private Double simMean;
	private Double simVol;
	private Integer compId;
	private String type;
	private Character searchable;
	private String notes;
	@JsonIgnore
	private Integer sortOrder;
	private Long sectorId;
	private String industryType;

	public TickerDto() {
		super();
	}

	public TickerDto(String symbol, BigDecimal totalReturn) {
		super();
		this.symbol = symbol;
		this.totalReturn = totalReturn;
	}

	public TickerDto(String symbol, String name) {
		super();
		this.symbol = symbol;
		this.name = name;
	}

	public TickerDto(String symbol,String name, Integer compId) {
		super();
		this.symbol = symbol;
		this.name = name;
		this.compId = compId;
	}
	
	public TickerDto(String symbol) {
		super();
		this.symbol = symbol;
	}

	public TickerDto(String symbol, String name, String type, Character searchable) {
		super();
		this.symbol = symbol;
		this.name = name;
		this.type = type;
		this.searchable = searchable;
	}
	
	public TickerDto(String symbol, String name, String type, Character searchable,Integer compId, Long sectorId, String industryType) {
		super();
		this.symbol = symbol;
		this.name = name;
		this.type = type;
		this.searchable = searchable;
		this.compId=compId;
		this.sectorId=sectorId;
		this.industryType=industryType;
	}
	
	public TickerDto(String symbol, String name, String type) {
		super();
		this.symbol = symbol;
		this.name = name;
		this.type = type;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getTotalReturn() {
		return totalReturn;
	}

	public void setTotalReturn(BigDecimal totalReturn) {
		this.totalReturn = totalReturn;
	}

	public BigDecimal getFilter() {
		return filter;
	}

	public Double getNoOfShares() {
		return noOfShares;
	}

	public void setNoOfShares(Double noOfShares) {
		this.noOfShares = noOfShares;
	}

	public void setFilter(BigDecimal filter) {
		this.filter = filter;
	}

	public Double getMinWt() {
		return minWt;
	}

	public void setMinWt(Double minWt) {
		this.minWt = minWt;
	}

	public Double getMaxWt() {
		return maxWt;
	}

	public void setMaxWt(Double maxWt) {
		this.maxWt = maxWt;
	}

	public Double getFixedWt() {
		return fixedWt;
	}

	public void setFixedWt(Double fixedWt) {
		this.fixedWt = fixedWt;
	}

	public Double getPassiveWt() {
		return passiveWt;
	}

	public void setPassiveWt(Double passiveWt) {
		this.passiveWt = passiveWt;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Character getSearchable() {
		return searchable;
	}

	public void setSearchable(Character searchable) {
		this.searchable = searchable;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Double getSimMean() {
		return simMean;
	}

	public void setSimMean(Double simMean) {
		this.simMean = simMean;
	}

	public Double getSimVol() {
		return simVol;
	}

	public void setSimVol(Double simVol) {
		this.simVol = simVol;
	}

	
	public Integer getCompId() {
		return compId;
	}

	public void setCompId(Integer compId) {
		this.compId = compId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TickerDto other = (TickerDto) obj;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}

	public Long getSectorId() {
		return sectorId;
	}

	public void setSectorId(Long sectorId) {
		this.sectorId = sectorId;
	}

	public String getIndustryType() {
		return industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}

	@Override
	public String toString() {
		return "TickerDto [symbol=" + symbol + ", name=" + name
				+ ", totalReturn=" + totalReturn + ", filter=" + filter
				+ ", minWt=" + minWt + ", maxWt=" + maxWt + ", fixedWt="
				+ fixedWt + ", simMean=" + simMean + ", simVol=" + simVol
				+ ", type=" + type + ", searchable=" + searchable
				+ ", sortOrder=" + sortOrder + "]";
	}

	@Override
	public int compareTo(TickerDto obj) {
		if (this == obj)
			return 0;
		if (obj == null)
			return 1;
		if (getClass() != obj.getClass())
			return 1;
		TickerDto other = (TickerDto) obj;
		if (symbol == null || other.symbol == null) {
			return 1;
		} else if (Constants.MONEY_MKT_FUNDS.equals(symbol)) {
			return -1;
		} else if (Constants.MONEY_MKT_FUNDS.equals(other.symbol)) {
			return 1;
		} else {
			return symbol.compareTo(other.symbol);
		}
	}
	
	@JsonIgnore
	public String getCachableKey() {
		return symbol + "|" + name + "|" + type + "|" + compId + "|" + sectorId;
	}
	
	@JsonIgnore
	public static TickerDto getFromCachableKey(String key) {
		TickerDto dto = new TickerDto();
		String[] tokens = key.split("\\|");
		dto.setSymbol(tokens[0]);
		dto.setName(tokens[1]);
		dto.setType(tokens[2]);
		dto.setCompId(Integer.parseInt(tokens[3]));
		dto.setSectorId(Long.parseLong(tokens[4]));
		return dto;
	}

}
