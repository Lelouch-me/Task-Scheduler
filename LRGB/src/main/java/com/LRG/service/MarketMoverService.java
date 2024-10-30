package com.LRG.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.LRG.Utils.CommonUtils;
import com.LRG.domain.Company;
import com.LRG.domain.MarketData;
import com.LRG.model.MarketMoverDto;
import com.LRG.repository.MarketMoverRepository;

@Service
public class MarketMoverService {

	private DecimalFormat df = new DecimalFormat("#.##"); 
	private MarketMoverRepository marketMoverRepository;
	
	public MarketMoverService(MarketMoverRepository marketMoverRepository) {
		super();
		this.marketMoverRepository = marketMoverRepository;
	}
	
	public List<MarketMoverDto> getTop10MarketMover(String type){
		List<MarketMoverDto> marketMoverList = new ArrayList<MarketMoverDto>();
		List<MarketData> marketMoverData;
		if(type.equals("first")) {
			marketMoverData = marketMoverRepository.findTop10ByOrderByContributionDesc();
		} else {
			marketMoverData = marketMoverRepository.findTop10ByOrderByContributionAsc();
		}
		
		if(marketMoverData != null && !marketMoverData.isEmpty()) {
			for(MarketData marketMover : marketMoverData) {
				MarketMoverDto marketMoverDto = convertDomainToDto(marketMover);
				marketMoverList.add(marketMoverDto);
			}
		}
		return marketMoverList;	
	}

	
	public List<MarketMoverDto> getGeinersLoosers(String type){
		List<MarketMoverDto> marketMoverList = new ArrayList<MarketMoverDto>();
		List<MarketData> marketMoverData;
		if(type.equals("gainer")) {
			marketMoverData = marketMoverRepository.findTop10ByOrderByPriceChangePercentDesc();
		} else {
			marketMoverData = marketMoverRepository.findTop10ByOrderByPriceChangePercentAsc();
		}
		
		if(marketMoverData != null && !marketMoverData.isEmpty()) {
			for(MarketData marketMover : marketMoverData) {
				MarketMoverDto marketMoverDto = convertDomainToDto(marketMover);
				marketMoverList.add(marketMoverDto);
			}
		}
		return marketMoverList;	
	}
	
	public ArrayList<Integer> getTradedIssueCount() {
		int total_traded_issue = marketMoverRepository.countByPriceChangePercentNot(0.0);
		int issue_advanced = marketMoverRepository.countByPriceChangePercentGreaterThan(0.0);
		int issue_declined = marketMoverRepository.countByPriceChangePercentLessThan(0.0);
		int total_issue = (int) marketMoverRepository.count();
		
		ArrayList<Integer> tradeCount = new ArrayList<Integer>();
		tradeCount.add(total_traded_issue);
		tradeCount.add(issue_advanced);
		tradeCount.add(issue_declined);
		tradeCount.add(total_issue-total_traded_issue);
		
		return tradeCount;
	}
	
	public Map<String,Integer> getSectorDataCount(String type) {
		Map<String,Integer> sectorDataCountMap = new LinkedHashMap<String,Integer>();
		List<Object[]> count = null;
		if(type.equals("total")) count = marketMoverRepository.findSectorMapTotal();
		if(type.equals("up")) count = marketMoverRepository.findSectorMapUp();
		if(type.equals("down")) count = marketMoverRepository.findSectorMapDown();
		if(type.equals("flat")) count = marketMoverRepository.findSectorMapFlat();
		
		for(Object[] obj : count) {
			BigInteger total = (BigInteger) obj[1];
			sectorDataCountMap.put((String) obj[0],total.intValue());
		}
		
		return sectorDataCountMap;
	}
	
	public Map<String,Double> getSectorReturn(){
		Map<String,Double> sectorReturnMap = new HashMap<String,Double>();
		List<Object[]> sectorReturnArray = marketMoverRepository.findSectorReturn();
		for(Object[] obj : sectorReturnArray) {
			BigDecimal mcap = (BigDecimal) obj[0];
			BigDecimal lmcap = (BigDecimal) obj[2];
			double sectorReturn = (double) ((mcap.doubleValue() / lmcap.doubleValue()) -1) * 100; 
			sectorReturnMap.put((String) obj[1],Double.valueOf(df.format(sectorReturn)));
		}
		return CommonUtils.sortMap(sectorReturnMap,false);
	}
	
	public Map<String,Double> getSectorTurnover(){
		Map<String,Double> sectorTurnoverMap = new LinkedHashMap<String,Double>();
		List<Object[]> sectorTurnoverArray = marketMoverRepository.findSectorTurnover();
		DecimalFormat df1 = new DecimalFormat("#.#"); 
		for(Object[] obj : sectorTurnoverArray) {
			BigDecimal value = (BigDecimal) obj[1];
			sectorTurnoverMap.put((String) obj[0],Double.valueOf(df1.format(value.doubleValue())));
		}
		return sectorTurnoverMap;
	}
	
	
	private MarketMoverDto convertDomainToDto(MarketData marketMover) {
		String sector=" ";
		Company code =marketMover.getCompany();
		if (code!=null) {
			sector=marketMover.getCompany().getSector();
		} else {
			System.out.println(code +"  Don't has sector in Company table, please check");
		}
		return new MarketMoverDto(marketMover.getCode(),marketMover.getPriceChange(),marketMover.getPriceChangePercent(),
				Double.valueOf(df.format(marketMover.getContribution()*100)),marketMover.getPoints(),marketMover.getMarketcap(),
				sector,marketMover.getLastMcap());
	}

	public Map<String, Double> getWeightedMcap() {
		Map<String,Double> weightedMarketcapMap = new HashMap<String,Double>();
		List<Object[]> weightedMarketcapList = marketMoverRepository.findWeightedMarketcap();
		for(Object[] obj : weightedMarketcapList) {
			BigDecimal wMcap = (BigDecimal) obj[1];
			double weightedMcap = wMcap.doubleValue(); 
			weightedMarketcapMap.put((String) obj[0],Double.valueOf(df.format(weightedMcap)));
		}
		return weightedMarketcapMap;
	}
	
	public Map<String, Double> getSectoralPE() {
		Map<String,Double> sectoralPEMap = new HashMap<String,Double>();
		List<Object[]> sectoralPEList = marketMoverRepository.findSectoralPE();
		for(Object[] obj : sectoralPEList) {
			BigDecimal sPE = (BigDecimal) obj[1];
			double sectoralPE = sPE.doubleValue(); 
			sectoralPEMap.put((String) obj[0],Double.valueOf(df.format(sectoralPE)));
		}
		return sectoralPEMap;
	}

	public Map<String, String> getTickerValue() {
		Map<String,String> tickerDataMap = new LinkedHashMap<String,String>();
		List<Object[]> tickerValueArray = marketMoverRepository.findTickerValue();
		for(Object[] obj : tickerValueArray) {
			BigDecimal value = (BigDecimal) obj[2];
			tickerDataMap.put((String) obj[0],obj[1]+"::"+value.doubleValue());
		}
		return tickerDataMap;
	}
	
	public Map<String, String> getTickerVolume() {
		Map<String,String> tickerDataMap = new LinkedHashMap<String,String>();
		List<Object[]> tickerValueArray = marketMoverRepository.findTickerVolume();
		for(Object[] obj : tickerValueArray) {
			tickerDataMap.put((String) obj[0],obj[1]+"::"+obj[2]);
		}
		return tickerDataMap;
	}
	
	public Map<String, String> getTickerMcap() {
		Map<String,String> tickerDataMap = new LinkedHashMap<String,String>();
		List<Object[]> tickerValueArray = marketMoverRepository.findTickerMcap();
		for(Object[] obj : tickerValueArray) {
			BigDecimal value = (BigDecimal) obj[2];
			tickerDataMap.put((String) obj[0],obj[1]+"::"+value.doubleValue());
		}
		return tickerDataMap;
	}
	
	public Map<String, String> getTickerInfo() {
		Map<String,String> tickerDataMap = new LinkedHashMap<String,String>();
		List<Object[]> tickerDataArray = marketMoverRepository.findTickerInfo();
		for(Object[] obj : tickerDataArray) {
			tickerDataMap.put((String) obj[0],obj[1]+"::"+obj[2]+"::"+obj[3]);
		}
		return tickerDataMap;
	}
	
	public Map<String, Double> getSectoralDividendYield() {
		Map<String,Double> weightedDividendYieldMap = new LinkedHashMap<String,Double>();
		List<Object[]> weightedDividendYieldList = marketMoverRepository.findWeightedDividendYield();
		for(Object[] obj : weightedDividendYieldList) {
			BigDecimal wDvdYield = (BigDecimal) obj[1];
			double weightedDividendYield = wDvdYield.doubleValue(); 
			weightedDividendYieldMap.put((String) obj[0],Double.valueOf(df.format(weightedDividendYield)));
		}
		return weightedDividendYieldMap;
	}
	
	public Double getIndexDividendYield(String index) {
		Double dividendYield = null;
		if(index.equals("DSEX")) {
			dividendYield = marketMoverRepository.findDSEXDividendYield();
		}else {
			dividendYield = marketMoverRepository.findDS30DividendYield();
		}
		return dividendYield;
	}
}
