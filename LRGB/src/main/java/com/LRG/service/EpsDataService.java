package com.LRG.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Service;

import com.LRG.Utils.Constants;
import com.LRG.domain.EpsData;
import com.LRG.model.EpsDataDto;
import com.LRG.repository.EpsDataRepository;

@Service
public class EpsDataService {
	
private EpsDataRepository epsDataRepository;
	
	public EpsDataService(EpsDataRepository epsDataRepository) {
		super();
		this.epsDataRepository = epsDataRepository;
	}
	
	public List<Double> getEpsYoyDataByTicker(String ticker) {
		List<EpsDataDto> EpsDataDtoList = new ArrayList<EpsDataDto>();
		List<EpsData> epsDataList = epsDataRepository.findAllByCodeOrderByYearDesc(ticker);
		if(epsDataList != null) {
			for(EpsData epsData : epsDataList) {
				EpsDataDto epsDataDto = convertDomainToDto(epsData);
				if(epsDataDto != null) EpsDataDtoList.add(epsDataDto);
			}
		}		
		int size = EpsDataDtoList.size();
		Double epsYoy = null;	
		Double epsCagr = null;
		Double currEps = null;
		Double prevEps = null;
		Double epsToCalculateCagr = null;
		boolean flag = false;
		for(int i = 0; i < size; i++) {
			if(i==0) currEps = EpsDataDtoList.get(i).getAnnualEps();
			if(i==1) prevEps = EpsDataDtoList.get(i).getAnnualEps();
			if(size>3) {
				if(i==3) epsToCalculateCagr = EpsDataDtoList.get(i).getAnnualEps();
			}else if(size==3) {
				if(i==2) epsToCalculateCagr = EpsDataDtoList.get(i).getAnnualEps();
				flag = true;
			}
		}
		epsYoy = prevEps!=null && prevEps!=0 && currEps>=0 && prevEps>=0 ? ((currEps-prevEps)/prevEps)*100 : null;
		if(flag) {
			epsCagr = epsToCalculateCagr!=null && epsToCalculateCagr!=0 && currEps>=0 && epsToCalculateCagr>=0? 
					((Math.pow((currEps/epsToCalculateCagr),(0.5)))-1)*100 : null;
		}else {
			epsCagr = epsToCalculateCagr!=null && epsToCalculateCagr!=0 && currEps>=0 && epsToCalculateCagr>=0? 
					((Math.pow((currEps/epsToCalculateCagr),(0.33333333)))-1)*100 : null;
		}

		List<Double> epsList = new ArrayList<Double>();
		epsList.add(epsYoy);
		epsList.add(epsCagr);
		return epsList;
	}
	
	public List<Double> getEpsYoyDataBySector(String sector) {
		List<Object[]> epsDataList = epsDataRepository.findEpsDataForSector(sector);
		List<Double> epsList = calculateEPSYoY(epsDataList);
		return epsList;
	}

	public List<Double> getEpsYoyDataForDSEX() {
		List<Object[]> epsDataList = epsDataRepository.findEpsDataForDSEX();
		List<Double> epsList = calculateEPSYoY(epsDataList);
		return epsList;
	}

	public List<Double> getEpsYoyDataForDS30() {
		List<Object[]> epsDataList = epsDataRepository.findEpsDataForDS30();
		List<Double> epsList = calculateEPSYoY(epsDataList);
		return epsList;
	}
	
	private List<Double> calculateEPSYoY(List<Object[]> epsDataList){
		List<Double> epsList = new ArrayList<Double>();
		if(epsDataList != null) {
			int size = epsDataList.size();
			Double epsYoy = null;	
			Double epsCagr = null;
			Double currEps = null;
			Double prevEps = null;
			Double epsToCalculateCagr = null;
			boolean flag = false;
			for(int i = 0; i < size; i++) {
				BigDecimal bd = (BigDecimal) epsDataList.get(i)[1];
				if(bd!=null) {
					if(i==0) currEps = bd.doubleValue();
					
					if(i==1) prevEps = bd.doubleValue();
					if(size>3) {
						if(i==3) epsToCalculateCagr = bd.doubleValue();
					}else if(size==3) {
						if(i==2) epsToCalculateCagr = bd.doubleValue();
						flag = true;
					}
				}
			}
			epsYoy = prevEps!=null && prevEps!=0 ? ((currEps-prevEps)/(prevEps<0 ? prevEps*(-1) : prevEps))*100 : null;
			if(flag) {
				epsCagr = epsToCalculateCagr!=null && epsToCalculateCagr!=0 && currEps>=0 && epsToCalculateCagr>=0? 
						((Math.pow((currEps/epsToCalculateCagr),(0.5)))-1)*100 : null;
			}else {
				epsCagr = epsToCalculateCagr!=null && epsToCalculateCagr!=0 && currEps>=0 && epsToCalculateCagr>=0? 
						((Math.pow((currEps/epsToCalculateCagr),(0.33333333)))-1)*100 : null;
			}

			DecimalFormat df = new DecimalFormat("#.##"); 		
			epsList.add(epsYoy != null ? Double.valueOf(df.format(epsYoy)) : null);
			epsList.add(epsCagr != null ? Double.valueOf(df.format(epsCagr)) : null);
			
			return epsList;	
		}
		return null;
	}
	
	private EpsDataDto convertDomainToDto(EpsData epsData) {
		return new EpsDataDto(epsData.getCode(),epsData.getSector(),epsData.getYear(),epsData.getAnnualEps());
	}
	
	public List<Double> getEpsYoyDataBySector(String sector, Integer sectorTickerCount) {
		List<Double> epsList = new ArrayList<Double>();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int count = epsDataRepository.countByYearForSector(sector,year);
		if(count < sectorTickerCount * Constants.countPercentageForCompany) {
			year = year - 1;
			List<Object[]> epsDataList = epsDataRepository.findEpsDataForSectorForPrevYear(sector,year);
			epsList = calculateEPSYoY(epsDataList);
		}else {
			List<Object[]> epsDataList = epsDataRepository.findEpsDataForSector(sector,year);
			epsList = calculateEPSYoY(epsDataList);
		}
		return epsList;
	}
	
	public List<Double> getEpsYoyDataForDSEX(Integer tickerCount) {
		List<Double> epsList = new ArrayList<Double>();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int count = epsDataRepository.countByYearForDSEX(year);
		if(count < tickerCount * Constants.countPercentageForCompany) {
			year = year - 1;
			List<Object[]> epsDataList = epsDataRepository.findEpsDataForDSEXForPrevYear(year);
			epsList = calculateEPSYoY(epsDataList);
		}else {
			List<Object[]> epsDataList = epsDataRepository.findEpsDataForDSEX(year);
			epsList = calculateEPSYoY(epsDataList);
		}
		return epsList;
	}
	
	public List<Double> getEpsYoyDataForDS30(Integer tickerCount) {
		List<Double> epsList = new ArrayList<Double>();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int count = epsDataRepository.countByYearForDS30(year);
		if(count < tickerCount * Constants.countPercentageForCompany) {
			year = year - 1;
			List<Object[]> epsDataList = epsDataRepository.findEpsDataForDS30ForPrevYear(year);
			epsList = calculateEPSYoY(epsDataList);
		}else {
			List<Object[]> epsDataList = epsDataRepository.findEpsDataForDS30(year);
			epsList = calculateEPSYoY(epsDataList);
		}
		return epsList;
	}
}
