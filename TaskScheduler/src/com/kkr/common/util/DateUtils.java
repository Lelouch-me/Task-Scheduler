package com.kkr.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

	public static String stringTodate(String date, String formatter, String format) throws Exception
	{
		//System.out.println(  "String   "+   date+ " formatter  "+ formatter+" format   "+ format);
		SimpleDateFormat desiredFormat = new SimpleDateFormat(format);
		SimpleDateFormat dateFormatter = new SimpleDateFormat(formatter); 

		Date newdate = null;
		String newDateString = null;
	    try {
	        newdate = dateFormatter.parse(date);
	        newDateString = desiredFormat.format(newdate);
	        //System.out.println(newDateString);
	    } catch (ParseException e) {
	        e.printStackTrace();
	        throw e;
	    }
		//System.out.println("newDateString : "+newDateString);
		return newDateString;
		
	}
	
	public static String getYearFromCurrentDate()
	{
		Date date=new Date();
		Calendar cal=new GregorianCalendar();
		cal.setTime(date);
		int year=cal.get(Calendar.YEAR);	
	    return String.valueOf(year);
	}
	
	public static String getPreviousDate(Date lastDate, String format, int noOfDays) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(lastDate);
		cal.add(Calendar.DATE, -noOfDays);
		return formatDate(cal.getTime(), format);
	}
	
	public static String formatDate(Date date, String format) {
		if(null != date) {		
			return new SimpleDateFormat(format).format(date);
		}
		return null;
	}
	
	   public static String getFileNameOnCurrentDate()
	    {
	    	Date current_date=new Date();
	    	String st_date=new SimpleDateFormat("yyyy-MM-dd").format(current_date);
	    	st_date=st_date.replace("-", "_");
	    	return st_date;
	    }
}
