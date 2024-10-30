package com.LRG.model;

public class NewsDto {

	private String title;

	private String link;

	private String thumbnailUrl;

	private String pubDate;

	private String guid;

	private String type;
	
	public NewsDto() {
		super();
	}
	
	public NewsDto(String title, String link, String thumbnailUrl, String pubDate, String guid, String type) {
		this.title = title;
		this.link = link;
		this.thumbnailUrl = thumbnailUrl;
		this.pubDate = pubDate;
		this.guid = guid;
		this.type = type;		
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
}
