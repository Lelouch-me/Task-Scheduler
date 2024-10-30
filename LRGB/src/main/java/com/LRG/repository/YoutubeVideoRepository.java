package com.LRG.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.LRG.domain.YouTubeVideos;

@Repository
public interface YoutubeVideoRepository extends JpaRepository<YouTubeVideos, Integer>{

	List<YouTubeVideos> findTop3ByOrderByIdDesc();
	
}
