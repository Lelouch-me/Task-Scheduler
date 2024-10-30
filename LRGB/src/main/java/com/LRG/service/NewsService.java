package com.LRG.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.LRG.domain.News;
import com.LRG.model.NewsDto;
import com.LRG.repository.NewsRepository;

@Service
public class NewsService {

	private NewsRepository newsRepository;
	
	public NewsService(NewsRepository newsRepository) {
		super();
		this.newsRepository = newsRepository;
	}
	
	public List<NewsDto> getNewsByType(String type){
		List<NewsDto> newsList = new ArrayList<NewsDto>();
		List<News> news = newsRepository.findAllByTypeOrderByPubDateDesc(type);
		if(news != null && !news.isEmpty()) {
			for(News n : news) {
				NewsDto newsDto = convertDomainToDto(n);
				newsList.add(newsDto);
			}
		}
		return newsList;	
	}
	
	private NewsDto convertDomainToDto(News news) {
		String pubDate = news.getPubDate();
//		try {
//			if(news.getPubDate()!=null) {
//				String date = news.getPubDate();
//				Date time = new SimpleDateFormat("HH:mm").parse(news.getPubDate().split(" ")[4]);
//				pubDate = date.split(" ")[0]+" "+date.split(" ")[1]+" "+date.split(" ")[2]+" "+date.split(" ")[3]+" " 
//						+ new SimpleDateFormat("hh.mm aa").format(time);
//			}
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}		
		return new NewsDto(news.getTitle(),news.getLink(),news.getThumbnailUrl(),pubDate,news.getGuid(),news.getType());	
	}
}
