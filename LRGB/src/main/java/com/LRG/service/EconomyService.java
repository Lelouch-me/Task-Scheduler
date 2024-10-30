package com.LRG.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.LRG.domain.Economy;
import com.LRG.model.EconomyDto;
import com.LRG.repository.EconomyRepository;

@Service
public class EconomyService {

private EconomyRepository economyRepository;
	
	public EconomyService(EconomyRepository economyRepository) {
		super();
		this.economyRepository = economyRepository;
	}
	
	public List<EconomyDto> getEconomy(){
		List<EconomyDto> economyDtoList = new ArrayList<EconomyDto>();
		List<Economy> economyy = economyRepository.findAll();
		if(economyy != null && !economyy.isEmpty()) {
			for(Economy economy : economyy) {
				EconomyDto economyDto = convertDomainToDto(economy);
				if(economyDto!=null) economyDtoList.add(economyDto);				
			}
		}
		return economyDtoList;	
	}

	private EconomyDto convertDomainToDto(Economy economy) {
		return new EconomyDto(economy.getId(),economy.getIndicator(),economy.getPeriod(),economy.getUpdateDate(),economy.getCurrentFY(),economy.getPreviousFY(),economy.getChange());
	}
//	public void addReport(String p, String d, int y, String l) {
//		reportRepository.save(new Report(p,d,y,l));			
//	}
}
