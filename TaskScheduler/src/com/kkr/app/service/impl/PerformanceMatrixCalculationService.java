package com.kkr.app.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kkr.app.service.IPerformanceMatrixCalculationService;
import com.kkr.app.task.PerformanceMatrixDataPopulateTask;
import com.kkr.common.util.CommonUtils;

public class PerformanceMatrixCalculationService implements IPerformanceMatrixCalculationService{
	private static List<String> performanceMatrixTickers = new ArrayList<String>();
	private static List<String> performanceMatrixTickersProcessed = new ArrayList<String>();
	private static String ds30Error;
	private static String dsexError;
	private static String dailyPriceAdjstedError;
	private static boolean ds30Status = false;
	private static boolean dsexStatus = false;
	private static boolean dailyPriceAdjstedStatus = false;
	private static String time;
	private static int failed = 0;
	public static void main (String [] args) throws ClassNotFoundException, Exception{
		PerformanceMatrixCalculationService pm = new PerformanceMatrixCalculationService();
		pm.treasuryBondScrapping();
		
	}
	
	public void insertDailyPriceVolumeData() {
		try {
//			Connection conDSELocal = CommonUtils.connectMainDB();
			Connection conProd = CommonUtils.connectDC();
			
		    List<String> tickerList = CommonUtils.getTickerList(conProd);
		    String insert_query_price_volume = "insert ignore into daily_price_volume(close,volume,date,ticker) values(?,?,?,?)";
//		    PreparedStatement pst2 = conDSELocal.prepareStatement(insert_query_price_volume);
		    PreparedStatement pstProd = conProd.prepareStatement(insert_query_price_volume);
		    
		    String insert_query_adjusted_price = "insert ignore into adjusted_price(code,adjstd_close,volume,date) values(?,?,?,?)";
//		    PreparedStatement pst_adjusted = conDSELocal.prepareStatement(insert_query_adjusted_price);
		    PreparedStatement pst_adjusted_prod = conProd.prepareStatement(insert_query_adjusted_price);
		    
		    Statement fixedTimeStatement = conProd.createStatement();
		    ResultSet fixedTimeResultSet = fixedTimeStatement.executeQuery("select value from system_configuration where id=2");
		    fixedTimeResultSet.next();
		    String fixedTime = (LocalTime.parse(fixedTimeResultSet.getString(1)).minusMinutes(10)).toString();
		    
		    LocalDate date = LocalDate.now();
		    date = date.minusDays(3);
		    String DSEXQuery = "insert ignore into dse_analyzer.adjusted_price(code,adjstd_close,date) select id,capital_value,Date(date_time) "
		    		+ "from dse_analyzer.idx_archive where id='dsex' and Date(date_time)>='"+date.toString()+"' and TIME(date_time)>='"+fixedTime+"'";
//		    Statement DSEXStatement = conDSELocal.createStatement();
		    Statement DSEXStatementProd = conProd.createStatement();
		    try {
//		    	DSEXStatement.executeUpdate(DSEXQuery);
		    	DSEXStatementProd.executeUpdate(DSEXQuery);
		    	System.out.println(DSEXQuery);
		    	dsexStatus = true;
		    }catch(Exception e) {
		    	dsexStatus = false;
		    	dsexError = e.toString();
		    	System.out.println("Problem occurred in inserting DSEX data due to "+e);
		    }
		    
		    String DS30Query = "insert ignore into dse_analyzer.adjusted_price(code,adjstd_close,date) select id,capital_value,Date(date_time) "
		    		+ "from dse_analyzer.idx_archive where id='ds30' and Date(date_time)>='"+date.toString()+"' and TIME(date_time)>='"+fixedTime+"'";
//		    Statement DS30Statement = conDSELocal.createStatement();
		    Statement DS30StatementProd = conProd.createStatement();
		    try {
//		    	DS30Statement.executeUpdate(DS30Query);
		    	DS30StatementProd.executeUpdate(DS30Query);
		    	System.out.println(DS30Query);
		    	ds30Status = true;
		    }catch(Exception e) {
		    	ds30Status = false;
		    	ds30Error = e.toString();
		    	System.out.println("Problem occurred in inserting DS30 data due to "+e);
		    }
		    		    
		    for(String ticker : tickerList) {
		    	String price_volume_select_query = "select close,volume,date from dse.daily_price where ticker='"+ticker+"' order by date desc limit 3";
		    	Statement s = conProd.createStatement();
		  	  	ResultSet rs = s.executeQuery(price_volume_select_query);
		  	  	while(rs.next()) {
//		  	  		pst2.setDouble(1,rs.getDouble(1));
//		  	  		pst2.setDouble(2,rs.getDouble(2));
//		  	  		pst2.setString(3,rs.getString(3));
//		  	  		pst2.setString(4,ticker);
//
//		  	  		pst2.addBatch();	
//		  	  		
//		  	  		pst_adjusted.setString(1,ticker);
//		  	  		pst_adjusted.setDouble(2,rs.getDouble(1));
//		  	  		pst_adjusted.setDouble(3,rs.getDouble(2));
//		  	  		pst_adjusted.setString(4,rs.getString(3));
//		  	  		
//		  	  		pst_adjusted.addBatch();	
		  	  		
		  	  		
		  	  		pstProd.setDouble(1,rs.getDouble(1));
		  	  		pstProd.setDouble(2,rs.getDouble(2));
		  	  		pstProd.setString(3,rs.getString(3));
		  	  		pstProd.setString(4,ticker);

		  	  		pstProd.addBatch();	
	  	  		
		  	  		pst_adjusted_prod.setString(1,ticker);
		  	  		pst_adjusted_prod.setDouble(2,rs.getDouble(1));
		  	  		pst_adjusted_prod.setDouble(3,rs.getDouble(2));
		  	  		pst_adjusted_prod.setString(4,rs.getString(3));
	  	  		
		  	  		pst_adjusted_prod.addBatch();	
		  	  	}
		  	  	System.out.println(pst_adjusted_prod);
//		  	  	pst2.executeBatch();
//		  	  	pst_adjusted.executeBatch();
		  	  	
		  	  	pstProd.executeBatch();
		  	  	pst_adjusted_prod.executeBatch();
		  	  dailyPriceAdjstedStatus = true;
		    }
//		    conDSELocal.close();
		    conProd.close();
		}catch(Exception e) {
			dailyPriceAdjstedError = e.toString();
			e.printStackTrace();
		}
	}

	public void insertPerformanceMatrixData() {
		try {
//			  Connection conDSELocal = CommonUtils.connectMainDB();
			  Connection conProd = CommonUtils.connectDC();
			  
		      List<String> tickerList = CommonUtils.getTickerList(conProd);
		      performanceMatrixTickers = tickerList;
		      String insert_query = "insert ignore into dse_analyzer.company_performance_matrix values(?,?,?,NOW())";   
//		      PreparedStatement pst = conDSELocal.prepareStatement(insert_query);
		      PreparedStatement pstProd = conProd.prepareStatement(insert_query);
		      boolean status = false;
		      for(String ticker : tickerList) {
		    	  status = true;
		    	  double l_price = 0;
		    	  LocalDate date = LocalDate.now();
		    	  date = date.minusYears(1);  
		    	  LocalDate lastDayOfYear = date.with(TemporalAdjusters.lastDayOfYear());
		    	  
		    	  String p_query = "select close from dse.daily_price where ticker='"+ticker+"' and date<='"+lastDayOfYear.toString()
		    	  	+"' order by date desc limit 1";
		    	  Statement s = conProd.createStatement();
		    	  ResultSet roypr = s.executeQuery(p_query);
		    	  while(roypr.next()) {
		    		  l_price = roypr.getDouble(1);
		    	  }
		    	  
		    	  double close = 0;
		    	  String query = "select close from dse.daily_price where ticker='"+ticker+"' order by date desc limit 1";
		    	  ResultSet rs = s.executeQuery(query);
		    	  while(rs.next()) {
		    		  close = rs.getDouble(1);
		    	  }
		    	  
		    	  double ytd = l_price!=0 ? ((close/l_price) - 1) * 100 : 0;
		    	  
		    	  double avg_vol = 0;
		    	  String avg_query = "select avg(volume) from dse.daily_price where ticker='"+ticker+"' and date>='"+lastDayOfYear.toString()+"'";
		    	  ResultSet r = s.executeQuery(avg_query);
		    	  while(r.next()) {
		    		  avg_vol = r.getDouble(1);
		    	  }
		    	  
//		    	  pst.setString(1,ticker);
//		    	  if(ytd!=0) {
//		    		  pst.setDouble(2,ytd);
//		    	  }else {
//		    		  pst.setNull(2,java.sql.Types.NULL);
//		    	  }
//		    	  
//		    	  if(avg_vol!=0) {
//		    		  pst.setDouble(3,avg_vol);
//		    	  }else {
//		    		  pst.setNull(3,java.sql.Types.NULL);
//		    	  }
		    	  
//		    	  PerformanceMatrixDataPopulateTask.file_Writer.write(pst.toString());	 
//		    	  PerformanceMatrixDataPopulateTask.file_Writer.write(System.lineSeparator());
//		    	  pst.addBatch();
		    	  
		    	  
		    	  pstProd.setString(1,ticker);
		    	  if(ytd!=0) {
		    		  pstProd.setDouble(2,ytd);
		    	  }else {
		    		  pstProd.setNull(2,java.sql.Types.NULL);
		    	  }
		    	  
		    	  if(avg_vol!=0) {
		    		  pstProd.setDouble(3,avg_vol);
		    	  }else {
		    		  pstProd.setNull(3,java.sql.Types.NULL);
		    	  }
		    	  
		    	  pstProd.addBatch();
		    	  performanceMatrixTickersProcessed.add(ticker);
		      }		      
		      if(status) {
//		    	  CommonUtils.truncateTable(conDSELocal,"company_performance_matrix");
		    	  CommonUtils.truncateTable(conProd,"company_performance_matrix");
	  	      }
		      
//		      pst.executeBatch();		   
		      pstProd.executeBatch();	
		      failed = performanceMatrixTickers.size() - performanceMatrixTickersProcessed.size();
//		      LocalTime currentTime = LocalTime.now();
//		        
//		        if (currentTime.equals(LocalTime.of(10, 5)) || currentTime.equals(LocalTime.of(14, 20))) {
//		        	time = currentTime.toString();
//		        	sendMail();
//		        }
		      sendMail();
//		      conDSELocal.close();
		      conProd.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void populateReturnValueInAdjustedPrice() {
		try {
//			Connection con = CommonUtils.connectMainDB();
			Connection con = CommonUtils.connectDC();
		    List<String> tickerList = CommonUtils.getTickerList(con);
		    tickerList.add("DSEX");
		    tickerList.add("DS30");
		    String update_query = "update adjusted_price set return_value=? where code=? and date=?";
		    PreparedStatement pst=con.prepareStatement(update_query);
		    
		    for(String ticker : tickerList) {
		    	double prevValue = 0;
		    	String dateQuerry = "select min(date) from (select date from adjusted_price where code ='"+ticker+"' order by date desc limit 2) a";		    	
				Statement s = con.createStatement();
				ResultSet dateRs = s.executeQuery(dateQuerry);
				String date = null;
				while(dateRs.next()) {
					date = dateRs.getString(1);
				}
				if(date!=null) {
					String q = "select adjstd_close,date from adjusted_price where code='"+ticker+"' and date>='"+date+"' order by date";
					ResultSet r = s.executeQuery(q);
					int j = 0;
					while(r.next()) {
						Double ret = null;
						if(j==0) {
							prevValue = r.getDouble(1);
						}
						if(j!=0 && prevValue!=0) {
							ret = (r.getDouble(1)/prevValue) - 1;
							prevValue = r.getDouble(1);
						}
						j++;
						pst.setString(2,ticker);
						if(ret!=null) {
							pst.setDouble(1,ret*100);
							pst.setString(3,r.getString(2));

							pst.addBatch();
						}	  	    			
					}
					pst.executeBatch();
				}
		    }
		    con.close();
		}catch(Exception e) {
			System.out.println("problem occurred while updating return data"+e);
		}
	}
	
	public void treasuryBondScrapping() throws ClassNotFoundException, Exception {
//		Connection con = connect();
		Connection conDc =  CommonUtils.connectDC();
		Document doc = Jsoup.connect("https://www.bb.org.bd/en/index.php/monetaryactivity/treasury").get();
		String[] tenorDaysName = {"91 days T.Bill","182 days T.Bill"};  //,"364 days T.Bill"
		
		String[] tenorYearsName = {"2yr T.Bond","5yr T.Bond","10yr T.Bond","15yr T.Bond","20yr T.Bond"}; 
//		days(tenorDaysName, doc, con);
//		years(tenorYearsName, doc, con);
		days(tenorDaysName, doc, conDc);
		years(tenorYearsName, doc, conDc);	
	}
	
	public static void days(String [] names, Document doc, Connection conn) throws IOException, SQLException {
		Statement stmt = conn.createStatement();
		String truncateQuery = "TRUNCATE TABLE treasury_bill_bond_auction;";
		stmt.execute(truncateQuery);
		
		Element table = doc.select("table").get(0);
		Elements tableRows = table.select("tr");
		
		for(String name : names){
			for(Element tableRow : tableRows) {
				Elements tableDatas = tableRow.select("td");
				for(Element tableData : tableDatas) {
					if (tableData.text().contains(name)) {
						
						getDaysData(tableRow,conn);
	                    break;
	                }
				}
			}
		}
		
	}
	
	public static void years(String [] names, Document doc, Connection conn) throws IOException, SQLException {
		Statement stmt = conn.createStatement();
//		String truncateQuery = "TRUNCATE TABLE treasury_bill_bond_auction;";
//		stmt.execute(truncateQuery);
		
		Element table = doc.select("table").get(0);
		Elements tableRows = table.select("tr");
		
		for(String name : names){
			for(Element tableRow : tableRows) {
				Elements tableDatas = tableRow.select("td");
				for(Element tableData : tableDatas) {
					if (tableData.text().contains(name)) {
						getYearsData(tableRow,conn);
	                    break;
	                }
				}
			}
		}
		
	}
	
	static void getDaysData(Element tableRow, Connection conn) throws SQLException {
		ArrayList<String> treasuryData = new ArrayList<>();
		
		Elements tableDatas = tableRow.select("td");
		String date = tableDatas.select("td:nth-child(1)").text();
		String tenorName = tableDatas.select("td:nth-child(3)").text();
		String yield = tableDatas.select("td:nth-child(12)").text();
		
		
		
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate newDate = LocalDate.parse(date, inputFormatter);
        String formattedDate = newDate.format(outputFormatter);
        
        treasuryData.add(tenorName);
        treasuryData.add(yield);
        treasuryData.add(formattedDate);
        insert(conn,treasuryData);
//		System.out.println(formattedDate+" "+tenorName+" "+yield);
		
	}
	
	static void getYearsData(Element tableRow, Connection conn) throws SQLException {
		ArrayList<String> treasuryData = new ArrayList<>();
		
		Elements tableDatas = tableRow.select("td");
		String date = tableDatas.select("td:nth-child(1)").text();
		String tenorName = tableDatas.select("td:nth-child(3)").text();
		String yield = tableDatas.select("td:nth-child(13)").text();
		
		
		
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate newDate = LocalDate.parse(date, inputFormatter);
        String formattedDate = newDate.format(outputFormatter);
        
        treasuryData.add(tenorName);
        treasuryData.add(yield);
        treasuryData.add(formattedDate);
        insert(conn,treasuryData);

		
	}
	
	static void insert(Connection conn, ArrayList<String> value) throws SQLException {
		Connection con = conn;
		Statement stmt = con.createStatement();
		Double yield =Double.parseDouble(value.get(1));
		
		String query = "insert ignore into treasury_bill_bond_auction (treasury_name, yield, issued_date) values ('"+ value.get(0)+"'"+","+"'"+ yield + "'"+","+"'"+value.get(2)+"'"+")";
		String archiveQuery = "insert ignore into treasury_bill_bond_auction_archive (treasury_name, yield, issued_date) values ('"+ value.get(0)+"'"+","+"'"+ yield + "'"+","+"'"+value.get(2)+"'"+")";
		
		
		stmt.execute(query);
		stmt.execute(archiveQuery);
	}
	
	public  void otherScraping() throws ClassNotFoundException, SQLException, IOException{
		Connection conDC =  CommonUtils.connectDC();
		Document exchangeDoc = Jsoup.connect("https://www.bb.org.bd/en/index.php/econdata/exchangerate").get();
		Document callMoneyDoc = Jsoup.connect("https://www.bb.org.bd/en/index.php/monetaryactivity/call_money_market").get();
		Document oilDoc = Jsoup.connect("https://www.investing.com/commodities/crude-oil-historical-data").get();
		Document goldDoc = Jsoup.connect("https://www.investing.com/commodities/gold-historical-data").get();

		exchangeRate(exchangeDoc, conDC);
		callMoneyRate(callMoneyDoc, conDC);
		oilRate(oilDoc, conDC);
		goldRate(goldDoc, conDC);
	}
	
	void exchangeRate(Document exchangeDoc, Connection conn) throws SQLException {
		String outputDate = "";
		
		Element headingRow = exchangeDoc.select("table").first().select("thead tr").first();
		String dateText = headingRow.select("th").text();
		Pattern pattern = Pattern.compile("on (\\w+ \\d+, \\d+)");
        Matcher matcher = pattern.matcher(dateText);

        if (matcher.find()) {
            String date = matcher.group(1);
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate parsedDate = LocalDate.parse(date, inputFormatter);


            outputDate = parsedDate.format(outputFormatter);
        } else {
            System.out.println("Date not found.");
        }
        
        
        Element bodyRow = exchangeDoc.select("table").first().select("tbody tr").first();
        Double lowestDollarRate = Double.parseDouble(bodyRow.select("td:nth-child(2)").text()) ;
        Double heightDollarRate = Double.parseDouble(bodyRow.select("td:nth-child(3)").text()) ;
        Double exchangeRate = (heightDollarRate+lowestDollarRate)/2;
        
        insertKeyindicator(conn,"ExchngeRate", exchangeRate, outputDate);
	}
	
	void callMoneyRate(Document callMoneyRateee, Connection conn) throws SQLException {
		String dateText = callMoneyRateee.select("table").first().select("tbody tr").get(0).text();
		String callMoneyRate = callMoneyRateee.select("table").first().select("tbody tr").get(1).select("td:nth-child(6)").text();
		Double callMoney = Double.parseDouble(callMoneyRate);
		
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(dateText, inputFormatter);
        
        String outputDate = parsedDate.format(outputFormatter);
        
        insertKeyindicator(conn,"Call Money", callMoney, outputDate);
	}
	
	void oilRate(Document oilDoc, Connection conn) throws SQLException {
		String oilRate = oilDoc.select("div[data-test=instrument-price-last]").text();
		Double oil = Double.parseDouble(oilRate);
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        insertKeyindicator(conn,"Oil", oil, formattedDate);
	}
	
	void goldRate(Document goldDoc, Connection conn) throws SQLException {
		String goldRate = goldDoc.select("div[data-test=instrument-price-last]").text();
		goldRate = goldRate.replace(",", "");
		Double gold = Double.parseDouble(goldRate);
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        insertKeyindicator(conn,"Gold", gold, formattedDate);
	}
	
	void insertKeyindicator(Connection conn, String indicator, Double value, String date) throws SQLException {
		Connection con = conn;
		Statement stmt = con.createStatement();
		String query = "insert ignore into key_economic_indicators (indicators, value, issued_date) values ('"+ indicator+"'"+","+"'"+ value + "'"+","+"'"+date+"'"+")";
		stmt.execute(query);
		System.out.println("SuccessFully Inserted: "+indicator+" "+value+" "+date);
	}
	
	private static void sendMail() {
		try {
			String mailBody = "Performance Matrix Calculation Task Executed Successfully.\n\n"
					+"Task Execution Time : " + time +".\n"
					+"Total Tickers Found : " + (performanceMatrixTickers.size()!=0 ? performanceMatrixTickers.size() :"NA") +  ".\n"		
					+"Total Tickers succesfully processed : " + (performanceMatrixTickersProcessed.size()!=0 ? performanceMatrixTickersProcessed.size() : "NA") + ".\n"				
					+"Total failed tickers : " + (failed!=0 ? failed : "NA")+ ".\n\n"
					+"DSEX Table status : "+ (dsexStatus == true ? "Success" :"Failed. "+ dsexError) +  ".\n"
					+"DS30 Table status : "+ (ds30Status == true ? "Success" :"Failed. "+ ds30Error) +  ".\n"
					+"Daily_price and Adjusted_price Table status : "+ (dailyPriceAdjstedStatus == true ? "Success" :"Failed. "+ dailyPriceAdjstedError) +  ".\n\n";
//					+"Missing data for following tickers:\n"
//					+missingTickerList+"\n\n";
//					+"Ticker Contains NAV data but didn't get processed due to pattern mismatch:\n"
//					+patternMismatchTickerList+"\n\n\n"
//					+"Data not found for ticker :\n"
//					+dataMissingTickerList+"\n\n\n"
//					+"Please manually insert data for the failed tickers.";
			
			CommonUtils.sendMail(null,mailBody,"Performance Matrix Calculation");
		} catch(Exception e) {
			System.out.println("Exception while sending wwekly nav mail due to "+e);
		}
	}
}
