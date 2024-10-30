package com.kkr.app.task;

import java.io.FileWriter;
import java.util.Date;

import com.kkr.app.service.IProcessMarketAnnouncementDataService;
import com.kkr.app.service.ITaskService;
import com.kkr.common.util.CommonUtils;
import com.kkr.common.util.Constants;

public class MarketAnnouncementDataProcessTask extends BaseTask{

public static FileWriter file_Writer=null;
	
	public void run(){
		
		ITaskService service = (ITaskService) getCtx().getBean("taskService");
		IProcessMarketAnnouncementDataService processMarketAnnouncementDataService =(IProcessMarketAnnouncementDataService) getCtx()
				.getBean("processMarketAnnouncementDataService");		
		try{
			file_Writer = CommonUtils.prepareOutputFile("MarketAnnouncementDataProcessTask");
			if(getTask().getRunType().equals(Constants.RUN_TYPE_AUTO)){
				System.out.println("Market Announcement Data Process Task Starting..."+new Date());
				file_Writer.write("Market Announcement Data Process Task Starting...");
				file_Writer.write(System.lineSeparator());
				
				boolean status = processMarketAnnouncementDataService.processMANData();
				processMarketAnnouncementDataService.insertMANDataIntoArchiveTable();				
				if(status) {
					System.out.println("Market Announcement Data Process Task Completed");
					file_Writer.write("Market Announcement Data Process Task Completed.");
					file_Writer.write(System.lineSeparator());
					service.markTaskComplete(getTask().getTaskId(), "SUCCESS");
				}else {
					file_Writer.write("Market Announcement Data Process Task Failed");
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
