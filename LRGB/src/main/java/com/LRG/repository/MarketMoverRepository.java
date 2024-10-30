package com.LRG.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.LRG.domain.MarketData;

@Repository
public interface MarketMoverRepository extends JpaRepository<MarketData, Integer>{

	@Query(value="select * from market_data where contribution!=0 order by contribution desc limit 10",nativeQuery = true)
	List<MarketData> findTop10ByOrderByContributionDesc();

	@Query(value="select * from market_data where contribution!=0 order by contribution limit 10",nativeQuery = true)
	List<MarketData> findTop10ByOrderByContributionAsc();

	@Query(value="select * from market_data where price_change_percent!= -100 order by price_change_percent desc limit 10",nativeQuery = true)
	List<MarketData> findTop10ByOrderByPriceChangePercentDesc();

	@Query(value="select * from market_data where price_change_percent!= -100 order by price_change_percent limit 10",nativeQuery = true)
	List<MarketData> findTop10ByOrderByPriceChangePercentAsc();

	int countByPriceChangePercentNot(double d);
	
	int countByPriceChangePercentGreaterThan(double d);
	
	int countByPriceChangePercentLessThan(double d);

	@Query(value="select sum(marketcap),sector,sum(last_mcap) from "
			+ "(select marketcap,sector,last_mcap from market_data u inner join company as c "
			+ "where c.code = u.code) as a where marketcap!=0 group by sector",nativeQuery = true)
	List<Object[]> findSectorReturn();
	
	
	@Query(value="select sector,sum(value) as tot_val from (select sector,total_value as value from "
			+ "market_stat u inner join company as c where (u.type like '%MF' or u.type like '%EQ') and c.code = u.code) as a "
			+ "group by sector order by tot_val desc",nativeQuery = true)
	List<Object[]> findSectorTurnover();
	
	
	@Query(value="select sector,count(a.code) as tot_count from (select sector,u.code from "
			+ "market_data u inner join company as c where c.code = u.code) as a "
			+ "group by sector order by tot_count desc",nativeQuery = true)
	List<Object[]> findSectorMapTotal();
	
	
	@Query(value="select sector,count(a.code) from (select sector,u.code,u.price_change_percent from "
			+ "market_data u inner join company as c where c.code = u.code) as a "
			+ "where a.price_change_percent>0 group by sector",nativeQuery = true)
	List<Object[]> findSectorMapUp();
	
	
	@Query(value="select sector,count(a.code) from (select sector,u.code,u.price_change_percent from "
			+ "market_data u inner join company as c where c.code = u.code) as a "
			+ "where a.price_change_percent<0 group by sector",nativeQuery = true)
	List<Object[]> findSectorMapDown();
	
	
	@Query(value="select sector,count(a.code) from (select sector,u.code,u.price_change_percent from "
			+ "market_data u inner join company as c where c.code = u.code) as a "
			+ "where a.price_change_percent=0 group by sector",nativeQuery = true)
	List<Object[]> findSectorMapFlat();

	@Query(value="select sector,((b.tot_mcap/(select sum(marketcap) from dse_analyzer.market_data))*100) as weighted_mcap from "
			+ "(select sum(marketcap) as tot_mcap,sector from (select marketcap,sector from dse_analyzer.market_data u inner join "
			+ "dse_analyzer.company as c where c.code = u.code) as a group by sector) as b",nativeQuery = true)
	List<Object[]> findWeightedMarketcap();

	@Query(value="select sector,sum(marketcap)/sum(tot_income) from (select marketcap,sector,tot_income from dse_analyzer.market_data u inner join "
			+ "dse_analyzer.company as c where c.code = u.code) as a group by sector",nativeQuery = true)
	List<Object[]> findSectoralPE();

	@Query(value="select u.code,sector,total_value from dse_analyzer.market_stat u inner join dse_analyzer.company as c where c.code = u.code",
			nativeQuery = true)
	List<Object[]> findTickerValue();

	@Query(value="select u.code,sector,total_volume from dse_analyzer.market_stat u inner join dse_analyzer.company as c where c.code = u.code",
			nativeQuery = true)
	List<Object[]> findTickerVolume();
	
	@Query(value="select u.code,sector,marketcap from dse_analyzer.market_data u inner join dse_analyzer.company as c where c.code = u.code",
			nativeQuery = true)
	List<Object[]> findTickerMcap();
	
	@Query(value="select code,ROUND(((LTP-YCP)/YCP)*100,2) as ret,LTP,total_trades from dse_analyzer.market_stat",nativeQuery = true)
	List<Object[]> findTickerInfo();

	@Query(value="select z.sector,sum(weighted_dvd_yield) from (select d.code,d.sector,(d.marketcap/b.tot_mcap)*d.dvd_yield*100 as weighted_dvd_yield from "
			+ "(select sum(marketcap) as tot_mcap,sector from (select marketcap,sector from market_data u inner join company as c "
			+ "where c.code = u.code) as a group by sector) as b inner join (select m.code,m.marketcap,m.dvd_yield,c.sector from market_data m "
			+ "inner join company c on m.code=c.code) as d on b.sector=d.sector) as z group by z.sector",nativeQuery = true)
	List<Object[]> findWeightedDividendYield();
	
	@Query(value="select sum(marketcap/((select sum(marketcap) from dse_analyzer.market_data))*dvd_yield*100) as index_dvd_yield "
			+ "from dse_analyzer.market_data",nativeQuery = true)
	Double findDSEXDividendYield();
	
	@Query(value="select sum(marketcap/((select sum(marketcap) from dse_analyzer.market_data m inner join "
			+ "dse_analyzer.daily_company_info d on m.code=d.code and d.dse30='Y'))*dvd_yield*100) as index_dvd_yield "
			+ "from dse_analyzer.market_data;",nativeQuery = true)
	Double findDS30DividendYield();
}
