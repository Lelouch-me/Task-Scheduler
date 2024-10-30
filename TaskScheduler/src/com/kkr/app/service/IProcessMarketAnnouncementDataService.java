package com.kkr.app.service;

public interface IProcessMarketAnnouncementDataService {

	public boolean processMANData();
	
	public void insertMANDataIntoArchiveTable();
}
