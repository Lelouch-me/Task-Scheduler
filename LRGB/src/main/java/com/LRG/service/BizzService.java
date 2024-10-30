package com.LRG.service;

import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import com.LRG.Utils.CommonUtils;
import com.LRG.domain.AdjustedPrice;


@Service
public class BizzService {
	private static ArrayList<String> datess = new ArrayList<>();
	public static List<TreeMap<String, String>> getDseNews(String ticker) throws ClassNotFoundException, SQLException {
		Connection con = connectLRGBLoader();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		List<TreeMap<String, String>> news = new ArrayList<>();
		String query = "SELECT * FROM dse_analyzer_loader.MAN_archive where ticker = '"+ticker+"' order by post_date_time desc limit 7;";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			TreeMap<String,String> newsDate = new TreeMap<>();
			
			String date = rs.getString(1);
			String dseNews = rs.getString(3);
			newsDate.put(date, dseNews);
			news.add(newsDate);
		}
		con.close();
		return news;
	}
	
	public static boolean createNewWatchList(int userId, Map<String, Object> userWatchlist) throws ClassNotFoundException, SQLException {
		Connection con = connectLRGB();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		String query = "SELECT * FROM dse_analyzer.bizz_watchlist where userID = "+userId+";";
		rs = stmt.executeQuery(query);
		if(!rs.next()) {
			int count = 0;
			List<Map<String, Object>> defaultWatchList = (List<Map<String, Object>>) userWatchlist.get("defaultWatchList");
			for (Map<String, Object> entry : defaultWatchList) {
	            String ticker = (String) entry.get("ticker");
	            Integer weightNumber = (Integer) entry.get("weight");
	            String weight = weightNumber.toString();
	            String share = (String) entry.get("share");
	            String iquery = "insert ignore into dse_analyzer.bizz_watchlist(watchlist_id,watchlist_name,userID,ticker,share_no,weight,time)"
	            		+ "values(0, 'Default watchlist', "+userId+", '"+ticker+"', "+share+", "+weight+", NOW());";
	            stmt.execute(iquery);
	            count++;
	            
	        }
			con.close();
			if(count == 10) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
		
	}
	
	public static List<Map<String, Object>> viewWatchList(String auth, Map<String, Object> userWatchlistID) throws SQLException, ClassNotFoundException {
		Connection con = connectLRGB();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		String id = getUserId(auth);
		String watchListId = (String) userWatchlistID.get("id");
		List<Map<String, Object>> watchListsss = new ArrayList<>();
		
		String query = "SELECT * FROM dse_analyzer.bizz_watchlist where userID = "+id+" and watchlist_id = "+watchListId+";";
		rs = stmt.executeQuery(query);
		if(rs != null) {
			while(rs.next()) {
				Map<String, Object> watchList = new HashMap<>();
				String ticker = rs.getString("ticker");
				String weight = rs.getString("weight") == null ? null : rs.getString("weight");
				String share = rs.getString("share_no") == null ? null : rs.getString("share_no");
				watchList.put("ticker", ticker);
				watchList.put("weight", weight);
				watchList.put("share", share);
				watchList.put("time", rs.getString("time"));
				watchListsss.add(watchList);
			}
		}
		con.close();
		return watchListsss;
	}
	
	public static List<Map<String, Object>> getWatchListIdAndName(String auth) throws SQLException, ClassNotFoundException {
		Connection con = connectLRGB();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		String id = getUserId(auth);
		List<Map<String, Object>> watchListsss = new ArrayList<>();
		
		String query = "select distinct watchlist_id, watchlist_name from dse_analyzer.bizz_watchlist where userID = "+id+";";
		rs = stmt.executeQuery(query);
		if(rs != null) {
			while(rs.next()) {
				Map<String, Object> watchList = new HashMap<>();
				watchList.put("id",rs.getString(1));
				watchList.put("name",rs.getString(2));
				watchListsss.add(watchList);
			}
		}
		con.close();
		return watchListsss;
	}
	
	public static Map<String, List<Map<String, Object>>>updateWatchList(String auth, Map<String, Object> userWatchlist) throws ClassNotFoundException, SQLException {
	Connection con = connectLRGB();
	Statement stmt = con.createStatement();
	ResultSet rs = null;
	String id = getUserId(auth);
	Map<String, List<Map<String, Object>>> returnData = new HashMap<String, List<Map<String, Object>>>();
	List<Map<String, Object>> watchListsss = new ArrayList<>();
	
	String query = "SELECT * FROM dse_analyzer.bizz_watchlist where userID = "+id+";";
	rs = stmt.executeQuery(query);
	String type = (String) userWatchlist.get("type");
	String watchlistId = (String) userWatchlist.get("watchlist_id");
	String watchlistName = (String) userWatchlist.get("watchlist_name");
	List<Map<String, Object>> defaultWatchList = (List<Map<String, Object>>) userWatchlist.get("data");
	if(type.equalsIgnoreCase("weight")) {
		if(defaultWatchList != null) {
			if(rs.next()) {
				String deleteQuery = "DELETE FROM dse_analyzer.bizz_watchlist where userID = "+id+" and watchlist_id = "+watchlistId+" and watchlist_name = '"+watchlistName+"';";
				stmt.execute(deleteQuery);
			}
		}
		for (Map<String, Object> entry : defaultWatchList) {
			
	        String ticker = (String) entry.get("ticker");
	        String weightNumber = (String) entry.get("weight");
	        String insertQuery = "insert ignore into dse_analyzer.bizz_watchlist(watchlist_id,watchlist_name, userID,ticker,weight,time)"
	        		+ "values("+watchlistId+", '"+watchlistName+"', "+id+", '"+ticker+"', "+weightNumber+", NOW());";
	        stmt.execute(insertQuery);      
	    }
			
		String getQuery = "SELECT * FROM dse_analyzer.bizz_watchlist where userID = "+id+" and watchlist_id = "+watchlistId+" and watchlist_name = '"+watchlistName+"';";
		rs = stmt.executeQuery(getQuery);
		if(rs != null) {
			while(rs.next()) {
				Map<String, Object> watchList = new HashMap<>();
				String ticker = rs.getString("ticker");
				String weight = rs.getString("weight") == null ? null : rs.getString("weight");
				String share = rs.getString("share_no") == null ? null : rs.getString("share_no");
				watchList.put("watchlist_name", rs.getString("watchlist_name"));
				watchList.put("watchlist_id", rs.getString("watchlist_id"));
				watchList.put("ticker", ticker);
				watchList.put("weight", weight);
				watchList.put("share", share);
				watchList.put("time", rs.getString("time"));
				watchListsss.add(watchList);
			}
		}
		returnData.put("Weight",watchListsss );
		return returnData;
	}
	else {
		
		TreeMap<String, Double> tickerData = new TreeMap<>();
		TreeMap<String, String> tickerShare = new TreeMap<>();
		for (Map<String, Object> entry : defaultWatchList) {
			
	        String ticker = (String) entry.get("ticker");
//	        String weightNumber = (String) entry.get("weight");
//	        String weight = weightNumber.toString();
	        Integer share = (Integer) entry.get("share");
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
		
		TreeMap<String, Double> weightedMap = new TreeMap<>();
		for(Map.Entry<String,Double> map :tickerData.entrySet()) {
			Double weight = map.getValue()/sumMcap;
			if(!weight.isNaN()) {
				weightedMap.put(map.getKey(), weight*100);
			}
			else {
				weightedMap.put(map.getKey(), 0.0);
			}
			
		}
		
		List<Double> weights = price(weightedMap);
		

		if(defaultWatchList != null) {
			String deleteQuery = "DELETE FROM dse_analyzer.bizz_watchlist where userID = "+id+" and watchlist_id = "+watchlistId+" and watchlist_name = '"+watchlistName+"';";
			stmt.execute(deleteQuery);
				
		}
		for (Entry<String, Double> entry : weightedMap.entrySet()) {
				
			String ticker = entry.getKey();
		    Double weightNumber = entry.getValue();
		    String share = tickerShare.get(ticker);
		    String insertQuery = "insert ignore into dse_analyzer.bizz_watchlist(watchlist_id,watchlist_name,userID,ticker,share_no,weight,time)"
		        	+ "values("+watchlistId+", '"+watchlistName+"',"+id+", '"+ticker+"',"+share+", "+weightNumber+", NOW());";
		    System.out.println(insertQuery);
		    stmt.execute(insertQuery);      
		    }
			String getQuery = "SELECT * FROM dse_analyzer.bizz_watchlist where userID = "+id+" and watchlist_id = "+watchlistId+" and watchlist_name = '"+watchlistName+"';";
			rs = stmt.executeQuery(getQuery);
			if(rs != null) {
				while(rs.next()) {
					Map<String, Object> watchList = new HashMap<>();
					String ticker = rs.getString("ticker");
					String weight = rs.getString("weight") == null ? null : rs.getString("weight");
					String share = rs.getString("share_no") == null ? null : rs.getString("share_no");
					watchList.put("watchlist_name", rs.getString("watchlist_name"));
					watchList.put("watchlist_id", rs.getString("watchlist_id"));
					watchList.put("share", share);
					watchList.put("ticker", ticker);
					watchList.put("weight", weight);
					watchList.put("share", share);
					watchList.put("time", rs.getString("time"));
					watchListsss.add(watchList);
				}
			}
			returnData.put("Share",watchListsss );
		}
	
	con.close();
	return returnData;
}
	
	public static Map<String, List<Map<String, Object>>>addWatchList(String auth, Map<String, Object> userWatchlist) throws ClassNotFoundException, SQLException {
		Connection con = connectLRGB();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		String id = getUserId(auth);
		Map<String, List<Map<String, Object>>> returnData = new HashMap<String, List<Map<String, Object>>>();
		List<Map<String, Object>> watchListsss = new ArrayList<>();
		
		String query = "SELECT max(watchlist_id)as watchlistId FROM dse_analyzer.bizz_watchlist where userID = "+id+";";
		rs = stmt.executeQuery(query);
		int watchlistId = rs.getInt(1) + 1;
		String type = (String) userWatchlist.get("type");
		String watchlistName = (String) userWatchlist.get("watchlistName");
		List<Map<String, Object>> defaultWatchList = (List<Map<String, Object>>) userWatchlist.get("data");
		if(type.equalsIgnoreCase("weight")) {
			for (Map<String, Object> entry : defaultWatchList) {
				
		        String ticker = (String) entry.get("ticker");
		        String weightNumber = (String) entry.get("weight");
		        String insertQuery = "insert ignore into dse_analyzer.bizz_watchlist(watchlist_id,watchlist_name, userID,ticker,weight,time)"
		        		+ "values("+watchlistId+", '"+watchlistName+"', "+id+", '"+ticker+"', "+weightNumber+", NOW());";
		        stmt.execute(insertQuery);      
		    }
				
			String getQuery = "SELECT * FROM dse_analyzer.bizz_watchlist where userID = "+id+" and watchlist_id = "+watchlistId+" and watchlist_name = '"+watchlistName+"';";
			rs = stmt.executeQuery(getQuery);
			if(rs != null) {
				while(rs.next()) {
					Map<String, Object> watchList = new HashMap<>();
					String ticker = rs.getString("ticker");
					String weight = rs.getString("weight") == null ? null : rs.getString("weight");
					String share = rs.getString("share_no") == null ? null : rs.getString("share_no");
					watchList.put("watchlist_name", rs.getString("watchlist_name"));
					watchList.put("watchlist_id", rs.getString("watchlist_id"));
					watchList.put("ticker", ticker);
					watchList.put("weight", weight);
					watchList.put("share", share);
					watchList.put("time", rs.getString("time"));
					watchListsss.add(watchList);
				}
			}
			returnData.put("Weight",watchListsss );
			return returnData;
		}
		else {
			
			TreeMap<String, Double> tickerData = new TreeMap<>();
			TreeMap<String, String> tickerShare = new TreeMap<>();
			for (Map<String, Object> entry : defaultWatchList) {
				
		        String ticker = (String) entry.get("ticker");
//		        String weightNumber = (String) entry.get("weight");
//		        String weight = weightNumber.toString();
		        Integer share = (Integer) entry.get("share");
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
//		        String insertQuery = "insert ignore into dse_analyzer.bizz_watchlist(userID,ticker,weight,time)"
//		        		+ "values("+id+", '"+ticker+"', "+weightNumber+", NOW());";
//		        stmt.execute(insertQuery);      
		    }
			
			Set<String> tickers = tickerData.keySet();
			List<Double> mcap = price(tickerData);
			
			Double sumMcap = 0.0;
			for(Double value : mcap) {
				sumMcap += value;
			}
			
			TreeMap<String, Double> weightedMap = new TreeMap<>();
			for(Map.Entry<String,Double> map :tickerData.entrySet()) {
				Double weight = map.getValue()/sumMcap;
				if(!weight.isNaN()) {
					weightedMap.put(map.getKey(), weight*100);
				}
				else {
					weightedMap.put(map.getKey(), 0.0);
				}
				
			}
			
			List<Double> weights = price(weightedMap);
			for (Entry<String, Double> entry : weightedMap.entrySet()) {
					
				String ticker = entry.getKey();
			    Double weightNumber = entry.getValue();
			    String share = tickerShare.get(ticker);
			    String insertQuery = "insert ignore into dse_analyzer.bizz_watchlist(watchlist_id,watchlist_name,userID,ticker,share_no,weight,time)"
			        	+ "values("+watchlistId+", '"+watchlistName+"',"+id+", '"+ticker+"',"+share+", "+weightNumber+", NOW());";
			    System.out.println(insertQuery);
			    stmt.execute(insertQuery);      
			    }
				String getQuery = "SELECT * FROM dse_analyzer.bizz_watchlist where userID = "+id+" and watchlist_id = "+watchlistId+" and watchlist_name = '"+watchlistName+"';";
				rs = stmt.executeQuery(getQuery);
				if(rs != null) {
					while(rs.next()) {
						Map<String, Object> watchList = new HashMap<>();
						String ticker = rs.getString("ticker");
						String weight = rs.getString("weight") == null ? null : rs.getString("weight");
						String share = rs.getString("share_no") == null ? null : rs.getString("share_no");
						watchList.put("watchlist_name", rs.getString("watchlist_name"));
						watchList.put("watchlist_id", rs.getString("watchlist_id"));
						watchList.put("share", share);
						watchList.put("ticker", ticker);
						watchList.put("weight", weight);
						watchList.put("share", share);
						watchList.put("time", rs.getString("time"));
						watchListsss.add(watchList);
					}
				}
				returnData.put("Share",watchListsss );
			}
		
		con.close();
		return returnData;
	}
	
	public static boolean deleteWatchlsitForUser(String auth,Map<String, Object> userInfoList) throws ClassNotFoundException, SQLException {
		Connection con = connectLRGB();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		String id = getUserId(auth);
		
		String query = "SELECT * FROM dse_analyzer.bizz_watchlist where userID = "+id+";";
		rs = stmt.executeQuery(query);
		String watchlistId = (String) userInfoList.get("id");
		String watchlistName = (String) userInfoList.get("name");
		if(rs.next()) {
			String deleteQuery = "DELETE FROM dse_analyzer.bizz_watchlist where userID = "+id+" and watchlist_id = "+watchlistId+" and watchlist_name = '"+watchlistName+"';";
			stmt.execute(deleteQuery);
			return true;
		}
		return false;
	}
	
	public static List<List<Object>> getWatchListNews(String auth, String watchlistId) throws ClassNotFoundException, SQLException {
		Connection con = connectLRGB();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		String id = getUserId(auth);
		List<String> tickers = new ArrayList<>();
		List<List<Object>> newsList = new ArrayList<>();
		
		String query = "SELECT * FROM dse_analyzer.bizz_watchlist where userID = "+id+" and watchlist_id = "+watchlistId+";";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			while(rs.next()) {
				tickers.add(rs.getString("ticker"));
			}	
		}
		for(String tic : tickers) {
			List<Object> ticNews = new ArrayList<>();
			String newQuery = "SELECT * FROM dse_analyzer_loader.MAN_archive where ticker = '"+tic+"' "
					+ "and post_date_time> 2022-12-01 order by post_date_time desc;";
			rs = stmt.executeQuery(newQuery);
			while(rs.next()) {
				Map<Object,String> newsData = new HashMap<>();
				newsData.put(tic+"::"+rs.getString("post_date_time"), rs.getString("news"));
				ticNews.add(newsData);
			}
			newsList.add(ticNews);
		}
		
		con.close();
		return newsList;
	}
	
	public static Map<String, Double> getTickerWeightsFromDB(String auth, String watchlistId, Connection conn) throws SQLException {
        Map<String, Double> weights = new HashMap<>();
        String id = getUserId(auth);

        Statement stmt = conn.createStatement();
		String query = "SELECT * FROM dse_analyzer.bizz_watchlist where userID = "+id+" and watchlist_id = "+watchlistId+"; ";
       
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String ticker = rs.getString("ticker");
            double weight = rs.getDouble("weight")/100;
            weights.put(ticker, weight);
        }
        
        return weights;
    }
	public static TreeMap<String, Double> getDailyReturnsDB(Map<String, Double> weights,String dateRange,Connection conn) throws SQLException {
        TreeMap<String, Double> portfolioData = new TreeMap<>();
        Set<String> tickers = weights.keySet();
		TreeMap<String, HashMap<String, Double>> dateTickerReturnMap = getDateTickerReturnMap("'"+String.join("','", tickers)+"'",dateRange,  conn);
       
        for(String thisDate : dateTickerReturnMap.keySet()) {
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
        	double totalReturn = 0;
        	for(double ret: returns) {
        		totalReturn += ret * multiplier;
        	}
        	portfolioData.put(thisDate, totalReturn);
    	}
        List<Double> priceList =price( portfolioData);
        List<String> dateList = new ArrayList<>(portfolioData.keySet());
        
        TreeMap<String,Double> priceMap = new TreeMap<String,Double>();
		int i = 0;
//		Double firstRetVal = null;
		Double initialVal = 100.0;
		Double firstPriceVal = null;
		DecimalFormat df = new DecimalFormat("#.##"); 
		if(priceList.size()>0) {
			int count = 0;
			for(Double dailyPriceReturn : priceList) {
				if(dailyPriceReturn!=null) {

					Double currPriceVal = dailyPriceReturn;
					if(i==0) {
						firstPriceVal = dailyPriceReturn;
						priceMap.put(dateList.get(count),initialVal);
						i++;
						count++;
						continue;
					}

					initialVal = initialVal+currPriceVal;
					if(initialVal!=null) {
						initialVal = Double.valueOf(df.format(initialVal));
						priceMap.put(dateList.get(count),initialVal);
					}
				}
				count++;
			}
		}
		return priceMap;
    }
	
	public static TreeMap<String, Double> getIntraDayReturnsDB(Map<String, Double> weights, Connection conn) throws SQLException {
        TreeMap<String, Double> portfolioData = new TreeMap<>();
        Set<String> tickers = weights.keySet();
		TreeMap<String, HashMap<String, Double>> dateTickerReturnMap = getIntradayDateTickerReturnMap("'"+String.join("','", tickers)+"'",  conn);
       
        for(String thisDate : dateTickerReturnMap.keySet()) {
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
        	double totalReturn = 0;
        	for(double ret: returns) {
        		totalReturn += ret * multiplier;
        	}
        	portfolioData.put(thisDate, totalReturn);
    	}
        List<Double> priceList =price( portfolioData);
        List<String> dateList = new ArrayList<>(portfolioData.keySet());
        
        TreeMap<String,Double> priceMap = new TreeMap<String,Double>();
		int i = 0;
//		Double firstRetVal = null;
		Double initialVal = 100.0;
		Double firstPriceVal = null;
		DecimalFormat df = new DecimalFormat("#.##"); 
		if(priceList.size()>0) {
			int count = 0;
			for(Double dailyPriceReturn : priceList) {
				if(dailyPriceReturn!=null) {

					Double currPriceVal = dailyPriceReturn;
					if(i==0) {
						firstPriceVal = dailyPriceReturn;
						priceMap.put(dateList.get(count),initialVal);
						i++;
						count++;
						continue;
					}

					initialVal = initialVal+currPriceVal;
					if(initialVal!=null) {
						initialVal = Double.valueOf(df.format(initialVal));
						priceMap.put(dateList.get(count),initialVal);
					}
				}
				count++;
			}
		}

		return priceMap;
		
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
        String query = "SELECT date, ticker, return_price FROM dse_analyzer.eod_data where ticker in (" + tickerCSV + ") "
                + "and date between '"+startDate+"' and '"+endDate+"' order by date, ticker";
        ResultSet rs = stmt.executeQuery(query);
        String date = "";
        HashMap<String, Double> tickerReturn = new HashMap<>();
        while (rs.next()) {
            String returnDate = rs.getString("date");
            String ticker = rs.getString("ticker");
            double returnVal = rs.getDouble("return_price");
            if (!returnDate.equals(date)) {
                tickerReturn = new HashMap<>();
                dateTickerReturnMap.put(returnDate, tickerReturn);
                date = returnDate;
            }


            tickerReturn.put(ticker, returnVal);
        }
        return dateTickerReturnMap;
    }
        
	
	
	public static TreeMap<String, HashMap<String, Double>> getIntradayDateTickerReturnMap(String tickerCSV, Connection conn) throws SQLException {
        TreeMap<String, HashMap<String, Double>> dateTickerReturnMap = new TreeMap<>();
        Statement stmt = conn.createStatement();
        String query = "SELECT time, ticker,  return_price FROM dse_analyzer.intra_day_return where ticker in (" + tickerCSV + ")  order by time, ticker ;";
        ResultSet rs = stmt.executeQuery(query);
        String date = "";
        HashMap<String, Double> tickerReturn = new HashMap<>();
        while (rs.next()) {
            String returnDate = rs.getString("time");
            String ticker = rs.getString("ticker");
            double returnVal = rs.getDouble("return_price");
            if (!returnDate.equals(date)) {
                tickerReturn = new HashMap<>();
                dateTickerReturnMap.put(returnDate, tickerReturn);
                date = returnDate;
            }


            tickerReturn.put(ticker, returnVal);
        }
        return dateTickerReturnMap;
    }
	
    public static TreeMap<String, Double> getWeightedHistoricalData(String auth, String watchlistId, String startDate, String endDate) throws ClassNotFoundException, SQLException {
    	Connection con = connectLRGB();
		Map<String, Double> weights = getTickerWeightsFromDB(auth,watchlistId, con);
		TreeMap<String, Double> portfolioData = getDailyReturnsDB(weights,startDate+":"+endDate ,con);
		con.close();
		return portfolioData;

    }
    
    
    public static TreeMap<String, Double> getWeightedIntradayData(String auth, String watchlistId) throws ClassNotFoundException, SQLException {
    	Connection con = connectLRGB();
		Map<String, Double> weights = getTickerWeightsFromDB(auth, watchlistId,  con);
		TreeMap<String, Double> portfolioData = getIntraDayReturnsDB(weights,con);
		con.close();
		return portfolioData;

    }
    

    
    public static TreeMap<String, Double> getGuestWeightedHistoricalData(String startDate, String endDate, Map<String, Double> list) throws ClassNotFoundException, SQLException {
    	Connection con = connectLRGB();
		TreeMap<String, Double> portfolioData = getDailyReturnsDB(list,startDate+":"+endDate ,con);
		return portfolioData;

    }
    
    
    public static TreeMap<String, Double> getGuestWeightedIntradayData(Map<String, Double> list) throws ClassNotFoundException, SQLException {
    	Connection con = connectLRGB();
		TreeMap<String, Double> portfolioData = getIntraDayReturnsDB(list,con);
		return portfolioData;
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

  
    
    public static List<List<Object>> getGuestWatchListNews(Set<String> tickers) throws ClassNotFoundException, SQLException {
		Connection con = connectLRGB();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		List<List<Object>> newsList = new ArrayList<>();
		

		for(String tic : tickers) {
			List<Object> ticNews = new ArrayList<>();
			String newQuery = "SELECT * FROM dse_analyzer_loader.MAN_archive where ticker = '"+tic+"' "
					+ "and post_date_time> 2022-12-01 order by post_date_time desc;";
			rs = stmt.executeQuery(newQuery);
			while(rs.next()) {
				Map<Object,String> newsData = new HashMap<>();
				newsData.put(tic+"::"+rs.getString("post_date_time"), rs.getString("news"));
				ticNews.add(newsData);
			}
			newsList.add(ticNews);
		}
		
		con.close();
		return newsList;
	}
    
    public static TreeMap<String, TreeMap<String,Double>> getBenchmakTicDailyReturnsDB(List<String> tickers, String dateRange,Connection conn) throws SQLException {
    	TreeMap<String, TreeMap<String,Double>>  benchMarkPrice = new TreeMap<String, TreeMap<String,Double>>();
    	for(String ticker : tickers) {
    		TreeMap<String, Double> portfolioData = getBenchmarkReturnByDate(ticker, dateRange, conn);
            
            List<Double> priceList =price( portfolioData);
            List<String> dateList = new ArrayList<>(portfolioData.keySet());
            
            TreeMap<String,Double> priceMap = new TreeMap<String,Double>();
    		int i = 0;
//    		Double firstRetVal = null;
    		Double initialVal = 100.0;
    		Double firstPriceVal = null;
    		DecimalFormat df = new DecimalFormat("#.##"); 
    		if(priceList.size()>0) {
    			int count = 0;
    			for(Double dailyPriceReturn : priceList) {
    				if(dailyPriceReturn!=null) {

    					Double currPriceVal = dailyPriceReturn;
    					if(i==0) {
    						firstPriceVal = dailyPriceReturn;
    						priceMap.put(dateList.get(count),initialVal);
    						i++;
    						count++;
    						continue;
    					}

    					initialVal = initialVal*(1+currPriceVal);
    					if(initialVal!=null) {
    						initialVal = Double.valueOf(df.format(initialVal));
    						priceMap.put(dateList.get(count),initialVal);
    					}
    				}
    				count++;
    			}
    		}
    		benchMarkPrice.put(ticker, priceMap);
    	}
    	return benchMarkPrice;
    }
    
    public static TreeMap<String, Double> getBenchmarkReturnByDate(String ticker,String dateRange,Connection con) throws SQLException{
    	TreeMap<String, Double> dailyReturn = new TreeMap<String, Double>();
    	String startDate = dateRange.split(":")[0];
        String endDate = dateRange.split(":")[1];
    	Statement stmt = con.createStatement();
    	String query = "SELECT date, ticker, return_price FROM dse_analyzer.eod_data where ticker = '"+ticker+"' "
                + "and date between '"+startDate+"' and '"+endDate+"' order by date, ticker";
    	ResultSet rs = stmt.executeQuery(query);
    	while(rs.next()) {
    		dailyReturn.put(rs.getString("date"), (rs.getDouble("return_price")/100));
    	}
    	return dailyReturn;
    }
    
    public static TreeMap<String, TreeMap<String,Double>> getBenchMark(List<String> ticker, String dateRange) throws ClassNotFoundException, SQLException{
    	Connection con = connectLRGB();
    	TreeMap<String, TreeMap<String,Double>> getModifiedValue = getBenchmakTicDailyReturnsDB(ticker, dateRange, con);
    	return getModifiedValue;
    }
    
	public static String getUserId(String auth) {
		String token = null;
		if(auth != null && auth.startsWith("Bearer ")) {
			token = auth.split("-")[1];
			}
		return token;
		
	}

	public static Map<String, Double> getTickerSharesFromDB(String auth, String watchlistId, Connection conn) throws SQLException {
        Map<String, Double> weights = new HashMap<>();
        String id = getUserId(auth);

        Statement stmt = conn.createStatement();
		String query = "SELECT * FROM dse_analyzer.bizz_watchlist where userID = "+id+" and watchlist_id = "+watchlistId+";";
       
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String ticker = rs.getString("ticker");
            double weight = rs.getDouble("share_no");
            weights.put(ticker, weight);
        }
        
        return weights;
    }
	
    public static TreeMap<String, Double> getPortfolioValueData(String auth, String watchlistId, String startDate, String endDate) throws ClassNotFoundException, SQLException {
    	Connection con = connectLRGB();
		Map<String, Double> share = getTickerSharesFromDB(auth, watchlistId, con);
		TreeMap<String, Double> portfolioData = getPortfolioValueDB(share,startDate+":"+endDate ,con);
		con.close();
		return portfolioData;

    }
    
	public static TreeMap<String, HashMap<String, Double>> getTickerPriceDB(String tickerCSV, String dateRange, Connection conn) throws SQLException {
        TreeMap<String, HashMap<String, Double>> dateTickerReturnMap = new TreeMap<>();
        String startDate = dateRange.split(":")[0];
        String endDate = dateRange.split(":")[1];
        Statement stmt = conn.createStatement();
        String query = "SELECT date, ticker,  close FROM dse.daily_price where ticker in (" + tickerCSV + ")"
        		+ "and date between '"+startDate+"' and '"+endDate+"'   order by date, ticker ;";
        ResultSet rs = stmt.executeQuery(query);
        String date = "";
        HashMap<String, Double> tickerReturn = new HashMap<>();
        while (rs.next()) {
            String returnDate = rs.getString("date");
            String ticker = rs.getString("ticker");
            double returnVal = rs.getDouble("close");
            if (!returnDate.equals(date)) {
                tickerReturn = new HashMap<>();
                dateTickerReturnMap.put(returnDate, tickerReturn);
                date = returnDate;
            }


            tickerReturn.put(ticker, returnVal);
        }
        return dateTickerReturnMap;
    }
    
    public static TreeMap<String, Double> getPortfolioValueDB(Map<String, Double> weights,String dateRange,Connection conn) throws SQLException {
        TreeMap<String, Double> portfolioData = new TreeMap<>();
        Set<String> tickers = weights.keySet();
		TreeMap<String, HashMap<String, Double>> dateTickerPriceMap = getTickerPriceDB("'"+String.join("','", tickers)+"'",dateRange,  conn);
       
        for(String thisDate : dateTickerPriceMap.keySet()) {
        	List<Double> returns = new ArrayList<Double>();
        	HashMap<String, Double> tickerReturnMap = dateTickerPriceMap.get(thisDate);
        	for(String ticker: tickers) {
        		Double thisReturn = tickerReturnMap.get(ticker);
        		if(thisReturn != null) {
        			double thisWt = weights.get(ticker);
        			returns.add(thisReturn * thisWt);
        		}
        	}
        	
        	double totalReturn = 0;
        	for(double ret: returns) {
        		totalReturn += ret;
        	}
        	portfolioData.put(thisDate, totalReturn);
    	}
        
//        List<Double> priceList =price( portfolioData);
//        List<String> dateList = new ArrayList<>(portfolioData.keySet());
        
        
		return portfolioData;
    }
    
    public static HashMap<String, Object> getTickerInfoFromDB(String code,String startDate, String endDate) throws ClassNotFoundException, SQLException{
    	Connection con = connectLRGB();
    	Statement stmt = con.createStatement();
    	LinkedHashMap<String, Object> tickerData = new LinkedHashMap<String, Object>();
    	
    	String query = "Select company_name FROM dse_analyzer.company where code = '"+code+"'";
    	ResultSet rs = stmt.executeQuery(query);
    	while(rs.next()) {
    		tickerData.put("companyName", rs.getString("company_name"));
    	}
    	String previousClose=null;
    	//String company=null;
        previousClose =getPreviousDate(startDate,code, con,previousClose);
    	tickerData.put("previousClosePrice", previousClose);
    	//tickerData.put("companyName", company);
    	tickerData.put("ticker",code);
  
        
//    	String mkStatQuery = "SELECT ltp, high,low,YCP, total_volume FROM dse_analyzer.market_stat where code = '"+code+"';";
//    	ResultSet rs1 = stmt.executeQuery(mkStatQuery);
//    	while(rs1.next()) {
//    		tickerData.put("openPrice", rs1.getString("ltp"));
//    		tickerData.put("highPrice", rs1.getString("high"));
//    		tickerData.put("lowPrice", rs1.getString("low"));
//    		tickerData.put("yesterdayClosePrice", rs1.getString("YCP"));
//    		tickerData.put("totalVolume", rs1.getString("total_volume"));
//    	}
    	
    	String dailyPriceQuery = "SELECT date,open, close,adjusted_price, high,low,volume FROM dse_analyzer.daily_price where ticker = '"+code+"' and date between'"+startDate+"' and '"+endDate+"' order by date asc;";
    	ResultSet rs2 = stmt.executeQuery(dailyPriceQuery);
    	List<LinkedHashMap<String, String>> dailyPriceMap = new ArrayList<>();
    	Double highClosingPrice=0.0;
    	Double lowestClosingPrice=0.0;
    	Double openChartPrice= 0.0;
    	String lastColse="";
    	String tot_volume="";
    	int count=1;
    	while(rs2.next()) {
    		LinkedHashMap<String, String> priceMap = new LinkedHashMap<String, String>();
    		priceMap.put("open", rs2.getString("open"));
    		priceMap.put("high", rs2.getString("high"));
    		priceMap.put("low", rs2.getString("low"));
    		priceMap.put("close", rs2.getString("adjusted_price"));
    		priceMap.put("volume", rs2.getString("volume"));
    		priceMap.put("date", rs2.getString("date"));
    		lastColse=rs2.getString("adjusted_price");
    		tot_volume= rs2.getString("volume");
    		dailyPriceMap.add(priceMap);
    		highClosingPrice = rs2.getDouble("adjusted_price")> highClosingPrice ? rs2.getDouble("adjusted_price") :highClosingPrice ;
    		lowestClosingPrice = rs2.getDouble("adjusted_price") < lowestClosingPrice ? rs2.getDouble("adjusted_price") :lowestClosingPrice ;
    		//System.out.println(rs2.getString("date")+" "+lowestClosingPrice);
    		if(count==1) {
    			openChartPrice=rs2.getDouble("adjusted_price");
    			lowestClosingPrice=rs2.getDouble("adjusted_price");
    			highClosingPrice=rs2.getDouble("adjusted_price");
    			count++;
    		}
    		
    	}
    	LinkedHashMap<String, String> upperMapHistorical = new LinkedHashMap<String, String>();
    	upperMapHistorical.put("openPrice", openChartPrice.toString());
    	upperMapHistorical.put("highPrice", highClosingPrice.toString());
    	upperMapHistorical.put("lowPrice", lowestClosingPrice.toString());
    	upperMapHistorical.put("lastClosePrice",lastColse);
    	upperMapHistorical.put("previousClosePrice", previousClose);
    	upperMapHistorical.put("totalVolume", tot_volume);
    	
    	
    	tickerData.put("historicalUpperValue",upperMapHistorical );
		tickerData.put("openPrice", openChartPrice);
		tickerData.put("highPrice", highClosingPrice);
		tickerData.put("lowPrice", lowestClosingPrice);
		tickerData.put("lastClosePrice", lastColse);
    	tickerData.put("historical", dailyPriceMap);
		tickerData.put("yesterdayClosePrice", previousClose);
		tickerData.put("totalVolume", tot_volume);
    	
    	
// Intraday part Start
    	
    	LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = dateFormat.format(currentDate);
        boolean dataFound = false;

        // Loop until data is found
        
        if (!code.contains("DSEX") && !code.contains("DS30") && !code.contains(" NAV Index")) {
        	while (!dataFound) {     	
//              String intraDayQuery = "SELECT  MKISTAT_PUB_LAST_TRADED_PRICE, MKISTAT_TOTAL_VOLUME,MKISTAT_LM_DATE_TIME FROM dse_analyzer.mkistat_archive " +
//                             "WHERE MKISTAT_INSTRUMENT_CODE = '"+code+"' AND DATE(MKISTAT_LM_DATE_TIME) = '" + formattedDate + "' " +
//                             "ORDER BY MKISTAT_LM_DATE_TIME DESC";
              
              String intraDayQuery = " SELECT MKISTAT_OPEN_PRICE,MKISTAT_PUB_LAST_TRADED_PRICE, MKISTAT_HIGH_PRICE, MKISTAT_LOW_PRICE,MKISTAT_CLOSE_PRICE, MKISTAT_YDAY_CLOSE_PRICE, "
              		+ "MKISTAT_PUBLIC_TOTAL_VOLUME , MKISTAT_LM_DATE_TIME FROM dse_analyzer.mkistat_archive " +
                      "WHERE MKISTAT_INSTRUMENT_CODE = '"+code+"' AND DATE(MKISTAT_LM_DATE_TIME) = '" + formattedDate + "' " +
                      "ORDER BY MKISTAT_LM_DATE_TIME asc";
                     
              ResultSet rs4 = stmt.executeQuery(intraDayQuery);
              List<LinkedHashMap<String, String>> intraDay = new ArrayList<LinkedHashMap<String, String>>();
              if (rs4.next()) {
                  dataFound = true;
                  int i=1;
                  Double close=0.0;
                  while(rs4.next()) {
                  	
                  	LinkedHashMap<String, String> priceMap = new LinkedHashMap<String, String>();
//                  	priceMap.put("date",rs4.getString("MKISTAT_LM_DATE_TIME"));
//              		priceMap.put("close", rs4.getString("MKISTAT_PUB_LAST_TRADED_PRICE"));
//              		priceMap.put("volume", rs4.getString("MKISTAT_TOTAL_VOLUME"));
                  	
              		priceMap.put("open", rs4.getString("MKISTAT_OPEN_PRICE"));
              		priceMap.put("high", rs4.getString("MKISTAT_HIGH_PRICE"));
              		priceMap.put("low", rs4.getString("MKISTAT_LOW_PRICE"));
              		close = rs4.getDouble("MKISTAT_PUB_LAST_TRADED_PRICE")==0.0 ? 
              				rs4.getDouble("MKISTAT_YDAY_CLOSE_PRICE") : rs4.getDouble("MKISTAT_PUB_LAST_TRADED_PRICE");
              		priceMap.put("close", close.toString());
              		tot_volume= rs4.getString("MKISTAT_PUBLIC_TOTAL_VOLUME");
              		priceMap.put("volume", rs4.getString("MKISTAT_PUBLIC_TOTAL_VOLUME"));
              		priceMap.put("date", rs4.getString("MKISTAT_LM_DATE_TIME"));
              		lastColse=rs4.getString("MKISTAT_YDAY_CLOSE_PRICE");
              		intraDay.add(priceMap);
              		
              		highClosingPrice = close> highClosingPrice ?close :highClosingPrice ;
              		lowestClosingPrice = close < lowestClosingPrice ? close :lowestClosingPrice ;
              		
              		
              		if(i==1 && rs4.getDouble("MKISTAT_PUB_LAST_TRADED_PRICE") ==0.0 ) {
              			openChartPrice=rs4.getDouble("MKISTAT_OPEN_PRICE");
              			lowestClosingPrice=rs4.getDouble("MKISTAT_YDAY_CLOSE_PRICE");
              			highClosingPrice=rs4.getDouble("MKISTAT_YDAY_CLOSE_PRICE");
              			i++;
              		}
              		
              		if(i==1 && rs4.getDouble("MKISTAT_PUB_LAST_TRADED_PRICE") > 0.0 ) {
              			openChartPrice=rs4.getDouble("MKISTAT_OPEN_PRICE");
              			lowestClosingPrice=rs4.getDouble("MKISTAT_PUB_LAST_TRADED_PRICE");
              			highClosingPrice=rs4.getDouble("MKISTAT_PUB_LAST_TRADED_PRICE");
              			i++;
              		}
                  }
                  
                  LinkedHashMap<String, String> upperMapIntraday= new LinkedHashMap<String, String>();
                  upperMapIntraday.put("openPrice", openChartPrice.toString());
                  upperMapIntraday.put("highPrice", highClosingPrice.toString());
                  upperMapIntraday.put("lowPrice", lowestClosingPrice.toString());
                  upperMapIntraday.put("lastClosePrice",close.toString());
                  upperMapIntraday.put("previousClosePrice", lastColse);
                  upperMapIntraday.put("totalVolume", tot_volume);
                  

                  tickerData.put("IntradayUpperValue",upperMapIntraday );
                  tickerData.put("intraDay", intraDay);
                  //break;
//                Gson gson =  new GsonBuilder().setPrettyPrinting().create(); 
//                String json = gson.toJson(upperMapIntraday);
//                System.out.println(json);

              } else {
              	 LocalDate nextdate = LocalDate.parse(formattedDate, DateTimeFormatter.ISO_LOCAL_DATE);
               	LocalDate previousDate = nextdate.minusDays(1);
               	formattedDate = dateFormat.format(previousDate);
              }
          }
        }
        
        if (code.contains("DSEX") || code.contains("DS30") || code.contains(" NAV Index")) {
        	//List<LinkedHashMap<String, String>> intraDay = new ArrayList<LinkedHashMap<String, String>>();
        	//tickerData.put("intraDay", intraDay);
        	tickerData=getDSEX_DS30_Intraday(tickerData,code,con);
        }
        
    	
// Intraday part Ends 	
        con.close();
        return tickerData;
    }
    
    public static LinkedHashMap<String, Object>  getDSEX_DS30_Intraday(LinkedHashMap<String, Object> tickerData, String code, 
    		Connection con) throws SQLException {
    	String lastDate=null;
    	Double close=0.0;
    	
    	Double highClosingPrice=0.0;
    	Double lowestClosingPrice=0.0;
    	Double openChartPrice= 0.0;
    	String lastColse="";
    	String tot_volume=" ";
    	int count=1;
    	
    	List<LinkedHashMap<String, String>> intraDay = new ArrayList<LinkedHashMap<String, String>>();
    	String selectDate = "select max(date(date_time)) from dse_analyzer.idx_archive";
    	Statement stmt = con.createStatement();
    	ResultSet rs = stmt.executeQuery(selectDate);
    	while(rs.next()) {
    		lastDate=rs.getString(1);
    	}
    	
    	String select="SELECT id, capital_value, volume,date_time FROM dse_analyzer.idx_archive where id='"+code+"' "
    			+ "and date_time >='"+lastDate+" 09:30:00"+"'";
    	Statement stmt1 = con.createStatement();
    	ResultSet rs1 = stmt1.executeQuery(select);
    	
    	while(rs1.next()) {
    		LinkedHashMap<String, String> priceMap = new LinkedHashMap<String, String>();
    		priceMap.put("close", rs1.getString("capital_value"));
    		priceMap.put("date", rs1.getString("date_time"));
    		//priceMap.put("volume", rs1.getString("volume"));
    		tot_volume= rs1.getString("volume") ==null ? " ":rs1.getString("volume");
    		priceMap.put("volume", tot_volume);
    		intraDay.add(priceMap);
    		close =rs1.getDouble("capital_value");
    		
    		if(count==1) {
    			openChartPrice=close;
    			highClosingPrice=close;
    			lowestClosingPrice=close;  			
    		}else {
    			highClosingPrice= close > highClosingPrice ? close : highClosingPrice;
    			lowestClosingPrice = close < lowestClosingPrice ? close :lowestClosingPrice;
    		}
    		count++;
    	}
    	
    	String selectPrevious="SELECT id, capital_value, date_time FROM dse_analyzer.idx_archive where id='"+code+"' "
    			+ "and date_time <='"+lastDate+" 09:30:00"+"'";
    	Statement stmt2 = con.createStatement();
    	ResultSet rs2 = stmt2.executeQuery(selectPrevious);
    	while(rs2.next()) {
    		lastColse=rs2.getString("capital_value");
    	}
    	
    	LinkedHashMap<String, String> upperMapIntraday= new LinkedHashMap<String, String>();
        upperMapIntraday.put("openPrice", openChartPrice.toString());
        upperMapIntraday.put("highPrice", highClosingPrice.toString());
        upperMapIntraday.put("lowPrice", lowestClosingPrice.toString());
        upperMapIntraday.put("lastClosePrice",close.toString());
        upperMapIntraday.put("previousClosePrice", lastColse);
        upperMapIntraday.put("totalVolume", tot_volume);////no value yet
        

        tickerData.put("IntradayUpperValue",upperMapIntraday );
        tickerData.put("intraDay", intraDay);
		return tickerData;
    }
    
    public static String getPreviousDate(String currentDate, String code,Connection con, String close) throws SQLException {
    		boolean flag =false;
    		String previousDateString=currentDate;
    		LocalDate dateStart = LocalDate.parse(previousDateString);
            LocalDate dayBeforeDate = dateStart.minusDays(1);
            previousDateString = dayBeforeDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
            while(!flag) {
            	String queryYclose = "SELECT adjusted_price FROM dse_analyzer.daily_price where ticker = '"+code+"' "+ "and date ='"+previousDateString+"'";
            	
//            	String queryYclose ="SELECT c.company_name,p.ticker,p.adjusted_price FROM "
//            			+ "dse_analyzer.daily_price p inner join dse_analyzer.company c  "
//            			+ "where p.ticker = '"+code+"' and c.code='"+code+"' and p.date ='"+previousDateString+"'";
            	
                Statement stmt = con.createStatement();
             	ResultSet rss = stmt.executeQuery(queryYclose);
             	while (rss.next()) {
             		close=rss.getString("adjusted_price");
//             		name=rss.getString("company_name");
             		flag=true;
             	}
             	if( close ==null) {
             		dateStart = LocalDate.parse(previousDateString);
                    dayBeforeDate = dateStart.minusDays(1);
                    previousDateString = dayBeforeDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
             		//getPreviousDate(previousDateString,code,con,null);         	
             	}
            }        	
         	return close; 
    } 
    
    public static void getCompareChartData(String code,String startDate, String endDate) throws ClassNotFoundException, SQLException {
    	Connection con = connectLRGB();
    	Statement stmt = con.createStatement();
    	HashMap<String, Object> tickerData = new HashMap<String, Object>();
    	tickerData.put("ticker", code);
    	String query = "Select company_name FROM dse_analyzer.company where code = '"+code+"'";
    	ResultSet rs = stmt.executeQuery(query);
    	while(rs.next()) {
    		tickerData.put("companyName", rs.getString("company_name"));
    	}
    	
    	String mkStatQuery = "SELECT max(return_price),min(return_price) FROM dse_analyzer.eod_data "
    			+ "where ticker ='"+code+"' and date between '"+startDate+"' and '"+endDate+"'";
    	ResultSet rs1 = stmt.executeQuery(mkStatQuery);
    	while(rs1.next()) {
    		tickerData.put("cumulativeVolume", "");
    		tickerData.put("highClosePrice", rs1.getString(1));
    		tickerData.put("lowClosePrice", rs1.getString(2));
    		tickerData.put("priceClosePrior", "");
//    		tickerData.put("totalVolume", rs1.getString("total_volume"));
    	}
    	

    	
    	TreeMap<String, Double> returnPrice = new TreeMap<String, Double>();
    	String queryEOD = "SELECT date,return_price FROM dse_analyzer.eod_data where ticker ='"+code+"' and date between '"+startDate+"' "
    			+ "and '"+endDate+"' order by date;";
    	ResultSet rs2 = stmt.executeQuery(queryEOD);
    	while(rs2.next()) {
    		returnPrice.put(rs2.getString("date"),rs2.getDouble("return_price"));
    	}
    	
    	Collection<Double> priceList= returnPrice.values();
    	List<String> dateList = new ArrayList<>(returnPrice.keySet());
    	System.out.println(priceList);
    	
    	LinkedHashMap<String,Double> priceMap = new LinkedHashMap<String,Double>();
		int i = 0;
//		Double firstRetVal = null;
		Double initialVal = 0.0;
		DecimalFormat df = new DecimalFormat("#.##"); 
		if(priceList.size()>0) {
			int count = 0;
			for(Double dailyPriceReturn : priceList) {
				if(dailyPriceReturn!=null) {
					//Double currRetVal = dailyPriceReturn.getReturnValue();
					Double currPriceVal = dailyPriceReturn;
					if(i==0) {
						//firstRetVal = dailyPriceReturn.getReturnValue();
						priceMap.put(dateList.get(count),initialVal);
						i++;
						count++;
						continue;
					}
					initialVal = initialVal + currPriceVal;
					if(initialVal!=null) {
						initialVal = Double.valueOf(df.format(initialVal));
						priceMap.put(dateList.get(count),initialVal);
					}
				}
				count++;
			}
		}
		con.close();
		System.out.println(priceMap);
    }
    
    public static List<Map<String, String>> getStockNews(String ticker, String startDate, String endDate) throws ClassNotFoundException, SQLException, ParseException{
    	List<Map<String,String>> newsList = new ArrayList<Map<String,String>>();
    	Connection con = connectLRGBLoader();
    	Statement stmt = con.createStatement();
    	String newsQuery = "SELECT * FROM dse_analyzer_loader.MAN_archive WHERE ticker = '"+ticker+"' AND post_date_time BETWEEN '"+startDate+"' AND '"+endDate+"'"
    			+ "ORDER BY post_date_time DESC;";
    	ResultSet rs = stmt.executeQuery(newsQuery);
    	while(rs.next()) {
    		Map<String, String> newMap = new HashMap<String, String>();
    		String dateString = rs.getString("post_date_time");
    		String news = rs.getString("news");
    		
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormat.parse(dateString);
            Double unixTime = date.getTime() / 1000.00;
            
            newMap.put("title", news);
            newMap.put("pubDate", unixTime.toString());
            newsList.add(newMap);
    	}
    	con.close();
    	return newsList;
    }
    
    public static String getDate(String code) throws ClassNotFoundException, SQLException {
    	String firstDate=null;
    	Connection con = connectLRGB();
    	String query="select date from dse_analyzer.daily_price where ticker='"+code+"'  and adjusted_price is not null order by date asc limit 1";
    	Statement stmt = con.createStatement();
    	ResultSet rs = stmt.executeQuery(query);
    	while(rs.next()) {
    		firstDate=rs.getString(1);
    	}
		return firstDate;
		
    	
    }
    
    public static String getValidation(String date) throws ClassNotFoundException, SQLException {
        Connection con = connectLRGB();
        String query = "SELECT EXISTS(SELECT 1 FROM dse_analyzer.mkistat_archive WHERE date(MKISTAT_LM_DATE_TIME)='" + date + "') AS data_available;";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            if (rs.getString(1).contains("1")) {
                return date;
            } else {
                String previousDate = CommonUtils.getPreviousDatee(date);
                return getValidation(previousDate);
            }
        }
        rs.close();
        stmt.close();
        con.close();
        return null;
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
	
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		getCompareChartData("primebank", "2023-01-01", "2024-01-01");
	}
}
