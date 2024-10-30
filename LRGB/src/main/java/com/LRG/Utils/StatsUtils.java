package com.LRG.Utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;
import org.apache.commons.math3.stat.descriptive.moment.Skewness;
import org.apache.commons.math3.util.Precision;

public class StatsUtils {
	
	private static final DecimalFormat DF4 = new DecimalFormat("0.0000");
	public static final double TRADINGDAYS_PER_YEAR = 252.0;
	private static final double INITVAL = 100;
	
	
	public static double getAnnualizedReturn(List<Double> returns) {
		double endValue = INITVAL; 
		for(Double thisRet: returns) {
			endValue = endValue * (Math.exp(thisRet));
		}
		return Math.pow(endValue/INITVAL, TRADINGDAYS_PER_YEAR/returns.size()) - 1.0;
	}
	
	public static double getStandardDeviation(Double[] data) {
		double average = 0;
		double variance = 0;
		for (int i = 0; i < data.length; i++) {
			double sample = data[i];
			average += sample;
			variance += sample * sample;
		}

		average = average / data.length;
		variance = variance / data.length - average * average;
		return Math.sqrt(variance);
	}
	
	public static Double getCaptureRatioUp(List<Double> dailyReturnBMList, List<Double> dailyReturnTickerList) {
    	List<Double> indexreturnValueGE1 = new ArrayList<Double>();
		List<Double> fundValueGE1 = new ArrayList<Double>();
		
    	for(int m = 0; m < dailyReturnBMList.size(); m++) {
			if(dailyReturnBMList.get(m) >= 1) {
				fundValueGE1.add(dailyReturnTickerList.get(m));
				indexreturnValueGE1.add(dailyReturnBMList.get(m));
			}
		}
    	Double fundValueGE1Product = CommonUtils.getProductofList(fundValueGE1);
		Double indexreturnValueGE1Product = CommonUtils.getProductofList(indexreturnValueGE1);
		
		return Precision.round(((Math.pow(fundValueGE1Product, 
				(Double.valueOf(TRADINGDAYS_PER_YEAR)/indexreturnValueGE1.size()))-1)/(Math.pow(indexreturnValueGE1Product, 
						(Double.valueOf(TRADINGDAYS_PER_YEAR)/indexreturnValueGE1.size()))-1)), 4);
    }
    
    public static Double getCaptureRatioDown(List<Double> dailyReturnBMList, List<Double> dailyReturnTickerList) {
		List<Double> indexreturnValueLE1 = new ArrayList<Double>();
		List<Double> fundValueLE1 = new ArrayList<Double>();
		
    	for(int m = 0; m < dailyReturnBMList.size(); m++) {
			if(dailyReturnBMList.get(m) < 1) {
				fundValueLE1.add(dailyReturnTickerList.get(m));
				indexreturnValueLE1.add(dailyReturnBMList.get(m));
			}
		}
		Double fundValueLE1Product = CommonUtils.getProductofList(fundValueLE1);
		Double indexreturnValueLE1Product = CommonUtils.getProductofList(indexreturnValueLE1);
		
		return Precision.round(((Math.pow(fundValueLE1Product, 
				(Double.valueOf(TRADINGDAYS_PER_YEAR)/indexreturnValueLE1.size()))-1)/(Math.pow(indexreturnValueLE1Product, 
						(Double.valueOf(TRADINGDAYS_PER_YEAR)/indexreturnValueLE1.size()))-1)), 3);
    }
    
	public static Double getKurtosis(List<Double> dailyReturnTickerList) {
    	Kurtosis kurt = new Kurtosis();
    	Double[] dailyReturnTickerArr = CommonUtils.getDoubleArray(dailyReturnTickerList);
    	return Precision.round(kurt.evaluate(ArrayUtils.toPrimitive(dailyReturnTickerArr))*100, 1);
    }
    
    public static Double getSkewness(List<Double> dailyReturnTickerList) {
    	Skewness skew = new Skewness();
    	Double[] dailyReturnTickerArr = CommonUtils.getDoubleArray(dailyReturnTickerList);
    	return Precision.round(skew.evaluate(ArrayUtils.toPrimitive(dailyReturnTickerArr))*100, 1);
    }
    
	public static Double getMaxDrawdown(List<Double> tickerPriceList) {    	
    	double maxDD = 0, hwm = 0;
		for(double thisPrice: tickerPriceList) {
			hwm = Math.max(hwm, thisPrice);
			maxDD = Math.max(maxDD, ((hwm - thisPrice)/hwm));
		}
		return maxDD;
    }
	
	public static Double getBiasRatio(List<Double> dailyReturnTickerList) {    	
    	List<Double> fundreturnvalueGT1 = new ArrayList<Double>();
		List<Double> fundreturnvalueLT1 = new ArrayList<Double>();
		List<Double> fundreturnvalueGTsd = new ArrayList<Double>();
		List<Double> fundreturnvalueLTsd = new ArrayList<Double>();  	
    	Double sd = getStandardDeviation(CommonUtils.getDoubleArray(dailyReturnTickerList));
    	
		for(int i = 0; i < dailyReturnTickerList.size(); i++) {
			if(dailyReturnTickerList.get(i) > 1) {
				fundreturnvalueGT1.add(dailyReturnTickerList.get(i));
			} else {
				fundreturnvalueLT1.add(dailyReturnTickerList.get(i));
			}
			if(dailyReturnTickerList.get(i) > (1+sd)) {
				fundreturnvalueGTsd.add(dailyReturnTickerList.get(i));
			} else {
				fundreturnvalueLTsd.add(dailyReturnTickerList.get(i));
			}
		}
		Double fundreturnvalueGT1Product = CommonUtils.getProductofList(fundreturnvalueGT1);
		Double fundreturnvalueLT1Product = CommonUtils.getProductofList(fundreturnvalueLT1);
		Double fundreturnvalueGTsdProduct = CommonUtils.getProductofList(fundreturnvalueGTsd);
		Double fundreturnvalueLTsdProduct = CommonUtils.getProductofList(fundreturnvalueLTsd);
    	return ((fundreturnvalueGT1Product/fundreturnvalueGTsdProduct) / (1+(fundreturnvalueLT1Product)/fundreturnvalueLTsdProduct));
    }
    
    public static Double getCalmerRatio(List<Double> tickerPriceList, Double tickerReturn) {    	
    	Double tickerPriceAvg = CommonUtils.average(CommonUtils.getDoubleArray(tickerPriceList));
    	return (tickerReturn/((Collections.max(tickerPriceList)-Collections.min(tickerPriceList))/tickerPriceAvg))/100;
    }
    
	public static Double getUpMarketReturn(List<Double> dailyReturnBMList, List<Double> dailyReturnTickerList) {
    	List<Double> indexreturnValueGE1 = new ArrayList<Double>();
		List<Double> fundValueGE1 = new ArrayList<Double>();
		
		for(int m = 0; m < dailyReturnBMList.size(); m++) {
			if(dailyReturnBMList.get(m) >= 1) {
				//if(dailyReturnTickerList.get(m)!=0) {
					fundValueGE1.add(dailyReturnTickerList.get(m));
					indexreturnValueGE1.add(dailyReturnBMList.get(m));
				//}
			}
		}
		Double fundValueGE1Product = CommonUtils.getProductofList(fundValueGE1);
    	return (Math.pow(fundValueGE1Product, (Double.valueOf(TRADINGDAYS_PER_YEAR))/(indexreturnValueGE1.size()))-1)*100;
    }
    
    public static Double getDownMarketReturn(List<Double> dailyReturnBMList, List<Double> dailyReturnTickerList) {
    	List<Double> indexreturnValueLE1 = new ArrayList<Double>();
		List<Double> fundValueLE1 = new ArrayList<Double>();
		
		for(int m = 0; m < dailyReturnBMList.size(); m++) {
			if(dailyReturnBMList.get(m) < 1) {
				//if(dailyReturnTickerList.get(m)!=0) {
					fundValueLE1.add(dailyReturnTickerList.get(m));
					indexreturnValueLE1.add(dailyReturnBMList.get(m));
				//}
			}
		}
		Double fundValueLE1Product = CommonUtils.getProductofList(fundValueLE1);
    	return (Math.pow(fundValueLE1Product, (Double.valueOf(TRADINGDAYS_PER_YEAR))/(indexreturnValueLE1.size()))-1)*100;
    }
    
    public static Map<String, String> getRegressionMap(List<Double> xData, List<Double> yData) {
    	Map<String, String> regMap = new HashMap<String, String>();
		double sumx = 0.0, sumy = 0.0, sumx2 = 0.0, sumxy = 0.0, ssr = 0.0;
		int dataSize = xData.size();
    	try {
    		for (int i = 0; i < dataSize; i++) {
    			sumx += xData.get(i);
    			sumy += yData.get(i);
    			sumx2 += xData.get(i) * xData.get(i);
    			sumxy += xData.get(i) * yData.get(i);
    		}
    		double slope = (dataSize * sumxy - sumx * sumy) / (dataSize * sumx2 - sumx * sumx);
    		double intercept = (sumy - slope * sumx) / dataSize;
    		if (Double.isNaN(slope) || Double.isInfinite(slope) || Double.isNaN(intercept) || Double.isInfinite(intercept)) {
    			return regMap;
    		}
    		regMap.put("slope", DF4.format(slope));
    		regMap.put("intercept", DF4.format(intercept));
    		
    		double ybar = sumy / dataSize;
    		double yybar = 0.0;
    		for (int i = 0; i < dataSize; i++) {
    			yybar += (yData.get(i) - ybar) * (yData.get(i) - ybar);
    		}
    		for (int i = 0; i < dataSize; i++) {
    			double fit = slope * xData.get(i) + intercept;
    			ssr += (fit - ybar) * (fit - ybar);
    		}
    		double rSquare = ssr / yybar;
    		if (Double.isNaN(rSquare) || Double.isInfinite(rSquare)) {
    			return regMap;
    		}
    		regMap.put("rSquare", DF4.format(rSquare));
    		
    	}catch(Exception e) {
    		regMap.put("slope", null);
    		regMap.put("intercept", null);
    		regMap.put("rSquare", null);
    	}
		
		return regMap;
	}
    
    public static Double getTrackingError(List<Double> dailyReturnBMList, List<Double> dailyReturnTickerList) {
    	List<Double> excessReturnList = getExcessReturnList(dailyReturnBMList, dailyReturnTickerList);
    	Double[] excessReturnArr = CommonUtils.getDoubleArray(excessReturnList);
    	double sdExcessReturnList = getStandardDeviation(excessReturnArr);
    	return (sdExcessReturnList * Math.sqrt(TRADINGDAYS_PER_YEAR))*100;
	}
    
    public static List<Double> getExcessReturnList(List<Double> dailyReturnBMList, List<Double> dailyReturnTickerList) {
    	List<Double> excessReturnList = new ArrayList<Double>();
    	for(int m = 0; m < dailyReturnBMList.size(); m++) {
			excessReturnList.add(dailyReturnTickerList.get(m) - dailyReturnBMList.get(m));
		}
    	return excessReturnList;
    }
    
    public static Double getDownsideRisk(List<Double> dailyReturnBMList, List<Double> dailyReturnTickerList, Double riskFreeRate) {
    	List<Double> excessReturnList = getExcessReturnList(dailyReturnBMList, dailyReturnTickerList);
    	List<Double> downsideRiskList = new ArrayList<Double>();
    	for(int n = 0; n < excessReturnList.size(); n++) {
			if(excessReturnList.get(n) > Math.pow(Double.valueOf(riskFreeRate), (1.0/Double.valueOf(TRADINGDAYS_PER_YEAR)))) {
				downsideRiskList.add(0.0);
			} else {
				downsideRiskList.add(excessReturnList.get(n));
			}
		}
    	Double[] downsideRiskArr = CommonUtils.getDoubleArray(downsideRiskList);
    	double sdDownsideRiskList = StatsUtils.getStandardDeviation(downsideRiskArr);
    	return (sdDownsideRiskList * Math.sqrt(TRADINGDAYS_PER_YEAR))*100;
    }
    
    public static double getCorrelation(List<Double> data1, List<Double> data2) {
		double sum_sq_x = 0;
		double sum_sq_y = 0;
		double sum_coproduct = 0;
		double mean_x = data1.get(0);
		double mean_y = data2.get(0);
		for (int i = 2; i < data1.size() + 1 && i <= data2.size(); i += 1) {
			double sweep = Double.valueOf(i - 1) / i;
			double delta_x = data1.get(i - 1) - mean_x;
			double delta_y = data2.get(i - 1) - mean_y;
			sum_sq_x += delta_x * delta_x * sweep;
			sum_sq_y += delta_y * delta_y * sweep;
			sum_coproduct += delta_x * delta_y * sweep;
			mean_x += delta_x / i;
			mean_y += delta_y / i;
		}
		double pop_sd_x = (double) Math.sqrt(sum_sq_x / data1.size());
		double pop_sd_y = (double) Math.sqrt(sum_sq_y / data1.size());
		double cov_x_y = sum_coproduct / data1.size();
		if(pop_sd_x != 0 && pop_sd_y != 0) {
			return cov_x_y / (pop_sd_x * pop_sd_y);
		} else {
			return 0;
		}
	}
    
    public static List<Double> getPersistencyList (int age, String gender) {
		Map<Integer,Double[]> shortFallMasterDataList = Constants.populateMasterDataMap();
		List<Double> mortalityRateList = new ArrayList<Double>();
		List<Double> persistencyList = new ArrayList<Double>();
		
		for(int i = 0; i < (Constants.SHORTFALL_MAX_LIST_SIZE + age); i ++) {
			Double mortalityrate = 0.0;
			if(i >= age-1) {
				if(i < shortFallMasterDataList.size()) {
					mortalityrate = gender.equalsIgnoreCase("MALE") ? shortFallMasterDataList.get(i)[0] : shortFallMasterDataList.get(i)[1];
				} else {
					mortalityrate = 1.0;
				}
				mortalityRateList.add(mortalityrate);
				if(persistencyList.size() == 0) {
					persistencyList.add(1.0);
				} else {
					Double persistency = persistencyList.get(persistencyList.size()-1).doubleValue() * 
							(1.0 - mortalityRateList.get(mortalityRateList.size()-1).doubleValue());
					persistencyList.add(persistency);
				}
			}
		}
		return persistencyList;
    }
    
    public static double mean(Double[] dataArray) {
	    double sum = 0;  
	    for (int i = 0; i< dataArray.length; i++) {
	        sum += dataArray[i];
	    }
	    return dataArray.length == 0 ? 0.0000 : sum / dataArray.length;
	}
    
    public static Double NORMINV(Double probability, Double mean, Double sd) {
    	Double inverseNormalDistr = 0.0;
    	//NormalDistribution distribution = new NormalDistribution(mean, sd);
    	//inverseNormalDistr = distribution.inverse(probability);
    	
    	NormalDistribution distribution = new NormalDistribution(mean, sd);
    	inverseNormalDistr = distribution.inverseCumulativeProbability(probability);
    	
    	return inverseNormalDistr;
    }
    
    public static List<List<Double>> getListOfDoubleList(int outerListSize, int innerListSize) {
		List<List<Double>> outputList = new ArrayList<List<Double>>();
		for(int i=0; i<outerListSize; i++) {
			List<Double> innerList = new ArrayList<Double>();
			for(int j=0; j<innerListSize; j++) {
				innerList.add(0.0);
			}
			outputList.add(innerList);
		}
		return outputList;
	}	
    
    public static Double getSumOfList(List<Double> list) {
		Double sum = 1.0;
		for(int i = 0; i < list.size(); i++) {
			sum += list.get(i);
		}
		return sum;
	}
    
    public static BigDecimal annualToDailyReturn(BigDecimal annualReturn, String format) {
		if(annualReturn == null) {
			return new BigDecimal(0);
		}
		DecimalFormat decimalFormat = new DecimalFormat(format);
		return new BigDecimal(decimalFormat.format((Math.pow(annualReturn.doubleValue() + 1, 0.0039682540)) - 1.0));
	}
    
    public static List<Double> getReturnFromPrice(List<Double> priceSeries) {
    	List<Double> returnSeries = new ArrayList<Double>();
    	double oldPf = priceSeries.get(0);
		for(double thisPf: priceSeries) {
			Double thisRet = (thisPf!=0 && oldPf!=0) ? Math.log(thisPf / oldPf) : 0.0;
			returnSeries.add(thisRet.isNaN() ? 0.0 : thisRet);
			oldPf = thisPf;
		}
    	return returnSeries;
    }
}
