package com.LRG.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.LRG.domain.Trade;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Integer>{
	
}
