package com.kkr.app.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kkr.app.service.IForeignIndexDataScrappingService;
import com.kkr.app.task.ForeignIndexDataScrappingTask;
import com.kkr.common.util.CommonUtils;
import com.kkr.common.util.Constants;

public class ForeignIndexDataScrappingService implements IForeignIndexDataScrappingService{
	
	public static final String urlSENSEX = "https://www.investing.com/indices/sensex-historical-data";
	public static final String urlSPX = "https://www.investing.com/indices/us-spx-500-historical-data";
	public static final String urlNDX = "https://www.investing.com/indices/nq-100-historical-data";
	public static final String urlDAX = "https://www.investing.com/indices/germany-30-historical-data";
	public static final String urlESTX50 = "https://www.investing.com/indices/eu-stoxx50-historical-data";
	public static final String urlN225 = "https://www.investing.com/indices/japan-ni225-historical-data";
	public static final String urlHSI = "https://www.investing.com/indices/hang-sen-40-historical-data";
	public static final String urlSPI = "https://www.investing.com/indices/swiss-performance-historical-data";
	
	public static final String insert_query = "insert into dse_analyzer.adjusted_price(code,adjstd_close,date) values(?,?,?)";
	public static final String delete_query = "delete from dse_analyzer.adjusted_price where code=? and date=?";
	public static final String update_query = "update dse_analyzer.adjusted_price set adjstd_close=? where code=? and date=?";

	public static void main(String args[]) {
		new ForeignIndexDataScrappingService().populateForeignIndexData();
	}
	
	public boolean populateForeignIndexData() {
		Map<String,String> indexMap = generateIndexURLMap();
		Document doc = null;
		int tickerCount = 0;
		try {		
//			Connection con = CommonUtils.connectMainDB();
			Connection conProd = CommonUtils.connectDC();
//			PreparedStatement pst = con.prepareStatement(insert_query);
//			PreparedStatement pst1 = con.prepareStatement(update_query);
			
			PreparedStatement pst2 = conProd.prepareStatement(insert_query);
			PreparedStatement pst3 =conProd.prepareStatement(update_query);

			for(Map.Entry<String,String> entry : indexMap.entrySet()) {
				doc = Jsoup.connect(entry.getValue()).maxBodySize(0)
						.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
						.ignoreContentType(true).maxBodySize(0).timeout(20 * 1000).get();
//				processDocString(doc.text(),entry.getKey(),pst,pst1);
				processDocString(doc.text(),entry.getKey(),pst2,pst3);
//				processDoc(doc,entry.getKey());
				tickerCount++;
			}
//			pst.executeBatch();
//			con.close();
		}catch(Exception e) {
			System.out.println(e);
		}
		
		return tickerCount>3;
	}

	private static Map<String, String> generateIndexURLMap() {
		Map<String,String> indexMap = new HashMap<String,String>();		
		indexMap.put(Constants.foreignIndex[0],urlSPX);
		indexMap.put(Constants.foreignIndex[1],urlNDX);
		indexMap.put(Constants.foreignIndex[2],urlDAX);
		indexMap.put(Constants.foreignIndex[3],urlESTX50);
		indexMap.put(Constants.foreignIndex[4],urlN225);
		indexMap.put(Constants.foreignIndex[5],urlHSI);
		indexMap.put(Constants.foreignIndex[6],urlSPI);
		indexMap.put(Constants.foreignIndex[7],urlSENSEX);
		
		return indexMap;
	}
	
	private void processDocString(String doc, String index, PreparedStatement pst, PreparedStatement pst1) {
		String firstPara = doc.split("Date")[1].trim();
		String mainPara = "Date "+firstPara.split("Highest")[0].trim();
		String[] mainArray = mainPara.split(" ");
		if(mainArray[1].equals("Price") && mainArray[7].equals("%")) {
			for(int i = 8; i < 17; i = i+7) {
				String date = mainArray[i];
				String close = mainArray[i+1];							
				try {
					Date d = new SimpleDateFormat("MM/dd/yyyy").parse(date);
					String md = new SimpleDateFormat("yyyy-MM-dd").format(d);
					processPreparedStatements(md,close,i,8,index,pst,pst1);
				}catch(Exception e) {
					if(e.getMessage().contains("Unparseable date")) {
						i = i-8;
						continue;
					}
					System.out.println("Error while processing docString for "+index+" due to "+e);
				}
			}
		}else {
			System.out.println("table format changed");
		}
	}
	
//	private void processDoc(Document doc, String index) {
//		try {
//			Element table = doc.select("datatable_table__2Qbdw.datatable_table--border__1hROx datatable_table--mobile-basic__2Up9u datatable_table--freeze-column__2e8u1")
//					.get(0);
//			Elements rows = table.select("tr");
//			int limit = rows.size();
//			if(limit>3) limit = 3;
//			for(int i = 1; i < limit; i++) {
//				Element row =  rows.get(i);
//				String date = row.select("td").get(0).text();
//				Date d = new SimpleDateFormat("MMM dd, yyyy").parse(date);
//				String md = new SimpleDateFormat("yyyy-MM-dd").format(d);
//				String close = row.select("td").get(1).text();
//				processPreparedStatements(md,close,i,1,index);
//			}
//		}catch(Exception e) {
//			System.out.println("Error while processing doc due to "+e);
//		}
//	}
	
	private void processPreparedStatements(String date, String close, int iteratingNumber, int columnNumber, String index, 
			PreparedStatement pst, PreparedStatement pst1) 
			throws SQLException, IOException {
		Double closeValue = close==null || close.equals("") || close.isEmpty() ? null : new Double(close.replaceAll("[^0-9-.]", ""));
		if(closeValue!=null) {
			if(iteratingNumber!=columnNumber) {
				pst.setString(1,index);
				pst.setString(3,date);							
				pst.setDouble(2,closeValue);
				
				try {
					pst.execute();	
				} catch(Exception e) {
					if(e.getMessage().toString().contains("Duplicate")) {
						pst1.setDouble(1,closeValue);
						pst1.setString(2,index);
						pst1.setString(3,date);
						
						pst1.executeUpdate();
					}
				}
			}
			
			pst.setString(1,index+" C");
			pst.setString(3,date);							
			pst.setDouble(2,closeValue);
				
			pst1.setString(1,index+ " C");
			pst1.setString(2,date);
			pst1.executeUpdate();
				
			pst.addBatch();	
			
			try {
				pst.execute();	
			} catch(Exception e) {
				if(e.getMessage().toString().contains("Duplicate")) {
					pst1.setDouble(1,closeValue);
					pst1.setString(2,index+" C");
					pst1.setString(3,date);
					
					pst1.executeUpdate();
				}
			}
			ForeignIndexDataScrappingTask.file_Writer.write(pst.toString());
			ForeignIndexDataScrappingTask.file_Writer.write(System.lineSeparator());	
		}
	}
}
