package com.LRG.controller;

import java.sql.SQLException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.LRG.Utils.CommonUtils;
import com.LRG.model.EconomicIndicatorsDto;
import com.LRG.model.EconomyDto;
import com.LRG.model.MarketStatDto;
import com.LRG.repository.HolidayRepository;
import com.LRG.repository.SysConfigRepository;
import com.LRG.service.EconomicIndicatorsService;
import com.LRG.service.EconomicYieldService;
import com.LRG.service.EconomyService;
import com.LRG.service.MarketStatService;

@Controller
public class EconomicIndicatorControlller {
	
	@Autowired
	private EconomicIndicatorsService economic_indicatorsService;
	
	@Autowired
	private EconomicYieldService economic_yieldService;
	
	@Autowired
	private MarketStatService marketStatService;
	
	@Autowired
	private EconomyService economyService;
		
	@Autowired
	private HolidayRepository holidayRepository;
	
	@Autowired
	private SysConfigRepository sysConRepository;
	
		
	@GetMapping(value = {"/economy"})
	public String economyChart(ModelMap modelMap) throws ClassNotFoundException, SQLException {
		List<String> headerList=new ArrayList<String>();  
		List<MarketStatDto> marketDataList = marketStatService.getAllIntradayData();
		boolean isMarketOpen = CommonUtils.isMarketOpen(holidayRepository,sysConRepository);
		String[] tickers = CommonUtils.getTickerArray(marketDataList);
		List<EconomyDto> economyList = economyService.getEconomy();	
		modelMap.addAttribute("tickerData", marketDataList);
		modelMap.addAttribute("marketOpen", isMarketOpen);
		modelMap.addAttribute("tickers", tickers);
		modelMap.addAttribute("economy", economyList);
		
		List<EconomicIndicatorsDto> monthlyValueList = economic_indicatorsService.getMonthlyData();
		String exchageDate = null;
		String exchageDatePrevious = null;
		String latestDate=null;
		for (EconomicIndicatorsDto th : monthlyValueList){
			String endMonthValue=new SimpleDateFormat("MMM-yy").format(th.getMonth());
			exchageDate=new SimpleDateFormat("yyyy-MM-dd").format(th.getMonth());			
			Calendar cal = Calendar.getInstance();
	        cal.setTime(th.getMonth());
	        cal.add(Calendar.MONTH, 1);
	        latestDate=new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			String monthValue = new SimpleDateFormat("MMM-yy").format(th.getLastMonth());
			exchageDatePrevious=new SimpleDateFormat("yyyy-MM-dd").format(th.getLastMonth());						
			headerList.add(endMonthValue);
			headerList.add(monthValue);
			if(headerList.size()==2)
				break;
		}
		
		Map<String, Double> exchangeRate = new HashMap<String,Double>();
		//Map<String, Double> exchangeRatePrevious = new HashMap<String,Double>();	
		exchangeRate=economic_indicatorsService.getExchangeData(latestDate,exchageDate,exchageDatePrevious);
		//exchangeRatePrevious=economic_indicatorsService.getExchangeData(exchageDatePrevious,exchageDate);
		modelMap.addAttribute("monthly", monthlyValueList);
		modelMap.addAttribute("header", headerList);
		modelMap.addAttribute("exchange", exchangeRate);
		//modelMap.addAttribute("exchangePrevious", exchangeRatePrevious);
//		System.out.println("showing economy_chart....");
		return "economy";
	}
	
	@RequestMapping("/economyExportChart")
	@ResponseBody
	public String getExportDate(@RequestParam("months") String months,@RequestParam("endMonth") String lastMonth) throws ParseException {
		
		Date d = new SimpleDateFormat("yyyy-MM").parse(months);
		Date d2 = new SimpleDateFormat("yyyy-MM").parse(lastMonth);
		String monthValue = new SimpleDateFormat("yyyy-MM-dd").format(d);
		String endMonthValue=new SimpleDateFormat("yyyy-MM-dd").format(d2);
		Map<Date,Double> exportMap = economic_indicatorsService.getIndicatorData(monthValue,endMonthValue,"Export");
		
		JSONObject obj = new JSONObject();
		JSONArray monthArray = new JSONArray();
		JSONArray priceArray = new JSONArray();
		
		for(Map.Entry<Date,Double> entry : exportMap.entrySet()) {
			
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			String s = formatter.format(entry.getKey());			
			d = new SimpleDateFormat("yyyy-MM-dd").parse(s);
			String month = new SimpleDateFormat("MMM-yy").format(d);
			
			monthArray.put(month);
			priceArray.put(entry.getValue());
		}		
		obj.put("month",monthArray);
		obj.put("price",priceArray);
		obj.put("name1", "Export");
		
		return obj.toString();		
	}
	
	@RequestMapping("/foreignExchangeChart")
	@ResponseBody
	public String getForeignExchange(@RequestParam("months") String months,@RequestParam("endMonth") String lastMonth) throws ParseException {
		
		Date d = new SimpleDateFormat("yyyy-MM").parse(months);
		Date d2 = new SimpleDateFormat("yyyy-MM").parse(lastMonth);
		String monthValue = new SimpleDateFormat("yyyy-MM-dd").format(d);
		String endMonthValue=new SimpleDateFormat("yyyy-MM-dd").format(d2);
		Map<Date,Double> importtMap = economic_indicatorsService.getIndicatorData(monthValue,endMonthValue,"Foreign Exchange Reserve");
		
		JSONObject obj = new JSONObject();
		JSONArray monthArray = new JSONArray();
		JSONArray priceArray = new JSONArray();
		
		for(Map.Entry<Date,Double> entry : importtMap.entrySet()) {
			
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			String s = formatter.format(entry.getKey());			
			d = new SimpleDateFormat("yyyy-MM-dd").parse(s);
			String month = new SimpleDateFormat("MMM-yy").format(d);
			
			monthArray.put(month);
			priceArray.put(entry.getValue());
		}		
		obj.put("month",monthArray);
		obj.put("price",priceArray);
		obj.put("name1", "Foreign Exchange");
		
		return obj.toString();		
	}
	
	@RequestMapping("/remittanceChart")
	@ResponseBody
	public String getRemittance(@RequestParam("months") String months,@RequestParam("endMonth") String lastMonth) throws ParseException {
		
		Date d = new SimpleDateFormat("yyyy-MM").parse(months);
		Date d2 = new SimpleDateFormat("yyyy-MM").parse(lastMonth);
		String monthValue = new SimpleDateFormat("yyyy-MM-dd").format(d);
		String endMonthValue=new SimpleDateFormat("yyyy-MM-dd").format(d2);
		Map<Date,Double> remittancetMap = economic_indicatorsService.getIndicatorData(monthValue,endMonthValue,"Remittance");
		
		JSONObject obj = new JSONObject();
		JSONArray monthArray = new JSONArray();
		JSONArray priceArray = new JSONArray();
		
		for(Map.Entry<Date,Double> entry : remittancetMap.entrySet()) {
			
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			String s = formatter.format(entry.getKey());			
			d = new SimpleDateFormat("yyyy-MM-dd").parse(s);
			String month = new SimpleDateFormat("MMM-yy").format(d);
			
			monthArray.put(month);
			priceArray.put(entry.getValue());
		}		
		obj.put("month",monthArray);
		obj.put("price",priceArray);
		obj.put("name1", "Remittance");
		
		return obj.toString();		
	}
		
	@RequestMapping("/exchangeRateChart")
	@ResponseBody
	public String getExchange(@RequestParam("months") String months,@RequestParam("endMonth") String lastMonth) throws ParseException {
		Date d = new SimpleDateFormat("yyyy-MM").parse(months);
		Date d2 = new SimpleDateFormat("yyyy-MM").parse(lastMonth);
		String monthValue = new SimpleDateFormat("yyyy-MM-dd").format(d);
		String endMonthValue=new SimpleDateFormat("yyyy-MM-dd").format(d2);
		Map<Date,Double> ratetMap = economic_indicatorsService.getIndicatorData(monthValue,endMonthValue,"ExchngeRate");
		
		JSONObject obj = new JSONObject();
		JSONArray monthArray = new JSONArray();
		JSONArray priceArray = new JSONArray();
		
		for(Map.Entry<Date,Double> entry : ratetMap.entrySet()) {
			
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			String s = formatter.format(entry.getKey());			
			d = new SimpleDateFormat("yyyy-MM-dd").parse(s);
			String month = new SimpleDateFormat("dd-MMM-yy").format(d);
			
			monthArray.put(month);
			priceArray.put(entry.getValue());
			obj.put("name1", "BDT value");
		}
		
		obj.put("month",monthArray);
		obj.put("price",priceArray);
		
		return obj.toString();		
	}
	
	@RequestMapping("/lendingDepositChart")
	@ResponseBody
	public String getLandingDeposite(@RequestParam("months") String months,@RequestParam("endMonth") String lastMonth) throws ParseException {
		
		Date d = new SimpleDateFormat("yyyy-MM").parse(months);
		Date d2 = new SimpleDateFormat("yyyy-MM").parse(lastMonth);
		String monthValue = new SimpleDateFormat("yyyy-MM-dd").format(d);
		String endMonthValue=new SimpleDateFormat("yyyy-MM-dd").format(d2);
		Map<Date,Double> landingMap = economic_indicatorsService.getIndicatorData(monthValue,endMonthValue,"landingRate");
		Map<Date,Double> depositeMap = economic_indicatorsService.getIndicatorData(monthValue,endMonthValue,"DepositeRate");
		
		JSONObject obj = new JSONObject();
		JSONArray monthArray = new JSONArray();
		JSONArray landingPriceArray = new JSONArray();
		JSONArray depositePriceArray = new JSONArray();
		
		for(Map.Entry<Date,Double> entry : landingMap.entrySet()) {
			
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			String s = formatter.format(entry.getKey());			
			d = new SimpleDateFormat("yyyy-MM-dd").parse(s);
			String month = new SimpleDateFormat("MMM-yy").format(d);
			
			monthArray.put(month);
			landingPriceArray.put(entry.getValue());
		}
		
		for(Map.Entry<Date,Double> entry : depositeMap.entrySet()) {
			depositePriceArray.put(entry.getValue());
		}
		obj.put("month",monthArray);
		obj.put("price",landingPriceArray);
		obj.put("price2",depositePriceArray);
		obj.put("name1", "Lending");
		obj.put("name2", "Deposit");
		
		return obj.toString();		
	}
		
	@RequestMapping("/callMoneyRateChart")
	@ResponseBody
	public String getCallMoney(@RequestParam("months") String months,@RequestParam("endMonth") String lastMonth) throws ParseException {
		
		Date d = new SimpleDateFormat("yyyy-MM").parse(months);
		Date d2 = new SimpleDateFormat("yyyy-MM").parse(lastMonth);
		String monthValue = new SimpleDateFormat("yyyy-MM-dd").format(d);
		String endMonthValue=new SimpleDateFormat("yyyy-MM-dd").format(d2);
		Map<Date,Double> callMoneytMap = economic_indicatorsService.getIndicatorData(monthValue,endMonthValue,"Call Money");
		
		JSONObject obj = new JSONObject();
		JSONArray monthArray = new JSONArray();
		JSONArray priceArray = new JSONArray();
		
		for(Map.Entry<Date,Double> entry : callMoneytMap.entrySet()) {
			
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			String s = formatter.format(entry.getKey());			
			d = new SimpleDateFormat("yyyy-MM-dd").parse(s);
			String month = new SimpleDateFormat("MMM-yy").format(d);
			
			monthArray.put(month);
			priceArray.put(entry.getValue());
		}		

		obj.put("month",monthArray);
		obj.put("price",priceArray);
		obj.put("name1", "Call Money");
		
		return obj.toString();		
	}
		
	@RequestMapping("/inflationChart")
	@ResponseBody
	public String getInflation(@RequestParam("months") String months,@RequestParam("endMonth") String lastMonth) throws ParseException {
		
		Date d = new SimpleDateFormat("yyyy-MM").parse(months);
		Date d2 = new SimpleDateFormat("yyyy-MM").parse(lastMonth);
		String monthValue = new SimpleDateFormat("yyyy-MM-dd").format(d);
		String endMonthValue=new SimpleDateFormat("yyyy-MM-dd").format(d2);
		Map<Date,Double> oneYrMap = economic_indicatorsService.getIndicatorData(monthValue,endMonthValue,"Inflation1Y");
		Map<Date,Double> p2pMap = economic_indicatorsService.getIndicatorData(monthValue,endMonthValue,"InflationP2P");
		
		JSONObject obj = new JSONObject();
		JSONArray monthArray = new JSONArray();
		JSONArray oneYrPriceArray = new JSONArray();
		JSONArray p2pPriceArray = new JSONArray();
		
		for(Map.Entry<Date,Double> entry : oneYrMap.entrySet()) {
			
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			String s = formatter.format(entry.getKey());
			
			d = new SimpleDateFormat("yyyy-MM-dd").parse(s);
			String month = new SimpleDateFormat("MMM-yy").format(d);			
			monthArray.put(month);
			oneYrPriceArray.put(entry.getValue());
		}
		
		for(Map.Entry<Date,Double> entry : p2pMap.entrySet()) {
			p2pPriceArray.put(entry.getValue());
		}
		obj.put("month",monthArray);
		obj.put("price",oneYrPriceArray);
		obj.put("price2",p2pPriceArray);
		obj.put("name1", "1Year Avg Basis");
		obj.put("name2", "Point to Point Basis");
		
		return obj.toString();		
	}
	
	@RequestMapping("/yieldChart")
	@ResponseBody
	public String getYield(@RequestParam("months") String months) throws ParseException {
		
		Date d = new SimpleDateFormat("MMM-yy").parse(months);
		String monthValue = new SimpleDateFormat("yyyy-MM-dd").format(d);
		
		List<Double> yieldList = economic_yieldService.getYieldData(monthValue);	
		JSONObject obj = new JSONObject();
		JSONArray priceArray = new JSONArray();
	
		for (Double value : yieldList) {
			priceArray.put(value);
        }						
		obj.put("price",priceArray);		
		return obj.toString();		
	}
}
