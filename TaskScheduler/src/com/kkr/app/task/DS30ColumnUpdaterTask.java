package com.kkr.app.task;

import java.io.FileWriter;
import java.util.Date;

import com.kkr.app.service.IDS30ColumnUpdaterService;
import com.kkr.app.service.ITaskService;
import com.kkr.common.util.CommonUtils;
import com.kkr.common.util.Constants;

public class DS30ColumnUpdaterTask extends BaseTask{

public static FileWriter file_Writer=null;
	
	public void run(){
		
		ITaskService service = (ITaskService) getCtx().getBean("taskService");
		IDS30ColumnUpdaterService ds30ColumnUpdaterService =(IDS30ColumnUpdaterService) getCtx()
				.getBean("ds30ColumnUpdaterService");		
		try{
			file_Writer = CommonUtils.prepareOutputFile("DS30ColumnUpdaterTask");
			if(getTask().getRunType().equals(Constants.RUN_TYPE_AUTO)){
				file_Writer.write("DS30 Column Updater Task Starting...");
				file_Writer.write(System.lineSeparator());
				System.out.println("DS30 Column Updater Task Starting..."+new Date());
				boolean status = ds30ColumnUpdaterService.updateDS30Column();
				if(status) {
					System.out.println("DS30 Column Updater Task Completed.");
					file_Writer.write("DS30 Column Updater Task Completed");
					file_Writer.write(System.lineSeparator());
					service.markTaskComplete(getTask().getTaskId(), "SUCCESS");
				}else {
					file_Writer.write("DS30 Column Updater Task Failed");
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
