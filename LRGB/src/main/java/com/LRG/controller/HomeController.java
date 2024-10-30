package com.LRG.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import com.LRG.Utils.CommonUtils;
import com.LRG.model.DailyCompanyInfoDto;
import com.LRG.model.DividendInfoDto;
import com.LRG.model.IndexDto;
import com.LRG.model.InterimFinancialDataDto;
import com.LRG.model.MarketMoverDto;
import com.LRG.model.MarketStatDto;
import com.LRG.model.NewsDto;
import com.LRG.model.ReportDto;
import com.LRG.model.TradeDto;
import com.LRG.repository.HolidayRepository;
import com.LRG.repository.SysConfigRepository;
import com.LRG.service.CompanyService;
import com.LRG.service.EpsDataService;
import com.LRG.service.IndexService;
import com.LRG.service.MarketMoverService;
import com.LRG.service.MarketStatService;
import com.LRG.service.NewsService;
import com.LRG.service.ReportService;
import com.LRG.service.TradeService;
import com.LRG.service.XLService;

@Controller
public class HomeController {	
	
	@Autowired
	private MarketStatService marketStatService;
	
	@Autowired
	private MarketMoverService marketMoverService;
	
	@Autowired
	private TradeService tradeService;
	
	@Autowired
	private IndexService indexService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private ReportService reportService;
	
	@Autowired
	private EpsDataService epsDataService;
	
	@Autowired
	private HolidayRepository holidayRepository;
	
	@Autowired
	private SysConfigRepository sysConRepository;
	
	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
	public String home(ModelMap modelMap,HttpServletRequest request) throws ClassNotFoundException, SQLException {
		String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		List<MarketStatDto> marketDataList = marketStatService.getAllIntradayData();
		boolean isMarketOpen = CommonUtils.isMarketOpen(holidayRepository,sysConRepository);
		String[] tickers = CommonUtils.getTickerArray(marketDataList);
		
		List<MarketMoverDto> highMarketMoverList = marketMoverService.getTop10MarketMover("first");
		List<MarketMoverDto> lowMarketMoverList = marketMoverService.getTop10MarketMover("last");
		List<MarketMoverDto> gainerList = marketMoverService.getGeinersLoosers("gainer");
		List<MarketMoverDto> looserList = marketMoverService.getGeinersLoosers("looser");
		List<Integer> tradeCount = marketMoverService.getTradedIssueCount();
		List<TradeDto> tradeList = tradeService.getAllTradeData();
		List<IndexDto> indexList = indexService.getCurrentIndexData();
		List<String> videoIdList = indexService.getYoutubeVideos();
		
		List<NewsDto> marketNewsList = newsService.getNewsByType("Market");
		List<NewsDto> economyNewsList = newsService.getNewsByType("Economy");
		List<NewsDto> internationalNewsList = newsService.getNewsByType("International");
		List<MarketStatDto> foreignIndex = indexService.getForeignIndexData();
		
		modelMap.addAttribute("tickerData", marketDataList);
		modelMap.addAttribute("marketOpen", isMarketOpen);
		modelMap.addAttribute("videoIdList", videoIdList);
		modelMap.addAttribute("highMarketMoverList", highMarketMoverList);
		modelMap.addAttribute("lowMarketMoverList", lowMarketMoverList);
		modelMap.addAttribute("gainerList", gainerList);
		modelMap.addAttribute("looserList", looserList);
		modelMap.addAttribute("tradeList", tradeList);
		modelMap.addAttribute("indexList", indexList);
		modelMap.addAttribute("tradeCount", tradeCount);
		modelMap.addAttribute("tickers", tickers);
		modelMap.addAttribute("foreignIndex", foreignIndex);
		modelMap.addAttribute("marketNews", marketNewsList);
		modelMap.addAttribute("economyNews", economyNewsList);
		modelMap.addAttribute("internationalNews", internationalNewsList);
		String clientIpAddress = request.getRemoteAddr();
		modelMap.addAttribute("requestIP", clientIpAddress);
		System.out.println(clientIpAddress);
		
		System.out.println("Going Home...." + sessionId);
		return "index";
	}
	
	@RequestMapping(value = {"/company/{code}"}, method = RequestMethod.GET)
	public String company(ModelMap modelMap, @PathVariable(value = "code") String code) throws ClassNotFoundException, SQLException {
		modelMap = getCompanyPageInfo(modelMap,code);	
		return "company";
	}
	
	@RequestMapping(value = {"/companyBySearch"}, method = RequestMethod.GET)
	public String companyBySearch(ModelMap modelMap, @RequestParam("ticker") String code) throws ClassNotFoundException, SQLException {
		modelMap = getCompanyPageInfo(modelMap,code);	
		return "company";
	}
	
	private ModelMap getCompanyPageInfo(ModelMap modelMap,String code) throws ClassNotFoundException, SQLException {
		List<MarketStatDto> marketDataList = marketStatService.getAllIntradayData();
		boolean isMarketOpen = CommonUtils.isMarketOpen(holidayRepository,sysConRepository);
		String[] tickers = CommonUtils.getTickerArray(marketDataList);
		DailyCompanyInfoDto companyInfoDto = companyService.getCompanyData(code);
		
		List<InterimFinancialDataDto> interimFinancialDataList = companyService.getInterimFinancialData(code);
		List<InterimFinancialDataDto> interimFinancialDataList2 = new ArrayList<InterimFinancialDataDto>();
		List<DividendInfoDto> dividendInfoList = new ArrayList<DividendInfoDto>();
		for(int i =0 ; i<6;i++) {
			dividendInfoList.add(new DividendInfoDto());
			interimFinancialDataList2.add(new InterimFinancialDataDto());
		}
		
		int j = 0;
		for(InterimFinancialDataDto interimFinancialDataDto : interimFinancialDataList) {
			interimFinancialDataList2.add(j,interimFinancialDataDto);
			j++;
		}
		if(companyInfoDto.getDividendInfo()!=null) {
			for(DividendInfoDto dividendInfo : companyInfoDto.getDividendInfo()) {
				if(dividendInfo.getYear()==2018) dividendInfoList.add(0,dividendInfo);
				if(dividendInfo.getYear()==2019) dividendInfoList.add(1,dividendInfo);
				if(dividendInfo.getYear()==2020) dividendInfoList.add(2,dividendInfo);
				if(dividendInfo.getYear()==2021) dividendInfoList.add(3,dividendInfo);
				if(dividendInfo.getYear()==2022) dividendInfoList.add(4,dividendInfo);
				if(dividendInfo.getYear()==2023) dividendInfoList.add(5,dividendInfo);				
			}
		}

		List<Object> marketRatioMapList = companyService.getMarketRatioMapList(marketDataList, companyInfoDto);
		
		Map<String, Double> sectoralMap = (Map<String, Double>) marketRatioMapList.get(0);
		Map<String, Double> weightedValuationMap = (Map<String, Double>) marketRatioMapList.get(1);
		MarketStatDto tickerValuationData = (MarketStatDto) marketRatioMapList.get(2);

		modelMap.addAttribute("tickerData", marketDataList);
		modelMap.addAttribute("marketOpen", isMarketOpen);
		modelMap.addAttribute("companyInfo", companyInfoDto);
		modelMap.addAttribute("dividendInfo", dividendInfoList);
		modelMap.addAttribute("interimFinancialData", interimFinancialDataList2);
		modelMap.addAttribute("tickerValuationData", tickerValuationData);
		modelMap.addAttribute("sectorValuationData", sectoralMap);
		modelMap.addAttribute("weightedValuationData", weightedValuationMap);
		modelMap.addAttribute("tickers", tickers);
		
		System.out.println("showing company....");
		return modelMap;
	}
	
	@RequestMapping(value = {"/research"}, method = RequestMethod.GET)
	public String dashboard(ModelMap modelMap) throws ClassNotFoundException, SQLException {
		List<MarketStatDto> marketDataList = marketStatService.getAllIntradayData();
		boolean isMarketOpen = CommonUtils.isMarketOpen(holidayRepository,sysConRepository);
		String[] tickers = CommonUtils.getTickerArray(marketDataList);
		List<ReportDto> reportList = reportService.getReports();
		
		modelMap.addAttribute("tickerData", marketDataList);
		modelMap.addAttribute("marketOpen", isMarketOpen);
		modelMap.addAttribute("tickers", tickers);
		modelMap.addAttribute("reports", reportList);
		System.out.println("showing research...."); 
		return "dashboard";
	}
	
	@RequestMapping(value = {"/equity_market"}, method = RequestMethod.GET)
	public String marketMap(ModelMap modelMap) throws IOException, ClassNotFoundException, SQLException {
		List<MarketStatDto> marketDataList = marketStatService.getAllIntradayData();
		boolean isMarketOpen = CommonUtils.isMarketOpen(holidayRepository,sysConRepository);
		String[] tickers = CommonUtils.getTickerArray(marketDataList);
		
		Map<String, Integer> sectorTotalCount = marketMoverService.getSectorDataCount("total");
		Map<String, Integer> sectorUpCount = marketMoverService.getSectorDataCount("up");
		Map<String, Integer> sectorDownCount = marketMoverService.getSectorDataCount("down");
		Map<String, Integer> sectorFlatCount = marketMoverService.getSectorDataCount("flat");
		Map<String,Double> sectorReturnMap = marketMoverService.getSectorReturn();
		
		Map<String, Double> weightedMarketcapMap = marketMoverService.getWeightedMcap();
		Map<String, Double> sectoralDvdYieldMap = marketMoverService.getSectoralDividendYield();
		Map<String,Double> threeMonTotalVolMap = marketStatService.get3mAvgVolMap();
		Map<String, Double> dsexDataMap = indexService.getIndexRatios(marketDataList,"DSEX");
		Map<String, Double> dse30DataMap = indexService.getIndexRatios(marketDataList,"DS30");
		
		Map<String, Double> sectoralEpsYoyMap = new HashMap<String,Double>();
		Map<String, Double> sectoralEpsCagrMap = new HashMap<String,Double>();
		Map<String,Integer[]> sectorDataCountMap = new LinkedHashMap<String,Integer[]>();
		
		for(Map.Entry<String, Integer> entry : sectorTotalCount.entrySet()) {
			Integer countArray[] = new Integer[4];
			countArray[0] = entry.getValue();
			countArray[1] = sectorUpCount.containsKey(entry.getKey()) ? sectorUpCount.get(entry.getKey()) : 0;			
			countArray[2] = sectorDownCount.containsKey(entry.getKey()) ? sectorDownCount.get(entry.getKey()) : 0;
			countArray[3] = sectorFlatCount.containsKey(entry.getKey()) ? sectorFlatCount.get(entry.getKey()) : 0;
			
			sectorDataCountMap.put(entry.getKey(),countArray);	
			
			List<Double> epsData = epsDataService.getEpsYoyDataBySector(entry.getKey(),entry.getValue());
			if(epsData!=null && epsData.size()==2) {
				sectoralEpsYoyMap.put(entry.getKey(),epsData.get(0));
				sectoralEpsCagrMap.put(entry.getKey(),epsData.get(1));
			}
		}	
		
		List<Object> marketRatioMapList = marketStatService.getMarketRatioMaps(sectorTotalCount,marketDataList,threeMonTotalVolMap);
		
		
		
		Map<String,List<MarketStatDto>> sectorCompanyMap = (Map<String, List<MarketStatDto>>) marketRatioMapList.get(0);
		Map<String, Double> sectoralPEMap = (Map<String, Double>) marketRatioMapList.get(1);
		Map<String, Double> sectoralPBMap = (Map<String, Double>) marketRatioMapList.get(2);
		Map<String, Double> sectoralPSMap = (Map<String, Double>) marketRatioMapList.get(3);
		Map<String, Double> sectoralDEMap = (Map<String, Double>) marketRatioMapList.get(4);
		Map<String, Double> sectoralVolumeMap = (Map<String, Double>) marketRatioMapList.get(5);
		Map<String, Double> sectoral3MonthVolumeMap = (Map<String, Double>) marketRatioMapList.get(6);
		Map<String, Double> sectoralEPSMap = (Map<String, Double>) marketRatioMapList.get(7);
		
		marketDataList = marketStatService.getMarketDataListAfterFVCalculation(sectoralPEMap,sectoralPBMap,sectoralPSMap,
				dsexDataMap,sectorTotalCount,marketDataList);
		
		
//		Map<String, Double> CompanyPeMap = new LinkedHashMap<String,Double>();
//		Map<String, Double> CompanyDeMap = new LinkedHashMap<String,Double>();
//		for(MarketStatDto marketStat : marketDataList) {
//			String ticker= marketStat.getCode();
//			Double pe= marketStat.getPe();
//			Double de= marketStat.getDe();
//			CompanyDeMap.put(ticker, de);			
//			CompanyPeMap.put(ticker,pe);			
//		}
//		XLService.createExel(CompanyPeMap, "PE", "TickerPE");
//		XLService.createExel(CompanyDeMap, "DE", "TickerDE");
		
		modelMap.addAttribute("tickerData", marketDataList);
		modelMap.addAttribute("marketOpen", isMarketOpen);
		modelMap.addAttribute("sectorDataCountMap", sectorDataCountMap);
		modelMap.addAttribute("sectorCompanyMap", sectorCompanyMap);
		modelMap.addAttribute("sectorReturn", sectorReturnMap);
		modelMap.addAttribute("weightedMcap", weightedMarketcapMap);
		modelMap.addAttribute("sectoralPE", sectoralPEMap);
		modelMap.addAttribute("sectoralPB", sectoralPBMap);
		modelMap.addAttribute("sectoralPS", sectoralPSMap);
		modelMap.addAttribute("sectoralDE", sectoralDEMap);
		modelMap.addAttribute("threeMonAvgVol", threeMonTotalVolMap);
		modelMap.addAttribute("sectoralVolume", sectoralVolumeMap);
		modelMap.addAttribute("sectoral3MonVolume", sectoral3MonthVolumeMap);
		modelMap.addAttribute("sectoralEPS", sectoralEPSMap);
		modelMap.addAttribute("sectoralEpsYoy", sectoralEpsYoyMap);
		modelMap.addAttribute("sectoralEpsCagr", sectoralEpsCagrMap);
		modelMap.addAttribute("sectoralDvdYield", sectoralDvdYieldMap);
		modelMap.addAttribute("dsexRatios", dsexDataMap);
		modelMap.addAttribute("dse30Ratios", dse30DataMap);
		modelMap.addAttribute("tickers", tickers);
		
		System.out.println("showing market-map....");
		return "market-map";
	}
	
	@RequestMapping(value = {"/debt_board"}, method = RequestMethod.GET)
	public String debtBoard(ModelMap modelMap) throws ClassNotFoundException, SQLException {
		List<MarketStatDto> marketDataList = marketStatService.getAllIntradayData();
		boolean isMarketOpen = CommonUtils.isMarketOpen(holidayRepository,sysConRepository);
		String[] tickers = CommonUtils.getTickerArray(marketDataList);
		List<MarketStatDto> bondDataList = marketStatService.getAllBondData();		
		
		modelMap.addAttribute("tickerData", marketDataList);
		modelMap.addAttribute("marketOpen", isMarketOpen);
		modelMap.addAttribute("bondData", bondDataList);
		modelMap.addAttribute("tickers", tickers);
		System.out.println("showing debt_board...."); 
		return "debt-board";
	}
	@RequestMapping(value = {"/header"}, method = RequestMethod.GET)
	public String header(ModelMap modelMap) {
		
		return "header";
	}
	@RequestMapping(value = {"/stock_analysis"}, method = RequestMethod.GET)
	public String stockAnalysis(ModelMap modelMap) throws ClassNotFoundException, SQLException {
		List<MarketStatDto> marketDataList = marketStatService.getAllIntradayData();
		boolean isMarketOpen = CommonUtils.isMarketOpen(holidayRepository,sysConRepository);		
		String[] tickers = CommonUtils.getTickerArray(marketDataList);
		
		modelMap.addAttribute("tickerData", marketDataList);
		modelMap.addAttribute("marketOpen", isMarketOpen);
		modelMap.addAttribute("tickers", tickers);
		System.out.println("showing stock_analysis...."); 
		return "stock-analysis";
	}
	
	
	@RequestMapping(value = {"/market_tree_map"}, method = RequestMethod.GET)
	public String marketTreeMap(ModelMap modelMap , HttpServletRequest request) throws ClassNotFoundException, SQLException {
		List<MarketStatDto> marketDataList = marketStatService.getAllIntradayData();
		boolean isMarketOpen = CommonUtils.isMarketOpen(holidayRepository,sysConRepository);		
		String[] tickers = CommonUtils.getTickerArray(marketDataList);
		
		modelMap.addAttribute("tickerData", marketDataList);
		modelMap.addAttribute("marketOpen", isMarketOpen);
		modelMap.addAttribute("tickers", tickers);
		System.out.println("showing market-tree-map...."); 
		String clientIpAddress = request.getRemoteAddr();
		System.out.println(clientIpAddress); 
		return "market-tree-map";
	}
	
	@RequestMapping(value = {"/my_finance"}, method = RequestMethod.GET)
	public String myFinance(ModelMap modelMap) throws ClassNotFoundException, SQLException {
		List<MarketStatDto> marketDataList = marketStatService.getAllIntradayData();
		boolean isMarketOpen = CommonUtils.isMarketOpen(holidayRepository,sysConRepository);		
		String[] tickers = CommonUtils.getTickerArray(marketDataList);
		
		modelMap.addAttribute("tickerData", marketDataList);
		modelMap.addAttribute("marketOpen", isMarketOpen);
		modelMap.addAttribute("tickers", tickers);
		System.out.println("showing my finance...."); 
		return "my-finance";
	}
	
	@RequestMapping(value = {"/login"}, method = RequestMethod.GET)
	public String showLogin(ModelMap modelMap) {
		System.out.println("showing login...."); 
		return "login";
	}
	
	@RequestMapping(value = {"/signup"}, method = RequestMethod.GET)
	public String showSignup(ModelMap modelMap) {
		System.out.println("showing signup...."); 
		return "signup";
	}
}
