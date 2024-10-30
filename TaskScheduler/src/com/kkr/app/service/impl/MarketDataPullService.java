package com.kkr.app.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.kkr.app.service.IMarketDataPullService;
import com.kkr.common.util.CommonUtils;

public class MarketDataPullService implements IMarketDataPullService{

	static String idx_query = "select * from IDX";
	static String mkistat_archive_query = "select * from MKISTAT";
	static String trd_query = "select * from TRD";
	static String idx_insert_query = "insert ignore into idx values(?,?,?,?,?)";
	static String idx_archive_insert_query = "insert ignore into idx_archive (id, date_time, capital_value, deviation, deviation_percent) values (?,?,?,?,?)";
	static String mkistat_archive_insert_query = "insert ignore into mkistat_archive values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	static String trd_insert_query = "insert ignore into trade values(?,?,?,?)";
	private static boolean marketStateStatus = false;
	private static String marketStatError = "";
	private static boolean idxStatus = false;
	private static boolean idx_archive = false;
	private static boolean mkistat_archive = false;
	private static boolean trade = false;
	private static String otherTableError = "";
	private static String time = "";
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException {
		MarketDataPullService md = new MarketDataPullService();
//		md.pullMarketData();
//		md.intradayCalculation();
	}
	@Override
	public void pullMarketData(){
		try {
			Connection conDSE = CommonUtils.connectDSE();
//		    Connection conLocal = CommonUtils.connectMainDB();
		    Connection conProd = CommonUtils.connectDC();//CommonUtils.connectAWSDB();
		    
		    insertMKISTATData(conDSE,conProd); //conLocal,
		    smeUpdate(conDSE,conProd);
		    intradayCalculation(conProd);
		    Statement fixedTimeStatement = conProd.createStatement();
		    ResultSet fixedTimeResultSet = fixedTimeStatement.executeQuery("select value from system_configuration where id=1");
		    fixedTimeResultSet.next();
		    String startTime = fixedTimeResultSet.getString(1);
		    
		    if(LocalTime.now().isBefore(LocalTime.parse(startTime))) {
		    	CommonUtils.truncateTable(conProd,"idx");
		    	CommonUtils.truncateTable(conProd,"intra_day_return");
		    }
			insertOtherData(conDSE,conProd);
			conDSE.close();
			conProd.close();
			LocalTime currentTime = LocalTime.now();
			LocalTime range1Start = LocalTime.of(10, 5);
			LocalTime range1End = LocalTime.of(10, 8);
			LocalTime range2Start = LocalTime.of(14, 20);
			LocalTime range2End = LocalTime.of(14, 23); // Adjusted to 14:26 to make it a range
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

	private void insertOtherData(Connection con, Connection conProd) { //Connection conLocal,
		try {
			Statement idx_st = con.createStatement();
			ResultSet idx_rs = idx_st.executeQuery(idx_query);
			
			PreparedStatement idx_pst_prod = conProd.prepareStatement(idx_insert_query);
			PreparedStatement idx_archive_pst_prod = conProd.prepareStatement(idx_archive_insert_query);
			PreparedStatement mkistat_pst_prod = conProd.prepareStatement(mkistat_archive_insert_query);
			PreparedStatement trd_pst_prod = conProd.prepareStatement(trd_insert_query);
			
			while(idx_rs.next()) {

				  idx_pst_prod.setString(1, idx_rs.getString(1));
				  idx_pst_prod.setString(2, idx_rs.getString(2));
				  idx_pst_prod.setDouble(3, idx_rs.getDouble(3));
				  idx_pst_prod.setDouble(4, idx_rs.getDouble(4));
				  idx_pst_prod.setDouble(5, idx_rs.getDouble(5));
				
				  idx_pst_prod.execute();
				  
				  idx_archive_pst_prod.setString(1, idx_rs.getString(1));
				  idx_archive_pst_prod.setString(2, idx_rs.getString(2));
				  idx_archive_pst_prod.setDouble(3, idx_rs.getDouble(3));
				  idx_archive_pst_prod.setDouble(4, idx_rs.getDouble(4));
				  idx_archive_pst_prod.setDouble(5, idx_rs.getDouble(5));
				
				  idx_archive_pst_prod.execute();
				  idx_archive = true;
			}
			
			Statement mkistat_st = con.createStatement();
			ResultSet mkistat_rs = mkistat_st.executeQuery(mkistat_archive_query);
			while(mkistat_rs.next()) {

				mkistat_pst_prod.setString(1, mkistat_rs.getString(1));
				mkistat_pst_prod.setInt(2, mkistat_rs.getInt(2));
				mkistat_pst_prod.setString(3, mkistat_rs.getString(3));
				mkistat_pst_prod.setDouble(4, mkistat_rs.getDouble(4));
				mkistat_pst_prod.setDouble(5, mkistat_rs.getDouble(5));
				
				mkistat_pst_prod.setDouble(6, mkistat_rs.getDouble(6));
				mkistat_pst_prod.setDouble(7, mkistat_rs.getDouble(7));
				mkistat_pst_prod.setDouble(8, mkistat_rs.getDouble(8));
				mkistat_pst_prod.setDouble(9, mkistat_rs.getDouble(9));
				mkistat_pst_prod.setDouble(10, mkistat_rs.getDouble(10));
				
				mkistat_pst_prod.setInt(11, mkistat_rs.getInt(11));
				mkistat_pst_prod.setInt(12, mkistat_rs.getInt(12));
				mkistat_pst_prod.setDouble(13, mkistat_rs.getDouble(13));
				mkistat_pst_prod.setInt(14, mkistat_rs.getInt(14));
				mkistat_pst_prod.setInt(15, mkistat_rs.getInt(15));
				
				mkistat_pst_prod.setDouble(16, mkistat_rs.getDouble(16));
				mkistat_pst_prod.setInt(17, mkistat_rs.getInt(17));
				mkistat_pst_prod.setInt(18, mkistat_rs.getInt(18));
				mkistat_pst_prod.setDouble(19, mkistat_rs.getDouble(19));
				mkistat_pst_prod.setString(20, mkistat_rs.getString(20));
				
				mkistat_pst_prod.execute();
				mkistat_archive = true;
			}
			
			Statement trd_st = con.createStatement();
			ResultSet trd_rs = trd_st.executeQuery(trd_query);
			
			boolean status = false;
			while(trd_rs.next()) {
				status = true;	
				trd_pst_prod.setInt(1, trd_rs.getInt(1));
				trd_pst_prod.setInt(2, trd_rs.getInt(2));
				trd_pst_prod.setInt(3, trd_rs.getInt(3));
				trd_pst_prod.setDouble(4, trd_rs.getDouble(4));
				
				trd_pst_prod.addBatch();
			}
			if(status) {
				CommonUtils.truncateTable(conProd,"trade");
			}
			trd_pst_prod.executeBatch();
			trade = true;

		}catch(Exception e) {
			idxStatus = false;
			idx_archive = false;
			mkistat_archive = false;
			trade = false;
			otherTableError = e.toString();
			e.printStackTrace();
		}
	}

	private void insertMKISTATData(Connection conDSE, Connection conProd) { //Connection conLocal, 
		try {
			String select_query = "select MKISTAT_INSTRUMENT_CODE,MKISTAT_PUB_LAST_TRADED_PRICE,MKISTAT_HIGH_PRICE,MKISTAT_LOW_PRICE,"
					+ "MKISTAT_CLOSE_PRICE,MKISTAT_YDAY_CLOSE_PRICE,MKISTAT_TOTAL_TRADES,MKISTAT_TOTAL_VOLUME,MKISTAT_TOTAL_VALUE,"
					+ "MKISTAT_PUBLIC_TOTAL_TRADES,MKISTAT_PUBLIC_TOTAL_VOLUME,MKISTAT_PUBLIC_TOTAL_VALUE,MKISTAT_QUOTE_BASES,"
					+ "MKISTAT_SPOT_LAST_TRADED_PRICE from mdsdata.MKISTAT "
					+ "where MKISTAT_QUOTE_BASES not in('A-TB','A-GOVDBT')";
			
			String insert_query = "insert into market_stat values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			Statement stDSE = conDSE.createStatement();
			ResultSet rs = stDSE.executeQuery(select_query);
			PreparedStatement pstProd = conProd.prepareStatement(insert_query);
			boolean status = false;
			while(rs.next()) {
				status = true;
				if(rs.getString(1).equals("IICICL")) continue;
				pstProd.setString(1,rs.getString(1));
				if(rs.getDouble(2)!=0) {
					pstProd.setDouble(2,rs.getDouble(2));
				}else {
					pstProd.setDouble(2,rs.getDouble(14));
				}
				pstProd.setDouble(3,rs.getDouble(3));
				pstProd.setDouble(4,rs.getDouble(4));
				pstProd.setDouble(5,rs.getDouble(5));
				pstProd.setDouble(6,rs.getDouble(6));
				pstProd.setInt(7,rs.getInt(7));
				pstProd.setInt(8,rs.getInt(8));
				pstProd.setDouble(9,rs.getDouble(9));
				pstProd.setInt(10,rs.getInt(10));
				pstProd.setInt(11,rs.getInt(11));
				pstProd.setDouble(12,rs.getDouble(12));
				pstProd.setString(13,rs.getString(13));
				
				pstProd.addBatch();
			}			
			if(status) {
				CommonUtils.truncateTable(conProd,"market_stat");
			}
			pstProd.executeBatch();
			marketStateStatus = true;
		}catch(Exception e) {
			marketStateStatus = false;
			marketStatError = e.toString();
			e.printStackTrace();
		}
	}
	private void smeUpdate(Connection conDSE, Connection conProd) {
	    try {
	        String select_query = "select MKISTAT_INSTRUMENT_CODE,MKISTAT_PUB_LAST_TRADED_PRICE,MKISTAT_HIGH_PRICE,MKISTAT_LOW_PRICE,"
	        		+ "MKISTAT_CLOSE_PRICE,MKISTAT_YDAY_CLOSE_PRICE,MKISTAT_TOTAL_TRADES,MKISTAT_TOTAL_VOLUME,MKISTAT_TOTAL_VALUE,MKISTAT_SPOT_LAST_TRADED_PRICE from mdsdata.SME_MKISTAT;";

	        // Update query instead of insert query
	        String update_query = "update sme set LTP = ?, high = ?, low = ?, "
	                + "close = ?, YCP = ?, trade = ?, `change` = ?, "
	                + "value = ?, volume = ? where ticker = ?";

	        Statement stDSE = conDSE.createStatement();
	        ResultSet rs = stDSE.executeQuery(select_query);

	        PreparedStatement pstProd = conProd.prepareStatement(update_query);
	        while (rs.next()) {
	            if (rs.getString(1).equals("IICICL")) continue;

	            // Set the values for the update query
	            if (rs.getDouble(2) != 0) {
	                pstProd.setDouble(1, rs.getDouble(2)); // MKISTAT_PUB_LAST_TRADED_PRICE
	                pstProd.setDouble(7, rs.getDouble(6)-rs.getDouble(2)); // CHANGE
	            } else {
	                pstProd.setDouble(1, rs.getDouble(10)); // MKISTAT_SPOT_LAST_TRADED_PRICE
	                pstProd.setDouble(7, rs.getDouble(6)-rs.getDouble(10)); // CHANGE
	            }
	            pstProd.setDouble(2, rs.getDouble(3)); // MKISTAT_HIGH_PRICE
	            pstProd.setDouble(3, rs.getDouble(4)); // MKISTAT_LOW_PRICE
	            pstProd.setDouble(4, rs.getDouble(5)); // MKISTAT_CLOSE_PRICE
	            pstProd.setDouble(5, rs.getDouble(6)); // MKISTAT_YDAY_CLOSE_PRICE
	            pstProd.setInt(6, rs.getInt(7)); // MKISTAT_TOTAL_TRADES
	            pstProd.setDouble(8, rs.getDouble(9)); // MKISTAT_TOTAL_VALUE
	            pstProd.setDouble(9, rs.getDouble(8)); // MKISTAT_TOTAL_VOLUME
	            pstProd.setString(10, rs.getString(1)); // MKISTAT_INSTRUMENT_CODE as the condition for update

	            pstProd.addBatch();
	        }
	        pstProd.executeBatch();
	        marketStateStatus = true;
	    } catch (Exception e) {
	        marketStateStatus = false;
	        marketStatError = e.toString();
	        e.printStackTrace();
	    }
	}
	
	private static void intradayCalculation(Connection conDC) throws ClassNotFoundException, SQLException {
		Statement stmtInsert = conDC.createStatement();
		Statement stmtDC = conDC.createStatement();
		String query = "SELECT code, LTP, YCP FROM dse_analyzer.market_stat;";
		ResultSet rs = stmtDC.executeQuery(query);
		LocalDateTime currentTime = LocalDateTime.now();

        // Define the desired date-time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the current time using the formatter
        String formattedTime = currentTime.format(formatter);
		while(rs.next()) {
			Double returnPrice = 0.0;
			String ticker = rs.getString(1);
			Double ltp = Double.parseDouble(rs.getString(2));
			Double ycp = Double.parseDouble(rs.getString(3));
			
			if(ltp == 0.0) {
				returnPrice = ((ycp - ycp) / ycp) ;
			}
			else {
				returnPrice = ((ltp - ycp) / ycp) ;
			}
			returnPrice = returnPrice.isInfinite() || returnPrice.isNaN() ? 0.0 : returnPrice;
			String insert = "insert ignore into dse_analyzer.intra_day_return(ticker,ltp,ycp,return_price,time) "
					+ "values('"+ticker+"',"+ltp+","+ycp+","+returnPrice+",'"+formattedTime+"');";
			stmtInsert.execute(insert);
		}
		rs.close();
		stmtDC.close();
		stmtInsert.close();
	}
	
	private static void sendMail() {
		try {
			String mailBody = "Market Data Pull Task Executed Successfully.\n\n"
					+"Market Stat Table Status : " + (marketStateStatus == true ? "market_stat Table Populated Successfully." 
							:"Error in Populating market_stat. "+marketStatError)+".\n"		
					+"IDX Table Status : " + ( idxStatus == true? "IDX Table Successfully Updated" 
							: "Error in Populating IDX, IDX_ARCHIVE, MKISTAT_ARCHIVE, TRADE Table. " + otherTableError) + ".\n"		
					+"IDX_ARCHIVE Table Status : " + ( idx_archive == true? "IDX Table Successfully Updated" 
							: "Error in Populating IDX, IDX_ARCHIVE, MKISTAT_ARCHIVE, TRADE Table. " + otherTableError) + ".\n"	
					+"MKISTAT_ARCHIVE Table Status : " + ( mkistat_archive == true? "IDX Table Successfully Updated" 
							: "Error in Populating IDX, IDX_ARCHIVE, MKISTAT_ARCHIVE, TRADE Table. " + otherTableError) + ".\n"	
					+"TRADE Table Status : " + ( trade == true? "IDX Table Successfully Updated" 
							: "Error in Populating IDX, IDX_ARCHIVE, MKISTAT_ARCHIVE, TRADE Table. " + otherTableError) + ".\n"	;
			
			CommonUtils.sendMail(null,mailBody,"Market Data Pull Task");
		} catch(Exception e) {
			System.out.println("Exception while sending wwekly nav mail due to "+e);
		}
	}
}
