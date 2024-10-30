package com.LRG.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.LRG.domain.Index;

@Repository
public interface IndexRepository extends JpaRepository<Index, Integer>{

	List<Index> findTop3ByOrderByDateTimeDesc();

	@Query(value="select * from idx where id=?1 and Time(date_time)>=?2",nativeQuery = true)
	List<Index> findAllById(String code, String startTime);

}
