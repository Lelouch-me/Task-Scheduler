package com.LRG.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLService {
	
	public static void createExel(Map<String, Double> tickerValueMap, String sheetName, String xlName) throws IOException {
		String excelFilePathSectorReturn = "E:\\MidDayUpdater\\"+xlName+".xlsx";
		DecimalFormat df = new DecimalFormat("#.##"); 
		Map<String, Double> randomValue = tickerValueMap;
		
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = null;
      
        	sheet = workbook.createSheet(sheetName); 
        
         
       
        Row row= sheet.createRow(0);
        for(int i = 0;i<3;i++) {
        	Cell cell = row.createCell(i);
   		 	if(i==0) cell.setCellValue((String) "Ticker");
   		 	if(i==1) cell.setCellValue(xlName);
   		   // if(i==2) cell.setCellValue((String) "P/E");
        }
        
        int rowCount=1;
        for (Map.Entry<String,Double> entry : randomValue.entrySet()) {
        		row= sheet.createRow(rowCount);
           	 	for(int i = 0;i<2;i++) {
           	 		Cell cell = row.createCell(i);
           	 		if(i==0) cell.setCellValue((String) entry.getKey());
           	 		if(i==1 && entry.getValue()!=null) cell.setCellValue((Double) Double.valueOf(df.format(entry.getValue())));
           	 		if(i==1 && entry.getValue()==null) cell.setCellValue((String) "  N/A");
           	 		
           	 		//if(i==2) cell.setCellValue((Double) sortedSectorPEMap.get(entry.getKey()));
           	 	}
           	    rowCount++;
        	
         }
         FileOutputStream outputStream = new FileOutputStream( excelFilePathSectorReturn);
         workbook.write(outputStream);
         workbook.close();
//         System.out.println("XL generated ");
	}

}
