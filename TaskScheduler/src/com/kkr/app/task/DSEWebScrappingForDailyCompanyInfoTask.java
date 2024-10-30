package com.kkr.app.task;

import java.io.FileWriter;
import java.util.Date;

import com.kkr.app.service.IDSEWebScrappingForDailyCompanyInfoService;
import com.kkr.app.service.ITaskService;
import com.kkr.common.util.CommonUtils;
import com.kkr.common.util.Constants;

public class DSEWebScrappingForDailyCompanyInfoTask extends BaseTask{

	public static FileWriter file_Writer=null;
	
	public void run(){
		
		ITaskService service = (ITaskService) getCtx().getBean("taskService");
		IDSEWebScrappingForDailyCompanyInfoService dseWebScrappingForDailyCompanyInfoService =(IDSEWebScrappingForDailyCompanyInfoService) getCtx()
				.getBean("dseWebScrappingForDailyCompanyInfoService");
		
		try{
			file_Writer = CommonUtils.prepareOutputFile("DSEWebScrappingForDailyCompanyInfoTask");
			if(getTask().getRunType().equals(Constants.RUN_TYPE_AUTO)){
				System.out.println("DSE WebScrapping For DailyCompanyInfo Task Starting..."+new Date());
				file_Writer.write("DSE WebScrapping For DailyCompanyInfo Task Starting...");
				file_Writer.write(System.lineSeparator());
				dseWebScrappingForDailyCompanyInfoService.scrapDSESite();	
				System.out.println("DSE WebScrapping For DailyCompanyInfo Task Completed");
				file_Writer.write("DSE WebScrapping For DailyCompanyInfo Task Completed");
				file_Writer.write(System.lineSeparator());
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
