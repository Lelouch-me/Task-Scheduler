package com.kkr.app.task;

import java.io.FileWriter;

import com.kkr.app.service.IProcessForeignIndexDataService;
import com.kkr.app.service.ITaskService;
import com.kkr.common.util.CommonUtils;
import com.kkr.common.util.Constants;

public class ProcessForeignIndexDataTask extends BaseTask{

public static FileWriter file_Writer=null;
	
	public void run(){
		
		ITaskService service = (ITaskService) getCtx().getBean("taskService");
		IProcessForeignIndexDataService foreignIndexDataProcessService =(IProcessForeignIndexDataService) getCtx()
				.getBean("foreignIndexDataProcessService");
		
		try{
			file_Writer = CommonUtils.prepareOutputFile("ForeignIndexDataProcessTask");
			if(getTask().getRunType().equals(Constants.RUN_TYPE_AUTO)){
				file_Writer.write("Foreign Index Data Process Task Starting...");
				file_Writer.write(System.lineSeparator());
				boolean status =foreignIndexDataProcessService.processForeignIndexData();
				if(status) {
					file_Writer.write("Foreign Index Data Process Task Completed.");
					file_Writer.write(System.lineSeparator());
				    service.markTaskComplete(getTask().getTaskId(), "SUCCESS");
				}else {
					file_Writer.write("Foreign Index Data Process Task Failed.");
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
