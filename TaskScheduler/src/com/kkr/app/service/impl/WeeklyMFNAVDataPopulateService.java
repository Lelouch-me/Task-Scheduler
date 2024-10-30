package com.kkr.app.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kkr.app.service.IWeeklyMFNAVDataPopulateService;
import com.kkr.common.util.CommonUtils;

public class WeeklyMFNAVDataPopulateService implements IWeeklyMFNAVDataPopulateService{

	private static String select_news_query = "SELECT ticker, news FROM dse_analyzer_loader.MAN_archive "
			+ "where news like '%the Fund has reported Net Asset Value (NAV)%' and DATE(post_date_time) = DATE(NOW())";
	private static String insert_query = "insert into dse_analyzer_loader.temp_weekly_nav(code,nav,date) values(?,?,?)";
//	private static PreparedStatement pst = null;
//	private static Connection conLocal = null;
	private static PreparedStatement pstProd = null;
	private static Connection conProd = null;
	private static List<String> totalMFTickerList = new ArrayList<String>();
	private static Set<String> totalNewsTickerList = new HashSet<String>();
	private static Set<String> patternMatchedTickerList = new HashSet<String>();
	private static Set<String> failedTickerList = new HashSet<String>();
	private static Set<String> successfulTickerList = new HashSet<String>();
	
	
	public boolean populateNavForMutualFund() {
		boolean status = false;
		try {
//			conLocal = CommonUtils.connectMainDB();
			conProd = CommonUtils.connectDC();
//			pst = conLocal.prepareStatement(insert_query);
			pstProd = conProd.prepareStatement(insert_query);
			Statement stmt = conProd.createStatement();
			totalMFTickerList = CommonUtils.getMFTickerList(conProd);
			
			String ticker = "";;
			String news = "";
			Double navValue = null;
			String nav;
			String date_string = LocalDate.now().minusDays(3).toString();
			
			LocalDate date = LocalDate.now();
			if(date.getDayOfWeek().name().equals("SUNDAY")) {
				date = getModifiedDate(date,3);
			}else if(date.getDayOfWeek().name().equals("MONDAY")) {
				date = getModifiedDate(date,4);
			}
			
			date_string = date.toString();
					
			ResultSet rs= stmt.executeQuery(select_news_query);
			while(rs.next()) {
				try {
					ticker=rs.getString(1);
					news=rs.getString(2);
					totalNewsTickerList.add(ticker);
					
					if(news.contains("On the close of operation on") && news.contains("(NAV)")) {
						patternMatchedTickerList.add(ticker);
						String firstSection = news.split("of Tk.")[1].trim();
						nav = firstSection.split(" ")[0];
						navValue = new Double(nav.replaceAll("[^a-zA-Z0-9-.]", ""));
							
//						pst.setString(1,ticker);	
//						pst.setDouble(2,navValue);
//						pst.setString(3,date_string);	
//		
//						pst.execute();
						
						pstProd.setString(1,ticker);	
						pstProd.setDouble(2,navValue);
						pstProd.setString(3,date_string);	
		
						pstProd.execute();
						successfulTickerList.add(ticker);
					}
				} catch(Exception e) {
					failedTickerList.add(ticker);
				}
			}
			
			sendMail();
			
			totalNewsTickerList.clear();
			failedTickerList.clear();
			successfulTickerList.clear();
			patternMatchedTickerList.clear();
			totalMFTickerList.clear();
			
			status = true;
//			conLocal.close();
			pstProd.close();
		} catch(Exception e) {
			System.out.println(e);
		}
		return status;
	}
	
	private LocalDate getModifiedDate(LocalDate date,int daysTominus) throws Exception {
		LocalDate modified_date = date.minusDays(daysTominus);
		if(CommonUtils.getHolidaysList().contains(modified_date)) {
			modified_date = modified_date.minusDays(1);
			if(CommonUtils.getHolidaysList().contains(modified_date)) {
				modified_date = modified_date.minusDays(1);				
			}
		}
		
		return modified_date;
	}
	
	private void sendMail() {
		
		ArrayList<String> dataMissingTickerList = new ArrayList<String>();
		for(String ticker : totalMFTickerList) {
			if(totalNewsTickerList.contains(ticker))
				continue;
			
			dataMissingTickerList.add(ticker);
		}
		
		ArrayList<String> patternMismatchTickerList = new ArrayList<String>();
		for(String ticker : totalNewsTickerList) {
			if(patternMatchedTickerList.contains(ticker))
				continue;
			
			patternMismatchTickerList.add(ticker);
		}
		
		try {
			String mailBody = "Weekly NAV Data Updater Task completed successfully.\n\n"
					+"Total MF Tickers Found : " + (totalMFTickerList.size()!=0 ? totalMFTickerList.size() :"NA") +  ".\n"
					+"Total Tickers Processed : " + (totalNewsTickerList.size()!=0 ? totalNewsTickerList.size() :"NA") +  ".\n"		
					+"Total Tickers succesfully processed : " + (successfulTickerList.size()!=0 ? successfulTickerList.size() : "NA") + ".\n"				
					+"Total failed tickers : " + (failedTickerList.size()!=0 ? failedTickerList.size() : "NA")+ ".\n\n"				
					+"Failed to insert data for following tickers:\n"
					+failedTickerList+"\n\n"
					+"Ticker Contains NAV data but didn't get processed due to pattern mismatch:\n"
					+patternMismatchTickerList+"\n\n\n"
					+"Data not found for ticker :\n"
					+dataMissingTickerList+"\n\n\n"
					+"Please manually insert data for the failed tickers.";
			
			CommonUtils.sendMail(null,mailBody,"Weekly NAV Updater Details");
		} catch(Exception e) {
			System.out.println("Exception while sending wwekly nav mail due to "+e);
		}
	}
//	public static void main(String[] args) {
//		WeeklyMFNAVDataPopulateService ss = new WeeklyMFNAVDataPopulateService();
//		ss.populateNavForMutualFund();
//	}
}
