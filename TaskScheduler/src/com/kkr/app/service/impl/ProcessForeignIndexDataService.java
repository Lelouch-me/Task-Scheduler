package com.kkr.app.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kkr.app.service.IProcessForeignIndexDataService;
import com.kkr.common.util.CommonUtils;
import com.kkr.common.util.Constants;

public class ProcessForeignIndexDataService implements IProcessForeignIndexDataService{

	public static String manualDate = LocalDate.now().minusMonths(3).toString();
	
	public static String query = "select date from dse_analyzer.adjusted_price where code='DSEX' and date>='"+manualDate+"'";
	public static String query2 = "select date from dse_analyzer.adjusted_price where code='<TICKER>' and date>='"+manualDate+"' order by date";
	public static String insert_query = "insert ignore into dse_analyzer.adjusted_price(code,adjstd_close,date) values(?,?,?)";
	public static List<String> tradingDateList = new ArrayList<String>();	
	
	public static void main(String args[]) {
		ProcessForeignIndexDataService g = new ProcessForeignIndexDataService();
		g.processForeignIndexData();
	}
	public boolean processForeignIndexData() {
		boolean status = false;
		try {
			deleteForeignIndexData();
			processDiscontinuousData();
			status = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return status;
	}

	private void processDiscontinuousData() throws ClassNotFoundException, SQLException {
//		Connection con = CommonUtils.connectMainDB();
		Connection conProd = CommonUtils.connectDC();
//		PreparedStatement pst = null;
		PreparedStatement pstProd = null;
		tradingDateList = getDateList(conProd,query);
		
		Map<String,List<Double>> priceMap = new HashMap<String,List<Double>>();		
		for(String s : Constants.foreignIndex) {
			priceMap.put(s,getPriceList(conProd,s));
		}
		
		for(String s : Constants.foreignIndex) {
			List<String> dateList = getDateList(conProd,query2.replace("<TICKER>",s));
			for(int i = 0;i<dateList.size();i++) {
				if(i+1 >= dateList.size()) break;
//				pst = con.prepareStatement(insert_query);
				pstProd = conProd.prepareStatement(insert_query);
				LocalDate startDate = LocalDate.parse(dateList.get(i));
				LocalDate endDate = LocalDate.parse(dateList.get(i+1));
				int count = getNumberOfTradingDay(startDate,endDate);
				if(count>0) {
					List<Double> priceList = priceMap.get(s);
					if(i+1 >= priceList.size()) continue;
					Double diff = priceList.get(i+1)-priceList.get(i);
					Double increment = diff/(count+1);
					Double initialVal = priceList.get(i) + increment;
					Double nextVal;
					int k = 0;
					for(LocalDate date = startDate.plusDays(1); date.isBefore(endDate);date = date.plusDays(1)) {
						String date_string = date.toString();
						if(tradingDateList.contains(date_string)) {
//							pst.setString(1,s);
//							pst.setString(3,date_string);
//							if(k==0) {
//								pst.setDouble(2,initialVal);
//								k++;
//							}else {
//								nextVal = initialVal + increment;
//								initialVal = nextVal;
//								pst.setDouble(2,nextVal);
//							}
//							pst.addBatch();
							
							
							pstProd.setString(1,s);
							pstProd.setString(3,date_string);
							if(k==0) {
								pstProd.setDouble(2,initialVal);
								k++;
							}else {
								nextVal = initialVal + increment;
								initialVal = nextVal;
								pstProd.setDouble(2,nextVal);
							}
							pstProd.addBatch();
						}
					}
//					pst.executeBatch();
					pstProd.executeBatch();
				}
			}
		}
//		con.close();
		conProd.close();
	}

	private void deleteForeignIndexData() throws ClassNotFoundException, SQLException {
//		Connection conLocal = CommonUtils.connectMainDB();
		Connection conProd = CommonUtils.connectDC();
		for(String s : Constants.foreignIndex) {
			String query = "select date from dse_analyzer.adjusted_price where code = '"+s+"' and date>'"+manualDate+"' "
					+ "and date not in(select date from dse_analyzer.adjusted_price where code = 'dsex')";
			
			Statement stLocal = conProd.createStatement();
			ResultSet rs = stLocal.executeQuery(query);
			while(rs.next()) {
				String deleteQuery = "delete from dse_analyzer.adjusted_price where code = '"+s+"' and date='"+rs.getString(1)+"'";
//				Statement stDelete = conLocal.createStatement();
//				stDelete.executeUpdate(deleteQuery);
				
				Statement stDeleteProd = conProd.createStatement();
				stDeleteProd.executeUpdate(deleteQuery);
			}
		}
//		conLocal.close();
		conProd.close();
	}
	
	private List<Double> getPriceList(Connection con,String ticker) throws SQLException {
		List<Double> tickerList = new ArrayList<Double>();
		String q = "select adjstd_close from dse_analyzer.adjusted_price where code='"+ticker+"' and date>='"+manualDate+"' order by date";
		Statement stLocal = con.createStatement();
		ResultSet rs = stLocal.executeQuery(q);
		while(rs.next()) {
			tickerList.add(rs.getDouble(1));
		}
		return tickerList;
	}

	private int getNumberOfTradingDay(LocalDate startDate,LocalDate endDate) {
		int count=0;
		for(LocalDate date = startDate.plusDays(1); date.isBefore(endDate);date = date.plusDays(1)) {
			String date_string = date.toString();
			if(tradingDateList.contains(date_string)) count++;
		}
		return count;
	}
	
	private List<String> getDateList(Connection conLocal,String q) throws SQLException {
		List<String> tickerList = new ArrayList<String>();
		Statement stLocal = conLocal.createStatement();
		ResultSet rs = stLocal.executeQuery(q);
		while(rs.next()) {
			tickerList.add(rs.getString(1));
		}
		return tickerList;
	}
}
