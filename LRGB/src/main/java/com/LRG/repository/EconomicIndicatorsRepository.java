package com.LRG.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.LRG.domain.EconomicIndicators;

@Repository
public interface EconomicIndicatorsRepository extends JpaRepository<EconomicIndicators, Date> {
	//List<Economy_indicators> findFirst10IndicatorsOrderByMonthDesc();	
	@Query(value="select * from economic_indicators where indicator= :type and month >= :interval AND month <= :last",nativeQuery = true)
	List<EconomicIndicators> getIndicatorValue(String interval, String last, String type);
	
	@Query(value="SELECT * FROM dse_analyzer.economic_indicators where indicator = :type order by month desc limit 2;",nativeQuery = true)
	List<EconomicIndicators> getMonthlyValue(String type);
	
	@Query(value="select indicator, avg(value) as month from dse_analyzer.economic_indicators where indicator='ExchngeRate' and"
			+ " month >= :middle and month < :high union select indicator, avg(value) as month from dse_analyzer.economic_indicators where indicator='ExchngeRate'"
			+ " and month >= :lowest and month < :middle ;",nativeQuery = true)
	List<Object[]> getExcnaheRate(String high,String middle, String lowest);
}
