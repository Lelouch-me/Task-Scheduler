package com.LRG.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.LRG.model.MarketStatDto;
import com.LRG.repository.HolidayRepository;
import com.LRG.repository.SysConfigRepository;

public class CommonUtils {
	private static Log logger = LogFactory.getLog(CommonUtils.class);
	public static Map<String, Double> sortMap(Map<String, Double> map, boolean order) {  
		List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(map.entrySet());   
		Collections.sort(list, new Comparator<Entry<String, Double>>(){  
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2){  
				if(order){  
					return o1.getValue().compareTo(o2.getValue());}   
				else{  
					return o2.getValue().compareTo(o1.getValue());  
				}  
			}  
		});   
			
		Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();  
		for (Entry<String, Double> entry : list){  
			sortedMap.put(entry.getKey(), entry.getValue());  
		}
		return sortedMap;  			
	}
	
	public static boolean isMarketOpen(HolidayRepository holidayRepository, SysConfigRepository sysConRepository) {
		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		boolean isWeekend = day==6 || day==7;
		boolean isHoliday = false;
		LocalDate date = LocalDate.now(ZoneId.of("Asia/Dhaka"));
		for(String s : holidayRepository.findAllHolidayDate()) {
			if(s.equals(date.toString())) {
				isHoliday = true;
			}
		}
		String marketStartTime = sysConRepository.findById(1).getValue();
		String marketEndTime = sysConRepository.findById(2).getValue();
		
		LocalTime time = LocalTime.now(ZoneId.of("Asia/Dhaka"));
		
		return time.isAfter(LocalTime.parse(marketStartTime)) && time.isBefore(LocalTime.parse(marketEndTime)) && !isWeekend && !isHoliday;		
	}
	
	public static Double getProductofList(List<Double> productList) {
		Double product = 1.0;
		for(int i = 0; i < productList.size(); i++) {
			product *= productList.get(i);
		}
		return product;
	}
	
	public static Double[] getDoubleArray(List<Double> doubleList) {
		Double[] mainArray = new Double[doubleList.size()];
		for(int i = 0; i < mainArray.length; i++) {
			mainArray[i] = Double.valueOf(doubleList.get(i));
		}
		return mainArray;
	}
	
	public static Double average(Double[] numbers) {
		double sum = 0;
         for(int i=0; i < numbers.length ; i++) {
        	 sum = sum + numbers[i];
         }
         Double average = sum / numbers.length;
        
         return average;
	}

	public static int getDayDifference(String dateRange) {
		int dayDifference = 0;
		if(dateRange!=null && !dateRange.equals("")) {
			String startDate = dateRange.split(" - ")[0];
			String endDate = dateRange.split(" - ")[1];
			try {
				Date date1 = new SimpleDateFormat("MM/dd/yyyy").parse(startDate);
				Date date2 = new SimpleDateFormat("MM/dd/yyyy").parse(endDate);
				
				dayDifference = (int) ((Math.abs(date2.getTime() - date1.getTime()))/(1000*60*60*24));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return dayDifference;
	}
	
	public static int getDayDifferenceee(String dateRange) {
	    int dayDifference = 0;
	    if(dateRange != null && !dateRange.equals("")) {
	        String startDate = dateRange.split(" - ")[0];
	        String endDate = dateRange.split(" - ")[1];
	        try {
	            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
	            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
	            
	            dayDifference = (int) ((Math.abs(date2.getTime() - date1.getTime())) / (1000 * 60 * 60 * 24));
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	    }
	    return dayDifference;
	}
	
	public static String[] getTickerArray(List<MarketStatDto> marketDataList) {
		String[] tickers = new String[marketDataList.size()];
		int i = 0;
		for(MarketStatDto marketStatDto : marketDataList) {
			tickers[i] = marketStatDto.getCode();						
			i++;
		}
		
		return tickers;
	}
	
	public static boolean isNotInBetweenThreshold(Double value) {
		if(value > Constants.fvThreshold || value < (-1*Constants.fvThreshold)) {
			return true;
		}
		return false;
	}
	
	public static String getPreviousDate(String currentDate) {
		String previousDateString=currentDate;
		LocalDate dateStart = LocalDate.parse(previousDateString);
        LocalDate dayBeforeDate = dateStart.plusDays(1);
        previousDateString = dayBeforeDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		return previousDateString;
		
	}
	
	public static String getPreviousDatee(String currentDate) {
		String previousDateString=currentDate;
		LocalDate dateStart = LocalDate.parse(previousDateString);
        LocalDate dayBeforeDate = dateStart.minusDays(1);
        previousDateString = dayBeforeDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		return previousDateString;
		
	}
	
	public static String runExternalProcess(String workingDir, long execTimeLimit, String ticker, String days, String satrtDate, String endDate, String fileName) {
		
		
		String pythonScriptPath = "/opt/python/lrg/forecasting.py";
//		String pythonScriptPath ="F:/python/";
        //String[] commandd = {"/usr/bin/python3", pythonScriptPath, ticker, days, satrtDate,endDate,fileName};
		String[] commandd = {"/opt/python/lrg/myenv/bin/python3", pythonScriptPath, ticker, days, satrtDate,endDate,fileName};
//		String[] commandd = {"python3", pythonScriptPath, ticker, days, satrtDate,endDate,fileName};
        String tempDirPath = workingDir+"/guestPortfolioExcel/"+fileName;
        File tmpDir = new File(tempDirPath);
		ProcessBuilder builder = new ProcessBuilder(commandd);
		builder.directory(new File(workingDir));
		builder.redirectErrorStream(true);
		boolean hasProperExit = true;
		StringBuilder sb = new StringBuilder();
		try {
			long exeStartTime = System.currentTimeMillis();
			Process exeProc = builder.start();
			BufferedReader br = new BufferedReader(new InputStreamReader((exeProc.getInputStream())));
			String line;
			
			while((line = br.readLine()) != null){
				sb.append(line);
				if ((System.currentTimeMillis() - exeStartTime) > execTimeLimit){
					exeProc.destroy();
					hasProperExit = false;
					break;
				}
			}
		} catch (Exception ex) {
			logger.error("Error while running process for command: " + Arrays.toString(commandd), ex);
			throw new BusinessException("Error while running external process");
		}
		try {
			FileUtils.forceDelete(tmpDir);
		} catch (Exception ex) {
			System.out.println("Error removing temp folder due to "+ ex);
		}
		if(!hasProperExit){
			logger.error("Process interrupted for command:" + Arrays.toString(commandd));
			throw new BusinessException("No return data found. Please change input values and try again.");
		}
		return sb.toString();
	}
	
}
