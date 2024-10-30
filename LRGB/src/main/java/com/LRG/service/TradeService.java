package com.LRG.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.LRG.domain.Trade;
import com.LRG.model.TradeDto;
import com.LRG.repository.TradeRepository;

@Service
public class TradeService {

	private TradeRepository tradeRepository;
	
	public TradeService(TradeRepository tradeRepository) {
		super();
		this.tradeRepository = tradeRepository;
	}
	
	public List<TradeDto> getAllTradeData(){
		List<TradeDto> tradeList = new ArrayList<TradeDto>();
		List<Trade> tradeData = tradeRepository.findAll();
		if(tradeData != null && !tradeData.isEmpty()) {
			for(Trade trade : tradeData) {
				TradeDto tradeDto = convertDomainToDto(trade);
				tradeList.add(tradeDto);
			}
		}
		return tradeList;	
	}
	
	private TradeDto convertDomainToDto(Trade trade) {
		DecimalFormat df = new DecimalFormat("#.##"); 
		return new TradeDto(trade.getTotalTrades(),trade.getTotalVolume(),Double.valueOf(df.format(trade.getTotalValue())));	
	}
}
