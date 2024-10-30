package com.LRG.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "report")
public class Report {

	@Id
	@Column(name = "id", nullable = false)
	private int id;
	
	@Column(name = "publisher")
	private String publisher;
	
	@Column(name = "discussion_area")
	private String discussionArea;
	
	@Column(name = "year")
	private int year;
	
	@Column(name = "link")
	private String link;
	
	public Report() {
		super();
	}
	
	public Report(String publisher, String discussionArea, int year, String link) {
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
