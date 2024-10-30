package com.LRG.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.LRG.domain.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer>{
	
	List<News> findAllByTypeOrderByPubDateDesc(String type);
}
