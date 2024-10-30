package com.LRG.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.LRG.domain.TreasuryBill;

@Repository
public interface TreasuryBillRepository extends JpaRepository<TreasuryBill, Integer>{
	
}