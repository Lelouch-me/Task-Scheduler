package com.LRG.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.LRG.domain.AdjustedPrice;

public interface AdjustedPriceRepository extends JpaRepository<AdjustedPrice, Integer>{

	@Query(value="select * from adjusted_price where code=?1 and date>=?2 and date<=?3 order by date",nativeQuery = true)
	ArrayList<AdjustedPrice> findCloseVolumeByTicker(String code, String startDate,String endDate);
	
	@Query(value="select ((curr/prev)-1)*100 as ret from (select * from(select capital_value as curr "
			+ "from dse_analyzer.idx where id=?1 order by date_time desc LIMIT 1) a join "
			+ "(select adjstd_close as prev from dse_analyzer.adjusted_price  where code=?2 and date>=?3 "
			+ "order by date LIMIT 1) b) c",nativeQuery = true)
	double findIndexReturn(String code, String code2, String date);
	
	@Query(value="select ((curr/prev)-1)*100 as ret from (select * from(select capital_value as curr "
			+ "from dse_analyzer.idx where id=?1 order by date_time desc LIMIT 1) a join "
			+ "(select adjstd_close as prev from dse_analyzer.adjusted_price  where code=?2 and date<=?3 "
			+ "order by date desc LIMIT 1) b) c",nativeQuery = true)
	double findDailyIndexReturn(String code, String code2, String date);

	@Query(value="select * from adjusted_price where code=?1 and date>=?2 and date<=(select max(date) from dse_analyzer.adjusted_price "
			+ "where code='LR Global Index') order by date",nativeQuery = true)
	List<AdjustedPrice> findCloseVolumeByTickerForIndex(String code, String startDate);
	
	@Query(value="select * from adjusted_price where code=?1 and date>=?2 and date in(select date from dse_analyzer.adjusted_price "
			+ "where code='LR Global Index W') and date<=?3 order by date",nativeQuery = true)
	List<AdjustedPrice> findCloseVolumeByTickerForWeeklyIndex(String code, String startDate, String endDate);

	@Query(value="select * from adjusted_price where date>='2018-01-01' and code in(:tickers) order by date",nativeQuery = true)
	List<Object[]> getPriceDataForOptimizer(@Param("tickers") List<String> tickers);

	@Query(value="select * from adjusted_price where code=?1 and date>=?2 and date<=(select max(date) from dse_analyzer.adjusted_price "
			+ "where code='SPX Index') order by date",nativeQuery = true)
	List<AdjustedPrice> findCloseByTickerForForeignIndex(String code, String startDate);
	
	@Query(value="select * from(select curr,(curr-prev) as price_change, ((curr-prev)/prev)*100 as price_change_percent from "
			+ "(select * from (SELECT adjstd_close as curr FROM dse_analyzer.adjusted_price where code=?1 order by date desc limit 1) a "
			+ "join (SELECT adjstd_close as prev FROM dse_analyzer.adjusted_price where code=?1 order by date desc limit 1,1) b) c)d",
			nativeQuery = true)
	Object[][] getPriceDataForForeignIndex(String code);

	@Query(value="select * from adjusted_price where code=?1 and date>=?2 and date in(select date from dse_analyzer.adjusted_price "
			+ "where code='LR Global Index W') and date<=?3 order by date",nativeQuery = true)
	ArrayList<Object[]> findCloseVolumeByTickerForMF(String code, String startDate, String endDate);
	
//	@Query(value="select * from adjusted_price where code=?1 and date>=?2  and date<=?3 order by date",nativeQuery = true)
//	ArrayList<Object[]> findCloseVolumeByTickerForMF(String code, String startDate, String endDate);

	@Query(value="select max(date) from (select min(date) as date,code from dse_analyzer.adjusted_price where code in(:tickers) group by code) a",nativeQuery = true)
	String findModifiedStockAnalysisStartDate(@Param("tickers") List<String> tickerList);
	
	@Query(value="select * from adjusted_price where code=?1 and date>=?2 order by date",nativeQuery = true)
	ArrayList<AdjustedPrice> findDSEXDailyValue(String code, String startDate);
}
