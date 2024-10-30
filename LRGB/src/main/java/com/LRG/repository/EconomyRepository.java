package com.LRG.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.LRG.domain.Economy;

@Repository
public interface EconomyRepository extends JpaRepository<Economy, Integer> {
	List<Economy> findAll();
}
