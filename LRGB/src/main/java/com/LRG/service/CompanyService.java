package com.LRG.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.LRG.Utils.CommonUtils;
import com.LRG.Utils.StatsUtils;
import com.LRG.domain.AdjustedPrice;
import com.LRG.domain.DailyCompanyInfo;
import com.LRG.domain.DailyPriceVolume;
import com.LRG.domain.DividendInfo;
import com.LRG.domain.InterimFinancialData;
import com.LRG.domain.TreasuryBill;
import com.LRG.model.DailyCompanyInfoDto;
import com.LRG.model.DividendInfoDto;
import com.LRG.model.InterimFinancialDataDto;
import com.LRG.model.MarketStatDto;
import com.LRG.repository.AdjustedPriceRepository;
import com.LRG.repository.DailyCompanyInfoRepository;
import com.LRG.repository.DailyPriceVolumeRepository;
import com.LRG.repository.InterimFinancialDataRepository;
import com.LRG.repository.SysConfigRepository;
import com.LRG.repository.TreasuryBillRepository;

@Service
public class CompanyService {
	
	@Autowired
	private InterimFinancialDataRepository interimFinancialDataRepository;
	
	@Autowired
	private DailyPriceVolumeRepository dailyPriceVolumeRepository;
	
	@Autowired
	private AdjustedPriceRepository adjustedPriceRepository;
	
	@Autowired
	private SysConfigRepository sysConfigRepository;
	
	private TreasuryBillRepository treasuryBillRepository;
	
	private DailyCompanyInfoRepository dailyCompanyInfoRepository;
	
	public CompanyService(DailyCompanyInfoRepository dailyCompanyInfoRepository) {
		super();
		this.dailyCompanyInfoRepository = dailyCompanyInfoRepository;
	}
	
	public DailyCompanyInfoDto getCompanyData(String code){
		DailyCompanyInfoDto companyInfoDto = new DailyCompanyInfoDto();
		DailyCompanyInfo companyData = dailyCompanyInfoRepository.findByCode(code);
		if(companyData != null) {
			companyInfoDto = convertCompanyDomainToDto(companyData);
		}
		return companyInfoDto;	
	}

	private DailyCompanyInfoDto convertCompanyDomainToDto(DailyCompanyInfo company) {	
		List<DividendInfoDto> dividendInfoList = new ArrayList<DividendInfoDto>();
		if(company.getDividendInfo() !=null && !company.getDividendInfo().isEmpty()) {
			for(DividendInfo dividendInfo : company.getDividendInfo()) {
				if(dividendInfo!=null) {
					DividendInfoDto dividendInfoDto = convertDividendDomainToDto(dividendInfo);
					dividendInfoList.add(dividendInfoDto);
				}
			}
		}
		DecimalFormat df = new DecimalFormat("#.##"); 
		Double oneYearTotalReturn = null;
		Double dvdYield = company.getMarketData().getDvdYield();
		Double oneYearPriceReturn = company.getMarketData().getOneYearPriceReturn();
		if(dvdYield!=null && oneYearPriceReturn!=null) {
			oneYearTotalReturn = dvdYield + oneYearPriceReturn; 
		}
		dvdYield = dvdYield != null ? Double.valueOf(df.format(dvdYield*100)) : null;
		oneYearTotalReturn = oneYearTotalReturn!=null ? Double.valueOf(df.format(oneYearTotalReturn)) : null;
		Double freeFloat = null;
		if(company.getSponsorDirector()!=null) {
			freeFloat = 100 - company.getSponsorDirector();
			freeFloat = Double.valueOf(df.format(freeFloat));
		}
		boolean midYear = company.getCompany().getYearEnd().contains("J") ? true : false;
		
		Double ytd = company.getPerformanceMatrix()!=null ? company.getPerformanceMatrix().getYtd() : null;
		Double oneYearAvgVol = company.getPerformanceMatrix()!=null ? company.getPerformanceMatrix().getOneYearAvgVol() : null;
		Double outShares = company.getOutShares()!=null ? company.getOutShares()/1000000 : null;
		outShares = outShares!=null ? Double.valueOf(df.format(outShares)) : null;
		
//		Double pe = null;
//		if(company.getCompany().getIncomePerShare()!=null) {
//			pe = company.getMkstat().getLtp() / company.getCompany().getIncomePerShare();
//			pe = Double.valueOf(df.format(pe));
//		}
		Double nav = null;
		if(company.getCompany().getEquitiesPerShare()!=null) {
			nav = company.getCompany().getEquitiesPerShare();
//			pb = company.getMkstat().getLtp() / company.getCompany().getEquitiesPerShare();
			nav = Double.valueOf(df.format(nav));
		}
//		Double ps = null;
//		if(company.getCompany().getSalesPerShare()!=null) {
//			ps = company.getMkstat().getLtp() / company.getCompany().getSalesPerShare();
//			ps = Double.valueOf(df.format(ps));
//		}
		
		return new DailyCompanyInfoDto(company.getCode(),company.getPriceRange(),company.getAuthorizedCapital(),company.getPaidupCapital(),
				outShares,company.getAuditedPe(),company.getSponsorDirector(),company.getGovt(),company.getInstitute(),
				company.getForeign(),company.getPublicc(),company.getCompany().getSector(),company.getCompany().getCompanyName(),
				dividendInfoList,company.getMkstat().getLtp(),dvdYield,ytd,oneYearPriceReturn,oneYearTotalReturn,oneYearAvgVol,
				Double.valueOf(df.format(company.getMarketData().getMarketcap()/1000000)),freeFloat,midYear,null,nav,null,
				company.getMkstat().getYcp(),company.getCompany().getLastUpdateTimePb());
	}

	private DividendInfoDto convertDividendDomainToDto(DividendInfo dividendInfo) {
		return new DividendInfoDto(dividendInfo.getCode(),dividendInfo.getCashDividend(),dividendInfo.getStockDividend(),
				dividendInfo.getDps(),dividendInfo.getYear());
	}
	
	public List<InterimFinancialDataDto> getInterimFinancialData(String code){
		List<InterimFinancialDataDto> interimFinancialDataList = new ArrayList<InterimFinancialDataDto>();
		List<InterimFinancialData> interimFinancialData = interimFinancialDataRepository.findAllByCode(code);
		if(interimFinancialData != null && !interimFinancialData.isEmpty()) {
			for(InterimFinancialData interimFinancial : interimFinancialData) {
				if(interimFinancial!=null) {
					InterimFinancialDataDto interimFinancialDto = convertInterimFinancialDomainToDto(interimFinancial);
					interimFinancialDataList.add(interimFinancialDto);
				}
			}
		}
		return interimFinancialDataList;	
	}

	private InterimFinancialDataDto convertInterimFinancialDomainToDto(InterimFinancialData interimFinancial) {
		return new InterimFinancialDataDto(interimFinancial.getCode(),interimFinancial.getPeriod(),interimFinancial.getTurnover(),
				interimFinancial.getProfitFromContOp(),interimFinancial.getProfitForPeriod(),interimFinancial.getTotComprIncomeForPeriod(),
				interimFinancial.getEpsBasic(),interimFinancial.getEpsDiluted(),interimFinancial.getEpsBasicContOp(),
				interimFinancial.getEpsDilutedContOp(),interimFinancial.getPricePerShareAtPeriodEnd());
	}
	
	public LinkedHashMap<String,String> getPriceAndVolumeData(String code){
		LinkedHashMap<String,String> priceVolumeInfoMap = new LinkedHashMap<String,String>();
		LocalDate date = LocalDate.now();
  	  	date = date.minusYears(2);
		List<DailyPriceVolume> dailyPriceVolumeList = dailyPriceVolumeRepository.findAllByTicker(code,date.toString());
		if(dailyPriceVolumeList.size()>0) {
			for(DailyPriceVolume dailyPriceVolume : dailyPriceVolumeList) {
				if(dailyPriceVolume!=null) {
					priceVolumeInfoMap.put(dailyPriceVolume.getDate(),dailyPriceVolume.getClose()+"::"+dailyPriceVolume.getVolume());
				}
			}
		}
		return priceVolumeInfoMap;	
	}

	public List<AdjustedPrice> getPriceAndVolumeData(String code, String dateRange,boolean isIndex,boolean isTable,boolean isForeignIndex, 
			boolean isMF, boolean isTickerMF) {
		List<AdjustedPrice> dailyPriceVolumeList = new ArrayList<AdjustedPrice>();
		if(dateRange!=null && !dateRange.equals("")) {
			dateRange = getFormattedDate(dateRange);
			String startDate = dateRange.split(" - ")[0];
			String endDate = dateRange.split(" - ")[1];
			
			if(isTable && isIndex) {
				if(code.contains("Index")) code = code+" W";
				dailyPriceVolumeList = adjustedPriceRepository.findCloseVolumeByTickerForWeeklyIndex(code,startDate,endDate);
			}else if(isIndex && !isTable){
				dailyPriceVolumeList = adjustedPriceRepository.findCloseVolumeByTickerForIndex(code,startDate);
			}else if(isForeignIndex){
				dailyPriceVolumeList = adjustedPriceRepository.findCloseByTickerForForeignIndex(code,startDate);
			}else if(isTable && isMF){ 
				if(isTickerMF) {
					List<Object[]> indexedDataList = adjustedPriceRepository.findCloseVolumeByTickerForMF(code,startDate,endDate);
					for(Object[] indexedData : indexedDataList) {
						AdjustedPrice indexedPrice = new AdjustedPrice();
						BigDecimal close = (BigDecimal) indexedData[2];
						Float volume = (Float) indexedData[3];
						indexedPrice.setAdjstdClose(close.doubleValue());
						indexedPrice.setVolume(volume.doubleValue());
						dailyPriceVolumeList.add(indexedPrice);
					}
				}else {
					dailyPriceVolumeList = adjustedPriceRepository.findCloseVolumeByTickerForWeeklyIndex(code,startDate,endDate);
				}
			}else {
				dailyPriceVolumeList = adjustedPriceRepository.findCloseVolumeByTicker(code,startDate,endDate);
			}
		}
		return dailyPriceVolumeList;
	}
	
	public Map<String, String> getModifiedPriceData(List<AdjustedPrice> dailyPriceReturnList) {
		LinkedHashMap<String,String> priceMap = new LinkedHashMap<String,String>();
		int i = 0;
//		Double firstRetVal = null;
		Double initialVal = 100.0;
		Double firstPriceVal = null;
		DecimalFormat df = new DecimalFormat("#.##"); 
		if(dailyPriceReturnList.size()>0) {
			for(AdjustedPrice dailyPriceReturn : dailyPriceReturnList) {
				if(dailyPriceReturn!=null) {
					//Double currRetVal = dailyPriceReturn.getReturnValue();
					Double currPriceVal = dailyPriceReturn.getAdjstdClose();
					if(i==0) {
						//firstRetVal = dailyPriceReturn.getReturnValue();
						firstPriceVal = dailyPriceReturn.getAdjstdClose();
						priceMap.put(dailyPriceReturn.getDate(),initialVal+"::"+dailyPriceReturn.getAdjstdClose()+"::"+dailyPriceReturn.getVolume());
						i++;
						continue;
					}
//					if(firstRetVal==0) {
//						firstRetVal = dailyPriceReturn.getReturnValue();
//					}
					//Double modReturnVal = firstRetVal !=0 && currRetVal!=null ? (currRetVal/firstRetVal) - 1 : null;

					Double modPriceVal = firstPriceVal !=0 ? (currPriceVal/firstPriceVal) - 1 : null;
					Double newPriceVal = initialVal + initialVal*modPriceVal;
					if(newPriceVal!=null) {
						newPriceVal = Double.valueOf(df.format(newPriceVal));
						priceMap.put(dailyPriceReturn.getDate(),newPriceVal+"::"+dailyPriceReturn.getAdjstdClose()+"::"+dailyPriceReturn.getVolume());
					}
				}
			}
		}
		return priceMap;
	}
	
	private String getFormattedDate(String dateRange) {
		String startDate = dateRange.split(" - ")[0];
		String endDate = dateRange.split(" - ")[1];
		
		if (dateRange.contains("/")) {
			try {
				Date date1 = new SimpleDateFormat("MM/dd/yyyy").parse(startDate);
				Date date2 = new SimpleDateFormat("MM/dd/yyyy").parse(endDate);
				
				startDate = new SimpleDateFormat("yyyy-MM-dd").format(date1);
				endDate = new SimpleDateFormat("yyyy-MM-dd").format(date2);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}		
		return startDate+" - "+endDate;
	}

	public Map<String, Double> getTableChartData(List<AdjustedPrice> dailyPriceList, List<AdjustedPrice> dailyBMPriceList,boolean isIndex) {		
		Map<String,Double> tableDataMap = new LinkedHashMap<String,Double>();
		List<Double> returnList = new ArrayList<Double>();
		List<Double> priceList = new ArrayList<Double>();
		List<Double> bmReturnList = new ArrayList<Double>();
		List<Double> bmPriceList = new ArrayList<Double>();
		List<Double> dailyReturnWithoutAdding1List = new ArrayList<Double>();
		Double count = 0.0;
		Double countGE = 0.0;
		Double countLT = 0.0;
		Double sumUpVolume = 0.0;
		Double sumDownVolume = 0.0;
		
		if(dailyPriceList.size()>0) {
			for(AdjustedPrice dailyPriceReturn : dailyPriceList) {
				if(dailyPriceReturn!=null) {
//					System.out.println("Code " +dailyPriceReturn.getCode());
//					System.out.println("Date "+ dailyPriceReturn.getDate());
//					System.out.println("Price " + dailyPriceReturn.getAdjstdClose());
					priceList.add(dailyPriceReturn.getAdjstdClose());
				}
			}
		}
		for(int k = 0; k < priceList.size()-1; k++) {
			returnList.add(priceList.get(k+1)/priceList.get(k));
			if(priceList.get(k+1)/priceList.get(k) > 1) {
				count++;
			}
			if(priceList.get(k+1)/priceList.get(k) >= 1) {
				countGE++;
				if(k<50) {
					if(dailyPriceList.get(k).getVolume()!=null) sumUpVolume = sumUpVolume + dailyPriceList.get(k).getVolume();
				}
			}
			if(priceList.get(k+1)/priceList.get(k) < 1) {
				countLT++;
				if(k<50) {
					if(dailyPriceList.get(k).getVolume()!=null) sumDownVolume = sumDownVolume + dailyPriceList.get(k).getVolume();
				}
			}
		}
		
		if(dailyBMPriceList.size()>0) {
			for(AdjustedPrice dailyBMPriceReturn : dailyBMPriceList) {
				if(dailyBMPriceReturn!=null) {
					bmPriceList.add(dailyBMPriceReturn.getAdjstdClose());
				}
			}
		}
		for(int k = 0; k < bmPriceList.size()-1; k++) {
			bmReturnList.add(bmPriceList.get(k+1)/bmPriceList.get(k));
		}	
		
		if(bmPriceList.size()==priceList.size()) {
			Double riskFreeRate = new Double(sysConfigRepository.findById(3).getValue());
			Double alpha = null;
			Double battingaverage = null;
			Double tickerReturn = (priceList.get(priceList.size()-1)/priceList.get(0))-1;
//			System.out.println("End Price"+priceList.get(priceList.size()-1));
//			System.out.println("Start Price"+priceList.get(0));
			
			double sd = StatsUtils.getStandardDeviation(CommonUtils.getDoubleArray(returnList));
			Double stdDeviation = isIndex ? Precision.round((sd * Math.sqrt(52.0))*100, 3) : Precision.round((sd * Math.sqrt(252.0))*100, 3);
			Map<String, String> regMapPort = StatsUtils.getRegressionMap(bmReturnList, returnList);			
			Double beta = (regMapPort.get("slope") != null) ? Double.parseDouble(regMapPort.get("slope")) : 0;
			Double rsquared = (regMapPort.get("rSquare") != null) ? Double.parseDouble(regMapPort.get("rSquare")) : 0;
			Double fundReturn = isIndex ? (Math.pow(priceList.get(priceList.size()-1)/priceList.get(0), 52.0/priceList.size()) - 1.0)*100 :
				(Math.pow(priceList.get(priceList.size()-1)/priceList.get(0), 252.0/priceList.size()) - 1.0)*100;
			double fundReturnAbs = fundReturn/100;
			Double bmReturn = isIndex ? (Math.pow(bmPriceList.get(bmPriceList.size()-1)/bmPriceList.get(0), 52.0/bmPriceList.size()) - 1.0)*100 :
				(Math.pow(bmPriceList.get(bmPriceList.size()-1)/bmPriceList.get(0), 252.0/bmPriceList.size()) - 1.0)*100;
			double stddeviationAbs = stdDeviation/100;
			Double indexReturn = isIndex ? (Math.pow(bmPriceList.get(bmPriceList.size()-1)/bmPriceList.get(0), 52.0/bmPriceList.size()) - 1.0)*100 :
				(Math.pow(bmPriceList.get(bmPriceList.size()-1)/bmPriceList.get(0), 252.0/bmPriceList.size()) - 1.0)*100;
			alpha = Precision.round((fundReturn - (beta*indexReturn)), 4);
			battingaverage = Precision.round(count/returnList.size(), 4);
			Double sharpRatio = Precision.round((fundReturn - Double.valueOf(riskFreeRate))/((sd * Math.sqrt(252))*100), 2);
			
			//Double annualizedReturn = StatsUtils.getAnnualizedReturn(returnList);
			Double kurt = StatsUtils.getKurtosis(returnList);
			Double skew = StatsUtils.getSkewness(returnList);
			Double calmerRatio = Precision.round(StatsUtils.getCalmerRatio(priceList, fundReturn), 2);
			Double captureRatioUp = StatsUtils.getCaptureRatioUp(bmReturnList, returnList);
			Double captureRatioDown = StatsUtils.getCaptureRatioDown(bmReturnList, returnList);
			Double informationRatio = Precision.round((fundReturn-bmReturn)/StatsUtils.getTrackingError(bmReturnList, returnList), 2);
			Double trackingError = Precision.round((StatsUtils.getTrackingError(bmReturnList, returnList)),1);
			Double downsideRisk = Precision.round(StatsUtils.getDownsideRisk(bmReturnList, returnList,riskFreeRate), 3);
			Double maxDrawDownAnnualized = Precision.round(StatsUtils.getMaxDrawdown(priceList)*100, 1);
			Double biasRatio = Precision.round(StatsUtils.getBiasRatio(returnList), 2);
			Double bestPeriod = Precision.round((Collections.max(returnList)-1)*100, 1);
			Double worstPeriod = Precision.round((Collections.min(returnList)-1)*100, 1);
			Double correlationCoefficient = Precision.round(StatsUtils.getCorrelation(returnList, bmReturnList), 4);
			Double upMktReturn = Precision.round(StatsUtils.getUpMarketReturn(bmReturnList, returnList), 1);
			Double downMktReturn = Precision.round(StatsUtils.getDownMarketReturn(bmReturnList, returnList), 1);
			Double averageFundsVal = 0.0;
			for(int i = 0; i < priceList.size(); i++) {
				averageFundsVal += priceList.get(i);
			}
			for(int m = 0; m < returnList.size(); m++) {
				dailyReturnWithoutAdding1List.add(returnList.get(m) - 1);
			}
			
			tableDataMap.put("Return",tickerReturn*100);
			tableDataMap.put("Alpha",alpha);
			tableDataMap.put("Beta",beta);
			tableDataMap.put("Annualized Return",Precision.round(fundReturn, 2));
			tableDataMap.put("Standard Deviation",stdDeviation);
			tableDataMap.put("Return/Risk",Precision.round(((fundReturnAbs/stddeviationAbs)), 3));
			tableDataMap.put("Batting Average",battingaverage);
			tableDataMap.put("Skewness",skew);
//			tableDataMap.put("Kurtosis",kurt);		
			tableDataMap.put("Max Drawdown",maxDrawDownAnnualized);
//			tableDataMap.put("Best Period",bestPeriod);
//			tableDataMap.put("Worst Period",worstPeriod);
//			tableDataMap.put("Calmer Ratio",calmerRatio);
//			tableDataMap.put("Correlation Coefficient",correlationCoefficient);	
//			tableDataMap.put("Current Drawdown", Precision.round((Collections.min(returnList)-1)*100, 4));
//			tableDataMap.put("Bias Ratio",biasRatio);
			tableDataMap.put("Up Market Return",upMktReturn!=-100 ? upMktReturn : Double.NaN);
			tableDataMap.put("Down Market Return",downMktReturn!=-100 ? downMktReturn : Double.NaN);
			tableDataMap.put("No. of Negative period", countLT);
			tableDataMap.put("No. of positive period", countGE);
			tableDataMap.put("Excess Return", Precision.round((fundReturn - indexReturn), 4));
			tableDataMap.put("Capture Ratio Up",captureRatioUp);
			tableDataMap.put("Capture Ratio Down",captureRatioDown);
			tableDataMap.put("Information Ratio",informationRatio);
			tableDataMap.put("Max drawdown Period Peak", Precision.round((Collections.max(priceList)), 2));
//			tableDataMap.put("Sterling Ratio", Precision.round((fundReturn/Math.abs(((Collections.max(priceList)-
//					Collections.min(priceList))/(averageFundsVal/priceList.size())*100)-10)), 2));
//			tableDataMap.put("Tracking Error",trackingError);
//			tableDataMap.put("Sharp Ratio",sharpRatio);
//			tableDataMap.put("RSquared",Precision.round(rsquared, 3));
//			tableDataMap.put("Variance", Precision.round((Math.pow(((sd * Math.sqrt(252.0))*100/100), 2))*100, 1));
			tableDataMap.put("Jensen Alpha", Precision.round(((fundReturn - Double.valueOf(riskFreeRate))-
					(beta*(indexReturn - Double.valueOf(riskFreeRate)))),3));
//			tableDataMap.put("Treynor", Precision.round(((fundReturn - Double.valueOf(riskFreeRate))/beta)/100, 2));
			tableDataMap.put("Downside Risk",downsideRisk);
//			tableDataMap.put("Sortino", Precision.round(((fundReturn - Double.valueOf(riskFreeRate))/downsideRisk), 1));
			tableDataMap.put("Upside/Downside Ratio", Precision.round(Double.valueOf(sumUpVolume/sumDownVolume), 2));
			
		}else {
			tableDataMap.put("Return",null);
			tableDataMap.put("Alpha",null);
			tableDataMap.put("Beta",null);
			tableDataMap.put("Annualized Return",null);
			tableDataMap.put("Standard Deviation",null);
			tableDataMap.put("Return/Risk",null);
			tableDataMap.put("Batting Average",null);
			tableDataMap.put("Skewness",null);
//			tableDataMap.put("Kurtosis",null);		
			tableDataMap.put("Max Drawdown",null);
//			tableDataMap.put("Best Period",null);
//			tableDataMap.put("Worst Period",null);
//			tableDataMap.put("Calmer Ratio",null);
//			tableDataMap.put("Correlation Coefficient",null);	
//			tableDataMap.put("Current Drawdown", null);
//			tableDataMap.put("Bias Ratio",null);
			tableDataMap.put("Up Market Return",null);
			tableDataMap.put("Down Market Return",null);
			tableDataMap.put("No. of Negative period", null);
			tableDataMap.put("No. of positive period", null);
			tableDataMap.put("Excess Return", null);
			tableDataMap.put("Capture Ratio Up",null);
			tableDataMap.put("Capture Ratio Down",null);
			tableDataMap.put("Information Ratio",null);
			tableDataMap.put("Max drawdown Period Peak", null);
//			tableDataMap.put("Sterling Ratio", null);
//			tableDataMap.put("Tracking Error",null);
//			tableDataMap.put("Sharp Ratio",null);
//			tableDataMap.put("RSquared",null);
//			tableDataMap.put("Variance", null);
			tableDataMap.put("Jensen Alpha", null);
//			tableDataMap.put("Treynor", null);
			tableDataMap.put("Downside Risk",null);
//			tableDataMap.put("Sortino", null);
			tableDataMap.put("Upside/Downside Ratio", Precision.round(Double.valueOf(sumUpVolume/sumDownVolume), 2));
		}
				
		return tableDataMap;
	}
	
	public Map<String, Double> getTableChartData(List<AdjustedPrice> dailyPriceList, List<AdjustedPrice> dailyBMPriceList,boolean isIndex,
			String riskFreer, String tradingDays) {		
		Double totalTradingDays = Double.parseDouble(tradingDays);
		Map<String,Double> tableDataMap = new LinkedHashMap<String,Double>();
		List<Double> returnList = new ArrayList<Double>();
		List<Double> priceList = new ArrayList<Double>();
		List<Double> bmReturnList = new ArrayList<Double>();
		List<Double> bmPriceList = new ArrayList<Double>();
		List<Double> dailyReturnWithoutAdding1List = new ArrayList<Double>();
		Double count = 0.0;
		Double countGE = 0.0;
		Double countLT = 0.0;
		Double sumUpVolume = 0.0;
		Double sumDownVolume = 0.0;
		
		if(dailyPriceList.size()>0) {
			for(AdjustedPrice dailyPriceReturn : dailyPriceList) {
				if(dailyPriceReturn!=null) {
//					System.out.println("Code " +dailyPriceReturn.getCode());
//					System.out.println("Date "+ dailyPriceReturn.getDate());
//					System.out.println("Price " + dailyPriceReturn.getAdjstdClose());
					priceList.add(dailyPriceReturn.getAdjstdClose());
				}
			}
		}
		for(int k = 0; k < priceList.size()-1; k++) {
			returnList.add(priceList.get(k+1)/priceList.get(k));
			if(priceList.get(k+1)/priceList.get(k) > 1) {
				count++;
			}
			if(priceList.get(k+1)/priceList.get(k) >= 1) {
				countGE++;
				if(k<50) {
					if(dailyPriceList.get(k).getVolume()!=null) sumUpVolume = sumUpVolume + dailyPriceList.get(k).getVolume();
				}
			}
			if(priceList.get(k+1)/priceList.get(k) < 1) {
				countLT++;
				if(k<50) {
					if(dailyPriceList.get(k).getVolume()!=null) sumDownVolume = sumDownVolume + dailyPriceList.get(k).getVolume();
				}
			}
		}
		
		if(dailyBMPriceList.size()>0) {
			for(AdjustedPrice dailyBMPriceReturn : dailyBMPriceList) {
				if(dailyBMPriceReturn!=null) {
					bmPriceList.add(dailyBMPriceReturn.getAdjstdClose());
				}
			}
		}
		for(int k = 0; k < bmPriceList.size()-1; k++) {
			bmReturnList.add(bmPriceList.get(k+1)/bmPriceList.get(k));
		}	
		
		if(bmPriceList.size()==priceList.size()) {
			Double riskFreeRate = new Double(riskFreer);
			Double alpha = null;
			Double battingaverage = null;
			Double tickerReturn = (priceList.get(priceList.size()-1)/priceList.get(0))-1;
//			System.out.println("End Price"+priceList.get(priceList.size()-1));
//			System.out.println("Start Price"+priceList.get(0));
			
			double sd = StatsUtils.getStandardDeviation(CommonUtils.getDoubleArray(returnList));
			//sd=Precision.round(sd,2);
			Double stdDeviation =Precision.round((sd * Math.sqrt(totalTradingDays))*100,2) ;
			Map<String, String> regMapPort = StatsUtils.getRegressionMap(bmReturnList, returnList);			
			Double beta = (regMapPort.get("slope") != null) ? Double.parseDouble(regMapPort.get("slope")) : 0;
			Double rsquared = (regMapPort.get("rSquare") != null) ? Double.parseDouble(regMapPort.get("rSquare")) : 0;
			System.out.println(priceList.get(priceList.size()-1)+"   "+priceList.get(0)+"  "+priceList.size());
			
			Double fundReturn = (Math.pow(priceList.get(priceList.size()-1)/priceList.get(0), totalTradingDays/priceList.size()) - 1.0)*100 ;
			fundReturn=Precision.round(fundReturn,3);
			double fundReturnAbs = fundReturn/100;
			Double bmReturn = (Math.pow(bmPriceList.get(bmPriceList.size()-1)/bmPriceList.get(0), totalTradingDays/bmPriceList.size()) - 1.0)*100;
			double stddeviationAbs = stdDeviation/100;
			Double indexReturn = (Math.pow(bmPriceList.get(bmPriceList.size()-1)/bmPriceList.get(0), totalTradingDays/bmPriceList.size()) - 1.0)*100 ;
			alpha = Precision.round((fundReturn - (beta*indexReturn)), 4);
			battingaverage = Precision.round(count/returnList.size(), 4);
			Double sharpRatio = Precision.round((fundReturn - Double.valueOf(riskFreeRate))/((sd * Math.sqrt(252))*100), 2);
			
			//Double annualizedReturn = StatsUtils.getAnnualizedReturn(returnList);
			Double kurt = StatsUtils.getKurtosis(returnList);
			Double skew = StatsUtils.getSkewness(returnList);
			Double calmerRatio = Precision.round(StatsUtils.getCalmerRatio(priceList, fundReturn), 2);
			Double captureRatioUp = StatsUtils.getCaptureRatioUp(bmReturnList, returnList);
			Double captureRatioDown = StatsUtils.getCaptureRatioDown(bmReturnList, returnList);
			Double informationRatio = Precision.round((fundReturn-bmReturn)/StatsUtils.getTrackingError(bmReturnList, returnList), 2);
			Double trackingError = Precision.round((StatsUtils.getTrackingError(bmReturnList, returnList)),1);
			Double downsideRisk = Precision.round(StatsUtils.getDownsideRisk(bmReturnList, returnList,riskFreeRate), 3);
			Double maxDrawDownAnnualized = Precision.round(StatsUtils.getMaxDrawdown(priceList)*100, 1);
			Double biasRatio = Precision.round(StatsUtils.getBiasRatio(returnList), 2);
			Double bestPeriod = Precision.round((Collections.max(returnList)-1)*100, 1);
			Double worstPeriod = Precision.round((Collections.min(returnList)-1)*100, 1);
			Double correlationCoefficient = Precision.round(StatsUtils.getCorrelation(returnList, bmReturnList), 4);
			Double upMktReturn = Precision.round(StatsUtils.getUpMarketReturn(bmReturnList, returnList), 1);
			Double downMktReturn = Precision.round(StatsUtils.getDownMarketReturn(bmReturnList, returnList), 1);
			Double averageFundsVal = 0.0;
			for(int i = 0; i < priceList.size(); i++) {
				averageFundsVal += priceList.get(i);
			}
			for(int m = 0; m < returnList.size(); m++) {
				dailyReturnWithoutAdding1List.add(returnList.get(m) - 1);
			}
			
			tableDataMap.put("Return",tickerReturn*100);
			tableDataMap.put("Alpha",alpha);
			tableDataMap.put("Beta",beta);
			tableDataMap.put("Annualized Return",Precision.round(fundReturn, 2));
			tableDataMap.put("Standard Deviation",stdDeviation);
			tableDataMap.put("Return/Risk",Precision.round(((fundReturnAbs/stddeviationAbs)), 3));
			tableDataMap.put("Batting Average",battingaverage);
			tableDataMap.put("Skewness",skew);
//			tableDataMap.put("Kurtosis",kurt);		
			tableDataMap.put("Max Drawdown",maxDrawDownAnnualized);
//			tableDataMap.put("Best Period",bestPeriod);
//			tableDataMap.put("Worst Period",worstPeriod);
//			tableDataMap.put("Calmer Ratio",calmerRatio);
//			tableDataMap.put("Correlation Coefficient",correlationCoefficient);	
//			tableDataMap.put("Current Drawdown", Precision.round((Collections.min(returnList)-1)*100, 4));
//			tableDataMap.put("Bias Ratio",biasRatio);
			tableDataMap.put("Up Market Return",upMktReturn!=-100 ? upMktReturn : Double.NaN);
			tableDataMap.put("Down Market Return",downMktReturn!=-100 ? downMktReturn : Double.NaN);
			tableDataMap.put("No. of Negative period", countLT);
			tableDataMap.put("No. of positive period", countGE);
			tableDataMap.put("Excess Return", Precision.round((fundReturn - indexReturn), 4));
			tableDataMap.put("Capture Ratio Up",captureRatioUp);
			tableDataMap.put("Capture Ratio Down",captureRatioDown);
			tableDataMap.put("Information Ratio",informationRatio);
			tableDataMap.put("Max drawdown Period Peak", Precision.round((Collections.max(priceList)), 2));
//			tableDataMap.put("Sterling Ratio", Precision.round((fundReturn/Math.abs(((Collections.max(priceList)-
//					Collections.min(priceList))/(averageFundsVal/priceList.size())*100)-10)), 2));
//			tableDataMap.put("Tracking Error",trackingError);
//			tableDataMap.put("Sharp Ratio",sharpRatio);
//			tableDataMap.put("RSquared",Precision.round(rsquared, 3));
//			tableDataMap.put("Variance", Precision.round((Math.pow(((sd * Math.sqrt(252.0))*100/100), 2))*100, 1));
			tableDataMap.put("Jensen Alpha", Precision.round(((fundReturn - Double.valueOf(riskFreeRate))-
					(beta*(indexReturn - Double.valueOf(riskFreeRate)))),3));
//			tableDataMap.put("Treynor", Precision.round(((fundReturn - Double.valueOf(riskFreeRate))/beta)/100, 2));
			tableDataMap.put("Downside Risk",downsideRisk);
//			tableDataMap.put("Sortino", Precision.round(((fundReturn - Double.valueOf(riskFreeRate))/downsideRisk), 1));
			Double up_down_ratio=sumUpVolume/sumDownVolume;
			tableDataMap.put("Upside/Downside Ratio", Precision.round(Double.valueOf(up_down_ratio), 2));
			
		}else {
			tableDataMap.put("Return",null);
			tableDataMap.put("Alpha",null);
			tableDataMap.put("Beta",null);
			tableDataMap.put("Annualized Return",null);
			tableDataMap.put("Standard Deviation",null);
			tableDataMap.put("Return/Risk",null);
			tableDataMap.put("Batting Average",null);
			tableDataMap.put("Skewness",null);
//			tableDataMap.put("Kurtosis",null);		
			tableDataMap.put("Max Drawdown",null);
//			tableDataMap.put("Best Period",null);
//			tableDataMap.put("Worst Period",null);
//			tableDataMap.put("Calmer Ratio",null);
//			tableDataMap.put("Correlation Coefficient",null);	
//			tableDataMap.put("Current Drawdown", null);
//			tableDataMap.put("Bias Ratio",null);
			tableDataMap.put("Up Market Return",null);
			tableDataMap.put("Down Market Return",null);
			tableDataMap.put("No. of Negative period", null);
			tableDataMap.put("No. of positive period", null);
			tableDataMap.put("Excess Return", null);
			tableDataMap.put("Capture Ratio Up",null);
			tableDataMap.put("Capture Ratio Down",null);
			tableDataMap.put("Information Ratio",null);
			tableDataMap.put("Max drawdown Period Peak", null);
//			tableDataMap.put("Sterling Ratio", null);
//			tableDataMap.put("Tracking Error",null);
//			tableDataMap.put("Sharp Ratio",null);
//			tableDataMap.put("RSquared",null);
//			tableDataMap.put("Variance", null);
			tableDataMap.put("Jensen Alpha", null);
//			tableDataMap.put("Treynor", null);
			tableDataMap.put("Downside Risk",null);
//			tableDataMap.put("Sortino", null);
			tableDataMap.put("Upside/Downside Ratio", Precision.round(Double.valueOf(sumUpVolume/sumDownVolume), 2));
		}
				
		return tableDataMap;
	}

	public List<Object> getMarketRatioMapList(List<MarketStatDto> marketDataList, DailyCompanyInfoDto companyInfoDto) {
		
		DecimalFormat df = new DecimalFormat("#.##"); 
	
		List<Object> marketRatioMapList = new ArrayList<Object>();
		
		Map<String, Double> sectoralMap = new HashMap<String, Double>();
		Map<String, Double> weightedValuationMap = new HashMap<String, Double>();
		MarketStatDto tickerValuationData = new MarketStatDto();
		double sumMcapForPE = 0.0;
		double sumEarnings = 0.0;
		double sumMcapForPB = 0.0;
		double sumBook = 0.0;
		double sumMcapForPS = 0.0;
		double sumSales = 0.0;
		double sumEquities = 0.0;
		double sumDebt = 0.0;
		Double sumEPS = 0.0;
		double count = 0;
		for(MarketStatDto marketStat : marketDataList) {
			if(marketStat.getSector()!=null && marketStat.getSector().equals(companyInfoDto.getSector())) {
				if(marketStat.getOutShares()!=null) {
					double mcap = marketStat.getLtp() * marketStat.getOutShares();
					if(mcap==0.0) mcap = marketStat.getYcp() * marketStat.getOutShares();
					if(marketStat.getEps()!=null) {
						sumMcapForPE += mcap;
						double earnings = marketStat.getEps() * marketStat.getOutShares();
						sumEarnings += earnings;
						
						sumEPS += marketStat.getEps();
						count++;
					}
					if(marketStat.getPb()!=null) {
						sumMcapForPB += mcap;
						double book = (marketStat.getLtp() / marketStat.getPb()) * marketStat.getOutShares();
						if(book==0.0) book = (marketStat.getYcp() / marketStat.getPb()) * marketStat.getOutShares();
						sumBook += book;
					}
					if(marketStat.getPs()!=null && marketStat.getPs()!=0) {
						sumMcapForPS += mcap;
						double sale = (marketStat.getLtp() / marketStat.getPs()) * marketStat.getOutShares();
						if(sale==0.0) sale = (marketStat.getYcp() / marketStat.getPs()) * marketStat.getOutShares();
						sumSales += sale;
					}
					
					if(marketStat.getSector().equals("Bank") || marketStat.getSector().equals("NBFI")) {
						if(marketStat.getTotExposOrRiskWeightedAsset()!=null && marketStat.getTier1Capital()!=null) {
							sumEquities += marketStat.getTotExposOrRiskWeightedAsset();
							sumDebt += marketStat.getTier1Capital();
						}
					}else {
						if(marketStat.getEquitiesPerShare()!=null && marketStat.getTotalDebt()!=null) {
							sumEquities += marketStat.getEquitiesPerShare()*marketStat.getOutShares();
							sumDebt += marketStat.getTotalDebt();
						}
					}
				}
			}
			if(marketStat.getCode().equals(companyInfoDto.getTicker())) tickerValuationData = marketStat;
		}
		if(sumEarnings!=0.0) {
			Double sectorPE = sumMcapForPE / sumEarnings;
			sectoralMap.put("PE",Double.valueOf(df.format(sectorPE)));
			if(sectorPE!=null && tickerValuationData.getPe()!=null && sectorPE!=0 && tickerValuationData.getPe()>=0) {
				weightedValuationMap.put("PE",Double.valueOf(df.format(tickerValuationData.getPe()/sectorPE)));
			}
		}
		if(sumBook!=0.0) {
			Double sectorPB = sumMcapForPB / sumBook;
			sectoralMap.put("PB",Double.valueOf(df.format(sectorPB)));
			if(sectorPB!=null && tickerValuationData.getPb()!=null && sectorPB!=0 && tickerValuationData.getPb()>=0) {
				weightedValuationMap.put("PB",Double.valueOf(df.format(tickerValuationData.getPb()/sectorPB)));
			}
		}
		if(sumSales!=0.0) {
			Double sectorPS = sumMcapForPS / sumSales;
			sectoralMap.put("PS",Double.valueOf(df.format(sectorPS)));
			if(sectorPS!=null && tickerValuationData.getPs()!=null && sectorPS!=0 && tickerValuationData.getPs()>=0) {
				weightedValuationMap.put("PS",Double.valueOf(df.format(tickerValuationData.getPs()/sectorPS)));
			}
		}
		if(sumEquities!=0.0) {
			Double sectorDE = sumDebt / sumEquities;
			if(companyInfoDto.getSector().equals("Bank") || companyInfoDto.getSector().equals("NBFI")) sectorDE = sectorDE * 100;
			sectoralMap.put("DE",Double.valueOf(df.format(sectorDE)));
			if(sectorDE!=null && tickerValuationData.getDe()!=null && sectorDE!=0) {
				weightedValuationMap.put("DE",Double.valueOf(df.format(tickerValuationData.getDe()/sectorDE)));
			}
		}
		Double avgEPS = sumEPS/count;
		sectoralMap.put("EPS",Double.valueOf(df.format(avgEPS)));
		if(avgEPS!=null && tickerValuationData.getEps()!=null && avgEPS!=0 && tickerValuationData.getEps()>=0) {
			weightedValuationMap.put("EPS",Double.valueOf(df.format(tickerValuationData.getEps()/avgEPS)));
		}	
		
		marketRatioMapList.add(sectoralMap);
		marketRatioMapList.add(weightedValuationMap);
		marketRatioMapList.add(tickerValuationData);

		return marketRatioMapList;
	}

	public String getModifiedDateRange(String dateRange, List<String> tickerList) {
		if(dateRange!=null && !dateRange.equals("")) {
			dateRange = getFormattedDate(dateRange);
			String startDate = dateRange.split(" - ")[0];
			String endDate = dateRange.split(" - ")[1];
			String modifiedStartDate = adjustedPriceRepository.findModifiedStockAnalysisStartDate(tickerList);			
			try {
				Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(modifiedStartDate);
				Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
				String finalStartDate = new SimpleDateFormat("MM/dd/yyyy").format(date1.compareTo(date2) > 0 ? date1 : date2);
				String finalEndDate = new SimpleDateFormat("MM/dd/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(endDate));
				return finalStartDate+" - "+finalEndDate;				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public List<AdjustedPrice> dsexDailyData(String code, String date) {
		List<AdjustedPrice> dsexList = adjustedPriceRepository.findDSEXDailyValue(code, date);
		return dsexList;
	}
	
	public List<List<String>> marketDataR(String date) throws ClassNotFoundException, SQLException{
		List<List<String>> companyData = new ArrayList<>();
		
		Connection con = connectLRGB();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		ResultSet rs1 = null;
		Map<String, String> companyMap = new HashMap<String, String>();
		
		
		String query = "SELECT code, sector, company_name FROM dse_analyzer.company where trading_status='Trading';";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			companyMap.put(rs.getString("code"),rs.getString("company_name")+"::"+rs.getString("sector"));
		}
		
		
		String bond_data="select code from market_stat where type in('A-CB','A-DB','N-CB')";
		Statement stmt1 = con.createStatement();
		rs1 = stmt1.executeQuery(bond_data);
		while(rs1.next()) {
			companyMap.put(rs1.getString("code"),rs1.getString("code")+"::Bond");
		}
		
		for(Map.Entry<String, String> entry : companyMap.entrySet()) {
			boolean falg=false;
			List<String> data = new ArrayList<>();
			String code = entry.getKey();
			if(code.equalsIgnoreCase("SEBL1STMF")) {
				System.out.println("check");
			}
			String companyName = entry.getValue().split("::")[0];
			String sector = entry.getValue().split("::")[1];
			String ltp = " ";
			String close = " ";
			String ycp = " ";
			data.add(companyName);
			data.add(code);
			data.add(sector);
			String hisQuery = "SELECT MKISTAT_PUB_LAST_TRADED_PRICE, MKISTAT_CLOSE_PRICE, MKISTAT_YDAY_CLOSE_PRICE FROM dse_analyzer.mkistat_archive "
					+ "where MKISTAT_INSTRUMENT_CODE = '"+code+"' "
					+ "and date(MKISTAT_LM_DATE_TIME) = '"+date+"' order by MKISTAT_LM_DATE_TIME desc limit 1;";
			ResultSet rs2  = stmt.executeQuery(hisQuery);
			while(rs2.next()) {
				ltp = rs2.getString("MKISTAT_PUB_LAST_TRADED_PRICE") == null ?"0.0" : rs2.getString("MKISTAT_PUB_LAST_TRADED_PRICE");
				close = rs2.getString("MKISTAT_CLOSE_PRICE")== null ?"0.0" : rs2.getString("MKISTAT_CLOSE_PRICE");
				ycp = rs2.getString("MKISTAT_YDAY_CLOSE_PRICE")== null ?"0.0" :rs2.getString("MKISTAT_YDAY_CLOSE_PRICE");
				data.add(ltp);
				data.add(close);
				data.add(ycp);
				falg=true;
			}
			
			if(falg) {
				companyData.add(data);
			} else {
				data.add("0.0");
				data.add("0.0");
				data.add("0.0");
				companyData.add(data);
			}			
		}
		//Other company data
//		LocalDate queryDate = LocalDate.parse(date);
//		boolean dataFound = false;
//        int attempts = 0;
//        final int maxAttempts = 30;
//        
//        while (!dataFound && attempts < maxAttempts) {
//        	
//            String otherCompany = "SELECT * FROM dse_analyzer.other_company_price WHERE sector != 'Pre IPO' OR sector IS NULL and date = '" + queryDate +"';";
//            try (ResultSet resultSet = stmt.executeQuery(otherCompany)) {
//
//                if (resultSet.next()) {
//                    dataFound = true;
//                    do {
//                    	List<String> data = new ArrayList<>();
//                        data.add(resultSet.getString(2));
//                        data.add(resultSet.getString(3));
//                        data.add(resultSet.getString(4));
//                        data.add(resultSet.getString(5));
//                        data.add(resultSet.getString(6));
//                        data.add(resultSet.getString(7));
//                        companyData.add(data);
//                        // Add data to companyData or any other collection
//                    } while (resultSet.next());
//                } else {
//                    queryDate = queryDate.minusDays(1);
//                    attempts++;
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (!dataFound) {
//            queryDate = LocalDate.now();
//            attempts = 0;
//
//            while (!dataFound && attempts < maxAttempts) {
//                String otherCompany = "SELECT * FROM dse_analyzer.other_company_price WHERE sector != 'Pre IPO' OR sector IS NULL and date = '" + queryDate +"';";
//                try (ResultSet resultSet = stmt.executeQuery(otherCompany)) {
//
//                    if (resultSet.next()) {
//                        dataFound = true;
//                        do {
//                        	List<String> data = new ArrayList<>();
//                            data.add(resultSet.getString(2));
//                            data.add(resultSet.getString(3));
//                            data.add(resultSet.getString(4));
//                            data.add(resultSet.getString(5));
//                            data.add(resultSet.getString(6));
//                            data.add(resultSet.getString(7));
//                            companyData.add(data);
//                            // Add data to companyData or any other collection
//                        } while (resultSet.next());
//                    } else {
//                        queryDate = queryDate.minusDays(1);
//                        attempts++;
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        //PRE IPO SECTOR
        String ipoCompany = "SELECT * FROM dse_analyzer.other_company_price ocp WHERE (code, date) IN (SELECT code, max(date)  FROM dse_analyzer.other_company_price where date <= '"+date+"' group by code);";

        try (ResultSet resultSet = stmt.executeQuery(ipoCompany)) {

            while(resultSet.next()) {
                	List<String> data = new ArrayList<>();
                    data.add(resultSet.getString(2));
                    data.add(resultSet.getString(3));
                    data.add(resultSet.getString(4));
                    data.add(resultSet.getString(5));
                    data.add(resultSet.getString(6));
                    data.add(resultSet.getString(7));
                    companyData.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		String sme = "SELECT ticker, LTP, close, YCP  FROM dse_analyzer.sme";
		ResultSet rs4  = stmt.executeQuery(sme);		
		while(rs4.next()) {
			List<String> data = new ArrayList<>();
			data.add(rs4.getString(1));
			data.add(rs4.getString(1));
			data.add("SME");
			data.add(rs4.getString(2));
			data.add(rs4.getString(3));
			data.add(rs4.getString(4));
			companyData.add(data);
		}
		
		con.close();
//		companyData.add(getExtraPrice("","CAPMUF","Unlisted","102.22","102.22","102.22"));
//		companyData.add(getExtraPrice("","CIBBLSUF","Unlisted","08.63","08.63","08.63"));
//		companyData.add(getExtraPrice("","CSRBGF","Unlisted","9.43","9.43","9.43 "));
//		companyData.add(getExtraPrice("","MBLUF","Unlisted","10.35","10.35","10.35"));
//		companyData.add(getExtraPrice("","SAMLIUF","Unlisted","11.65","11.65","11.65"));
//		companyData.add(getExtraPrice("","SEBL1STUF","Unlisted","10.10","10.10","10.10"));
//		companyData.add(getExtraPrice("","Pre IPO Share Energyprima Ltd.","Pre IPO","45.99","45.99","45.99"));
//		companyData.add(getExtraPrice("","Pre IPO/Non-Listed Private Company Investment Unicorn Industries Ltd.","Pre IPO","11.97","11.97","11.97"));
//		companyData.add(getExtraPrice("","Pre IPO/Non-Listed Private Company Investment Unicorn Industries Ltd. (Preference Shares)","Pre IPO","11.97","11.97","11.97"));
		
		
		return companyData;
		
	}
	
//	public String getHistoricalCloseFromMKstat(String code,String date, Connection con) throws SQLException, ClassNotFoundException{
//
//		Statement stmt = con.createStatement();
//		String value = "";
//		String hisQuery = "SELECT MKISTAT_CLOSE_PRICE FROM dse_analyzer.mkistat_archive WHERE MKISTAT_INSTRUMENT_CODE = '"+code+"' "
//				+ "AND date(MKISTAT_LM_DATE_TIME) = '"+date+"' order by MKISTAT_LM_DATE_TIME desc limit 1;";
//		ResultSet rs2  = stmt.executeQuery(hisQuery);
//		while(rs2.next()) {
//			value = rs2.getString("MKISTAT_CLOSE_PRICE");
//		}
//
//
//		return value;
//	}
	
//	public static  List<String> getBondData(){
//		List<String> price_data = new ArrayList<>() ;
//	}
	
	public static List<String> getExtraPrice(String cName, String code, String sector, String LTP, String close, String YCP){
		List<String> price_data = new ArrayList<>() ;
		price_data.add(cName);
		price_data.add(code);
		price_data.add(sector);
		price_data.add(LTP);
		price_data.add(close);
		price_data.add(YCP);		
		return price_data;
	}
	
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
		con.close();
		return ticker;
	}
	
	public static TreeMap<String, Map<String, String>> getSmeCalculation(ArrayList<String> tickers) throws ClassNotFoundException, SQLException {
		TreeMap<String, Map<String, String>> companyData = new TreeMap<>(); 
		Connection con = connectLRGB();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		for(String ticker : tickers) {
			if(!ticker.equals("WONDERTOYS")) {
				Map<String, String> valueMap = new HashMap<>();
				System.out.println(ticker);
				String query = "SELECT * FROM dse_analyzer.sme where ticker = '"+ticker+"';";
				Map<String, String> tickerData = getDataFromCompanyTableSME(ticker);
				Double outShare =  Double.parseDouble(getOutShateSME(ticker).isEmpty()? "0.0" :getOutShateSME(ticker))  ; 
				Double oneYearChange = get1YearChange(ticker);
				oneYearChange = oneYearChange.isInfinite() || oneYearChange.isNaN() ? 0.0 : oneYearChange;
				Double threeYearchange = get3YearChange(ticker) * 100;
				threeYearchange = threeYearchange.isInfinite() || threeYearchange.isNaN() ? 0.0 : threeYearchange;
				rs = stmt.executeQuery(query);
				while(rs.next()) {
					String sector = tickerData.get("Sector");
					Double eps = Double.parseDouble(tickerData.get("EPS")== null ? "0.0" : tickerData.get("EPS"));
					Double totalDebt = Double.parseDouble(tickerData.get("Total Debt"));
					Double nav = Double.parseDouble(tickerData.get("NAV"));
					Double sales = Double.parseDouble(tickerData.get("Sales Per Share")== null ? "0.0" : tickerData.get("Sales Per Share"));
					String code = rs.getString(2);
					Double ltp = Double.parseDouble( rs.getString(3));
					Double high = Double.parseDouble(  rs.getString(4));
					Double low = Double.parseDouble(  rs.getString(5));
					Double close = Double.parseDouble(  rs.getString(6));
					Double ycp = Double.parseDouble(  rs.getString(7));
					Double change = Double.parseDouble( rs.getString(8) );
					Double trade = Double.parseDouble( rs.getString(9));
					Double value = Double.parseDouble(  rs.getString(10));
					Double volume = Double.parseDouble( rs.getString(11));
					Double div = Double.parseDouble( rs.getString(12) == null ? "0.0" :  rs.getString(12))*10;
					
					Double weightedDiv = ltp == 0?(div/ycp)*100 : (div/ltp)*100;
					weightedDiv = weightedDiv.isInfinite() || weightedDiv.isNaN() ? 0.0 : weightedDiv;
					
					
					Double changeValue =ltp == 0? 0: ((ltp-ycp)/ycp)*100 ;
					changeValue = changeValue.isInfinite() || changeValue.isNaN() ? 0.0 : changeValue;
					Double de = totalDebt / (nav*outShare);
					de = de.isInfinite() || de.isNaN() ? 0.0 : de;
					Double pe = ltp == 0 ? ycp/eps : ltp / eps;
					pe = pe.isInfinite() || pe.isNaN() ? 0.0 : pe;
					Double pb = ltp / nav;
					pb = pb.isInfinite() || pb.isNaN() ? 0.0 : pb;
					Double ps = ltp/sales;
					ps = ps.isInfinite() || ps.isNaN() ? 0.0 : ps;
					
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
					
					
					companyData.put(ticker, valueMap);
					//System.out.println(ltp+"cng "+ changeValue+"de "+de+"pe "+pe+"pb "+pb+"ps "+ps+"eps "+eps+"one "+oneYearChange);
				}
				
			}
			}
		con.close();	
		return companyData;
	}
	
	public static Map<String, String> getDataFromCompanyTableSME(String ticker) throws ClassNotFoundException, SQLException {
		Connection con = connectLRGB();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		Map<String, String> data = new HashMap<>();
		
		String query = "select code, sector, eps,total_debt,equities_per_share,sales_per_share from dse_analyzer.company where code = '"+ticker+"';";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			data.put("Code", rs.getString("code"));
			data.put("Sector", rs.getString("sector"));
			data.put("EPS", rs.getString("eps"));
			data.put("Total Debt", rs.getString("total_debt")==null? "0.0" :  rs.getString("total_debt"));
			data.put("NAV", rs.getString("equities_per_share"));
			data.put("Sales Per Share", rs.getString("sales_per_share"));
		}
		con.close();
		return data;
	}
	
	public static String getOutShateSME(String ticker) throws ClassNotFoundException, SQLException {
		Connection con = connectLRGB();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		String query = "SELECT out_shares FROM dse_analyzer.eps_data where code = '"+ticker+"' and out_shares is not null;";
		String outShare = "";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			outShare = rs.getString(1);
		}
		con.close();
		return outShare;
		
	}
	
	public static Double get3YearChange(String ticker) throws ClassNotFoundException, SQLException {
		Connection con = connectLRGB();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		Double threeYearChange = 0.0;
		Double epsCurrent=0.0;
		Double eps3year=0.0;
		String query = "SELECT * FROM dse_analyzer.eps_data where code = '"+ticker+"' and year=2023 ";
		String query1 = "SELECT * FROM dse_analyzer.eps_data where code = '"+ticker+"' and year=2020 ";
		rs = stmt.executeQuery(query);
		//List<Double> epsCurrent = new ArrayList<>();
		while(rs.next()) {
			epsCurrent = rs.getDouble("annual_eps");
		}
		
		rs = stmt.executeQuery(query1);
		//List<Double> epsCurrent = new ArrayList<>();
		while(rs.next()) {
			eps3year = rs.getDouble("annual_eps");
		}
		

			Double diiv = epsCurrent/eps3year;
			threeYearChange = Math.pow(diiv, 1.0/3.0);
		
			threeYearChange = threeYearChange.isInfinite() ? 0.0 : threeYearChange;
			con.close();
			
			System.out.println(threeYearChange);
			return threeYearChange;
		
			
		}		
		
	
	
	public static Double get1YearChange(String ticker) throws ClassNotFoundException, SQLException {
		Connection con = connectLRGB();
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		String query = "SELECT * FROM dse_analyzer.eps_data where code = '"+ticker+"' order by year desc limit 2;";
		rs = stmt.executeQuery(query);
		Double[] eps = new Double[2];
		int i = 0;
		while(rs.next()) {
			Double data = rs.getDouble("annual_eps");
			eps[i]= data.isNaN() ? 0.0 : data;
			i++;
		}
		Double oneYearChange = ((eps[0] - eps[1])/eps[1]) * 100;
		Double value = oneYearChange.isInfinite() ? 0.0 : oneYearChange;
		con.close();
		return value;
	}
	
	
	public static Connection connectLRGB() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://192.168.100.12:3306/dse_analyzer?rewriteBatchedStatements=true", "lrgb_loader",
		"developer4");
		con.setAutoCommit(true);
		return con;
	}
}
