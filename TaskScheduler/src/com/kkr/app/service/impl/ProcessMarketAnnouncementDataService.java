package com.kkr.app.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kkr.app.service.IProcessMarketAnnouncementDataService;
import com.kkr.app.task.MarketAnnouncementDataProcessTask;
import com.kkr.common.util.CommonUtils;

public class ProcessMarketAnnouncementDataService implements IProcessMarketAnnouncementDataService{

	private static String man_prefix_query = "select distinct(MAN_ANNOUNCEMENT_PREFIX) from mdsdata.MAN where MAN_ANNOUNCEMENT_PREFIX not in ('EXCH','REGL')";
	private static String insert_query = "insert ignore into dse_analyzer_loader.temp_eps_details(code,post_date,year,period,eps) values(?,?,?,?,?)";
	private static String temp_nav_query = "insert ignore into dse_analyzer_loader.temp_nav(code,nav,date) values(?,?,?)";
	private static final String epsForOthers = "EPS";
	private static final String epuForMF = "EPU";
//	private static final String navForMF = "unit at market price";
	private static final String navForOthers = "share";
	private static final String[] quarterDetails = {"January-March","April-June","July-September","October-December"};
	
//	private static PreparedStatement pst = null;
//	private static PreparedStatement pst1 = null;
	
	private static PreparedStatement pst2 = null;
	private static PreparedStatement pst3 = null;
	private Connection con = null;
//	private static Connection conLocal = null;
	private static Connection conProd = null;
	
	private static Set<String> epsFailedTickerList = new HashSet<String>();
	private static Set<String> epsSuccessfulTickerList = new HashSet<String>();
	private static Set<String> newsContainsEPSTickerList = new HashSet<String>();
	private static Set<String> epsQuarterTickerList = new HashSet<String>();
	private static Set<String> epsAnnualTickerListType1 = new HashSet<String>();
	private static Set<String> epsAnnualTickerListType2 = new HashSet<String>();
	
	private static Set<String> navFailedTickerList = new HashSet<String>();
	private static Set<String> navSuccessfulTickerList = new HashSet<String>();		
	private static Set<String> newsContainsNAVTickerList = new HashSet<String>();
	private static Set<String> tempNewsContainsNAVTickerList = new HashSet<String>();
	private static Set<String> navPatternMatchedTickerList = new HashSet<String>();
	
	private static Set<String> totalNewsTickerList = new HashSet<String>();
	
	public static void main(String args[]) throws IOException {
		MarketAnnouncementDataProcessTask.file_Writer = CommonUtils.prepareOutputFile("MarketAnnouncementDataProcessTask");
//		CommonUtils.sendMail(null,"test","Manual EPS Updater Details");
//		new ProcessMarketAnnouncementDataService().processMANData();
//		String news = "The Board of Directors has recommended No dividend for the year ended June 30, 2022. Date of AGM: 21.12.2022, Time: 6:00 PM, "
//				+ "Venue: InterContinental, Dhaka. Record date: 30.11.2022. The Company has also reported EPS of Tk. (11.35), "
//				+ "NAV per share with revaluation of Tk. 243.04, NAV per share without revaluation of Tk. (36.58) and NOCFPS of Tk. (8.06) for the year "
//				+ "ended June 30, 2022 as against Tk. (18.47), Tk. (25.24), Tk. (25.24) and Tk. (9.60) respectively for the same period of the previous year.";
//		String ticker = "BDSERVICE";
//		String dateTime= "2022-11-10";
//		try {
//			conLocal = CommonUtils.connectMainDB();
//			pst = conLocal.prepareStatement(insert_query);
//			pst1 = conLocal.prepareStatement(update_query);
//			new ProcessMarketAnnouncementDataService().processMANString(ticker,dateTime,news,ticker.contains("MF") ? epuForMF : epsForOthers,
//					ticker.contains("MF") ? navForMF : navForOthers);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String news = "(Q2 Un-audited): EPS was Tk. (0.37) for October-December 2022 as against Tk. (0.38) for October-December 2021; EPS was Tk. (0.82) "
				+ "for July-December 2022 as against Tk. (0.86) for July-December 2021. NOCFPS was Tk. 0.37 for July-December 2022 as against Tk. (1.45) for "
				+ "July-December 2021. NAV per share (with revaluation) was Tk. 22.70 as on December 31, 2022 and Tk. 23.56 as on June 30, 2022. (cont.)";
//		new ProcessMarketAnnouncementDataService().processNavData("HAKKANIPUL",news,navForOthers);
//		new ProcessMarketAnnouncementDataService().processEPSData("NPOLYMER","2023-01-29 09:35:10",news,epsForOthers);
	}
	
	public boolean processMANData() {
		boolean status = false;
		try {
			con = CommonUtils.connectDSE();
//			conLocal = CommonUtils.connectMainDB();
			conProd = CommonUtils.connectDC();
//			pst = conLocal.prepareStatement(insert_query);
//			pst1 = conLocal.prepareStatement(temp_nav_query);
			pst2 = conProd.prepareStatement(insert_query);
			pst3 = conProd.prepareStatement(temp_nav_query);
			Statement man_prefix_st = con.createStatement();
			ResultSet man_prefix_rs = man_prefix_st.executeQuery(man_prefix_query);
			
			List<String> tickerList = CommonUtils.getTickerList(conProd);
			List<String> mfTickerList = CommonUtils.getMFTickerList(conProd);
					
			while(man_prefix_rs.next()) {
				String ticker = man_prefix_rs.getString(1);
				if(!tickerList.contains(ticker)) continue;
				totalNewsTickerList.add(ticker);
				String man_query = "select MAN_ANNOUNCEMENT_DATE_TIME,MAN_ANNOUNCEMENT from mdsdata.MAN where MAN_ANNOUNCEMENT_PREFIX = '"+ticker+"'";
				Statement man_st = con.createStatement();
				ResultSet man_rs = man_st.executeQuery(man_query);
				String news = "";
				String dateTime = "";
				while(man_rs.next()) {					
					dateTime = man_rs.getString(1);
					String tempNews = man_rs.getString(2);					
					if(tempNews.contains("NAV") && !mfTickerList.contains(ticker)) {
						tempNewsContainsNAVTickerList.add(ticker);
						processNavData(ticker,tempNews,navForOthers);
					}
						
					if(tempNews.contains("Continuation news of "+ticker)) {
						news += " " + man_rs.getString(2);
					}else {
						news = tempNews;
					}
					if(news.toLowerCase().contains("(cont.") && !news.toLowerCase().contains("(end)")) continue;					
					
					processEPSData(ticker,dateTime,news,mfTickerList.contains(ticker) ? epuForMF : epsForOthers);
					news = "";
				}
			}
			
			if(newsContainsEPSTickerList.size()>0 || newsContainsNAVTickerList.size()>0) {
				sendMail();
			}
			
			totalNewsTickerList.clear();
			
			epsFailedTickerList.clear();
			epsSuccessfulTickerList.clear();
			newsContainsEPSTickerList.clear();
			epsQuarterTickerList.clear();
			epsAnnualTickerListType1.clear();
			epsAnnualTickerListType2.clear();
			
			navFailedTickerList.clear();
			navSuccessfulTickerList.clear();
			tempNewsContainsNAVTickerList.clear();
			newsContainsNAVTickerList.clear();
			navPatternMatchedTickerList.clear();
			
			status = true;
			con.close();
//			conLocal.close();
		}catch(Exception e) {
			System.out.println(e);
		}
		
		return status;
	}
	
	private void processNavData(String ticker, String news, String navType) {
		Double nav = null;
		String navLastUpdateTime=null;
		try {
			if(news.toLowerCase().contains("nav per "+navType)){
				newsContainsNAVTickerList.add(ticker);
				if(news.toLowerCase().contains("(with revaluation)")) {
					news = news.replace("(with revaluation) ","");
				}
				if(news.toLowerCase().contains("with revaluation")) {
					news = news.replace("with revaluation ","");
				}
				if(news.toLowerCase().contains("nav per "+navType+" was tk.")) {
					navPatternMatchedTickerList.add(ticker);
					String navSection = news.toLowerCase().split("nav per "+navType+" was tk.")[1].trim();
					if(navSection.contains("as on")) {
						String navData = navSection.split("as on")[0].trim();
						navData = navData.replaceAll("[^0-9-.()]", "");
						if(navData.contains("(")) {
							nav = new Double(navData.substring(1,navData.length()-1));
							nav = nav * (-1);
						}else {
							nav = new Double(navData);
						}
						//nav = new Double(navData.replaceAll("[^0-9-.]", ""));
						String navLastUpdateTimeSection = navSection.split("as on")[1].trim();
						if(navLastUpdateTimeSection.contains("and")) {
							navLastUpdateTime = navLastUpdateTimeSection.split("and")[0].trim();
						}
					}
				}else if(news.toLowerCase().contains("nav per "+navType+" of tk.")) {
					navPatternMatchedTickerList.add(ticker);
					String navSection = news.toLowerCase().split("nav per "+navType+" of tk.")[1].trim();
					String navData = navSection.split(" ")[0];
					if(navData.contains("(")) {
						nav = new Double(navData.substring(1,navData.length()-1));
						nav = nav * (-1);
					}else {
						nav = new Double(navData);
					}

					if(news.contains("year ended on")) {
						String lastSection = news.split("year ended on")[1].trim();
						if(lastSection.contains("as against")) {
							String timePeriod = lastSection.split("as against")[0].trim();
							navLastUpdateTime = timePeriod;
						}
					}else if(news.contains("year ended")) {
						String lastSection = news.split("year ended")[1].trim();
						if(lastSection.contains("as against")) {
							String timePeriod = lastSection.split("as against")[0].trim();
							navLastUpdateTime = timePeriod;
						}
					}
				}   
			} 
			
			if(nav!=null && navLastUpdateTime!=null) {
//				pst1.setString(1,ticker);
//				pst1.setDouble(2,nav);
//				pst1.setString(3,navLastUpdateTime);
//				pst1.execute();
				
				pst3.setString(1,ticker);
				pst3.setDouble(2,nav);
				pst3.setString(3,navLastUpdateTime);
				pst3.execute();
				
				navSuccessfulTickerList.add(ticker);
//				MarketAnnouncementDataProcessTask.file_Writer.write(pst1.toString());
				MarketAnnouncementDataProcessTask.file_Writer.write(pst3.toString());
				MarketAnnouncementDataProcessTask.file_Writer.write(System.lineSeparator());
			}else {
				if(navPatternMatchedTickerList.contains(ticker) && !navSuccessfulTickerList.contains(ticker)) 
					navFailedTickerList.add(ticker);
			}
			
		}catch(Exception e) {
			if(!navSuccessfulTickerList.contains(ticker)) navFailedTickerList.add(ticker);
			System.out.println("Exception in processing NAV data due to "+e);
		}
	}

	private void processEPSData(String ticker, String dateTime, String news, String epsType) throws IOException {		
		Double eps = null;
		int year = 0;
		String period = null;	
		try {		
			if(news.contains(epsType)) {
				newsContainsEPSTickerList.add(ticker);
				if(news.contains(epsType+" was Tk") && news.contains("as against")) {
					epsQuarterTickerList.add(ticker);
					String firstSection = news.split("as against")[0].trim();
					if(firstSection.contains("Tk.")) {
						String secondSection = firstSection.split("Tk.")[1];
						if(secondSection.contains("for")) {
							String epsData = secondSection.split("for")[0].trim();
							String timePeriod = secondSection.split("for")[1].trim();
							if(epsData.contains("(")) {
								eps = new Double(epsData.substring(1,epsData.length()-1));
								eps = eps * (-1);
							}else {
								eps = new Double(epsData);
							}
							year = Integer.parseInt(timePeriod.split(" ")[1]);
							period = timePeriod.split(" ")[0];
							if(period.contains("-")) {
								String stratPeriod = period.split("-")[0].trim();
								String endPeriod = period.split("-")[1].trim();
										
								if(stratPeriod.toLowerCase().contains("jan") && endPeriod.toLowerCase().contains("mar")) period = quarterDetails[0];
								else if(stratPeriod.toLowerCase().contains("apr") && endPeriod.toLowerCase().contains("jun")) period = quarterDetails[1];
								else if(stratPeriod.toLowerCase().contains("jul") && endPeriod.toLowerCase().contains("sep")) period = quarterDetails[2];
								else if(stratPeriod.toLowerCase().contains("oct") && endPeriod.toLowerCase().contains("dec")) period = quarterDetails[3];
							}
						}
					}
//					if(news.toLowerCase().contains("nav per "+navType+" was tk.")) {
//						String navSection = news.toLowerCase().split("nav per "+navType+" was tk.")[1].trim();
//						if(navSection.contains("as on")) {
//							String navData = navSection.split("as on")[0].trim();
//							if(navData.contains("(")) {
//								nav = new Double(navData.substring(1,navData.length()-1));
//								nav = nav * (-1);
//							}else {
//								nav = new Double(navData);
//							}
//							//nav = new Double(navData.replaceAll("[^0-9-.]", ""));
//							String navLastUpdateTimeSection = navSection.split("as on")[1].trim();
//							if(navLastUpdateTimeSection.contains("and")) {
//								navLastUpdateTime = navLastUpdateTimeSection.split("and")[0].trim();
//							}
//						}
//					}
				}
				
				//annual
				if(news.contains(epsType+" of Tk.") && news.contains("as against")) {
					epsAnnualTickerListType1.add(ticker);
					String firstSection = news.split(epsType+" of Tk.")[1].trim();
					String epsData = firstSection.split(",")[0];
					if(epsData.contains("(")) {
						eps = new Double(epsData.substring(1,epsData.length()-1));
						eps = eps * (-1);
					}else {
						eps = new Double(epsData);
					}
					//eps = new Double(epsData.replaceAll("[^0-9-.]", ""));
//					if(firstSection.toLowerCase().contains("nav per "+navType+" of tk.")) {
//						String secondSection = firstSection.toLowerCase().split("nav per "+navType+" of tk.")[1].trim();
//						String navData = secondSection.split(" ")[0];
//						if(navData.contains("(")) {
//							nav = new Double(navData.substring(1,navData.length()-1));
//							nav = nav * (-1);
//						}else {
//							nav = new Double(navData);
//						}
//						//nav = new Double(navData.replaceAll("[^0-9-.]", ""));
//					}
					if(firstSection.contains("year ended")) {
						String lastSection = firstSection.split("year ended")[1].trim();
						if(lastSection.contains("as against")) {
							String timePeriod = lastSection.split("as against")[0].trim();
							year = Integer.parseInt(timePeriod.split(",")[1].trim());
							period = "Annual";
//							navLastUpdateTime = timePeriod;
						}
					}
					if(firstSection.contains("year ended on")) {
						String lastSection = firstSection.split("year ended on")[1].trim();
						if(lastSection.contains("as against")) {
							String timePeriod = lastSection.split("as against")[0].trim();
							year = Integer.parseInt(timePeriod.split(",")[1].trim());
							period = "Annual";
//							navLastUpdateTime = timePeriod;
						}
					}
				}
				
				if(news.contains("The Board of Directors has recommended") && news.contains("year ended")) {
					epsAnnualTickerListType2.add(ticker);
					String firstSection = news.split("year ended")[1].trim();
					if(firstSection.contains(".")) {
						String timePeriod = firstSection.split("\\.")[0].trim();
						year = Integer.parseInt(timePeriod.split(",")[1].trim());
						period = "Annual";
//						navLastUpdateTime = timePeriod;
					}
					if(firstSection.contains(epsType+" of Tk.") /*&& firstSection.toLowerCase().contains("nav per "+navType+" of tk.")*/) {
						String secondSection = firstSection.split(epsType+" of Tk.")[1].trim();
						String epsData = secondSection.split(",")[0];	
						if(epsData.contains("(")) {
							eps = new Double(epsData.substring(1,epsData.length()-1));
							eps = eps * (-1);
						}else {
							eps = new Double(epsData);
						}
//						String thirdSection = secondSection.toLowerCase().split("nav per "+navType+" of tk.")[1].trim();
//						String navData = thirdSection.split(" ")[0];
//						if(navData.contains("(")) {
//							nav = new Double(navData.substring(1,navData.length()-1));
//							nav = nav * (-1);
//						}else {
//							nav = new Double(navData);
//						}
						//nav = new Double(navData.replaceAll("[^0-9-.]", ""));
					}
				}
			}
		}catch(Exception e) {
			epsFailedTickerList.add(ticker+" for period: "+period);
			System.out.println(ticker+"::"+e+"::"+news);
			MarketAnnouncementDataProcessTask.file_Writer.write(System.lineSeparator());
			MarketAnnouncementDataProcessTask.file_Writer.write("Failed Ticker: "+ticker);
			MarketAnnouncementDataProcessTask.file_Writer.write(System.lineSeparator());
			MarketAnnouncementDataProcessTask.file_Writer.write(e+" :: "+news);
			MarketAnnouncementDataProcessTask.file_Writer.write(System.lineSeparator());
		}

		try {
			if(eps!=null && period!=null && year!=0) {
//				pst.setString(1,ticker);
//				pst.setString(2,dateTime.split(" ")[0]);
//				pst.setInt(3,year);
//				pst.setString(4,period);
//				pst.setDouble(5,eps);
//				pst.execute();
				
				pst2.setString(1,ticker);
				pst2.setString(2,dateTime.split(" ")[0]);
				pst2.setInt(3,year);
				pst2.setString(4,period);
				pst2.setDouble(5,eps);
				pst2.execute();

				MarketAnnouncementDataProcessTask.file_Writer.write(ticker);
				MarketAnnouncementDataProcessTask.file_Writer.write(System.lineSeparator());
				MarketAnnouncementDataProcessTask.file_Writer.write("NEWS: "+news);
				MarketAnnouncementDataProcessTask.file_Writer.write(System.lineSeparator());
//				MarketAnnouncementDataProcessTask.file_Writer.write(pst.toString());
				MarketAnnouncementDataProcessTask.file_Writer.write(pst2.toString());
				MarketAnnouncementDataProcessTask.file_Writer.write(System.lineSeparator());
				
				if(period.equals("Annual")) insertDataInEpsDataTable(eps,ticker,year);
				
				epsSuccessfulTickerList.add(ticker+" for period: "+period);
			}else {
				if(epsQuarterTickerList.contains(ticker) || epsAnnualTickerListType1.contains(ticker) || epsAnnualTickerListType2.contains(ticker))
					epsFailedTickerList.add(ticker+" for period: "+period);
			}
		}catch(Exception e) {
			epsFailedTickerList.add(ticker+" for period: "+period);
			System.out.println(ticker+" :: "+e);
		}
	}

	private void insertDataInEpsDataTable(Double eps, String ticker, int year) throws SQLException, IOException, ClassNotFoundException {
		String query = "select out_shares,sector from daily_company_info d inner join company c on d.code=c.code where c.code='"+ticker+"'";
		Statement st = conProd.createStatement();
//		Connection conProd = CommonUtils.connectMainDB();
		Statement stProd = conProd.createStatement();
		ResultSet rs = stProd.executeQuery(query);
		rs.next();
		Double share_outstanding = rs.getDouble(1);
		String sector = rs.getString(2);
		if(share_outstanding!=null) {
			String insert_query = "insert ignore into eps_data(code,sector,year,annual_eps,out_shares) values('"+ticker+"','"+sector+"',"
					+year+","+eps+","+share_outstanding+")";
			st.executeUpdate(insert_query);
			stProd.executeUpdate(insert_query);
			MarketAnnouncementDataProcessTask.file_Writer.write(insert_query);
			MarketAnnouncementDataProcessTask.file_Writer.write(System.lineSeparator());
		}
//		conProd.close();
	}
	
	private void sendMail() {		
		ArrayList<String> epsPatternMismatchTickerList = new ArrayList<String>();
		for(String ticker : newsContainsEPSTickerList) {
			if(epsQuarterTickerList.contains(ticker) || epsAnnualTickerListType1.contains(ticker) || epsAnnualTickerListType2.contains(ticker))
				continue;
			
			epsPatternMismatchTickerList.add(ticker);
		}
		
		ArrayList<String> navPatternMismatchTickerList = new ArrayList<String>();
		for(String ticker : newsContainsNAVTickerList) {
			if(navPatternMatchedTickerList.contains(ticker))
				continue;
			
			navPatternMismatchTickerList.add(ticker);
		}
		
		ArrayList<String> navPatternMismatchTickerListForNews = new ArrayList<String>();
		for(String ticker : tempNewsContainsNAVTickerList) {
			if(newsContainsNAVTickerList.contains(ticker))
				continue;
			
			navPatternMismatchTickerListForNews.add(ticker);
		}
		
		try {
			String mailBody = "EPS & NAV Data Updater Task completed successfully.\n"
					+"Total Tickers Processed : " + (totalNewsTickerList.size()!=0 ? totalNewsTickerList.size() :"NA") +  ".\n\n"
					+"Detailed Info For EPS:\n"
					+"Total Tickers containing EPS Data : " + (newsContainsEPSTickerList.size()!=0 ? newsContainsEPSTickerList.size() :"NA") +  ".\n"	
					+"Total Tickers succesfully processed for EPS : " + (epsSuccessfulTickerList.size()!=0 ? epsSuccessfulTickerList.size() : "NA") + ".\n"	
					+"Total failed tickers for EPS: " + (epsFailedTickerList.size()!=0 ? epsFailedTickerList.size() : "NA")+ ".\n"
					+"Successfully inserted EPS data for following tickers:\n"
					+epsSuccessfulTickerList+"\n\n"
					+"Failed to insert EPS data for following tickers:\n"
					+epsFailedTickerList+"\n\n\n"
					
					+"Detailed Info For NAV:\n"
					+"Total Tickers containing NAV Data : " + (newsContainsNAVTickerList.size()!=0 ? newsContainsNAVTickerList.size() :"NA") +  ".\n"
					+"Total Tickers succesfully processed for NAV : " + (navSuccessfulTickerList.size()!=0 ? navSuccessfulTickerList.size() : "NA") + ".\n"			
					+"Total failed tickers for NAV: " + (navFailedTickerList.size()!=0 ? navFailedTickerList.size() : "NA")+ ".\n"
					+"Successfully inserted NAV data for following tickers:\n"
					+navSuccessfulTickerList+"\n\n"
					+"Failed to insert NAV data for following tickers:\n"
					+navFailedTickerList+"\n\n\n"
						
					+"Type mismatched Data Details:\n"
					+"Ticker Contains EPS data but didn't get processed due to pattern mismatch:\n"
					+epsPatternMismatchTickerList+"\n\n"
					+"Ticker Contains NAV data but didn't get processed due to pattern mismatch:\n"
					+navPatternMismatchTickerList+"\n\n"					
					+"Ticker Contains NAV keyword but didn't get processed due to NEWS pattern mismatch:\n"
					+navPatternMismatchTickerListForNews+"\n\n\n"
					
					+"Please manually insert data for these failed tickers.";
			
			CommonUtils.sendMail(null,mailBody,"Manual EPS Updater Details");
		} catch(Exception e) {
			System.out.println("Exception while sending mail due to "+e);
		}
	}
	
	public void insertMANDataIntoArchiveTable() {
		try {
			con = CommonUtils.connectDSE();
//			conLocal = CommonUtils.connectMainDB();
			conProd = CommonUtils.connectDC();
			String select_query = "select MAN_ANNOUNCEMENT_DATE_TIME,MAN_ANNOUNCEMENT_PREFIX,MAN_ANNOUNCEMENT from MAN "
					+ "where MAN_ANNOUNCEMENT_PREFIX not in ('EXCH','REGL')";
			List<String> tickerList = CommonUtils.getTickerList(conProd);
			String insert_query = "insert ignore into dse_analyzer_loader.MAN_archive(post_date_time,ticker,news) values(?,?,?)";
//			PreparedStatement man_pst = conLocal.prepareStatement(insert_query);
			PreparedStatement man_pstProd = conProd.prepareStatement(insert_query);
			
			Statement man_st = con.createStatement();
			ResultSet man_rs = man_st.executeQuery(select_query);
			while(man_rs.next()) {
				if(tickerList.contains(man_rs.getString(2))) {
					
//					man_pst.setString(1, man_rs.getString(1));
//					man_pst.setString(2, man_rs.getString(2));
//					man_pst.setString(3, man_rs.getString(3));
//					
//					man_pst.execute();
					
					man_pstProd.setString(1, man_rs.getString(1));
					man_pstProd.setString(2, man_rs.getString(2));
					man_pstProd.setString(3, man_rs.getString(3));
					
					man_pstProd.execute();
				}
			}
			
			con.close();
//			conLocal.close();
		} catch(Exception e) {
			System.out.println("Exception while inserting data into MAN_archive due to "+e);
		}
	}
}
