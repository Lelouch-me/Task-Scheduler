package com.LRG.controller;

import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.LRG.Utils.CommonUtils;
import com.LRG.Utils.Constants;
import com.LRG.domain.AdjustedPrice;
import com.LRG.domain.TreasuryBill;
import com.LRG.model.DailyCompanyInfoDto;
import com.LRG.model.DividendInfoDto;
import com.LRG.model.InterimFinancialDataDto;
import com.LRG.model.MarketStatDto;
import com.LRG.model.TreasuryBillDto;
import com.LRG.service.BizzService;
import com.LRG.service.CompanyService;
import com.LRG.service.EpsDataService;
import com.LRG.service.IndexService;
import com.LRG.service.KeyEconomicIndicatorService;
import com.LRG.service.MarketMoverService;
import com.LRG.service.MarketStatService;
import com.LRG.service.MyFinanceService;
import com.LRG.service.SmeService;
import com.LRG.service.TreasuryBillBondService;
import com.LRG.service.ZesniaFundemantalService;

@RestController
//@RequestMapping("/api/marketData")
public class BizApiController {
	@Autowired
	private MarketStatService marketStatService;
	
	@Autowired
    private CompanyService companyService;
	
	@Autowired
	private MarketMoverService marketMoverService;
	
	@Autowired
	private MyFinanceService myFinanceService;
	
	@Autowired
	private IndexService indexService;
	
	@Autowired
	private EpsDataService epsDataService;
	
	@Autowired
	private TreasuryBillBondService treasuryBillBondService;
	
	@Autowired
	private KeyEconomicIndicatorService keyEconomicIndicatorService;
	
	@Autowired
	private ZesniaFundemantalService zesniaFundemantalService;
	
	@Autowired
	private SmeService smeService;
	
	
	@CrossOrigin(origins = "*")
	@GetMapping(value = {"/marketApi"})
	@ResponseBody
//	@RequestHeader("Authorization") String authorizationToken
	public ResponseEntity<String> marketData() throws ClassNotFoundException, SQLException {
		JSONArray array = new JSONArray();
		List<MarketStatDto> marketDataList = marketStatService.getAllIntradayData();
		Map<String,Double> threeMonTotalVolMap = marketStatService.get3mAvgVolMap();
		Map<String, Integer> sectorTotalCount = marketMoverService.getSectorDataCount("total");
		Map<String, Double> dsexDataMap = indexService.getIndexRatios(marketDataList,"DSEX");
		List<Object> marketRatioMapList = marketStatService.getMarketRatioMaps(sectorTotalCount,marketDataList,threeMonTotalVolMap);
		Map<String,List<MarketStatDto>> sectorCompanyMap = (Map<String, List<MarketStatDto>>) marketRatioMapList.get(0);
		Map<String, Double> sectoralPEMap = (Map<String, Double>) marketRatioMapList.get(1);
		Map<String, Double> sectoralPBMap = (Map<String, Double>) marketRatioMapList.get(2);
		Map<String, Double> sectoralPSMap = (Map<String, Double>) marketRatioMapList.get(3);
		
		marketDataList = marketStatService.getMarketDataListAfterFVCalculation(sectoralPEMap,sectoralPBMap,sectoralPSMap,
				dsexDataMap,sectorTotalCount,marketDataList);
		
		for (Entry<String, List<MarketStatDto>> entry : sectorCompanyMap.entrySet()) {
			for(MarketStatDto marketDto : entry.getValue()) {
				JSONObject obj = new JSONObject();
				obj.put("Report Date", marketDto.getReportDate());
				obj.put("Fair Value", marketDto.getFairValue());
				obj.put("Code", marketDto.getCode());
				obj.put("Sector", marketDto.getSector());
				obj.put("LTP", marketDto.getLtp());
				obj.put("High", marketDto.getHigh());
				obj.put("Low", marketDto.getLow());
				obj.put("Close", marketDto.getClose());
				obj.put("YCP", marketDto.getYcp());
				obj.put("Total Trades", marketDto.getTotalTrades());
				obj.put("Total Volume", marketDto.getTotalVolume());
				obj.put("Total Value", marketDto.getTotalValue());
				obj.put("Public Total Trades", marketDto.getPublicTotalTrades());
				obj.put("Public Total Volume", marketDto.getPublicTotalVolume());
				obj.put("Public Total Value", marketDto.getPublicTotalValue());
				obj.put("Change", marketDto.getChange());
				obj.put("Change Percentage", marketDto.getChangePercent());
				obj.put("PE", marketDto.getPe());
				obj.put("PB", marketDto.getPb());
				obj.put("PS", marketDto.getPs());
				obj.put("Audited PE", marketDto.getAuditedPe());
				obj.put("Out Shares", marketDto.getOutShares());
				obj.put("Discount OR Premium", marketDto.getDiscountOrPremium());
				obj.put("Issue Price", marketDto.getIssuePrice());
				obj.put("EPS", marketDto.getEps());
				obj.put("DE", marketDto.getDe());
				obj.put("Total Debt", marketDto.getTotalDebt());
				obj.put("Equities Per Share", marketDto.getEquitiesPerShare());
				obj.put("Is DSE 30", marketDto.getIsDse30());
				obj.put("EPS Yoy", marketDto.getEpsYoy());
				obj.put("EPS Cagr", marketDto.getEpsCagr());
				obj.put("Dividend Yield", marketDto.getDvdYield());
				obj.put("Total Exposer or Risk Weighted Asset", marketDto.getTotExposOrRiskWeightedAsset());
				obj.put("Tier 1 Capital", marketDto.getTier1Capital());
				obj.put("Sales Per Share", marketDto.getSalesPerShare());
				obj.put("Fair Value", marketDto.getFairValue());
				obj.put("Daily Volume",marketDto.getPublicTotalVolume()/1000000 );
				obj.put("3M Avg Volume", threeMonTotalVolMap.get(marketDto.getCode())/1000000);
//				System.out.println("Change " + marketDto.getChange());
//				System.out.println("Change Percentage " + marketDto.getChangePercent());
				array.put(obj);
			}
		    }
//		System.out.println(authorizationToken);
//		if(authorizationToken != null && authorizationToken.startsWith("Bearer ")) {
//			String token = authorizationToken.substring(7);
//			if(token.equals("xyz")) {
				
				
				
//				System.out.println(marketDataList);
//				modelMap.addAttribute("tickerData", marketDataList);
				System.out.println("From /marketApi.....");
				return new ResponseEntity<>(array.toString(), HttpStatus.OK);
//			}
//			else {
//				System.out.println("Invalid token");
//			}
//			
//		}
//		else {
//			return new ResponseEntity<>(array.toString(), HttpStatus.OK);
//		}
//		return null;
		
	}
	
	
	@GetMapping(value = {"/bondApi"})
	@ResponseBody
	public ResponseEntity<String> bondData() throws ClassNotFoundException, SQLException {
		JSONArray array = new JSONArray();
//		System.out.println(authorizationToken);
//		if(authorizationToken != null && authorizationToken.startsWith("Bearer ")) {
//			String token = authorizationToken.substring(7);
//			if(token.equals("xyz")) {
				List<MarketStatDto> bondDataList = marketStatService.getAllBondData();	
				for(MarketStatDto bondtDto : bondDataList) {
					JSONObject obj = new JSONObject();
					obj.put("Code", bondtDto.getCode());
					obj.put("Sector", bondtDto.getSector());
					obj.put("LTP", bondtDto.getLtp());
					obj.put("High", bondtDto.getHigh());
					obj.put("Low", bondtDto.getLow());
					obj.put("Close", bondtDto.getClose());
					obj.put("YCP", bondtDto.getYcp());
					obj.put("Total Trades", bondtDto.getTotalTrades());
					obj.put("Total Volume", bondtDto.getTotalVolume());
					obj.put("Total Value", bondtDto.getTotalValue());
					obj.put("Public Total Trades", bondtDto.getPublicTotalTrades());
					obj.put("Public Total Volume", bondtDto.getPublicTotalVolume());
					obj.put("Public Total Value", bondtDto.getPublicTotalValue());
					if(bondtDto.getChange()==null) {
						obj.put("Change", 0.0);
					}else {
						obj.put("Change", bondtDto.getChange());
					}
					
					obj.put("Change Percentage", bondtDto.getChangePercent());
					obj.put("PE", bondtDto.getPe());
					obj.put("PB", bondtDto.getPb());
					obj.put("PS", bondtDto.getPs());
					obj.put("Audited PE", bondtDto.getAuditedPe());
					obj.put("Out Shares", bondtDto.getOutShares());
					obj.put("Discount OR Premium", bondtDto.getDiscountOrPremium());
					obj.put("Issue Price", bondtDto.getIssuePrice());
					obj.put("EPS", bondtDto.getEps());
					obj.put("DE", bondtDto.getDe());
					obj.put("Total Debt", bondtDto.getTotalDebt());
					obj.put("Equities Per Share", bondtDto.getEquitiesPerShare());
					obj.put("Is DSE 30", bondtDto.getIsDse30());
					obj.put("EPS Yoy", bondtDto.getEpsYoy());
					obj.put("EPS Cagr", bondtDto.getEpsCagr());
					obj.put("Dividend Yield", bondtDto.getDvdYield());
					obj.put("Total Exposer or Risk Weighted Asset", bondtDto.getTotExposOrRiskWeightedAsset());
					obj.put("Tier 1 Capital", bondtDto.getTier1Capital());
					obj.put("Sales Per Share", bondtDto.getSalesPerShare());
					obj.put("Fair Value", bondtDto.getFairValue());
//					System.out.println("Change " + marketDto.getChange());
//					System.out.println("Change Percentage " + marketDto.getChangePercent());
					array.put(obj);
				}
				
//				System.out.println(marketDataList);
//				modelMap.addAttribute("tickerData", marketDataList);
				System.out.println("From /bondApi.....");
				return new ResponseEntity<>(array.toString(), HttpStatus.OK);
//			}
//			else {
//				System.out.println("Invalid token");
//			}
//			
//		}
//		else {
//			return new ResponseEntity<>(array.toString(), HttpStatus.OK);
//		}
//		return null;
		
	}
	
	@GetMapping(value="/stockData")
	@ResponseBody
	public ResponseEntity<String> stock_analysis(@RequestParam(name = "code") String code, @RequestParam(name = "dateRange") String dateRange,
			@RequestParam(name = "bmTicker") String bmTicker){
		List<String> indices = Constants.populateIndexList();
		List<String> foreignIndices = Constants.populateForeignIndexList();
		boolean isIndex = indices.contains(code) || indices.contains(bmTicker);
		boolean isForeignIndex = foreignIndices.contains(code) || indices.contains(bmTicker);
		boolean isTickerMF = indices.stream().anyMatch(in -> in.split(" ")[0].equals(code));
		boolean isBMTickerMF = indices.stream().anyMatch(in -> in.split(" ")[0].equals(bmTicker));
		
		List<AdjustedPrice> dailyPriceVolumeListForTable = companyService.getPriceAndVolumeData(code,dateRange,isIndex,true,isForeignIndex,
				isTickerMF || isBMTickerMF,isTickerMF);	
		List<AdjustedPrice> dailyBMPriceList = companyService.getPriceAndVolumeData(bmTicker,dateRange,isIndex,true,isForeignIndex,isTickerMF || isBMTickerMF,isBMTickerMF);
		Map<String,Double> tableDataMap = companyService.getTableChartData(dailyPriceVolumeListForTable,dailyBMPriceList,isIndex || isTickerMF || isBMTickerMF);
//		System.out.println(tableDataMap);
		
		JSONObject obj = new JSONObject();
		for (Map.Entry<String, Double> entry : tableDataMap.entrySet()) {
//			System.out.println(entry.getKey()+"--"+ entry.getValue());
			if(entry.getValue().isNaN() || entry.getValue().isInfinite()) {
				obj.put(entry.getKey(), 0.0);
			}
			else{
				obj.put(entry.getKey(), entry.getValue());
			}
		}
		System.out.println("From /stockData.....");
		return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
	}
	
	@GetMapping(value="/dsex_ds30_Api")
	@ResponseBody
	public ResponseEntity<String> dsex_ds30_ratio() throws ClassNotFoundException, SQLException{
		List<MarketStatDto> marketDataList = marketStatService.getAllIntradayData();
		Map<String, Double> dsexDataMap = indexService.getIndexRatios(marketDataList,"DSEX");
//		System.out.println(dsexDataMap);
		Map<String, Double> dse30DataMap = indexService.getIndexRatios(marketDataList,"DS30");
//		System.out.println(dse30DataMap);
		
		JSONArray array = new JSONArray();
		JSONObject dsexObj = new JSONObject();
		for (Map.Entry<String, Double> entry : dsexDataMap.entrySet()) {
//			System.out.println(entry.getKey()+"--"+ entry.getValue());
			dsexObj.put("Index Name", "DSEX");
			if(entry.getValue().isNaN() || entry.getValue().isInfinite()) {
				dsexObj.put(entry.getKey(), 0.0);
			}
			else{
				dsexObj.put(entry.getKey(), entry.getValue());
			}
		}
		array.put(dsexObj);
		JSONObject ds30Obj = new JSONObject();
		for (Map.Entry<String, Double> entry : dse30DataMap.entrySet()) {
			ds30Obj.put("Index Name", "DS30");
//			System.out.println(entry.getKey()+"--"+ entry.getValue());
			if(entry.getValue().isNaN() || entry.getValue().isInfinite()) {
				ds30Obj.put(entry.getKey(), 0.0);
			}
			else{
				ds30Obj.put(entry.getKey(), entry.getValue());
			}
		}
		array.put(ds30Obj);
		System.out.println("From /dsex_ds30_Api.....");
		return new ResponseEntity<>(array.toString(), HttpStatus.OK);
	}
	
	@GetMapping(value="/sectoralValueApi")
	@ResponseBody
	public ResponseEntity<String> sectoralValues() throws ClassNotFoundException, SQLException{
		JSONArray array = new JSONArray();
		List<MarketStatDto> marketDataList = marketStatService.getAllIntradayData();
		
		Map<String, Integer> sectorTotalCount = marketMoverService.getSectorDataCount("total");
		Map<String, Integer> sectorUpCount = marketMoverService.getSectorDataCount("up");
		Map<String, Integer> sectorDownCount = marketMoverService.getSectorDataCount("down");
		Map<String, Integer> sectorFlatCount = marketMoverService.getSectorDataCount("flat");
		Map<String,Double> sectorReturnMap = marketMoverService.getSectorReturn();
		
		Map<String, Double> weightedMarketcapMap = marketMoverService.getWeightedMcap();
		Map<String, Double> sectoralDvdYieldMap = marketMoverService.getSectoralDividendYield();
		Map<String,Double> threeMonTotalVolMap = marketStatService.get3mAvgVolMap();
		
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
		
		
		Map<String, Double> sectoralPEMap = (Map<String, Double>) marketRatioMapList.get(1);
		Map<String, Double> sectoralPBMap = (Map<String, Double>) marketRatioMapList.get(2);
		Map<String, Double> sectoralPSMap = (Map<String, Double>) marketRatioMapList.get(3);
		Map<String, Double> sectoralDEMap = (Map<String, Double>) marketRatioMapList.get(4);
		Map<String, Double> sectoralVolumeMap = (Map<String, Double>) marketRatioMapList.get(5);
		Map<String, Double> sectoral3MonthVolumeMap = (Map<String, Double>) marketRatioMapList.get(6);
		Map<String, Double> sectoralEPSMap = (Map<String, Double>) marketRatioMapList.get(7);
		
		
		String[] sectors = {"Miscellaneous", "Textile", "Mutual Fund", "Bank", "Telecommunication", "IT", "Ceramics", "Tannery", "Engineering", "NBFI", "Insurance", "Real Estate", "Fuel", "Cement", "Travel Leisure", "Paper", "Pharmaceuticals", "Food", "Jute"};
		for( String sector : sectors) {
			JSONObject obj = new JSONObject();
			obj.put("Report Date", marketStatService.getSectoralReportDate(sector));
			obj.put("Sector Name", sector);
			obj.put("Weighted MCap", weightedMarketcapMap.get(sector));
			obj.put("DE", sectoralDEMap.get(sector));
			obj.put("PE", sectoralPEMap.get(sector));
			obj.put("PB", sectoralPBMap.get(sector));
			obj.put("PS", sectoralPSMap.get(sector));
			obj.put("EPS", sectoralEPSMap.get(sector));
			obj.put("Sector Return", sectorReturnMap.get(sector));
			obj.put("Sector total", sectorTotalCount.get(sector));
			obj.put("Sector Up", sectorUpCount.get(sector));
			obj.put("Sector Down", sectorDownCount.get(sector));
			obj.put("Sector Flat", sectorFlatCount.get(sector));
			obj.put("EPS 1 year Change", sectoralEpsYoyMap.get(sector));
			obj.put("EPS 3 year Change", sectoralEpsCagrMap.get(sector));
			obj.put("Dividend Yield", sectoralDvdYieldMap.get(sector));
			obj.put("Daily Volume", sectoralVolumeMap.get(sector));
			obj.put("3 Month Volume", sectoral3MonthVolumeMap.get(sector));
			
			array.put(obj);
		}
		
		System.out.println("From /sectoralValueApi.....");
		return new ResponseEntity<>(array.toString(), HttpStatus.OK);
	}
	
	@GetMapping(value="/companyinfoApi")
	@ResponseBody
	public ResponseEntity<String> shareholderChartApi(@RequestParam("code") String code) throws ClassNotFoundException, SQLException{
		List<MarketStatDto> marketDataList = marketStatService.getAllIntradayData();
		DailyCompanyInfoDto companyInfoDto = companyService.getCompanyData(code);
		List<InterimFinancialDataDto> interimFinancialDataList = companyService.getInterimFinancialData(code);
		List<InterimFinancialDataDto> interimFinancialDataList2 = new ArrayList<InterimFinancialDataDto>();
		List<DividendInfoDto> dividendInfoList = new ArrayList<DividendInfoDto>();
		List<Object> marketRatioMapList = companyService.getMarketRatioMapList(marketDataList, companyInfoDto);
		Map<String, Double> sectoralMap = (Map<String, Double>) marketRatioMapList.get(0);
		MarketStatDto tickerValuationData = (MarketStatDto) marketRatioMapList.get(2);
		Map<String, Double> weightedValuationMap = (Map<String, Double>) marketRatioMapList.get(1);
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
			int currentYear = YearMonth.now().getYear();
			for(DividendInfoDto dividendInfo : companyInfoDto.getDividendInfo()) {
				if(dividendInfo.getYear()==currentYear-5) dividendInfoList.add(0,dividendInfo);
				if(dividendInfo.getYear()==currentYear-4) dividendInfoList.add(1,dividendInfo);
				if(dividendInfo.getYear()==currentYear-3) dividendInfoList.add(2,dividendInfo);
				if(dividendInfo.getYear()==currentYear-2) dividendInfoList.add(3,dividendInfo);
				if(dividendInfo.getYear()==currentYear-1) dividendInfoList.add(4,dividendInfo);
				if(dividendInfo.getYear()==currentYear) dividendInfoList.add(5,dividendInfo);				
			}
		}
		//Dividend History
		JSONObject cashDividendobj = new JSONObject();
		JSONArray cashDividendArray = new JSONArray();
		cashDividendobj.put(String.valueOf(dividendInfoList.get(0).getYear()), dividendInfoList.get(0).getCashDividend());
		cashDividendobj.put(String.valueOf(dividendInfoList.get(1).getYear()), dividendInfoList.get(1).getCashDividend());
		cashDividendobj.put(String.valueOf(dividendInfoList.get(2).getYear()), dividendInfoList.get(2).getCashDividend());
		cashDividendobj.put(String.valueOf(dividendInfoList.get(3).getYear()), dividendInfoList.get(3).getCashDividend());
		cashDividendobj.put(String.valueOf(dividendInfoList.get(4).getYear()), dividendInfoList.get(4).getCashDividend());
		cashDividendobj.put(String.valueOf(dividendInfoList.get(5).getYear()), dividendInfoList.get(5).getCashDividend());
		cashDividendArray.put(cashDividendobj);
		
		JSONObject stockDividendObj = new JSONObject();
		JSONArray stockDividendArray = new JSONArray();
		stockDividendObj.put(String.valueOf(dividendInfoList.get(0).getYear()), dividendInfoList.get(0).getStockDividend());
		stockDividendObj.put(String.valueOf(dividendInfoList.get(1).getYear()), dividendInfoList.get(1).getStockDividend());
		stockDividendObj.put(String.valueOf(dividendInfoList.get(2).getYear()), dividendInfoList.get(2).getStockDividend());
		stockDividendObj.put(String.valueOf(dividendInfoList.get(3).getYear()), dividendInfoList.get(3).getStockDividend());
		stockDividendObj.put(String.valueOf(dividendInfoList.get(4).getYear()), dividendInfoList.get(4).getStockDividend());
		stockDividendObj.put(String.valueOf(dividendInfoList.get(5).getYear()), dividendInfoList.get(5).getStockDividend());
		stockDividendArray.put(stockDividendObj);
		
		JSONObject dpsObj = new JSONObject();
		JSONArray dpsArray = new JSONArray();
		dpsObj.put(String.valueOf(dividendInfoList.get(0).getYear()), dividendInfoList.get(0).getDps());
		dpsObj.put(String.valueOf(dividendInfoList.get(1).getYear()), dividendInfoList.get(1).getDps());
		dpsObj.put(String.valueOf(dividendInfoList.get(2).getYear()), dividendInfoList.get(2).getDps());
		dpsObj.put(String.valueOf(dividendInfoList.get(3).getYear()), dividendInfoList.get(3).getDps());
		dpsObj.put(String.valueOf(dividendInfoList.get(4).getYear()), dividendInfoList.get(4).getDps());
		dpsObj.put(String.valueOf(dividendInfoList.get(5).getYear()), dividendInfoList.get(5).getDps());
		dpsArray.put(dpsObj);
		
		JSONObject dividendHistory = new JSONObject();
		dividendHistory.put("Cash Dividend", cashDividendArray);
		dividendHistory.put("Stock Dividend", stockDividendArray);
		dividendHistory.put("DPS", dpsArray);
		//Intreim
		
		JSONObject q1Obj = new JSONObject();
		JSONArray q1Array = new JSONArray();
		q1Obj.put("Particulars", interimFinancialDataList2.get(0).getPeriod());
		q1Obj.put("Earnings Per Share (EPS) Basic", interimFinancialDataList2.get(0).getEpsBasic());
		q1Obj.put("Earnings Per Share (EPS) - continuing operations Basic", interimFinancialDataList2.get(0).getEpsBasicContOp());
		q1Obj.put("Market price per share at period end", interimFinancialDataList2.get(0).getPricePerShareAtPeriodEnd());
		q1Array.put(q1Obj);
		
		JSONObject q2Obj = new JSONObject();
		JSONArray q2Array = new JSONArray();
		q2Obj.put("Particulars", interimFinancialDataList2.get(1).getPeriod());
		q2Obj.put("Earnings Per Share (EPS) Basic", interimFinancialDataList2.get(1).getEpsBasic());
		q2Obj.put("Earnings Per Share (EPS) - continuing operations Basic", interimFinancialDataList2.get(1).getEpsBasicContOp());
		q2Obj.put("Market price per share at period end", interimFinancialDataList2.get(1).getPricePerShareAtPeriodEnd());
		q2Array.put(q2Obj);
		
		JSONObject m6Obj = new JSONObject();
		JSONArray m6Array = new JSONArray();
		m6Obj.put("Particulars", interimFinancialDataList2.get(2).getPeriod());
		m6Obj.put("Earnings Per Share (EPS) Basic", interimFinancialDataList2.get(2).getEpsBasic());
		m6Obj.put("Earnings Per Share (EPS) - continuing operations Basic", interimFinancialDataList2.get(2).getEpsBasicContOp());
		m6Obj.put("Market price per share at period end", interimFinancialDataList2.get(2).getPricePerShareAtPeriodEnd());
		m6Array.put(m6Obj);
		
		JSONObject q3Obj = new JSONObject();
		JSONArray q3Array = new JSONArray();
		q3Obj.put("Particulars", interimFinancialDataList2.get(3).getPeriod());
		q3Obj.put("Earnings Per Share (EPS) Basic", interimFinancialDataList2.get(3).getEpsBasic());
		q3Obj.put("Earnings Per Share (EPS) - continuing operations Basic", interimFinancialDataList2.get(3).getEpsBasicContOp());
		q3Obj.put("Market price per share at period end", interimFinancialDataList2.get(3).getPricePerShareAtPeriodEnd());
		q3Array.put(q3Obj);
		
		JSONObject m9Obj = new JSONObject();
		JSONArray m9Array = new JSONArray();
		m9Obj.put("Particulars", interimFinancialDataList2.get(4).getPeriod());
		m9Obj.put("Earnings Per Share (EPS) Basic", interimFinancialDataList2.get(4).getEpsBasic());
		m9Obj.put("Earnings Per Share (EPS) - continuing operations Basic", interimFinancialDataList2.get(4).getEpsBasicContOp());
		m9Obj.put("Market price per share at period end", interimFinancialDataList2.get(4).getPricePerShareAtPeriodEnd());
		m9Array.put(m9Obj);
		
		JSONObject quaterlyObj = new JSONObject();
		JSONArray quaterlyArray = new JSONArray();
		quaterlyObj.put("Q1", q1Array);
		quaterlyObj.put("Q2", q2Array);
		quaterlyObj.put("6 Months", m6Array);
		quaterlyObj.put("Q3", q3Array);
		quaterlyObj.put("9 Months", m9Array);
		quaterlyArray.put(quaterlyObj);
		
		JSONObject performanceMatrixObject = new JSONObject();
		JSONArray performanceMatrixArray = new JSONArray();
		performanceMatrixObject.put("Market Price", (companyInfoDto.getLtp() != 0) ? companyInfoDto.getLtp() : companyInfoDto.getYcp());
		performanceMatrixObject.put("Dividend Yield", companyInfoDto.getDividendYield());
		performanceMatrixObject.put("52 Weeks price Range", companyInfoDto.getPriceRange());
		performanceMatrixObject.put("YTD", companyInfoDto.getYtd());
		performanceMatrixObject.put("One year return price", companyInfoDto.getOneYearPriceReturn());
		performanceMatrixObject.put("One year total return", companyInfoDto.getOneYearTotalReturn());
		performanceMatrixObject.put("One year Average Volume", companyInfoDto.getOneYearAvgVol());
		performanceMatrixArray.put(performanceMatrixObject);
		
		JSONObject financialDataObj = new JSONObject();
		JSONArray financialDataArray = new JSONArray();
		financialDataObj.put("Market Capital", companyInfoDto.getMarketcap());
		financialDataObj.put("Paid-up Capital", companyInfoDto.getPaidupCapital());
		financialDataObj.put("Authorized Capital", companyInfoDto.getAuthorizedCapital());
		financialDataObj.put("No. of out Standing Share", companyInfoDto.getOutShares());
		financialDataObj.put("Free-float", companyInfoDto.getFreeFloat());
		financialDataObj.put("Audited P/E", companyInfoDto.getAuditedPe());
		financialDataArray.put(financialDataObj);
		
		JSONObject shareholdingChartObj = new JSONObject();
		JSONArray shareholdingChartArray = new JSONArray();
		shareholdingChartObj.put("Sponsor/Director", companyInfoDto.getSponsorDirector());
		shareholdingChartObj.put("Government", companyInfoDto.getGovt());
		shareholdingChartObj.put("Foreign", companyInfoDto.getForeign());
		shareholdingChartObj.put("Public", companyInfoDto.getPublicc());
		shareholdingChartObj.put("Institute", companyInfoDto.getInstitute());
		shareholdingChartArray.put(shareholdingChartObj);
		
		JSONObject valuationMatrixObj = new JSONObject();
		JSONArray valuationArray = new JSONArray();
		valuationMatrixObj.put("DE",tickerValuationData.getDe());
		valuationMatrixObj.put("PE",tickerValuationData.getPe());
		valuationMatrixObj.put("PB",tickerValuationData.getPb());
		valuationMatrixObj.put("PS",tickerValuationData.getPs());
		valuationMatrixObj.put("EPS",tickerValuationData.getEps());
		valuationArray.put(valuationMatrixObj);
		
		
		
		JSONObject sectoralDataObj = new JSONObject();
		JSONArray sectoralDataArray = new JSONArray();
		sectoralDataObj.put("DE", sectoralMap.get("DE"));
		sectoralDataObj.put("PB", sectoralMap.get("PB"));
		sectoralDataObj.put("PS", sectoralMap.get("PS"));
		sectoralDataObj.put("PE", sectoralMap.get("PE"));
		sectoralDataObj.put("EPS", sectoralMap.get("EPS"));
		sectoralDataArray.put(sectoralDataObj);
		
		JSONObject weightedValuationObj = new JSONObject();
		JSONArray weightedValuationArray = new JSONArray();
		weightedValuationObj.put("DE", weightedValuationMap.get("DE"));
		weightedValuationObj.put("PB", weightedValuationMap.get("PB"));
		weightedValuationObj.put("PS", weightedValuationMap.get("PS"));
		weightedValuationObj.put("PE", weightedValuationMap.get("PE"));
		weightedValuationObj.put("EPS", weightedValuationMap.get("EPS"));
		weightedValuationArray.put(weightedValuationObj);
		
		JSONObject obj = new JSONObject();
		obj.put("Company Name", code);
		obj.put("Company Name", companyInfoDto.getCompanyName());
		obj.put("ShareHolding", shareholdingChartArray);
		obj.put("Performance Matrix", performanceMatrixArray);
		obj.put("Financial Data", financialDataArray);
		obj.put("Dividend History", dividendHistory);
		obj.put("Interim Financial Performance", quaterlyArray);
		obj.put("Company Valuation", valuationArray);
		obj.put("Sectoral Valuation", sectoralDataArray);
		obj.put("Weighted Valuation", weightedValuationArray);
		
		
		System.out.println("From /companyinfoApi.....");
		return new ResponseEntity<>(obj.toString(),HttpStatus.OK);
		
	}
	
	@GetMapping(value="/stockAnalysisTickerApi")
	@ResponseBody
	public ResponseEntity<String> indextickerList () throws ClassNotFoundException, SQLException{
		List<MarketStatDto> marketDataList = marketStatService.getAllIntradayData();
		String[] tickers = CommonUtils.getTickerArray(marketDataList);
		
		JSONArray obj = new JSONArray();
		JSONObject indexObj = new JSONObject();
		obj.put("DS30");
		obj.put("DSEX");
		
		for(String ticker : tickers) {
			obj.put(ticker);
		}
		
		obj.put("LR Global Index");
		obj.put("SND Index");
		obj.put("FD Index");
		obj.put("5-yr T-Bond Index");
		obj.put("10-yr T-Bond Index");
		    	
		obj.put("1JANATAMF NAV Index");
		obj.put("1STPRIMFMF NAV Index");
		obj.put("ABB1STMF NAV Index");
		obj.put("AIBL1STIMF NAV Index");
		obj.put("ATCSLGF NAV Index");
		obj.put("CAPMBDBLMF NAV Index");
		obj.put("CAPMIBBLMF NAV Index");
		obj.put("DBH1STMF NAV Index");
		obj.put("EBL1STMF NAV Index");
		obj.put("EBLNRBMF NAV Index");
		obj.put("EXIM1STMF NAV Index");
		obj.put("FBFIF NAV Index");
		obj.put("GRAMEENS2 NAV Index");
		obj.put("GREENDELMF NAV Index");
		obj.put("ICB3RDNRB NAV Index");
		obj.put("ICBAGRANI1 NAV Index");
		obj.put("ICBAMCL2ND NAV Index");
		obj.put("ICBEPMF1S1 NAV Index");
		obj.put("ICBSONALI1 NAV Index");
		obj.put("IFIC1STMF NAV Index");
		obj.put("IFILISLMF1 NAV Index");
		obj.put("LRGLOBMF1 NAV Index");
		obj.put("MBL1STMF NAV Index");
		obj.put("NCCBLMF1 NAV Index");
		obj.put("PF1STMF NAV Index");
		obj.put("PHPMF1 NAV Index");
		obj.put("POPULAR1MF NAV Index");
		obj.put("PRIME1ICBA NAV Index");
		obj.put("RELIANCE1 NAV Index");
		obj.put("SEMLFBSLGF NAV Index");
		obj.put("SEMLIBBLSF NAV Index");
		obj.put("SEMLLECMF NAV Index");
		obj.put("TRUSTB1MF NAV Index");
		obj.put("VAMLBDMF1 NAV Index");
		obj.put("VAMLRBBF NAV Index");
		    	
		obj.put("SPX Index");
		obj.put("NDX Index");
		obj.put("DAX Index");
		obj.put("ESTX50 Index");
		obj.put("N225 Index");
		obj.put("HSI Index");
		obj.put("SPI Index");
		obj.put("SENSEX Index");
		indexObj.put("Indexs", obj);
		System.out.println("From /stockAnalysisTickerApi.....");
		return new ResponseEntity<>(indexObj.toString(),HttpStatus.OK);
		
	}
	
	@GetMapping(value="/dsexApi")
	@ResponseBody
	public ResponseEntity<String> dsexHistoricalData(@RequestParam(name = "code") String code, @RequestParam(name = "date") String date){
		List<AdjustedPrice> dsexList = companyService.dsexDailyData(code,date);
		JSONObject obj = new JSONObject();
		for(AdjustedPrice dailyValue : dsexList) {
			String priceDate = dailyValue.getDate();
			Double price = dailyValue.getAdjstdClose();
			obj.put(priceDate,price);
		}
		System.out.println("From /dsexApi.....");
		return new ResponseEntity<>(obj.toString(),HttpStatus.OK);
	}
	
	@GetMapping(value="/treasuryBondApi")
	@ResponseBody
	public ResponseEntity<String> treasuryBill(){
		List<TreasuryBillDto> treasury =treasuryBillBondService.getAllTreasuryData() ;
		JSONObject obj = new JSONObject();
		for(TreasuryBillDto treasuryValue : treasury) {
			if(!treasuryValue.getTreasuryName().equals("3yr FRT.Bond")) {
				String treasuryName = treasuryValue.getTreasuryName();
				Double price = treasuryValue.getYield();
				String issuedDate = treasuryValue.getIssuedDate();
				obj.put(treasuryName,issuedDate+"--"+price);
			}
			
		}
		System.out.println("From /treasuryBondApi.....");
		return new ResponseEntity<>(obj.toString(),HttpStatus.OK);
	}
	
	@GetMapping(value="/keyEconomicIndicator")
	@ResponseBody
	public ResponseEntity<String> keyIndicators() throws ClassNotFoundException, SQLException{
		Map<String, Double> callMoney = keyEconomicIndicatorService.keyIndicatiors("Call Money");
		Map<String, Double> exchange = keyEconomicIndicatorService.keyIndicatiors("ExchngeRate");
		Map<String, Double> oil = keyEconomicIndicatorService.keyIndicatiors("Oil");
		Map<String, Double> gold = keyEconomicIndicatorService.keyIndicatiors("Gold");
		String date = keyEconomicIndicatorService.getDate();
		
		JSONObject callMoneyObj = new JSONObject();
		for (Map.Entry<String, Double> entry : callMoney.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            callMoneyObj.put(key, value);
        }
		JSONObject exchangeObj = new JSONObject();
		for (Map.Entry<String, Double> entry : exchange.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            exchangeObj.put(key, value);
        }
		JSONObject oilObj = new JSONObject();
		for (Map.Entry<String, Double> entry : oil.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            oilObj.put(key, value);
        }
		JSONObject goldObj = new JSONObject();
		for (Map.Entry<String, Double> entry : gold.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            goldObj.put(key, value);
        }
		
		JSONObject keyIndicator = new JSONObject();
		keyIndicator.put("Call Money", callMoneyObj);
		keyIndicator.put("Exchange Rate", exchangeObj);
		keyIndicator.put("Oil", oilObj);
		keyIndicator.put("Gold", goldObj);
		keyIndicator.put("date", date);
		System.out.println("From /keyEconomicIndicator.....");
		return new ResponseEntity<>(keyIndicator.toString(),HttpStatus.OK);
	}
	@GetMapping(value="/companyDataApi")
	@ResponseBody
	public ResponseEntity<String> marketDataR(@RequestParam(name = "date") String date) throws ClassNotFoundException, SQLException{
		date=BizzService.getValidation(date);
		List<List<String>> companyData = companyService.marketDataR(date);
		JSONArray list = new JSONArray();
		for(List<String> data : companyData) {
			list.put(data);
		}
		System.out.println("From /companyDataApi.....");
		return new ResponseEntity<>(list.toString(),HttpStatus.OK);
	}
	
	@GetMapping(value="/zesniaFundemantalApi")
	@ResponseBody
	public ResponseEntity<String> zesniafundemantalSummary() throws ClassNotFoundException, SQLException{
		JSONObject summary = new JSONObject();

	// Data for S&P
	
		JSONObject epsGrowth = new JSONObject();
		TreeMap<String, String> epsGrowthIndexSPX = new TreeMap<>();
		List<List<String>> growthSummarySPX = zesniaFundemantalService.indexGrowthSummary("S&P");
		for (List<String> a : growthSummarySPX) {
			epsGrowthIndexSPX.put(a.get(0) + " " + a.get(1), a.get(2));
		}
		epsGrowth.put("S&P", epsGrowthIndexSPX);

	// Data for Nasdaq
		TreeMap<String, String> epsGrowthIndexNDX = new TreeMap<>();
		List<List<String>> growthSummaryNDX = zesniaFundemantalService.indexGrowthSummary("Nasdaq");
		for (List<String> a : growthSummaryNDX) {
			epsGrowthIndexNDX.put(a.get(0) + " " + a.get(1), a.get(2));
		}
		epsGrowth.put("Nasdaq", epsGrowthIndexNDX);
	// Data for SOX
		TreeMap<String, String> epsGrowthIndexSOX = new TreeMap<>();
		List<List<String>> growthSummarySOX = zesniaFundemantalService.indexGrowthSummary("SOX");
		for (List<String> a : growthSummarySOX) {
			epsGrowthIndexSOX.put(a.get(0) + " " + a.get(1), a.get(2));
		}
		epsGrowth.put("SOX", epsGrowthIndexSOX);
		
		summary.put("epsGrowth",epsGrowth);
		
		
		//Sales growth
		JSONObject salesGrowth = new JSONObject();
		TreeMap<String, String> salesGrowthIndexSPX = new TreeMap<>();
		List<List<String>> salesgrowthSummarySPX = zesniaFundemantalService.indexGrowthSummary("S&P");
		for (List<String> a : salesgrowthSummarySPX) {
			salesGrowthIndexSPX.put(a.get(0) + " " + a.get(1), a.get(3));
		}
		salesGrowth.put("S&P", salesGrowthIndexSPX);
		
		
		TreeMap<String, String> salesGrowthIndexNDX = new TreeMap<>();
		List<List<String>> salesgrowthSummaryNDX = zesniaFundemantalService.indexGrowthSummary("Nasdaq");
		for (List<String> a : salesgrowthSummaryNDX) {
			salesGrowthIndexNDX.put(a.get(0) + " " + a.get(1), a.get(3));
		}
		salesGrowth.put("Nasdaq", salesGrowthIndexNDX);
		
		
		TreeMap<String, String> salesGrowthIndexSOX = new TreeMap<>();
		List<List<String>> salesgrowthSummarySOX = zesniaFundemantalService.indexGrowthSummary("SOX");
		for (List<String> a : salesgrowthSummarySOX) {
			salesGrowthIndexSOX.put(a.get(0) + " " + a.get(1), a.get(3));
		}
		salesGrowth.put("SOX", salesGrowthIndexSOX);
		summary.put("salesGrowth",salesGrowth);
		
		
		//EPS Surprise growth
		JSONObject epsSurpriseGrowth = new JSONObject();
		TreeMap<String, String> epsSurpriseIndexSPX = new TreeMap<>();
		List<List<String>> epsSurpriseSummarySPX = zesniaFundemantalService.indexSurpriseGrowthSummary("S&P");
		for (List<String> a : epsSurpriseSummarySPX) {
			System.out.println(a);
			epsSurpriseIndexSPX.put(a.get(0) + " " + a.get(1), a.get(2));
		}
		epsSurpriseGrowth.put("S&P", epsSurpriseIndexSPX);
		
		TreeMap<String, String> epsSurpriseIndexNDX = new TreeMap<>();
		List<List<String>> epsSurpriseSummaryNDX = zesniaFundemantalService.indexSurpriseGrowthSummary("Nasdaq");
		for (List<String> a : epsSurpriseSummaryNDX) {
			epsSurpriseIndexNDX.put(a.get(0) + " " + a.get(1), a.get(2));
		}
		epsSurpriseGrowth.put("Nasdaq", epsSurpriseIndexNDX);
		
		TreeMap<String, String> epsSurpriseIndexSOX = new TreeMap<>();
		List<List<String>> epsSurpriseSummarySOX = zesniaFundemantalService.indexSurpriseGrowthSummary("SOX");
		for (List<String> a : epsSurpriseSummarySOX) {
			epsSurpriseIndexSOX.put(a.get(0) + " " + a.get(1), a.get(2));
		}
		epsSurpriseGrowth.put("SOX", epsSurpriseIndexSOX);
		
		summary.put("epsSurprise",epsSurpriseGrowth);
		
		// Sales Surprise 
		
		JSONObject salesSurpriseGrowth = new JSONObject();
		TreeMap<String, String> salesSurpriseIndexSPX = new TreeMap<>();
		List<List<String>> salesSurpriseSummarySPX = zesniaFundemantalService.indexSurpriseGrowthSummary("S&P");
		for (List<String> a : salesSurpriseSummarySPX) {
			System.out.println(a);
			salesSurpriseIndexSPX.put(a.get(0) + " " + a.get(1), a.get(3));
		}
		salesSurpriseGrowth.put("S&P", salesSurpriseIndexSPX);
		
		TreeMap<String, String> salesSurpriseIndexNDX = new TreeMap<>();
		List<List<String>> salesSurpriseSummaryNDX = zesniaFundemantalService.indexSurpriseGrowthSummary("Nasdaq");
		for (List<String> a : salesSurpriseSummaryNDX) {
			salesSurpriseIndexNDX.put(a.get(0) + " " + a.get(1), a.get(3));
		}
		salesSurpriseGrowth.put("Nasdaq", salesSurpriseIndexNDX);
		
		TreeMap<String, String> salesSurpriseIndexSOX = new TreeMap<>();
		List<List<String>> salesSurpriseSummarySOX = zesniaFundemantalService.indexSurpriseGrowthSummary("SOX");
		for (List<String> a : salesSurpriseSummarySOX) {
			salesSurpriseIndexSOX.put(a.get(0) + " " + a.get(1), a.get(3));
		}
		salesSurpriseGrowth.put("SOX", salesSurpriseIndexSOX);
		
		summary.put("salesSurprise",salesSurpriseGrowth);
		
		// Index Eps Growth
		JSONObject index = new JSONObject();
		JSONObject indexEpsActualGrowth = new JSONObject();
		TreeMap<String, String> indexEpsActualIndexSPX = new TreeMap<>();
		List<List<String>> indexEpsActualSummarySPX = zesniaFundemantalService.indexEpsAGrowthSummary("SPX","Actual");
		for (List<String> a : indexEpsActualSummarySPX) {
			indexEpsActualIndexSPX.put(a.get(0) + " " + a.get(1), a.get(2));
		}
		indexEpsActualGrowth.put("Actual", indexEpsActualIndexSPX);
		

		TreeMap<String, String> indexEpsEstimatedIndexSPX = new TreeMap<>();
		List<List<String>> indexEpsEstimatedSummarySPX = zesniaFundemantalService.indexEpsAGrowthSummary("SPX","Estimated");
		for (List<String> a : indexEpsEstimatedSummarySPX) {
			indexEpsEstimatedIndexSPX.put(a.get(0) + " " + a.get(1), a.get(2));
		}
		indexEpsActualGrowth.put("Estimated", indexEpsEstimatedIndexSPX);
		index.put("S&P", indexEpsActualGrowth);
		
		
		JSONObject indexEpsActualGrowthNDX = new JSONObject();
		TreeMap<String, String> indexEpsActualIndexNDX = new TreeMap<>();
		List<List<String>> indexEpsActualSummaryNDX = zesniaFundemantalService.indexEpsAGrowthSummary("NDX","Actual");
		for (List<String> a : indexEpsActualSummaryNDX) {
			indexEpsActualIndexNDX.put(a.get(0) + " " + a.get(1), a.get(2));
		}
		indexEpsActualGrowthNDX.put("Actual", indexEpsActualIndexNDX);
		

		TreeMap<String, String> indexEpsEstimatedIndexNDX = new TreeMap<>();
		List<List<String>> indexEpsEstimatedSummaryNDX = zesniaFundemantalService.indexEpsAGrowthSummary("NDX","Estimated");
		for (List<String> a : indexEpsEstimatedSummaryNDX) {
			indexEpsEstimatedIndexNDX.put(a.get(0) + " " + a.get(1), a.get(2));
		}
		indexEpsActualGrowthNDX.put("Estimated", indexEpsEstimatedIndexNDX);
		index.put("Nasdaq", indexEpsActualGrowthNDX);
		
		
		JSONObject indexEpsActualGrowthSOX = new JSONObject();
		TreeMap<String, String> indexEpsActualIndexSOX = new TreeMap<>();
		List<List<String>> indexEpsActualSummarySOX = zesniaFundemantalService.indexEpsAGrowthSummary("SOX","Actual");
		for (List<String> a : indexEpsActualSummarySOX) {
			indexEpsActualIndexSOX.put(a.get(0) + " " + a.get(1), a.get(2));
		}
		indexEpsActualGrowthSOX.put("Actual", indexEpsActualIndexSOX);
		

		TreeMap<String, String> indexEpsEstimatedIndexSOX = new TreeMap<>();
		List<List<String>> indexEpsEstimatedSummarySOX = zesniaFundemantalService.indexEpsAGrowthSummary("SOX","Estimated");
		for (List<String> a : indexEpsEstimatedSummarySOX) {
			indexEpsEstimatedIndexSOX.put(a.get(0) + " " + a.get(1), a.get(2));
		}
		indexEpsActualGrowthSOX.put("Estimated", indexEpsEstimatedIndexSOX);
		index.put("SOX", indexEpsActualGrowthSOX);
		
		summary.put("indexEpsGrowth",index);
		
		
		

		System.out.println("From /zesniaFundemantalApi.....");

		return new ResponseEntity<>(summary.toString(), HttpStatus.OK);
	}
	
	@GetMapping(value="/zesniaProjectedApi")
	@ResponseBody
	public ResponseEntity<String> zesniaProjectedSummary() throws ClassNotFoundException, SQLException{
		JSONObject summary = new JSONObject();

	// Data for S&P
	
		JSONObject projectedGrowth = new JSONObject();
		TreeMap<String, String> epsGrowthIndexSPX = new TreeMap<>();
		List<List<String>> growthSummarySPX = zesniaFundemantalService.getProjectedGrowthSummary("SPX");
		for (List<String> a : growthSummarySPX) {
			epsGrowthIndexSPX.put(a.get(0) + " " + a.get(1), a.get(2));
		}
		projectedGrowth.put("SPX", epsGrowthIndexSPX);

	// Data for Nasdaq
		TreeMap<String, String> epsGrowthIndexNDX = new TreeMap<>();
		List<List<String>> growthSummaryNDX = zesniaFundemantalService.getProjectedGrowthSummary("NDX");
		for (List<String> a : growthSummaryNDX) {
			epsGrowthIndexNDX.put(a.get(0) + " " + a.get(1), a.get(2));
		}
		projectedGrowth.put("NDX", epsGrowthIndexNDX);
	// Data for SOX
		TreeMap<String, String> epsGrowthIndexSOX = new TreeMap<>();
		List<List<String>> growthSummarySOX = zesniaFundemantalService.getProjectedGrowthSummary("SOX");
		for (List<String> a : growthSummarySOX) {
			epsGrowthIndexSOX.put(a.get(0) + " " + a.get(1), a.get(2));
		}
		projectedGrowth.put("SOX", epsGrowthIndexSOX);
		
		summary.put("Projected EPS Growth",projectedGrowth);
		
		
		//Sales growth
		JSONObject projectedSalesGrowth = new JSONObject();
		TreeMap<String, String> salesSPX = new TreeMap<>();
		List<List<String>> salesSummarySPX = zesniaFundemantalService.getProjectedGrowthSummary("SPX");
		for (List<String> a : salesSummarySPX) {
			salesSPX.put(a.get(0) + " " + a.get(1), a.get(3));
		}
		projectedSalesGrowth.put("SPX", salesSPX);
		
		
		TreeMap<String, String> salesNDX = new TreeMap<>();
		List<List<String>> salesSummaryNDX = zesniaFundemantalService.getProjectedGrowthSummary("NDX");
		for (List<String> a : salesSummaryNDX) {
			salesNDX.put(a.get(0) + " " + a.get(1), a.get(3));
		}
		projectedSalesGrowth.put("NDX", salesNDX);
		
		
		TreeMap<String, String> salesSOX = new TreeMap<>();
		List<List<String>> salesSummarySOX = zesniaFundemantalService.getProjectedGrowthSummary("SOX");
		for (List<String> a : salesSummarySOX) {
			salesSOX.put(a.get(0) + " " + a.get(1), a.get(3));
		}
		projectedSalesGrowth.put("SOX", salesSOX);
		summary.put("Projected Sales Growth",projectedSalesGrowth);
		
		// Data for S&P
		
		JSONObject marginGrowth = new JSONObject();
		TreeMap<String, String> opmGrowthIndexSPX = new TreeMap<>();
		List<List<String>> opmGrowthSummarySPX = zesniaFundemantalService.getProjectedMarginSummary("SPX");
		for (List<String> a : opmGrowthSummarySPX) {
			opmGrowthIndexSPX.put(a.get(0) + " " + a.get(1), a.get(2));
		}
		marginGrowth.put("SPX", opmGrowthIndexSPX);

	// Data for Nasdaq
		TreeMap<String, String> opmGrowthIndexNDX = new TreeMap<>();
		List<List<String>> opmGrowthSummaryNDX = zesniaFundemantalService.getProjectedMarginSummary("NDX");
		for (List<String> a : opmGrowthSummaryNDX) {
			opmGrowthIndexNDX.put(a.get(0) + " " + a.get(1), a.get(2));
		}
		marginGrowth.put("NDX", opmGrowthIndexNDX);
	// Data for SOX
		TreeMap<String, String> opmGrowthIndexSOX = new TreeMap<>();
		List<List<String>> opmGrowthSummarySOX = zesniaFundemantalService.getProjectedMarginSummary("SOX");
		for (List<String> a : opmGrowthSummarySOX) {
			opmGrowthIndexSOX.put(a.get(0) + " " + a.get(1), a.get(2));
		}
		marginGrowth.put("SOX", opmGrowthIndexSOX);
		
		summary.put("Projected Margin OPM",marginGrowth);
		
		
		//Sales growth
		JSONObject projectedMarginNpm = new JSONObject();
		TreeMap<String, String> npmSPX = new TreeMap<>();
		List<List<String>> npmSummarySPX = zesniaFundemantalService.getProjectedMarginSummary("SPX");
		for (List<String> a : npmSummarySPX) {
			npmSPX.put(a.get(0) + " " + a.get(1), a.get(3));
		}
		projectedMarginNpm.put("SPX", npmSPX);
		
		
		TreeMap<String, String> npmNDX = new TreeMap<>();
		List<List<String>> npmSummaryNDX = zesniaFundemantalService.getProjectedMarginSummary("NDX");
		for (List<String> a : npmSummaryNDX) {
			npmNDX.put(a.get(0) + " " + a.get(1), a.get(3));
		}
		projectedMarginNpm.put("NDX", npmNDX);
		
		
		TreeMap<String, String> npmSOX = new TreeMap<>();
		List<List<String>> npmSummarySOX = zesniaFundemantalService.getProjectedMarginSummary("SOX");
		for (List<String> a : npmSummarySOX) {
			npmSOX.put(a.get(0) + " " + a.get(1), a.get(3));
		}
		projectedMarginNpm.put("SOX", npmSOX);
		summary.put("Projected Margin NPM",projectedMarginNpm);
		

		System.out.println("From /zesniaProjectedApi.....");

		return new ResponseEntity<>(summary.toString(), HttpStatus.OK);
	}
	
	@GetMapping(value="/peBandApi")
	@ResponseBody
	public ResponseEntity<String> peBandApi() throws ClassNotFoundException, SQLException{
		JSONObject totalPEBand = new JSONObject();
		//PE Band
		JSONObject peBand = new JSONObject();
		TreeMap<Date,String> peBandValueSPX = zesniaFundemantalService.getPeBandValue("S&P");
		TreeMap<Date,String> peBandValueNDX = zesniaFundemantalService.getPeBandValue("Nasdaq");
		TreeMap<Date,String> peBandValueSOX = zesniaFundemantalService.getPeBandValue("SOX");
		peBand.put("S&P", peBandValueSPX);
		peBand.put("Nasdaq", peBandValueNDX);
		peBand.put("SOX", peBandValueSOX);
		
		//Average 5 & 10;
		JSONObject peAvg = new JSONObject();
		TreeMap<String, Double> peAvgSPX = new TreeMap<String, Double>();
		Double peAvgSPX5y = zesniaFundemantalService.getAverageBandValue("S&P",5);
		Double peAvgSPX10y = zesniaFundemantalService.getAverageBandValue("S&P",10);
		peAvgSPX.put("5y", peAvgSPX5y);
		peAvgSPX.put("10y", peAvgSPX10y);
		peAvg.put("S&P", peAvgSPX);
		
		TreeMap<String, Double> peAvgNDX = new TreeMap<String, Double>();
		Double peAvgNDX5y = zesniaFundemantalService.getAverageBandValue("Nasdaq",5);
		Double peAvgNDX10y = zesniaFundemantalService.getAverageBandValue("Nasdaq",10);
		peAvgNDX.put("5y", peAvgNDX5y);
		peAvgNDX.put("10y", peAvgNDX10y);
		peAvg.put("Nasdaq", peAvgNDX);
		
		TreeMap<String, Double> peAvgSOX = new TreeMap<String, Double>();
		Double peAvgSOX5y = zesniaFundemantalService.getAverageBandValue("SOX",5);
		Double peAvgSOX10y = zesniaFundemantalService.getAverageBandValue("SOX",10);
		peAvgSOX.put("5y", peAvgSOX5y);
		peAvgSOX.put("10y", peAvgSOX10y);
		peAvg.put("SOX", peAvgSOX);
		
		totalPEBand.put("All PE", peBand);
		totalPEBand.put("Average PE", peAvg);
		
		return new ResponseEntity<String>(totalPEBand.toString(),HttpStatus.OK);
		
	}
	
	@GetMapping(value="/smeApi")
	@ResponseBody
	public ResponseEntity<String> getSmeData() throws ClassNotFoundException, SQLException, InterruptedException{
		JSONObject smeData = new JSONObject();
		TreeMap<String, Map<String, String>> companyData = smeService.getSmeCalculation(smeService.getSmeTicker());
		TreeMap<String, Double> smeSectorData = smeService.getSmeSectoralValue();
		smeData.put("SME", companyData);
		smeData.put("SME Sector data", smeSectorData);
		return new ResponseEntity<String>(smeData.toString(),HttpStatus.OK);
	}
	
	@PostMapping(value = "/zsenia/weeklyEps/growth")
	@ResponseBody
	public ResponseEntity<?> getWeeklyZesniaEpsGrowth(@RequestBody Map<String, Object> responsebody) throws ClassNotFoundException, SQLException{
		String idx = (String) responsebody.get("idx");
		String date = (String) responsebody.get("date");
		ArrayList<ArrayList<String>> tickerList = zesniaFundemantalService.getWeeklyEPSData(idx, date);
		JSONArray arr = new JSONArray();
		for(ArrayList<String> data: tickerList) {
			JSONObject obj = new JSONObject();
			obj.put("Ticker", data.get(0));
			obj.put("Report_Date", data.get(1));
			obj.put("Current_Estimated_EPS", data.get(2));
			obj.put("Previous_Actual_EPS", data.get(3));
			obj.put("EPS_Growth", data.get(4));
			obj.put("Current_MarketCap", data.get(5));
			obj.put("Previous_MarketCap", data.get(6));
			arr.put(obj);
		}
		return new ResponseEntity<String>(arr.toString(),HttpStatus.OK);
	}
	
	@PostMapping(value = "/zsenia/ltm/growth")
	@ResponseBody
	public ResponseEntity<?> getltmZesniaEpsSalesGrowth(@RequestBody Map<String, Object> responsebody) throws ClassNotFoundException, SQLException{
		String idx = (String) responsebody.get("idx");
		List<List<String>> tickerList = zesniaFundemantalService.getLtmgrowths(idx);
		JSONArray arr = new JSONArray();
		for(List<String> data: tickerList) {
			JSONObject obj = new JSONObject();
			obj.put("Flag", data.get(0));
			obj.put("Ticker", data.get(1));
			obj.put("Sector", data.get(2));
			obj.put("Estimated_Sales", data.get(3));
			obj.put("Actual_Sales", data.get(4));
			obj.put("Estimated_Eps", data.get(5));
			obj.put("Actual_Eps", data.get(6));
			obj.put("MarketCap", data.get(7));
			arr.put(obj);
		}
		return new ResponseEntity<String>(arr.toString(),HttpStatus.OK);
	}
	
	@PostMapping(value = "/zsenia/revision/growth")
	@ResponseBody
	public ResponseEntity<?> getRevisionZesniaEpsSalesGrowth(@RequestBody Map<String, Object> responsebody) throws ClassNotFoundException, SQLException{
		String idx = (String) responsebody.get("idx");
		List<List<String>> tickerList = zesniaFundemantalService.getRevisionGrowths(idx);
		JSONArray arr = new JSONArray();
		for(List<String> data: tickerList) {
			JSONObject obj = new JSONObject();
			obj.put("Flag", data.get(0));
			obj.put("Ticker", data.get(1));
			obj.put("Sector", data.get(2));
			obj.put("Estimated_Sales", data.get(3));
			obj.put("Actual_Sales", data.get(4));
			obj.put("Estimated_Eps", data.get(5));
			obj.put("Actual_Eps", data.get(6));
			obj.put("MarketCap", data.get(7));
			arr.put(obj);
		}
		return new ResponseEntity<String>(arr.toString(),HttpStatus.OK);
	}
}
