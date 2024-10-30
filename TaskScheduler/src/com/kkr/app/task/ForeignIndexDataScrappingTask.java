package com.kkr.app.task;

import java.io.FileWriter;

import com.kkr.app.service.IForeignIndexDataScrappingService;
import com.kkr.app.service.ITaskService;
import com.kkr.common.util.CommonUtils;
import com.kkr.common.util.Constants;

public class ForeignIndexDataScrappingTask extends BaseTask{

	public static FileWriter file_Writer=null;
	
	public void run(){
		
		ITaskService service = (ITaskService) getCtx().getBean("taskService");
		IForeignIndexDataScrappingService foreignIndexDataScrappingService =(IForeignIndexDataScrappingService) getCtx()
				.getBean("foreignIndexDataScrappingService");
		
		try{
			file_Writer = CommonUtils.prepareOutputFile("ForeignIndexDataScrappingTask");
			if(getTask().getRunType().equals(Constants.RUN_TYPE_AUTO)){
				file_Writer.write("Foreign Index Data Scrapping Task Starting...");
				file_Writer.write(System.lineSeparator());
				boolean status =foreignIndexDataScrappingService.populateForeignIndexData();
				if(status) {
					file_Writer.write("Foreign Index Data Scrapping Task Completed.");
					file_Writer.write(System.lineSeparator());
				    service.markTaskComplete(getTask().getTaskId(), "SUCCESS");
				}else {
					file_Writer.write("Foreign Index Data Scrapping Task Failed.");
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
