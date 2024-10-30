package com.LRG.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class KeyEconomicIndicatorService {
	public Map<String, Double> keyIndicatiors(String indicator) throws ClassNotFoundException, SQLException {
		Map<String, Double> dataMap = new HashMap<>();
		Connection con = connectLRGB();
		Statement stmt = con.createStatement();
		Double value[] = new Double[3];
		String query = "SELECT * FROM dse_analyzer.key_economic_indicators where indicators = '"+indicator+"' order by issued_date desc limit 2;";
		ResultSet rs = null;
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			String date = rs.getString("issued_date");
			Double rate = rs.getDouble("value");
			dataMap.put(date, rate);
		}
		String currentDate = previousDate(indicator);
		LocalDate startDate = LocalDate.parse(currentDate);
		while (true) {
	        String previousYearQuery = "SELECT * FROM dse_analyzer.key_economic_indicators where indicators = '"+indicator+"'and issued_date = '"+startDate.toString()+"';";
	        rs = null;
			rs = stmt.executeQuery(previousYearQuery);
			
			if(rs.next()) {
				dataMap.put(startDate.toString(), rs.getDouble("value"));		
				break;
			}
			else {
				startDate = startDate.minusDays(1);
			}
		}
		return dataMap;	
	}
	
	public static String previousDate(String indicator) throws ClassNotFoundException, SQLException {
		Connection con = connectLRGB();
		Statement stmt = con.createStatement();
		String query = "SELECT  DATE_SUB(a.issued_date, INTERVAL 1 YEAR) AS previous_date from (SELECT issued_date FROM dse_analyzer.key_economic_indicators \r\n"
				+ "where indicators = '"+indicator+"' order by issued_date desc limit 1) a ;";
		ResultSet rs = null;
		String date = null;
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			date = rs.getString("previous_date");
		}
		
		return date;
	}
	
	public static Connection connectLRGB() throws ClassNotFoundException, SQLException {
	Class.forName("com.mysql.jdbc.Driver");
	Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://192.168.100.12:3306/dse_analyzer?rewriteBatchedStatements=true", "lrgb_loader",
	"developer4");
	con.setAutoCommit(true);
	return con;
	}
	
	public static String getDate() throws ClassNotFoundException, SQLException {
		Connection con = connectLRGB();
		Statement stmt = con.createStatement();
		String query = "SELECT max(issued_date) FROM dse_analyzer.key_economic_indicators;";
		ResultSet rs = stmt.executeQuery(query);
		String date = "";
		while(rs.next()) {
			date = rs.getString(1);
		}
		return date;
	}
}
