package com.kkr.app.task;

import java.io.FileWriter;
import java.util.Date;

import com.kkr.app.service.ITaskService;
import com.kkr.app.service.IWeeklyMFNAVDataPopulateService;
import com.kkr.common.util.CommonUtils;
import com.kkr.common.util.Constants;

public class WeeklyMFNAVDataPopulateTask extends BaseTask{

public static FileWriter file_Writer=null;
	
	public void run(){
		
		ITaskService service = (ITaskService) getCtx().getBean("taskService");
		IWeeklyMFNAVDataPopulateService weeklyMFNAVDataPopulateService =(IWeeklyMFNAVDataPopulateService) getCtx()
				.getBean("weeklyMFNAVDataPopulateService");
		
		try{
			file_Writer = CommonUtils.prepareOutputFile("WeeklyMFNAVProcessTask");
			if(getTask().getRunType().equals(Constants.RUN_TYPE_AUTO)){
				System.out.println("Weekly MF NAV ProcessTask Starting..."+new Date());
				file_Writer.write("Weekly MF NAV Process Task Starting...");
				file_Writer.write(System.lineSeparator());
				
				boolean status = weeklyMFNAVDataPopulateService.populateNavForMutualFund();			
				if(status) {
					System.out.println("Weekly MF NAV Process Task Completed");
					file_Writer.write("Weekly MF NAV Process Task Completed.");
					file_Writer.write(System.lineSeparator());
					service.markTaskComplete(getTask().getTaskId(), "SUCCESS");
				}else {
					file_Writer.write("Weekly MF NAV Process Task Failed");
					file_Writer.write(System.lineSeparator());
					service.markTaskComplete(getTask().getTaskId(), "ERROR");
				}
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
