package com.kkr.app.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.kkr.app.service.IMarketMoverDataCalculateService;
import com.kkr.common.util.CommonUtils;

public class MarketMoverDataCalculateService implements IMarketMoverDataCalculateService{
	static List<String> executedTickers = new ArrayList<String>();
	static List<String> failedTickers = new ArrayList<String>();
	static List<String> contributionTickers = new ArrayList<String>();
	static String time = "";
	static DecimalFormat df = new DecimalFormat("#.##"); 
	static String select_query = "select MKISTAT_INSTRUMENT_CODE,MKISTAT_PUB_LAST_TRADED_PRICE,MKISTAT_YDAY_CLOSE_PRICE,MKISTAT_TOTAL_VALUE,"
			+ "MKISTAT_SPOT_LAST_TRADED_PRICE from mdsdata.MKISTAT where MKISTAT_QUOTE_BASES not in('A-TB','A-CB','A-DB','N-CB','A-GOVDBT')";
	static String idx_query = "select IDX_CAPITAL_VALUE from mdsdata.IDX where IDX_INDEX_ID='DSEX' order by IDX_DATE_TIME desc limit 1";
	
//	public static void main(String args[]) {
//		MarketMoverDataCalculateService md = new MarketMoverDataCalculateService();
//		md.calculateMoversData();
//	}
	
	@Override
	public void calculateMoversData() {
		try{
			double idx_value = 0.0;
	  		Connection conDSE = CommonUtils.connectDSE();
//	  	    Connection conLocal = CommonUtils.connectMainDB();
	  	    Connection conProd = CommonUtils.connectDC();
	  			
	  	    Statement stDSE = conDSE.createStatement();
	  	    ResultSet rs = stDSE.executeQuery(select_query);
	  			
	  	    Statement stidx = conDSE.createStatement();
	  	    ResultSet rsidx = stidx.executeQuery(idx_query);
	  	      
	  	    while(rsidx.next()) {
	  	    	idx_value = rsidx.getDouble(1);
	  	    }
	  			
	  	    String insert_query = "insert ignore into market_data(code,price_change,price_change_percent,marketcap,last_mcap,value,"
	  	    		+ "dvd_yield,one_year_price_return) values(?,?,?,?,?,?,?,?)";
//	  	    PreparedStatement pst = conLocal.prepareStatement(insert_query);
	  	    PreparedStatement pstProd = conProd.prepareStatement(insert_query);
	  			
	  	    double sumMcap = 0.0;
	  	    boolean status = false;
	  	    String ticker = "";
	  	    while(rs.next()) {
	  	    	if(rs.getString(1).equals("IICICL")) continue;
	  	    	try {
	  	    		status = true;
	  	    		double outShares = 0.0;         
	      	    	String query = "select out_shares from dse_analyzer.daily_company_info where code ='"+rs.getString(1)+"'";
	      	    	Statement s = conProd.createStatement();
	      	    	ResultSet r = s.executeQuery(query);
	      	    	while(r.next()) {
	      	    		outShares = r.getDouble(1);
	      	    	}
	      	    	  
	      	    	if(outShares==0.0) {
	      	  			String query1 = "select out_shares from dse.companies where ticker ='"+rs.getString(1)+"' order by date desc limit 1";
	      	  			ResultSet rs1 = s.executeQuery(query1);
	      	  			while(rs1.next()) {
	      	  				outShares = rs1.getDouble(1);
	      	  			}
	      	  		}
	      	    	ticker = rs.getString(1);
	      	    	double change;
	      	    	double marketcap;
	      	    	if(rs.getDouble(2)!=0) {
	      	    		change = rs.getDouble(2) - rs.getDouble(3);
	      	    		marketcap = rs.getDouble(2) * outShares;
	      	    	}else {
	      	    		change = rs.getDouble(5) - rs.getDouble(3);
	      	    		if(rs.getDouble(5)!=0) {
	      	    			marketcap = rs.getDouble(5) * outShares;
	      	    		}else {
	      	    			marketcap = rs.getDouble(3) * outShares;
	      	    		}
	      	    	}

	      	    	Double changePercent = rs.getDouble(3)!=0 ? (change / rs.getDouble(3)) * 100 : null;
	      	    	change = Double.valueOf(df.format(change));
	      	    	changePercent = changePercent!=null ? Double.valueOf(df.format(changePercent)) : null;
	      			
	      	    	sumMcap += marketcap;
	      	    	  
	      	    	double lastMcap = rs.getDouble(3) * outShares;
	      				
	      	    	String dvd_query = "select annual_cash_div,year from (select year,sum(cash_div) as annual_cash_div "
	      	    			+ "from dse_analyzer.details_dividend_info where code='"+rs.getString(1)+"' and "
	      	    					+ "year is not null group by year) a where year>=2020 order by year desc limit 1";
	      	    	  
	      	    	double cd = 0;
	      	    	Statement dvdst = conProd.createStatement();
	      	    	ResultSet dvdrs = dvdst.executeQuery(dvd_query);
	      	    	while(dvdrs.next()) {
	      	    		cd = dvdrs.getDouble(1);
	      	    	}	        
	      	    	double dvd_yield = rs.getDouble(2)!=0 ? cd/rs.getDouble(2) : 0;
	      	    	if(dvd_yield==0) {
	      	    		dvd_yield = rs.getDouble(5)!=0 ? cd/rs.getDouble(5) : 0;
	      	    	}
	      	    	if(dvd_yield==0) {
	      	    		dvd_yield = rs.getDouble(3)!=0 ? cd/rs.getDouble(3) : 0;
	      	    	}
	      	    	  
	      	    	double l_price = 0;
	      	    	LocalDate date = LocalDate.now();
	      	    	date = date.minusYears(1);  
	      	    	String oypr_query = "select close from dse.daily_price where ticker='"+rs.getString(1)+"' and date>='"+date.toString()+"' limit 1";
	      	    	ResultSet roypr = s.executeQuery(oypr_query);
	      	    	while(roypr.next()) {
	      	    		l_price = roypr.getDouble(1);
	      	    	}
	      	    	  
	      	    	double oypr = l_price!=0 ? ((rs.getDouble(2)/l_price) - 1) * 100 : 0;
	      	    	if(oypr==-100.0) {
	      	    		oypr = l_price!=0 ? ((rs.getDouble(5)/l_price) - 1) * 100 : 0;
	      	    	}
	      	    	if(oypr==-100.0) {
	      	    		oypr = l_price!=0 ? ((rs.getDouble(3)/l_price) - 1) * 100 : 0;
	      	    	}
	      	    	  
//	      	    	pst.setString(1, rs.getString(1));
//	      	    	pst.setDouble(2,change);
//	      	    	if(changePercent!=null) {
//	      	    		pst.setDouble(3,changePercent);
//	      	    	}else {
//	      	    		pst.setDouble(3,java.sql.Types.NULL);
//	      	    	}
//	      	    	pst.setDouble(4,marketcap);
//	      	    	pst.setDouble(5,lastMcap);
//	      	    	pst.setDouble(6,rs.getDouble(4));
//	      	    	  
//	      	    	if(dvd_yield!=0) {
//	      	    		pst.setDouble(7,dvd_yield);
//	      	    	}else {
//	      	    		pst.setNull(7,java.sql.Types.NULL);
//	      	    	}
//	      	    	  
//	      	    	if(oypr!=0) {
//	      	    		pst.setDouble(8,oypr);
//	      	    	}else {
//	      	    		pst.setNull(8,java.sql.Types.NULL);
//	      	    	}
//	      	    	  
//	      	    	pst.addBatch();
	      	    	
	      	    	
	      	    	
	      	    	pstProd.setString(1, rs.getString(1));
	      	    	pstProd.setDouble(2,change);
	      	    	if(changePercent!=null) {
	      	    		pstProd.setDouble(3,changePercent);
	      	    	}else {
	      	    		pstProd.setDouble(3,java.sql.Types.NULL);
	      	    	}
	      	    	pstProd.setDouble(4,marketcap);
	      	    	pstProd.setDouble(5,lastMcap);
	      	    	pstProd.setDouble(6,rs.getDouble(4));
	      	    	  
	      	    	if(dvd_yield!=0) {
	      	    		pstProd.setDouble(7,dvd_yield);
	      	    	}else {
	      	    		pstProd.setNull(7,java.sql.Types.NULL);
	      	    	}
	      	    	  
	      	    	if(oypr!=0) {
	      	    		pstProd.setDouble(8,oypr);
	      	    	}else {
	      	    		pstProd.setNull(8,java.sql.Types.NULL);
	      	    	}
	      	    	  
	      	    	pstProd.addBatch();
	      	    	executedTickers.add(ticker);
	  	    	}catch(Exception ex) {
	  	    		failedTickers.add(ticker);
	  	    		System.out.println(ticker);
	  	    		ex.printStackTrace();
	  	    	}
	  	    }
	  	    if(status) {
//	  	    	CommonUtils.truncateTable(conLocal,"market_data");
	  	    	CommonUtils.truncateTable(conProd,"market_data");
	  	    }
	  			
//	  	    pst.executeBatch();		
	  	    pstProd.executeBatch();		
	  		
	  	    Statement stlocal = conProd.createStatement();
	  	    ResultSet rslocal = stlocal.executeQuery("select code,marketcap,price_change_percent from market_data");
	  	    while(rslocal.next()) {
	  	    	double weight = rslocal.getDouble(2) / sumMcap ;
	  	    	double contribution = 0;
	  	    	if(rslocal.getDouble(3)!=0) {
	  	    		contribution = weight * rslocal.getDouble(3);
	  	    	}
	  	    	  
	  	    	double points = (contribution * idx_value) / 100;
	  	    	if(contribution!=0) {
	  	    		String update_query = "update market_data set contribution = "+contribution+",points="+points+" where code='"+rslocal.getString(1)+"'";
	      				
//	      	    	Statement stmt = conLocal.createStatement();
	      	    	Statement stmtProd = conProd.createStatement();
	      	    	
//	      	    	stmt.executeUpdate(update_query);
	      	    	stmtProd.executeUpdate(update_query);
	      	    	contributionTickers.add(rslocal.getString(1));
	  	    	}
	  	    }
	  			
	  	    conDSE.close();
//	  	    conLocal.close();
	  	    conProd.close();
	  	   
	  	    LocalTime currentTime = LocalTime.now();
			LocalTime range1Start = LocalTime.of(10, 5);
			LocalTime range1End = LocalTime.of(10, 8);
			LocalTime range2Start = LocalTime.of(14, 20);
			LocalTime range2End = LocalTime.of(14, 23);
			int count = 0;
			if ((currentTime.isAfter(range1Start) && currentTime.isBefore(range1End))
			        || (currentTime.isAfter(range2Start) && currentTime.isBefore(range2End))) {
			    time = currentTime.toString();
			    if(count == 0) {
			    	sendMail();
			    	count++;
			    }
			}
	  	   
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void sendMail() {
		int totalTickers = executedTickers.size() + failedTickers.size();
		try {
			String mailBody = "Market Mover Data Calculation Task completed successfully.\n\n"
					+"Task Execution Time : " + time +".\n"
					+"Total Tickers Found : " + (totalTickers!=0 ? totalTickers :"NA") +  ".\n"	
					+"Total Tickers succesfully processed : " + (executedTickers.size()!=0 ? executedTickers.size() : "NA") + ".\n"				
					+"Total Updated Contribution tickers : " + (contributionTickers.size()!=0 ? contributionTickers.size() : "NA")+ ".\n"
					+"Total failed tickers : " + (failedTickers.size()!=0 ? failedTickers.size() : "NA")+ ".\n"
					+"Failed to insert data for following tickers:\n"
					+failedTickers+"\n\n";
			
			CommonUtils.sendMail(null,mailBody,"Market Mover Data Calculation");
		} catch(Exception e) {
			System.out.println("Exception while sending wwekly nav mail due to "+e);
		}
	}
}
