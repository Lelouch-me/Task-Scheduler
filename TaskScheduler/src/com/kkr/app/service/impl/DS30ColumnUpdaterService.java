package com.kkr.app.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kkr.app.service.IDS30ColumnUpdaterService;
import com.kkr.app.task.DS30ColumnUpdaterTask;
import com.kkr.common.util.CommonUtils;

public class DS30ColumnUpdaterService implements IDS30ColumnUpdaterService{

	public boolean updateDS30Column() {
		String URL = "https://www.dsebd.org/dse30_share.php";
		Document doc = null;
		String nullQuery = "update daily_company_info set dse30=null";
		boolean status = false;
		try {
//			Connection con = CommonUtils.connectMainDB();
			Connection conProd = CommonUtils.connectDC();
			doc = Jsoup.connect(URL).maxBodySize(0).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
					.ignoreContentType(true).maxBodySize(0).timeout(20 * 1000).get();
			
			Elements tabs = doc.select(".table.table-bordered.background-white.shares-table");	
	    	Element table = tabs.get(0);
	    	Elements rows = table.select("tr");
	    	Statement stNull = conProd.createStatement();
	    	stNull.executeUpdate(nullQuery);
	    	for(int i = 1; i < rows.size(); i++) {
	    		Element row = rows.get(i);
	    		Elements cols = row.select("td");
	    		String ticker = cols.get(1).text();		
	    		
//	    		updateData(con,ticker);
	    		updateData(conProd,ticker);
	    		
	    		DS30ColumnUpdaterTask.file_Writer.write(ticker);
	    	  	DS30ColumnUpdaterTask.file_Writer.write(System.lineSeparator());
	    	}
//	    	con.close();
	    	conProd.close();
	    	status = true;
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return status;
	}
	public static void main(String args[]) {
		DS30ColumnUpdaterTask.file_Writer = CommonUtils.prepareOutputFile("DS30ColumnUpdaterTask");
		new DS30ColumnUpdaterService().updateDS30Column();
	}
	
	private void updateData(Connection con, String ticker) throws SQLException {
		String q = "update daily_company_info set dse30='Y' where code='"+ticker+"'";	
		Statement stLocal = con.createStatement();
  	    stLocal.executeUpdate(q);
	}
}
