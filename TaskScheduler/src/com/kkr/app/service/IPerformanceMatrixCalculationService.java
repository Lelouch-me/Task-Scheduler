package com.kkr.app.service;

import java.io.IOException;
import java.sql.SQLException;

public interface IPerformanceMatrixCalculationService {

	public void insertDailyPriceVolumeData();
	
	public void insertPerformanceMatrixData();
	
	public void populateReturnValueInAdjustedPrice();
	
	public void treasuryBondScrapping() throws ClassNotFoundException, Exception;
	
	public void otherScraping() throws ClassNotFoundException, SQLException, IOException;
}
