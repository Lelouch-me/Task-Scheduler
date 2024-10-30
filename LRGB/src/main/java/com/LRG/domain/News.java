package com.LRG.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "news")
public class News {

	@Id
	@Column(name = "id", nullable = false)
	private int id;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "link")
	private String link;
	
	@Column(name = "thumbnail_url")
	private String thumbnailUrl;
	
	@Column(name = "pub_date")
	private String pubDate;
	
	@Column(name = "guid")
	private String guid;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "date_time")
	private String dateTime;
	
	public News() {
		super();
	}
	
	public News(String title, String link, String thumbnailUrl, String pubDate, String guid, String type, String dateTime) {
		this.title = title;
		this.link = link;
		this.thumbnailUrl = thumbnailUrl;
		this.pubDate = pubDate;
		this.guid = guid;
		this.type = type;
		this.dateTime = dateTime;
		
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	
	public String getPubDate() {
		return pubDate;
	}
	
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	
	public String getGuid() {
		return guid;
	}
	
	public void setGuid(String guid) {
		this.guid = guid;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	public String getDateTime() {
		return dateTime;
	}
	
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
}
