package com.LRG.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.LRG.Utils.CommonUtils;
import com.LRG.Utils.Constants;
import com.LRG.Utils.StatsUtils;
import com.LRG.model.AnnualisedContributionWithdrawalDto;
import com.LRG.model.OptimizerDto;
import com.LRG.model.OptimizerRequest;
import com.LRG.model.OptimizerResponse;
import com.LRG.model.RetirementPlanningDto;
import com.LRG.model.TickerDto;
import com.LRG.repository.AdjustedPriceRepository;

@Service
public class MyFinanceService {
	
	@Value("${optimizer.exe.file.path}")
	private String EXE_FILE_PATH;
	
	private static final DecimalFormat DF4 = new DecimalFormat("0.0000");
	private static final DecimalFormat DF2 = new DecimalFormat("0.00");
	
	@Autowired
	private AdjustedPriceRepository adjustedPriceRepository;

	public RetirementPlanningDto generateContributionWithdrawlList(RetirementPlanningDto shortFallCalculatorDto) {
		List<Double> contributionList = new ArrayList<Double>();
		List<Double> withdrawlList = new ArrayList<Double>();
		List<AnnualisedContributionWithdrawalDto> investmentList = null;
		int withdrawalDelay = shortFallCalculatorDto.getRetirementAge() - shortFallCalculatorDto.getInvestorAge();
		Calendar cal = Calendar.getInstance();
		if(shortFallCalculatorDto.getContributionWithdrawalType().equalsIgnoreCase(Constants.DEFAULT_CONTRIBUTION_WITHDRAWL_TYPE)) {
			investmentList = new ArrayList<AnnualisedContributionWithdrawalDto>();
			if(shortFallCalculatorDto.getShortTerm() != null) {
				AnnualisedContributionWithdrawalDto contributionWithdrawalDto = new AnnualisedContributionWithdrawalDto();
				contributionWithdrawalDto.setName("Short");
				contributionWithdrawalDto.setStartYear(cal.get(Calendar.YEAR));
				contributionWithdrawalDto.setEndYear(cal.get(Calendar.YEAR));
				contributionWithdrawalDto.setAnnualAmount(Math.abs(shortFallCalculatorDto.getShortTerm()));
				if(shortFallCalculatorDto.getShortTerm() > 0) {
					contributionWithdrawalDto.setType(Constants.CONTRIBUTION);
				} else {
					contributionWithdrawalDto.setType(Constants.WITHDRAWL);
				}
				investmentList.add(contributionWithdrawalDto);
			}
			if(shortFallCalculatorDto.getMediumTerm() != null) {
				AnnualisedContributionWithdrawalDto contributionWithdrawalDto = new AnnualisedContributionWithdrawalDto();
				contributionWithdrawalDto.setName("Medium");
				cal.add(Calendar.YEAR, 1);
				contributionWithdrawalDto.setStartYear(cal.get(Calendar.YEAR));
				cal.add(Calendar.YEAR, withdrawalDelay-1);
				contributionWithdrawalDto.setEndYear(cal.get(Calendar.YEAR));
				contributionWithdrawalDto.setAnnualAmount(Math.abs(shortFallCalculatorDto.getMediumTerm()));
				if(shortFallCalculatorDto.getMediumTerm() > 0) {
					contributionWithdrawalDto.setType(Constants.CONTRIBUTION);
				} else {
					contributionWithdrawalDto.setType(Constants.WITHDRAWL);
				}
				investmentList.add(contributionWithdrawalDto);
			}
			if(shortFallCalculatorDto.getLongTerm() != null) {
				cal = cal.getInstance();
				AnnualisedContributionWithdrawalDto contributionWithdrawalDto = new AnnualisedContributionWithdrawalDto();
				contributionWithdrawalDto.setName("Long");
				cal.add(Calendar.YEAR, withdrawalDelay+1);
				contributionWithdrawalDto.setStartYear(cal.get(Calendar.YEAR));
				cal.add(Calendar.YEAR, Constants.MAX_INVESTOR_AGE - (shortFallCalculatorDto.getRetirementAge()+1));
				contributionWithdrawalDto.setEndYear(cal.get(Calendar.YEAR));
				contributionWithdrawalDto.setAnnualAmount(Math.abs(shortFallCalculatorDto.getLongTerm()));
				if(shortFallCalculatorDto.getLongTerm() > 0) {
					contributionWithdrawalDto.setType(Constants.CONTRIBUTION);
				} else {
					contributionWithdrawalDto.setType(Constants.WITHDRAWL);
				}
				investmentList.add(contributionWithdrawalDto);
			}
		} else {
			investmentList = shortFallCalculatorDto.getAnnualisedContributionWithdrawal();
		}

		cal = Calendar.getInstance();
		for(int i=0; i<Constants.MAX_INVESTOR_AGE; i++) {
			if(i >= shortFallCalculatorDto.getInvestorAge()-1) {
					Double contributionAmount = 0.0;
					Double withdrawlAmount = 0.0;
					Integer year = cal.get(Calendar.YEAR);
					for(AnnualisedContributionWithdrawalDto contributionWithdrawalDto : investmentList) {
						if(year >= contributionWithdrawalDto.getStartYear() && year <= contributionWithdrawalDto.getEndYear()) {
							if(contributionWithdrawalDto.getType().equalsIgnoreCase(Constants.CONTRIBUTION)) {
								contributionAmount += contributionWithdrawalDto.getAnnualAmount();
							} else {
								withdrawlAmount += contributionWithdrawalDto.getAnnualAmount();
							}
						}
					}
					contributionList.add(contributionAmount);
					withdrawlList.add(withdrawlAmount);
					cal.add(Calendar.YEAR, 1);
				} 
		}
		shortFallCalculatorDto.setAnnualContributionList(contributionList);
		shortFallCalculatorDto.setAnnualWithdrawlList(withdrawlList);
		return shortFallCalculatorDto;
	}
	
	public BigDecimal calculateExpectedShortfall(RetirementPlanningDto shortFallCalculatorDto, List<Double> persistencyList, Double expectedReturn, Double expectedVolatility) {
		Double initialAssets = (shortFallCalculatorDto.getInitialAssets() < 1) ? 1 : shortFallCalculatorDto.getInitialAssets();
		List<Double> contributionList = shortFallCalculatorDto.getAnnualContributionList();
		List<Double> withdrawlList = shortFallCalculatorDto.getAnnualWithdrawlList();
		int simulationListSize = (shortFallCalculatorDto.getRetirementAge()  + Constants.MAX_POST_RETIREMENT_AGE_SIMULATION + 2) - shortFallCalculatorDto.getInvestorAge();
		List<List<Double>> assetValPerSimulation = StatsUtils.getListOfDoubleList(simulationListSize, Constants.NUM_SIMULATIONS.intValue());
		Double lastAssetValueSum = 0.0;
		List<Double> lastAssetValueList = new ArrayList<Double>();
		
		int shortfallProbabilityCounter = 0;
		Double shortFall = 0.0;
		Double AV;
		for(int i=0; i<Constants.NUM_SIMULATIONS; i++) {
			AV = initialAssets;
			for(int j = 0; j < (Constants.MAX_INVESTOR_AGE-shortFallCalculatorDto.getInvestorAge()); j++) {
				if(j == 0) {
					assetValPerSimulation.get(j).set(i,Double.parseDouble(DF4.format(Math.max(AV, 0.0))));
				}				
			    Double Z = StatsUtils.NORMINV(new Random().nextDouble(), 0.0, expectedVolatility);
			    AV = AV * Math.exp((expectedReturn - 0.5 * expectedVolatility * expectedVolatility) + Z * expectedVolatility);
			    AV = AV + (j >= contributionList.size() ? 0.0 : contributionList.get(j)) - (j >= withdrawlList.size() ? 0.0 : withdrawlList.get(j));
			    Double persistencyVal = j >= persistencyList.size() ? 0.0 : persistencyList.get(j).doubleValue();
		    	shortFall = shortFall + Math.abs(persistencyVal * Math.min(AV, 0));
		    	if(j < simulationListSize-1) {
					assetValPerSimulation.get(j+1).set(i,Double.parseDouble(DF4.format(Math.max(AV, 0.0))));					
				}
				if (j == (simulationListSize - 2)) {
					lastAssetValueList.add(Math.max(AV, 0.0));
				}
	       		AV = Math.max(AV, 0);				
			}
			if(AV == 0) {
				shortfallProbabilityCounter++;
			}
		}
		lastAssetValueSum = StatsUtils.getSumOfList(lastAssetValueList);
		shortFallCalculatorDto.setAvgAssetValue(lastAssetValueSum / lastAssetValueList.size());
		shortFallCalculatorDto.setAssetValuePerSimulationList(getRelevantAssetValueSimulationSeriesList(assetValPerSimulation));
		if(shortFall.doubleValue() < 0.0) shortFall = 0.0;
		Double shortfallProbability = Double.parseDouble(DF2.format((shortfallProbabilityCounter/Constants.NUM_SIMULATIONS) * 100));
		shortFallCalculatorDto.setShortfallProbability(shortfallProbability);
		return new BigDecimal(shortFall/Constants.NUM_SIMULATIONS).setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
	private List<List<Double>> getRelevantAssetValueSimulationSeriesList(List<List<Double>> assetValPerSimulation) {
		List<List<Double>> statsPerSimulationList = new ArrayList<List<Double>>();
		for(List<Double> AVList : assetValPerSimulation) {
			List<Double> statsList = new ArrayList<Double>();
			Double[] AVDblArr = CommonUtils.getDoubleArray(AVList);
			Double mean = StatsUtils.mean(AVDblArr);
			Double sd = StatsUtils.getStandardDeviation(AVDblArr);
			sd = sd.equals(Double.NaN) ? 0.0 : sd;
			statsList.add(Math.max((mean + (3*sd)), 0.0));
			statsList.add(Math.max((mean + (2*sd)), 0.0));
			statsList.add(Math.max((mean + sd), 0.0));
			statsList.add(Math.max(mean, 0.0));
			statsList.add(Math.max((mean - sd), 0.0));
			statsList.add(Math.max((mean - (2*sd)), 0.0));
			statsList.add(Math.max((mean - (3*sd)), 0.0));
			statsPerSimulationList.add(statsList);
		}
		List<List<Double>> finalSimulationChartList = new ArrayList<List<Double>>();
		for(int i=0; i<Constants.MAX_SIMULATION_LINE; i++) {
			List<Double> statsSimList = new ArrayList<Double>();
			for(List<Double> list : statsPerSimulationList) {
				statsSimList.add(list.get(i));
			}
			finalSimulationChartList.add(statsSimList);
		}
		
		return finalSimulationChartList;
	}
	
	public OptimizerRequest generateOptimizerRequest(Double revisedTargetReturn, RetirementPlanningDto shortFallCalculatorDto) throws Exception {
		OptimizerRequest optReqst = new OptimizerRequest();
		String endDate = new SimpleDateFormat(Constants.DATE_MYSQL_FORMAT).format(new Date());
		String startDate = "2018-01-01"; // Hard-coded to include Financial crisis of 2007
		optReqst.setOptimMode(Constants.DEFAULT_OPTIM_MODE_OPTION);
		optReqst.setAnnualReturnOnCash(new BigDecimal(0.01).setScale(2, BigDecimal.ROUND_HALF_UP));
		optReqst.setPortfolioId(1346);
		optReqst.setcVarLevel(new BigDecimal(0.9).setScale(2, BigDecimal.ROUND_HALF_UP));
		optReqst.setTargetRet(new BigDecimal(revisedTargetReturn).setScale(2, BigDecimal.ROUND_HALF_UP));
		optReqst.setEndDate(endDate);
		optReqst.setSlippage(new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP));
		optReqst.setShowChart("All");
		optReqst.setOptType("OPT");
		optReqst.setStartDate(startDate);
		optReqst.setInitialValue(100);
		optReqst.setRunEfficientFrontier("false");
		optReqst.setIsRetirementPlanOptimzation(true);
		List<TickerDto> tdList = new ArrayList<TickerDto>();
		TickerDto tdMMF = new TickerDto();
		tdMMF.setMaxWt(Constants.DEFAULT_MIN_WEIGHT);
		tdMMF.setMinWt(Constants.DEFAULT_MIN_WEIGHT);
		tdMMF.setSymbol(Constants.MONEY_MKT_FUNDS);
		tdList.add(tdMMF);
		
		for(String pt : Constants.portfolio2_tickers) {
			TickerDto td = new TickerDto();
			td.setMaxWt(1.0);
			td.setMinWt(0.0);
			td.setSymbol(pt);
			tdList.add(td);
		}
		optReqst.setInputTickers(tdList);
		return optReqst;
	}
	
	public OptimizerResponse optimize(OptimizerRequest request) {
		OptimizerResponse response = new OptimizerResponse();
		OptimizerDto optDto = new OptimizerDto();
		try {
					
			optDto = initializeOptimizerDto(request);
			
			Map<String, Object> tsPriceMap = getTickerPriceSeriesMap(request.getInputTickers(), request.getStartDate(), request.getEndDate());
			Map<String, Object> tsMap = getTSReturnFromTSPrice(tsPriceMap);
			List<String> dateList = (ArrayList<String>)tsMap.get(Constants.OPTOUT_DATE);
			if(dateList.isEmpty() || (request.getRebalance() != null && dateList.size() <= request.getRebalance().intValue()) 
					|| (request.getLookBack() != null && dateList.size() <= request.getLookBack().intValue())) {
				tsPriceMap = getTickerPriceSeriesMap(request.getInputTickers(), request.getStartDate(), request.getEndDate());
				tsMap = getTSReturnFromTSPrice(tsPriceMap);
				response.setUsingAllData(true);
			}
			dateList = (ArrayList<String>)tsMap.get(Constants.OPTOUT_DATE);
			if(dateList.isEmpty()) {
				throw new Exception("No records found where returns are available for all components");
			} else if(request.getLookBack() != null && dateList.size() <= request.getLookBack().intValue()) {
				throw new Exception("timeSeries record size is less than specified Lookback value");
			} else if(request.getRebalance() != null && dateList.size() <= request.getRebalance().intValue()) {
				throw new Exception("timeSeries record size is less than specified Rebalance value");
			} else {
				optDto.setInputTimeSeries(tsMap);
			}
			optimizeWrapper(request, optDto);
			
			//Save optimized weights as fixed weights for future use.
			Map<String, Double> tickerWtMap = new HashMap<String, Double>();
			if(optDto.getOptModule().equals("OPT")) {
				Map<String, String> outputStats = optDto.getOutputStats();
				double mmfFixedWt = Double.parseDouble(DF2.format(Double.parseDouble(outputStats.get(Constants.MONEY_MKT_FUNDS))));
				Double sumOfFixedWt = mmfFixedWt; 
				//Map<String, Double> tickerWtMap = new HashMap<String, Double>();
				
				if(optDto.getTickers() != null && !optDto.getTickers().isEmpty()) {
					for(String symbol: optDto.getTickers()) {
						double tickerWt = Double.parseDouble(DF2.format(Double.parseDouble(outputStats.get(symbol))));
						sumOfFixedWt += tickerWt;
					}
				}
				if(optDto.getPortfolios() != null && ! optDto.getPortfolios().isEmpty()) {
					for(String name : optDto.getPortfolios()) {
						double subPortfolioWt = Double.parseDouble(DF2.format(Double.parseDouble(outputStats.get(name))));
						sumOfFixedWt = sumOfFixedWt + subPortfolioWt;
					}
				}
				if(sumOfFixedWt == 0.0) {
					tickerWtMap.put(Constants.MONEY_MKT_FUNDS, 0.0);
				} else {
					tickerWtMap.put(Constants.MONEY_MKT_FUNDS, Double.parseDouble(DF2.format(mmfFixedWt/sumOfFixedWt)));
				}
				if(optDto.getTickers() != null && !optDto.getTickers().isEmpty()) {
					for(String symbol: optDto.getTickers()) {
						if(sumOfFixedWt == 0.0) {
							tickerWtMap.put(symbol, 0.0);
						} else {
							tickerWtMap.put(symbol, (Double.parseDouble(DF2.format(Double.parseDouble(outputStats.get(symbol))/sumOfFixedWt))));
						}
					}
				}
				if(optDto.getPortfolios() != null && ! optDto.getPortfolios().isEmpty()) {
					for(String name : optDto.getPortfolios()) {
						if(sumOfFixedWt == 0.0) {
							tickerWtMap.put(name, 0.0);
						} else {
							tickerWtMap.put(name, (Double.parseDouble(DF2.format(Double.parseDouble(outputStats.get(name))/sumOfFixedWt))));
						}
					}
				}

				response.setOptimizedWTMap(optDto.getOutputStats());
			}
			optDto.setRequest(request);
			response.setSuccess(true);
			response.setPortfolioReturn(Double.parseDouble(optDto.getOutputStats().get(Constants.OPTOUT_OPTMU)));
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorType(ex.getClass().getSimpleName());
			response.setErrorCode(Constants.SYSTEM_ERROR);
			response.setErrorDescription(ex.getMessage() + ": " + ex.toString());
		}
		return response;
	}
	
	private static OptimizerDto initializeOptimizerDto(OptimizerRequest request) {
		OptimizerDto optDto = new OptimizerDto();
		
		optDto.setOptModule("OPT");		
		optDto.setInitialValue(request.getInitialValue());
		optDto.setAnnRetCash(request.getAnnualReturnOnCash().doubleValue());
		optDto.setBenchmark(request.getBenchmark());
		optDto.setDailyMMFReturn(StatsUtils.annualToDailyReturn(request.getAnnualReturnOnCash(), Constants.TICKER_RETURN_FORMAT).doubleValue());
		optDto.setcVarLevel(request.getcVarLevel().doubleValue());
		optDto.setSlippage(request.getSlippage().doubleValue());
		optDto.setShowChart(request.getShowChart());
		
		if(request.getInputTickers() != null && !request.getInputTickers().isEmpty()) {
			List<String> symbols = new ArrayList<String>();
			Collections.sort(request.getInputTickers());
			for(TickerDto ticker: request.getInputTickers()) {
				if(! Constants.MONEY_MKT_FUNDS.equals(ticker.getSymbol())) {
					symbols.add(ticker.getSymbol());
				}
			}
			optDto.setTickers(symbols);
		}
		return optDto;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getTickerPriceSeriesMap(List<TickerDto> inputTickers,String startDate,String endDate) throws ParseException {
		Map<String, Object> tsMap = new HashMap<String, Object>();				
		Set<String> symbolList = new HashSet<String>();
		List<String> tickerList = new ArrayList<String>();
		if(inputTickers != null) {
			for(TickerDto dto: inputTickers) { 
				symbolList.add(dto.getSymbol());
				if(!dto.getSymbol().equalsIgnoreCase(Constants.MONEY_MKT_FUNDS)) {
					tickerList.add(dto.getSymbol());
				}
			}
		}
		
		List<Object[]> priceList = adjustedPriceRepository.getPriceDataForOptimizer(tickerList);		
		if(priceList == null || priceList.isEmpty()) {
			tsMap.put(Constants.OPTOUT_DATE, new ArrayList<String>());
			return tsMap;
		}
		
		Map<String, Object> tickerPriceMap = new HashMap<String, Object>();
		tickerPriceMap.put(Constants.OPTOUT_DATE, new ArrayList<String>());
		for(String symbol : symbolList) {
			if(!symbol.equalsIgnoreCase(Constants.MONEY_MKT_FUNDS)) {
				tickerPriceMap.put(symbol, new ArrayList<String>());
				//tickerPriceMap.put(symbol+Constants.volumeSuffix, new ArrayList<String>());
			}
		}
		if(priceList != null && !priceList.isEmpty()) {
			String prevDateStr =  priceList.get(0)[5].toString();
			int index = 0;
			Map<String, Double> previousPrice = new HashMap<String, Double>();
			Long symbolSize = 0l;
			for(Object[] objArray : priceList) {
				String symbol = objArray[1].toString();
				if(symbolSize == (symbolList.size() * 2)) {
					break;
				}
				if(!symbol.equalsIgnoreCase(Constants.OPTOUT_DATE) && !previousPrice.containsKey(symbol)) {
					symbolSize ++;
					previousPrice.put(symbol, (Double.parseDouble(objArray[2].toString())));
					//previousPrice.put(symbol+Constants.volumeSuffix, (Double.parseDouble(objArray[3].toString())));
				}			
			}
		
			for(Object[] objArray : priceList) {
				String priceDateStr = objArray[5].toString();
				String ticker = objArray[1].toString();
				Double tickerPrice = Double.parseDouble(objArray[2].toString());
				//Double tickerVolume = Double.parseDouble(objArray[3].toString());
				if(index == 0) {
					((ArrayList<String>) tickerPriceMap.get(Constants.OPTOUT_DATE)).add(new SimpleDateFormat("MM/dd/yyyy")
							.format(new SimpleDateFormat(Constants.DATE_MYSQL_FORMAT).parse(priceDateStr)));
				}
				if(prevDateStr.equalsIgnoreCase(priceDateStr)) {
					if(symbolList.contains(ticker)) {
						((ArrayList<Double>) tickerPriceMap.get(ticker)).add(tickerPrice);
						previousPrice.put(ticker, tickerPrice);
						//((ArrayList<Double>) tickerPriceMap.get(ticker+Constants.volumeSuffix)).add(tickerVolume);
						//previousPrice.put(ticker+Constants.volumeSuffix, tickerVolume);
					}
					index ++;
				} else {
					fillZeroForPrice(tickerPriceMap, ((ArrayList<String>) tickerPriceMap.get(Constants.OPTOUT_DATE)).size(), previousPrice);
				
					((ArrayList<String>) tickerPriceMap.get(Constants.OPTOUT_DATE)).add(new SimpleDateFormat("MM/dd/yyyy")
							.format(new SimpleDateFormat(Constants.DATE_MYSQL_FORMAT).parse(priceDateStr)));
					if(symbolList.contains(ticker)) {
						((ArrayList<Double>) tickerPriceMap.get(ticker)).add(tickerPrice);
						previousPrice.put(ticker, tickerPrice);
						//((ArrayList<Double>) tickerPriceMap.get(ticker+Constants.volumeSuffix)).add(tickerVolume);
						//previousPrice.put(ticker+Constants.volumeSuffix, tickerVolume);
					}
					prevDateStr = priceDateStr;				
					index++;
				}
			
				if(index >= priceList.size()) {
					fillZeroForPrice(tickerPriceMap, ((ArrayList<String>) tickerPriceMap.get(Constants.OPTOUT_DATE)).size(), previousPrice);
				} 
			}				
		}		
		List<String> dateList = ((ArrayList<String>) tickerPriceMap.get(Constants.OPTOUT_DATE));
		tsMap.put(Constants.OPTOUT_DATE, dateList);
		if(inputTickers != null) {
			for(TickerDto tDto : inputTickers) {
				if(tickerPriceMap.containsKey(tDto.getSymbol())) {
					tsMap.put(tDto.getSymbol(), ((ArrayList<String>) tickerPriceMap.get(tDto.getSymbol())));
				}
			}
		}
		
		if(inputTickers != null) {
			for(TickerDto tDto : inputTickers) {
				if(tickerPriceMap.containsKey(tDto.getSymbol())) {
					tsMap.put(tDto.getSymbol(), ((ArrayList<String>) tickerPriceMap.get(tDto.getSymbol())));
					//tsMap.put(tDto.getSymbol()+Constants.volumeSuffix, ((ArrayList<String>) tickerPriceMap.get(tDto.getSymbol()+Constants.volumeSuffix)));
				}
			}
		}
		return tsMap;
	}
	
	@SuppressWarnings("unchecked")
	private static void fillZeroForPrice(Map<String, Object> tickerPriceMap, int dateListSize, Map<String, Double> previousVal) {
		for(Entry<String, Object>  entry : tickerPriceMap.entrySet()) {
			if(!entry.getKey().equalsIgnoreCase(Constants.OPTOUT_DATE)) {
				if(((ArrayList<Double>) entry.getValue()).size() == (dateListSize -1)) {					
					((ArrayList<Double>) entry.getValue()).add(previousVal.get(entry.getKey()));
				}
			}
		}
	}
	
	private static Map<String, Object> getTSReturnFromTSPrice(Map<String, Object> tickerPriceMap) {
		Map<String, Object> tsMap = new LinkedHashMap<String, Object>();
		Map<String, List<Double>> returnListMap = new HashMap<String, List<Double>>();
		
		try {
			//Converting price to return here
			for(String key: tickerPriceMap.keySet()) {
				if(!key.equals(Constants.pDate) && !key.equals(Constants.OPTOUT_DATE) && !key.equals(Constants.pReturn) &&
						!key.equals(Constants.MONEY_MKT_FUNDS) && !key.equals(Constants.SUBSCRIPTION_EXCEL_MAP_KEY) && !key.endsWith("pVol") &&
						!key.endsWith(Constants.subPortfolioDateSuffix)) {
					@SuppressWarnings("unchecked")
					ArrayList<Double> TicPriceList = (ArrayList<Double>) tickerPriceMap.get(key);
					returnListMap.put(key, StatsUtils.getReturnFromPrice(TicPriceList));
				}
			}
			for(String symbol : returnListMap.keySet()) {
				if(symbol.equalsIgnoreCase(Constants.pPrice)) {
					tickerPriceMap.put(Constants.pReturn, returnListMap.get(symbol));
				} else {
					tickerPriceMap.put(symbol, returnListMap.get(symbol));
				}
			}
			
			@SuppressWarnings("unchecked")
			List<String> dateList = ((ArrayList<String>) tickerPriceMap.get(Constants.OPTOUT_DATE));
			tsMap.put(Constants.OPTOUT_DATE, dateList);
			
			for(String symbol : tickerPriceMap.keySet()) {
				tsMap.put(symbol, tickerPriceMap.get(symbol));
			}	
			
		}catch(Exception e) {
			System.out.println(e);
		}	
		
		return tsMap;
	}
	
	private void optimizeWrapper(OptimizerRequest request, OptimizerDto optDto) throws Exception {
		//String rootFolder = Constants.WIN_FILE_PATH;
		String rootFolder = EXE_FILE_PATH;
		String exePath = rootFolder + "/STRAT.exe";
		String tempDirPath = rootFolder + "/" + System.currentTimeMillis();
		String inpFileName = tempDirPath + "/input";
		//String inpFileName = rootFolder + "input";
		String csvFilePath = tempDirPath + "/PortfolioWalkForward.csv";
		
		System.out.println("time for folder creation" + System.currentTimeMillis());
		System.out.println("folder name" + tempDirPath);
		File tmpDir = new File(tempDirPath);
		if(! tmpDir.mkdir()) {
			System.out.println("mkdir status" + tmpDir.mkdir());
			System.out.println("Error while creating temp folder for exe input/output files for request#" + optDto.getRequestId());
			throw new Exception("Error while creating temp folder for exe input/output files");
		}

		if(! saveFile(inpFileName + ".inp", generateInputFileString(request, optDto))) {
			System.out.println("Error while creating input file for exe for request#" + optDto.getRequestId());
			throw new Exception("Error while creating input file for exe");
		}
		
		String optimModeOption = Constants.DEFAULT_OPTIM_MODE_OPTION;
		ProcessBuilder builder = new ProcessBuilder(exePath, inpFileName, optimModeOption, "-1", "FORMATTED");
		builder.directory(new File(tempDirPath));
		builder.redirectErrorStream(true);
		int exitValue = 1;
		boolean hasExit = false;
		try {
			long exeStartTime = System.currentTimeMillis();
			Process exeProc = builder.start();
			while(!hasExit){
				if ((System.currentTimeMillis() - exeStartTime) > Constants.EXECUTE_TIME_LIMIT_FOR_EXE){
					exeProc.destroy();
					exitValue = 0;
					break;
				}else{
					try {
						exitValue = exeProc.exitValue();
						hasExit = true;
				    } catch (IllegalThreadStateException e) {
				    	hasExit = false;
				    }
				}
			}
		} catch (Exception ex) {
			System.out.println("Error while running exe process for request#" + optDto.getRequestId()+" due to "+ ex);
			throw new Exception("Error while running exe process");
		}
		if(exitValue > 0) {
			System.out.println("Exe process completed with exit code " + exitValue + " for request#" + optDto.getRequestId());
			throw new Exception("Something went wrong while running optimizer");
		}
		if(!hasExit){
			System.out.println("Exe process interrupted with exit code " + exitValue + " for request# " + optDto.getRequestId());
			throw new Exception("No return data found. Please change input values and try again.");
		}

		String csvStr = readFile(csvFilePath);
		if(csvStr == null) {
			System.out.println("Error while reading exe output file. Missing, empty or invalid output for request#" + optDto.getRequestId());
			throw new Exception("Error while reading exe output file. Missing, empty or invalid output");
		}
		
		try {
			FileUtils.deleteDirectory(tmpDir);
		} catch (Exception ex) {
			System.out.println("Error removing temp folder due to "+ ex);
		}
//		String csvStr = "TARGETRET,VARLEVEL,CRA,CRP,EXPWT,LOOKBACK WINDOW,MoneyMktFunds,10-yr_T-Bond_Index,5-yr_T-Bond_Index,FD_Index,LR_Global_Index,SND_Index,OPT. MU,OPT. VOL,OPT. DAILY VAR,OPT. DAILY CVAR,WTSUM,ABSWTSUM,MUPENALTY,WTPENALTY,WTSUMPENALTY,CVARPENALTY,ITER\n"
//				+ "0.16,0.9,0.5,3,1,1017,0,1,0,0,0,0,0.079218,0.0071428,0,0,1,1,0,1,0,6.93213e-310,0";
		System.out.println(csvStr);
		parseOutputFileString(csvStr, optDto);
	}
	
	private static boolean saveFile(String filePath, String text) {
		boolean success = false;
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
			writer.write(text);
			writer.close();
			success = true;
		} catch (Exception e) {
			System.out.println("Error saving file at " + filePath+" due to "+e);
		}
		return success;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	private static String generateInputFileString(OptimizerRequest request, OptimizerDto optDto) {
		List<TickerDto> inputTickers = request.getInputTickers() != null ? request.getInputTickers() : new ArrayList<TickerDto>();
		List<String> portfoliosList =  request.getInputPortfolios() != null ?  request.getInputPortfolios() : new ArrayList<String>();
		Map<String, Object> tsMap = optDto.getInputTimeSeries();
		ArrayList<String> dateList = (ArrayList<String>)tsMap.get(Constants.OPTOUT_DATE);

		StringBuilder sb = new StringBuilder();
		sb.append("OPTIM_START").append("\n");
		sb.append("NUMTICKERS ").append(inputTickers.size()+portfoliosList.size()).append("\n");
		sb.append("FILTER");
		for(TickerDto ticker: inputTickers) {
			sb.append(" ").append(ticker.getFilter()!=null ? ticker.getFilter() : Constants.DEFAULT_TICKER_FILTER);
		}
		for(@SuppressWarnings("unused") String portfolioDto : portfoliosList) {
			sb.append(" ").append(Constants.DEFAULT_TICKER_FILTER);
		}
		sb.append("\n");
		sb.append("TICKERS");
		for(TickerDto ticker: inputTickers) {
			sb.append(" ").append(ticker.getSymbol().replace(" ","_"));
		}
		for(String portfolioDto : portfoliosList) {
			sb.append(" ").append(URLEncoder.encode(portfolioDto));
		}
		sb.append("\n");
		sb.append("MINWTS");
		for(TickerDto ticker: inputTickers) {
			sb.append(" ").append(ticker.getMinWt());
		}
		sb.append("\n");
		sb.append("MAXWTS");
		for(TickerDto ticker: inputTickers) {
			sb.append(" ").append(ticker.getMaxWt());
		}
		
		sb.append("\n");
		sb.append("RISKFREERATE ").append(request.getAnnualReturnOnCash()).append("\n");
		sb.append("CVARLEVEL ").append(request.getcVarLevel()).append("\n");
		sb.append("TARGETRET ").append(request.getTargetRet()).append("\n");
		sb.append("SLIPPAGE ").append(request.getSlippage()).append("\n");		
		sb.append("OPTIMMODE ").append(request.getOptimMode()).append("\n");
		sb.append("LOOKBACK ").append(dateList.size()).append("\n");
		sb.append("RUNGRID ").append("TRUE").append("\n");
		if("true".equals(request.getRunEfficientFrontier())) {
			sb.append("RUNEFFICIENTFRONTIER TRUE \n");
		}
		sb.append("NUMITER ").append(Constants.DEFAULT_NUM_ITERATIONS).append("\n");
		sb.append("TIMESERIES_START\n");
		
		for(int i = 0; i < dateList.size(); i++) {
			sb.append(dateList.get(i)).append(" ").append(optDto.getDailyMMFReturn());
			for(String symbol: optDto.getTickers()) {
				sb.append(" ").append(((ArrayList<Double>)tsMap.get(symbol)).get(i));
			}
			for(String pfDto: portfoliosList) {
				sb.append(" ").append(((ArrayList<Double>)tsMap.get(pfDto)).get(i));
			}
			sb.append("\n");
		}
		
		sb.append("TIMESERIES_END\n");
		sb.append("OPTIM_END\n");
		
		return sb.toString();
	}
	
	private static String readFile(String filePath) {
		String outStr = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			StringBuilder sb = new StringBuilder();
			String str = reader.readLine();
			while(str != null) {
				sb.append(str).append("\n");
				str = reader.readLine();
			}
			reader.close();
			outStr = sb.toString();
		} catch (Exception e) {
			System.out.println("Error reading file at " + filePath);
		}
		return outStr;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	private static void parseOutputFileString(String csvStr, OptimizerDto optDto) {
		Map<String, String> statsMap = new HashMap<String, String>();
		Map<String, Object> outputSeries = new HashMap<String, Object>();
		String[] rows = csvStr.split("\n");
		String[] headerRow, dataRow;
		List<List<String>> efData = new ArrayList<List<String>>(); 
		
				headerRow = rows[0].trim().split(",");
				dataRow = rows[1].trim().split(",");
				for(int i=0; i < dataRow.length; i++) {
					statsMap.put(URLDecoder.decode(headerRow[i]).replace("_"," "), dataRow[i]);
				}
				if (rows.length > 2){
					int efStartIndex =  Arrays.asList(rows).indexOf("EFFICIENT_FRONTIER_OUTPUT_START");
					if(efStartIndex != -1){
						List<String> efDataIndex = Arrays.asList(rows[efStartIndex + 1].trim().split(","));
						int indexMu = efDataIndex.indexOf(Constants.OPTOUT_OPTMU);
						int indexCVar = efDataIndex.indexOf(Constants.OPTOUT_OPTDAILYCVAR);
						int indexVol = efDataIndex.indexOf(Constants.OPTOUT_OPTVOL);
						
						for(int j = efStartIndex + 2; j < rows.length; j++){
							String efrowData [] = rows[j].trim().split(",");
							if ("EFFICIENT_FRONTIER_OUTPUT_END".equals(efrowData[0])) {
								break;
							}
							List<String> efRowdataList = new ArrayList<String>();
							efRowdataList.add(efrowData[indexMu]);
							efRowdataList.add(efrowData[indexCVar]);
							efRowdataList.add(efrowData[indexVol]);
							efData.add(efRowdataList);
						}
					}
				}

		if(efData != null && ! efData.isEmpty()) {
			optDto.setFrontier(efData);
		}
		optDto.setOutputStats(statsMap);
		optDto.setOutputTimeSeries(outputSeries);
	}
}
