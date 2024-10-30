package com.LRG.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.LRG.domain.MarketStat;

@Repository
public interface MarketStatRepository  extends JpaRepository<MarketStat, Integer>{

	@Query(value="select * from market_stat where type not in('A-CB','A-DB','N-CB') order by code",nativeQuery = true)
	List<MarketStat> findEquityAndMF();

	@Query(value="select * from market_stat where type in('A-CB','A-DB','N-CB')",nativeQuery = true)
	List<MarketStat> findBond();

}
