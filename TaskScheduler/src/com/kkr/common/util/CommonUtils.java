package com.kkr.common.util;

import java.io.File;
import java.io.FileWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.kkr.app.model.TaskDto;
import com.kkr.app.model.TaskStatusDto;

public class CommonUtils {
	
	public static List<String> getHolidaysList() throws Exception{
		List<String> holidayList = new ArrayList<>();
		Connection conn = CommonUtils.connectDC();
		Statement stmt = conn.createStatement();
		ResultSet rSet = stmt.executeQuery("select holiday_date from market_holidays");
		while(rSet.next()) {
			holidayList.add(rSet.getString(1));
		}
		if(conn!=null && !conn.isClosed()) conn.close();
		return holidayList;
		
	}
	
	public static boolean isHoliday() throws Exception {
		boolean isHoliday = false;
		LocalDate date = LocalDate.now();
		for(String s : getHolidaysList()) {
			if(s.equals(date.toString())) {
				isHoliday = true;
			}
		}
		return isHoliday;		
	}
	
	public static boolean isMarketOpen() throws Exception {
		Connection conn = CommonUtils.connectDC();
		Statement stmt = conn.createStatement();
		ResultSet rSet = stmt.executeQuery("select value from system_configuration where id in(1,2)");
		String marketStartTime;
		String marketEndTime;
		rSet.next();
		marketStartTime = rSet.getString(1);
		rSet.next();
		marketEndTime = rSet.getString(1);
		LocalTime time = LocalTime.now();
		if(conn!=null && !conn.isClosed()) conn.close();
		return time.isAfter((LocalTime.parse(marketStartTime)).minusMinutes(5)) 
				&& time.isBefore((LocalTime.parse(marketEndTime)).plusMinutes(10));		
	}
	
	public static String formatDate(Date date, String format) {
		if(null != date) {		
			return new SimpleDateFormat(format).format(date);
		}
		return null;
	}
	
	public static Date formatDate(String strDate, String format) {
		if(null != strDate && !strDate.isEmpty()) {
			try {
				return new SimpleDateFormat(format).parse(strDate);
			} catch (ParseException ex) {
				System.out.println("Error while parsing date: " + strDate+" due to "+ex);
			}
		}
		return null;
	}
	
	public static Date formatSqlDate(Date date, String format) {
		if (null != date) {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				String sqlDate = dateFormat.format(date);
				return dateFormat.parse(sqlDate);
			} catch (ParseException ex) {
				System.out.println("Error while parsing date: " + date+" due to "+ex);
			}
		}
		return null;
	}
	 
	 public static java.sql.Timestamp convertUtilToSqlDate(Date date) {
		 return new java.sql.Timestamp(date.getTime());
	 }
	
	public static int getDaysDifference(Date d1, Date d2) {
		int daysdiff=0;
		long diff = d1.getTime() - d2.getTime();
		long diffDays = diff / (24 * 60 * 60 * 1000)+1;
		daysdiff = (int) diffDays;
		return daysdiff;
	}	

	public static void sendMail(TaskStatusDto taskStatusDto, String mailBodyContent, String sub) {
		List<String> toRecipients = new ArrayList<String>();
		toRecipients.add("joy@questbdc.com");
		toRecipients.add("sivlee@lrglobalbd.com");
		toRecipients.add("fahim@lrglobalbd.com");
		
		try {
			sendMail("joy@questbdc.com", "Manual EPS Updater", toRecipients, null, null, InetAddress.getLocalHost().getHostAddress() +" : "+sub, mailBodyContent);
		} catch (UnknownHostException e) {
			System.out.println("Error while sending email: Not able to get IP Address: "+e);
		}
	}
	
	public static void sendMail(String fromEmailId, String fromName, List<String> toRecipients, List<String> ccRecipients, List<String> bccRecipients, String subject, String msg) {
		try {
			Properties properties = System.getProperties();
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "true");
			//properties.put("mail.smtp.starttls.required", "true");
			//properties.put("mail.smtp.ssl.enable", "false");
			//properties.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
			properties.put("mail.smtp.host", "smtp.gmail.com");
			properties.put("mail.smtp.port", "587");
			properties.put("userName", fromEmailId);
			properties.put("password", "JoY#^358");
			final String username =  fromEmailId;
			final String password =  "JoY#^358";
			Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
				protected PasswordAuthentication  getPasswordAuthentication() {
					return new PasswordAuthentication(username,password);
				}
			});
//			session.setDebug(true);
			MimeMessage message = new MimeMessage(session);
			message.addHeader("Content-type", "text/html");
			message.setFrom(new InternetAddress(fromEmailId, fromName));
			message.setReplyTo(new Address[] { new InternetAddress(fromEmailId,fromName) });
			StringBuilder mailTo = new StringBuilder();
			StringBuilder mailcc = new StringBuilder();
			if (toRecipients != null) {
				for (String email : toRecipients) {
					message.addRecipients(RecipientType.TO, email);
					mailTo.append(email).append(" ");
				}
			}
			if (ccRecipients != null) {
				for (String email : ccRecipients) {
					message.addRecipients(RecipientType.CC, email);  
					mailcc.append(email).append(" ");
				}
			}
			if (bccRecipients != null) {
				for (String email : bccRecipients) {
					message.addRecipients(RecipientType.BCC, email);
					mailTo.append(email).append(" ");
				}
			}
			message.setSubject(subject);
			message.setContent(msg, "text/plain");
			message.setSentDate(new Date());
			Transport.send(message);
			System.out.println("mail \""+ subject +"\" successfully sent to: " + mailTo+"mailcc"+mailcc);
		} catch (Exception ex) {
			System.out.println("Error while sending mails to" + toRecipients + ex);
		}
	}

	public static void truncateTables(List<String> tables, Connection connection) throws SQLException {
		Statement statement = connection.createStatement();
		for (String table : tables) {
			statement.execute("truncate "+table);
		}
	}
	
	public static TaskDto getUpdatedTaskStatusData(Long task_Id) {
		TaskDto taskDto = null;
		Connection con = null;
		try {
			con = CommonUtils.connectDC();
	        String sql = "select task_id, name, executer_class, frequency_minutes, last_run_start, last_run_complete, last_run_status, schedule_type, "
	        		+ "force_run, run_on_start_up, scheduled_time, scheduled_date, scheduled_day, skip_on_days, notify_via_mail, run_type, "
	        		+ "check_market_open  from dse_analyzer_loader.scheduled_task where task_id = "+task_Id+" and active = 1 "
	        		+ "and (last_run_status is null or last_run_status != '"+Constants.SCHED_STAT_RUNNING+"')";
	        if(con != null) {
		        Statement stmt=(Statement) con.createStatement();  
		        ResultSet rs=stmt.executeQuery(sql);
		        while(rs.next()) {
	        		taskDto = new TaskDto(rs.getInt("task_id"), rs.getString("executer_class"), rs.getInt("frequency_minutes"), 
	        				rs.getString("schedule_type"), rs.getBoolean("force_run"), rs.getBoolean("run_on_start_up"), 
	        				rs.getString("scheduled_day"), rs.getString("scheduled_date"), rs.getString("scheduled_time"), rs.getBoolean("notify_via_mail"), 
	        				rs.getString("last_run_complete"), rs.getString("last_run_start"), rs.getString("run_type"),rs.getString("skip_on_days"),
	        				rs.getString("last_run_status"), rs.getBoolean("check_market_open"));
		        }
	        }
		} catch (Exception e) {
			System.out.println("Error while fetching updated task status. " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if(con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("Error while closing. " + e2.getMessage());
				e2.printStackTrace();
			}
		}
        return taskDto;
	}
	
	public static void truncateTable(Connection con,String tableName) throws SQLException {
		Statement stLocal = con.createStatement();
		stLocal.executeUpdate("truncate table dse_analyzer."+tableName);
	}
	
	public static List<String> getTickerList(Connection conLocal) throws SQLException {
		List<String> tickerList = new ArrayList<String>();
		String query = "select code from market_data";
		Statement stLocal = conLocal.createStatement();
		ResultSet rs = stLocal.executeQuery(query);
		while(rs.next()) {
			tickerList.add(rs.getString(1));
		}
		return tickerList;
	}
	
	public static List<String> getMFTickerList(Connection conLocal) throws SQLException {
		List<String> tickerList = new ArrayList<String>();
		String query = "select m.code from market_data m inner join company c on m.code=c.code and c.sector='Mutual Fund'";
		Statement stLocal = conLocal.createStatement();
		ResultSet rs = stLocal.executeQuery(query);
		while(rs.next()) {
			tickerList.add(rs.getString(1));
		}
		return tickerList;
	}
	
	public static Connection connectDSE() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://202.84.32.15:3306/mdsdata?rewriteBatchedStatements=true"
				+ "&&useSSL=false", "LRglobal_user", "LR@963g_bd");
		con.setAutoCommit(true);
		return con;
	}
	
//	public static Connection connectMainDB() throws ClassNotFoundException, SQLException {
//		Class.forName("com.mysql.jdbc.Driver");
//		Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://119.148.4.4:3306/dse_analyzer?rewriteBatchedStatements=true"
//				+ "&&useSSL=false", "developer", "developer4");
//		con.setAutoCommit(true);
//		return con;
//	}
	
	public static Connection connectDC() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://192.168.100.12:3306/dse_analyzer?rewriteBatchedStatements=true&&useSSL=false", "lrgb_loader",
		"developer4");
		con.setAutoCommit(true);
		return con;
	}
	
	public static Connection connectLocal() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/dse_analyzer?rewriteBatchedStatements=true"
				+ "&&useSSL=false", "root", "admin123");
		con.setAutoCommit(true);
		return con;
	}
	
	public static FileWriter prepareOutputFile(String taskName){
		FileWriter file_Writer = null;
		try {
			String actualPath=Constants.outputFileBasePath+"\\"+taskName+"_"+DateUtils.getFileNameOnCurrentDate()+".txt";
			File file = new File(actualPath);
			if(file.exists() && !file.isDirectory()) { 
				// file already exist;
			}else{
				// create text file
				file.createNewFile();
			}				
			file_Writer=new FileWriter (file, true);
				  
			file_Writer.write(System.lineSeparator());
			file_Writer.write("*************************************************************");
			file_Writer.write(System.lineSeparator());
			file_Writer.write(new Date().toString());
			file_Writer.write(System.lineSeparator());
			file_Writer.write("*************************************************************");
			file_Writer.write(System.lineSeparator());				  				  			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		  
		return file_Writer;
	  }
	
}
