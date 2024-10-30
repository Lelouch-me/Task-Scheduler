package com.LRG.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.LRG.domain.EconomicYield;


@Repository
public interface EconomicYieldRepository extends JpaRepository <EconomicYield, Date>{
	
	@Query(value = "SELECT * FROM economic_yield where month = :month ;",nativeQuery = true)
	ArrayList<EconomicYield>findvalue(String month);
	
}
