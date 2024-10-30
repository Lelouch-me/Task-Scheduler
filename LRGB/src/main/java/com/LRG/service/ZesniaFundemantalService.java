package com.LRG.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

@Service
public class ZesniaFundemantalService {
	
	public static List<List<String>> indexSurpriseGrowthSummary(String idx) throws ClassNotFoundException, SQLException {
		Connection con = connectLocal();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		List<List<String>> growthSummary = new ArrayList<>();
		
		//String query = "SELECT * FROM bloomberg.fundamental_summary where flag ='"+idx+"' order by inserted_date desc limit 9;";
		String query = "SELECT * FROM bloomberg.fundamental_summary where flag ='"+idx+"' order by date(inserted_on) desc ,year desc, period desc limit 9;";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			List<String> values = new ArrayList<>();
			values.add(rs.getString("year"));
			values.add(rs.getString("period"));
			values.add(rs.getString("eps_surprise"));
			values.add(rs.getString("sales_surprise"));
			growthSummary.add(values);
		}
//		System.out.println(growthSummary);
		con.close();
		return growthSummary;
	}
	
	public static List<List<String>> indexGrowthSummary(String idx) throws ClassNotFoundException, SQLException {
		Connection con = connectLocal();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		List<List<String>> growthSummary = new ArrayList<>();
		
		//String query = "SELECT * FROM bloomberg.fundamental_summary where flag ='"+idx+"' order by inserted_date desc limit 8";
		String query = "SELECT * FROM bloomberg.fundamental_summary where flag ='"+idx+"' order by date(inserted_on) desc ,year desc, period desc limit 8";
		
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			List<String> values = new ArrayList<>();
			values.add(rs.getString("year"));
			values.add(rs.getString("period"));
			values.add(rs.getString("eps_growth"));
			values.add(rs.getString("sales_growth"));
			growthSummary.add(values);
		}
//		System.out.println(growthSummary);
		con.close();
		return growthSummary;
	}
	
	
	public static List<List<String>> indexEpsAGrowthSummary(String idx, String type) throws ClassNotFoundException, SQLException {
		Connection con = connectLocal();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		List<List<String>> growthSummary = new ArrayList<>();
		String query = "";
		if(type.equals("Actual")) {
			query = "SELECT * FROM bloomberg.bloomberg_index_eps where ticker = '"+idx+"' and type = 'Actual';";
		}
		else {
			query = "SELECT * FROM bloomberg.bloomberg_index_eps where ticker = '"+idx+"' and type = 'Estimated';";
		}
		
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			List<String> values = new ArrayList<>();
			values.add(rs.getString("year"));
			values.add(rs.getString("period"));
			values.add(rs.getString("eps"));
			growthSummary.add(values);
		}
//		System.out.println(growthSummary);
		con.close();
		return growthSummary;
	}
	
	public static TreeMap<Date, String> getPeBandValue(String idx) throws ClassNotFoundException, SQLException{
		Connection con = connectLocal();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		TreeMap<Date, String> bandValue = new TreeMap<>();
		String query = "SELECT * FROM bloomberg.bloomberg_peband where idx ='"+idx+"' order by date desc;";
		
		rs = stmt.executeQuery(query);
		
		while(rs.next()) {
			bandValue.put(rs.getDate("date"), rs.getString("pe"));
		}
		con.close();
		return bandValue;
	}
	
	public static Double getAverageBandValue(String idx, int year) throws SQLException, ClassNotFoundException {
		Connection con = connectLocal();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		Double value =0.0;
		
		LocalDate today = LocalDate.now();
        LocalDate previous = today.minusYears(year);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayDate = today.format(formatter);
        String previousDate = previous.format(formatter);
        
        String query = "SELECT avg(pe) FROM bloomberg.bloomberg_peband where idx = '"+idx+"' and date between '"+previousDate+"' and '"+todayDate+"' ;";
        rs = stmt.executeQuery(query);
		
		while(rs.next()) {
			value = rs.getDouble(1);
		}
		con.close();
		return value;

		
	}
	
	public static List<List<String>> getProjectedGrowthSummary(String idx) throws ClassNotFoundException, SQLException {
		Connection con = connectLocal();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		List<List<String>> growthSummary = new ArrayList<>();
		
		//String query = "SELECT * FROM bloomberg.fundamental_summary where flag ='"+idx+"' order by inserted_date desc limit 9;";
		String query = "SELECT * FROM bloomberg.projected_growth_summary where flag = '"+idx+"' order by year desc,period desc limit 5;";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			List<String> values = new ArrayList<>();
			values.add(rs.getString("year"));
			values.add(rs.getString("period"));
			values.add(rs.getString("eps_growth"));
			values.add(rs.getString("sales_growth"));
			growthSummary.add(values);
		}
//		System.out.println(growthSummary);
		con.close();
		return growthSummary;
	}
	
	public static List<List<String>> getProjectedMarginSummary(String idx) throws ClassNotFoundException, SQLException {
		Connection con = connectLocal();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		List<List<String>> growthSummary = new ArrayList<>();
		
		//String query = "SELECT * FROM bloomberg.fundamental_summary where flag ='"+idx+"' order by inserted_date desc limit 9;";
		String query = "SELECT * FROM bloomberg.projected_margin_summary where flag = '"+idx+"' order by year desc,period desc limit 5;";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			List<String> values = new ArrayList<>();
			values.add(rs.getString("year"));
			values.add(rs.getString("period"));
			values.add(rs.getString("opm"));
			values.add(rs.getString("npm"));
			growthSummary.add(values);
		}
//		System.out.println(growthSummary);
		con.close();
		return growthSummary;
	}
	
	public static ArrayList<ArrayList<String>> getWeeklyEPSData(String idx, String date) throws ClassNotFoundException, SQLException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        LocalDate nextDate = LocalDate.parse(date, formatter);
        LocalDate newDate = nextDate.plusDays(7);
		String query = "SELECT ticker,exp_report_date, est_eps, perv_eps, ((est_eps - perv_eps) / ABS(perv_eps))  AS eps_growth, cur_mcap,prev_mcap FROM bloomberg.weekly_earnings \r\n"
				+ "WHERE idx = '"+idx+"' AND exp_report_date between '"+date+"' and '"+newDate+"';";
		Connection con = connectLocal();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		ArrayList<ArrayList<String>> tickerList = new ArrayList<ArrayList<String>>();
		while(rs.next()) {
			ArrayList<String> data = new ArrayList<String>();
			data.add(rs.getString(1));
			data.add(rs.getString(2));
			data.add(rs.getString(3));
			data.add(rs.getString(4));
			data.add(rs.getString(5));
			data.add(rs.getString(6));
			data.add(rs.getString(7));
			tickerList.add(data);
			
		}
		return tickerList;
	}
	
	public static List<List<String>> getLtmgrowths(String idx) throws ClassNotFoundException, SQLException{
		List<List<String>> tickerList = new ArrayList<List<String>>();
		Connection con = connectLocal();
		String query = "SELECT s.flag, s.ticker, s.sector, s.estimated_sales,s.actual_sales, e.estimated_eps, e.actual_eps, e.market_cap FROM bloomberg.ltm_sales_growth s LEFT JOIN bloomberg.ltm_eps_growth e ON s.flag = e.flag AND s.ticker = e.ticker where s.flag = '"+idx+"'"
				+ "UNION SELECT s.flag, s.ticker, s.sector, s.estimated_sales,s.actual_sales, e.estimated_eps, e.actual_eps, e.market_cap  FROM bloomberg.ltm_sales_growth s RIGHT JOIN bloomberg.ltm_eps_growth e ON s.flag = e.flag AND s.ticker = e.ticker where e.flag = '"+idx+"';";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()) {
			List<String> tickerInfo = new ArrayList<String>();
			tickerInfo.add(rs.getString(1));
			tickerInfo.add(rs.getString(2));
			tickerInfo.add(rs.getString(3));
			tickerInfo.add(rs.getString(4));
			tickerInfo.add(rs.getString(5));
			tickerInfo.add(rs.getString(6));
			tickerInfo.add(rs.getString(7));
			tickerInfo.add(rs.getString(8));
			tickerList.add(tickerInfo);
		}
		return tickerList;
	}
	
	public static List<List<String>> getRevisionGrowths(String idx) throws ClassNotFoundException, SQLException{
		List<List<String>> tickerList = new ArrayList<List<String>>();
		Connection con = connectLocal();
		String query = "SELECT s.flag, s.ticker, s.sector, s.estimated_sales,s.actual_sales, e.estimated_eps, e.actual_eps, e.market_cap FROM bloomberg.revision_sales_growth s LEFT JOIN bloomberg.revision_eps_growth e ON s.flag = e.flag AND s.ticker = e.ticker where s.flag = '"+idx+"'"
				+ "UNION SELECT s.flag, s.ticker, s.sector, s.estimated_sales,s.actual_sales, e.estimated_eps, e.actual_eps, e.market_cap  FROM bloomberg.revision_sales_growth s RIGHT JOIN bloomberg.revision_eps_growth e ON s.flag = e.flag AND s.ticker = e.ticker where e.flag = '"+idx+"';";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()) {
			List<String> tickerInfo = new ArrayList<String>();
			tickerInfo.add(rs.getString(1));
			tickerInfo.add(rs.getString(2));
			tickerInfo.add(rs.getString(3));
			tickerInfo.add(rs.getString(4));
			tickerInfo.add(rs.getString(5));
			tickerInfo.add(rs.getString(6));
			tickerInfo.add(rs.getString(7));
			tickerInfo.add(rs.getString(8));
			tickerList.add(tickerInfo);
		}
		return tickerList;
	}
	
	public static Connection connectLocal() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://192.168.100.12:3306/dse_analyzer?rewriteBatchedStatements=true", "lrgb_loader",
		"developer4");
		con.setAutoCommit(true);
		return con;
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		getWeeklyEPSData("SPX","2024-07-18");
	}
}
