package com.LRG.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmeService {
	@Autowired
	private static SmeService smeService;
	
	public static ArrayList<String> getSmeTicker() throws ClassNotFoundException, SQLException {
		Connection con = connectLRGB();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		ArrayList<String> ticker = new ArrayList<>();
		
		String tickerQuery = "SELECT code FROM dse_analyzer.company where sector = 'SME';";
		rs = stmt.executeQuery(tickerQuery);
		
		while(rs.next()) {
			ticker.add(rs.getString(1));
		}
		return ticker;
	}
	
	public static TreeMap<String, Map<String, String>> getSmeCalculation(ArrayList<String> tickers) throws ClassNotFoundException, SQLException, InterruptedException {
		TreeMap<String, Map<String, String>> companyData = new TreeMap<>(); 
		Connection con = connectLRGB();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		for(String ticker : tickers) {
//			if(!ticker.equals("WONDERTOYS")) {
				Map<String, String> valueMap = new HashMap<>();
//				System.out.println(ticker);
				String query = "SELECT * FROM dse_analyzer.sme where ticker = '"+ticker+"';";
				Map<String, String> tickerData = getDataFromCompanyTableSME(ticker,con);
				Double outShare =  Double.parseDouble(getOutShateSME(ticker,con).isEmpty()? "0.0" :getOutShateSME(ticker,con))  ; 
				Double oneYearChange = get1YearChange(ticker,con);
				oneYearChange = oneYearChange.isInfinite() || oneYearChange.isNaN() ? 0.0 : oneYearChange;
				Double threeYearchange = get3YearChange(ticker,con) ;
				threeYearchange = threeYearchange.isInfinite() || threeYearchange.isNaN() ? 0.0 : threeYearchange;
				rs = stmt.executeQuery(query);
				while(rs.next()) {
					String sector = tickerData.get("Sector");
					Double eps = Double.parseDouble(tickerData.get("EPS")== null ? "0.0" : tickerData.get("EPS"));
					Double totalDebt = Double.parseDouble(tickerData.get("Total Debt"));
					totalDebt = totalDebt == null ? 0.0 : totalDebt;
					Double nav = Double.parseDouble(tickerData.get("NAV"));
					nav = nav == null ? 0.0 : nav;
					Double sales = Double.parseDouble(tickerData.get("Sales Per Share")== null ? "0.0" : tickerData.get("Sales Per Share"));
					String code = rs.getString(2);
					Double ltp = Double.parseDouble( rs.getString(3));
					ltp = ltp == null ? 0.0 : ltp;
					Double high = Double.parseDouble(  rs.getString(4));
					high = high == null ? 0.0 : high;
					Double low = Double.parseDouble(  rs.getString(5));
					low = low == null ? 0.0 : low;
					Double close = Double.parseDouble(  rs.getString(6));
					close = close == null ? 0.0 : close;
					Double ycp = Double.parseDouble(  rs.getString(7));
					ycp = ycp == null ? 0.0 : ycp;
					Double change = Double.parseDouble( rs.getString(8) );
					change = change == null ? 0.0 : change;
					Double trade = Double.parseDouble( rs.getString(9));
					trade = trade == null ? 0.0 : trade;
					Double value = Double.parseDouble(  rs.getString(10));
					value = value == null ? 0.0 : value;
					Double volume = Double.parseDouble( rs.getString(11));
					volume = volume == null ? 0.0 : volume;
					Double div = Double.parseDouble( rs.getString(12) == null ? "0.0" :  rs.getString(12))*10;
					
					Double weightedDiv = (ltp ==0.0) ? (div/ycp)*100 : (div/ltp)*100;
					weightedDiv = weightedDiv.isInfinite() || weightedDiv.isNaN() ? 0.0 : weightedDiv;
					
					
					Double changeValue = ltp == 0 ? 0 :((ltp-ycp)/ycp)*100 ;
					changeValue = changeValue.isInfinite() || changeValue.isNaN() ? 0.0 : changeValue;

					
					Double de = totalDebt / (nav*outShare);
					de = de.isInfinite() || de.isNaN() ? 0.0 : de;
					Double pe = ltp == 0 ? ycp/eps : ltp / eps;
					pe = pe.isInfinite() || pe.isNaN() ? 0.0 : pe;
					Double pb = ltp / nav;
					pb = pb.isInfinite() || pb.isNaN() ? 0.0 : pb;
					Double ps = ltp/sales;
					ps = ps.isInfinite() || ps.isNaN() ? 0.0 : ps;
					
					
					
					Double fv = getFVvalue(ticker,tickerData);
					
					valueMap.put("FV",fv.toString());
					valueMap.put("LTP", ltp.toString());
					valueMap.put("Change", changeValue.toString());
					valueMap.put("D/E", de.toString());
					valueMap.put("P/E", pe.toString());
					valueMap.put("P/B", pb.toString());
					valueMap.put("P/S", ps.toString());
					valueMap.put("EPS", eps.toString());
					valueMap.put("1 Year Change", oneYearChange.toString());
					valueMap.put("Daily Volume", value.toString());
					valueMap.put("Weighted Div Yield", weightedDiv.toString());
					valueMap.put("3 Year Change", threeYearchange.toString());
					valueMap.put("Report Date", rs.getString("report_date"));
					
					
					companyData.put(ticker, valueMap);
//					System.out.println(ltp+"cng "+ changeValue+"de "+de+"pe "+pe+"pb "+pb+"ps "+ps+"eps "+eps+"one "+oneYearChange);
				}
				
//			}
			}
		con.close();
		return companyData;
	}
	
	public static Map<String, String> getDataFromCompanyTableSME(String ticker, Connection con) throws ClassNotFoundException, SQLException {
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		Map<String, String> data = new HashMap<>();
		
		String query = "select code, sector, eps,total_debt,equities_per_share,sales_per_share from dse_analyzer.company where code = '"+ticker+"';";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			String code = rs.getString("code");
			String sector = rs.getString("sector");
			String eps = rs.getString("eps") == null ? "0.0" : rs.getString("eps");
			String debt = rs.getString("total_debt")==null? "0.0" :  rs.getString("total_debt");
			String nav = rs.getString("equities_per_share") == null ? "0.0" : rs.getString("equities_per_share");
			String sales =  rs.getString("sales_per_share") == null ? "0.0" : rs.getString("sales_per_share");
			data.put("Code", code);
			data.put("Sector", sector);
			data.put("EPS",eps);
			data.put("Total Debt",debt );
			data.put("NAV", nav);
			data.put("Sales Per Share", sales);
		}
		return data;
	}
	
	public static String getOutShateSME(String ticker, Connection con) throws ClassNotFoundException, SQLException {
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		String query = "SELECT out_shares FROM dse_analyzer.eps_data where code = '"+ticker+"' and out_shares is not null;";
		String outShare = "";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			outShare = rs.getString(1);
			outShare = outShare == null ? "0.0" : outShare;
		}
		return outShare;
		
	}
	
	public static Double get3YearChange(String ticker, Connection con) throws ClassNotFoundException, SQLException {
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		Double eps1 = 0.0;
		Double eps2 = 0.0;
		String query1 = "SELECT * FROM dse_analyzer.eps_data where code = '"+ticker+"' and  year = 2023;";
		String query2 = "SELECT * FROM dse_analyzer.eps_data where code = '"+ticker+"' and  year = 2020;";
		rs = stmt.executeQuery(query1);

		while(rs.next()) {
			eps1  = rs.getDouble("annual_eps");
		}
		
		rs = stmt.executeQuery(query2);

		while(rs.next()) {
			eps2  = rs.getDouble("annual_eps");
		}
		

		Double div = Math.pow(eps1/eps2, .33);
		
		Double threeYearChange = (div - 1) * 100;

		threeYearChange = threeYearChange == -100 || threeYearChange.isInfinite() ? 0.0 : threeYearChange;
		return threeYearChange;

		
	}
	
	public static Double get1YearChange(String ticker, Connection con) throws ClassNotFoundException, SQLException {
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		Double eps1 = 0.0;
		Double eps2 = 0.0;
		String query1 = "SELECT * FROM dse_analyzer.eps_data where code = '"+ticker+"' and  year = 2023;";
		String query2 = "SELECT * FROM dse_analyzer.eps_data where code = '"+ticker+"' and  year = 2022;";
		rs = stmt.executeQuery(query1);

		while(rs.next()) {
			eps1  = rs.getDouble("annual_eps");
		}
		
		rs = stmt.executeQuery(query2);

		while(rs.next()) {
			eps2  = rs.getDouble("annual_eps");
		}
		
		Double oneYearChange = eps2 != null ? ((eps1 - eps2)/Math.abs(eps2)) * 100 : 0.0;
		Double value = oneYearChange == -100 ? 0.0 : oneYearChange;
//		System.out.println(ticker+" "+eps1 + " "+ eps2 + " "+ value);
		return value;
	}
	
	public static TreeMap<String, Double> getSmeSectoralValue() throws ClassNotFoundException, SQLException, InterruptedException {
		//Thread.sleep(5);
		Connection con = connectLRGB();
		ArrayList<String> tickers = getSmeTicker();
		Map<String, Double> tickerPrice = getSmeMarketPrice();
		Map<String, Double> tickerOutShare = getSmeMarketOutShare();
		Map<String, Double> tickerEps = getSmeMarketEPS(con);
		Map<String, Double> tickerNav = getSmeMarketNAV(con);
		Map<String, Double> tickerDebt = getSmeMarketDEBT(con);  
		Map<String, Double> tickerSales = getSmeMarketSales(con); 
		Map<String, Double> tickerPrevEps = getSmeMarketPrevEps(con); 
		Map<String, Double> tickerPrev3yrEps = getSmeMarketPrev3yrEps(con);

		
//		TreeMap<String, Map<String, String>> getSmeCalculation = getSmeCalculation(tickers);
		
		Double sumPriceOut = 0.0;
		Double sumEpsOut = 0.0;
		Double sumNavOut = 0.0;
		Double sumDebt = 0.0;
		Double sumSalesOut = 0.0;
		Double sumPrvEpsOut = 0.0;
		Double sumPrv3yrEpsOut = 0.0;
		Double sumWeightedDiv = 0.0;
		
		for(String ticker : tickers ) {
//			if(!ticker.equals("WONDERTOYS")) {
				Double productPO = tickerPrice.get(ticker)* (tickerOutShare.get(ticker) == null ? 0.0 : tickerOutShare.get(ticker));
				productPO = productPO.isInfinite() || productPO.isNaN() ? 0.0 : productPO;
				sumPriceOut += productPO;
				
				Double productEO = tickerEps.get(ticker)* (tickerOutShare.get(ticker) == null ? 0.0 : tickerOutShare.get(ticker));
				productEO = productEO.isInfinite() || productEO.isNaN() ? 0.0 : productEO;
				sumEpsOut += productEO;
				
				Double productNO = tickerNav.get(ticker)* (tickerOutShare.get(ticker) == null ? 0.0 : tickerOutShare.get(ticker));
				productNO = productNO.isInfinite() || productNO.isNaN() ? 0.0 : productNO;
				sumNavOut += productNO;
				
				Double productDebt = tickerDebt.get(ticker);
				productDebt = productDebt.isInfinite() || productDebt.isNaN() ? 0.0 : productDebt;
				sumDebt += productDebt;
				
				Double productSO = tickerSales.get(ticker) *  (tickerOutShare.get(ticker) == null ? 0.0 : tickerOutShare.get(ticker));
				productSO = productSO.isInfinite() || productSO.isNaN() ? 0.0 : productSO;
				sumSalesOut += productSO;
				
				Double productPEO = tickerPrevEps.get(ticker) ==  null ? 0.0 : tickerPrevEps.get(ticker) *  (tickerOutShare.get(ticker) == null ? 0.0 : tickerOutShare.get(ticker));
				productPEO = productPEO.isInfinite() || productPEO.isNaN() ? 0.0 : productPEO;
				sumPrvEpsOut += productPEO;
				
				Double productP3EO = tickerPrev3yrEps.get(ticker) == null ? 0.0 : tickerPrev3yrEps.get(ticker) * (tickerOutShare.get(ticker) == null ? 0.0 : tickerOutShare.get(ticker));
				productP3EO = productP3EO.isInfinite() || productP3EO.isNaN() ? 0.0 : productP3EO;
				sumPrv3yrEpsOut += productP3EO;
				
//				Double weightMCap = (tickerPrice.get(ticker)* (tickerOutShare.get(ticker) == null ? 0.0 : tickerOutShare.get(ticker)))/sumPriceOut;
//				String div = getSmeCalculation.get(ticker).get("Weighted Div Yield");
//				Double divvv = Double.parseDouble(div);
//				sumWeightedDiv += weightMCap * divvv;			
		}
		Double sumEps = 0.0;
		int count = 0;
		for(Map.Entry<String, Double> epsMap : tickerEps.entrySet()) {
			sumEps += epsMap.getValue();
			count += 1;
		}
		
		Double sectoralEps = sumEps / count;
		
		TreeMap<String, Double> smeData = new TreeMap<>();
		smeData.put("P/E", sumPriceOut/sumEpsOut);
		smeData.put("P/B", sumPriceOut/sumNavOut);
		smeData.put("D/E", sumDebt/sumNavOut);
		smeData.put("P/S", sumPriceOut/sumSalesOut);
		smeData.put("EPS",sectoralEps);
		smeData.put("Weighted Div Yield", sumWeightedDiv);
		smeData.put("EPS Growth 1 year", ((sumEpsOut/sumPrvEpsOut)-1)*100);
		smeData.put("EPS Growth 3 year", (Math.pow(sumEpsOut/sumPrv3yrEpsOut, 1.0/3.0 )-1)*100);
		
		con.close();
		return smeData;
	}
	
	public static Map<String, Double> getSmeMarketPrice() throws SQLException, ClassNotFoundException {
		Connection con = connectLRGB();
		Map<String, Double> tickerPrice = new HashMap<>();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		String query = "SELECT * FROM dse_analyzer.sme";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			String code = rs.getString("ticker");
			Double ltp = rs.getDouble("LTP");
			Double ycp = rs.getDouble("YCP");
			if(!(ltp == 0.0)){
				tickerPrice.put(code, ltp);
			}
			else {
				tickerPrice.put(code, ycp);
			}
		}
		con.close();
		return tickerPrice;
	}
	
	public static Map<String, Double> getSmeMarketOutShare() throws SQLException, ClassNotFoundException {
		Connection con = connectLRGB();
		Map<String, Double> tickerPrice = new HashMap<>();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		String query = "SELECT * FROM dse_analyzer.eps_data where sector = 'sme' and out_shares is not null";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			String code = rs.getString("code");
			Double outShare = rs.getDouble("out_shares");
			outShare = outShare.isInfinite() || outShare.isNaN() ? 0.0 : outShare;

			tickerPrice.put(code, outShare);
		}
		con.close();
		return tickerPrice;
	}
	
	public static Map<String, Double> getSmeMarketEPS(Connection con) throws SQLException {
		Map<String, Double> tickerPrice = new HashMap<>();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		String query = "SELECT * FROM dse_analyzer.company where sector = 'sme';";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			String code = rs.getString("code");
			Double eps = rs.getDouble("eps");
			eps = eps.isInfinite() || eps.isNaN() ? 0.0 : eps;
			tickerPrice.put(code, eps);
		}
		return tickerPrice;
	}
	
	public static Map<String, Double> getSmeMarketNAV(Connection con) throws SQLException {
		Map<String, Double> tickerPrice = new HashMap<>();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		String query = "SELECT * FROM dse_analyzer.company where sector = 'sme';";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			String code = rs.getString("code");
			Double eps = rs.getDouble("equities_per_share");
			eps = eps.isInfinite() || eps.isNaN() ? 0.0 : eps;
			tickerPrice.put(code, eps);
		}
		return tickerPrice;
	}
	
	public static Map<String, Double> getSmeMarketDEBT(Connection con) throws SQLException {
		Map<String, Double> tickerPrice = new HashMap<>();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		String query = "SELECT * FROM dse_analyzer.company where sector = 'sme';";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			String code = rs.getString("code");
			Double debt = rs.getDouble("total_debt");
			debt = debt.isInfinite() || debt.isNaN() ? 0.0 : debt;
			tickerPrice.put(code, debt);
		}
		return tickerPrice;
	}
	
	public static Map<String, Double> getSmeMarketSales(Connection con) throws SQLException {
		Map<String, Double> tickerPrice = new HashMap<>();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		String query = "SELECT * FROM dse_analyzer.company where sector = 'sme';";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			String code = rs.getString("code");
			Double sales = rs.getDouble("sales_per_share");
			sales = sales.isInfinite() || sales.isNaN() ? 0.0 : sales;
			tickerPrice.put(code, sales);
		}
		return tickerPrice;
	}
	
	
	public static Map<String, Double> getSmeMarketPrevEps(Connection con) throws SQLException {
		Map<String, Double> tickerPrice = new HashMap<>();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		String query = "SELECT * FROM dse_analyzer.eps_data where sector = 'sme' and year = 2022";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			String code = rs.getString("code");
			Double outShare = rs.getDouble("annual_eps");
			outShare = outShare.isInfinite() || outShare.isNaN() ? 0.0 : outShare;

			tickerPrice.put(code, outShare);
		}
		return tickerPrice;
	}
	
	public static Map<String, Double> getSmeMarketPrev3yrEps(Connection con) throws SQLException {
		Map<String, Double> tickerPrice = new HashMap<>();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		String query = "SELECT * FROM dse_analyzer.eps_data where sector = 'sme' and year = 2020";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			String code = rs.getString("code");
			Double outShare = rs.getDouble("annual_eps");
			outShare = outShare.isInfinite() || outShare.isNaN() ? 0.0 : outShare;

			tickerPrice.put(code, outShare);
		}
		return tickerPrice;
	}
	
	
	public static Double getFVvalue (String ticker, Map<String, String> tickerData) throws ClassNotFoundException, SQLException, InterruptedException {
		TreeMap<String, Double> smeSectorData = smeService.getSmeSectoralValue();
		Double sectorPB=smeSectorData.get("P/B");
		
		Double fv;
		
		Double earningsPortion = smeSectorData.get("P/E")!= null && !(Double.parseDouble((tickerData.get("EPS"))) ==0.0) ? 
				smeSectorData.get("P/E")*Double.parseDouble((tickerData.get("EPS"))) : 0.0;
		
		
		
		Double bookingPortion = sectorPB!= null && !(Double.parseDouble((tickerData.get("NAV"))) ==0.0)&&(tickerData.get("NAV")) !=null ? 
				sectorPB*Double.parseDouble((tickerData.get("NAV"))) : 0.0;
		
		Double salesPortion = smeSectorData.get("P/S")!= null && !(Double.parseDouble((tickerData.get("Sales Per Share"))) ==0.0) ? 
				smeSectorData.get("P/E")*Double.parseDouble((tickerData.get("Sales Per Share"))) : 0.0;
		
		
		
//		
//		System.out.println("\n\n"+ticker);
//		System.out.println(smeSectorData.get("P/B"));
//		System.out.println(tickerData.get("NAV"));
//		System.out.println(bookingPortion);
		
		
		
		int count = 0;
		if(earningsPortion!=0) count++;
		if(bookingPortion!=0) count++;
		if(salesPortion!=0) count++;
		if(count==3) fv = (earningsPortion*0.5) + (bookingPortion*0.2) + (salesPortion*0.3);
		if(count==2) fv = (earningsPortion*0.5) + (bookingPortion*0.5) + (salesPortion*0.5);
		if(count==1 && earningsPortion!=0 || bookingPortion !=0) fv = earningsPortion + bookingPortion;
		else fv=null;
		
		
		return fv;
	}
	
	public static Connection connectLRGB() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://192.168.100.12:3306/dse_analyzer?rewriteBatchedStatements=true", "lrgb_loader",
		"developer4");
		con.setAutoCommit(true);
		return con;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		//getSmeCalculation(getSmeTicker());
		//Double tt =getFVvalue("KFL");
	}
}
