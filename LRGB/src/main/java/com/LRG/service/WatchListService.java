package com.LRG.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.LRG.model.RetirementPlanningDto;

public class WatchListService {
	
	public static HashMap<String, Object> getWeightedHistoricalData(String auth, String watchlistId, String startDate, String endDate) throws ClassNotFoundException, SQLException {
    	Connection con = connectLRGB();
		Map<String, Double> weights = getTickerWeightsFromDB(auth,watchlistId, con);
		HashMap<String, Object> portfolioData = getDailyReturnsDB(weights,startDate+":"+endDate ,con);
		con.close();
		return portfolioData;
    }
	
    public static List<LinkedHashMap<String, String>> getWeightedIntradayData(String auth, String watchlistId) throws ClassNotFoundException, SQLException {
    	Connection con = connectLRGB();
		Map<String, Double> weights = getTickerWeightsFromDB(auth, watchlistId, con);
		List<LinkedHashMap<String, String>> portfolioData = getIntraDayReturnsDB(weights,con);
		con.close();
		return portfolioData;

    }
    
    public static Map<String, Double> getUserWightsfromDb(String auth,String watchlistId) throws ClassNotFoundException, SQLException{
    	Connection con = connectLRGB();
		Map<String, Double> weights = getTickerWeightsFromDB(auth, watchlistId, con);
		return weights;
    }
    
	public static Map<String, Double> getTickerWeightsFromDB(String auth, String watchlistId, Connection conn) throws SQLException {
        Map<String, Double> weights = new HashMap<>();
        String id = getUserId(auth);

        Statement stmt = conn.createStatement();
		String query = "SELECT * FROM dse_analyzer.bizz_watchlist where userID = "+id+" and watchlist_id = "+watchlistId+";";
       
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String ticker = rs.getString("ticker");
            double weight = rs.getDouble("weight")/100;
            weights.put(ticker, weight);
        }
        
        return weights;
    }
	
	public static String getUserId(String auth) {
		String token = null;
		if(auth != null && auth.startsWith("Bearer ")) {
			token = auth.split("-")[1];
			}
		return token;
		
	}
	
	public static HashMap<String, Object> getDailyReturnsDB(Map<String, Double> weights,String dateRange,Connection conn) 
			throws SQLException, ClassNotFoundException {
		LinkedHashMap<String, Object> tickerData = new LinkedHashMap<String, Object>();
        //TreeMap<String, Double> portfolioData = new TreeMap<>();
        Set<String> tickers = weights.keySet();
        Double openChartPrice= 0.0;
        Double lowestClosingPrice=0.0;
        Double highClosingPrice=0.0;
        Double lastColse=0.0;
        Double tot_volume=0.0;
		TreeMap<String, HashMap<String, Double>> dateTickerReturnMap = getDateTickerReturnMap("'"+String.join("','", tickers)+"'",dateRange,  conn);
		TreeMap<String, HashMap<String, Double>> dateTickerVolumeMap = getDateTickerVolumeMap("'"+String.join("','", tickers)+"'",dateRange,  conn);
		
		List<LinkedHashMap<String, String>> dailyPriceMap = new ArrayList<>();
		int count=1;
        for(String thisDate : dateTickerReturnMap.keySet()) {
        	LinkedHashMap<String, String> priceMap = new LinkedHashMap<String, String>();        	
        	double usedWt = 0;
        	List<Double> returns = new ArrayList<Double>();
        	List<Double> volumes = new ArrayList<Double>();
        	HashMap<String, Double> tickerReturnMap = dateTickerReturnMap.get(thisDate);
        	HashMap<String, Double> tickerVolumeMap = dateTickerVolumeMap.get(thisDate);
        	for(String ticker: tickers) {
        		Double thisReturn = tickerReturnMap.get(ticker);
        		Double volume = tickerVolumeMap.get(ticker);
        		if(thisReturn != null && volume != null) {
        			double thisWt = weights.get(ticker);
        			usedWt += thisWt;
        			returns.add(thisReturn * thisWt);
        			volumes.add(volume * thisWt);
        		}
        	}
        	
        	double multiplier = 1 / usedWt;
        	Double totalClose = 0.0;
        	for(double ret: returns) {
        		totalClose += ret * multiplier;
        		lastColse=totalClose;
        	}
        	
        	Double totalVolume = 0.0;
        	for(double ret: volumes) {
        		totalVolume += ret * multiplier;
        	}       	
        	//portfolioData.put(thisDate, totalReturn);
        	priceMap.put("date", thisDate);
        	priceMap.put("close", totalClose.toString());
        	priceMap.put("volume", totalVolume.toString());
        	dailyPriceMap.add(priceMap);
        	tot_volume+=totalVolume;
        	highClosingPrice = totalClose> highClosingPrice ? totalClose :highClosingPrice ;
        	lowestClosingPrice = totalClose < lowestClosingPrice ? totalClose :lowestClosingPrice ;
        	
        	if(count==1) {
    			openChartPrice=totalClose;
    			lowestClosingPrice=totalClose;
    			highClosingPrice=totalClose;
    			count++;
    		}
    	}

        int mapSize=dailyPriceMap.size();
       // System.out.println(dailyPriceMap.get(mapSize-2).get("close"));
        
        
        LinkedHashMap<String, String> upperMapHistorical = new LinkedHashMap<String, String>();
    	upperMapHistorical.put("openPrice", openChartPrice.toString());
    	upperMapHistorical.put("highPrice", highClosingPrice.toString());
    	upperMapHistorical.put("lowPrice", lowestClosingPrice.toString());
    	upperMapHistorical.put("lastClosePrice",lastColse.toString());
    	upperMapHistorical.put("previousClosePrice", dailyPriceMap.get(mapSize-2).get("close")); //previousClose
    	upperMapHistorical.put("totalVolume", tot_volume.toString()); 
        
    	tickerData.put("historicalUpperValue",upperMapHistorical );
        tickerData.put("historical", dailyPriceMap);
        
        
        
        
       // List<LinkedHashMap<String, String>> IntradayPriceMap=getGuestWeightedIntradayData(weights);
        
		return tickerData;
    }
	
	public static List<Double> price(TreeMap<String, Double> portfolioData ){
		List<Double> tickers = new ArrayList<>();
		for(Map.Entry<String,Double> data: portfolioData.entrySet()) {
			tickers.add(data.getValue());
		}
		return tickers;
	}
	
	public static TreeMap<String, HashMap<String, Double>> getDateTickerReturnMap(String tickerCSV, String dateRange, Connection conn) throws SQLException {
        TreeMap<String, HashMap<String, Double>> dateTickerReturnMap = new TreeMap<>();
        String startDate = dateRange.split(":")[0];
        String endDate = dateRange.split(":")[1];
        Statement stmt = conn.createStatement();
        String query = "SELECT date, ticker, adjusted_price FROM dse_analyzer.daily_price where ticker in (" + tickerCSV + ") "
                + "and date between '"+startDate+"' and '"+endDate+"' order by date, ticker";
        ResultSet rs = stmt.executeQuery(query);
        String date = "";
        HashMap<String, Double> tickerReturn = new HashMap<>();
        while (rs.next()) {
            String returnDate = rs.getString("date");
            String ticker = rs.getString("ticker");
            double returnVal = rs.getDouble("adjusted_price");
            if (!returnDate.equals(date)) {
                tickerReturn = new HashMap<>();
                dateTickerReturnMap.put(returnDate, tickerReturn);
                date = returnDate;
            }
            tickerReturn.put(ticker, returnVal);
        }
        return dateTickerReturnMap;
    }
	
	public static TreeMap<String, HashMap<String, Double>> getDateTickerVolumeMap(String tickerCSV, String dateRange, Connection conn) throws SQLException {
        TreeMap<String, HashMap<String, Double>> dateTickerReturnMap = new TreeMap<>();
        String startDate = dateRange.split(":")[0];
        String endDate = dateRange.split(":")[1];
        Statement stmt = conn.createStatement();
        String query = "SELECT date, ticker, volume FROM dse_analyzer.daily_price where ticker in (" + tickerCSV + ") "
                + "and date between '"+startDate+"' and '"+endDate+"' order by date, ticker";
        ResultSet rs = stmt.executeQuery(query);
        String date = "";
        HashMap<String, Double> tickerReturn = new HashMap<>();
        while (rs.next()) {
            String returnDate = rs.getString("date");
            String ticker = rs.getString("ticker");
            double returnVal = rs.getDouble("volume");
            if (!returnDate.equals(date)) {
                tickerReturn = new HashMap<>();
                dateTickerReturnMap.put(returnDate, tickerReturn);
                date = returnDate;
            }
            tickerReturn.put(ticker, returnVal);
        }
        return dateTickerReturnMap;
    }
	
	
	public static Map<String, Map<String, Double>> getWeightUsingShare(TreeMap<String, Double> defaultWatchList) throws SQLException, ClassNotFoundException{
    	Connection con = connectLRGB();
    	Statement stmt = con.createStatement();
    	ResultSet rs = null;
 
    	Map<String, Map<String, Double>>  returnData = new TreeMap<>();
		TreeMap<String, Double> tickerData = new TreeMap<>();
		TreeMap<String, String> tickerShare = new TreeMap<>();
		for (Map.Entry<String, Double> entry : defaultWatchList.entrySet()) {
			
	        String ticker = (String) entry.getKey();
//	        String weightNumber = (String) entry.get("weight");
//	        String weight = weightNumber.toString();
	        Double share = entry.getValue();
	        String sharess = share.toString();
	        tickerShare.put(ticker, sharess);
	        String querysss = "SELECT LTP, YCP FROM dse_analyzer.market_stat where code = '"+ticker+"';";
	        rs = stmt.executeQuery(querysss);
	        while(rs.next()) {
	        	Double ltp = rs.getDouble("LTP");
	        	Double ycp = rs.getDouble("ycp");
	        	Double mcap = 0.0;
	        	if(ltp == 0.0) {
	        		mcap = ycp * Double.parseDouble(sharess);
	        	}
	        	else {
	        		mcap = ltp * Double.parseDouble(sharess);
	        	}
	        	tickerData.put(ticker, mcap);
	        }
//	        String insertQuery = "insert ignore into dse_analyzer.bizz_watchlist(userID,ticker,weight,time)"
//	        		+ "values("+id+", '"+ticker+"', "+weightNumber+", NOW());";
//	        stmt.execute(insertQuery);      
	    }
		
		Set<String> tickers = tickerData.keySet();
		List<Double> mcap = price(tickerData);
		
		Double sumMcap = 0.0;
		for(Double value : mcap) {
			sumMcap += value;
		}
		
		TreeMap<String, Double> weightedMap = new TreeMap<String, Double>();
		for(Map.Entry<String,Double> map :tickerData.entrySet()) {
			weightedMap.put(map.getKey(), (map.getValue()/sumMcap)*100);
		}
		
		List<Double> weights = price(weightedMap);
		
		Double sumWeights = 0.0;
		for(Double value : weights) {
			sumWeights += value;
		}
		
		returnData.put("weight", weightedMap);
		return returnData;
    }
	
	
	
	public static HashMap<String, Object> getGuestWeightedHistoricalData(String startDate, String endDate, Map<String, Double> list) throws ClassNotFoundException, SQLException {
    	Connection con = connectLRGB();
		HashMap<String, Object> portfolioData = getDailyReturnsDB(list,startDate+":"+endDate ,con);
		return portfolioData;

    }
	
	public static List<LinkedHashMap<String, String>> getGuestWeightedIntradayData(Map<String, Double> list) throws ClassNotFoundException, SQLException {
    	Connection con = connectLRGB();
    	List<LinkedHashMap<String, String>> portfolioData = getIntraDayReturnsDB(list,con);
		return portfolioData;
    }
	
	public static LinkedHashMap<String, String> getGuestWeightedIntradayUpperData(Map<String, Double> list) throws ClassNotFoundException, SQLException {
    	Connection con = connectLRGB();
    	LinkedHashMap<String, String> portfolioData = getIntraDayUpperReturnsDB(list,con);
		return portfolioData;
    }
	
	public static List<LinkedHashMap<String, String>> getUserWeightedIntradayData(String auth, String watchlistId) throws ClassNotFoundException, SQLException {
    	Connection con = connectLRGB();
    	Map<String, Double> weights = getTickerWeightsFromDB(auth, watchlistId, con);
    	List<LinkedHashMap<String, String>> portfolioData = getIntraDayReturnsDB(weights,con);
		return portfolioData;
    }
	
	public static LinkedHashMap<String, String> getUserWeightedIntradayUpperData(String auth, String watchlistId) throws ClassNotFoundException, SQLException {
    	Connection con = connectLRGB();
    	Map<String, Double> weights = getTickerWeightsFromDB(auth, watchlistId, con);
    	LinkedHashMap<String, String> portfolioData = getIntraDayUpperReturnsDB(weights,con);
		return portfolioData;
    }
	
	public static List<LinkedHashMap<String, String>> getIntraDayReturnsDB(Map<String, Double> weights, Connection conn) throws SQLException {
		
		 LinkedHashMap<String, Object> tickerData = new LinkedHashMap<String, Object>();
        TreeMap<String, Double> portfolioData = new TreeMap<>();
        Set<String> tickers = weights.keySet();
		TreeMap<String, HashMap<String, Double>> dateTickerReturnMap = getIntradayDateTickerReturnMap("'"+String.join("','", tickers)+"'",  conn);
		List<LinkedHashMap<String, String>> dailyPriceMap = new ArrayList<>();
       
        for(String thisDate : dateTickerReturnMap.keySet()) {
        	LinkedHashMap<String, String> priceMap = new LinkedHashMap<String, String>();
        	double usedWt = 0;
        	List<Double> returns = new ArrayList<Double>();
        	HashMap<String, Double> tickerReturnMap = dateTickerReturnMap.get(thisDate);
        	for(String ticker: tickers) {
        		Double thisReturn = tickerReturnMap.get(ticker);
        		if(thisReturn != null) {
        			double thisWt = weights.get(ticker);
        			usedWt += thisWt;
        			returns.add(thisReturn * thisWt);
        		}
        	}
        	
        	double multiplier = 1 / usedWt;
        	Double totalClose = 0.0;
        	for(double ret: returns) {
        		totalClose += ret * multiplier;
        	}
        	//portfolioData.put(thisDate, totalReturn);
        	priceMap.put("date", thisDate);
        	priceMap.put("close", totalClose.toString());
        	dailyPriceMap.add(priceMap);
    	}
//        List<Double> priceList =price( portfolioData);
//        List<String> dateList = new ArrayList<>(portfolioData.keySet());
//        
//        TreeMap<String,Double> priceMap = new TreeMap<String,Double>();
//		int i = 0;
////		Double firstRetVal = null;
//		Double initialVal = 100.0;
//		Double firstPriceVal = null;
//		DecimalFormat df = new DecimalFormat("#.##"); 
//		if(priceList.size()>0) {
//			int count = 0;
//			for(Double dailyPriceReturn : priceList) {
//				if(dailyPriceReturn!=null) {
//
//					Double currPriceVal = dailyPriceReturn;
//					if(i==0) {
//						firstPriceVal = dailyPriceReturn;
//						priceMap.put(dateList.get(count),initialVal);
//						i++;
//						count++;
//						continue;
//					}
//
//					initialVal = initialVal+currPriceVal;
//					if(initialVal!=null) {
//						initialVal = Double.valueOf(df.format(initialVal));
//						priceMap.put(dateList.get(count),initialVal);
//					}
//				}
//				count++;
//			}
//		}

		//return portfolioData;
		//return priceMap;
        
//        LinkedHashMap<String, String> upperMapHistorical = new LinkedHashMap<String, String>();
//        upperMapHistorical.put("openPrice", openChartPrice.toString());
//        upperMapHistorical.put("highPrice", highClosingPrice.toString());
//        upperMapHistorical.put("lowPrice", lowestClosingPrice.toString());
//        upperMapHistorical.put("lastClosePrice",lastColse.toString());
//        upperMapHistorical.put("previousClosePrice", dailyPriceMap.get(mapSize-2).get("close")); //previousClose
//        upperMapHistorical.put("totalVolume", tot_volume.toString()); 
//        
//        tickerData.put("IntradayUpperValue",upperMapHistorical );
        
//        
//        tickerData.put("intraDay", dailyPriceMap);
//        return tickerData;
        
        
        return dailyPriceMap;
		
    }
	
	
	public static LinkedHashMap<String, String> getIntraDayUpperReturnsDB(Map<String, Double> weights, Connection conn) throws SQLException {
		
        Double openChartPrice= 0.0;
        Double lowestClosingPrice=0.0;
        Double highClosingPrice=0.0;
        Double lastColse=0.0;
        Double tot_volume=0.0;
		
		LinkedHashMap<String, Object> tickerData = new LinkedHashMap<String, Object>();
       TreeMap<String, Double> portfolioData = new TreeMap<>();
       Set<String> tickers = weights.keySet();
		TreeMap<String, HashMap<String, Double>> dateTickerReturnMap = getIntradayDateTickerReturnMap("'"+String.join("','", tickers)+"'",  conn);
		List<LinkedHashMap<String, String>> dailyPriceMap = new ArrayList<>();
      int count=1;
       for(String thisDate : dateTickerReturnMap.keySet()) {
       	LinkedHashMap<String, String> priceMap = new LinkedHashMap<String, String>();
       	double usedWt = 0;
       	List<Double> returns = new ArrayList<Double>();
       	HashMap<String, Double> tickerReturnMap = dateTickerReturnMap.get(thisDate);
       	for(String ticker: tickers) {
       		Double thisReturn = tickerReturnMap.get(ticker);
       		if(thisReturn != null) {
       			double thisWt = weights.get(ticker);
       			usedWt += thisWt;
       			returns.add(thisReturn * thisWt);
       		}
       	}
       	
       	double multiplier = 1 / usedWt;
       	Double totalClose = 0.0;
       	for(double ret: returns) {
       		totalClose += ret * multiplier;
       		lastColse=totalClose;
       	}
       	
       	Double totalVolume = 0.0;
//    	for(double ret: volumes) {
//    		totalVolume += ret * multiplier;
//    	}
       	//portfolioData.put(thisDate, totalReturn);
       	priceMap.put("date", thisDate);
       	priceMap.put("close", totalClose.toString());
       	dailyPriceMap.add(priceMap);
       	
       	tot_volume+=totalVolume;
    	highClosingPrice = totalClose> highClosingPrice ? totalClose :highClosingPrice ;
    	lowestClosingPrice = totalClose < lowestClosingPrice ? totalClose :lowestClosingPrice ;
       	
       	if(count==1) {
			openChartPrice=totalClose;
			lowestClosingPrice=totalClose;
			highClosingPrice=totalClose;
			count++;
		}
   	}

       int mapSize=dailyPriceMap.size();
       LinkedHashMap<String, String> upperMapIntraday = new LinkedHashMap<String, String>();
       upperMapIntraday.put("openPrice", openChartPrice.toString());
       upperMapIntraday.put("highPrice", highClosingPrice.toString());
       upperMapIntraday.put("lowPrice", lowestClosingPrice.toString());
       upperMapIntraday.put("lastClosePrice",lastColse.toString());
       upperMapIntraday.put("previousClosePrice", dailyPriceMap.get(mapSize-2).get("close")); //previousClose
       upperMapIntraday.put("totalVolume", tot_volume.toString()); 
       
      // tickerData.put("IntradayUpperValue",upperMapHistorical );
             
       return upperMapIntraday;
		
   }
	
	public static TreeMap<String, HashMap<String, Double>> getIntradayDateTickerReturnMap(String tickerCSV, Connection conn) throws SQLException {
        TreeMap<String, HashMap<String, Double>> dateTickerReturnMap = new TreeMap<>();
        Statement stmt = conn.createStatement();
        String query = "SELECT time, ticker, ltp, ycp,  return_price FROM dse_analyzer.intra_day_return where ticker in (" + tickerCSV + ")  order by time, ticker ;";
        ResultSet rs = stmt.executeQuery(query);
        String date = "";
        HashMap<String, Double> tickerReturn = new HashMap<>();
        while (rs.next()) {
            String returnDate = rs.getString("time");
            String ticker = rs.getString("ticker");
            double returnVal = rs.getDouble("ltp") == 0.0 ? rs.getDouble("ycp") : rs.getDouble("ltp");
            if (!returnDate.equals(date)) {
                tickerReturn = new HashMap<>();
                dateTickerReturnMap.put(returnDate, tickerReturn);
                date = returnDate;
            }


            tickerReturn.put(ticker, returnVal);
        }
        return dateTickerReturnMap;
    }
	
	public static List<Object> getPriceMap(String startDate, String endDate, Map<String, Double> list) throws ClassNotFoundException, SQLException {
		
		Connection con = connectLRGB();
		List<Object> portfolioData = getDailyPrice(list,startDate+":"+endDate ,con);
		return portfolioData;		
	}
	
	public static List<Object> getDailyPrice(Map<String, Double> weights,String dateRange,Connection conn) 
			throws SQLException, ClassNotFoundException {
		//List<Object> tickerData = new ArrayList();
        //TreeMap<String, Double> portfolioData = new TreeMap<>();
        Set<String> tickers = weights.keySet();
		TreeMap<String, HashMap<String, Double>> dateTickerReturnMap = getDateTickerReturnMap("'"+String.join("','", tickers)+"'",dateRange,  conn);
		TreeMap<String, HashMap<String, Double>> dateTickerVolumeMap = getDateTickerVolumeMap("'"+String.join("','", tickers)+"'",dateRange,  conn);
		
		List<Object> dailyPriceMap = new ArrayList<>();
        for(String thisDate : dateTickerReturnMap.keySet()) {
        	LinkedHashMap<String, String> priceMap = new LinkedHashMap<String, String>();        	
        	double usedWt = 0;
        	List<Double> returns = new ArrayList<Double>();
        	List<Double> volumes = new ArrayList<Double>();
        	HashMap<String, Double> tickerReturnMap = dateTickerReturnMap.get(thisDate);
        	HashMap<String, Double> tickerVolumeMap = dateTickerVolumeMap.get(thisDate);
        	for(String ticker: tickers) {
        		Double thisReturn = tickerReturnMap.get(ticker);
        		Double volume = tickerVolumeMap.get(ticker);
        		if(thisReturn != null && volume != null) {
        			double thisWt = weights.get(ticker);
        			usedWt += thisWt;
        			returns.add(thisReturn * thisWt);
        			volumes.add(volume * thisWt);
        		}
        	}
        	
        	double multiplier = 1 / usedWt;
        	Double totalClose = 0.0;
        	for(double ret: returns) {
        		totalClose += ret * multiplier;
        	}
        	
        	Double totalVolume = 0.0;
        	for(double ret: volumes) {
        		totalVolume += ret * multiplier;
        	}       	
        	//portfolioData.put(thisDate, totalReturn);
        	priceMap.put("date", thisDate);
        	priceMap.put("close", totalClose.toString());
        	priceMap.put("volume", totalVolume.toString());
        	dailyPriceMap.add(priceMap);
    	}         
       // tickerData.add( dailyPriceMap);       
       // List<LinkedHashMap<String, String>> IntradayPriceMap=getGuestWeightedIntradayData(weights);
        
		return dailyPriceMap;
    }
	
	
    public static List<Map<String, String>> getStockNews(String ticker, String startDate, String endDate) throws ClassNotFoundException, SQLException, ParseException{
    	List<Map<String,String>> newsList = new ArrayList<Map<String,String>>();
    	Connection con = connectLRGBLoader();
    	Statement stmt = con.createStatement();
    	String newsQuery = "SELECT * FROM dse_analyzer_loader.MAN_archive WHERE ticker in ( "+ticker+" ) AND post_date_time BETWEEN '"+startDate+"' AND '"+endDate+"'"
    			+ "ORDER BY post_date_time DESC;";
    	ResultSet rs = stmt.executeQuery(newsQuery);
    	while(rs.next()) {
    		Map<String, String> newMap = new HashMap<String, String>();
    		String tickerName = rs.getString("ticker");
    		String dateString = rs.getString("post_date_time");
    		String news = rs.getString("news");
    		
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormat.parse(dateString);
            Double unixTime = date.getTime() / 1000.00;
            
            newMap.put("ticker", tickerName);
            newMap.put("title", news);
            newMap.put("pubDate", unixTime.toString());
            newsList.add(newMap);
    	}
    	con.close();
    	return newsList;
    }
    
	public static List<Map<String, String>> getGuestTickerForNews(String ticker, String startDate, String endDate) throws ClassNotFoundException, SQLException, ParseException {
    	List<Map<String, String>> news = getStockNews(ticker,startDate, endDate);
		return news;
    }
	
	public static List<Map<String, String>> getUserNews(String auth, String watchlistId, String startDate, String endDate) throws ClassNotFoundException, SQLException, ParseException {
    	Connection con = connectLRGB();
    	String tickerCSV = getTickerFromDB(auth, watchlistId, con);
    	List<Map<String, String>> stockNews = getStockNews(tickerCSV,startDate, endDate);
		return stockNews;
    }
	
	public static String  getTickerFromDB(String auth, String watchlistId, Connection con) throws SQLException {
		String id = getUserId(auth);
		String tickerCSV = "";
		String query = "SELECT GROUP_CONCAT(CONCAT(\"'\", ticker, \"'\") ORDER BY ticker ASC SEPARATOR ',') as tickersCSV FROM dse_analyzer.bizz_watchlist WHERE userID = "+id+" and watchlist_id = "+watchlistId+";";
		ResultSet rs = ((Statement) con.createStatement()).executeQuery(query);
		while(rs.next()) {
			tickerCSV = rs.getString("tickersCSV");
		}
		return tickerCSV;
	}
    
	public static Connection connectLRGB() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://192.168.100.12:3306/dse_analyzer?rewriteBatchedStatements=true", "lrgb_loader",
		"developer4");
		con.setAutoCommit(true);
		return con;
	}
	
	public static Connection connectLRGBLoader() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://192.168.100.12:3306/dse_analyzer_loader?rewriteBatchedStatements=true", "lrgb_loader",
		"developer4");
		con.setAutoCommit(true);
		return con;
	}
	
//	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
//		System.out.println(getUserNews("Bearer 123131-36-25", "2023-01-01", "2024-01-01"));
//	}
	
    public static String  generatePriceMapExcelForWatchlist(List<Object> dataList) {
    	
    	String directory = "/opt/python/lrg/guestPortfolioExcel/";// prod
//    	String directory = "D:/excel/";
    	Random random = new Random();
    	int randomNumber = random.nextInt();
        String filename = "priceList"+randomNumber+".csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(directory + filename))) {            
            writer.println("Date,Price");

            // Write data rows
            for (Object data : dataList) {
                LinkedHashMap<String, String> dailyPriceMap = (LinkedHashMap<String, String>) data;
                String date = dailyPriceMap.get("date");
                String price = dailyPriceMap.get("close");
                if(price.equals("0.0")) {
                	continue;
                }else {
                	writer.println(date + "," + price);
                }
                
            }
//            System.out.println("CSV file created successfully.");
        } catch (IOException e) {
            System.err.println("Error creating CSV file: " + e.getMessage());
        }
		return filename;
    }
    public static String getUserPersonalFinanceDataFromDb(String id) throws Exception {
    	JSONObject jArr = new JSONObject();
    	String query = "select * from dse_analyzer.bizz_personal_finance where user_id = "+id+";";
    	ResultSet rs = ((Statement) connectLRGB().createStatement()).executeQuery(query);
    	while (rs.next()) {
    		jArr.put("contributionWithdrawalType", rs.getString("contribution_withdrawal_type"))
    		.put("gender", rs.getString("gender"))
    	    .put("inflationrate", rs.getString("inflationrate"))
    	    .put("investorAge", rs.getString("investor_age"))
    	    .put("retirementAge", rs.getString("retirement_age"))
    	    .put("longTerm", rs.getString("long_term"))
    	    .put("initialAssets", rs.getString("initial_assets"))
    	    .put("mediumTerm", rs.getString("medium_term"))
    	    .put("spouse", rs.getString("spouse"))
    	    .put("shortTerm", rs.getString("short_term"));
    		}
    	    closeDBStmtRS(rs);
    	    
    	    if (jArr.isEmpty()) {
    	        return "{\r\n"
    	        		+ "    \"retirementAge\": \"50\",\r\n"
    	        		+ "    \"longTerm\": \"-150000\",\r\n"
    	        		+ "    \"gender\": \"MALE\",\r\n"
    	        		+ "    \"inflationrate\": \"1\",\r\n"
    	        		+ "    \"initialAssets\": \"25000\",\r\n"
    	        		+ "    \"mediumTerm\": \"12000\",\r\n"
    	        		+ "    \"shortTerm\": \"20000\",\r\n"
    	        		+ "    \"investorAge\": \"25\",\r\n"
    	        		+ "    \"contributionWithdrawalType\": \"ANNUAL\",\r\n"
    	        		+ "    \"spouse\": \"NONE\"\r\n"
    	        		+ "}";
    	    } else {
    	        return jArr.toString();
    	 }
    }
    
    public static String getPersonalFinanceData(String id) throws Exception {
    	String iniPayload =""; 
    	if(id.equals("0")) {
    		iniPayload = "{\r\n"
    				+ "    \"retirementAge\": \"50\",\r\n"
    				+ "    \"longTerm\": \"-150000\",\r\n"
    				+ "    \"gender\": \"MALE\",\r\n"
    				+ "    \"inflationrate\": \"1\",\r\n"
    				+ "    \"initialAssets\": \"25000\",\r\n"
    				+ "    \"mediumTerm\": \"12000\",\r\n"
    				+ "    \"shortTerm\": \"20000\",\r\n"
    				+ "    \"investorAge\": \"25\",\r\n"
    				+ "    \"contributionWithdrawalType\": \"ANNUAL\",\r\n"
    				+ "    \"spouse\": \"NONE\"\r\n"
    				+ "}";
    	}else {
    		iniPayload = getUserPersonalFinanceDataFromDb(id);
    	}
		return iniPayload;
    }
    
    public static void insertPersonalFinanceInfoDb(String userId, RetirementPlanningDto responseBody) throws ClassNotFoundException, SQLException {
		String contributionWithdrawalType = (String) responseBody.getContributionWithdrawalType();
		String gender = (String) responseBody.getGender();
		String inflationrate = (String) responseBody.getInflationrate().toString();
		String investorAge =  (String) responseBody.getInvestorAge().toString();
		String retirementAge= (String) responseBody.getRetirementAge().toString();
		String longTerm = (String) responseBody.getLongTerm().toString();
		String initialAssets = (String) responseBody.getInitialAssets().toString();
		String mediumTerm =  (String) responseBody.getMediumTerm().toString();
		String spouse= (String) responseBody.getSpouse();
		String shortTerm= (String) responseBody.getShortTerm().toString();
		
		String deleteQuery = "delete from dse_analyzer.bizz_personal_finance where user_id = "+userId+";";
		((Statement) connectLRGB().createStatement()).execute(deleteQuery);
		
		String insertQuery = "insert into dse_analyzer.bizz_personal_finance(user_id, contribution_withdrawal_type, gender, inflationrate, investor_age, retirement_age, long_term, initial_assets, medium_term, spouse, short_term)\r\n"
				+ "values("+userId+", '"+contributionWithdrawalType+"', '"+gender+"', "+inflationrate+", "+investorAge+", "+retirementAge+", "+longTerm+", "+initialAssets+", "
						+ ""+mediumTerm+", '"+spouse+"', "+shortTerm+");";
		((Statement) connectLRGB().createStatement()).execute(insertQuery);
    }
    
	private static void closeDBStmtRS(ResultSet rs) throws Exception {
		Statement stmt = rs.getStatement();
		if(rs != null && ! rs.isClosed()) {
			rs.close();
			rs = null;
		}
		if(stmt != null && ! stmt.isClosed()) {
			stmt.close();
			stmt = null;
		}
	}
    
    public static String getPersonalFinanceService() {
    	String value = "{\r\n"
    			+ "  \"portfolioFixedWtOutput\": {\r\n"
    			+ "    \"XLU\": \"0E-8\",\r\n"
    			+ "    \"AGG\": \"0.33377310\",\r\n"
    			+ "    \"EFA\": \"0E-8\",\r\n"
    			+ "    \"FLRN\": \"0E-8\",\r\n"
    			+ "    \"XLV\": \"0.16118305\",\r\n"
    			+ "    \"EEM\": \"0E-8\",\r\n"
    			+ "    \"QQQ\": \"0.30681409\",\r\n"
    			+ "    \"PFF\": \"0E-8\",\r\n"
    			+ "    \"XLF\": \"0E-8\",\r\n"
    			+ "    \"TFI\": \"0.16518805\",\r\n"
    			+ "    \"XLK\": \"0.03304171\",\r\n"
    			+ "    \"VNQ\": \"0E-8\",\r\n"
    			+ "    \"SPY\": \"0E-8\",\r\n"
    			+ "    \"IWM\": \"0E-8\"\r\n"
    			+ "  },\r\n"
    			+ "  \"tickerDtoList\": [\r\n"
    			+ "    {\r\n"
    			+ "      \"symbol\": \"MoneyMktFunds\",\r\n"
    			+ "      \"minWt\": 0,\r\n"
    			+ "      \"maxWt\": 0\r\n"
    			+ "    },\r\n"
    			+ "    {\r\n"
    			+ "      \"symbol\": \"AGG\",\r\n"
    			+ "      \"name\": \"iShares Core U.S. Aggregate Bond\",\r\n"
    			+ "      \"minWt\": 0,\r\n"
    			+ "      \"maxWt\": 0.5\r\n"
    			+ "    },\r\n"
    			+ "    {\r\n"
    			+ "      \"symbol\": \"EEM\",\r\n"
    			+ "      \"name\": \"iShares MSCI Emerging Index Fund\",\r\n"
    			+ "      \"minWt\": 0,\r\n"
    			+ "      \"maxWt\": 0.5\r\n"
    			+ "    },\r\n"
    			+ "    {\r\n"
    			+ "      \"symbol\": \"EFA\",\r\n"
    			+ "      \"name\": \"iShares MSCI EAFE\",\r\n"
    			+ "      \"minWt\": 0,\r\n"
    			+ "      \"maxWt\": 0.5\r\n"
    			+ "    },\r\n"
    			+ "    {\r\n"
    			+ "      \"symbol\": \"FLRN\",\r\n"
    			+ "      \"name\": \"SPDR Bloomberg Barclays Investment Grade Floating Rate\",\r\n"
    			+ "      \"minWt\": 0,\r\n"
    			+ "      \"maxWt\": 0.5\r\n"
    			+ "    },\r\n"
    			+ "    {\r\n"
    			+ "      \"symbol\": \"IWM\",\r\n"
    			+ "      \"name\": \"iShares Russell 2000\",\r\n"
    			+ "      \"minWt\": 0,\r\n"
    			+ "      \"maxWt\": 0.5\r\n"
    			+ "    },\r\n"
    			+ "    {\r\n"
    			+ "      \"symbol\": \"PFF\",\r\n"
    			+ "      \"name\": \"iShares U.S. Preferred Stock ETF\",\r\n"
    			+ "      \"minWt\": 0,\r\n"
    			+ "      \"maxWt\": 0.5\r\n"
    			+ "    },\r\n"
    			+ "    {\r\n"
    			+ "      \"symbol\": \"QQQ\",\r\n"
    			+ "      \"name\": \"PowerShares QQQ Trust Series 1\",\r\n"
    			+ "      \"minWt\": 0,\r\n"
    			+ "      \"maxWt\": 0.5\r\n"
    			+ "    },\r\n"
    			+ "    {\r\n"
    			+ "      \"symbol\": \"SPY\",\r\n"
    			+ "      \"name\": \"SPDR S&P 500\",\r\n"
    			+ "      \"minWt\": 0,\r\n"
    			+ "      \"maxWt\": 0.5\r\n"
    			+ "    },\r\n"
    			+ "    {\r\n"
    			+ "      \"symbol\": \"TFI\",\r\n"
    			+ "      \"name\": \"SPDR Nuveen Bloomberg Barclays Municipal Bond\",\r\n"
    			+ "      \"minWt\": 0,\r\n"
    			+ "      \"maxWt\": 0.5\r\n"
    			+ "    },\r\n"
    			+ "    {\r\n"
    			+ "      \"symbol\": \"VNQ\",\r\n"
    			+ "      \"name\": \"Vanguard Real Estate\",\r\n"
    			+ "      \"minWt\": 0,\r\n"
    			+ "      \"maxWt\": 0.5\r\n"
    			+ "    },\r\n"
    			+ "    {\r\n"
    			+ "      \"symbol\": \"XLF\",\r\n"
    			+ "      \"name\": \"SPDR Select Sector Fund - Financial\",\r\n"
    			+ "      \"minWt\": 0,\r\n"
    			+ "      \"maxWt\": 0.5\r\n"
    			+ "    },\r\n"
    			+ "    {\r\n"
    			+ "      \"symbol\": \"XLK\",\r\n"
    			+ "      \"name\": \"SPDR Select Sector Fund - Technology\",\r\n"
    			+ "      \"minWt\": 0,\r\n"
    			+ "      \"maxWt\": 0.5\r\n"
    			+ "    },\r\n"
    			+ "    {\r\n"
    			+ "      \"symbol\": \"XLU\",\r\n"
    			+ "      \"name\": \"SPDR Select Sector Fund - Utilities\",\r\n"
    			+ "      \"minWt\": 0,\r\n"
    			+ "      \"maxWt\": 0.5\r\n"
    			+ "    },\r\n"
    			+ "    {\r\n"
    			+ "      \"symbol\": \"XLV\",\r\n"
    			+ "      \"name\": \"SPDR Select Sector Fund - Health Care\",\r\n"
    			+ "      \"minWt\": 0,\r\n"
    			+ "      \"maxWt\": 0.5\r\n"
    			+ "    }\r\n"
    			+ "  ],\r\n"
    			+ "  \"expectedShortfall\": 0,\r\n"
    			+ "  \"shortfallProbability\": 0,\r\n"
    			+ "  \"targetReturn\": 0.0799999,\r\n"
    			+ "  \"targetVolatility\": 0.10248,\r\n"
    			+ "  \"avgAssetValue\": 5610576.8151648315,\r\n"
    			+ "  \"lifeExpectancy\": \"52.97\",\r\n"
    			+ "  \"optimizationCount\": 0,\r\n"
    			+ "  \"assetValuePerSimulationList\": [\r\n"
    			+ "    [\r\n"
    			+ "      25000,\r\n"
    			+ "      39776.32639567531,\r\n"
    			+ "      55563.95386041621,\r\n"
    			+ "      72721.5866704301,\r\n"
    			+ "      91228.46322820804,\r\n"
    			+ "      111202.57811138096,\r\n"
    			+ "      132903.5859989329,\r\n"
    			+ "      156380.72531313187,\r\n"
    			+ "      181793.44001349495,\r\n"
    			+ "      209469.57818575477,\r\n"
    			+ "      239005.3351365859,\r\n"
    			+ "      271539.3494651235,\r\n"
    			+ "      306383.3736000822,\r\n"
    			+ "      344327.2872505578,\r\n"
    			+ "      385416.0752287586,\r\n"
    			+ "      430159.8410096521,\r\n"
    			+ "      477932.8747737715,\r\n"
    			+ "      529925.8935723712,\r\n"
    			+ "      585483.0435091811,\r\n"
    			+ "      644985.7512528719,\r\n"
    			+ "      710302.5584008861,\r\n"
    			+ "      780773.4658377348,\r\n"
    			+ "      856542.6807917681,\r\n"
    			+ "      939303.2845448412,\r\n"
    			+ "      1028002.2797339547,\r\n"
    			+ "      1124114.5990514266,\r\n"
    			+ "      1226552.8293034942,\r\n"
    			+ "      1338010.2312394052,\r\n"
    			+ "      1460949.8503965377,\r\n"
    			+ "      1591815.1668832272,\r\n"
    			+ "      1735899.5315753,\r\n"
    			+ "      1887195.700431647,\r\n"
    			+ "      2054999.0726207767,\r\n"
    			+ "      2233469.7058595195,\r\n"
    			+ "      2426236.704378487,\r\n"
    			+ "      2636090.3995274478,\r\n"
    			+ "      2861291.520682322,\r\n"
    			+ "      2941644.93872506,\r\n"
    			+ "      3030670.368121983,\r\n"
    			+ "      3118804.692892575,\r\n"
    			+ "      3221665.507093451,\r\n"
    			+ "      3334265.883079559,\r\n"
    			+ "      3451720.046450042,\r\n"
    			+ "      3574698.4822592232,\r\n"
    			+ "      3709506.536773973,\r\n"
    			+ "      3854291.7678141696,\r\n"
    			+ "      4017915.200743838,\r\n"
    			+ "      4186532.079228457,\r\n"
    			+ "      4367972.39639263,\r\n"
    			+ "      4564768.092366119,\r\n"
    			+ "      4773166.254355622,\r\n"
    			+ "      4997236.328558248,\r\n"
    			+ "      5247631.057045164,\r\n"
    			+ "      5509698.045169078,\r\n"
    			+ "      5793880.789184617,\r\n"
    			+ "      6097070.968512654,\r\n"
    			+ "      6418786.364003003,\r\n"
    			+ "      6778307.645676289,\r\n"
    			+ "      7174503.040140765,\r\n"
    			+ "      7589248.814262325,\r\n"
    			+ "      8022017.321678352,\r\n"
    			+ "      8504567.266093925\r\n"
    			+ "    ],\r\n"
    			+ "    [\r\n"
    			+ "      25000,\r\n"
    			+ "      39498.2452365502,\r\n"
    			+ "      55026.635075310805,\r\n"
    			+ "      71867.94091305339,\r\n"
    			+ "      90011.16182133868,\r\n"
    			+ "      109587.42559205394,\r\n"
    			+ "      130803.34972752197,\r\n"
    			+ "      153757.30557688788,\r\n"
    			+ "      178554.39992959664,\r\n"
    			+ "      205456.55628766987,\r\n"
    			+ "      234242.930750124,\r\n"
    			+ "      265764.7381541823,\r\n"
    			+ "      299548.23975805484,\r\n"
    			+ "      336226.4723359388,\r\n"
    			+ "      375890.2444772057,\r\n"
    			+ "      418969.1729610682,\r\n"
    			+ "      465105.9422386809,\r\n"
    			+ "      515104.75233781413,\r\n"
    			+ "      568686.141950454,\r\n"
    			+ "      626164.2176688142,\r\n"
    			+ "      688811.664492057,\r\n"
    			+ "      756437.5048705899,\r\n"
    			+ "      829172.3593475787,\r\n"
    			+ "      908392.085579027,\r\n"
    			+ "      993441.0569006032,\r\n"
    			+ "      1085634.1435962182,\r\n"
    			+ "      1184082.0525608298,\r\n"
    			+ "      1290709.4484656034,\r\n"
    			+ "      1407793.5340273248,\r\n"
    			+ "      1532767.4399215847,\r\n"
    			+ "      1669664.810284734,\r\n"
    			+ "      1814599.6909238978,\r\n"
    			+ "      1974046.5868786508,\r\n"
    			+ "      2143941.5444485126,\r\n"
    			+ "      2327343.3334669587,\r\n"
    			+ "      2526417.437980098,\r\n"
    			+ "      2740225.574401446,\r\n"
    			+ "      2807898.0085522397,\r\n"
    			+ "      2883324.811096223,\r\n"
    			+ "      2959442.963353484,\r\n"
    			+ "      3046502.623247,\r\n"
    			+ "      3141264.526530006,\r\n"
    			+ "      3241142.3015882634,\r\n"
    			+ "      3345818.5668585477,\r\n"
    			+ "      3460741.415440616,\r\n"
    			+ "      3584068.0588548477,\r\n"
    			+ "      3722242.1583610917,\r\n"
    			+ "      3865822.799841903,\r\n"
    			+ "      4020542.0594449537,\r\n"
    			+ "      4187853.780449946,\r\n"
    			+ "      4365037.956663215,\r\n"
    			+ "      4556002.900809699,\r\n"
    			+ "      4768743.373570043,\r\n"
    			+ "      4991739.020242785,\r\n"
    			+ "      5233427.665716745,\r\n"
    			+ "      5491480.528343836,\r\n"
    			+ "      5766022.893740434,\r\n"
    			+ "      6071360.0282527935,\r\n"
    			+ "      6406582.700583743,\r\n"
    			+ "      6759079.471373754,\r\n"
    			+ "      7128977.0389624685,\r\n"
    			+ "      7539903.7821169505\r\n"
    			+ "    ],\r\n"
    			+ "    [\r\n"
    			+ "      25000,\r\n"
    			+ "      39220.1640774251,\r\n"
    			+ "      54489.3162902054,\r\n"
    			+ "      71014.29515567668,\r\n"
    			+ "      88793.86041446934,\r\n"
    			+ "      107972.27307272694,\r\n"
    			+ "      128703.11345611105,\r\n"
    			+ "      151133.8858406439,\r\n"
    			+ "      175315.3598456983,\r\n"
    			+ "      201443.53438958497,\r\n"
    			+ "      229480.5263636621,\r\n"
    			+ "      259990.12684324113,\r\n"
    			+ "      292713.10591602745,\r\n"
    			+ "      328125.6574213197,\r\n"
    			+ "      366364.4137256527,\r\n"
    			+ "      407778.5049124844,\r\n"
    			+ "      452279.0097035903,\r\n"
    			+ "      500283.611103257,\r\n"
    			+ "      551889.2403917267,\r\n"
    			+ "      607342.6840847566,\r\n"
    			+ "      667320.770583228,\r\n"
    			+ "      732101.5439034451,\r\n"
    			+ "      801802.0379033894,\r\n"
    			+ "      877480.8866132129,\r\n"
    			+ "      958879.8340672519,\r\n"
    			+ "      1047153.6881410098,\r\n"
    			+ "      1141611.2758181654,\r\n"
    			+ "      1243408.6656918013,\r\n"
    			+ "      1354637.217658112,\r\n"
    			+ "      1473719.7129599424,\r\n"
    			+ "      1603430.088994168,\r\n"
    			+ "      1742003.6814161486,\r\n"
    			+ "      1893094.101136525,\r\n"
    			+ "      2054413.3830375061,\r\n"
    			+ "      2228449.9625554304,\r\n"
    			+ "      2416744.4764327486,\r\n"
    			+ "      2619159.628120571,\r\n"
    			+ "      2674151.078379419,\r\n"
    			+ "      2735979.2540704636,\r\n"
    			+ "      2800081.233814393,\r\n"
    			+ "      2871339.739400549,\r\n"
    			+ "      2948263.1699804524,\r\n"
    			+ "      3030564.556726485,\r\n"
    			+ "      3116938.6514578727,\r\n"
    			+ "      3211976.294107259,\r\n"
    			+ "      3313844.3498955253,\r\n"
    			+ "      3426569.1159783457,\r\n"
    			+ "      3545113.5204553497,\r\n"
    			+ "      3673111.722497277,\r\n"
    			+ "      3810939.468533773,\r\n"
    			+ "      3956909.6589708067,\r\n"
    			+ "      4114769.473061149,\r\n"
    			+ "      4289855.690094922,\r\n"
    			+ "      4473779.995316492,\r\n"
    			+ "      4672974.542248872,\r\n"
    			+ "      4885890.088175017,\r\n"
    			+ "      5113259.423477866,\r\n"
    			+ "      5364412.410829297,\r\n"
    			+ "      5638662.361026721,\r\n"
    			+ "      5928910.128485181,\r\n"
    			+ "      6235936.7562465845,\r\n"
    			+ "      6575240.298139976\r\n"
    			+ "    ],\r\n"
    			+ "    [\r\n"
    			+ "      25000,\r\n"
    			+ "      38942.082918299995,\r\n"
    			+ "      53951.9975051,\r\n"
    			+ "      70160.64939829997,\r\n"
    			+ "      87576.5590076,\r\n"
    			+ "      106357.12055339992,\r\n"
    			+ "      126602.87718470013,\r\n"
    			+ "      148510.4661043999,\r\n"
    			+ "      172076.31976179997,\r\n"
    			+ "      197430.51249150006,\r\n"
    			+ "      224718.12197720018,\r\n"
    			+ "      254215.51553229993,\r\n"
    			+ "      285877.9720740001,\r\n"
    			+ "      320024.84250670066,\r\n"
    			+ "      356838.58297409967,\r\n"
    			+ "      396587.8368639005,\r\n"
    			+ "      439452.0771684997,\r\n"
    			+ "      485462.46986869985,\r\n"
    			+ "      535092.3388329996,\r\n"
    			+ "      588521.150500699,\r\n"
    			+ "      645829.8766743988,\r\n"
    			+ "      707765.5829363003,\r\n"
    			+ "      774431.7164592,\r\n"
    			+ "      846569.6876473987,\r\n"
    			+ "      924318.6112339004,\r\n"
    			+ "      1008673.2326858013,\r\n"
    			+ "      1099140.499075501,\r\n"
    			+ "      1196107.8829179993,\r\n"
    			+ "      1301480.9012888991,\r\n"
    			+ "      1414671.9859983,\r\n"
    			+ "      1537195.367703602,\r\n"
    			+ "      1669407.6719083993,\r\n"
    			+ "      1812141.615394399,\r\n"
    			+ "      1964885.2216264994,\r\n"
    			+ "      2129556.591643902,\r\n"
    			+ "      2307071.514885399,\r\n"
    			+ "      2498093.6818396957,\r\n"
    			+ "      2540404.1482065986,\r\n"
    			+ "      2588633.6970447036,\r\n"
    			+ "      2640719.504275302,\r\n"
    			+ "      2696176.855554098,\r\n"
    			+ "      2755261.8134308993,\r\n"
    			+ "      2819986.8118647067,\r\n"
    			+ "      2888058.736057197,\r\n"
    			+ "      2963211.1727739014,\r\n"
    			+ "      3043620.6409362033,\r\n"
    			+ "      3130896.0735955997,\r\n"
    			+ "      3224404.241068796,\r\n"
    			+ "      3325681.3855496002,\r\n"
    			+ "      3434025.1566176,\r\n"
    			+ "      3548781.361278399,\r\n"
    			+ "      3673536.0453125997,\r\n"
    			+ "      3810968.0066198003,\r\n"
    			+ "      3955820.9703902,\r\n"
    			+ "      4112521.4187809993,\r\n"
    			+ "      4280299.648006199,\r\n"
    			+ "      4460495.953215298,\r\n"
    			+ "      4657464.793405801,\r\n"
    			+ "      4870742.0214697,\r\n"
    			+ "      5098740.78559661,\r\n"
    			+ "      5342896.4735307,\r\n"
    			+ "      5610576.814163002\r\n"
    			+ "    ],\r\n"
    			+ "    [\r\n"
    			+ "      25000,\r\n"
    			+ "      38664.00175917489,\r\n"
    			+ "      53414.6787199946,\r\n"
    			+ "      69307.00364092326,\r\n"
    			+ "      86359.25760073065,\r\n"
    			+ "      104741.9680340729,\r\n"
    			+ "      124502.64091328921,\r\n"
    			+ "      145887.04636815592,\r\n"
    			+ "      168837.27967790165,\r\n"
    			+ "      193417.49059341516,\r\n"
    			+ "      219955.71759073826,\r\n"
    			+ "      248440.90422135874,\r\n"
    			+ "      279042.83823197277,\r\n"
    			+ "      311924.02759208163,\r\n"
    			+ "      347312.75222254667,\r\n"
    			+ "      385397.1688153166,\r\n"
    			+ "      426625.1446334091,\r\n"
    			+ "      470641.3286341427,\r\n"
    			+ "      518295.4372742724,\r\n"
    			+ "      569699.6169166412,\r\n"
    			+ "      624338.9827655696,\r\n"
    			+ "      683429.6219691555,\r\n"
    			+ "      747061.3950150106,\r\n"
    			+ "      815658.4886815845,\r\n"
    			+ "      889757.388400549,\r\n"
    			+ "      970192.7772305929,\r\n"
    			+ "      1056669.7223328366,\r\n"
    			+ "      1148807.1001441972,\r\n"
    			+ "      1248324.5849196862,\r\n"
    			+ "      1355624.2590366574,\r\n"
    			+ "      1470960.646413036,\r\n"
    			+ "      1596811.66240065,\r\n"
    			+ "      1731189.1296522731,\r\n"
    			+ "      1875357.0602154927,\r\n"
    			+ "      2030663.2207323737,\r\n"
    			+ "      2197398.5533380494,\r\n"
    			+ "      2377027.7355588204,\r\n"
    			+ "      2406657.218033778,\r\n"
    			+ "      2441288.1400189437,\r\n"
    			+ "      2481357.7747362107,\r\n"
    			+ "      2521013.9717076467,\r\n"
    			+ "      2562260.456881346,\r\n"
    			+ "      2609409.0670029284,\r\n"
    			+ "      2659178.8206565217,\r\n"
    			+ "      2714446.051440544,\r\n"
    			+ "      2773396.9319768813,\r\n"
    			+ "      2835223.0312128537,\r\n"
    			+ "      2903694.961682242,\r\n"
    			+ "      2978251.0486019235,\r\n"
    			+ "      3057110.844701427,\r\n"
    			+ "      3140653.063585991,\r\n"
    			+ "      3232302.6175640505,\r\n"
    			+ "      3332080.323144679,\r\n"
    			+ "      3437861.9454639074,\r\n"
    			+ "      3552068.2953131264,\r\n"
    			+ "      3674709.2078373805,\r\n"
    			+ "      3807732.4829527303,\r\n"
    			+ "      3950517.175982305,\r\n"
    			+ "      4102821.6819126788,\r\n"
    			+ "      4268571.442708039,\r\n"
    			+ "      4449856.190814816,\r\n"
    			+ "      4645913.330186028\r\n"
    			+ "    ],\r\n"
    			+ "    [\r\n"
    			+ "      25000,\r\n"
    			+ "      38385.92060004979,\r\n"
    			+ "      52877.359934889195,\r\n"
    			+ "      68453.35788354655,\r\n"
    			+ "      85141.9561938613,\r\n"
    			+ "      103126.81551474589,\r\n"
    			+ "      122402.4046418783,\r\n"
    			+ "      143263.62663191193,\r\n"
    			+ "      165598.2395940033,\r\n"
    			+ "      189404.46869533026,\r\n"
    			+ "      215193.31320427635,\r\n"
    			+ "      242666.29291041754,\r\n"
    			+ "      272207.7043899454,\r\n"
    			+ "      303823.21267746255,\r\n"
    			+ "      337786.92147099366,\r\n"
    			+ "      374206.50076673273,\r\n"
    			+ "      413798.2120983185,\r\n"
    			+ "      455820.18739958556,\r\n"
    			+ "      501498.5357155452,\r\n"
    			+ "      550878.0833325837,\r\n"
    			+ "      602848.0888567405,\r\n"
    			+ "      659093.6610020107,\r\n"
    			+ "      719691.0735708213,\r\n"
    			+ "      784747.2897157704,\r\n"
    			+ "      855196.1655671976,\r\n"
    			+ "      931712.3217753844,\r\n"
    			+ "      1014198.9455901722,\r\n"
    			+ "      1101506.3173703952,\r\n"
    			+ "      1195168.2685504735,\r\n"
    			+ "      1296576.5320750151,\r\n"
    			+ "      1404725.92512247,\r\n"
    			+ "      1524215.6528929009,\r\n"
    			+ "      1650236.6439101472,\r\n"
    			+ "      1785828.8988044863,\r\n"
    			+ "      1931769.8498208453,\r\n"
    			+ "      2087725.5917906999,\r\n"
    			+ "      2255961.789277945,\r\n"
    			+ "      2272910.2878609574,\r\n"
    			+ "      2293942.582993184,\r\n"
    			+ "      2321996.04519712,\r\n"
    			+ "      2345851.0878611957,\r\n"
    			+ "      2369259.1003317926,\r\n"
    			+ "      2398831.32214115,\r\n"
    			+ "      2430298.9052558467,\r\n"
    			+ "      2465680.9301071865,\r\n"
    			+ "      2503173.223017559,\r\n"
    			+ "      2539549.9888301077,\r\n"
    			+ "      2582985.6822956884,\r\n"
    			+ "      2630820.711654247,\r\n"
    			+ "      2680196.532785254,\r\n"
    			+ "      2732524.765893583,\r\n"
    			+ "      2791069.1898155008,\r\n"
    			+ "      2853192.639669558,\r\n"
    			+ "      2919902.920537615,\r\n"
    			+ "      2991615.171845254,\r\n"
    			+ "      3069118.767668562,\r\n"
    			+ "      3154969.0126901623,\r\n"
    			+ "      3243569.5585588086,\r\n"
    			+ "      3334901.3423556574,\r\n"
    			+ "      3438402.0998194665,\r\n"
    			+ "      3556815.908098933,\r\n"
    			+ "      3681249.846209054\r\n"
    			+ "    ],\r\n"
    			+ "    [\r\n"
    			+ "      25000,\r\n"
    			+ "      38107.83944092468,\r\n"
    			+ "      52340.04114978379,\r\n"
    			+ "      67599.71212616983,\r\n"
    			+ "      83924.65478699195,\r\n"
    			+ "      101511.66299541887,\r\n"
    			+ "      120302.16837046738,\r\n"
    			+ "      140640.20689566794,\r\n"
    			+ "      162359.19951010498,\r\n"
    			+ "      185391.44679724536,\r\n"
    			+ "      210430.90881781446,\r\n"
    			+ "      236891.68159947637,\r\n"
    			+ "      265372.57054791803,\r\n"
    			+ "      295722.3977628435,\r\n"
    			+ "      328261.0907194407,\r\n"
    			+ "      363015.8327181488,\r\n"
    			+ "      400971.2795632279,\r\n"
    			+ "      440999.0461650284,\r\n"
    			+ "      484701.63415681804,\r\n"
    			+ "      532056.549748526,\r\n"
    			+ "      581357.1949479114,\r\n"
    			+ "      634757.7000348659,\r\n"
    			+ "      692320.7521266319,\r\n"
    			+ "      753836.0907499562,\r\n"
    			+ "      820634.9427338461,\r\n"
    			+ "      893231.8663201759,\r\n"
    			+ "      971728.1688475078,\r\n"
    			+ "      1054205.5345965934,\r\n"
    			+ "      1142011.9521812606,\r\n"
    			+ "      1237528.8051133726,\r\n"
    			+ "      1338491.2038319039,\r\n"
    			+ "      1451619.6433851516,\r\n"
    			+ "      1569284.1581680214,\r\n"
    			+ "      1696300.7373934796,\r\n"
    			+ "      1832876.478909317,\r\n"
    			+ "      1978052.6302433505,\r\n"
    			+ "      2134895.8429970695,\r\n"
    			+ "      2139163.3576881373,\r\n"
    			+ "      2146597.0259674243,\r\n"
    			+ "      2162634.3156580287,\r\n"
    			+ "      2170688.2040147446,\r\n"
    			+ "      2176257.7437822395,\r\n"
    			+ "      2188253.577279371,\r\n"
    			+ "      2201418.989855171,\r\n"
    			+ "      2216915.8087738296,\r\n"
    			+ "      2232949.514058237,\r\n"
    			+ "      2243876.9464473613,\r\n"
    			+ "      2262276.4029091345,\r\n"
    			+ "      2283390.37470657,\r\n"
    			+ "      2303282.220869081,\r\n"
    			+ "      2324396.4682011753,\r\n"
    			+ "      2349835.7620669515,\r\n"
    			+ "      2374304.9561944366,\r\n"
    			+ "      2401943.8956113225,\r\n"
    			+ "      2431162.0483773816,\r\n"
    			+ "      2463528.3274997436,\r\n"
    			+ "      2502205.5424275943,\r\n"
    			+ "      2536621.941135313,\r\n"
    			+ "      2566981.002798636,\r\n"
    			+ "      2608232.756930895,\r\n"
    			+ "      2663775.6253830492,\r\n"
    			+ "      2716586.36223208\r\n"
    			+ "    ]\r\n"
    			+ "  ],\r\n"
    			+ "  \"success\": true\r\n"
    			+ "}";
    	return value;
    }
}


