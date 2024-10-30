package com.kkr.app.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kkr.app.service.IDSEWebScrappingForDailyCompanyInfoService;
import com.kkr.common.util.CommonUtils;

public class DSEWebScrappingForDailyCompanyInfoService implements IDSEWebScrappingForDailyCompanyInfoService{

	public static List<String> missingTickerList = new ArrayList<String>(); 
	private static List<String> totalTickerList= new ArrayList<String>();
	private static List<String> totalTickerProcessed= new ArrayList<String>();
	private static List<String> totalTickerFailed= new ArrayList<String>();
	
	public static void main(String args[]) {
		new DSEWebScrappingForDailyCompanyInfoService().scrapDSESite();
	}
	@Override
	public void scrapDSESite() {
		try {
			System.out.println("scrapDSESite \n");
			Connection con = CommonUtils.connectDC();
  	    	List<String> tickerList = CommonUtils.getTickerList(con);

  	    	totalTickerList = tickerList;
  	    	con.close();
  	    	
  	    	getDataFromDSE(tickerList);
  			
  	    	while(true) {
  	    		if(missingTickerList.size()>0) {
  	    			List<String> TempTickerList = new ArrayList<String>();
  	    			TempTickerList.addAll(missingTickerList);
  	    			getDataFromDSE(TempTickerList);
  	    		}else {
  	    			break;
  	    		}
  	    	}
  	    	sendMail();
  		}catch(Exception e) {
  			e.printStackTrace();
  		}
	}
	
	private static void getDataFromDSE(List<String> tickerList) {
		System.out.println("getDataFromDSE \n");
		missingTickerList.clear();
		for(String ticker : tickerList) {
			String URL = "https://www.dsebd.org/displayCompany.php?name="+ticker;
			Document doc = null;
			try {
				doc = Jsoup.connect(URL).maxBodySize(0).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
						.ignoreContentType(true).maxBodySize(0).timeout(20 * 1000).get();
				
				Elements tabs = doc.select(".table.table-bordered.background-white");	
		    	
		    	insertDataInCompanyInfo(tabs, ticker);

			}catch(Exception ex) {
				missingTickerList.add(ticker);
			}
		}
	}
	
	private static void insertDataInCompanyInfo(Elements tabs, String ticker) {
		
		try {
			System.out.println("insertDataInCompanyInfo \n");
//			Connection con = CommonUtils.connectMainDB();
			Connection conProd = CommonUtils.connectDC();
			
			String update_query = "update daily_company_info set w52_price_range=?,authorized_capital=?,paidup_capital=?,"
					+ "out_shares=?,audited_pe=?,sponsor_director=?,govt=?,institute=?,`foreign`=?,public=? where code=?";
//			PreparedStatement pst = con.prepareStatement(update_query);
			PreparedStatement pstProd = conProd.prepareStatement(update_query);
			
			//52Week moving range
			Element table1 = tabs.select("table").get(1);
			Elements rowsForTable1 = table1.select("tr");
			Element row = rowsForTable1.get(3);
			Elements cols = row.select("td");
			Elements headcols = row.select("th");
			String movingPrice;
			String columnName = headcols.get(0).text();
			movingPrice = columnName.contains("Moving") ? cols.get(1).text() : null;	
			
			//Basic info
			Double authorized_capital = null;
			Double paidup_capital = null;
			Double out_shares = null;
			Element table2 = tabs.select("table").get(2);
			Elements rowsForTable2 = table2.select("tr");
			for (int i = 0; i < 4; i++) { 
				if(i==2) continue;
				Element rowF = rowsForTable2.get(i);
				Elements headcol = rowF.select("th");
				Elements col = rowF.select("td");			
				String value = col.get(0).text();
				
				if(headcol.get(0).text().contains("Authorized")) authorized_capital = !value.contains("-") ? new Double(value.replaceAll("[^a-zA-Z0-9.]", "")) : null;
				if(headcol.get(0).text().contains("Paid")) paidup_capital = !value.contains("-") ? new Double(value.replaceAll("[^a-zA-Z0-9.]", "")) : null;
				if(headcol.get(0).text().contains("Outstanding")) out_shares = !value.contains("-") ? new Double(value.replaceAll("[^a-zA-Z0-9.]", "")) : null;
			}
			
			//Price Earnings (P/E) Ratio Based on latest Audited Financial Statements
			Element table6 = tabs.select("table").get(6);
			Elements rowsForTable6 = table6.select("tr");
			Element rowF = rowsForTable6.get(1);
			Elements col = rowF.select("td");
			Double audited_pe = !col.get(col.size()-1).text().contains("-") 
					&& !col.get(col.size()-1).text().contains("n/a") && !col.get(col.size()-1).text().isEmpty() ? new Double(col.get(col.size()-1).text()) : null;
					
			//Other shareHolding Info
			Double sp = null;
			Double govt = null;
			Double institute = null;
			Double foreign = null;
			Double pub = null;
			int tableNumber;
			if(ticker.equals("JHRML") || ticker.equals("UNIONINS")) {
				tableNumber =12;
			}else {
				tableNumber =13;
			}
			Element table13 = tabs.select("table").get(tableNumber);
			Elements rowsForTable13 = table13.select("tr");
			Element rowS = rowsForTable13.get(0);
			Elements colS = rowS.select("td");
			for(int i = 0; i < colS.size(); i++) {
				if(colS.get(i).text().split(": ").length==2) {
					String type = colS.get(i).text().split(": ")[0];
					String value = colS.get(i).text().split(": ")[1];
					if(type.contains("Sponsor")) sp = new Double(value);		
					if(type.contains("Govt")) govt = new Double(value);
					if(type.contains("Institute")) institute = new Double(value);
					if(type.contains("Foreign")) foreign = new Double(value);
					if(type.contains("Public")) pub = new Double(value);
				}
			}
			
//			updateData(pst,ticker,movingPrice,authorized_capital,paidup_capital,out_shares,audited_pe,sp,govt,institute,foreign,pub);
			updateData(pstProd,ticker,movingPrice,authorized_capital,paidup_capital,out_shares,audited_pe,sp,govt,institute,foreign,pub);
			totalTickerProcessed.add(ticker);
			conProd.close();
		}catch(Exception e) {
			totalTickerFailed.add(ticker);
			System.out.println(ticker);
			e.printStackTrace();
		}
	}
	private static void updateData(PreparedStatement pst, String ticker, String movingPrice, Double authorized_capital,
			Double paidup_capital, Double out_shares, Double audited_pe, Double sp, Double govt, Double institute,
			Double foreign, Double pub) throws SQLException, ClassNotFoundException {
		
		pst.setString(11,ticker);
		pst.setString(1,movingPrice);
		
		if(authorized_capital!=null) {
			pst.setDouble(2,authorized_capital);
		}else {
			pst.setNull(2,java.sql.Types.NULL);
		}
		
		if(paidup_capital!=null) {
			pst.setDouble(3,paidup_capital);
		}else {
			pst.setNull(3,java.sql.Types.NULL);
		}
		
		
		if(out_shares!=null) {
			pst.setDouble(4,out_shares);
		}else {
			String query = "select out_shares from dse.companies where ticker='"+ticker+"' order by date desc limit 1";
//			Connection con = CommonUtils.connectMainDB();
			Connection con = CommonUtils.connectDC();
			Statement stLocal = con.createStatement();
			ResultSet rs = stLocal.executeQuery(query); 
			while(rs.next()) {
				out_shares = rs.getDouble(1);
			}
			if(out_shares!=null) {
				pst.setDouble(4,out_shares);
			}else {
				pst.setNull(4,java.sql.Types.NULL);
			}
			con.close();
		}	
		
		if(audited_pe!=null) {
			pst.setDouble(5,audited_pe);
		}else {
			pst.setNull(5,java.sql.Types.NULL);
		}
		
		if(sp!=null) {
			pst.setDouble(6,sp);
		}else {
			pst.setNull(6,java.sql.Types.NULL);
		}
		
		if(govt!=null) {
			pst.setDouble(7,govt);
		}else {
			pst.setNull(7,java.sql.Types.NULL);
		}
		
		if(institute!=null) {
			pst.setDouble(8,institute);
		}else {
			pst.setNull(8,java.sql.Types.NULL);
		}
		
		if(foreign!=null) {
			pst.setDouble(9,foreign);
		}else {
			pst.setNull(9,java.sql.Types.NULL);
		}
		
		if(pub!=null) {
			pst.setDouble(10,pub);
		}else {
			pst.setNull(10,java.sql.Types.NULL);
		}
		System.out.println(ticker);
		pst.executeUpdate();
	}
	
	private static void sendMail() {
		try {
			String mailBody = "DSE Scrapping Task Successfully Executed for daily_company_info Table.\n\n"
					+"Total Tickers Found : " + (totalTickerList.size()!=0 ? totalTickerList.size() :"NA") +  ".\n"		
					+"Total Tickers succesfully processed : " + (totalTickerProcessed.size()!=0 ? totalTickerProcessed.size() : "NA") + ".\n"				
					+"Total failed tickers : " + (totalTickerFailed.size()!=0 ? totalTickerFailed.size() : "NA")+ ".\n\n"				
					+"Missing data for following tickers:\n"
					+missingTickerList+"\n\n";
//					+"Ticker Contains NAV data but didn't get processed due to pattern mismatch:\n"
//					+patternMismatchTickerList+"\n\n\n"
//					+"Data not found for ticker :\n"
//					+dataMissingTickerList+"\n\n\n"
//					+"Please manually insert data for the failed tickers.";
			
			CommonUtils.sendMail(null,mailBody,"DSE Scrapping Details. ");
		} catch(Exception e) {
			System.out.println("Exception while sending wwekly nav mail due to "+e);
		}
	}
}
