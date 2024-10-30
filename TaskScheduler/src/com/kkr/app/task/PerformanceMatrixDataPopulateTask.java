package com.kkr.app.task;

import java.io.FileWriter;
import java.util.Date;

import com.kkr.app.service.IPerformanceMatrixCalculationService;
import com.kkr.app.service.ITaskService;
import com.kkr.common.util.CommonUtils;
import com.kkr.common.util.Constants;

public class PerformanceMatrixDataPopulateTask extends BaseTask{

	public static FileWriter file_Writer=null;
	
	public void run(){
		
		ITaskService service = (ITaskService) getCtx().getBean("taskService");
		IPerformanceMatrixCalculationService performanceMatrixCalculationService =(IPerformanceMatrixCalculationService) getCtx()
				.getBean("performanceMatrixCalculationService");
		
		try{
			file_Writer = CommonUtils.prepareOutputFile("PerformanceMatrixDataPopulateTask");
			if(getTask().getRunType().equals(Constants.RUN_TYPE_AUTO)){
				System.out.println("Performance Matrix Calculation Task Starting..."+new Date());
				file_Writer.write("Performance Matrix Calculation Task Starting...");
				file_Writer.write(System.lineSeparator());
				
				file_Writer.write("Inserting Performance Matrix Data...");
				file_Writer.write(System.lineSeparator());
				performanceMatrixCalculationService.insertPerformanceMatrixData();	
				file_Writer.write("Insertion of Performance Matrix Data Completed.");
				file_Writer.write(System.lineSeparator());
				
				file_Writer.write("Inserting Daily Price Volume Data...");
				file_Writer.write(System.lineSeparator());
				performanceMatrixCalculationService.insertDailyPriceVolumeData();
				file_Writer.write("Insertion of Daily Price Volume Data Completed.");
				file_Writer.write(System.lineSeparator());
				performanceMatrixCalculationService.populateReturnValueInAdjustedPrice();
				performanceMatrixCalculationService.treasuryBondScrapping();
				performanceMatrixCalculationService.otherScraping();
				file_Writer.write("Performance Matrix Calculation Task Completed");
				file_Writer.write(System.lineSeparator());
				System.out.println("Performance Matrix Calculation Task Completed");
			    service.markTaskComplete(getTask().getTaskId(), "SUCCESS");
			}
			file_Writer.close();
		}catch(Exception e){
			try {
				service.markTaskComplete(getTask().getTaskId(), "ERROR");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
}
