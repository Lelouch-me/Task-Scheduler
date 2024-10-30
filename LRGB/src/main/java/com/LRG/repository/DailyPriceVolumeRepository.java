package com.LRG.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.LRG.domain.DailyPriceVolume;

@Repository
public interface DailyPriceVolumeRepository extends JpaRepository<DailyPriceVolume,Integer>{

	@Query(value="select * from daily_price_volume where ticker=?1 and date>?2 order by date",nativeQuery = true)
	ArrayList<DailyPriceVolume> findAllByTicker(String code,String date);
	
	@Query(value="select ticker,avg(volume) from daily_price_volume where date>=?1 group by ticker",nativeQuery = true)
	List<Object[]> find3mAvgVol(String date);
}
