package com.LRG.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.LRG.Utils.CommonUtils;
import com.LRG.Utils.StatsUtils;
import com.LRG.domain.AdjustedPrice;
import com.LRG.repository.SysConfigRepository;

@Service
public class AnalyticsService {
	
	@Autowired
	private SysConfigRepository sysConfigRepository;
	
	public static Map<String, Double> getTableChartData(List<Object> dailyPriceList, List<AdjustedPrice> dailyBMPriceList,boolean isIndex) {		
		Map<String,Double> tableDataMap = new LinkedHashMap<String,Double>();
		List<Double> returnList = new ArrayList<Double>();
		List<Double> priceList = new ArrayList<Double>();
		List<Double> bmReturnList = new ArrayList<Double>();
		List<Double> bmPriceList = new ArrayList<Double>();
		List<Double> dailyReturnWithoutAdding1List = new ArrayList<Double>();
		Double count = 0.0;
		Double countGE = 0.0;
		Double countLT = 0.0;
		Double sumUpVolume = 0.0;
		Double sumDownVolume = 0.0;
		
		if(dailyPriceList.size()>0) {
			for(Object dailyPriceReturn : dailyPriceList) {
				if(dailyPriceReturn!=null) {
//					System.out.println("Code " +dailyPriceReturn.getCode());
//					System.out.println("Date "+ dailyPriceReturn.getDate());
//					System.out.println("Price " + dailyPriceReturn.getAdjstdClose());
					LinkedHashMap<String, String> dailyPriceMap = (LinkedHashMap<String, String>) dailyPriceReturn;					
					String priceString = dailyPriceMap.get("close");
					Double price = Double.parseDouble(priceString);
					
					//Double price = dailyPriceMap.get("close");
					priceList.add(price);
				}
			}
		}
		for(int k = 0; k < priceList.size()-1; k++) {
			returnList.add(priceList.get(k+1)/priceList.get(k));
			if(priceList.get(k+1)/priceList.get(k) > 1) {
				count++;
			}
			if(priceList.get(k+1)/priceList.get(k) >= 1) {
				countGE++;
				if(k<50) {
					
					Object dailyPriceReturn = dailyPriceList.get(k);
					LinkedHashMap<String, String> dailyPriceMap = (LinkedHashMap<String, String>) dailyPriceReturn;
					String priceString = dailyPriceMap.get("volume");
					Double volume = Double.parseDouble(priceString);
					if(volume!=null) sumUpVolume = sumUpVolume + volume;
				}
			}
			if(priceList.get(k+1)/priceList.get(k) < 1) {
				countLT++;
				if(k<50) {
					
					Object dailyPriceReturn = dailyPriceList.get(k);
					LinkedHashMap<String, String> dailyPriceMap = (LinkedHashMap<String, String>) dailyPriceReturn;
					String priceString = dailyPriceMap.get("volume");
					Double volume = Double.parseDouble(priceString);
					if(volume!=null) sumDownVolume = sumDownVolume + volume;
				}
			}
		}
		
		if(dailyBMPriceList.size()>0) {
			for(AdjustedPrice dailyBMPriceReturn : dailyBMPriceList) {
				if(dailyBMPriceReturn!=null) {
					bmPriceList.add(dailyBMPriceReturn.getAdjstdClose());
				}
			}
		}
		for(int k = 0; k < bmPriceList.size()-1; k++) {
			bmReturnList.add(bmPriceList.get(k+1)/bmPriceList.get(k));
		}	
//		System.out.println(bmPriceList.size()+" "+priceList.size());
		if(bmPriceList.size()==priceList.size()) {
			Double riskFreeRate = 7.82;
			Double alpha = null;
			Double battingaverage = null;
			Double tickerReturn = (priceList.get(priceList.size()-1)/priceList.get(0))-1;
//			System.out.println("End Price"+priceList.get(priceList.size()-1));
//			System.out.println("Start Price"+priceList.get(0));
			
			double sd = StatsUtils.getStandardDeviation(CommonUtils.getDoubleArray(returnList));
			Double stdDeviation = isIndex ? Precision.round((sd * Math.sqrt(52.0))*100, 3) : Precision.round((sd * Math.sqrt(252.0))*100, 3);
			Map<String, String> regMapPort = StatsUtils.getRegressionMap(bmReturnList, returnList);			
			Double beta = (regMapPort.get("slope") != null) ? Double.parseDouble(regMapPort.get("slope")) : 0;
			Double rsquared = (regMapPort.get("rSquare") != null) ? Double.parseDouble(regMapPort.get("rSquare")) : 0;
			Double fundReturn = isIndex ? (Math.pow(priceList.get(priceList.size()-1)/priceList.get(0), 52.0/priceList.size()) - 1.0)*100 :
				(Math.pow(priceList.get(priceList.size()-1)/priceList.get(0), 252.0/priceList.size()) - 1.0)*100;
			double fundReturnAbs = fundReturn/100;
			Double bmReturn = isIndex ? (Math.pow(bmPriceList.get(bmPriceList.size()-1)/bmPriceList.get(0), 52.0/bmPriceList.size()) - 1.0)*100 :
				(Math.pow(bmPriceList.get(bmPriceList.size()-1)/bmPriceList.get(0), 252.0/bmPriceList.size()) - 1.0)*100;
			double stddeviationAbs = stdDeviation/100;
			Double indexReturn = isIndex ? (Math.pow(bmPriceList.get(bmPriceList.size()-1)/bmPriceList.get(0), 52.0/bmPriceList.size()) - 1.0)*100 :
				(Math.pow(bmPriceList.get(bmPriceList.size()-1)/bmPriceList.get(0), 252.0/bmPriceList.size()) - 1.0)*100;
			alpha = Precision.round((fundReturn - (beta*indexReturn)), 4);
			battingaverage = Precision.round(count/returnList.size(), 4);
			Double sharpRatio = Precision.round((fundReturn - Double.valueOf(riskFreeRate))/((sd * Math.sqrt(252))*100), 2);
			
			//Double annualizedReturn = StatsUtils.getAnnualizedReturn(returnList);
			Double kurt = StatsUtils.getKurtosis(returnList);
			Double skew = StatsUtils.getSkewness(returnList);
			Double calmerRatio = Precision.round(StatsUtils.getCalmerRatio(priceList, fundReturn), 2);
			Double captureRatioUp = StatsUtils.getCaptureRatioUp(bmReturnList, returnList);
			Double captureRatioDown = StatsUtils.getCaptureRatioDown(bmReturnList, returnList);
			Double informationRatio = Precision.round((fundReturn-bmReturn)/StatsUtils.getTrackingError(bmReturnList, returnList), 2);
			Double trackingError = Precision.round((StatsUtils.getTrackingError(bmReturnList, returnList)),1);
			Double downsideRisk = Precision.round(StatsUtils.getDownsideRisk(bmReturnList, returnList,riskFreeRate), 3);
			Double maxDrawDownAnnualized = Precision.round(StatsUtils.getMaxDrawdown(priceList)*100, 1);
			Double biasRatio = Precision.round(StatsUtils.getBiasRatio(returnList), 2);
			Double bestPeriod = Precision.round((Collections.max(returnList)-1)*100, 1);
			Double worstPeriod = Precision.round((Collections.min(returnList)-1)*100, 1);
			Double correlationCoefficient = Precision.round(StatsUtils.getCorrelation(returnList, bmReturnList), 4);
			Double upMktReturn = Precision.round(StatsUtils.getUpMarketReturn(bmReturnList, returnList), 1);
			Double downMktReturn = Precision.round(StatsUtils.getDownMarketReturn(bmReturnList, returnList), 1);
			Double averageFundsVal = 0.0;
			for(int i = 0; i < priceList.size(); i++) {
				averageFundsVal += priceList.get(i);
			}
			for(int m = 0; m < returnList.size(); m++) {
				dailyReturnWithoutAdding1List.add(returnList.get(m) - 1);
			}
			
			tableDataMap.put("Return",tickerReturn*100);
			tableDataMap.put("Alpha",alpha);
			tableDataMap.put("Beta",beta);
			tableDataMap.put("Annualized Return",Precision.round(fundReturn, 2));
			tableDataMap.put("Standard Deviation",stdDeviation);
			tableDataMap.put("Return/Risk",Precision.round(((fundReturnAbs/stddeviationAbs)), 3));
			tableDataMap.put("Batting Average",battingaverage);
			tableDataMap.put("Skewness",skew);
//			tableDataMap.put("Kurtosis",kurt);		
			tableDataMap.put("Max Drawdown",maxDrawDownAnnualized);
//			tableDataMap.put("Best Period",bestPeriod);
//			tableDataMap.put("Worst Period",worstPeriod);
//			tableDataMap.put("Calmer Ratio",calmerRatio);
//			tableDataMap.put("Correlation Coefficient",correlationCoefficient);	
//			tableDataMap.put("Current Drawdown", Precision.round((Collections.min(returnList)-1)*100, 4));
//			tableDataMap.put("Bias Ratio",biasRatio);
			tableDataMap.put("Up Market Return",upMktReturn!=-100 ? upMktReturn : Double.NaN);
			tableDataMap.put("Down Market Return",downMktReturn!=-100 ? downMktReturn : Double.NaN);
			tableDataMap.put("No. of Negative period", countLT);
			tableDataMap.put("No. of positive period", countGE);
			tableDataMap.put("Excess Return", Precision.round((fundReturn - indexReturn), 4));
			tableDataMap.put("Capture Ratio Up",captureRatioUp);
			tableDataMap.put("Capture Ratio Down",captureRatioDown);
			tableDataMap.put("Information Ratio",informationRatio);
			tableDataMap.put("Max drawdown Period Peak", Precision.round((Collections.max(priceList)), 2));
//			tableDataMap.put("Sterling Ratio", Precision.round((fundReturn/Math.abs(((Collections.max(priceList)-
//					Collections.min(priceList))/(averageFundsVal/priceList.size())*100)-10)), 2));
//			tableDataMap.put("Tracking Error",trackingError);
//			tableDataMap.put("Sharp Ratio",sharpRatio);
//			tableDataMap.put("RSquared",Precision.round(rsquared, 3));
//			tableDataMap.put("Variance", Precision.round((Math.pow(((sd * Math.sqrt(252.0))*100/100), 2))*100, 1));
			tableDataMap.put("Jensen Alpha", Precision.round(((fundReturn - Double.valueOf(riskFreeRate))-
					(beta*(indexReturn - Double.valueOf(riskFreeRate)))),3));
//			tableDataMap.put("Treynor", Precision.round(((fundReturn - Double.valueOf(riskFreeRate))/beta)/100, 2));
			tableDataMap.put("Downside Risk",downsideRisk);
//			tableDataMap.put("Sortino", Precision.round(((fundReturn - Double.valueOf(riskFreeRate))/downsideRisk), 1));
			tableDataMap.put("Upside/Downside Ratio", Precision.round(Double.valueOf(sumUpVolume/sumDownVolume), 2));
			
		}else {
			tableDataMap.put("Return",null);
			tableDataMap.put("Alpha",null);
			tableDataMap.put("Beta",null);
			tableDataMap.put("Annualized Return",null);
			tableDataMap.put("Standard Deviation",null);
			tableDataMap.put("Return/Risk",null);
			tableDataMap.put("Batting Average",null);
			tableDataMap.put("Skewness",null);
//			tableDataMap.put("Kurtosis",null);		
			tableDataMap.put("Max Drawdown",null);
//			tableDataMap.put("Best Period",null);
//			tableDataMap.put("Worst Period",null);
//			tableDataMap.put("Calmer Ratio",null);
//			tableDataMap.put("Correlation Coefficient",null);	
//			tableDataMap.put("Current Drawdown", null);
//			tableDataMap.put("Bias Ratio",null);
			tableDataMap.put("Up Market Return",null);
			tableDataMap.put("Down Market Return",null);
			tableDataMap.put("No. of Negative period", null);
			tableDataMap.put("No. of positive period", null);
			tableDataMap.put("Excess Return", null);
			tableDataMap.put("Capture Ratio Up",null);
			tableDataMap.put("Capture Ratio Down",null);
			tableDataMap.put("Information Ratio",null);
			tableDataMap.put("Max drawdown Period Peak", null);
//			tableDataMap.put("Sterling Ratio", null);
//			tableDataMap.put("Tracking Error",null);
//			tableDataMap.put("Sharp Ratio",null);
//			tableDataMap.put("RSquared",null);
//			tableDataMap.put("Variance", null);
			tableDataMap.put("Jensen Alpha", null);
//			tableDataMap.put("Treynor", null);
			tableDataMap.put("Downside Risk",null);
//			tableDataMap.put("Sortino", null);
			tableDataMap.put("Upside/Downside Ratio", Precision.round(Double.valueOf(sumUpVolume/sumDownVolume), 2));
		}
				
		return tableDataMap;
	}
	
	
	
	public static Map<String, Double> getTableChartData(List<Object> dailyPriceList, List<AdjustedPrice> dailyBMPriceList,boolean isIndex,
			String riskFreer, String tradingDays) {		
		Double totalTradingDays = Double.parseDouble(tradingDays);
		Map<String,Double> tableDataMap = new LinkedHashMap<String,Double>();
		List<Double> returnList = new ArrayList<Double>();
		List<Double> priceList = new ArrayList<Double>();
		List<Double> bmReturnList = new ArrayList<Double>();
		List<Double> bmPriceList = new ArrayList<Double>();
		List<Double> dailyReturnWithoutAdding1List = new ArrayList<Double>();
		Double count = 0.0;
		Double countGE = 0.0;
		Double countLT = 0.0;
		Double sumUpVolume = 0.0;
		Double sumDownVolume = 0.0;
		
		if(dailyPriceList.size()>0) {
			for(Object dailyPriceReturn : dailyPriceList) {
				if(dailyPriceReturn!=null) {
					LinkedHashMap<String, Double> dailyPriceMap = (LinkedHashMap<String, Double>) dailyPriceReturn;
					Double price = dailyPriceMap.get("close");
					//Double priceValue = Double.parseDouble(price);
					priceList.add(price);
				}
			}
		}
		for(int k = 0; k < priceList.size()-1; k++) {
			returnList.add(priceList.get(k+1)/priceList.get(k));
			if(priceList.get(k+1)/priceList.get(k) > 1) {
				count++;
			}
			if(priceList.get(k+1)/priceList.get(k) >= 1) {
				countGE++;
				if(k<50) {					
					Object dailyPriceReturn = dailyPriceList.get(k);
					LinkedHashMap<String, Double> dailyPriceMap = (LinkedHashMap<String, Double>) dailyPriceReturn;
					Double volume = dailyPriceMap.get("volume");
					
					if(volume!=null) sumUpVolume = sumUpVolume + volume;
				}
			}
			if(priceList.get(k+1)/priceList.get(k) < 1) {
				countLT++;
				if(k<50) {					
					Object dailyPriceReturn = dailyPriceList.get(k);
					LinkedHashMap<String, Double> dailyPriceMap = (LinkedHashMap<String, Double>) dailyPriceReturn;
					Double volume = dailyPriceMap.get("volume");
					if(volume!=null) sumDownVolume = sumDownVolume + volume;
				}
			}
		}
		
		if(dailyBMPriceList.size()>0) {
			for(AdjustedPrice dailyBMPriceReturn : dailyBMPriceList) {
				if(dailyBMPriceReturn!=null) {
					bmPriceList.add(dailyBMPriceReturn.getAdjstdClose());
				}
			}
		}
		for(int k = 0; k < bmPriceList.size()-1; k++) {
			bmReturnList.add(bmPriceList.get(k+1)/bmPriceList.get(k));
		}	
		
		if(bmPriceList.size()==priceList.size()) {
			Double riskFreeRate = new Double(riskFreer);
			Double alpha = null;
			Double battingaverage = null;
			Double tickerReturn = (priceList.get(priceList.size()-1)/priceList.get(0))-1;
//			System.out.println("End Price"+priceList.get(priceList.size()-1));
//			System.out.println("Start Price"+priceList.get(0));
			
			double sd = StatsUtils.getStandardDeviation(CommonUtils.getDoubleArray(returnList));
			Double stdDeviation =Precision.round((sd * Math.sqrt(totalTradingDays))*100, 3) ;
			Map<String, String> regMapPort = StatsUtils.getRegressionMap(bmReturnList, returnList);			
			Double beta = (regMapPort.get("slope") != null) ? Double.parseDouble(regMapPort.get("slope")) : 0;
			Double rsquared = (regMapPort.get("rSquare") != null) ? Double.parseDouble(regMapPort.get("rSquare")) : 0;
			Double fundReturn = (Math.pow(priceList.get(priceList.size()-1)/priceList.get(0), totalTradingDays/priceList.size()) - 1.0)*100 ;
			double fundReturnAbs = fundReturn/100;
			Double bmReturn = (Math.pow(bmPriceList.get(bmPriceList.size()-1)/bmPriceList.get(0), totalTradingDays/bmPriceList.size()) - 1.0)*100;
			double stddeviationAbs = stdDeviation/100;
			Double indexReturn = (Math.pow(bmPriceList.get(bmPriceList.size()-1)/bmPriceList.get(0), totalTradingDays/bmPriceList.size()) - 1.0)*100 ;
			alpha = Precision.round((fundReturn - (beta*indexReturn)), 4);
			battingaverage = Precision.round(count/returnList.size(), 4);
			Double sharpRatio = Precision.round((fundReturn - Double.valueOf(riskFreeRate))/((sd * Math.sqrt(252))*100), 2);
			
			//Double annualizedReturn = StatsUtils.getAnnualizedReturn(returnList);
			Double kurt = StatsUtils.getKurtosis(returnList);
			Double skew = StatsUtils.getSkewness(returnList);
			Double calmerRatio = Precision.round(StatsUtils.getCalmerRatio(priceList, fundReturn), 2);
			Double captureRatioUp = StatsUtils.getCaptureRatioUp(bmReturnList, returnList);
			Double captureRatioDown = StatsUtils.getCaptureRatioDown(bmReturnList, returnList);
			Double informationRatio = Precision.round((fundReturn-bmReturn)/StatsUtils.getTrackingError(bmReturnList, returnList), 2);
			Double trackingError = Precision.round((StatsUtils.getTrackingError(bmReturnList, returnList)),1);
			Double downsideRisk = Precision.round(StatsUtils.getDownsideRisk(bmReturnList, returnList,riskFreeRate), 3);
			Double maxDrawDownAnnualized = Precision.round(StatsUtils.getMaxDrawdown(priceList)*100, 1);
			Double biasRatio = Precision.round(StatsUtils.getBiasRatio(returnList), 2);
			Double bestPeriod = Precision.round((Collections.max(returnList)-1)*100, 1);
			Double worstPeriod = Precision.round((Collections.min(returnList)-1)*100, 1);
			Double correlationCoefficient = Precision.round(StatsUtils.getCorrelation(returnList, bmReturnList), 4);
			Double upMktReturn = Precision.round(StatsUtils.getUpMarketReturn(bmReturnList, returnList), 1);
			Double downMktReturn = Precision.round(StatsUtils.getDownMarketReturn(bmReturnList, returnList), 1);
			Double averageFundsVal = 0.0;
			for(int i = 0; i < priceList.size(); i++) {
				averageFundsVal += priceList.get(i);
			}
			for(int m = 0; m < returnList.size(); m++) {
				dailyReturnWithoutAdding1List.add(returnList.get(m) - 1);
			}
			
			tableDataMap.put("Return",tickerReturn*100);
			tableDataMap.put("Alpha",alpha);
			tableDataMap.put("Beta",beta);
			tableDataMap.put("Annualized Return",Precision.round(fundReturn, 1));
			tableDataMap.put("Standard Deviation",stdDeviation);
			tableDataMap.put("Return/Risk",Precision.round(((fundReturnAbs/stddeviationAbs)), 3));
			tableDataMap.put("Batting Average",battingaverage);
			tableDataMap.put("Skewness",skew);
//			tableDataMap.put("Kurtosis",kurt);		
			tableDataMap.put("Max Drawdown",maxDrawDownAnnualized);
//			tableDataMap.put("Best Period",bestPeriod);
//			tableDataMap.put("Worst Period",worstPeriod);
//			tableDataMap.put("Calmer Ratio",calmerRatio);
//			tableDataMap.put("Correlation Coefficient",correlationCoefficient);	
//			tableDataMap.put("Current Drawdown", Precision.round((Collections.min(returnList)-1)*100, 4));
//			tableDataMap.put("Bias Ratio",biasRatio);
			tableDataMap.put("Up Market Return",upMktReturn!=-100 ? upMktReturn : Double.NaN);
			tableDataMap.put("Down Market Return",downMktReturn!=-100 ? downMktReturn : Double.NaN);
			tableDataMap.put("No. of Negative period", countLT);
			tableDataMap.put("No. of positive period", countGE);
			tableDataMap.put("Excess Return", Precision.round((fundReturn - indexReturn), 4));
			tableDataMap.put("Capture Ratio Up",captureRatioUp);
			tableDataMap.put("Capture Ratio Down",captureRatioDown);
			tableDataMap.put("Information Ratio",informationRatio);
			tableDataMap.put("Max drawdown Period Peak", Precision.round((Collections.max(priceList)), 2));
//			tableDataMap.put("Sterling Ratio", Precision.round((fundReturn/Math.abs(((Collections.max(priceList)-
//					Collections.min(priceList))/(averageFundsVal/priceList.size())*100)-10)), 2));
//			tableDataMap.put("Tracking Error",trackingError);
//			tableDataMap.put("Sharp Ratio",sharpRatio);
//			tableDataMap.put("RSquared",Precision.round(rsquared, 3));
//			tableDataMap.put("Variance", Precision.round((Math.pow(((sd * Math.sqrt(252.0))*100/100), 2))*100, 1));
			tableDataMap.put("Jensen Alpha", Precision.round(((fundReturn - Double.valueOf(riskFreeRate))-
					(beta*(indexReturn - Double.valueOf(riskFreeRate)))),3));
//			tableDataMap.put("Treynor", Precision.round(((fundReturn - Double.valueOf(riskFreeRate))/beta)/100, 2));
			tableDataMap.put("Downside Risk",downsideRisk);
//			tableDataMap.put("Sortino", Precision.round(((fundReturn - Double.valueOf(riskFreeRate))/downsideRisk), 1));
			tableDataMap.put("Upside/Downside Ratio", Precision.round(Double.valueOf(sumUpVolume/sumDownVolume), 2));
			
		}else {
			tableDataMap.put("Return",null);
			tableDataMap.put("Alpha",null);
			tableDataMap.put("Beta",null);
			tableDataMap.put("Annualized Return",null);
			tableDataMap.put("Standard Deviation",null);
			tableDataMap.put("Return/Risk",null);
			tableDataMap.put("Batting Average",null);
			tableDataMap.put("Skewness",null);
//			tableDataMap.put("Kurtosis",null);		
			tableDataMap.put("Max Drawdown",null);
//			tableDataMap.put("Best Period",null);
//			tableDataMap.put("Worst Period",null);
//			tableDataMap.put("Calmer Ratio",null);
//			tableDataMap.put("Correlation Coefficient",null);	
//			tableDataMap.put("Current Drawdown", null);
//			tableDataMap.put("Bias Ratio",null);
			tableDataMap.put("Up Market Return",null);
			tableDataMap.put("Down Market Return",null);
			tableDataMap.put("No. of Negative period", null);
			tableDataMap.put("No. of positive period", null);
			tableDataMap.put("Excess Return", null);
			tableDataMap.put("Capture Ratio Up",null);
			tableDataMap.put("Capture Ratio Down",null);
			tableDataMap.put("Information Ratio",null);
			tableDataMap.put("Max drawdown Period Peak", null);
//			tableDataMap.put("Sterling Ratio", null);
//			tableDataMap.put("Tracking Error",null);
//			tableDataMap.put("Sharp Ratio",null);
//			tableDataMap.put("RSquared",null);
//			tableDataMap.put("Variance", null);
			tableDataMap.put("Jensen Alpha", null);
//			tableDataMap.put("Treynor", null);
			tableDataMap.put("Downside Risk",null);
//			tableDataMap.put("Sortino", null);
			tableDataMap.put("Upside/Downside Ratio", Precision.round(Double.valueOf(sumUpVolume/sumDownVolume), 2));
		}
				
		return tableDataMap;
	}
	
	public static List<Map<String, Object>> getTickerWeightsFromDB(String auth, String watchlistId, Connection conn) throws SQLException {
        
        List<Map<String, Object>> weightList = new ArrayList<Map<String, Object>>();
        String id = getUserId(auth);

        Statement stmt = conn.createStatement();
		String query = "SELECT * FROM dse_analyzer.bizz_watchlist where userID = "+id+" and watchlist_id = "+watchlistId+";";
       
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
        	Map<String, Object> weights = new HashMap<>();
            String ticker = rs.getString("ticker");
            Double weight = rs.getDouble("weight")/100.00;
            weights.put("ticker", ticker);
            weights.put("weight", weight);
            weightList.add(weights);
        }
        
        return weightList;
    }
	
    public static List<Map<String, Object>> getWeightedData(String auth, String watchlistId) throws ClassNotFoundException, SQLException {
    	Connection con = connectLRGB();
    	List<Map<String, Object>> weights = getTickerWeightsFromDB(auth,watchlistId, con);
//    	System.out.println(weights);
		con.close();
		return weights;

    }
    
	public static String getUserId(String auth) {
		String token = null;
		if(auth != null && auth.startsWith("Bearer ")) {
			token = auth.split("-")[1];
			}
		return token;
		
	}

	
	public static Connection connectLRGB() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://192.168.100.12:3306/dse_analyzer?rewriteBatchedStatements=true", "lrgb_loader",
		"developer4");
//		Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/dse_analyzer?rewriteBatchedStatements=true", "lrgb_loader",
				//"developer4");
		con.setAutoCommit(true);
		return con;
	}

}
