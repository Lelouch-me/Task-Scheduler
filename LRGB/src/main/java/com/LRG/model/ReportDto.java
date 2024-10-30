package com.LRG.model;

public class ReportDto {
	
	private String publisher;
	
	private String discussionArea;
	
	private int year;
	
	private String link;
	
	public ReportDto() {
		super();
	}
	
	public ReportDto(String publisher, String discussionArea, int year, String link) {
		this.publisher = publisher;
		this.discussionArea = discussionArea;
		this.year = year;
		this.link = link;		
	}
	
	public String getPublisher() {
		return publisher;
	}
	
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	public String getDiscussionArea() {
		return discussionArea;
	}
	
	public void setDiscussionArea(String discussionArea) {
		this.discussionArea = discussionArea;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
}
