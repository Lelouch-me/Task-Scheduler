package com.kkr.common.util;


public class Constants {

	//Scheduler status Running
	public static final String SCHED_STAT_RUNNING	 = "RUNNING";
	public static final String SCHED_STAT_TERMINATED = "TERMINATED";
	public static final String SCHED_STAT_ERROR = "ERROR";
	public static final int JOB_EXPIRE_MINUTES	 	 = 30;
	
	//News related constants
	public static final int ARTICLE_DESC_MAX_LEN 	 = 990;
	public static final int MAX_ARTICLES_TO_RETAIN	 = 1000;
	public static final long BRK_NEWS_INTERVAL		 = 10*60*1000; 
	public static final int ZERO_INDEX				 = 0;
	public static final String WHITE_SPACE			 = " ";
	public static final Character SEARCHABLE_YES	 = 'Y';
	public static final Character SEARCHABLE_NO		 = 'N';
	public static final long INTERVAL_MAIL		 	 = 21600000;
	
	//Date/Text formats Wed Feb 15 00:00:00 IST 2012
	public static final String DATE_FORMAT_SOCIAL		= "yyyy/MM/dd";
	public static final String DATE_FORMAT			 	= "MM/dd/yyyy";
	public static final String DATE_SQL_FORMAT			= "dd-MMM-yyyy";
	public static final String DATE_MYSQL_FORMAT	 	= "yyyy-MM-dd";
	public static final String DATETIME_MYSQL_FORMAT 	= "yyyy-MM-dd HH:mm:ss";
	public static final String DATETIME_QM_FORMAT	 	= "yyyy-MM-dd'T'HH:mm:ssX";
	public static final String DATETIME_FB_FORMAT 		= "EEE MMM dd HH:mm:ss z yyyy";
	public static final String ALPHA_NUMERIC_ONLY_REGEX = "[^a-zA-Z0-9]+";
	public static final String TIMEZONE_EST				= "America/New_York";
	public static final String DEFAULT_DATE				= "1900-01-01";
	public static final String DATE_FORMAT_STRING		= "dd-MMM-yyyy";
	public static final String DATE_FORMAT_STRING_FACEBOOK	= "MM/dd/yy";
	public static final String DATE_FORMAT_STRING_FB	= "dd/MM/yy";
	public static final String DATE_FORMAT_EMPLOYEE_FEEDBACK	= "dd/MM/yyyy";
	public static final String DATE_FORMAT_YEAR			= "yyyy";
	public static final String DATE_FORMAT_TIME 		= "HH:mm:ss";
	public static final String DATETIME_INTRADAY_FORMAT = "EEE MMM dd HH:mmss z yyyy";
	public static final String QM_INTRADAY_START_TIME	= "09:45:00";//"09:30:00-0400";//Added  15 min. since data is available after 15 min. from market open time
	public static final String QM_INTRADAY_END_TIME	= "16:15:00";//"09:30:00-0400";//Added  15 min. since data is available after 15 min. from market open time and we are checking for next 15 min as well
	
	// Graph 
	public static final String DURATION_CURRENT_YEAR 	= "ytd";
	public static final String DURATION_ONE_MONTH 		= "1m";
	public static final String DURATION_THREE_MONTH 	= "3m";
	public static final String DURATION_SIX_MONTH 		= "6m";
	public static final String DURATION_ONE_YEAR 		= "1y";
	public static final String DURATION_TWO_YEAR 		= "2y";
	public static final String DURATION_THREE_YEAR 		= "3y";
	public static final String DURATION_FOUR_YEAR 		= "4y";
	public static final String DURATION_FIVE_YEAR 		= "5y";
	public static final String DURATION_SIX_YEAR 		= "6y";
	public static final String DURATION_SEVEN_YEAR 		= "7y";
	public static final String DURATION_EIGHT_YEAR 		= "8y";
	public static final String DURATION_NINE_YEAR 		= "9y";
	public static final String DURATION_TEN_YEAR 		= "10y";
    //public static final String STRATEGY_URL				= "http://ec2-52-25-104-213.us-west-2.compute.amazonaws.com:8080/techanalysis/services/rest/PortfolioTech/getAnalysis";
    public static final String DURATION_Custom			= "Custom";
    
    public static final String QM_ENTERPRISE_TOKEN = "QM_ENTERPRISE_TOKEN";
    public static final String NEWS_CAT_MY_NEWS				= "My News";
    public static final String MONEY_MKT_FUNDS				 = "MoneyMktFunds";
    public static final String TICKER_TYPE_FIXED_INCOME = "FixedIncome";
    
    public static final String Scheduled_Daily  = "fixedDaily";
    public static final String Scheduled_Weekly  = "fixedWeekly";
    public static final String Scheduled_Monthly  = "fixedMonthly";
    public static final String Scheduled_continuous  = "continuous";
    
    public static final String RUN_TYPE_LOAD  = "LOAD";
    public static final String RUN_TYPE_MOVE  = "MOVE";
    public static final String RUN_TYPE_AUTO  = "AUTO";
    public static final String LAST_RUN_lOAD_SUCCESS  = "LOAD_SUCCESS";
    public static final String STATUS_SUCCESS="SUCCESS";
        
    public static double year = 365;
    
    public static final int FUND_LOAD__MAX_BATCH = 10;
    public static final int PRICE_LOAD__MAX_BATCH = 10;
    public static final String FULL_LOAD_START_DATE = "2000-01-01";
    public static final String SYSTEM_CONFIG_LOG_LEVEL	=	"LOG_LEVEL";
    
    
    public static final String OPTOUT_CVAR = "CVAR";
    
  //Analyzer Report
  	public static final int NO_OF_QTR = 4;
  	public static final double TRADINGDAYS_PER_YEAR = 252.0;
  	public static final double RISK_FREE_RATE = 2.0;
  	public static final double HURDLE_RATIO = 2.0;
  	public static final int REPORT_OUTPUT_EXPIRATION_TIME = 600;	//10 Minutes
  	public static final String cumulativeReturn = "Cumulative Growth Rate (%)";
  	public static final String avgGrowth = "Average Growth Rate (%)";
  	public static final String trend = "Trend";
  	public static final int AnalyzerDATALOAD__MAX_BATCH = 10;
  	public static final int ANALYZER_NO_OF_DAYS = 232;
  	public static final double MAX_RETURN_THRESHOLD = 1000; 
  	public static final String ANALYZER_CORRELATION_THRESHOLD_PREFIX = "ANALYZER_THRESHOLD_";
  	
  	public static final String outputFileBasePath="D:\\LRG Analyzer DI Output Files";

  	public static String[] customIndex = {"LR Global Index","5-yr T-Bond Index","10-yr T-Bond Index","FD Index","SND Index"};
	
	public static String[] foreignIndex = {"SPX Index","NDX Index","DAX Index","ESTX50 Index","N225 Index","HSI Index","SPI Index","SENSEX Index"};
	
	public static String[] mutualFundNAVIndex = {"1JANATAMF NAV Index","EXIM1STMF NAV Index","GRAMEENS2 NAV Index","ICB3RDNRB NAV Index",
			"IFIC1STMF NAV Index","LRGLOBMF1 NAV Index","RELIANCE1 NAV Index","SEMLLECMF NAV Index","TRUSTB1MF NAV Index","VAMLBDMF1 NAV Index",
			"1STPRIMFMF NAV Index","ABB1STMF NAV Index","AIBL1STIMF NAV Index","ATCSLGF NAV Index","CAPMBDBLMF NAV Index","CAPMIBBLMF NAV Index",
			"DBH1STMF NAV Index","EBL1STMF NAV Index","EBLNRBMF NAV Index","FBFIF NAV Index","GREENDELMF NAV Index","ICBAGRANI1 NAV Index",
			"ICBAMCL2ND NAV Index","ICBEPMF1S1 NAV Index","ICBSONALI1 NAV Index","IFILISLMF1 NAV Index","MBL1STMF NAV Index","NCCBLMF1 NAV Index",
			"PF1STMF NAV Index","PHPMF1 NAV Index","POPULAR1MF NAV Index","PRIME1ICBA NAV Index","SEMLFBSLGF NAV Index","SEMLIBBLSF NAV Index",
			"VAMLRBBF NAV Index"};
}


