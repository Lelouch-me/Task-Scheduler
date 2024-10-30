package com.LRG.Utils;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class TechnicalUtil {
	
	private static Core core = new Core();
	private static Log logger = LogFactory.getLog(TechnicalUtil.class);
	
	//For MACD we are defaulting to the following
	private static int MACD_FAST_PERIOD = 12;
	private static int MACD_SLOW_PERIOD = 26;
	private static int MACD_SIGNAL_PERIOD = 9;
	
	public TechnicalUtil() {
		core = new Core();
	}
	
	public static double [] getSMAOrRSI(Function function, double [] prices, int movingAvgDays) {
		MInteger outBegIdx = new MInteger();
		MInteger outNbElement = new MInteger();
		RetCode retCode = null;
		double [] taOutput = new double[prices.length];
		if (function != null && function.equals(Function.SMA)) {
			retCode = getSMAorRSI(Function.SMA, prices , movingAvgDays, outBegIdx, outNbElement, taOutput);
		} else {
			retCode = getSMAorRSI(Function.RSI, prices , movingAvgDays, outBegIdx, outNbElement, taOutput);
		}
		if (retCode == null || !retCode.equals(RetCode.Success)) {
	    	logger.error("Error while calculating SMA/RSI data");
	    	return null;
		}
		return getReturnData(prices, outBegIdx, outNbElement, taOutput);
	}
	
	public static double [] getOBV(double [] prices, double [] volume) {
		MInteger outBegIdx = new MInteger();
		MInteger outNbElement = new MInteger();
		double [] taOutput = new double[prices.length];
		RetCode retCode = getOBV(prices, volume, outBegIdx, outNbElement, taOutput);
		if (retCode == null || !retCode.equals(RetCode.Success)) {
	    	logger.error("Error while calculating OBV data");
	    	return null;
		}
		return getReturnData(prices, outBegIdx, outNbElement, taOutput);
	}

	public static double[] getMACDorBBAND(Function function,double [] pricesForPriceType) {
		MInteger outBegIdx = new MInteger();
		MInteger outNbElement = new MInteger();
		RetCode retCode = null;
		double [] taOutput = new double[pricesForPriceType.length];
		if (function != null && function.equals(Function.MACD)) {
			retCode = getMACD(pricesForPriceType,MACD_FAST_PERIOD,MACD_SLOW_PERIOD,MACD_SIGNAL_PERIOD, outBegIdx, outNbElement, taOutput);
		}
		if (retCode == null || !retCode.equals(RetCode.Success)) {
	    	logger.error("Error while calculating MACD data");
	    	return null;
		}
		return getReturnData(pricesForPriceType, outBegIdx, outNbElement, taOutput);
	}
	
	public static Object[] getMACDThreeSeries(double [] pricesForPriceType) {
		MInteger outBegIdx = new MInteger();
		MInteger outNbElement = new MInteger();
		RetCode retCode = null;
		double [] taOutput = new double[pricesForPriceType.length];
		double signal[] = new double[pricesForPriceType.length];
    	double hist[]   = new double[pricesForPriceType.length];
    	retCode = core.macd(0,pricesForPriceType.length-1,pricesForPriceType,MACD_FAST_PERIOD,MACD_SLOW_PERIOD,MACD_SIGNAL_PERIOD,outBegIdx,outNbElement,taOutput,signal,hist);
		if (retCode == null || !retCode.equals(RetCode.Success)) {
	    	logger.error("Error while calculating MACD data");
	    	return null;
		}
    	double [] returnData_0 = getReturnData(pricesForPriceType, outBegIdx, outNbElement, taOutput);
    	double [] returnData_1 = getReturnData(pricesForPriceType, outBegIdx, outNbElement, signal);
    	double [] returnData_2 = getReturnData(pricesForPriceType, outBegIdx, outNbElement, hist);
		return new Object[]{returnData_0, returnData_1, returnData_2};
	}

	public static double[] getDMI(double [] pricesForPriceType, int optInTimePeriod, double [] inLow, double [] inHigh) {
		MInteger outBegIdx = new MInteger();
		MInteger outNbElement = new MInteger();
		double [] outReal = new double[pricesForPriceType.length];
		RetCode retCode = null;
		retCode = core.dx(0, pricesForPriceType.length-1, inLow, inHigh, pricesForPriceType, optInTimePeriod, outBegIdx, outNbElement, outReal);
		if (retCode == null || !retCode.equals(RetCode.Success)) {
	    	logger.error("Error while calculating DMI data");
	    	return null;
		}
		return getReturnData(pricesForPriceType, outBegIdx, outNbElement, outReal);
	}

	public static double[] getAvrDirIndx(double [] pricesForPriceType, int optInTimePeriod, double [] inLow, double [] inHigh) {
		MInteger outBegIdx = new MInteger();
		MInteger outNbElement = new MInteger();
		double [] outReal = new double[pricesForPriceType.length];
		RetCode retCode = null;
		retCode = core.adx(0, pricesForPriceType.length-1, inLow, inHigh, pricesForPriceType, optInTimePeriod, outBegIdx, outNbElement, outReal);
		if (retCode == null || !retCode.equals(RetCode.Success)) {
	    	logger.error("Error while calculating DMI average data");
	    	return null;
		}
		return getReturnData(pricesForPriceType, outBegIdx, outNbElement, outReal);
	}

	public static double[] getDMIPlus(double [] pricesForPriceType, int optInTimePeriod, double [] inLow, double [] inHigh) {
		MInteger outBegIdx = new MInteger();
		MInteger outNbElement = new MInteger();
		double [] outReal = new double[pricesForPriceType.length];
		RetCode retCode = null;
		retCode = core.plusDI(0, pricesForPriceType.length-1, inLow, inHigh, pricesForPriceType, optInTimePeriod, outBegIdx, outNbElement, outReal);
		if (retCode == null || !retCode.equals(RetCode.Success)) {
	    	logger.error("Error while calculating DMI plus data");
	    	return null;
		}
		return getReturnData(pricesForPriceType, outBegIdx, outNbElement, outReal);
	}

	public static double[] getDMIMinus(double [] pricesForPriceType, int optInTimePeriod, double [] inLow, double [] inHigh) {
		MInteger outBegIdx = new MInteger();
		MInteger outNbElement = new MInteger();
		double [] outReal = new double[pricesForPriceType.length];
		RetCode retCode = null;
		retCode = core.minusDI(0, pricesForPriceType.length-1, inLow, inHigh, pricesForPriceType, optInTimePeriod, outBegIdx, outNbElement, outReal);
		if (retCode == null || !retCode.equals(RetCode.Success)) {
	    	logger.error("Error while calculating DMI minus data");
	    	return null;
		}
		return getReturnData(pricesForPriceType, outBegIdx, outNbElement, outReal);
	}
	
	private synchronized static RetCode getSMAorRSI(Function function, double pricesForPriceType [], int movingAvgDays, MInteger outBegIdx, MInteger outNbElement, double taOutput []) {
		RetCode retCode = null;
		if (function != null) {
			if (function.equals(Function.SMA)) {
				retCode = core.sma(0, pricesForPriceType.length-1, pricesForPriceType, movingAvgDays, outBegIdx, outNbElement, taOutput);
			} else if (function.equals(Function.RSI)) {
				retCode = core.rsi(0, pricesForPriceType.length-1, pricesForPriceType, movingAvgDays, outBegIdx, outNbElement, taOutput);
			} 
		}
		return retCode;
	}
	
	private synchronized static RetCode getOBV(double pricesForPriceType [], double volume [], MInteger outBegIdx, MInteger outNbElement, double taOutput []) {
		RetCode retCode = core.obv(0, pricesForPriceType.length-1, pricesForPriceType, volume, outBegIdx, outNbElement, taOutput);
		return retCode;
	}
	
	private static double [] getReturnData(double pricesPriceType[], MInteger outBegIdx, MInteger outNBElement, double output []) {
		double [] returnData = new double[pricesPriceType.length];
		Arrays.fill(returnData, Constants.PREFILL);
        for (int i = outBegIdx.value; i < pricesPriceType.length; i++) {
        	returnData [i] = output[i-outBegIdx.value];
        }
		return returnData;
	}
	
	private static RetCode getMACD(double [] pricesForPriceType, int fastPeriod, int slowPeriod, int signalPeriod, MInteger outBegIdx, MInteger outNbElement, double taOutput []) {     
		double signal[] = new double[pricesForPriceType.length];
    	double hist[]   = new double[pricesForPriceType.length];
    	return core.macd(0,pricesForPriceType.length-1,pricesForPriceType,fastPeriod,slowPeriod,signalPeriod,outBegIdx,outNbElement,taOutput,signal,hist);
    }
 }