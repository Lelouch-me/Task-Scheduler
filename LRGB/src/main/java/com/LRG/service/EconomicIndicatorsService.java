package com.LRG.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.LRG.domain.EconomicIndicators;
import com.LRG.model.EconomicIndicatorsDto;
import com.LRG.repository.EconomicIndicatorsRepository;




@Service
public class EconomicIndicatorsService {
	
	@Autowired
	private EconomicIndicatorsRepository  economic_indicatorsRepository;	
	
	public LinkedHashMap<Date,Double> getIndicatorData(String month,String lastMonth, String type){
		LinkedHashMap<Date,Double> priceValueMap = new LinkedHashMap<Date,Double>();
		List<EconomicIndicators> monthlyPriceValueList = economic_indicatorsRepository.getIndicatorValue(month,lastMonth,type);
		if(monthlyPriceValueList.size()>0) {
			for(EconomicIndicators monthlyPriceValue : monthlyPriceValueList) {
				if(monthlyPriceValue!=null) {
					priceValueMap.put(monthlyPriceValue.getMonth(),monthlyPriceValue.getValue());
				}
			}
		}
		return priceValueMap;	
	}
	
	public List<EconomicIndicatorsDto> getMonthlyData(){
		Date date1 = null ;
		Date date2 = null;
		Date date3 = null;
		
		Double latestValue=0.0;
		//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<EconomicIndicatorsDto> economyDtoList = new ArrayList<EconomicIndicatorsDto>();
		String [] indicatorList =new String[] {"Foreign Exchange Reserve", "Call Money", "ExchngeRate"};
		
		for ( String strindicatorName: indicatorList){
			List<EconomicIndicators> economyy=new ArrayList<EconomicIndicators>();
			int i=0;			
			
			if(strindicatorName.equals("ExchngeRate")) {
				//economyy = economic_indicatorsRepository.getExcnaheRate(StartDate,endDate);
			}else {
				economyy = economic_indicatorsRepository.getMonthlyValue(strindicatorName);
				if(economyy != null && !economyy.isEmpty()) {
					for(EconomicIndicators economy : economyy) {				
						if(i==0) {
							latestValue=economy.getValue();
							//date2=dateFormat.format(economy.getMonth());
							date2=economy.getMonth();
							//System.out.println(date2);
							
						}
						if(i==1) {
							EconomicIndicatorsDto economyDto = convertDomainToDto(economy,date2,latestValue);
							if(economyDto!=null) economyDtoList.add(economyDto);
							//date3=dateFormat.format(economy.getMonth());
							//date1=dateFormat.format((economy.getMonth()));
							date3=economy.getMonth();
							//System.out.println(date3);						
						}
						i++;
					}
				}
				
			}

						
		}
		
		return economyDtoList;	
	}
	
	private EconomicIndicatorsDto convertDomainToDto(EconomicIndicators economy, Date last, Double latestValue) {
		Double lastValue=latestValue;
		Date latestMonth=last;
		//Date previousLatestMonth=previous;
		
		return new EconomicIndicatorsDto(economy.getIndicator(),latestMonth,economy.getMonth(),lastValue,economy.getValue(),economy.getUpdateDate());
	}
	
	public LinkedHashMap<String,Double> getExchangeData(String highestDate,String middleDate,String lowest ){
		String indicator= null;
		Double avgRate=0.0;
		LinkedHashMap<String,Double> priceValueMap = new LinkedHashMap<String,Double>();
		List<Object[]> rateValue = economic_indicatorsRepository.getExcnaheRate(highestDate,middleDate,lowest);
		int i=0;
		for(Object[] obj : rateValue) {
			if(i==0){
				indicator=(String) obj[0];
				avgRate = (Double) obj[1];
				priceValueMap.put(indicator,avgRate);
				i++;
			}
			
			if(i==1){
				indicator=(String) obj[0]+"1";
				avgRate = (Double) obj[1];
				priceValueMap.put(indicator,avgRate);
			}
			
			
		}		
		return priceValueMap;		
	}	
}
