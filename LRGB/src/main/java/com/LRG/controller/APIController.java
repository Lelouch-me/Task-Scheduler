package com.LRG.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.LRG.Utils.CommonUtils;
import com.LRG.Utils.Constants;
import com.LRG.Utils.StatsUtils;
import com.LRG.domain.AdjustedPrice;
import com.LRG.model.IndexDto;
import com.LRG.model.OptimizerRequest;
import com.LRG.model.RetirementPlanningDto;
import com.LRG.model.TickerDto;
import com.LRG.service.CompanyService;
import com.LRG.service.IndexService;
import com.LRG.service.MarketMoverService;
import com.LRG.service.MyFinanceService;
import com.LRG.service.WatchListService;

@RestController
@RequestMapping("/api/chartData")
public class APIController {
	
	@Autowired
    private IndexService indexService;
	
	@Autowired
    private CompanyService companyService;
	
	@Autowired
	private MarketMoverService marketMoverService;
	
	@Autowired
	private MyFinanceService myFinanceService;
	
	@GetMapping("/index/{id}")
	@ResponseBody
    public ResponseEntity<String> getIndexData(@PathVariable(value = "id") String id) {
		List<IndexDto> indexList = indexService.getIndexData(id);	
		JSONObject obj = new JSONObject();
		JSONArray timeArray = new JSONArray();
		JSONArray valueArray = new JSONArray();
		for(IndexDto index : indexList) {
			timeArray.put(index.getDateTime().substring(0,5));
			valueArray.put(index.getCapitalValue());
		}
		obj.put("time",timeArray);
		obj.put("capitalValue",valueArray);
		
		System.out.println("request on /api/chartData/index/"+id);
		return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
    }
	
	@GetMapping("/sectorReturn")
	@ResponseBody
    public ResponseEntity<String> getSectorReturnData() {
		Map<String,Double> sectorDataMap = marketMoverService.getSectorReturn();
		JSONObject obj = new JSONObject();
		JSONArray sectorArray = new JSONArray();
		JSONArray returnArray = new JSONArray();
		for(Map.Entry<String,Double> entry : sectorDataMap.entrySet()) {
			sectorArray.put(entry.getKey());
			returnArray.put(entry.getValue());
		}
		obj.put("sector",sectorArray);
		obj.put("returns",returnArray);
		System.out.println("request on /api/chartData/sectorReturn/");
		return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
    }
	
	@GetMapping("/sectorTurnover")
	@ResponseBody
    public ResponseEntity<String> getSectorTurnoverData() {
		Map<String,Double> sectorDataMap = marketMoverService.getSectorTurnover();
		JSONObject obj = new JSONObject();
		JSONArray sectorArray = new JSONArray();
		JSONArray turnoverArray = new JSONArray();
		for(Map.Entry<String,Double> entry : sectorDataMap.entrySet()) {
			sectorArray.put(entry.getKey());
			turnoverArray.put(entry.getValue());
		}
		obj.put("sector",sectorArray);
		obj.put("turnover",turnoverArray);
		System.out.println("request on /api/chartData/sectorTurnover/");
		return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
    }
	
	@GetMapping("/company/{code}")
	@ResponseBody
    public ResponseEntity<String> getCompanyData(@PathVariable(value = "code") String code) {
		Map<String,String> priceVolumeInfoMap = companyService.getPriceAndVolumeData(code);	
		JSONObject obj = new JSONObject();
		JSONArray dateArray = new JSONArray();
		JSONArray priceArray = new JSONArray();
		JSONArray volumeArray = new JSONArray();
//		Set<String> months = new HashSet<String>();
		for(Map.Entry<String,String> entry : priceVolumeInfoMap.entrySet()) {
			JSONArray datePriceArray = new JSONArray();
			JSONArray dateVolumeArray = new JSONArray();
			
			datePriceArray.put(entry.getKey());
			datePriceArray.put(new Double(entry.getValue().split("::")[0]));
			dateVolumeArray.put(entry.getKey());
			dateVolumeArray.put(new Double(entry.getValue().split("::")[1]));
			
			try {
				Date date = new SimpleDateFormat("yyyy-MM-dd").parse(entry.getKey());
				String monthYear = new SimpleDateFormat("MMM").format(date) + " "+new SimpleDateFormat("yy").format(date);
				
//				if(!months.contains(monthYear)) {
//					months.add(monthYear);
//					dateArray.put(monthYear);
//				}
				dateArray.put(monthYear);
				priceArray.put(datePriceArray);
				volumeArray.put(dateVolumeArray);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		obj.put("date",dateArray);
		obj.put("price",priceArray);
		obj.put("volume",volumeArray);
		
		System.out.println("request on /api/chartData/company/"+code);
		return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
    }
	
	@GetMapping("/stock/{code}")
	@ResponseBody
    public ResponseEntity<String> getStockData(@PathVariable(value = "code") String code,String dateRange,String bmTicker) {
		List<String> indices = Constants.populateIndexList();
		List<String> foreignIndices = Constants.populateForeignIndexList();
		boolean isIndex = indices.contains(code) || indices.contains(bmTicker);
		boolean isForeignIndex = foreignIndices.contains(code) || indices.contains(bmTicker);
		boolean isTickerMF = false;
		//boolean isTickerMF = indices.stream().anyMatch(in -> in.split(" ")[0].equals(code));
		boolean isBMTickerMF = indices.stream().anyMatch(in -> in.split(" ")[0].equals(bmTicker));
		
		List<AdjustedPrice> dailyPriceVolumeList = companyService.getPriceAndVolumeData(code,dateRange,isIndex,false,isForeignIndex,
				isTickerMF || isBMTickerMF,isTickerMF);	
		List<AdjustedPrice> dailyPriceVolumeListForTable = companyService.getPriceAndVolumeData(code,dateRange,isIndex,true,isForeignIndex,
				isTickerMF || isBMTickerMF,isTickerMF);	
		Map<String,String> priceVolumeMap = companyService.getModifiedPriceData(dailyPriceVolumeList);	
		List<AdjustedPrice> dailyBMPriceList = companyService.getPriceAndVolumeData(bmTicker,dateRange,isIndex,true,isForeignIndex,isTickerMF || isBMTickerMF,isBMTickerMF);
		Map<String,Double> tableDataMap = companyService.getTableChartData(dailyPriceVolumeListForTable,dailyBMPriceList,isIndex || isTickerMF || isBMTickerMF);
		int dayDiff = CommonUtils.getDayDifference(dateRange);
		
		JSONObject obj = new JSONObject();
		JSONArray dateArray = new JSONArray();
		JSONArray modifiedPriceArray = new JSONArray();
		JSONArray priceArray = new JSONArray();
		JSONArray volumeArray = new JSONArray();
		JSONArray tableDataArray = new JSONArray();
		JSONArray dataPointArray = new JSONArray();

		for(Map.Entry<String,String> entry : priceVolumeMap.entrySet()) {
			JSONArray dateModifiedPriceArray = new JSONArray();
			JSONArray datePriceArray = new JSONArray();
			JSONArray dateVolumeArray = new JSONArray();
			
			dateModifiedPriceArray.put(entry.getKey());
			dateModifiedPriceArray.put(new Double(entry.getValue().split("::")[0]));
			datePriceArray.put(entry.getKey());
			datePriceArray.put(new Double(entry.getValue().split("::")[1]));
			dateVolumeArray.put(entry.getKey());
			
			indices.add("DSEX");indices.add("DS30");
			if(!indices.contains(code) && !foreignIndices.contains(code)) {
				dateVolumeArray.put(new Double(entry.getValue().split("::")[2]));
			}
			
			try {
				Date date = new SimpleDateFormat("yyyy-MM-dd").parse(entry.getKey());
				String monthYear = null;
				if(dayDiff<=92) {
					monthYear = new SimpleDateFormat("dd").format(date) + " "+ new SimpleDateFormat("MMM").format(date) + " "+
							new SimpleDateFormat("yyyy").format(date);
				}else {
					monthYear = new SimpleDateFormat("MMM").format(date) + " "+new SimpleDateFormat("yyyy").format(date);
				}	
				
				dateArray.put(monthYear);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			modifiedPriceArray.put(dateModifiedPriceArray);
			priceArray.put(datePriceArray);
			volumeArray.put(dateVolumeArray);
		}
		
		for(Map.Entry<String,Double> entry : tableDataMap.entrySet()) {
			dataPointArray.put(entry.getKey());
			if(entry.getValue()==null || Double.isNaN(entry.getValue()) || Double.isInfinite(entry.getValue())){
				tableDataArray.put(99999999);
			}else {
				tableDataArray.put(entry.getValue());
			}
		}
		
		obj.put("date",dateArray);
		obj.put("modifiedPrice",modifiedPriceArray);
		obj.put("price",priceArray);
		obj.put("volume",volumeArray);
		obj.put("indicatorHeading",dataPointArray);
		obj.put("indicatorData",tableDataArray);
		indices.addAll(foreignIndices);
		obj.put("indices",indices.toArray());
		
		System.out.println("request on /api/chartData/stock/"+code);
		return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
    }
	
	@GetMapping("/stockStartDate")
	@ResponseBody
    public ResponseEntity<String> getStockAnalysisStartDate(String dateRange,String tickers) {
		List<String> tickerList = Arrays.asList(tickers.split(","));    
		String modifiedDateRange = companyService.getModifiedDateRange(dateRange,tickerList);
		JSONObject obj = new JSONObject();
		obj.put("modifiedDateRange",modifiedDateRange);
		System.out.println("request on /api/chartData/stockStartDate");
		return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
	}
	
	@GetMapping("/sectorValue")
	@ResponseBody
    public ResponseEntity<String> getSectorValueData() {
		Map<String,Double> sectorDataMap = marketMoverService.getSectorTurnover();
		Map<String,String> tickerDataMap = marketMoverService.getTickerValue();
		Map<String,String> tickerDataMap2 = marketMoverService.getTickerInfo();
		Map<String,Double> sectorReturnMap = marketMoverService.getSectorReturn();
		JSONArray objArray = new JSONArray();
		for(Map.Entry<String,Double> entry : sectorDataMap.entrySet()) {
			JSONObject obj = new JSONObject();
			obj.put("id",entry.getKey());
			obj.put("name",entry.getKey());
			obj.put("sec_ret","Return: "+sectorReturnMap.get(entry.getKey())+"%");
			obj.put("color",sectorReturnMap.get(entry.getKey())>=0 ? "#1E5631" : "#CC252C");
			objArray.put(obj);
		}
	
		for(Map.Entry<String,String> entry : tickerDataMap.entrySet()) {
			JSONObject obj = new JSONObject();
			obj.put("name",entry.getKey());
			obj.put("value",Double.parseDouble(entry.getValue().split("::")[1]));
			obj.put("parent",entry.getValue().split("::")[0]);
			
			obj.put("ltp","LTP: "+tickerDataMap2.get(entry.getKey()).split("::")[1]);
			obj.put("ret","Return: "+tickerDataMap2.get(entry.getKey()).split("::")[0]+"%");
			obj.put("trade","Total Trade: "+tickerDataMap2.get(entry.getKey()).split("::")[2]);
			if(tickerDataMap2.get(entry.getKey()).split("::")[0].equals("null")) continue;
			double ret = Double.parseDouble(tickerDataMap2.get(entry.getKey()).split("::")[0]);
			if(ret>=6) {
				obj.put("color","#1E5631");
			}else if(ret>=3) {
				obj.put("color","#4C9A2A");
			}else if(ret>=1) {
				obj.put("color","#68BB59");
			}else if(ret>=0) {
				obj.put("color","#545454");
			}else if(ret>=-1) {
				obj.put("color","#B30000");
			}else if(ret>=-3) {
				obj.put("color","#B22222");
			}else if(ret>=-6) {
				obj.put("color","#800000");
			}else {
				obj.put("color","#8B0000");
			}
			objArray.put(obj);
		}
		System.out.println("request on /api/chartData/sectorValue/");
		return new ResponseEntity<>(objArray.toString(), HttpStatus.OK);
    }
	
	@GetMapping("/sectorVolume")
	@ResponseBody
    public ResponseEntity<String> getSectorVolumeData() {
		Map<String,Double> sectorDataMap = marketMoverService.getSectorTurnover();
		Map<String,String> tickerDataMap = marketMoverService.getTickerVolume();
		Map<String,String> tickerDataMap2 = marketMoverService.getTickerInfo();
		Map<String,Double> sectorReturnMap = marketMoverService.getSectorReturn();
		JSONArray objArray = new JSONArray();
		for(Map.Entry<String,Double> entry : sectorDataMap.entrySet()) {
			JSONObject obj = new JSONObject();
			obj.put("id",entry.getKey());
			obj.put("name",entry.getKey());
			obj.put("sec_ret","Return: "+sectorReturnMap.get(entry.getKey())+"%");
			obj.put("color",sectorReturnMap.get(entry.getKey())>=0 ? "#1E5631" : "#CC252C");
			objArray.put(obj);
		}
		
		for(Map.Entry<String,String> entry : tickerDataMap.entrySet()) {
			JSONObject obj = new JSONObject();
			obj.put("name",entry.getKey());
			obj.put("value",Double.parseDouble(entry.getValue().split("::")[1])/1000000);
			obj.put("parent",entry.getValue().split("::")[0]);
			
			obj.put("ltp","LTP: "+tickerDataMap2.get(entry.getKey()).split("::")[1]);
			obj.put("ret","Return: "+tickerDataMap2.get(entry.getKey()).split("::")[0]+"%");
			obj.put("trade","Total Trade: "+tickerDataMap2.get(entry.getKey()).split("::")[2]);
			if(tickerDataMap2.get(entry.getKey()).split("::")[0].equals("null")) continue;
			double ret = Double.parseDouble(tickerDataMap2.get(entry.getKey()).split("::")[0]);
			if(ret>=6) {
				obj.put("color","#1E5631");
			}else if(ret>=3) {
				obj.put("color","#4C9A2A");
			}else if(ret>=1) {
				obj.put("color","#68BB59");
			}else if(ret>=0) {
				obj.put("color","#545454");
			}else if(ret>=-1) {
				obj.put("color","#B30000");
			}else if(ret>=-3) {
				obj.put("color","#B22222");
			}else if(ret>=-6) {
				obj.put("color","#800000");
			}else {
				obj.put("color","#8B0000");
			}
			objArray.put(obj);	
		}
		System.out.println("request on /api/chartData/sectorVolume/");
		return new ResponseEntity<>(objArray.toString(), HttpStatus.OK);
    }
	
	@GetMapping("/sectorMarketCap")
	@ResponseBody
    public ResponseEntity<String> getSectorMcapData() {
		Map<String,Double> sectorDataMap = marketMoverService.getSectorTurnover();
		Map<String,String> tickerDataMap = marketMoverService.getTickerMcap();
		Map<String,String> tickerDataMap2 = marketMoverService.getTickerInfo();
		Map<String,Double> sectorReturnMap = marketMoverService.getSectorReturn();
		JSONArray objArray = new JSONArray();
		for(Map.Entry<String,Double> entry : sectorDataMap.entrySet()) {
			JSONObject obj = new JSONObject();
			obj.put("id",entry.getKey());
			obj.put("name",entry.getKey());
			obj.put("sec_ret","Return: "+sectorReturnMap.get(entry.getKey())+"%");
			obj.put("color",sectorReturnMap.get(entry.getKey())>=0 ? "#1E5631" : "#CC252C");
			objArray.put(obj);
		}
		
		for(Map.Entry<String,String> entry : tickerDataMap.entrySet()) {
			JSONObject obj = new JSONObject();
			obj.put("name",entry.getKey());
			obj.put("value",Double.parseDouble(entry.getValue().split("::")[1])/1000000);
			obj.put("parent",entry.getValue().split("::")[0]);
			
			obj.put("ltp","LTP: "+tickerDataMap2.get(entry.getKey()).split("::")[1]);
			obj.put("ret","Return: "+tickerDataMap2.get(entry.getKey()).split("::")[0]+"%");
			obj.put("trade","Total Trade: "+tickerDataMap2.get(entry.getKey()).split("::")[2]);
			if(tickerDataMap2.get(entry.getKey()).split("::")[0].equals("null")) continue;
			double ret = Double.parseDouble(tickerDataMap2.get(entry.getKey()).split("::")[0]);
			if(ret>=6) {
				obj.put("color","#1E5631");
			}else if(ret>=3) {
				obj.put("color","#4C9A2A");
			}else if(ret>=1) {
				obj.put("color","#68BB59");
			}else if(ret>=0) {
				obj.put("color","#545454");
			}else if(ret>=-1) {
				obj.put("color","#B30000");
			}else if(ret>=-3) {
				obj.put("color","#B22222");
			}else if(ret>=-6) {
				obj.put("color","#800000");
			}else {
				obj.put("color","#8B0000");
			}
			objArray.put(obj);
		}
		System.out.println("request on /api/chartData/sectorMarketCap/");
		return new ResponseEntity<>(objArray.toString(), HttpStatus.OK);
    }
	
	@SuppressWarnings("removal")
	@PostMapping("/personalFinance")
	@ResponseBody
	public RetirementPlanningDto calculateShortfall(@RequestHeader("Authorization") String authorizationToken,@RequestBody RetirementPlanningDto shortFallCalculatorDto) throws ClassNotFoundException, SQLException {
		String userId = WatchListService.getUserId(authorizationToken);
		if(!userId.equals("0")) {
			WatchListService.insertPersonalFinanceInfoDb(userId, shortFallCalculatorDto);
		}
		RetirementPlanningDto shortFallCalculatorResponse = new RetirementPlanningDto();
		List<Double> persistencyList = new ArrayList<Double>();
		try {
			if(shortFallCalculatorDto.getSpouse() == null || shortFallCalculatorDto.getSpouse().isEmpty()) {
				shortFallCalculatorDto.setSpouse("NONE");
				shortFallCalculatorDto.setSpouseAge(null);
			} else if (shortFallCalculatorDto.getSpouse().equalsIgnoreCase("NONE")) {
				shortFallCalculatorDto.setSpouseAge(null);
			}
			
			if(shortFallCalculatorDto.getContributionWithdrawalType() == null || shortFallCalculatorDto.getContributionWithdrawalType().isEmpty()) {
				shortFallCalculatorDto.setContributionWithdrawalType(Constants.DEFAULT_CONTRIBUTION_WITHDRAWL_TYPE);
			}
			shortFallCalculatorDto.setAcceptableShortfall(0.0);
			
			String spouse = shortFallCalculatorDto.getSpouse();
			if(spouse == null || spouse.isEmpty() || spouse.equalsIgnoreCase("NONE") || shortFallCalculatorDto.getSpouseAge() == null) {
				persistencyList = StatsUtils.getPersistencyList(shortFallCalculatorDto.getInvestorAge(), shortFallCalculatorDto.getGender());
			} else {
				List<Double> primaryPersistencyList = StatsUtils.getPersistencyList(shortFallCalculatorDto.getInvestorAge(), shortFallCalculatorDto.getGender());
				List<Double> secondaryPersistencyList = StatsUtils.getPersistencyList(shortFallCalculatorDto.getSpouseAge(), spouse);
				for(int i=0; i<primaryPersistencyList.size(); i++) {
					persistencyList.add(1.0 - ((1.0-primaryPersistencyList.get(i)) * (1.0-secondaryPersistencyList.get(i))));
				}
			}
			Double lifeExpectancy = 0.0;
			for(int i=0; i<persistencyList.size()-1; i++) {
				lifeExpectancy += (i+1) * (persistencyList.get(i) - persistencyList.get(i+1));
			}

			Double revisedTargetReturn = Constants.MAX_PORTFOLIO_RET;
			double lastMaxReturn = Constants.MAX_PORTFOLIO_RET;
			double lastMinReturn = 0.0;
			BigDecimal shortFall = null;
			OptimizerRequest optReqst = null;
			Map<String, String> outputStats = null;
			Double optimizedReturn = null;
			Double optimizedVolatility = null;
			boolean foundMinReturn = false;
			myFinanceService.generateContributionWithdrawlList(shortFallCalculatorDto);
			
			while(true) {
				optReqst = myFinanceService.generateOptimizerRequest(revisedTargetReturn, shortFallCalculatorDto);
				outputStats = myFinanceService.optimize(optReqst).getOptimizedWTMap();
				optimizedReturn = Double.parseDouble(outputStats.get(Constants.OPTOUT_OPTMU));
				optimizedVolatility = Double.parseDouble(outputStats.get(Constants.OPTOUT_OPTVOL));
				try {
					shortFall = myFinanceService.calculateExpectedShortfall(shortFallCalculatorDto, persistencyList, 
							optimizedReturn, optimizedVolatility);
				} catch (Exception e) {
					if(e instanceof NumberFormatException) {
						System.out.println("Number format exception while calculating shortfall. Trying again.." + shortFall);
						shortFall = myFinanceService.calculateExpectedShortfall(shortFallCalculatorDto, persistencyList, 
								optimizedReturn, optimizedVolatility);
					} else {
						throw new Exception("Unable to calculate shortfall");
					}
				}
				if((revisedTargetReturn == Constants.MAX_PORTFOLIO_RET && 
						shortFall.doubleValue() > shortFallCalculatorDto.getAcceptableShortfall().doubleValue()) || foundMinReturn) break;
				if(shortFall.doubleValue() <= shortFallCalculatorDto.getAcceptableShortfall().doubleValue()) {
					if(new Double(revisedTargetReturn*100).intValue() % 2 != 0) {
						break;
					}
					lastMaxReturn = revisedTargetReturn;
					revisedTargetReturn = (lastMinReturn + revisedTargetReturn) / 2;
				} else {
					if(new Double(revisedTargetReturn*100).intValue() % 2 != 0) {
						revisedTargetReturn = lastMaxReturn;
						foundMinReturn = true;
						continue;
					}
					lastMinReturn = revisedTargetReturn;
					revisedTargetReturn = (lastMaxReturn + revisedTargetReturn)  / 2;
				}
			}
			
			BigDecimal mmfFixedWt = new BigDecimal(Double.parseDouble(new DecimalFormat("0.0000")
					.format(Double.parseDouble(outputStats.get(Constants.MONEY_MKT_FUNDS)))));
			BigDecimal sumOfFixedWt = mmfFixedWt; 
			Map<String, String> portfolioFixedWtOutput = new HashMap<String, String>();
			for(TickerDto tDto : optReqst.getInputTickers()) {
				if(!tDto.getSymbol().equalsIgnoreCase(Constants.MONEY_MKT_FUNDS)) {
					BigDecimal tickerWt = new BigDecimal(Double.parseDouble(outputStats.get(tDto.getSymbol())));
					sumOfFixedWt = sumOfFixedWt.add(tickerWt);
				}
			}
			for(TickerDto tDto : optReqst.getInputTickers()) {
				if(!tDto.getSymbol().equalsIgnoreCase(Constants.MONEY_MKT_FUNDS)) {
					if(sumOfFixedWt.equals(new BigDecimal(0.0))) {
						portfolioFixedWtOutput.put(tDto.getSymbol(), "0.0");
					} else {
						portfolioFixedWtOutput.put(tDto.getSymbol(), new BigDecimal(Double.parseDouble(outputStats.get(tDto.getSymbol())))
								.divide(sumOfFixedWt, 8, RoundingMode.HALF_UP) + "");
					}
				}
			}
			shortFallCalculatorResponse.setAssetValuePerSimulationList(shortFallCalculatorDto.getAssetValuePerSimulationList());
			shortFallCalculatorResponse.setShortfallProbability(shortFallCalculatorDto.getShortfallProbability());
			shortFallCalculatorResponse.setPortfolioFixedWtOutput(portfolioFixedWtOutput);
			shortFallCalculatorResponse.setTickerDtoList(optReqst.getInputTickers());
			shortFallCalculatorResponse.setExpectedShortfall(shortFall.doubleValue());
			shortFallCalculatorResponse.setTargetReturn(optimizedReturn);
			shortFallCalculatorResponse.setTargetVolatility(optimizedVolatility);
			shortFallCalculatorResponse.setLifeExpectancy(new DecimalFormat("0.00").format(lifeExpectancy));
			shortFallCalculatorResponse.setAvgAssetValue(shortFallCalculatorDto.getAvgAssetValue());
			shortFallCalculatorResponse.setSuccess(true);
		} catch (Exception ex) {
			System.out.println("Error during Retirement Plan calculation : "+ ex);
			shortFallCalculatorResponse.setSuccess(false);
			shortFallCalculatorResponse.setErrorType(ex.getClass().getSimpleName());
			shortFallCalculatorResponse.setErrorCode(Constants.SYSTEM_ERROR);
			shortFallCalculatorResponse.setErrorDescription(ex.getMessage() + ": " + ex.toString());
		}
		return shortFallCalculatorResponse;
	}
}
