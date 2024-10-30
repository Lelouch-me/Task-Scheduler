package com.LRG.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.LRG.domain.DailyCompanyInfo;

@Repository
public interface DailyCompanyInfoRepository extends JpaRepository<DailyCompanyInfo, Integer>{

	DailyCompanyInfo findByCode(String code);
}
