package com.LRG.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.LRG.Utils.BusinessException;
import com.LRG.Utils.Function;
import com.LRG.Utils.TechnicalUtil;
import com.LRG.model.ResearchElement;

import org.hibernate.HibernateException;
import java.sql.SQLException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;



public class TechnicalService extends HibernateDaoSupport {
	
	
	public Map<String,List<ResearchElement>> getTechnicalData(String ticker, Integer companyId) {
		ticker=ticker.split(",")[0];
		Map<String, List<ResearchElement>> techData = new LinkedHashMap<String, List<ResearchElement>>();
		techData.put("Support Resistance", new ArrayList<ResearchElement>());
		techData.get("Support Resistance").add(new ResearchElement("50 Day SMA", null, null));
		techData.get("Support Resistance").add(new ResearchElement("100 Day SMA", null, null));
		techData.get("Support Resistance").add(new ResearchElement("200 Day SMA", null, null));
		techData.get("Support Resistance").add(new ResearchElement("Pivot Point 1st Level Resistance", null, null));
		techData.get("Support Resistance").add(new ResearchElement("Pivot Point 2nd Level Resistance", null, null));
		techData.get("Support Resistance").add(new ResearchElement("Pivot Point 1st Level Support", null, null));
		techData.get("Support Resistance").add(new ResearchElement("Pivot Point 2nd Level Support", null, null));
		techData.put("Technical Summary", new ArrayList<ResearchElement>());
		techData.get("Technical Summary").add(new ResearchElement("RSI", null, null));
		techData.get("Technical Summary").add(new ResearchElement("Relative Strength Index (14)", null, null));
		techData.get("Technical Summary").add(new ResearchElement("DMI", null, null));
		techData.get("Technical Summary").add(new ResearchElement("Average Directional Index (14)", null, null));
		techData.get("Technical Summary").add(new ResearchElement("Directional Movement Index (14)(+)", null, null));
		techData.get("Technical Summary").add(new ResearchElement("Directional Movement Index (14)(-)", null, null));
		techData.get("Technical Summary").add(new ResearchElement("MACD", null, null));
		techData.get("Technical Summary").add(new ResearchElement("MACD (12,26)", null, null));
		techData.get("Technical Summary").add(new ResearchElement("MACD Signal (9)", null, null));
		techData.get("Technical Summary").add(new ResearchElement("MACD Difference", null, null));
		techData.get("Technical Summary").add(new ResearchElement("OBV", null, null));
		techData.get("Technical Summary").add(new ResearchElement("On Balance Volume", null, null));
		
		String sql = "SELECT a.PRICE_CLOSE, a.day_high, a.day_low, a.volume FROM kkrdb.kkr_price a where a.KKR_company_ID = "+companyId+" order by a.PRICE_DATE";
		List<Object[]> valuelist  = (List<Object[]>) findNative(sql);
		if(valuelist == null || valuelist.isEmpty()) {
			throw new BusinessException("DATA_UNAVAILABLE", "No Returns data available for ticker: " + ticker);
		}
		double[] priceList = new double[valuelist.size()];
		double maxValue[] = new double[valuelist.size()];
		double minValue[] = new double[valuelist.size()];
		double[] volumeList = new double[valuelist.size()];
		for(int i = 0; i < valuelist.size(); i++) { 
			priceList[i] = Double.valueOf(valuelist.get(i)[0].toString());
			maxValue[i] = Double.valueOf(valuelist.get(i)[1].toString());
			minValue[i] = Double.valueOf(valuelist.get(i)[2].toString());
			volumeList[i] = Double.valueOf(valuelist.get(i)[3].toString());
		}
		int len = priceList.length;
		BigDecimal lastPrice = new BigDecimal(priceList[len-1]);
		
		List<ResearchElement> srList = techData.get("Support Resistance");
		if(len > 50) {
			double[] smaSeries = TechnicalUtil.getSMAOrRSI(Function.SMA, Arrays.copyOfRange(priceList, len-51, len), 50);
			if(smaSeries != null) {
				double sma50 = smaSeries[smaSeries.length-1];
				srList.set(0, new ResearchElement("50 Day SMA", (new BigDecimal(sma50).setScale(2,BigDecimal.ROUND_HALF_UP)).toString(), new BigDecimal(sma50).compareTo(lastPrice)+""));
			}
		}
		if(len > 100) {
			double[] smaSeries = TechnicalUtil.getSMAOrRSI(Function.SMA, Arrays.copyOfRange(priceList, len-101, len), 100);
			if(smaSeries != null) {
				double sma100 = smaSeries[smaSeries.length-1];
				srList.set(1, new ResearchElement("100 Day SMA", (new BigDecimal(sma100).setScale(2,BigDecimal.ROUND_HALF_UP)).toString(), new BigDecimal(sma100).compareTo(lastPrice)+""));
			}
		}
		if(len > 200) {
			double[] smaSeries = TechnicalUtil.getSMAOrRSI(Function.SMA, Arrays.copyOfRange(priceList, len-201, len), 200);
			if(smaSeries != null) {
				double sma200 = smaSeries[smaSeries.length-1];
				srList.set(2, new ResearchElement("200 Day SMA", (new BigDecimal(sma200).setScale(2,BigDecimal.ROUND_HALF_UP)).toString(), new BigDecimal(sma200).compareTo(lastPrice)+""));
			}
		}
		
		try{
			sql = "SELECT GetPivot('" + ticker +"','S1',a.PRICE_DATE) as firstLevelS,GetPivot('"+ ticker +"','R1',a.PRICE_DATE) as firstLevelR,GetPivot('" + ticker +"','S2',a.PRICE_DATE) as secondLevelS,GetPivot('" +ticker+"','R2',a.PRICE_DATE) as secondLevelR FROM kkrdb.kkr_price a where a.KKR_company_ID = "+companyId+" order by a.PRICE_DATE desc limit 1";
			BigDecimal firstLevelS = new BigDecimal(0);
			BigDecimal firstLevelR = new BigDecimal(0);
			BigDecimal secondLevelS = new BigDecimal(0);
			BigDecimal secondLevelR = new BigDecimal(0);
			List<Object[]> valueObjlist  = (List<Object[]>) findNative(sql);
			if(valueObjlist != null && !valueObjlist.isEmpty()){
				Object[] tempvalues = valueObjlist.get(0);
				if(tempvalues[0] != null) {
					firstLevelS = new BigDecimal((Double)tempvalues[0]);
				}
				if(tempvalues[1] != null) {
					firstLevelR = new BigDecimal((Double)tempvalues[1]);
				}
				if(tempvalues[2] != null) {
					secondLevelS = new BigDecimal((Double)tempvalues[2]);
				}
				if(tempvalues[3] != null) {
					secondLevelR = new BigDecimal((Double)tempvalues[3]);
				}
			}
			srList.set(3, new ResearchElement("Pivot Point 1st Level Resistance",firstLevelR.setScale(2,BigDecimal.ROUND_HALF_UP).toString(),firstLevelR.compareTo(lastPrice)+""));
			srList.set(4, new ResearchElement("Pivot Point 2nd Level Resistance",secondLevelR.setScale(2,BigDecimal.ROUND_HALF_UP).toString(),secondLevelR.compareTo(lastPrice)+""));
			srList.set(5, new ResearchElement("Pivot Point 1st Level Support",firstLevelS.setScale(2,BigDecimal.ROUND_HALF_UP).toString(),firstLevelS.compareTo(lastPrice)+""));
			srList.set(6, new ResearchElement("Pivot Point 2nd Level Support",secondLevelS.setScale(2,BigDecimal.ROUND_HALF_UP).toString(),secondLevelS.compareTo(lastPrice)+""));
		} catch(Exception ex) {
			//logger.error("Error while fetching Pivot data for ticker:"+ticker, ex);
			srList.set(3, new ResearchElement("Pivot Point 1st Level Resistance", "NA"));
			srList.set(4, new ResearchElement("Pivot Point 2nd Level Resistance", "NA"));
			srList.set(5, new ResearchElement("Pivot Point 1st Level Support", "NA"));
			srList.set(6, new ResearchElement("Pivot Point 2nd Level Support", "NA"));
		}
		
		List<ResearchElement> tsList = techData.get("Technical Summary");
		if(len > 250) {
			double[] rsiSeries = TechnicalUtil.getSMAOrRSI(Function.RSI, Arrays.copyOfRange(priceList, len-251, len), 14);
			BigDecimal rsi = new BigDecimal(rsiSeries[rsiSeries.length-1]).setScale(2,BigDecimal.ROUND_HALF_UP);
			String rsiStatus = "";
			if(rsi.compareTo(new BigDecimal(30))<=0) rsiStatus = "1";
			else if(rsi.compareTo(new BigDecimal(30.01))>0 && (rsi.compareTo(new BigDecimal(70))<0)) rsiStatus = "0";
			else if(rsi.compareTo(new BigDecimal(70.01))>0) rsiStatus = "-1";
			tsList.set(0, new ResearchElement("RSI", null, rsiStatus));
			tsList.set(1,new ResearchElement("Relative Strength Index (14)", rsi.toString(), ""));
			
			double[] avrDirIndxSeries = TechnicalUtil.getAvrDirIndx(priceList, 14, minValue, maxValue);
			if(avrDirIndxSeries.length > 0) {
				double avrDirIndx = avrDirIndxSeries[avrDirIndxSeries.length-1];
				tsList.set(3, new ResearchElement("Average Directional Index (14)",(new BigDecimal(avrDirIndx)).setScale(2,BigDecimal.ROUND_HALF_UP).toString(),""));
			}
			double dmiPlus = 0.0;
			double[] dmiPlusSeries = TechnicalUtil.getDMIPlus(priceList, 14, minValue, maxValue);
			if(dmiPlusSeries.length > 0) {
				dmiPlus = dmiPlusSeries[dmiPlusSeries.length-1];
				tsList.set(4, new ResearchElement("Directional Movement Index (14)(+)",(new BigDecimal(dmiPlus)).setScale(2,BigDecimal.ROUND_HALF_UP).toString(),""));
			}
			double dmiMinus = 0.0;
			double[] dmiMinusSeries = TechnicalUtil.getDMIMinus(priceList, 14, minValue, maxValue);
			if(dmiMinusSeries.length > 0) {
				dmiMinus = dmiMinusSeries[dmiMinusSeries.length-1];
				tsList.set(5, new ResearchElement("Directional Movement Index (14)(-)",(new BigDecimal(dmiMinus)).setScale(2,BigDecimal.ROUND_HALF_UP).toString(),""));
			}
			double[] dmiSeries = TechnicalUtil.getDMI(priceList, 14, minValue, maxValue);
			if(dmiSeries.length > 0) {
				double dmi = dmiSeries[dmiSeries.length-1];
				String dmiStatus = "";
				if((new BigDecimal(dmi)).compareTo(new BigDecimal(20))>0 && (new BigDecimal(dmiPlus)).compareTo((new BigDecimal(dmiMinus)))>0) {
						dmiStatus = "1";
				} else if((new BigDecimal(dmi)).compareTo(new BigDecimal(20))>0 && (new BigDecimal(dmiPlus)).compareTo((new BigDecimal(dmiMinus)))<0) {
					dmiStatus = "-1";
				} else {
					dmiStatus = "0";
				}
				tsList.set(2, new ResearchElement("DMI",null,dmiStatus));
			}
			double[] obvSeries = TechnicalUtil.getOBV(Arrays.copyOfRange(priceList, len-251, len), Arrays.copyOfRange(volumeList, len-251, len));
			if(obvSeries.length > 0) {
				BigDecimal obv = new BigDecimal(obvSeries[obvSeries.length-1]).setScale(2,BigDecimal.ROUND_HALF_UP);
				String obvStatus = "";
				double tickerTrend = priceList[len-1] - priceList[len-6];
				double obvTrend = obvSeries[obvSeries.length-1] - obvSeries[obvSeries.length-6];
				if(tickerTrend > 0 && obvTrend > 0) obvStatus = "1";
				else if(tickerTrend < 0 && obvTrend > 0) obvStatus = "-1";
				else obvStatus = "0";
				tsList.set(10, new ResearchElement("OBV", null, obvStatus));
				tsList.set(11,new ResearchElement("On Balance Volume", obv.toString(), ""));
			}
//			for(double thisObv: obvSeries) {
//				logger.error(">>>>>>>>>>>>>>>>>>>>\t\t" + ticker + "\t\t" + thisObv);
//			}
		}
		
		Object[] macdSeries = TechnicalUtil.getMACDThreeSeries(priceList);
		double[] macd1226Array = ((double[])(macdSeries[0]));
		if(macd1226Array.length > 0) {
			double macd1226 = macd1226Array[macd1226Array.length-1];
			tsList.set(7, new ResearchElement("MACD (12,26)", (new BigDecimal(macd1226)).setScale(2,BigDecimal.ROUND_HALF_UP).toString(), ""));
		}
		double macdSig9 = 0.0;
		double[] macdSig9Array = ((double[])(macdSeries[1]));
		if(macdSig9Array.length > 0) {
			macdSig9 = macdSig9Array[macdSig9Array.length-1];
			tsList.set(8, new ResearchElement("MACD Signal (9)", (new BigDecimal(macdSig9)).setScale(2,BigDecimal.ROUND_HALF_UP).toString(), ""));
		}
		double[] macdDifArray = ((double[])(macdSeries[2]));
		if(macdDifArray.length > 0) {
			double macdDif = macdDifArray[macdDifArray.length-1];
			tsList.set(9, new ResearchElement("MACD Difference", (new BigDecimal(macdDif)).setScale(2,BigDecimal.ROUND_HALF_UP).toString(), ""));
		}
		double[] macd = TechnicalUtil.getMACDorBBAND(Function.MACD, priceList);
		if(macd.length > 0) {
			double macdS = macd[macd.length-1];
			String macdStatus = "" + (macdS>0 ? 1 : (macdS<0 ? -1 : 0));
			
		if(macdS > 0 && macdS > macdSig9) {
			macdStatus = "1";
		} else if(macdS < 0 && macdS < macdSig9) {
			macdStatus = "-1";
		} else {
			macdStatus = "0";
		}
		tsList.set(6, new ResearchElement("MACD", null, macdStatus));
		}
		return techData;
	}
	
	public Object findNative(final String sqlQuery) {
		return getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Query query = session.createSQLQuery(sqlQuery);
				return query.list();
			}
		});
	}

}
