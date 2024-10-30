package com.LRG.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LRG.domain.Report;

public interface ReportRepository extends JpaRepository<Report, Integer>{

	ArrayList<Report> findAllByOrderByYearDesc();

}
