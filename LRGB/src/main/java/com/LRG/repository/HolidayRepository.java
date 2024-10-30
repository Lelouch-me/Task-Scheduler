package com.LRG.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.LRG.domain.MarketHoliday;

@Repository
public interface HolidayRepository extends JpaRepository<MarketHoliday,Integer>{

	@Query(value="select holiday_date from market_holidays",nativeQuery = true)
	List<String> findAllHolidayDate();
}
