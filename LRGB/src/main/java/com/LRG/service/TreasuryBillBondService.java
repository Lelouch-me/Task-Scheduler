package com.LRG.service;


import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.LRG.domain.TreasuryBill;
import com.LRG.model.TreasuryBillDto;
import com.LRG.repository.TreasuryBillRepository;

@Service
public class TreasuryBillBondService {

	private TreasuryBillRepository treasuryBillRepository;
	
	public TreasuryBillBondService(TreasuryBillRepository treasuryBillRepository) {
		super();
		this.treasuryBillRepository = treasuryBillRepository;
	}
	
	public List<TreasuryBillDto> getAllTreasuryData(){
		List<TreasuryBillDto> treasuryBillList = new ArrayList<TreasuryBillDto>();
		List<TreasuryBill> treasuryData = treasuryBillRepository.findAll();
		if(treasuryData != null && !treasuryData.isEmpty()) {
			for(TreasuryBill treasuryBill : treasuryData) {
				TreasuryBillDto treasuryBillDto = convertDomainToDto(treasuryBill);
				treasuryBillList.add(treasuryBillDto);
			}
		}
		return treasuryBillList;	
	}
	
	private TreasuryBillDto convertDomainToDto(TreasuryBill treasuryBill) {
		return new TreasuryBillDto(treasuryBill.getTreasuryName(), treasuryBill.getYield(),treasuryBill.getIssuedDate(),treasuryBill.getInsertDate());	
	}
}
