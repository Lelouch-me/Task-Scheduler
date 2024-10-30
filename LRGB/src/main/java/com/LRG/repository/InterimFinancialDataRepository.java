package com.LRG.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.LRG.domain.InterimFinancialData;

@Repository
public interface InterimFinancialDataRepository extends JpaRepository<InterimFinancialData, Integer>{

	List<InterimFinancialData> findAllByCode(String code);

}
