package com.LRG.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.LRG.domain.EpsData;

public interface EpsDataRepository extends JpaRepository<EpsData, Integer>{

	List<EpsData> findAllByCodeOrderByYearDesc(String ticker);
	
	@Query(value="select year,tot_earnings from (select sector,year,sum(annual_earnings) as tot_earnings from (select code,sector,year,annual_eps*out_shares as annual_earnings "
			+ "from eps_data) b group by sector,year) a where sector=?1 order by a.year desc",nativeQuery = true)
	ArrayList<Object[]> findEpsDataForSector(String sector);

	@Query(value="select * from (select year,sum(annual_earnings) from (select code,year,annual_eps*out_shares as annual_earnings "
			+ "from dse_analyzer.eps_data ) b group by year) a order by a.year desc",nativeQuery = true)
	ArrayList<Object[]> findEpsDataForDSEX();

	@Query(value="select * from (select year,sum(annual_earnings) from (select code,year,annual_eps*out_shares as annual_earnings "
			+ "from dse_analyzer.eps_data where code in(select code from dse_analyzer.daily_company_info where dse30='Y')) b "
			+ "group by year) a order by a.year desc",nativeQuery = true)
	ArrayList<Object[]> findEpsDataForDS30();
	
	
	@Query(value="select count(code) from eps_data where sector=?1 and year=?2",nativeQuery = true)
	int countByYearForSector(String sector,int year);
	
	@Query(value="select count(code) from eps_data where year=?1",nativeQuery = true)
	int countByYearForDSEX(int year);
	
	@Query(value="select count(code) from eps_data where code in(select code from daily_company_info "
			+ "where dse30='Y') and year=?1",nativeQuery = true)
	int countByYearForDS30(int year);
	
	@Query(value="select year,tot_earnings from (select year,sum(annual_earnings) as tot_earnings from "
			+ "(select code,sector,year,annual_eps*out_shares as annual_earnings from eps_data where code in(select code from eps_data "
			+ "where sector=?1 and year=?2)) b group by year) a where year<=?2 order by a.year desc",nativeQuery = true)
	ArrayList<Object[]> findEpsDataForSectorForPrevYear(String sector,int year);
	
	@Query(value="select year,tot_earnings from (select sector,year,sum(annual_earnings) as tot_earnings from "
			+ "(select code,sector,year,annual_eps*out_shares as annual_earnings from eps_data where code in(select code from eps_data "
			+ "where sector=?1 and year=?2)) b group by sector,year) a where sector=?1 order by a.year desc",nativeQuery = true)
	ArrayList<Object[]> findEpsDataForSector(String sector,int year);
	
	@Query(value="select * from (select year,sum(annual_earnings) from (select code,year,annual_eps*out_shares as annual_earnings "
			+ "from dse_analyzer.eps_data where code in(select code from eps_data where year=?1)) b group by year) a "
			+ "where year<=?1 order by a.year desc",nativeQuery = true)
	ArrayList<Object[]> findEpsDataForDSEXForPrevYear(int year);
	
	@Query(value="select * from (select year,sum(annual_earnings) from (select code,year,annual_eps*out_shares as annual_earnings "
			+ "from dse_analyzer.eps_data where code in(select code from eps_data where year=?1)) b group by year) a "
			+ "order by a.year desc",nativeQuery = true)
	ArrayList<Object[]> findEpsDataForDSEX(int year);
	
	@Query(value="select * from (select year,sum(annual_earnings) from (select code,year,annual_eps*out_shares as annual_earnings "
			+ "from dse_analyzer.eps_data where code in(select code from eps_data where year=?1) and code in "
			+ "(select code from dse_analyzer.daily_company_info where dse30='Y')) b group by year) a "
			+ "where year<=?1 order by a.year desc",nativeQuery = true)
	ArrayList<Object[]> findEpsDataForDS30ForPrevYear(int year);
	
	@Query(value="select * from (select year,sum(annual_earnings) from (select code,year,annual_eps*out_shares as annual_earnings "
			+ "from dse_analyzer.eps_data where code in(select code from eps_data where year=?1) and code in(select code "
			+ "from daily_company_info where dse30='Y')) b group by year) a order by a.year desc",nativeQuery = true)
	ArrayList<Object[]> findEpsDataForDS30(int year);
}
