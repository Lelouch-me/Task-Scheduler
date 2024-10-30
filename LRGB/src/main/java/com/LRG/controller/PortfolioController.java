package com.LRG.controller;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.LRG.Utils.BusinessException;
import com.LRG.Utils.CommonUtils;
import com.LRG.Utils.Constants;
import com.LRG.domain.AdjustedPrice;
import com.LRG.model.MarketMoverDto;
import com.LRG.service.AnalyticsService;
import com.LRG.service.BizzService;
import com.LRG.service.CompanyService;
import com.LRG.service.MarketMoverService;
import com.LRG.service.WatchListService;

@RestController
public class PortfolioController {
	@Value("${optimizer.exe.file.path}")
	private String EXE_FILE_PATH;
	
	DecimalFormat df = new DecimalFormat("#.##");
	private static Log logger = LogFactory.getLog(PortfolioController.class);
	
	@Autowired
	private BizzService bizzService;
	
	@Autowired
	private MarketMoverService marketMoverService;
	
	@Autowired
    private CompanyService companyService;
	
	@GetMapping(value="/bizz/topTickers")
	@ResponseBody
	public ResponseEntity<String> getTopTickers() throws ClassNotFoundException, SQLException{
		JSONArray ticker = new JSONArray();
		List<MarketMoverDto> highMarketMoverList = marketMoverService.getTop10MarketMover("first");
		for(MarketMoverDto m : highMarketMoverList) {
			ticker.put(m.getCode());
		}
		return new ResponseEntity<String>(ticker.toString(),HttpStatus.OK);
	}
	
	@PostMapping(value="/user/watchlist")
	public ResponseEntity<String> createDefaultWatchList(@RequestParam(name = "id") int userId,  @RequestBody Map<String, Object> userWatchlist) throws ClassNotFoundException, SQLException {
		boolean flag = bizzService.createNewWatchList(userId,userWatchlist);
		JSONObject obj = new JSONObject();
		if(flag) {
			obj.put("Message", "User Added to Watchlist Successfully");
			obj.put("Status", flag);
		}
		else {
			obj.put("Message", "Error in Watchlist Creation.");
			obj.put("Status", flag);
		}
		return new ResponseEntity<String>(obj.toString(), HttpStatus.OK);
	}
	
	@GetMapping(value="/user/watchlist/list")
	public ResponseEntity<String> allWatchlistList(@RequestHeader("Authorization") String authorizationToken) throws ClassNotFoundException, SQLException{
		List<Map<String, Object>>watchList = bizzService.getWatchListIdAndName(authorizationToken);
		JSONArray jsonArray = new JSONArray();
		for(Map<String,Object> map : watchList) {
			JSONObject jsonObj = new JSONObject(map);
			jsonArray.put(jsonObj);
		}
		
		if(!watchList.isEmpty()) {
			return new ResponseEntity<String>(jsonArray.toString(), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<String>("User Not Found", HttpStatus.OK);
		}
	}
	
	@PostMapping(value="/user/watchlist/view")
	public ResponseEntity<String> viweWatchList(@RequestHeader("Authorization") String authorizationToken,@RequestBody Map<String, Object> userWatchlistID ) throws ClassNotFoundException, SQLException {
		List<Map<String, Object>>watchList = bizzService.viewWatchList(authorizationToken, userWatchlistID);
		JSONObject listObj = new JSONObject();
		if(!watchList.isEmpty()) {
			listObj.put("defaultWatchList", watchList);
			return new ResponseEntity<String>(listObj.toString(), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<String>("User Not Found", HttpStatus.OK);
		}	
	}
	
	@PostMapping(value="/user/watchlist/update")
	public ResponseEntity<?> createDefaultWatchList(@RequestHeader("Authorization") String authorizationToken,  @RequestBody Map<String, Object> userWatchlist) throws ClassNotFoundException, SQLException {
		Map<String,List<Map<String, Object>>>watchList = bizzService.updateWatchList(authorizationToken,userWatchlist);
		String message = "";
		List<Map<String, Object>>data = new ArrayList<Map<String, Object>>();
		if(watchList.containsKey("Weight")) {
			data = watchList.get("Weight");
			message = "Weight Successfully Updated";
		}
		else if(watchList.containsKey("Share")) {
			data = watchList.get("Share");
			message = "Share Successfully Updated";
		}
		JSONObject listObj = new JSONObject();
		if(!watchList.isEmpty()) {
			listObj.put("defaultWatchList", data);
			listObj.put("Message", message);
			return new ResponseEntity<String>(listObj.toString(), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<String>("User Not Found", HttpStatus.OK);
		}
	}
	
	@PostMapping(value="/user/watchlist/add")
	public ResponseEntity<?> addNewWatchList(@RequestHeader("Authorization") String authorizationToken,  @RequestBody Map<String, Object> userWatchlist) throws ClassNotFoundException, SQLException {
		Map<String,List<Map<String, Object>>>watchList = bizzService.addWatchList(authorizationToken,userWatchlist);
		String message = "";
		List<Map<String, Object>>data = new ArrayList<Map<String, Object>>();
		if(watchList.containsKey("Weight")) {
			data = watchList.get("Weight");
			message = "Weight Successfully Updated";
		}
		else if(watchList.containsKey("Share")) {
			data = watchList.get("Share");
			message = "Share Successfully Updated";
		}
		JSONObject listObj = new JSONObject();
		if(!watchList.isEmpty()) {
			listObj.put("defaultWatchList", data);
			listObj.put("Message", message);
			return new ResponseEntity<String>(listObj.toString(), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<String>("User Not Found", HttpStatus.OK);
		}
	}
	
	@PostMapping(value="/user/watchlist/delete")
	public ResponseEntity<?> deleteWatchList(@RequestHeader("Authorization") String authorizationToken,  @RequestBody Map<String, Object> userWatchlist) throws ClassNotFoundException, SQLException {
		boolean flag = bizzService.deleteWatchlsitForUser(authorizationToken,userWatchlist);
		if(flag) {
			return new ResponseEntity<String>("Watchlist Deleted Successfully", HttpStatus.OK);
		}
		else {
			return new ResponseEntity<String>("User Not Found", HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/user/watchlist/news")
	public ResponseEntity<String> watchListNews(@RequestHeader("Authorization") String authorizationToken,@RequestBody Map<String, Object> responseBody) throws ClassNotFoundException, SQLException {
		String watchlistId = (String) responseBody.get("watchlist_id");
		List<List<Object>>watchList = bizzService.getWatchListNews(authorizationToken,watchlistId);
		JSONArray listObj = new JSONArray();
		if(!watchList.isEmpty()) {
			for(List<Object> obj : watchList) {
				for(Object wtlst : obj) {
					listObj.put(wtlst);
				}
			}
			return new ResponseEntity<String>(listObj.toString(), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<String>("User Not Found", HttpStatus.OK);
		}	
	}
	
	@PostMapping(value="/user/watchlist/charts")
	public ResponseEntity<String> getChartData(@RequestHeader("Authorization") String authorizationToken,  @RequestBody Map<String, Object> responseBody) throws ClassNotFoundException, SQLException{
		JSONObject chartData = new JSONObject();
		String startDate = (String) responseBody.get("start_date");
		String endDate = (String) responseBody.get("end_date");
		String watchlistId = (String) responseBody.get("watchlist_id");
		TreeMap<String,Double> historicalChartData = bizzService.getWeightedHistoricalData(authorizationToken,watchlistId,startDate,endDate);
		TreeMap<String, Double> intraDayChartData = bizzService.getWeightedIntradayData(authorizationToken,watchlistId);
		TreeMap<String,Double> portfolioValueChartData = bizzService.getPortfolioValueData(authorizationToken,watchlistId,startDate,endDate);
		chartData.put("Historical Data", historicalChartData);
		chartData.put("Intraday Data", intraDayChartData);
		chartData.put("Portfolio Value",portfolioValueChartData);
		
//		chartData.put("IntraDay Data", intraDayChartData);
		return new ResponseEntity<String>(chartData.toString(), HttpStatus.OK);
		
	}
	
	@PostMapping(value="/user/portfolio/charts")
	public ResponseEntity<String> getPortfolioData(@RequestHeader("Authorization") String authorizationToken,  @RequestBody Map<String, Object> responseBody) throws ClassNotFoundException, SQLException{
		JSONObject chartData = new JSONObject();
		String startDate = (String) responseBody.get("start_date");
		String endDate = (String) responseBody.get("end_date");
		String watchlistId = (String) responseBody.get("watchlist_id");
		HashMap<String, Object> historicalChartData = WatchListService.getWeightedHistoricalData(authorizationToken, watchlistId,startDate,endDate);
		List<LinkedHashMap<String, String>> intraDayChartData = WatchListService.getWeightedIntradayData(authorizationToken, watchlistId);
		HashMap<String, String> intraDayUpperData= intraDayUpperData = WatchListService.getUserWeightedIntradayUpperData(authorizationToken,watchlistId);
		historicalChartData.put("intraDay", intraDayChartData);
		historicalChartData.put("IntradayUpperValue", intraDayUpperData);
		historicalChartData.put("intraDay", intraDayChartData);
		historicalChartData.put("IntradayUpperValue", intraDayUpperData);
		chartData.put("responseBody", historicalChartData);
		return new ResponseEntity<String>(chartData.toString(), HttpStatus.OK);
		
	}
	
	@PostMapping(value="/guest/watchlist/charts")
	public ResponseEntity<String> getGuestChartData(@RequestBody Map<String, Object> responseBody) throws ClassNotFoundException, SQLException{
		JSONObject chartData = new JSONObject();
		String startDate = (String) responseBody.get("start_date");
		String endDate = (String) responseBody.get("end_date");
		String type =  (String) responseBody.get("type");
		List<Map<String, Object>> defaultWatchList = (List<Map<String, Object>>) responseBody.get("data");
		TreeMap<String,Double> historicalChartData = new TreeMap<String,Double>();
		TreeMap<String, Double> intraDayChartData = new TreeMap<String, Double>();
		TreeMap<String, Double> tickerWeight = new TreeMap<>();
		
		if(type.equalsIgnoreCase("weight")) {
			for (Map<String, Object> entry : defaultWatchList) {
				
		        String ticker = (String) entry.get("ticker");
		        Integer share = (Integer) entry.get("weight");
		        String sshare = share.toString();
		        Double weight = Double.parseDouble(sshare);
		        tickerWeight.put(ticker, weight);
		        }
			historicalChartData =bizzService.getGuestWeightedHistoricalData(startDate, endDate,tickerWeight);;
			intraDayChartData = bizzService.getGuestWeightedIntradayData(tickerWeight);
		}
		else {
			for (Map<String, Object> entry : defaultWatchList) {
				
		        String ticker = (String) entry.get("ticker");
		        Integer share = (Integer) entry.get("share");
		        String sshare = share.toString();
		        Double weight = Double.parseDouble(sshare);
		        tickerWeight.put(ticker, weight);
		        }
			Map<String, Map<String, Double>> wweightedShare = bizzService.getWeightUsingShare(tickerWeight);
			if(wweightedShare.get("weight") != null) {
				historicalChartData =bizzService.getGuestWeightedHistoricalData(startDate, endDate,wweightedShare.get("weight"));;
				intraDayChartData = bizzService.getGuestWeightedIntradayData(tickerWeight);
			}
		}
		Set<String> tickers = tickerWeight.keySet();
		List<List<Object>> news = bizzService.getGuestWatchListNews(tickers);
		chartData.put("Historical Data", historicalChartData);
		chartData.put("Intraday Data", intraDayChartData);
		
		JSONArray listObj = new JSONArray();
		if(!news.isEmpty()) {
			for(List<Object> obj : news) {
				for(Object wtlst : obj) {
					listObj.put(wtlst);
				}
			}
		}
		chartData.put("News", listObj);
		return new ResponseEntity<String>(chartData.toString(), HttpStatus.OK);
		
	}
	
	@PostMapping(value="/guest/portfolio/charts")
	public ResponseEntity<String> getGuestprotfolioData(@RequestBody Map<String, Object> responseBody) throws ClassNotFoundException, SQLException{
		JSONObject chartData = new JSONObject();
		String startDate = (String) responseBody.get("start_date");
		String endDate = (String) responseBody.get("end_date");
		String type =  (String) responseBody.get("type");
		List<Map<String, Object>> defaultWatchList = (List<Map<String, Object>>) responseBody.get("data");
		HashMap<String, Object> historicalChartData = new HashMap<String,Object>();
		List<LinkedHashMap<String, String>> intraDayChartData= new ArrayList<>();
		HashMap<String, String> intraDayUpperData= new HashMap<String,String>();
		
		TreeMap<String, Double> tickerWeight = new TreeMap<>();
		
		if(type.equalsIgnoreCase("weight")) {
			for (Map<String, Object> entry : defaultWatchList) {
				
		        String ticker = (String) entry.get("ticker");
		        Integer share = (Integer) entry.get("weight");
		        String sshare = share.toString();
		        Double weight = Double.parseDouble(sshare);
		        tickerWeight.put(ticker, weight);
		        }
			historicalChartData =WatchListService.getGuestWeightedHistoricalData(startDate, endDate,tickerWeight);
			intraDayChartData = WatchListService.getGuestWeightedIntradayData(tickerWeight);
			intraDayUpperData = WatchListService.getGuestWeightedIntradayUpperData(tickerWeight);
		}
		else {
			for (Map<String, Object> entry : defaultWatchList) {
				
		        String ticker = (String) entry.get("ticker");
		        Integer share = (Integer) entry.get("share");
		        String sshare = share.toString();
		        Double weight = Double.parseDouble(sshare);
		        tickerWeight.put(ticker, weight);
		        }
			Map<String, Map<String, Double>> wweightedShare = WatchListService.getWeightUsingShare(tickerWeight);
			if(wweightedShare.get("weight") != null) {
				historicalChartData =WatchListService.getGuestWeightedHistoricalData(startDate, endDate,wweightedShare.get("weight"));;
				intraDayChartData = WatchListService.getGuestWeightedIntradayData(tickerWeight);
				intraDayUpperData = WatchListService.getGuestWeightedIntradayUpperData(tickerWeight);
			}
		}
		Set<String> tickers = tickerWeight.keySet();
		List<List<Object>> news = bizzService.getGuestWatchListNews(tickers);
		
		historicalChartData.put("intraDay", intraDayChartData);
		historicalChartData.put("IntradayUpperValue", intraDayUpperData);
		
		chartData.put("responseBody", historicalChartData);
		
		JSONArray listObj = new JSONArray();
		if(!news.isEmpty()) {
			for(List<Object> obj : news) {
				for(Object wtlst : obj) {
					listObj.put(wtlst);
				}
			}
		}
		chartData.put("News", listObj);
		return new ResponseEntity<String>(chartData.toString(), HttpStatus.OK);
		
	}
	
	@PostMapping(value="/wathclist/charts/benchmark")
	@ResponseBody
	public ResponseEntity<String> getBenchmarkTicker(@RequestBody Map<String, Object> responseBody) throws ClassNotFoundException, SQLException{
		JSONObject chartData = new JSONObject();
		String startDate = (String) responseBody.get("start_date");
		String endDate = (String) responseBody.get("end_date");
		List<String> ticker =  (List) responseBody.get("ticker");
		TreeMap<String, TreeMap<String,Double>> getModifiedValue = bizzService.getBenchMark(ticker,startDate+":"+endDate);
		for(Map.Entry<String, TreeMap<String,Double>> tickerValue : getModifiedValue.entrySet()) {
			
			chartData.put(tickerValue.getKey(), tickerValue.getValue());
		}
		
		return new ResponseEntity<String>(chartData.toString(),HttpStatus.OK);
	}
	
	@PostMapping(value="/stockAnalysis/charts")
	public ResponseEntity<String> gettickerChartData(@RequestBody Map<String, Object> responseBody) throws JSONException, ClassNotFoundException, SQLException{
		//System.out.println(LocalTime.now());
		String startDate = (String) responseBody.get("start_date");
		String endDate = (String) responseBody.get("end_date");
		String ticker =  (String) responseBody.get("ticker");
		JSONObject obj = new JSONObject();
		String priceStartDate=BizzService.getDate(ticker);
		
		int comparisonResult = startDate.compareTo(priceStartDate);
		System.out.println("date comparison:   "+comparisonResult);
		if (comparisonResult <0) {
			startDate= CommonUtils.getPreviousDate(priceStartDate);
		}
		obj.put("responseBody", bizzService.getTickerInfoFromDB(ticker, startDate, endDate));
		return new ResponseEntity<String>(obj.toString(), HttpStatus.OK);
	}
	
	@GetMapping(value="/stockAnalysis/charts/compare")
	public ResponseEntity<String> gettickerChartCompareData(@RequestBody Map<String, Object> responseBody) throws JSONException, ClassNotFoundException, SQLException{
		String startDate = (String) responseBody.get("start_date");
		String endDate = (String) responseBody.get("end_date");
		String ticker =  (String) responseBody.get("ticker");
		JSONObject obj = new JSONObject();
		System.out.println(bizzService.getTickerInfoFromDB(ticker, startDate, endDate));
		obj.put("responseBody", bizzService.getTickerInfoFromDB(ticker, startDate, endDate));
		return new ResponseEntity<String>(obj.toString(), HttpStatus.OK);
	}
	
	
	@PostMapping("/stock/analysis")
	@ResponseBody
    public ResponseEntity<String> getStockData(@RequestBody Map<String, Object> responseBody) throws ClassNotFoundException, SQLException {
		String startDate = (String) responseBody.get("start_date");  
        String endDate = (String) responseBody.get("end_date");
		String dateRange = startDate+" - "+endDate;
		String bmTicker = (String) responseBody.get("bmTicker");
		String code =  (String) responseBody.get("ticker");
		String riskFreeRate = (String) responseBody.get("risk_free");
		String tradingDays = (String) responseBody.get("trading_day");
		String priceStartDate=BizzService.getDate(code);
		
		int comparisonResult = startDate.compareTo(priceStartDate);
		if (comparisonResult <0) {	
			dateRange= priceStartDate+" - "+endDate ;
		}
		List<String> indices = Constants.populateIndexList();
		List<String> foreignIndices = Constants.populateForeignIndexList();
		boolean isIndex = indices.contains(code) || indices.contains(bmTicker);
		boolean isForeignIndex = foreignIndices.contains(code) || indices.contains(bmTicker);
		boolean isTickerMF = false;
		boolean isBMTickerMF = indices.stream().anyMatch(in -> in.split(" ")[0].equals(bmTicker));
		
		List<AdjustedPrice> dailyPriceVolumeList = companyService.getPriceAndVolumeData(code,dateRange,isIndex,false,isForeignIndex,
				isTickerMF || isBMTickerMF,isTickerMF);	
		List<AdjustedPrice> dailyPriceVolumeListForTable = companyService.getPriceAndVolumeData(code,dateRange,isIndex,true,isForeignIndex,
				isTickerMF || isBMTickerMF,isTickerMF);	
		Map<String,String> priceVolumeMap = companyService.getModifiedPriceData(dailyPriceVolumeList);	
		List<AdjustedPrice> dailyBMPriceList = companyService.getPriceAndVolumeData(bmTicker,dateRange,isIndex,true,isForeignIndex,isTickerMF || isBMTickerMF,isBMTickerMF);
		Map<String,Double> tableDataMap;
		
		if (riskFreeRate == null && tradingDays == null) {
			tableDataMap = companyService.getTableChartData(dailyPriceVolumeListForTable,dailyBMPriceList,isIndex || isTickerMF ||  isBMTickerMF);
		}
		else {
			tableDataMap = companyService.getTableChartData(dailyPriceVolumeListForTable,dailyBMPriceList,isIndex || isTickerMF ||  isBMTickerMF,riskFreeRate,
					tradingDays);
		}
		
		int dayDiff = CommonUtils.getDayDifferenceee(dateRange);
		JSONObject obj = new JSONObject();
		JSONArray dateArray = new JSONArray();
		JSONArray modifiedPriceArray = new JSONArray();
		JSONArray priceArray = new JSONArray();
		JSONArray volumeArray = new JSONArray();

		List<Double> tableDataArray  = new ArrayList<Double>();
		List<String> dataPointArray = new ArrayList<String>();

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
			dataPointArray.add(entry.getKey());
			if(entry.getValue()==null || Double.isNaN(entry.getValue()) || Double.isInfinite(entry.getValue())){
				tableDataArray.add(99999999.00);
			}else {
				String formattedValue = df.format(entry.getValue());
                Double parsedValue = Double.parseDouble(formattedValue);
                tableDataArray.add(entry.getValue());
                tableDataArray.add(parsedValue);
			}
		}
		for(int i = 0; i<dataPointArray.size(); i++) {
			obj.put(dataPointArray.get(i),tableDataArray.get(i));
		}
		System.out.println("request on /stock/anlysis/"+code);
		return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
    }
	
	@PostMapping(value="/stock/news")
	public ResponseEntity<String> getStockNews(@RequestBody Map<String, Object> responseBody) throws ClassNotFoundException, SQLException, ParseException{
		String startDate = (String) responseBody.get("start_date");
		String endDate = (String) responseBody.get("end_date");
		String ticker = (String) responseBody.get("ticker");
		JSONObject obj = new JSONObject();
		List<Map<String,String>> newsList = bizzService.getStockNews(ticker,startDate,endDate);
		obj.put("articles", newsList);
		obj.put("catId", 0);
		obj.put("maxArticleId", 0);
		obj.put("success", true);
		
		return new ResponseEntity<String>(obj.toString(), HttpStatus.OK);
		
	}
	
	@PostMapping(value="/guest/portfolio/analytics")
	public ResponseEntity<String> getAnalytics(@RequestBody Map<String, Object> responseBody) throws ClassNotFoundException, SQLException{
		String startDate = (String) responseBody.get("start_date");
		String endDate = (String) responseBody.get("end_date");
		String type =  (String) responseBody.get("type");
		String bmTicker= (String) responseBody.get("bmTicker");
		String dateRange = startDate+" - "+endDate;
	    String riskFreeRate = (String) responseBody.get("risk_free");
	    String tradingDays = (String) responseBody.get("trading_day");
	    
	    List<Map<String, Object>> defaultWatchList = (List<Map<String, Object>>) responseBody.get("data");
	    List<String> indices = Constants.populateIndexList();
	    boolean isIndex = false;
	    boolean isForeignIndex = false;
	    boolean isTickerMF = false;
	    boolean isBMTickerMF = indices.stream().anyMatch(in -> in.split(" ")[0].equals(bmTicker));
   
	    TreeMap<String, Double> tickerWeight = new TreeMap<>();
	    List<Object> datePriceVolumeMap = null;
		if(type.equalsIgnoreCase("weight")) {
			for (Map<String, Object> entry : defaultWatchList) {
				
		        String ticker = (String) entry.get("ticker");
		        Integer share = (Integer) entry.get("weight");
		        String sshare = share.toString();
		        Double weight = Double.parseDouble(sshare);
		        tickerWeight.put(ticker, weight);
		        }
			datePriceVolumeMap=WatchListService.getPriceMap(startDate, endDate,tickerWeight);
		}
		else {
			for (Map<String, Object> entry : defaultWatchList) {
				
		        String ticker = (String) entry.get("ticker");
		        Integer share = (Integer) entry.get("share");
		        String sshare = share.toString();
		        Double weight = Double.parseDouble(sshare);
		        tickerWeight.put(ticker, weight);
		        }
			Map<String, Map<String, Double>> wweightedShare = WatchListService.getWeightUsingShare(tickerWeight);
			if(wweightedShare.get("weight") != null) {
				datePriceVolumeMap=WatchListService.getPriceMap(startDate, endDate,wweightedShare.get("weight"));
			}
		}
	    List<AdjustedPrice> dailyBMPriceList = companyService.getPriceAndVolumeData(bmTicker,dateRange,isIndex,true,isForeignIndex,isTickerMF || isBMTickerMF,isBMTickerMF);
	    Map<String,Double> tableDataMap;
	    if (riskFreeRate == null && tradingDays == null) {
	        tableDataMap = AnalyticsService.getTableChartData(datePriceVolumeMap,dailyBMPriceList,isIndex || isTickerMF ||  isBMTickerMF);
	    }
	    else {
	        tableDataMap = AnalyticsService.getTableChartData(datePriceVolumeMap,dailyBMPriceList,isIndex || isTickerMF ||  isBMTickerMF,riskFreeRate,
	                tradingDays);
	    }
		
		JSONObject obj = new JSONObject();


		List<Double> tableDataArray  = new ArrayList<Double>();

		List<String> dataPointArray = new ArrayList<String>();		
		
		for(Map.Entry<String,Double> entry : tableDataMap.entrySet()) {
			dataPointArray.add(entry.getKey());
			if(entry.getValue()==null || Double.isNaN(entry.getValue()) || Double.isInfinite(entry.getValue())){
				tableDataArray.add(99999999.00);
			}else {
				tableDataArray.add(entry.getValue());
			}
		}


		for(int i = 0; i<dataPointArray.size(); i++) {
			obj.put(dataPointArray.get(i),tableDataArray.get(i));
		}
		
		System.out.println("request on /stock/anlysis/"+"Portfolio");
		return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
		
	}
	@PostMapping(value="/user/portfolio/analytics")
	public ResponseEntity<String> getUserAnalytics(@RequestHeader("Authorization") String authorizationToken,@RequestBody Map<String, Object> responseBody) throws ClassNotFoundException, SQLException{

		String startDate = (String) responseBody.get("start_date");
		String endDate = (String) responseBody.get("end_date");
		String type =  (String) responseBody.get("type");
		String bmTicker= (String) responseBody.get("bmTicker");
		String dateRange = startDate+" - "+endDate;
	    String riskFreeRate = (String) responseBody.get("risk_free");
	    String tradingDays = (String) responseBody.get("trading_day");
	    String watchlistId = (String) responseBody.get("watchlistId");
	    List<Map<String, Object>> defaultWatchList = AnalyticsService.getWeightedData(authorizationToken,watchlistId);
	    List<String> indices = Constants.populateIndexList();
	    boolean isIndex = false;
	    boolean isForeignIndex = false;
	    boolean isTickerMF = false;
	    boolean isBMTickerMF = indices.stream().anyMatch(in -> in.split(" ")[0].equals(bmTicker));
   
	    TreeMap<String, Double> tickerWeight = new TreeMap<>();
	    List<Object> datePriceVolumeMap = null;
		if(type.equalsIgnoreCase("weight")) {
			for (Map<String, Object> entry : defaultWatchList) {
				
		        String ticker = (String) entry.get("ticker");
		        Double share = (Double) entry.get("weight");
		        String sshare = share.toString();
		        Double weight = Double.parseDouble(sshare);
		        tickerWeight.put(ticker, weight);
		        }
			datePriceVolumeMap=WatchListService.getPriceMap(startDate, endDate,tickerWeight);
		}
		else {
			for (Map<String, Object> entry : defaultWatchList) {
				
		        String ticker = (String) entry.get("ticker");
		        Integer share = (Integer) entry.get("share");
		        String sshare = share.toString();
		        Double weight = Double.parseDouble(sshare);
		        tickerWeight.put(ticker, weight);
		        }
			Map<String, Map<String, Double>> wweightedShare = WatchListService.getWeightUsingShare(tickerWeight);
			if(wweightedShare.get("weight") != null) {
				datePriceVolumeMap=WatchListService.getPriceMap(startDate, endDate,wweightedShare.get("weight"));
			}
		}
	    
	    List<AdjustedPrice> dailyBMPriceList = companyService.getPriceAndVolumeData(bmTicker,dateRange,isIndex,true,isForeignIndex,isTickerMF || isBMTickerMF,isBMTickerMF);
	    Map<String,Double> tableDataMap;
	    if (riskFreeRate == null && tradingDays == null) {
	        tableDataMap = AnalyticsService.getTableChartData(datePriceVolumeMap,dailyBMPriceList,isIndex || isTickerMF ||  isBMTickerMF);
	    }
	    else {
	        tableDataMap = AnalyticsService.getTableChartData(datePriceVolumeMap,dailyBMPriceList,isIndex || isTickerMF ||  isBMTickerMF,riskFreeRate,
	                tradingDays);
	    }
		JSONObject obj = new JSONObject();

		List<Double> tableDataArray  = new ArrayList<Double>();
		List<String> dataPointArray = new ArrayList<String>();
		
		for(Map.Entry<String,Double> entry : tableDataMap.entrySet()) {
			dataPointArray.add(entry.getKey());
			if(entry.getValue()==null || Double.isNaN(entry.getValue()) || Double.isInfinite(entry.getValue())){
				tableDataArray.add(99999999.00);
			}else {
				tableDataArray.add(entry.getValue());
			}
		}
		for(int i = 0; i<dataPointArray.size(); i++) {
			obj.put(dataPointArray.get(i),tableDataArray.get(i));
		}		
		System.out.println("request on /stock/anlysis/"+"Portfolio");
		return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
		
	}
	
	@PostMapping(value="/user/portfolio/news")
	public ResponseEntity<String> getUserPortfolioNews(@RequestHeader("Authorization") String authorizationToken, @RequestBody Map<String, Object> responseBody) throws ClassNotFoundException, SQLException, ParseException{
		String startDate = (String) responseBody.get("start_date");
		String endDate = (String) responseBody.get("end_date");
		String watchlistId = (String) responseBody.get("watchlist_id");

		JSONObject obj = new JSONObject();
		List<Map<String,String>> newsList =  WatchListService.getUserNews(authorizationToken,watchlistId,startDate,endDate);
		obj.put("articles", newsList);
		obj.put("catId", 0);
		obj.put("maxArticleId", 0);
		obj.put("success", true);
		
		return new ResponseEntity<String>(obj.toString(), HttpStatus.OK);
		
	}
	
	@PostMapping(value="/guest/portfolio/news")
	public ResponseEntity<String> getGuestPortfolioNews(@RequestBody Map<String, Object> responseBody) throws ClassNotFoundException, SQLException, ParseException{
		String startDate = (String) responseBody.get("start_date");
		String endDate = (String) responseBody.get("end_date");
		Set<String> tickers = new HashSet<String>();
		List<Map<String, Object>> defaultWatchList = (List<Map<String, Object>>) responseBody.get("data");
		for(Map<String, Object> guestData :defaultWatchList ) {
			tickers.add(guestData.get("ticker").toString());
		}
		StringBuilder result = new StringBuilder();
        for (String str : tickers) {
            result.append("'").append(str).append("',");
        }
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
		JSONObject obj = new JSONObject();
		List<Map<String,String>> newsList = WatchListService.getGuestTickerForNews(result.toString(),startDate,endDate);
		obj.put("articles", newsList);
		obj.put("catId", 0);
		obj.put("maxArticleId", 0);
		obj.put("success", true);
		
		return new ResponseEntity<String>(obj.toString(), HttpStatus.OK);
		
	}
	
	
	@PostMapping(value="/portfolio/forcast")
	public String getForcating(@RequestHeader("Authorization") String authorizationToken, @RequestBody Map<String, Object> responseBody) throws ClassNotFoundException, SQLException{
		String userId = WatchListService.getUserId(authorizationToken);
		String startDate = (String) responseBody.get("start_date");
		String endDate = (String) responseBody.get("end_date");
		String ticker =  (String) responseBody.get("ticker");
		String days= (String) responseBody.get("days");
		String watchlistId = (String) responseBody.get("watchlist_id");
		
		final long execTimeLimit = 60000; //60 seconds
		Double portfolioCurrentValue = 0.0;

		String rootFolder = EXE_FILE_PATH;
		String fileName="notGenerated";

		if(userId.equals("0")) {
			if(ticker.equals("watchlist")) {
				List<Map<String, Object>> defaultWatchList = (List<Map<String, Object>>) responseBody.get("data");
			    List<Object> datePriceVolumeMap = null;
				TreeMap<String, Double> tickerWeight = new TreeMap<>();
				for (Map<String, Object> entry : defaultWatchList) {
					String tickerrr = (String) entry.get("ticker");
				    Integer share = (Integer) entry.get("weight");
				    String sshare = share.toString();
				    Double weight = Double.parseDouble(sshare);
				    tickerWeight.put(tickerrr, weight);
				    }
				datePriceVolumeMap=WatchListService.getPriceMap(startDate, endDate,tickerWeight);
				fileName = WatchListService.generatePriceMapExcelForWatchlist(datePriceVolumeMap);
			}
		} else {
			Map<String, Double> defaultWatchList = (Map<String, Double>) WatchListService.getUserWightsfromDb(authorizationToken, watchlistId);
			List<Object> datePriceVolumeMap = null;
			TreeMap<String, Double> tickerWeight = new TreeMap<>();
			for (Map.Entry<String,Double> entry : defaultWatchList.entrySet()) {
				String tickerrr = (String) entry.getKey();
				Double share = (Double) entry.getValue();
				String sshare = share.toString();
				Double weight = Double.parseDouble(sshare);
				tickerWeight.put(tickerrr, weight);
				}
			datePriceVolumeMap=WatchListService.getPriceMap(startDate, endDate,tickerWeight);
			fileName = WatchListService.generatePriceMapExcelForWatchlist(datePriceVolumeMap);

		}
		
		
		
		try {
			String response = CommonUtils.runExternalProcess(rootFolder, execTimeLimit,ticker, days, startDate, endDate,fileName);
			System.out.println(response);
			JSONObject jObj = new JSONObject(response);
			if(portfolioCurrentValue != null && portfolioCurrentValue != 0.0) {
				jObj.put("portfolioCurrentValue", portfolioCurrentValue);
			}
			return jObj.toString();
		} catch (Exception ex) {
			logger.error("Error while running Forecast for " + ticker + ":" + days + ":" + startDate +":" + endDate, ex);
			throw new BusinessException(BusinessException.SYSTEM_ERROR);
		}
		
	}
	
	@GetMapping(value = "/personalFinance/data")
	public String getPersonalFinance(@RequestHeader("Authorization") String authorizationToken) throws Exception {
		String userId = WatchListService.getUserId(authorizationToken);
		String payload = WatchListService.getPersonalFinanceData(userId);
		return payload;
	}
	
//	@PostMapping(value = "/personalFinance/service")
//	public ResponseEntity<String> getPersonalFinanceData(@RequestHeader("Authorization") String authorizationToken, @RequestBody Map<String, Object> responseBody) throws ClassNotFoundException, SQLException{
//		String userId = WatchListService.getUserId(authorizationToken);
//
//		if(userId.equals("0")) {
//			JSONObject obj = new JSONObject(WatchListService.getPersonalFinanceService());
//			return new ResponseEntity<String>(obj.toString(), HttpStatus.OK);
//		}else {
//			WatchListService.insertPersonalFinanceInfoDb(userId, responseBody);
//			JSONObject obj = new JSONObject(WatchListService.getPersonalFinanceService());
//			return new ResponseEntity<String>(obj.toString(), HttpStatus.OK);
//		}
//	}
}
