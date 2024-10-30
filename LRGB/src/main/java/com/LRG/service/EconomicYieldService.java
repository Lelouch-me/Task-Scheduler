package com.LRG.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.LRG.domain.EconomicYield;
import com.LRG.repository.EconomicYieldRepository;

@Service
public class EconomicYieldService {
	
	@Autowired
	private EconomicYieldRepository  economicYieldRepository;
	
	public List<Double> getYieldData(String month){
		
//		LinkedHashMap<Date,Double> priceValueMap = new LinkedHashMap<Date,Double>();
//		LocalDate date = LocalDate.now();
//  	  	date = date.minusYears(2);
		
		List <Double> priceValueMap= new ArrayList <Double>(); 
		List<EconomicYield> monthlyPriceValueList = economicYieldRepository.findvalue(month);
		if(monthlyPriceValueList.size()>0) {
			for(EconomicYield monthlyPriceValue : monthlyPriceValueList) {
				if(monthlyPriceValue!=null) {
					priceValueMap.add(monthlyPriceValue.getNine1DayTbill());
					priceValueMap.add(monthlyPriceValue.getOne82DayTbill());
					priceValueMap.add(monthlyPriceValue.getThree64DayTbill());
					priceValueMap.add(monthlyPriceValue.getFiveYrTbond());
					priceValueMap.add(monthlyPriceValue.getTenYrTbond());
					priceValueMap.add(monthlyPriceValue.getFifteenYrTbond());
					priceValueMap.add(monthlyPriceValue.getTwentyYrTbond());					
				}
			}
		}
		return priceValueMap;	
	}
}
