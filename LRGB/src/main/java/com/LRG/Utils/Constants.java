package com.LRG.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Constants {

	public static final int SHORTFALL_MAX_LIST_SIZE = 156;
	public static final Double MAX_PORTFOLIO_RET = 0.16;
	public static final String DEFAULT_CONTRIBUTION_WITHDRAWL_TYPE = "ANNUAL";
	public static final String CONTRIBUTION = "CONTRIBUTION";
	public static final String WITHDRAWL = "WITHDRAWL";
	public static final int MAX_INVESTOR_AGE = 115;
	public static final int MAX_POST_RETIREMENT_AGE_SIMULATION = 25;
	public static final Double NUM_SIMULATIONS = 1000.0;
	public static final int MAX_SIMULATION_LINE = 7;
	public static final String MONEY_MKT_FUNDS = "MoneyMktFunds";
	public static final double DEFAULT_MIN_WEIGHT			 = 0.0;
	public static final double DEFAULT_MAX_WEIGHT			 = 0.25;
	public static final String DATE_MYSQL_FORMAT	 	= "yyyy-MM-dd";
	//public static final String volumeSuffix = "-Volume";
	public static final double PREFILL = -999999 ;
	public static final String subPortfolioDateSuffix = "-SubPortDate";
	public static final String OPTOUT_DATE				 = "DATE";
	public static final String OPTOUT_OPTMU				 = "OPT. MU";
	public static final String OPTOUT_OPTVOL			 = "OPT. VOL";
	public static final String TICKER_RETURN_FORMAT			 = "#.00000000000000000000";
	public static final String pDate = "pDate";
	public static final String pReturn = "pReturn";
	public static final String pPrice = "pPrice";
	public static final String SUBSCRIPTION_EXCEL_MAP_KEY = "PortfolioMetadata"; 
	public static final String SYSTEM_ERROR 			= "SYSTEM_ERROR";
//	public static final String EXE_FILE_PATH 			= "/opt/tomcat/lrg";
//	public static final String EXE_FILE_PATH 			= "/opt/tomcat/apache-tomcat-9.0.58/lrg";
	public static final String DEFAULT_TICKER_FILTER		 = "1.00000000";
	public static final int DEFAULT_NUM_ITERATIONS			 = 50;
	public static final String DEFAULT_OPTIM_MODE_OPTION	 = "CVAR";
	public static final long  EXECUTE_TIME_LIMIT_FOR_EXE = 1000 * 90 ;
	public static final String OPTOUT_OPTDAILYCVAR		 = "OPT. DAILY CVAR";
	
	public static final String[] portfolio_tickers	 	= {"BATBC","GP","UCB","RENATA","SQURPHARMA","BATASHOE","BXPHARMA",
															"SINGERBD","CITYBANK","DBH","ACMELAB","SQUARETEXT","BRACBANK","RUNNERAUTO",
															"ADNTEL","GENEXIL","COPPERTECH","SSSTEEL","DELTALIFE","IDLC","BGIC"};
	
	public static final String[] portfolio2_tickers	 	= {"LR Global Index","FD Index","SND Index","10-yr T-Bond Index","5-yr T-Bond Index"};
	public static final String WIN_FILE_PATH = "D:\\Mazhar\\";
	
	public static final double countPercentageForCompany = 0.4;
	public static final double fvThreshold = 0.5;
	
	public static ArrayList<String> getBondList(){
		ArrayList<String> bondList = new ArrayList<String>();
		
		bondList.add("AIBLPBOND");
		bondList.add("APSCLBOND");
		bondList.add("BEXGSUKUK");
		bondList.add("DEBARACEM");
		bondList.add("DEBBDLUGG");
		bondList.add("DEBBDWELD");
		bondList.add("DEBBDZIPP");
		bondList.add("DEBBXDENIM");
		bondList.add("DEBBXFISH");
		bondList.add("DEBBXKNI");
		bondList.add("DEBBXTEX");
		bondList.add("IBBL2PBOND");
		bondList.add("IBBLPBOND");
		bondList.add("PREBPBOND");
		bondList.add("SJIBLPBOND");
		bondList.add("PBLPBOND");
		bondList.add("CBLPBOND");
		
		return bondList;
	}
	
	public static ArrayList<String> getIgnoredTickerListForSectorPE(){
		ArrayList<String> sectorList = new ArrayList<String>();
		
		sectorList.add("FASFIN");
		sectorList.add("FIRSTFIN");
		sectorList.add("PREMIERLEA");
		sectorList.add("UNIONCAP");
		sectorList.add("ILFSL");	
		
		return sectorList;
	}
	
	public static ArrayList<String> populateForeignIndexList(){
		ArrayList<String> indexList = new ArrayList<String>();
		
		indexList.add("SPX Index");
		indexList.add("NDX Index");
		indexList.add("DAX Index");
		indexList.add("ESTX50 Index");
		indexList.add("N225 Index");		
		indexList.add("HSI Index");
		indexList.add("SPI Index");
		indexList.add("SENSEX Index");

		return indexList;
	}
	
	public static Map<String,String> populateForeignIndexDetailsMap(){
		Map<String,String> foreignIndexDetailsMap = new HashMap<String,String>();
		
		foreignIndexDetailsMap.put("SPX","S&P 500 Index (USA)");
		foreignIndexDetailsMap.put("NDX","Nasdaq 100 Index (USA)");
		foreignIndexDetailsMap.put("DAX","DAX 40 Index (Germany)");
		foreignIndexDetailsMap.put("ESTX50","EURO STOXX 50 Index (Europe)");
		foreignIndexDetailsMap.put("N225","Nikkei 225 Index (Japan)");
		foreignIndexDetailsMap.put("HSI","Hang Seng Index (Hongkong)");
		foreignIndexDetailsMap.put("SPI","Swiss Performance Index (Switzerland)");
		foreignIndexDetailsMap.put("SENSEX","BSE SENSEX Index (India)");
		
		return foreignIndexDetailsMap;
	}
	
	public static ArrayList<String> populateIndexList(){
		ArrayList<String> indexList = new ArrayList<String>();
		indexList.add("LR Global Index");
		indexList.add("SND Index");
		indexList.add("FD Index");
		indexList.add("5-yr T-Bond Index");
		indexList.add("10-yr T-Bond Index");
		
		indexList.add("1JANATAMF NAV Index");
		indexList.add("1STPRIMFMF NAV Index");
		indexList.add("ABB1STMF NAV Index");
		indexList.add("AIBL1STIMF NAV Index");
		indexList.add("ATCSLGF NAV Index");
		indexList.add("CAPMBDBLMF NAV Index");
		indexList.add("CAPMIBBLMF NAV Index");
		indexList.add("DBH1STMF NAV Index");
		indexList.add("EBL1STMF NAV Index");
		indexList.add("EBLNRBMF NAV Index");
		indexList.add("EXIM1STMF NAV Index");
		indexList.add("FBFIF NAV Index");
		indexList.add("GRAMEENS2 NAV Index");
		indexList.add("GREENDELMF NAV Index");
		indexList.add("ICB3RDNRB NAV Index");
		indexList.add("ICBAGRANI1 NAV Index");
		indexList.add("ICBAMCL2ND NAV Index");
		indexList.add("ICBEPMF1S1 NAV Index");
		indexList.add("ICBSONALI1 NAV Index");
		indexList.add("IFIC1STMF NAV Index");
		indexList.add("IFILISLMF1 NAV Index");
		indexList.add("LRGLOBMF1 NAV Index");
		indexList.add("MBL1STMF NAV Index");
		indexList.add("NCCBLMF1 NAV Index");
		indexList.add("PF1STMF NAV Index");
		indexList.add("PHPMF1 NAV Index");
		indexList.add("POPULAR1MF NAV Index");
		indexList.add("PRIME1ICBA NAV Index");
		indexList.add("RELIANCE1 NAV Index");
		indexList.add("SEMLFBSLGF NAV Index");
		indexList.add("SEMLIBBLSF NAV Index");
		indexList.add("SEMLLECMF NAV Index");
		indexList.add("TRUSTB1MF NAV Index");
		indexList.add("VAMLBDMF1 NAV Index");
		indexList.add("VAMLRBBF NAV Index");
		return indexList;
	}
	
	public static Map<Integer,Double[]> populateMasterDataMap(){
		Map<Integer,Double[]> masterDataMap = new HashMap<Integer,Double[]>();
		masterDataMap.put(0,new Double[]{0.006519,	0.005377});
		masterDataMap.put(	1,new Double[]{	0.000462,	0.000379});
		masterDataMap.put(	2,new Double[]{	0.000291	,0.000221});
		masterDataMap.put(	3,new Double[]{	0.000209,	0.000162});
		masterDataMap.put(	4,new Double[]{	0.000176,	0.000133});
		masterDataMap.put(	5,new Double[]{	0.000159,	0.000119});
		masterDataMap.put(	6,new Double[]{	0.000146,	0.000109});
		masterDataMap.put(	7,new Double[]{	0.000133,	0.000101});
		masterDataMap.put(	8,new Double[]{	0.000118,	0.000096});
		masterDataMap.put(	9,new Double[]{	0.000102,	0.000093});
		masterDataMap.put(	10,new Double[]{	0.000091,	0.000094});
		masterDataMap.put(	11,new Double[]{	0.000096,	0.000100});
		masterDataMap.put(	12,	new Double[]{0.000128,	0.000112});
		masterDataMap.put(	13,new Double[]{	0.000195,	0.000134});
		masterDataMap.put(	14,new Double[]{	0.000288,	0.000162});
		masterDataMap.put(	15,new Double[]{	0.000389,	0.000194});
		masterDataMap.put(	16,new Double[]{	0.000492,	0.000226});
		masterDataMap.put(	17,new Double[]{	0.000607,	0.000261});
		masterDataMap.put(	18,new Double[]{	0.000735,	0.000297});
		masterDataMap.put(	19,new Double[]{	0.000869,	0.000334});
		masterDataMap.put(	20,new Double[]{	0.001011,	0.000373});
		masterDataMap.put(	21,new Double[]{	0.001145,	0.000412});
		masterDataMap.put(	22,new Double[]{	0.001246,	0.000446});
		masterDataMap.put(	23,new Double[]{	0.001301,	0.000472});
		masterDataMap.put(	24,new Double[]{	0.001321,	0.000493});
		masterDataMap.put(	25,new Double[]{	0.001330,	0.000513});
		masterDataMap.put(	26,new Double[]{	0.001345,	0.000537});
		masterDataMap.put(	27,new Double[]{	0.001363,	0.000563});
		masterDataMap.put(	28,	new Double[]{0.001391,	0.000593});
		masterDataMap.put(	29,new Double[]{	0.001427,	0.000627});
		masterDataMap.put(	30,new Double[]{	0.001467,	0.000664});
		masterDataMap.put(	31,new Double[]{	0.001505,	0.000705});
		masterDataMap.put(	32,new Double[]{	0.001541,	0.000748});
		masterDataMap.put(	33,new Double[]{	0.001573,	0.000794});
		masterDataMap.put(	34,new Double[]{	0.001606,	0.000845});
		masterDataMap.put(	35,new Double[]{	0.001648,	0.000903});
		masterDataMap.put(	36,new Double[]{	0.001704,	0.000968});
		masterDataMap.put(	37,new Double[]{	0.001774,	0.001038});
		masterDataMap.put(	38,new Double[]{	0.001861,	0.001113});
		masterDataMap.put(	39,new Double[]{	0.001967,	0.001196});
		masterDataMap.put(	40,	new Double[]{0.002092,	0.001287});
		masterDataMap.put(	41,new Double[]{	0.002240,	0.001393});
		masterDataMap.put(	42,new Double[]{	0.002418,	0.001517});
		masterDataMap.put(	43,new Double[]{	0.002629,	0.001662});
		masterDataMap.put(	44,new Double[]{	0.002873	,0.001827});
		masterDataMap.put(	45,new Double[]{	0.003146,	0.002005});
		masterDataMap.put(	46,new Double[]{	0.003447,	0.002198});
		masterDataMap.put(	47,new Double[]{	0.003787,	0.002412});
		masterDataMap.put(	48,new Double[]{	0.004167,	0.002648});
		masterDataMap.put(	49,new Double[]{	0.004586,	0.002904});
		masterDataMap.put(	50,new Double[]{	0.005038,	0.003182});
		masterDataMap.put(	51,new Double[]{	0.005520,	0.003473});
		masterDataMap.put(	52,new Double[]{	0.006036,	0.003767});
		masterDataMap.put(	53,new Double[]{	0.006587,	0.004058});
		masterDataMap.put(	54,new Double[]{	0.007170,	0.004352});
		masterDataMap.put(	55,new Double[]{	0.007801,	0.004681});
		masterDataMap.put(	56,new Double[]{	0.008466,	0.005040});
		masterDataMap.put(	57,new Double[]{	0.009133,	0.005400});
		masterDataMap.put(	58,new Double[]{	0.009792,	0.005756});
		masterDataMap.put(	59,new Double[]{	0.010462,	0.006128});
		masterDataMap.put(	60,new Double[]{	0.011197,	0.006545});
		masterDataMap.put(	61,new Double[]{	0.012009,	0.007034});
		masterDataMap.put(	62,new Double[]{	0.012867,	0.007607});
		masterDataMap.put(	63,new Double[]{	0.013772,	0.008281});
		masterDataMap.put(	64,new Double[]{	0.014749,	0.009057});
		masterDataMap.put(	65,new Double[]{	0.015852,	0.009953});
		masterDataMap.put(	66,new Double[]{	0.017097,	0.010950});
		masterDataMap.put(	67,new Double[]{	0.018463,	0.012010});
		masterDataMap.put(	68,new Double[]{	0.019959,	0.013124});
		masterDataMap.put(	69,new Double[]{	0.021616,	0.014330});
		masterDataMap.put(	70,new Double[]{	0.023528,	0.015728});
		masterDataMap.put(	71,new Double[]{	0.025693,	0.017338});
		masterDataMap.put(	72,new Double[]{	0.028041,	0.019108});
		masterDataMap.put(	73,new Double[]{	0.030567	,0.021041});
		masterDataMap.put(	74,	new Double[]{0.033347,	0.023191});
		masterDataMap.put(	75,new Double[]{	0.036572,	0.025713});
		masterDataMap.put(	76,new Double[]{	0.040276,	0.028609});
		masterDataMap.put(	77,new Double[]{	0.044348,	0.031760});
		masterDataMap.put(	78,new Double[]{	0.048797,	0.035157});
		masterDataMap.put(	79,new Double[]{	0.053739,	0.038920});
		masterDataMap.put(	80,new Double[]{	0.059403,	0.043289});
		masterDataMap.put(	81,new Double[]{	0.065873,	0.048356});
		masterDataMap.put(	82,new Double[]{	0.073082,	0.054041});
		masterDataMap.put(	83,new Double[]{	0.081070,	0.060384});
		masterDataMap.put(	84,new Double[]{	0.089947,	0.067498});
		masterDataMap.put(	85,new Double[]{	0.099842	,0.075516});
		masterDataMap.put(	86,new Double[]{	0.110863	,0.084556});
		masterDataMap.put(	87,new Double[]{	0.123088,	0.094703});
		masterDataMap.put(	88,new Double[]{	0.136563,	0.106014});
		masterDataMap.put(	89,new Double[]{	0.151299,	0.118513});
		masterDataMap.put(	90,new Double[]{	0.167291,	0.132206});
		masterDataMap.put(	91,new Double[]{	0.184520,	0.147092});
		masterDataMap.put(	92,new Double[]{	0.202954,	0.163154});
		masterDataMap.put(	93,new Double[]{	0.222555,	0.180371});
		masterDataMap.put(	94,new Double[]{	0.243272,	0.198714});
		masterDataMap.put(	95,new Double[]{	0.263821,	0.217264});
		masterDataMap.put(	96,	new Double[]{0.283833,	0.235735});
		masterDataMap.put(	97,	new Double[]{0.302916,	0.253810});
		masterDataMap.put(	98,	new Double[]{0.320672,	0.271155});
		masterDataMap.put(	99,	new Double[]{0.336706,	0.287424});
		masterDataMap.put(	100,new Double[]{	0.353541,	0.304670});
		masterDataMap.put(	101,new Double[]{	0.371218,	0.322950});
		masterDataMap.put(	102,new Double[]{	0.389779,	0.342327});
		masterDataMap.put(	103,new Double[]{	0.409268,	0.362867});
		masterDataMap.put(	104,new Double[]{	0.429732,	0.384639});
		masterDataMap.put(	105,new Double[]{	0.451218,	0.407717});
		masterDataMap.put(	106,new Double[]{	0.473779,	0.432180});
		masterDataMap.put(	107,new Double[]{	0.497468,	0.458111});
		masterDataMap.put(	108,new Double[]{	0.522341,	0.485597});
		masterDataMap.put(	109,new Double[]{	0.548458,	0.514733});
		masterDataMap.put(	110,new Double[]{	0.575881,	0.545617});
		masterDataMap.put(	111,new Double[]{	0.604675,	0.578354});
		masterDataMap.put(	112,new Double[]{	0.634909,	0.613055});
		masterDataMap.put(	113,new Double[]{	0.666655,	0.649839});
		masterDataMap.put(	114,new Double[]{	0.699987,	0.688829});
		masterDataMap.put(	115,new Double[]{	0.734987,	0.730159});
		masterDataMap.put(	116,new Double[]{	0.771736,	0.771736});
		masterDataMap.put(	117,new Double[]{	0.810323,	0.810323});
		masterDataMap.put(	118,new Double[]{	0.850839,	0.850839});
		masterDataMap.put(	119,new Double[]{	0.893381,	0.893381});
		return masterDataMap;
	}
}
