package com.LRG.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.LRG.Utils.CommonUtils;
import com.LRG.Utils.Constants;
import com.LRG.domain.MarketStat;
import com.LRG.model.MarketStatDto;
import com.LRG.repository.DailyPriceVolumeRepository;
import com.LRG.repository.MarketStatRepository;

@Service
public class MarketStatService {
	
	@Autowired
	private DailyPriceVolumeRepository dailyPriceVolumeRepository;
	
	@Autowired
	private EpsDataService epsDataService;
	
	private MarketStatRepository marketStatRepository;
	
	private DecimalFormat df = new DecimalFormat("#.##");
	private DecimalFormat df1 = new DecimalFormat("#.#"); 
	
	public MarketStatService(MarketStatRepository marketStatRepository) {
		super();
		this.marketStatRepository = marketStatRepository;
	}
	
	public List<MarketStatDto> getAllIntradayData() throws ClassNotFoundException, SQLException{
		List<MarketStatDto> marketDataList = new ArrayList<MarketStatDto>();
		List<MarketStat> marketData = marketStatRepository.findEquityAndMF();
		if(marketData != null && !marketData.isEmpty()) {
			for(MarketStat marketStat : marketData) {
				MarketStatDto marketStatDto = convertDomainToDto(marketStat);
				if(marketStatDto!=null) marketDataList.add(marketStatDto);				
			}
		}
		return marketDataList;	
	}
	
	private MarketStatDto convertDomainToDto(MarketStat marketStat) throws ClassNotFoundException, SQLException {
		Double change = marketStat.getLtp() - marketStat.getYcp();
		if(marketStat.getYcp()!=0) {
//			String ticker=marketStat.getCode();
//			System.out.println(ticker);
//			if(ticker.contains("ACTIVEFINE")) {
//				System.out.println("Stop");
//			}
			Double changePercent = (change / marketStat.getYcp()) * 100;
			String sector = null;
			Double de = null;
			Double totalDebt = null;
			Double pe = null;
			Double pb = null;
			Double ps = null;
			Double epsYoy = null;
			Double epsCagr = null;
			String reportDate = getTickerEpsReportDate(marketStat.getCode());
			
			Double eps = marketStat.getCompany()!=null ? marketStat.getCompany().getEps() : null;
			eps = eps!=null ? Double.valueOf(df.format(eps)) : null;
			Double outShares = marketStat.getDailyCompanyInfo()!=null ? marketStat.getDailyCompanyInfo().getOutShares() : null;
			Double equitiesPerShare = null;
			Double salesPerShare = null;
			boolean isDse30 = marketStat.getDailyCompanyInfo()!=null ? marketStat.getDailyCompanyInfo().getDse30()!=null ? 
					marketStat.getDailyCompanyInfo().getDse30().equals("Y") : false : false;
			
			Double totExposOrRiskWeightedAsset = null;			
			Double tier1Capital = null;
			
			if(marketStat.getCompany()!=null) {
				if(eps!=null) {
					if(marketStat.getLtp()!=0) {
						pe = marketStat.getLtp() / eps;
					}else {
						pe = marketStat.getYcp() / eps;
					}				
					pe = Double.valueOf(df.format(pe));
				}
				
				equitiesPerShare = marketStat.getCompany().getEquitiesPerShare();
				if(equitiesPerShare!=null) {
					if(marketStat.getLtp()!=0) {
						pb = marketStat.getLtp() / equitiesPerShare;
					}else {
						pb = marketStat.getYcp() / equitiesPerShare;
					}				
					pb = Double.valueOf(df.format(pb));
//					if(marketStat.getCompany().getTicker().contains("JUTESPINN") || 
//					   marketStat.getCompany().getTicker().contains("NORTHERN") || 
//					   marketStat.getCompany().getTicker().contains("SONALIANSH")) {
//						System.out.println(marketStat.getCompany().getTicker()+"   "+pb);
//					}
					
					//calculate DE
					if(marketStat.getCompany().getTotalDebt()!=null) {
						//for other sector
						de = outShares!=null ? marketStat.getCompany().getTotalDebt() / (equitiesPerShare*outShares) : null;
					}
				}
				
				salesPerShare = marketStat.getCompany().getSalesPerShare();
				if(salesPerShare!=null && salesPerShare!=0.0) {
					if(marketStat.getLtp()!=0) {
						ps = marketStat.getLtp() / salesPerShare;
					}else {
						ps = marketStat.getYcp() / salesPerShare;
					}				
					ps = Double.valueOf(df.format(ps));
				}
				sector = marketStat.getCompany().getSector();
				totalDebt = marketStat.getCompany().getTotalDebt();
				
				totExposOrRiskWeightedAsset = marketStat.getCompany().getTotExposOrRiskWeightedAsset()!=null ? 
						marketStat.getCompany().getTotExposOrRiskWeightedAsset() : null;			
				tier1Capital = marketStat.getCompany().getTier1Capital()!= null ? marketStat.getCompany().getTier1Capital() : null;
				
				if(tier1Capital!=null && totExposOrRiskWeightedAsset!=null) {
					//for Bank,NBFI
					de =  (tier1Capital/totExposOrRiskWeightedAsset)*100;
				}
				de = de!=null ? Double.valueOf(df.format(de)) : null;
			}
			
			List<Double> epsData = epsDataService.getEpsYoyDataByTicker(marketStat.getCode());
			epsYoy = epsData.get(0);
			epsYoy = epsYoy!=null ? Double.valueOf(df.format(epsYoy)) : null;
			
			epsCagr = epsData.get(1);
			epsCagr = epsCagr!=null ? Double.valueOf(df.format(epsCagr)) : null;
			
			Double auditedPe = marketStat.getDailyCompanyInfo()!=null ? marketStat.getDailyCompanyInfo().getAuditedPe() : null;
			if(auditedPe!=null) auditedPe = Double.valueOf(df.format(auditedPe));
			
			Double disPrem = null;	
			Double issuePrice = marketStat.getDailyCompanyInfo()!=null ? marketStat.getDailyCompanyInfo().getIssuePrice() : null;
			if(issuePrice!=null) {
				if(marketStat.getLtp()!=0.0) {
					disPrem = issuePrice!=0.0 ? ((marketStat.getLtp() / issuePrice) - 1) : null;
				}else {
					disPrem = issuePrice!=0.0 ? ((marketStat.getYcp() / issuePrice) - 1) : null;
				}
			}
			disPrem = disPrem!=null ? Double.valueOf(df.format(disPrem * 100)) : null;
			if(Constants.getBondList().contains(marketStat.getCode()) && marketStat.getLtp()!=null && marketStat.getLtp()==0 && 
					marketStat.getClose()!=null && marketStat.getYcp()!=null) {
				change = marketStat.getTotalTrades()!=0 ? marketStat.getClose() - marketStat.getYcp() : null;
			}
			change = change!=null ? Double.valueOf(df.format(change)) : null;
				
			
			Double dvdYield = null;
			if(marketStat.getDailyCompanyInfo()!=null) {
				dvdYield = marketStat.getDailyCompanyInfo().getMarketData()!= null ? 
						marketStat.getDailyCompanyInfo().getMarketData().getDvdYield() : null;
			}
			dvdYield = dvdYield != null ? Double.valueOf(df.format(dvdYield*100)) : null;
			
			return new MarketStatDto(marketStat.getCode(),marketStat.getLtp(),marketStat.getHigh(),marketStat.getLow(),marketStat.getClose(),
					marketStat.getYcp(),marketStat.getTotalTrades(),marketStat.getTotalVolume(),marketStat.getTotalValue(),
					marketStat.getPublicTotalTrades(),Double.valueOf(marketStat.getPublicTotalVolume()),marketStat.getPublicTotalValue(),
					change,Double.valueOf(df.format(changePercent)),sector,pe,pb,ps,auditedPe,
					outShares,disPrem,issuePrice,eps,de,totalDebt,equitiesPerShare,isDse30,epsYoy,epsCagr,dvdYield,totExposOrRiskWeightedAsset,
					tier1Capital,salesPerShare,null,reportDate);
		}
		return null;
	}
	
	public Map<String,Double> get3mAvgVolMap() {
		Map<String,Double> threeMonAvgVolMap = new LinkedHashMap<String,Double>();
		LocalDate date = LocalDate.now();
  	  	date = date.minusMonths(3);
  	  	List<Object[]> threeMonAvgVolArray = dailyPriceVolumeRepository.find3mAvgVol(date.toString());
  	  	DecimalFormat df1 = new DecimalFormat("#.##"); 
  	  	for(Object[] obj : threeMonAvgVolArray) {
			//BigDecimal value = (BigDecimal) obj[1];

			threeMonAvgVolMap.put((String) obj[0],Double.valueOf(df1.format(obj[1])));
		}
		return threeMonAvgVolMap;
	}

	public List<MarketStatDto> getAllBondData() throws ClassNotFoundException, SQLException{
		List<MarketStatDto> bondDataList = new ArrayList<MarketStatDto>();
		List<MarketStat> bondData = marketStatRepository.findBond();
		if(bondData != null && !bondData.isEmpty()) {
			for(MarketStat bond : bondData) {
				MarketStatDto bondDto = convertDomainToDto(bond);
				bondDataList.add(bondDto);
			}
		}
		return bondDataList;	
	}
	
	private Double getConditionalFV(Double fv, Double ltp, Double marketRatio, Double indicator, boolean isFinalFV) {
		if(fv!=null) {
			Double deviation = (fv/ltp)-1;
			if(CommonUtils.isNotInBetweenThreshold(deviation)) {
				if(isFinalFV) {
					fv = marketRatio;
				}else {
					fv = marketRatio!=null && indicator!=null && indicator>0 ? marketRatio * indicator : null;
				}
			}
		}
		return fv;
	}

	public List<MarketStatDto> getMarketDataListAfterFVCalculation(Map<String, Double> sectoralPEMap,
			Map<String, Double> sectoralPBMap, Map<String, Double> sectoralPSMap, Map<String, Double> dsexDataMap,
			Map<String, Integer> sectorTotalCount, List<MarketStatDto> marketDataList) {
		for(MarketStatDto marketStat : marketDataList) {
			String sector = marketStat.getSector();
			Double fv = null;
			
			if(!(sector!= null)){
				continue;
			}

			if(sector.equals("Mutual Fund")) {
				fv = marketStat.getEquitiesPerShare();
				marketStat.setFairValue(fv!=null ? Double.valueOf(df1.format(fv)) : null);
				continue;
			}
			Double earningsPortion = null;
			Double bookingPortion = null;
			Double salesPortion = null;
			Double sectorPE = sectoralPEMap.get(sector);
			Double sectorPB = sectoralPBMap.get(sector);
			Double sectorPS = sectoralPSMap.get(sector);
			earningsPortion = sectorPE!=null && marketStat.getEps()!=null && marketStat.getEps()>0 ? sectorPE * marketStat.getEps() : 0;
			bookingPortion = sectorPB!=null && marketStat.getEquitiesPerShare()!=null && marketStat.getEquitiesPerShare()>0 ? 
					sectorPB * marketStat.getEquitiesPerShare() : 0;
			salesPortion = sectorPS!=null && marketStat.getSalesPerShare()!=null && marketStat.getSalesPerShare()>0 ? 
					sectorPS * marketStat.getSalesPerShare() : 0;
			if(sector.equals("Mutual Fund")) {
				marketStat.setFairValue(fv!=null ? Double.valueOf(df1.format(fv)) : null);
			}
			int count = 0;
			if(earningsPortion!=0) count++;
			if(bookingPortion!=0) count++;
			if(salesPortion!=0) count++;
			if(count==3) fv = (earningsPortion*0.5) + (bookingPortion*0.2) + (salesPortion*0.3);
			if(count==2) fv = (earningsPortion*0.5) + (bookingPortion*0.5) + (salesPortion*0.5);
			if(count==1 && earningsPortion!=0 || bookingPortion !=0) fv = earningsPortion + bookingPortion;
			else fv=null;
			
			if(sectorTotalCount.get(sector) < 10) {
				Double tempFV = fv;
				Double deviation = null;
				if(fv!=null && marketStat.getLtp()!=0) {
					deviation = (fv/marketStat.getLtp())-1;
					if(CommonUtils.isNotInBetweenThreshold(deviation)) {
						Double marketPE = dsexDataMap.get("PE");
						Double marketPB = dsexDataMap.get("PB");
						Double marketPS = dsexDataMap.get("PS");					
						earningsPortion = marketPE!=null && marketStat.getEps()!=null && marketStat.getEps()>0 ? marketPE * marketStat.getEps() : 0;
						bookingPortion = marketPB!=null && marketStat.getEquitiesPerShare()!=null && marketStat.getEquitiesPerShare()>0 ? 
								marketPB * marketStat.getEquitiesPerShare() : 0;
						salesPortion = marketPS!=null && marketStat.getSalesPerShare()!=null && marketStat.getSalesPerShare()>0 ? 
								marketPS * marketStat.getSalesPerShare() : 0;
						
						fv = (earningsPortion*0.5) + (bookingPortion*0.2) + (salesPortion*0.3);
						
						fv = getConditionalFV(fv,marketStat.getLtp(),marketPE,marketStat.getEps(),false);
						fv = getConditionalFV(fv,marketStat.getLtp(),marketPB,marketStat.getEquitiesPerShare(),false);
						fv = getConditionalFV(fv,marketStat.getLtp(),marketPS,marketStat.getSalesPerShare(),false);
						fv = getConditionalFV(fv,marketStat.getLtp(),tempFV,null,true);
					}
				}				
			}
			marketStat.setFairValue(fv!=null ? Double.valueOf(df1.format(fv)) : null);
 		}
		return marketDataList;
	}

	public List<Object> getMarketRatioMaps(Map<String, Integer> sectorTotalCount,
			List<MarketStatDto> marketDataList, Map<String,Double> threeMonTotalVolMap) {
		
		List<Object> marketRatioMapList = new ArrayList<Object>();
		
		Map<String, Double> sectoralPEMap = new HashMap<String,Double>();
		Map<String, Double> sectoralPBMap = new HashMap<String,Double>();
		Map<String, Double> sectoralPSMap = new HashMap<String,Double>();
		Map<String, Double> sectoralDEMap = new HashMap<String,Double>();
		Map<String, Double> sectoralVolumeMap = new HashMap<String,Double>();
		Map<String, Double> sectoral3MonthVolumeMap = new HashMap<String,Double>();
		Map<String, Double> sectoralEPSMap = new HashMap<String,Double>();
		Map<String,List<MarketStatDto>> sectorCompanyMap = new LinkedHashMap<String,List<MarketStatDto>>();	
		
		for(Map.Entry<String, Integer> entry : sectorTotalCount.entrySet()) {
			List<MarketStatDto> sectorCompanyList = new ArrayList<MarketStatDto>();
			double sumMcapForPE = 0.0;
			double sumEarnings = 0.0;
			double sumMcapForPB = 0.0;
			double sumBook = 0.0;
			double sumMcapForPS = 0.0;
			double sumSales = 0.0;
			double sumEquities = 0.0;
			double sumDebt = 0.0;
			double sumVolume = 0;
			double sum3MonthVolume = 0;
			Double sumEPS = 0.0;
			double count = 0;
			double totalUP=0.0;
			for(MarketStatDto marketStat : marketDataList) {

				if(marketStat.getSector()!=null && marketStat.getSector().equals(entry.getKey())) {
					sectorCompanyList.add(marketStat);
					if(marketStat.getOutShares()!=null) {
						double mcap = marketStat.getLtp() * marketStat.getOutShares();
//						if(marketStat.getCode().contains("JUTESPINN") || 
//								marketStat.getCode().contains("NORTHERN") || 
//								marketStat.getCode().contains("SONALIANSH")) {
//									System.out.println(marketStat.getCode()+"   "+mcap);
//									totalUP +=mcap;
//									System.out.println(totalUP);	
//								}
						
						if(mcap==0.0) mcap = marketStat.getYcp() * marketStat.getOutShares();
						if(marketStat.getEps()!=null) {
							if(!Constants.getIgnoredTickerListForSectorPE().contains(marketStat.getCode())) {
								sumMcapForPE += mcap;
								double earnings = marketStat.getEps() * marketStat.getOutShares();
								sumEarnings += earnings;
							}
							
							sumEPS += marketStat.getEps();
							count++;
						}
						if(marketStat.getPb()!=null) {
							sumMcapForPB += mcap;
							double book = (marketStat.getLtp() / marketStat.getPb()) * marketStat.getOutShares();
							
							if(marketStat.getCode().contains("JUTESPINN") || 
									marketStat.getCode().contains("NORTHERN") || 
									marketStat.getCode().contains("SONALIANSH")) {
										//System.out.println(marketStat.getCode()+"   "+book);
										totalUP +=book;
										//System.out.println(totalUP);	
									}
							
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
					sumVolume += marketStat.getTotalVolume();
					System.out.println(marketStat.getCode());
					sum3MonthVolume += threeMonTotalVolMap.get(marketStat.getCode());
				}	
			}
			sectorCompanyMap.put(entry.getKey(),sectorCompanyList);
			if(sumEarnings!=0.0) {
				double sectorPE = sumMcapForPE / sumEarnings;
				sectoralPEMap.put(entry.getKey(),Double.valueOf(df.format(sectorPE)));
			}
			if(sumBook!=0.0) {
				double sectorPB = sumMcapForPB / sumBook;
				
				if (entry.getKey().contains("Jute")) {
					//System.out.println(entry.getKey()+ "  "+sectorPB+"  "+sumMcapForPB+"  "+sumBook);
				}
								
				sectoralPBMap.put(entry.getKey(),Double.valueOf(df.format(sectorPB)));
			}
			if(sumSales!=0.0) {
				double sectorPS = sumMcapForPS / sumSales;
				sectoralPSMap.put(entry.getKey(),Double.valueOf(df.format(sectorPS)));
			}
			if(sumEquities!=0.0) {
				double sectorDE = sumDebt / sumEquities;
				if(entry.getKey().equals("Bank") || entry.getKey().equals("NBFI")) sectorDE = sectorDE * 100;
				sectoralDEMap.put(entry.getKey(),Double.valueOf(df.format(sectorDE)));
			}
			sectoralVolumeMap.put(entry.getKey(),sumVolume);
			sectoral3MonthVolumeMap.put(entry.getKey(),sum3MonthVolume);
			sectoralEPSMap.put(entry.getKey(),Double.valueOf(df.format(sumEPS/count)));
		}
		
		marketRatioMapList.add(sectorCompanyMap);
		marketRatioMapList.add(sectoralPEMap);
		marketRatioMapList.add(sectoralPBMap);
		marketRatioMapList.add(sectoralPSMap);
		marketRatioMapList.add(sectoralDEMap);
		marketRatioMapList.add(sectoralVolumeMap);
		marketRatioMapList.add(sectoral3MonthVolumeMap);
		marketRatioMapList.add(sectoralEPSMap);
		
		return marketRatioMapList;
	}
    public static String getTickerEpsReportDate(String ticker) throws ClassNotFoundException, SQLException {
    	Connection con = connectLRGBLoader();
    	String date = "";
        String query = "SELECT report_date FROM dse_analyzer_loader.eps_details_archive where code = '"+ticker+"' order by report_date desc limit 1;";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
        	date = rs.getString("report_date");
        }
        rs.close();
        stmt.close();
        con.close();
		return date;
    }
    
    public static String getSectoralReportDate(String sector) throws ClassNotFoundException, SQLException {
    	Connection con = connectLRGBLoader();
    	Statement stmt = con.createStatement();
    	ResultSet rs = stmt.executeQuery("select max(report_date) as date from dse_analyzer_loader.eps_details_archive where code in "
    			+ "(select code from dse_analyzer.company where sector = '"+sector+"' and trading_status = 'Trading');");
    	String reportDate = "";
    	while(rs.next()) {
    		reportDate = rs.getString("date");
    	}
    	return reportDate;
    }
    
	public static Connection connectLRGBLoader() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://192.168.100.12:3306/dse_analyzer?rewriteBatchedStatements=true", "lrgb_loader",
		"developer4");
		con.setAutoCommit(true);
		return con;
	}
}
