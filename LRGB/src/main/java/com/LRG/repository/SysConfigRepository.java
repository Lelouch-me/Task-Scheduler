package com.LRG.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.LRG.domain.SystemConfig;

@Repository
public interface SysConfigRepository extends JpaRepository<SystemConfig,Integer>{

	SystemConfig findById(int id);
}
