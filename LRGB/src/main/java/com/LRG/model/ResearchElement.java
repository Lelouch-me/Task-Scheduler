package com.LRG.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
public class ResearchElement {
	
	private String text;
	private String company;
	private String vs3Ytrend;
	private String industry;
	private String vsIndustry;
	private String latest;
	private String last;
	private String fund;
	private String peers;
	private String vsPeers;
	private String benchMark;
	private String vsBenchmark;
	private String value;
	
	public ResearchElement(){
		
	}
	
	public ResearchElement(String text, String comp,String vs3ytrend,String industry,String vsIndustry){
		this.text=text;
		this.company=comp;
		this.vs3Ytrend=vs3ytrend;
		this.industry=industry;
		this.vsIndustry=vsIndustry;		
	}
	
	public ResearchElement(String text, String latest,String last){
		this.text=text;
		this.latest=latest;
		this.last=last;		
	}
	
	public ResearchElement(String text, String fund,String peers,String vsPeers,String benchMark,String vsBenchmark){
		this.text=text;
		this.fund=fund;
		this.peers=peers;
		this.vsPeers=vsPeers;
		this.benchMark=benchMark;
		this.vsBenchmark=vsBenchmark;
	}
	
	public ResearchElement(String text, String value){
		this.text=text;
		this.value=value;		
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getVs3Ytrend() {
		return vs3Ytrend;
	}

	public void setVs3Ytrend(String vs3Ytrend) {
		this.vs3Ytrend = vs3Ytrend;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getVsIndustry() {
		return vsIndustry;
	}

	public void setVsIndustry(String vsIndustry) {
		this.vsIndustry = vsIndustry;
	}

	public String getLatest() {
		return latest;
	}

	public void setLatest(String latest) {
		this.latest = latest;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public String getFund() {
		return fund;
	}

	public void setFund(String fund) {
		this.fund = fund;
	}

	public String getPeers() {
		return peers;
	}

	public void setPeers(String peers) {
		this.peers = peers;
	}

	public String getVsPeers() {
		return vsPeers;
	}

	public void setVsPeers(String vsPeers) {
		this.vsPeers = vsPeers;
	}

	public String getBenchMark() {
		return benchMark;
	}

	public void setBenchMark(String benchMark) {
		this.benchMark = benchMark;
	}

	public String getvsBenchmark() {
		return vsBenchmark;
	}

	public void setvsBenchmark(String vsBenchmark) {
		this.vsBenchmark = vsBenchmark;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
