package com.LRG.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.LRG.Utils.Constants;
import com.LRG.domain.Index;
import com.LRG.domain.YouTubeVideos;
import com.LRG.model.IndexDto;
import com.LRG.model.MarketStatDto;
import com.LRG.repository.AdjustedPriceRepository;
import com.LRG.repository.IndexRepository;
import com.LRG.repository.SysConfigRepository;
import com.LRG.repository.YoutubeVideoRepository;

@Service
public class IndexService {

	private IndexRepository indexRepository;
	
	@Autowired
	private YoutubeVideoRepository youtubeVideoRepository;
	
	@Autowired
	private AdjustedPriceRepository adjustedPriceRepository;
	
	@Autowired
	private MarketMoverService marketMoverService;
	
	@Autowired
	private EpsDataService epsDataService;
	
	@Autowired
	private SysConfigRepository sysConRepository;
	
	public IndexService(IndexRepository indexRepository) {
		super();
		this.indexRepository = indexRepository;
	}
	
	public List<IndexDto> getCurrentIndexData(){
		List<IndexDto> indexList = new ArrayList<IndexDto>();
		List<Index> indexData = indexRepository.findTop3ByOrderByDateTimeDesc();
		if(indexData != null && !indexData.isEmpty()) {
			for(Index index : indexData) {
				IndexDto indexDto = convertDomainToDto(index);
				indexList.add(indexDto);
			}
		}
		return indexList;	
	}
	
	public List<IndexDto> getIndexData(String code){
		List<IndexDto> indexList = new ArrayList<IndexDto>();
		String marketStartTime = sysConRepository.findById(1).getValue();
		List<Index> indexData = indexRepository.findAllById(code,marketStartTime);
		if(indexData != null && !indexData.isEmpty()) {
			for(Index index : indexData) {
				IndexDto indexDto = convertDomainToDto(index);
				indexDto.setDateTime(index.getDateTime().split(" ")[1]);
				indexList.add(indexDto);
			}
		}
		return indexList;	
	}
	
	private IndexDto convertDomainToDto(Index index) {
		DecimalFormat df = new DecimalFormat("#.##"); 
		return new IndexDto(index.getId(),index.getDateTime(),Double.valueOf(df.format(index.getCapitalValue())),
				Double.valueOf(df.format(index.getDeviation())),Double.valueOf(df.format(index.getDeviationPercent())));	
	}
	
	public Map<String, Double> getIndexRatios(List<MarketStatDto> marketDataList, String index){
		DecimalFormat df = new DecimalFormat("#.##"); 
		Map<String, Double> indexDataMap = new HashMap<String, Double>();
		double marketSumMcapForPE = 0.0;
		double marketSumEarnings = 0.0;
		double marketSumMcapForPB = 0.0;
		double marketSumBook = 0.0;
		double marketSumMcapForPS = 0.0;
		double marketSumSales = 0.0;
		double marketSumEquities = 0.0;
		double marketSumDebt = 0.0;
		Double marketSumEPS = 0.0;
		double countForMarket = 0;
		Double sumUpVolume = 0.0;
		Double sumDownVolume = 0.0;
		
		List<Double> epsData = new ArrayList<Double>();
		
		if(index.equals("DSEX")) {
			for(MarketStatDto marketStat : marketDataList) {
				if(marketStat.getOutShares()!=null) {
					double mcap = marketStat.getLtp() * marketStat.getOutShares();
					if(mcap==0.0) mcap = marketStat.getYcp() * marketStat.getOutShares();
					if(marketStat.getEps()!=null) {
						marketSumMcapForPE += mcap;
						double earnings = marketStat.getEps() * marketStat.getOutShares();
						marketSumEarnings += earnings;
						
						marketSumEPS += marketStat.getEps();
						countForMarket++;
					}
					if(marketStat.getPb()!=null) {
						marketSumMcapForPB += mcap;
						double book = (marketStat.getLtp() / marketStat.getPb()) * marketStat.getOutShares();
						if(book==0.0) book = (marketStat.getYcp() / marketStat.getPb()) * marketStat.getOutShares();
						marketSumBook += book;
					}
					if(marketStat.getPs()!=null && marketStat.getPs()!=0) {
						marketSumMcapForPS += mcap;
						double sale = (marketStat.getLtp() / marketStat.getPs()) * marketStat.getOutShares();
						if(sale==0.0) sale = (marketStat.getYcp() / marketStat.getPs()) * marketStat.getOutShares();
						marketSumSales += sale;
					}
					if(marketStat.getEquitiesPerShare()!=null && marketStat.getTotalDebt()!=null) {
						marketSumEquities += marketStat.getEquitiesPerShare()*marketStat.getOutShares();
						marketSumDebt += marketStat.getTotalDebt();
					}
				}
				if(marketStat.getChange()>=0) {
					sumUpVolume += marketStat.getPublicTotalVolume();
				}else {
					sumDownVolume += marketStat.getPublicTotalVolume();
				}
			}
			epsData = epsDataService.getEpsYoyDataForDSEX(marketDataList.size());
		}else {
			for(MarketStatDto marketStat : marketDataList) {
				if(marketStat.getIsDse30()) {
					if(marketStat.getOutShares()!=null) {
						double mcap = marketStat.getLtp() * marketStat.getOutShares();
						if(mcap==0.0) mcap = marketStat.getYcp() * marketStat.getOutShares();
						if(marketStat.getEps()!=null) {
							marketSumMcapForPE += mcap;
							double earnings = marketStat.getEps() * marketStat.getOutShares();
							marketSumEarnings += earnings;
							
							marketSumEPS += marketStat.getEps();
							countForMarket++;
						}
						if(marketStat.getPb()!=null) {
							marketSumMcapForPB += mcap;
							double book = (marketStat.getLtp() / marketStat.getPb()) * marketStat.getOutShares();
							if(book==0.0) book = (marketStat.getYcp() / marketStat.getPb()) * marketStat.getOutShares();
							marketSumBook += book;
						}
						if(marketStat.getPs()!=null && marketStat.getPs()!=0) {
							marketSumMcapForPS += mcap;
							double sale = (marketStat.getLtp() / marketStat.getPs()) * marketStat.getOutShares();
							if(sale==0.0) sale = (marketStat.getYcp() / marketStat.getPs()) * marketStat.getOutShares();
							marketSumSales += sale;
						}
						if(marketStat.getEquitiesPerShare()!=null && marketStat.getTotalDebt()!=null) {
							marketSumEquities += marketStat.getEquitiesPerShare()*marketStat.getOutShares();
							marketSumDebt += marketStat.getTotalDebt();
						}
					}
					if(marketStat.getChange()>0) {
						sumUpVolume += marketStat.getPublicTotalVolume();
					}else {
						sumDownVolume += marketStat.getPublicTotalVolume();
					}
				}
			}
			epsData = epsDataService.getEpsYoyDataForDS30(30);
		}
		if(marketSumEarnings!=0.0) {
			double marketPE = marketSumMcapForPE / marketSumEarnings;
			indexDataMap.put("PE",Double.valueOf(df.format(marketPE)));
		}
		if(marketSumBook!=0.0) {
			double marketPB = marketSumMcapForPB / marketSumBook;
			indexDataMap.put("PB",Double.valueOf(df.format(marketPB)));
		}
		if(marketSumSales!=0.0) {
			double marketPS = marketSumMcapForPS / marketSumSales;
			indexDataMap.put("PS",Double.valueOf(df.format(marketPS)));
		}
		if(marketSumEquities!=0.0) {
			double marketDE = marketSumDebt / marketSumEquities;
			indexDataMap.put("DE",Double.valueOf(df.format(marketDE)));
		}
		indexDataMap.put("EPS",Double.valueOf(df.format(marketSumEPS/countForMarket)));
		
		if(sumDownVolume!=0.0) {
			double upDownRatio = sumUpVolume/sumDownVolume;
			indexDataMap.put("UpDownRatio",Double.valueOf(df.format(upDownRatio)));
		}
		
		LocalDate date = LocalDate.now();
		LocalDate yesterdayDate = date.minusDays(1);
		Double dailyReturn = adjustedPriceRepository.findDailyIndexReturn(index,index,yesterdayDate.toString());
		
  	  	date = date.minusMonths(1); 	  	
  	  	Double oneMonthReturn = adjustedPriceRepository.findIndexReturn(index,index,date.toString());
  	  	
  	  	date = date.minusMonths(2);
  	  	Double threeMonthReturn = adjustedPriceRepository.findIndexReturn(index,index,date.toString());
  	  	
  	  	date = date.minusMonths(3);
	  	Double sixMonthReturn = adjustedPriceRepository.findIndexReturn(index,index,date.toString());
	  	
	  	date = date.minusMonths(6);
  	  	Double twelveMonthReturn = adjustedPriceRepository.findIndexReturn(index,index,date.toString());
  	  	
  	  	indexDataMap.put("dailyReturn",Double.valueOf(df.format(dailyReturn)));
  	  	indexDataMap.put("1MReturn",Double.valueOf(df.format(oneMonthReturn)));
  	  	indexDataMap.put("3MReturn",Double.valueOf(df.format(threeMonthReturn)));
  	  	indexDataMap.put("6MReturn",Double.valueOf(df.format(sixMonthReturn)));
  	  	indexDataMap.put("1YReturn",Double.valueOf(df.format(twelveMonthReturn)));
  	  	
  	  	if(epsData!=null && epsData.size()==2) {
  	  		indexDataMap.put("EPSYoY",epsData.get(0));
  	  		indexDataMap.put("EPSCagr",epsData.get(1));
		}
  	  	Double divYield = marketMoverService.getIndexDividendYield(index);
  	  	divYield = divYield!=null ? Double.valueOf(df.format(divYield)) : null;
  	  	
  	  	indexDataMap.put("DvdYield",divYield);
		return indexDataMap;
	}
	
	public List<MarketStatDto> getForeignIndexData(){
		DecimalFormat df = new DecimalFormat("#.##");
		List<MarketStatDto> resultList = new ArrayList<MarketStatDto>();
		Map<String,String> foreignIndexDetailsMap = Constants.populateForeignIndexDetailsMap();
		for(String ticker :Constants.populateForeignIndexList()) {
			Object[][] data = adjustedPriceRepository.getPriceDataForForeignIndex(ticker+" C");
			if(data != null) {
				MarketStatDto mstat = new MarketStatDto();
				mstat.setCode(ticker.split(" ")[0]);
				mstat.setSector(foreignIndexDetailsMap.get(mstat.getCode()));
				mstat.setLtp(Double.valueOf(df.format(Double.parseDouble(data[0][0].toString()))));
				mstat.setChange(Double.valueOf(df.format(Double.parseDouble(data[0][1].toString()))));
				mstat.setChangePercent(Double.valueOf(df.format(Double.parseDouble(data[0][2].toString()))));
				resultList.add(mstat);
			}
		}
		return resultList;
	}
	
	public void addVideoId(String videoId){
		youtubeVideoRepository.save(new YouTubeVideos(videoId.replaceAll(" ","")));	
	}
	
	public List<String> getYoutubeVideos(){
		List<String> videoList = new ArrayList<String>();
		List<YouTubeVideos> videoData = youtubeVideoRepository.findTop3ByOrderByIdDesc();
		if(videoData != null && !videoData.isEmpty()) {
			for(YouTubeVideos video : videoData) {
				String videoId = video.getVideoId();
				videoList.add(videoId);
			}
		}
		return videoList;	
	}
}
